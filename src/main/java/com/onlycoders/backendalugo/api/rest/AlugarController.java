package com.onlycoders.backendalugo.api.rest;

import com.onlycoders.backendalugo.model.entity.RetornaAluguelUsuarioProduto;
import com.onlycoders.backendalugo.model.entity.aluguel.Aluguel;
import com.onlycoders.backendalugo.model.entity.aluguel.RetornaAluguel;
import com.onlycoders.backendalugo.model.entity.produto.templates.RetornaProduto;
import com.onlycoders.backendalugo.model.entity.usuario.templates.RetornaUsuario;
import com.onlycoders.backendalugo.model.repository.AluguelRepository;
import com.onlycoders.backendalugo.model.repository.ProdutoRepository;
import com.onlycoders.backendalugo.model.repository.UsuarioRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@Api(value = "Aluguel")
@RequestMapping("/aluguel")
@CrossOrigin(origins = "*")
public class AlugarController {

    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    AluguelRepository aluguelRepository;

    public AlugarController(AluguelRepository aluguelRepository) {
        this.aluguelRepository = aluguelRepository;
    }

    @ApiOperation(value = "Efetua aluguel do usuario logado. Param id_produto")
    @PostMapping("/aluguel-efetua")
    @ResponseStatus(HttpStatus.OK)
    public Boolean EfetuaAluguel(@RequestBody Aluguel aluguel) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));

        Date hoje  = new SimpleDateFormat("yyyy-MM-dd").parse(sdf.format(calendar.getTime()));
        Date dtFim = new SimpleDateFormat("yyyy-MM-dd").parse(aluguel.getData_fim());
        Date dtIni = new SimpleDateFormat("yyyy-MM-dd").parse(aluguel.getData_inicio());

        if(dtFim.before(hoje) || dtIni.before(hoje) || dtFim.before(dtIni)){
            throw new NullPointerException("Período de data informado incorreto");
        }

        String valida = aluguelRepository.validaALuguel(getIdUsuario(), aluguel.getId_produto(), aluguel.getData_inicio(), aluguel.getData_fim());

        if (!valida.equals("0")){
            throw new NullPointerException(valida);
        }
        else {
            return aluguelRepository.efetuaAluguel(getIdUsuario(), aluguel.getId_produto(),
                    aluguel.getData_inicio(), aluguel.getData_fim(), aluguel.getValor_aluguel());
        }
    }

    @ApiOperation(value = "Retorna todos alugueis do usuario logado como locador")
    @GetMapping("/locador")
    @ResponseStatus(HttpStatus.OK)
    public List<RetornaAluguelUsuarioProduto> retornaAluguelLocadorLogado() {

        String id_locador = getIdUsuario();
        String id_locatario;
        String id_produto;

        List<RetornaAluguelUsuarioProduto >alugueis = new ArrayList<RetornaAluguelUsuarioProduto>();

        List<RetornaAluguel> aluguelEfeutuado = aluguelRepository
                .retornaAluguel(id_locador, "0","0","0",2);
        System.out.println(aluguelEfeutuado.get(0));

        for (RetornaAluguel a : aluguelEfeutuado) {
            RetornaAluguelUsuarioProduto aluguel = new RetornaAluguelUsuarioProduto();
                id_locatario = a.getId_locatario();
                id_produto = a.getId_produto();


            aluguel.setAluguel(a);
            RetornaUsuario locador = usuarioRepository.findUsuario(id_locador).get(0);
            aluguel.setLocador(locador);

            RetornaUsuario locatario = usuarioRepository.findUsuario(id_locatario).get(0);
            aluguel.setLocatario(locatario);

            RetornaProduto produto = produtoRepository.findProduto("0", id_produto, 3).get(0);
            aluguel.setProduto(produto);

            alugueis.add(aluguel);

        }

        return alugueis;
    }

    @ApiOperation(value = "Retorna todos alugueis do usuario logado como locatario")
    @GetMapping("/locatario")
    @ResponseStatus(HttpStatus.OK)
    public List<RetornaAluguelUsuarioProduto> retornaAluguelLocatarioLogado() {

        String id_locatario = getIdUsuario();
        String id_produto;
        String id_locador;

        List<RetornaAluguelUsuarioProduto >alugueis = new ArrayList<RetornaAluguelUsuarioProduto>();

        List<RetornaAluguel> aluguelEfeutuado = aluguelRepository
                .retornaAluguel("0", id_locatario,"0","0",3);

        for (RetornaAluguel a : aluguelEfeutuado) {
            RetornaAluguelUsuarioProduto aluguel = new RetornaAluguelUsuarioProduto();
            id_locador = a.getId_locador();
            id_produto = a.getId_produto();


            aluguel.setAluguel(a);
            RetornaUsuario locador = usuarioRepository.findUsuario(id_locador).get(0);
            aluguel.setLocador(locador);

            RetornaUsuario locatario = usuarioRepository.findUsuario(id_locatario).get(0);
            aluguel.setLocatario(locatario);

            RetornaProduto produto = produtoRepository.findProduto("0", id_produto, 3).get(0);
            aluguel.setProduto(produto);

            alugueis.add(aluguel);

        }

        return alugueis;

        //return aluguelRepository.retornaAluguel("0",getIdUsuario(),"0","0",3);
    }

    @ApiOperation(value = "Retorna unico aluguel pelo id_aluguel")
    @GetMapping("/aluguel")
    @ResponseStatus(HttpStatus.OK)
    public RetornaAluguel retornaAluguelAluguel(@RequestParam("id_aluguel") String id_aluguel) {
    return aluguelRepository.retornaAluguel("0","0",id_aluguel,"0",1).get(0);
    }

    @ApiOperation(value = "Retorna alugueis do produto")
    @GetMapping("/produto")
    @ResponseStatus(HttpStatus.OK)
    public List<RetornaAluguel> retornaAluguelProduto(@RequestParam("id_produto") String id_produto) {
        return aluguelRepository.retornaAluguel("0","0","0",id_produto,4);
    }

    public String getIdUsuario(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login;

        if(!auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken){
            throw new NullPointerException("Usuario não logado");
        }

        login = usuarioRepository.retornaIdUsuario(auth.getName(),"username");
        if (login.isEmpty() || login == null) {
            throw new NullPointerException("Usuario não encontrado");
        }
        return login;
    }
}
