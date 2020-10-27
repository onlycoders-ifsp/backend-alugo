package com.onlycoders.backendalugo.model.entity.usuario.templates;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class AlteraSenha {

    @NotNull
    private String senha_antiga;

    @NotNull
    private String senha_nova;

    public String getSenha_antiga() {
        return senha_antiga;
    }

    public void setSenha_antiga(String senha_antiga) {
        this.senha_antiga = senha_antiga;
    }

    public String getSenha_nova() {
        return senha_nova;
    }

    public void setSenha_nova(String senha_nova) {
        this.senha_nova = senha_nova;
    }
}
