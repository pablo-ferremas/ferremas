package cl.duoc.integracion.ferremas.repository;

import cl.duoc.integracion.ferremas.entity.Producto;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    // Producto actualizarProducto(Integer id, Producto producto);

    @Query("SELECT p FROM Producto p WHERE p.categoria.id = :categoriaId")
    List<Producto> findByCategoriaId(@Param("categoriaId") Integer categoriaId);

    @Query("SELECT p FROM Producto p WHERE p.marca.id = :marcaId")
    List<Producto> findByMarcaId(@Param("marcaId") Integer marcaId);
    
    List<Producto> findByDestacadoTrue();

}
