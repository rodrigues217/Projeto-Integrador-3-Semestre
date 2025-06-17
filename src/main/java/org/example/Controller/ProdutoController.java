package org.example.Controller;

import org.example.Model.Entity.ProdutosMODEL;
import org.example.Model.Service.ProdutoService;

import java.util.List;


public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController() {
        this.produtoService = new ProdutoService();
    }


    public void criarProdutoComCategoria(String nomeProduto,
                                         Double valor,
                                         Integer estoque,
                                         String codigoProduto,
                                         Long categoriaId) throws Exception {
        produtoService.criarProdutoComCategoria(nomeProduto, valor, estoque, codigoProduto, categoriaId);
    }


    public List<ProdutosMODEL> listarProdutosComCategoria() {
        return produtoService.listarProdutosComCategoria();
    }


    public void atualizarEstoque(Long idProduto, int quantidade, boolean adicionar) throws Exception {
        produtoService.atualizarEstoque(idProduto, quantidade, adicionar);
    }

    public List<ProdutoService.ProdutoComValorTotal> listarPorCurvaABC() {
        return produtoService.listarProdutosPorCurvaABC();
    }
}
