# Pastelería 1000 Sabores - Backend

Backend desarrollado en Spring Boot para el sistema de gestión de la Pastelería 1000 Sabores. Incluye gestión de órdenes, productos, usuarios y un módulo de pagos y notificaciones por correo.

## Requisitos

- Java 21
- Maven
- PostgreSQL

## Configuración

### Variables de Entorno

Para despliegue en EC2 o local, se recomienda configurar las siguientes variables de entorno. Puedes crear un archivo `.env` o configurarlas en el sistema.

```properties
# Base de Datos
DB_URL=jdbc:postgresql://localhost:5432/pasteleria
DB_USER=postgres
DB_PASSWORD=postgres

# JWT
JWT_SECRET=MI_SUPER_SECRETO_1234_5678901234567890
JWT_EXPIRATION=86400000

# Email (Opcional - Si no se configura, usa simulación por logs)
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=tu-email@gmail.com
SPRING_MAIL_PASSWORD=tu-password-aplicacion
SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH=true
SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE=true
```

## Ejecución

### Local

```bash
./mvnw spring-boot:run
```

### Producción (EC2)

1. **Construir el JAR:**
   ```bash
   ./mvnw clean package -DskipTests
   ```

2. **Ejecutar:**
   ```bash
   java -jar target/pasteleria-backend-1.0.0.jar
   ```

   Para mantenerlo corriendo en segundo plano:
   ```bash
   nohup java -jar target/pasteleria-backend-1.0.0.jar > app.log 2>&1 &
   ```

## Módulo de Pagos

Actualmente el sistema utiliza un **Mock de Pagos** para facilitar pruebas sin costos.

### Endpoint: Crear Pago

**POST** `/api/payments/create`

**Headers:**
- `Authorization`: `Bearer <TOKEN_JWT>`
- `Content-Type`: `application/json`

**Body:**
```json
{
  "orderId": 1,
  "cardNumber": "1234567890123456",
  "cardHolder": "Juan Perez",
  "expirationDate": "12/25",
  "cvv": "123"
}
```

**Respuesta Exitosa (200 OK):**
Retorna el objeto `Order` con estado `PAID`.

**Errores Comunes:**
- `404 Not Found`: Orden no existe.
- `400 Bad Request`: Orden ya pagada o tarjeta rechazada (simular rechazo usando tarjeta "0000000000000000").

## Módulo de Correos

El sistema intenta enviar correos reales si las variables `SPRING_MAIL_*` están configuradas. De lo contrario, **simula el envío** escribiendo el contenido del correo en los logs de la aplicación (consola).

Para ver los correos simulados:
```bash
tail -f app.log
```
