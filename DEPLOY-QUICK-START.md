# 🚀 Guía Rápida de Despliegue en EC2

## ⚡ Pasos Rápidos (Resumen)

### 1️⃣ Conectarse a EC2
```bash
ssh -i tu-clave.pem ubuntu@tu-ip-ec2
```

### 2️⃣ Instalar Java 21 y PostgreSQL
```bash
sudo apt update
sudo apt install openjdk-21-jdk postgresql postgresql-contrib -y
```

### 3️⃣ Configurar PostgreSQL
```bash
sudo -u postgres psql
```
Dentro de PostgreSQL:
```sql
CREATE DATABASE pasteleria;
ALTER USER postgres WITH PASSWORD 'tu_password_seguro';
\q
```

### 4️⃣ Clonar y Compilar
```bash
cd ~
git clone https://github.com/Vi8let/pasteleria-backend.git
cd pasteleria-backend
git checkout Integracion-FB
chmod +x deploy.sh start.sh
./deploy.sh
```

### 5️⃣ Configurar Variables (Opcional)
```bash
nano ~/.pasteleria-env
```
Agregar:
```bash
export DB_PASSWORD=tu_password_seguro
export JWT_SECRET=tu_secreto_jwt_seguro
export CORS_ORIGINS=http://tu-frontend-ec2:puerto
```

### 6️⃣ Iniciar Backend
```bash
source ~/.pasteleria-env
./start.sh
```

### 7️⃣ Configurar Security Group en AWS
- Puerto: 8080
- Protocolo: TCP
- Origen: 0.0.0.0/0

### 8️⃣ Verificar
```bash
curl http://localhost:8080/api/products
```

## 🔧 Usar como Servicio (Recomendado)

Ver `README-DEPLOY.md` para configuración con systemd.

## 📝 URLs Importantes

- API: `http://tu-ip-ec2:8080/api`
- Swagger: `http://tu-ip-ec2:8080/swagger-ui.html`

