package com.onlycoders.backendalugo.api.rest;
import com.google.common.base.Throwables;
import com.onlycoders.backendalugo.model.entity.admin.RetornaGravidades;
import com.onlycoders.backendalugo.model.entity.admin.RetornaProblemas;
import com.onlycoders.backendalugo.model.entity.admin.RetornaTiposProblema;
import com.onlycoders.backendalugo.model.entity.aluguel.template.Meses;
import com.onlycoders.backendalugo.model.entity.admin.LogErros;
import com.onlycoders.backendalugo.model.entity.email.RetornoUsuarioProdutoNoficacao;
import com.onlycoders.backendalugo.model.entity.email.templatesEmails.TemplateEmails;
import com.onlycoders.backendalugo.model.entity.logs.*;
import com.onlycoders.backendalugo.model.entity.produto.templates.ProdutoAluguel;
import com.onlycoders.backendalugo.model.entity.usuario.templates.RetornaUsuario;
import com.onlycoders.backendalugo.model.repository.AdminRepository;
import com.onlycoders.backendalugo.model.repository.LogRepository;
import com.onlycoders.backendalugo.model.repository.ProdutoRepository;
import com.onlycoders.backendalugo.service.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;

@RestController
@Api(value = "Admin")
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
@Secured("ROLE_ADMIN")
public class AdminController {

    private final AdminRepository adminRepository;
    private final ProdutoRepository produtoRepository;
    private final LogRepository logRepository;
    private final EmailService emailService;
    private final ProdutoController produtoController;
    private final UsuarioController usuarioController;

    @Autowired
    public AdminController(AdminRepository adminRepository, ProdutoRepository produtoRepository, LogRepository logRepository, EmailService emailService, ProdutoController produtoController, UsuarioController usuarioController) {
        this.adminRepository = adminRepository;
        this.produtoRepository = produtoRepository;
        this.logRepository = logRepository;
        this.emailService = emailService;
        this.produtoController = produtoController;
        this.usuarioController = usuarioController;
    }

    @ApiOperation(value = "Desativa e ativa usuário")
    @DeleteMapping("inativa-usuario")
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Boolean activateDesactivateUserById(@RequestParam String id_usuario, @RequestParam(value = "motivo",defaultValue = "",required = false) String motivo) {
        try{
            String usuario = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            if(adminRepository.activateDesactivateUserById(id_usuario,usuario,motivo)){
                RetornaUsuario dadosUsuario = adminRepository.findUsuario(id_usuario,usuario).get(0);
                //System.out.println(dadosUsuario.getNome() + "|" + dadosUsuario.getEmail());
                String bodyMail = (dadosUsuario.getAtivo()) ?
                        new TemplateEmails().usuarioAtivado(dadosUsuario.getNome()) : new TemplateEmails().usuarioInativado(dadosUsuario.getNome(),motivo);
                emailService.sendEmail(dadosUsuario.getEmail(),"Status da conta",bodyMail);
            }
            return true;
        }catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, (e.getMessage()==null)?"":e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @ApiOperation(value = "Retorna dados de todos os usuarios", response = RetornaUsuario.class)
    @GetMapping("/lista-usuario")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Page<RetornaUsuario>
    retornaUsuario(@RequestParam(value = "page",
                                 required = false,
                                 defaultValue = "0") int page,
                   @RequestParam(value = "size",
                                 required = false,
                                 defaultValue = "10") int size) {
        try {
            Pageable paging = PageRequest.of(page, size);
            List<RetornaUsuario> listUsuarios = adminRepository.findUsuario("0",SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]);

            int start = (int) paging.getOffset();
            int end = Math.min((start + paging.getPageSize()), listUsuarios.size());
            return new PageImpl<>(listUsuarios.subList(start, end), paging, listUsuarios.size());
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return new PageImpl<>(new ArrayList<>(),PageRequest.of(1,1),0);
        }
    }

