package cl.duoc.integracion.ferremas.dto;

import lombok.Data;

@Data
public class DireccionBasicaResponse {
    private Long id;
    private String calle;
    private String numero;
    private String complemento;
    private String nombreComuna;
    private String nombreRegion;
}
