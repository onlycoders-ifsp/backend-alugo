package com.onlycoders.backendalugo.api.rest;

import com.onlycoders.backendalugo.model.entity.login.RetornaLogin;
import com.onlycoders.backendalugo.model.entity.login.UsuarioLogin;
import com.onlycoders.backendalugo.model.entity.produto.Produto;
import com.onlycoders.backendalugo.model.entity.produto.templates.CadAtuProduto;
import com.onlycoders.backendalugo.model.entity.produto.templates.RetornaProduto;
import com.onlycoders.backendalugo.model.entity.produto.templates.UsuarioProduto;
import com.onlycoders.backendalugo.model.entity.usuario.Usuario;
import com.onlycoders.backendalugo.model.repository.ProdutoRepository;
import com.onlycoders.backendalugo.model.repository.UsuarioRepository;
import com.onlycoders.backendalugo.service.CustomUserDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.util.*;

@RestController
@Api(value = "Produtos")
@RequestMapping("/produtos")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProdutoController {

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private UsuarioRepository repositoryUsuario;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @ApiOperation(value = "Retorna todos os produtos")
    @GetMapping("/lista-todos")
    @ResponseStatus(HttpStatus.OK)
    public List<UsuarioProduto> retornaProdutos() {
        //System.out.println(id.idUsuario + " - " + id.produto.idProduto);
        List<Produto> p = repository.findProdutoByUsuario("0", "0");

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

    @ApiOperation(value = "Retorna produtos do usuario logado. Param: id_produto (0 para retornar todos os atributos)")
    @GetMapping("/usuario")
    @ResponseStatus(HttpStatus.OK)
    public List<UsuarioProduto> retornaProdutosUsuario(@RequestParam String id_usuario,@RequestParam String id_produto) {
        //System.out.println(id.idUsuario + " - " + id.produto.idProduto);
        List<Produto> p = repository.findProdutoByUsuario(id_usuario, id_produto);

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

    @ApiOperation(value = "Cadastra novo produto do usuario logado")
    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public Boolean inserir(@RequestBody CadAtuProduto produto){

        return repository.createProduto(getIdUsuario(),produto.produto.nome,produto.produto.descricaoCurta,produto.produto.descricao,
                produto.produto.valorBaseDiaria, produto.produto.valorBaseMensal, produto.produto.valorProduto,
                produto.produto.dataCompra,produto.produto.capaFoto);
    }

    @ApiOperation(value = "Altera dados cadastrais do produto")
    @PutMapping("/altera")
    @ResponseStatus(HttpStatus.OK)
    public Boolean update(@RequestBody CadAtuProduto produto) {
        return repository.updateProduto(produto.produto.idProduto, produto.produto.nome, produto.produto.descricaoCurta,
                produto.produto.descricao,produto.produto.valorBaseDiaria, produto.produto.valorBaseMensal, produto.produto.valorProduto,
                produto.produto.dataCompra,produto.produto.capaFoto);
    }

    @ApiOperation(value = "Ativa ou inativa produto.")
    @DeleteMapping("ativa-inativa")
    @ResponseStatus(HttpStatus.OK)
    public Boolean ativaInativaProduto(@RequestBody RetornaProduto produto){
        return repository.ativaInativaProduto(produto.getIdProduto());
    }

    public String getIdUsuario(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Optional<String> u = Optional.of(repositoryUsuario.retornaIdUsuario(auth.getName()));

        String usuario = String.valueOf(u.orElseThrow(()
                -> new UsernameNotFoundException("Usuario não encontrado")));

        usuario = UriUtils.decode(usuario,"UTF-8");

        return usuario;
    }
}
