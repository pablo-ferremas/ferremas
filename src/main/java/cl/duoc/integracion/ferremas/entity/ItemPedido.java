package cl.duoc.integracion.ferremas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "items_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con Pedido
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    // Información del producto (snapshot al momento del pedido)
    @Column(nullable = false)
    private Long productoId;
    
    @Column(nullable = false)
    private String nombreProducto;
    
    @Column(nullable = false)
    private BigDecimal precioUnitario;
    
    @Column(nullable = false)
    private Integer cantidad;
    
    @Column(nullable = false)
    private BigDecimal subtotalItem; // precioUnitario * cantidad

    // Información adicional del producto al momento del pedido
    private String descripcionProducto;
    private String imagenProducto;
    private String skuProducto;
}
