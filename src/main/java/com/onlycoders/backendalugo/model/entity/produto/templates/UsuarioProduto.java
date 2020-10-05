package com.onlycoders.backendalugo.model.entity.produto.templates;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonSetter;

public class UsuarioProduto {
    private String id_usuario;

    @JsonAlias("produto")
    @JsonSetter("produto")
    private RetornaProduto produto;

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public RetornaProduto getProduto() {
        return produto;
    }

    public void setProduto(RetornaProduto produto) {
        this.produto = produto;
    }
}
