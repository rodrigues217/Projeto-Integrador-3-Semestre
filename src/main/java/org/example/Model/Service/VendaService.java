package org.example.Model.Service;

import jakarta.persistence.EntityManager;
import org.example.Model.Entity.AuditoriaVendaMODEL;
import org.example.Model.Entity.CompradorMODEL;
import org.example.Model.Entity.FuncionarioMODEL;
import org.example.Model.Entity.ProdutosMODEL;
import org.example.Model.Util.HibernateUtil;

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
}
