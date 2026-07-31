/**
 * Configuración de la aplicación.
 *
 * `apiBaseUrl` vacío = rutas relativas. En desarrollo, `proxy.conf.json`
 * redirige `/diccionarios` a `http://localhost:8080`; en producción, si la web
 * se sirve desde el mismo origen que la API, las rutas relativas ya funcionan.
 * Es el equivalente al `application.properties` del servidor.
 */
export const environment = {
  apiBaseUrl: '',
};
