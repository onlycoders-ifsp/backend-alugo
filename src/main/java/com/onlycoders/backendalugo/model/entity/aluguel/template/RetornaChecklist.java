package com.onlycoders.backendalugo.model.entity.aluguel.template;

import com.fasterxml.jackson.annotation.JsonAlias;

public interface RetornaChecklist {

    @JsonAlias("id_aluguel")
    String getIdAluguel();

    @JsonAlias("descricao")
    String getDescricao();

    @JsonAlias("foto")
    String getFoto();

    @JsonAlias("ok_locatario")
    String getOkLocatario();
}
