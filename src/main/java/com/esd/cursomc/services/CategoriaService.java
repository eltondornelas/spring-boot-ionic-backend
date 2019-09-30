package com.esd.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esd.cursomc.domain.Categoria;
import com.esd.cursomc.repositories.CategoriaRepository;
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
		find(obj.getId());
		return repo.save(obj);
		//o .save serve tanto para inserir novo como para atualizar, o que diferencia é que o id tem que estar nulo para ser inserção.
	}
}
