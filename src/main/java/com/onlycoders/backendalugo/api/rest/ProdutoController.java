package com.onlycoders.backendalugo.api.rest;

import com.onlycoders.backendalugo.model.entity.produto.Produto;
import com.onlycoders.backendalugo.model.entity.produto.templates.Fotos;
import com.onlycoders.backendalugo.model.entity.produto.templates.RetornaProduto;
import com.onlycoders.backendalugo.model.repository.ProdutoRepository;
import com.onlycoders.backendalugo.model.repository.UsuarioRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sun.nio.ch.IOUtil;

import java.io.IOException;
import java.io.InputStream;
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

    public ProdutoRepository getRepository() {
        return repository;
    }

    public void setRepository(ProdutoRepository repository) {
        this.repository = repository;
    }

    public UsuarioRepository getRepositoryUsuario() {
        return repositoryUsuario;
    }

    public void setRepositoryUsuario(UsuarioRepository repositoryUsuario) {
        this.repositoryUsuario = repositoryUsuario;
    }

    @ApiOperation(value = "Retorna produtos do usuario logado.", response = RetornaProduto.class)
    @GetMapping("/lista-produto-logado")
    @ResponseStatus(HttpStatus.OK)
    public List<RetornaProduto> retornaProdutosUsuarioLogado() {
        return repository.findProdutoByUsuario(getIdUsuario(), "0");

        //return GeraLista(listaProduto);
    }

    @ApiOperation(value = "Retorna um único produto do usuario logado.", response = RetornaProduto.class)
    @GetMapping("/produto-logado")
    @ResponseStatus(HttpStatus.OK)
    public RetornaProduto retornaProdutoUsuarioLogado(@RequestParam String id_produto) {
        return repository.findProdutoByUsuario(getIdUsuario(), validaProduto(id_produto)).get(0);
        //return GeraLista(listaProduto);
    }

    @ApiOperation(value = "Retorna todos os produtos", response = RetornaProduto.class)
    @GetMapping("/lista-produto")
    @ResponseStatus(HttpStatus.OK)
    public List<RetornaProduto> retornaProdutos() {
        //return repository.teste(id);
       return repository.findProdutoByUsuario("0", "0");

        // GeraLista(listaProduto);
/*
        List<UsuarioProduto> listaProdutos = new ArrayList<UsuarioProduto>();

        /*
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
     */
    }

    @ApiOperation(value = "Retorna um único produto", response = RetornaProduto.class)
    @GetMapping("/produto")
    @ResponseStatus(HttpStatus.OK)
    public RetornaProduto retornaProduto(@RequestParam String id_produto) {
        return repository.findProdutoByUsuario("0", validaProduto(id_produto)).get(0);
    }

    @ApiOperation(value = "Retorna produtos de um usuario, id ou login", response = RetornaProduto.class)
    @GetMapping("/produto-usuario")
    @ResponseStatus(HttpStatus.OK)
    public RetornaProduto retornaProdutosUsuario(@RequestParam String id_usuario) {
        return repository.findProdutoByUsuario(getIdUsuario(), "0").get(0);
    }

    @ApiOperation(value = "Cadastra novo produto do usuario logado")
    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public Boolean cadastra(@RequestBody Produto produto){
        return repository.createProduto(getIdUsuario(),produto.getNome(),produto.getDescricao_curta(),produto.getDescricao(),
                produto.getValor_base_diaria(), produto.getValor_base_mensal(), produto.getValor_produto(),
                produto.getData_compra(),produto.getCapa_foto());
    }

    @ApiOperation(value = "Altera dados cadastrais do produto")
    @PutMapping("/upload-foto")
    @ResponseStatus(HttpStatus.OK)
    public Boolean atualiza(@RequestBody Fotos foto) {
        if (!getIdUsuario().isEmpty()){
            try{
                InputStream is = foto.getCapa_foto().getInputStream();
                byte[] bytes = new byte[(int) foto.getCapa_foto().getSize()];
                IOUtils.readFully(is,bytes);
                is.close();
                System.out.println(foto);
                return repository.uploadFoto(foto.getId_produto(), bytes);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        else
            return false;
    }

    @ApiOperation(value = "Altera dados cadastrais do produto")
    @PutMapping("/altera")
    @ResponseStatus(HttpStatus.OK)
    public Boolean atualiza(@RequestBody Produto produto) {
        /*String fotos = null;
        List<String> f = Arrays.asList(produto.getFotos());

        fotos = f.stream()
                .collect(Collectors.joining(","));
*/
        return repository.updateProduto(validaProduto(produto.getId_produto()), produto.getNome(), produto.getDescricao_curta(),
                produto.getDescricao(),produto.getValor_base_diaria(), produto.getValor_base_mensal(), produto.getValor_produto(),
                produto.getData_compra(),produto.getCapa_foto());
    }

    @ApiOperation(value = "Ativa ou inativa produto.")
    @DeleteMapping("ativa-inativa")
    @ResponseStatus(HttpStatus.OK)
    public Boolean ativaInativa(@RequestParam String id_produto){
        return repository.ativaInativaProduto(validaProduto(id_produto));
    }

    /*
    public String validaLogin(){
        String id = IdUsuario.getId_usuario();
        if(id.isEmpty() || id == null || id.equals("0"))
            throw new NullPointerException("Usuario não está logado");

        return id;
    }
    */
    public String getIdUsuario(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login;

        if(!auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken){
            throw new NullPointerException("Usuario não logado");
        }

        login = repositoryUsuario.retornaIdUsuario(auth.getName());
        if (login.isEmpty() || login == null) {
            throw new NullPointerException("Usuario não encontrado");
        }
        return login;
    }

    public String validaProduto(String id_produto){
        if (id_produto.isEmpty() || id_produto == null || id_produto.equals("0"))
            throw new NullPointerException("Parametro id_produto vazio");

        return id_produto;
    }
}
