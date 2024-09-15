package com.iasi.iasi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "TB_IASI_EMPRESA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empresa {

    @Column(name = "NOME_EMPRESA")
    private String nome;
    @Column(name = "SETOR_INDUSTRIAL_EMPRESA")
    private String setorIndustrial;
    @Column(name = "LOCALIZACAO_EMPRESA")
    private String localizacao;
    @Column(name = "TIPO_EMPRESA")
    private String tipo;
    @Column(name = "CONFORMIDADE_REGULAMENTAR")
    private String conformidadeRegular;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EMPRESA")
    private long id;

    public Empresa(String nome, String setorIndustrial, String localizacao, String tipo, String conformidadeRegular) {
        this.nome = nome;
        this.setorIndustrial = setorIndustrial;
        this.localizacao = localizacao;
        this.tipo = tipo;
        this.conformidadeRegular = conformidadeRegular;
    }

}
