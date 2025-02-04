package model.dao;

import db.DB;
import model.dao.impl.ProdutoDaoJDBC;

public class DaoFactory {

	public static ProdutoDao createProdutoDao() {
		return new ProdutoDaoJDBC(DB.getConnection());
	}
	
}
