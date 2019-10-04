package com.esd.cursomc.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	
	private JWTUtil jwtUtil;

	private UserDetailsService userDetailsService;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
			UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
		//ele vai validar o token (é o que vem depois do "Bearer") gerado no login do usuário e por isso precisa extrair o usuário (UserDetailService) e verificar se ele existe
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
				HttpServletResponse response, FilterChain chain) 
					throws IOException, ServletException {
		//Ele executa antes de deixar a requisição continuar
		
		String header = request.getHeader("Authorization");
		//é o token gerado na chave Header(Cabeçalho)
		//Authorization é o nome da chave lá no Postman
		
		if (header != null && header.startsWith("Bearer ")) {
			UsernamePasswordAuthenticationToken auth = getAuthentication(header.substring(7));
			//substring 7 -> significa que vai ignorar os 7 primeiros caracteres, que é o "Bearer "

			if (auth != null) {
				SecurityContextHolder.getContext().setAuthentication(auth);
				//se entrar no esta tudo certo. Libera o usuário
			}
		}
		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		if (jwtUtil.tokenValido(token)) {
			String username = jwtUtil.getUsername(token);
			UserDetails user = userDetailsService.loadUserByUsername(username);
			//busca o usuário no DB;
			
			return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		}
		return null;
	}
}