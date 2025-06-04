package org.example.Controller;

import org.example.Model.Entity.ProdutosMODEL;
import org.example.Model.Service.ProdutoService;

import java.util.List;
import java.util.Scanner;

public class ProdutoController {

    private final ProdutoService produtoService = new ProdutoService();

    public void criarProdutoComCategoria(Scanner scanner) {
        produtoService.criarProdutoComCategoria(scanner);
    }

    public void listarProdutosComCategoria(Scanner scanner) {
        produtoService.listarProdutosComCategoria(scanner);
    }

    public void atualizarEstoque(Scanner scanner, boolean adicionar) {
        produtoService.atualizarEstoque(scanner, adicionar);
    }
}
