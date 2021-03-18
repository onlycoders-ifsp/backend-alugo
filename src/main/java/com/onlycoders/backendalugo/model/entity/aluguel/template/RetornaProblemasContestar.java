package com.onlycoders.backendalugo.model.entity.aluguel.template;

import com.fasterxml.jackson.annotation.JsonAlias;

public interface RetornaProblemasContestar {

    @JsonAlias("id_problema")
    String getId_problema();

    @JsonAlias("tipo_problema")
    Integer getTipo_problema();

    @JsonAlias("gravidade")
    Integer getGravidade();

    @JsonAlias("descricao")
    String getDescricao();

    @JsonAlias("situacao")
    String getSituacao();

    @JsonAlias("valorr")
    Double getValor();

    @JsonAlias("foto")
    byte[] getFoto();
}
