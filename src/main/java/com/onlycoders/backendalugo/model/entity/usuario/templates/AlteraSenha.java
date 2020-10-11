package com.onlycoders.backendalugo.model.entity.usuario.templates;

import javax.validation.constraints.NotNull;

public class AlteraSenha {

    @NotNull
    private String senha;

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
