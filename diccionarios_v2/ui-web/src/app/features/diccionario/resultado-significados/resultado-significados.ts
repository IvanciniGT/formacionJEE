import { ChangeDetectionStrategy, Component, input } from '@angular/core';

/**
 * Componente PRESENTACIONAL ("tonto"): sólo recibe datos y los muestra.
 * No sabe que existe una API ni cómo se obtuvieron los significados.
 * Muestra una palabra encontrada y su lista de significados.
 */
@Component({
  selector: 'app-resultado-significados',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './resultado-significados.html',
  styleUrl: './resultado-significados.css',
})
export class ResultadoSignificados {
  readonly palabra = input.required<string>();
  readonly significados = input.required<string[]>();
}
