package com.onlycoders.backendalugo.api.rest;

import com.onlycoders.backendalugo.model.entity.login.IdUsuario;
import com.onlycoders.backendalugo.model.entity.login.UsuarioLogin;
import com.onlycoders.backendalugo.model.entity.produto.Produto;
import com.onlycoders.backendalugo.model.entity.produto.templates.RetornaProduto;
import com.onlycoders.backendalugo.model.entity.usuario.Usuario;
import com.onlycoders.backendalugo.model.repository.ProdutoRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@Api(value = "Produtos")
@RequestMapping("/produtos")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProdutoController {

    @Autowired
    private ProdutoRepository repository;

    @ApiOperation(value = "Retorna produtos do usuario logado.", response = RetornaProduto.class)
    @GetMapping("/lista-produto-logado")
    @ResponseStatus(HttpStatus.OK)
    public List<RetornaProduto> retornaProdutosUsuarioLogado() {
        System.out.println(validaLogin());
        return repository.findProdutoByUsuario(validaLogin(), "0");

        //return GeraLista(listaProduto);
    }

    @ApiOperation(value = "Retorna um único produto do usuario logado.", response = RetornaProduto.class)
    @GetMapping("/produto-logado")
    @ResponseStatus(HttpStatus.OK)
    public RetornaProduto retornaProdutoUsuarioLogado(@RequestParam String id_produto) {
        return repository.findProdutoByUsuario(validaLogin(), validaProduto(id_produto)).get(0);
        //return GeraLista(listaProduto);
    }

    @ApiOperation(value = "Retorna todos os produtos", response = RetornaProduto.class)
    @GetMapping("/lista-produto")
    @ResponseStatus(HttpStatus.OK)
    public List<RetornaProduto> retornaProdutos() {
        System.out.println(validaLogin());
       return repository.findProdutoByUsuario("0", "0");

        // GeraLista(listaProduto);
/*
        List<UsuarioProduto> listaProdutos = new ArrayList<UsuarioProduto>();

        /*
        for(Produto i :p){
            RetornaProduto user = new RetornaProduto();
            UsuarioProduto usrPrd = new UsuarioProduto();
            user.setIdProduto(i.idProduto);
            user.setNome(i.nome);
            user.setDescricaoCurta(i.descricaoCurta);
            user.setDescricao(i.descricao);
            user.setValorBaseDiaria(i.valorBaseDiaria);
            user.setValorBaseMensal(i.valorBaseMensal);
            user.setValorProduto(i.valorProduto);
            user.setDataCompra(i.dataCompra);
            user.setQtdAlugueis(i.qtdAlugueis);
            user.setTotalGanhos(i.totalGanhos);
            user.setMediaAvaliacao(i.mediaAvaliacao);
            user.setCapaFoto(i.capaFoto);
            user.setAtivo(i.ativo);
            usrPrd.setId_usuario(i.idUsuario);
            usrPrd.setProduto(user);
            listaProdutos.add(usrPrd);
        }
        //System.out.println(listProduto.get(0).getIdUsuario());
        return listaProdutos;
     */
    }

    @ApiOperation(value = "Retorna um único produto", response = RetornaProduto.class)
    @GetMapping("/produto")
    @ResponseStatus(HttpStatus.OK)
    public RetornaProduto retornaProduto(@RequestParam String id_produto) {
        return repository.findProdutoByUsuario("0", validaProduto(id_produto)).get(0);
    }

    @ApiOperation(value = "Retorna produtos de um usuario, id ou login", response = RetornaProduto.class)
    @GetMapping("/produto-usuario")
    @ResponseStatus(HttpStatus.OK)
    public RetornaProduto retornaProdutosUsuario(@RequestParam String id_usuario) {
        return repository.findProdutoByUsuario(validaLogin(), "0").get(0);
    }

    @ApiOperation(value = "Cadastra novo produto do usuario logado")
    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public Boolean cadastra(@RequestBody Produto produto){

        return repository.createProduto(validaLogin(),produto.getNome(),produto.getDescricao_curta(),produto.getDescricao(),
                produto.getValor_base_diaria(), produto.getValor_base_mensal(), produto.getValor_produto(),
                produto.getData_compra(),produto.getCapa_foto());
    }

    @ApiOperation(value = "Altera dados cadastrais do produto")
    @PutMapping("/altera")
    @ResponseStatus(HttpStatus.OK)
    public Boolean atualiza(@RequestBody Produto produto) {
        return repository.updateProduto(validaProduto(produto.getId_produto()), produto.getNome(), produto.getDescricao_curta(),
                produto.getDescricao(),produto.getValor_base_diaria(), produto.getValor_base_mensal(), produto.getValor_produto(),
                produto.getData_compra(),produto.getCapa_foto());
    }

    @ApiOperation(value = "Ativa ou inativa produto.")
    @DeleteMapping("ativa-inativa")
    @ResponseStatus(HttpStatus.OK)
    public Boolean ativaInativa(@RequestParam String id_produto){
        return repository.ativaInativaProduto(validaProduto(id_produto));
    }

    public String validaLogin(){
        String id = IdUsuario.getId_usuario();
        if(id.isEmpty() || id == null || id.equals("0"))
            throw new NullPointerException("Usuario não está logado");

        return id;
    }

    public String validaProduto(String id_produto){
        if (id_produto.isEmpty() || id_produto == null || id_produto.equals("0"))
            throw new NullPointerException("Parametro id_produto vazio");

        return id_produto;
    }
}
