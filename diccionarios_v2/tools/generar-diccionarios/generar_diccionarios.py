#!/usr/bin/env python3
"""Genera un diccionario en el formato del proyecto (palabra=sig1|sig2) a partir
de un extracto JSONL de Wiktionary (kaikki.org), quedandose solo con las palabras
mas frecuentes de una lista de frecuencias.

El extracto JSONL se lee por la entrada estandar (stdin) para poder hacer streaming
sin almacenar el fichero crudo (que ocupa >1 GB descomprimido). Ejemplo:

    curl --compressed -s "<URL_KAIKKI_JSONL>" \
      | python3 generar_diccionarios.py \
          --freq es_50k.txt --out es.grande.txt --max 20000

Formato de salida (una palabra por linea, UTF-8, sin lineas vacias ni comentarios):

    palabra=significado 1|significado 2|significado 3

Restricciones impuestas por el loader del backend
(SuministradorDeDiccionariosEnFicheros): la linea se parte por '=' y por '|', y una
linea vacia o sin '=' rompe la carga. Por eso este generador nunca emite '=' ni '|'
dentro de una glosa, ni lineas vacias, ni palabras sin al menos una glosa valida.
"""
from __future__ import annotations

import argparse
import json
import re
import sys

# Palabra valida como clave: letras (con tildes y ñ), permitiendo guion/apostrofo
# internos. Deja fuera numeros, espacios, siglas raras, etc.
RE_PALABRA = re.compile(r"^[a-záéíóúüñ][a-záéíóúüñ'’\-]*$")

# Partes de la oracion que NO aportan a un diccionario de consulta habitual.
POS_DESCARTADAS = {
    "name",       # nombres propios
    "character",
    "symbol",
    "prefix",
    "suffix",
    "infix",
    "syllable",
    "punct",
}

# Espacios en blanco (incluye tabs y saltos) para colapsar.
RE_ESPACIOS = re.compile(r"\s+")

# Sub/superindices Unicode -> digitos normales, para que "H₂O" quede "H2O" (y no
# "HO") y las desambiguaciones tipo "libro₂" queden como "libro2".
TABLA_INDICES = str.maketrans("₀₁₂₃₄₅₆₇₈₉⁰¹²³⁴⁵⁶⁷⁸⁹", "01234567890123456789")

# Puntuacion repetida (p. ej. ".." al unir una glosa con su punto final).
RE_PUNTUACION_REPETIDA = re.compile(r"([.,;:!?])\1+")

# Glosas que NO son definiciones de verdad, sino remisiones (abreviatura de,
# forma de, variante de...). Se conservan, pero se dejan al final.
RE_SECUNDARIA = re.compile(
    r"^\s*("
    r"initialism|abbreviation|acronym|alternative (form|spelling)|"
    r"obsolete (form|spelling)|misspelling|eye dialect|inflection|"
    r"forma (del|verbal|femenina|masculina|plural)|grafía|variante de|"
    r"acrónimo|sigla|abreviatura|apócope|plural de|femenino de"
    r")\b",
    re.IGNORECASE,
)


def es_secundaria(glosa: str) -> bool:
    """True si la glosa es una remision (no una definicion propia)."""
    return bool(RE_SECUNDARIA.match(glosa))


def cargar_frecuencias(ruta: str, limite_candidatas: int) -> tuple[dict[str, int], set[str]]:
    """Lee una lista de frecuencias 'palabra recuento' y devuelve el ranking
    (palabra -> posicion) y el conjunto de palabras candidatas, en orden de
    frecuencia y ya filtradas/deduplicadas."""
    ranking: dict[str, int] = {}
    with open(ruta, "r", encoding="utf-8") as f:
        for linea in f:
            token = linea.split(" ", 1)[0].strip().lower()
            if not token or token in ranking:
                continue
            if not RE_PALABRA.match(token):
                continue
            ranking[token] = len(ranking)
            if len(ranking) >= limite_candidatas:
                break
    return ranking, set(ranking.keys())


def limpiar_glosa(texto: str, largo_max: int) -> str | None:
    """Normaliza una glosa para que sea segura y legible en nuestro formato."""
    if not texto:
        return None
    # Quita '=' y '|' porque son separadores del formato del fichero.
    texto = texto.replace("=", "-").replace("|", ",")
    texto = texto.translate(TABLA_INDICES)
    texto = RE_ESPACIOS.sub(" ", texto).strip()
    texto = RE_PUNTUACION_REPETIDA.sub(r"\1", texto)
    # Descarta glosas vacias o demasiado cortas (ruido).
    if len(texto) < 3:
        return None
    if len(texto) > largo_max:
        recorte = texto[:largo_max].rsplit(" ", 1)[0].rstrip(",;:. ")
        texto = (recorte or texto[:largo_max]) + "…"
    return texto


