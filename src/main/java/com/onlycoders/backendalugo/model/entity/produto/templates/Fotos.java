package com.onlycoders.backendalugo.model.entity.produto.templates;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import javax.persistence.Lob;
import javax.servlet.http.Part;

@Data
public class Fotos {

    @JsonAlias("id_produto")
    private String id_produto;

    @Lob
    @JsonAlias("foto")
    private Part fotos;

    public String getId_produto() {
        return id_produto;
    }

    public void setId_produto(String id_produto) {
        this.id_produto = id_produto;
    }

    public Part getFotos() {
        return fotos;
    }

    public void setFotos(Part fotos) {
        this.fotos = fotos;
    }
}
