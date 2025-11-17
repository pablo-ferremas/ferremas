package cl.duoc.integracion.ferremas.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.duoc.integracion.ferremas.dto.LoginResponse;
import cl.duoc.integracion.ferremas.entity.Usuario;
import cl.duoc.integracion.ferremas.entity.Direccion;
import cl.duoc.integracion.ferremas.service.UsuarioService;

@RestController
@RequestMapping("/api/v1/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerUsuarios() {
        return ResponseEntity.ok(usuarioService.obtenerTodosLosUsuarios());
    }

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario usuario) {
        Usuario nuevo = usuarioService.registrarUsuario(usuario);
        return ResponseEntity.ok(nuevo);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        boolean eliminado = usuarioService.eliminarUsuario(id);
        if (eliminado) {
            return ResponseEntity.ok().body("Usuario eliminado correctamente");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuario);
        if (usuarioActualizado != null) {
            return ResponseEntity.ok(usuarioActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        Usuario usuarioCompleto = usuarioService.buscarPorEmail(usuario.getEmail());
        
        String token = usuarioService.loginYGenerarToken(usuario.getEmail(), usuario.getPassword());
        if (token != null && usuarioCompleto != null) {
            LoginResponse.UsuarioDto usuarioDto = new LoginResponse.UsuarioDto(
                usuarioCompleto.getId(),
                usuarioCompleto.getNombre(),
                usuarioCompleto.getEmail(),
                usuarioCompleto.getRol(),
                usuarioCompleto.getTelefono(),
                usuarioCompleto.getDirecciones()
            );
            
            LoginResponse response = new LoginResponse(token, usuarioDto);
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }
    
    /**
     * Agrega una nueva dirección a un usuario
     */
    @PostMapping("/{usuarioId}/direcciones")
    public ResponseEntity<?> agregarDireccion(@PathVariable Long usuarioId, @RequestBody Direccion direccion) {
        try {
            Direccion nuevaDireccion = usuarioService.agregarDireccion(usuarioId, direccion);
            return ResponseEntity.ok(nuevaDireccion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * Obtiene todas las direcciones de un usuario
     */
    @GetMapping("/{usuarioId}/direcciones")
    public ResponseEntity<List<Direccion>> obtenerDireccionesUsuario(@PathVariable Long usuarioId) {
        List<Direccion> direcciones = usuarioService.obtenerDireccionesUsuario(usuarioId);
        return ResponseEntity.ok(direcciones);
    }
    
    /**
     * Elimina una dirección específica de un usuario
     */
    @DeleteMapping("/{usuarioId}/direcciones/{direccionId}")
    public ResponseEntity<?> eliminarDireccionUsuario(@PathVariable Long usuarioId, @PathVariable Long direccionId) {
        boolean eliminado = usuarioService.eliminarDireccionUsuario(usuarioId, direccionId);
        if (eliminado) {
            return ResponseEntity.ok().body("Dirección eliminada correctamente");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}