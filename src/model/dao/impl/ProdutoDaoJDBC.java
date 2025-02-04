package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.ProdutoDao;
import model.entities.Produto;

public class ProdutoDaoJDBC implements ProdutoDao {

	private Connection conn;

	public ProdutoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void adicionarProduto(Produto produto) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO produtos " + "(nome, preco, quantidade)" + "VALUES " + "(?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, produto.getNome());
			st.setDouble(2, produto.getPreco());
			st.setInt(3, produto.getQuantidade());

			int linhasAfetadas = st.executeUpdate();

			if (linhasAfetadas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					System.out.println("\nProduto adicionado com sucesso. ID GERADO: " + id);
				}
				DB.closeResultSet(rs);
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void atualizarPreco(Produto produto) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE produtos "
					+ "SET preco = ? "
					+ "WHERE Id = ?");
			st.setDouble(1, produto.getPreco());
			st.setInt(2, produto.getId());
			
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void atualizarQuantidade(Produto produto) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE produtos "
					+ "SET quantidade = ? "
					+ "WHERE Id = ?");
			st.setInt(1, produto.getQuantidade());
			st.setInt(2, produto.getId());
			
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public List<Produto> listarTodosProdutos() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT produtos.* "
					+ "FROM produtos");
			
			rs = st.executeQuery();
			List<Produto> produtos = new ArrayList<>();
			while (rs.next()) {
				Produto produto = instanciarProduto(rs);
				produtos.add(produto);
			}
			return produtos;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public void deletarProduto(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Produto buscarPeloId(int id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT "
					+ "produtos.id, produtos.nome, "
					+ "produtos.preco, produtos.quantidade "
					+ " from produtos where id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Produto produto = instanciarProduto(rs);
				return produto;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	public Produto instanciarProduto(ResultSet rs) throws SQLException {
		Produto produto = new Produto();
		produto.setId(rs.getInt("id"));
		produto.setNome(rs.getString("nome"));
		produto.setPreco(rs.getDouble("preco"));
		produto.setQuantidade(rs.getInt("quantidade"));
		return produto;
	}

}
