package com.onlycoders.backendalugo.model.entity.admin;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@Data
@Entity
public class LogErros {

    @JsonAlias("id")
    @Id
    int id;

    @JsonAlias("procedure")
    String procedure;

    @JsonAlias("tabela")
    String tabela;

    @JsonAlias("erro")
    String erro;

    @JsonAlias("query")
    String query;

    @JsonAlias("data_erro")
    String dataErro;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProcedure() {
        return procedure;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public String getTabela() {
        return tabela;
    }

    public void setTabela(String tabela) {
        this.tabela = tabela;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getDataErro() {
        return dataErro;
    }

    public void setDataErro(String dataErro) {
        this.dataErro = dataErro;
    }
}
