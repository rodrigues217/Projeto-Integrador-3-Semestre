package org.example.Model.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.Model.Entity.CategoriaProdutoMODEL;
import org.example.Model.Entity.ProdutosMODEL;
import org.example.Model.Util.HibernateUtil;

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
        List<CategoriaProdutoMODEL> categorias = em.createQuery("FROM CategoriaProduto", CategoriaProdutoMODEL.class).getResultList();
        em.close();
        return categorias;
    }

    /**
     * Conta quantos produtos estão associados a uma categoria específica.
     * @param categoriaId ID da categoria
     * @return Número de produtos associados à categoria
     */
    public int contarProdutosPorCategoria(Long categoriaId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(p) FROM Produtos p WHERE p.categoria.id = :categoriaId",
                    Long.class
            );
            query.setParameter("categoriaId", categoriaId);
            return query.getSingleResult().intValue();
        } finally {
            em.close();
        }
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
