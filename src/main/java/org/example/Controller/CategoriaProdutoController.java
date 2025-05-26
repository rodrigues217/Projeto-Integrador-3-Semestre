/*package org.example.Controller;

import jakarta.persistence.EntityManager;
import org.example.Model.CategoriaProdutoMODEL;
import org.example.Model.ProdutosMODEL;
import org.example.Model.Repository.CategoriaProdutoRepository;
import org.example.Model.Repository.ProdutosRepository;

import java.util.*;

public class CategoriaProdutoController {

    private final EntityManager em;

    public CategoriaProdutoController(EntityManager em) {
        this.em = em;
    }

        private void associarProdutosACategoria(Scanner scanner) {
        ProdutosRepository produtosRepo = new ProdutosRepository(em);
        CategoriaProdutoRepository categoriaProdutoRepo = new CategoriaProdutoRepository(em);

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
    public void listarCategoriasComProdutos() {
        List<CategoriaProdutoMODEL> categorias = em.createQuery(
                        "SELECT DISTINCT c FROM CategoriaProduto c LEFT JOIN FETCH c.produtos", CategoriaProdutoMODEL.class)
                .getResultList();

        for (CategoriaProdutoMODEL categoria : categorias) {
            System.out.println("Categoria: " + categoria.getNome());
            if (categoria.getProdutos().isEmpty()) {
                System.out.println("  Nenhum produto nesta categoria.");
            } else {
                for (ProdutosMODEL produto : categoria.getProdutos()) {
                    System.out.println("  - Produto: " + produto.getNome());
                }
            }
        }
    }

}
*/