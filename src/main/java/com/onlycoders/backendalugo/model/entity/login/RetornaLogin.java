package com.onlycoders.backendalugo.model.entity.login;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonGetter;

public interface RetornaLogin {

    @JsonGetter("id_usuario")
    @JsonAlias({"Id_Usuario","Id_usuario","idUsuario"})
    public String getIdUsuario();

    @JsonGetter("ativo")
    public Boolean getAtivo();
}
