package cl.duoc.integracion.ferremas.controller;

import cl.duoc.integracion.ferremas.dto.ProductoDTO;
import cl.duoc.integracion.ferremas.entity.Producto;
import cl.duoc.integracion.ferremas.service.ProductoService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @PostMapping
    public ResponseEntity<ProductoDTO> crearProducto(@RequestBody ProductoDTO productoDTO) {
        Producto nuevoProducto = productoService.crearProducto(productoDTO);
        return ResponseEntity.status(201).body(new ProductoDTO(nuevoProducto));
    }

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> getAllProductos() {
        return ResponseEntity.ok(productoService.getAllProductos()
                .stream()
                .map(ProductoDTO::new)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> getProductoById(@PathVariable Integer id) {
        Optional<Producto> producto = productoService.obtenerProducto(id);
        if (producto.isPresent()) {
            return ResponseEntity.ok(new ProductoDTO(producto.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProductoDTO>> getProductosByCategoria(@PathVariable Integer categoriaId) {
        return ResponseEntity.ok(productoService.findByCategoriaId(categoriaId)
                .stream()
                .map(ProductoDTO::new)
                .collect(Collectors.toList()));
    }

    @GetMapping("/destacados")
    public ResponseEntity<List<ProductoDTO>> getProductosDestacados() {
        return ResponseEntity.ok(productoService.findByDestacadoTrue()
                .stream()
                .map(ProductoDTO::new)
                .collect(Collectors.toList()));
    }
}
