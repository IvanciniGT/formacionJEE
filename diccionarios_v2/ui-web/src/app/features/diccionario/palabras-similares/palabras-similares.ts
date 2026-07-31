import { ChangeDetectionStrategy, Component, input, output } from '@angular/core';

/**
 * Componente PRESENTACIONAL ("tonto"): muestra las palabras similares cuando
 * la búsqueda no ha encontrado la palabra exacta. Cada sugerencia es clicable
 * y emite un evento; no decide nada, sólo avisa de la selección.
 */
@Component({
  selector: 'app-palabras-similares',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './palabras-similares.html',
  styleUrl: './palabras-similares.css',
})
export class PalabrasSimilares {
  readonly palabra = input.required<string>();
  readonly sugerencias = input.required<string[]>();
  readonly seleccion = output<string>();
}
