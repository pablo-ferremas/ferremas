package cl.duoc.integracion.ferremas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.integracion.ferremas.entity.Region;
import cl.duoc.integracion.ferremas.repository.RegionRepository;

@Service
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    // Obtener todas las regiones
    public List<Region> obtenerTodasLasRegiones() {
        return regionRepository.findAll();
    }

    // Buscar región por ID
    public Optional<Region> buscarPorId(Long id) {
        return regionRepository.findById(id);
    }

    // Buscar región por nombre
    public Optional<Region> buscarPorNombre(String nombre) {
        return regionRepository.findByNombre(nombre);
    }

    // Buscar región por nombre ignorando mayúsculas/minúsculas
    public Optional<Region> buscarPorNombreIgnoreCase(String nombre) {
        return regionRepository.findByNombreIgnoreCase(nombre);
    }

    // Guardar nueva región
    public Region guardarRegion(Region region) {
        return regionRepository.save(region);
    }

    // Actualizar región existente
    public Region actualizarRegion(Long id, Region regionActualizada) {
        return regionRepository.findById(id)
            .map(regionExistente -> {
                if (regionActualizada.getNombre() != null) {
                    regionExistente.setNombre(regionActualizada.getNombre());
                }
                return regionRepository.save(regionExistente);
            })
            .orElse(null);
    }

    // Eliminar región por ID
    public boolean eliminarRegion(Long id) {
        if (regionRepository.existsById(id)) {
            regionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Verificar si existe una región con ese nombre
    public boolean existeRegionConNombre(String nombre) {
        return regionRepository.existsByNombre(nombre);
    }

    // Contar total de regiones
    public long contarRegiones() {
        return regionRepository.count();
    }
}
