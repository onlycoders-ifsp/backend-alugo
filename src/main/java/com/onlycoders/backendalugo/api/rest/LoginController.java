/* ########################## CONTROLLER API DE USUÁRIO ##########################
 * Data Criação: 14/06/2020
 * Programador: Dilan Lima
 * Decrição: Recebe as requisições, delega os processamentos e cuida dos retornos para api de usuários
 * #########################################################################
 */
package com.onlycoders.backendalugo.api.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onlycoders.backendalugo.model.entity.login.RetornaLogin;
import com.onlycoders.backendalugo.model.entity.usuario.Usuario;
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
@RequestMapping("/login")
@RequiredArgsConstructor
@CrossOrigin("*")
public class LoginController {
    @Autowired
    private  UsuarioRepository repository;

    @ApiOperation(value = "Verifica login")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RetornaLogin> verificaLogin(@RequestBody CadAtuUsuario id) {
        System.out.println(id.login + "," + id.senha);
        return repository.verificaLogin(id.login, id.senha);
    }


}
