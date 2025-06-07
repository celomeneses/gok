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
public class DetalheCompraDto {

    private String nome;

    private String cpf;

    @JsonProperty("codigo_produto")
    private Integer codigoProduto;

    @JsonProperty("tipo_produto")
    private String tipoProduto;

    private Double preco;

    private Integer quantidade;

    @JsonProperty("valor_total")
    private Double valorTotal;
}
