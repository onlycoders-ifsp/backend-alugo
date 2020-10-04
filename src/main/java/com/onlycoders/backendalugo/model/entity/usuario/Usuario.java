/* ########################## ENTIDADE DE USUÁRIO ##########################
 * Data Criação: 14/06/2020
 * Programador: Dilan Lima
 * Decrição: Entidade que mapea a tabela de usuários
 * #########################################################################
 */

package com.onlycoders.backendalugo.model.entity.usuario;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

/*o @Entity diz que esta classe é uma entidade*/
/*o @Getter e @Setter gera automaticamente os métodos básicos em tempo de execução*/
/*o @NoArgsConstructor diz que o método construtor não possui parâmetros*/
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Usuario {

    @Id
    private String id_usuario;

    @Column
    private String nome;

    @Column
    private String cpf;

    @Column
    private String email;

    @Column
    private String sexo;

    @Column
    private String data_nascimento;

    @Column
    private Boolean isDeletado;

    @Column
    private String data_inclusao;

    @Column
    private String data_modificacao;
}
