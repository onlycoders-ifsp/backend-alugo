package com.onlycoders.backendalugo.model.entity.login;

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

    @Id
    public String idUsuario;

    private String login;

    private String password;

    private boolean admin;

    private boolean ativo;

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public boolean isAdmin() {
        return admin;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
