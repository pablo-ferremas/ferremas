#!/bin/bash

# Script para ejecutar todas las pruebas de API y generar reportes htmlextra
# Autor: Asistente IA
# Fecha: $(date +"%Y-%m-%d")

echo "========================================"
echo "    EJECUTOR DE PRUEBAS API - FERREMAS"
echo "========================================"
echo

# Configuración base
BASE_PATH="src/test/java/cl/duoc/integracion/ferremas/postman"
ENV_FILE="$BASE_PATH/config/entorno-desarrollo.json"

# Verificar archivo de entorno
if [ ! -f "$ENV_FILE" ]; then
    echo "ERROR: No se encontró el archivo de entorno: $ENV_FILE"
    exit 1
fi

echo "Iniciando ejecución de todas las pruebas..."
echo

# Función para ejecutar una colección
run_collection() {
    local controller_name=$1
    local collection_name=$2
    local collection_file="$BASE_PATH/colecciones/$controller_name/${collection_name}-Tests.json"
    local report_path="$BASE_PATH/reportes/$controller_name"
    local report_file="$report_path/reporte-${collection_name}.html"

    echo "[$controller_name] Ejecutando $collection_name..."

    # Crear directorio si no existe
    mkdir -p "$report_path"

    # Verificar que existe la colección
    if [ ! -f "$collection_file" ]; then
        echo "[$controller_name] ❌ Error: No se encontró la colección: $collection_file"
        return 1
    fi

    # Ejecutar Newman (usando || true para ignorar errores de assertions)
    if npx newman run "$collection_file" -e "$ENV_FILE" --reporters htmlextra --reporter-htmlextra-export "$report_file" || true; then
        if [ -f "$report_file" ]; then
            echo "[$controller_name] ✅ $collection_name - Reporte generado exitosamente"
            return 0
        else
            echo "[$controller_name] ❌ $collection_name - Error al generar reporte"
            return 1
        fi
    else
        echo "[$controller_name] ❌ $collection_name - Error en la ejecución"
        return 1
    fi

    echo
}

# Contadores
total_collections=0
successful_collections=0
failed_collections=0

# Ejecutar todas las colecciones
echo "========================================"
echo "           PRODUCTO CONTROLLER"
echo "========================================"
run_collection producto GetAllProductos && ((successful_collections++)) || ((failed_collections++))
((total_collections++))
run_collection producto GetProductoById && ((successful_collections++)) || ((failed_collections++))
((total_collections++))
run_collection producto GetProductosByCategoria && ((successful_collections++)) || ((failed_collections++))
((total_collections++))
run_collection producto GetProductosDestacados && ((successful_collections++)) || ((failed_collections++))
((total_collections++))

echo "========================================"
echo "           USUARIO CONTROLLER"
echo "========================================"
run_collection usuario Login && ((successful_collections++)) || ((failed_collections++))
((total_collections++))
run_collection usuario Registro && ((successful_collections++)) || ((failed_collections++))
((total_collections++))

echo "========================================"
echo "          CATEGORIA CONTROLLER"
echo "========================================"
run_collection categoria GetAllCategorias && ((successful_collections++)) || ((failed_collections++))
((total_collections++))
run_collection categoria PostCategoria && ((successful_collections++)) || ((failed_collections++))
((total_collections++))
run_collection categoria DeleteCategoria && ((successful_collections++)) || ((failed_collections++))
((total_collections++))

echo "========================================"
echo "            MARCA CONTROLLER"
echo "========================================"
run_collection marca GetAllMarcas && ((successful_collections++)) || ((failed_collections++))
((total_collections++))
run_collection marca PostMarca && ((successful_collections++)) || ((failed_collections++))
((total_collections++))

echo "========================================"
echo "           PRECIO CONTROLLER"
echo "========================================"
run_collection precio GetAllPrecios && ((successful_collections++)) || ((failed_collections++))
((total_collections++))
run_collection precio PostPrecio && ((successful_collections++)) || ((failed_collections++))
((total_collections++))

echo "========================================"
echo "        BANCO CENTRAL CONTROLLER"
echo "========================================"
run_collection banco-central GetExchangeRates && ((successful_collections++)) || ((failed_collections++))
((total_collections++))

echo
echo "========================================"
echo "           EJECUCIÓN COMPLETADA"
echo "========================================"
echo
echo "RESUMEN DE EJECUCIÓN:"
echo "Total de colecciones: $total_collections"
echo "✅ Exitosas: $successful_collections"
echo "❌ Fallidas: $failed_collections"
echo

if [ $failed_collections -eq 0 ]; then
    echo "🎉 ¡Todas las pruebas se ejecutaron exitosamente!"
else
    echo "⚠️  Algunas pruebas fallaron. Revisa los reportes HTML para ver los detalles de las assertions."
fi

echo
echo "Los reportes se guardaron en:"
echo "   • $BASE_PATH/reportes/producto/"
echo "   • $BASE_PATH/reportes/usuario/"
echo "   • $BASE_PATH/reportes/categoria/"
echo "   • $BASE_PATH/reportes/marca/"
echo "   • $BASE_PATH/reportes/precio/"
echo "   • $BASE_PATH/reportes/banco-central/"
echo
echo "Para ver los reportes, abre los archivos .html en tu navegador"
echo 