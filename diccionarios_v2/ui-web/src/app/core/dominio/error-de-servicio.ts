/**
 * Error de DOMINIO que representa un fallo real del servicio de diccionarios
 * (servidor caído, error 500, red no disponible...).
 *
 * Es importante distinguirlo de un 404 legítimo: que una palabra no exista
 * NO es un error, es un resultado de dominio válido. Solo los fallos reales
 * de infraestructura se traducen a este error.
 */
export class ErrorDeServicio extends Error {
  constructor(mensaje: string) {
    super(mensaje);
    this.name = 'ErrorDeServicio';
  }
}
