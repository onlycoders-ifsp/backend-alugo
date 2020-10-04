package com.onlycoders.backendalugo.api.rest;

import com.onlycoders.backendalugo.model.entity.produto.Produto;
import com.onlycoders.backendalugo.model.entity.produto.templates.RetornaProduto;
import com.onlycoders.backendalugo.model.repository.ProdutoRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.List;


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
    public List<RetornaProduto> retornaProduto(@PathVariable String id){
        return  repository.findProduto(id);

    }

    @ApiOperation(value = "Cria novo produto")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produto inserir(@RequestBody Produto produto){
        return repository.save(produto);
    }


    @ApiOperation(value = "Muda dados produto")
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody Produto usuario){
        repository.save(usuario);
    }
}
