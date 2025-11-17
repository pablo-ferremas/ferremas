package cl.duoc.integracion.ferremas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.integracion.ferremas.entity.Comuna;
import cl.duoc.integracion.ferremas.repository.ComunaRepository;

@Service
public class ComunaService {

    @Autowired
    private ComunaRepository comunaRepository;

    // Obtener todas las comunas
    public List<Comuna> obtenerTodasLasComunas() {
        return comunaRepository.findAll();
    }

    // Buscar comuna por ID
    public Optional<Comuna> buscarPorId(Long id) {
        return comunaRepository.findById(id);
    }

    // Buscar comuna por nombre
    public Optional<Comuna> buscarPorNombre(String nombre) {
        return comunaRepository.findByNombre(nombre);
    }

    // Buscar comuna por nombre ignorando mayúsculas/minúsculas
    public Optional<Comuna> buscarPorNombreIgnoreCase(String nombre) {
        return comunaRepository.findByNombreIgnoreCase(nombre);
    }

    // Obtener todas las comunas de una región específica
    public List<Comuna> obtenerComunasPorRegion(Long regionId) {
        return comunaRepository.findByRegionId(regionId);
    }

    // Buscar comunas por nombre parcial
    public List<Comuna> buscarComunasPorNombreParcial(String nombre) {
        return comunaRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // Guardar nueva comuna
    public Comuna guardarComuna(Comuna comuna) {
        return comunaRepository.save(comuna);
    }

    // Actualizar comuna existente
    public Comuna actualizarComuna(Long id, Comuna comunaActualizada) {
        return comunaRepository.findById(id)
            .map(comunaExistente -> {
                if (comunaActualizada.getNombre() != null) {
                    comunaExistente.setNombre(comunaActualizada.getNombre());
                }
                // Nota: regionId no se actualiza ya que es manejado por la relación
                return comunaRepository.save(comunaExistente);
            })
            .orElse(null);
    }

    // Eliminar comuna por ID
    public boolean eliminarComuna(Long id) {
        if (comunaRepository.existsById(id)) {
            comunaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Verificar si existe una comuna con ese nombre
    public boolean existeComunaConNombre(String nombre) {
        return comunaRepository.existsByNombre(nombre);
    }

    // Contar total de comunas
    public long contarComunas() {
        return comunaRepository.count();
    }

    // Contar comunas de una región específica
    public long contarComunasPorRegion(Long regionId) {
        return comunaRepository.countByRegionId(regionId);
    }
}
