package com.onlycoders.backendalugo.model.entity.email;

public class TemplateEmails {
    String usuario = "Olá [[usuarioNome]],<br>";
    String equipe = "<br><br>Atenciosamente,<br>Equipe aluGo.";

    public String cadastroUsuario(String usuarioNome, String verificationURL) {
        String mailBody =  usuario +
                "<br>Obrigado por se cadastrar em nossa plataforma!" +
                "<br>Para confirmar o cadastro, clique no link abaixo. " +
                "<br>[[verificationURL]]" +
                equipe;
        return mailBody.replace("[[usuarioNome]]",usuarioNome).replace("[[verificationURL]]",verificationURL);
    }

    public String cadastroProduto(String usuarioNome, String produtoNome) {
        String mailBody =  usuario +
                "<br>Recebemos o cadastro do produto [[nomeProduto]] " +
                "Seu produto irá passar por uma análise, para aprovação ou não da publicação na plataforma." +
                "<br>Iremos te atualizar sobre qualquer atualização." +
                equipe;
        return mailBody.replace("[[usuarioNome]]",usuarioNome).replace("[[nomeProduto]]",produtoNome);
    }

    public String produtoRejeitado(String usuarioNome, String produtoNome, String obs) {
        String mailBody = usuario +
                "<br>O cadastro do produto, [[produtoNome]] foi rejeitado por um de nossos moderadores, pelo seguinte motivo:" +
                "<br>[[observacao]]." +
                "<br>Prossiga com um novo cadastro e atente-se com as observações." +
                equipe;
        return mailBody.replace("[[usuarioNome]]",usuarioNome)
                .replace("[[nomeProduto]]",produtoNome)
                .replace("[[observacao]]",obs);
    }

    public String usuarioInativado(String usuarioNome, String obs) {
        String mailBody = usuario +
                "<br>Você desrespeitou nossos termos de serviço e seu perfil foi bloqueado para acesso em nossa plataforma com o seguinte motivo:" +
                "<br>[[observacao]]" +
                "<br>Caso queira contestar o bloqueio, entre em contato com nosso suporte." +
                equipe;
        return mailBody.replace("[[usuarioNome]]",usuarioNome)
                .replace("[[observacao]]",obs);
    }

    public String usuarioAtivado(String usuarioNome) {
        String mailBody = usuario +
                "<br>Seu perfil foi ativado para acesso em nossa plataforma." +
                "<br>Você já pode efetuar aluguel e cadastrar produtos normalmente." +
                equipe;
        return mailBody.replace("[[usuarioNome]]",usuarioNome);
    }

    public String produtoAceito(String usuarioNome, String produtoNome) {
        String mailBody = usuario +
                "<br>Seu produto [[produtoNome]] foi aprovado e já está disponivel para aluguel." +
                " Fique de olho na sua caixa de email para os aluguéis." +
                equipe;
        return mailBody.replace("[[usuarioNome]]",usuarioNome).replace("[[nomeProduto]]",produtoNome);
    }

    public String aluguelEfetuado(String usuarioNome, String produtoNome) {
        String mailBody = usuario +
                "<br>Recebemos sua solicitação de aluguel do produto [[produtoNome]]." +
                "<br>Aguarde a confirmação do dono do produto para prosseguir com o pagamento." +
                equipe;
        return mailBody.replace("[[usuarioNome]]",usuarioNome).replace("[[nomeProduto]]",produtoNome);
    }

    public String aceiteAluguelDono(String usuarioNome, String produtoNome) {
        String mailBody = usuario +
                "<br>Recebemos uma solicitação de aluguel do produto [[produtoNome]]." +
                "<br>Siga o link abaixo para confirmar o aluguel e liberar o pagamento ao locatario." +
                equipe;
        return mailBody.replace("[[usuarioNome]]",usuarioNome).replace("[[nomeProduto]]",produtoNome);
    }

    public String pagamentoAluguel(String usuarioNome, String produtoNome) {
        String mailBody = usuario +
                "<br>O locador confirmou o agendamento do produto [[produtoNome]]." +
                "<br>Siga o link abaixo para efetuar o pagamento." +
                "<br>Aluguel será cancelado, se o pagamento não ocorrer em até duas horas após esta notificação." +
                equipe;
        return mailBody.replace("[[usuarioNome]]",usuarioNome).replace("[[nomeProduto]]",produtoNome);
    }

    public String confirmacaoAluguelLocador(String usuarioNome, String produtoNome) {
        String mailBody = usuario +
                "<br>Recebemos o pagamento de aluguel do produto [[produtoNome]]!" +
                "<br>Seu produto está alugado, aguarde até a entrega do produto." +
                equipe;
        return mailBody.replace("[[usuarioNome]]",usuarioNome).replace("[[nomeProduto]]",produtoNome);
    }

    public String confirmacaoAluguelLocatario(String usuarioNome, String produtoNome) {
        String mailBody = usuario +
                "<br>Recebemos o pagamento de aluguel do produto [[produtoNome]]!" +
                "<br>Seu aluguel está agendado, aguarde até a entrega do produto." +
                equipe;
        return mailBody.replace("[[usuarioNome]]",usuarioNome).replace("[[nomeProduto]]",produtoNome);
    }

    public String notificaAluguelLocadorInicio(String locadorNome, String produtoNome, String locatarioNome){
        String mailBody = usuario +
                "<br>Faltam duas horas para a entrega de seu produto, [[produtoNome]]," +
                " para [[locatarioNome]]."
                + equipe;
        return mailBody.replace("[[usuarioNome]]",locadorNome)
                .replace("[[produtoNome]]",produtoNome)
                .replace("[[locatarioNome]]", locatarioNome);
    }

    public String notificaAluguelLocatarioInicio(String locatarioNome, String produtoNome, String locadorNome){
        String mailBody = usuario +
                "<br>Faltam duas horas para a retirada do produto, [[produtoNome]]," +
                " do locador [[locadorNome]]."
                + equipe;
        return mailBody.replace("[[usuarioNome]]",locatarioNome)
                .replace("[[produtoNome]]",produtoNome)
                .replace("[[locadorNome]]",locadorNome);
    }

    public String notificaAluguelLocatarioFim(String locatarioNome, String produtoNome, String locadorNome){
        String mailBody = usuario +
                "<br>Faltam duas horas para a devolução do produto, [[produtoNome]]," +
                " do locador [[locadorNome]]."
                + equipe;
        return mailBody.replace("[[usuarioNome]]",locatarioNome)
                .replace("[[produtoNome]]",produtoNome)
                .replace("[[locadorNome]]",locadorNome);
    }

    public String notificaAluguelLocadorFim(String locadorNome, String produtoNome, String locatarioNome){
        String mailBody = usuario +
                "<br>Faltam duas horas para a devoluçao do seu produto, [[produtoNome]]," +
                " pelo [[locatarioNome]]."
                + equipe;
        return mailBody.replace("[[usuarioNome]]",locadorNome)
                .replace("[[produtoNome]]",produtoNome)
                .replace("[[locatarioNome]]", locatarioNome);
    }
}
