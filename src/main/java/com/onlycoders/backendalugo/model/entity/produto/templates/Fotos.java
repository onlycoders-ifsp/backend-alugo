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
    @JsonAlias("capa_foto")
    private Part capa_foto;

    public String getId_produto() {
        return id_produto;
    }

    public void setId_produto(String id_produto) {
        this.id_produto = id_produto;
    }

    public Part getCapa_foto() {
        return capa_foto;
    }

    public void setCapa_foto(Part capa_foto) {
        this.capa_foto = capa_foto;
    }
}
