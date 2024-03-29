package com.onlycoders.backendalugo.api.rest;
import com.google.common.base.Throwables;
import com.onlycoders.backendalugo.model.entity.email.templatesEmails.TemplateEmails;
import com.onlycoders.backendalugo.model.entity.produto.Produto;
import com.onlycoders.backendalugo.model.entity.produto.templates.*;
import com.onlycoders.backendalugo.model.repository.LogRepository;
import com.onlycoders.backendalugo.model.repository.ProdutoRepository;
import com.onlycoders.backendalugo.model.repository.UsuarioRepository;
import com.onlycoders.backendalugo.service.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RestController
@Api(value = "Produtos")
@RequestMapping("/produtos")
@CrossOrigin("*")
public class ProdutoController {

    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LogRepository logRepository;
    private final EmailService emailService;

    @Autowired
    public ProdutoController(ProdutoRepository produtoRepository, UsuarioRepository usuarioRepository, LogRepository logRepository, EmailService emailService) {
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
        this.logRepository = logRepository;
        this.emailService = emailService;
    }

    @ApiOperation(value = "Retorna produtos do usuario logado.", response = RetornaProduto.class)
    @GetMapping("/lista-produto-logado")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public //ResponseEntity<?>//
    Page<ProdutoAluguel>
    retornaProdutosUsuarioLogado(@RequestParam(value = "page",required = false,defaultValue = "0") int page,
                                 @RequestParam(value = "size",required = false,defaultValue = "10") int size,
                                 @RequestParam(value = "sort",required = false,defaultValue = "qtd_alugueis") String sortBy,
                                 @RequestParam(value = "order",required = false,defaultValue = "desc") String order,
                                 @RequestParam(value = "categoria",required = false,defaultValue = "0") int categoria){

        try {
            Pageable paging = PageRequest.of(page, size, (order.equalsIgnoreCase("desc")) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
            Optional<Page<ProdutoAluguel>> produtos = Optional.ofNullable(transformaRetornoProdutoToPage(produtoRepository
                    .findProduto(getIdUsuario(false), "0", 1, categoria,SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]), paging));
        /*if (!produtos.get().getContent().isEmpty()) {
            //return new ResponseEntity<>(produtos.get(), HttpStatus.OK);

        }*/
            //return new ResponseEntity<>("Nenhum produto disponível", HttpStatus.OK);
            //throw new NotFoundException("Nenhum produto disponível");
            return produtos.orElse(null);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String[] user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|");
            logRepository.gravaLogBackend(className, methodName, endpoint, user[0], (e.getMessage()==null) ? "":e.getMessage(), Throwables.getStackTraceAsString(e));
            return new PageImpl<>(new ArrayList<>(),PageRequest.of(1,1),0);
        }
    }

