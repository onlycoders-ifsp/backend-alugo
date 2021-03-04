package com.onlycoders.backendalugo.model.entity.admin;

import com.fasterxml.jackson.annotation.JsonAlias;

public interface RetornaTiposProblema {

    @JsonAlias("cod_tipo_problema")
    Integer getCod_tipo_problema();

    @JsonAlias("descricao")
    String getDescricao();

    @JsonAlias("perc_locador")
    Double getPerc_locador();

    @JsonAlias("perc_locatario")
    Double getPerc_locatario();

    @JsonAlias("descricao_calculo")
    String getDescricao_calculo();
}
