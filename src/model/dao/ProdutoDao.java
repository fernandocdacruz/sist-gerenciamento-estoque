package model.dao;

import java.util.List;

import model.entities.Produto;

public interface ProdutoDao {

	void adicionarProduto(Produto produto);
	void atualizarPreco(Produto produto);
	void atualizarQuantidade(Produto produto);
	List<Produto> listarTodosProdutos();
	void deletarProduto(int id);
	Produto buscarPeloId(int id);
}
