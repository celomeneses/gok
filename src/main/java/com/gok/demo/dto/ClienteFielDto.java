package com.gok.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteFielDto {

    private String nome;

    private String cpf;

    @JsonProperty("valor_total_compras")
    private Double valorTotalCompras;
}
