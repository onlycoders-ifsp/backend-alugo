package com.onlycoders.backendalugo.model.entity.logs;

import com.fasterxml.jackson.annotation.JsonAlias;

public interface RetornaLogBackendDetalhe {

    @JsonAlias("controller")
    String getController();

    @JsonAlias("metodo")
    String getMetodo();

    @JsonAlias("endpoint")
    String getEndpoint();

    @JsonAlias("usuario")
    String getUsuario();

    @JsonAlias("message")
    String getMessage();

    @JsonAlias("stack_trace")
    String getStackTrace();
}
