package com.basis.srs.servico.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Getter
@Setter//autor = "lucas.costa"
public class ReservaDTO {

    private Integer id;

    @NotNull
    private Integer idCliente;

    @NotNull
    private Integer idSala;

    @NotNull
    private LocalDate dataInicio;

    @NotNull
    private LocalDate dataFim;

    @NotNull
    @Positive
    private Double total;

}
