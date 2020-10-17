package com.basis.srs.web.rest;

import com.basis.srs.builder.EquipamentoBuilder;
import com.basis.srs.builder.ReservaBuilder;
import com.basis.srs.builder.SalaBuilder;
import com.basis.srs.dominio.Equipamento;
import com.basis.srs.dominio.Reserva;
import com.basis.srs.dominio.Sala;
import com.basis.srs.dominio.SalaEquipamento;
import com.basis.srs.repositorio.ReservaRepositorio;
import com.basis.srs.repositorio.SalaRepositorio;
import com.basis.srs.servico.dto.SalaDTO;
import com.basis.srs.util.IntTestComum;
import com.basis.srs.util.TestUtil;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@RunWith(SpringRunner.class)
@Transactional
public class SalaRecursoIT extends IntTestComum {

    @Autowired
    private SalaBuilder salaBuilder;

    @Autowired
    private ReservaBuilder reservaBuilder;

    @Autowired
    private EquipamentoBuilder equipamentoBuilder;

    @Autowired
    private SalaRepositorio salaRepositorio;

    @BeforeEach
    public void limparBanco() {
        salaRepositorio.deleteAll();
    }

    @Test
    public void listar() throws Exception {
        salaBuilder.construir();
        getMockMvc().perform(get("/api/salas"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id", Matchers.hasSize(1)));

    }

    @Test
    public void listarPorId() throws Exception {
        Sala sb = salaBuilder.construir();
        getMockMvc().perform(get("/api/salas/" + sb.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(sb.getId()));
    }

    @Test
    public void salvar() throws Exception {
        Sala sala = salaBuilder.construirEntidade();
        getMockMvc().perform(post("/api/salas/")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(salaBuilder.converterParaDto(sala)))
        )
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void atualizar() throws Exception {
        Sala sala = salaBuilder.construir();
        SalaDTO salaDTO = salaBuilder.converterParaDto(sala);
        getMockMvc().perform(put("/api/salas/")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(salaBuilder.converterParaDto(sala)))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(sala.getId()));
    }

    @Test
    public void deletar() throws Exception {
        Sala sala = salaBuilder.construir();
        getMockMvc().perform(delete("/api/salas/" + sala.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////TESTE DE ENDPOINTS COM BAD REQUEST///////////////////////////////////////////////

    @Test
    public void atualizarExcessao1() throws Exception { //irá testar quando apagar um equipamento obrigatório - deve retornar BAD REQUEST
        Sala sala = salaBuilder.construir();
        List<SalaEquipamento> salaEquipamentos = new ArrayList<SalaEquipamento>();
        sala.setEquipamentos(salaEquipamentos);
        SalaDTO salaDTO = salaBuilder.converterParaDto(sala);
        getMockMvc().perform(put("/api/salas/")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(salaBuilder.converterParaDto(sala)))
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void atualizarExcessao2() throws Exception { //irá testar quando zerar a quantidade de um equipamento obrigatório - deve retornar BAD REQUEST
        Sala sala = salaBuilder.construir();
        List<SalaEquipamento> salaEquipamentos = sala.getEquipamentos();
        for (int i = 0; i < salaEquipamentos.size(); i ++) {
            salaEquipamentos.get(i).setQuantidade(0);
        }
        sala.setEquipamentos(salaEquipamentos);
        getMockMvc().perform(put("/api/salas/")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(salaBuilder.converterParaDto(sala)))
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void deletarExcessao1() throws Exception { //irá testar quando apagar uma sala que não existe - deve retornar BAD REQUEST
        Sala sala = salaBuilder.construir();
        getMockMvc().perform(delete("/api/salas/" + 50))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void deletarExcessao2() throws Exception { //irá testar quando apagar uma sala que está reservada - deve retornar BAD REQUEST
        Reserva reserva = reservaBuilder.construir();

        getMockMvc().perform(delete("/api/salas/" + reserva.getSala().getId()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void listarPorIdExcessao() throws Exception { //irá testar quando apagar um elemento obrigatório - deve retornar BAD REQUEST
        Sala sb = salaBuilder.construir();
        getMockMvc().perform(get("/api/salas/" + 89))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}