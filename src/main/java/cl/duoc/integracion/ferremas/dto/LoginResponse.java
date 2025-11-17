package cl.duoc.integracion.ferremas.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

import java.util.List;

import cl.duoc.integracion.ferremas.entity.Direccion;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private UsuarioDto usuario;
    
    @Data
    @AllArgsConstructor
    public static class UsuarioDto {
        private Long id;
        private String nombre;
        private String email;
        private String rol;
        private String telefono;
        private List<Direccion> direcciones;
    }
}
