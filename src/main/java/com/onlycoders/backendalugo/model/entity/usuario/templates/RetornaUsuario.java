package com.onlycoders.backendalugo.model.entity.usuario.templates;

import com.google.gson.annotations.SerializedName;

public interface RetornaUsuario{

    //@Transient
    @SerializedName("IdUsuario")
    public String getIdUsuario();

    @SerializedName("Nome")
    public String getNome();

    @SerializedName("Cpf")
    public String getCpf();

    @SerializedName("Email")
    public String getEmail();

    @SerializedName("Sexo")
    public String getSexo();

    @SerializedName("Data_Nascimento")
    public String getDataNascimento();

    @SerializedName("Telefone")
    public String getTelefone();

    @SerializedName("Celular")
    public String getCelular();

    @SerializedName("Data_Inclusao")
    public String getDataInclusao();

    @SerializedName("Estado")
    public String getEstado();

    @SerializedName("Cidade")
    public String getCidade();

    @SerializedName("Logradouro")
    public String getLogradouro();

    @SerializedName("Numero")
    public String getNumero();

    @SerializedName("Bairro")
    public String getBairro();
}
