package cl.duoc.integracion.ferremas.unitarias.service.MarcaServiceTest;

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

import cl.duoc.integracion.ferremas.entity.Marca;
import cl.duoc.integracion.ferremas.repository.MarcaRepository;
import cl.duoc.integracion.ferremas.service.MarcaService;

@ExtendWith(MockitoExtension.class)
class MarcaServiceTest {

    @Mock
    private MarcaRepository marcaRepository;

    @InjectMocks
    private MarcaService marcaService;

    private Marca marca1;
    private Marca marca2;

    @BeforeEach
    void setUp() {
        marca1 = new Marca();
        marca1.setId(1);
        marca1.setNombre("Stanley");

        marca2 = new Marca();
        marca2.setId(2);
        marca2.setNombre("DeWalt");
    }

    @Test
    void testGetAllMarcas() {
        // Arrange
        List<Marca> marcas = Arrays.asList(marca1, marca2);
        when(marcaRepository.findAll()).thenReturn(marcas);

        // Act
        List<Marca> result = marcaService.getAllMarcas();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Stanley", result.get(0).getNombre());
        assertEquals("DeWalt", result.get(1).getNombre());
        verify(marcaRepository, times(1)).findAll();
    }

    @Test
    void testGuardarMarca() {
        // Arrange
        Marca nuevaMarca = new Marca();
        nuevaMarca.setNombre("Nueva Marca");
        
        when(marcaRepository.save(any(Marca.class))).thenReturn(nuevaMarca);

        // Act
        Marca result = marcaService.guardarMarca(nuevaMarca);

        // Assert
        assertNotNull(result);
        assertEquals("Nueva Marca", result.getNombre());
        verify(marcaRepository, times(1)).save(nuevaMarca);
    }

    @Test
    void testEliminarMarca() {
        // Arrange
        Integer id = 1;
        doNothing().when(marcaRepository).deleteById(id);

        // Act
        marcaService.eliminarMarca(id);

        // Assert
        verify(marcaRepository, times(1)).deleteById(id);
    }

    @Test
    void testObtenerMarca_Existe() {
        // Arrange
        Integer id = 1;
        when(marcaRepository.findById(id)).thenReturn(Optional.of(marca1));

        // Act
        Marca result = marcaService.obtenerMarca(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Stanley", result.getNombre());
        verify(marcaRepository, times(1)).findById(id);
    }

    @Test
    void testObtenerMarca_NoExiste() {
        // Arrange
        Integer id = 999;
        when(marcaRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Marca result = marcaService.obtenerMarca(id);

        // Assert
        assertNull(result);
        verify(marcaRepository, times(1)).findById(id);
    }
} 