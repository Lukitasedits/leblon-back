package com.leblon.app.repositories;

import com.leblon.app.models.PeliculaModel;
import com.leblon.app.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<Usuario, Long> {
    public Usuario findByUsername(String username);

    public boolean existsByUsername(String username);

    public boolean existsById(Long id);

    public void deleteByUsername(String username);

    public Optional<Usuario> findByUsernameAndEmail(String username, String email);

    public Optional<Usuario> findByTokenPassword(String tokenPassword);

    public Optional<Usuario> findByTokenEmail(String tokenEmail);

    public boolean existsByEmail(String email);
}
