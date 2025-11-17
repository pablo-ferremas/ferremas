package cl.duoc.integracion.ferremas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@AllArgsConstructor
@Entity
@Table(name="productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "codigo_producto")
    private String codigo_producto;

    @Column(name = "nombre")
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "imagen_url")
    private String imagen;
    
    @Column(name = "descuento_porcentaje")
    private Integer descuento;  // Porcentaje de descuento (0-100)
    
    @Column(name = "es_destacado")
    private Boolean destacado = false;  // Si el producto está destacado
    
    @Column(name = "esta_oculto")
    private Boolean oculto = false;

    @Column(name = "stock")
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "marca_id")
    private Marca marca;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Precio> precios = new ArrayList<>();

    public Producto() {
        this.precios = new ArrayList<>();
        this.destacado = false;
        this.oculto = false;
    }

    public Integer getCategoriaId() {
        return categoria != null ? categoria.getId() : null;
    }

}
