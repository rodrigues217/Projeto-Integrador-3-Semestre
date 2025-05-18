package org.example.DAO;

import jakarta.persistence.*;
import org.example.Controller.CurvaABC;
import org.example.Controller.LucroService;
import org.example.Model.AuditoriaVendaMODEL;
import org.example.Model.ProdutosMODEL;
import org.example.Model.UsuarioMODEL;

import java.util.List;

public class ProdutosDAO {

    private EntityManager em;

    public ProdutosDAO(EntityManager em) {
        this.em = em;
    }

    public void salvar(ProdutosMODEL produto) {
        try {
            em.getTransaction().begin();
            em.persist(produto);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
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

    private void aplicarCurvaABC() {
        List<ProdutosMODEL> produtos = em.createQuery("SELECT p FROM Produtos p", ProdutosMODEL.class).getResultList();
        CurvaABC.classificar(produtos);
    }

    public List<ProdutosMODEL> buscarTodos() {
        List<ProdutosMODEL> produtos = em.createQuery("SELECT p FROM Produtos p", ProdutosMODEL.class).getResultList();
        aplicarCurvaABCEmLista(produtos);
        return produtos;
    }

    public ProdutosMODEL buscarPorId(long id) {
        ProdutosMODEL produto = em.find(ProdutosMODEL.class, id);
        aplicarCurvaABCEmProduto(produto);
        return produto;
    }

    private void aplicarCurvaABCEmLista(List<ProdutosMODEL> produtos) {
        CurvaABC.classificar(produtos);
    }

    private void aplicarCurvaABCEmProduto(ProdutosMODEL produto) {
        if (produto != null) {
            CurvaABC.classificar(List.of(produto));
        }
    }

    public void atualizarProdutos(List<ProdutosMODEL> produtos) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            for (ProdutosMODEL produto : produtos) {
                em.merge(produto);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    public void adicionarEstoque(long idProduto, int quantidade) {
        ProdutosMODEL produto = em.find(ProdutosMODEL.class, idProduto);
        if (produto != null) {
            em.getTransaction().begin();
            produto.setEstoque(produto.getEstoque() + quantidade);
            em.merge(produto);
            em.getTransaction().commit();
            System.out.println("Estoque atualizado com sucesso!");
        } else {
            System.out.println("Produto não encontrado.");
        }
    }
}
