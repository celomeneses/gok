package com.gok.demo.database.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "produtos")
public class ProdutoEntity {

    @Id
    @JsonProperty("_id")
    private String id;

    private Integer codigo;

    @Field("tipo_vinho")
    private String tipoVinho;

    private Double preco;

    private String safra;

    @Field("ano_compra")
    private Long anoCompra;
}
