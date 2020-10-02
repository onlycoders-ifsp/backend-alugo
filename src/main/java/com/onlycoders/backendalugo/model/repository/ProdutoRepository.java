package com.onlycoders.backendalugo.model.repository;

import com.onlycoders.backendalugo.model.entity.produto.Produto;
import com.onlycoders.backendalugo.model.entity.produto.templates.RetornaProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    //List<Produto> getByNome(String nome);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "SELECT FN_RETORNA_USUARIO_PRODUTO(:id);",
    nativeQuery = true)
    List<RetornaProduto> findProduto(@Param("id") String id);
}
