import { Observable } from 'rxjs';
import { ResultadoBusqueda } from './resultado-busqueda';

/**
 * CONTRATO (abstracción) del que depende toda la aplicación.
 *
 * Es el equivalente en el frontend del módulo `diccionarios-api` del servidor:
 * define QUÉ se puede hacer, sin decir CÓMO. Los componentes dependen de esta
 * clase abstracta, nunca de una implementación concreta (principio DIP).
 *
 * Angular la usa como token de inyección; en `app.config.ts` se decide qué
 * implementación concreta se entrega (HTTP hoy, un mock mañana), igual que se
 * elige una implementación cambiando una dependencia en un `pom.xml`.
 */
export abstract class SuministradorDeDiccionarios {
  /** Lista de códigos de idioma disponibles en el servidor. */
  abstract dameIdiomas(): Observable<string[]>;

  /** Busca una palabra en un idioma y devuelve siempre un resultado de dominio. */
  abstract buscarPalabra(idioma: string, palabra: string): Observable<ResultadoBusqueda>;
}
