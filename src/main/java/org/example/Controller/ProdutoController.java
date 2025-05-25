package org.example.Controller;

import org.example.Model.CategoriaProdutoMODEL;
import org.example.Model.ProdutosMODEL;
import org.example.Model.Repository.ProdutosRepository;
import org.example.Model.Repository.CategoriaProdutoRepository;
import org.example.View.ProdutoView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProdutoController {

    private ProdutosRepository produtoRepo;
    private CategoriaProdutoRepository categoriaRepo;
    private ProdutoView produtoView;

    public ProdutoController(ProdutosRepository produtoRepo, CategoriaProdutoRepository categoriaRepo, ProdutoView produtoView) {
        this.produtoRepo = produtoRepo;
        this.categoriaRepo = categoriaRepo;
        this.produtoView = produtoView;
    }

    public void criarProduto() {
        List<CategoriaProdutoMODEL> categorias = categoriaRepo.buscarTodasCategorias();

        if (categorias.isEmpty()) {
            produtoView.mostrarMensagem("Nenhuma categoria encontrada. Você precisa criar uma nova categoria.");
            CategoriaProdutoMODEL novaCategoria = produtoView.pedirDadosCategoria();
            categoriaRepo.CriarCategoria(novaCategoria);
            categorias = List.of(novaCategoria); // atualiza a lista para continuar
        }

        produtoView.mostrarCategorias(categorias);
        Long categoriaId = produtoView.pedirIdCategoria();

        CategoriaProdutoMODEL categoriaEscolhida = categoriaRepo.buscarCategoriaPorId(categoriaId);
        if (categoriaEscolhida == null) {
            produtoView.mostrarMensagem("Categoria inválida. Produto não criado.");
            return;
        }

        ProdutosMODEL produto = produtoView.pedirDadosProduto();

        // Criando um Set com a categoria escolhida
        Set<CategoriaProdutoMODEL> categoriasSet = new HashSet<>();
        categoriasSet.add(categoriaEscolhida);

        // Associando o Set de categorias ao produto
        produto.setCategoriaProdutos(categoriasSet);

        // (Opcional) Adiciona o produto na lista da categoria para manter bidirecional
        categoriaEscolhida.getProdutos().add(produto);

        produtoRepo.CriarProduto(produto);
        produtoView.mostrarMensagem("Produto criado com sucesso e associado à categoria: " + categoriaEscolhida.getNome());
    }

    public void listarProdutos() {
        List<ProdutosMODEL> produtos = produtoRepo.buscarTodosProdutos();
        produtoView.mostrarListaProdutos(produtos);
    }

    public void atualizarEstoque() {
        long id = produtoView.pedirIdProduto();
        ProdutosMODEL produto = produtoRepo.buscarProdutoPorId(id);
        if (produto == null) {
            produtoView.mostrarMensagem("Produto não encontrado!");
            return;
        }
        int quantidade = produtoView.pedirQuantidade();
        produtoRepo.adicionarEstoqueProduto(id, quantidade);
        produtoView.mostrarMensagem("Estoque atualizado com sucesso!");
    }

    public void removerProduto() {
        long id = produtoView.pedirIdProduto();
        ProdutosMODEL produto = produtoRepo.buscarProdutoPorId(id);
        if (produto == null) {
            produtoView.mostrarMensagem("Produto não encontrado!");
            return;
        }
        String confirmacao = produtoView.pedirConfirmacao("Tem certeza que deseja deletar o produto '" + produto.getNome() + "'? (sim/não)");
        if (confirmacao.equalsIgnoreCase("sim")) {
            produtoRepo.removerProduto(id);
            produtoView.mostrarMensagem("Produto removido com sucesso!");
        } else {
            produtoView.mostrarMensagem("Operação cancelada.");
        }
    }
}
