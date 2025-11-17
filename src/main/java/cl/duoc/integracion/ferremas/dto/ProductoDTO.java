package cl.duoc.integracion.ferremas.dto;

import cl.duoc.integracion.ferremas.entity.Precio;
import cl.duoc.integracion.ferremas.entity.Producto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {

    private Integer id;
    private String codigo_producto;
    private String nombre;
    private String categoria;
    private String marca;
    private List<Precio> precios;
    
    // NUEVOS CAMPOS PARA COMPATIBILIDAD CON FRONTEND
    private String descripcion;   // Descripción del producto
    private String imagen;        // URL de la imagen
    private Integer descuento;    // Porcentaje de descuento
    private Boolean destacado;    // Si está destacado
    private Boolean oculto;       // Si está oculto
    private Integer categoriaId;  // ID de la categoría
    private Integer marcaId;      // ID de la marca
    private Integer stock;        // Stock del producto
    
    // Constructor para mapear desde la entidad Producto
    public ProductoDTO(Producto producto) {
        this.id = producto.getId();
        this.codigo_producto = producto.getCodigo_producto();
        this.nombre = producto.getNombre();
        this.categoria = producto.getCategoria() != null ? producto.getCategoria().getNombre() : null;
        this.marca = producto.getMarca() != null ? producto.getMarca().getNombre() : null;
        this.precios = producto.getPrecios();
        this.stock = producto.getStock();
        
        // Nuevos campos
        this.descripcion = producto.getDescripcion();
        this.imagen = producto.getImagen();
        this.descuento = producto.getDescuento();
        this.destacado = producto.getDestacado();
        this.oculto = producto.getOculto();
        this.categoriaId = producto.getCategoria() != null ? producto.getCategoria().getId() : null;
        this.marcaId = producto.getMarca() != null ? producto.getMarca().getId() : null;
    }
    
}