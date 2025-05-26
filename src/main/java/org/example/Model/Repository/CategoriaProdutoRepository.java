package org.example.Model.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.example.Model.CategoriaProdutoMODEL;
import org.example.Model.ProdutosMODEL;

import java.util.List;

public class CategoriaProdutoRepository {

    private EntityManager em;

    public CategoriaProdutoRepository(EntityManager em) {
        this.em = em;
    }


    @Transactional
    public void CriarCategoria(CategoriaProdutoMODEL categoria) {
        if (categoria.getId() == null) {
            em.persist(categoria);
        } else {
            em.merge(categoria);
        }
    }

    public CategoriaProdutoMODEL buscarCategoriaPorId(Long id) {
        return em.find(CategoriaProdutoMODEL.class, id);
    }

    public List<CategoriaProdutoMODEL> buscarTodasCategorias() {
        return em.createQuery("SELECT c FROM CategoriaProduto c", CategoriaProdutoMODEL.class)
                .getResultList();
    }


    public CategoriaProdutoMODEL buscarCategoriaPorNome(String nome) {
        try {
            return em.createQuery("SELECT c FROM CategoriaProduto c WHERE c.nome = :nome", CategoriaProdutoMODEL.class)
                    .setParameter("nome", nome)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }



    @Transactional
    public void deletarCategoria(Long id) {
        CategoriaProdutoMODEL categoria = em.find(CategoriaProdutoMODEL.class, id);
        if (categoria != null) {
            // Remove a associação com produtos antes de excluir a categoria
            for (ProdutosMODEL produto : categoria.getProdutos()) {
                produto.getCategoriaProdutos().remove(categoria);
                em.merge(produto);
            }
            em.remove(categoria);
            System.out.println("Categoria excluída com sucesso!");
        } else {
            System.out.println("Categoria não encontrada para exclusão.");
        }
    }


    @Transactional
    public void associarProdutos(Long categoriaId, List<ProdutosMODEL> produtos) {
        CategoriaProdutoMODEL categoria = em.find(CategoriaProdutoMODEL.class, categoriaId);
        if (categoria != null) {
            for (ProdutosMODEL produto : produtos) {
                if (!categoria.getProdutos().contains(produto)) {
                    categoria.getProdutos().add(produto);
                    produto.getCategoriaProdutos().add(categoria);
                    em.merge(produto);
                }
            }
            em.merge(categoria);
        }
    }


    @Transactional
    public void desvincularProdutos(Long categoriaId, List<ProdutosMODEL> produtos) {
        CategoriaProdutoMODEL categoria = em.find(CategoriaProdutoMODEL.class, categoriaId);
        if (categoria != null) {
            for (ProdutosMODEL produto : produtos) {
                if (categoria.getProdutos().contains(produto)) {
                    categoria.getProdutos().remove(produto);
                    produto.getCategoriaProdutos().remove(categoria);
                    em.merge(produto);
                }
            }
            em.merge(categoria);
        }
    }
}