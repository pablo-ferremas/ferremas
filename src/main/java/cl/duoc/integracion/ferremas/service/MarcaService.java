package cl.duoc.integracion.ferremas.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.duoc.integracion.ferremas.entity.Marca;
import cl.duoc.integracion.ferremas.repository.MarcaRepository;

@Service
public class MarcaService {

    @Autowired
    private MarcaRepository marcaRepository;

    public List<Marca> getAllMarcas() {
        return marcaRepository.findAll();
    }

    public Marca guardarMarca(Marca marca) {
        return marcaRepository.save(marca);
    }           

    // public Marca actualizarMarca(Integer id, Marca marca) {
    //     return marcaRepository.actualizarMarca(id, marca);
    // }

    public void eliminarMarca(Integer id) {
        marcaRepository.deleteById(id);
    }   

    public Marca obtenerMarca(Integer id) {
        return marcaRepository.findById(id).orElse(null);
    }   
}
