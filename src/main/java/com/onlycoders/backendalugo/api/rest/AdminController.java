package com.onlycoders.backendalugo.api.rest;


import com.onlycoders.backendalugo.model.entity.admin.LogErros;
import com.onlycoders.backendalugo.model.entity.usuario.templates.RetornaUsuario;
import com.onlycoders.backendalugo.model.repository.AdminRepository;
import com.onlycoders.backendalugo.model.repository.UsuarioRepository;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "Admin")
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
//@Secured("ADMIN")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

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

        System.out.println(sortBy);
        Pageable paging = PageRequest.of(page, size, (order.equalsIgnoreCase("desc")) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());

        List<LogErros> listErros = adminRepository.retornaErros();
        int start =(int) paging.getOffset();
        int end = (start + paging.getPageSize()) > listErros.size() ? listErros.size() : (start + paging.getPageSize());
        return new PageImpl<>(listErros.subList(start,end), paging, listErros.size());
    }
}
