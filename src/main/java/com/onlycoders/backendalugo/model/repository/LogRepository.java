package com.onlycoders.backendalugo.model.repository;
import com.onlycoders.backendalugo.model.entity.admin.LogErros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
public interface LogRepository extends JpaRepository<LogErros,Integer> {

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Query(value = "select FN_REPORT_ERRO_BACKEND(:controller, :metodo, :endpoint,:usuario, :message, :stack_trace);",nativeQuery = true)
    Boolean gravaLogBackend(@Param("controller") String controller,
                         @Param("metodo") String metodo,
                         @Param("endpoint") String endpoint,
                         @Param("usuario") String usuario,
                         @Param("message") String message,
                         @Param("stack_trace") String stack_trace);
}
