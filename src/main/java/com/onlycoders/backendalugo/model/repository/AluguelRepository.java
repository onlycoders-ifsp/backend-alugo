package com.onlycoders.backendalugo.model.repository;
import com.onlycoders.backendalugo.model.entity.aluguel.RetornaAluguel;
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
            ":data_fim, :valor_aluguel)",nativeQuery = true)
    Boolean efetuaAluguel(@Param("id_usuario") String id_usuario,
                          @Param("id_produto") String id_produto,
                          @Param("data_inicio") String data_inicio,
                          @Param("data_fim") String data_fim,
                          @Param("valor_aluguel") Double valor_aluguel);


    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "Select *FROM FN_RETORNA_ALUGUEL(:id_locador,:id_locatario,:id_aluguel,:id_produto)" +
                   "AS T (ID_ALUGUEL TEXT, ID_PRODUTO TEXT, ID_LOCATARIO TEXT," +
                    "ID_LOCADOR TEXT, DATA_INICIO TEXT, DATA_FIM TEXT," +
                    "VALOR_ALUGUEL DOUBLE PRECISION, DATA_SAQUE TEXT)",
            nativeQuery = true)
    List<RetornaAluguel> retornaAluguel(@Param("id_locador") String id_locador,
                                               @Param("id_locatario") String id_locatario,
                                               @Param("id_aluguel") String id_aluguel,
                                               @Param("id_produto") String id_produto);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "SELECT FN_VALIDA_ALUGUEL(:id_usuario, :id_produto, :inicio, :fim)",
            nativeQuery = true)
    String validaALuguel(@Param("id_usuario")String id_usuario,
                         @Param("id_produto")String id_produto,
                         @Param("inicio")String inicio,
                         @Param("fim")String fim);
}
