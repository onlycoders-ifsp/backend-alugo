package com.onlycoders.backendalugo.model.repository;

import com.onlycoders.backendalugo.model.entity.aluguel.Aluguel;
import com.onlycoders.backendalugo.model.entity.produto.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AluguelRepository extends JpaRepository<Produto, Integer> {

    @Transactional
    @Query(value = "Select FN_EFETUA_ALUGUEL(:id_usuario, :id_produto, :data_inicio," +
            ":data_fim, :valor_aluguel)",nativeQuery = true)
    Boolean efetuaAluguel(@Param("id_usuario") String id_usuario,
                          @Param("id_produto") String id_produto,
                          @Param("data_inicio") String data_inicio,
                          @Param("data_fim") String data_fim,
                          @Param("valor_aluguel") Double valor_aluguel);


    @Transactional
    @Query(value = "Select FN_RETORNA_TODOS_ALUGUEL_USUARIO(:id_usuario,)",nativeQuery = true)
    Aluguel retornaAluguelLocador(@Param("id_usuario") String id_usuario);
}
