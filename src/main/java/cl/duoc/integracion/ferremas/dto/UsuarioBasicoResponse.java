package cl.duoc.integracion.ferremas.dto;

import lombok.Data;

@Data
public class UsuarioBasicoResponse {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
}
