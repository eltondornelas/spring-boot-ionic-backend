package com.esd.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.esd.cursomc.domain.Categoria;
import com.esd.cursomc.dto.CategoriaDTO;
import com.esd.cursomc.services.CategoriaService;

//Resource (recurso) é o nome padrão para os controladores REST, vem após o nome da classe domínio

@RestController
@RequestMapping(value="/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaService service;
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET) //atribuindo verbo HTTP "get"
	public ResponseEntity<Categoria> find(@PathVariable Integer id) { //o PathVariable diz que o {id} da URL vai para o id da função
		//Response Entity é um tipo especial do Spring que já armazena várias informações de uma resposta HTTP para um serviço REST
		//? porque ele pode ser de qualquer tipo e que pode encontrar ou não.
		
		Categoria obj = service.find(id);
		return ResponseEntity.ok().body(obj);

		//nessa aula 14, vamos criar manualmente o banco de dados no H2
	}
	
	@RequestMapping(method = RequestMethod.POST) //POST para inserir novo
	public ResponseEntity<Void> insert(@RequestBody Categoria obj) {
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
				buildAndExpand(obj.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.PUT) //PUT para atualizar
	public ResponseEntity<Void> update(@RequestBody Categoria obj, @PathVariable Integer id) {
		obj.setId(id);
		obj = service.update(obj);
		
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE) //atribuindo verbo HTTP "get"
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(method=RequestMethod.GET) //como queremos que ele list todas as categorias, não vamos colocar o value id
	public ResponseEntity<List<CategoriaDTO>> findAll() {
				
		List<Categoria> list = service.findAll();
		List<CategoriaDTO> listDto = list.stream().map(obj -> new CategoriaDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
		
		//perceba que dessa forma quando colocamos no Postman o get categorias ele vem acoplado com seus produtos e não queremos isso
		//para corrigir e controlar para que venha apenas as categrias vamos utilizar DTO (Data Transfer Object) ou projeção de dados, controlar os dados que você quiser
	}
}
