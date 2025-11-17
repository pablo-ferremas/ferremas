package cl.duoc.integracion.ferremas.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.duoc.integracion.ferremas.entity.Direccion;
import cl.duoc.integracion.ferremas.service.DireccionService;

@RestController
@RequestMapping("/api/v1/direcciones")
@CrossOrigin(origins = "*")
public class DireccionController {

    private final DireccionService direccionService;

    public DireccionController(DireccionService direccionService) {
        this.direccionService = direccionService;
    }

    /**
     * Obtiene todas las direcciones
     */
    @GetMapping
    public ResponseEntity<List<Direccion>> obtenerTodasLasDirecciones() {
        List<Direccion> direcciones = direccionService.obtenerTodasLasDirecciones();
        return ResponseEntity.ok(direcciones);
    }

    /**
     * Obtiene una dirección por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Direccion> obtenerDireccionPorId(@PathVariable Long id) {
        Direccion direccion = direccionService.buscarPorId(id);
        if (direccion != null) {
            return ResponseEntity.ok(direccion);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crea una nueva dirección
     */
    @PostMapping
    public ResponseEntity<?> crearDireccion(@RequestBody Direccion direccion) {
        try {
            Direccion nuevaDireccion = direccionService.crearDireccion(direccion);
            return ResponseEntity.ok(nuevaDireccion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Actualiza una dirección existente
     */
    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarDireccion(@PathVariable Long id, @RequestBody Direccion direccion) {
        try {
            Direccion direccionActualizada = direccionService.actualizarDireccion(id, direccion);
            if (direccionActualizada != null) {
                return ResponseEntity.ok(direccionActualizada);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Elimina una dirección
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDireccion(@PathVariable Long id) {
        boolean eliminado = direccionService.eliminarDireccion(id);
        if (eliminado) {
            return ResponseEntity.ok().body("Dirección eliminada correctamente");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Busca direcciones por usuario
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Direccion>> obtenerDireccionesPorUsuario(@PathVariable Long usuarioId) {
        List<Direccion> direcciones = direccionService.buscarPorUsuarioId(usuarioId);
        return ResponseEntity.ok(direcciones);
    }

    /**
     * Busca direcciones por región
     */
    @GetMapping("/region/{regionId}")
    public ResponseEntity<List<Direccion>> obtenerDireccionesPorRegion(@PathVariable Long regionId) {
        List<Direccion> direcciones = direccionService.buscarPorRegionId(regionId);
        return ResponseEntity.ok(direcciones);
    }

    /**
     * Busca direcciones por comuna
     */
    @GetMapping("/comuna/{comunaId}")
    public ResponseEntity<List<Direccion>> obtenerDireccionesPorComuna(@PathVariable Long comunaId) {
        List<Direccion> direcciones = direccionService.buscarPorComunaId(comunaId);
        return ResponseEntity.ok(direcciones);
    }

    /**
     * Busca direcciones por calle (búsqueda parcial)
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Direccion>> buscarDireccionesPorCalle(@RequestParam String calle) {
        List<Direccion> direcciones = direccionService.buscarPorCalle(calle);
        return ResponseEntity.ok(direcciones);
    }

    /**
     * Busca direcciones por región y comuna
     */
    @GetMapping("/filtrar")
    public ResponseEntity<List<Direccion>> filtrarDirecciones(
            @RequestParam Long regionId,
            @RequestParam Long comunaId) {
        List<Direccion> direcciones = direccionService.buscarPorRegionYComuna(regionId, comunaId);
        return ResponseEntity.ok(direcciones);
    }

    /**
     * Cuenta direcciones por usuario
     */
    @GetMapping("/contar/usuario/{usuarioId}")
    public ResponseEntity<Long> contarDireccionesPorUsuario(@PathVariable Long usuarioId) {
        long count = direccionService.contarPorUsuarioId(usuarioId);
        return ResponseEntity.ok(count);
    }
}
