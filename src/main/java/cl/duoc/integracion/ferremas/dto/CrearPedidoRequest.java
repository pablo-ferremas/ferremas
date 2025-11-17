package cl.duoc.integracion.ferremas.dto;

import cl.duoc.integracion.ferremas.entity.TipoEnvio;
import lombok.Data;

import java.util.List;

@Data
public class CrearPedidoRequest {
    
    private UsuarioRequest usuario;
    private DireccionRequest direccionEntrega;
    private TipoEnvio tipoEnvio;
    private String numeroOrden;
    private List<ItemPedidoRequest> items;
    
    @Data
    public static class UsuarioRequest {
        private Long id;
    }
    
    @Data
    public static class DireccionRequest {
        private Long id;
    }
    
    @Data
    public static class ItemPedidoRequest {
        private ProductoRequest producto;
        private Integer cantidad;
        private java.math.BigDecimal precioUnitario;
        private String nombreProducto;
        private String descripcionProducto;
        private String imagenProducto;
        private String skuProducto;
    }
    
    @Data
    public static class ProductoRequest {
        private Integer id;
    }
}
