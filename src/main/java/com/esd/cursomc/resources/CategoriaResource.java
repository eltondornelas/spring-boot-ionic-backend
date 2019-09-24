package com.esd.cursomc.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//Resource (recurso) é o nome padrão para os controladores REST, vem após o nome da classe domínio

@RestController
@RequestMapping(value="/categorias")
public class CategoriaResource {

	@RequestMapping(method=RequestMethod.GET) //atribuindo verbo HTTP "get"
	public String listar() {
		return "Rest está funcionando";
	}
	
	
}
