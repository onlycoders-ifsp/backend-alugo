package com.onlycoders.backendalugo.api.rest;
import com.google.common.base.Throwables;
import com.onlycoders.backendalugo.model.entity.aluguel.template.Meses;
import com.onlycoders.backendalugo.model.entity.admin.LogErros;
import com.onlycoders.backendalugo.model.entity.email.RetornoUsuarioProdutoNoficacao;
import com.onlycoders.backendalugo.model.entity.email.templatesEmails.TemplateEmails;
import com.onlycoders.backendalugo.model.entity.logs.*;
import com.onlycoders.backendalugo.model.entity.produto.templates.Categorias;
import com.onlycoders.backendalugo.model.entity.produto.templates.DtAlugadas;
import com.onlycoders.backendalugo.model.entity.produto.templates.ProdutoAluguel;
import com.onlycoders.backendalugo.model.entity.produto.templates.RetornaProduto;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
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

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private EmailService emailService;

    @ApiOperation(value = "Desativa e ativa usuário")
    @DeleteMapping("inativa-usuario")
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
            int end = (start + paging.getPageSize()) > listUsuarios.size() ? listUsuarios.size() : (start + paging.getPageSize());
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
    public Page<LogErros>
    retornaLogErros(@RequestParam(value = "page",required = false,defaultValue = "0") int page,
                    @RequestParam(value = "size",required = false,defaultValue = "10") int size,
                    @RequestParam(value = "sort",required = false,defaultValue = "id") String sortBy,
                    @RequestParam(value = "order",required = false,defaultValue = "desc") String order){
        Pageable paging = PageRequest.of(page, size, (order.equalsIgnoreCase("desc")) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());

        try {
            List<LogErros> listErros = adminRepository.retornaErros(SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]);
            int start = (int) paging.getOffset();
            int end = (start + paging.getPageSize()) > listErros.size() ? listErros.size() : (start + paging.getPageSize());
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
    public Page<ProdutoAluguel>
    retornaProdutosNaoPublicados(@RequestParam(value = "page",required = false,defaultValue = "0") int page,
                                 @RequestParam(value = "size",required = false,defaultValue = "10") int size,
                                 @RequestParam(value = "sort",required = false,defaultValue = "qtd_alugueis") String sortBy,
                                 @RequestParam(value = "order",required = false,defaultValue = "desc") String order,
                                 @RequestParam(value = "categoria",required = false,defaultValue = "0") int categoria){
        Pageable paging = PageRequest.of(page, size, (order.equalsIgnoreCase("desc")) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
        try {
            Optional<Page<ProdutoAluguel>> produtos = Optional.ofNullable(
                    transformaRetornoProdutoToPage(produtoRepository.findProduto("0", "0", 2, categoria,SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]), paging));
            return produtos.get();
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
    public List<ErrosControllerMetodos> retornaLogsBackend(){
        String usuario = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
        try{
            String[] mes;
            String[] mesDescricao;
            String[] mesQuantidade;
            List<RetornoErrosBackendController> retornoErrosBackendController = adminRepository.retornaErrosController(usuario);
            List<ErrosBackendMetodo> errosBackendMetodos = null;
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
                            mesesMetodos.add(new Meses(mesDescricao[i], Integer.valueOf(mesQuantidade[i])));
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
                        mesesController.add(new Meses(mesDescricao[i], Integer.valueOf(mesQuantidade[i])));
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
                        meses.add(new Meses(mesDescricao[i], Integer.valueOf(mesQuantidade[i])));
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

    @ApiOperation(value = "Retorna erros backend detalhado", response = ErrosProcedureAgrupado.class)
    @GetMapping("/log-erros-backend-detalhe")
    @ResponseStatus(HttpStatus.OK)
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
            int end = (start + paging.getPageSize()) > retornaLogBackendDetalhes.size() ? retornaLogBackendDetalhes.size() : (start + paging.getPageSize());
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