    @ApiOperation(value = "Retorna log erros", response = LogErros.class)
    @GetMapping("/log-erros")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Page<LogErros>
    retornaLogErros(@RequestParam(value = "page",required = false,defaultValue = "0") int page,
                    @RequestParam(value = "size",required = false,defaultValue = "10") int size,
                    @RequestParam(value = "sort",required = false,defaultValue = "id") String sortBy,
                    @RequestParam(value = "order",required = false,defaultValue = "desc") String order){
        Pageable paging = PageRequest.of(page, size, (order.equalsIgnoreCase("desc")) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());

        try {
            List<LogErros> listErros = adminRepository.retornaErros(SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]);
            int start = (int) paging.getOffset();
            int end = Math.min((start + paging.getPageSize()), listErros.size());
            return new PageImpl<>(listErros.subList(start, end), paging, listErros.size());
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return new PageImpl<>(new ArrayList<>(),PageRequest.of(1,1),0);
        }
    }

    @ApiOperation(value = "Retorna produtos não publicados", response = ProdutoAluguel.class)
    @GetMapping("/publicar-produtos")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Page<ProdutoAluguel>
    retornaProdutosNaoPublicados(@RequestParam(value = "page",required = false,defaultValue = "0") int page,
                                 @RequestParam(value = "size",required = false,defaultValue = "10") int size,
                                 @RequestParam(value = "sort",required = false,defaultValue = "qtd_alugueis") String sortBy,
                                 @RequestParam(value = "order",required = false,defaultValue = "desc") String order,
                                 @RequestParam(value = "categoria",required = false,defaultValue = "0") int categoria){
        Pageable paging = PageRequest.of(page, size, (order.equalsIgnoreCase("desc")) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
        try {
            Optional<Page<ProdutoAluguel>> produtos = Optional.ofNullable(
                    produtoController.transformaRetornoProdutoToPage(produtoRepository.findProduto("0", "0", 2, categoria,SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]), paging));
            return produtos.orElse(null);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return new PageImpl<>(new ArrayList<>(),PageRequest.of(1,1),0);
        }
    }

    @ApiOperation(value = "Reprovar produtos")
    @GetMapping("/reprovar-produto")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Boolean rejeitaProduto(@RequestParam("id_produto")String idProduto,
                                  @RequestParam("motivo") String motivo){
        try{
            String usuario = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            RetornoUsuarioProdutoNoficacao ret = adminRepository.rejeitaProduto(idProduto,motivo,usuario);
            String mailBody = new TemplateEmails().produtoRejeitado(ret.getNomeUsuario(),ret.getNomeProduto(),motivo);
            emailService.sendEmail(ret.getEmailUsuario(),"Cadastro de produto", mailBody);
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

    @ApiOperation(value = "Aprovar produtos")
    @GetMapping("/aprovar-produto")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Boolean aprovaProduto(@RequestParam("id_produto")String idProduto,
                                 @RequestParam("motivo") String motivo){
        try{
            String usuario = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            RetornoUsuarioProdutoNoficacao ret = adminRepository.aprovaProduto(idProduto,motivo,usuario);
            String mailBody = new TemplateEmails().produtoAceito(ret.getNomeUsuario(),ret.getNomeProduto());
            emailService.sendEmail(ret.getEmailUsuario(),"Cadastro de produto", mailBody);
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

    @ApiOperation(value = "Retorna log backend", response = LogErros.class)
    @GetMapping("/log-erros-backend")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<ErrosControllerMetodos> retornaLogsBackend(){
        String usuario = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
        try{
            String[] mes;
            String[] mesDescricao;
            String[] mesQuantidade;
            List<RetornoErrosBackendController> retornoErrosBackendController = adminRepository.retornaErrosController(usuario);
            List<ErrosBackendMetodo> errosBackendMetodos;
            List<ErrosControllerMetodos> errosControllerMetodos = new ArrayList<>();
            List<Meses> mesesMetodos = null;
            List<Meses> mesesController = null;
            for(RetornoErrosBackendController retController : retornoErrosBackendController){
                List<RetornoErrosBackendMetodos> retornoErrosBackendMetodos = adminRepository.retornaErrosMetodo(retController.getController(),usuario);
                errosBackendMetodos = new ArrayList<>();
                for(RetornoErrosBackendMetodos retMetodos : retornoErrosBackendMetodos) {
                    if (retMetodos.getMes().contains(";")) {
                        mes = retMetodos.getMes().split(";");
                        mesDescricao = mes[0].split(",");
                        mesQuantidade = mes[1].split(",");
                        mesesMetodos = new ArrayList<>();
                        for (int i = 0; i < mesDescricao.length; i++) {
                            mesesMetodos.add(new Meses(mesDescricao[i], Integer.parseInt(mesQuantidade[i])));
                        }
                    }
                    errosBackendMetodos.add(new ErrosBackendMetodo(retMetodos.getMetodo(), retMetodos.getEndpoint(), retMetodos.getQuantidade(), mesesMetodos));
                }
                if (retController.getMes().contains(";")){
                    mes = retController.getMes().split(";");
                    mesDescricao = mes[0].split(",");
                    mesQuantidade = mes[1].split(",");
                    mesesController = new ArrayList<>();
                    for(int i = 0;i<mesDescricao.length;i++) {
                        mesesController.add(new Meses(mesDescricao[i], Integer.parseInt(mesQuantidade[i])));
                    }
                }
                errosControllerMetodos.add(new ErrosControllerMetodos(retController.getController(),retController.getQuantidade(),mesesController,errosBackendMetodos));
            }
            return errosControllerMetodos;
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, (e.getMessage()==null) ? "" : e.getMessage(), Throwables.getStackTraceAsString(e));
            return new ArrayList<>();
        }
    }

    @ApiOperation(value = "Retorna erros procedure agrupados", response = ErrosProcedureAgrupado.class)
    @GetMapping("/log-erros-banco")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<ErrosProcedureAgrupado> retornaErrosProcedureAgurapado(){
        try{
            String usuario = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            List<RetornaErrosProcedureAgrupado> errosProcedureAgrupado = adminRepository.retornaErrosProcedureAgrupado(usuario);
            String[] mes;
            String[] mesDescricao;
            String[] mesQuantidade;
            List<Meses> meses = null;
            List<ErrosProcedureAgrupado> errosProcedureAgrupados = new ArrayList<>();
            for(RetornaErrosProcedureAgrupado retProcedure : errosProcedureAgrupado){
                if (retProcedure.getMes().contains(";")) {
                    mes = retProcedure.getMes().split(";");
                    mesDescricao = mes[0].split(",");
                    mesQuantidade = mes[1].split(",");
                    meses = new ArrayList<>();
                    for (int i = 0; i < mesDescricao.length; i++) {
                        meses.add(new Meses(mesDescricao[i], Integer.parseInt(mesQuantidade[i])));
                    }
                }
                errosProcedureAgrupados.add(new ErrosProcedureAgrupado(retProcedure.getProcedure(),retProcedure.getQuantidade(),meses));
            }
            return errosProcedureAgrupados;
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, (e.getMessage()==null) ? "" : e.getMessage(), Throwables.getStackTraceAsString(e));
            return new ArrayList<>();
        }
    }

    @ApiOperation(value = "Retorna erros backend detalhado")
    @GetMapping("/log-erros-backend-detalhe")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Page<RetornaLogBackendDetalhe> retornaLogsBackendDetalhe(@RequestParam(value = "page",required = false,defaultValue = "0") int page,
                                                                    @RequestParam(value = "size",required = false,defaultValue = "10") int size,
                                                                    @RequestParam(value = "sort",required = false,defaultValue = "usuario") String sortBy,
                                                                    @RequestParam(value = "order",required = false,defaultValue = "desc") String order){
        try {
            String usuario = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            //Sort sort = Sort.by((order.equalsIgnoreCase("desc")) ? Sort.Direction.DESC : Sort.Direction.ASC,sortBy);
            Pageable paging = PageRequest.of(page, size, Sort.by((order.equalsIgnoreCase("desc")) ? Sort.Order.by(sortBy) : Sort.Order.desc(sortBy)));
            List<RetornaLogBackendDetalhe> retornaLogBackendDetalhes = adminRepository.retornaLogBackendDetalhe(usuario);
            //retornaLogBackendDetalhes.sort(Comparator.comparing(RetornaLogBackendDetalhe::getUsuario));

            int start = (int) paging.getOffset();
            int end = Math.min((start + paging.getPageSize()), retornaLogBackendDetalhes.size());
            return new PageImpl<>(retornaLogBackendDetalhes.subList(start, end), paging, retornaLogBackendDetalhes.size());//listPa;
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return new PageImpl<>(new ArrayList<>(),PageRequest.of(1,1),0);
        }

    }

    //@ApiOperation(value = "Acata problema e efetua devolução de acordo com a gravidade", response = ErrosProcedureAgrupado.class)
    //@GetMapping("/estorno-aluguel")
    //@ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Retorna problemas")
    @GetMapping("/problemas")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Page<RetornaProblemas> retornaProblemas(@RequestParam(value = "page",required = false,defaultValue = "0") int page,
                                                   @RequestParam(value = "size",required = false,defaultValue = "10") int size,
                                                   @RequestParam(value = "sort",required = false,defaultValue = "usuario") String sortBy,
                                                   @RequestParam(value = "order",required = false,defaultValue = "desc") String order,
                                                   @RequestParam(value = "tipo_problema",required = false,defaultValue = "0") int tipo_problema,
                                                   @RequestParam(value = "gravidade",required = false,defaultValue = "0")int gravidade,
                                                   @RequestParam(value = "status",required = false,defaultValue = "0")int status,
                                                   @RequestParam(value = "data_inicio", required = false, defaultValue = "")String data_inicio){
        try{
            String usuario = usuarioController.getIdUsuario();
            Pageable paging = PageRequest.of(page, size, Sort.by((order.equalsIgnoreCase("desc")) ? Sort.Order.by(sortBy) : Sort.Order.desc(sortBy)));
            List<RetornaProblemas> lista = adminRepository.retornaProblemas(tipo_problema,gravidade,status,data_inicio,usuario);
            int start = (int) paging.getOffset();
            int end = Math.min((start + paging.getPageSize()), lista.size());
            return new PageImpl<>(lista.subList(start, end), paging, lista.size());//listPa;

        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return new PageImpl<>(new ArrayList<>(),PageRequest.of(1,1),0);
        }
    }

    @ApiOperation(value = "Aprova problema")
    @GetMapping("/aprova-problema")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Boolean aprovaProblema(@RequestParam("id_problema")String id_problema,
                                   @RequestParam("gravidade") Integer gravidade){
        try{
            String usuario = usuarioController.getIdUsuario();
            //TODO realzar estorno/pagamento do problema
            return adminRepository.aprovaProblema(id_problema,gravidade,usuario);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = usuarioController.getIdUsuario();
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @ApiOperation(value = "Reprova problema")
    @GetMapping("/reprova-problema")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Boolean reprovaProblema(@RequestParam("id_problema")String id_problema){
        try{
            String usuario = usuarioController.getIdUsuario();
            return adminRepository.reprovaProblema(id_problema,usuario);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = usuarioController.getIdUsuario();
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @ApiOperation(value = "Retorna gravidade")
    @GetMapping("/gravidades")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<RetornaGravidades> reprovaGravidades(){
        try{
            return adminRepository.retornaGravidades();
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
}
