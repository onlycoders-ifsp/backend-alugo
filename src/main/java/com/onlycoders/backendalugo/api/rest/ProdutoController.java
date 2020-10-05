package com.onlycoders.backendalugo.api.rest;

import com.onlycoders.backendalugo.model.entity.produto.Produto;
import com.onlycoders.backendalugo.model.entity.produto.templates.CadAtuProduto;
import com.onlycoders.backendalugo.model.entity.produto.templates.RetornaProduto;
import com.onlycoders.backendalugo.model.entity.produto.templates.UsuarioProduto;
import com.onlycoders.backendalugo.model.repository.ProdutoRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.util.*;

@RestController
@Api(value = "Produtos")
@RequestMapping("/produtos")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProdutoController {

    @Autowired
    private ProdutoRepository repository;

    @ApiOperation(value = "Retorna produtos pelo idusuario e/ou idproduto")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UsuarioProduto> retornaProduto(@RequestParam String id_usuario, @RequestParam String id_produto) {
        //System.out.println(id.idUsuario + " - " + id.produto.idProduto);
        id_usuario = UriUtils.decode(id_usuario,"UTF-8");
        id_produto = UriUtils.decode(id_produto,"UTF-8");
        List<Produto> p = repository.findProduto(id_usuario, id_produto);
        List<UsuarioProduto> listaProdutos = new ArrayList<UsuarioProduto>();

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
    }
    @ApiOperation(value = "Cria novo produto")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Boolean inserir(@RequestBody CadAtuProduto produto){
        return repository.createProduto(produto.idUsuario,produto.produto.nome,produto.produto.descricaoCurta,produto.produto.descricao,
                produto.produto.valorBaseDiaria, produto.produto.valorBaseMensal, produto.produto.valorProduto,
                produto.produto.dataCompra,produto.produto.capaFoto);
    }


    @ApiOperation(value = "Muda dados produto")
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Boolean update(@RequestBody CadAtuProduto produto) {
        return repository.updateProduto(produto.produto.idProduto, produto.produto.nome, produto.produto.descricaoCurta,
                produto.produto.descricao,produto.produto.valorBaseDiaria, produto.produto.valorBaseMensal, produto.produto.valorProduto,
                produto.produto.dataCompra,produto.produto.capaFoto);
    }

    @ApiOperation(value = "Ativa ou inativa o produto")
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public Boolean ativaInativaProduto(@RequestBody RetornaProduto produto){
        return repository.ativaInativaProduto(produto.getIdProduto());
    }
}
