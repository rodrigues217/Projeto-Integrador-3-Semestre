package org.example.Controller;

import org.example.Model.Entity.CategoriaProdutoMODEL;
import org.example.Model.Service.CategoriaProdutoService;

import java.util.List;


public class CategoriaProdutoController {

    private final CategoriaProdutoService categoriaService;

    public CategoriaProdutoController() {
        this.categoriaService = new CategoriaProdutoService();
    }

    public void criarCategoria(String nome) throws Exception {
        categoriaService.criarCategoria(nome);
    }


    public void trocarCategoriaDeProduto(Long idProduto, Long idNovaCategoria) throws Exception {
        categoriaService.trocarCategoriaDeProduto(idProduto, idNovaCategoria);
    }

    public void atualizarNomeCategoria(Long idCategoria, String novoNome) throws Exception {
        categoriaService.atualizarNomeCategoria(idCategoria, novoNome);
    }

    public void removerCategoria(Long idCategoria) throws Exception {
        categoriaService.removerCategoria(idCategoria);
    }

    public List<CategoriaProdutoMODEL> listarCategorias() {
        return categoriaService.listarCategorias();
    }
}
