package cl.duoc.integracion.ferremas.repository;

import cl.duoc.integracion.ferremas.entity.Marca;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Integer> {
    Optional<Marca> findOneByNombre(String nombre);

    // Marca actualizarMarca(Integer id, Marca marca);
}
