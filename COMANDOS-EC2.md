# 📋 Comandos Listos para Copiar y Pegar en EC2

## 🔗 Repositorio: https://github.com/Vi8let/pasteleria-backend
## 🌿 Rama: `Integracion-FB`

---

## 1️⃣ Instalar Dependencias

```bash
sudo apt update
sudo apt install openjdk-21-jdk postgresql postgresql-contrib -y
```

---

## 2️⃣ Configurar PostgreSQL

```bash
sudo -u postgres psql
```

Dentro de PostgreSQL, ejecutar:
```sql
CREATE DATABASE pasteleria;
ALTER USER postgres WITH PASSWORD 'pasteleria123';
GRANT ALL PRIVILEGES ON DATABASE pasteleria TO postgres;
\q
```

---

## 3️⃣ Clonar y Preparar Proyecto

```bash
cd ~
git clone https://github.com/Vi8let/pasteleria-backend.git
cd pasteleria-backend
git checkout Integracion-FB
chmod +x deploy.sh start.sh
```

---

## 4️⃣ Compilar

```bash
./deploy.sh
```

O manualmente:
```bash
./mvnw clean package -DskipTests
```

---

## 5️⃣ Configurar Variables de Entorno (Opcional)

```bash
nano ~/.pasteleria-env
```

Agregar:
```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=pasteleria
export DB_USER=postgres
export DB_PASSWORD=pasteleria123
export JWT_SECRET=MI_SUPER_SECRETO_1234_5678901234567890
export JWT_EXPIRATION=86400000
export PORT=8080
export CORS_ORIGINS=http://tu-frontend-ec2:puerto
```

Guardar (Ctrl+O, Enter, Ctrl+X)

Cargar variables:
```bash
source ~/.pasteleria-env
```

---

## 6️⃣ Iniciar Backend

### Opción A: Ejecución Directa
```bash
java -jar target/pasteleria-backend-1.0.0.jar --spring.profiles.active=prod
```

### Opción B: Usando Script
```bash
./start.sh
```

---

## 7️⃣ Verificar que Funciona

```bash
# Desde EC2
curl http://localhost:8080/api/products

# Debería devolver JSON con productos (puede estar vacío si no hay datos)
```

---

## 8️⃣ Configurar como Servicio (Recomendado)

```bash
sudo nano /etc/systemd/system/pasteleria-backend.service
```

Pegar este contenido:
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
Environment="DB_PASSWORD=pasteleria123"
Environment="JWT_SECRET=MI_SUPER_SECRETO_1234_5678901234567890"
Environment="JWT_EXPIRATION=86400000"
Environment="PORT=8080"
ExecStart=/usr/bin/java -jar /home/ubuntu/pasteleria-backend/target/pasteleria-backend-1.0.0.jar --spring.profiles.active=prod
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Activar servicio:
```bash
sudo systemctl daemon-reload
sudo systemctl enable pasteleria-backend
sudo systemctl start pasteleria-backend
```

Ver estado:
```bash
sudo systemctl status pasteleria-backend
```

Ver logs:
```bash
sudo journalctl -u pasteleria-backend -f
```

---

## 🔧 Comandos Útiles

### Ver si el backend está corriendo
```bash
sudo lsof -i :8080
```

### Detener backend
```bash
# Si está como servicio
sudo systemctl stop pasteleria-backend

# Si está ejecutándose directamente
# Presionar Ctrl+C o encontrar el proceso y matarlo
pkill -f pasteleria-backend
```

### Reiniciar backend
```bash
sudo systemctl restart pasteleria-backend
```

### Ver logs en tiempo real
```bash
sudo journalctl -u pasteleria-backend -f
```

### Verificar PostgreSQL
```bash
sudo systemctl status postgresql
sudo -u postgres psql -d pasteleria -c "SELECT 1;"
```

---

## ⚠️ Recordatorios

1. **Security Group**: Asegúrate de abrir el puerto 8080 en AWS EC2 Security Groups
2. **CORS**: Actualiza `CORS_ORIGINS` con la URL exacta de tu frontend
3. **Contraseñas**: Cambia las contraseñas por defecto en producción
4. **JWT Secret**: Usa un secreto fuerte y único

---

## 📝 URLs después del despliegue

- **API Base**: `http://tu-ip-ec2:8080/api`
- **Swagger UI**: `http://tu-ip-ec2:8080/swagger-ui.html`
- **API Docs**: `http://tu-ip-ec2:8080/v3/api-docs`

