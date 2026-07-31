import { Routes } from '@angular/router';
import { Buscador } from './features/diccionario/buscador/buscador';

/**
 * El estado de la búsqueda vive en la URL (patrón "URL como estado"):
 *  - `/`                        → buscador vacío
 *  - `/:idioma/:palabra`        → búsqueda concreta, compartible y recargable
 *
 * Es el equivalente web de los argumentos de línea de comandos que recibía la
 * aplicación de terminal (`buscarPalabra "melón" "es"`).
 */
export const routes: Routes = [
  { path: '', component: Buscador },
  { path: ':idioma/:palabra', component: Buscador },
];
