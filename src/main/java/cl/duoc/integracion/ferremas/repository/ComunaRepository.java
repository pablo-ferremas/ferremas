package cl.duoc.integracion.ferremas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cl.duoc.integracion.ferremas.entity.Comuna;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComunaRepository extends JpaRepository<Comuna, Long> {
    
    // Buscar comuna por nombre
    Optional<Comuna> findByNombre(String nombre);
    
    // Buscar comuna por nombre ignorando mayúsculas/minúsculas
    Optional<Comuna> findByNombreIgnoreCase(String nombre);
    
    // Verificar si existe una comuna con ese nombre
    boolean existsByNombre(String nombre);
    
    // Buscar todas las comunas de una región específica
    @Query("SELECT c FROM Comuna c WHERE c.regionId = :regionId")
    List<Comuna> findByRegionId(@Param("regionId") Long regionId);
    
    // Buscar comunas por nombre parcial (búsqueda con LIKE)
    @Query("SELECT c FROM Comuna c WHERE c.nombre LIKE %:nombre%")
    List<Comuna> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);
    
    // Contar comunas por región
    @Query("SELECT COUNT(c) FROM Comuna c WHERE c.regionId = :regionId")
    Long countByRegionId(@Param("regionId") Long regionId);
}
