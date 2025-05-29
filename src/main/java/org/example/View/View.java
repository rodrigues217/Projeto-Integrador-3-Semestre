package org.example.View;

import org.example.Controller.*;
import org.example.Model.Entity.ProdutosMODEL;
import java.util.List;
import java.util.Scanner;

public class View {

    public void menuInicial() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Menu Principal ===");
            System.out.println("1. Venda");
            System.out.println("2. Gerenciar Estoque");
            System.out.println("3. Gerenciar Categorias");
            System.out.println("4. Gerenciar Setores");
            System.out.println("5. Gerenciar Compradores");
            System.out.println("6. Gerenciar AuditoriaVendas");
            System.out.println("7. Gerenciar Usuarios");
            System.out.println("8. Gerenciar Funcionários");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");
            String escolha = scanner.nextLine();

            switch (escolha) {
                case "1" -> mostrarMenuVenda(scanner);
                case "2" -> mostrarMenuEstoque(scanner);
                case "3" -> mostrarMenuCategorias(scanner);
                case "4" -> mostrarMenuSetores(scanner);
                case "5" -> mostrarmenuComprador(scanner);
                case "6" -> mostrarMenuAuditoria(scanner);
                case "7" -> mostrarMenuUsuario(scanner);
                case "8" -> mostrarMenuFuncionario(scanner);// <- Novo menu
                case "0" -> {
                    System.out.println("Saindo...");
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }
    public void mostrarMenuVenda(Scanner scanner) {
        VendaController vendaController = new VendaController();
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
        ProdutoController produtoController = new ProdutoController();
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
       CategoriaProdutoController categoriaController = new CategoriaProdutoController();
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
        SetorController setorController = new SetorController();
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
        CompradorController compradorController = new CompradorController();
      while (true) {
            System.out.println("\n--- Menu Comprador ---");
            System.out.println("1. Cadastrar comprador");
            System.out.println("2. Listar compradores");
            System.out.println("3. Buscar comprador por CPF");
            System.out.println("4. Atualizar comprador");
            System.out.println("5. Remover comprador");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> compradorController.criarComprador();
                case "2" -> compradorController.listarCompradores();
                case "3" -> compradorController.buscarCompradorPorCPF();
                case "4" -> compradorController.atualizarComprador() ;
                case "5" -> compradorController.deletarComprador();
                case "0" -> { return; }
                default  -> System.out.println("Opção inválida.");
            }
        }
    }

    public void mostrarMenuAuditoria(Scanner scanner) {
        AuditoriaVendaController auditoriaController = new AuditoriaVendaController();

        while (true) {
            System.out.println("\n--- Menu Auditoria de Vendas ---");
            System.out.println("1. Listar todas as auditorias");
            System.out.println("2. Buscar auditorias por comprador");
            System.out.println("3. Buscar auditorias por funcionário");
            System.out.println("4. Buscar auditorias por produto");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> auditoriaController.listarAuditorias();
                case "2" -> auditoriaController.buscarPorComprador(scanner);
                case "3" -> auditoriaController.buscarPorFuncionario(scanner);
                case "4" -> auditoriaController.buscarPorProduto(scanner);
                case "0" -> { return; }
                default  -> System.out.println("Opção inválida.");
            }
        }
    }

    public void mostrarMenuUsuario(Scanner scanner) {
        UsuarioController usuarioController = new UsuarioController();

        while (true) {
            System.out.println("\n--- Menu Usuário ---");
            System.out.println("1. Criar usuário");
            System.out.println("2. Listar usuários");
            System.out.println("3. Atualizar usuário");
            System.out.println("4. Deletar usuário");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> usuarioController.criarUsuario();
                case "2" -> usuarioController.listarUsuarios();
                case "3" -> usuarioController.atualizarUsuario();
                case "4" -> usuarioController.deletarUsuario();
                case "0" -> { return; }
                default  -> System.out.println("Opção inválida.");
            }
        }
    }

    public void mostrarMenuFuncionario(Scanner scanner) {
        FuncionarioController funcionarioController = new FuncionarioController();

        while (true) {
            System.out.println("\n--- Menu Funcionário ---");
            System.out.println("1. Criar funcionário");
            System.out.println("2. Listar funcionários");
            System.out.println("4. Buscar funcionário por CPF");
            System.out.println("5. Atualizar funcionário");
            System.out.println("6. Trocar usuário do funcionário");
            System.out.println("7. Trocar setor do funcionário");
            System.out.println("7. Deletar funcionário");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> funcionarioController.criarFuncionario();
                case "2" -> funcionarioController.listarFuncionarios();
                case "4" -> funcionarioController.buscarFuncionarioPorCPF();
                case "5" -> funcionarioController.atualizarFuncionario();
                case "6" -> funcionarioController.trocarUsuarioDeFuncionario();
                case "7" -> funcionarioController.trocarSetorDeFuncionario();
                case "8" -> funcionarioController.deletarFuncionario(scanner);
                case "0" -> { return; }
                default  -> System.out.println("Opção inválida.");
            }
        }
    }




}
