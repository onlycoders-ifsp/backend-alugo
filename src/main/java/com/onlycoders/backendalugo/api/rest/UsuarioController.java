/* ########################## CONTROLLER API DE USUÁRIO ##########################
 * Data Criação: 14/06/2020
 * Programador: Dilan Lima
 * Decrição: Recebe as requisições, delega os processamentos e cuida dos retornos para api de usuários
 * #########################################################################
 */
package com.onlycoders.backendalugo.api.rest;

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

    @ApiOperation(value = "Seleciona unico usuário pelo id")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RetornaUsuario> retornaUsuario(@RequestParam String id_usuario) {

        id_usuario = UriUtils.decode(id_usuario,"UTF-8");
        //System.out.println(id.idUsuario);
        return repository.findUsuario(id_usuario);
    }

    @ApiOperation(value = "Cria novo usuário")
    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public List<RetornaUsuario> salvar(@RequestBody CadAtuUsuario usuario) {
        //System.out.println(usuario.nome);

       validaCampos(usuario.login, usuario.cpf,usuario.email);

        return repository
                .createUsuarioMin(usuario.nome, usuario.email,usuario.login,
                        usuario.senha,usuario.cpf, usuario.celular);
    }

    private void validaCampos(String login, String cpf, String email) {
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
        return repository.alteraSenha(getIdUsuario(), id.senha);
    }

    @ApiOperation(value = "Muda dados usuário")
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public List <RetornaUsuario>  alteraUsuario(@RequestBody CadAtuUsuario usuario) {
                return repository.updateUserById(getIdUsuario(),usuario.nome,usuario.email, usuario.login,
                usuario.cpf, usuario.celular,usuario.dataNascimento, usuario.cep,usuario.logradouro,
                usuario.complemento, usuario.bairro, usuario.numero);
    }

    @ApiOperation(value = "Retorna produtos do usuario logado pelo idproduto ou po 0")
    @GetMapping("/produto")
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
