package org.example.Model.Service;

import jakarta.persistence.EntityManager;
import org.example.Model.Entity.AuditoriaVendaMODEL;
import org.example.Model.Entity.CompradorMODEL;
import org.example.Model.Entity.FuncionarioMODEL;
import org.example.Model.Entity.ProdutosMODEL;
import org.example.Model.Repository.CompradorRepository;
import org.example.Model.Util.HibernateUtil;

import java.util.List;
import java.util.Scanner;

public class VendaService {

    public String realizarVenda(String codProd, String cpfFuncionario, int quantidade, String cpfComprador) {
            EntityManager em = HibernateUtil.getEntityManager();

            try {
                em.getTransaction().begin();

                // Buscar o produto pelo código
                ProdutosMODEL produto = em.createQuery("FROM Produtos p WHERE p.codProd = :codProd", ProdutosMODEL.class)
                        .setParameter("codProd", codProd)
                        .getResultStream().findFirst().orElse(null);

                if (produto == null) {
                    em.getTransaction().rollback();
                    return "Produto não encontrado.";
                }

                // Buscar o funcionário pelo CPF
                FuncionarioMODEL funcionario = em.createQuery("FROM Funcionario f WHERE f.CPF = :CPF", FuncionarioMODEL.class)
                        .setParameter("CPF", cpfFuncionario)
                        .getResultStream().findFirst().orElse(null);

                if (funcionario == null) {
                    em.getTransaction().rollback();
                    return "Funcionário não encontrado.";
                }

                if (produto.getEstoque() < quantidade) {
                    em.getTransaction().rollback();
                    return "Estoque insuficiente.";
                }

                // Buscar o comprador pelo CPF (se informado)
                CompradorMODEL comprador = null;
                if (cpfComprador != null && !cpfComprador.isBlank()) {
                    comprador = em.createQuery("FROM Comprador c WHERE c.CPF = :CPF", CompradorMODEL.class)
                            .setParameter("CPF", cpfComprador)
                            .getResultStream().findFirst().orElse(null);
                }

                // Atualizar estoque
                produto.setEstoque(produto.getEstoque() - quantidade);
                em.merge(produto);

                // Atualizar total de vendas do funcionário
                double totalVenda = quantidade * produto.getValor();
                funcionario.setTotalVendas(funcionario.getTotalVendas() + totalVenda);
                em.merge(funcionario);

                // Criar auditoria da venda
                AuditoriaVendaMODEL auditoria = new AuditoriaVendaMODEL();
                auditoria.setProduto(produto);
                auditoria.setFuncionario(funcionario);
                auditoria.setQuantidade(quantidade);
                auditoria.setComprador(comprador);
                auditoria.setDataVenda(java.time.LocalDateTime.now());

                em.persist(auditoria);

                em.getTransaction().commit();
                return "SUCESSO";

            } catch (Exception e) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                return "Erro inesperado: " + e.getMessage();
            } finally {
                em.close();
            }
        }


    public void executarVendaViaMenu(Scanner scanner) {
        EntityManager em = HibernateUtil.getEntityManager();

        try {
            ProdutosMODEL produto = null;
            FuncionarioMODEL funcionario = null;
            Integer quantidade = null;
            CompradorMODEL comprador = null;

            etapa:
            while (true) {
                System.out.println("\n=== Realizar Venda ===");

                // Produto
                while (produto == null) {
                    System.out.print("Código do Produto (codProd) (ou 'cancelar'): ");
                    String input = scanner.nextLine().trim();
                    if (input.equalsIgnoreCase("cancelar")) return;

                    try {
                        produto = em.createQuery("SELECT p FROM Produtos p WHERE p.codProd = :cod", ProdutosMODEL.class)
                                .setParameter("cod", input)
                                .getResultStream()
                                .findFirst()
                                .orElse(null);

                        if (produto == null) System.out.println("Produto não encontrado.");
                    } catch (Exception e) {
                        System.out.println("Erro ao buscar produto: " + e.getMessage());
                    }
                }

                // Funcionário
                while (funcionario == null) {
                    System.out.print("CPF do Funcionário ('voltar' / 'cancelar'): ");
                    String input = scanner.nextLine().trim();

                    if (input.equalsIgnoreCase("voltar")) {
                        produto = null;
                        continue etapa;
                    }
                    if (input.equalsIgnoreCase("cancelar")) return;

                    try {
                        funcionario = em.createQuery("SELECT f FROM Funcionario f WHERE f.CPF = :CPF", FuncionarioMODEL.class)
                                .setParameter("CPF", input)
                                .getResultStream()
                                .findFirst()
                                .orElse(null);

                        if (funcionario == null) System.out.println("Funcionário não encontrado.");
                    } catch (Exception e) {
                        System.out.println("Erro ao buscar funcionário: " + e.getMessage());
                    }
                }

                // Quantidade
                while (quantidade == null || quantidade <= 0) {
                    System.out.print("Quantidade ('voltar' / 'cancelar'): ");
                    String input = scanner.nextLine().trim();

                    if (input.equalsIgnoreCase("voltar")) {
                        funcionario = null;
                        continue etapa;
                    }
                    if (input.equalsIgnoreCase("cancelar")) return;

                    try {
                        int qtd = Integer.parseInt(input);
                        if (qtd <= 0) {
                            System.out.println("Deve ser maior que zero.");
                        } else if (qtd > produto.getEstoque()) {
                            System.out.println("Estoque insuficiente. Estoque atual: " + produto.getEstoque());
                        } else {
                            quantidade = qtd;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Número inválido.");
                    }
                }

                // Comprador (opcional)
                while (comprador == null) {
                    System.out.print("CPF do Comprador (vazio para venda sem comprador / 'voltar' / 'cancelar'): ");
                    String input = scanner.nextLine().trim();

                    if (input.equalsIgnoreCase("voltar")) {
                        quantidade = null;
                        continue etapa;
                    }
                    if (input.equalsIgnoreCase("cancelar")) return;

                    if (input.isEmpty()) break;

                    try {
                        comprador = em.createQuery("SELECT c FROM Comprador c WHERE c.CPF = :CPF", CompradorMODEL.class)
                                .setParameter("CPF", input)
                                .getResultStream()
                                .findFirst()
                                .orElse(null);

                        if (comprador == null) {
                            System.out.print("Comprador não encontrado. Deseja cadastrar? (s/n): ");
                            String resp = scanner.nextLine().trim();
                            if (resp.equalsIgnoreCase("s")) {
                                System.out.print("Nome do Comprador: ");
                                String nome = scanner.nextLine().trim();

                                em.getTransaction().begin();
                                comprador = new CompradorMODEL();
                                comprador.setNome(nome);
                                comprador.setCPF(input);
                                em.persist(comprador);
                                em.getTransaction().commit();

                                System.out.println("Comprador cadastrado.");
                            } else {
                                comprador = null; // continua no loop
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Erro ao buscar comprador: " + e.getMessage());
                    }
                }

                // Realizar venda
                VendaService service = new VendaService();
                String resultado = service.realizarVenda(produto.getCodProd(), funcionario.getCPF(), quantidade,
                        comprador != null ? comprador.getCPF() : null);
                System.out.println("Resultado da venda: " + resultado);
                return;
            }

        } finally {
            em.close();
        }
    }



}
