package org.example.Model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "CategoriaProduto") // Nome usado nas queries
@Table(name = "categoria_produto") // Nome da tabela no banco
public class CategoriaProdutoMODEL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToMany(mappedBy = "categoriasProduto")
    private Set<ProdutosMODEL> produtos = new HashSet<>();

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
