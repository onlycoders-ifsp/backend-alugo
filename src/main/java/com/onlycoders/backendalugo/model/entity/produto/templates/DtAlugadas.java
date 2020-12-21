package com.onlycoders.backendalugo.model.entity.produto.templates;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonGetter;

public class DtAlugadas {

    @JsonAlias("dt_inicio")
    String dtInicio;
    @JsonAlias("dt_fim")
    String dtFim;

    public DtAlugadas(String dtInicio, String dtFim) {
        this.dtInicio = dtInicio;
        this.dtFim = dtFim;
    }

    @JsonGetter("dt_inicio")
    public String getDtInicio() {
        return dtInicio;
    }

    @JsonGetter("dt_fim")
    public String getDtFim() {
        return dtFim;
    }
}
