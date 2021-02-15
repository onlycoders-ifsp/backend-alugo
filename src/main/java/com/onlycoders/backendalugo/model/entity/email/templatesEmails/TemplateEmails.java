package com.onlycoders.backendalugo.model.entity.email.templatesEmails;

import java.io.*;

public class TemplateEmails {

    public String cadastroProduto(String usuarioNome, String produtoNome) throws IOException {
        String mailBody = leTemplate("CadastroProduto.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome).replace("[[nomeProduto]]", produtoNome);
    }

    public String produtoRejeitado(String usuarioNome, String produtoNome, String obs) throws IOException {
        String mailBody = leTemplate("RejeicaoProduto.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome)
                .replace("[[nomeProduto]]", produtoNome)
                .replace("[[observacao]]", obs);
    }

    public String usuarioInativado(String usuarioNome, String obs) throws IOException {
        String mailBody = leTemplate("UsuarioInativado.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome)
                .replace("[[observacao]]", obs);
    }

    public String usuarioAtivado(String usuarioNome) throws IOException {
        String mailBody = leTemplate("UsuarioAtivado.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome);
    }

    public String produtoAceito(String usuarioNome, String produtoNome) throws IOException {
        String mailBody = leTemplate("AprovacaoProduto.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome).replace("[[nomeProduto]]", produtoNome);
    }

    public String pagamentoAluguelDono(String usuarioNome, String nomeLocatario, String emailLocatario, String celularLocatario) throws IOException {
        String mailBody = leTemplate("DonoPagamentoRecebido.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome)
                .replace("[[nomeLocatario]]", nomeLocatario)
                .replace("[[emailLocatario]]",emailLocatario)
                .replace("[[celularLocatario]]", celularLocatario);
    }

    public String pagamentoAluguelLocatario(String usuarioNome, String nomeLocador, String emailLocador,String celularLocador) throws IOException {
        String mailBody = leTemplate("LocatarioConfirmacaoPagamento.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome)
                .replace("[[nomeLocador]]", nomeLocador)
                .replace("[[emailLocador]]", emailLocador)
                .replace("[[celularLocador]]", celularLocador);
    }

    /*
    public String confirmacaoAluguelLocador(String usuarioNome, String produtoNome) throws IOException {
        String mailBody = Files.toString(new File("Usuário Inativado.html"), StandardCharsets.UTF_8);
        return mailBody.replace("[[usuarioNome]]",usuarioNome).replace("[[nomeProduto]]",produtoNome);
    }

    public String confirmacaoAluguelLocatario(String usuarioNome, String produtoNome) throws IOException {
        String mailBody = Files.toString(new File("Usuário Inativado.html"), StandardCharsets.UTF_8);
        return mailBody.replace("[[usuarioNome]]",usuarioNome).replace("[[nomeProduto]]",produtoNome);
    }
*/
    public String notificaAluguelLocadorInicio(String locadorNome, String localEntrega, String bairroEntrega, String cepEntrega, String detalheEntrega, String dataEntrega) throws IOException {
        String mailBody = leTemplate("DonoLembreteEntrega.html");
        return mailBody.replace("[[usuarioNome]]", locadorNome)
                .replace("[[localEntrega]]", localEntrega)
                .replace("[[bairroEntrega]]",bairroEntrega)
                .replace("[[cepEntrega]]", cepEntrega)
                .replace("[[detalheEntrega]]",detalheEntrega)
                .replace("[[dataEntrega]]",dataEntrega);
    }

    public String notificaAluguelLocatarioInicio(String locatarioNome, String localEntrega, String bairroEntrega, String cepEntrega, String detalheEntrega, String dataEntrega) throws IOException {
        String mailBody = leTemplate("LocatarioLembreteBuscarProduto.html");
        return mailBody.replace("[[usuarioNome]]", locatarioNome)
                .replace("[[localEntrega]]", localEntrega)
                .replace("[[bairroEntrega]]",bairroEntrega)
                .replace("[[cepEntrega]]", cepEntrega)
                .replace("[[detalheEntrega]]",detalheEntrega)
                .replace("[[dataEntrega]]",dataEntrega);
    }

    public String notificaAluguelLocatarioFim(String locatarioNome, String localDevolucao, String bairroDevolucao, String cepDevolucao, String detalheDevolucao, String dataDevolucao) throws IOException {
        String mailBody = leTemplate("LocatarioLembreteDevolucao.html");
        return mailBody.replace("[[usuarioNome]]", locatarioNome)
                .replace("[[localDevolucao]]", localDevolucao)
                .replace("[[bairroDevolucao]]",bairroDevolucao)
                .replace("[[cepDevolucao]]", cepDevolucao)
                .replace("[[detalheDevolucao]]",detalheDevolucao)
                .replace("[[dataDevolucao]]",dataDevolucao);
    }

    public String notificaAluguelLocadorFim(String locadorNome, String localDevolucao, String bairroDevolucao, String cepDevolucao, String detalheDevolucao, String dataDevolucao) throws IOException {
        String mailBody = leTemplate("DonoLembreteDevolucao.html");
        return mailBody.replace("[[usuarioNome]]", locadorNome)
                .replace("[[localDevolucao]]", localDevolucao)
                .replace("[[bairroDevolucao]]",bairroDevolucao)
                .replace("[[cepDevolucao]]", cepDevolucao)
                .replace("[[detalheDevolucao]]",detalheDevolucao)
                .replace("[[dataDevolucao]]",dataDevolucao);
    }

    public String informaAceiteLocalLocador(String usuarioNome, String locadorNome, String nomeProduto, String periodo, String valor) throws IOException {
        String mailBody = leTemplate("LocatarioAluguelAceitoPagar.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome)
                .replace("[[locadorNome]]",locadorNome)
                .replace("[[nomeProduto]]", nomeProduto)
                .replace("[[periodo]]",periodo)
                .replace("[[valorAluguel]]",valor);
    }

    public String informaLocalLocatario(String usuarioNome, String localEntrega, String bairroEntrega, String cepEntrega, String detalheEntrega,
                                        String dataEntrega,String localDevoluca,String bairroDevolucao, String cepDevolucao,
                                        String detalheDevolucao, String dataDevolucao, String locatarioNome, String produtoNome,
                                        String periodo, String valor) throws IOException {
        String mailBody = leTemplate("DonoAceiteLocal.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome)
                .replace("[[localEntrega]]",localEntrega)
                .replace("[[bairroEntrega]]", bairroEntrega)
                .replace("[[cepEntrega]]",cepEntrega)
                .replace("[[detalheEntrega]]",detalheEntrega)
                .replace("[[dataEntrega]]",dataEntrega)
                .replace("[[localDevoluca]]",localDevoluca)
                .replace("[[bairroDevolucao]]", bairroDevolucao)
                .replace("[[cepDevolucao]]",cepDevolucao)
                .replace("[[detalheDevolucao]]",detalheDevolucao)
                .replace("[[dataDevolucao]]",dataDevolucao)
                .replace("[[locatarioNome]]",locatarioNome)
                .replace("[[produtoNome]]",produtoNome)
                .replace("[[periodo]]",periodo)
                .replace("[[valor]]",valor.replace(".",","));
    }

    public String notificaLocadorAluguel(String usuarioNome, String locatarioNome, String nomeProduto, String periodo, String valor) throws IOException {
        String mailBody = leTemplate("DonoSolicitacaoAluguel.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome)
                .replace("[[locatarioNome]]",locatarioNome)
                .replace("[[nomeProduto]]", nomeProduto)
                .replace("[[periodo]]",periodo)
                .replace("[[valorAluguel]]",valor);
    }

    public String notificaLocatarioAluguel(String usuarioNome, String locadorNome, String nomeProduto, String periodo, String valor) throws IOException {
        String mailBody = leTemplate("LocatarioSolicitacaoAluguel.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome)
                .replace("[[locadorNome]]",locadorNome)
                .replace("[[nomeProduto]]", nomeProduto)
                .replace("[[periodo]]",periodo)
                .replace("[[valorAluguel]]",valor);
    }

    public String notificaLocatarioAluguelLocal(String usuarioNome, String locadorNome, String nomeProduto, String periodo, String valor) throws IOException {
        String mailBody = leTemplate("LocatarioPreenchaLocal.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome)
                .replace("[[locadorNome]]",locadorNome)
                .replace("[[nomeProduto]]", nomeProduto)
                .replace("[[periodo]]",periodo)
                .replace("[[valorAluguel]]",valor);
    }

    public String confirmaCadastro(String usuarioNome, String urlCadastro) throws IOException {
        String mailBody = leTemplate("UsuarioCadastro.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome)
                .replace("[[urlCadastro]]",urlCadastro);
    }

    public String notificaChkEntregaLocatario(String locatarioNome) throws IOException {
        String mailBody = leTemplate("LocatarioChecklistInicial.html");
        return mailBody.replace("[[usuarioNome]]", locatarioNome);
    }

    public String notificaChkDevolucaoLocatario(String locatarioNome) throws IOException {
        String mailBody = leTemplate("LocatarioChecklistFinal.html");
        return mailBody.replace("[[usuarioNome]]", locatarioNome);
    }

    public String notificaChkEntregaLocador(String locadorNome) throws IOException {
        String mailBody = leTemplate("DonoChecklistInicial.html");
        return mailBody.replace("[[usuarioNome]]", locadorNome);
    }

    public String notificaChkDevolucaoLocador(String locadorNome) throws IOException {
        String mailBody = leTemplate("DonoChecklistFinal.html");
        return mailBody.replace("[[usuarioNome]]", locadorNome);
    }

    public String notificaLocatarioConfirmaLocal(String usuarioNome, String locadorNome, String nomeProduto, String periodo, String valor) throws IOException {
        String mailBody = leTemplate("LocatarioLocalConfirmado.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome)
                .replace("[[locadorNome]]",locadorNome)
                .replace("[[nomeProduto]]", nomeProduto)
                .replace("[[periodo]]",periodo)
                .replace("[[valorAluguel]]",valor);
    }

    public String notificaAvalicaoLocador(String locadorNome) throws IOException {
        String mailBody = leTemplate("DonoAvaliacaoLocatario.html");
        return mailBody.replace("[[usuarioNome]]", locadorNome);
    }

    public String notificaAvaliacaoLocatario(String locatarioNome) throws IOException {
        String mailBody = leTemplate("LocatarioAvaliacoes.html");
        return mailBody.replace("[[usuarioNome]]", locatarioNome);
    }

    String leTemplate(String arquivo) throws IOException {
        //String path = Paths.get(arquivo).toUri().toString().replace("file:///","").replace(arquivo,"");
        StringBuilder contentBuilder = new StringBuilder();
        BufferedReader in = new BufferedReader(new FileReader(System.getProperty("user.dir") +
                File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "com" +
                File.separator + "onlycoders" + File.separator + "backendalugo" + File.separator +
                "model" + File.separator + "entity" + File.separator + "email" + File.separator +
                "templatesEmails" + File.separator + arquivo));
        //BufferedReader in = new BufferedReader(new FileReader(arquivo));
        String str;
        while ((str = in.readLine()) != null) {
            contentBuilder.append(str);
        }
        in.close();
        return contentBuilder.toString();
    }
}
