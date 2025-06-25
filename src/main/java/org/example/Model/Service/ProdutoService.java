package org.example.Model.Service;

import org.example.Model.Entity.CategoriaProdutoMODEL;
import org.example.Model.Entity.ProdutosMODEL;
import org.example.Model.Repository.CategoriaProdutoRepository;
import org.example.Model.Repository.ProdutosRepository;

import java.util.Comparator;
import java.util.List;

import java.util.stream.Collectors;

public class ProdutoService {

    private final ProdutosRepository produtosRepository = new ProdutosRepository();
    private final CategoriaProdutoRepository categoriaRepository = new CategoriaProdutoRepository();

    public void criarProdutoComCategoria(String nomeProduto, Double valor, Integer estoque, String codProd, Long categoriaId) throws Exception {
        CategoriaProdutoMODEL categoriaSelecionada = categoriaRepository.buscarPorId(categoriaId);
        if (categoriaSelecionada == null) {
            throw new Exception("Categoria não encontrada.");
        }
        if (nomeProduto == null || nomeProduto.trim().isEmpty()) {
            throw new Exception("Nome do produto não pode ser vazio.");
        }
        if (valor == null || valor <= 0) {
            throw new Exception("Valor do produto inválido.");
        }
        if (estoque == null || estoque < 0) {
            throw new Exception("Quantidade em estoque inválida.");
        }
        if (codProd == null || codProd.trim().isEmpty()) {
            throw new Exception("Código do produto não pode ser vazio.");
        }

        ProdutosMODEL produto = new ProdutosMODEL(nomeProduto, valor, estoque, codProd, categoriaSelecionada);
        produtosRepository.salvar(produto);
    }

    public List<ProdutosMODEL> listarProdutosComCategoria() {
        return produtosRepository.listarTodos();
    }

    public void atualizarEstoque(Long id, int quantidade, boolean adicionar) throws Exception {
        ProdutosMODEL produto = produtosRepository.buscarPorId(id);

        if (produto == null) {
            throw new Exception("Produto não encontrado.");
        }

        int novoEstoque = adicionar ? produto.getEstoque() + quantidade : produto.getEstoque() - quantidade;
        if (novoEstoque < 0) {
            throw new Exception("Estoque insuficiente para remoção.");
        }

        produto.setEstoque(novoEstoque);
        produtosRepository.atualizar(produto);

    }

    public List<ProdutosMODEL> listarProdutosPorCategoria(Long idCategoria) {
        return produtosRepository.listarTodos().stream()
                .filter(p -> p.getCategoria() != null && p.getCategoria().getId().equals(idCategoria))
                .toList();
    }

    public List<ProdutoComValorTotal> listarProdutosPorCurvaABC() {
        List<ProdutosMODEL> produtos = produtosRepository.listarTodos();

        if (produtos.isEmpty()) {
            return List.of();
        }

        List<ProdutoComValorTotal> produtosComValor = produtos.stream()
                .map(p -> new ProdutoComValorTotal(p, p.getEstoque() * p.getValor()))
                .sorted(Comparator.comparingDouble(ProdutoComValorTotal::getValorTotal).reversed())
                .collect(Collectors.toList());


        double valorTotalGeral = produtosComValor.stream()
                .mapToDouble(ProdutoComValorTotal::getValorTotal)
                .sum();

        double acumulado = 0.0;

        for (ProdutoComValorTotal pw : produtosComValor) {
            acumulado += pw.getValorTotal();
            double percentualAcumulado = (acumulado / valorTotalGeral) * 100;

            String classe;
            if (percentualAcumulado <= 80) {
                classe = "A";
            } else if (percentualAcumulado <= 95) {
                classe = "B";
            } else {
                classe = "C";
            }
            pw.setClasseABC(classe);
        }

        return produtosComValor;
    }

    public static class ProdutoComValorTotal {
        private ProdutosMODEL produto;
        private double valorTotal;
        private String classeABC;

        public ProdutoComValorTotal(ProdutosMODEL produto, double valorTotal) {
            this.produto = produto;
            this.valorTotal = valorTotal;
        }

        public ProdutosMODEL getProduto() {
            return produto;
        }

        public double getValorTotal() {
            return valorTotal;
        }


        public String getClasseABC() {
            return classeABC;
        }

        public void setClasseABC(String classeABC) {
            this.classeABC = classeABC;
        }
    }
}