def extraer_glosa(sentido: dict) -> str | None:
    """Obtiene la definicion mas especifica de un 'sense' de kaikki."""
    glosas = sentido.get("glosses") or sentido.get("raw_glosses")
    if not glosas:
        return None
    if isinstance(glosas, list):
        return glosas[-1] if glosas else None
    return glosas


# Cuantas glosas guardar como maximo por palabra antes de reordenar/recortar.
# Recogemos de sobra para poder priorizar definiciones reales frente a remisiones.
COLECTA_MAX = 8


def procesar_stream(entrada, candidatas: set[str], largo_max: int) -> dict[str, list[str]]:
    """Recorre el JSONL (una entrada por linea) y acumula glosas por palabra."""
    resultado: dict[str, list[str]] = {}
    for linea in entrada:
        linea = linea.strip()
        if not linea:
            continue
        try:
            entrada_json = json.loads(linea)
        except json.JSONDecodeError:
            continue
        palabra = entrada_json.get("word")
        if not palabra:
            continue
        clave = palabra.lower()
        if clave not in candidatas:
            continue
        if entrada_json.get("pos") in POS_DESCARTADAS:
            continue
        acumuladas = resultado.setdefault(clave, [])
        if len(acumuladas) >= COLECTA_MAX:
            continue
        for sentido in entrada_json.get("senses", []):
            glosa = extraer_glosa(sentido)
            glosa = limpiar_glosa(glosa, largo_max) if glosa else None
            if glosa and glosa not in acumuladas:
                acumuladas.append(glosa)
                if len(acumuladas) >= COLECTA_MAX:
                    break
    return resultado


def priorizar(glosas: list[str], max_glosas: int) -> list[str]:
    """Pone las definiciones reales antes que las remisiones y recorta a max_glosas,
    conservando el orden original dentro de cada grupo (orden estable)."""
    reales = [g for g in glosas if not es_secundaria(g)]
    remisiones = [g for g in glosas if es_secundaria(g)]
    return (reales + remisiones)[:max_glosas]


def escribir_salida(ruta: str, ranking: dict[str, int], glosas_por_palabra: dict[str, list[str]], maximo: int, max_glosas: int) -> int:
    """Emite las palabras con glosas en orden de frecuencia."""
    palabras_ordenadas = sorted(glosas_por_palabra.keys(), key=lambda p: ranking[p])
    escritas = 0
    with open(ruta, "w", encoding="utf-8", newline="\n") as f:
        for palabra in palabras_ordenadas:
            glosas = priorizar(glosas_por_palabra[palabra], max_glosas)
            if not glosas:
                continue
            f.write(f"{palabra}={'|'.join(glosas)}\n")
            escritas += 1
            if escritas >= maximo:
                break
    return escritas


def main() -> int:
    parser = argparse.ArgumentParser(description="Genera diccionarios del proyecto desde Wiktionary (kaikki.org).")
    parser.add_argument("--freq", required=True, help="Fichero de frecuencias 'palabra recuento'.")
    parser.add_argument("--out", required=True, help="Fichero de salida en formato palabra=sig1|sig2.")
    parser.add_argument("--max", type=int, default=20000, help="Maximo de palabras a emitir.")
    parser.add_argument("--candidatas", type=int, default=60000, help="Cuantas palabras frecuentes considerar como candidatas.")
    parser.add_argument("--max-glosas", type=int, default=3, help="Maximo de significados por palabra.")
    parser.add_argument("--largo-glosa", type=int, default=200, help="Longitud maxima de cada significado.")
    args = parser.parse_args()

    ranking, candidatas = cargar_frecuencias(args.freq, args.candidatas)
    print(f"Candidatas por frecuencia: {len(candidatas)}", file=sys.stderr)

    glosas_por_palabra = procesar_stream(sys.stdin, candidatas, args.largo_glosa)
    print(f"Palabras con definicion encontrada: {len(glosas_por_palabra)}", file=sys.stderr)

    escritas = escribir_salida(args.out, ranking, glosas_por_palabra, args.max, args.max_glosas)
    print(f"Palabras escritas en {args.out}: {escritas}", file=sys.stderr)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
