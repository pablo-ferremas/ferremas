package cl.duoc.integracion.ferremas.unitarias.service.BancoCentralServiceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClient;

import cl.duoc.integracion.ferremas.dto.BancoCentralDTO;
import cl.duoc.integracion.ferremas.dto.ExchangeRatesDTO;
import cl.duoc.integracion.ferremas.service.BancoCentralService;
import reactor.core.publisher.Mono;

@SpringBootTest(classes = {BancoCentralService.class})
@TestPropertySource(properties = {
    "spring.main.allow-bean-definition-overriding=true"
})
class BancoCentralServiceTest {

    @MockBean
    private WebClient webClient;

    @SuppressWarnings("rawtypes")
    @MockBean
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @MockBean
    private WebClient.ResponseSpec responseSpec;

    private BancoCentralService bancoCentralService;

    private BancoCentralDTO mockResponse;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        // Crear el servicio usando el constructor de testing
        bancoCentralService = new BancoCentralService(webClient);
        
        // Configurar mocks para WebClient
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void testGetCurrentExchangeRates_Success() {
        // Arrange
        mockResponse = new BancoCentralDTO();
        mockResponse.setCodigo(0);
        mockResponse.setDescripcion("OK");
        
        BancoCentralDTO.Series series = new BancoCentralDTO.Series();
        BancoCentralDTO.Observation observation = new BancoCentralDTO.Observation();
        observation.setStatusCode("OK");
        observation.setValue("850.50");
        observation.setIndexDateString("2024-01-15");
        
        series.setObservations(List.of(observation));
        mockResponse.setSeries(series);
        
        @SuppressWarnings("unchecked")
        Mono<BancoCentralDTO> monoMock = mock(Mono.class);
        when(responseSpec.bodyToMono(BancoCentralDTO.class)).thenReturn(monoMock);
        when(monoMock.block()).thenReturn(mockResponse);

        // Act
        List<ExchangeRatesDTO> result = bancoCentralService.getCurrentExchangeRates();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size()); // USD y EUR
        
        // Verificar que se intentó obtener al menos USD y EUR
        verify(webClient, atLeastOnce()).get();
    }

    @Test
    void testGetCurrentExchangeRates_ErrorResponse() {
        // Arrange
        mockResponse = new BancoCentralDTO();
        mockResponse.setCodigo(1);
        mockResponse.setDescripcion("Error en la consulta");
        
        when(responseSpec.bodyToMono(BancoCentralDTO.class)).thenReturn(
            reactor.core.publisher.Mono.just(mockResponse)
        );

        // Act
        List<ExchangeRatesDTO> result = bancoCentralService.getCurrentExchangeRates();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        // Verificar que se intentó hacer la llamada
        verify(webClient, atLeastOnce()).get();
    }

    @Test
    void testGetCurrentExchangeRates_NullResponse() {
        // Arrange
        when(responseSpec.bodyToMono(BancoCentralDTO.class)).thenReturn(
            reactor.core.publisher.Mono.empty()
        );

        // Act
        List<ExchangeRatesDTO> result = bancoCentralService.getCurrentExchangeRates();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        // Verificar que se intentó hacer la llamada
        verify(webClient, atLeastOnce()).get();
    }

    @Test
    void testGetCurrentExchangeRates_InvalidValue() {
        // Arrange
        mockResponse = new BancoCentralDTO();
        mockResponse.setCodigo(0);
        mockResponse.setDescripcion("OK");
        
        BancoCentralDTO.Series series = new BancoCentralDTO.Series();
        BancoCentralDTO.Observation observation = new BancoCentralDTO.Observation();
        observation.setStatusCode("OK");
        observation.setValue("INVALID_VALUE"); // Valor no numérico
        observation.setIndexDateString("2024-01-15");
        
        series.setObservations(List.of(observation));
        mockResponse.setSeries(series);
        
        when(responseSpec.bodyToMono(BancoCentralDTO.class)).thenReturn(
            reactor.core.publisher.Mono.just(mockResponse)
        );

        // Act
        List<ExchangeRatesDTO> result = bancoCentralService.getCurrentExchangeRates();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        // Verificar que se intentó hacer la llamada
        verify(webClient, atLeastOnce()).get();
    }

    @Test
    void testGetCurrentExchangeRates_NonOKStatusCode() {
        // Arrange
        mockResponse = new BancoCentralDTO();
        mockResponse.setCodigo(0);
        mockResponse.setDescripcion("OK");
        
        BancoCentralDTO.Series series = new BancoCentralDTO.Series();
        BancoCentralDTO.Observation observation = new BancoCentralDTO.Observation();
        observation.setStatusCode("ERROR"); // Status code no OK
        observation.setValue("850.50");
        observation.setIndexDateString("2024-01-15");
        
        series.setObservations(List.of(observation));
        mockResponse.setSeries(series);
        
        when(responseSpec.bodyToMono(BancoCentralDTO.class)).thenReturn(
            reactor.core.publisher.Mono.just(mockResponse)
        );

        // Act
        List<ExchangeRatesDTO> result = bancoCentralService.getCurrentExchangeRates();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        // Verificar que se intentó hacer la llamada
        verify(webClient, atLeastOnce()).get();
    }
}
