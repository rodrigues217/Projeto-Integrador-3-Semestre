package org.example.Model.Entity;

public enum PerfilUsuario {
    ADM("Administrador"),
    GERENTE("Gerente"),
    OPERADOR("Operador");

    private final String descricao;

    PerfilUsuario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}