package org.example.Controller;

import jakarta.persistence.EntityManager;
import org.example.Model.*;
import org.example.Model.Repository.*;
import java.util.*;

public class SistemaService {

    private final EntityManager em;
    private final ProdutosRepository produtosRepository;
    private final SetorRepository setorRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final AuthService authService;

    public SistemaService(EntityManager em) {
        this.em = em;
        this.produtosRepository = new ProdutosRepository(em);
        this.setorRepository = new SetorRepository(em);
        this.funcionarioRepository = new FuncionarioRepository(em);
        this.authService = new AuthService(em);
    }

    public UsuarioMODEL login(Scanner scanner) {
        return authService.fazerLogin(scanner);
    }

    public void menuPrincipal(Scanner scanner, UsuarioMODEL adminLogado) {
        while (true) {
            System.out.println("\n*** MENU INTERATIVO ***");
            System.out.println("1 - Produtos e Categorias");
            System.out.println("2 - Registrar Venda");
            System.out.println("3 - Mostrar Lucro do Dia");
            System.out.println("4 - Colaboradores");
            System.out.println("5 - Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();

            switch (opcao) {
                case 1 -> menuProdutosECategorias(scanner, adminLogado);
                case 2 -> registrarVenda(scanner, adminLogado);
                case 3 -> System.out.println("Lucro total do dia: R$ " + LucroService.getLucroTotalDoDia());
                case 4 -> menuColaboradores(scanner);
                case 5 -> {
                    System.out.println("Saindo...");
                    em.close();
                    return;
                }
                default -> System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }

    private void menuProdutosECategorias(Scanner scanner, UsuarioMODEL adminLogado) {
        while (true) {
            System.out.println("\n*** MENU DE PRODUTOS E CATEGORIAS ***");
            System.out.println("1 - Cadastrar Produto");
            System.out.println("2 - Listar Produtos");
            System.out.println("3 - Adicionar Estoque");
            System.out.println("4 - Criar Categoria");
            System.out.println("5 - Associar Produtos a Categoria");
            System.out.println("6 - Listar Categorias com Produtos");
            System.out.println("7 - Desassociar Produtos de Categoria");
            System.out.println("8 - Voltar");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();

            switch (opcao) {
                case 1 -> cadastrarProduto(scanner);
                case 2 -> listarProdutos();
                case 3 -> adicionarEstoque(scanner);
                case 4 -> {
                    try {
                        em.getTransaction().begin();
                        criarCategoria(scanner);
                        em.getTransaction().commit();
                    } catch (Exception e) {
                        em.getTransaction().rollback();
                        System.out.println("Erro ao criar categoria: " + e.getMessage());
                    }
                }
                case 5 -> {
                    try {
                        em.getTransaction().begin();
                        associarProdutosACategoria(scanner);
                        em.getTransaction().commit();
                    } catch (Exception e) {
                        em.getTransaction().rollback();
                        System.out.println("Erro ao associar produtos: " + e.getMessage());
                    }
                }
                case 6 -> listarCategoriasComProdutos(scanner);
                case 7 -> {
                    try {
                        em.getTransaction().begin();
                        desassociarProdutosDaCategoria(scanner);
                        em.getTransaction().commit();
                    } catch (Exception e) {
                        em.getTransaction().rollback();
                        System.out.println("Erro ao desassociar produtos: " + e.getMessage());
                    }
                }
                case 8 -> {
                    return;
                }
                default -> System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }

    private void cadastrarProduto(Scanner scanner) {
        scanner.nextLine();
        System.out.print("Digite o nome do produto: ");
        String nome = scanner.nextLine();
        System.out.print("Digite o valor do produto: ");
        double valor = scanner.nextDouble();
        System.out.print("Digite a quantidade em estoque: ");
        int estoque = scanner.nextInt();

        ProdutosMODEL produto = new ProdutosMODEL();
        produto.setNome(nome);
        produto.setValor(valor);
        produto.setEstoque(estoque);
        produto.setQuantidade_vendida(0);

        produtosRepository.CriarProduto(produto);
        System.out.println("Produto cadastrado com sucesso!");
        System.out.println("ID do produto cadastrado: " + produto.getId());
    }

    private void registrarVenda(Scanner scanner, UsuarioMODEL vendedor) {
        System.out.print("Informe o ID do produto: ");
        long idProduto = scanner.nextLong();
        System.out.print("Informe a quantidade de venda: ");
        int quantidade = scanner.nextInt();

        produtosRepository.registrarVenda(idProduto, quantidade, vendedor);

        List<ProdutosMODEL> produtos = produtosRepository.buscarTodosProdutos();
        List<ProdutosMODEL> produtosMODELClassificados = CurvaABC.classificar(produtos);
        produtosRepository.atualizarProdutos(produtosMODELClassificados);

        System.out.println("Venda registrada com sucesso!");
    }

    private void listarProdutos() {
        List<ProdutosMODEL> produtos = produtosRepository.buscarTodosProdutos();
        List<ProdutosMODEL> produtosMODELClassificados = CurvaABC.classificar(produtos);

        System.out.println("\n*** LISTA DE PRODUTOS ***");
        for (ProdutosMODEL produto : produtosMODELClassificados) {
            System.out.print("ID: " + produto.getId() +
                    ", Nome: " + produto.getNome() +
                    ", Estoque: " + produto.getEstoque() +
                    ", Quantidade Vendida: " + produto.getQuantidade_vendida() +
                    ", Categoria: " + produto.getCategoriaProdutos() +
                    ", Valor Consumo: " + produto.getValorConsumo());

            Set<CategoriaProdutoMODEL> categorias = produto.getCategoriaProdutos();
            System.out.print(", Categorias: ");
            if (categorias != null && !categorias.isEmpty()) {
                categorias.forEach(c -> System.out.print(c.getNome() + " "));
            } else {
                System.out.print("Nenhuma");
            }
            System.out.println();
        }
    }

    private void adicionarEstoque(Scanner scanner) {
        System.out.print("Informe o ID do produto: ");
        long idProduto = scanner.nextLong();
        System.out.print("Informe a quantidade a ser adicionada ao estoque: ");
        int quantidade = scanner.nextInt();
        produtosRepository.adicionarEstoqueProduto(idProduto, quantidade);
    }

    private void criarCategoria(Scanner scanner) {
        CategoriaProdutoRepository categoriaProdutoRepo = new CategoriaProdutoRepository(em);
        categoriaProdutoRepo.setEm(em);

        scanner.nextLine();
        System.out.println("*** CRIAÇÃO DE CATEGORIA ***");
        System.out.print("Digite o nome da nova categoria: ");
        String nomeCategoria = scanner.nextLine();

        CategoriaProdutoMODEL categoria = new CategoriaProdutoMODEL();
        categoria.setNome(nomeCategoria);

        categoriaProdutoRepo.CriarCategoria(categoria);
        System.out.println("Categoria '" + nomeCategoria + "' criada com sucesso!");
    }

    private void associarProdutosACategoria(Scanner scanner) {
        ProdutosRepository produtosRepo = new ProdutosRepository(em);
        CategoriaProdutoRepository categoriaProdutoRepo = new CategoriaProdutoRepository(em);
        categoriaProdutoRepo.setEm(em);

        scanner.nextLine();
        System.out.print("Digite o nome da categoria para associar produtos: ");
        String nomeCategoria = scanner.nextLine();

        CategoriaProdutoMODEL categoria = categoriaProdutoRepo.buscarCategoriaPorNome(nomeCategoria);
        if (categoria == null) {
            System.out.println("Categoria '" + nomeCategoria + "' não encontrada.");
            return;
        }

        List<ProdutosMODEL> todosProdutos = produtosRepo.buscarTodosProdutos();
        if (todosProdutos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado. Cadastre produtos antes.");
            return;
        }

        System.out.println("\nProdutos disponíveis:");
        for (ProdutosMODEL produto : todosProdutos) {
            System.out.println("ID: " + produto.getId() + " - Nome: " + produto.getNome());
        }

        System.out.print("Informe os IDs dos produtos para associar à categoria (separados por vírgula): ");
        String idsInput = scanner.nextLine();

        List<Long> idsProdutos = new ArrayList<>();
        for (String idStr : idsInput.split(",")) {
            try {
                idsProdutos.add(Long.parseLong(idStr.trim()));
            } catch (NumberFormatException e) {
                System.out.println("ID inválido ignorado: " + idStr.trim());
            }
        }

        for (Long id : idsProdutos) {
            ProdutosMODEL produto = produtosRepo.buscarProdutoPorId(id);
            if (produto != null) {
                categoria.getProdutos().add(produto);
                produto.getCategoriaProdutos().add(categoria);
            } else {
                System.out.println("Produto com ID " + id + " não encontrado.");
            }
        }

        categoriaProdutoRepo.CriarCategoria(categoria);
        System.out.println("Produtos associados à categoria '" + nomeCategoria + "' com sucesso!");
    }

    private void desassociarProdutosDaCategoria(Scanner scanner) {
        ProdutosRepository produtosRepo = new ProdutosRepository(em);
        CategoriaProdutoRepository categoriaProdutoRepo = new CategoriaProdutoRepository(em);
        categoriaProdutoRepo.setEm(em);

        scanner.nextLine();
        System.out.print("Digite o nome da categoria para desassociar produtos: ");
        String nomeCategoria = scanner.nextLine();

        CategoriaProdutoMODEL categoria = categoriaProdutoRepo.buscarCategoriaPorNome(nomeCategoria);
        if (categoria == null) {
            System.out.println("Categoria '" + nomeCategoria + "' não encontrada.");
            return;
        }

        if (categoria.getProdutos().isEmpty()) {
            System.out.println("Nenhum produto está associado a esta categoria.");
            return;
        }

        System.out.println("\nProdutos atualmente associados à categoria:");
        for (ProdutosMODEL produto : categoria.getProdutos()) {
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

        Iterator<ProdutosMODEL> iterator = categoria.getProdutos().iterator();
        while (iterator.hasNext()) {
            ProdutosMODEL produto = iterator.next();
            if (idsParaRemover.contains(produto.getId())) {
                iterator.remove();
                produto.getCategoriaProdutos().remove(categoria);
            }
        }

        categoriaProdutoRepo.CriarCategoria(categoria);
        System.out.println("Produtos desassociados da categoria '" + nomeCategoria + "' com sucesso!");
    }

    private void listarCategoriasComProdutos(Scanner scanner) {
        CategoriaProdutoRepository categoriaProdutoRepo = new CategoriaProdutoRepository(em);
        categoriaProdutoRepo.listarCategoriasComProdutos(scanner);
    }


}
