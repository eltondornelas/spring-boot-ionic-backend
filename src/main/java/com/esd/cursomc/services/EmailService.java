package com.esd.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.esd.cursomc.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);

	void sendEmail(SimpleMailMessage msg);

}
