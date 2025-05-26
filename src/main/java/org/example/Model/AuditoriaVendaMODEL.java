package org.example.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "AuditoriaVenda")
@Table(name = "auditoria_vendas")
public class AuditoriaVendaMODEL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_venda")
    private LocalDateTime dataVenda;

    @Column(nullable = false)
    private Integer quantidade;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private ProdutosMODEL produto;

    @ManyToOne
    @JoinColumn(name = "funcionarios_id", nullable = false)
    private FuncionarioMODEL funcionario;

    @PrePersist
    protected void onCreate() {
        this.dataVenda = LocalDateTime.now();
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public ProdutosMODEL getProduto() {
        return produto;
    }

    public void setProduto(ProdutosMODEL produto) {
        this.produto = produto;
    }

    public FuncionarioMODEL getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioMODEL funcionario) {
        this.funcionario = funcionario;
    }
}
