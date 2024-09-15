package com.iasi.iasi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TB_IASI_EQUIPAMENTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Equipamento {

    @Column(name = "NOME_EQUIPAMENTO")
    private String nomeEquipamento;
    @Column(name = "TIPO_EQUIPAMENTO")
    private String tipoEquipamento;
    @Column(name = "LOCALIZACAO_EQUIPAMENTO")
    private String localizacaoEquipamento;
    @Column(name = "DATA_INSTALACAO_EQUIPAMENTO")
    private Date dataInstalacaoEquipamento;
    @Column(name = "ESTADO_EQUIPAMENTO")
    private String estadoEquipamento;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EQUIPAMENTO")
    private long idEquipamento;

    @Transient
    private Equipamento equipamento;

     // preciso analisar o id_empresa, pois não tem esse
     // @ManyToOne
     // @JoinColumn(name = "ID_EMPRESA")
     // private Equipamento equipamento;

    public Equipamento(String nomeEquipamento, String tipoEquipamento, String localizacaoEquipamento, Date dataInstalacaoEquipamento, String estadoEquipamento) {
        //    , Empresa empresa) {
        this.nomeEquipamento = nomeEquipamento;
        this.tipoEquipamento = tipoEquipamento;
        this.localizacaoEquipamento = localizacaoEquipamento;
        this.dataInstalacaoEquipamento = dataInstalacaoEquipamento;
        this.estadoEquipamento = estadoEquipamento;
        //estamos usando empresa//this.empresa = empresa;
    }

}
