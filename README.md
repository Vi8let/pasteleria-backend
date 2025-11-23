# Pastelería Frontend – React + Vite + Bootstrap

Este proyecto corresponde al frontend de una aplicación de gestión de pastelería desarrollado con React y Vite. Incluye implementación completa de autenticación mediante JWT, integración con backend Spring Boot, gestión de productos, carrito de compras, sistema de pedidos, panel de administración y sistema de descuentos.

El objetivo inicial fue construir una interfaz de usuario moderna y funcional que consuma los servicios del backend, implementar autenticación basada en tokens JWT, gestionar sesiones de usuario y proporcionar una experiencia fluida tanto para clientes como para administradores.

## Tecnologías utilizadas

- **React 18**
- **Vite 5**
- **React Router DOM 6**
- **Bootstrap 5**
- **JWT (JSON Web Tokens)**
- **Karma + Jasmine** (Testing)
- **Babel** (Transpilación)

## Estructura del proyecto

```
src/
│
├── components/
│   └── Navbar.jsx
│
├── config/
│   └── api.js
│
├── data/
│   └── regiones-comunas.js
│
├── pages/
│   ├── Home.jsx
│   ├── Login.jsx
│   ├── Productos.jsx
│   ├── ProductoDetalle.jsx
│   ├── Carrito.jsx
│   ├── Perfil.jsx
│   ├── Pedidos.jsx
│   └── Admin.jsx
│
├── services/
│   ├── apiClient.js
│   ├── authService.js
│   ├── productService.js
│   ├── orderService.js
│   ├── cartService.js
│   ├── discountService.js
│   ├── userService.js
│   └── userServiceBackend.js
│
├── App.jsx
├── main.jsx
└── styles.css
```

## Descripción de carpetas

