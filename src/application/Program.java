package application;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import model.dao.DaoFactory;
import model.dao.ProdutoDao;
import model.entities.Produto;

public class Program {

	// PROGRAMA PRINCIPAL

	public static void main(String[] args) {

		Locale.setDefault(Locale.US);
		Scanner scanner = new Scanner(System.in);

		System.out.println("Bem vindo ao Gerenciador de Estoques");
		int menuOp = 0;
		do {
			mostrarMenu();
			menuOp = escolherOp(scanner);
			executarOp(menuOp, scanner);
		} while (menuOp != 0);
		System.out.println("\nPrograma encerrado. Volte sempre.");
		
		scanner.close();
		
	}

	// PROCESSO DE ESCOLHA DE OPÇÂO

	public static void mostrarMenu() {
		System.out.println("\n[0] - Sair");
		System.out.println("[1] - Adicionar produto ao estoque");
		System.out.println("[2] - Atualizar preço de um produto");
		System.out.println("[3] - Atualizar quantidade de um produto");
		System.out.println("[4] - Listar todos produtos cadastrados");
		System.out.println("[5] - Remover produto cadastrado");
	}

	public static int escolherOp(Scanner scanner) {
		int op = 0;
		boolean inputValido = false;
		while (!inputValido) {
			try {
				System.out.print("\nDigite a opção desejada: ");
				op = scanner.nextInt();
				testarOp(op);
				inputValido = true;
			} catch (InputMismatchException e) {
				System.out.println("\nInput inválido. Tente novamente.");
				scanner.next();
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
		}
		return op;
	}

	public static void testarOp(int op) {
		if (op < 0 || op > 5) {
			throw new IllegalArgumentException("\nOpção inválida. Tente novamente");
		}
	}

	// EXECUÇÃO DA OPÇÃO ESCOLHIDA

	public static void executarOp(int menuOp, Scanner scanner) {
		ProdutoDao produtoDao = DaoFactory.createProdutoDao();
		switch (menuOp) {
		case 1:
			adicionarProduto(scanner, produtoDao);
			break;
		case 2:
			atulaizarPrecoProduto(scanner, produtoDao);
			break;
		case 3:
			atulaizarQuantidadeProduto(scanner, produtoDao);
			break;
		case 4:
			listarTodosProdutos(scanner, produtoDao);
			break;
		case 5:
			removerProduto(scanner, produtoDao);
			break;
		}
	}

	// ADICIONAR PRODUTO

	public static void adicionarProduto(Scanner scanner, ProdutoDao produtoDao) {
		Produto produto = obterProduto(scanner);
		produtoDao.adicionarProduto(produto);
	}

	public static Produto obterProduto(Scanner scanner) {
		Produto produto = null;
		boolean inputValido = false;
		while (!inputValido) {
			String nome = obterNome(scanner);
			double preco = obterPreco(scanner);
			int quantidade = obterQuantidade(scanner);
			produto = new Produto(nome, preco, quantidade);
			inputValido = true;
		}
		return produto;
	}

	public static String obterNome(Scanner scanner) {
		String nome = " ";
		boolean inputValido = false;
		boolean adicionarNextLine = true;
		while (!inputValido) {
			try {
				System.out.print("\nNOME: ");
				if (adicionarNextLine) {
					scanner.nextLine();
				}
				nome = scanner.nextLine();
				boolean resultadoTeste = testarString(nome);
				if (resultadoTeste) {
					adicionarNextLine = false;
					throw new InputMismatchException("\nNão é permitido esse input ficar em branco. Tente novamente.");
				}
				inputValido = true;
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
			}
		}
		return nome;
	}

	public static boolean testarString(String nome) {
		boolean teste = false;
		if (nome.isBlank()) {
			teste = true;
		}
		return teste;
	}

	public static double obterPreco(Scanner scanner) {
		double preco = 0;
		boolean inputValido = false;
		while (!inputValido) {
			try {
				System.out.print("PREÇO: ");
				preco = scanner.nextDouble();
				testarDouble(preco);
				inputValido = true;
			} catch (InputMismatchException e) {
				System.out.println("\nInput inválido. Tente novamente.\n");
				scanner.next();
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
		}
		return preco;
	}

	public static void testarDouble(double preco) {
		if (preco <= 0) {
			throw new IllegalArgumentException("\nPreço inválido. Tente novamente.\n");
		}
	}

	public static int obterQuantidade(Scanner scanner) {
		int quantidade = 0;
		boolean inputValido = false;
		while (!inputValido) {
			try {
				System.out.print("QUANTIDADE: ");
				quantidade = scanner.nextInt();
				testarInt(quantidade);
				inputValido = true;
			} catch (InputMismatchException e) {
				System.out.println("\nInput inválido. Tente novamente.\n");
				scanner.next();
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
		}
		return quantidade;
	}

	public static void testarInt(int quantidade) {

		if (quantidade < 0) {
			throw new IllegalArgumentException("\nQuantidade inválida. Tente novamente.\n");
		}
	}

	// ATUALIZAR PREÇO PRODUTO

	public static void atulaizarPrecoProduto(Scanner scanner, ProdutoDao produtoDao) {
		List<Produto> produtos = produtoDao.listarTodosProdutos();
		if (produtos.isEmpty()) {
			System.out.println("\nNão há nenhum produto cadastrado no sistema.");
		} else {
			char op = ' ';
			do {
				System.out.print("\nDigite o id do produto: ");
				int id = scanner.nextInt();
				Produto produto = produtoDao.buscarPeloId(id);
				if (produto == null) {
					System.out.println("\nProduto não encontrado. Deseja tentar novamete ?");
					op = obterOp(scanner);
				} else {
					System.out.println(produto);
					System.out.println("\nDIGITE O VALOR DO CAMPO A ATUALIZAR. ");
					double preco = obterPreco(scanner);
					produto.setPreco(preco);
					produtoDao.atualizarPreco(produto);
					System.out.println("\nPreço atualizado com sucesso.");
					op = 'n';
				}
			} while (op != 'n');
		}
	}

	public static char obterOp(Scanner scanner) {
		char op = ' ';
		boolean inputValido = false;
		while (!inputValido) {
			try {
				System.out.print("Digite 's' para sim e 'n' para não: ");
				op = scanner.next().toLowerCase().charAt(0);
				testarOp(op);
				inputValido = true;
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			} catch (InputMismatchException e) {
				System.out.println("\nInput inválido. Tente novamente.");
				scanner.next();
			}
		}
		return op;
	}

	public static void testarOp(char op) {
		if (op != 's' && op != 'n') {
			throw new IllegalArgumentException("\nOpção inválida. Tente novamente.");
		}
	}

	// ATUALIZAR QUANTIDADE PRODUTO

	public static void atulaizarQuantidadeProduto(Scanner scanner, ProdutoDao produtoDao) {
		List<Produto> produtos = produtoDao.listarTodosProdutos();
		if (produtos.isEmpty()) {
			System.out.println("\nNão há nenhum produto cadastrado no sistema.");
		} else {
			char op = ' ';
			do {
				System.out.print("\nDigite o id do produto: ");
				int id = scanner.nextInt();
				Produto produto = produtoDao.buscarPeloId(id);
				if (produto == null) {
					System.out.println("\nProduto não encontrado. Deseja tentar novamete ?");
					op = obterOp(scanner);
				} else {
					System.out.println(produto);
					System.out.println("\nDIGITE O VALOR DO CAMPO A ATUALIZAR. ");
					int quantidade = obterQuantidade(scanner);
					produto.setQuantidade(quantidade);
					produtoDao.atualizarQuantidade(produto);
					System.out.println("\nQuantidade atualizada com sucesso.");
					op = 'n';
				}
			} while (op != 'n');
		}
	}

	// LISTAR TODOS PRODUTOS

	public static void listarTodosProdutos(Scanner scanner, ProdutoDao produtoDao) {
		List<Produto> produtos = produtoDao.listarTodosProdutos();
		if (produtos.isEmpty()) {
			System.out.println("\nNão há nenhum produto cadastrado no sistema.");
		} else {
			System.out.println();
			produtos.forEach(produto -> System.out.println(produto.toString2()));
		}
	}

	// REMOVER PRODUTO

	public static void removerProduto(Scanner scanner, ProdutoDao produtoDao) {
		List<Produto> produtos = produtoDao.listarTodosProdutos();
		if (produtos.isEmpty()) {
			System.out.println("\nNão há nenhum produto cadastrado no sistema.");
		} else {
			int id = obterId(scanner);
			if (!checarId(id, produtos)) {
				produtoDao.deletarProduto(id);
				System.out.println("\nProduto deletado com sucesso.");
			} else {
				System.out.println("\nID de produto inexistente.");
			}
		}
	}

	public static int obterId(Scanner scanner) {
		int id = 0;
		boolean inputValido = false;
		while (!inputValido) {
			try {
				System.out.print("\nDigite o ID do produto a excluir: ");
				id = scanner.nextInt();
				testarId(id);
				inputValido = true;
			} catch (InputMismatchException e) {
				System.out.println("\nInput inválido. Tente novamente.");
				scanner.next();
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
		}
		return id;
	}

	public static void testarId(int id) {
		if (id <= 0) {
			throw new IllegalArgumentException("\nID inválido. Tente novamente");
		}
	}

	public static boolean checarId(int id, List<Produto> produtos) {
		 Optional<Produto> produto = produtos.stream().filter(p -> p.getId() == id).findFirst();
		 return produto.isEmpty();
	}

}
