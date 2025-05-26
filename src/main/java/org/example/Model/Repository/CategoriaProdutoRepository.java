package org.example.Model.Repository;

import jakarta.persistence.EntityManager;
import org.example.Model.CategoriaProdutoMODEL;
import org.example.Util.HibernateUtil;

import java.util.List;

public class CategoriaProdutoRepository {

    public void salvar(CategoriaProdutoMODEL categoria) {
        EntityManager em = HibernateUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(categoria);
        em.getTransaction().commit();
        em.close();
    }

    public CategoriaProdutoMODEL buscarPorId(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        CategoriaProdutoMODEL categoria = em.find(CategoriaProdutoMODEL.class, id);
        em.close();
        return categoria;
    }

    public List<CategoriaProdutoMODEL> listarTodos() {
        EntityManager em = HibernateUtil.getEntityManager();
        List<CategoriaProdutoMODEL> categorias = em.createQuery("FROM CategoriaProdutoMODEL", CategoriaProdutoMODEL.class).getResultList();
        em.close();
        return categorias;
    }

    public void atualizar(CategoriaProdutoMODEL categoria) {
        EntityManager em = HibernateUtil.getEntityManager();
        em.getTransaction().begin();
        em.merge(categoria);
        em.getTransaction().commit();
        em.close();
    }

    public void deletar(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        em.getTransaction().begin();
        CategoriaProdutoMODEL categoria = em.find(CategoriaProdutoMODEL.class, id);
        if (categoria != null) {
            em.remove(categoria);
        }
        em.getTransaction().commit();
        em.close();
    }
}
