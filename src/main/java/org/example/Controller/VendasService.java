package org.example.Controller;

import jakarta.persistence.EntityManager;
import org.example.Model.ProdutosMODEL;
import org.example.Model.UsuarioMODEL;
import org.example.DAO.ProdutosDAO;

import java.util.List;
import java.util.Scanner;

public class VendasService {

    private final ProdutosDAO ProdutosRepository;

    public VendasService(EntityManager em) {
        this.ProdutosRepository = new ProdutosDAO(em);
    }


    public void registrarVenda(Scanner scanner, UsuarioMODEL vendedor) {
        System.out.print("Informe o ID do produto: ");
        long idProduto = scanner.nextLong();
        System.out.print("Informe a quantidade de venda: ");
        int quantidade = scanner.nextInt();

        ProdutosRepository.registrarVenda(idProduto, quantidade, vendedor);

        List<ProdutosMODEL> produtos = ProdutosRepository.buscarTodos();
        List<ProdutosMODEL> produtosMODELClassificados = CurvaABC.classificar(produtos);
        ProdutosRepository.atualizarProdutos(produtosMODELClassificados);

        System.out.println("Venda registrada com sucesso!");
    }
}
