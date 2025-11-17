package cl.duoc.integracion.ferremas.unitarias.service.ProductoServiceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.integracion.ferremas.entity.Producto;
import cl.duoc.integracion.ferremas.repository.ProductoRepository;
import cl.duoc.integracion.ferremas.service.ProductoService;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto1;
    private Producto producto2;

    @BeforeEach
    void setUp() {
        producto1 = new Producto();
        producto1.setId(1);
        producto1.setNombre("Martillo");
        producto1.setDescripcion("Martillo de carpintero");
        producto1.setStock(50);
        producto1.setDestacado(true);

        producto2 = new Producto();
        producto2.setId(2);
        producto2.setNombre("Destornillador");
        producto2.setDescripcion("Destornillador Phillips");
        producto2.setStock(30);
        producto2.setDestacado(false);
    }

    @Test
    void testGetAllProductos() {
        // Arrange
        List<Producto> productos = Arrays.asList(producto1, producto2);
        when(productoRepository.findAll()).thenReturn(productos);

        // Act
        List<Producto> result = productoService.getAllProductos();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Martillo", result.get(0).getNombre());
        assertEquals("Destornillador", result.get(1).getNombre());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void testGuardarProducto() {
        
        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre("Nuevo Producto");
        nuevoProducto.setDescripcion("Descripción del nuevo producto");
        nuevoProducto.setStock(25);
        
        when(productoRepository.save(any(Producto.class))).thenReturn(nuevoProducto);

        Producto result = productoService.guardarProducto(nuevoProducto);

        assertNotNull(result);
        assertEquals("Nuevo Producto", result.getNombre());
        verify(productoRepository, times(1)).save(nuevoProducto);
    }

    @Test
    void testEliminarProducto() {
        // Arrange
        Integer id = 1;
        doNothing().when(productoRepository).deleteById(id);

        // Act
        productoService.eliminarProducto(id);

        // Assert
        verify(productoRepository, times(1)).deleteById(id);
    }

    @Test
    void testObtenerProducto_Existe() {
        // Arrange
        Integer id = 1;
        when(productoRepository.findById(id)).thenReturn(Optional.of(producto1));

        // Act
        Optional<Producto> result = productoService.obtenerProducto(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        assertEquals("Martillo", result.get().getNombre());
        verify(productoRepository, times(1)).findById(id);
    }

    @Test
    void testObtenerProducto_NoExiste() {
        // Arrange
        Integer id = 999;
        when(productoRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Producto> result = productoService.obtenerProducto(id);

        // Assert
        assertFalse(result.isPresent());
        verify(productoRepository, times(1)).findById(id);
    }

    @Test
    void testFindByCategoriaId() {
        // Arrange
        Integer categoriaId = 1;
        List<Producto> productos = Arrays.asList(producto1, producto2);
        when(productoRepository.findByCategoriaId(categoriaId)).thenReturn(productos);

        // Act
        List<Producto> result = productoService.findByCategoriaId(categoriaId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productoRepository, times(1)).findByCategoriaId(categoriaId);
    }

    @Test
    void testFindByDestacadoTrue() {
        // Arrange
        List<Producto> productosDestacados = Arrays.asList(producto1);
        when(productoRepository.findByDestacadoTrue()).thenReturn(productosDestacados);

        // Act
        List<Producto> result = productoService.findByDestacadoTrue();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getDestacado());
        verify(productoRepository, times(1)).findByDestacadoTrue();
    }

    @Test
    void testFindByMarcaId() {
        // Arrange
        Integer marcaId = 1;
        List<Producto> productos = Arrays.asList(producto1, producto2);
        when(productoRepository.findByMarcaId(marcaId)).thenReturn(productos);

        // Act
        List<Producto> result = productoService.findByMarcaId(marcaId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productoRepository, times(1)).findByMarcaId(marcaId);
    }
} 