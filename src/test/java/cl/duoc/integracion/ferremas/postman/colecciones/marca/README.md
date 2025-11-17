# Colecciones de Pruebas - MarcaController

Este directorio contiene las colecciones de Postman para probar el MarcaController de la API Ferremas.

## Estructura de Archivos

### 📁 Colecciones
- **`GetAllMarcas-Tests.json`** - Pruebas para el endpoint `GET /api/v1/marcas`
- **`PostMarca-Tests.json`** - Pruebas para el endpoint `POST /api/v1/marcas`

### 📁 Configuración
- **`../config/entorno-desarrollo.json`** - Variables de entorno para desarrollo

## Casos de Prueba Incluidos

### 📋 **GetAllMarcas Tests (10 casos)**
1. Obtener todas las marcas (request básico)
2. Con header Content-Type
3. Con Accept XML
4. Con Accept Text
5. Con User-Agent
6. Con Cache-Control
7. Con Authorization Header
8. Con query parameter inválido
9. Con múltiples query parameters
10. Con URL encoding

### 📝 **PostMarca Tests (9 casos)**
1. Crear marca válida
2. Crear marca duplicada
3. Crear marca con nombre vacío
4. Crear marca con nombre largo
5. Crear marca con caracteres especiales
6. Crear marca con cuerpo vacío
7. Crear marca sin Content-Type header
8. Crear marca con Content-Type XML
9. Crear marca con Authorization header

## Variables de Entorno Utilizadas

- `{{base_url}}` - URL base: `http://localhost:8090`
- `{{api_version}}` - Versión de API: `api/v1`
- `{{valid_marca_id}}` - ID de marca válido: `1`
- `{{invalid_marca_id}}` - ID de marca inválido: `99999`
- `{{test_marca_nombre}}` - Nombre de marca de prueba: `Stanley`
- `{{test_marca_nombre_duplicado}}` - Nombre duplicado: `Stanley`
- `{{test_marca_nombre_nuevo}}` - Nombre nuevo: `Bosch`
- `{{test_marca_nombre_vacio}}` - Nombre vacío
- `{{test_marca_nombre_largo}}` - Nombre muy largo
- `{{test_marca_nombre_especial}}` - Nombre con caracteres especiales

## Ejecución con Newman

```bash
# Ejecutar pruebas de obtener todas las marcas
newman run GetAllMarcas-Tests.json -e ../config/entorno-desarrollo.json

# Ejecutar pruebas de crear marca
newman run PostMarca-Tests.json -e ../config/entorno-desarrollo.json

# Ejecutar ambas colecciones
newman run GetAllMarcas-Tests.json PostMarca-Tests.json -e ../config/entorno-desarrollo.json
``` 