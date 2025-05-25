package org.example.Model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "Produtos") // Nome da entidade para as queries HQL
@Table(name = "produtos")  // Nome da tabela real no banco
public class ProdutosMODEL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "valor")
    private double valor;

    @Column(name = "estoque")
    private int estoque;

    @Column(name = "quantidade_vendida")
    private int quantidade_vendida;

    @Column(name = "valor_consumo")
    private Double valorConsumo;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false) // FK para categoria
    private CategoriaProdutoMODEL categoria;

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

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public int getQuantidade_vendida() {
        return quantidade_vendida;
    }

    public void setQuantidade_vendida(int quantidade_vendida) {
        this.quantidade_vendida = quantidade_vendida;
    }

    public Double getValorConsumo() {
        return valorConsumo;
    }

    public void setValorConsumo(Double valorConsumo) {
        this.valorConsumo = valorConsumo;
    }

    private Set<CategoriaProdutoMODEL> categoriasProduto = new HashSet<>();

    public Set<CategoriaProdutoMODEL> getCategoriaProdutos() {
        return categoriasProduto;
    }

    public void setCategoriaProdutos(Set<CategoriaProdutoMODEL> categoriasProduto) {
        this.categoriasProduto = categoriasProduto;
    }

}
