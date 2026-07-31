import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';

import { routes } from './app.routes';
import { SuministradorDeDiccionarios } from './core/dominio/suministrador-de-diccionarios';
import { SuministradorDeDiccionariosHttp } from './core/infraestructura/suministrador-de-diccionarios-http';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    provideHttpClient(),
    // Aquí se "cablea" la inyección de dependencias: cuando alguien pida el
    // contrato abstracto, Angular entrega la implementación HTTP. Cambiar de
    // implementación (p. ej. un mock) es cambiar sólo esta línea (DIP/OCP).
    { provide: SuministradorDeDiccionarios, useClass: SuministradorDeDiccionariosHttp },
  ],
};
