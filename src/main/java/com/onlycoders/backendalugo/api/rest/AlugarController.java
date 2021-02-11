package com.onlycoders.backendalugo.api.rest;

import com.google.common.base.Throwables;
import com.onlycoders.backendalugo.model.entity.AluguelEncontro;
import com.onlycoders.backendalugo.model.entity.aluguel.template.*;
import com.onlycoders.backendalugo.model.entity.aluguel.Aluguel;
import com.onlycoders.backendalugo.model.entity.email.RetornoAlugueisNotificacao;
import com.onlycoders.backendalugo.model.entity.email.templatesEmails.TemplateEmails;
import com.onlycoders.backendalugo.model.entity.produto.templates.RetornaProduto;
import com.onlycoders.backendalugo.model.entity.usuario.templates.RetornaUsuario;
import com.onlycoders.backendalugo.model.repository.AluguelRepository;
import com.onlycoders.backendalugo.model.repository.LogRepository;
import com.onlycoders.backendalugo.model.repository.ProdutoRepository;
import com.onlycoders.backendalugo.model.repository.UsuarioRepository;
import com.onlycoders.backendalugo.service.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.servlet.http.Part;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
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

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private EmailService emailService;

    public AlugarController(AluguelRepository aluguelRepository) {
        this.aluguelRepository = aluguelRepository;
    }

    @ApiOperation(value = "Efetua aluguel do usuario logado. Param id_produto")
    @PostMapping("/aluguel-efetua")
    @ResponseStatus(HttpStatus.OK)
    public String EfetuaAluguel(@RequestBody Aluguel aluguel) throws ParseException {
        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));

            Date hoje = new SimpleDateFormat("yyyy-MM-dd").parse(sdf.format(calendar.getTime()));
            Date dtFim = new SimpleDateFormat("yyyy-MM-dd").parse(aluguel.getData_fim());
            Date dtIni = new SimpleDateFormat("yyyy-MM-dd").parse(aluguel.getData_inicio());

            if (dtFim.before(hoje) || dtIni.before(hoje) || dtFim.before(dtIni)) {
                throw new NullPointerException("11");
            }

            String valida = aluguelRepository.validaALuguel(getIdUsuario(), aluguel.getId_produto(), aluguel.getData_inicio(), aluguel.getData_fim(), SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]);

            if (!valida.equals("0")) {
                throw new NullPointerException(valida);
            } else {
                String idAluguel = aluguelRepository.efetuaAluguel(getIdUsuario(), aluguel.getId_produto(),
                        aluguel.getData_inicio(), aluguel.getData_fim(), aluguel.getValor_aluguel(), SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]);
                if(idAluguel.isEmpty())
                    return null;
                RetornaAluguelEncontro r = aluguelRepository.retornaAluguelEncontro(idAluguel,user);
                RetornoAlugueisNotificacao dados = aluguelRepository.retornaDadosLocadorLocatario(idAluguel,user);
                String locadorMail = new TemplateEmails().notificaLocadorAluguel(dados.getLocadorNome(),dados.getLocatarioNome(),dados.getProdutoNome(),r.getPeriodo(),r.getValor(),dados.getLocatarioCelular());
                emailService.sendEmail(dados.getLocatarioEmail(),"Solicitação de aluguel",locadorMail);
                return idAluguel;
            }
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return "";
        }
    }

    @ApiOperation(value = "Retorna todos alugueis do usuario logado como locador")
    @GetMapping("/locador")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?>// Page<RetornaAluguelUsuarioProduto>
    retornaAluguelLocadorLogado(@RequestParam(value = "page",
            required = false,
            defaultValue = "0") int page,
                                @RequestParam(value = "size",
                                        required = false,
                                        defaultValue = "10") int size,
                                @RequestParam(value = "categoria",required = false,defaultValue = "0") int categoria) throws NotFoundException {
        try {
            Pageable paging = PageRequest.of(page, size);

            String id_locador = getIdUsuario();
            String id_locatario;
            String id_produto;

            List<RetornaAluguelUsuarioProduto> alugueis = new ArrayList<RetornaAluguelUsuarioProduto>();

            Optional<List<RetornaAluguel>> aluguelEfeutuado = Optional.ofNullable(aluguelRepository
                    .retornaAluguel(id_locador, "0", "0", "0", 2, SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]));
            if (!aluguelEfeutuado.get().isEmpty()) {
                for (RetornaAluguel a : aluguelEfeutuado.get()) {
                    RetornaAluguelUsuarioProduto aluguel = new RetornaAluguelUsuarioProduto();

                    id_locatario = a.getId_locatario();
                    id_produto = a.getId_produto();
                    aluguel.setAluguel(a);
                    RetornaUsuario locador = usuarioRepository.findUsuario(id_locador).get(0);
                    aluguel.setLocador(locador);

                    RetornaUsuario locatario = usuarioRepository.findUsuario(id_locatario).get(0);
                    aluguel.setLocatario(locatario);

                    RetornaProduto produto = produtoRepository.findProduto("0", id_produto, 6, categoria, SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]).get(0);
                    aluguel.setProduto(produto);
                    alugueis.add(aluguel);
                }
                int start = (int) paging.getOffset();
                int end = (start + paging.getPageSize()) > alugueis.size() ? alugueis.size() : (start + paging.getPageSize());
                return new ResponseEntity<>(new PageImpl<>(alugueis.subList(start, end), paging, alugueis.size()), HttpStatus.OK);
            }else
                return new ResponseEntity<>(new PageImpl<>(new ArrayList<>(), PageRequest.of(1,1), 0), HttpStatus.OK);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return new ResponseEntity<>(new PageImpl<>(new ArrayList<>(), PageRequest.of(1,1), 0), HttpStatus.OK);
        }
        //return new ResponseEntity<>("Nenhum produto alugado",HttpStatus.OK);
    }

    @ApiOperation(value = "Retorna todos alugueis do usuario logado como locatario")
    @GetMapping("/locatario")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?>//Page<RetornaAluguelUsuarioProduto>
    retornaAluguelLocatarioLogado(@RequestParam(value = "page",
            required = false,
            defaultValue = "0") int page,
                                  @RequestParam(value = "size",
                                          required = false,
                                          defaultValue = "10") int size,
                                  @RequestParam(value = "categoria",required = false,defaultValue = "0") int categoria) throws NotFoundException {

        try {
            Pageable paging = PageRequest.of(page, size);
            String id_locatario = getIdUsuario();
            String id_produto;
            String id_locador;

            List<RetornaAluguelUsuarioProduto> alugueis = new ArrayList<RetornaAluguelUsuarioProduto>();

            Optional<List<RetornaAluguel>> aluguelEfeutuado = Optional.ofNullable(aluguelRepository
                    .retornaAluguel("0", id_locatario, "0", "0", 3, SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]));
            if (!aluguelEfeutuado.get().isEmpty()) {
                for (RetornaAluguel a : aluguelEfeutuado.get()) {
                    RetornaAluguelUsuarioProduto aluguel = new RetornaAluguelUsuarioProduto();
                    id_locador = a.getId_locador();
                    id_produto = a.getId_produto();
                    aluguel.setAluguel(a);
                    RetornaUsuario locador = usuarioRepository.findUsuario(id_locador).get(0);
                    aluguel.setLocador(locador);
                    RetornaUsuario locatario = usuarioRepository.findUsuario(id_locatario).get(0);
                    aluguel.setLocatario(locatario);
                    RetornaProduto produto = produtoRepository.findProduto("0", id_produto, 6, categoria, SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]).get(0);
                    aluguel.setProduto(produto);
                    alugueis.add(aluguel);
                }
                int start = (int) paging.getOffset();
                int end = (start + paging.getPageSize()) > alugueis.size() ? alugueis.size() : (start + paging.getPageSize());
                return new ResponseEntity<>(new PageImpl<>(alugueis.subList(start, end), paging, alugueis.size()), HttpStatus.OK);
            }else
                return new ResponseEntity<>(new PageImpl<>(new ArrayList<>(), PageRequest.of(1,1), 0), HttpStatus.OK);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return new ResponseEntity<>(new PageImpl<>(new ArrayList<>(), PageRequest.of(1,1), 0), HttpStatus.OK);
        }
        //return new ResponseEntity<>("Nenhum aluguel efetuado",HttpStatus.OK);
    }

    @ApiOperation(value = "Retorna unico aluguel pelo id_aluguel")
    @GetMapping("/aluguel")
    @ResponseStatus(HttpStatus.OK)
    public RetornaAluguel retornaAluguelAluguel(@RequestParam("id_aluguel") String id_aluguel) {
        try {
            return aluguelRepository.retornaAluguel("0", "0", id_aluguel, "0", 1, SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]).get(0);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    @ApiOperation(value = "Retorna alugueis do produto")
    @GetMapping("/produto")
    @ResponseStatus(HttpStatus.OK)
    public List<RetornaAluguel> retornaAluguelProduto(@RequestParam("id_produto") String id_produto) {
        try {
            return aluguelRepository.retornaAluguel("0", "0", "0", id_produto, 4, SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return new ArrayList<>();
        }
    }
    @ApiOperation(value = "Realiza a baixa do pagamento no sistema e envia os emails.")
    @GetMapping("/pagamento/efetua")
    @ResponseStatus(HttpStatus.OK)
    public Boolean salvaPagamento(@RequestParam("id_aluguel") String id_aluguel) {
        try {
            String usuario = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            aluguelRepository.alteraStatusAluguel(id_aluguel, 11, usuario);
            RetornoAlugueisNotificacao r = aluguelRepository.retornaDadosLocadorLocatario(id_aluguel,usuario);
            String locadorMail = new TemplateEmails().pagamentoAluguelDono(r.getLocadorNome(),r.getProdutoNome());
            String locatarioMail = new TemplateEmails().pagamentoAluguelLocatario(r.getLocatarioNome(),r.getProdutoNome());
            emailService.sendEmail(r.getLocadorEmail(),"Confirmação de pagamento", locadorMail);
            emailService.sendEmail(r.getLocatarioEmail(),"Confirmação de pagamento", locatarioMail);
            return true;
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @ApiOperation(value = "Salva URL Pagamento")
    @PutMapping("/confirma-aluguel")
    @ResponseStatus(HttpStatus.OK)
    public Boolean confirmaAluguel(@RequestParam("id_aluguel") String id_aluguel,@RequestParam("ok") Boolean ok) {
        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            aluguelRepository.alteraStatusAluguel(id_aluguel, (ok) ? 7 : 5, user);
            RetornaAluguelEncontro r = aluguelRepository.retornaAluguelEncontro(id_aluguel,user);
            RetornoAlugueisNotificacao dados = aluguelRepository.retornaDadosLocadorLocatario(id_aluguel,user);
            String mailLocatario = new TemplateEmails().informaAceiteLocalLocador(dados.getLocatarioNome(),dados.getLocadorNome(),dados.getProdutoNome(),r.getPeriodo(),r.getValor());
            emailService.sendEmail(dados.getLocatarioEmail(),"Confirmação do locador",mailLocatario);
            return true;
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @ApiOperation(value = "Confirma aluguel")
    @GetMapping("/pagamento/url-pagamento")
    @ResponseStatus(HttpStatus.OK)
    public Boolean salvaUrl(@Param(value = "id_aluguel") String id_aluguel,@Param(value = "url_pagamento") String url_pagamento) {
        try {
            return aluguelRepository.salvaUrlPagamento(id_aluguel, url_pagamento,SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @ApiOperation(value = "Retorna detalhes do aluguel")
    @GetMapping("/detalhe")
    @ResponseStatus(HttpStatus.OK)
    public Page<RetornaAluguelDetalhe> retornaAluguelProduto(@RequestParam("id_produto") String id_produto,
                                                             @RequestParam(value = "page",required = false,defaultValue = "0") int page,
                                                             @RequestParam(value = "size",required = false,defaultValue = "10") int size,
                                                             @RequestParam(value = "sort",required = false,defaultValue = "valor_ganho") String sortBy,
                                                             @RequestParam(value = "order",required = false,defaultValue = "desc") String order) {
        try {
            List<RetornaAluguelDetalhe> detAlugeis = aluguelRepository.retornaAluguelDetalhe(id_produto, SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]);
            Pageable paging = PageRequest.of(page, size, (order.equalsIgnoreCase("desc")) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
            int start = (int) paging.getOffset();
            int end = (start + paging.getPageSize()) > detAlugeis.size() ? detAlugeis.size() : (start + paging.getPageSize());
            return new PageImpl<>(detAlugeis.subList(start, end), paging, detAlugeis.size());//listPa;
            //return detAlugeis;
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return new PageImpl<>(new ArrayList<>(), PageRequest.of(1,1), 0);
        }
    }

    @ApiOperation(value = "Salva dados de entrega e devolução do produto")
    @PostMapping("/entrega-devolucao")
    @ResponseStatus(HttpStatus.OK)
    public Boolean inserirAluguelEncontro(@RequestBody AluguelEncontro aluguelEncontro) throws ParseException {
        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];

            RetornaAluguelEncontro r = aluguelRepository.retornaAluguelEncontro(aluguelEncontro.getId_aluguel(),user);
            RetornoAlugueisNotificacao dados = aluguelRepository.retornaDadosLocadorLocatario(aluguelEncontro.getId_aluguel(),user);
            System.out.println(dados.getLocadorNome());
            String locadorMail = new TemplateEmails().informaLocalLocatario(dados.getLocadorNome(),r.getLogradouro_entrega(),r.getBairro_entrega(),r.getCep_entrega(),
                    r.getDescricao_entrega(),r.getData_entrega(),r.getLogradouro_devolucao(),r.getBairro_devolucao(),r.getCep_devolucao(),r.getDescricao_devolucao(),r.getData_devolucao(),
                    dados.getLocatarioNome(),dados.getProdutoNome(), r.getPeriodo(),r.getValor());
            emailService.sendEmail(dados.getLocatarioEmail(),"Confirmação de encontro",locadorMail);

            aluguelRepository.alteraStatusAluguel(aluguelEncontro.getId_aluguel(), 9, user);

                return aluguelRepository.insereAluguelEncontro(aluguelEncontro.getId_aluguel(),
                        aluguelEncontro.getCep_entrega(),
                        aluguelEncontro.getLogradouro_entrega(),
                        aluguelEncontro.getBairro_entrega(),
                        aluguelEncontro.getDescricao_entrega(),
                        aluguelEncontro.getData_entrega(),
                        aluguelEncontro.getCep_devolucao(),
                        aluguelEncontro.getLogradouro_devolucao(),
                        aluguelEncontro.getBairro_devolucao(),
                        aluguelEncontro.getDescricao_devolucao(),
                        aluguelEncontro.getData_devolucao(),
                        aluguelEncontro.isAceite_locador(),
                        aluguelEncontro.getObservacao_recusa(),user);
            }

        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, (e.getMessage()==null)?"":e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @ApiOperation(value = "Retorna checklist de devolucao")
    @GetMapping("/checklist/retorna-devolucao")
    @ResponseStatus(HttpStatus.OK)
    RetornaChecklist retornaChecklistDevolucao(@Param("id_aluguel") String idAluguel){
        try{
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            return aluguelRepository.retornaCheckListDevolucao(idAluguel,user);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    @ApiOperation(value = "Salva checklist entrega")
    @PostMapping("/checklist/salva-entrega")
    @ResponseStatus(HttpStatus.OK)
    Boolean salvaChecklistEntrega(@RequestBody Checklist checklist, @RequestParam(value = "foto",required = false) Part foto){
        try{
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            byte[] bytes;

            aluguelRepository.alteraStatusAluguel(checklist.getId_aluguel(), 14, user);
            if(foto != null) {
                InputStream is = foto.getInputStream();
                bytes = new byte[(int) foto.getSize()];
                IOUtils.readFully(is, bytes);
                is.close();
                return aluguelRepository.gravaCheckListEntregaFoto(checklist.getId_aluguel(), checklist.getDescricao(), bytes, user);
            }
            else{
                return aluguelRepository.gravaCheckListEntrega(checklist.getId_aluguel(), checklist.getDescricao(), user);
            }
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @ApiOperation(value = "Salva checklist devolucao")
    @PostMapping("/checklist/salva-devolucao")
    @ResponseStatus(HttpStatus.OK)
    Boolean salvaChecklistDevolucao(@RequestBody Checklist checklist, @RequestParam(value = "foto",required = false) Part foto){
        try{
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            byte[] bytes;

            aluguelRepository.alteraStatusAluguel(checklist.getId_aluguel(), 15, user);
            if(foto != null) {
                InputStream is = foto.getInputStream();
                bytes = new byte[(int) foto.getSize()];
                IOUtils.readFully(is, bytes);
                is.close();
                return aluguelRepository.gravaCheckListDevolucaoFoto(checklist.getId_aluguel(), checklist.getDescricao(), bytes, user);
            }
            else {
                return aluguelRepository.gravaCheckListDevolucao(checklist.getId_aluguel(), checklist.getDescricao(), user);
            }
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, (e.getMessage()==null)?"":e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @ApiOperation(value = "retorna checklist de entrega")
    @GetMapping("/checklist/retorna-entrega")
    @ResponseStatus(HttpStatus.OK)
    RetornaChecklist retornaChecklistEntrega(@Param("id_aluguel") String idAluguel){
        try{
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            return aluguelRepository.retornaCheckListEntrega(idAluguel,user);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    @ApiOperation(value = "Aprova/Reprova checklist de entrega")
    @GetMapping("/checklist/aceite-entrega")
    @ResponseStatus(HttpStatus.OK)
    Boolean aceiteChecklistEntrega(@Param(value = "id_aluguel") String id_aluguel,@Param(value = "ok") Boolean ok, @Param(value = "motivoRecusa") String motivoRecusa){
        try{
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            aluguelRepository.alteraStatusAluguel(id_aluguel, 2, user);
            return aluguelRepository.aprovaReprovaCheckListEntrega(id_aluguel,ok,motivoRecusa,user);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @ApiOperation(value = "Aprova/Reprova checklist de devolucao")
    @GetMapping("/checklist/aceite-devolucao")
    @ResponseStatus(HttpStatus.OK)
    Boolean aceiteChecklistDevolucao(@Param(value = "id_aluguel") String id_aluguel,@Param(value = "ok") Boolean ok, @Param(value = "motivoRecusa") String motivoRecusa){
        try{
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            aluguelRepository.alteraStatusAluguel(id_aluguel, 3, user);
            return aluguelRepository.aprovaReprovaCheckListDevolucao(id_aluguel,ok,motivoRecusa,user);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @ApiOperation(value = "Retorna destalhes do encontro do aluguel")
    @GetMapping("/encontro")
    @ResponseStatus(HttpStatus.OK)
    RetornaAluguelEncontro retornaAluguelEncontro(@RequestParam("id_aluguel") String idAluguel){
        try{
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            return aluguelRepository.retornaAluguelEncontro(idAluguel,user);
        }catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    @ApiOperation(value = "Confirma encontro do aluguel")
    @GetMapping("/confirma-encontro")
    @ResponseStatus(HttpStatus.OK)
    Boolean retornaAluguelEncontro(@RequestParam("id_aluguel") String id_aluguel, @RequestParam("ok") Boolean ok, @RequestParam(value = "motivo", required = false, defaultValue = "") String motivo){
        try{
            if(!ok && motivo.isEmpty()){
                throw new NotFoundException("13");
            }
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            if(ok) {
                aluguelRepository.alteraStatusAluguel(id_aluguel, 7, user);
            }
            return aluguelRepository.confirmaEncontro(id_aluguel,ok,(ok) ? "" : motivo,user);
        }catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @ApiOperation(value = "Salva avaliação do produto")
    @PostMapping("/avaliacao/grava/produto")
    @ResponseStatus(HttpStatus.OK)
    Boolean salvaAvaliacaoProduto(@RequestBody Avalicao avalicao){
        try{
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            return aluguelRepository.salvaAvaliacao(avalicao.getId_aluguel(),avalicao.getComentario(),avalicao.getNota(),1,user);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @ApiOperation(value = "Salva avaliação do locatário(Locador para locatario)")
    @PostMapping("/avaliacao/grava/locatario")
    @ResponseStatus(HttpStatus.OK)
    Boolean salvaAvaliacaoLocatario(@RequestBody Avalicao avalicao){
        try{
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            return aluguelRepository.salvaAvaliacao(avalicao.getId_aluguel(),avalicao.getComentario(),avalicao.getNota(),2,user);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @ApiOperation(value = "Salva avaliação do locador(Locatario para locador)")
    @PostMapping("/avaliacao/grava/locador")
    @ResponseStatus(HttpStatus.OK)
    Boolean salvaAvaliacaoLocador(@RequestBody Avalicao avalicao){
        try{
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            return aluguelRepository.salvaAvaliacao(avalicao.getId_aluguel(),avalicao.getComentario(),avalicao.getNota(),3,user);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @ApiOperation(value = "Retorna avaliacoes produto")
    @GetMapping("/avaliacao/retorna/produto")
    @ResponseStatus(HttpStatus.OK)
    List<RetornaAvaliacoes> retornaAvaliacoesProduto(@RequestParam("id_produto") String id_produto){
        try{
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            return aluguelRepository.retornaAvaliacaoProduto(id_produto,user);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    @ApiOperation(value = "Retorna avaliacoes locatario")
    @GetMapping("/avaliacao/retorna/locatario")
    @ResponseStatus(HttpStatus.OK)
    List<RetornaAvaliacoes> retornaAvaliacoesLocatario(@RequestParam("id_usuario") String id_usuario){
        try{
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            return aluguelRepository.retornaAvaliacaoLocatario(id_usuario,user);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    @ApiOperation(value = "Retorna avaliacoes locador")
    @GetMapping("/avaliacao/retorna/locador")
    @ResponseStatus(HttpStatus.OK)
    List<RetornaAvaliacoes> retornaAvaliacoesLocador(@RequestParam("id_usuario") String id_usuario){
        try{
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            return aluguelRepository.retornaAvaliacaoLocador(id_usuario,user);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    public String getIdUsuario(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login;

        if(!auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken){
            throw new NullPointerException("Usuario não logado");
        }

        login = usuarioRepository.retornaIdUsuario(auth.getName().split("\\|")[0]);
        if (login.isEmpty() || login == null) {
            throw new NullPointerException("Usuario não encontrado");
        }
        return login;
    }
    //1x cada 15 minutos
    @Scheduled(cron = "0 */15 * * * ?")
    public void enviaNotificacaoAluguel(){
        try {
            System.out.println(LocalTime.now(ZoneId.of("America/Sao_Paulo")) + " - Iniciando verificação de notificacao de alugueis para entrega do produto");
            String usuario = "alugoMail";
            List<RetornoAlugueisNotificacao> alugueisNotificacao = aluguelRepository.retornaAlugueisNotificacaoInicio(usuario);
            System.out.println("Foram encontrados " + alugueisNotificacao.size() * 2 + " emails para enviar.");
            int cont = 0;
            for (RetornoAlugueisNotificacao ret : alugueisNotificacao) {
                String bodyMailLocador = new TemplateEmails().notificaAluguelLocadorInicio(ret.getLocadorNome(), ret.getProdutoNome(), ret.getLocatarioNome());
                String bodyMailLocatario = new TemplateEmails().notificaAluguelLocatarioInicio(ret.getLocatarioNome(), ret.getProdutoNome(), ret.getLocadorNome());
                //System.out.println("Enviando email para locador " + ret.getLocadorEmail());
                emailService.sendEmail(ret.getLocadorEmail(), "Notificação de aluguel", bodyMailLocador);
                //System.out.println("Enviando email para locatario " + ret.getLocatarioEmail());
                emailService.sendEmail(ret.getLocatarioEmail(), "Notificação de aluguel", bodyMailLocatario);
                aluguelRepository.alteraStatusAluguel(ret.getIdAluguel(),12,usuario); //Aguardando entrega
                cont++;
            }
            System.out.println(LocalTime.now(ZoneId.of("America/Sao_Paulo")) + " - Foram enviados " + cont * 2 + " emails para entrega de produto.");

            System.out.println(LocalTime.now(ZoneId.of("America/Sao_Paulo")) + " - Iniciando verificação de notificacao de alugueis para devolução do produto");

            alugueisNotificacao = aluguelRepository.retornaAlugueisNotificacaoFim(usuario);
            System.out.println("Foram encontrados " + alugueisNotificacao.size() * 2 + " emails para enviar.");
            cont = 0;
            for (RetornoAlugueisNotificacao ret : alugueisNotificacao) {
                String bodyMailLocador = new TemplateEmails().notificaAluguelLocadorFim(ret.getLocadorNome(), ret.getProdutoNome(), ret.getLocatarioNome());
                String bodyMailLocatario = new TemplateEmails().notificaAluguelLocatarioFim(ret.getLocatarioNome(), ret.getProdutoNome(), ret.getLocadorNome());
                //System.out.println("Enviando email para locador " + ret.getLocadorEmail());
                emailService.sendEmail(ret.getLocadorEmail(), "Notificação de aluguel", bodyMailLocador);
                //System.out.println("Enviando email para locatario " + ret.getLocatarioEmail());
                emailService.sendEmail(ret.getLocatarioEmail(), "Notificação de aluguel", bodyMailLocatario);
                aluguelRepository.alteraStatusAluguel(ret.getIdAluguel(),10,usuario); //Aguardando devolução
                cont++;
            }
            System.out.println(LocalTime.now(ZoneId.of("America/Sao_Paulo")) + " - Foram enviados " + cont * 2 + " emails para devolução de produto.");
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = "alugoMail";
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
        }
    }
}
