package org.example;

import jakarta.persistence.EntityManager;
import org.example.Service.CurvaABC;
import org.example.Util.Factory;
import org.example.entities.*;
import org.example.repository.*;

import java.time.LocalDateTime;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        EntityManager em = Factory.getEntityManager();
        ProdutosRepository produtosRepository = new ProdutosRepository(em);
        UsuarioRepository usuarioRepository = new UsuarioRepository(em);
        SetorRepository setorRepository = new SetorRepository(em);
        FuncionarioRepository funcionarioRepository = new FuncionarioRepository(em);
        Scanner scanner = new Scanner(System.in);

        // Inicia login de administrador
        Usuario adminLogado = fazerLoginAdmin(scanner, usuarioRepository);

        // Se o login for bem-sucedido, carrega o menu principal
        if (adminLogado != null) {
            menuPrincipal(scanner, produtosRepository, usuarioRepository, em, adminLogado, setorRepository, funcionarioRepository);
        } else {
            System.out.println("Login falhou! Finalizando o sistema.");
        }
    }

    private static Usuario fazerLoginAdmin(Scanner scanner, UsuarioRepository usuarioRepository) {
        Usuario adminLogado = null;

        while (adminLogado == null) {
            System.out.println("*** LOGIN ADMINISTRADOR ***");
            System.out.print("Login: ");
            String login = scanner.next();
            System.out.print("Senha: ");
            String senha = scanner.next();

            adminLogado = usuarioRepository.autenticar(login, senha);

            if (adminLogado == null || !adminLogado.getLogin().equals("admin")) {
                System.out.println("Credenciais inválidas ou não é administrador.");
            } else {
                System.out.println("Último login registrado: " +
                        (adminLogado.getUltimoLogin() != null ? adminLogado.getUltimoLogin() : "Nunca"));

                // Atualiza o último login
                adminLogado.setUltimoLogin(LocalDateTime.now());
                usuarioRepository.salvar(adminLogado);

                System.out.println("Login bem-sucedido!\n");
            }
        }
        return adminLogado;
    }

    private static void menuPrincipal(Scanner scanner, ProdutosRepository produtosRepository,
                                      UsuarioRepository usuarioRepository, EntityManager em, Usuario adminLogado,
                                      SetorRepository setorRepository, FuncionarioRepository funcionarioRepository) {
        while (true) {
            System.out.println("\n*** MENU INTERATIVO ***");
            System.out.println("1 - Cadastrar Produto");
            System.out.println("2 - Registrar Venda");
            System.out.println("3 - Listar Produtos");
            System.out.println("4 - Adicionar Estoque");
            System.out.println("5 - Mostrar Lucro do Dia");
            System.out.println("6 - Colaboradores");
            System.out.println("7 - Criar Categoria de Produto");
            System.out.println("8 - Associar Produtos a Categoria");
            System.out.println("9 - Listar e deletar Categorias e Produtos");
            System.out.println("10 - Desassociar Produtos de uma Categoria");
            System.out.println("11 - Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    cadastrarProduto(scanner, produtosRepository);
                    break;
                case 2:
                    registrarVenda(scanner, produtosRepository, adminLogado);
                    break;
                case 3:
                    listarProdutos(produtosRepository);
                    break;
                case 4:
                    adicionarEstoque(scanner, produtosRepository);
                    break;
                case 5:
                    System.out.println("Lucro total do dia: R$ " + org.example.Service.LucroService.getLucroTotalDoDia());
                    break;
                case 6:
                    menuColaboradores(scanner, setorRepository, funcionarioRepository);
                    break;
                case 7:
                    try {
                        em.getTransaction().begin(); // Inicia transação
                        criarCategoria(em, scanner);
                        em.getTransaction().commit(); // Commit após sucesso
                    } catch (Exception e) {
                        em.getTransaction().rollback(); // Rollback se erro
                        System.out.println("Erro ao criar categoria: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 8:
                    try {
                        em.getTransaction().begin(); // Inicia transação
                        associarProdutosACategoria(em, scanner);
                        em.getTransaction().commit(); // Commit após sucesso
                    } catch (Exception e) {
                        em.getTransaction().rollback(); // Rollback se erro
                        System.out.println("Erro ao associar produtos: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 9:
                    listarCategoriasComProdutos(scanner, em);
                    break;

                case 10:
                    try {
                        em.getTransaction().begin();
                        desassociarProdutosDaCategoria(em, scanner);
                        em.getTransaction().commit();
                    } catch (Exception e) {
                        em.getTransaction().rollback();
                        System.out.println("Erro ao desassociar produtos da categoria: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;

                case 11:
                    System.out.println("Saindo...");
                    em.close();
                    return;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }

        private static void listarCategoriasComProdutos(Scanner scanner, EntityManager em) {
        CategoriaProdutoRepository categoriaRepo = new CategoriaProdutoRepository(em);
        List<CategoriaProduto> categorias = categoriaRepo.buscarTodos();

        System.out.println("\n*** LISTA DE CATEGORIAS E PRODUTOS ***");
        if (categorias.isEmpty()) {
            System.out.println("Nenhuma categoria encontrada.");
        } else {
            for (CategoriaProduto categoria : categorias) {
                System.out.println("Categoria: " + categoria.getNome());
                if (categoria.getProdutos().isEmpty()) {
                    System.out.println("  Não há produtos nesta categoria.");
                } else {
                    System.out.println("  Produtos: ");
                    for (Produtos produto : categoria.getProdutos()) {
                        System.out.println("    - " + produto.getNome() + " (ID: " + produto.getId() + ")");
                    }
                }
            }
        }

        // Adiciona a opção para excluir uma categoria por nome
        System.out.print("\nDeseja excluir alguma categoria? Digite o nome da categoria ou 'voltar' para retornar: ");
        scanner.nextLine();  // Limpar o buffer
        String nomeCategoriaExcluir = scanner.nextLine();

        if (!nomeCategoriaExcluir.equalsIgnoreCase("voltar")) {
            try {
                em.getTransaction().begin();
                categoriaRepo.deletarCategoriaPorNome(nomeCategoriaExcluir); // Chama o método de deletar por nome
                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
                System.out.println("Erro ao excluir categoria: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }




    private static void menuColaboradores(Scanner scanner, SetorRepository setorRepository, FuncionarioRepository funcionarioRepository) {
        while (true) {
            System.out.println("\n*** MENU DE COLABORADORES ***");
            System.out.println("1 - Cadastrar Setor");
            System.out.println("2 - Cadastrar Funcionário");
            System.out.println("3 - Listar Setores e Funcionários");
            System.out.println("4 - Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    cadastrarSetor(scanner, setorRepository);
                    break;
                case 2:
                    cadastrarFuncionario(scanner, funcionarioRepository, setorRepository);
                    break;
                case 3:
                    listarSetoresComFuncionarios(setorRepository);
                    break;
                case 4:
                    return; // Volta ao menu principal
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }

    private static void adicionarEstoque(Scanner scanner, ProdutosRepository produtosRepository) {
        System.out.print("Informe o ID do produto: ");
        long idProduto = scanner.nextLong();
        System.out.print("Informe a quantidade a ser adicionada ao estoque: ");
        int quantidade = scanner.nextInt();
        produtosRepository.adicionarEstoque(idProduto, quantidade);
    }

    private static void cadastrarProduto(Scanner scanner, ProdutosRepository produtosRepository) {
        scanner.nextLine(); // Limpar buffer
        System.out.print("Digite o nome do produto: ");
        String nome = scanner.nextLine();
        System.out.print("Digite o valor do produto: ");
        double valor = scanner.nextDouble();
        System.out.print("Digite a quantidade em estoque: ");
        int estoque = scanner.nextInt();

        Produtos produto = new Produtos();
        produto.setNome(nome);
        produto.setValor(valor);
        produto.setEstoque(estoque);
        produto.setQuantidade_vendida(0);

        produtosRepository.salvar(produto);
        System.out.println("Produto cadastrado com sucesso!");
        System.out.println("ID do produto cadastrado: " + produto.getId());
    }

    private static void registrarVenda(Scanner scanner, ProdutosRepository produtosRepository, Usuario vendedor) {
        System.out.print("Informe o ID do produto: ");
        long idProduto = scanner.nextLong();
        System.out.print("Informe a quantidade de venda: ");
        int quantidade = scanner.nextInt();

        produtosRepository.registrarVenda(idProduto, quantidade, vendedor);

        List<Produtos> produtos = produtosRepository.buscarTodos();
        List<Produtos> produtosClassificados = CurvaABC.classificar(produtos);
        produtosRepository.atualizarProdutos(produtosClassificados);

        System.out.println("Venda registrada com sucesso!");
    }

    private static void listarProdutos(ProdutosRepository produtosRepository) {
        List<Produtos> produtos = produtosRepository.buscarTodos();
        List<Produtos> produtosClassificados = CurvaABC.classificar(produtos);

        System.out.println("\n*** LISTA DE PRODUTOS ***");
        for (Produtos produto : produtosClassificados) {
            System.out.print("ID: " + produto.getId() +
                    ", Nome: " + produto.getNome() +
                    ", Estoque: " + produto.getEstoque() +
                    ", Quantidade Vendida: " + produto.getQuantidade_vendida() +
                    ", Categoria: " + produto.getCategoria() +
                    ", Valor Consumo: " + produto.getValorConsumo());

            // Exibir categorias associadas
            Set<CategoriaProduto> categorias = produto.getCategoriasProduto();
            System.out.print(", Categorias: ");
            if (categorias != null && !categorias.isEmpty()) {
                categorias.forEach(c -> System.out.print(c.getNome() + " "));
            } else {
                System.out.print("Nenhuma");
            }
            System.out.println(); // Nova linha para cada produto
        }
    }

    public static void criarCategoria(EntityManager em, Scanner scanner) {
        CategoriaProdutoRepository categoriaProdutoRepo = new CategoriaProdutoRepository(em);
        categoriaProdutoRepo.setEm(em); // Injeta manualmente o EntityManager

        scanner.nextLine(); // Limpar o buffer
        System.out.println("*** CRIAÇÃO DE CATEGORIA ***");
        System.out.print("Digite o nome da nova categoria: ");
        String nomeCategoria = scanner.nextLine();

        CategoriaProduto categoria = new CategoriaProduto();
        categoria.setNome(nomeCategoria);

        categoriaProdutoRepo.salvar(categoria);
        System.out.println("Categoria '" + nomeCategoria + "' criada com sucesso!");
    }
    public static void associarProdutosACategoria(EntityManager em, Scanner scanner) {
        ProdutosRepository produtosRepo = new ProdutosRepository(em);
        CategoriaProdutoRepository categoriaProdutoRepo = new CategoriaProdutoRepository(em);
        categoriaProdutoRepo.setEm(em); // Injeta manualmente o EntityManager

        scanner.nextLine(); // Limpar o buffer
        System.out.print("Digite o nome da categoria para associar produtos: ");
        String nomeCategoria = scanner.nextLine();

        // Buscar a categoria
        CategoriaProduto categoria = categoriaProdutoRepo.buscarPorNome(nomeCategoria);
        if (categoria == null) {
            System.out.println("Categoria '" + nomeCategoria + "' não encontrada.");
            return;
        }

        List<Produtos> todosProdutos = produtosRepo.buscarTodos();
        if (todosProdutos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado. Cadastre produtos antes.");
            return;
        }

        System.out.println("\nProdutos disponíveis:");
        for (Produtos produto : todosProdutos) {
            System.out.println("ID: " + produto.getId() + " - Nome: " + produto.getNome());
        }

        System.out.print("Informe os IDs dos produtos para associar à categoria (separados por vírgula): ");
        String idsInput = scanner.nextLine();

        List<Long> idsProdutos = new java.util.ArrayList<>();
        for (String idStr : idsInput.split(",")) {
            try {
                idsProdutos.add(Long.parseLong(idStr.trim()));
            } catch (NumberFormatException e) {
                System.out.println("ID inválido ignorado: " + idStr.trim());
            }
        }

        for (Long id : idsProdutos) {
            Produtos produto = produtosRepo.buscarPorId(id);
            if (produto != null) {
                categoria.getProdutos().add(produto);
                produto.getCategoriasProduto().add(categoria);
            } else {
                System.out.println("Produto com ID " + id + " não encontrado.");
            }
        }

        categoriaProdutoRepo.salvar(categoria);
        System.out.println("Produtos associados à categoria '" + nomeCategoria + "' com sucesso!");
    }
    public static void desassociarProdutosDaCategoria(EntityManager em, Scanner scanner) {
        ProdutosRepository produtosRepo = new ProdutosRepository(em);
        CategoriaProdutoRepository categoriaProdutoRepo = new CategoriaProdutoRepository(em);
        categoriaProdutoRepo.setEm(em); // Injeta manualmente o EntityManager

        scanner.nextLine(); // Limpar o buffer
        System.out.print("Digite o nome da categoria para desassociar produtos: ");
        String nomeCategoria = scanner.nextLine();

        CategoriaProduto categoria = categoriaProdutoRepo.buscarPorNome(nomeCategoria);
        if (categoria == null) {
            System.out.println("Categoria '" + nomeCategoria + "' não encontrada.");
            return;
        }

        if (categoria.getProdutos().isEmpty()) {
            System.out.println("Nenhum produto está associado a esta categoria.");
            return;
        }

        System.out.println("\nProdutos atualmente associados à categoria:");
        for (Produtos produto : categoria.getProdutos()) {
            System.out.println("ID: " + produto.getId() + " - Nome: " + produto.getNome());
        }

        System.out.print("Informe os IDs dos produtos para desassociar da categoria (separados por vírgula): ");
        String idsInput = scanner.nextLine();

        List<Long> idsParaRemover = new ArrayList<>();
        for (String idStr : idsInput.split(",")) {
            try {
                idsParaRemover.add(Long.parseLong(idStr.trim()));
            } catch (NumberFormatException e) {
                System.out.println("ID inválido ignorado: " + idStr.trim());
            }
        }

        Iterator<Produtos> iterator = categoria.getProdutos().iterator();
        while (iterator.hasNext()) {
            Produtos produto = iterator.next();
            if (idsParaRemover.contains(produto.getId())) {
                iterator.remove(); // Remove da categoria
                produto.getCategoriasProduto().remove(categoria); // Remove a categoria do produto
            }
        }

        categoriaProdutoRepo.salvar(categoria);
        System.out.println("Produtos desassociados da categoria '" + nomeCategoria + "' com sucesso!");
    }




    private static void cadastrarSetor(Scanner scanner, SetorRepository setorRepository) {
        scanner.nextLine(); // Limpar buffer
        System.out.print("Digite o nome do setor: ");
        String nome = scanner.nextLine();

        Setor setor = new Setor();
        setor.setNome(nome);

        setorRepository.salvar(setor);
        System.out.println("Setor cadastrado com sucesso!");
    }

    private static void cadastrarFuncionario(Scanner scanner, FuncionarioRepository funcionarioRepository, SetorRepository setorRepository) {
        scanner.nextLine(); // Limpa buffer

        System.out.print("Digite o nome do funcionário: ");
        String nome = scanner.nextLine();

        System.out.print("Digite o endereço do funcionário: ");
        String endereco = scanner.nextLine();

        System.out.print("Digite o documento do funcionário: ");
        String documento = scanner.nextLine();

        // Lista setores disponíveis antes da escolha
        List<Setor> setores = setorRepository.buscarTodos();
        if (setores.isEmpty()) {
            System.out.println("Nenhum setor cadastrado. Cadastre um setor antes de adicionar funcionários.");
            return;
        }

        System.out.println("\nSetores disponíveis:");
        for (Setor setor : setores) {
            System.out.println("ID: " + setor.getId() + " - Nome: " + setor.getNome());
        }

        System.out.print("Informe o setor do funcionário (ID): ");
        long setorId = scanner.nextLong();
        Setor setor = setorRepository.buscarPorId(setorId);

        if (setor == null) {
            System.out.println("Setor não encontrado!");
            return;
        }

        Funcionario funcionario = new Funcionario();
        funcionario.setNome(nome);
        funcionario.setEndereco(endereco);
        funcionario.setDocumento(documento);
        funcionario.setSetor(setor);
        funcionario.setTotalVendas(0.0);

        funcionarioRepository.salvar(funcionario);
        System.out.println("Funcionário cadastrado com sucesso!");
    }


    private static void listarSetoresComFuncionarios(SetorRepository setorRepository) {
        List<Setor> setores = setorRepository.listarSetoresComFuncionarios();

        System.out.println("\n*** LISTA DE SETORES E FUNCIONÁRIOS ***");
        for (Setor setor : setores) {
            System.out.println("Setor: " + setor.getNome());
            if (setor.getFuncionarios().isEmpty()) {
                System.out.println("  Não há funcionários neste setor.");
            } else {
                System.out.println("  Funcionários: ");
                for (Funcionario funcionario : setor.getFuncionarios()) {
                    System.out.println("    - " + funcionario.getNome() +
                            ", Documento: " + funcionario.getDocumento());
                }
            }
        }
    }
}
