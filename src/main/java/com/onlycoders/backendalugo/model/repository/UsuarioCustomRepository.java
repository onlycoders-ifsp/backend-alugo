package com.onlycoders.backendalugo.model.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class UsuarioCustomRepository {
    private final String sql = "SELECT FN_INSERIR_USUARIO(@nome, @cpf, @rua, @numero, @cep, @email, @CodSexo, @dataNasc, @senha, @telefoneCel, @Tel2)";
    
    private final EntityManager em;

    public UsuarioCustomRepository(EntityManager em) {
        this.em = em;
    }

    public List<String> list(String usuario){

        String function = "Select FN_RETORNA_USUARIO('".concat((usuario == "0") ? "0'":usuario + "'");

        return this.em.createNativeQuery(function).getResultList();
    }
/*
    public void salvar(String nome, String cpf, String rua, String numero, String cep,
                       String email, String codSexo, Date dataNasc, String senha, String telefone, String cel){

        String query = "SELECT FN_INSERIR_USUARIO(:nome, :cpf, :rua, :numero, :cep, ";
        query += " :email, :CodSexo, :dataNasc, :senha, :telefone, :cel)";

        var q = em.createQuery(query, Usuario.class);
        q.setParameter("nome", nome);
        q.setParameter("cpf", cpf);
        q.setParameter("rua", rua);
        q.setParameter("numero", numero);
        q.setParameter("cep", cep);
        q.setParameter("email", email);
        q.setParameter("CodSexo", codSexo);
        q.setParameter("dataNasc", dataNasc);
        q.setParameter("senha", senha);
        q.setParameter("telefone", telefone);
        q.setParameter("cel", cel);

        q.getResultList();
    }*/
}
