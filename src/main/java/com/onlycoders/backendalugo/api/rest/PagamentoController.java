package com.onlycoders.backendalugo.api.rest;

import com.google.common.base.Throwables;
import com.google.gson.Gson;
import com.mercadopago.MercadoPago;
import com.mercadopago.resources.Payment;
import com.mercadopago.resources.datastructures.payment.AdditionalInfo;
import com.mercadopago.resources.datastructures.payment.Item;
import com.mercadopago.resources.datastructures.payment.Payer;
import com.onlycoders.backendalugo.model.entity.aluguel.template.RetornoSaqueLocador;
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
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.onlycoders.backendalugo.model.entity.aluguel.StatusInterfaceEnum.StatusAluguel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Value("${mercado.pago.access.token}")
    String accessToken;// = "TEST-3839591210769699-020717-920ee176862d215166e271d66e8432f7-132870722";

    @Value("${backend.url}")
    String backendUrl;

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

            MercadoPago.SDK.setAccessToken(accessToken);

            String tipoRetorno = webHookPagamento.getType();
            String idPagamento = webHookPagamento.getData().getId();
            Payment pagamento = Payment.findById(idPagamento);
            String statusDetail = pagamento.getStatusDetail();

            /*
            final String uri = "https://api.mercadopago.com/v1/payments/"; //Exemplo
            // = "TEST-3839591210769699-020717-920ee176862d215166e271d66e8432f7-132870722";
            RestTemplate restTemplate = new RestTemplateBuilder(rt-> rt.getInterceptors().add((request, body, execution) -> {
                request.getHeaders().add("Authorization", "Bearer " + accessToken);
                return execution.execute(request, body);
            })).build();

            String response = restTemplate.getForObject(uri.concat(idPagamento), String.class);
            */

            /*
            JSONObject result = new JSONObject(response);
            String status = result.getString("status");
            String idAluguel = result.getString("external_reference"); //exemplo
            */
            String status = pagamento.getStatus().toString();
            if (statusDetail.contains("refunded"))
                status = "refunded";

            String idAluguel = pagamento.getExternalReference();
            String locatarioMail;

            RetornoAlugueisNotificacao r = aluguelRepository.retornaDadosLocadorLocatario(idAluguel,usuario);
            switch (status){
                case "approved":
                    String locadorMail = new TemplateEmails().pagamentoAluguelDono(r.getLocadorNome(), r.getLocatarioNome(), r.getLocatarioEmail(), r.getLocatarioCelular());
                    locatarioMail = new TemplateEmails().pagamentoAluguelLocatario(r.getLocatarioNome(), r.getLocadorNome(), r.getLocadorEmail(), r.getLocadorCelular());
                    emailService.sendEmail(r.getLocadorEmail(), "Confirmação de pagamento", locadorMail);
                    emailService.sendEmail(r.getLocatarioEmail(), "Confirmação de pagamento", locatarioMail);
                    aluguelRepository.alteraStatusAluguel(idAluguel, StatusAluguel.PAGAMENTO_CONFIRMADO.getCod_status(), usuario);
                    return aluguelRepository.salvaRetornoPagamento(idAluguel, idPagamento, tipoRetorno, status, SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]);

                    //todo
                case "refunded":
                    locatarioMail = new TemplateEmails().locatarioPagamentoRecusado(r.getLocatarioNome());
                    emailService.sendEmail(r.getLocatarioEmail(),"Confirmação de pagamento", locatarioMail);
                    return aluguelRepository.salvaRetornoPagamento(idAluguel, idPagamento, tipoRetorno, status, SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0]);

                case "rejected":
                    locatarioMail = new TemplateEmails().locatarioPagamentoRecusado(r.getLocatarioNome());
                    emailService.sendEmail(r.getLocatarioEmail(),"Confirmação de pagamento", locatarioMail);
                    return true;

                default:
                    return true;
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

    @ApiOperation(value = "Recebe notificacao de pagamento do Mercado Pago")
    @PostMapping("/retorno-pagamento-locador")
    @ResponseStatus(HttpStatus.OK)
    public Boolean retornoPagamentoLocador(@RequestBody(required = false) WebHookPagamento webHookPagamento,
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
            MercadoPago.SDK.setAccessToken(accessToken);

            String tipoRetorno = webHookPagamento.getType();
            String idPagamento = webHookPagamento.getData().getId();
            Payment pagamento = Payment.findById(idPagamento);
            String saque = pagamento.getExternalReference();
            String status = pagamento.getStatus().toString();
            String statusDetail = pagamento.getStatusDetail();

            System.out.println(saque);

            if (statusDetail.contains("refunded"))
                status = "refunded";

            return aluguelRepository.salvaRetornoPagamentoLocador(saque,idPagamento,tipoRetorno,status, usuario);
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
    @GetMapping("/saque-locador")
    @ResponseStatus(HttpStatus.OK)
    public Boolean efetuaSaqueLocador(){
        try{
            String idLocador = getIdUsuario();
            Optional<List<RetornoSaqueLocador>> produtos = Optional.ofNullable(aluguelRepository.retornaAlugueisSaque(idLocador));
            if(!produtos.isPresent()){
                throw new NotFoundException("14");
            }

            String id_saque = produtos.get().get(0).getId_saque();
            float valotTotal = 0.00f;
            ArrayList<Item> listaProdutos = new ArrayList<>();
            /*MercadoPago.SDK.setAccessToken(accessToken);

            Gson json = new Gson();

            Payer pagador = new Payer().setEmail("fabiriciots99@gmail.com");
                    //.setType(Payer.type.registered);
            Payment pagamento = new Payment()
                    .setPayer(pagador)
                    .setExternalReference(id_saque)
                    .setPaymentMethodId("account_money")
                    .setNotificationUrl(backendUrl.concat("/retorno-pagamento-locador"))
                    .setCapture(true)
                    .setBinaryMode(false)
                    .setStatementDescriptor("Saque aluGo");
            */
            for (RetornoSaqueLocador p : produtos.get()){/*
                Item produto = new Item()
                        .setId(p.getId_produto())
                        .setTitle(p.getNome_produto())
                        .setUnitPrice((float)(double) p.getValor())
                        .setDescription(p.getDescricao_curta())
                        .setQuantity(1);
                listaProdutos.add(produto);*/
                valotTotal += (float)(double) p.getValor();
            }/*
            pagamento.setAdditionalInfo(new AdditionalInfo().setItems(listaProdutos));
            pagamento.setTransactionAmount(valotTotal);
            System.out.println(pagamento.getExternalReference());
            System.out.println(pagamento.getStatus());
            System.out.println(pagamento.getStatusDetail());
            System.out.println(pagamento.getId());
            //System.out.println(pagamento.getExternalReference());
            Payment retornoMp = pagamento.save();
            System.out.println(retornoMp.getExternalReference());
            System.out.println(retornoMp.getStatus());
            System.out.println(retornoMp.getStatusDetail());
            System.out.println(retornoMp.getId());
            */
            return aluguelRepository.efetuaSaque(idLocador,((double) valotTotal),id_saque);
        }
        catch(Exception e) {
            String className = this.getClass().getSimpleName();
            String methodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String endpoint = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
            String user = SecurityContextHolder.getContext().getAuthentication().getName().split("\\|")[0];
            logRepository.gravaLogBackend(className, methodName, endpoint, user, (e.getMessage()==null) ? "" : e.getMessage(), Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    @ApiOperation(value = "Efetua estorno do pagamento")
    @PostMapping("/estorno")
    @ResponseStatus(HttpStatus.OK)
    public Boolean estornoPagmenento(@RequestParam("id_pagamento")String id_pagamento, @RequestParam(value = "valor") double valor, @RequestParam Double retencao){
        try {
            MercadoPago.SDK.setAccessToken(accessToken);
            Payment pagamento = Payment.findById(id_pagamento);
            if(retencao == 1.00){
                pagamento.refund();
            }
            else {
                pagamento.refund((float) valor);
            }
            return true;
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
