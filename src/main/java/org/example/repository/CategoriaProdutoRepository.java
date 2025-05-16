package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.entities.CategoriaProduto;
import org.example.entities.Produtos;

import java.util.List;
import java.util.Scanner;

public class CategoriaProdutoRepository {

    @PersistenceContext
    private EntityManager em;


   public CategoriaProdutoRepository(EntityManager em) {
     this.em = em;
   }

    @Transactional
    public void salvar(CategoriaProduto categoriaProduto) {
        if (categoriaProduto.getId() == null) {
            em.persist(categoriaProduto);
        } else {
            em.merge(categoriaProduto);
        }
    }
    public void listarCategoriasComProdutos(Scanner scanner) {
        List<CategoriaProduto> categorias = em.createQuery(
                        "SELECT DISTINCT c FROM CategoriaProduto c LEFT JOIN FETCH c.produtos", CategoriaProduto.class)
                .getResultList();

        for (CategoriaProduto categoria : categorias) {
            System.out.println("Categoria: " + categoria.getNome());
            if (categoria.getProdutos().isEmpty()) {
                System.out.println("  Nenhum produto nesta categoria.");
            } else {
                for (Produtos produto : categoria.getProdutos()) {
                    System.out.println("  - Produto: " + produto.getNome());
                }
            }
        }
    }

    public CategoriaProduto buscarPorId(Long id) {
        return em.find(CategoriaProduto.class, id);
    }

    public List<CategoriaProduto> buscarTodos() {
        return em.createQuery("SELECT c FROM CategoriaProduto c", CategoriaProduto.class).getResultList();
    }

    @Transactional
    public void deletarCategoriaPorNomeProduto(String nomeProduto) {
        // Encontra todas as categorias
        List<CategoriaProduto> categorias = em.createQuery("SELECT c FROM CategoriaProduto c", CategoriaProduto.class)
                .getResultList();
        for (CategoriaProduto categoria : categorias) {
            // Itera sobre os produtos da categoria
            for (Produtos produto : categoria.getProdutos()) {
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
        CategoriaProduto categoria = em.createQuery("SELECT c FROM CategoriaProduto c WHERE c.nome = :nome", CategoriaProduto.class)
                .setParameter("nome", nomeCategoria)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);

        if (categoria != null) {
            // Excluir todos os produtos dessa categoria
            for (Produtos produto : categoria.getProdutos()) {
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
    public void associarProdutos(Long categoriaId, List<Produtos> produtos) {
        CategoriaProduto categoria = em.find(CategoriaProduto.class, categoriaId);
        if (categoria != null) {
            for (Produtos produto : produtos) {
                categoria.getProdutos().add(produto);
                produto.getCategoriasProduto().add(categoria);
                em.merge(produto); // Atualiza o produto também
            }
            em.merge(categoria); // Atualiza a categoria
        }
    }

    @Transactional
    public void desvincularProdutos(Long categoriaId, List<Produtos> produtos) {
        CategoriaProduto categoria = em.find(CategoriaProduto.class, categoriaId);
        if (categoria != null) {
            for (Produtos produto : produtos) {
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

    public CategoriaProduto buscarPorNome(String nome) {
        try {
            return em.createQuery("SELECT c FROM CategoriaProduto c WHERE c.nome = :nome", CategoriaProduto.class)
                    .setParameter("nome", nome)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}

