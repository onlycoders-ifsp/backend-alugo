package com.onlycoders.backendalugo.model.entity.usuario.templates;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"idUsuario","Id_Usuario","nome"})
public interface RetornaUsuario{

    //@Transient
    @JsonGetter("id_usuario")
    @JsonAlias({"Id_Usuario","Id_usuario","idUsuario"})
    public String getIdUsuario();

    @JsonGetter("nome")
    public String getNome();

    @JsonGetter("email")
    public String getEmail();

    @JsonGetter("login")
    public String getLogin();

    @JsonGetter("cpf")
    public String getCpf();

    @JsonGetter("celular")
    public String getCelular();

    @JsonGetter("data_nascimento")
    @JsonAlias({"Data_Nascimento","data_nascimento","DataNascimento"})
    public String getDataNascimento();

    @JsonGetter("cep")
    public String getCep();

    @JsonGetter("logradouro")
    public String getLogradouro();

    @JsonGetter("complemento")
    public String getComplemento();

    @JsonGetter("bairro")
    public String getBairro();

    @JsonGetter("numero")
    public String getNumero();

    @JsonGetter("ativo")
    public Boolean getAtivo();
}
