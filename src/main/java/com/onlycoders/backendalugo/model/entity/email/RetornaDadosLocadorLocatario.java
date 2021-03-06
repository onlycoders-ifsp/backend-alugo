package com.onlycoders.backendalugo.model.entity.email;

import com.fasterxml.jackson.annotation.JsonAlias;

public interface RetornaDadosLocadorLocatario {

    @JsonAlias("locador_nome")
    String getLocadorNome();

    @JsonAlias("locador_email")
    String getLocadorEmail();

    @JsonAlias("locatario_nome")
    String getLocatarioNome();

    @JsonAlias("locatario_email")
    String getLocatarioEmail();
}
