package com.gok.demo.controller;


import com.gok.demo.controller.impl.CompraController;
import com.gok.demo.dto.ClienteComprasDto;
import com.gok.demo.dto.ClienteFielDto;
import com.gok.demo.dto.DetalheCompraDto;
import com.gok.demo.service.CompraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

class CompraControllerTest {

    private CompraService compraService;
    private CompraController compraController;

    @BeforeEach
    void setUp() {
        compraService = mock(CompraService.class);
        compraController = new CompraController(compraService);
    }

    @Test
    void deveObterRecomendacao() {
        Map<String, Object> recomendacao = new HashMap<>();
        recomendacao.put("mensagem", "Recomendação personalizada");

        List<Map<String, Object>> recomendacoesMock = List.of(recomendacao);

        when(compraService.obterRecomendacao()).thenReturn(recomendacoesMock);

        ResponseEntity<?> response = compraController.obterRecomendacao();

        assertEquals(200, response.getStatusCode().value());

        List<?> body = (List<?>) response.getBody();
        assertFalse(body.isEmpty());

        Map<?, ?> resultado = (Map<?, ?>) body.get(0);
        assertEquals("Recomendação personalizada", resultado.get("mensagem"));

        verify(compraService, times(1)).obterRecomendacao();
    }

    @Test
    void deveObterMaiorCompraQuandoEncontrado() {
        Long ano = 2023L;
        DetalheCompraDto detalhe = new DetalheCompraDto();
        detalhe.setCpf("12345678900");
        detalhe.setNome("João da Silva");

        when(compraService.obterMaiorCompraPorAno(ano)).thenReturn(Optional.of(detalhe));

        ResponseEntity<?> response = compraController.obterMaiorCompra(ano);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(detalhe, response.getBody());
    }

    @Test
    void deveRetornarNotFoundQuandoNaoHouverMaiorCompraNoAno() {
        Long ano = 2025L;

        when(compraService.obterMaiorCompraPorAno(ano)).thenReturn(Optional.empty());

        ResponseEntity<?> response = compraController.obterMaiorCompra(ano);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void deveRetornarListaDeComprasAgrupadasPorCliente() {
        ClienteComprasDto dto1 = new ClienteComprasDto();
        dto1.setNome("Maria");
        dto1.setCpf("12345678900");
        dto1.setValorTotalCompras(500.0);

        ClienteComprasDto dto2 = new ClienteComprasDto();
        dto2.setNome("João");
        dto2.setCpf("98765432100");
        dto2.setValorTotalCompras(800.0);

        List<ClienteComprasDto> mockLista = Arrays.asList(dto1, dto2);

        when(compraService.listarComprasAgrupadasPorCliente()).thenReturn(mockLista);

        ResponseEntity<?> response = compraController.obterCompras();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockLista, response.getBody());

        verify(compraService, times(1)).listarComprasAgrupadasPorCliente();
    }

    @Test
    void deveObterTop3ClientesMaisFieis() {
        List<ClienteFielDto> lista = List.of(new ClienteFielDto());
        when(compraService.obterClientesMaisFieis()).thenReturn(lista);
        ResponseEntity<?> response = compraController.buscarTop3ClientesMaisFieis();
        assertEquals(200, response.getStatusCode().value());
        assertEquals(lista, response.getBody());
    }
}

