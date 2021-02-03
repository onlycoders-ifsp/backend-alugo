package com.onlycoders.backendalugo.model.repository;
import com.onlycoders.backendalugo.model.entity.aluguel.template.RetornaAluguel;
import com.onlycoders.backendalugo.model.entity.aluguel.template.RetornaAluguelDetalhe;
import com.onlycoders.backendalugo.model.entity.email.RetornaDadosLocadorLocatario;
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
    Boolean efetuaAluguel(@Param("id_usuario") String id_usuario,
                          @Param("id_produto") String id_produto,
                          @Param("data_inicio") String data_inicio,
                          @Param("data_fim") String data_fim,
                          @Param("valor_aluguel") Double valor_aluguel,
                          @Param("user") String user);


    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "Select *FROM FN_RETORNA_ALUGUEL(:id_locador,:id_locatario,:id_aluguel,:id_produto,:op,:user)" +
                   "AS T (ID_ALUGUEL TEXT, ID_PRODUTO TEXT, ID_LOCATARIO TEXT," +
                    "ID_LOCADOR TEXT, DATA_INICIO TEXT, DATA_FIM TEXT," +
                    "VALOR_ALUGUEL DOUBLE PRECISION, DATA_SAQUE TEXT)",
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
            "VALOR_ALUGUEL DOUBLE PRECISION, VALOR_GANHO DOUBLE PRECISION, DATA_DEVOLUCAO TEXT)",
            nativeQuery = true)
    List<RetornaAluguelDetalhe> retornaAluguelDetalhe(@Param("id_produto") String id_produto,
                                                      @Param("user") String user);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "SELECT *FROM FN_RETORNA_ALUGUEIS_NOTIFICACAO_INICIO(:usuario)" +
            "AS (IDALUGUEL TEXT,PRODUTONOME TEXT, LOCADORNOME TEXT," +
            "LOCADOREMAIL TEXT, LOCATARIONOME TEXT," +
            "LOCATARIOEMAIL TEXT);",nativeQuery = true)
    List<RetornoAlugueisNotificacao> retornaAlugueisNotificacaoInicio(@Param("usuario") String usuario);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "SELECT *FROM FN_RETORNA_ALUGUEIS_NOTIFICACAO_FIM(:usuario)" +
            "AS (IDALUGUEL TEXT,PRODUTONOME TEXT, LOCADORNOME TEXT," +
            "LOCADOREMAIL TEXT, LOCATARIONOME TEXT," +
            "LOCATARIOEMAIL TEXT);",nativeQuery = true)
    List<RetornoAlugueisNotificacao> retornaAlugueisNotificacaoFim(@Param("usuario") String usuario);

    @Transactional()
    @Query(value = "SELECT FN_STATUS_ALUGUEL(:id_aluguel,:status,:usuario);",nativeQuery = true)
    void alteraStatusAluguel(@Param("id_aluguel") String id_aluguel,
                                @Param("status") int status,
                                @Param("usuario") String usuario);

    @Transactional()
    @Query(value = "SELECT FN_SALVA_DETALHE_ALUGUEL(:id_aluguel,:cep,:logradouro,:bairro," +
            ":descricao,:data_hora,:tipo,:usuario);",nativeQuery = true)
    Boolean salvaDetalheAluguel(@Param("id_aluguel") String id_aluguel,
                                @Param("cep") String cep,
                                @Param("logradouro") String logradouro,
                                @Param("bairro") String bairro,
                                @Param("descricao") String descricao,
                                @Param("data_hora") String data_hora,
                                @Param("tipo") String tipo,
                                @Param("usuario") String usuario);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "SELECT *FROM FN_RETORNA_DADOS_LOCADOR_LOCATARIO(:id_aluguel,:usuario)" +
            "AS (PRODUTONOME TEXT, LOCADORNOME TEXT," +
            "LOCADOREMAIL TEXT, LOCATARIONOME TEXT," +
            "LOCATARIOEMAIL TEXT);",nativeQuery = true)
    RetornoAlugueisNotificacao retornaDadosLocadorLocatario(@Param("id_aluguel") String id_aluguel,
                                                              @Param("usuario") String usuario);
}
