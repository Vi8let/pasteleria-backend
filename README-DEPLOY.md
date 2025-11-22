# 🚀 Guía de Despliegue del Backend en EC2

Esta guía te ayudará a desplegar el backend Spring Boot en tu instancia EC2.

## 📋 Requisitos Previos

1. **Instancia EC2** con acceso SSH
2. **Java 21** instalado
3. **PostgreSQL** instalado y corriendo
4. **Maven** (opcional, se puede usar Maven Wrapper)

## 🔧 Paso 1: Conectarse a EC2

```bash
ssh -i tu-clave.pem ubuntu@tu-ip-ec2
# o
ssh -i tu-clave.pem ec2-user@tu-ip-ec2
```

## 📦 Paso 2: Instalar Dependencias

### Instalar Java 21

```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-21-jdk -y

# Verificar instalación
java -version
```

### Instalar PostgreSQL

```bash
# Ubuntu/Debian
sudo apt install postgresql postgresql-contrib -y

# Iniciar PostgreSQL
sudo systemctl start postgresql
sudo systemctl enable postgresql
```

### Configurar PostgreSQL

```bash
# Cambiar al usuario postgres
sudo -u postgres psql

# Dentro de PostgreSQL, ejecutar:
CREATE DATABASE pasteleria;
CREATE USER postgres WITH PASSWORD 'tu_password_seguro';
ALTER ROLE postgres SET client_encoding TO 'utf8';
ALTER ROLE postgres SET default_transaction_isolation TO 'read committed';
ALTER ROLE postgres SET timezone TO 'UTC';
GRANT ALL PRIVILEGES ON DATABASE pasteleria TO postgres;
\q
```

## 📥 Paso 3: Clonar el Repositorio

```bash
# Navegar a donde quieres el proyecto
cd ~

# Clonar tu repositorio
git clone https://github.com/Vi8let/pasteleria-backend.git
cd pasteleria-backend

# Cambiar a la rama Integracion-FB
git checkout Integracion-FB
```

## 🔨 Paso 4: Compilar el Proyecto

```bash
# Dar permisos de ejecución al script
chmod +x deploy.sh

# Ejecutar el script de despliegue
./deploy.sh

# O manualmente:
./mvnw clean package -DskipTests
```

## ⚙️ Paso 5: Configurar Variables de Entorno

Crea un archivo `.env` o exporta las variables:

```bash
# Crear archivo de configuración
nano ~/.pasteleria-env

# Agregar:
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=pasteleria
export DB_USER=postgres
export DB_PASSWORD=tu_password_seguro
export JWT_SECRET=MI_SUPER_SECRETO_1234_5678901234567890
export JWT_EXPIRATION=86400000
export PORT=8080
export CORS_ORIGINS=http://tu-frontend-ec2:puerto

# Cargar variables
source ~/.pasteleria-env
```

## 🚀 Paso 6: Ejecutar el Backend

### Opción 1: Ejecución Directa

```bash
java -jar target/pasteleria-backend-1.0.0.jar --spring.profiles.active=prod
```

### Opción 2: Usando systemd (Recomendado para producción)

Crear servicio systemd:

```bash
sudo nano /etc/systemd/system/pasteleria-backend.service
```

Contenido del archivo:

```ini
[Unit]
Description=Pasteleria Backend Service
After=network.target postgresql.service

[Service]
Type=simple
User=ubuntu
WorkingDirectory=/home/ubuntu/pasteleria-backend
Environment="DB_HOST=localhost"
Environment="DB_PORT=5432"
Environment="DB_NAME=pasteleria"
Environment="DB_USER=postgres"
Environment="DB_PASSWORD=tu_password_seguro"
Environment="JWT_SECRET=MI_SUPER_SECRETO_1234_5678901234567890"
Environment="JWT_EXPIRATION=86400000"
Environment="PORT=8080"
Environment="CORS_ORIGINS=http://tu-frontend-ec2:puerto"
ExecStart=/usr/bin/java -jar /home/ubuntu/pasteleria-backend/target/pasteleria-backend-1.0.0.jar --spring.profiles.active=prod
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Activar y iniciar el servicio:

```bash
sudo systemctl daemon-reload
sudo systemctl enable pasteleria-backend
sudo systemctl start pasteleria-backend

# Verificar estado
sudo systemctl status pasteleria-backend

# Ver logs
sudo journalctl -u pasteleria-backend -f
```

## 🔒 Paso 7: Configurar Firewall (Security Group)

En la consola de AWS EC2:

1. Ir a **Security Groups**
2. Seleccionar el grupo de seguridad de tu instancia
3. Agregar regla de entrada:
   - **Tipo**: Custom TCP
   - **Puerto**: 8080
   - **Origen**: 0.0.0.0/0 (o solo tu IP para mayor seguridad)

## ✅ Paso 8: Verificar el Despliegue

```bash
# Verificar que el backend está corriendo
curl http://localhost:8080/api/products

# Ver Swagger
# Abre en navegador: http://tu-ip-ec2:8080/swagger-ui.html
```

## 🔧 Solución de Problemas

### El backend no inicia

```bash
# Ver logs
sudo journalctl -u pasteleria-backend -n 50

# Verificar que PostgreSQL está corriendo
sudo systemctl status postgresql

# Verificar conexión a BD
sudo -u postgres psql -d pasteleria -c "SELECT 1;"
```

### Error de conexión a base de datos

```bash
# Verificar que PostgreSQL acepta conexiones
sudo nano /etc/postgresql/*/main/postgresql.conf
# Buscar: listen_addresses = 'localhost'

# Verificar pg_hba.conf
sudo nano /etc/postgresql/*/main/pg_hba.conf
# Debe tener: local   all   postgres   md5
```

### Puerto 8080 ya en uso

```bash
# Ver qué está usando el puerto
sudo lsof -i :8080

# Cambiar puerto en application-prod.properties
# O matar el proceso: sudo kill -9 <PID>
```

## 📝 Notas Importantes

- **Seguridad**: Cambia las contraseñas por defecto en producción
- **JWT Secret**: Usa un secreto fuerte y único en producción
- **CORS**: Ajusta `CORS_ORIGINS` con la URL exacta de tu frontend
- **Logs**: Los logs se guardan en `/var/log/syslog` si usas systemd

## 🔄 Actualizar el Backend

```bash
cd ~/pasteleria-backend
git checkout Integracion-FB
git pull
./mvnw clean package -DskipTests
sudo systemctl restart pasteleria-backend
```

## 📞 URLs Importantes

- **API Base**: `http://tu-ip-ec2:8080/api`
- **Swagger UI**: `http://tu-ip-ec2:8080/swagger-ui.html`
- **API Docs**: `http://tu-ip-ec2:8080/v3/api-docs`

