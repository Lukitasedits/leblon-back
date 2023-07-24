package com.leblon.app.services;

import com.leblon.app.models.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuariosService {
    public List<Usuario> obtenerUsuarios();

    public Usuario guardarUsuario(Usuario usuario) throws Exception;

    public Usuario save(Usuario usuario);

    public Usuario update(Usuario usuario, Long id);

    public Usuario obtenerUsuario(String username);

    public Usuario obtenerPorId(Long id);

    public void eliminarUsuario(Long id);

    public void eliminarUsuario(String username);

    public boolean existeUsuario(Usuario usuario);

    public  boolean existeUsuario(String username);

    public boolean existeUsuario(Long id);

    public boolean emailRegistrado(String email);

    public Usuario changePassword(Usuario usuario, String newPassword);

    public Optional<Usuario> getByUsernameAndEmail(String username, String email);

    public Optional<Usuario> getByTokenPassword(String tokenPassword);

    public Optional<Usuario> getByTokenEmail(String tokenEmail);

}
