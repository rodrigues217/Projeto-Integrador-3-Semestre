package org.example.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "AuditoriaVenda")
@Table(name = "auditoria_vendas")
public class AuditoriaVendaMODEL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_venda", nullable = false)
    private LocalDateTime dataVenda = LocalDateTime.now();

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private ProdutosMODEL produto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id", nullable = false)
    private FuncionarioMODEL funcionario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comprador_id")
    private CompradorMODEL comprador;

    // Construtores
    public AuditoriaVendaMODEL() {
    }

    public AuditoriaVendaMODEL(Integer quantidade, ProdutosMODEL produto, FuncionarioMODEL funcionario, CompradorMODEL comprador) {
        this.quantidade = quantidade;
        this.produto = produto;
        this.funcionario = funcionario;
        this.comprador = comprador;
        this.dataVenda = LocalDateTime.now(); // valor padrão se não for passado
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public CompradorMODEL getComprador() {
        return comprador;
    }

    public void setComprador(CompradorMODEL comprador) {
        this.comprador = comprador;
    }
}
