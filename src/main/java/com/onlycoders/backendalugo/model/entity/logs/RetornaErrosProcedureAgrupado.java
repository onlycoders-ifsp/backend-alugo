package com.onlycoders.backendalugo.model.entity.logs;

import com.fasterxml.jackson.annotation.JsonAlias;

public interface RetornaErrosProcedureAgrupado {

    @JsonAlias("procedure")
    String getProcedure();

    @JsonAlias("quantidade")
    int getQuantidade();

    @JsonAlias("mes")
    String getMes();
}
