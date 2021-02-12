package com.onlycoders.backendalugo.model.entity.email.templatesEmails;

import java.io.*;

public class TemplateEmails {

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
        String mailBody = leTemplate("LocatarioConfirmacaoPagamento.html");
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

    public String notificaLocadorAluguel(String usuarioNome, String locatarioNome, String nomeProduto, String periodo, String valor, String celularLocatario) throws IOException {
        String mailBody = leTemplate("DonoSolicitacaoAluguel.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome)
                .replace("[[locatarioNome]]",locatarioNome)
                .replace("[[nomeProduto]]", nomeProduto)
                .replace("[[periodo]]",periodo)
                .replace("[[valorAluguel]]",valor)
                .replace("[[celularLocatario]]",celularLocatario);
    }

    public String notificaLocatarioAluguelLocal(String usuarioNome, String locatarioNome, String nomeProduto, String periodo, String valor, String celularLocatario) throws IOException {
        String mailBody = leTemplate("LocatarioPreenchaLocal.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome)
                .replace("[[locatarioNome]]",locatarioNome)
                .replace("[[nomeProduto]]", nomeProduto)
                .replace("[[periodo]]",periodo)
                .replace("[[valorAluguel]]",valor)
                .replace("[[celularLocatario]]",celularLocatario);
    }

    public String confirmaCadastro(String usuarioNome, String urlCadastro) throws IOException {
        String mailBody = leTemplate("UsuarioCadastro.html");
        return mailBody.replace("[[usuarioNome]]", usuarioNome)
                .replace("[[urlCadastro]]",urlCadastro);
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
