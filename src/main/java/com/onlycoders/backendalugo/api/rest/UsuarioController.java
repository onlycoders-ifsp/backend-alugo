/* ########################## CONTROLLER API DE USUÁRIO ##########################
 * Data Criação: 14/06/2020
 * Programador: Dilan Lima
 * Decrição: Recebe as requisições, delega os processamentos e cuida dos retornos para api de usuários
 * #########################################################################
 */
package com.onlycoders.backendalugo.api.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onlycoders.backendalugo.model.entity.usuario.templates.CadAtuUsuario;
import com.onlycoders.backendalugo.model.entity.usuario.templates.RetornaUsuario;
import com.onlycoders.backendalugo.model.repository.UsuarioRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javassist.Modifier;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Api(value = "Usuarios")
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UsuarioController {

    @Autowired
    private  UsuarioRepository repository;

    Gson gson = new GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.STATIC) //Modifier.FINAL, Modifier.TRANSIENT, Modifier.Static,
            .serializeNulls()
            .setPrettyPrinting()
            .create();
    public UsuarioController(UsuarioRepository repository) {
        this.repository = repository;
    }

    //private final UsuarioCustomRepository repository;

    @ApiOperation(value = "Seleciona unico usuário pelo id")
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<RetornaUsuario> retornaUsuario(@PathVariable String id) {
        //List<RetornaUsuario> listUsuario;
        //listUsuario = repository.findUsuario();
        //return listUsuario;
        //Map<String, ArrayList<RetornaUsuario>> map = new HashMap<String, ArrayList<RetornaUsuario>>();

        //Map<String, List<RetornaUsuario>> result =
        //      listUsuario.stream().collect(Collectors.groupingBy(a -> a.getIdUsuario()));

        //JSONObject jsonData = new JSONObject();

        //jsonData = getJsonFromMap(result);

        return repository.findUsuario(id);
    }

    @ApiOperation(value = "Cria novo usuário")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Boolean salvar(@RequestBody CadAtuUsuario usuario) {
        //System.out.println(usuario.sexo);
        return repository
                .createUsuario(usuario.nome, usuario.cpf,usuario.email,usuario.sexo,
                        usuario.dataNascimento, usuario.senha, usuario.login,
                        usuario.telefone, usuario.celular);
        //return usuario;
    }

    @ApiOperation(value = "Deleta usuário")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Boolean deletar(@PathVariable String id) {
        return repository.deleteUserById(id);
    }
/*
    @ApiOperation(value = "Lista usuário")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RetornaUsuario> list() {
        return repository.findUsuario("0");
    }
*/

/*
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
*/
    @ApiOperation(value = "Muda dados usuário")
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Boolean mudaDado(@RequestBody CadAtuUsuario usuario) {
        System.out.println(usuario.idUsuario);
        return repository.updateUserById(usuario.idUsuario,usuario.nome, usuario.cpf,usuario.email,usuario.sexo,
                usuario.dataNascimento, usuario.senha, usuario.login,
                usuario.telefone, usuario.celular);
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
private JSONObject getJsonFromMap(Map<String, List<RetornaUsuario>> result) throws JSONException {
    JSONObject jsonData = new JSONObject();
    for (String key : result.keySet()) {
        Object value = result.get(key);
        if (value instanceof Map<?, ?>) {
            value = getJsonFromMap((Map<String,List<RetornaUsuario>>) value);
        }
        jsonData.put(key, value);
    }
    return jsonData;
}
}
