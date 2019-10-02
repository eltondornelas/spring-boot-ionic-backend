package com.esd.cursomc.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.esd.cursomc.domain.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
	
	@Transactional //(readOnly=true) não funciona nessa versão
	Cliente findByEmail(String email); 
	//apenas essa declaração, o Spring Data já entende que você quer fazer uma busca por e-mail no banco de dados.
}
