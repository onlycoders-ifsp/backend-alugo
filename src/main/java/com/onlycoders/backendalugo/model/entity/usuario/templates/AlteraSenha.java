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

    public String getSenha_nova() {
        return senha_nova;
    }

}
