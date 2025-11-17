package cl.duoc.integracion.ferremas.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.duoc.integracion.ferremas.entity.Comuna;
import cl.duoc.integracion.ferremas.service.ComunaService;

@RestController
@RequestMapping("/api/v1/comunas")
@CrossOrigin(origins = "*")
public class ComunaController {

    @Autowired
    private ComunaService comunaService;

    @GetMapping
    public ResponseEntity<List<Comuna>> obtenerTodasLasComunas() {
        List<Comuna> comunas = comunaService.obtenerTodasLasComunas();
        if (comunas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(comunas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comuna> obtenerComunaPorId(@PathVariable Long id) {
        Optional<Comuna> comuna = comunaService.buscarPorId(id);
        if (comuna.isPresent()) {
            return ResponseEntity.ok(comuna.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Comuna> obtenerComunaPorNombre(@PathVariable String nombre) {
        Optional<Comuna> comuna = comunaService.buscarPorNombreIgnoreCase(nombre);
        if (comuna.isPresent()) {
            return ResponseEntity.ok(comuna.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/region/{regionId}")
    public ResponseEntity<List<Comuna>> obtenerComunasPorRegion(@PathVariable Long regionId) {
        List<Comuna> comunas = comunaService.obtenerComunasPorRegion(regionId);
        if (comunas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(comunas);
    }

    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<List<Comuna>> buscarComunasPorNombre(@PathVariable String nombre) {
        List<Comuna> comunas = comunaService.buscarComunasPorNombreParcial(nombre);
        if (comunas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(comunas);
    }

    @PostMapping
    public ResponseEntity<Comuna> crearComuna(@RequestBody Comuna comuna) {
        try {
            // Verificar si ya existe una comuna con ese nombre
            if (comunaService.existeComunaConNombre(comuna.getNombre())) {
                return ResponseEntity.badRequest().build();
            }
            Comuna nuevaComuna = comunaService.guardarComuna(comuna);
            return ResponseEntity.ok(nuevaComuna);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Comuna> actualizarComuna(@PathVariable Long id, @RequestBody Comuna comuna) {
        Comuna comunaActualizada = comunaService.actualizarComuna(id, comuna);
        if (comunaActualizada != null) {
            return ResponseEntity.ok(comunaActualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarComuna(@PathVariable Long id) {
        boolean eliminada = comunaService.eliminarComuna(id);
        if (eliminada) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/existe/{nombre}")
    public ResponseEntity<Boolean> verificarExistenciaComuna(@PathVariable String nombre) {
        boolean existe = comunaService.existeComunaConNombre(nombre);
        return ResponseEntity.ok(existe);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> contarComunas() {
        long total = comunaService.contarComunas();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/count/region/{regionId}")
    public ResponseEntity<Long> contarComunasPorRegion(@PathVariable Long regionId) {
        long total = comunaService.contarComunasPorRegion(regionId);
        return ResponseEntity.ok(total);
    }
}
