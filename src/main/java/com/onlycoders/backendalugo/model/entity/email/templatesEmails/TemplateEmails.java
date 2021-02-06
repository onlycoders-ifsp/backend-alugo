package com.onlycoders.backendalugo.model.entity.email.templatesEmails;

import java.io.*;
import java.nio.file.Paths;

public class TemplateEmails {

    public String cadastroUsuario(String usuarioNome, String verificationURL) throws IOException {
        String mailBody = leTemplate("Cadastro de produto.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome).replace("[[verificationURL]]", verificationURL);
    }

    public String cadastroProduto(String usuarioNome, String produtoNome) throws IOException {
        String mailBody = leTemplate("Cadastro de produto.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome).replace("[[nomeProduto]]", produtoNome);
    }

    public String produtoRejeitado(String usuarioNome, String produtoNome, String obs) throws IOException {
        String mailBody = leTemplate("Rejeição de produto.html");
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
        String mailBody = leTemplate("Aprovação de produto.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome).replace("[[nomeProduto]]", produtoNome);
    }

    public String aluguelEfetuado(String usuarioNome, String produtoNome) throws IOException {
        String mailBody = leTemplate("Usuário Inativado.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome).replace("[[nomeProduto]]", produtoNome);
    }

    public String aceiteAluguelDono(String usuarioNome, String produtoNome) throws IOException {
        String mailBody = leTemplate("DONO Aceite de Local e Hora.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome).replace("[[nomeProduto]]", produtoNome);
    }

    public String pagamentoAluguelDono(String usuarioNome, String produtoNome) throws IOException {
        String mailBody = leTemplate("DONO Pagamento Recebido.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome).replace("[[nomeProduto]]", produtoNome);
    }

    public String pagamentoAluguelLocatario(String usuarioNome, String produtoNome) throws IOException {
        String mailBody = leTemplate("LOCATÁRIO Confirmação de Pagamento.html");
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
        String mailBody = leTemplate("DONO Lembrete Entrega.html");
        return mailBody.replace("[[usuarioNome]]", locadorNome)
                .replace("[[produtoNome]]", produtoNome)
                .replace("[[locatarioNome]]", locatarioNome);
    }

    public String notificaAluguelLocatarioInicio(String locatarioNome, String produtoNome, String locadorNome) throws IOException {
        String mailBody = leTemplate("LOCATÁRIO Lembrete de ir buscar produto.html");
        return mailBody.replace("[[usuarioNome]]", locatarioNome)
                .replace("[[produtoNome]]", produtoNome)
                .replace("[[locadorNome]]", locadorNome);
    }

    public String notificaAluguelLocatarioFim(String locatarioNome, String produtoNome, String locadorNome) throws IOException {
        String mailBody = leTemplate("LOCATÁRIO Lembrete Devolução.html");
        return mailBody.replace("[[usuarioNome]]", locatarioNome)
                .replace("[[produtoNome]]", produtoNome)
                .replace("[[locadorNome]]", locadorNome);
    }

    public String notificaAluguelLocadorFim(String locadorNome, String produtoNome, String locatarioNome) throws IOException {
        String mailBody = leTemplate("DONO Lembrete Devolução.html");
        return mailBody.replace("[[usuarioNome]]", locadorNome)
                .replace("[[produtoNome]]", produtoNome)
                .replace("[[locatarioNome]]", locatarioNome);
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