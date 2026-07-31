import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map, shareReplay } from 'rxjs/operators';

import { SuministradorDeDiccionarios } from '../dominio/suministrador-de-diccionarios';
import { ResultadoBusqueda } from '../dominio/resultado-busqueda';
import { ErrorDeServicio } from '../dominio/error-de-servicio';
import { RespuestaPalabraDto } from './respuesta-palabra.dto';
import { aResultadoBusqueda } from './respuesta-palabra.mapper';
import { environment } from '../../../environments/environment';

/**
 * ADAPTER: implementación del contrato `SuministradorDeDiccionarios` que habla
 * con el servicio web REST por HTTP. Es el equivalente del módulo Java
 * `diccionarios-en-servicio-web` (patrón Adapter / Business Delegate).
 *
 * Es el ÚNICO que conoce `HttpClient`, las URLs y el DTO. Sus métodos devuelven
 * siempre modelo de dominio: el DTO no se filtra hacia el resto de la app.
 */
@Injectable()
export class SuministradorDeDiccionariosHttp extends SuministradorDeDiccionarios {
  private readonly http = inject(HttpClient);
  private readonly base = environment.apiBaseUrl;

  /** Cacheado: los idiomas no cambian durante la sesión. */
  private idiomas$?: Observable<string[]>;

  override dameIdiomas(): Observable<string[]> {
    if (!this.idiomas$) {
      this.idiomas$ = this.http.get<string[]>(`${this.base}/diccionarios`).pipe(
        catchError(() =>
          throwError(() => new ErrorDeServicio('No se pudo obtener la lista de idiomas.')),
        ),
        shareReplay(1),
      );
    }
    return this.idiomas$;
  }

  override buscarPalabra(idioma: string, palabra: string): Observable<ResultadoBusqueda> {
    const url =
      `${this.base}/diccionarios/${encodeURIComponent(idioma)}/${encodeURIComponent(palabra)}`;

    return this.http.get<RespuestaPalabraDto>(url, { observe: 'response' }).pipe(
      map((respuesta) =>
        aResultadoBusqueda(respuesta.body ?? {}, {
          idioma,
          palabra,
          httpStatus: respuesta.status,
        }),
      ),
      catchError((error: HttpErrorResponse) => {
        // Un 404 es un resultado de dominio VÁLIDO: palabra no encontrada
        // (posiblemente con sugerencias en el cuerpo del error).
        if (error.status === 404) {
          return of(
            aResultadoBusqueda(error.error ?? {}, { idioma, palabra, httpStatus: 404 }),
          );
        }
        // Cualquier otro fallo (500, red caída...) es un error real de servicio
        // y NO debe disfrazarse de "palabra no encontrada".
        return throwError(
          () => new ErrorDeServicio('El servicio de diccionarios no está disponible.'),
        );
      }),
    );
  }
}
