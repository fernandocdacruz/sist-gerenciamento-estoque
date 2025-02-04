package model.entities;

public class Produto {
	
	private int id;
	private String nome;
	private double preco;
	private int quantidade;
	
	public Produto() {
	}

	public Produto(String nome, double preco, int quantidade) {
		super();
		this.nome = nome;
		this.preco = preco;
		this.quantidade = quantidade;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	@Override
	public String toString() {
		return "\nID: " + id + "\nNOME: " + nome + "\nPREÇO: R$" + String.format("%.2f", preco) + "\nQUANTIDADE: " + quantidade;
	}
	
	public String toString2() {
		return "ID: " + id + " - NOME: " + nome + " - PREÇO: R$ " + String.format("%.2f", preco) + " - QUANTIDADE: " + quantidade;
	}
	
	
	
}
