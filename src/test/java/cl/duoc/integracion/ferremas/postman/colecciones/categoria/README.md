# Colecciones de Pruebas - CategoriaController

Este directorio contiene las colecciones de Postman para probar el CategoriaController de la API Ferremas.

## Estructura de Archivos

### 📁 Colecciones
- **`GetAllCategorias-Tests.json`** - Pruebas para el endpoint `GET /api/v1/categorias`
- **`PostCategoria-Tests.json`** - Pruebas para el endpoint `POST /api/v1/categorias`
- **`DeleteCategoria-Tests.json`** - Pruebas para el endpoint `DELETE /api/v1/categorias/{id}`

### 📁 Configuración
- **`../config/entorno-desarrollo.json`** - Variables de entorno para desarrollo

## Casos de Prueba Incluidos

### 📋 **GetAllCategorias Tests (10 casos)**
1. Obtener todas las categorías (request básico)
2. Con header Content-Type
3. Con Accept XML
4. Con Accept Text
5. Con User-Agent
6. Con Cache-Control
7. Con Authorization Header
8. Con query parameter inválido
9. Con múltiples query parameters
10. Con URL encoding

### 📝 **PostCategoria Tests (9 casos)**
1. Crear categoría válida
2. Crear categoría duplicada
3. Crear categoría con nombre vacío
4. Crear categoría con nombre largo
5. Crear categoría con caracteres especiales
6. Crear categoría con cuerpo vacío
7. Crear categoría sin Content-Type header
8. Crear categoría con Content-Type XML
9. Crear categoría con Authorization header

### 🗑️ **DeleteCategoria Tests (9 casos)**
1. Eliminar categoría con ID válido
2. Eliminar categoría con ID inválido
3. Eliminar categoría con ID 0
4. Eliminar categoría con ID negativo
5. Eliminar categoría con ID como string
6. Eliminar categoría con ID con caracteres especiales
7. Eliminar categoría con ID con espacios
8. Eliminar categoría con ID decimal
9. Eliminar categoría con ID vacío

## Variables de Entorno Utilizadas

- `{{base_url}}` - URL base: `http://localhost:8090`
- `{{api_version}}` - Versión de API: `api/v1`
- `{{valid_categoria_id}}` - ID de categoría válido: `1`
- `{{invalid_categoria_id}}` - ID de categoría inválido: `99999`
- `{{test_categoria_nombre}}` - Nombre de categoría de prueba: `Herramientas`
- `{{test_categoria_nombre_duplicado}}` - Nombre duplicado: `Herramientas`
- `{{test_categoria_nombre_nuevo}}` - Nombre nuevo: `Electricidad`
- `{{test_categoria_nombre_vacio}}` - Nombre vacío
- `{{test_categoria_nombre_largo}}` - Nombre muy largo
- `{{test_categoria_nombre_especial}}` - Nombre con caracteres especiales

## Ejecución con Newman

```bash
# Ejecutar pruebas de obtener todas las categorías
newman run GetAllCategorias-Tests.json -e ../config/entorno-desarrollo.json

# Ejecutar pruebas de crear categoría
newman run PostCategoria-Tests.json -e ../config/entorno-desarrollo.json

# Ejecutar pruebas de eliminar categoría
newman run DeleteCategoria-Tests.json -e ../config/entorno-desarrollo.json

# Ejecutar todas las colecciones
newman run GetAllCategorias-Tests.json PostCategoria-Tests.json DeleteCategoria-Tests.json -e ../config/entorno-desarrollo.json
``` 