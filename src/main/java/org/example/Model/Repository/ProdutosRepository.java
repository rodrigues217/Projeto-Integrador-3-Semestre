package org.example.Model.Repository;

import jakarta.persistence.EntityManager;
import org.example.Model.Entity.ProdutosMODEL;
import org.example.Util.HibernateUtil;

import java.util.List;

public class ProdutosRepository {

    public void salvar(ProdutosMODEL produto) {
        EntityManager em = HibernateUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(produto);
        em.getTransaction().commit();
        em.close();
    }

    public ProdutosMODEL buscarPorId(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        ProdutosMODEL produto = em.find(ProdutosMODEL.class, id);
        em.close();
        return produto;
    }

    public List<ProdutosMODEL> listarTodos() {
        EntityManager em = HibernateUtil.getEntityManager();
        List<ProdutosMODEL> produtos = em.createQuery("FROM ProdutosMODEL", ProdutosMODEL.class).getResultList();
        em.close();
        return produtos;
    }

    public void atualizar(ProdutosMODEL produto) {
        EntityManager em = HibernateUtil.getEntityManager();
        em.getTransaction().begin();
        em.merge(produto);
        em.getTransaction().commit();
        em.close();
    }

    public void deletar(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        em.getTransaction().begin();
        ProdutosMODEL produto = em.find(ProdutosMODEL.class, id);
        if (produto != null) {
            em.remove(produto);
        }
        em.getTransaction().commit();
        em.close();
    }
}
