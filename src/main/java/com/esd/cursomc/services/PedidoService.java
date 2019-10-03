package com.esd.cursomc.services;

import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esd.cursomc.domain.ItemPedido;
import com.esd.cursomc.domain.PagamentoComBoleto;
import com.esd.cursomc.domain.Pedido;
import com.esd.cursomc.domain.Produto;
import com.esd.cursomc.domain.enums.EstadoPagamento;
import com.esd.cursomc.repositories.ItemPedidoRepository;
import com.esd.cursomc.repositories.PagamentoRepository;
import com.esd.cursomc.repositories.PedidoRepository;
import com.esd.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;

	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj); //associação de mão dupla
		
		//vamos considerar que a data de vencimento é 1 semana depois da data do pedigo
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		
		obj = repo.save(obj);		
		pagamentoRepository.save(obj.getPagamento());
		
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			//ip.setPreco(produtoRepository.findById(ip.getProduto().getId()).getPreco());
			Optional<Produto> p = produtoService.find(ip.getProduto().getId());
			ip.setProduto(p.get());
			ip.setPreco(p.get().getPreco());
			ip.setPedido(obj);
		}
		
		itemPedidoRepository.saveAll(obj.getItens());
		emailService.sendOrderConfirmationEmail(obj);
		
		return obj;
	}

}
