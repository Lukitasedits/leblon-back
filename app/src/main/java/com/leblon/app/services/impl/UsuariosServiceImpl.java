package com.leblon.app.services.impl;

import com.leblon.app.models.Usuario;
import com.leblon.app.repositories.UsuariosRepository;
import com.leblon.app.services.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UsuariosServiceImpl implements UsuariosService {

    @Autowired
    private UsuariosRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerUsuarios() {
        return (List<Usuario>) usuarioRepository.findAll();
    }

    @Override
    @Transactional
    public Usuario guardarUsuario(Usuario usuario) throws Exception {
        if(usuario.getRol() != "ADMIN"){
            usuario.setRol("USER");
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario update(Usuario usuarioNuevo, Long id) {
        Usuario usuarioTabla = usuarioRepository.findById(id).orElse(null);

        if(usuarioNuevo.getUsername()!=null){
            usuarioTabla.setUsername(usuarioNuevo.getUsername());
        }
        if(usuarioNuevo.getPassword()!=null){
            usuarioTabla.setPassword(usuarioNuevo.getPassword());
        }
        return  usuarioRepository.save(usuarioTabla);

    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerUsuario(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void eliminarUsuario(String username){
        usuarioRepository.deleteByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeUsuario(Usuario usuario) {
        return usuarioRepository.existsByUsername(usuario.getUsername());
    }

    public boolean existeUsuario(String username){
        return  usuarioRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeUsuario(Long id) {
        return usuarioRepository.existsById(id);
    }

    @Override
    @Transactional
    public Usuario changePassword(Usuario usuario, String newPassword) {

        Usuario usuarioActualizado = usuario;

        usuarioActualizado.setPassword(newPassword);

        return usuarioRepository.save(usuarioActualizado);
    }

    @Override
    public Optional<Usuario> getByUsernameAndEmail(String username, String email) {
        return usuarioRepository.findByUsernameAndEmail(username, email);
    }

    @Override
    public Optional<Usuario> getByTokenPassword(String tokenPassword) {
        return usuarioRepository.findByTokenPassword(tokenPassword);
    }

    @Override
    public Optional<Usuario> getByTokenEmail(String tokenEmail) {
        return usuarioRepository.findByTokenEmail(tokenEmail);
    }


    @Override
    @Transactional(readOnly = true)
    public boolean emailRegistrado(String email) {
        return usuarioRepository.existsByEmail(email);
    }


}