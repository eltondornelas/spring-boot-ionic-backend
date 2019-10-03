package com.esd.cursomc.services;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import com.esd.cursomc.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj); //versão texto plano

	void sendEmail(SimpleMailMessage msg); //enviar e-mail com texto plano
	
	void sendOrderConfirmationHtmlEmail(Pedido obj); //enviar email versão html

	void sendHtmlEmail(MimeMessage msg);

}
