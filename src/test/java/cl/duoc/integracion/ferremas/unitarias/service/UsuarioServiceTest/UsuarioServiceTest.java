package cl.duoc.integracion.ferremas.unitarias.service.UsuarioServiceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import cl.duoc.integracion.ferremas.entity.Usuario;
import cl.duoc.integracion.ferremas.repository.UsuarioRepository;
import cl.duoc.integracion.ferremas.security.JwtUtil;
import cl.duoc.integracion.ferremas.service.UsuarioService;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private String email = "test@example.com";
    private String password = "password123";

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.setNombre("Test User");
    }

    @Test
    void testRegistrarUsuario() {
        // Arrange
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        Usuario result = usuarioService.registrarUsuario(usuario);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals("Test User", result.getNombre());
        verify(usuarioRepository, times(1)).save(usuario);
        
        // Verificar que la contraseña fue encriptada
        assertNotEquals(password, result.getPassword());
    }

    @Test
    void testValidarCredenciales_CredencialesValidas() {
        // Arrange
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        usuario.setPassword(encodedPassword);
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        // Act
        boolean result = usuarioService.validarCredenciales(email, password);

        // Assert
        assertTrue(result);
        verify(usuarioRepository, times(1)).findByEmail(email);
    }

    @Test
    void testValidarCredenciales_CredencialesInvalidas() {
        // Arrange
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        usuario.setPassword(encodedPassword);
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        // Act
        boolean result = usuarioService.validarCredenciales(email, "wrongpassword");

        // Assert
        assertFalse(result);
        verify(usuarioRepository, times(1)).findByEmail(email);
    }

    @Test
    void testValidarCredenciales_UsuarioNoExiste() {
        // Arrange
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        boolean result = usuarioService.validarCredenciales(email, password);

        // Assert
        assertFalse(result);
        verify(usuarioRepository, times(1)).findByEmail(email);
    }

    @Test
    void testLoginYGenerarToken_CredencialesValidas() {
        // Arrange
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        usuario.setPassword(encodedPassword);
        
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        // Act
        String result = usuarioService.loginYGenerarToken(email, password);

        // Assert
        assertNotNull(result);
        assertTrue(result.length() > 0); // Verificar que se generó un token
        verify(usuarioRepository, times(1)).findByEmail(email);
    }

    @Test
    void testLoginYGenerarToken_CredencialesInvalidas() {
        // Arrange
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        usuario.setPassword(encodedPassword);
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        // Act
        String result = usuarioService.loginYGenerarToken(email, "wrongpassword");

        // Assert
        assertNull(result);
        verify(usuarioRepository, times(1)).findByEmail(email);
    }

    @Test
    void testLoginYGenerarToken_UsuarioNoExiste() {
        // Arrange
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        String result = usuarioService.loginYGenerarToken(email, password);

        // Assert
        assertNull(result);
        verify(usuarioRepository, times(1)).findByEmail(email);
    }
} 