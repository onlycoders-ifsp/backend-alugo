package com.onlycoders.backendalugo.model.repository;

import com.onlycoders.backendalugo.model.entity.admin.LogErros;
import com.onlycoders.backendalugo.model.entity.email.RetornoUsuarioProdutoNoficacao;
import com.onlycoders.backendalugo.model.entity.logs.RetornaErrosProcedureAgrupado;
import com.onlycoders.backendalugo.model.entity.logs.RetornaLogBackendDetalhe;
import com.onlycoders.backendalugo.model.entity.logs.RetornoErrosBackendController;
import com.onlycoders.backendalugo.model.entity.logs.RetornoErrosBackendMetodos;
import com.onlycoders.backendalugo.model.entity.usuario.templates.RetornaUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Secured("ROLE_ADMIN")
public interface AdminRepository extends JpaRepository<LogErros,Integer> {

    @Transactional()
    @Query(value = "SELECT FN_ATIVA_INATIVA_USUARIO(:id,:user);",
            nativeQuery = true)
    Boolean deleteUserById(@Param("id") String id,
                           @Param("user") String user);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "SELECT *FROM FN_RETORNA_LOG_ERROS(:user) " +
                   "AS T(ID INTEGER, PROCEDURE TEXT, TABELA TEXT, " +
                   "USUARIO TEXT ,ERRO TEXT, QUERY TEXT, DATA_ERRO TEXT);",nativeQuery = true)
    List<LogErros> retornaErros(@Param("user") String user);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "Select *From FN_RETORNA_USUARIO(:id,:user) " +
            "AS T(IDUSUARIO TEXT, NOME TEXT, EMAIL TEXT, LOGIN TEXT, CPF TEXT, CELULAR TEXT," +
            "            DATANASCIMENTO TEXT, CEP TEXT, ENDERECO TEXT, " +
            "            COMPLEMENTO TEXT, BAIRRO TEXT, NUMERO TEXT, ATIVO BOOLEAN, CAPA_FOTO BYTEA);",
            //@Query(value = "Select u.*From Usuarios u",
            nativeQuery = true)
    List<RetornaUsuario> findUsuario(@Param("id") String id,@Param("user") String user);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "select *from FN_RETORNA_LOG_BACKEND_CONTROLLER_MES(:usuario)" +
            "as (controller text, quantidade int, mes text);",nativeQuery = true)
    List<RetornoErrosBackendController> retornaErrosController(@Param("usuario") String usuario);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "select *from FN_RETORNA_LOG_BACKEND_METODOS_MES(:controller,:usuario)" +
            "as (metodo text, endpoint text, quantidade int, mes text);",nativeQuery = true)
    List<RetornoErrosBackendMetodos> retornaErrosMetodo(@Param("controller") String controller,
                                                        @Param("usuario") String usuario);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "select *from FN_RETORNA_LOG_MES(:usuario)" +
            "as (procedure text, quantidade int, mes text);",nativeQuery = true)
    List<RetornaErrosProcedureAgrupado> retornaErrosProcedureAgrupado(@Param("usuario") String usuario);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "select * FROM FN_RETORNA_LOG_BACKEND_DETALHE()" +
                    "AS (CONTROLLER TEXT, METODO TEXT, ENDPOINT TEXT, USUARIO TEXT, MESSAGE TEXT, STACKTRACE TEXT);",nativeQuery = true)
    List<RetornaLogBackendDetalhe> retornaLogBackendDetalhe(@Param("usuario") String usuario);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "select FN_REJEITA_PRODUTO(:id_produto,:motivo,:usuario);", nativeQuery = true)
    RetornoUsuarioProdutoNoficacao rejeitaProduto(@Param("id_produto") String id_produto,
                                                  @Param("motivo") String motivo,
                                                  @Param("usuario") String usuario);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "select FN_APROVA_PRODUTO(:id_produto,:motivo,:usuario);", nativeQuery = true)
    RetornoUsuarioProdutoNoficacao aprovaProduto(@Param("id_produto") String id_produto,
                                                  @Param("motivo") String motivo,
                                                  @Param("usuario") String usuario);
}
