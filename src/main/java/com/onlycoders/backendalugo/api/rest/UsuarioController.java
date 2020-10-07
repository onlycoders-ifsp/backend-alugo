/* ########################## CONTROLLER API DE USUÁRIO ##########################
 * Data Criação: 14/06/2020
 * Programador: Dilan Lima
 * Decrição: Recebe as requisições, delega os processamentos e cuida dos retornos para api de usuários
 * #########################################################################
 */
package com.onlycoders.backendalugo.api.rest;

import com.onlycoders.backendalugo.exception.NotFoundException;
import com.onlycoders.backendalugo.model.entity.produto.Produto;
import com.onlycoders.backendalugo.model.entity.produto.templates.RetornaProduto;
import com.onlycoders.backendalugo.model.entity.produto.templates.UsuarioProduto;
import com.onlycoders.backendalugo.model.entity.usuario.templates.CadAtuUsuario;
import com.onlycoders.backendalugo.model.entity.usuario.templates.RetornaUsuario;
import com.onlycoders.backendalugo.model.repository.ProdutoRepository;
import com.onlycoders.backendalugo.model.repository.UsuarioRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
import org.omg.CORBA.RepositoryIdHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriUtils;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
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

    @ApiOperation(value = "Retorna dados do usuario. Param: id_usuario (0 retorna todos usuarios)")
    @GetMapping("/usuario")
    @ResponseStatus(HttpStatus.OK)
    public List<RetornaUsuario> retornaUsuario(@RequestParam String id_usuario) {

        if (id_usuario.isEmpty() || id_usuario == null)
            throw new NullPointerException("Parametro id_usuario vazio");

        return repository.findUsuario(id_usuario,0);
    }

    @ApiOperation(value = "Retorna dados do usuario pelo login. Param: login")
    @GetMapping("/usuario-login")
    @ResponseStatus(HttpStatus.OK)
    public List<RetornaUsuario> retornaUsuarioLogin(@RequestParam String login) {

        if (login.isEmpty() || login == null || login.length()<2)
            throw new NullPointerException("Parametro login vazio");

        return repository.findUsuario(login,1);
    }

    @ApiOperation(value = "Cadastro de usuário")
    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public List<RetornaUsuario> salvar(@RequestBody CadAtuUsuario usuario) throws NotFoundException {
        //System.out.println(usuario.nome);

       validaCampos(usuario.login, usuario.cpf,usuario.email, usuario.celular, usuario.nome);

        return repository
                .createUsuarioMin(usuario.nome, usuario.email,usuario.login,
                        usuario.senha,usuario.cpf, usuario.celular);
    }

    private void validaCampos(String login, String cpf, String email, String celular, String nome) throws NotFoundException {
        if(celular.isEmpty() || celular == null){
            throw new NotFoundException("Celular inválido");
        }

        if(nome.isEmpty() || nome == null){
            throw new NotFoundException("Nome inválido");
        }

        if(login.isEmpty() || login == null) {
            throw new NotFoundException("Login inválido");
        }
        else if(repository.validaDado(login,3)){
            throw new NotFoundException("Login já existe");
        }

        if(cpf.isEmpty() || cpf == null) {
            throw new NotFoundException("CPF inválido");
        }
        else if(repository.validaDado(cpf,1)){
                throw new NotFoundException("CPF já existe");
        }

        if (email.isEmpty() || email == null || !email.contains("@")) {
            throw new NotFoundException("Email inválido");
        }
        else if(repository.validaDado(email, 2)){
            throw new NotFoundException("Email já existe");
        }
        //  return valida = repository.validaCampos(login, cpf, email);
    }

    @Secured("ADMIN")
    @ApiOperation(value = "Deleta usuário, apenas usuario com role admin")
    @DeleteMapping("inativa")
    @ResponseStatus(HttpStatus.OK)
    public Boolean deletar(@RequestBody CadAtuUsuario id) {
        return repository.deleteUserById(id.idUsuario);
    }

    @ApiOperation(value = "Muda senha do usuario cadastrado")
    @PutMapping("/altera-senha")
    @ResponseStatus(HttpStatus.OK)
    public Boolean alteraSenha(@RequestBody CadAtuUsuario id) {
        return repository.alteraSenha(getIdUsuario(), id.senha);
    }

    @ApiOperation(value = "Alterar dados cadastrais do usuario cadastrado")
    @PutMapping("altera-dados")
    @ResponseStatus(HttpStatus.OK)
    public List <RetornaUsuario>  alteraUsuario(@RequestBody CadAtuUsuario usuario) {
                return repository.updateUserById(getIdUsuario(),usuario.nome,usuario.email, usuario.login,
                usuario.cpf, usuario.celular,usuario.dataNascimento, usuario.cep,usuario.logradouro,
                usuario.complemento, usuario.bairro, usuario.numero);
    }

    @ApiOperation(value = "Retorna todos os produtos do usuario logado. Param: id_produto (0 retorna todos os produtos)")
    @GetMapping("/produtos")
    @ResponseStatus(HttpStatus.OK)
    public List<UsuarioProduto> retornaProdutoByLoggedUsuario(@RequestParam String id_produto) {
        //System.out.println(id.idUsuario + " - " + id.produto.idProduto);

        id_produto = UriUtils.decode(id_produto,"UTF-8");
        List<Produto> p = repositoryProduto.findProdutoByUsuario(getIdUsuario(), id_produto);

        List<UsuarioProduto> listaProdutos = new ArrayList<UsuarioProduto>();

        for(Produto i :p){
            RetornaProduto user = new RetornaProduto();
            UsuarioProduto usrPrd = new UsuarioProduto();
            user.setIdProduto(i.idProduto);
            user.setNome(i.nome);
            user.setDescricaoCurta(i.descricaoCurta);
            user.setDescricao(i.descricao);
            user.setValorBaseDiaria(i.valorBaseDiaria);
            user.setValorBaseMensal(i.valorBaseMensal);
            user.setValorProduto(i.valorProduto);
            user.setDataCompra(i.dataCompra);
            user.setQtdAlugueis(i.qtdAlugueis);
            user.setTotalGanhos(i.totalGanhos);
            user.setMediaAvaliacao(i.mediaAvaliacao);
            user.setCapaFoto(i.capaFoto);
            user.setAtivo(i.ativo);
            usrPrd.setId_usuario(i.idUsuario);
            usrPrd.setProduto(user);
            listaProdutos.add(usrPrd);
        }
        //System.out.println(listProduto.get(0).getIdUsuario());
        return listaProdutos;
    }

    public String getIdUsuario(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Optional<String> u = Optional.of(repository.retornaIdUsuario(auth.getName()));

        String usuario = String.valueOf(u.orElseThrow(()
                -> new UsernameNotFoundException("Usuario não encontrado")));

        usuario = UriUtils.decode(usuario,"UTF-8");

        return usuario;
    }
}
