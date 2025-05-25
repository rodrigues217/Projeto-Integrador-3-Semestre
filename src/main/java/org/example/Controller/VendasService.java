package org.example.Controller;

import jakarta.persistence.EntityManager;
import org.example.Model.AuditoriaVendaMODEL;
import org.example.Model.ProdutosMODEL;
import org.example.Model.UsuarioMODEL;
import org.example.Model.Repository.ProdutosRepository;

import java.util.List;
import java.util.Scanner;

public class VendasService {

    private final ProdutosRepository ProdutosRepository;

    public VendasService(EntityManager em) {
        this.ProdutosRepository = new ProdutosRepository(em);
    }


    public void registrarVenda(Scanner scanner, UsuarioMODEL vendedor) {
        System.out.print("Informe o ID do produto: ");
        long idProduto = scanner.nextLong();
        System.out.print("Informe a quantidade de venda: ");
        int quantidade = scanner.nextInt();

        registrarVenda(idProduto, quantidade, vendedor);

        List<ProdutosMODEL> produtos = ProdutosRepository.buscarTodosProdutos();
        List<ProdutosMODEL> produtosMODELClassificados = CurvaABC.classificar(produtos);
        ProdutosRepository.atualizarProdutos(produtosMODELClassificados);

        System.out.println("Venda registrada com sucesso!");
    }

    // Atualizado: método com lucro e auditoria
    public void registrarVenda(long idProduto, int quantidade, UsuarioMODEL vendedor) {
        ProdutosMODEL produto = em.find(ProdutosMODEL.class, idProduto);
        if (produto != null) {
            if (produto.getEstoque() >= quantidade) {
                produto.setQuantidade_vendida(produto.getQuantidade_vendida() + quantidade);
                produto.setEstoque(produto.getEstoque() - quantidade);

                // 🟢 Registra o lucro da venda (30% do valor total vendido)
                double valorVenda = produto.getValor() * quantidade;
                LucroService.registrarLucro(valorVenda);

                try {
                    em.getTransaction().begin();

                    em.merge(produto);

                    // 🟢 Salva a venda na auditoria
                    AuditoriaVendaMODEL auditoria = new AuditoriaVendaMODEL();
                    auditoria.setProduto(produto);
                    auditoria.setVendedor(vendedor);
                    auditoria.setQuantidade(quantidade);
                    em.persist(auditoria);

                    em.getTransaction().commit();

                    aplicarCurvaABC();
                } catch (Exception e) {
                    if (em.getTransaction().isActive()) em.getTransaction().rollback();
                    e.printStackTrace();
                }
            } else {
                System.out.println("Estoque insuficiente para a venda.");
            }
        } else {
            System.out.println("Produto não encontrado.");
        }
    }


}
