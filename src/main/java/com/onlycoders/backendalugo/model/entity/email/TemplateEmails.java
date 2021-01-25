package com.onlycoders.backendalugo.model.entity.email;

public class TemplateEmails {
    String usuario = "Olá [[usuarioNome]],";
    String equipe = "<br>Equipe aluGo.";

    public String cadastroProduto(String usuarioNome, String produtoNome) {
        String mailBody =  usuario +
                "<br>Recebemos o cadastro do produto [[nomeProduto]] " +
                "Seu produto irá passar por uma análise, para aprovação ou não da publicação na plataforma." +
                "<br>Iremos te atualizar sobre qualquer atualização." +
                equipe.replace("[[usuarioNome]]",usuarioNome)
                      .replace("[[nomeProduto]]",produtoNome);
        return mailBody.replace("[[usuarioNome]]",usuarioNome).replace("[[nomeProduto]]",produtoNome);
    }

    public String produtoRejeitado(String usuarioNome, String produtoNome, String obs) {
        String mailBody = usuario +
                "<br>O cadastro do produto, [[produtoNome]] foi rejeitado por um de nossos moderadores, com a seguinte observação:" +
                "<br>[[observacao]]." +
                "<br>Prossiga com um novo cadastro e atente-se com as obsrvações." +
                equipe;
        return mailBody.replace("[[usuarioNome]]",usuarioNome)
                .replace("[[nomeProduto]]",produtoNome).replace("[[observacao]]",obs);
    }

    public String produtoAceito(String usuarioNome, String produtoNome) {
        String mailBody = usuario +
                "<br>Seu produto [[produtoNome]] foi aprovado e já está disponivel para aluguel." +
                " Fique de olho na sua caixa de email para os alugueis." +
                equipe;
        return mailBody.replace("[[usuarioNome]]",usuarioNome).replace("[[nomeProduto]]",produtoNome);
    }

    public String aluguelEfetuado(String usuarioNome, String produtoNome) {
        String mailBody = usuario +
                "<br>Recebemos sua solicitação de alugule do produto [[produtoNome]]." +
                "<br>Aguarde a confirmação do dono do produto para prosseguir com o pagamento." +
                equipe;
        return mailBody.replace("[[usuarioNome]]",usuarioNome).replace("[[nomeProduto]]",produtoNome);
    }

    public String confimaAluguel(String usuarioNome, String produtoNome) {
        String mailBody = usuario +
                "<br>Recebemos uma solicitação de aluguel do produto [[produtoNome]]." +
                "<br>Siga o link abaixo para confirmar o aluguel." +
                equipe;
        return mailBody.replace("[[usuarioNome]]",usuarioNome).replace("[[nomeProduto]]",produtoNome);
    }

    public String confirmaçãoAluguel(String usuarioNome, String produtoNome) {
        String mailBody = usuario +
                "<br>Seu produto [[produtoNome]] foi agendado para aluguel." +
                equipe;
        return mailBody.replace("[[usuarioNome]]",usuarioNome).replace("[[nomeProduto]]",produtoNome);
    }

    public String notificaAluguelLocador(String locadorNome, String produtoNome, String locatarioNome){
        String mailBody = usuario +
                "<br>Faltam duas horas para a entrega de seu produto, [[produtoNome]]," +
                " para [[locatarioNome]]."
                + equipe;
        return mailBody.replace("[[usuarioNome]]",locadorNome)
                .replace("[[produtoNome]]",produtoNome)
                .replace("[[locatarioNome]]", locatarioNome);
    }
    public String notificaAluguelLocatario(String locatarioNome, String produtoNome, String locadorNome){
        String mailBody = usuario +
                "<br>Faltam duas horas para a retirada do produto, [[produtoNome]]," +
                " do locador [[locadorNome]]."
                + equipe;
        return mailBody.replace("[[usuarioNome]]",locatarioNome)
                .replace("[[produtoNome]]",produtoNome)
                .replace("[[locadorNome]]",locadorNome);
    }
}
