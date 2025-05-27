package org.example.Model.Service;

import jakarta.persistence.EntityManager;
import org.example.Model.Entity.CategoriaProdutoMODEL;
import org.example.Model.Entity.ProdutosMODEL;
import org.example.Model.Repository.CategoriaProdutoRepository;
import org.example.Model.Repository.ProdutosRepository;
import org.example.Model.Util.HibernateUtil;

import java.util.List;
import java.util.Scanner;

public class CategoriaProdutoService {

    private final CategoriaProdutoRepository categoriaRepository = new CategoriaProdutoRepository();
    private final ProdutosRepository produtosRepository = new ProdutosRepository();

    public void criarCategoria(Scanner scanner) {
        System.out.print("Digite o nome da nova categoria: ");
        String nome = scanner.nextLine();
        CategoriaProdutoMODEL categoria = new CategoriaProdutoMODEL(nome);
        categoriaRepository.salvar(categoria);
        System.out.println("Categoria criada com sucesso.");
    }

    public void trocarCategoriaDeProduto(Scanner scanner) {
        List<ProdutosMODEL> produtos = produtosRepository.listarTodos();
        List<CategoriaProdutoMODEL> categorias = categoriaRepository.listarTodos();

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto encontrado. Cadastre um produto antes de trocar categoria.");
            return;
        }

        if (categorias.isEmpty()) {
            System.out.println("Nenhuma categoria encontrada. Cadastre uma categoria antes de trocar.");
            return;
        }

        System.out.println("\n=== Produtos Disponíveis ===");
        for (ProdutosMODEL p : produtos) {
            System.out.printf("ID: %d | Nome: %s | Categoria atual: %s%n",
                    p.getId(), p.getNome(), p.getCategoria().getNome());
        }

        System.out.print("Informe o ID do produto a ser alterado: ");
        Long idProduto = Long.parseLong(scanner.nextLine());
        ProdutosMODEL produto = produtosRepository.buscarPorId(idProduto);

        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        System.out.println("\n=== Categorias Disponíveis ===");
        for (CategoriaProdutoMODEL c : categorias) {
            System.out.printf("ID: %d | Nome: %s%n", c.getId(), c.getNome());
        }

        System.out.print("Informe o ID da nova categoria: ");
        Long idNovaCategoria = Long.parseLong(scanner.nextLine());
        CategoriaProdutoMODEL novaCategoria = categoriaRepository.buscarPorId(idNovaCategoria);

        if (novaCategoria == null) {
            System.out.println("Categoria não encontrada.");
            return;
        }

        produto.setCategoria(novaCategoria);
        produtosRepository.atualizar(produto);
        System.out.println("Categoria do produto atualizada com sucesso.");
    }

    public void atualizarNomeCategoria(Scanner scanner) {
        List<CategoriaProdutoMODEL> categorias = categoriaRepository.listarTodos();

        if (categorias.isEmpty()) {
            System.out.println("Nenhuma categoria cadastrada.");
            return;
        }

        System.out.println("\n=== Categorias Disponíveis ===");
        for (CategoriaProdutoMODEL c : categorias) {
            System.out.printf("ID: %d | Nome: %s%n", c.getId(), c.getNome());
        }

        System.out.print("Informe o ID da categoria que deseja renomear: ");
        Long id = Long.parseLong(scanner.nextLine());
        CategoriaProdutoMODEL categoria = categoriaRepository.buscarPorId(id);

        if (categoria == null) {
            System.out.println("Categoria não encontrada.");
            return;
        }

        System.out.print("Digite o novo nome: ");
        String novoNome = scanner.nextLine();
        categoria.setNome(novoNome);
        categoriaRepository.atualizar(categoria);
        System.out.println("Nome da categoria atualizado com sucesso.");
    }

    public void removerCategoria(Scanner scanner) {
        List<CategoriaProdutoMODEL> categorias = categoriaRepository.listarTodos();

        if (categorias.isEmpty()) {
            System.out.println("Nenhuma categoria cadastrada.");
            return;
        }

        System.out.println("\n=== Categorias Disponíveis ===");
        for (CategoriaProdutoMODEL c : categorias) {
            System.out.printf("ID: %d | Nome: %s%n", c.getId(), c.getNome());
        }

        System.out.print("Informe o ID da categoria que deseja remover: ");
        Long id = Long.parseLong(scanner.nextLine());
        CategoriaProdutoMODEL categoria = categoriaRepository.buscarPorId(id);

        if (categoria == null) {
            System.out.println("Categoria não encontrada.");
            return;
        }

        boolean temProdutos = produtosRepository.listarTodos()
                .stream()
                .anyMatch(p -> p.getCategoria().getId().equals(id));

        if (temProdutos) {
            System.out.println("Não é possível remover a categoria. Existem produtos associados a ela.");
            return;
        }

        categoriaRepository.deletar(id);
        System.out.println("Categoria removida com sucesso.");
    }

    public void listarCategorias() {
        List<CategoriaProdutoMODEL> categorias = categoriaRepository.listarTodos();

        if (categorias.isEmpty()) {
            System.out.println("Nenhuma categoria cadastrada.");
            return;
        }

        System.out.println("\n=== Categorias Cadastradas ===");
        for (CategoriaProdutoMODEL c : categorias) {
            System.out.printf("ID: %d | Nome: %s%n", c.getId(), c.getNome());
        }
    }

   }
