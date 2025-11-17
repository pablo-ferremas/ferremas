#!/bin/bash

echo "=== Iniciando ejecución de pruebas JUnit con limpieza de reportes ==="

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

# Eliminar reportes Allure anteriores
if [ -d "target/allure-results" ]; then
    echo "  - Eliminando resultados Allure anteriores..."
    rm -rf target/allure-results/*
fi

if [ -d "allure-results" ]; then
    echo "  - Eliminando resultados Allure en directorio raíz..."
    rm -rf allure-results/*
fi

# Crear directorios si no existen
mkdir -p "$JUNIT_REPORTS_DIR"
mkdir -p target/surefire-reports
mkdir -p target/allure-results

echo "✅ Limpieza completada"
echo ""

# Ejecutar pruebas unitarias
echo "🧪 Ejecutando pruebas unitarias JUnit..."
./mvnw clean test

# Verificar si las pruebas fueron exitosas
if [ $? -eq 0 ]; then
    echo "✅ Pruebas JUnit ejecutadas exitosamente"
    
    # Generar reportes HTML con Surefire
    echo "📊 Generando reportes HTML con Surefire..."
    ./mvnw surefire-report:report
    
    # Intentar generar reporte Allure si está configurado
    echo "📊 Intentando generar reporte Allure..."
    ./mvnw allure:report 2>/dev/null
    
    ALLURE_SUCCESS=$?
    
    # Mostrar la ubicación de los reportes
    SUREFIRE_REPORT_PATH="target/site/surefire-report.html"
    ALLURE_REPORT_PATH="target/site/allure-maven-plugin/index.html"
    
    echo ""
    echo "📋 REPORTES GENERADOS:"
    echo "  - Reportes JUnit: $JUNIT_REPORTS_DIR/"
    echo "  - Reportes Surefire XML: target/surefire-reports/"
    
    if [ -f "$SUREFIRE_REPORT_PATH" ]; then
        echo "  - Reporte Surefire HTML: $SUREFIRE_REPORT_PATH"
        echo "🌐 Para ver el reporte principal, abra: $SUREFIRE_REPORT_PATH"
    fi
    
    if [ $ALLURE_SUCCESS -eq 0 ] && [ -f "$ALLURE_REPORT_PATH" ]; then
        echo "  - Reporte Allure: $ALLURE_REPORT_PATH"
        echo "🌐 Para ver el reporte Allure, abra: $ALLURE_REPORT_PATH"
    else
        echo "⚠️  Reporte Allure no disponible (configuración requerida)"
    fi
    
    echo ""
    echo "📄 Los reportes detallados están disponibles en:"
    echo "   - target/surefire-reports/ (XML y TXT)"
    if [ -f "$SUREFIRE_REPORT_PATH" ]; then
        echo "   - $SUREFIRE_REPORT_PATH (HTML)"
    fi
else
    echo "❌ Error en la ejecución de pruebas JUnit"
    exit 1
fi

echo ""
echo "=== Ejecución completada ===" 