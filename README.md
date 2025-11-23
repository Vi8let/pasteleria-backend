

# Pastelería Backend – Spring Boot + JWT + Swagger

Este proyecto corresponde al backend de una aplicación de gestión de pastelería desarrollado con Spring Boot.
Incluye implementación completa de autenticación mediante JWT, estructura modular del proyecto, conexión a base de datos PostgreSQL y documentación de API a través de Swagger/OpenAPI.

El objetivo inicial fue construir la base del sistema, habilitar el registro e inicio de sesión de usuarios y asegurar los endpoints mediante middleware JWT.

---

## Tecnologías utilizadas

* Java 21
* Spring Boot 3
* Spring Security
* JWT (JSON Web Tokens)
* PostgreSQL
* Spring Data JPA / Hibernate
* Maven
* Swagger UI (OpenAPI 3)

---

## Estructura del proyecto

```
src/main/java/com/pasteleria/pasteleria_backend
│
├── config
│   └── SecurityConfig.java
│
├── controller
│   └── AuthController.java
│
├── model
│   └── User.java
│
├── repository
│   └── UserRepository.java
│
└── security
    ├── JwtUtil.java
    ├── JwtFilter.java
    └── CustomUserDetailsService.java
```

### Descripción de carpetas

* **config/**
  Contiene la configuración central de Spring Security, reglas de acceso, manejo de sesiones y registro del filtro JWT.

* **controller/**
  Implementa los controladores REST. Incluye las rutas de autenticación: registro e inicio de sesión.

* **model/**
  Define las entidades del sistema. El modelo `User` incluye email, contraseña encriptada y rol.

* **repository/**
  Contiene las interfaces JPA que permiten consultas hacia la base de datos.

* **security/**
  Módulo completo de seguridad JWT: generación de tokens, validación, filtro global y cargador de usuarios.

---

# Implementación de autenticación

### 1. Entidad User

Incluye los siguientes campos:

* id
* email
* password (encriptado con BCrypt)
* role

La contraseña nunca se almacena en texto plano.

### 2. UserRepository

Método principal:

```java
User findByEmail(String email);
```

### 3. CustomUserDetailsService

Servicio utilizado por Spring Security para cargar usuarios desde la base de datos durante el proceso de autenticación.

### 4. JwtUtil

Clase encargada de:

* generar tokens JWT con email y rol
* extraer información del token
* validar expiración y firma

### 5. JwtFilter

Filtro que intercepta cada petición, valida el token y establece el usuario autenticado en el contexto de Spring Security.

### 6. SecurityConfig

Configuración completa de seguridad, incluyendo:

* desactivación de CSRF
* habilitación de CORS
* endpoints públicos
* endpoints privados
* modo de sesión stateless para JWT
* registro de AuthenticationProvider
* inclusión del JwtFilter antes del filtro UsernamePasswordAuthenticationFilter

Endpoints públicos configurados:

```
/api/auth/login
/api/auth/register
/swagger-ui/**
/v3/api-docs/**
```

---

# Endpoints implementados

## POST /api/auth/register

### Request body

```json
{
  "email": "correo@example.com",
  "password": "123456"
}
```

### Respuesta

```json
{
  "ok": true,
  "userId": 1
}
```

---

## POST /api/auth/login

### Request body

```json
{
  "email": "correo@example.com",
  "password": "123456"
}
```

### Respuesta

```json
{
  "ok": true,
  "token": "jwt-generado",
  "role": "USER",
  "email": "correo@example.com"
}
```

---

# Documentación de API

La documentación del backend está disponible mediante Swagger UI:

```
http://localhost:8080/swagger-ui/index.html
```

Desde esta interfaz se verificó el funcionamiento de:

* Registro de usuarios
* Inicio de sesión
* Generación de token JWT

---

# Estado del proyecto en este punto

Hasta esta etapa se ha implementado:

* Configuración base de Spring Boot
* Conexión a PostgreSQL
* Seguridad con JWT completamente funcional
* Endpoints de registro y login
* Filtro de autenticación integrado
* Documentación automática mediante Swagger
* Pruebas exitosas de registro e inicio de sesión desde Swagger UI

El backend queda preparado para continuar con módulos adicionales como gestión de productos, ventas, roles avanzados y consumo desde frontend.
