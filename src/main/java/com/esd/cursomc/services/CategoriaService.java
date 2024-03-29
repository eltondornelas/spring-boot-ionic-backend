package com.esd.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.esd.cursomc.domain.Categoria;
import com.esd.cursomc.dto.CategoriaDTO;
import com.esd.cursomc.repositories.CategoriaRepository;
import com.esd.cursomc.services.exceptions.DataIntegrityException;
import com.esd.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;

	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}

	public Categoria insert(Categoria obj) {
		obj.setId(null); //por desencargo de consciência, pois se ele não for null o método save vai achar que é uma atualização e não uma inserção
		return repo.save(obj);
	}
	
	public Categoria update(Categoria obj) {
		Categoria newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}
	
	public void delete(Integer id) {
		find(id); //caso não exista já dispara a exceção
		try {
			repo.deleteById(id);
		}
		catch (DataIntegrityViolationException e) { 
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos!");
			//Data Integrity é uma excessão dada pelo Spring Data, que vem do banco de dados.
		}
		
	}
	
	public List<Categoria> findAll() {
		return repo.findAll();
	}
	
	//encapsula informações e operações sobre a paginação
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage,
				Direction.valueOf(direction), orderBy);
		
		return repo.findAll(pageRequest);
	}
	
	public Categoria fromDTO(CategoriaDTO objDto) {
		return new Categoria(objDto.getId(), objDto.getNome());
	}
	
	private void updateData(Categoria newObj, Categoria obj) {
		newObj.setNome(obj.getNome());	
	}
}
