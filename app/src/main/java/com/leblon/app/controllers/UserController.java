package com.leblon.app.controllers;


import com.leblon.app.models.Usuario;
import com.leblon.app.services.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = {"http://localhost:4200"})
public class UserController {

    @Autowired
    private UsuariosService usuariosService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/")
    public ResponseEntity<?> guardarUsuario(@Valid @RequestBody Usuario usuario, BindingResult result) throws Exception{
        Usuario nuevoUsuario = null;
        //usuario.setRol("USER");
        Map<String, Object> response = new HashMap<>();

        usuario.setPassword(this.bCryptPasswordEncoder.encode(usuario.getPassword()));

        if(result.hasErrors()){
            List<String> errors = result.getFieldErrors().stream()
                    .map(err -> "el campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if(usuariosService.existeUsuario(usuario.getUsername())){
            response.put("mensaje", "El usuario " + usuario.getUsername() + " ya existe.");
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        if (usuariosService.emailRegistrado(usuario.getEmail())){
            response.put("mensaje", "El email " + usuario.getEmail() + " ya está registrado.");
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try{
            nuevoUsuario = usuariosService.guardarUsuario(usuario);
        } catch (DataAccessException e){
            response.put("mensaje", "Error al intentar registrar usuario.");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        return new ResponseEntity<Usuario>(nuevoUsuario, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<?> obtenerUsuarios(){
        Map<String, Object> response = new HashMap<>();

        List<Usuario> usuarios = new ArrayList<>();
        try{
            usuarios = usuariosService.obtenerUsuarios();

            if(usuarios.isEmpty()){
                response.put("mensaje", "No existen usuarios.");
                return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }

        } catch (DataAccessException e){
            response.put("mensaje", "Error al intentar obtener usuarios.");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<List<Usuario>>(usuarios, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> obtenerUsuario(@PathVariable("username") String username){
        Map<String, Object> response = new HashMap<>();

        if(!usuariosService.existeUsuario(username)){
            response.put("mensaje", "El usuario " + username + " no existe.");
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        Usuario nuevoUsuario = null;
        try{
            nuevoUsuario = usuariosService.obtenerUsuario(username);
        } catch (DataAccessException e){
            response.put("mensaje", "Error al intentar obtener usuario.");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return  new ResponseEntity<Usuario>(nuevoUsuario, HttpStatus.OK);

    }

    @PutMapping("/update/")
    public ResponseEntity<?> actualizarUsuario(@Valid @RequestBody() Usuario usuario, BindingResult result){
        Map<String, Object> response = new HashMap<>();

        if(result.hasErrors()){
            List<String> errors = result.getFieldErrors().stream()
                    .map(err -> "el campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if(!usuariosService.existeUsuario(usuario.getUsername())){
            response.put("mensaje", "El usuario " + usuario.getUsername() + " no existe.");
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        Usuario nuevoUsuario = null;

        try{
            nuevoUsuario = usuariosService.update(usuario, usuario.getId());
        } catch (DataAccessException e){
            response.put("mensaje", "Error al intentar cambiar la contraseña del usuario " + usuario.getUsername() + ".");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Usuario>(nuevoUsuario, HttpStatus.OK);
    }

    @PutMapping("/change-password/{username}/{newPassword}")
    public ResponseEntity<?> changePassword(@PathVariable("username") String username, @PathVariable("newPassword") String newPassword){

        Map<String, Object> response = new HashMap<>();

        if(!usuariosService.existeUsuario(username)){
            response.put("mensaje", "El usuario " + username + " no existe.");
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try{
            Usuario usuario = usuariosService.obtenerUsuario(username);
            usuariosService.changePassword(usuario, newPassword);
        } catch (DataAccessException e){
            response.put("mensaje", "Error al intentar cambiar la contraseña del usuario " + username + ".");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<?> eliminarUsuarioById(@PathVariable("usuarioId") Long id){

        Map<String, Object> response = new HashMap<>();

        if(!usuariosService.existeUsuario(id)){
            response.put("mensaje", "El usuario con id " + id + " no existe.");
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try{
            usuariosService.eliminarUsuario(id);
        } catch (DataAccessException e){
            response.put("mensaje", "Error al intentar eliminiar el usuario con id " + id + ".");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/username/{username}")
    public ResponseEntity<?> eliminarUsuarioByUsername(@PathVariable("username") String username){

        Map<String, Object> response = new HashMap<>();

        if(!usuariosService.existeUsuario(username)){
            response.put("mensaje", "El usuario " + username + " no existe.");
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try{
            usuariosService.eliminarUsuario(username);
        } catch (DataAccessException e){
            response.put("mensaje", "Error al intentar eliminiar el usuario " + username + ".");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
