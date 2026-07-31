/**
 * Representa un idioma disponible en el servidor, ya enriquecido con un
 * nombre legible y una bandera para mostrarlo en la interfaz.
 */
export interface Idioma {
  /** Código tal cual lo entrega el servidor: "ES", "EN", "ELFICO", "ES.GRANDE". */
  readonly codigo: string;
  /** Nombre legible para el usuario: "Español", "Inglés"... */
  readonly nombre: string;
  /** Emoji de bandera (o icono de reserva para idiomas sin país). */
  readonly bandera: string;
}
