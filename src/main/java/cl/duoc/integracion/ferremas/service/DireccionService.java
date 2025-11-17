package cl.duoc.integracion.ferremas.service;

import org.springframework.stereotype.Service;

import cl.duoc.integracion.ferremas.entity.Direccion;
import cl.duoc.integracion.ferremas.entity.Region;
import cl.duoc.integracion.ferremas.entity.Comuna;
import cl.duoc.integracion.ferremas.repository.DireccionRepository;
import cl.duoc.integracion.ferremas.repository.RegionRepository;
import cl.duoc.integracion.ferremas.repository.ComunaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DireccionService {

    private final DireccionRepository direccionRepository;
    private final RegionRepository regionRepository;
    private final ComunaRepository comunaRepository;

    public DireccionService(DireccionRepository direccionRepository, 
                           RegionRepository regionRepository,
                           ComunaRepository comunaRepository) {
        this.direccionRepository = direccionRepository;
        this.regionRepository = regionRepository;
        this.comunaRepository = comunaRepository;
    }

    /**
     * Obtiene todas las direcciones
     */
    public List<Direccion> obtenerTodasLasDirecciones() {
        return direccionRepository.findAll();
    }

    /**
     * Busca una dirección por ID
     */
    public Direccion buscarPorId(Long id) {
        Optional<Direccion> direccion = direccionRepository.findById(id);
        return direccion.orElse(null);
    }

    /**
     * Crea una nueva dirección
     */
    public Direccion crearDireccion(Direccion direccion) {
        // Validar que existan la región y comuna
        if (direccion.getRegion() != null && direccion.getRegion().getId() != null) {
            Optional<Region> region = regionRepository.findById(direccion.getRegion().getId());
            if (region.isPresent()) {
                direccion.setRegion(region.get());
            } else {
                throw new RuntimeException("Región no encontrada con ID: " + direccion.getRegion().getId());
            }
        }

        if (direccion.getComuna() != null && direccion.getComuna().getId() != null) {
            Optional<Comuna> comuna = comunaRepository.findById(direccion.getComuna().getId());
            if (comuna.isPresent()) {
                direccion.setComuna(comuna.get());
            } else {
                throw new RuntimeException("Comuna no encontrada con ID: " + direccion.getComuna().getId());
            }
        }

        return direccionRepository.save(direccion);
    }

    /**
     * Actualiza una dirección existente
     */
    public Direccion actualizarDireccion(Long id, Direccion direccionActualizada) {
        Optional<Direccion> direccionExistente = direccionRepository.findById(id);
        
        if (direccionExistente.isPresent()) {
            Direccion direccion = direccionExistente.get();
            
            // Actualizar campos básicos
            if (direccionActualizada.getCalle() != null) {
                direccion.setCalle(direccionActualizada.getCalle());
            }
            
            if (direccionActualizada.getNumero() != null) {
                direccion.setNumero(direccionActualizada.getNumero());
            }

            // Actualizar región si se proporciona
            if (direccionActualizada.getRegion() != null && direccionActualizada.getRegion().getId() != null) {
                Optional<Region> region = regionRepository.findById(direccionActualizada.getRegion().getId());
                if (region.isPresent()) {
                    direccion.setRegion(region.get());
                } else {
                    throw new RuntimeException("Región no encontrada con ID: " + direccionActualizada.getRegion().getId());
                }
            }

            // Actualizar comuna si se proporciona
            if (direccionActualizada.getComuna() != null && direccionActualizada.getComuna().getId() != null) {
                Optional<Comuna> comuna = comunaRepository.findById(direccionActualizada.getComuna().getId());
                if (comuna.isPresent()) {
                    direccion.setComuna(comuna.get());
                } else {
                    throw new RuntimeException("Comuna no encontrada con ID: " + direccionActualizada.getComuna().getId());
                }
            }

            return direccionRepository.save(direccion);
        }
        
        return null;
    }

    /**
     * Elimina una dirección
     */
    public boolean eliminarDireccion(Long id) {
        if (direccionRepository.existsById(id)) {
            direccionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Busca direcciones por usuario
     */
    public List<Direccion> buscarPorUsuarioId(Long usuarioId) {
        return direccionRepository.findByUsuarioId(usuarioId);
    }

    /**
     * Busca direcciones por región
     */
    public List<Direccion> buscarPorRegionId(Long regionId) {
        return direccionRepository.findByRegionId(regionId);
    }

    /**
     * Busca direcciones por comuna
     */
    public List<Direccion> buscarPorComunaId(Long comunaId) {
        return direccionRepository.findByComunaId(comunaId);
    }

    /**
     * Busca direcciones por calle
     */
    public List<Direccion> buscarPorCalle(String calle) {
        return direccionRepository.findByCalleContainingIgnoreCase(calle);
    }

    /**
     * Busca direcciones por región y comuna
     */
    public List<Direccion> buscarPorRegionYComuna(Long regionId, Long comunaId) {
        return direccionRepository.findByRegionAndComuna(regionId, comunaId);
    }

    /**
     * Cuenta direcciones por usuario
     */
    public long contarPorUsuarioId(Long usuarioId) {
        return direccionRepository.countByUsuarioId(usuarioId);
    }
}
