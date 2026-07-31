# Generador de diccionarios

Genera los ficheros de diccionario del proyecto (`palabra=sig1|sig2`) a partir de
extractos abiertos de **Wiktionary/Wikcionario** publicados por
[kaikki.org](https://kaikki.org) (proyecto *wiktextract*), quedándose solo con las
palabras **más frecuentes** de una lista de frecuencias.

El extracto JSONL se procesa por *streaming* (entrada estándar) para no almacenar el
fichero crudo, que ocupa más de 1 GB descomprimido.

## Requisitos

- Python 3.9+
- `curl`
- Acceso a Internet

## Uso

```bash
WORK="$(mktemp -d)"

# 1) Listas de frecuencia (OpenSubtitles, via hermitdave/FrequencyWords)
curl --compressed -s -o "$WORK/es_50k.txt" \
  "https://raw.githubusercontent.com/hermitdave/FrequencyWords/master/content/2018/es/es_50k.txt"
curl --compressed -s -o "$WORK/en_50k.txt" \
  "https://raw.githubusercontent.com/hermitdave/FrequencyWords/master/content/2018/en/en_50k.txt"

# 2) Español (Wikcionario, definiciones en español)
curl --compressed -s "https://kaikki.org/eswiktionary/Espa%C3%B1ol/kaikki.org-dictionary-Espa%C3%B1ol.jsonl" \
  | python3 generar_diccionarios.py --freq "$WORK/es_50k.txt" \
      --out ../../diccionario-es/src/main/resources/diccionarios/es.grande.txt --max 20000

# 3) Inglés (Wiktionary, definiciones en inglés)
curl --compressed -s "https://kaikki.org/dictionary/English/kaikki.org-dictionary-English.jsonl" \
  | python3 generar_diccionarios.py --freq "$WORK/en_50k.txt" \
      --out ../../diccionario-en/src/main/resources/diccionarios/en.txt --max 20000
```

Opciones (`--help` para la lista completa): `--max` (palabras a emitir),
`--candidatas` (cuántas frecuentes considerar), `--max-glosas`, `--largo-glosa`.

## Formato de salida

Una palabra por línea, UTF-8, sin líneas vacías ni comentarios:

```
palabra=significado 1|significado 2|significado 3
```

Esto respeta el *loader* del backend (`SuministradorDeDiccionariosEnFicheros`), que
parte cada línea por `=` y por `|`. El generador nunca emite `=` ni `|` dentro de una
glosa, ni líneas vacías, ni palabras sin al menos una definición válida.

## Fuentes y licencias (atribución)

- **Definiciones**: [Wiktionary](https://en.wiktionary.org/) y
  [Wikcionario](https://es.wiktionary.org/), extraídos con
  [wiktextract](https://github.com/tatuylonen/wiktextract) y distribuidos por
  [kaikki.org](https://kaikki.org). Contenido bajo **CC BY-SA** y **GFDL**.
- **Frecuencias**: [hermitdave/FrequencyWords](https://github.com/hermitdave/FrequencyWords)
  (a partir de OpenSubtitles), bajo **MIT**.

Al reutilizar estas definiciones se debe mantener la atribución a Wiktionary/Wikcionario
y conservar la licencia CC BY-SA. Véase también el fichero `NOTICE.txt` junto a cada
diccionario generado.
