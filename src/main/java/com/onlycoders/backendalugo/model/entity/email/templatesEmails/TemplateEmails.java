package com.onlycoders.backendalugo.model.entity.email.templatesEmails;

import java.io.*;
import java.nio.file.Paths;

public class TemplateEmails {

    public String cadastroUsuario(String usuarioNome, String verificationURL) throws IOException {
        String mailBody = leTemplate("CadastroProduto.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome).replace("[[verificationURL]]", verificationURL);
    }

    public String cadastroProduto(String usuarioNome, String produtoNome) throws IOException {
        String mailBody = leTemplate("CadastrProduto.html");
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

    public String aluguelEfetuado(String usuarioNome, String produtoNome) throws IOException {
        String mailBody = leTemplate("UsuarioInativado.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome).replace("[[nomeProduto]]", produtoNome);
    }

    public String aceiteAluguelDono(String usuarioNome, String produtoNome) throws IOException {
        String mailBody = leTemplate("DonoAceiteLocal.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome).replace("[[nomeProduto]]", produtoNome);
    }

    public String pagamentoAluguelDono(String usuarioNome, String produtoNome) throws IOException {
        String mailBody = leTemplate("DonoPagamentoRecebido.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome).replace("[[nomeProduto]]", produtoNome);
    }

    public String pagamentoAluguelLocatario(String usuarioNome, String produtoNome) throws IOException {
        String mailBody = leTemplate("LocatarioConfirmacaoPPagamento.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome).replace("[[nomeProduto]]", produtoNome);
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
    public String notificaAluguelLocadorInicio(String locadorNome, String produtoNome, String locatarioNome) throws IOException {
        String mailBody = leTemplate("DonoLembreteEntrega.html");
        return mailBody.replace("[[usuarioNome]]", locadorNome)
                .replace("[[produtoNome]]", produtoNome)
                .replace("[[locatarioNome]]", locatarioNome);
    }

    public String notificaAluguelLocatarioInicio(String locatarioNome, String produtoNome, String locadorNome) throws IOException {
        String mailBody = leTemplate("LocatarioLembreteBuscarProduto.html");
        return mailBody.replace("[[usuarioNome]]", locatarioNome)
                .replace("[[produtoNome]]", produtoNome)
                .replace("[[locadorNome]]", locadorNome);
    }

    public String notificaAluguelLocatarioFim(String locatarioNome, String produtoNome, String locadorNome) throws IOException {
        String mailBody = leTemplate("LocatarioLembreteDevolução.html");
        return mailBody.replace("[[usuarioNome]]", locatarioNome)
                .replace("[[produtoNome]]", produtoNome)
                .replace("[[locadorNome]]", locadorNome);
    }

    public String notificaAluguelLocadorFim(String locadorNome, String produtoNome, String locatarioNome) throws IOException {
        String mailBody = leTemplate("DonoLembreteDevolucao.html");
        return mailBody.replace("[[usuarioNome]]", locadorNome)
                .replace("[[produtoNome]]", produtoNome)
                .replace("[[locatarioNome]]", locatarioNome);
    }

    public String aceiteLocalLocatario(String usuarioNome, String locadorNome, String nomeProduto, String periodo, String valor) throws IOException {
        String mailBody = leTemplate("CadastroProduto.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome)
                .replace("[[locadorNome]]",locadorNome)
                .replace("[[nomeProduto]]", nomeProduto)
                .replace("[[periodo]]",periodo)
                .replace("[[valorAluguel]]",valor);
    }

    String leTemplate(String arquivo) throws IOException {
        String path = Paths.get(arquivo).toUri().toString().replace("file:///","").replace(arquivo,"");
        StringBuilder contentBuilder = new StringBuilder();
        BufferedReader in = new BufferedReader(new FileReader(path + "src/main/java/com/onlycoders/backendalugo/model/entity/email/templatesEmails/" + arquivo));
        String str;
        while ((str = in.readLine()) != null) {
            contentBuilder.append(str);
        }
        in.close();
        return contentBuilder.toString();
    }
}
