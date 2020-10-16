package com.onlycoders.backendalugo.api.rest;

import com.onlycoders.backendalugo.model.entity.aluguel.Aluguel;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

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
    @PostMapping("/alugar")
    @ResponseStatus(HttpStatus.OK)
    public Boolean EfetuaAluguel(@RequestBody Aluguel aluguel) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));

        Date hoje  = new SimpleDateFormat("yyyy-MM-dd").parse(sdf.format(calendar.getTime()));
        Date dtFim = new SimpleDateFormat("yyyy-MM-dd").parse(aluguel.getData_fim());
        Date dtIni = new SimpleDateFormat("yyyy-MM-dd").parse(aluguel.getData_inicio());

        if(dtFim.before(hoje) || dtIni.before(hoje) || dtFim.before(dtIni)){
            throw new NullPointerException("Período de data informado incorreto");
        }

        return aluguelRepository.efetuaAluguel(getIdUsuario(), aluguel.getId_produto(),
                aluguel.getData_inicio(), aluguel.getData_fim(), aluguel.getValor_aluguel());
    }

    @ApiOperation(value = "Retorna aluguel do usuario logado")
    @GetMapping("/aluguel")
    @ResponseStatus(HttpStatus.OK)
    public Aluguel retornaAluguel() {
        return aluguelRepository.retornaAluguelLocador(getIdUsuario());
    }

    public String getIdUsuario(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login;

        if(!auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken){
            throw new NullPointerException("Usuario não logado");
        }

        login = usuarioRepository.retornaIdUsuario(auth.getName());
        if (login.isEmpty() || login == null) {
            throw new NullPointerException("Usuario não encontrado");
        }
        return login;
    }
}
