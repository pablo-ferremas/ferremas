package cl.duoc.integracion.ferremas.unitarias.service.PrecioServiceTest;

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

import cl.duoc.integracion.ferremas.entity.Precio;
import cl.duoc.integracion.ferremas.repository.PrecioRepository;
import cl.duoc.integracion.ferremas.service.PrecioService;

@ExtendWith(MockitoExtension.class)
class PrecioServiceTest {

    @Mock
    private PrecioRepository precioRepository;

    @InjectMocks
    private PrecioService precioService;

    private Precio precio1;
    private Precio precio2;

    @BeforeEach
    void setUp() {
        precio1 = new Precio();
        precio1.setId(1);
        precio1.setPrecio(15000.0);

        precio2 = new Precio();
        precio2.setId(2);
        precio2.setPrecio(20000.0);
    }

    @Test
    void testGetAllPrecios() {
        // Arrange
        List<Precio> precios = Arrays.asList(precio1, precio2);
        when(precioRepository.findAll()).thenReturn(precios);

        // Act
        List<Precio> result = precioService.getAllPrecios();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(15000.0, result.get(0).getPrecio());
        assertEquals(20000.0, result.get(1).getPrecio());
        verify(precioRepository, times(1)).findAll();
    }

    @Test
    void testGuardarPrecio() {
        // Arrange
        Precio nuevoPrecio = new Precio();
        nuevoPrecio.setPrecio(25000.0);
        
        when(precioRepository.save(any(Precio.class))).thenReturn(nuevoPrecio);

        // Act
        Precio result = precioService.guardarPrecio(nuevoPrecio);

        // Assert
        assertNotNull(result);
        assertEquals(25000.0, result.getPrecio());
        verify(precioRepository, times(1)).save(nuevoPrecio);
    }

    @Test
    void testEliminarPrecio() {
        // Arrange
        Integer id = 1;
        doNothing().when(precioRepository).deleteById(id);

        // Act
        precioService.eliminarPrecio(id);

        // Assert
        verify(precioRepository, times(1)).deleteById(id);
    }

    @Test
    void testObtenerPrecio_Existe() {
        // Arrange
        Integer id = 1;
        when(precioRepository.findById(id)).thenReturn(Optional.of(precio1));

        // Act
        Precio result = precioService.obtenerPrecio(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(15000.0, result.getPrecio());
        verify(precioRepository, times(1)).findById(id);
    }

    @Test
    void testObtenerPrecio_NoExiste() {
        // Arrange
        Integer id = 999;
        when(precioRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Precio result = precioService.obtenerPrecio(id);

        // Assert
        assertNull(result);
        verify(precioRepository, times(1)).findById(id);
    }
} 