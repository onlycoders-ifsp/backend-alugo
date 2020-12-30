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
    public void retornaUsuariosTest() throws NotFoundException {
        Page<RetornaUsuario> usuarios = controllerUsuario.retornaUsuario(0,10);

        assertTrue(usuarios.hasContent());
    }

    @Test
    public void retornaUsuarioTest() throws NotFoundException {
        Page<RetornaUsuario> usuarios = controllerUsuario.retornaUsuario(0,10);

        RetornaUsuario usuario = controllerUsuario.retornaUsuario(usuarios.get().findFirst().get().getIdUsuario());
        assertEquals(usuarios.get().findFirst().get().getNome(),usuario.getNome());
    }
}
