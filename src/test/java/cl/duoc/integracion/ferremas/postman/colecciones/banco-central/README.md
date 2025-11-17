# BancoCentralController - Pruebas Postman

Este directorio contiene las colecciones de pruebas para el `BancoCentralController`, que maneja la integración con la API externa del Banco Central de Chile para obtener tasas de cambio.

## Estructura del Controlador

El `BancoCentralController` expone un endpoint que consume datos de la API externa del Banco Central:

- **Base URL**: `/api/v1/tasas`
- **Método**: GET
- **Descripción**: Obtiene las tasas de cambio actuales (USD y EUR) desde la API del Banco Central

## DTOs Utilizados

### ExchangeRatesDTO
```json
{
  "currency": "string",  // Moneda (USD, EUR)
  "rate": "number",      // Tasa de cambio
  "date": "string"       // Fecha de la tasa
}
```

### BancoCentralDTO
DTO interno para mapear la respuesta de la API externa del Banco Central.

## Colecciones de Pruebas

### 1. GetExchangeRates-Tests.json
Colección completa de pruebas para el endpoint GET de tasas de cambio.

#### Casos de Prueba Incluidos:

**Pruebas Exitosas:**
- GET - Obtener tasas de cambio exitoso
- GET - Obtener tasas con Accept header específico
- GET - Obtener tasas sin headers
- GET - Obtener tasas con User-Agent personalizado
- GET - Obtener tasas con Cache-Control
- GET - Obtener tasas con Accept-Language
- GET - Obtener tasas con Accept */*
- GET - Obtener tasas con múltiples headers

**Pruebas de Headers:**
- GET - Obtener tasas con Authorization header (debe funcionar ya que no requiere auth)
- GET - Obtener tasas con Content-Type incorrecto
- GET - Obtener tasas con Accept XML (debe devolver JSON)
- GET - Obtener tasas con If-Modified-Since
- GET - Obtener tasas con If-None-Match
- GET - Obtener tasas con X-Requested-With
- GET - Obtener tasas con Origin header (CORS)
- GET - Obtener tasas con Referer header

#### Validaciones Implementadas:

1. **Código de Estado**: Verifica que la respuesta sea 200 OK
2. **Estructura de Respuesta**: Valida que sea un array de objetos
3. **Propiedades**: Verifica que cada objeto tenga `currency`, `rate`, y `date`
4. **Tipos de Datos**: Valida que `currency` y `date` sean strings, `rate` sea number
5. **Monedas Válidas**: Verifica que las monedas sean USD o EUR
6. **Tasas Positivas**: Valida que las tasas sean mayores a 0
7. **Tiempo de Respuesta**: Verifica que sea menor a 5000ms
8. **Headers de Respuesta**: Valida Content-Type application/json
9. **CORS**: Verifica headers de CORS cuando se incluye Origin

## Variables de Entorno Requeridas

Asegúrate de que las siguientes variables estén configuradas en tu entorno de Postman:

```json
{
  "base_url": "http://localhost:8090",
  "api_version": "api/v1",
  "banco_central_endpoint": "tasas",
  "test_fecha_banco_central": "2025-05-30",
  "test_fecha_banco_central_invalida": "2025-13-45",
  "test_fecha_banco_central_futura": "2030-01-01",
  "test_fecha_banco_central_pasada": "2020-01-01"
}
```

## Características Especiales

### Integración con API Externa
- El controlador consume datos de la API del Banco Central de Chile
- Utiliza WebClient para realizar peticiones HTTP
- Maneja errores de la API externa y los convierte en respuestas apropiadas

### Sin Autenticación
- El endpoint no requiere autenticación
- Las pruebas verifican que funcione correctamente sin tokens

### CORS Habilitado
- El controlador tiene `@CrossOrigin(origins = "*")`
- Las pruebas verifican que los headers CORS estén presentes

### Manejo de Errores
- El controlador maneja excepciones y devuelve 500 Internal Server Error
- Las pruebas están diseñadas para manejar tanto respuestas exitosas como errores

## Ejecución de Pruebas

### Con Postman
1. Importa la colección `GetExchangeRates-Tests.json`
2. Configura el entorno con las variables requeridas
3. Ejecuta las pruebas individualmente o en conjunto

### Con Newman (CLI)
```bash
# Ejecutar todas las pruebas del controlador
newman run "GetExchangeRates-Tests.json" -e "entorno-desarrollo.json"

# Ejecutar con reporte HTML
newman run "GetExchangeRates-Tests.json" -e "entorno-desarrollo.json" --reporters html --reporter-html-export "reporte-banco-central.html"

# Ejecutar con reporte JSON
newman run "GetExchangeRates-Tests.json" -e "entorno-desarrollo.json" --reporters json --reporter-json-export "reporte-banco-central.json"
```

### Con Newman en CI/CD
```bash
# Ejecutar en modo silencioso para CI/CD
newman run "GetExchangeRates-Tests.json" -e "entorno-desarrollo.json" --silent

# Ejecutar con salida detallada
newman run "GetExchangeRates-Tests.json" -e "entorno-desarrollo.json" --verbose
```

## Consideraciones Importantes

### Dependencia de API Externa
- Las pruebas dependen de la disponibilidad de la API del Banco Central
- Los datos pueden variar según la fecha configurada en el servicio
- Considera implementar mocks para pruebas unitarias

### Fecha Hardcodeada
- El servicio usa una fecha fija (`2025-05-30`) para las pruebas
- En producción, debería usar la fecha actual

### Credenciales
- El servicio incluye credenciales hardcodeadas para la API del Banco Central
- En producción, estas deberían estar en variables de entorno

### Monedas Soportadas
- Actualmente solo soporta USD y EUR
- Las pruebas validan específicamente estas monedas

## Estructura de Archivos

```
banco-central/
├── GetExchangeRates-Tests.json    # Colección principal de pruebas
└── README.md                      # Este archivo de documentación
```

## Notas de Desarrollo

- Las pruebas están diseñadas para ser robustas y cubrir casos edge
- Se incluyen validaciones tanto de éxito como de manejo de errores
- Las pruebas verifican la integración completa con la API externa
- Se consideran aspectos de CORS y headers HTTP
- Las validaciones incluyen tipos de datos y rangos válidos 