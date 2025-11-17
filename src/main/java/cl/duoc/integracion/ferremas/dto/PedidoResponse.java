package cl.duoc.integracion.ferremas.dto;

import cl.duoc.integracion.ferremas.entity.EstadoPedido;
import cl.duoc.integracion.ferremas.entity.TipoEnvio;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoResponse {
    private Long id;
    private String numeroOrden;
    private EstadoPedido estado;
    private TipoEnvio tipoEnvio;
    private BigDecimal subtotal;
    private BigDecimal costoEnvio;
    private BigDecimal total;
    private LocalDateTime fechaCreacion;
    
    // Información básica del usuario (sin relaciones)
    private UsuarioBasicoResponse usuario;
    
    // Información básica de la dirección (sin relaciones anidadas)
    private DireccionBasicaResponse direccionEntrega;
    
    // Items del pedido (sin referencias al pedido padre)
    private List<ItemPedidoResponse> items;
}
