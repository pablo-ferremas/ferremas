#!/bin/bash

# Definir rutas
JMETER_TEST_FILE="src/test/java/cl/duoc/integracion/ferremas/jmeter/FerremasPerformanceTests.jmx"
RESULTS_DIR="src/test/java/cl/duoc/integracion/ferremas/jmeter/resultados"
RESULTS_FILE="$RESULTS_DIR/results.jtl"
REPORT_DIR="$RESULTS_DIR/reporte"

# Crear la estructura de directorios si no existe
mkdir -p "$RESULTS_DIR"

# Limpiar directorio de resultados previos
echo "Limpiando directorio de resultados..."
rm -rf "$RESULTS_DIR"/*

# Crear directorio para el reporte
mkdir -p "$REPORT_DIR"

# Ejecutar pruebas JMeter
echo "Ejecutando pruebas de JMeter..."
jmeter -n \
    -t "$JMETER_TEST_FILE" \
    -l "$RESULTS_FILE" \
    -e -o "$REPORT_DIR" \
    -Juser.dir=$(pwd) \
    -Jcsv.file=$(pwd)/src/test/java/cl/duoc/integracion/ferremas/jmeter/test-users.csv

# Verificar si la ejecución fue exitosa
if [ $? -eq 0 ]; then
    echo "✅ Pruebas completadas exitosamente"
    echo "📊 Resultados guardados en: $RESULTS_FILE"
    echo "📋 Reporte HTML generado en: $REPORT_DIR"
else
    echo "❌ Error al ejecutar las pruebas"
    exit 1
fi 