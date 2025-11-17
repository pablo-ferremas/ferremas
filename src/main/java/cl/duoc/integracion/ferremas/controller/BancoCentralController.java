package cl.duoc.integracion.ferremas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.integracion.ferremas.dto.ExchangeRatesDTO;
import cl.duoc.integracion.ferremas.service.BancoCentralService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/tasas")
@CrossOrigin(origins = "*", allowCredentials = "false") 
@Slf4j
public class BancoCentralController {

    @Autowired
    private BancoCentralService bancoCentralService;

    @GetMapping
    public ResponseEntity<List<ExchangeRatesDTO>> getCurrentExchangeRates() {
        try {
            log.info("Iniciando consulta de tasas de cambio actuales");
            List<ExchangeRatesDTO> rates = bancoCentralService.getCurrentExchangeRates();
            log.info("Tasas obtenidas exitosamente: {}", rates.size());
            return ResponseEntity.ok(rates);
        } catch (Exception e) {
            log.error("Error al obtener tasas de cambio: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}