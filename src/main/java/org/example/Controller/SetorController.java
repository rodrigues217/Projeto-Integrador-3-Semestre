package org.example.Controller;

import org.example.Model.Service.SetorService;

public class SetorController {
    private final SetorService setorService = new SetorService();

    public void criarSetor() {
        setorService.criarSetor();
    }

    public void listarSetores() {
        setorService.listarSetores();
    }

    public void atualizarSetor() {
        setorService.atualizarSetor();
    }

    public void deletarSetor() {
        setorService.deletarSetor();
    }
}
