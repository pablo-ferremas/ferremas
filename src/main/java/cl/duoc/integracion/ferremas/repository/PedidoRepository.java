package cl.duoc.integracion.ferremas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cl.duoc.integracion.ferremas.entity.Pedido;
import cl.duoc.integracion.ferremas.entity.EstadoPedido;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    /**
     * Busca pedidos por ID de usuario
     */
    List<Pedido> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId);
    
    /**
     * Busca pedidos por estado
     */
    List<Pedido> findByEstadoOrderByFechaCreacionDesc(EstadoPedido estado);
    
    /**
     * Busca pedidos por usuario y estado
     */
    List<Pedido> findByUsuarioIdAndEstadoOrderByFechaCreacionDesc(Long usuarioId, EstadoPedido estado);
    
    /**
     * Busca pedido por número de orden
     */
    Optional<Pedido> findByNumeroOrden(String numeroOrden);
    
    /**
     * Busca pedidos en un rango de fechas
     */
    @Query("SELECT p FROM Pedido p WHERE p.fechaCreacion BETWEEN :fechaInicio AND :fechaFin ORDER BY p.fechaCreacion DESC")
    List<Pedido> findByFechaCreacionBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                           @Param("fechaFin") LocalDateTime fechaFin);
    
    /**
     * Busca pedidos por tipo de envío
     */
    List<Pedido> findByTipoEnvioOrderByFechaCreacionDesc(cl.duoc.integracion.ferremas.entity.TipoEnvio tipoEnvio);
    
    /**
     * Cuenta pedidos por usuario
     */
    long countByUsuarioId(Long usuarioId);
    
    /**
     * Cuenta pedidos por estado
     */
    long countByEstado(EstadoPedido estado);
    
    /**
     * Busca pedidos recientes (últimos N días)
     */
    @Query("SELECT p FROM Pedido p WHERE p.fechaCreacion >= :fechaLimite ORDER BY p.fechaCreacion DESC")
    List<Pedido> findPedidosRecientes(@Param("fechaLimite") LocalDateTime fechaLimite);
}
