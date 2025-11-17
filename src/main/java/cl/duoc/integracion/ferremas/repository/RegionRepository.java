package cl.duoc.integracion.ferremas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.integracion.ferremas.entity.Region;

import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    
    // Buscar región por nombre
    Optional<Region> findByNombre(String nombre);
    
    // Buscar región por nombre ignorando mayúsculas/minúsculas
    Optional<Region> findByNombreIgnoreCase(String nombre);
    
    // Verificar si existe una región con ese nombre
    boolean existsByNombre(String nombre);
}
