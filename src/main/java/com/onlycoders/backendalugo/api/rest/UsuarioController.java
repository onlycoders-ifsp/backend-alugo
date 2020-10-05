/* ########################## CONTROLLER API DE USUÁRIO ##########################
 * Data Criação: 14/06/2020
 * Programador: Dilan Lima
 * Decrição: Recebe as requisições, delega os processamentos e cuida dos retornos para api de usuários
 * #########################################################################
 */
package com.onlycoders.backendalugo.api.rest;

import com.onlycoders.backendalugo.model.entity.usuario.templates.CadAtuUsuario;
import com.onlycoders.backendalugo.model.entity.usuario.templates.RetornaUsuario;
import com.onlycoders.backendalugo.model.repository.UsuarioRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.util.List;

@RestController
@Api(value = "Usuarios")
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UsuarioController {

    @Autowired
    private  UsuarioRepository repository;

    public UsuarioController(UsuarioRepository repository) {
        this.repository = repository;
    }

    //private final UsuarioCustomRepository repository;

    @ApiOperation(value = "Seleciona unico usuário pelo id")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RetornaUsuario> retornaUsuario(@RequestParam String id_usuario) {

        id_usuario = UriUtils.decode(id_usuario,"UTF-8");
        //System.out.println(id.idUsuario);
        return repository.findUsuario(id_usuario);
    }

    @ApiOperation(value = "Cria novo usuário")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<RetornaUsuario> salvar(@RequestBody CadAtuUsuario usuario) {
        //System.out.println(usuario.nome);
        return repository
                .createUsuarioMin(usuario.nome, usuario.email,usuario.login,
                        usuario.senha,usuario.cpf, usuario.celular);
        //return usuario;
    }

    @ApiOperation(value = "Deleta usuário")
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public Boolean deletar(@RequestBody CadAtuUsuario id) {
        return repository.deleteUserById(id.idUsuario);
    }

    @ApiOperation(value = "Muda senha usuário")
    @PutMapping("/alterasenha")
    @ResponseStatus(HttpStatus.OK)
    public Boolean alteraSenha(@RequestBody CadAtuUsuario id) {
        System.out.println(id.idUsuario + "," + id.senha);
        return repository.alteraSenha(id.idUsuario, id.senha);
    }

    @ApiOperation(value = "Muda dados usuário")
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public List <RetornaUsuario>  mudaDado(@RequestBody CadAtuUsuario usuario) {
        System.out.println(usuario.idUsuario);
        System.out.println(usuario.nome);
        return repository.updateUserById(usuario.idUsuario,usuario.nome,usuario.email, usuario.login,
                usuario.cpf, usuario.celular,usuario.dataNascimento, usuario.cep,usuario.logradouro,
                usuario.complemento, usuario.bairro, usuario.numero);
    }
/*
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
*/
}
