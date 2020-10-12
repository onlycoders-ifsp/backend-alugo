package com.onlycoders.backendalugo.model.entity.login;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonGetter;

public interface RetornaLogin {

    @JsonAlias("id_usuario")
    @JsonGetter("id_usuario")
    public String getId_Usuario();

    @JsonGetter("login")
    public String getLogin();

    @JsonGetter("password")
    public String getPassword();

    @JsonGetter("admin")
    public boolean getAdmin();

    @JsonGetter("ativo")
    public boolean getAtivo();
}
