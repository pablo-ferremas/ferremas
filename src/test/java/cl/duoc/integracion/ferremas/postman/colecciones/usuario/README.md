# Colecciones de Pruebas - UsuarioController

Este directorio contiene las colecciones de Postman para probar el UsuarioController de la API Ferremas.

## Estructura de Archivos

### 📁 Colecciones
- **`Login-Tests.json`** - Pruebas para el endpoint `/api/v1/usuarios/login`
- **`Registro-Tests.json`** - Pruebas para el endpoint `/api/v1/usuarios/registro`

### 📁 Configuración
- **`../config/entorno-desarrollo.json`** - Variables de entorno para desarrollo

## Casos de Prueba Incluidos

### 🔐 Login Tests (12 casos)
1. **Credenciales Correctas** - Usuario y contraseña válidos
2. **Credenciales Incorrectas** - Usuario y contraseña inválidos
3. **Email Vacío** - Email vacío, contraseña válida
4. **Password Vacío** - Email válido, contraseña vacía
5. **Email Correcto, Password Incorrecto** - Email válido, contraseña inválida
6. **Email Incorrecto, Password Correcto** - Email inválido, contraseña válida
7. **Email con Formato Inválido** - Email sin formato válido
8. **Password Muy Corto** - Contraseña de solo 3 caracteres
9. **Cuerpo de Request Vacío** - JSON vacío
10. **Sin Content-Type Header** - Sin header de contenido
11. **Email con Espacios** - Email con espacios al inicio y final
12. **Password con Espacios** - Contraseña con espacios al inicio y final

### 📝 Registro Tests (14 casos)
1. **Datos Válidos** - Registro con todos los campos correctos
2. **Email Duplicado** - Intentar registrar email ya existente
3. **Nombre Vacío** - Nombre vacío, otros campos válidos
4. **Email Vacío** - Email vacío, otros campos válidos
5. **Password Vacío** - Contraseña vacía, otros campos válidos
6. **Email con Formato Inválido** - Email sin formato válido
7. **Sin Rol** - Registro sin especificar rol (debería usar default)
8. **Cuerpo de Request Vacío** - JSON vacío
9. **Nombre Muy Largo** - Nombre extremadamente largo
10. **Password Muy Corto** - Contraseña de solo 3 caracteres
11. **Rol Inválido** - Rol que no existe en el sistema
12. **Caracteres Especiales en Nombre** - Nombre con acentos y caracteres especiales
13. **Email con Subdominio** - Email con estructura compleja
14. **Sin Content-Type Header** - Sin header de contenido

## Variables de Entorno Utilizadas

- `{{base_url}}` - URL base: `http://localhost:8090`
- `{{api_version}}` - Versión de API: `api/v1`
- `{{valid_email}}` - Email válido: `pablo@mail.com`
- `{{valid_password}}` - Contraseña válida: `macoy123`
- `{{invalid_email}}` - Email inválido: `usuario_inexistente@mail.com`
- `{{invalid_password}}` - Contraseña inválida: `password_incorrecto`
- `{{test_nombre}}` - Nombre de prueba: `Usuario Test`
- `{{test_email}}` - Email de prueba: `test@mail.com`
- `{{test_password}}` - Contraseña de prueba: `test123`

## Ejecución con Newman

```bash
# Ejecutar pruebas de login
newman run Login-Tests.json -e ../config/entorno-desarrollo.json

# Ejecutar pruebas de registro
newman run Registro-Tests.json -e ../config/entorno-desarrollo.json

# Ejecutar ambas colecciones
newman run Login-Tests.json Registro-Tests.json -e ../config/entorno-desarrollo.json
``` 