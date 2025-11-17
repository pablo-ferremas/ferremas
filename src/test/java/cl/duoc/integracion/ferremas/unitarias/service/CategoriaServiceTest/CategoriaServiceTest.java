package cl.duoc.integracion.ferremas.unitarias.service.CategoriaServiceTest;

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

import cl.duoc.integracion.ferremas.entity.Categoria;
import cl.duoc.integracion.ferremas.repository.CategoriaRepository;
import cl.duoc.integracion.ferremas.service.CategoriaService;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoria1;
    private Categoria categoria2;

    @BeforeEach
    void setUp() {
        categoria1 = new Categoria();
        categoria1.setId(1);
        categoria1.setNombre("Herramientas Manuales");

        categoria2 = new Categoria();
        categoria2.setId(2);
        categoria2.setNombre("Herramientas Eléctricas");
    }

    @Test
    void testGetAllCategorias() {
        // Arrange
        List<Categoria> categorias = Arrays.asList(categoria1, categoria2);
        when(categoriaRepository.findAll()).thenReturn(categorias);

        // Act
        List<Categoria> result = categoriaService.getAllCategorias();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Herramientas Manuales", result.get(0).getNombre());
        assertEquals("Herramientas Eléctricas", result.get(1).getNombre());
        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    void testGuardarCategoria() {
        // Arrange
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre("Nueva Categoría");
        
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(nuevaCategoria);

        // Act
        Categoria result = categoriaService.guardarCategoria(nuevaCategoria);

        // Assert
        assertNotNull(result);
        assertEquals("Nueva Categoría", result.getNombre());
        verify(categoriaRepository, times(1)).save(nuevaCategoria);
    }

    @Test
    void testEliminarCategoria() {
        // Arrange
        Integer id = 1;
        doNothing().when(categoriaRepository).deleteById(id);

        // Act
        categoriaService.eliminarCategoria(id);

        // Assert
        verify(categoriaRepository, times(1)).deleteById(id);
    }

    @Test
    void testObtenerCategoria_Existe() {
        // Arrange
        Integer id = 1;
        when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoria1));

        // Act
        Categoria result = categoriaService.obtenerCategoria(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Herramientas Manuales", result.getNombre());
        verify(categoriaRepository, times(1)).findById(id);
    }

    @Test
    void testObtenerCategoria_NoExiste() {
        // Arrange
        Integer id = 999;
        when(categoriaRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Categoria result = categoriaService.obtenerCategoria(id);

        // Assert
        assertNull(result);
        verify(categoriaRepository, times(1)).findById(id);
    }
} 