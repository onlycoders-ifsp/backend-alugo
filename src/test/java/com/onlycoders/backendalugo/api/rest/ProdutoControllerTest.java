package com.onlycoders.backendalugo.api.rest;

import com.onlycoders.backendalugo.model.entity.produto.templates.ProdutoAluguel;
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
public class ProdutoControllerTest {

    @Autowired
    private ProdutoController controllerProduto;

    @Test
    public void retornaProdutosTest() throws NotFoundException {
        Page<ProdutoAluguel> produtos = controllerProduto.retornaProdutos(0,10,"qtd_alugueis","desc");
        assertTrue(produtos.hasContent());
    }

    @Test
    public void retornaProdutoTest() throws NotFoundException {
        Page<ProdutoAluguel> produtos = controllerProduto.retornaProdutos(0,10,"qtd_alugueis","desc");

        ProdutoAluguel produto = controllerProduto.retornaProduto(produtos.get().findFirst().get().getId_produto());
        assertEquals(produtos.get().findFirst().get().getNome(),produto.getNome());
    }

    @Test
    public void retornaProdutoPesquisaTest() throws NotFoundException {
        String palavraPesquisada = "livro";
        Page<ProdutoAluguel> produtos = controllerProduto.retornaProdutoPesquisa(palavraPesquisada,0,10,"qtd_alugueis","desc");

        assertTrue(produtos.get().allMatch(nome -> nome.getNome().toLowerCase().contains(palavraPesquisada)));
    }

    @Test
    public void retornaProdutosUsuarioTest() throws NotFoundException {
        String id_usuario = controllerProduto.retornaProdutos(0,10,"qtd_alugueis","desc").get().findFirst().get().getId_usuario();
        Page<ProdutoAluguel> produtos = controllerProduto.retornaProdutosUsuario(id_usuario,0,10,"qtd_alugueis","desc");

        assertTrue(produtos.hasContent());
    }


}
