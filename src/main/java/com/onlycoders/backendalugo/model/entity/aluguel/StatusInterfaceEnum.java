package com.onlycoders.backendalugo.model.entity.aluguel;

public class StatusInterfaceEnum {
    public enum StatusAluguel{
        AGENDADO(1),
        AGUARDANDO_ACEITE_DO_ALUGUEL(13),
        AGUARDANDO_ACEITE_DO_DONO(9),
        AGUARDANDO_DEVOLUCAO_DO_PRODUTO(10),
        AGUARDANDO_ENTREGA_DO_PRODUTO(12),
        AGUARDANDO_OK_DO_LOCATARIO_CHECLILIST_ENTREGA(14),
        AGUARDANDO_OK_LOCATARIO_CHECKLIST_DEVOLUCAO(15),
        AGUARDANDO_PAGAMENTO(7),
        CANCELADO_PELO_LOCADOR(5),
        CANCELADO_PELO_LOCATARIO(6),
        CANCELAMENTO_AUTOMATICO_DONO_NAO_CONFIRMOU_O_ALUGUEL_DENTRO_DO_PRAZO(16),
        CANCELAMENTO_AUTOMATICO_DONO_NAO_CONFIRMOU_O_ENCONTRO_DENTRO_DO_PRAZO(17),
        CANCELAMENTO_AUTOMATICO_DONO_NAO_REALIZOU_O_CHECKLIST_DE_ENTREGA_DENTRO_DO_PRAZO(19),
        CANCELAMENTO_AUTOMATICO_LOCATARIO_NAO_EFETUOU_PAGAMENTO_NO_PRAZO_ESTIPULADO(8),
        CANCELAMENTO_AUTOMATICO_LOCATARIO_NAO_PREENCHEU_AS_INFORMACOES_DE_ENCONTRO_DENTRO_DO_PRAZO(18),
        EM_ANDAMENTO(2),
        FINALIZADO_COM_OCORRENCIA(4),
        FINALIZADO_SEM_OCORRRENCIA(3),
        PAGAMENTO_CONFIRMADO(11);

        private Integer cod_status;

        StatusAluguel(Integer status){
            this.cod_status = status;
        }

        public Integer getCod_status(){
            return cod_status;
        }

    }
}
