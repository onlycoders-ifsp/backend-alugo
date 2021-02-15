package com.onlycoders.backendalugo.model.entity.aluguel.template;

import com.fasterxml.jackson.annotation.JsonAlias;
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
public class Avalicao {

    @JsonAlias("id_aluguel")
    @Id
    String id_aluguel;

    @JsonAlias("comentario")
    String comentario;

    @JsonAlias("nota")
    Double nota;

    public String getId_aluguel() {
        return id_aluguel;
    }

    public void setId_aluguel(String id_aluguel) {
        this.id_aluguel = id_aluguel;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }
}
