package org.example.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.Model.CategoriaProdutoMODEL;
import org.example.Model.ProdutosMODEL;

import java.util.List;
import java.util.Scanner;

public class CategoriaProdutoDAO {

    @PersistenceContext
    private EntityManager em;


   public CategoriaProdutoDAO(EntityManager em) {
     this.em = em;
   }

    @Transactional
    public void salvar(CategoriaProdutoMODEL categoriaProdutoMODEL) {
        if (categoriaProdutoMODEL.getId() == null) {
            em.persist(categoriaProdutoMODEL);
        } else {
            em.merge(categoriaProdutoMODEL);
        }
    }
    public void listarCategoriasComProdutos(Scanner scanner) {
        List<CategoriaProdutoMODEL> categorias = em.createQuery(
                        "SELECT DISTINCT c FROM CategoriaProduto c LEFT JOIN FETCH c.produtos", CategoriaProdutoMODEL.class)
                .getResultList();

        for (CategoriaProdutoMODEL categoria : categorias) {
            System.out.println("Categoria: " + categoria.getNome());
            if (categoria.getProdutos().isEmpty()) {
                System.out.println("  Nenhum produto nesta categoria.");
            } else {
                for (ProdutosMODEL produto : categoria.getProdutos()) {
                    System.out.println("  - Produto: " + produto.getNome());
                }
            }
        }
    }

    public CategoriaProdutoMODEL buscarPorId(Long id) {
        return em.find(CategoriaProdutoMODEL.class, id);
    }

    public List<CategoriaProdutoMODEL> buscarTodos() {
        return em.createQuery("SELECT c FROM CategoriaProduto c", CategoriaProdutoMODEL.class).getResultList();
    }

    @Transactional
    public void deletarCategoriaPorNomeProduto(String nomeProduto) {
        // Encontra todas as categorias
        List<CategoriaProdutoMODEL> categorias = em.createQuery("SELECT c FROM CategoriaProduto c", CategoriaProdutoMODEL.class)
                .getResultList();
        for (CategoriaProdutoMODEL categoria : categorias) {
            // Itera sobre os produtos da categoria
            for (ProdutosMODEL produto : categoria.getProdutos()) {
                // Se o nome do produto corresponder ao nome fornecido, o remove da categoria
                if (produto.getNome().equalsIgnoreCase(nomeProduto)) {
                    categoria.getProdutos().remove(produto);
                    produto.getCategoriasProduto().remove(categoria);
                    em.merge(produto); // Atualiza o produto
                }
            }
            em.merge(categoria); // Atualiza a categoria após remoção do produto
        }
    }
    @Transactional
    public void deletarCategoriaPorNome(String nomeCategoria) {
        CategoriaProdutoMODEL categoria = em.createQuery("SELECT c FROM CategoriaProduto c WHERE c.nome = :nome", CategoriaProdutoMODEL.class)
                .setParameter("nome", nomeCategoria)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);

        if (categoria != null) {
            // Excluir todos os produtos dessa categoria
            for (ProdutosMODEL produto : categoria.getProdutos()) {
                produto.getCategoriasProduto().remove(categoria);  // Remove a associação com a categoria
                em.merge(produto);  // Atualiza o produto
            }
            em.remove(categoria);  // Exclui a categoria
            System.out.println("Categoria '" + nomeCategoria + "' excluída com sucesso.");
        } else {
            System.out.println("Categoria '" + nomeCategoria + "' não encontrada.");
        }
    }



    @Transactional
    public void associarProdutos(Long categoriaId, List<ProdutosMODEL> produtos) {
        CategoriaProdutoMODEL categoria = em.find(CategoriaProdutoMODEL.class, categoriaId);
        if (categoria != null) {
            for (ProdutosMODEL produto : produtos) {
                categoria.getProdutos().add(produto);
                produto.getCategoriasProduto().add(categoria);
                em.merge(produto); // Atualiza o produto também
            }
            em.merge(categoria); // Atualiza a categoria
        }
    }

    @Transactional
    public void desvincularProdutos(Long categoriaId, List<ProdutosMODEL> produtos) {
        CategoriaProdutoMODEL categoria = em.find(CategoriaProdutoMODEL.class, categoriaId);
        if (categoria != null) {
            for (ProdutosMODEL produto : produtos) {
                categoria.getProdutos().remove(produto);
                produto.getCategoriasProduto().remove(categoria);
                em.merge(produto); // Atualiza o produto também
            }
            em.merge(categoria); // Atualiza a categoria
        }
    }


    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void deletarCategoria(long idCategoriaExcluir) {
    }

    public CategoriaProdutoMODEL buscarPorNome(String nome) {
        try {
            return em.createQuery("SELECT c FROM CategoriaProduto c WHERE c.nome = :nome", CategoriaProdutoMODEL.class)
                    .setParameter("nome", nome)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}

