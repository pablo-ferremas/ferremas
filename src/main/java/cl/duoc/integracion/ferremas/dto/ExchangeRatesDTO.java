package cl.duoc.integracion.ferremas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRatesDTO {
    private String currency;
    private Double rate;
    private String date;
}
