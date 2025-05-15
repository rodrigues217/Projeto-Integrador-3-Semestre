package org.example.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "produtos")
public class Produtos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private double valor;
    private int estoque;

    @Column(name = "quantidade_vendida")
    private int quantidade_vendida;

    @Column(name = "valor_consumo")
    private double valorConsumo;

    /* ---------- Classe ABC (A/B/C) ---------- */
    @Column(name = "categoria", length = 1)   // mantém o mesmo nome que já era usado
    private String categoria;

    /* ---------- Many‑to‑Many ---------- */
    @ManyToMany
    @JoinTable(
            name               = "produto_categoria",
            joinColumns        = @JoinColumn(name = "produto_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<Categoria> categorias = new HashSet<>();
    /* ----------------------------------- */

    public Produtos() {}

    /* getters / setters */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public int getEstoque() { return estoque; }
    public void setEstoque(int estoque) { this.estoque = estoque; }

    public int getQuantidade_vendida() { return quantidade_vendida; }
    public void setQuantidade_vendida(int q) { this.quantidade_vendida = q; }

    public double getValorConsumo() { return valorConsumo; }
    public void setValorConsumo(double v) { this.valorConsumo = v; }

    /* ---- classe ABC ---- */
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    /* ---- categorias (many‑to‑many) ---- */
    public Set<Categoria> getCategorias() { return categorias; }
    public void setCategorias(Set<Categoria> categorias) { this.categorias = categorias; }
}
