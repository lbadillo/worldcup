# World Cup

Aplicación web construida con React y Vite para explorar y administrar información relacionada con grupos, partidos y flujo de autenticación por roles.

## Tecnologías

- React 19
- Vite
- React Router DOM
- Axios
- Bootstrap
- Bootstrap Icons
- react-circle-flags

## Requisitos

- Node.js 18 o superior
- npm

## Instalación

```bash
npm install
```

## Scripts disponibles

- `npm run dev`: inicia el servidor de desarrollo de Vite.
- `npm run loc`: inicia Vite usando el modo `devlocal`.
- `npm run build`: genera la versión de producción.
- `npm run build:dev`: genera el build usando el modo `dev`.
- `npm run build:qa`: genera el build usando el modo `qa`.
- `npm run build:stg`: genera el build usando el modo `stg`.
- `npm run build:prod`: genera el build usando el modo `prod`.
- `npm run lint`: ejecuta ESLint sobre el proyecto.
- `npm run preview`: previsualiza el build generado.

## Variables de entorno

El proyecto usa variables `VITE_` para configurar el acceso al backend y el login.

Ejemplo de `.env`:

```env
VITE_API_URL=/api
```

Ejemplo de `.env.devlocal`:

```env
VITE_LOGIN=y
VITE_API_TARGET=http://localhost:3000
```

### Notas sobre API

- El cliente HTTP vive en [src/services/api.js](src/services/api.js).
- El service principal de grupos está en [src/services/groupService.js](src/services/groupService.js).
- En modo local, Vite usa proxy para redirigir `/api` hacia `VITE_API_TARGET`.

## Estructura principal

- [src/main.jsx](src/main.jsx): punto de entrada de la app.
- [src/App.jsx](src/App.jsx): envuelve la aplicación con `BrowserRouter`.
- [src/routers/AppRouter.jsx](src/routers/AppRouter.jsx): define rutas y navegación por roles.
- [src/layouts/MainLayout.jsx](src/layouts/MainLayout.jsx): layout principal con header, navegación y footer.
- [src/context/AuthContext.jsx](src/context/AuthContext.jsx): contexto de autenticación en `localStorage`.
- [src/Features/Group/Group.jsx](src/Features/Group/Group.jsx): vista de grupos y equipos.

## Flujo de autenticación

La app guarda el usuario autenticado en `localStorage` y usa el contexto global para decidir qué tabs y rutas mostrar. El login acepta roles `user`, `admin` y `manager`.

## Desarrollo

Para arrancar en local con backend propio:

```bash
npm run loc
```

Si tu backend corre en otra URL, ajusta `VITE_API_TARGET` en `.env.devlocal`.

## Build

```bash
npm run build
```

## Licencia

Proyecto interno sin licencia definida.
