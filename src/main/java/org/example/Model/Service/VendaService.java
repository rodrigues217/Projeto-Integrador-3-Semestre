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

    public String realizarVenda(Long produtoId, Long funcionarioId, int quantidade, Long compradorId) {
        EntityManager em = HibernateUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            ProdutosMODEL produto = em.find(ProdutosMODEL.class, produtoId);
            if (produto == null) {
                em.getTransaction().rollback();
                return "Produto não encontrado.";
            }

            FuncionarioMODEL funcionario = em.find(FuncionarioMODEL.class, funcionarioId);
            if (funcionario == null) {
                em.getTransaction().rollback();
                return "Funcionário não encontrado.";
            }

            if (produto.getEstoque() < quantidade) {
                em.getTransaction().rollback();
                return "Estoque insuficiente.";
            }

            CompradorMODEL comprador = compradorId != null ? em.find(CompradorMODEL.class, compradorId) : null;

            produto.setEstoque(produto.getEstoque() - quantidade);
            em.merge(produto);

            double totalVenda = quantidade * produto.getValor();
            funcionario.setTotalVendas(funcionario.getTotalVendas() + totalVenda);
            em.merge(funcionario);

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
            Long compradorId = null;

            etapa:
            while (true) {
                System.out.println("\n=== Realizar Venda ===");

                // Produto
                while (produto == null) {
                    System.out.print("ID do Produto (ou 'cancelar'): ");
                    String input = scanner.nextLine().trim();
                    if (input.equalsIgnoreCase("cancelar")) return;

                    try {
                        Long id = Long.parseLong(input);
                        produto = em.find(ProdutosMODEL.class, id);
                        if (produto == null) System.out.println("Produto não encontrado.");
                    } catch (NumberFormatException e) {
                        System.out.println("ID inválido.");
                    }
                }

                // Funcionário
                while (funcionario == null) {
                    System.out.print("ID do Funcionário ('voltar' / 'cancelar'): ");
                    String input = scanner.nextLine().trim();

                    if (input.equalsIgnoreCase("voltar")) {
                        produto = null;
                        continue etapa;
                    }
                    if (input.equalsIgnoreCase("cancelar")) return;

                    try {
                        Long id = Long.parseLong(input);
                        funcionario = em.find(FuncionarioMODEL.class, id);
                        if (funcionario == null) System.out.println("Funcionário não encontrado.");
                    } catch (NumberFormatException e) {
                        System.out.println("ID inválido.");
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
                        System.out.println("Quantidade inválida.");
                    }
                }

                // Comprador
                while (true) {
                    System.out.println("ID do Comprador:");
                    System.out.println(" - Deixe em branco para venda sem comprador");
                    System.out.println(" - Digite 'novo' para cadastrar um novo");
                    System.out.println(" - Digite 'voltar' para voltar");
                    System.out.println(" - Digite 'cancelar' para cancelar");
                    System.out.print("Entrada: ");
                    String input = scanner.nextLine().trim();

                    if (input.equalsIgnoreCase("voltar")) {
                        quantidade = null;
                        continue etapa;
                    }
                    if (input.equalsIgnoreCase("cancelar")) return;

                    if (input.isBlank()) break;

                    if (input.equalsIgnoreCase("novo")) {
                        System.out.print("Nome do comprador: ");
                        String nome = scanner.nextLine().trim();
                        System.out.print("Telefone do comprador: ");
                        String telefone = scanner.nextLine().trim();
                        System.out.print("CPF do comprador: ");
                        String CPF = scanner.nextLine().trim();


                        List<CompradorMODEL> compradores = new CompradorRepository().listarTodos();
                        for (CompradorMODEL c : compradores) {
                            if (c.getNome().equalsIgnoreCase(nome) && c.getTelefone().equals(telefone)) {
                                System.out.println("Já existe um comprador com esse nome e telefone.");
                                compradorId = c.getId();
                                System.out.println("Usando comprador já existente. ID: " + compradorId);
                                break;
                            }
                        }

                        if (compradorId == null) {
                            CompradorMODEL novoComprador = new CompradorMODEL(nome, telefone, CPF);
                            new CompradorRepository().salvar(novoComprador);
                            compradorId = novoComprador.getId();
                            System.out.println("Comprador cadastrado com ID: " + compradorId);
                        }

                        break;
                    }

                    try {
                        Long id = Long.parseLong(input);
                        CompradorMODEL comprador = em.find(CompradorMODEL.class, id);
                        if (comprador == null) {
                            System.out.println("Comprador não encontrado.");
                        } else {
                            compradorId = comprador.getId();
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("ID inválido.");
                    }
                }

                // Realizar venda
                String resultado = realizarVenda(produto.getId(), funcionario.getId(), quantidade, compradorId);
                if (resultado.equals("SUCESSO")) {
                    System.out.println("Venda realizada com sucesso!");
                } else {
                    System.out.println("Erro: " + resultado);
                }
                break;
            }
        } finally {
            em.close();
            System.out.println("Operação finalizada.");
        }
    }

}
