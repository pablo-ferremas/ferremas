package cl.duoc.integracion.ferremas.controller;

import cl.duoc.integracion.ferremas.entity.Marca;
import cl.duoc.integracion.ferremas.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/marcas")
public class MarcaController {

    @Autowired
    private MarcaRepository marcaRepository;

    @GetMapping
    public ResponseEntity<List<Marca>> getMarcas(){
        return ResponseEntity.ok(marcaRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Marca> guardarMarca(@RequestBody Marca marca) {
        Optional<Marca> marcaBusqueda = marcaRepository.findOneByNombre(marca.getNombre());
        if(marcaBusqueda.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe una marca con ese nombre.");
        }
        return ResponseEntity.ok(marcaRepository.save(marca));
    }


}