    @ApiOperation(value = "Retorna um único produto do usuario logado.", response = RetornaProduto.class)
    @GetMapping("/produto-logado")
    @ResponseStatus(HttpStatus.OK)
    public ProdutoAluguel /*ResponseEntity<?>*/
    retornaProdutoUsuarioLogado(@RequestParam String id_produto,
                                @RequestParam(value = "categoria",required = false,defaultValue = "0") int categoria){
        try{
            Pageable paging = PageRequest.of(0, 1);
            Optional<ProdutoAluguel> produtos = Optional.ofNullable(transformaRetornoProdutoToPage(
                    produtoRepository.findProduto(getIdUsuario(false), id_produto, 2, categoria,SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]), paging)
                    .getContent().get(0));
        /*if (produtos.isPresent()) {
            return new ResponseEntity<>(produtos.get(), HttpStatus.OK);
        }*/
            //return new ResponseEntity<>("Nenhum produto disponível", HttpStatus.OK);
            //throw new NotFoundException("Nenhum produto disponível");
            return produtos.orElse(null);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, e.getMessage(), Throwables.getStackTraceAsString(e));
            return new ProdutoAluguel();
        }
    }

    @ApiOperation(value = "Retorna todos os produtos", response = RetornaProduto.class)
    @GetMapping("/lista-produto")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public //ResponseEntity<?>//
    Page<ProdutoAluguel>
    retornaProdutos(@RequestParam(value = "page",required = false,defaultValue = "0") int page,
                    @RequestParam(value = "size",required = false,defaultValue = "10") int size,
                    @RequestParam(value = "sort",required = false,defaultValue = "qtd_alugueis") String sortBy,
                    @RequestParam(value = "order",required = false,defaultValue = "desc") String order,
                    @RequestParam(value = "categoria",required = false,defaultValue = "0") int categoria){
        try{
            //System.out.println(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().toString().replace("/lista-usuario","/retorno-pagamento"));

            Pageable paging = PageRequest.of(page, size, (order.equalsIgnoreCase("desc")) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
            //System.out.println(ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath());
            Optional<Page<ProdutoAluguel>> produtos = Optional.ofNullable(transformaRetornoProdutoToPage(produtoRepository
                    .findProduto("0", "0", 4, categoria,SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]), paging));

        /*if (!produtos.get().isEmpty()) {
            return new ResponseEntity<>(produtos.get(), HttpStatus.OK);
        }*/
            //throw new NotFoundException("Nenhum produto localizado");

            return produtos.orElse(null);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, (e.getMessage()==null) ? "" : e.getMessage(), Throwables.getStackTraceAsString(e));
            return new PageImpl<>(new ArrayList<>(),PageRequest.of(1,1),0);
        }
        //return new ResponseEntity<>("Nenhum produto localizado", HttpStatus.OK);

    }

    @ApiOperation(value = "Retorna um único produto", response = RetornaProduto.class)
    @GetMapping("/produto")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ProdutoAluguel /*ResponseEntity<?>*/
    retornaProduto(@RequestParam String id_produto,
                   @RequestParam(value = "categoria",required = false,defaultValue = "0") int categoria){
        try {
            Pageable paging = PageRequest.of(0, 1);
            Optional<ProdutoAluguel> produtos = Optional.ofNullable(transformaRetornoProdutoToPage(
                    produtoRepository.findProduto("0", id_produto, 7, categoria,SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]), paging)
                    .getContent()
                    .get(0));
        /*if (produtos.isPresent()) {
            return new ResponseEntity<>(produtos.get(), HttpStatus.OK);
        }*/
            return produtos.orElse(null);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, (e.getMessage()==null)?"":e.getMessage(), Throwables.getStackTraceAsString(e));
            return new ProdutoAluguel();
        }
        //throw new NotFoundException("Produto não localizado");
        //return new ResponseEntity<>(produtos.get(), HttpStatus.OK);
        //return new ResponseEntity<>("Produto não localizado", HttpStatus.OK);
    }

    @ApiOperation(value = "Pesquisa de produto", response = RetornaProduto.class)
    @GetMapping("/produto-pesquisa")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public //ResponseEntity<?>//
    Page <ProdutoAluguel>
    retornaProdutoPesquisa(@RequestParam String produto,
                           @RequestParam(value = "page",required = false,defaultValue = "0") int page,
                           @RequestParam(value = "size",required = false,defaultValue = "10") int size,
                           @RequestParam(value = "sort",required = false,defaultValue = "qtd_alugueis") String sortBy,
                           @RequestParam(value = "order",required = false,defaultValue = "desc") String order,
                           @RequestParam(value = "categoria",required = false,defaultValue = "0") int categoria){
        try {
            Pageable paging = PageRequest.of(page, size, (order.equalsIgnoreCase("desc")) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
            Optional<Page<ProdutoAluguel>> produtos = Optional.ofNullable(
                    transformaRetornoProdutoToPage(produtoRepository.findProduto("0", produto, 3, categoria,SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]), paging));
        /*if (!produtos.get().getContent().isEmpty()) {
            return new ResponseEntity<>(produtos.get(), HttpStatus.OK);
        }*/
            return produtos.orElse(null);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String[] user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|");
            logRepository.gravaLogBackend(className, methodName, endpoint, user[0], e.getMessage(), Throwables.getStackTraceAsString(e));
            return new PageImpl<>(new ArrayList<>(),PageRequest.of(1,1),0);
        }
        //throw new NotFoundException("Nenhum produto encontrado");
        //return new ResponseEntity<>("Nenhum produto encontrado", HttpStatus.OK);
    }

    @ApiOperation(value = "Retorna produtos de um usuario, id ou login", response = RetornaProduto.class)
    @GetMapping("/produto-usuario")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Page<ProdutoAluguel>
    retornaProdutosUsuario(@RequestParam String id_usuario, @RequestParam(value = "page",required = false,defaultValue = "0") int page,
                           @RequestParam(value = "size",required = false,defaultValue = "10") int size,
                           @RequestParam(value = "sort",required = false,defaultValue = "qtd_alugueis") String sortBy,
                           @RequestParam(value = "order",required = false,defaultValue = "desc") String order,
                           @RequestParam(value = "categoria",required = false,defaultValue = "0") int categoria) {
        try {
            Pageable paging = PageRequest.of(page, size, (order.equalsIgnoreCase("desc")) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
            return transformaRetornoProdutoToPage(
                    produtoRepository.findProduto(id_usuario, "0", 1, categoria,SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]), paging);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String[] user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|");
            logRepository.gravaLogBackend(className, methodName, endpoint, user[0], e.getMessage(), Throwables.getStackTraceAsString(e));
            return new PageImpl<>(new ArrayList<>(),PageRequest.of(1,1),0);
        }
    }

    @ApiOperation(value = "Cadastra novo produto do usuario logado")
    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ProdutoAluguel cadastra(@RequestBody Produto produto){
        try {
            String[] usuario = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|");
            Pageable paging = PageRequest.of(0, 1);
            String categoria = trasnformaCategoriasToString(produto.getCategorias());
            String idProduto =  produtoRepository.createProduto(getIdUsuario(false),
                    produto.getNome(), produto.getDescricao_curta(), produto.getDescricao(),
                    produto.getValor_base_diaria(), produto.getValor_base_mensal(), produto.getValor_produto(),
                    produto.getData_compra(), categoria, usuario[0]);
            List<RetornaProduto> ret = produtoRepository.findProduto("0", idProduto, 7, 0,SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]);
            ProdutoAluguel r = transformaRetornoProdutoToPage(ret,paging).getContent().get(0);
            //ProdutoAluguel p = transformaRetornoProdutoToPage(r, paging).getContent().get(0);
            String mailBody = new TemplateEmails().cadastroProduto(usuario[0],r.getNome());
            emailService.sendEmail(usuario[1],"Cadastro de produto", mailBody);
            return r;
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String[] user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|");
            logRepository.gravaLogBackend(className, methodName, endpoint, user[0], e.getMessage(), Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    @ApiOperation(value = "Atualiza/cadastra foto de produto")
    @PutMapping("/upload-foto")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Boolean atualizaCadastrafoto(@RequestParam Part capa_foto,
                                        @RequestParam String id_produto){
        String usuario = getIdUsuario(false);
        try{
            InputStream is = capa_foto.getInputStream();
            byte[] bytes = new byte[(int) capa_foto.getSize()];
            IOUtils.readFully(is,bytes);
            is.close();
            return produtoRepository.uploadFoto(usuario, id_produto, bytes,SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]);

        } catch(IOException e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String[] user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|");
            logRepository.gravaLogBackend(className, methodName, endpoint, user[0], e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }
    @ApiOperation(value = "Altera dados cadastrais do produto")
    @PutMapping("/altera")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Boolean atualiza(@RequestBody Produto produto) {
        try {
            String categoria = trasnformaCategoriasToString(produto.getCategorias());
            //String idUsuario = getIdUsuario(false);
            //System.out.println(produto);
            return produtoRepository.updateProduto(produto.getId_produto(), produto.getNome(), produto.getDescricao_curta(),
                    produto.getDescricao(), produto.getValor_base_diaria(), produto.getValor_base_mensal(), produto.getValor_produto(),
                    produto.getData_compra(), produto.getAtivo(), categoria, SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String[] user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|");
            logRepository.gravaLogBackend(className, methodName, endpoint, user[0], (e.getMessage()==null)?"":e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @ApiOperation(value = "Ativa ou inativa produto.")
    @DeleteMapping("ativa-inativa")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Boolean ativaInativa(@RequestParam String id_produto){
        try {
            return produtoRepository.ativaInativaProduto(id_produto,SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String[] user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|");
            logRepository.gravaLogBackend(className, methodName, endpoint, user[0], e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @ApiOperation(value = "Retorna categorias", response = RetornaProduto.class)
    @GetMapping("/categorias")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<RetornaCategorias> retornaCategorias(){
        try {
            return produtoRepository.retornaCategorias();
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String[] user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|");
            logRepository.gravaLogBackend(className, methodName, endpoint, user[0], e.getMessage(), Throwables.getStackTraceAsString(e));
            return new ArrayList<>();
        }
    }


    public String trasnformaCategoriasToString(List<Categorias> listCategorias){
        StringBuilder categoria = new StringBuilder();
        for(Categorias c : listCategorias) {
            if (categoria.length() == 0){
                categoria = new StringBuilder(c.getIdCategoria());
            }else {
                categoria.append(',').append(c.getIdCategoria());
            }
        }
        return categoria.toString();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public String getIdUsuario(boolean pesquisa){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login;
        if(!auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken){
            if(!pesquisa)
                throw new NullPointerException("Usuario não logado");
            else
                return "0";
        }
        login = usuarioRepository.retornaIdUsuario(auth.getName().split("\\|")[0]);
        if (login.isEmpty()) {
            if(!pesquisa)
                throw new NullPointerException("Usuario não encontrado");
            else
                return "0";
        }
        return login;
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
            List<DtAlugadas> dt = new ArrayList<>();// = produtoRepository.dtAlugadas(r.getId_produto());
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
        int end = Math.min((start + page.getPageSize()), listPa.size());
        return new PageImpl<>(listPa.subList(start,end),page,listPa.size());//listPa;
    }

    /*public List<ProdutoAluguel> transformaRetornoProduto(List<RetornaProduto> ret){
        List<ProdutoAluguel> listPa = new ArrayList<>();
        for(RetornaProduto r : ret){
            List<DatasAlugadas> dt = produtoRepository.dtAlugadas(r.getId_produto());
            ProdutoAluguel pa = new ProdutoAluguel();
            pa.setAtivo(r.getAtivo());
            pa.setCapa_foto(r.getCapa_foto());
            pa.setData_compra(r.getData_compra());
            pa.setDescricao(r.getDescricao());
            pa.setDescricao_curta(r.getDescricao_curta());
            //pa.setDt_aluguel(r.getDt_alugadas().split(","));
            pa.setDt_alugadas(dt);
            pa.setId_produto(r.getId_produto());
            pa.setId_usuario(r.getId_usuario());
            pa.setMedia_avaliacao(r.getMedia_avaliacao());
            pa.setNome(r.getNome());
            pa.setQtd_alugueis(r.getQtd_alugueis());
            pa.setTotal_ganhos(r.getTotal_ganhos());
            pa.setValor_base_diaria(r.getValor_base_diaria());
            pa.setValor_base_mensal(r.getValor_base_mensal());
            pa.setValor_produto(r.getValor_produto());
            listPa.add(pa);
        }
        //Page<ProdutoAluguel> produtos = new PageImpl<>(listPa,page, listPa.size());
        return listPa;
    }*/
}
