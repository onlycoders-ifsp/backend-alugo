package com.onlycoders.backendalugo.model.repository;
import com.onlycoders.backendalugo.model.entity.aluguel.template.*;
import com.onlycoders.backendalugo.model.entity.email.RetornoAlugueisNotificacao;
import com.onlycoders.backendalugo.model.entity.produto.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AluguelRepository extends JpaRepository<Produto, Integer> {

    @Transactional
    @Query(value = "Select FN_EFETUA_ALUGUEL(:id_usuario, :id_produto, :data_inicio," +
            ":data_fim, :valor_aluguel,:user)",nativeQuery = true)
    String efetuaAluguel(@Param("id_usuario") String id_usuario,
                          @Param("id_produto") String id_produto,
                          @Param("data_inicio") String data_inicio,
                          @Param("data_fim") String data_fim,
                          @Param("valor_aluguel") Double valor_aluguel,
                          @Param("user") String user);


    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "Select *FROM FN_RETORNA_ALUGUEL(:id_locador,:id_locatario,:id_aluguel,:id_produto,:op,:user)" +
                   "AS T (ID_ALUGUEL TEXT, ID_PRODUTO TEXT, ID_LOCATARIO TEXT," +
                    "ID_LOCADOR TEXT, DATA_INICIO TEXT, DATA_FIM TEXT," +
                    "VALOR_ALUGUEL DOUBLE PRECISION, DATA_SAQUE TEXT, STATUS_ALUGUEL INT, URL_PAGAMENTO TEXT)",
            nativeQuery = true)
    List<RetornaAluguel> retornaAluguel(@Param("id_locador") String id_locador,
                                        @Param("id_locatario") String id_locatario,
                                        @Param("id_aluguel") String id_aluguel,
                                        @Param("id_produto") String id_produto,
                                        @Param("op") int op,
                                        @Param("user") String user);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "SELECT FN_VALIDA_ALUGUEL(:id_usuario, :id_produto, :inicio, :fim, :user)",
            nativeQuery = true)
    String validaALuguel(@Param("id_usuario")String id_usuario,
                         @Param("id_produto")String id_produto,
                         @Param("inicio")String inicio,
                         @Param("fim")String fim,
                         @Param("user") String user);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "Select *FROM FN_RETORNA_ALUGUEL_DETALHE(:id_produto,:user)" +
            "AS T (ID_PRODUTO TEXT, NOME_PRODUTO TEXT, FOTO_PRODUTO BYTEA, ID_LOCATARIO TEXT," +
            "NOME_LOCATARIO TEXT, FOTO_LOCATARIO BYTEA, DATA_INICIO TEXT, DATA_FIM TEXT," +
            "VALOR_ALUGUEL DOUBLE PRECISION, VALOR_GANHO DOUBLE PRECISION, DATA_DEVOLUCAO TEXT, STATUS_ALUGUEL INT)",
            nativeQuery = true)
    List<RetornaAluguelDetalhe> retornaAluguelDetalhe(@Param("id_produto") String id_produto,
                                                      @Param("user") String user);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "SELECT *FROM FN_RETORNA_ALUGUEIS_NOTIFICACAO_INICIO(:op,:usuario)" +
            "AS (IDALUGUEL TEXT,PRODUTONOME TEXT, LOCADORNOME TEXT," +
            "LOCADOREMAIL TEXT,LOCADORCELULAR TEXT, LOCATARIONOME TEXT," +
            "LOCATARIOEMAIL TEXT,LOCATARIOCELULAR TEXT);",nativeQuery = true)
    List<RetornoAlugueisNotificacao> retornaAlugueisNotificacaoInicio(@Param("op") Integer op,@Param("usuario") String usuario);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "SELECT *FROM FN_RETORNA_ALUGUEIS_NOTIFICACAO_FIM(:op,:usuario)" +
            "AS (IDALUGUEL TEXT,PRODUTONOME TEXT, LOCADORNOME TEXT," +
            "LOCADOREMAIL TEXT,LOCADORCELULAR TEXT, LOCATARIONOME TEXT," +
            "LOCATARIOEMAIL TEXT,LOCATARIOCELULAR TEXT);",nativeQuery = true)
    List<RetornoAlugueisNotificacao> retornaAlugueisNotificacaoFim(@Param("op") Integer op,@Param("usuario") String usuario);

    @Transactional()
    @Query(value = "SELECT FN_STATUS_ALUGUEL(:id_aluguel,:status,:usuario);",nativeQuery = true)
    Boolean alteraStatusAluguel(@Param("id_aluguel") String id_aluguel,
                                @Param("status") int status,
                                @Param("usuario") String usuario);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "SELECT *FROM FN_RETORNA_DADOS_LOCADOR_LOCATARIO(:id_aluguel,:usuario)" +
            "AS (IDALUGUEL TEXT,PRODUTONOME TEXT, LOCADORNOME TEXT," +
            "LOCADOREMAIL TEXT, LOCADORCELULAR TEXT, LOCATARIONOME TEXT," +
            "LOCATARIOEMAIL TEXT, LOCATARIOCELULAR TEXT);",nativeQuery = true)
    RetornoAlugueisNotificacao retornaDadosLocadorLocatario(@Param("id_aluguel") String id_aluguel,
                                                              @Param("usuario") String usuario);

    @Transactional
    @Query(value = "Select FN_INSERE_ALUGUEL_ENCONTRO(:id_aluguel, :cep_entrega, :logradouro_entrega," +
            ":bairro_entrega, :descricao_entrega,:data_entrega, :cep_devolucao, :logradouro_devolucao," +
            ":bairro_devolucao, :descricao_devolucao, :data_devolucao, :aceite_locador, :observacao_recusa, :id_usuario);",nativeQuery = true)
    Boolean insereAluguelEncontro(@Param("id_aluguel") String id_aluguel,
                                  @Param("cep_entrega") String cep_entrega,
                                  @Param("logradouro_entrega") String logradouro_entrega,
                                  @Param("bairro_entrega") String bairro_entrega,
                                  @Param("descricao_entrega") String descricao_entrega,
                                  @Param("data_entrega") String data_entrega,
                                  @Param("cep_devolucao") String cep_devolucao,
                                  @Param("logradouro_devolucao") String logradouro_devolucao,
                                  @Param("bairro_devolucao") String bairro_devolucao,
                                  @Param("descricao_devolucao") String descricao_devolucao,
                                  @Param("data_devolucao") String data_devolucao,
                                  @Param("aceite_locador") boolean aceite_locador,
                                  @Param("observacao_recusa") String observacao_recusa,
                                  @Param("id_usuario") String id_usuario);

    @Transactional()
    @Query(value = "SELECT FN_GRVAVA_CHK_DEVOLUCAO(:id_aluguel,:descricao,:usuario);",nativeQuery = true)
    Boolean gravaCheckListDevolucao(@Param("id_aluguel") String id_aluguel,
                                    @Param("descricao") String descricao,
                                    @Param("usuario") String usuario);

    @Transactional()
    @Query(value = "SELECT FN_GRVAVA_FOTO_CHK_DEVOLUCAO(:id_aluguel,:foto,:usuario);",nativeQuery = true)
    Boolean gravaFotoCheckListDevolucao(@Param("id_aluguel") String id_aluguel,
                                    @Param("foto") byte[] foto,
                                    @Param("usuario") String usuario);

    @Transactional()
    @Query(value = "SELECT FN_GRVAVA_CHK_ENTREGA(:id_aluguel,:descricao,:usuario);",nativeQuery = true)
    Boolean gravaCheckListEntrega(@Param("id_aluguel") String id_aluguel,
                                  @Param("descricao") String descricao,
                                  @Param("usuario") String usuario);

    @Transactional()
    @Query(value = "SELECT FN_GRVAVA_FOTO_CHK_ENTREGA(:id_aluguel,:foto,:usuario);",nativeQuery = true)
    Boolean gravaFotoCheckListEntrega(@Param("id_aluguel") String id_aluguel,
                                        @Param("foto") byte[] foto,
                                        @Param("usuario") String usuario);

    @Transactional()
    @Query(value = "SELECT FN_APROVA_REPROVA_CHK_DEVOLUCAO(:id_aluguel,:ok,:motivoRecusa,:usuario);",nativeQuery = true)
    Boolean aprovaReprovaCheckListDevolucao(@Param("id_aluguel") String id_aluguel,
                                              @Param("ok") Boolean ok,
                                              @Param("motivoRecusa") String motivoRecusa,
                                              @Param("usuario") String usuario);

    @Transactional()
    @Query(value = "SELECT FN_APROVA_REPROVA_CHK_ENTREGA(:id_aluguel,:ok, :motivoRecusa,:usuario);",nativeQuery = true)
    Boolean aprovaReprovaCheckListEntrega(@Param("id_aluguel") String id_aluguel,
                                          @Param("ok") Boolean ok,
                                          @Param("motivoRecusa") String motivoRecusa,
                                          @Param("usuario") String usuario);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "SELECT *FROM FN_RETORNA_CHK_ENTREGA(:id_aluguel,:usuario)" +
            "AS (ID_ALUGUEL TEXT, DESCRICAO TEXT," +
            "FOTO BYTEA, OK_LOCADOR BOOLEAN, MOTIVO_RECUSA TEXT);",nativeQuery = true)
    RetornaChecklist retornaCheckListEntrega(@Param("id_aluguel") String id_aluguel,
                                             @Param("usuario") String usuario);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "SELECT *FROM FN_RETORNA_CHK_DEVOLUCAO(:id_aluguel,:usuario)" +
            "AS (ID_ALUGUEL TEXT, DESCRICAO TEXT," +
            "FOTO BYTEA, OK_LOCADOR BOOLEAN, MOTIVO_RECUSA TEXT);",nativeQuery = true)
    RetornaChecklist retornaCheckListDevolucao(@Param("id_aluguel") String id_aluguel,
                                               @Param("usuario") String usuario);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "SELECT *FROM FN_RETORNA_ALUGUEL_ENCONTRO(:id_aluguel,:usuario)" +
            "AS (ID_ALUGUEL TEXT, CEP_ENTREGA TEXT, LOGRADOURO_ENTREGA TEXT, BAIRRO_ENTREGA TEXT," +
            "DESCRICAO_ENTREGA TEXT, DATA_ENTREGA TEXT, CEP_DEVOLUCAO TEXT, LOGRADOURO_DEVOLUCAO TEXT," +
            "BAIRRO_DEVOLUCAO TEXT, DESCRICAO_DEVOLUCAO TEXT, DATA_DEVOLUCAO TEXT, ACEITE_LOCADOR BOOLEAN," +
            "OBSERVACAO_RECUSA TEXT, PERIODO TEXT, VALOR TEXT);",nativeQuery = true)
    RetornaAluguelEncontro retornaAluguelEncontro(@Param("id_aluguel") String id_aluguel,
                                                   @Param("usuario") String usuario);

    @Transactional()
    @Query(value = "SELECT FN_SALVA_AVALIACAO(:id_aluguel,:comentario,:nota,:tipo,:usuario);",nativeQuery = true)
    Boolean salvaAvaliacao(@Param("id_aluguel") String id_aluguel,
                            @Param("comentario") String comentario,
                            @Param("nota") Double nota,
                            @Param("tipo") int tipo,
                           @Param("usuario") String usuario);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "SELECT *FROM FN_RETORNA_AVALIACAO_PRODUTO(:id_produto,:usuario)" +
            "AS (NOME_AVALIADOR TEXT, COMENTARIO TEXT, NOTA DECIMAL(2,1));",nativeQuery = true)
    List<RetornaAvaliacoes> retornaAvaliacaoProduto(@Param("id_produto") String id_aluguel,
                                                    @Param("usuario") String usuario);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "SELECT *FROM FN_RETORNA_AVALIACAO_LOCATARIO(:id_usuario,:usuario)" +
            "AS (NOME_AVALIADOR TEXT, COMENTARIO TEXT, NOTA DECIMAL(2,1));",nativeQuery = true)
    List<RetornaAvaliacoes> retornaAvaliacaoLocatario(@Param("id_usuario") String id_aluguel,
                                                   @Param("usuario") String usuario);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "SELECT *FROM FN_RETORNA_AVALIACAO_LOCADOR(:id_usuario,:usuario)" +
            "AS (NOME_AVALIADOR TEXT, COMENTARIO TEXT, NOTA DECIMAL(2,1));",nativeQuery = true)
    List<RetornaAvaliacoes> retornaAvaliacaoLocador(@Param("id_usuario") String id_aluguel,
                                                      @Param("usuario") String usuario);

    @Transactional()
    @Query(value = "SELECT FN_SALVA_URL_PAGAMENTO(:id_aluguel,:url_pagamento,:usuario);",nativeQuery = true)
    Boolean salvaUrlPagamento(@Param("id_aluguel") String id_aluguel,
                            @Param("url_pagamento") String url_pagamento,
                            @Param("usuario") String usuario);

    @Transactional()
    @Query(value = "SELECT FN_CONFIRMA_ENCONTRO(:id_aluguel,:ok,:motivo,:usuario);",nativeQuery = true)
    Boolean confirmaEncontro(@Param("id_aluguel") String id_aluguel,
                              @Param("ok") Boolean ok,
                              @Param("motivo") String motivo,
                              @Param("usuario") String usuario);

    @Transactional()
    @Query(value = "SELECT FN_SALVA_RETORNO_PAGAMENTO(:id_aluguel,:id_pagamento,:tipo_retorno,:status,:usuario);",nativeQuery = true)
    Boolean salvaRetornoPagamento(@Param("id_aluguel") String id_aluguel,
                             @Param("id_pagamento") String id_pagamento,
                             @Param("tipo_retorno") String tipo_retorno,
                             @Param("status") String status,
                             @Param("usuario") String usuario);

    @Transactional()
    @Query(value = "SELECT *FROM FN_RETORNA_SALDO_LOCADOR(:id_usuario,:usuario)" +
            "AS (VALOR DECIMAL (18,2),DESCRICAO TEXT, DATA_INCLUSAO TEXT);",nativeQuery = true)
    List<ExtratoLocadorDetalhe> retornaExtratoLocador(@Param("id_usuario") String id_usuario,
                                  @Param("usuario") String usuario);

    @Transactional()
    @Query(value = "SELECT FN_EFETUA_SAQUE_LOCADOR(:id_usuario,:valor, :id_saque);",nativeQuery = true)
    Boolean efetuaSaque(@Param("id_usuario") String id_usuario,
                        @Param("valor") Double valor,
                        @Param("id_saque") String id_pagamento_saque);

    @Transactional()
    @Query(value = "SELECT ID_ALUGUEL, ID_PAGAMENTO_MP, VALOR, RETENCAO, STATUS " +
            "FROM FN_VERIFICAALUGUEIESTORNO(:usuario) " +
            "AS (ID INT, ID_ALUGUEL TEXT, ID_PAGAMENTO_MP TEXT, VALOR DECIMAL(18,2), RETENCAO DECIMAL(3,2), STATUS INT);",nativeQuery = true)
    List<RetornoAlugueisEstorno> retornaPagameentosEstorno(@Param("usuario") String usuario);

    @Transactional()
        @Query(value = "SELECT * " +
            "FROM FN_SAQUE_LOCADOR(:id_usuario) " +
            "AS (ID_USUARIO TEXT, VALOR DECIMAL (18,2), ID_PRODUTO TEXT, NOME_PRODUTO TEXT, " +
            "DESCRICAO_CURTA TEXT, ID_PAGAMENTO TEXT, ID_SAQUE TEXT);",nativeQuery = true)
    List<RetornoSaqueLocador> retornaAlugueisSaque(@Param("id_usuario") String id_usuario);

    @Transactional()
    @Query(value = "SELECT FN_SALVA_RETORNO_PAGAMENTO_LOCADOR(:id_saque,:id_pagamento,:tipo_retorno,:status,:usuario);",nativeQuery = true)
    Boolean salvaRetornoPagamentoLocador(@Param("id_saque") String id_saque,
                                  @Param("id_pagamento") String id_pagamento,
                                  @Param("tipo_retorno") String tipo_retorno,
                                  @Param("status") String status,
                                  @Param("usuario") String usuario);
}
