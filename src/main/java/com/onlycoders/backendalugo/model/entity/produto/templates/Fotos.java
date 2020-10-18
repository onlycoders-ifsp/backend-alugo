package com.onlycoders.backendalugo.model.entity.produto.templates;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Fotos {

    @JsonAlias("fotos")
    private String[] fotos;

    @JsonAlias("capa_foto")
    private String capa_foto;

    public String[] getFotos() {
        return fotos;
    }

    public void setFotos(String[] fotos) {
        this.fotos = fotos;
    }

    public String getCapa_foto() {
        return capa_foto;
    }

    public void setCapa_foto(String capa_foto) {
        this.capa_foto = capa_foto;
    }
}
