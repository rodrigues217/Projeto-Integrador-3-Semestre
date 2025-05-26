package org.example.Model.Repository;

import jakarta.persistence.*;
import org.example.Model.CategoriaProdutoMODEL;
import java.util.List;

public class CategoriaProdutoRepository {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    public void salvar(CategoriaProdutoMODEL categoria) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(categoria);
        em.getTransaction().commit();
        em.close();
    }

    public CategoriaProdutoMODEL buscarPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        CategoriaProdutoMODEL categoria = em.find(CategoriaProdutoMODEL.class, id);
        em.close();
        return categoria;
    }

    public List<CategoriaProdutoMODEL> listarTodos() {
        EntityManager em = emf.createEntityManager();
        List<CategoriaProdutoMODEL> categorias = em.createQuery("FROM CategoriaProduto", CategoriaProdutoMODEL.class).getResultList();
        em.close();
        return categorias;
    }

    public void atualizar(CategoriaProdutoMODEL categoria) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(categoria);
        em.getTransaction().commit();
        em.close();
    }

    public void deletar(Long id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        CategoriaProdutoMODEL categoria = em.find(CategoriaProdutoMODEL.class, id);
        if (categoria != null) {
            em.remove(categoria);
        }
        em.getTransaction().commit();
        em.close();
    }
}