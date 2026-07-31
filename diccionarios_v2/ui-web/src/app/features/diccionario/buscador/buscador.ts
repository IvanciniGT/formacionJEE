import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

import { SuministradorDeDiccionarios } from '../../../core/dominio/suministrador-de-diccionarios';
import { ResultadoBusqueda } from '../../../core/dominio/resultado-busqueda';
import { ErrorDeServicio } from '../../../core/dominio/error-de-servicio';
import { Idioma } from '../../../core/dominio/idioma';
import { describeIdioma } from '../../../core/dominio/catalogo-de-idiomas';
import { ResultadoSignificados } from '../resultado-significados/resultado-significados';
import { PalabrasSimilares } from '../palabras-similares/palabras-similares';

type EstadoBusqueda = 'inicial' | 'cargando' | 'resultado' | 'error';

/**
 * Componente CONTENEDOR ("inteligente"): orquesta la búsqueda.
 *
 * Depende del CONTRATO abstracto `SuministradorDeDiccionarios`, nunca de la
 * implementación HTTP concreta (DIP). El estado de la búsqueda vive en la URL:
 * el formulario navega a `/:idioma/:palabra` y es el cambio de ruta quien
 * dispara la búsqueda real, de modo que las URLs son compartibles y recargables.
 */
@Component({
  selector: 'app-buscador',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [FormsModule, ResultadoSignificados, PalabrasSimilares],
  templateUrl: './buscador.html',
  styleUrl: './buscador.css',
})
export class Buscador {
  private readonly suministrador = inject(SuministradorDeDiccionarios);
  private readonly ruta = inject(ActivatedRoute);
  private readonly router = inject(Router);

  readonly idiomas = signal<Idioma[]>([]);
  readonly idiomaSeleccionado = signal<string>('');
  readonly termino = signal<string>('');

  readonly estado = signal<EstadoBusqueda>('inicial');
  readonly resultado = signal<ResultadoBusqueda | null>(null);
  readonly mensajeError = signal<string>('');

  constructor() {
    this.cargarIdiomas();

    // La ruta es la fuente de verdad de la búsqueda.
    this.ruta.paramMap.pipe(takeUntilDestroyed()).subscribe((parametros) => {
      const idioma = parametros.get('idioma');
      const palabra = parametros.get('palabra');
      if (idioma && palabra) {
        this.idiomaSeleccionado.set(this.normalizarIdioma(idioma));
        this.termino.set(palabra);
        this.ejecutarBusqueda(idioma, palabra);
      }
    });
  }

  /** Manejador del formulario: navega; la navegación dispara la búsqueda. */
  buscar(): void {
    const idioma = this.idiomaSeleccionado();
    const palabra = this.termino().trim();
    if (!idioma || !palabra) {
      return;
    }
    this.router.navigate([idioma, palabra]);
  }

  /** Al pulsar una sugerencia, se lanza una nueva búsqueda con esa palabra. */
  seleccionarSugerencia(sugerencia: string): void {
    this.termino.set(sugerencia);
    this.router.navigate([this.idiomaSeleccionado(), sugerencia]);
  }

  private cargarIdiomas(): void {
    this.suministrador.dameIdiomas().subscribe({
      next: (codigos) => {
        this.idiomas.set(codigos.map(describeIdioma));
        const actual = this.idiomaSeleccionado();
        if (actual) {
          // Reconcilia un idioma llegado por URL (p. ej. "es") con el código
          // canónico del servidor (p. ej. "ES"), para que el desplegable case.
          this.idiomaSeleccionado.set(this.normalizarIdioma(actual));
        } else if (codigos.length > 0) {
          this.idiomaSeleccionado.set(codigos[0]);
        }
      },
      error: () => {
        this.estado.set('error');
        this.mensajeError.set('No se pudo cargar la lista de idiomas del servidor.');
      },
    });
  }

  /** Devuelve el código canónico del servidor que coincide (sin distinguir mayúsculas). */
  private normalizarIdioma(idioma: string): string {
    const encontrado = this.idiomas().find(
      (i) => i.codigo.toLowerCase() === idioma.toLowerCase(),
    );
    return encontrado ? encontrado.codigo : idioma;
  }

  private ejecutarBusqueda(idioma: string, palabra: string): void {
    this.estado.set('cargando');
    this.suministrador.buscarPalabra(idioma, palabra).subscribe({
      next: (resultado) => {
        this.resultado.set(resultado);
        this.estado.set('resultado');
      },
      error: (error: unknown) => {
        this.mensajeError.set(
          error instanceof ErrorDeServicio
            ? error.message
            : 'Ha ocurrido un error inesperado.',
        );
        this.estado.set('error');
      },
    });
  }
}
