package com.onlycoders.backendalugo.model.entity.usuario.templates;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CadAtuUsuario {

    @SerializedName("id_Usuario")
    public String idUsuario;

    @SerializedName("Nome")
    public String nome;

    @SerializedName("Cpf")
    public String cpf;

    @SerializedName("Email")
    public String email;

    @SerializedName("Sexo")
    public String sexo;

    @SerializedName("Data_Nascimento")
    public String dataNascimento;

    @SerializedName("Senha")
    public String senha;

    @SerializedName("Login")
    public String login;

    @SerializedName("Telefone")
    public String telefone;

    @SerializedName("Celular")
    public String celular;

//@SerializedName("Estado")
    //public String getEstado;

    //@SerializedName("Cidade")
    //public String getCidade;

    //@SerializedName("Cep")
    //public String getCep;

    //@SerializedName("Logradouro")
    //public String getLogradouro;

    //@SerializedName("Numero")
    //public String getNumero;

    //@SerializedName("Bairro")
    //public String getBairro;
}
