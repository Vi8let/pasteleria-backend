

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




# COMANDOS EC2 - PASTELERÍA 1000 SABORES

Backend: 54.164.104.243:8080  
Frontend: 54.236.225.25:5173

---

## BACKEND (EC2 pasteleria-backend)

```bash
cd ~/pasteleria-backend
git pull origin Integracion-FB
chmod +x ./mvnw
./mvnw clean package -DskipTests
pkill -f pasteleria-backend || true
nohup java -jar target/pasteleria-backend-1.0.0.jar --spring.profiles.active=prod > backend.log 2>&1 &
ps aux | grep -i java
curl -s http://localhost:8080/api/products | head
```

---

## FRONTEND (EC2 pasteleria-react en ~/app)

```bash
cd ~/app
if git status --short | grep -q "src/config/api.js"; then
  git checkout -- src/config/api.js
fi
git pull origin react
npm install
pkill -f vite || true
nohup npm run dev -- --host > frontend.log 2>&1 &
ps aux | grep -i vite
curl -I http://localhost:5173 | head -n 1
```

---

## PRUEBAS - JASMINE Y KARMA

### Frontend (EC2 pasteleria-react)

```bash
cd ~/app
npm test
```

O para ejecución única (CI):

```bash
npm run test:ci
```

---

## POSTGRESQL - COMANDOS BD

### Conectarse a PostgreSQL

```bash
sudo -i -u postgres
psql
```

### Dentro de psql

```sql
\l
\c pasteleria
\dt
\d
\d nombre_tabla
SELECT * FROM nombre_tabla;
SELECT * FROM users;
SELECT * FROM products;
```

---

## POSTMAN - CRUD API

### 1. LOGIN

**POST** `http://54.164.104.243:8080/api/auth/login`

Headers:
```
Content-Type: application/json
```

Body (raw JSON):
```json
{
  "email": "admin@pasteleria.com",
  "password": "Admin123*"
}
```

---

### 2. REGISTRO

**POST** `http://54.164.104.243:8080/api/auth/register`

Headers:
```
Content-Type: application/json
```

Body (raw JSON):
```json
{
  "email": "nuevo@usuario.com",
  "password": "Password123*",
  "fullName": "Nuevo Usuario",
  "run": "12345678-9",
  "fechaNacimiento": "1990-05-15",
  "region": "Región Metropolitana de Santiago",
  "comuna": "Santiago",
  "direccion": "Av. Principal 123",
  "codigoPromocion": "FELICES50"
}
```

---

### 3. GET PRODUCTOS

**GET** `http://54.164.104.243:8080/api/products`

Headers: (ninguno)

---

### 4. GET PRODUCTO POR ID

**GET** `http://54.164.104.243:8080/api/products/1`

Headers: (ninguno)

---

### 5. CREATE PRODUCTO

**POST** `http://54.164.104.243:8080/api/products`

Headers:
```
Content-Type: application/json
Authorization: Bearer [token_admin]
```

Body (raw JSON):
```json
{
  "name": "Torta Chocolate",
  "description": "Deliciosa torta de chocolate",
  "price": 15000,
  "imageUrl": "https://example.com/torta.jpg",
  "stock": 50,
  "category": "Tortas"
}
```

---

### 6. UPDATE PRODUCTO

**PUT** `http://54.164.104.243:8080/api/products/1`

Headers:
```
Content-Type: application/json
Authorization: Bearer [token_admin]
```

Body (raw JSON):
```json
{
  "name": "Torta Chocolate Premium",
  "description": "Deliciosa torta de chocolate mejorada",
  "price": 18000,
  "imageUrl": "https://example.com/torta-premium.jpg",
  "stock": 30,
  "category": "Tortas"
}
```

---

### 7. DELETE PRODUCTO

**DELETE** `http://54.164.104.243:8080/api/products/1`

Headers:
```
Authorization: Bearer [token_admin]
```

---

### 8. CREATE ORDEN

**POST** `http://54.164.104.243:8080/api/orders`

Headers:
```
Content-Type: application/json
Authorization: Bearer [token]
```

Body (raw JSON):
```json
{
  "items": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 2,
      "quantity": 1
    }
  ]
}
```

---

### 9. GET MIS ÓRDENES

**GET** `http://54.164.104.243:8080/api/orders/mine`

Headers:
```
Authorization: Bearer [token]
```

---

### 10. GET TODAS LAS ÓRDENES (ADMIN)

**GET** `http://54.164.104.243:8080/api/orders`

Headers:
```
Authorization: Bearer [token_admin]
```

---

### 11. GET USUARIOS (ADMIN)

**GET** `http://54.164.104.243:8080/api/users`

Headers:
```
Authorization: Bearer [token_admin]
```

---

### 12. DELETE USUARIO (ADMIN)

**DELETE** `http://54.164.104.243:8080/api/users/1`

Headers:
```
Authorization: Bearer [token_admin]
```

---

## LINKS

Frontend: http://54.236.225.25:5173  
Backend API: http://54.164.104.243:8080/api  
Swagger UI: http://54.164.104.243:8080/swagger-ui/index.html

**Endpoints directos (JSON):**
- Productos: http://54.164.104.243:8080/api/products
- Usuarios (requiere token admin): http://54.164.104.243:8080/api/users
- Órdenes (requiere token): http://54.164.104.243:8080/api/orders
- Swagger JSON: http://54.164.104.243:8080/v3/api-docs

**Credenciales:**
- Admin: admin@pasteleria.com / Admin123*
- Cliente: cliente@pasteleria.com / Cliente123*



El backend queda preparado para continuar con módulos adicionales como gestión de productos, ventas, roles avanzados y consumo desde frontend.
