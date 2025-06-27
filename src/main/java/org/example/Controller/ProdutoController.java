package org.example.Controller;

import org.example.Model.Entity.ProdutosMODEL;
import org.example.Model.Service.ProdutoService;

import org.example.Model.Service.ProdutoService.ProdutoComValorTotal;

import java.util.List;

public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController() {
        this.produtoService = new ProdutoService();
    }

    public List<ProdutosMODEL> listarProdutos() {
        return produtoService.listarProdutosComCategoria();
    }

    public List<ProdutoComValorTotal> listarProdutosPorCurvaABC() {
        return produtoService.listarProdutosPorCurvaABC();
    }

    public void atualizarEstoque(Long idProduto, int quantidade, boolean adicionar) throws Exception {
        produtoService.atualizarEstoque(idProduto, quantidade, adicionar);
    }

    public List<ProdutosMODEL> listarProdutosPorCategoria(Long idCategoria) {
        return produtoService.listarProdutosPorCategoria(idCategoria);
    }

    public void criarProduto(String nomeProduto, Double valor, Integer estoque, String codProd, Long categoriaId) throws Exception {
        produtoService.criarProdutoComCategoria(nomeProduto, valor, estoque, codProd, categoriaId);
    }

    public void excluirProduto(Long idProduto) throws Exception {
        produtoService.excluirProduto(idProduto);
    }


}
