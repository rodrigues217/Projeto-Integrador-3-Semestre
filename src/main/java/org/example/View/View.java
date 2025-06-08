package org.example.View;

import org.example.Controller.*;
import org.example.Model.Entity.ProdutosMODEL;
import org.example.Model.Entity.UsuarioMODEL; // Importar para listar
import java.util.List;
import java.util.Scanner;

public class View {

    public void menuInicial() {
        Scanner scanner = new Scanner(System.in);
        // --- Login inicial --- (Assumindo que o login é feito antes de chamar menuInicial)
        // UsuarioController usuarioController = new UsuarioController();
        // System.out.println("Bem-vindo ao Sistema de Estoque!");
        // UsuarioMODEL usuarioLogado = usuarioController.fazerLogin(scanner);
        // if (usuarioLogado == null) {
        //     System.out.println("Falha no login. Encerrando.");
        //     return;
        // }
        // System.out.println("Login como: " + usuarioLogado.getLogin() + " (" + usuarioLogado.getPerfil() + ")");
        // --- Fim Login ---

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
                case "7" -> mostrarMenuUsuario(scanner); // Chamada corrigida
                case "8" -> mostrarMenuFuncionario(scanner);
                case "0" -> {
                    System.out.println("Saindo...");
                    scanner.close(); // Fechar o scanner ao sair
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
                case "2" -> produtoController.listarProdutosComCategoria(scanner);
                case "3" -> produtoController.atualizarEstoque(scanner, true);
                case "4" -> produtoController.atualizarEstoque(scanner, false);
                case "0" -> { return; }
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
            System.out.println("5. Ver categorias existentes");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            String escolha = scanner.nextLine();

            switch (escolha) {
                case "1" -> categoriaController.criarCategoria(scanner);
                case "2" -> categoriaController.trocarCategoriaDeProduto(scanner);
                case "3" -> categoriaController.atualizarNomeCategoria(scanner);
                case "4" -> categoriaController.removerCategoria(scanner);
                case "5" -> categoriaController.listarCategorias();
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

    // --- Método Corrigido ---
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
                case "1":
                    System.out.print("Digite o login do novo usuário: ");
                    String login = scanner.nextLine();
                    System.out.print("Digite a senha: ");
                    String senha = scanner.nextLine();
                    System.out.print("Digite o perfil (ADM, GERENTE, OPERADOR): ");
                    String perfil = scanner.nextLine().toUpperCase();
                    usuarioController.criarUsuario(login, senha, perfil);
                    break;
                case "2":
                    List<UsuarioMODEL> usuarios = usuarioController.listarUsuarios();
                    if (usuarios.isEmpty()) {
                        System.out.println("Não há usuários cadastrados.");
                    } else {
                        System.out.println("\n--- Lista de Usuários ---");
                        for (UsuarioMODEL u : usuarios) {
                            System.out.println("ID: " + u.getId() + ", Login: " + u.getLogin() + ", Perfil: " + u.getPerfil());
                        }
                        System.out.println("------------------------");
                    }
                    break;
                case "3":
                    System.out.print("Digite o ID do usuário a ser atualizado: ");
                    Long idAtualizar;
                    try {
                        idAtualizar = Long.parseLong(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("ID inválido.");
                        break;
                    }
                    System.out.print("Digite o novo login: ");
                    String novoLogin = scanner.nextLine();
                    System.out.print("Digite a nova senha: ");
                    String novaSenha = scanner.nextLine();
                    System.out.print("Digite o novo perfil (ADM, GERENTE, OPERADOR): ");
                    String novoPerfil = scanner.nextLine().toUpperCase();
                    usuarioController.atualizarUsuario(idAtualizar, novoLogin, novaSenha, novoPerfil);
                    break;
                case "4":
                    System.out.print("Digite o ID do usuário a ser deletado: ");
                    Long idDeletar;
                    try {
                        idDeletar = Long.parseLong(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("ID inválido.");
                        break;
                    }
                    // Adiciona confirmação antes de deletar
                    System.out.print("Tem certeza que deseja deletar o usuário com ID " + idDeletar + "? (s/n): ");
                    String confirmacao = scanner.nextLine();
                    if (confirmacao.equalsIgnoreCase("s")) {
                        boolean deletado = usuarioController.deletarUsuario(idDeletar);
                        if (!deletado) {
                            // Mensagem de erro já é exibida pelo service/controller
                            System.out.println("Falha ao deletar usuário (verifique se está vinculado a um funcionário).");
                        }
                    } else {
                        System.out.println("Operação cancelada.");
                    }
                    break;
                case "0":
                    return; // Volta ao menu anterior
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    public void mostrarMenuFuncionario(Scanner scanner) {
        FuncionarioController funcionarioController = new FuncionarioController();

        while (true) {
            System.out.println("\n--- Menu Funcionário ---");
            System.out.println("1. Criar funcionário");
            System.out.println("2. Listar funcionários");
            System.out.println("3. Buscar funcionário por CPF");
            System.out.println("4. Atualizar funcionário");
            System.out.println("5. Trocar usuário do funcionário");
            System.out.println("6. Trocar setor do funcionário");
            System.out.println("7. Deletar funcionário");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> funcionarioController.criarFuncionario();
                case "2" -> funcionarioController.listarFuncionarios();
                case "3" -> funcionarioController.buscarFuncionarioPorCPF();
                case "4" -> funcionarioController.atualizarFuncionario();
                case "5" -> funcionarioController.trocarUsuarioDeFuncionario();
                case "6" -> funcionarioController.trocarSetorDeFuncionario();
                case "7" -> funcionarioController.deletarFuncionario(scanner);
                case "0" -> { return; }
                default  -> System.out.println("Opção inválida.");
            }
        }
    }
}

