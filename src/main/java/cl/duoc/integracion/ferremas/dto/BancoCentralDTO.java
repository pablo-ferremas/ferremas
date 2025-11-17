package cl.duoc.integracion.ferremas.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BancoCentralDTO {
    
    @JsonProperty("Codigo")
    private Integer codigo;
    
    @JsonProperty("Descripcion")
    private String descripcion;
    
    @JsonProperty("Series")
    private Series series;
    
    @JsonProperty("SeriesInfos") // Corregido según la respuesta real
    private List<Object> seriesInfos;
    
    @Data
    public static class Series {
        @JsonProperty("descripEsp")
        private String descripcionEspanol;
        
        @JsonProperty("descripIng")
        private String descripcionIngles;
        
        @JsonProperty("seriesId")
        private String seriesId;
        
        @JsonProperty("Obs")
        private List<Observation> observations;
    }

    @Data
    public static class Observation {
        @JsonProperty("indexDateString")
        private String indexDateString;
        
        @JsonProperty("value")
        private String value;
        
        @JsonProperty("statusCode")
        private String statusCode;
    }
}