**components/** Contiene componentes reutilizables de la interfaz. El componente Navbar gestiona la navegación principal, muestra el estado de autenticación y el contador del carrito.

**config/** Configuración centralizada de la aplicación. Incluye la URL base del backend API para facilitar el cambio de entorno.

**data/** Datos estáticos de la aplicación. Incluye información de regiones y comunas de Chile para formularios de registro y perfil.

**pages/** Implementa todas las páginas/vistas de la aplicación. Cada página corresponde a una ruta específica y gestiona su propio estado y lógica de presentación.

**services/** Módulo completo de servicios que encapsulan la lógica de negocio y comunicación con el backend. Cada servicio maneja un dominio específico (autenticación, productos, órdenes, carrito, descuentos, usuarios).

## Implementación de funcionalidades

### 1. Autenticación y sesión

**authService.js** gestiona todo el ciclo de autenticación:

- Registro de nuevos usuarios con validación de campos
- Inicio de sesión con validación de credenciales
- Almacenamiento seguro del token JWT en localStorage
- Gestión de sesión de usuario en memoria
- Obtención de perfil de usuario desde el backend
- Cierre de sesión con limpieza de datos

El token JWT se almacena de forma segura y se incluye automáticamente en todas las peticiones autenticadas.

### 2. Cliente API

**apiClient.js** proporciona una capa de abstracción para todas las comunicaciones con el backend:

- Interceptor automático para agregar token JWT en headers
- Manejo centralizado de errores HTTP
- Métodos para GET, POST, PUT, PATCH, DELETE
- Soporte para endpoints públicos y autenticados
- Gestión automática de headers y formato JSON

### 3. Gestión de productos

**productService.js** maneja todas las operaciones relacionadas con productos:

- Obtención de lista de productos desde el backend
- Búsqueda de producto por ID
- Creación de nuevos productos (solo ADMIN)
- Actualización de productos existentes (solo ADMIN)
- Eliminación de productos (solo ADMIN)
- Mapeo de datos entre formato backend y frontend

### 4. Carrito de compras

**cartService.js** gestiona el carrito de compras del usuario:

- Agregar productos al carrito
- Actualizar cantidades
- Eliminar productos
- Vaciar carrito
- Persistencia en localStorage
- Eventos personalizados para sincronización en tiempo real

### 5. Sistema de órdenes

**orderService.js** maneja la creación y consulta de pedidos:

- Creación de órdenes desde el carrito
- Obtención de pedidos del usuario autenticado
- Obtención de todas las órdenes (solo ADMIN)
- Actualización de estado de órdenes (solo ADMIN)
- Integración completa con el backend

### 6. Sistema de descuentos

**discountService.js** implementa la lógica de cálculo de descuentos:

- Descuento por código promocional FELICES50 (10%)
- Descuento por cumpleaños de estudiantes DUOC (20%)
- Descuento para usuarios mayores de 50 años (50%)
- Selección automática del mejor descuento aplicable
- Validación de elegibilidad según datos del usuario

### 7. Gestión de usuarios

**userServiceBackend.js** permite a los administradores gestionar usuarios:

- Listado de todos los usuarios
- Eliminación de usuarios
- Activación de códigos promocionales para usuarios
- Integración con el backend para persistencia

## Páginas implementadas

### Home
Página principal que muestra productos destacados, información de la pastelería y enlaces principales. Adapta su contenido según el rol del usuario (cliente o administrador).

### Login
Página de autenticación con dos pestañas:
- **Iniciar sesión**: Permite a usuarios existentes autenticarse
- **Registrarse**: Formulario completo de registro con validación de campos, selección de región/comuna y código promocional opcional

### Productos
Catálogo completo de productos con:
- Filtrado por categorías
- Vista de tarjetas con información detallada
- Botones para agregar al carrito (solo usuarios autenticados)
- Enlaces a detalle de producto

### ProductoDetalle
Vista detallada de un producto individual con información completa, opción de agregar al carrito y navegación de retorno.

### Carrito
Gestión del carrito de compras con:
- Lista de productos seleccionados
- Modificación de cantidades
- Eliminación de productos
- Cálculo de subtotal y descuentos aplicables
- Finalización de compra con generación de orden
- Modal de confirmación con boleta detallada

### Perfil
Página de perfil de usuario donde se pueden visualizar y editar datos personales, información de descuentos activos y datos de contacto.

### Pedidos
Historial de pedidos del usuario autenticado con:
- Lista de todas las órdenes realizadas
- Detalle expandible de cada pedido
- Estado actual de cada orden
- Información de productos y totales

### Admin
Panel de administración con dos pestañas:
- **Productos**: Gestión completa del catálogo (crear, editar, eliminar productos)
- **Usuarios**: Gestión de usuarios (listar, eliminar, activar códigos promocionales)

## Rutas implementadas

```
/                    → Home (página principal)
/login               → Login/Registro
/productos           → Catálogo de productos
/producto/:id        → Detalle de producto
/carrito             → Carrito de compras
/perfil              → Perfil de usuario (requiere autenticación)
/pedidos             → Mis pedidos (requiere autenticación)
/admin               → Panel de administración (requiere rol ADMIN)
```

## Integración con backend

El frontend está completamente integrado con el backend Spring Boot mediante:

- **Autenticación JWT**: Todos los endpoints protegidos requieren token válido
- **REST API**: Comunicación mediante peticiones HTTP estándar
- **Manejo de roles**: Diferenciación entre usuarios ADMIN y USER
- **Gestión de sesiones**: Persistencia de autenticación en localStorage
- **CORS configurado**: El backend permite peticiones desde el frontend

## Sistema de pruebas

El proyecto incluye configuración completa de testing con:

- **Karma**: Ejecutor de pruebas
- **Jasmine**: Framework de testing
- **Cobertura de código**: Configurada con Istanbul
- **Tests unitarios**: Implementados para servicios principales (authService, productService, cartService, discountService)

Los tests se ejecutan con:
```bash
npm test
```

## Estado del proyecto en este punto

Hasta esta etapa se ha implementado:

- Configuración base de React con Vite
- Sistema de enrutamiento con React Router
- Integración completa con backend Spring Boot
- Autenticación JWT completamente funcional
- Gestión de sesiones de usuario
- Páginas principales de la aplicación
- Panel de administración funcional
- Sistema de carrito de compras
- Sistema de pedidos y órdenes
- Sistema de descuentos inteligente
- Gestión de usuarios (para administradores)
- Interfaz responsive con Bootstrap 5
- Validación de acceso según roles
- Pruebas unitarias implementadas

El frontend queda completamente preparado y funcional, consumiendo todos los módulos del backend como gestión de productos, ventas, roles avanzados y autenticación. La aplicación está lista para despliegue en producción y uso completo por parte de usuarios y administradores.
