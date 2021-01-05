package com.onlycoders.backendalugo.model.entity.produto.templates;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonGetter;

public interface RetornaCategorias {

    @JsonGetter("id_categoria")
    @JsonAlias("id_categoria")
    String getId_Categoria();

    @JsonGetter("descricao")
    @JsonAlias("descricao")
    String getDescricao();

}
