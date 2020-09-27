/* ########################## REPOSITORY DE USUÁRIO ##########################
 * Data Criação: 14/06/2020
 * Programador: Dilan Lima
 * Decrição: Classe que cuida da inserção de usuários
 * ##########################################################################
 */

package com.onlycoders.backendalugo.model.repository;

import com.onlycoders.backendalugo.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    @Query("select u from Usuario u where u.nome = ?1 and u.email = ?2")
    Optional<Usuario> getByNomeAndEmail(String nome, String email);
    Usuario getById(Integer id);
    Usuario getByCodigo(String codigo);

    List<Usuario> getByNome(String nome);

}
