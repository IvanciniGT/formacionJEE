import { ResultadoBusqueda } from '../dominio/resultado-busqueda';
import { RespuestaPalabraDto } from './respuesta-palabra.dto';

/**
 * MAPEADOR: función pura que traduce el DTO (formato de cable) al modelo de
 * dominio que consumen los componentes.
 *
 * Aquí se absorbe toda la "suciedad" del transporte:
 *  - El servidor usa el CÓDIGO DE ESTADO HTTP (200/404) para indicar si la
 *    palabra existe, no el cuerpo. Por eso recibimos `httpStatus` en el contexto.
 *  - Las listas pueden llegar a `null`; las normalizamos a listas vacías.
 *  - Cuando el JSON viene parcial (p. ej. sólo `idioma`), completamos con el
 *    contexto de la petición.
 *
 * Si el servidor cambiara su JSON, SÓLO habría que tocar este archivo.
 */
export function aResultadoBusqueda(
  dto: RespuestaPalabraDto,
  contexto: { idioma: string; palabra: string; httpStatus: number },
): ResultadoBusqueda {
  return {
    palabra: dto.palabra ?? contexto.palabra,
    idioma: dto.idioma ?? contexto.idioma,
    encontrada: contexto.httpStatus === 200,
    significados: dto.significados ?? [],
    sugerencias: dto.similares ?? [],
  };
}
