package org.example.Model.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.example.Model.Entity.CategoriaProdutoMODEL;
import org.example.Model.Entity.ProdutosMODEL;
import org.example.Model.Repository.CategoriaProdutoRepository;
import org.example.Model.Repository.ProdutosRepository;
import org.example.Model.Util.HibernateUtil; // Import HibernateUtil

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ProdutoService {

    private final ProdutosRepository produtosRepository = new ProdutosRepository();
    private final CategoriaProdutoRepository categoriaRepository = new CategoriaProdutoRepository();

    // --- Métodos para Swing ---

    /**
     * Busca um produto pelo seu código.
     * @param codProd O código do produto.
     * @return Optional contendo o produto se encontrado, Optional.empty() caso contrário.
     */
    public Optional<ProdutosMODEL> buscarPorCodProd(String codProd) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            // Usando Optional para um retorno mais claro
            return Optional.ofNullable(
                    em.createQuery("SELECT p FROM Produtos p WHERE p.codProd = :cod", ProdutosMODEL.class)
                            .setParameter("cod", codProd)
                            .getResultStream()
                            .findFirst()
                            .orElse(null)
            );
        } catch (Exception e) {
            System.err.println("Erro ao buscar produto por código: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty(); // Retorna vazio em caso de erro
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Busca um produto pelo seu ID.
     * @param id O ID do produto.
     * @return Optional contendo o produto se encontrado, Optional.empty() caso contrário.
     */
    public Optional<ProdutosMODEL> buscarPorId(Long id) {
        // Delega para o repositório, mas envolve em Optional para consistência
        return Optional.ofNullable(produtosRepository.buscarPorId(id));
    }

    /**
     * Lista todos os produtos.
     * @return Lista de produtos.
     */
    public List<ProdutosMODEL> listarTodos() {
        return produtosRepository.listarTodos();
    }

    // --- Métodos existentes (baseados em Scanner) ---
    // Manter por compatibilidade ou refatorar se a interface de console for removida

    public void criarProdutoComCategoria(Scanner scanner) {
        List<CategoriaProdutoMODEL> categorias = categoriaRepository.listarTodos();

        if (categorias.isEmpty()) {
            System.out.println("Nenhuma categoria encontrada. É necessário cadastrar uma antes de criar produtos.");
            System.out.print("Digite o nome da nova categoria: ");
            String nomeCategoria = scanner.nextLine();
            CategoriaProdutoMODEL novaCategoria = new CategoriaProdutoMODEL(nomeCategoria);
            categoriaRepository.salvar(novaCategoria);
            categorias = categoriaRepository.listarTodos(); // atualiza lista
        }

        System.out.println("\n=== Categorias Disponíveis ===");
        for (CategoriaProdutoMODEL cat : categorias) {
            System.out.printf("ID: %d | Nome: %s%n", cat.getId(), cat.getNome());
        }

        System.out.print("Informe o ID da categoria para associar ao produto: ");
        Long categoriaId = Long.parseLong(scanner.nextLine());
        CategoriaProdutoMODEL categoria = categoriaRepository.buscarPorId(categoriaId);

        if (categoria == null) {
            System.out.println("Categoria não encontrada. Produto não criado.");
            return;
        }

        System.out.print("Nome do produto: ");
        String nomeProduto = scanner.nextLine();

        System.out.print("Valor do produto: ");
        Double valor = Double.parseDouble(scanner.nextLine());

        System.out.print("Quantidade em estoque: ");
        Integer estoque = Integer.parseInt(scanner.nextLine());

        System.out.print("Código do produto: ");
        String codProd = (scanner.nextLine());

        ProdutosMODEL produto = new ProdutosMODEL(nomeProduto, valor, estoque,codProd, categoria);
        produtosRepository.salvar(produto);

        System.out.println("Produto criado com sucesso.");
    }

    public void listarProdutosComCategoria(Scanner scanner) {
        List<ProdutosMODEL> produtos = produtosRepository.listarTodos();

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            System.out.print("Deseja criar um novo produto agora? (s/n): ");
            String opcao = scanner.nextLine();

            if (opcao.equalsIgnoreCase("s")) {
                criarProdutoComCategoria(scanner);
            }
            return;
        }

        System.out.println("\n--- Produtos em Estoque ---");
        for (ProdutosMODEL p : produtos) {
            System.out.printf("ID: %d | Nome: %s | Estoque: %d | Categoria: %s | CodProd: %s%n",
                    p.getId(), p.getNome(), p.getEstoque(), p.getCategoria().getNome(), p.getCodProd());
        }
    }


    public void atualizarEstoque(Scanner scanner, boolean adicionar) {
        List<ProdutosMODEL> produtos = produtosRepository.listarTodos();

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto encontrado.");
            return;
        }

        System.out.println("\n=== Produtos Disponíveis ===");
        for (ProdutosMODEL p : produtos) {
            System.out.printf("ID: %d | Nome: %s | Estoque: %d%n | codProd: %s \n", p.getId(), p.getNome(), p.getEstoque(), p.getCodProd()); // Adicionado \n
        }

        System.out.print("Informe o ID do produto: ");
        Long id = Long.parseLong(scanner.nextLine());
        ProdutosMODEL produto = produtosRepository.buscarPorId(id);

        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        System.out.print("Quantidade: ");
        int qtd = Integer.parseInt(scanner.nextLine());

        int novoEstoque = adicionar ? produto.getEstoque() + qtd : produto.getEstoque() - qtd;
        if (novoEstoque < 0) {
            System.out.println("Estoque insuficiente para remoção.");
            return;
        }

        produto.setEstoque(novoEstoque);
        produtosRepository.atualizar(produto);
        System.out.println("Estoque atualizado com sucesso.");
    }
}

