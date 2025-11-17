# Colecciones de Pruebas - ProductoController

Este directorio contiene las colecciones de Postman para probar el ProductoController de la API Ferremas.

## Estructura de Archivos

### 📁 Colecciones
- **`GetAllProductos-Tests.json`** - Pruebas para el endpoint `GET /api/v1/productos`
- **`GetProductoById-Tests.json`** - Pruebas para el endpoint `GET /api/v1/productos/{id}`
- **`GetProductosByCategoria-Tests.json`** - Pruebas para el endpoint `GET /api/v1/productos/categoria/{categoriaId}`
- **`GetProductosDestacados-Tests.json`** - Pruebas para el endpoint `GET /api/v1/productos/destacados`

### 📁 Configuración
- **`../config/entorno-desarrollo.json`** - Variables de entorno para desarrollo

## Casos de Prueba Incluidos

### 📋 **GetAllProductos Tests (10 casos)**
1. **Obtener Todos los Productos** - Request básico
2. **Con Header Content-Type** - Incluyendo Content-Type header
3. **Con Accept XML** - Solicitando respuesta en XML
4. **Con Accept Text** - Solicitando respuesta en texto plano
5. **Con User-Agent** - Incluyendo User-Agent header
6. **Con Cache-Control** - Incluyendo Cache-Control header
7. **Con Authorization Header** - Incluyendo token de autorización
8. **Con Query Parameter Inválido** - Parámetros no soportados
9. **Con Múltiples Query Parameters** - Múltiples parámetros de consulta
10. **Con URL Encoding** - Parámetros con espacios y caracteres especiales

### 🔍 **GetProductoById Tests (12 casos)**
1. **ID Válido** - Obtener producto con ID existente
2. **ID Inválido** - Obtener producto con ID inexistente
3. **ID 0** - Obtener producto con ID cero
4. **ID Negativo** - Obtener producto con ID negativo
5. **ID Muy Grande** - Obtener producto con ID extremadamente grande
6. **ID como String** - Obtener producto con ID no numérico
7. **ID con Caracteres Especiales** - Obtener producto con ID con símbolos
8. **ID con Espacios** - Obtener producto con ID con espacios
9. **ID Decimal** - Obtener producto con ID decimal
10. **ID Vacío** - Obtener producto sin especificar ID
11. **Con Query Parameters** - Incluyendo parámetros adicionales
12. **Con Headers Adicionales** - Múltiples headers

### 📂 **GetProductosByCategoria Tests (14 casos)**
1. **Categoría Válida** - Obtener productos de categoría existente
2. **Categoría Inválida** - Obtener productos de categoría inexistente
3. **Categoría ID 0** - Obtener productos con ID de categoría cero
4. **Categoría ID Negativo** - Obtener productos con ID de categoría negativo
5. **Categoría ID Muy Grande** - Obtener productos con ID de categoría extremadamente grande
6. **Categoría ID como String** - Obtener productos con ID de categoría no numérico
7. **Categoría ID con Caracteres Especiales** - Obtener productos con ID de categoría con símbolos
8. **Categoría ID con Espacios** - Obtener productos con ID de categoría con espacios
9. **Categoría ID Decimal** - Obtener productos con ID de categoría decimal
10. **Categoría ID Vacío** - Obtener productos sin especificar ID de categoría
11. **Con Query Parameters** - Incluyendo parámetros de paginación y ordenamiento
12. **Con Headers Adicionales** - Múltiples headers
13. **Con Accept XML** - Solicitando respuesta en XML
14. **Con Accept Text** - Solicitando respuesta en texto plano

### ⭐ **GetProductosDestacados Tests (15 casos)**
1. **Obtener Productos Destacados** - Request básico
2. **Con Header Content-Type** - Incluyendo Content-Type header
3. **Con Accept XML** - Solicitando respuesta en XML
4. **Con Accept Text** - Solicitando respuesta en texto plano
5. **Con User-Agent** - Incluyendo User-Agent header
6. **Con Cache-Control** - Incluyendo Cache-Control header
7. **Con Authorization Header** - Incluyendo token de autorización
8. **Con Query Parameter Limit** - Limitando número de resultados
9. **Con Query Parameter Offset** - Paginación con offset
10. **Con Query Parameter Sort** - Ordenamiento por campo
11. **Con Múltiples Query Parameters** - Combinación de parámetros
12. **Con Query Parameter Inválido** - Parámetros no soportados
13. **Con Query Parameter con Espacios** - Parámetros con espacios
14. **Con Query Parameter con Caracteres Especiales** - Parámetros con símbolos
15. **Con Headers Múltiples** - Múltiples headers simultáneos

## Variables de Entorno Utilizadas

### **Variables Existentes:**
- `{{base_url}}` - URL base: `http://localhost:8090`
- `{{api_version}}` - Versión de API: `api/v1`

### **Variables Nuevas para Productos:**
- `{{valid_producto_id}}` - ID de producto válido: `1`
- `{{invalid_producto_id}}` - ID de producto inválido: `99999`
- `{{valid_categoria_id}}` - ID de categoría válida: `1`
- `{{invalid_categoria_id}}` - ID de categoría inválida: `99999`
- `{{test_producto_nombre}}` - Nombre de producto de prueba: `Martillo Profesional`
- `{{test_producto_codigo}}` - Código de producto de prueba: `MART001`
- `{{test_producto_descripcion}}` - Descripción de producto de prueba: `Martillo de alta calidad para uso profesional`
- `{{test_producto_imagen}}` - URL de imagen de producto de prueba: `https://ejemplo.com/imagenes/martillo.jpg`

## Ejecución con Newman

```bash
# Ejecutar pruebas de obtener todos los productos
newman run GetAllProductos-Tests.json -e ../config/entorno-desarrollo.json

# Ejecutar pruebas de obtener producto por ID
newman run GetProductoById-Tests.json -e ../config/entorno-desarrollo.json

# Ejecutar pruebas de obtener productos por categoría
newman run GetProductosByCategoria-Tests.json -e ../config/entorno-desarrollo.json

# Ejecutar pruebas de obtener productos destacados
newman run GetProductosDestacados-Tests.json -e ../config/entorno-desarrollo.json

# Ejecutar todas las colecciones de productos
newman run GetAllProductos-Tests.json GetProductoById-Tests.json GetProductosByCategoria-Tests.json GetProductosDestacados-Tests.json -e ../config/entorno-desarrollo.json
```

## Notas Importantes

- Todos los endpoints son de tipo GET (solo lectura)
- Las pruebas incluyen validaciones de headers, query parameters y path variables
- Se prueban casos edge como IDs negativos, decimales, strings, etc.
- Se incluyen pruebas de diferentes tipos de Accept headers
- Se prueban parámetros de query que podrían ser útiles para paginación y filtrado 