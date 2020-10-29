package com.onlycoders.backendalugo.api.rest;

import com.onlycoders.backendalugo.model.entity.produto.Produto;
import com.onlycoders.backendalugo.model.entity.produto.templates.Fotos;
import com.onlycoders.backendalugo.model.entity.produto.templates.RetornaProduto;
import com.onlycoders.backendalugo.model.repository.ProdutoRepository;
import com.onlycoders.backendalugo.model.repository.UsuarioRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sun.nio.ch.IOUtil;

import javax.servlet.http.Part;
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
        return repository.findProduto(getIdUsuario(false), "0",1);

        //return GeraLista(listaProduto);
    }

    @ApiOperation(value = "Retorna um único produto do usuario logado.", response = RetornaProduto.class)
    @GetMapping("/produto-logado")
    @ResponseStatus(HttpStatus.OK)
    public RetornaProduto retornaProdutoUsuarioLogado(@RequestParam String id_produto) {
        return repository.findProduto(getIdUsuario(false), validaProduto(id_produto),2).get(0);
        //return GeraLista(listaProduto);
    }

    @ApiOperation(value = "Retorna todos os produtos", response = RetornaProduto.class)
    @GetMapping("/lista-produto")
    @ResponseStatus(HttpStatus.OK)
    public List<RetornaProduto> retornaProdutos() {
        //return repository.teste(id);
        String user = getIdUsuario(true);
       return repository.findProduto(user, "0",4);
    }

    @ApiOperation(value = "Retorna um único produto", response = RetornaProduto.class)
    @GetMapping("/produto")
    @ResponseStatus(HttpStatus.OK)
    public RetornaProduto retornaProduto(@RequestParam String id_produto) {
        return repository.findProduto("0", validaProduto(id_produto),3).get(0);
    }

    @ApiOperation(value = "Pesquisa Produto", response = RetornaProduto.class)
    @GetMapping("/produto-pesquisa")
    @ResponseStatus(HttpStatus.OK)
    public List<RetornaProduto> retornaProdutoPesquisa(@RequestParam String produto) {
        return repository.findProduto("0", validaProduto(produto),3);
    }

    @ApiOperation(value = "Retorna produtos de um usuario, id ou login", response = RetornaProduto.class)
    @GetMapping("/produto-usuario")
    @ResponseStatus(HttpStatus.OK)
    public RetornaProduto retornaProdutosUsuario(@RequestParam String id_usuario) {
        return repository.findProduto(id_usuario, "0",1).get(0);
    }

    @ApiOperation(value = "Cadastra novo produto do usuario logado")
    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public RetornaProduto cadastra(@RequestBody Produto produto){
        return repository.createProduto(getIdUsuario(false),produto.getNome(),produto.getDescricao_curta(),produto.getDescricao(),
                produto.getValor_base_diaria(), produto.getValor_base_mensal(), produto.getValor_produto(),
                produto.getData_compra());
    }

    @ApiOperation(value = "Atualiza/cadastra foto de produto")
    @PutMapping("/upload-foto")
    @ResponseStatus(HttpStatus.OK)
    public Boolean atualizaCadastrafoto(@RequestParam Part capa_foto,
                            @RequestParam String id_produto) throws NotFoundException {
        Optional<String> usuario = Optional.ofNullable(Optional
                .of(getIdUsuario(false))
                .orElseThrow(() -> new NotFoundException("Usuario não logado")));
            try{
                InputStream is = capa_foto.getInputStream();
                byte[] bytes = new byte[(int) capa_foto.getSize()];
                IOUtils.readFully(is,bytes);
                is.close();
                return repository.uploadFoto(usuario.get(), id_produto, bytes);

            } catch (IOException e) {
                e.printStackTrace();
        }
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

    public String getIdUsuario(boolean pesquisa){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login;
        if(!auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken){
            if(!pesquisa)
                throw new NullPointerException("Usuario não logado");
            else
                return "0";
        }

        login = repositoryUsuario.retornaIdUsuario(auth.getName());
        if (login.isEmpty() || login == null) {
            if(!pesquisa)
                throw new NullPointerException("Usuario não encontrado");
            else
                return "0";
        }
        return login;
    }

    public String validaProduto(String id_produto){
        if (id_produto.isEmpty() || id_produto == null || id_produto.equals("0"))
            throw new NullPointerException("Parametro id_produto vazio");

        return id_produto;
    }
}
