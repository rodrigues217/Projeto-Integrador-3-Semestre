package org.example.Model.Repository;

import jakarta.persistence.*;
import org.example.Model.ProdutosMODEL;
import java.util.List;

public class ProdutosRepository {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    public void salvar(ProdutosMODEL produto) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(produto);
        em.getTransaction().commit();
        em.close();
    }

    public ProdutosMODEL buscarPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        ProdutosMODEL produto = em.find(ProdutosMODEL.class, id);
        em.close();
        return produto;
    }

    public List<ProdutosMODEL> listarTodos() {
        EntityManager em = emf.createEntityManager();
        List<ProdutosMODEL> produtos = em.createQuery("FROM Produto", ProdutosMODEL.class).getResultList();
        em.close();
        return produtos;
    }

    public void atualizar(ProdutosMODEL produto) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(produto);
        em.getTransaction().commit();
        em.close();
    }

    public void deletar(Long id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ProdutosMODEL produto = em.find(ProdutosMODEL.class, id);
        if (produto != null) {
            em.remove(produto);
        }
        em.getTransaction().commit();
        em.close();
    }
}
