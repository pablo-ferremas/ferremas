package cl.duoc.integracion.ferremas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.integracion.ferremas.entity.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}
