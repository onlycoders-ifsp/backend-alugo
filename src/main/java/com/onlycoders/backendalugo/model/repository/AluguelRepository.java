package com.onlycoders.backendalugo.model.repository;

import com.onlycoders.backendalugo.model.entity.produto.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

public interface AluguelRepository extends JpaRepository<Produto, Integer> {

    @Transactional
    @Query(value = "Select FN_EFETUA_ALUGUEL(:id_produto,:id_usuario,)",nativeQuery = true)
    Boolean efetuaAluguel(@Param("id_produto") String id_produto, @Param("id_usuario") String id_usuario);
}
