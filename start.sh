#!/bin/bash

# Script rápido para iniciar el backend
# Uso: ./start.sh

echo "🚀 Iniciando backend..."

# Cargar variables de entorno si existen
if [ -f ~/.pasteleria-env ]; then
    source ~/.pasteleria-env
    echo "✅ Variables de entorno cargadas"
fi

# Verificar que el JAR existe
JAR_FILE="target/pasteleria-backend-1.0.0.jar"
if [ ! -f "$JAR_FILE" ]; then
    echo "❌ JAR no encontrado. Compilando..."
    ./mvnw clean package -DskipTests
fi

# Iniciar el backend
echo "📦 Ejecutando backend..."
java -jar "$JAR_FILE" --spring.profiles.active=prod

