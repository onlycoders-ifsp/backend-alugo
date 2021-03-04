package com.onlycoders.backendalugo.model.entity.admin;

import com.fasterxml.jackson.annotation.JsonAlias;

public interface RetornaProblemas {
    @JsonAlias("descricao")
    String getDescricao();

    @JsonAlias("valor_locador")
    Double getValor_locador();

    @JsonAlias("valor_locatario")
    Double getValor_locatario();

    @JsonAlias("tipo_problema")
    Integer getTipo_problema();

    @JsonAlias("gravidade")
    Integer getGravidade();

    @JsonAlias("status_problema")
    Integer getStatus_problema();

    @JsonAlias("id_solicitante")
    String getId_solicitante();

    @JsonAlias("solicitante")
    String getSolicitante();

    @JsonAlias("data_inclusao")
    String getData_inclusao();

    @JsonAlias("usuario_operacao")
    String getUsuario_operacao();
}
