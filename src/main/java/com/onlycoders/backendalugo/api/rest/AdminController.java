package com.onlycoders.backendalugo.api.rest;


import com.onlycoders.backendalugo.model.entity.admin.LogErros;
import com.onlycoders.backendalugo.model.entity.produto.templates.Categorias;
import com.onlycoders.backendalugo.model.entity.produto.templates.DtAlugadas;
import com.onlycoders.backendalugo.model.entity.produto.templates.ProdutoAluguel;
import com.onlycoders.backendalugo.model.entity.produto.templates.RetornaProduto;
import com.onlycoders.backendalugo.model.entity.usuario.templates.RetornaUsuario;
import com.onlycoders.backendalugo.model.repository.AdminRepository;
import com.onlycoders.backendalugo.model.repository.ProdutoRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Api(value = "Admin")
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
@Secured("ROLE_ADMIN")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @ApiOperation(value = "Deleta usuário")
    @DeleteMapping("inativa-usuario")
    public Boolean deletar(@RequestParam String id_usuario) {
        if (id_usuario.isEmpty() || id_usuario == null || id_usuario.equals("0"))
            throw new NullPointerException("Parametro id_usuario vazio");

        return adminRepository.deleteUserById(id_usuario);
    }

    @ApiOperation(value = "Retorna dados de todos os usuarios", response = RetornaUsuario.class)
    @GetMapping("/lista-usuario")
    @ResponseStatus(HttpStatus.OK)
    public Page<RetornaUsuario>
    retornaUsuario(@RequestParam(value = "page",
                                 required = false,
                                 defaultValue = "0") int page,
                   @RequestParam(value = "size",
                                 required = false,
                                 defaultValue = "10") int size) {
        Pageable paging = PageRequest.of(page, size);
        List<RetornaUsuario> listUsuarios = adminRepository.findUsuario("0");

        int start =(int) paging.getOffset();
        int end = (start + paging.getPageSize()) > listUsuarios.size() ? listUsuarios.size() : (start + paging.getPageSize());
        return new PageImpl<>(listUsuarios.subList(start,end), paging, listUsuarios.size());
    }

    @ApiOperation(value = "Retorna log erros", response = LogErros.class)
    @GetMapping("/log-erros")
    @ResponseStatus(HttpStatus.OK)
    public Page<LogErros>
    retornaLogErros(@RequestParam(value = "page",required = false,defaultValue = "0") int page,
                    @RequestParam(value = "size",required = false,defaultValue = "10") int size,
                    @RequestParam(value = "sort",required = false,defaultValue = "id") String sortBy,
                    @RequestParam(value = "order",required = false,defaultValue = "desc") String order){
        Pageable paging = PageRequest.of(page, size, (order.equalsIgnoreCase("desc")) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());

        List<LogErros> listErros = adminRepository.retornaErros();
        int start =(int) paging.getOffset();
        int end = (start + paging.getPageSize()) > listErros.size() ? listErros.size() : (start + paging.getPageSize());
        return new PageImpl<>(listErros.subList(start,end), paging, listErros.size());
    }

    @ApiOperation(value = "Retorna produtos não publicados", response = ProdutoAluguel.class)
    @GetMapping("/publicar-produtos")
    @ResponseStatus(HttpStatus.OK)
    public Page<ProdutoAluguel>
    retornaProdutosNaoPublicados(@RequestParam(value = "page",required = false,defaultValue = "0") int page,
                                 @RequestParam(value = "size",required = false,defaultValue = "10") int size,
                                 @RequestParam(value = "sort",required = false,defaultValue = "qtd_alugueis") String sortBy,
                                 @RequestParam(value = "order",required = false,defaultValue = "desc") String order){
        Pageable paging = PageRequest.of(page, size, (order.equalsIgnoreCase("desc")) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
        Optional<Page<ProdutoAluguel>> produtos = Optional.ofNullable(
                transformaRetornoProdutoToPage(produtoRepository.findProduto("0", "0",2),paging));
        return produtos.get();
    }

    public Page <ProdutoAluguel> transformaRetornoProdutoToPage(List<RetornaProduto> ret, Pageable page){
        List<ProdutoAluguel> listPa = new ArrayList<>();
        String[] dtAluguel;
        String[] dtAluguelInicio;
        String[] dtAluguelFim;
        String[] categoria;
        String[] idCategoria;
        String[] nomeCategoria;

        for(RetornaProduto r : ret){
            List<DtAlugadas> dt = new ArrayList<DtAlugadas>();// = repository.dtAlugadas(r.getId_produto());
            List<Categorias> categorias = new ArrayList<>();

            if (r.getDt_Aluguel().contains(";")) {
                dtAluguel = r.getDt_Aluguel().split(";");
                dtAluguelInicio = dtAluguel[0].split(",");
                dtAluguelFim = dtAluguel[1].split(",");
                for (int i = 0; i < dtAluguelInicio.length; i++) {
                    DtAlugadas dtAlugadas = new DtAlugadas(dtAluguelInicio[i],dtAluguelFim[i]);
                    dt.add(dtAlugadas);
                }
            }
            if(r.getCategorias().contains(";")){
                categoria = r.getCategorias().split(";");
                idCategoria = categoria[0].split(",");
                nomeCategoria = categoria[1].split(",");

                for(int i = 0;i<idCategoria.length;i++){
                    Categorias cat = new Categorias();
                    cat.setIdCategoria(idCategoria[i]);
                    cat.setNomeCategoria(nomeCategoria[i]);
                    categorias.add(cat);
                }
            }
            ProdutoAluguel pa = new ProdutoAluguel();
            pa.setAtivo(r.getAtivo());
            pa.setCapa_foto(r.getCapa_foto());
            pa.setData_compra(r.getData_compra());
            pa.setDescricao(r.getDescricao());
            pa.setDescricao_curta(r.getDescricao_curta());
            pa.setDt_alugadas(dt);
            pa.setCategorias(categorias);
            pa.setId_produto(r.getId_produto());
            pa.setId_usuario(r.getId_usuario());
            pa.setMedia_avaliacao(r.getMedia_avaliacao());
            pa.setNome(r.getNome());
            pa.setQtd_alugueis(r.getQtd_alugueis());
            pa.setTotal_ganhos(r.getTotal_ganhos());
            pa.setValor_base_diaria(r.getValor_base_diaria());
            pa.setValor_base_mensal(r.getValor_base_mensal());
            pa.setValor_produto(r.getValor_produto());
            pa.setPublicado(r.getPublicado());
            listPa.add(pa);
        }
        //Page<ProdutoAluguel> produtos = new PageImpl<>(listPa,page, listPa.size());
        //PagedListHolder paging = new PagedListHolder(listPa);
        //paging.setPage(page.getPageNumber());
        //paging.setPageSize(page.getPageSize());
        //return paging;//
        int start =(int) page.getOffset();
        int end = (start + page.getPageSize()) > listPa.size() ? listPa.size() : (start + page.getPageSize());
        return new PageImpl<>(listPa.subList(start,end),page,listPa.size());//listPa;
    }
}
