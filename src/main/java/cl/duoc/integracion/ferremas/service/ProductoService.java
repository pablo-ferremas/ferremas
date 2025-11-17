package cl.duoc.integracion.ferremas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.duoc.integracion.ferremas.entity.Producto;
import cl.duoc.integracion.ferremas.repository.ProductoRepository;
import cl.duoc.integracion.ferremas.repository.MarcaRepository;
import cl.duoc.integracion.ferremas.repository.CategoriaRepository;
import cl.duoc.integracion.ferremas.dto.ProductoDTO;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    // public Producto actualizarProducto(Integer id, Producto producto) {
    // return productoRepository.actualizarProducto(id, producto);
    // }

    public void eliminarProducto(Integer id) {
        productoRepository.deleteById(id);
    }

    public Optional<Producto> obtenerProducto(Integer id) {
        return productoRepository.findById(id);
    }

    public List<Producto> findByCategoriaId(Integer categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }

    public List<Producto> findByDestacadoTrue() {
        return productoRepository.findByDestacadoTrue();
    }

    public List<Producto> findByMarcaId(Integer marcaId) {
        return productoRepository.findByMarcaId(marcaId);
    }

    public Producto crearProducto(ProductoDTO productoDTO) {
        Producto producto = new Producto();
        producto.setNombre(productoDTO.getNombre());
        producto.setCodigo_producto(productoDTO.getCodigo_producto());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setImagen(productoDTO.getImagen());
        producto.setDescuento(productoDTO.getDescuento());
        producto.setDestacado(productoDTO.getDestacado());
        producto.setOculto(productoDTO.getOculto());
        producto.setStock(productoDTO.getStock());

        // Establecer la categoría
        if (productoDTO.getCategoriaId() != null) {
            categoriaRepository.findById(productoDTO.getCategoriaId())
                .ifPresent(producto::setCategoria);
        }

        // Establecer la marca
        if (productoDTO.getMarcaId() != null) {
            marcaRepository.findById(productoDTO.getMarcaId())
                .ifPresent(producto::setMarca);
        }

        return productoRepository.save(producto);
    }

}
