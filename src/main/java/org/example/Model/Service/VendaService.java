package org.example.Model.Service;

import jakarta.persistence.EntityManager;
import org.example.Model.Entity.AuditoriaVendaMODEL;
import org.example.Model.Entity.CompradorMODEL;
import org.example.Model.Entity.FuncionarioMODEL;
import org.example.Model.Entity.ProdutosMODEL;
import org.example.Util.HibernateUtil;

public class VendaService {

    // Não armazenamos EntityManagerFactory, usamos direto EntityManager pelo HibernateUtil
    public boolean realizarVenda(Long produtoId, Long funcionarioId, int quantidade, Long compradorId) {
        EntityManager em = HibernateUtil.getEntityManager();  // Usa HibernateUtil para criar EntityManager

        try {
            em.getTransaction().begin();

            ProdutosMODEL produto = em.find(ProdutosMODEL.class, produtoId);
            FuncionarioMODEL funcionario = em.find(FuncionarioMODEL.class, funcionarioId);
            CompradorMODEL comprador = compradorId != null ? em.find(CompradorMODEL.class, compradorId) : null;

            if (produto == null || funcionario == null) {
                System.out.println("Produto ou Funcionário não encontrado.");
                em.getTransaction().rollback();
                return false;
            }

            if (produto.getEstoque() < quantidade) {
                System.out.println("Estoque insuficiente.");
                em.getTransaction().rollback();
                return false;
            }

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
            return true;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;

        } finally {
            em.close();
        }
    }
}
