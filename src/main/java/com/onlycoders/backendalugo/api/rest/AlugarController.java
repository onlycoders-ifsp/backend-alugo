package com.onlycoders.backendalugo.api.rest;

import com.google.common.base.Throwables;
import com.onlycoders.backendalugo.model.entity.aluguel.template.RetornaAluguelUsuarioProduto;
import com.onlycoders.backendalugo.model.entity.aluguel.Aluguel;
import com.onlycoders.backendalugo.model.entity.aluguel.template.RetornaAluguel;
import com.onlycoders.backendalugo.model.entity.aluguel.template.RetornaAluguelDetalhe;
import com.onlycoders.backendalugo.model.entity.email.RetornoAlugueisNotificacao;
import com.onlycoders.backendalugo.model.entity.email.TemplateEmails;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
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
    public Boolean EfetuaAluguel(@RequestBody Aluguel aluguel) throws ParseException {
        try {
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
                return aluguelRepository.efetuaAluguel(getIdUsuario(), aluguel.getId_produto(),
                        aluguel.getData_inicio(), aluguel.getData_fim(), aluguel.getValor_aluguel(), SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]);
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
            //List<AluguelDetalhe> detalhe = new ArrayList<>();
            //for(RetornaAluguelDetalhe r : detAlugeis){
            //    detalhe.add(new AluguelDetalhe(r.getId_produto(),r.getNome_produto(),r.getId_locatario(),
            //            r.getNome_locatario(),r.getCapa_foto(),r.getData_inicio(),r.getData_fim(),
            //            r.getValor_aluguel(),r.getValor_ganho(),r.getData_devolucao()));
            //}
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
            System.out.println(LocalTime.now() + " - Iniciando verificação de notificacao de alugueis");
            String usuario = "alugoMail";
            List<RetornoAlugueisNotificacao> alugueisNotificacao = aluguelRepository.retornaAlugueisNotificacao(usuario);
            System.out.println("Foram encontrados " + alugueisNotificacao.size() * 2 + " emails para enviar.");
            int cont = 0;
            for (RetornoAlugueisNotificacao ret : alugueisNotificacao) {
                String bodyMailLocador = new TemplateEmails().notificaAluguelLocador(ret.getLocadorNome(), ret.getProdutoNome(), ret.getLocatarioNome());
                String bodyMailLocatario = new TemplateEmails().notificaAluguelLocatario(ret.getLocatarioNome(), ret.getProdutoNome(), ret.getLocadorNome());
                //System.out.println("Enviando email para locador " + ret.getLocadorEmail());
                emailService.sendEmail(ret.getLocadorEmail(), "Notificação de aluguel", bodyMailLocador);
                //System.out.println("Enviando email para locatario " + ret.getLocatarioEmail());
                emailService.sendEmail(ret.getLocatarioEmail(), "Notificação de aluguel", bodyMailLocatario);
                cont++;
            }
            System.out.println(LocalTime.now() + " - Foram enviados " + cont * 2 + " emails.");
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


    @ApiOperation(value = "Salva dados de entrega e devolução do produto")
    @PostMapping("/entrega-devolucao")
    @ResponseStatus(HttpStatus.OK)
    public Boolean inserirAluguelEncontro(@RequestBody Aluguel aluguel) throws ParseException {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));

            return aluguelRepository.efetuaAluguel(getIdUsuario(), aluguel.getId_produto(),
                    aluguel.getData_inicio(), aluguel.getData_fim(), aluguel.getValor_aluguel(), SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]);
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

}
