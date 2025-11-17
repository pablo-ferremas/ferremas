package cl.duoc.integracion.ferremas.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ItemPedidoResponse {
    private Long id;
    private Long productoId;
    private String nombreProducto;
    private String descripcionProducto;
    private String imagenProducto;
    private String skuProducto;
    private BigDecimal precioUnitario;
    private Integer cantidad;
    private BigDecimal subtotalItem;
}
