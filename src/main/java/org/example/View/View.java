package org.example.View;

import jakarta.persistence.EntityManager;
import org.example.Controller.*;
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
    private final SetorController setorController = new SetorController();


    public void menuInicial() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Menu Principal ===");
            System.out.println("1. Venda");
            System.out.println("2. Gerenciar Estoque");
            System.out.println("3. Gerenciar Categorias");
            System.out.println("4. Gerenciar Setores");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");
            String escolha = scanner.nextLine();

            switch (escolha) {
                case "1" -> mostrarMenuVenda(scanner);
                case "2" -> mostrarMenuEstoque(scanner);
                case "3" -> mostrarMenuCategorias(scanner);
                case "4" -> mostrarMenuSetores(scanner);// <- Novo menu
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

        ProdutosMODEL produto = null;
        FuncionarioMODEL funcionario = null;
        Integer quantidade = null;
        Long compradorId = null;

        etapa:
        while (true) {
            System.out.println("\n=== Realizar Venda ===");

            // 1. Produto
            while (produto == null) {
                System.out.print("ID do Produto (ou 'cancelar'): ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("cancelar")) break etapa;

                try {
                    Long id = Long.parseLong(input);
                    produto = em.find(ProdutosMODEL.class, id);
                    if (produto == null) {
                        System.out.println("Produto não encontrado.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("ID inválido.");
                }
            }

            // 2. Funcionário
            while (funcionario == null) {
                System.out.print("ID do Funcionário ('voltar' / 'cancelar'): ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("voltar")) {
                    produto = null;
                    continue etapa;
                }
                if (input.equalsIgnoreCase("cancelar")) break etapa;

                try {
                    Long id = Long.parseLong(input);
                    funcionario = em.find(FuncionarioMODEL.class, id);
                    if (funcionario == null) {
                        System.out.println("Funcionário não encontrado.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("ID inválido.");
                }
            }

            // 3. Quantidade
            while (quantidade == null || quantidade <= 0) {
                System.out.print("Quantidade ('voltar' / 'cancelar'): ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("voltar")) {
                    funcionario = null;
                    continue etapa;
                }
                if (input.equalsIgnoreCase("cancelar")) break etapa;

                try {
                    int qtd = Integer.parseInt(input);
                    if (qtd <= 0) {
                        System.out.println("Deve ser maior que zero.");
                    } else if (qtd > produto.getEstoque()) {
                        System.out.println("Estoque insuficiente. Estoque atual: " + produto.getEstoque());
                    } else {
                        quantidade = qtd;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Quantidade inválida.");
                }
            }

            // 4. Comprador
            while (true) {
                System.out.println("ID do Comprador:");
                System.out.println(" - Deixe em branco para venda sem comprador");
                System.out.println(" - Digite 'novo' para cadastrar um novo");
                System.out.println(" - Digite 'voltar' para voltar");
                System.out.println(" - Digite 'cancelar' para cancelar");
                System.out.print("Entrada: ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("voltar")) {
                    quantidade = null;
                    continue etapa;
                }
                if (input.equalsIgnoreCase("cancelar")) break etapa;

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
                        System.out.println("Comprador não encontrado.");
                    } else {
                        compradorId = comprador.getId();
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("ID inválido.");
                }
            }

            // Finalizar venda
            String resultado = vendaController.processarVenda(produto.getId(), funcionario.getId(), quantidade, compradorId);
            if (resultado.equals("SUCESSO")) {
                System.out.println("Venda realizada com sucesso!");
            } else {
                System.out.println("Erro: " + resultado);
            }
            break;
        }

        em.close();
        System.out.println("Operação finalizada.");
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

    public void mostrarMenuSetores(Scanner scanner) {

        while (true) {
            System.out.println("\n==== Gerenciar Setor ====");
            System.out.println("1. Criar setor");
            System.out.println("2. Listar setores");
            System.out.println("3. Atualizar setor");
            System.out.println("4. Deletar setor");
            System.out.println("0. Voltar");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> setorController.criarSetor();
                case "2" -> setorController.listarSetores();
                case "3" -> setorController.atualizarSetor();
                case "4" -> setorController.deletarSetor();
                case "0" -> { return; }
                default  -> System.out.println("Opção inválida.");
            }
        }
    }


}
