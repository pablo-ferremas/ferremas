package cl.duoc.integracion.ferremas.controller;
import cl.duoc.integracion.ferremas.entity.Precio;
import cl.duoc.integracion.ferremas.repository.PrecioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/precios")
public class PrecioController {

    @Autowired
    private PrecioRepository precioRepository;

    @GetMapping
    public ResponseEntity<List<Precio>> listarPrecios() {
        try {
            return ResponseEntity.ok(precioRepository.findAll());
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @PostMapping
    public ResponseEntity<Precio> guardarPrecio(@RequestBody Precio precio) {
        try{
            return ResponseEntity.ok(precioRepository.save(precio));
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
