package com.onlycoders.backendalugo.model.repository;
import com.onlycoders.backendalugo.model.entity.admin.LogErros;
import com.onlycoders.backendalugo.model.entity.logs.RetornoErrosBackendController;
import com.onlycoders.backendalugo.model.entity.logs.RetornoErrosBackendMetodos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface LogRepository extends JpaRepository<LogErros,Integer> {

    @Transactional()
    @Query(value = "select FN_REPORT_ERRO_BACKEND(:controller, :metodo, :endpoint,:usuario, :message, :stack_trace);",nativeQuery = true)
    Boolean gravaLogBackend(@Param("controller") String controller,
                         @Param("metodo") String metodo,
                         @Param("endpoint") String endpoint,
                         @Param("usuario") String usuario,
                         @Param("message") String message,
                         @Param("stack_trace") String stack_trace);
    @Transactional()
    @Query(value = "select *from FN_RETORNA_LOG_BACKEND_CONTROLLER_MES(:usuario)" +
                    "as (controller text, quantidade int, mes text);",nativeQuery = true)
    List<RetornoErrosBackendController> retornaErrosController(@Param("usuario") String usuario);

    @Transactional()
    @Query(value = "select *from FN_RETORNA_LOG_BACKEND_METODOS_MES(:controller,:usuario)" +
                    "as (metodo text, endpoint text, quantidade int, mes text);",nativeQuery = true)
    List<RetornoErrosBackendMetodos> retornaErrosMetodo(@Param("controller") String controller,
                                                        @Param("usuario") String usuario);
}
