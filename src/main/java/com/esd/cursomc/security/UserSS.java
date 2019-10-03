package com.esd.cursomc.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.esd.cursomc.domain.enums.Perfil;

public class UserSS implements UserDetails {
	//SS de Spring Security
	
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String email;
	private String senha;

	private Collection<? extends GrantedAuthority> authorities; //exigência do Spring..., vamos usar como referência do Perfil

	public UserSS() {

	}

	public UserSS(Integer id, String email, String senha, Set<Perfil> perfis) {
		super();
		this.id = id;
		this.email = email;
		this.senha = senha;
		this.authorities = perfis.stream().map(x -> new SimpleGrantedAuthority(x.getDescricao()))
				.collect(Collectors.toList());

		//Recebe a lista de perfil e convertemos para a lista de collection que o Spring exige
	}

	public Integer getId() {
		return id;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
		//pode incluir uma lógica para considerar que ela fique expirada depois de um certo tempo, tudo depende da regra do Negócio
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
		//conta bloqueada
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
		//credenciais expiradas
	}

	@Override
	public boolean isEnabled() {
		return true;
		//usuário ativo
	}

}