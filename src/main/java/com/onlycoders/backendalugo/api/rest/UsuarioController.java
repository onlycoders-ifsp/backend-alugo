/* ########################## CONTROLLER API DE USUÁRIO ##########################
 * Data Criação: 14/06/2020
 * Programador: Dilan Lima
 * Decrição: Recebe as requisições, delega os processamentos e cuida dos retornos para api de usuários
 * #########################################################################
 */
package com.onlycoders.backendalugo.api.rest;
import com.onlycoders.backendalugo.model.entity.usuario.Usuario;
import com.onlycoders.backendalugo.model.entity.usuario.templates.AlteraSenha;
import com.onlycoders.backendalugo.model.entity.usuario.templates.RequestUsuario;
import com.onlycoders.backendalugo.model.entity.usuario.templates.RetornaUsuario;
import com.onlycoders.backendalugo.model.repository.UsuarioRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriUtils;
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
    public UsuarioController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @ApiOperation(value = "Retorna dados do usuario logado", response = RetornaUsuario.class)
    @GetMapping("/usuario-logado")
    @ResponseStatus(HttpStatus.OK)
    public RetornaUsuario retornaUsuarioLogado() {

        return repository.findUsuario(getIdUsuario()).get(0);
        //return GeraLista(repository.findUsuario(id_usuario));
    }

    @ApiOperation(value = "Retorna dados de um único usuario pelo id ou login", response = RetornaUsuario.class)
    @GetMapping("/usuario")
    @ResponseStatus(HttpStatus.OK)
    public RetornaUsuario retornaUsuario(@RequestParam String id_usuario) {

        if (id_usuario.isEmpty() || id_usuario == null || id_usuario.equals("0"))
            throw new NullPointerException("Parametro id_usuario vazio");

            return repository.findUsuario(id_usuario).get(0);
 }

    @ApiOperation(value = "Retorna dados de todos os usuarios", response = RetornaUsuario.class)
    @GetMapping("/lista-usuario")
    @ResponseStatus(HttpStatus.OK)
    public List<RetornaUsuario> retornaUsuario() {

        return repository.findUsuario("0");
        //return GeraLista(repository.findUsuario(id_usuario));
    }

    @ApiOperation(value = "Cadastro de usuário", response = RetornaUsuario.class)
    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public RetornaUsuario salvar(@RequestBody Usuario usuario){
       validaCampos(getIdUsuario(), usuario.getCpf(), usuario.getEmail(),
               usuario.getCelular(), usuario.getNome());

        return repository
                .createUsuarioMin(usuario.getNome(), usuario.getEmail(),usuario.getLogin(),
                usuario.getSenha(),usuario.getCpf(), usuario.getCelular()).get(0);
    }

    @Secured("ADMIN")
    @ApiOperation(value = "Deleta usuário, apenas usuario com role admin")
    @DeleteMapping("inativa")
    @ResponseStatus(HttpStatus.OK)
    public Boolean deletar(@RequestParam String id_usuario) {
        if (id_usuario.isEmpty() || id_usuario == null || id_usuario.equals("0"))
            throw new NullPointerException("Parametro id_usuario vazio");

        return repository.deleteUserById(id_usuario);
    }

    @ApiOperation(value = "Muda senha do usuario logado")
    @PutMapping("/altera-senha")
    @ResponseStatus(HttpStatus.OK)
    public Boolean alteraSenha(@RequestBody AlteraSenha senha) {
        return repository.alteraSenha(getIdUsuario(), senha.getSenha());
    }

    @ApiOperation(value = "Alterar dados cadastrais do usuario logado", response = RetornaUsuario.class)
    @PutMapping("altera-dados")
    @ResponseStatus(HttpStatus.OK)
    public RetornaUsuario alteraUsuario(@RequestBody RequestUsuario usuario) {
        return repository.updateUserById(getIdUsuario(),usuario.getNome(),usuario.getEmail(),
                usuario.getLogin(),usuario.getCpf(), usuario.getCelular(),usuario.getData_nascimento(),
                usuario.getCep(),usuario.getLogradouro(),usuario.getComplemento(), usuario.getBairro(),
                usuario.getBairro()).get(0);
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
        String login;

        if(!auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken){
            throw new NullPointerException("Usuario não logado");
        }

        login = repository.retornaIdUsuario(auth.getName());
        if (login.isEmpty() || login == null) {
            throw new NullPointerException("Usuario não encontrado");
        }
        return login;
    }


    /*public  HashMap<String,Object> GeraLista(List<RetornaUsuario> listaUsuario){
        HashMap<String, Object> mapUsuario = new HashMap<String, Object>();
        int cont = 1;
        for(RetornaUsuario p : listaUsuario){
            mapUsuario.put("Usuario " + String.valueOf(cont), p);
            cont++;
        }
        return  mapUsuario;
    }
     */
/*
    public String validaLogin(){
        String id = IdUsuario.getId_usuario();
        System.out.println(id);
        if(id.isEmpty() || id == null || id.equals("0"))
            throw new NullPointerException("Usuario não está logado");

        return id;
    }*/
}
