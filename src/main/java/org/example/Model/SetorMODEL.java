package org.example.Model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Setor")  // Nome da entidade para JPQL
@Table(name = "setores")
public class SetorMODEL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @OneToMany(mappedBy = "setor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FuncionarioMODEL> funcionarioMODELS = new ArrayList<>();

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<FuncionarioMODEL> getFuncionarioMODELS() {
        return funcionarioMODELS;
    }

    public void setFuncionarioMODELS(List<FuncionarioMODEL> funcionarioMODELS) {
        this.funcionarioMODELS = funcionarioMODELS;
    }
}
