package com.onlycoders.backendalugo.api.rest;

import com.google.common.base.Throwables;
import com.onlycoders.backendalugo.model.entity.aluguel.template.AluguelEncontro;
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
import com.onlycoders.backendalugo.model.entity.aluguel.StatusInterfaceEnum.StatusAluguel;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.servlet.http.Part;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@RestController
@Api(value = "Aluguel")
@RequestMapping("/aluguel")
@CrossOrigin(origins = "*")
public class AlugarController {

    private final UsuarioRepository usuarioRepository;
    private final PagamentoController pagamentoController;
    private final ProdutoRepository produtoRepository;
    private final AluguelRepository aluguelRepository;
    private final LogRepository logRepository;
    private final EmailService emailService;
    private final UsuarioController usuarioController;

    @Autowired
    public AlugarController(UsuarioRepository usuarioRepository, PagamentoController pagamentoController, ProdutoRepository produtoRepository, AluguelRepository aluguelRepository, LogRepository logRepository, EmailService emailService, UsuarioController usuarioController) {
        this.usuarioRepository = usuarioRepository;
        this.pagamentoController = pagamentoController;
        this.produtoRepository = produtoRepository;
        this.aluguelRepository = aluguelRepository;
        this.logRepository = logRepository;
        this.emailService = emailService;
        this.usuarioController = usuarioController;
    }

    @ApiOperation(value = "Efetua aluguel do usuario logado. Param id_produto")
    @PostMapping("/aluguel-efetua")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public String EfetuaAluguel(@RequestBody Aluguel aluguel){
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

            String valida = aluguelRepository.validaALuguel(usuarioController.getIdUsuario(), aluguel.getId_produto(), aluguel.getData_inicio(), aluguel.getData_fim(), SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]);

