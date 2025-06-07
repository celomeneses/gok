package com.gok.demo.database.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "clientes_compras")
public class ClienteCompraEntity {

    @Id
    @JsonProperty("_id")
    private String id;

    private String nome;

    private String cpf;

    private List<Compra> compras;

    @Setter
    @Getter
    public static class Compra {
        private String codigo;
        private Integer quantidade;
    }
}
