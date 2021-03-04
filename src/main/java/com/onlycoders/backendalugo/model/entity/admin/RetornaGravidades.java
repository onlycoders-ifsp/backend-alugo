package com.onlycoders.backendalugo.model.entity.admin;

import com.fasterxml.jackson.annotation.JsonAlias;

public interface RetornaGravidades {

    @JsonAlias("cod_gravidade")
    Integer getCod_gravidade();

    @JsonAlias("descricao")
    String getDescricao();

    @JsonAlias("devolucao")
    Double getDevolucao();
}
