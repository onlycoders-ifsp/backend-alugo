package com.onlycoders.backendalugo.api.rest;

import com.onlycoders.backendalugo.model.entity.Produto;
import com.onlycoders.backendalugo.model.repository.ProdutoRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@Api(value = "Produtos")
@RequestMapping("/produtos")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProdutoController {

    private final ProdutoRepository repository;

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

    @ApiOperation(value = "Inativa usuário pelo id")
    @GetMapping("{id}/inativo")
    @ResponseStatus(HttpStatus.OK)
    public Boolean inativaById(@PathVariable Integer id){
        Optional<Produto> produto = repository.findById(id);
        produto.ifPresent(u -> {
            u.setAtivo(false);
            repository.save(u);
        });
        if (! produto.isPresent())
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        return produto.get().getAtivo();
    }



}
