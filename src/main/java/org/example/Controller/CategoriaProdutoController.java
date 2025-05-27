package org.example.Controller;

import org.example.Model.Service.CategoriaProdutoService;

import java.util.Scanner;

public class CategoriaProdutoController {

    private final CategoriaProdutoService categoriaService = new CategoriaProdutoService();

    public void criarCategoria(Scanner scanner) {
        categoriaService.criarCategoria(scanner);
    }

    public void trocarCategoriaDeProduto(Scanner scanner) {
        categoriaService.trocarCategoriaDeProduto(scanner);
    }

    public void atualizarNomeCategoria(Scanner scanner) {
        categoriaService.atualizarNomeCategoria(scanner);
    }

    public void removerCategoria(Scanner scanner) {
        categoriaService.removerCategoria(scanner);
    }

    public void listarCategorias() {
        categoriaService.listarCategorias();
    }


}
