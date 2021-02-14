package com.onlycoders.backendalugo.api.rest;

import com.google.common.base.Throwables;
import com.onlycoders.backendalugo.model.entity.email.RetornoAlugueisNotificacao;
import com.onlycoders.backendalugo.model.entity.email.templatesEmails.TemplateEmails;
import com.onlycoders.backendalugo.model.entity.pagamento.Data;
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
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@Api(value = "Pagamento")
@RequestMapping("/pagamento")
@CrossOrigin(origins = "*")
@Secured("ROLE_USER")
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
    public Boolean retornoPagamento(@RequestBody WebHookPagamento webHookPagamento,
                                    @RequestParam(value = "data.id",required = false) String DataId,
                                    @RequestParam(value = "id",required = false) String id,
                                    @RequestParam(value = "topic",required = false)String topic,
                                    @RequestParam(value = "type",required = false)String type) {
        try {
            if(!topic.isEmpty())
                return true;

            String tipoRetorno = webHookPagamento.getType();
            System.out.println("tipo retorno: " + tipoRetorno);
            String idPagamento = webHookPagamento.getData().getId();
            System.out.println("tipo pagamento " + idPagamento);
            final String uri = "https://api.mercadopago.com/v1/payments/"; //Exemplo

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(uri.concat(DataId), String.class);
            System.out.println("response: " + response);
            JSONObject result = new JSONObject(response);
            System.out.println("result: " + result);
            String status = result.getString("status");
            System.out.println("status: " + status);
            String idAluguel = result.getString("external_reference"); //exemplo
            System.out.println(idAluguel);
            return aluguelRepository.salvaRetornoPagamento(idAluguel, idPagamento,tipoRetorno,status,SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]);
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
