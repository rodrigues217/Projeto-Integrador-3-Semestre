package org.example.View;

import org.example.Model.CategoriaProdutoMODEL;
import org.example.Model.ProdutosMODEL;

import java.util.List;
import java.util.Scanner;

public class ProdutoView {

    private Scanner scanner = new Scanner(System.in);

    public ProdutosMODEL pedirDadosProduto() {
        System.out.print("Digite o nome do produto: ");
        String nome = scanner.nextLine();

        System.out.print("Digite o preço do produto: ");
        while (!scanner.hasNextDouble()) {
            System.out.print("Digite um valor válido para o preço: ");
            scanner.next();
        }
        double preco = scanner.nextDouble();

        System.out.print("Digite a quantidade em estoque: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Digite um número válido para a quantidade: ");
            scanner.next();
        }
        int estoque = scanner.nextInt();
        scanner.nextLine(); // limpar buffer

        ProdutosMODEL produto = new ProdutosMODEL();
        produto.setNome(nome);
        produto.setValor(preco);
        produto.setEstoque(estoque);

        return produto;
    }

    public CategoriaProdutoMODEL pedirDadosCategoria() {
        System.out.print("Digite o nome da nova categoria: ");
        String nomeCategoria = scanner.nextLine();

        CategoriaProdutoMODEL categoria = new CategoriaProdutoMODEL();
        categoria.setNome(nomeCategoria);

        return categoria;
    }

    public void mostrarCategorias(List<CategoriaProdutoMODEL> categorias) {
        System.out.println("Categorias disponíveis:");
        for (CategoriaProdutoMODEL categoria : categorias) {
            System.out.println(categoria.getId() + ": " + categoria.getNome());
        }
    }

    public Long pedirIdCategoria() {
        System.out.print("Digite o ID da categoria desejada: ");
        while (!scanner.hasNextLong()) {
            System.out.print("Digite um ID válido: ");
            scanner.next();
        }
        Long id = scanner.nextLong();
        scanner.nextLine(); // limpar buffer
        return id;
    }

    public void mostrarMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    public void mostrarListaProdutos(List<ProdutosMODEL> produtos) {
        System.out.println("\n--- Lista de Produtos ---");
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }
        for (ProdutosMODEL produto : produtos) {
            System.out.println("ID: " + produto.getId() +
                    " | Nome: " + produto.getNome() +
                    " | Preço: " + produto.getValor() +
                    " | Estoque: " + produto.getEstoque() +
                    " | Categoria: " + (produto.getCategoriaProdutos() != null ? produto.getCategoriaProdutos() : "Nenhuma"));
        }
    }

    public long pedirIdProduto() {
        System.out.print("Digite o ID do produto: ");
        while (!scanner.hasNextLong()) {
            System.out.print("Por favor, digite um número válido para o ID: ");
            scanner.next();
        }
        long id = scanner.nextLong();
        scanner.nextLine(); // limpar buffer
        return id;
    }

    public int pedirQuantidade() {
        System.out.print("Digite a quantidade para adicionar ao estoque: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Por favor, digite um número válido para a quantidade: ");
            scanner.next();
        }
        int quantidade = scanner.nextInt();
        scanner.nextLine(); // limpar buffer
        return quantidade;
    }

    public String pedirConfirmacao(String mensagem) {
        System.out.print(mensagem + " ");
        return scanner.nextLine();
    }
}
