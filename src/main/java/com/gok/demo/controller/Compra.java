package com.gok.demo.controller;

import com.gok.demo.dto.ClienteComprasDto;
import com.gok.demo.dto.ClienteFielDto;
import com.gok.demo.dto.DetalheCompraDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Tag(name = "Endpoints demo", description = "Endpoint de consultas")
@RequestMapping("/demo/v1")
public interface Compra {

    @GetMapping("/compras")
    @Operation(
            summary = "Retornar uma lista de compras ordenadas de forma crescente por valor, deve conter o nome dos clientes, cpf dos clientes, dados dos produtos, quantidade das compras e valores totais de cada compra.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ClienteComprasDto.class)
                            )
                    )
            }
    )
    ResponseEntity<?> obterCompras();

    @GetMapping("/maior-compra/{ano}")
    @Operation(
            summary = "Retornar a maior compra do ano informado com os dados da compra disponibilizada, deve ter o nome do cliente, cpf do cliente, dado do produto quantidade da compra e seu valor total.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DetalheCompraDto.class)
                            )
                    )
            }
    )
    ResponseEntity<?> obterMaiorCompra(@PathVariable Long ano);

    @GetMapping("/clientes-fieis")
    @Operation(
            summary = "Retornar o Top 3 clientes mais fiéis, clientes que possuem mais compras recorrentes com maiores valores.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ClienteFielDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<?> buscarTop3ClientesMaisFieis();

    @GetMapping("/recomendacao/cliente/tipo")
    @Operation(
            summary = "Retornar uma recomendação de vinho baseada nos tipos de vinho que o cliente mais compra.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class)
                            )
                    )
            }
    )
    public ResponseEntity<?> obterRecomendacao();
}
