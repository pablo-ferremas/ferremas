package cl.duoc.integracion.ferremas.repository;

import cl.duoc.integracion.ferremas.entity.Precio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrecioRepository extends JpaRepository<Precio, Long> {

    // Precio actualizarPrecio(Integer id, Precio precio);

    void deleteById(Integer id);

    Optional<Precio> findById(Integer id);
}
