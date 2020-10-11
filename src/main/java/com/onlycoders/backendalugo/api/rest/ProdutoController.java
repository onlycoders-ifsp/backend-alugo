package com.onlycoders.backendalugo.api.rest;

import com.onlycoders.backendalugo.model.entity.produto.Produto;
import com.onlycoders.backendalugo.model.entity.produto.templates.RetornaProduto;
import com.onlycoders.backendalugo.model.entity.produto.templates.UsuarioProduto;
import com.onlycoders.backendalugo.model.repository.ProdutoRepository;
import com.onlycoders.backendalugo.model.repository.UsuarioRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
/*
    @ApiOperation(value = "Retorna todos os produtos", response = RetornaProduto.class)
    @GetMapping("/lista-todos")
    @ResponseStatus(HttpStatus.OK)
    public List<RetornaProduto> RetornaProdutos() {
        //System.out.println(id.idUsuario + " - " + id.produto.idProduto);
        return repository.findProdutoByUsuario("0", "0");
    }
*/
    @ApiOperation(value = "Retorna produtos do usuario logado. id_produto (0 para retornar todos os produtos)", response = RetornaProduto.class)
    @GetMapping("/usuario")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> RetornaProdutosUsuarioLogado(@RequestParam String id_produto) {

        List<RetornaProduto> listaProduto =  repository.findProdutoByUsuario(GetIdUsuario(), id_produto);

        return GeraLista(listaProduto);
    }

    @ApiOperation(value = "Retorna produtos. id_produto para produto ou 0 para todos. id_usuario para o usuario ou 0 para todos", response = RetornaProduto.class)
    @GetMapping("/pesquisa")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> RetornaProdutosUsuario(@RequestParam String id_usuario, @RequestParam String id_produto) {
       List<RetornaProduto> listaProduto = repository.findProdutoByUsuario(id_usuario, id_produto);

        return GeraLista(listaProduto);
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

    @ApiOperation(value = "Cadastra novo produto do usuario logado")
    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public Boolean Cadastra(@RequestBody Produto produto){
        return repository.createProduto(GetIdUsuario(),produto.getNome(),produto.getDescricao_curta(),produto.getDescricao(),
                produto.getValor_base_diaria(), produto.getValor_base_mensal(), produto.getValor_produto(),
                produto.getData_compra(),produto.getCapa_foto());
    }

    @ApiOperation(value = "Altera dados cadastrais do produto")
    @PutMapping("/altera")
    @ResponseStatus(HttpStatus.OK)
    public Boolean Atualiza(@RequestBody Produto produto) {
        return repository.updateProduto(produto.getId_produto(), produto.getNome(), produto.getDescricao_curta(),
                produto.getDescricao(),produto.getValor_base_diaria(), produto.getValor_base_mensal(), produto.getValor_produto(),
                produto.getData_compra(),produto.getCapa_foto());
    }

    @ApiOperation(value = "Ativa ou inativa produto.")
    @DeleteMapping("ativa-inativa")
    @ResponseStatus(HttpStatus.OK)
    public Boolean AtivaInativa(@RequestParam String id_produto){
        return repository.ativaInativaProduto(id_produto);
    }

    public String GetIdUsuario(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Optional<String> u = Optional.of(repositoryUsuario.retornaIdUsuario(auth.getName()));

        String usuario = String.valueOf(u.orElseThrow(()
                -> new UsernameNotFoundException("Usuario não encontrado")));

        usuario = UriUtils.decode(usuario,"UTF-8");

        return usuario;
    }

    public HashMap<String, Object>GeraLista(List<RetornaProduto> listaProduto){
        HashMap<String, Object> mapProduto = new HashMap<String, Object>();
        int cont = 1;
        for (RetornaProduto p : listaProduto){
            mapProduto.put("Produto " + String.valueOf(cont),p);
            //System.out.println(mapProduto.get("Produto").getNome());
            cont++;
        }
        //mapProduto.put("Produto",listaProduto);
        return mapProduto;
    }
}
