package com.onlycoders.backendalugo.model.entity.usuario.templates;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CadAtuUsuario {

    //@JsonIgnore
    @JsonAlias({"id_Usuario","id_usuario"})
    public String idUsuario;

    @JsonAlias({"nome"})
    public String nome;

    @JsonAlias({"email"})
    public String email;

    @JsonAlias({"login"})
    public String login;

    @JsonAlias({"senha"})
    public String senha;

    @JsonAlias({"phoneNumber","numero_Celular","numeroCelular","celular"})
    public String celular;

    @JsonAlias({"cpf","Cpf"})
    public String cpf;

    @JsonAlias({"data_Nascimento","Data_nascimento","data_nascimento"})
    public String dataNascimento;

    @JsonAlias({"cep","Cep"})
    public String cep;

    @JsonAlias({"logradouro"})
    public String logradouro;

    @JsonAlias({"complemento"})
    public String complemento;

    @JsonAlias({"bairro"})
    public String bairro;

    @JsonAlias({"numero"})
    public String numero;
}
