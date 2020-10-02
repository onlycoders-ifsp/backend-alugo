/* ########################## REPOSITORY DE USUÁRIO ##########################
 * Data Criação: 14/06/2020
 * Programador: Dilan Lima
 * Decrição: Classe que cuida da inserção de usuários
 * ##########################################################################
 */

package com.onlycoders.backendalugo.model.repository;

import com.onlycoders.backendalugo.model.entity.usuario.templates.RetornaUsuario;
import com.onlycoders.backendalugo.model.entity.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    /*@Query("select u from Usuario u where u.nome = ?1 and u.email = ?2")
    Optional<Usuario> getByNomeAndEmail(String nome, String email);b
    Usuario getById(Integer id);
    Usuario getByCodigo(String codigo);

    List<Usuario> getByNome(String nome);
*/
    //@Query(value = "Select FN_RETORNA_USUARIO(':id_usuario'", nativeQuery = true)

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "Select *From FN_RETORNA_USUARIO(:id) AS T(" +
            " IDUSUARIO TEXT, NOME TEXT, CPF TEXT, EMAIL TEXT, SEXO TEXT,  DATANASCIMENTO TEXT," +
            " TELEFONE TEXT, CELULAR TEXT, DATAINCLUSAO TEXT," +
            " ESTADO TEXT, CIDADE TEXT, " +
            " LOGRADOURO TEXT, NUMERO TEXT, BAIRRO TEXT)",
    //@Query(value = "Select u.*From Usuarios u",
    nativeQuery = true)
    List<RetornaUsuario> findUsuario(@Param("id") String id);

    @Transactional()
    @Query(value = "SELECT FN_INSERIR_USUARIO(:nome,:cpf,:email,:sexo," +
            ":nascimento,:senha,:login,:telefone,:celular);",nativeQuery = true)
    Boolean createUsuario(@Param("nome") String nome,
                          @Param("cpf") String cpf,
                          @Param("email") String email,
                          @Param("sexo") String sexo,
                          @Param("nascimento") String nascimento,
                          @Param("senha") String senha,
                          @Param("login") String login,
                          @Param("telefone") String telefone,
                          @Param("celular") String celular);

    @Transactional()
    @Query(value = "SELECT FN_ATIVA_INATIVA_USUARIO(:id);",
    nativeQuery = true)
    Boolean deleteUserById(String id);

    @Transactional()
    @Query(value = "SELECT FN_ATUALIZA_USUARIO(:id,:nome,:cpf,:email,:sexo," +
            " :nascimento,:senha,:login,:telefone,:celular);",
            nativeQuery = true)
    Boolean updateUserById(@Param("id") String id,
                           @Param("nome") String nome,
                           @Param("cpf") String cpf,
                           @Param("email") String email,
                           @Param("sexo") String sexo,
                           @Param("nascimento") String nascimento,
                           @Param("senha") String senha,
                           @Param("login") String login,
                           @Param("telefone") String telefone,
                           @Param("celular") String celular);
}