            if (!valida.equals("0")) {
                return valida;
            }
            String idAluguel = aluguelRepository.efetuaAluguel(usuarioController.getIdUsuario(), aluguel.getId_produto(),
                    aluguel.getData_inicio(), aluguel.getData_fim(), aluguel.getValor_aluguel(), SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]);
            if(idAluguel.isEmpty())
                return null;
            RetornaAluguelEncontro r = aluguelRepository.retornaAluguelEncontro(idAluguel.replace("\"",""),user);
            RetornoAlugueisNotificacao dados = aluguelRepository.retornaDadosLocadorLocatario(idAluguel.replace("\"",""),user);
            String locadorMail = new TemplateEmails().notificaLocadorAluguel(dados.getLocadorNome(),dados.getLocatarioNome(),dados.getProdutoNome(),r.getPeriodo(),r.getValor());
            String locatarioMail = new TemplateEmails().notificaLocatarioAluguel(dados.getLocatarioNome(),dados.getLocadorNome(),dados.getProdutoNome(),r.getPeriodo(),r.getValor());
            emailService.sendEmail(dados.getLocadorEmail(),"Solicitação de aluguel",locadorMail);
            emailService.sendEmail(dados.getLocatarioEmail(),"Solicitação de aluguel",locatarioMail);
            return idAluguel;
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

    @ApiOperation(value = "Retorna todos alugueis do usuario logado como locador")
    @GetMapping("/locador")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ResponseEntity<?>// Page<RetornaAluguelUsuarioProduto>
    retornaAluguelLocadorLogado(@RequestParam(value = "page",
                                              required = false,
                                              defaultValue = "0") int page,
                                @RequestParam(value = "size",
                                              required = false,
                                              defaultValue = "10") int size,
                                @RequestParam(value = "categoria",
                                              required = false,
                                              defaultValue = "0") int categoria){
        try {
            Pageable paging = PageRequest.of(page, size);

            String id_locador = usuarioController.getIdUsuario();
            String id_locatario;
            String id_produto;

            List<RetornaAluguelUsuarioProduto> alugueis = new ArrayList<>();

            Optional<List<RetornaAluguel>> aluguelEfeutuado = Optional.ofNullable(aluguelRepository
                    .retornaAluguel(id_locador, "0", "0", "0", 2, SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]));
            if (aluguelEfeutuado.isPresent() && !aluguelEfeutuado.get().isEmpty()) {
                //TODO retirar este for
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
                int end = Math.min((start + paging.getPageSize()), alugueis.size());
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
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ResponseEntity<?>//Page<RetornaAluguelUsuarioProduto>
    retornaAluguelLocatarioLogado(@RequestParam(value = "page",
                                                required = false,
                                                defaultValue = "0") int page,
                                  @RequestParam(value = "size",
                                                required = false,
                                                defaultValue = "10") int size,
                                  @RequestParam(value = "categoria",
                                                required = false,
                                                defaultValue = "0") int categoria){

        try {
            Pageable paging = PageRequest.of(page, size);
            String id_locatario = usuarioController.getIdUsuario();
            String id_produto;
            String id_locador;

            List<RetornaAluguelUsuarioProduto> alugueis = new ArrayList<>();

            Optional<List<RetornaAluguel>> aluguelEfeutuado = Optional.ofNullable(aluguelRepository
                    .retornaAluguel("0", id_locatario, "0", "0", 3, SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]));
            if (aluguelEfeutuado.isPresent() && !aluguelEfeutuado.get().isEmpty()) {
                //TODO retirar este for
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
                int end = Math.min((start + paging.getPageSize()), alugueis.size());
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
    @Transactional(isolation = Isolation.READ_COMMITTED)
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
    @Transactional(isolation = Isolation.READ_COMMITTED)
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

    @ApiOperation(value = "Confirma aluguel(Dono)")
    @GetMapping("/confirma-aluguel")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Boolean confirmaAluguel(@RequestParam("id_aluguel") String id_aluguel,@RequestParam("ok") Boolean ok,@RequestParam(value = "motivoRecusa",required = false,defaultValue = "")String motivoRecusa) {
        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            if (!ok){
                RetornoAlugueisNotificacao dados = aluguelRepository.retornaDadosLocadorLocatario(id_aluguel,user);
                String mailLocatario = new TemplateEmails().locatarioAluguelRecusado(dados.getLocatarioNome(), dados.getProdutoNome(), motivoRecusa);
                emailService.sendEmail(dados.getLocatarioEmail(),"Confirmação de aluguel", mailLocatario);
                aluguelRepository.alteraStatusAluguel(id_aluguel, StatusAluguel.CANCELADO_PELO_LOCADOR.getCod_status(), user);
                return false;
            }
            else {
                RetornaAluguelEncontro r = aluguelRepository.retornaAluguelEncontro(id_aluguel, user);
                RetornoAlugueisNotificacao dados = aluguelRepository.retornaDadosLocadorLocatario(id_aluguel, user);
                String mailLocatario = new TemplateEmails().informaAceiteLocalLocador(dados.getLocatarioNome(), dados.getLocadorNome(), dados.getProdutoNome(), r.getPeriodo(), r.getValor());
                emailService.sendEmail(dados.getLocatarioEmail(), "Confirmação de aluguel", mailLocatario);
                aluguelRepository.alteraStatusAluguel(id_aluguel, StatusAluguel.AGUARDANDO_PAGAMENTO.getCod_status(), user);
                return true;
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

    @ApiOperation(value = "Retorna detalhes do aluguel")
    @GetMapping("/detalhe")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Page<RetornaAluguelDetalhe> retornaAluguelProduto(@RequestParam("id_produto") String id_produto,
                                                             @RequestParam(value = "page",required = false,defaultValue = "0") int page,
                                                             @RequestParam(value = "size",required = false,defaultValue = "10") int size,
                                                             @RequestParam(value = "sort",required = false,defaultValue = "valor_ganho") String sortBy,
                                                             @RequestParam(value = "order",required = false,defaultValue = "desc") String order) {
        try {
            List<RetornaAluguelDetalhe> detAlugeis = aluguelRepository.retornaAluguelDetalhe(id_produto, SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]);
            Pageable paging = PageRequest.of(page, size, (order.equalsIgnoreCase("desc")) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
            int start = (int) paging.getOffset();
            int end = Math.min((start + paging.getPageSize()), detAlugeis.size());
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
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Boolean inserirAluguelEncontro(@RequestBody AluguelEncontro aluguelEncontro){
        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];

            Boolean ok = aluguelRepository.alteraStatusAluguel(aluguelEncontro.getId_aluguel(), StatusAluguel.AGUARDANDO_ACEITE_DO_DONO.getCod_status(), user);

            if (!ok){
                System.out.println("deu ruim no status");
            }

            ok  = aluguelRepository.insereAluguelEncontro(aluguelEncontro.getId_aluguel(),
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

            if (ok){
                RetornaAluguelEncontro r = aluguelRepository.retornaAluguelEncontro(aluguelEncontro.getId_aluguel(),user);
                RetornoAlugueisNotificacao dados = aluguelRepository.retornaDadosLocadorLocatario(aluguelEncontro.getId_aluguel(),user);
                String locadorMail = new TemplateEmails().informaLocalLocatario(dados.getLocadorNome(),r.getLogradouro_entrega(),r.getBairro_entrega(),r.getCep_entrega(),
                        r.getDescricao_entrega(),r.getData_entrega(),r.getLogradouro_devolucao(),r.getBairro_devolucao(),r.getCep_devolucao(),r.getDescricao_devolucao(),r.getData_devolucao(),
                        dados.getLocatarioNome(),dados.getProdutoNome(), r.getPeriodo(),r.getValor());
                emailService.sendEmail(dados.getLocadorEmail(),"Confirmação de encontro",locadorMail);
                return true;
            }
            else
                return false;
        }

        catch(Exception e) {
            System.out.println("erro");
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
    @Transactional(isolation = Isolation.READ_COMMITTED)
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
    @Transactional(isolation = Isolation.READ_COMMITTED)
    Boolean salvaChecklistEntrega(@RequestBody Checklist checklist){
        try{
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            if (aluguelRepository.gravaCheckListEntrega(checklist.getId_aluguel(), checklist.getDescricao(), user)){
                RetornoAlugueisNotificacao dados = aluguelRepository.retornaDadosLocadorLocatario(checklist.getId_aluguel(),user);
                aluguelRepository.alteraStatusAluguel(checklist.getId_aluguel(), StatusAluguel.AGUARDANDO_OK_DO_LOCATARIO_CHECLILIST_ENTREGA.getCod_status(), user);
                String locadorMail = new TemplateEmails().notificaChkEntregaLocatario(dados.getLocatarioNome());
                emailService.sendEmail(dados.getLocatarioEmail(),"Confirme o checklist de entrega",locadorMail);
                return true;
            }
            return false;
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

    @ApiOperation(value = "Salva foto checklist entrega")
    @PutMapping("/checklist/salva-foto-entrega")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    Boolean salvaFotoChecklistEntrega(@RequestParam String id_aluguel, @RequestParam Part foto) {
        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            byte[] bytes;
            InputStream is = foto.getInputStream();
            bytes = new byte[(int) foto.getSize()];
            IOUtils.readFully(is, bytes);
            is.close();
            return aluguelRepository.gravaFotoCheckListEntrega(id_aluguel, bytes, user);
        } catch (Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, (e.getMessage() == null) ? "" : e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @ApiOperation(value = "Salva checklist devolucao")
    @PostMapping("/checklist/salva-devolucao")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    Boolean salvaChecklistDevolucao(@RequestBody Checklist checklist) {

        try{
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            if (aluguelRepository.gravaCheckListDevolucao(checklist.getId_aluguel(), checklist.getDescricao(), user)) {
                RetornoAlugueisNotificacao dados = aluguelRepository.retornaDadosLocadorLocatario(checklist.getId_aluguel(), user);
                aluguelRepository.alteraStatusAluguel(checklist.getId_aluguel(), StatusAluguel.AGUARDANDO_OK_LOCATARIO_CHECKLIST_DEVOLUCAO.getCod_status(), user);
                String locadorMail = new TemplateEmails().notificaChkDevolucaoLocatario(dados.getLocatarioNome());
                emailService.sendEmail(dados.getLocatarioEmail(), "Confirme o checklist de devolução", locadorMail);
                return true;
            }
            return false;
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

    @ApiOperation(value = "Salva foto checklist devolucao")
    @PutMapping("/checklist/salva-foto-devolucao")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    Boolean salvaFotoChecklistDevolucao(@RequestParam String id_aluguel, @RequestParam Part foto) {
        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            byte[] bytes;
            InputStream is = foto.getInputStream();
            bytes = new byte[(int) foto.getSize()];
            IOUtils.readFully(is, bytes);
            is.close();
            return aluguelRepository.gravaFotoCheckListDevolucao(id_aluguel, bytes, user);
        } catch (Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, (e.getMessage() == null) ? "" : e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @ApiOperation(value = "retorna checklist de entrega")
    @GetMapping("/checklist/retorna-entrega")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
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
    @Transactional(isolation = Isolation.READ_COMMITTED)
    Boolean aceiteChecklistEntrega(@Param(value = "id_aluguel") String id_aluguel,@Param(value = "ok") Boolean ok, @Param(value = "motivoRecusa") String motivoRecusa){
        try{
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            if(aluguelRepository.aprovaReprovaCheckListEntrega(id_aluguel,ok,motivoRecusa,user)){
                RetornoAlugueisNotificacao dados = aluguelRepository.retornaDadosLocadorLocatario(id_aluguel,user);
                String locadorMail;
                if(!ok){
                    locadorMail = new TemplateEmails().DonoRecusaChecklistEntrega(dados.getLocadorNome(), dados.getProdutoNome(), motivoRecusa);
                    emailService.sendEmail(dados.getLocadorEmail(),"Confirmação de checklist de entrega", locadorMail);
                    aluguelRepository.alteraStatusAluguel(id_aluguel, StatusAluguel.AGUARDANDO_ENTREGA_DO_PRODUTO.getCod_status(), user);
                }
                else{
                    locadorMail = new TemplateEmails().notificaChkEntregaLocador(dados.getLocadorNome());
                    emailService.sendEmail(dados.getLocadorEmail(),"Confirmação de checklist de entrega", locadorMail);
                    aluguelRepository.alteraStatusAluguel(id_aluguel, StatusAluguel.EM_ANDAMENTO.getCod_status(), user);
                }
                return true;
            }
            else
                return false;
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
    @Transactional(isolation = Isolation.READ_COMMITTED)
    Boolean aceiteChecklistDevolucao(@Param(value = "id_aluguel") String id_aluguel,@Param(value = "ok") Boolean ok, @Param(value = "motivoRecusa") String motivoRecusa){
        try{
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            if (aluguelRepository.aprovaReprovaCheckListDevolucao(id_aluguel, ok, motivoRecusa, user)) {
                RetornoAlugueisNotificacao dados = aluguelRepository.retornaDadosLocadorLocatario(id_aluguel, user);
                if(!ok){
                    String locadorMail = new TemplateEmails().DonoRecusaChcklistDevolucao(dados.getLocadorNome(), dados.getProdutoNome(), motivoRecusa);
                    emailService.sendEmail(dados.getLocadorEmail(), "Confirmação de checklist de devolução", locadorMail);
                    aluguelRepository.alteraStatusAluguel(id_aluguel, StatusAluguel.AGUARDANDO_DEVOLUCAO_DO_PRODUTO.getCod_status(), user);
                } else{
                    String locadorMail = new TemplateEmails().notificaChkDevolucaoLocador(dados.getLocadorNome());
                    String locadorMailAvaliacao = new TemplateEmails().notificaAvalicaoLocador(dados.getLocadorNome());
                    String locatarioMailAvaliacao = new TemplateEmails().notificaAvaliacaoLocatario(dados.getLocatarioNome());
                    emailService.sendEmail(dados.getLocadorEmail(), "Confirmação de checklist de devolução", locadorMail);
                    emailService.sendEmail(dados.getLocatarioEmail(), "Avalie sua experiencia", locatarioMailAvaliacao);
                    emailService.sendEmail(dados.getLocadorEmail(), "Avalie sua experiencia", locadorMailAvaliacao);
                    aluguelRepository.alteraStatusAluguel(id_aluguel, StatusAluguel.FINALIZADO_SEM_OCORRRENCIA.getCod_status(), user);
                }
                return true;
            }
            else
                return false;
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
    @Transactional(isolation = Isolation.READ_COMMITTED)
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
    @Transactional(isolation = Isolation.READ_COMMITTED)
    Boolean confirmaAluguelEncontro(@RequestParam("id_aluguel") String id_aluguel, @RequestParam("ok") Boolean ok, @RequestParam(value = "motivo", required = false, defaultValue = "") String motivo){
        try{
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            RetornoAlugueisNotificacao dados = aluguelRepository.retornaDadosLocadorLocatario(id_aluguel,user);
            RetornaAluguelEncontro r = aluguelRepository.retornaAluguelEncontro(id_aluguel, user);
            if(aluguelRepository.confirmaEncontro(id_aluguel,ok,(ok) ? "" : motivo,user)) {
                if (!ok) {
                    if (motivo.isEmpty()) {
                        throw new NotFoundException("13");
                    }
                    String locatarioMail = new TemplateEmails().locatarioLocalRecusado(dados.getLocatarioNome(), dados.getLocadorNome(), dados.getProdutoNome(), r.getPeriodo(), r.getValor(),motivo);
                    emailService.sendEmail(dados.getLocatarioEmail(), "Local de entrega", locatarioMail);
                    aluguelRepository.alteraStatusAluguel(id_aluguel, StatusAluguel.PAGAMENTO_CONFIRMADO.getCod_status(), user);
                } else {
                    String locatarioMail = new TemplateEmails().notificaLocatarioConfirmaLocal(dados.getLocatarioNome(), dados.getLocadorNome(), dados.getProdutoNome(), r.getPeriodo(), r.getValor());
                    emailService.sendEmail(dados.getLocatarioEmail(), "Local de entrega", locatarioMail);
                    aluguelRepository.alteraStatusAluguel(id_aluguel, StatusAluguel.AGENDADO.getCod_status(), user);
                }
                return true;
            }
            else
                return false;
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
    @Transactional(isolation = Isolation.READ_COMMITTED)
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
    @Transactional(isolation = Isolation.READ_COMMITTED)
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
    @Transactional(isolation = Isolation.READ_COMMITTED)
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
    @Transactional(isolation = Isolation.READ_COMMITTED)
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
    @Transactional(isolation = Isolation.READ_COMMITTED)
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
    @Transactional(isolation = Isolation.READ_COMMITTED)
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

    @ApiOperation(value = "Envia notificacações manualmente")
    @GetMapping("/notificacoes")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public String enviaNotificacaoAluguelManual() {
        try {
            System.out.println(LocalTime.now(ZoneId.of("America/Sao_Paulo")) + " - Iniciando verificação de notificacao de alugueis para entrega do produto");
            String usuario = "alugoMail";
            List<RetornoAlugueisNotificacao> alugueisNotificacao = aluguelRepository.retornaAlugueisNotificacaoInicio(2,usuario);
            String log = "Foram encontrados " + alugueisNotificacao.size() * 2 + " emails para enviar.<br>";
            System.out.println("Foram encontrados " + alugueisNotificacao.size() * 2 + " emails para enviar.");
            int cont = 0;
            for (RetornoAlugueisNotificacao ret : alugueisNotificacao) {
                RetornaAluguelEncontro encontro =  aluguelRepository.retornaAluguelEncontro(ret.getIdAluguel(),usuario);
                String bodyMailLocador = new TemplateEmails().notificaAluguelLocadorInicio(ret.getLocadorNome(), encontro.getLogradouro_entrega(),encontro.getBairro_entrega(),encontro.getCep_entrega(),encontro.getDescricao_entrega(), encontro.getData_entrega());
                String bodyMailLocatario = new TemplateEmails().notificaAluguelLocatarioInicio(ret.getLocatarioNome(), encontro.getLogradouro_entrega(),encontro.getBairro_entrega(),encontro.getCep_entrega(),encontro.getDescricao_entrega(), encontro.getData_entrega());
                //System.out.println("Enviando email para locador " + ret.getLocadorEmail());
                emailService.sendEmail(ret.getLocadorEmail(), "Notificação de aluguel", bodyMailLocador);
                //System.out.println("Enviando email para locatario " + ret.getLocatarioEmail());
                emailService.sendEmail(ret.getLocatarioEmail(), "Notificação de aluguel", bodyMailLocatario);
                aluguelRepository.alteraStatusAluguel(ret.getIdAluguel(),StatusAluguel.AGUARDANDO_ENTREGA_DO_PRODUTO.getCod_status(), usuario); //Aguardando entrega
                cont++;
            }
            System.out.println(LocalTime.now(ZoneId.of("America/Sao_Paulo")) + " - Foram enviados " + cont * 2 + " emails para entrega de produto.");
            log = log.concat(LocalTime.now(ZoneId.of("America/Sao_Paulo")) + " - Foram enviados " + cont * 2 + " emails para entrega de produto.<br><br>");
            System.out.println(LocalTime.now(ZoneId.of("America/Sao_Paulo")) + " - Iniciando verificação de notificacao de alugueis para devolução do produto");

            alugueisNotificacao = aluguelRepository.retornaAlugueisNotificacaoFim(2,usuario);
            System.out.println("Foram encontrados " + alugueisNotificacao.size() * 2 + " emails para enviar.");
            log = log.concat("Foram encontrados " + alugueisNotificacao.size() * 2 + " emails para enviar.<br>");
            cont = 0;
            for (RetornoAlugueisNotificacao ret : alugueisNotificacao) {
                RetornaAluguelEncontro encontro =  aluguelRepository.retornaAluguelEncontro(ret.getIdAluguel(),usuario);
                String bodyMailLocador = new TemplateEmails().notificaAluguelLocadorFim(ret.getLocadorNome(), encontro.getLogradouro_devolucao(),encontro.getBairro_devolucao(),encontro.getCep_devolucao(),encontro.getDescricao_devolucao(), encontro.getData_devolucao());
                String bodyMailLocatario = new TemplateEmails().notificaAluguelLocatarioFim(ret.getLocatarioNome(), encontro.getLogradouro_devolucao(),encontro.getBairro_devolucao(),encontro.getCep_devolucao(),encontro.getDescricao_devolucao(), encontro.getData_devolucao());
                //System.out.println("Enviando email para locador " + ret.getLocadorEmail());
                emailService.sendEmail(ret.getLocadorEmail(), "Notificação de aluguel", bodyMailLocador);
                //System.out.println("Enviando email para locatario " + ret.getLocatarioEmail());
                emailService.sendEmail(ret.getLocatarioEmail(), "Notificação de aluguel", bodyMailLocatario);
                aluguelRepository.alteraStatusAluguel(ret.getIdAluguel(),StatusAluguel.AGUARDANDO_DEVOLUCAO_DO_PRODUTO.getCod_status(), usuario); //Aguardando devolução
                cont++;
            }
            System.out.println(LocalTime.now(ZoneId.of("America/Sao_Paulo")) + " - Foram enviados " + cont * 2 + " emails para devolução de produto.");
            log = log.concat(LocalTime.now(ZoneId.of("America/Sao_Paulo")) + " - Foram enviados " + cont * 2 + " emails para devolução de produto.<br>");
            return log;
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = "alugoMail";
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return "Erro ao enviar notificações";
        }
    }

    @ApiOperation(value = "Retorna extrato de locador")
    @GetMapping("/extrato-locador")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Page<ExtratoLocadorDetalhe> retornaExtratoLocador(@RequestParam(value = "page",required = false,defaultValue = "0") int page,
                                                             @RequestParam(value = "size",required = false,defaultValue = "10") int size,
                                                             @RequestParam(value = "sort",required = false,defaultValue = "valor_ganho") String sortBy,
                                                             @RequestParam(value = "order",required = false,defaultValue = "desc") String order){
        try{
            String usuario = usuarioController.getIdUsuario();
            List<ExtratoLocadorDetalhe> detalhe = aluguelRepository.retornaExtratoLocador(usuario,usuario);
            //Double saldo = aluguelRepository.retornaExtratoLocadorSaldo(usuario,usuario);
            //ExtratoLocador extratoLocador = new ExtratoLocador(saldo,detalhe);
            Pageable paging = PageRequest.of(page, size, (order.equalsIgnoreCase("desc")) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
            int start = (int) paging.getOffset();
            int end = Math.min((start + paging.getPageSize()), detalhe.size());
            return new PageImpl<>(detalhe.subList(start, end), paging, detalhe.size());//listPa;
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = usuarioController.getIdUsuario();
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    //TODO
    void salvaExtensaoAluguel(){

    }
    //TODO
    void retornaExtensaoAluguel(){

    }
    //TODO
    void aceitaRejeitaExtensao(){

    }
    //TODO
    void cancelaAluguel(){

    }
    //TODO
    void retornaChatAluguel(){

    }
    //TODO
    void salvaChatAluguel(){

    }


    //1x cada 5 minutos
    @Scheduled(cron = "0 */05 * * * ?")
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void enviaNotificacaoAluguel(){
        try {
            System.out.println(LocalTime.now(ZoneId.of("America/Sao_Paulo")) + " - Iniciando verificação de notificacao de alugueis para entrega do produto");
            String usuario = "alugoMail";
            List<RetornoAlugueisNotificacao> alugueisNotificacao = aluguelRepository.retornaAlugueisNotificacaoInicio(1,usuario);
            System.out.println(LocalTime.now(ZoneId.of("America/Sao_Paulo")) + " - Foram encontrados " + alugueisNotificacao.size() * 2 + " emails para enviar.");
            int cont = 0;
            for (RetornoAlugueisNotificacao ret : alugueisNotificacao) {
                RetornaAluguelEncontro encontro =  aluguelRepository.retornaAluguelEncontro(ret.getIdAluguel(),usuario);
                String bodyMailLocador = new TemplateEmails().notificaAluguelLocadorInicio(ret.getLocadorNome(), encontro.getLogradouro_entrega(),encontro.getBairro_entrega(),encontro.getCep_entrega(),encontro.getDescricao_entrega(), encontro.getData_entrega());
                String bodyMailLocatario = new TemplateEmails().notificaAluguelLocatarioInicio(ret.getLocatarioNome(), encontro.getLogradouro_entrega(),encontro.getBairro_entrega(),encontro.getCep_entrega(),encontro.getDescricao_entrega(), encontro.getData_entrega());
                //System.out.println("Enviando email para locador " + ret.getLocadorEmail());
                emailService.sendEmail(ret.getLocadorEmail(), "Notificação de aluguel", bodyMailLocador);
                //System.out.println("Enviando email para locatario " + ret.getLocatarioEmail());
                emailService.sendEmail(ret.getLocatarioEmail(), "Notificação de aluguel", bodyMailLocatario);
                aluguelRepository.alteraStatusAluguel(ret.getIdAluguel(),StatusAluguel.AGUARDANDO_ENTREGA_DO_PRODUTO.getCod_status(), usuario); //Aguardando entrega
                cont++;
            }
            System.out.println(LocalTime.now(ZoneId.of("America/Sao_Paulo")) + " - Foram enviados " + cont * 2 + " emails para entrega de produto.");

            System.out.println(LocalTime.now(ZoneId.of("America/Sao_Paulo")) + " - Iniciando verificação de notificacao de alugueis para devolução do produto");

            alugueisNotificacao = aluguelRepository.retornaAlugueisNotificacaoFim(1,usuario);
            System.out.println(LocalTime.now(ZoneId.of("America/Sao_Paulo")) + "Foram encontrados " + alugueisNotificacao.size() * 2 + " emails para enviar.");
            cont = 0;
            for (RetornoAlugueisNotificacao ret : alugueisNotificacao) {
                RetornaAluguelEncontro encontro =  aluguelRepository.retornaAluguelEncontro(ret.getIdAluguel(),usuario);
                String bodyMailLocador = new TemplateEmails().notificaAluguelLocadorFim(ret.getLocadorNome(), encontro.getLogradouro_devolucao(),encontro.getBairro_devolucao(),encontro.getCep_devolucao(),encontro.getDescricao_devolucao(), encontro.getData_devolucao());
                String bodyMailLocatario = new TemplateEmails().notificaAluguelLocatarioFim(ret.getLocatarioNome(), encontro.getLogradouro_devolucao(),encontro.getBairro_devolucao(),encontro.getCep_devolucao(),encontro.getDescricao_devolucao(), encontro.getData_devolucao());
                //System.out.println("Enviando email para locador " + ret.getLocadorEmail());
                emailService.sendEmail(ret.getLocadorEmail(), "Notificação de aluguel", bodyMailLocador);
                //System.out.println("Enviando email para locatario " + ret.getLocatarioEmail());
                emailService.sendEmail(ret.getLocatarioEmail(), "Notificação de aluguel", bodyMailLocatario);
                aluguelRepository.alteraStatusAluguel(ret.getIdAluguel(),StatusAluguel.AGUARDANDO_DEVOLUCAO_DO_PRODUTO.getCod_status(), usuario); //Aguardando devolução
                cont++;
            }
            System.out.println(LocalTime.now(ZoneId.of("America/Sao_Paulo")) + " - Foram enviados " + cont * 2 + " emails para devolução de produto.");
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = "";
            String user = "alugoMail";
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
        }
    }

    @Scheduled(cron = "0 */05 * * * ?")
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void efetuaEstornoAluguel(){
        try{
            System.out.println(LocalTime.now(ZoneId.of("America/Sao_Paulo")) + " - Iniciando verificação de alugueis para estornar");
            List<RetornoAlugueisEstorno> l = aluguelRepository.retornaPagameentosEstorno("");
            System.out.println(LocalTime.now(ZoneId.of("America/Sao_Paulo")) + " - Encontrados " + l.size() + " alugueis para estornar");
            String locatarioMail = "";
            String locadorMail = "";
            for(RetornoAlugueisEstorno ret : l){
                if(pagamentoController.estornoPagmenento(ret.getId_pagamento_mp(),ret.getValor(),ret.getRetencao())){
                    RetornoAlugueisNotificacao dados = aluguelRepository.retornaDadosLocadorLocatario(ret.getId_aluguel(),"");
                    RetornaAluguelEncontro r = aluguelRepository.retornaAluguelEncontro(ret.getId_aluguel(),"");
                    switch (ret.getStatus()){
                        case 11:
                            locadorMail = new TemplateEmails().donoEstornoNaoInseridoLocal(dados.getLocadorNome(),dados.getLocatarioNome(), dados.getProdutoNome(),r.getPeriodo(),r.getValor());
                            locatarioMail = new TemplateEmails().locatarioEstornoNaoInseridoLocal(dados.getLocatarioNome(),dados.getLocadorNome(), dados.getProdutoNome(),r.getPeriodo(),r.getValor());
                            aluguelRepository.alteraStatusAluguel(ret.getId_aluguel(),StatusAluguel.CANCELAMENTO_AUTOMATICO_LOCATARIO_NAO_PREENCHEU_AS_INFORMACOES_DE_ENCONTRO_DENTRO_DO_PRAZO.getCod_status(),"");
                            break;

                        case 9:
                            locadorMail = new TemplateEmails().donoEstornoNaoConfirmadoLocal(dados.getLocadorNome(),dados.getLocatarioNome(), dados.getProdutoNome(),r.getPeriodo(),r.getValor());
                            locatarioMail = new TemplateEmails().locatarioEstornoNaoConfirmadoLocal(dados.getLocatarioNome(),dados.getLocadorNome(), dados.getProdutoNome(),r.getPeriodo(),r.getValor());
                            aluguelRepository.alteraStatusAluguel(ret.getId_aluguel(),StatusAluguel.CANCELAMENTO_AUTOMATICO_DONO_NAO_CONFIRMOU_O_ENCONTRO_DENTRO_DO_PRAZO.getCod_status(), "");
                            break;

                        case 12:
                            //TODO
                            break;

                        default:
                            break;
                    }
                    emailService.sendEmail(dados.getLocatarioEmail(),"Cancelamento de aluguel", locatarioMail);
                    emailService.sendEmail(dados.getLocadorEmail(),"Cancelamento de aluguel", locadorMail);
                    //String locatarioMail = new TemplateEmails().locatario

                }
                else
                    System.out.println("erro ao estornar pagamento");
            }
            System.out.println(LocalTime.now(ZoneId.of("America/Sao_Paulo")) + " - Foram enviados " + l.size() * 2 + " emails");
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = "";
            //String user = usuarioController.getIdUsuario();
            logRepository.gravaLogBackend(className, methodName, endpoint, "", e.getMessage(), Throwables.getStackTraceAsString(e));
        }

    }
}
