package com.esd.cursomc.resources;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.esd.cursomc.security.JWTUtil;
import com.esd.cursomc.security.UserSS;
import com.esd.cursomc.services.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {
	
	@Autowired
	private JWTUtil jwtUtil;

	//o endpoint abaixo é protegido por autenticação, ele precisa estar logado para acessar
	
	@RequestMapping(value = "/refresh_token", method = RequestMethod.POST)  
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		UserSS user = UserService.authenticated();
		//pega usuário logado
		String token = jwtUtil.generateToken(user.getUsername());
		//gera novo token com o usuário com a data atual, renovando o tempo de expiração
		response.addHeader("Authorization", "Bearer " + token);

		return ResponseEntity.noContent().build();		
	}
}