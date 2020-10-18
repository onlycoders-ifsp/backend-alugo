/* ########################## ENTIDADE DE USUÁRIO ##########################
 * Data Criação: 14/06/2020
 * Programador: Dilan Lima
 * Decrição: Entidade que mapea a tabela de usuários
 * #########################################################################
 */

package com.onlycoders.backendalugo.model.entity.usuario;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Usuario {

    @JsonIgnore
    @JsonAlias("id_usuario")
    @Id
    private String id_usuario = null;

    @JsonAlias("nome")
    private String nome;

    @JsonAlias("email")
    private String email;

    @JsonAlias("login")
    private String login;

    @JsonAlias("senha")
    private String senha;

    @JsonAlias("celular")
    private String celular;

    @JsonAlias("cpf")
    private String cpf;

    @JsonInclude
    @JsonIgnore
    @JsonAlias("data_nascimento")
    private String data_nascimento = null;

    @JsonIgnore
    @JsonAlias("cep")
    private String cep = null;

    @JsonIgnore
    @JsonAlias("endereco")
    private String logradouro = null;

    @JsonIgnore
    @JsonAlias("complemento")
    private String complemento = null;

    @JsonIgnore
    @JsonAlias("bairro")
    private String bairro = null;

    @JsonIgnore
    @JsonAlias("numero")
    private String numero = null;

    @JsonIgnore
    @JsonAlias("ativo")
    private Boolean ativo = null;

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(String data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
