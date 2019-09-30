package com.esd.cursomc.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.esd.cursomc.domain.Pedido;
import com.esd.cursomc.services.PedidoService;

//Resource (recurso) é o nome padrão para os controladores REST, vem após o nome da classe domínio

@RestController
@RequestMapping(value="/pedidos")
public class PedidoResource {

	@Autowired
	private PedidoService service;
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET) //atribuindo verbo HTTP "get"
	public ResponseEntity<Pedido> find(@PathVariable Integer id) { //o PathVariable diz que o {id} da URL vai para o id da função
		//Response Entity é um tipo especial do Spring que já armazena várias informações de uma resposta HTTP para um serviço REST
		//? porque ele pode ser de qualquer tipo e que pode encontrar ou não.
		
		Pedido obj = service.find(id);
		return ResponseEntity.ok().body(obj);

		//nessa aula 14, vamos criar manualmente o banco de dados no H2
	}
	
	
}
