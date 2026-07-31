/**
 * DTO (Data Transfer Object) — FORMATO DE CABLE.
 *
 * Refleja EXACTAMENTE el JSON que emite el servicio web `servicio-web`
 * (clase Java `RespuestaPalabra`), con todas sus particularidades: campos
 * que pueden faltar y listas que pueden llegar a `null`.
 *
 * Es un detalle privado de la capa de infraestructura: NUNCA cruza la
 * frontera hacia los componentes. Para eso está el mapeador, que lo
 * convierte en el modelo de dominio `ResultadoBusqueda`.
 */
export interface RespuestaPalabraDto {
  palabra?: string;
  idioma?: string;
  significados?: string[] | null;
  similares?: string[] | null;
}
