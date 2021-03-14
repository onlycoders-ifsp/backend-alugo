/* ########################## CONTROLLER API DE USUÁRIO ##########################
 * Data Criação: 14/06/2020
 * Programador: Dilan Lima
 * Decrição: Recebe as requisições, delega os processamentos e cuida dos retornos para api de usuários
 * #########################################################################
 */
package com.onlycoders.backendalugo.api.rest;
import com.google.common.base.Throwables;
import com.onlycoders.backendalugo.model.entity.email.templatesEmails.TemplateEmails;
import com.onlycoders.backendalugo.model.entity.usuario.Usuario;
import com.onlycoders.backendalugo.model.entity.usuario.templates.AlteraSenha;
import com.onlycoders.backendalugo.model.entity.usuario.templates.RequestUsuario;
import com.onlycoders.backendalugo.model.entity.usuario.templates.RetornaUsuario;
import com.onlycoders.backendalugo.model.repository.LogRepository;
import com.onlycoders.backendalugo.model.repository.UsuarioRepository;
import com.onlycoders.backendalugo.service.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RestController
@Api(value = "Usuarios")
@RequestMapping("/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final LogRepository logRepository;
    private final EmailService emailService;

    @Autowired
    public UsuarioController(UsuarioRepository usuarioRepository, LogRepository logRepository, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.logRepository = logRepository;
        this.emailService = emailService;
    }

    @ApiOperation(value = "Retorna dados do usuario logado", response = RetornaUsuario.class)
    @GetMapping("/usuario-logado")
    @ResponseStatus(HttpStatus.OK)
    public RetornaUsuario retornaUsuarioLogado() {
        try {
            return usuarioRepository.findUsuario(getIdUsuario()).get(0);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return null;
        }
        //return GeraLista(usuarioRepository.findUsuario(id_usuario));
    }

    @ApiOperation(value = "Retorna dados de um único usuario pelo id ou login", response = RetornaUsuario.class)
    @GetMapping("/usuario")
    @ResponseStatus(HttpStatus.OK)
    public RetornaUsuario retornaUsuario(@RequestParam String id_usuario) {

        return usuarioRepository.findUsuario(id_usuario).get(0);
    }

 /*
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
        List<RetornaUsuario> listUsers = usuarioRepository.findUsuario("0");
        return new PageImpl<>(listUsers,paging,listUsers.size());
        //return GeraLista(usuarioRepository.findUsuario(id_usuario));
    }

  */

    @ApiOperation(value = "Verifica código de ativação")
    @GetMapping("/verficacao-email")
    @ResponseStatus(HttpStatus.CREATED)
    public Boolean verificaCodigo(@RequestParam("code") String code){
        String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
        try {
            Boolean ativo = usuarioRepository.ativaUsuario(code, user);
            System.out.println(ativo);
            return ativo;
        }catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @ApiOperation(value = "Cadastro de usuário", response = RetornaUsuario.class)
    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public Boolean salvar(@RequestBody Usuario usuario,@RequestParam("url")String url){
        //validaCampos(usuario.getLogin(), usuario.getCpf(), usuario.getEmail(),
        //        usuario.getCelular(), usuario.getNome(), false);
        try {
            String site = url;
            String verificationCode = UUID.randomUUID().toString();
            site += "/valida-cadastro?key=" + verificationCode;
            if(usuarioRepository
                    .createUsuarioMin(usuario.getNome(), usuario.getEmail().toLowerCase(), usuario.getLogin(),
                            usuario.getSenha(), usuario.getCpf(), usuario.getCelular(), verificationCode)){
                String mailBody = new TemplateEmails().confirmaCadastro(usuario.getNome(),site);
                emailService.sendEmail(usuario.getEmail(),"Registro de usuário",mailBody);
                return true;
            }
            else
                return false;
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @ApiOperation(value = "Atualiza/Cadastra foto de usuario")
    @PutMapping("/upload-foto")
    @ResponseStatus(HttpStatus.CREATED)
    public Boolean atualizaFoto(@RequestParam Part capa_foto){
        String usuario = getIdUsuario();
        try{
            InputStream is = capa_foto.getInputStream();
            byte[] bytes = new byte[(int) capa_foto.getSize()];
            IOUtils.readFully(is,bytes);
            is.close();
            return usuarioRepository.uploadFoto(usuario, bytes);

        } catch(IOException e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @ApiOperation(value = "Muda senha do usuario logado")
    @PutMapping("/altera-senha")
    @ResponseStatus(HttpStatus.OK)
    public Boolean alteraSenha(@RequestBody AlteraSenha senha){
        try {
            if (new BCryptPasswordEncoder().matches(senha.getSenha_antiga(), usuarioRepository.retornaSenha(getIdUsuario())))
                return usuarioRepository.alteraSenha(getIdUsuario(), senha.getSenha_nova());
            else
                throw new NotFoundException("Senha incorreta");
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @ApiOperation(value = "Alterar dados cadastrais do usuario logado", response = RetornaUsuario.class)
    @PutMapping("altera-dados")
    @ResponseStatus(HttpStatus.OK)
    public RetornaUsuario alteraUsuario(@RequestBody RequestUsuario usuario) {
        //validaCampos(usuario.getLogin(),usuario.getCpf(),usuario.getEmail(),usuario.getCelular(),usuario.getNome(),true);
        try {
            return usuarioRepository.updateUserById(getIdUsuario(), usuario.getNome(), usuario.getEmail().toLowerCase(),
                    usuario.getLogin(), usuario.getCpf(), usuario.getCelular(), usuario.getData_nascimento(),
                    usuario.getCep(), usuario.getLogradouro(), usuario.getComplemento(), usuario.getBairro(),
                    usuario.getNumero()).get(0);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    @ApiOperation(value = "Retorna se existe o email informado", response = RetornaUsuario.class)
    @GetMapping("/verifica/email")
    @ResponseStatus(HttpStatus.OK)
    public Boolean verificaEmail(@RequestParam String email){
        try {
            return usuarioRepository.validaDado(email, 2);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @ApiOperation(value = "Retorna se existe o CPF informado", response = RetornaUsuario.class)
    @GetMapping("/verifica/cpf")
    @ResponseStatus(HttpStatus.OK)
    public Boolean verificaCpf(@RequestParam String cpf){
        try {
            return usuarioRepository.validaDado(cpf, 1);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @ApiOperation(value = "Retorna se existe o nome de usuario informado", response = RetornaUsuario.class)
    @GetMapping("/verifica/username")
    @ResponseStatus(HttpStatus.OK)
    public Boolean verificaUserName(@RequestParam String user){
        try {
            return usuarioRepository.validaDado(user, 3);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String userName = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, userName, e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @ApiOperation(value = "Retorna se existe o email informado do usuario logado", response = RetornaUsuario.class)
    @GetMapping("/verifica/email-update")
    @ResponseStatus(HttpStatus.OK)
    public Boolean verificaEmailUpdate(@RequestParam String email){
        try {
            return usuarioRepository.validaDadouUpdate(email, getIdUsuario(), 2);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @ApiOperation(value = "Retorna se existe o CPF informado do usuario logado", response = RetornaUsuario.class)
    @GetMapping("/verifica/cpf-update")
    @ResponseStatus(HttpStatus.OK)
    public Boolean verificaCpfUpdate(@RequestParam String cpf){
        try {
            return usuarioRepository.validaDadouUpdate(cpf, getIdUsuario(), 1);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @ApiOperation(value = "Retorna se existe o nome de usuario informado do usuario logado", response = RetornaUsuario.class)
    @GetMapping("/verifica/username-update")
    @ResponseStatus(HttpStatus.OK)
    public Boolean verificaUserNameUpdate(@RequestParam String user){
        try {
            return usuarioRepository.validaDadouUpdate(user, getIdUsuario(), 3);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String userName = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, userName, e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
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
                if(usuarioRepository.validaDadouUpdate(login,getIdUsuario(),3)){
                    throw new NullPointerException("Login já existe");
                }
                else if(usuarioRepository.validaDadouUpdate(cpf, getIdUsuario(),1)){
                    throw new NullPointerException("CPF já existe");
                }
                else if(usuarioRepository.validaDadouUpdate(email,getIdUsuario(),2)){
                    throw new NullPointerException("Email já existe");
                }
            }
            else {

                if (usuarioRepository.validaDado(login, 3)) {
                    throw new NullPointerException("Login já existe");
                }

                if (cpf.isEmpty() || cpf == null) {
                    throw new NullPointerException("CPF inválido");
                } else if (usuarioRepository.validaDado(cpf, 1)) {
                    throw new NullPointerException("CPF já existe");
                }

                if (email.isEmpty() || email == null || !email.contains("@")) {
                    throw new NullPointerException("Email inválido");
                } else if (usuarioRepository.validaDado(email, 2)) {
                    throw new NullPointerException("Email já existe");
                }
            }
            //  return valida = usuarioRepository.validaCampos(login, cpf, email);
        }
    */
    public String getIdUsuario(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login;

        if(!auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken){
            throw new NullPointerException("Usuario não logado");
        }

        login = usuarioRepository.retornaIdUsuario(auth.getName().split("\\|")[0]);
        if (login.isEmpty()) {
            throw new NullPointerException("Usuario não encontrado");
        }
        return login;
    }
}
