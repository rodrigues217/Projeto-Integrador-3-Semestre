package org.example.Model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Produtos") // <- Nome da entidade usado nas queries
@Table(name = "produtos")  // <- Nome da tabela no banco de dados
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

    @Column(name = "categoria")
    private String categoria;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "produto_categoriaproduto",
            joinColumns = @JoinColumn(name = "produtos_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_produto_id")
    )
    private Set<CategoriaProdutoMODEL> categoriasProduto = new HashSet<>();

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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Set<CategoriaProdutoMODEL> getCategoriasProduto() {
        return categoriasProduto;
    }

    public void setCategoriasProduto(Set<CategoriaProdutoMODEL> categoriasProduto) {
        this.categoriasProduto = categoriasProduto;
    }

    public void adicionarCategoria(CategoriaProdutoMODEL categoriaProdutoMODEL) {
        this.categoriasProduto.add(categoriaProdutoMODEL);
    }

    public void removerCategoria(CategoriaProdutoMODEL categoriaProdutoMODEL) {
        this.categoriasProduto.remove(categoriaProdutoMODEL);
    }
}
