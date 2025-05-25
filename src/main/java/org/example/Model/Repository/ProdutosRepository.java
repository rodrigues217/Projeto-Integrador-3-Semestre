package org.example.Model.Repository;

import jakarta.persistence.*;
import org.example.Model.ProdutosMODEL;

import java.util.List;

public class ProdutosRepository {

    private EntityManager em;

    public ProdutosRepository(EntityManager em) {
        this.em = em;
    }

    public void CriarProduto(ProdutosMODEL produto) {
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

    public List<ProdutosMODEL> buscarTodosProdutos() {
        List<ProdutosMODEL> produtos = em.createQuery("SELECT p FROM Produtos p", ProdutosMODEL.class).getResultList();
        return produtos;
    }

    public ProdutosMODEL buscarProdutoPorId(long id) {
        ProdutosMODEL produto = em.find(ProdutosMODEL.class, id);
        return produto;
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

    public void adicionarEstoqueProduto(long idProduto, int quantidade) {
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

    public void removerProduto(long id) {
        ProdutosMODEL produto = em.find(ProdutosMODEL.class, id);
        if (produto != null) {
            try {
                em.getTransaction().begin();
                em.remove(produto);
                em.getTransaction().commit();
                System.out.println("Produto removido com sucesso!");
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                e.printStackTrace();
                System.out.println("Erro ao remover produto.");
            }
        } else {
            System.out.println("Produto não encontrado.");
        }
    }


}
