package com.leblon.app;

import com.leblon.app.models.Usuario;
import com.leblon.app.repositories.UsuariosRepository;
import com.leblon.app.services.EmailService;
import com.leblon.app.services.PeliculasService;
import com.leblon.app.services.impl.UsuariosServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class LeblonApplicationTests {


	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private PeliculasService peliculasService;

	@Autowired
	private EmailService emailService;


	@Test
	public void busquedaTest(){
	assertTrue(peliculasService.peliculaContieneBusqueda("Five nights at freddy's", "five"));
	}

	@Test
	public void probarEmail(){
		emailService.sendEmail();
	}
}
