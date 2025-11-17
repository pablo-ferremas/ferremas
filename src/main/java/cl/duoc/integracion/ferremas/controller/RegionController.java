package cl.duoc.integracion.ferremas.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.duoc.integracion.ferremas.entity.Region;
import cl.duoc.integracion.ferremas.service.RegionService;

@RestController
@RequestMapping("/api/v1/regiones")
@CrossOrigin(origins = "*")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @GetMapping
    public ResponseEntity<List<Region>> obtenerTodasLasRegiones() {
        List<Region> regiones = regionService.obtenerTodasLasRegiones();
        if (regiones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(regiones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Region> obtenerRegionPorId(@PathVariable Long id) {
        Optional<Region> region = regionService.buscarPorId(id);
        if (region.isPresent()) {
            return ResponseEntity.ok(region.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Region> obtenerRegionPorNombre(@PathVariable String nombre) {
        Optional<Region> region = regionService.buscarPorNombreIgnoreCase(nombre);
        if (region.isPresent()) {
            return ResponseEntity.ok(region.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Region> crearRegion(@RequestBody Region region) {
        try {
            // Verificar si ya existe una región con ese nombre
            if (regionService.existeRegionConNombre(region.getNombre())) {
                return ResponseEntity.badRequest().build();
            }
            Region nuevaRegion = regionService.guardarRegion(region);
            return ResponseEntity.ok(nuevaRegion);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Region> actualizarRegion(@PathVariable Long id, @RequestBody Region region) {
        Region regionActualizada = regionService.actualizarRegion(id, region);
        if (regionActualizada != null) {
            return ResponseEntity.ok(regionActualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRegion(@PathVariable Long id) {
        boolean eliminada = regionService.eliminarRegion(id);
        if (eliminada) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/existe/{nombre}")
    public ResponseEntity<Boolean> verificarExistenciaRegion(@PathVariable String nombre) {
        boolean existe = regionService.existeRegionConNombre(nombre);
        return ResponseEntity.ok(existe);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> contarRegiones() {
        long total = regionService.contarRegiones();
        return ResponseEntity.ok(total);
    }
}
