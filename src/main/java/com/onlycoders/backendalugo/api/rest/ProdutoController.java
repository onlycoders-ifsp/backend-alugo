package com.onlycoders.backendalugo.api.rest;

import com.onlycoders.backendalugo.model.entity.produto.Produto;
import com.onlycoders.backendalugo.model.entity.produto.templates.CadAtuProduto;
import com.onlycoders.backendalugo.model.entity.produto.templates.RetornaProduto;
import com.onlycoders.backendalugo.model.entity.produto.templates.UsuarioProduto;
import com.onlycoders.backendalugo.model.entity.usuario.Usuario;
import com.onlycoders.backendalugo.model.repository.ProdutoRepository;
import com.sun.org.apache.xpath.internal.operations.Bool;
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

    @ApiOperation(value = "Retorna todos produtos ou por id")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UsuarioProduto> retornaProduto(@RequestBody CadAtuProduto id) {
        //System.out.println(id.idUsuario + " - " + id.produto.idProduto);
        List<Produto> p = repository.findProduto(id.idUsuario, id.produto.idProduto);
        List<UsuarioProduto> listaProdutos = new ArrayList<UsuarioProduto>();


            for(Produto i :p){
                RetornaProduto user = new RetornaProduto();
                UsuarioProduto usrPrd = new UsuarioProduto();
                user.setIdProduto(i.idProduto);
                user.setNome(i.nome);
                user.setDescricao(i.descricao);
                user.setValorBaseDiaria(i.valorBaseDiaria);
                user.setValorBaseMensal(i.valorBaseMensal);
                user.setValorProduto(i.valorProduto);
                user.setDataCompra(i.dataCompra);
                user.setAtivo(i.ativo);
                usrPrd.setId_usuario(i.idUsuario);
                usrPrd.setProduto(user);
                listaProdutos.add(usrPrd);
            }
            //System.out.println(listProduto.get(0).getIdUsuario());
        return listaProdutos;
    }
    @ApiOperation(value = "Cria novo produto")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Boolean inserir(@RequestBody CadAtuProduto produto){
        return repository.createProduto(produto.idUsuario,produto.produto.nome,produto.produto.descricao,
                produto.produto.valorBaseDiaria, produto.produto.valorBaseMensal, produto.produto.valorProduto,
                produto.produto.dataCompra);
    }


    @ApiOperation(value = "Muda dados produto")
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Boolean update(@RequestBody CadAtuProduto produto) {
        return repository.updateProduto(produto.produto.idProduto, produto.produto.nome,produto.produto.descricao,
                produto.produto.valorBaseDiaria, produto.produto.valorBaseMensal, produto.produto.valorProduto,
                produto.produto.dataCompra);
    }

    @ApiOperation(value = "Ativa ou inativa o produto")
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public Boolean ativaInativaProduto(@RequestBody RetornaProduto produto){
        return repository.ativaInativaProduto(produto.getIdProduto());
    }
}
