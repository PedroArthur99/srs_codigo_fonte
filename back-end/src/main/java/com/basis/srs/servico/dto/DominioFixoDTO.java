package com.basis.srs.servico.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DominioFixoDTO implements Serializable {

    private Integer id;

    private Integer idTipoSala;

    private String descricao;

    private Integer limitePessoas;

    private double precoDiaria;
}