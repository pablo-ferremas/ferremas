package cl.duoc.integracion.ferremas.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import cl.duoc.integracion.ferremas.dto.BancoCentralDTO;
import cl.duoc.integracion.ferremas.dto.ExchangeRatesDTO;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BancoCentralService {

    private final WebClient webClient; 
    private static final String BASE_URL = "https://si3.bcentral.cl/SieteRestWS/SieteRestWS.ashx";
    private static final String USER = "pa.gutierrezu@duocuc.cl";
    private static final String PASSWORD = "Oracle.123456";
    private static final String USD_SERIES = "F073.TCO.PRE.Z.D"; 
    private static final String EUR_SERIES = "F072.CLP.EUR.N.O.D"; 

    public BancoCentralService() {
        this.webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .build();
    }
    
    // Constructor para testing
    public BancoCentralService(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<ExchangeRatesDTO> getCurrentExchangeRates() {
        List<ExchangeRatesDTO> rates = new ArrayList<>();
        String dateToUse = getValidBusinessDate();
        //String dateToUse = "2025-05-30";
        
        try {
            // Obtener USD - Corregido: USD_SERIES
            ExchangeRatesDTO usdRate = getExchangeRate("USD", USD_SERIES, dateToUse);
            if (usdRate != null) {
                rates.add(usdRate);
            }
            
            // Obtener EUR - Corregido: EUR_SERIES
            ExchangeRatesDTO eurRate = getExchangeRate("EUR", EUR_SERIES, dateToUse);
            if (eurRate != null) {
                rates.add(eurRate);
            }
            
        } catch (Exception e) {
            log.error("Error al obtener tipos de cambio: ", e);
        }
        
        return rates;
    }
    
    /**
     * Obtiene una fecha válida para consultar la API del Banco Central.
     * Si la fecha actual es sábado o domingo, retorna el viernes más cercano.
     * Para los demás días de la semana, retorna la fecha actual.
     */
    private String getValidBusinessDate() {
        LocalDate today = LocalDate.now();
        LocalDate dateToUse = today;
        
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        
        if (dayOfWeek == DayOfWeek.SATURDAY) {
            // Si es sábado, usar el viernes anterior (restar 1 día)
            dateToUse = today.minusDays(1);
            log.info("Es sábado, usando el viernes anterior: {}", dateToUse);
        } else if (dayOfWeek == DayOfWeek.SUNDAY) {
            // Si es domingo, usar el viernes anterior (restar 2 días)
            dateToUse = today.minusDays(2);
            log.info("Es domingo, usando el viernes anterior: {}", dateToUse);
        } else {
            log.info("Día de semana válido, usando fecha actual: {}", dateToUse);
        }
        
        return dateToUse.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    
    private ExchangeRatesDTO getExchangeRate(String currency, String series, String date) {
        try {
            log.info("Obteniendo tipo de cambio para {} con serie {}", currency, series);
            
            BancoCentralDTO response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("user", USER)
                            .queryParam("pass", PASSWORD)
                            .queryParam("firstdate", date)
                            .queryParam("lastdate", date)
                            .queryParam("timeseries", series)
                            .build())
                    .retrieve()
                    .bodyToMono(BancoCentralDTO.class)
                    .block();
            
            if (response != null && response.getCodigo() == 0) {
                log.info("Respuesta exitosa: {}", response.getDescripcion());
                
                if (response.getSeries() != null && 
                    response.getSeries().getObservations() != null && 
                    !response.getSeries().getObservations().isEmpty()) {
                    
                    BancoCentralDTO.Observation obs = response.getSeries().getObservations().get(0);
                    
                    if ("OK".equals(obs.getStatusCode())) {
                        try {
                            Double rateValue = Double.parseDouble(obs.getValue());
                            return new ExchangeRatesDTO(currency, rateValue, obs.getIndexDateString());
                        } catch (NumberFormatException e) {
                            log.error("Error al convertir el valor a número: {}", obs.getValue());
                        }
                    } else {
                        log.warn("StatusCode no es OK para {}: {}", currency, obs.getStatusCode());
                    }
                } else {
                    log.warn("No se encontraron observaciones para {}", currency);
                }
            } else {
                log.error("Error en la respuesta del Banco Central. Código: {}, Descripción: {}", 
                         response != null ? response.getCodigo() : "null",
                         response != null ? response.getDescripcion() : "Respuesta nula");
            }
            
        } catch (Exception e) {
            log.error("Error al obtener tipo de cambio para {}: ", currency, e);
        }
        
        return null;
    }
}