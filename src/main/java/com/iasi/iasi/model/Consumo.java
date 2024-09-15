package com.iasi.iasi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TB_IASI_CONSUMO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consumo {

    @Column(name = "DATA_CONSUMO")
    private Date dataConsumo;
    @Column(name = "QUANTIDADE_CONSUMO")
    private Float quantidadeConsumo;
    @Column(name = "TIPO_ENERGIA_CONSUMO")
    private String tipoEnergiaConsumo;
    @Column(name = "EMISSAO_GAS_CONSUMO")
    private Float emissaoGasConsumo;
    @Transient
    private Equipamento equipamento;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CONSUMO")
    private long idConsumo;
    @Column(name = "ID_EQUIPAMENTO")
    private long idEquipamento;

    public Consumo(Equipamento equipamento, Date dataConsumo, Float quantidadeConsumo, String tipoEnergiaConsumo, Float emissaoGasConsumo) {
        this.equipamento = equipamento;
        this.dataConsumo = dataConsumo;
        this.quantidadeConsumo = quantidadeConsumo;
        this.tipoEnergiaConsumo = tipoEnergiaConsumo;
        this.emissaoGasConsumo = emissaoGasConsumo;
    }

}
