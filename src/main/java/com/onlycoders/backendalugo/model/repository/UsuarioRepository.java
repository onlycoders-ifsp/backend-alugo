/* ########################## REPOSITORY DE USUÁRIO ##########################
 * Data Criação: 14/06/2020
 * Programador: Dilan Lima
 * Decrição: Classe que cuida da inserção de usuários
 * ##########################################################################
 */

package com.onlycoders.backendalugo.model.repository;

import com.onlycoders.backendalugo.model.entity.login.RetornaLogin;
import com.onlycoders.backendalugo.model.entity.usuario.templates.RetornaUsuario;
import com.onlycoders.backendalugo.model.entity.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    //Retorna usuario
     @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "Select *From FN_RETORNA_USUARIO(:id) " +
            "AS T(IDUSUARIO TEXT, NOME TEXT, EMAIL TEXT, LOGIN TEXT, CPF TEXT, CELULAR TEXT," +
    "            DATANASCIMENTO TEXT, CEP TEXT, ENDERECO TEXT, " +
    "            COMPLEMENTO TEXT, BAIRRO TEXT, NUMERO TEXT, ATIVO BOOLEAN, CAPA_FOTO BYTEA," +
    "            LOCATARIO_AVALIACAO DECIMAL(2,1), LOCADOR_AVALIACAO DECIMAL(2,1)," +
    "            PRODUTO_AVALIACAO DECIMAL(2,1));",
    //@Query(value = "Select u.*From Usuarios u",
    nativeQuery = true)
    List<RetornaUsuario> findUsuario(@Param("id") String id);

    //Insere usuario
    @Transactional()
    @Query(value = "SELECT *FROM FN_INSERIR_USUARIO(:nome,:email,:login,:senha," +
            ":cpf,:celular,:nascimento,:cep,:endereco,:complemento," +
            ":bairro,:numero)" +
            "AS T(IDUSUARIO TEXT, NOME TEXT, EMAIL TEXT, LOGIN TEXT, CPF TEXT, CELULAR TEXT," +
            "   DATANASCIMENTO TEXT, CEP TEXT, ENDERECO TEXT," +
            "   COMPLEMENTO TEXT, BAIRRO TEXT, NUMERO TEXT, ATIVO BOOLEAN);",nativeQuery = true)
    List <RetornaUsuario> createUsuario(@Param("nome") String nome,
                          @Param("email") String email,
                          @Param("login") String login,
                          @Param("senha") String senha,
                          @Param("cpf") String cpf,
                          @Param("celular") String celular,
                          @Param("nascimento") String nascimento,
                          @Param("cep") String cep,
                          @Param("endereco") String endereco,
                          @Param("complemento") String complemento,
                          @Param("bairro") String bairro,
                          @Param("numero") String numero);

    //Insere apenas informações minimas
    @Transactional()
    @Query(value = "SELECT FN_INSERIR_USUARIO_MIN(:nome,:email,:login,:senha,:cpf,:celular,:verificationCode);",
            nativeQuery = true)
    Boolean createUsuarioMin(@Param("nome") String nome,
                            @Param("email") String email,
                            @Param("login") String login,
                            @Param("senha") String senha,
                            @Param("cpf") String cpf,
                            @Param("celular") String celular,
                             @Param("verificationCode") String verificationCode);

    @Transactional
    @Query(value = "SELECT FN_FOTO_USUARIO(:id_usuario,:foto) ;",nativeQuery = true)
    Boolean uploadFoto(@Param("id_usuario") String id_usuario, @Param("foto") byte[] foto);

    //Atualiza usuario
    @Transactional()
    @Query(value = "SELECT *FROM FN_ATUALIZA_USUARIO(:id,:nome,:email,:login,:cpf," +
            " :celular,:nascimento,:cep,:endereco,:complemento,:bairro,:numero) " +
            " AS T(IDUSUARIO TEXT, NOME TEXT, EMAIL TEXT, LOGIN TEXT, CPF TEXT, " +
            " CELULAR TEXT,DATANASCIMENTO TEXT, CEP TEXT, ENDERECO TEXT, " +
            " COMPLEMENTO TEXT, BAIRRO TEXT, NUMERO TEXT, ATIVO BOOLEAN, CAPA_FOTO BYTEA);",nativeQuery = true)
    List <RetornaUsuario>  updateUserById(@Param("id") String id,
                           @Param("nome") String nome,
                           @Param("email") String email,
                           @Param("login") String login,
                           @Param("cpf") String cpf,
                           @Param("celular") String celular,
                           @Param("nascimento") String nascimento,
                           @Param("cep") String cep,
                           @Param("endereco") String endereco,
                           @Param("complemento") String complemento,
                           @Param("bairro") String bairro,
                           @Param("numero") String numemro);

    //Verifica login
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "Select *from FN_VERIFCAR_LOGIN(:login)" +
                   " AS T(ID_USUARIO TEXT, LOGIN TEXT, PASSWORD TEXT, ADMIN BOOLEAN, ATIVO BOOLEAN);",
                    nativeQuery = true)
    RetornaLogin verificaLogin(@Param("login") String login);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "Select ID_USUARIO from FN_VERIFCAR_LOGIN(:login)" +
            " AS T(ID_USUARIO TEXT, LOGIN TEXT, PASSWORD TEXT, ADMIN BOOLEAN, ATIVO BOOLEAN);",
            nativeQuery = true)
    String retornaIdUsuario(@Param("login") String login);

    @Transactional()
    @Query(value = "Select FN_ATUALIZA_SENHA(:idUsuario,:senha);",
                    nativeQuery = true)
    Boolean alteraSenha(@Param("idUsuario") String idUsuario, @Param("senha") String senha);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "Select FN_RETORNA_SENHA(:idUsuario);",
            nativeQuery = true)
    String retornaSenha(@Param("idUsuario") String idUsuario);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "SELECT FN_VALIDA_DADOS(:dado,:opcao);",nativeQuery = true)
    Boolean validaDado(@Param("dado") String dado,
                        @Param("opcao") int opcao);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "SELECT FN_VALIDA_DADOS_UPDATE(:dado,:id_usuario,:opcao);",nativeQuery = true)
    Boolean validaDadouUpdate(@Param("dado") String dado,
                       @Param("id_usuario") String id_usuario,
                       @Param("opcao") int opcao);

    @Transactional()
    @Query(value = "SELECT FN_ATIVA_USUARIO(:key,:usuario);",nativeQuery = true)
    Boolean ativaUsuario(@Param("key") String key,
                         @Param("usuario") String usuario);


}
