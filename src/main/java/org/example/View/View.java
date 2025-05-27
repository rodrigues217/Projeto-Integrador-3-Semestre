package org.example.View;

import jakarta.persistence.EntityManager;
import org.example.Controller.CategoriaProdutoController;
import org.example.Controller.CompradorController;
import org.example.Controller.ProdutoController;
import org.example.Controller.VendaController;
import org.example.Model.Entity.CategoriaProdutoMODEL;
import org.example.Model.Entity.CompradorMODEL;
import org.example.Model.Entity.FuncionarioMODEL;
import org.example.Model.Entity.ProdutosMODEL;
import org.example.Model.Util.HibernateUtil;

import java.util.List;
import java.util.Scanner;

public class View {
    private final VendaController vendaController = new VendaController();
    private final CompradorController compradorController = new CompradorController();
    private final ProdutoController produtoController = new ProdutoController();
    private final CategoriaProdutoController categoriaController = new CategoriaProdutoController();


    public void menuInicial() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Menu Principal ===");
            System.out.println("1. Venda");
            System.out.println("2. Gerenciar Estoque");
            System.out.println("3. Gerenciar Categorias");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");
            String escolha = scanner.nextLine();

            switch (escolha) {
                case "1" -> mostrarMenuVenda(scanner);
                case "2" -> mostrarMenuEstoque(scanner);
                case "3" -> mostrarMenuCategorias(scanner); // <- Novo menu
                case "0" -> {
                    System.out.println("Saindo...");
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }


    public void mostrarMenuVenda(Scanner scanner) {
        EntityManager em = HibernateUtil.getEntityManager();

        while (true) {
            System.out.println("\n=== Realizar Venda ===");

            // Produto
            ProdutosMODEL produto = null;
            while (produto == null) {
                System.out.print("ID do Produto: ");
                String produtoInput = scanner.nextLine().trim();
                if (produtoInput.isBlank()) {
                    System.out.println("ID do Produto não pode estar em branco.");
                    continue;
                }
                try {
                    Long produtoId = Long.parseLong(produtoInput);
                    produto = em.find(ProdutosMODEL.class, produtoId);
                    if (produto == null) {
                        System.out.println("Produto não encontrado.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("ID do Produto inválido.");
                }
            }

            // Funcionário
            FuncionarioMODEL funcionario = null;
            while (funcionario == null) {
                System.out.print("ID do Funcionário: ");
                String funcionarioInput = scanner.nextLine().trim();
                if (funcionarioInput.isBlank()) {
                    System.out.println("ID do Funcionário não pode estar em branco.");
                    continue;
                }
                try {
                    Long funcionarioId = Long.parseLong(funcionarioInput);
                    funcionario = em.find(FuncionarioMODEL.class, funcionarioId);
                    if (funcionario == null) {
                        System.out.println("Funcionário não encontrado.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("ID do Funcionário inválido.");
                }
            }

            // Quantidade
            int quantidade = 0;
            while (quantidade <= 0) {
                System.out.print("Quantidade: ");
                String quantidadeInput = scanner.nextLine().trim();
                if (quantidadeInput.isBlank()) {
                    System.out.println("Quantidade não pode estar em branco.");
                    continue;
                }
                try {
                    quantidade = Integer.parseInt(quantidadeInput);
                    if (quantidade <= 0) {
                        System.out.println("Quantidade deve ser maior que zero.");
                    } else if (produto.getEstoque() < quantidade) {
                        System.out.println("Estoque insuficiente. Estoque atual: " + produto.getEstoque());
                        quantidade = 0;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Quantidade inválida.");
                }
            }

            // Comprador
            Long compradorId = null;
            while (true) {
                System.out.println("ID do Comprador:");
                System.out.println(" - Deixe em branco para venda sem comprador");
                System.out.println(" - Digite 'novo' para cadastrar um comprador agora");
                System.out.print("Entrada: ");
                String input = scanner.nextLine().trim();

                if (input.isBlank()) {
                    break;
                }

                if (input.equalsIgnoreCase("novo")) {
                    System.out.print("Nome do comprador: ");
                    String nome = scanner.nextLine();
                    System.out.print("Telefone: ");
                    String telefone = scanner.nextLine();

                    CompradorMODEL novoComprador = compradorController.criarComprador(nome, telefone);
                    compradorId = novoComprador.getId();
                    System.out.println("Comprador cadastrado com ID: " + compradorId);
                    break;
                }

                try {
                    Long id = Long.parseLong(input);
                    CompradorMODEL comprador = em.find(CompradorMODEL.class, id);
                    if (comprador == null) {
                        System.out.println("Comprador não encontrado. Tente novamente.");
                    } else {
                        compradorId = comprador.getId();
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("ID do comprador inválido. Tente novamente.");
                }
            }

            String resultado = vendaController.processarVenda(produto.getId(), funcionario.getId(), quantidade, compradorId);

            if (resultado.equals("SUCESSO")) {
                System.out.println("Venda realizada com sucesso!");
                break;
            } else {
                System.out.println("Erro: " + resultado);
                System.out.println("Tente novamente.\n");
            }
        }

        em.close();
    }

    public void mostrarMenuEstoque(Scanner scanner) {
        while (true) {
            System.out.println("\n=== Gerenciar Estoque ===");
            System.out.println("1. Cadastrar Produto");
            System.out.println("2. Ver Produtos em Estoque");
            System.out.println("3. Adicionar Estoque");
            System.out.println("4. Remover Estoque");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> produtoController.criarProdutoComCategoria(scanner);
                case "2" -> {
                    List<ProdutosMODEL> produtos = produtoController.listarProdutosComCategoria();
                    System.out.println("\n--- Produtos em Estoque ---");
                    produtos.forEach(p -> System.out.printf("ID: %d | Nome: %s | Estoque: %d | Categoria: %s%n",
                            p.getId(), p.getNome(), p.getEstoque(), p.getCategoria().getNome()));
                }
                case "3" -> produtoController.atualizarEstoque(scanner, true);
                case "4" -> produtoController.atualizarEstoque(scanner, false);
                case "0" -> {
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }
    public void mostrarMenuCategorias(Scanner scanner) {
        while (true) {
            System.out.println("\n=== Gerenciar Categorias ===");
            System.out.println("1. Criar nova categoria");
            System.out.println("2. Trocar categoria de um produto");
            System.out.println("3. Atualizar nome de uma categoria");
            System.out.println("4. Remover categoria");
            System.out.println("5. Ver categorias existentes"); // <- NOVA OPÇÃO
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            String escolha = scanner.nextLine();

            switch (escolha) {
                case "1" -> categoriaController.criarCategoria(scanner);
                case "2" -> categoriaController.trocarCategoriaDeProduto(scanner);
                case "3" -> categoriaController.atualizarNomeCategoria(scanner);
                case "4" -> categoriaController.removerCategoria(scanner);
                case "5" -> categoriaController.listarCategorias(); // <- NOVA CHAMADA
                case "0" -> { return; }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

}
