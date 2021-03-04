package com.onlycoders.backendalugo.model.repository;

import com.onlycoders.backendalugo.model.entity.admin.LogErros;
import com.onlycoders.backendalugo.model.entity.admin.RetornaGravidades;
import com.onlycoders.backendalugo.model.entity.admin.RetornaProblemas;
import com.onlycoders.backendalugo.model.entity.admin.RetornaTiposProblema;
import com.onlycoders.backendalugo.model.entity.email.RetornoUsuarioProdutoNoficacao;
import com.onlycoders.backendalugo.model.entity.logs.RetornaErrosProcedureAgrupado;
import com.onlycoders.backendalugo.model.entity.logs.RetornaLogBackendDetalhe;
import com.onlycoders.backendalugo.model.entity.logs.RetornoErrosBackendController;
import com.onlycoders.backendalugo.model.entity.logs.RetornoErrosBackendMetodos;
import com.onlycoders.backendalugo.model.entity.usuario.templates.RetornaUsuario;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Secured("ROLE_ADMIN")
public interface AdminRepository extends JpaRepository<LogErros,Integer> {

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Query(value = "SELECT FN_ATIVA_INATIVA_USUARIO(:id,:user,:motivo);",nativeQuery = true)
        //@Type(type = "com.onlycoders.backendalugo.configuracao.GenericArrayUserType")
    Boolean activateDesactivateUserById(@Param("id") String id,
                                        @Param("user") String user,
                                        @Param("motivo") String motivo);

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Query(value = "SELECT *FROM FN_RETORNA_LOG_ERROS(:user) " +
            "AS T(ID INTEGER, PROCEDURE TEXT, TABELA TEXT, " +
            "USUARIO TEXT ,ERRO TEXT, QUERY TEXT, DATA_ERRO TEXT);",nativeQuery = true)
    List<LogErros> retornaErros(@Param("user") String user);

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Query(value = "Select *From FN_RETORNA_USUARIO(:id,:user) " +
            "AS T(IDUSUARIO TEXT, NOME TEXT, EMAIL TEXT, LOGIN TEXT, CPF TEXT, CELULAR TEXT," +
            "            DATANASCIMENTO TEXT, CEP TEXT, ENDERECO TEXT, " +
            "            COMPLEMENTO TEXT, BAIRRO TEXT, NUMERO TEXT, ATIVO BOOLEAN, CAPA_FOTO BYTEA," +
            "            LOCATARIO_AVALIACAO DECIMAL(2,1), LOCADOR_AVALIACAO DECIMAL(2,1)," +
            "            PRODUTO_AVALIACAO DECIMAL(2,1), SALDO_LOCADOR Decimal(18,2));",
            //@Query(value = "Select u.*From Usuarios u",
            nativeQuery = true)
    List<RetornaUsuario> findUsuario(@Param("id") String id,@Param("user") String user);

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Query(value = "select *from FN_RETORNA_LOG_BACKEND_CONTROLLER_MES(:usuario)" +
            "as (controller text, quantidade int, mes text);",nativeQuery = true)
    List<RetornoErrosBackendController> retornaErrosController(@Param("usuario") String usuario);

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Query(value = "select *from FN_RETORNA_LOG_BACKEND_METODOS_MES(:controller,:usuario)" +
            "as (metodo text, endpoint text, quantidade int, mes text);",nativeQuery = true)
    List<RetornoErrosBackendMetodos> retornaErrosMetodo(@Param("controller") String controller,
                                                        @Param("usuario") String usuario);

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Query(value = "select *from FN_RETORNA_LOG_MES(:usuario)" +
            "as (procedure text, quantidade int, mes text);",nativeQuery = true)
    List<RetornaErrosProcedureAgrupado> retornaErrosProcedureAgrupado(@Param("usuario") String usuario);

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Query(value = "select * FROM FN_RETORNA_LOG_BACKEND_DETALHE()" +
            "AS (CONTROLLER TEXT, METODO TEXT, ENDPOINT TEXT, USUARIO TEXT, MESSAGE TEXT, STACKTRACE TEXT);",nativeQuery = true)
    List<RetornaLogBackendDetalhe> retornaLogBackendDetalhe(@Param("usuario") String usuario);

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Query(value = "select FN_REJEITA_PRODUTO(:id_produto,:motivo,:usuario);", nativeQuery = true)
    RetornoUsuarioProdutoNoficacao rejeitaProduto(@Param("id_produto") String id_produto,
                                                  @Param("motivo") String motivo,
                                                  @Param("usuario") String usuario);

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Query(value = "select FN_APROVA_PRODUTO(:id_produto,:motivo,:usuario);", nativeQuery = true)
    RetornoUsuarioProdutoNoficacao aprovaProduto(@Param("id_produto") String id_produto,
                                                 @Param("motivo") String motivo,
                                                 @Param("usuario") String usuario);

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Query(value = "select *FROM FN_RETORNA_PROBLEMAS(:tipo_problema,:gravidade,:status, :data_inicio, :usuario)" +
                   " as (DESCRICAO TEXT, VALOR_LOCADOR DECIMAL(18,2),VALOR_LOCATARIO DECIMAL(18,2)," +
                   " TIPO_PROBLEMa integer, gravidade integer, status_problema integer, id_solicitante text," +
                   " solicitante char(1), data_inclusao text, usuario_operacao text);", nativeQuery = true)
    List<RetornaProblemas> retornaProblemas(@Param("tipo_problema") Integer tipo_problema,
                                      @Param("gravidade") Integer gravidade,
                                      @Param("status") Integer status,
                                      @Param("data_inicio") String data_inicio,
                                      @Param("usuario") String usuario);

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Query(value = "select FN_INSERE_PROBLEMA(:id_aluguel,:id_usuario,:problema, :descricao);", nativeQuery = true)
    Boolean cadastraProblema(@Param("id_aluguel") String id_aluguel,
                              @Param("id_usuario") String id_usuario,
                              @Param("problema") Integer problema,
                              @Param("descricao") String descricao);

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Query(value = "select FN_APROVA_PROBLEMA(:id_problema,:gravidade,:usuario);", nativeQuery = true)
    Boolean aprovaProblema(@Param("id_problema") String id_aluguel,
                             @Param("gravidade") Integer gravidade,
                             @Param("usuario") String usuario);

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Query(value = "select FN_REPROVA_PROBLEMA(:id_problema,:usuario);", nativeQuery = true)
    Boolean reprovaProblema(@Param("id_problema") String id_aluguel,
                            @Param("usuario") String usuario);


    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Query(value = "SELECT COD_GRAVIDADE, DESCRICAO, DEVOLUCAO from gravidades " +
                   "order by 1;", nativeQuery = true)
    List<RetornaGravidades> retornaGravidades();

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Query(value = " SELECT TP.cod_tipo_problema, TP.descricao, TP.perc_locador, TP.perc_locatario, B.descricao descricao_calculo " +
                   " from tipo_problema TP " +
                   " inner join base_calculo_estorno B " +
                   " ON TP.BASE_CALCULO = B.id " +
                   " order by 1; ", nativeQuery = true)
    List<RetornaTiposProblema> retornaTiposProblema();

}
