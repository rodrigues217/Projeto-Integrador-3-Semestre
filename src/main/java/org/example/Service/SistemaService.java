package org.example.Service;
import jakarta.persistence.EntityManager;
import org.example.entities.*;
import org.example.repository.*;
import java.time.LocalDateTime;
import java.util.*;

public class SistemaService {


        private final EntityManager em;
        private final ProdutosRepository produtosRepository;
        private final UsuarioRepository usuarioRepository;
        private final SetorRepository setorRepository;
        private final FuncionarioRepository funcionarioRepository;

        public SistemaService(EntityManager em) {
            this.em = em;
            this.produtosRepository = new ProdutosRepository(em);
            this.usuarioRepository = new UsuarioRepository(em);
            this.setorRepository = new SetorRepository(em);
            this.funcionarioRepository = new FuncionarioRepository(em);
        }

        public Usuario fazerLoginAdmin(Scanner scanner) {
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

                    adminLogado.setUltimoLogin(LocalDateTime.now());
                    usuarioRepository.salvar(adminLogado);

                    System.out.println("Login bem-sucedido!\n");
                }
            }
            return adminLogado;
        }

        public void menuPrincipal(Scanner scanner, Usuario adminLogado) {
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
                    case 1 -> cadastrarProduto(scanner);
                    case 2 -> registrarVenda(scanner, adminLogado);
                    case 3 -> listarProdutos();
                    case 4 -> adicionarEstoque(scanner);
                    case 5 -> System.out.println("Lucro total do dia: R$ " + org.example.Service.LucroService.getLucroTotalDoDia());
                    case 6 -> menuColaboradores(scanner);
                    case 7 -> {
                        try {
                            em.getTransaction().begin();
                            criarCategoria(scanner);
                            em.getTransaction().commit();
                        } catch (Exception e) {
                            em.getTransaction().rollback();
                            System.out.println("Erro ao criar categoria: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    case 8 -> {
                        try {
                            em.getTransaction().begin();
                            associarProdutosACategoria(scanner);
                            em.getTransaction().commit();
                        } catch (Exception e) {
                            em.getTransaction().rollback();
                            System.out.println("Erro ao associar produtos: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    case 9 -> listarCategoriasComProdutos(scanner);
                    case 10 -> {
                        try {
                            em.getTransaction().begin();
                            desassociarProdutosDaCategoria(scanner);
                            em.getTransaction().commit();
                        } catch (Exception e) {
                            em.getTransaction().rollback();
                            System.out.println("Erro ao desassociar produtos da categoria: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    case 11 -> {
                        System.out.println("Saindo...");
                        em.close();
                        return;
                    }
                    default -> System.out.println("Opção inválida! Tente novamente.");
                }
            }
        }

        // Os métodos abaixo são os mesmos que você já tinha, mas agora no serviço
        private void cadastrarProduto(Scanner scanner) {
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

        private void registrarVenda(Scanner scanner, Usuario vendedor) {
            System.out.print("Informe o ID do produto: ");
            long idProduto = scanner.nextLong();
            System.out.print("Informe a quantidade de venda: ");
            int quantidade = scanner.nextInt();

            produtosRepository.registrarVenda(idProduto, quantidade, vendedor);

            List<Produtos> produtos = produtosRepository.buscarTodos();
            List<Produtos> produtosClassificados = org.example.Service.CurvaABC.classificar(produtos);
            produtosRepository.atualizarProdutos(produtosClassificados);

            System.out.println("Venda registrada com sucesso!");
        }

        private void listarProdutos() {
            List<Produtos> produtos = produtosRepository.buscarTodos();
            List<Produtos> produtosClassificados = org.example.Service.CurvaABC.classificar(produtos);

            System.out.println("\n*** LISTA DE PRODUTOS ***");
            for (Produtos produto : produtosClassificados) {
                System.out.print("ID: " + produto.getId() +
                        ", Nome: " + produto.getNome() +
                        ", Estoque: " + produto.getEstoque() +
                        ", Quantidade Vendida: " + produto.getQuantidade_vendida() +
                        ", Categoria: " + produto.getCategoria() +
                        ", Valor Consumo: " + produto.getValorConsumo());

                Set<CategoriaProduto> categorias = produto.getCategoriasProduto();
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
            produtosRepository.adicionarEstoque(idProduto, quantidade);
        }

        private void criarCategoria(Scanner scanner) {
            CategoriaProdutoRepository categoriaProdutoRepo = new CategoriaProdutoRepository(em);
            categoriaProdutoRepo.setEm(em);

            scanner.nextLine(); // Limpar buffer
            System.out.println("*** CRIAÇÃO DE CATEGORIA ***");
            System.out.print("Digite o nome da nova categoria: ");
            String nomeCategoria = scanner.nextLine();

            CategoriaProduto categoria = new CategoriaProduto();
            categoria.setNome(nomeCategoria);

            categoriaProdutoRepo.salvar(categoria);
            System.out.println("Categoria '" + nomeCategoria + "' criada com sucesso!");
        }

        private void associarProdutosACategoria(Scanner scanner) {
            ProdutosRepository produtosRepo = new ProdutosRepository(em);
            CategoriaProdutoRepository categoriaProdutoRepo = new CategoriaProdutoRepository(em);
            categoriaProdutoRepo.setEm(em);

            scanner.nextLine();
            System.out.print("Digite o nome da categoria para associar produtos: ");
            String nomeCategoria = scanner.nextLine();

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

            List<Long> idsProdutos = new ArrayList<>();
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

        private void desassociarProdutosDaCategoria(Scanner scanner) {
            ProdutosRepository produtosRepo = new ProdutosRepository(em);
            CategoriaProdutoRepository categoriaProdutoRepo = new CategoriaProdutoRepository(em);
            categoriaProdutoRepo.setEm(em);

            scanner.nextLine();
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
                    iterator.remove();
                    produto.getCategoriasProduto().remove(categoria);
                }
            }

            categoriaProdutoRepo.salvar(categoria);
            System.out.println("Produtos desassociados da categoria '" + nomeCategoria + "' com sucesso!");
        }

        private void menuColaboradores(Scanner scanner) {
            while (true) {
                System.out.println("\n*** MENU DE COLABORADORES ***");
                System.out.println("1 - Cadastrar Setor");
                System.out.println("2 - Cadastrar Funcionário");
                System.out.println("3 - Listar Setores");
                System.out.println("4 - Listar Funcionários");
                System.out.println("5 - Voltar");
                System.out.print("Escolha uma opção: ");
                int opcao = scanner.nextInt();  switch (opcao) {
                    case 1 -> setorRepository.cadastrarSetor(scanner);
                    case 2 -> funcionarioRepository.cadastrarFuncionario(scanner);
                    case 3 -> setorRepository.listarSetores();
                    case 4 -> funcionarioRepository.listarFuncionarios();
                    case 5 -> {
                        return;
                    }
                    default -> System.out.println("Opção inválida! Tente novamente.");
                }
            }
        }

        private void listarCategoriasComProdutos(Scanner scanner) {
            CategoriaProdutoRepository categoriaProdutoRepo = new CategoriaProdutoRepository(em);
            categoriaProdutoRepo.listarCategoriasComProdutos(scanner);
        }
}
