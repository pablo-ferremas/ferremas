package cl.duoc.integracion.ferremas.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.duoc.integracion.ferremas.entity.Categoria;
import cl.duoc.integracion.ferremas.repository.CategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> getAllCategorias() {
        return categoriaRepository.findAll();
    }

    public Categoria guardarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    // public Categoria actualizarCategoria(Integer id, Categoria categoria) {
    //     return categoriaRepository.actualizarCategoria(id, categoria);
    // }

    public void eliminarCategoria(Integer id) {
        categoriaRepository.deleteById(id);
    }

    public Categoria obtenerCategoria(Integer id) {
        return categoriaRepository.findById(id).orElse(null);
    }
}
