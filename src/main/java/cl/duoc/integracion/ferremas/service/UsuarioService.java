package cl.duoc.integracion.ferremas.service;

import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import cl.duoc.integracion.ferremas.entity.Usuario;
import cl.duoc.integracion.ferremas.entity.Direccion;
import cl.duoc.integracion.ferremas.repository.UsuarioRepository;
import cl.duoc.integracion.ferremas.security.JwtUtil;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @SuppressWarnings("unused")
    private JwtUtil jwtUtil;

    public UsuarioService(UsuarioRepository usuarioRepository, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario registrarUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    public boolean validarCredenciales(String email, String password) {
        return usuarioRepository.findByEmail(email)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public boolean eliminarUsuario(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        return usuarioRepository.findById(id)
            .map(usuarioExistente -> {
                // Actualizar solo los campos que no son null
                if (usuarioActualizado.getNombre() != null) {
                    usuarioExistente.setNombre(usuarioActualizado.getNombre());
                }
                if (usuarioActualizado.getEmail() != null) {
                    usuarioExistente.setEmail(usuarioActualizado.getEmail());
                }
                if (usuarioActualizado.getPassword() != null) {
                    usuarioExistente.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
                }
                if (usuarioActualizado.getRol() != null) {
                    usuarioExistente.setRol(usuarioActualizado.getRol());
                }
                if (usuarioActualizado.getTelefono() != null) {
                    usuarioExistente.setTelefono(usuarioActualizado.getTelefono());
                }
                // Actualizar direcciones si se proporcionan
                if (usuarioActualizado.getDirecciones() != null) {
                    // Limpiar direcciones existentes
                    usuarioExistente.getDirecciones().clear();
                    // Agregar las nuevas direcciones
                    usuarioExistente.getDirecciones().addAll(usuarioActualizado.getDirecciones());
                }
                return usuarioRepository.save(usuarioExistente);
            })
            .orElse(null);
    }

    public String loginYGenerarToken(String email, String password) {
        return usuarioRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> JwtUtil.generateToken(user.getEmail())) // ✅ llamado al método estático
                .orElse(null);
    }

    /**
     * Agrega una dirección a un usuario específico
     */
    public Direccion agregarDireccion(Long usuarioId, Direccion direccion) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            
            // Agregar la dirección a la lista del usuario
            usuario.getDirecciones().add(direccion);
            
            // Guardar el usuario (esto también guardará la dirección por cascada)
            Usuario usuarioGuardado = usuarioRepository.save(usuario);
            
            // Devolver la última dirección agregada
            List<Direccion> direcciones = usuarioGuardado.getDirecciones();
            return direcciones.get(direcciones.size() - 1);
        } else {
            throw new RuntimeException("Usuario no encontrado con ID: " + usuarioId);
        }
    }

    /**
     * Obtiene todas las direcciones de un usuario
     */
    public List<Direccion> obtenerDireccionesUsuario(Long usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        
        if (usuarioOpt.isPresent()) {
            return usuarioOpt.get().getDirecciones();
        } else {
            throw new RuntimeException("Usuario no encontrado con ID: " + usuarioId);
        }
    }

    /**
     * Elimina una dirección específica de un usuario
     */
    public boolean eliminarDireccionUsuario(Long usuarioId, Long direccionId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            
            // Buscar y eliminar la dirección
            boolean eliminado = usuario.getDirecciones().removeIf(direccion -> direccion.getId().equals(direccionId));
            
            if (eliminado) {
                usuarioRepository.save(usuario);
                return true;
            }
        }
        
        return false;
    }

}