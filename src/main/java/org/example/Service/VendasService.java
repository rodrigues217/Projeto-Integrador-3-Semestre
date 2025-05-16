package org.example.Service;

import jakarta.persistence.EntityManager;
import org.example.entities.Produtos;
import org.example.entities.Usuario;
import org.example.repository.ProdutosRepository;

import java.util.List;
import java.util.Scanner;

public class VendasService {

    private final ProdutosRepository ProdutosRepository;

    public VendasService(EntityManager em) {
        this.ProdutosRepository = new ProdutosRepository(em);
    }


    public void registrarVenda(Scanner scanner, Usuario vendedor) {
        System.out.print("Informe o ID do produto: ");
        long idProduto = scanner.nextLong();
        System.out.print("Informe a quantidade de venda: ");
        int quantidade = scanner.nextInt();

        ProdutosRepository.registrarVenda(idProduto, quantidade, vendedor);

        List<Produtos> produtos = ProdutosRepository.buscarTodos();
        List<Produtos> produtosClassificados = CurvaABC.classificar(produtos);
        ProdutosRepository.atualizarProdutos(produtosClassificados);

        System.out.println("Venda registrada com sucesso!");
    }
}
