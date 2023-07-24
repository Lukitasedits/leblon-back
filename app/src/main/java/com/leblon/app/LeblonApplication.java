package com.leblon.app;

import com.leblon.app.models.Usuario;
import com.leblon.app.services.EmailService;
import com.leblon.app.services.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class LeblonApplication {

	@Autowired
	private EmailService sender;

	public static void main(String[] args) {
		SpringApplication.run(LeblonApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void sendEmail(){
		sender.sendEmail();
	}

}
