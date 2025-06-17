package org.example.Model.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.example.Model.Entity.CategoriaProdutoMODEL;
import org.example.Model.Entity.ProdutosMODEL;
import org.example.Model.Repository.CategoriaProdutoRepository;
import org.example.Model.Repository.ProdutosRepository;
import org.example.Util.JPAUtil; // <- Certifique-se que existe essa classe

import java.util.List;

public class CategoriaProdutoService {

    private final CategoriaProdutoRepository categoriaRepository = new CategoriaProdutoRepository();
    private final ProdutosRepository produtosRepository = new ProdutosRepository();
    private final EntityManagerFactory emf = JPAUtil.getEntityManagerFactory(); // <- Adiciona isso

    public void criarCategoria(String nome) throws Exception {
        if (nome == null || nome.trim().isEmpty()) {
            throw new Exception("O nome da categoria não pode ser vazio.");
        }
        List<CategoriaProdutoMODEL> categorias = CategoriaProdutoRepository.listarTodos();
        boolean nomeExistente = categorias.stream()
                .anyMatch(cat -> cat.getNome().equalsIgnoreCase(nome));

        if (nomeExistente) {
            throw new Exception("Já existe uma categoria com esse nome.");
        }
        CategoriaProdutoMODEL categoria = new CategoriaProdutoMODEL(nome);
        categoriaRepository.salvar(categoria);
    }

    public List<CategoriaProdutoMODEL> listarCategoriasComProdutos() {
        EntityManager em = emf.createEntityManager(); // <- Corrigido

        try {
            return em.createQuery(
                    "SELECT DISTINCT c FROM CategoriaProdutoMODEL c LEFT JOIN FETCH c.produtos",
                    CategoriaProdutoMODEL.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    public void trocarCategoriaDeProduto(Long idProduto, Long idNovaCategoria) throws Exception {
        ProdutosMODEL produto = produtosRepository.buscarPorId(idProduto);
        if (produto == null) {
            throw new Exception("Produto não encontrado.");
        }

        CategoriaProdutoMODEL novaCategoria = categoriaRepository.buscarPorId(idNovaCategoria);
        if (novaCategoria == null) {
            throw new Exception("Categoria não encontrada.");
        }

        produto.setCategoria(novaCategoria);
        produtosRepository.atualizar(produto);
    }

    public void atualizarNomeCategoria(Long id, String novoNome) throws Exception {
        if (novoNome == null || novoNome.trim().isEmpty()) {
            throw new Exception("O novo nome da categoria não pode ser vazio.");
        }
        CategoriaProdutoMODEL categoria = categoriaRepository.buscarPorId(id);
        if (categoria == null) {
            throw new Exception("Categoria não encontrada.");
        }

        List<CategoriaProdutoMODEL> categorias = categoriaRepository.listarTodos();
        boolean nomeExistente = categorias.stream()
                .anyMatch(c -> !c.getId().equals(id) && c.getNome().equalsIgnoreCase(novoNome));

        if (nomeExistente) {
            throw new Exception("Já existe outra categoria com esse nome.");
        }

        categoria.setNome(novoNome);
        categoriaRepository.atualizar(categoria);
    }

    public void removerCategoria(Long id) throws Exception {
        CategoriaProdutoMODEL categoria = categoriaRepository.buscarPorId(id);
        if (categoria == null) {
            throw new Exception("Categoria não encontrada.");
        }

        boolean temProdutos = produtosRepository.listarTodos()
                .stream()
                .anyMatch(p -> p.getCategoria() != null && p.getCategoria().getId().equals(id));

        if (temProdutos) {
            throw new Exception("Não é possível remover a categoria. Existem produtos associados a ela.");
        }

        categoriaRepository.deletar(id);
    }

    public static List<CategoriaProdutoMODEL> listarCategorias() {
        return CategoriaProdutoRepository.listarTodos();
    }
}
