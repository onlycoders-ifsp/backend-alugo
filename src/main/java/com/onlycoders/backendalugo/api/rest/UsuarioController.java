/* ########################## CONTROLLER API DE USUÁRIO ##########################
 * Data Criação: 14/06/2020
 * Programador: Dilan Lima
 * Decrição: Recebe as requisições, delega os processamentos e cuida dos retornos para api de usuários
 * #########################################################################
 */
package com.onlycoders.backendalugo.api.rest;

import com.onlycoders.backendalugo.exception.NotFoundException;
import com.onlycoders.backendalugo.model.entity.usuario.Usuario;
import com.onlycoders.backendalugo.model.entity.usuario.templates.AlteraSenha;
import com.onlycoders.backendalugo.model.entity.usuario.templates.RequestUsuario;
import com.onlycoders.backendalugo.model.entity.usuario.templates.RetornaUsuario;
import com.onlycoders.backendalugo.model.repository.ProdutoRepository;
import com.onlycoders.backendalugo.model.repository.UsuarioRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@Api(value = "Usuarios")
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UsuarioController {

    private  UsuarioRepository repository;

    @Autowired
    private ProdutoRepository repositoryProduto;

    @Autowired
    public UsuarioController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @ApiOperation(value = "Retorna dados do usuario pelo id ou login(0 para todos)", response = RetornaUsuario.class)
    @GetMapping("/usuario")
    @ResponseStatus(HttpStatus.OK)
    public HashMap<String, Object> retornaUsuario(@RequestParam String id_usuario) {

        if (id_usuario.isEmpty() || id_usuario == null)
            throw new NullPointerException("Parametro id_usuario vazio");

        return GeraLista(repository.findUsuario(id_usuario));
    }

    /*
    @ApiOperation(value = "Retorna dados do usuario pelo login. Param: login")
    @GetMapping("/usuario-login")
    @ResponseStatus(HttpStatus.OK)
    public List<RetornaUsuario> retornaUsuarioLogin(@RequestParam String login) {

        if (login.isEmpty() || login == null || login.length()<2)
            throw new NullPointerException("Parametro login vazio");

        return repository.findUsuario(login,1);
    }*/

    @ApiOperation(value = "Cadastro de usuário", response = RetornaUsuario.class)
    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public HashMap<String, Object> salvar(@RequestBody Usuario usuario) throws NotFoundException {

        //System.out.println(usuario.nome);

       validaCampos(usuario.getLogin(), usuario.getCpf(), usuario.getEmail(),
               usuario.getCelular(), usuario.getNome());

        return GeraLista(repository
                .createUsuarioMin(usuario.getNome(), usuario.getEmail(),usuario.getLogin(),
                usuario.getSenha(),usuario.getCpf(), usuario.getCelular()));
    }

    @Secured("ADMIN")
    @ApiOperation(value = "Deleta usuário, apenas usuario com role admin")
    @DeleteMapping("inativa")
    @ResponseStatus(HttpStatus.OK)
    public Boolean deletar(@RequestParam String id) {
        return repository.deleteUserById(id);
    }

    @ApiOperation(value = "Muda senha do usuario logado")
    @PutMapping("/altera-senha")
    @ResponseStatus(HttpStatus.OK)
    public Boolean alteraSenha(@RequestBody AlteraSenha senha) {
        System.out.println(senha.getSenha());
        return repository.alteraSenha(getIdUsuario(), senha.getSenha());
    }

    @ApiOperation(value = "Alterar dados cadastrais do usuario logado", response = RetornaUsuario.class)
    @PutMapping("altera-dados")
    @ResponseStatus(HttpStatus.OK)
    public HashMap<String, Object> alteraUsuario(@RequestBody RequestUsuario usuario) {
        System.out.println(usuario.getNome());
        return GeraLista(repository.updateUserById(getIdUsuario(),usuario.getNome(),usuario.getEmail(),
                usuario.getLogin(),usuario.getCpf(), usuario.getCelular(),usuario.getData_nascimento(),
                usuario.getCep(),usuario.getLogradouro(),usuario.getComplemento(), usuario.getBairro(),
                usuario.getBairro()));
    }

    private void validaCampos(String login, String cpf, String email, String celular, String nome) {
        if(celular.isEmpty() || celular == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Celular inválido");
        }

        if(nome.isEmpty() || nome == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome inválido");
        }

        if(login.isEmpty() || login == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Login inválido");
        }
        else if(repository.validaDado(login,3)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Login já existe");
        }

        if(cpf.isEmpty() || cpf == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF inválido");
        }
        else if(repository.validaDado(cpf,1)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF já existe");
        }

        if (email.isEmpty() || email == null || !email.contains("@")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email inválido");
        }
        else if(repository.validaDado(email, 2)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já existe");
        }
        //  return valida = repository.validaCampos(login, cpf, email);
    }

    public String getIdUsuario(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Optional<String> u = Optional.of(repository.retornaIdUsuario(auth.getName()));

        String usuario = String.valueOf(u.orElseThrow(()
                -> new UsernameNotFoundException("Usuario não encontrado")));

        usuario = UriUtils.decode(usuario,"UTF-8");

        return usuario;
    }

    public  HashMap<String,Object> GeraLista(List<RetornaUsuario> listaUsuario){
        HashMap<String, Object> mapUsuario = new HashMap<String, Object>();
        int cont = 1;
        for(RetornaUsuario p : listaUsuario){
            mapUsuario.put("Usuario " + String.valueOf(cont), p);
            cont++;
        }
        return  mapUsuario;
    }
}
