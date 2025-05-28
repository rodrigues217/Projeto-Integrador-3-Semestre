package org.example.View;

import jakarta.persistence.EntityManager;
import org.example.Controller.*;
import org.example.Model.Entity.CategoriaProdutoMODEL;
import org.example.Model.Entity.CompradorMODEL;
import org.example.Model.Entity.FuncionarioMODEL;
import org.example.Model.Entity.ProdutosMODEL;
import org.example.Model.Repository.CompradorRepository;
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
            System.out.println("5. Gerenciar Compradores");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");
            String escolha = scanner.nextLine();

            switch (escolha) {
                case "1" -> mostrarMenuVenda(scanner);
                case "2" -> mostrarMenuEstoque(scanner);
                case "3" -> mostrarMenuCategorias(scanner);
                case "4" -> mostrarMenuSetores(scanner);
                case "5" -> mostrarmenuComprador(scanner);// <- Novo menu
                case "0" -> {
                    System.out.println("Saindo...");
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }
    public void mostrarMenuVenda(Scanner scanner) {
        while (true) {
            System.out.println("\n=== Menu de Vendas ===");
            System.out.println("1. Realizar nova venda");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            String escolha = scanner.nextLine();

            switch (escolha) {
                case "1" -> vendaController.iniciarProcessoDeVenda(scanner);
                case "0" -> { return; }
                default -> System.out.println("Opção inválida.");
            }
        }
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

    public void mostrarmenuComprador(Scanner scanner) {

      while (true) {
            System.out.println("\n--- Menu Comprador ---");
            System.out.println("1. Cadastrar comprador");
            System.out.println("2. Listar compradores");
            System.out.println("3. Atualizar comprador");
            System.out.println("4. Remover comprador");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> compradorController.criarComprador();
                case "2" -> compradorController.listarCompradores();
                case "3" -> compradorController.atualizarComprador() ;
                case "4" -> compradorController.deletarComprador();
                case "0" -> { return; }
                default  -> System.out.println("Opção inválida.");
            }
        }
    }


}
