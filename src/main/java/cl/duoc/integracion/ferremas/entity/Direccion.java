package cl.duoc.integracion.ferremas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "direcciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String calle;

    @Column(nullable = false)
    private String numero;

    // Esta columna será manejada por la relación unidireccional desde Usuario
    @Column(name = "usuario_id", insertable = false, updatable = false)
    private Long usuarioId;

    @ManyToOne(fetch = FetchType.EAGER)  // Cambio a EAGER para obtener los datos
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @ManyToOne(fetch = FetchType.EAGER)  // Cambio a EAGER para obtener los datos
    @JoinColumn(name = "comuna_id", nullable = false)
    private Comuna comuna;

}
