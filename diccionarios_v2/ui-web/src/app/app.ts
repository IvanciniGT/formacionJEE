import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { RouterOutlet, RouterLink } from '@angular/router';
import { TemaService } from './core/tema.service';

@Component({
  selector: 'app-root',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterOutlet, RouterLink],
  template: `
    <div class="marco">
      <header class="cabecera">
        <a class="titulo" routerLink="/">
          <span class="logo" aria-hidden="true">📚</span>
          Diccionarios
        </a>

        <button
          type="button"
          class="boton-tema"
          (click)="temaService.alternar()"
          [attr.aria-label]="
            temaService.tema() === 'oscuro' ? 'Cambiar a tema claro' : 'Cambiar a tema oscuro'
          "
        >
          {{ temaService.tema() === 'oscuro' ? '☀️' : '🌙' }}
        </button>
      </header>

      <main class="contenido">
        <router-outlet />
      </main>
    </div>
  `,
  styles: [
    `
      .marco {
        max-width: 640px;
        margin: 0 auto;
        padding: 1.5rem 1.25rem 3rem;
      }

      .cabecera {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 2rem;
      }

      .titulo {
        display: inline-flex;
        align-items: center;
        gap: 0.5rem;
        font-size: 1.5rem;
        font-weight: 700;
        color: var(--color-texto);
        text-decoration: none;
      }

      .logo {
        font-size: 1.6rem;
      }

      .boton-tema {
        width: 2.5rem;
        height: 2.5rem;
        display: grid;
        place-items: center;
        font-size: 1.15rem;
        background: var(--color-superficie);
        border: 1px solid var(--color-borde);
        border-radius: 50%;
        cursor: pointer;
        transition: border-color 0.15s, transform 0.05s;
      }

      .boton-tema:hover {
        border-color: var(--color-acento);
      }

      .boton-tema:active {
        transform: translateY(1px);
      }
    `,
  ],
})
export class App {
  protected readonly temaService = inject(TemaService);
}
