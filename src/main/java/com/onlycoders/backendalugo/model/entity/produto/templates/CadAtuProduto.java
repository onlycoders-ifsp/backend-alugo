package com.onlycoders.backendalugo.model.entity.produto.templates;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.onlycoders.backendalugo.model.entity.produto.Produto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CadAtuProduto {

    @JsonAlias({"id_usuario"})
    public String idUsuario;

    @JsonAlias({"produto"})
    public Produto produto;

    /*
    public CadAtuProduto(String idUsuario, Produto produto) {
        this.idUsuario = idUsuario;
        this.produto = produto;
    }*/
}
