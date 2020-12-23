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
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@RestController
@Api(value = "Usuarios")
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UsuarioController {

    private UsuarioRepository repository;

    private String usuarioId;

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId() {
        this.usuarioId = getIdUsuario();
    }

    @Autowired
    public UsuarioController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @ApiOperation(value = "Retorna dados do usuario logado", response = RetornaUsuario.class)
    @GetMapping("/usuario-logado")
    @ResponseStatus(HttpStatus.OK)
    public RetornaUsuario retornaUsuarioLogado() {
        setUsuarioId();
        return repository.findUsuario(getUsuarioId()).get(0);
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
    public Page<RetornaUsuario> retornaUsuario(@RequestParam(value = "page",
                                                             required = false,
                                                             defaultValue = "0") int page,
                                               @RequestParam(value = "size",
                                                             required = false,
                                                             defaultValue = "10") int size) {
        Pageable paging = PageRequest.of(page, size);
        List<RetornaUsuario> listUsers = repository.findUsuario("0");
        return new PageImpl<>(listUsers,paging,listUsers.size());
        //return GeraLista(repository.findUsuario(id_usuario));
    }

    @ApiOperation(value = "Cadastro de usuário", response = RetornaUsuario.class)
    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public RetornaUsuario salvar(@RequestBody Usuario usuario){
       //validaCampos(usuario.getLogin(), usuario.getCpf(), usuario.getEmail(),
       //        usuario.getCelular(), usuario.getNome(), false);

        return repository
                .createUsuarioMin(usuario.getNome(), usuario.getEmail().toLowerCase(),usuario.getLogin(),
                usuario.getSenha(),usuario.getCpf(), usuario.getCelular()).get(0);
    }

    @ApiOperation(value = "Atualiza/Cadastra foto de usuario")
    @PutMapping("/upload-foto")
    @ResponseStatus(HttpStatus.CREATED)
    public Boolean atualizaFoto(@RequestParam Part capa_foto) throws NotFoundException {
        setUsuarioId();
        Optional<String> usuario = Optional.ofNullable(Optional
                .of(getUsuarioId())
                .orElseThrow(() -> new NotFoundException("Usuario não logado")));
        try{
            InputStream is = capa_foto.getInputStream();
            byte[] bytes = new byte[(int) capa_foto.getSize()];
            IOUtils.readFully(is,bytes);
            is.close();
            return repository.uploadFoto(usuario.get(), bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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
    public Boolean alteraSenha(@RequestBody AlteraSenha senha) throws NotFoundException {
        setUsuarioId();
        if(new BCryptPasswordEncoder().matches(senha.getSenha_antiga(),repository.retornaSenha(getUsuarioId())))
            return repository.alteraSenha(getUsuarioId(), senha.getSenha_nova());
        else
            throw new NotFoundException("Senha incorreta");
    }

    @ApiOperation(value = "Alterar dados cadastrais do usuario logado", response = RetornaUsuario.class)
    @PutMapping("altera-dados")
    @ResponseStatus(HttpStatus.OK)
    public RetornaUsuario alteraUsuario(@RequestBody RequestUsuario usuario) {
        //validaCampos(usuario.getLogin(),usuario.getCpf(),usuario.getEmail(),usuario.getCelular(),usuario.getNome(),true);
        setUsuarioId();
        return repository.updateUserById(getUsuarioId(),usuario.getNome(),usuario.getEmail().toLowerCase(),
                usuario.getLogin(),usuario.getCpf(), usuario.getCelular(),usuario.getData_nascimento(),
                usuario.getCep(),usuario.getLogradouro(),usuario.getComplemento(), usuario.getBairro(),
                usuario.getNumero()).get(0);
    }

    @ApiOperation(value = "Retorna se existe o email informado", response = RetornaUsuario.class)
    @GetMapping("/verifica/email")
    @ResponseStatus(HttpStatus.OK)
    public Boolean verificaEmail(@RequestParam String email){
        return repository.validaDado(email, 2);
    }

    @ApiOperation(value = "Retorna se existe o CPF informado", response = RetornaUsuario.class)
    @GetMapping("/verifica/cpf")
    @ResponseStatus(HttpStatus.OK)
    public Boolean verificaCpf(@RequestParam String cpf){
        return repository.validaDado(cpf, 1);
    }

    @ApiOperation(value = "Retorna se existe o nome de usuario informado", response = RetornaUsuario.class)
    @GetMapping("/verifica/username")
    @ResponseStatus(HttpStatus.OK)
    public Boolean verificaUserName(@RequestParam String user){
        return repository.validaDado(user, 3);
    }

    @ApiOperation(value = "Retorna se existe o email informado do usuario logado", response = RetornaUsuario.class)
    @GetMapping("/verifica/email-update")
    @ResponseStatus(HttpStatus.OK)
    public Boolean verificaEmailUpdate(@RequestParam String email){
        setUsuarioId();
        return repository.validaDadouUpdate(email, getUsuarioId(),2);
    }

    @ApiOperation(value = "Retorna se existe o CPF informado do usuario logado", response = RetornaUsuario.class)
    @GetMapping("/verifica/cpf-update")
    @ResponseStatus(HttpStatus.OK)
    public Boolean verificaCpfUpdate(@RequestParam String cpf){
        setUsuarioId();
        return repository.validaDadouUpdate(cpf, getUsuarioId(),1);
    }

    @ApiOperation(value = "Retorna se existe o nome de usuario informado do usuario logado", response = RetornaUsuario.class)
    @GetMapping("/verifica/username-update")
    @ResponseStatus(HttpStatus.OK)
    public Boolean verificaUserNameUpdate(@RequestParam String user){
        setUsuarioId();
        return repository.validaDadouUpdate(user, getUsuarioId(),3);
    }
/*
    private void validaCampos(String login, String cpf, String email, String celular, String nome, Boolean isUpdate) {
        if(celular.isEmpty() || celular == null){
            throw new NullPointerException("Celular inválido");
        }

        if(nome.isEmpty() || nome == null){
            throw new NullPointerException("Nome inválido");
        }

        if(login.isEmpty() || login == null) {
            throw new NullPointerException("Login inválido");
        }

        else if(isUpdate){
            if(repository.validaDadouUpdate(login,getIdUsuario(),3)){
                throw new NullPointerException("Login já existe");
            }
            else if(repository.validaDadouUpdate(cpf, getIdUsuario(),1)){
                throw new NullPointerException("CPF já existe");
            }
            else if(repository.validaDadouUpdate(email,getIdUsuario(),2)){
                throw new NullPointerException("Email já existe");
            }
        }
        else {

            if (repository.validaDado(login, 3)) {
                throw new NullPointerException("Login já existe");
            }

            if (cpf.isEmpty() || cpf == null) {
                throw new NullPointerException("CPF inválido");
            } else if (repository.validaDado(cpf, 1)) {
                throw new NullPointerException("CPF já existe");
            }

            if (email.isEmpty() || email == null || !email.contains("@")) {
                throw new NullPointerException("Email inválido");
            } else if (repository.validaDado(email, 2)) {
                throw new NullPointerException("Email já existe");
            }
        }
        //  return valida = repository.validaCampos(login, cpf, email);
    }
*/
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
}
