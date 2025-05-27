package org.example.Model.Entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "CategoriaProduto")
@Table(name = "categoria_produto")
public class CategoriaProdutoMODEL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ProdutosMODEL> produtos = new HashSet<>();

    public CategoriaProdutoMODEL() {
    }

    public CategoriaProdutoMODEL(String nome) {
        this.nome = nome;
    }

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

    public Set<ProdutosMODEL> getProdutos() {
        return produtos;
    }

    public void setProdutos(Set<ProdutosMODEL> produtos) {
        this.produtos = produtos;
    }


}
