package cl.duoc.integracion.ferremas.repository;

import cl.duoc.integracion.ferremas.entity.Categoria;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Categoria actualizarCategoria(Integer id, Categoria categoria);

    void deleteById(Integer id);

    Optional<Categoria> findById(Integer id);

}
