package com.gok.demo.controller.impl;

import com.gok.demo.controller.Compra;
import com.gok.demo.dto.ClienteFielDto;
import com.gok.demo.service.CompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CompraController implements Compra {

    private final CompraService service;

    @Override
    public ResponseEntity<?> obterRecomendacao() {
        return ResponseEntity.ok(service.obterRecomendacao());
    }

    @Override
    public ResponseEntity<?> obterMaiorCompra(@PathVariable Long ano) {
        return service.obterMaiorCompraPorAno(ano)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<?> obterCompras(){
        return ResponseEntity.ok(service.listarComprasAgrupadasPorCliente());
    }

    @Override
    public ResponseEntity<?> buscarTop3ClientesMaisFieis() {
        List<ClienteFielDto> clientesFieis = service.obterClientesMaisFieis();
        return ResponseEntity.ok(clientesFieis);
    }
}
