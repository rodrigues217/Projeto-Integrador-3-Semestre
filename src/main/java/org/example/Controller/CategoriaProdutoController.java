package org.example.Controller;

import org.example.Model.Entity.CategoriaProdutoMODEL;
import org.example.Model.Service.CategoriaProdutoService;

import java.util.List;

public class CategoriaProdutoController {

    private final CategoriaProdutoService categoriaService = new CategoriaProdutoService();

    public void criarCategoria(String nome) throws Exception {
        categoriaService.criarCategoria(nome);
    }

    public List<CategoriaProdutoMODEL> listarCategorias() {
        return categoriaService.listarCategorias();
    }

    public List<CategoriaProdutoMODEL> listarCategoriasComProdutos() {
        return categoriaService.listarCategoriasComProdutos();
    }

    public void trocarCategoriaDeProduto(Long idProduto, Long idNovaCategoria) throws Exception {
        categoriaService.trocarCategoriaDeProduto(idProduto, idNovaCategoria);
    }

    public void atualizarNomeCategoria(Long id, String novoNome) throws Exception {
        categoriaService.atualizarNomeCategoria(id, novoNome);
    }

    public void removerCategoria(Long id) throws Exception {
        categoriaService.removerCategoria(id);
    }
}
