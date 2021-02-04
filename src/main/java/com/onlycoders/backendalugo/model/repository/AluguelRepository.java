package com.onlycoders.backendalugo.model.repository;
import com.onlycoders.backendalugo.model.entity.aluguel.template.RetornaAluguel;
import com.onlycoders.backendalugo.model.entity.aluguel.template.RetornaAluguelDetalhe;
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
    @Query(value = "SELECT *FROM FN_RETORNA_ALUGUEIS_NOTIFICACAO(:usuario)" +
            "AS (PRODUTONOME TEXT, LOCADORNOME TEXT," +
            "LOCADOREMAIL TEXT, LOCATARIONOME TEXT," +
            "LOCATARIOEMAIL TEXT);",nativeQuery = true)
    List<RetornoAlugueisNotificacao> retornaAlugueisNotificacao(@Param("usuario") String usuario);

    @Transactional
    @Query(value = "Select FN_INSERE_ALUGUEL_ENCONTRO(:id_usuario, :id_aluguel, :cep_entrega, :logradouro_entrega," +
            ":bairro_entrega, :descricao_entrega,:data_entrega, :cep_devolucao, :logradouro_devolucao," +
            ":bairro_devolucao, :descricao_devolucao, :data_devolucao, :aceite_locador, :observacao_recusa)",nativeQuery = true)
    Boolean insereAluguelEncontro(@Param("id_usuario") String id_usuario,
                                  @Param("id_aluguel") String id_aluguel,
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
                                  @Param("observacao_recusa") String observacao_recusa);

}
