package com.esd.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.esd.cursomc.domain.Categoria;
import com.esd.cursomc.dto.CategoriaDTO;
import com.esd.cursomc.services.CategoriaService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

//Resource (recurso) é o nome padrão para os controladores REST, vem após o nome da classe domínio

@RestController
@RequestMapping(value="/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaService service;
	
	@ApiOperation(value="Busca por id")
	@RequestMapping(value="/{id}", method=RequestMethod.GET) //atribuindo verbo HTTP "get"
	public ResponseEntity<Categoria> find(@PathVariable Integer id) { //o PathVariable diz que o {id} da URL vai para o id da função
		//Response Entity é um tipo especial do Spring que já armazena várias informações de uma resposta HTTP para um serviço REST
		//? porque ele pode ser de qualquer tipo e que pode encontrar ou não.
		
		Categoria obj = service.find(id);
		return ResponseEntity.ok().body(obj);

		//nessa aula 14, vamos criar manualmente o banco de dados no H2
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value="Insere categoria")
	@RequestMapping(method = RequestMethod.POST) //POST para inserir novo
	public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO objDto) {
		Categoria obj = service.fromDTO(objDto);
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
				buildAndExpand(obj.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value="Atualiza categoria")
	@RequestMapping(value="/{id}", method = RequestMethod.PUT) //PUT para atualizar
	public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO objDto, @PathVariable Integer id) {
		Categoria obj = service.fromDTO(objDto);
		obj.setId(id);
		obj = service.update(obj);
		
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value="Remove categoria")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Não é possível excluir uma categoria que possui produtos"),
			@ApiResponse(code = 404, message = "Código inexistente") })
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE) //atribuindo verbo HTTP "get"
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value="Retorna todas categorias")
	@RequestMapping(method=RequestMethod.GET) //como queremos que ele list todas as categorias, não vamos colocar o value id
	public ResponseEntity<List<CategoriaDTO>> findAll() {
				
		List<Categoria> list = service.findAll();
		List<CategoriaDTO> listDto = list.stream().map(obj -> new CategoriaDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
		
		//perceba que dessa forma quando colocamos no Postman o get categorias ele vem acoplado com seus produtos e não queremos isso
		//para corrigir e controlar para que venha apenas as categrias vamos utilizar DTO (Data Transfer Object) ou projeção de dados, controlar os dados que você quiser
		//Aula 34 bem importante sobre isso.
	}
	
	@ApiOperation(value="Retorna todas categorias com paginação")
	@RequestMapping(value = "/page", method=RequestMethod.GET) //concatena o /page com o /categoria lá em cima da classe
	public ResponseEntity<Page<CategoriaDTO>> findPage(
			@RequestParam(value="page", defaultValue = "0") Integer page,
			@RequestParam(value="linesPerPage", defaultValue = "24")Integer linesPerPage,
			@RequestParam(value="orderBy", defaultValue = "nome")String orderBy,
			@RequestParam(value="direction", defaultValue = "ASC")String direction) {

		//página default 0 é a primeira página. Linhas por página é interessante o número 24, porque é múltiplo de 1, 2, 3 e 4...
		//Aula 35
		
		Page<Categoria> list = service.findPage(page, linesPerPage, orderBy, direction);
		Page<CategoriaDTO> listDto = list.map(obj -> new CategoriaDTO(obj));
		return ResponseEntity.ok().body(listDto);
		
	}
}
