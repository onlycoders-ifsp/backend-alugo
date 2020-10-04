package com.onlycoders.backendalugo.model.repository;

import com.onlycoders.backendalugo.model.entity.login.RetornaLogin;
import com.onlycoders.backendalugo.model.entity.produto.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    //List<Produto> getByNome(String nome);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "SELECT *FROM FN_RETORNA_PRODUTO(:id_produto,:id_usuario)" +
                    "as t (ID_USUARIO TEXT, ID_PRODUTO TEXT, NOME TEXT, DESCRICAO TEXT, " +
                        " VALOR_BASE_DIARIA NUMERIC(16,2), VALOR_BASE_MENSAL NUMERIC(16,2)," +
                        " VALOR_PRODUTO NUMERIC(16,2), DATA_COMPRA TEXT, ATIVO BOOLEAN);",
    nativeQuery = true)
    List<Produto> findProduto(@Param("id_produto") String id_produto,
                                            @Param("id_usuario") String id_usuario);

    @Transactional
    @Query(value = "SELECT FN_CADASTRAR_PRODUTO(:id,:nome,:descricao,:diaria," +
                    ":mensal,:valorProduto,:dataCompra);",nativeQuery = true)
    Boolean createProduto(@Param("id") String id,
                            @Param("nome") String nome,
                            @Param("descricao") String descricao,
                            @Param("diaria") Double diaria,
                            @Param("mensal") Double mensal,
                            @Param("valorProduto") Double valorProduto,
                            @Param("dataCompra") String dataCompra);

    @Transactional
    @Query(value = "SELECT FN_ATUALIZA_PRODUTO(:id,:nome,:descricao,:diaria," +
            ":mensal,:valorProduto,:dataCompra);",nativeQuery = true)
    Boolean updateProduto(@Param("id") String id,
                          @Param("nome") String nome,
                          @Param("descricao") String descricao,
                          @Param("diaria") Double diaria,
                          @Param("mensal") Double mensal,
                          @Param("valorProduto") Double valorProduto,
                          @Param("dataCompra") String dataCompra);

    @Transactional
    @Query(value = "SELECT FN_ATIVA_INATIVA_PRODUTO(:id);",nativeQuery = true)
    Boolean ativaInativaProduto(@Param("id") String id);
}
