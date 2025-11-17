#!/bin/bash

echo "=== Iniciando ejecución de pruebas JUnit (sin Allure) ==="

# Limpiar reportes anteriores
echo "🧹 Limpiando reportes anteriores..."

# Eliminar reportes JUnit anteriores en directorio unitarias/reportes
JUNIT_REPORTS_DIR="src/test/java/cl/duoc/integracion/ferremas/unitarias/reportes"
if [ -d "$JUNIT_REPORTS_DIR" ]; then
    echo "  - Eliminando reportes JUnit anteriores en $JUNIT_REPORTS_DIR..."
    rm -rf "$JUNIT_REPORTS_DIR"/*
fi

# Eliminar reportes Surefire anteriores
if [ -d "target/surefire-reports" ]; then
    echo "  - Eliminando reportes Surefire anteriores..."
    rm -rf target/surefire-reports/*
fi

if [ -d "target/site" ]; then
    echo "  - Eliminando reportes HTML anteriores..."
    rm -rf target/site/*
fi

# Crear directorios si no existen
mkdir -p "$JUNIT_REPORTS_DIR"
mkdir -p target/surefire-reports
mkdir -p target/site

echo "✅ Limpieza completada"
echo ""

# Ejecutar pruebas unitarias
echo "🧪 Ejecutando pruebas unitarias JUnit..."
./mvnw clean test

# Verificar si las pruebas fueron exitosas
if [ $? -eq 0 ]; then
    echo "✅ Pruebas JUnit ejecutadas exitosamente"
    
    # Generar reportes HTML con Surefire
    echo "📊 Generando reportes HTML..."
    ./mvnw surefire-report:report
    
    if [ $? -eq 0 ]; then
        echo "✅ Reportes HTML generados exitosamente"
    else
        echo "⚠️  Error al generar reportes HTML, pero las pruebas se ejecutaron correctamente"
    fi
    
    # Contar pruebas ejecutadas
    if [ -d "target/surefire-reports" ]; then
        TEST_COUNT=$(find target/surefire-reports -name "TEST-*.xml" | wc -l)
        echo "📊 Total de clases de test ejecutadas: $TEST_COUNT"
    fi
    
    # Mostrar la ubicación de los reportes
    SUREFIRE_REPORT_PATH="target/site/surefire-report.html"
    
    echo ""
    echo "📋 REPORTES GENERADOS:"
    echo "  - Reportes JUnit: $JUNIT_REPORTS_DIR/"
    echo "  - Reportes Surefire XML: target/surefire-reports/"
    
    if [ -f "$SUREFIRE_REPORT_PATH" ]; then
        echo "  - Reporte Surefire HTML: $SUREFIRE_REPORT_PATH"
        echo ""
        echo "🌐 Para ver el reporte HTML, abra: $SUREFIRE_REPORT_PATH"
    else
        echo ""
        echo "📄 Los reportes detallados están disponibles en target/surefire-reports/"
    fi
    
    echo ""
    echo "✅ Todas las pruebas completadas exitosamente"
else
    echo "❌ Error en la ejecución de pruebas JUnit"
    echo "📋 Revise los logs anteriores para más detalles"
    exit 1
fi

echo ""
echo "=== Ejecución completada ==="
