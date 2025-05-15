package org.example.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    /* lado inverso do many‑to‑many */
    @ManyToMany(mappedBy = "categorias")
    private Set<Produtos> produtos = new HashSet<>();

    /* getters / setters */
    public Long getId()               { return id; }
    public void setId(Long id)        { this.id = id; }

    public String getNome()           { return nome; }
    public void setNome(String nome)  { this.nome = nome; }

    public Set<Produtos> getProdutos()              { return produtos; }
    public void setProdutos(Set<Produtos> produtos) { this.produtos = produtos; }
}
