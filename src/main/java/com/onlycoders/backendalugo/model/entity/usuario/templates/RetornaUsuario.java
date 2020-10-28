package com.onlycoders.backendalugo.model.entity.usuario.templates;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id_usuario","nome","email","login","cpf"})
public interface RetornaUsuario{

    //@Transient
    @JsonGetter("id_usuario")
    @JsonAlias({"id_usuario"})
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
    @JsonAlias({"data_nascimento"})
    public String getDataNascimento();

    @JsonGetter("cep")
    public String getCep();

    @JsonAlias("endereco")
    public String getEndereco();

    @JsonGetter("complemento")
    public String getComplemento();

    @JsonGetter("bairro")
    public String getBairro();

    @JsonGetter("numero")
    public String getNumero();

    @JsonGetter("ativo")
    public Boolean getAtivo();

    @JsonGetter("capa_foto")
    byte[] getCapa_foto();
}
