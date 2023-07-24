package com.leblon.app;

import com.leblon.app.models.Usuario;
import com.leblon.app.services.EmailService;
import com.leblon.app.services.PeliculasService;
import com.leblon.app.services.impl.UsuariosServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class LeblonApplicationTests {

	@Autowired
	private UsuariosServiceImpl usuariosService;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private PeliculasService peliculasService;

	@Autowired
	private EmailService emailService;


	@Test
	public void crearUsuarioTest() throws Exception {
		Usuario us = new Usuario();
		us.setUsername("bruno-donnini");
		us.setEmail("brunodonninimoreo@gmail.com");
		us.setPassword(encoder.encode("lunompi"));
		us.setEnabled(true);
		us.setRol("ADMIN");
		usuariosService.guardarUsuario(us);

		//assertTrue(retorno.getUsername().equals(us.getUsername()));

	}

	@Test
	public void busquedaTest(){
	assert(peliculasService.peliculaContieneBusqueda("Five nights at freddy's", "five"));
	}

	@Test
	public void probarEmail(){
		emailService.sendEmail();
	}
}
