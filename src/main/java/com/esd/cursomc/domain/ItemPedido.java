package com.esd.cursomc.domain;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ItemPedido implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore // não vai precisar do Json com referência
	@EmbeddedId
	private ItemPedidoPK id = new ItemPedidoPK();
	// atributo composto, precisa colocar a anotação Embeddable nessa classe
	// chave composta sendo feita por uma classe auxiliar

	private Double desconto;
	private Integer quantidade;
	private Double preco;

	// por ser classe de associação, ela não tem id, quem identifica são as duas
	// classes que ela está ligada
	// como precisa de referência para pedido e produto vamos criar uma classe
	// auxiliar ItemPedidoPK que serve como a chave primária dessa classe

	// a associação é feita no ItemPedidoPK

	public ItemPedido() {

	}

	public ItemPedido(Pedido pedido, Produto p1, Double desconto, Integer quantidade, Double preco) {
		super();
		id.setPedido(pedido);
		id.setProduto(p1);
		this.desconto = desconto;
		this.quantidade = quantidade;
		this.preco = preco;
	}

	public double getSubTotal() {
		// tem que deixar o nome get para o Json enxergar e rodar ele
		return (preco - desconto) * quantidade;
	}

	@JsonIgnore
	public Pedido getPedido() {
		return id.getPedido();
	}

	public void setPedido(Pedido pedido) {
		id.setPedido(pedido);
	}

	// @JsonIgnore incluímos mas depois tiramos para que ele possa aparacer no item
	// do pedido no Postman
	public Produto getProduto() {
		return id.getProduto();
	}
	// é interessante ter os gets desses dois itens para que não tenha a necessidade
	// de entrar primeiro no PK para obter esses dados

	public void setProduto(Produto produto) {
		id.setProduto(produto);
	}

	public ItemPedidoPK getId() {
		return id;
	}

	public void setId(ItemPedidoPK id) {
		this.id = id;
	}

	public Double getDesconto() {
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemPedido other = (ItemPedido) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
		StringBuilder builder = new StringBuilder();
		builder.append(getProduto().getNome());
		builder.append(", Qte: ");
		builder.append(getQuantidade());
		builder.append(", Preço unitário: ");
		builder.append(nf.format(getPreco()));
		builder.append(", Subtotal: ");
		builder.append(nf.format(getSubTotal()));
		builder.append("\n");

		return builder.toString();
	}

}
