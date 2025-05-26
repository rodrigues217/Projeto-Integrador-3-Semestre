package org.example.Model.Repository;

import jakarta.persistence.*;
import org.example.Model.FuncionarioMODEL;
import java.util.List;

public class FuncionarioRepository {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    public void salvar(FuncionarioMODEL funcionario) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(funcionario);
        em.getTransaction().commit();
        em.close();
    }

    public FuncionarioMODEL buscarPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        FuncionarioMODEL funcionario = em.find(FuncionarioMODEL.class, id);
        em.close();
        return funcionario;
    }

    public List<FuncionarioMODEL> listarTodos() {
        EntityManager em = emf.createEntityManager();
        List<FuncionarioMODEL> funcionarios = em.createQuery("FROM Funcionario", FuncionarioMODEL.class).getResultList();
        em.close();
        return funcionarios;
    }

    public void atualizar(FuncionarioMODEL funcionario) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(funcionario);
        em.getTransaction().commit();
        em.close();
    }

    public void deletar(Long id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        FuncionarioMODEL funcionario = em.find(FuncionarioMODEL.class, id);
        if (funcionario != null) {
            em.remove(funcionario);
        }
        em.getTransaction().commit();
        em.close();
    }
}
