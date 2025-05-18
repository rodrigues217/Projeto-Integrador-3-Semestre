package org.example.Controller;

import jakarta.persistence.EntityManager;
import org.example.Model.CategoriaProdutoMODEL;
import org.example.Model.ProdutosMODEL;
import org.example.DAO.CategoriaProdutoDAO;
import org.example.DAO.ProdutosDAO;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ProdutosService {

    private final ProdutosDAO produtosRepository;
    private final CategoriaProdutoDAO categoriaProdutoDAO;

    public ProdutosService(EntityManager em) {
        this.produtosRepository = new ProdutosDAO(em);
        this.categoriaProdutoDAO = new CategoriaProdutoDAO(em);
    }

    public void cadastrarProduto(Scanner scanner) {
        scanner.nextLine(); // Limpa buffer
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

        produtosRepository.salvar(produto);
        System.out.println("Produto cadastrado com sucesso!");
        System.out.println("ID do produto cadastrado: " + produto.getId());
    }

    public void listarProdutos() {
        List<ProdutosMODEL> produtos = produtosRepository.buscarTodos();
        List<ProdutosMODEL> produtosMODELClassificados = CurvaABC.classificar(produtos);

        System.out.println("\n*** LISTA DE PRODUTOS ***");
        for (ProdutosMODEL produto : produtosMODELClassificados) {
            System.out.print("ID: " + produto.getId() +
                    ", Nome: " + produto.getNome() +
                    ", Estoque: " + produto.getEstoque() +
                    ", Quantidade Vendida: " + produto.getQuantidade_vendida() +
                    ", Categoria: " + produto.getCategoria() +
                    ", Valor Consumo: " + produto.getValorConsumo());

            Set<CategoriaProdutoMODEL> categorias = produto.getCategoriasProduto();
            System.out.print(", Categorias: ");
            if (categorias != null && !categorias.isEmpty()) {
                categorias.forEach(c -> System.out.print(c.getNome() + " "));
            } else {
                System.out.print("Nenhuma");
            }
            System.out.println();
        }
    }

    public void adicionarEstoque(Scanner scanner) {
        System.out.print("Informe o ID do produto: ");
        long idProduto = scanner.nextLong();
        System.out.print("Informe a quantidade a ser adicionada ao estoque: ");
        int quantidade = scanner.nextInt();
        produtosRepository.adicionarEstoque(idProduto, quantidade);
    }
}
