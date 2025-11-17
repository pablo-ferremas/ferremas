package cl.duoc.integracion.ferremas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cl.duoc.integracion.ferremas.entity.Direccion;

import java.util.List;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Long> {
    
    /**
     * Busca direcciones por ID de usuario
     */
    List<Direccion> findByUsuarioId(Long usuarioId);
    
    /**
     * Busca direcciones por región
     */
    List<Direccion> findByRegionId(Long regionId);
    
    /**
     * Busca direcciones por comuna
     */
    List<Direccion> findByComunaId(Long comunaId);
    
    /**
     * Busca direcciones por calle (búsqueda parcial)
     */
    List<Direccion> findByCalleContainingIgnoreCase(String calle);
    
    /**
     * Busca direcciones por región y comuna
     */
    @Query("SELECT d FROM Direccion d WHERE d.region.id = :regionId AND d.comuna.id = :comunaId")
    List<Direccion> findByRegionAndComuna(@Param("regionId") Long regionId, @Param("comunaId") Long comunaId);
    
    /**
     * Cuenta direcciones por usuario
     */
    long countByUsuarioId(Long usuarioId);
}
