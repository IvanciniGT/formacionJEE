import { Idioma } from './idioma';

/**
 * Traduce un código de idioma del servidor a un nombre legible y una bandera.
 *
 * La comparación es insensible a mayúsculas porque el servidor entrega los
 * códigos en mayúsculas ("ES", "EN"...). Cualquier idioma desconocido recibe
 * un icono de reserva, de modo que añadir idiomas nuevos en el servidor no
 * rompe la interfaz.
 */
const CATALOGO: Record<string, { nombre: string; bandera: string }> = {
  ES: { nombre: 'Español', bandera: '🇪🇸' },
  'ES.GRANDE': { nombre: 'Español (ampliado)', bandera: '🇪🇸' },
  EN: { nombre: 'Inglés', bandera: '🇬🇧' },
  ELFICO: { nombre: 'Élfico', bandera: '📖' },
};

const BANDERA_DE_RESERVA = '📖';

export function describeIdioma(codigo: string): Idioma {
  const entrada = CATALOGO[codigo.toUpperCase()];
  if (entrada) {
    return { codigo, nombre: entrada.nombre, bandera: entrada.bandera };
  }
  return { codigo, nombre: capitalizar(codigo), bandera: BANDERA_DE_RESERVA };
}

function capitalizar(texto: string): string {
  if (!texto) return texto;
  return texto.charAt(0).toUpperCase() + texto.slice(1).toLowerCase();
}
