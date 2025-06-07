package com.gok.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteComprasDto {

    private String nome;

    private String cpf;

    private List<CompraItemDto> compras;

    @JsonProperty("valor_total_compras")
    private Double valorTotalCompras;
}
