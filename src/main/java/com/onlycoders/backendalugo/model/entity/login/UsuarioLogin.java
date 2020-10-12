package com.onlycoders.backendalugo.model.entity.login;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dom4j.tree.AbstractEntity;
import org.springframework.security.core.userdetails.User;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class UsuarioLogin extends AbstractEntity {

    @JsonAlias("id_usuario")
    @Id
    private String id_usuario;

    private String password;

    private String login;

    private boolean admin;

    private boolean ativo;

    public String getIdUsuario() {
        return id_usuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.id_usuario = idUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
