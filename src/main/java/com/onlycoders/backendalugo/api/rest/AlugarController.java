package com.onlycoders.backendalugo.api.rest;

import com.onlycoders.backendalugo.model.entity.aluguel.Aluguel;
import com.onlycoders.backendalugo.model.entity.produto.Produto;
import com.onlycoders.backendalugo.model.repository.AluguelRepository;
import com.onlycoders.backendalugo.model.repository.ProdutoRepository;
import com.onlycoders.backendalugo.model.repository.UsuarioRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.util.Optional;

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
    public Boolean EfetuaAluguel(@RequestBody Aluguel aluguel) {
        return aluguelRepository.efetuaAluguel(getIdUsuario(), aluguel.getId_produto(),
                aluguel.getData_inicio(), aluguel.getData_fim(), aluguel.getValor_aluguel());
    }

    @ApiOperation(value = "Retorna aluguel do usuario logado")
    @GetMapping("/aluguel")
    @ResponseStatus(HttpStatus.OK)
    public Aluguel retornaAluguel() {
        return aluguelRepository.retornaAluguelLocador(getIdUsuario());
    }

    public String getIdUsuario() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Optional<String> u = Optional.of(usuarioRepository.retornaIdUsuario(auth.getName()));

        String usuario = String.valueOf(u.orElseThrow(()
                -> new UsernameNotFoundException("Usuario não encontrado")));

        return usuario = UriUtils.decode(usuario, "UTF-8");
    }
}
