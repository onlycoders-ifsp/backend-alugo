package com.onlycoders.backendalugo.model.entity.produto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.onlycoders.backendalugo.model.entity.produto.templates.Fotos;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Constraint;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.sql.Array;
import java.util.ArrayList;

@Data
@Entity
@Getter
@Setter
@NoArgsConstructor

public class Produto {

    @JsonIgnore
    @JsonAlias({"id_usuario"})
    private String id_usuario;

    @Id
    @JsonAlias({"id_produto"})
    private String id_produto = null;

    @NotNull
    @JsonAlias({"nome"})
    private String nome;

    @JsonAlias({"descricao_curta"})
    private String descricao_curta;

    @JsonAlias({"descricao"})
    private String descricao;

    @DecimalMin("0.01")
    @JsonAlias({"valor_base_diario","valor_base_diaria"})
    private Double valor_base_diaria;

    @DecimalMin("0.01")
    @JsonAlias({"valor_base_mensal"})
    private Double valor_base_mensal;

    @DecimalMin("0.01")
    @JsonAlias({"valor_produto"})
    private Double valor_produto;

    @JsonAlias({"data_compra"})
    private String data_compra;

    @JsonIgnore
    @JsonAlias({"qtd_alugueis"})
    private int qtd_alugueis = 0;

    @JsonIgnore
    @JsonAlias({"total_ganhos"})
    private Double total_ganhos = 0.0;

    @JsonIgnore
    @JsonAlias({"media_avaliacao"})
    private Double media_avaliacao = 0.0;

    @JsonAlias({"capa_foto"})
    private String capa_foto;

  //  @JsonAlias("fotos")
/*  @Lob
    private Byte[] fotos;
*/
    @JsonAlias({"ativo"})
    private Boolean ativo;

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getId_produto() {
        return id_produto;
    }

    public void setId_produto(String id_produto) {
        this.id_produto = id_produto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao_curta() {
        return descricao_curta;
    }

    public void setDescricao_curta(String descricao_curta) {
        this.descricao_curta = descricao_curta;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor_base_diaria() {
        return valor_base_diaria;
    }

    public void setValor_base_diaria(Double valor_base_diaria) {
        this.valor_base_diaria = valor_base_diaria;
    }

    public Double getValor_base_mensal() {
        return valor_base_mensal;
    }

    public void setValor_base_mensal(Double valor_base_mensal) {
        this.valor_base_mensal = valor_base_mensal;
    }

    public Double getValor_produto() {
        return valor_produto;
    }

    public void setValor_produto(Double valor_roduto) {
        this.valor_produto = valor_roduto;
    }

    public String getData_compra() {
        return data_compra;
    }

    public void setData_compra(String data_compra) {
        this.data_compra = data_compra;
    }

    public int getQtd_alugueis() {
        return qtd_alugueis;
    }

    public void setQtd_alugueis(int qtd_alugueis) {
        this.qtd_alugueis = qtd_alugueis;
    }

    public Double getTotal_ganhos() {
        return total_ganhos;
    }

    public void setTotal_ganhos(Double total_ganhos) {
        this.total_ganhos = total_ganhos;
    }

    public Double getMedia_avaliacao() {
        return media_avaliacao;
    }

    public void setMedia_avaliacao(Double media_avaliacao) {
        this.media_avaliacao = media_avaliacao;
    }

    public String getCapa_foto() {
        return capa_foto;
    }

    public void setCapa_foto(String capa_foto) {
        this.capa_foto = capa_foto;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
