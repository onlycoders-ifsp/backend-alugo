package com.onlycoders.backendalugo.model.entity;

import com.fasterxml.jackson.annotation.*;
import com.onlycoders.backendalugo.model.entity.aluguel.RetornaAluguel;
import com.onlycoders.backendalugo.model.entity.produto.templates.RetornaProduto;
import com.onlycoders.backendalugo.model.entity.usuario.templates.RetornaUsuario;


@JsonRootName("RetornaAluguel")
@JsonPropertyOrder({"aluguel","locador","locatario"})
public class RetornaAluguelUsuarioProduto {

    @JsonUnwrapped
    private RetornaAluguel aluguel;

    private RetornaUsuario locador;
    private RetornaUsuario locatario;
    private RetornaProduto produto;

    public RetornaUsuario getLocador() {
        return locador;
    }

    public void setLocador(RetornaUsuario locador) {
        this.locador = locador;
    }

    public RetornaUsuario getLocatario() {
        return locatario;
    }

    public void setLocatario(RetornaUsuario locatario) {
        this.locatario = locatario;
    }

    public RetornaProduto getProduto() {
        return produto;
    }

    public void setProduto(RetornaProduto produto) {
        this.produto = produto;
    }

    public RetornaAluguel getAluguel() {
        return aluguel;
    }

    public void setAluguel(RetornaAluguel aluguel) {
        this.aluguel = aluguel;
    }
}
