package com.onlycoders.backendalugo.api.rest;

import com.onlycoders.backendalugo.model.entity.produto.templates.ProdutoAluguel;
import com.onlycoders.backendalugo.model.entity.usuario.templates.RetornaUsuario;
import javassist.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;


@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class UsuarioControllerTest {

    @Autowired
    private UsuarioController controllerUsuario;

    @Test
    public void retornaUsuariosTest(){
        RetornaUsuario usuarios = controllerUsuario.retornaUsuario("0");

        assertTrue(!usuarios.getEmail().isEmpty());
    }

    @Test
    public void retornaUsuarioTest(){
        RetornaUsuario usuarios = controllerUsuario.retornaUsuario("0");

        RetornaUsuario usuario = controllerUsuario.retornaUsuario(usuarios.getIdUsuario());
        assertEquals(usuarios.getNome(),usuario.getNome());
    }
}
