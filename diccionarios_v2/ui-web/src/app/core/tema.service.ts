import { Injectable, effect, signal } from '@angular/core';

type Tema = 'claro' | 'oscuro';

/**
 * Gestiona el tema claro/oscuro de la interfaz.
 *
 * - Por defecto respeta la preferencia del sistema operativo.
 * - Permite alternar manualmente.
 * - Recuerda la elección del usuario en `localStorage`.
 *
 * Es un servicio singleton (como los beans de Spring): una única instancia
 * compartida en toda la aplicación.
 */
@Injectable({ providedIn: 'root' })
export class TemaService {
  private static readonly CLAVE = 'diccionarios.tema';

  readonly tema = signal<Tema>(this.temaInicial());

  constructor() {
    // Cada vez que cambia el tema, lo reflejamos en el DOM y lo persistimos.
    effect(() => {
      const tema = this.tema();
      document.documentElement.dataset['tema'] = tema;
      localStorage.setItem(TemaService.CLAVE, tema);
    });
  }

  alternar(): void {
    this.tema.update((actual) => (actual === 'claro' ? 'oscuro' : 'claro'));
  }

  private temaInicial(): Tema {
    const guardado = localStorage.getItem(TemaService.CLAVE);
    if (guardado === 'claro' || guardado === 'oscuro') {
      return guardado;
    }
    const prefiereOscuro = window.matchMedia('(prefers-color-scheme: dark)').matches;
    return prefiereOscuro ? 'oscuro' : 'claro';
  }
}
