package com.esd.cursomc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.esd.cursomc.domain.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {
	
	@Transactional(readOnly=true)
	public List<Estado> findAllByOrderByNome();
	//lembrando que essa nomenclatura faz parte do padrão de nomes do Spring data
}
