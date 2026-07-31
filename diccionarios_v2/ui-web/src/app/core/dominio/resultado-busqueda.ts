/**
 * Modelo de DOMINIO que consumen los componentes.
 *
 * Es deliberadamente independiente del JSON que devuelve el servicio web
 * (ese formato de cable vive en la capa de infraestructura, en el DTO).
 * Aquí no hay campos nulos ni rarezas del transporte: expresa intención.
 */
export interface ResultadoBusqueda {
  readonly palabra: string;
  readonly idioma: string;
  readonly encontrada: boolean;
  readonly significados: string[];
  readonly sugerencias: string[];
}
