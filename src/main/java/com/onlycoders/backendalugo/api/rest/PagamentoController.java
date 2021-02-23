package com.onlycoders.backendalugo.api.rest;

import com.google.common.base.Throwables;
import com.onlycoders.backendalugo.model.entity.email.RetornoAlugueisNotificacao;
import com.onlycoders.backendalugo.model.entity.email.templatesEmails.TemplateEmails;
import com.onlycoders.backendalugo.model.entity.pagamento.WebHookPagamento;
import com.onlycoders.backendalugo.model.repository.AluguelRepository;
import com.onlycoders.backendalugo.model.repository.LogRepository;
import com.onlycoders.backendalugo.model.repository.ProdutoRepository;
import com.onlycoders.backendalugo.model.repository.UsuarioRepository;
import com.onlycoders.backendalugo.service.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.onlycoders.backendalugo.model.entity.aluguel.StatusInterfaceEnum.StatusAluguel;

@RestController
@Api(value = "Pagamento")
@RequestMapping("/pagamento")
@CrossOrigin(origins = "*")
//@Secured("ROLE_USER")
public class PagamentoController {

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
/*
    @ApiOperation(value = "Realiza a baixa do pagamento no sistema e envia os emails.")
    @GetMapping("/efetua")
    @ResponseStatus(HttpStatus.OK)
    public Boolean salvaPagamento(@RequestParam("id_aluguel") String id_aluguel) {
        try {
            String usuario = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            aluguelRepository.alteraStatusAluguel(id_aluguel, 11, usuario);
            RetornoAlugueisNotificacao r = aluguelRepository.retornaDadosLocadorLocatario(id_aluguel,usuario);
            String locadorMail = new TemplateEmails().pagamentoAluguelDono(r.getLocadorNome(),r.getLocatarioNome(),r.getLocatarioCelular());
            String locatarioMail = new TemplateEmails().pagamentoAluguelLocatario(r.getLocatarioNome(),r.getLocadorNome(),r.getLocadorCelular());
            emailService.sendEmail(r.getLocadorEmail(),"Confirmação de pagamento", locadorMail);
            emailService.sendEmail(r.getLocatarioEmail(),"Confirmação de pagamento", locatarioMail);
            //String locatarioMailLocal = new TemplateEmails().notificaLocatarioAluguelLocal()
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
*/
    @ApiOperation(value = "Salva url para pagamento")
    @GetMapping("/url-pagamento")
    @ResponseStatus(HttpStatus.OK)
    public Boolean salvaUrl(@Param(value = "id_aluguel") String id_aluguel, @Param(value = "url_pagamento") String url_pagamento) {
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

    @ApiOperation(value = "Recebe notificacao de pagamento do Mercado Pago")
    @PostMapping("/retorno-pagamento")
    @ResponseStatus(HttpStatus.OK)
    public Boolean retornoPagamento(@RequestBody(required = false) WebHookPagamento webHookPagamento,
                                    @RequestParam(value = "data.id",required = false) String DataId,
                                    @RequestParam(value = "id",required = false) String id,
                                    @RequestParam(value = "topic",required = false,defaultValue = "")String topic,
                                    @RequestParam(value = "type",required = false)String type) {
        try {
            String usuario = "Mercado Pago";
            if(!topic.isEmpty()){
                System.out.println("Topic");
                return true;
            }
            String tipoRetorno = webHookPagamento.getType();
            String idPagamento = webHookPagamento.getData().getId();
            final String uri = "https://api.mercadopago.com/v1/payments/"; //Exemplo

            String accessToken= "TEST-3839591210769699-020717-920ee176862d215166e271d66e8432f7-132870722";
            RestTemplate restTemplate = new RestTemplateBuilder(rt-> rt.getInterceptors().add((request, body, execution) -> {
                request.getHeaders().add("Authorization", "Bearer " + accessToken);
                return execution.execute(request, body);
            })).build();

            //HttpHeaders headers = new HttpHeaders();
            //headers.add("Authorization","bearer TEST-3839591210769699-020717-920ee176862d215166e271d66e8432f7-132870722");
            //headers.set("Authorization","bearer TEST-3839591210769699-020717-920ee176862d215166e271d66e8432f7-132870722");
            //HttpEntity<String> entity = new HttpEntity<String>("Authorization", headers);
            //RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(uri.concat(idPagamento), String.class);
            //HttpEntity<String> response = restTemplate.exchange(uri.concat(idPagamento), HttpMethod.GET, entity, String.class);

            JSONObject result = new JSONObject(response);
            String status = result.getString("status");
            String idAluguel = result.getString("external_reference"); //exemplo
            RetornoAlugueisNotificacao r = aluguelRepository.retornaDadosLocadorLocatario(idAluguel,usuario);
            if(!status.equals("approved")){
                String locatarioMail = new TemplateEmails().locatarioPagamentoRecusado(r.getLocatarioNome());
                emailService.sendEmail(r.getLocatarioEmail(),"Confirmação de pagamento", locatarioMail);
                return true;
            }
            else {
                String locadorMail = new TemplateEmails().pagamentoAluguelDono(r.getLocadorNome(), r.getLocatarioNome(), r.getLocatarioEmail(), r.getLocatarioCelular());
                String locatarioMail = new TemplateEmails().pagamentoAluguelLocatario(r.getLocatarioNome(), r.getLocadorNome(), r.getLocadorEmail(), r.getLocadorCelular());
                emailService.sendEmail(r.getLocadorEmail(), "Confirmação de pagamento", locadorMail);
                emailService.sendEmail(r.getLocatarioEmail(), "Confirmação de pagamento", locatarioMail);
                aluguelRepository.alteraStatusAluguel(idAluguel, StatusAluguel.PAGAMENTO_CONFIRMADO.getCod_status(), usuario);
                return aluguelRepository.salvaRetornoPagamento(idAluguel, idPagamento, tipoRetorno, status, SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]);
            }
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, (e.getMessage()==null) ? "" : e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
        }
    }


    @Secured("ROLE_USER")
    @ApiOperation(value = "Efetua saque do saldo do locador")
    @PostMapping("/saque-locador")
    @ResponseStatus(HttpStatus.OK)
    public Boolean efetuaSaqueLocador(){
        try{
            String usaurio = getIdUsuario();
            return aluguelRepository.efetuaSaque(usaurio,usaurio);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, (e.getMessage()==null) ? "" : e.getMessage(), Throwables.getStackTraceAsString(e));
            return false;
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
}
