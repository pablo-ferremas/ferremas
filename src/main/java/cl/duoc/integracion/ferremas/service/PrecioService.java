package cl.duoc.integracion.ferremas.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.duoc.integracion.ferremas.entity.Precio;
import cl.duoc.integracion.ferremas.repository.PrecioRepository;

@Service
public class PrecioService {

    @Autowired
    private PrecioRepository precioRepository;

    public List<Precio> getAllPrecios() {
        return precioRepository.findAll();
    }

    public Precio guardarPrecio(Precio precio) {
        return precioRepository.save(precio);
    }

    // public Precio actualizarPrecio(Integer id, Precio precio) {
    //     return precioRepository.actualizarPrecio(id, precio);
    // }

    public void eliminarPrecio(Integer id) {
        precioRepository.deleteById(id);
    }
    public Precio obtenerPrecio(Integer id) {
        return precioRepository.findById(id).orElse(null);
    }
}
