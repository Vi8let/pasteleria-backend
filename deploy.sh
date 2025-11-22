#!/bin/bash

# Script de despliegue para EC2
# Uso: ./deploy.sh

echo "🚀 Iniciando despliegue del backend..."

# Colores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Verificar que estamos en el directorio correcto
if [ ! -f "pom.xml" ]; then
    echo -e "${RED}❌ Error: No se encontró pom.xml. Asegúrate de estar en el directorio del proyecto.${NC}"
    exit 1
fi

# Verificar Java
if ! command -v java &> /dev/null; then
    echo -e "${RED}❌ Error: Java no está instalado. Instala Java 21 primero.${NC}"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 21 ]; then
    echo -e "${YELLOW}⚠️  Advertencia: Se requiere Java 21. Versión actual: $JAVA_VERSION${NC}"
fi

# Verificar Maven
if ! command -v mvn &> /dev/null; then
    echo -e "${YELLOW}⚠️  Maven no encontrado, usando Maven Wrapper...${NC}"
    MVN_CMD="./mvnw"
else
    MVN_CMD="mvn"
fi

# Compilar el proyecto
echo -e "${YELLOW}📦 Compilando el proyecto...${NC}"
$MVN_CMD clean package -DskipTests

if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Error al compilar el proyecto${NC}"
    exit 1
fi

# Verificar que el JAR se creó
JAR_FILE="target/pasteleria-backend-1.0.0.jar"
if [ ! -f "$JAR_FILE" ]; then
    echo -e "${RED}❌ Error: No se encontró el archivo JAR en $JAR_FILE${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Compilación exitosa${NC}"
echo -e "${GREEN}📦 JAR creado: $JAR_FILE${NC}"

# Instrucciones para ejecutar
echo ""
echo -e "${YELLOW}📋 Próximos pasos:${NC}"
echo "1. Asegúrate de que PostgreSQL esté instalado y corriendo"
echo "2. Crea la base de datos: CREATE DATABASE pasteleria;"
echo "3. Configura las variables de entorno (opcional):"
echo "   export DB_HOST=localhost"
echo "   export DB_PORT=5432"
echo "   export DB_NAME=pasteleria"
echo "   export DB_USER=postgres"
echo "   export DB_PASSWORD=tu_password"
echo "   export JWT_SECRET=tu_secreto_jwt_seguro"
echo "4. Ejecuta el backend:"
echo "   java -jar $JAR_FILE --spring.profiles.active=prod"
echo ""
echo -e "${GREEN}✅ Despliegue completado${NC}"

