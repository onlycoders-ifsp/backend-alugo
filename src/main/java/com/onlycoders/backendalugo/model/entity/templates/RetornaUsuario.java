package com.onlycoders.backendalugo.model.entity.Templates;

import java.beans.Transient;

public interface RetornaUsuario{

    @Transient
    public String getIdUsuario();

    public String getNome();

    public String getCpf();

    public String getEmail();

    public String getSexo();

    public String getDataNascimento();

    public String getTelefone();

    public String getCelular();

    public String getDataInclusao();

    public String getEstado();

    public String getCidade();

    public String getLogradouro();

    public String getNumero();

    public String getBairro();
}
