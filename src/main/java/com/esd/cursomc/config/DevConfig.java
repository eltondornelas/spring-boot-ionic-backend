package com.esd.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.esd.cursomc.services.DBService;

@Configuration
@Profile("dev")
public class DevConfig {

	@Autowired
	private DBService dbService;
	
	@Value("${spring.jpa.hibernate.ddl-auto}") //esse comando pega o valor da chave e armazenamos na variável abaixo.
	private String strategy;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		/* o comando (ou chave) em application-dev.properties: spring.jpa.hibernate.ddl-auto=create
		 * está setado para criar toda vez que rodarmos o programa. Como já criamos o banco de dados, não há necessidade de sempre que rodar criar o banco.
		 * por conta disso, vamos fazer uma validação aqui para que ele só instancie os dados, caso esse comando esteja como "create"
		 * se colocar no google a chave/comando: spring.jpa.hibernate.ddl-auto vai aparecer as outras opções possíveis para esse comando. 
		 * */
		
		if(!"create".equals(strategy)) {
			return false;
		}
				
		dbService.instantiateTestDatabase();		
		
		return true;
	}
}
