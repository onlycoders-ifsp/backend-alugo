package com.onlycoders.backendalugo.model.entity.produto.templates;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@Data
@Entity
public class Categorias {

    @Id
    @JsonAlias("id_categoria")
    String idCategoria;

    @JsonAlias("nome_categoria")
    String nomeCategoria;

    @JsonGetter("id_categoria")
    public String getIdCategoria() {
        return idCategoria;
    }

    @JsonSetter("id_categoria")
    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    @JsonGetter("nome_categoria")
    public String getNomeCategoria() {
        return nomeCategoria;
    }

    @JsonSetter("nome_categoria")
    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }
}
