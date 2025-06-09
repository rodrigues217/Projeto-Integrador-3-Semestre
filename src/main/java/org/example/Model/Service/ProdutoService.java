package org.example.Model.Service;

import org.example.Model.Entity.CategoriaProdutoMODEL;
import org.example.Model.Entity.ProdutosMODEL;
import org.example.Model.Repository.CategoriaProdutoRepository;
import org.example.Model.Repository.ProdutosRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ProdutoService {

    private final ProdutosRepository produtosRepository = new ProdutosRepository();
    private final CategoriaProdutoRepository categoriaRepository = new CategoriaProdutoRepository();

    public void criarProdutoComCategoria(Scanner scanner) {
        List<CategoriaProdutoMODEL> categorias = categoriaRepository.listarTodos();

        if (categorias.isEmpty()) {
            System.out.println("Nenhuma categoria encontrada. É necessário cadastrar uma antes de criar produtos.");
            System.out.print("Digite o nome da nova categoria (ou 'cancelar' para sair): ");
            String nomeCategoria = scanner.nextLine().trim();

            if (nomeCategoria.equalsIgnoreCase("cancelar")) return;
            while (nomeCategoria.isEmpty()) {
                System.out.print("Nome inválido. Digite novamente (ou 'cancelar'): ");
                nomeCategoria = scanner.nextLine().trim();
                if (nomeCategoria.equalsIgnoreCase("cancelar")) return;
            }

            CategoriaProdutoMODEL novaCategoria = new CategoriaProdutoMODEL(nomeCategoria);
            categoriaRepository.salvar(novaCategoria);
            categorias = categoriaRepository.listarTodos(); // atualiza lista
        }

        int etapaAtual = 1;
        Long categoriaId = null;
        CategoriaProdutoMODEL categoriaSelecionada = null;
        String nomeProduto = null;
        Double valor = null;
        Integer estoque = null;
        String codProd = null;

        while (true) {
            switch (etapaAtual) {
                case 1 -> {
                    System.out.println("\n=== Categorias Disponíveis ===");
                    for (CategoriaProdutoMODEL cat : categorias) {
                        System.out.printf("ID: %d | Nome: %s%n", cat.getId(), cat.getNome());
                    }
                    System.out.print("Informe o ID da categoria (ou 'cancelar'): ");
                    String input = scanner.nextLine().trim();

                    if (input.equalsIgnoreCase("cancelar")) return;

                    try {
                        categoriaId = Long.parseLong(input);
                        categoriaSelecionada = categoriaRepository.buscarPorId(categoriaId);
                        if (categoriaSelecionada == null) {
                            System.out.println("Categoria não encontrada.");
                        } else {
                            etapaAtual++;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("ID inválido.");
                    }
                }

                case 2 -> {
                    System.out.print("Nome do produto (ou 'voltar' / 'cancelar'): ");
                    nomeProduto = scanner.nextLine().trim();

                    if (nomeProduto.equalsIgnoreCase("cancelar")) return;
                    if (nomeProduto.equalsIgnoreCase("voltar")) {
                        etapaAtual--;
                        continue;
                    }

                    if (!nomeProduto.isEmpty()) {
                        etapaAtual++;
                    } else {
                        System.out.println("Nome inválido.");
                    }
                }

                case 3 -> {
                    System.out.print("Valor do produto (ou 'voltar' / 'cancelar'): ");
                    String val = scanner.nextLine().trim();

                    if (val.equalsIgnoreCase("cancelar")) return;
                    if (val.equalsIgnoreCase("voltar")) {
                        etapaAtual--;
                        continue;
                    }

                    try {
                        valor = Double.parseDouble(val);
                        etapaAtual++;
                    } catch (NumberFormatException e) {
                        System.out.println("Valor inválido. Digite um número decimal.");
                    }
                }

                case 4 -> {
                    System.out.print("Quantidade em estoque (ou 'voltar' / 'cancelar'): ");
                    String est = scanner.nextLine().trim();

                    if (est.equalsIgnoreCase("cancelar")) return;
                    if (est.equalsIgnoreCase("voltar")) {
                        etapaAtual--;
                        continue;
                    }

                    try {
                        estoque = Integer.parseInt(est);
                        etapaAtual++;
                    } catch (NumberFormatException e) {
                        System.out.println("Quantidade inválida. Digite um número inteiro.");
                    }
                }

                case 5 -> {
                    System.out.print("Código do produto (ou 'voltar' / 'cancelar'): ");
                    codProd = scanner.nextLine().trim();

                    if (codProd.equalsIgnoreCase("cancelar")) return;
                    if (codProd.equalsIgnoreCase("voltar")) {
                        etapaAtual--;
                        continue;
                    }

                    if (!codProd.isEmpty()) {
                        ProdutosMODEL produto = new ProdutosMODEL(nomeProduto, valor, estoque, codProd, categoriaSelecionada);
                        produtosRepository.salvar(produto);
                        System.out.println("Produto criado com sucesso.");
                        return;
                    } else {
                        System.out.println("Código inválido.");
                    }
                }
            }
        }
    }


    public void listarProdutosComCategoria(Scanner scanner) {
        List<ProdutosMODEL> produtos = produtosRepository.listarTodos();

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            System.out.print("Deseja criar um novo produto agora? (s/n): ");
            String opcao = scanner.nextLine();

            if (opcao.equalsIgnoreCase("s")) {
                criarProdutoComCategoria(scanner);
            }
            return;
        }

        System.out.println("\n--- Produtos em Estoque ---");
        for (ProdutosMODEL p : produtos) {
            System.out.printf("ID: %d | Nome: %s | Estoque: %d | Categoria: %s | CodProd: %s%n",
                    p.getId(), p.getNome(), p.getEstoque(), p.getCategoria().getNome(), p.getCodProd());
        }
    }


    public void atualizarEstoque(Scanner scanner, boolean adicionar) {
        List<ProdutosMODEL> produtos = produtosRepository.listarTodos();

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto encontrado.");
            return;
        }

        System.out.println("\n=== Produtos Disponíveis ===");
        for (ProdutosMODEL p : produtos) {
            System.out.printf("ID: %d | Nome: %s | Estoque: %d%n | codProd: %s ", p.getId(), p.getNome(), p.getEstoque(), p.getCodProd());
        }

        System.out.print("Informe o ID do produto: ");
        Long id = Long.parseLong(scanner.nextLine());
        ProdutosMODEL produto = produtosRepository.buscarPorId(id);

        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        System.out.print("Quantidade: ");
        int qtd = Integer.parseInt(scanner.nextLine());

        int novoEstoque = adicionar ? produto.getEstoque() + qtd : produto.getEstoque() - qtd;
        if (novoEstoque < 0) {
            System.out.println("Estoque insuficiente para remoção.");
            return;
        }

        produto.setEstoque(novoEstoque);
        produtosRepository.atualizar(produto);
        System.out.println("Estoque atualizado com sucesso.");
    }

    public void listarProdutosPorCurvaABC() {
        List<ProdutosMODEL> produtos = produtosRepository.listarTodos();

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        // 1. Calcular o valor total de cada produto (estoque * valor unitário)
        List<ProdutoComValorTotal> produtosComValor = produtos.stream()
                .map(p -> new ProdutoComValorTotal(p, p.getEstoque() * p.getValor()))
                .sorted(Comparator.comparingDouble(ProdutoComValorTotal::getValorTotal).reversed())
                .collect(Collectors.toList());

        // 2. Calcular o total geral
        double valorTotalGeral = produtosComValor.stream()
                .mapToDouble(ProdutoComValorTotal::getValorTotal)
                .sum();

        // 3. Classificar por ABC
        double acumulado = 0.0;

        System.out.println("=== Classificação ABC dos Produtos ===");

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

            System.out.printf("Produto: %s | Código: %s | Valor Total: R$ %.2f | Classe: %s%n",
                    pw.getProduto().getNome(),
                    pw.getProduto().getCodProd(),
                    pw.getValorTotal(),
                    classe);
        }
    }

    public class ProdutoComValorTotal {
        private ProdutosMODEL produto;
        private double valorTotal;

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
    }


}
