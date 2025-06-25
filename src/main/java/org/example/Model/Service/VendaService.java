package org.example.Model.Service;

import jakarta.persistence.EntityManager;
import org.example.Model.Entity.AuditoriaVendaMODEL;
import org.example.Model.Entity.CompradorMODEL;
import org.example.Model.Entity.FuncionarioMODEL;
import org.example.Model.Entity.ProdutosMODEL;
import org.example.Model.Util.HibernateUtil;

import java.time.LocalDateTime;

public class VendaService {

    public String realizarVenda(String codProd, String cpfFuncionario, int quantidade, String cpfComprador) throws Exception {
        EntityManager em = HibernateUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            // Buscar o produto pelo código
            ProdutosMODEL produto = em.createQuery("FROM Produtos p WHERE p.codProd = :codProd", ProdutosMODEL.class)
                    .setParameter("codProd", codProd)
                    .getResultStream().findFirst().orElse(null);

            if (produto == null) {
                em.getTransaction().rollback();
                throw new Exception("Produto não encontrado.");
            }

            // Buscar o funcionário pelo CPF
            FuncionarioMODEL funcionario = em.createQuery("FROM Funcionario f WHERE f.CPF = :CPF", FuncionarioMODEL.class)
                    .setParameter("CPF", cpfFuncionario)
                    .getResultStream().findFirst().orElse(null);

            if (funcionario == null) {
                em.getTransaction().rollback();
                throw new Exception("Funcionário não encontrado.");
            }

            if (produto.getEstoque() < quantidade) {
                em.getTransaction().rollback();
                throw new Exception("Estoque insuficiente.");
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
            auditoria.setDataVenda(LocalDateTime.now());

            em.persist(auditoria);

            em.getTransaction().commit();
            return "SUCESSO";

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}


