package com.onlycoders.backendalugo.model.entity.produto.templates;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public interface DatasAlugadas {

    @JsonSetter("dt_inicio")
    String SetDt_inicio(String s);

    @JsonSetter("dt_fim")
    String SetDt_Fim(String s);

    @JsonGetter("dt_inicio")
    String getDt_Fim();

    @JsonGetter("dt_inicio")
    String getDt_inicio();


}
