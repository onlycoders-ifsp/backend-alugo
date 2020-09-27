/* ########################## CONTROLLER API DE USUÁRIO ##########################
 * Data Criação: 14/06/2020
 * Programador: Dilan Lima
 * Decrição: Recebe as requisições, delega os processamentos e cuida dos retornos para api de usuários
 * #########################################################################
 */
package com.onlycoders.backendalugo.api.rest;

import com.onlycoders.backendalugo.model.entity.Usuario;
import com.onlycoders.backendalugo.model.repository.UsuarioRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@Api(value = "Usuarios")
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UsuarioController {

    private final UsuarioRepository repository;

    @ApiOperation(value = "Seleciona unico usuário pelo id")
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Usuario selecionaUm(@PathVariable Integer id) {
        return repository.getById(id);
    }


    @ApiOperation(value = "Cria novo usuário")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario salvar(@RequestBody Usuario usuario) {
        return repository.save(usuario);
    }

    @ApiOperation(value = "Deleta usuário")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        repository.deleteById(id);
    }

    @ApiOperation(value = "Lista usuário")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Usuario> list() {
        return repository.findAll();
    }

    @ApiOperation(value = "Muda senha usuário")
    @PatchMapping("{id}/muda-senha")
    @ResponseStatus(HttpStatus.OK)
    public void mudaSenha(@PathVariable Integer id, @RequestBody String nome) {
        Optional<Usuario> usuario = repository.findById(id);
        usuario.ifPresent(c -> {
            c.setNome(nome);
            repository.save(c);
        });
    }

    @ApiOperation(value = "Muda dados usuário")
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Usuario mudaDado(@RequestBody Usuario usuario) {
        return repository.save(usuario);
    }

    @ApiOperation(value = "Pega o nome do usuário pelo id")
    @GetMapping("{id}/nome")
    @ResponseStatus(HttpStatus.OK)
    public String getNomeById(@PathVariable Integer id) {
        Optional<Usuario> usuarioOp = repository.findById(id);
        if (usuarioOp.isPresent())
            return usuarioOp.get().getNome();
        else
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
    }

    @ApiOperation(value = "Inativa usuário pelo id")
    @GetMapping("{id}/inativo")
    @ResponseStatus(HttpStatus.OK)
    public Boolean inativaById(@PathVariable Integer id) {
        Optional<Usuario> usuarioOp = repository.findById(id);
        usuarioOp.ifPresent(u -> {
            u.setAtivo(false);
            repository.save(u);
        });

        if (!usuarioOp.isPresent())
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );

        return usuarioOp.get().getAtivo();
    }


}
