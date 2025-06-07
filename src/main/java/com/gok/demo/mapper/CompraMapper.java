package com.gok.demo.mapper;

import com.gok.demo.database.entity.ClienteCompraEntity;
import com.gok.demo.database.entity.ProdutoEntity;
import com.gok.demo.dto.CompraItemDto;
import com.gok.demo.dto.DetalheCompraDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Mapper(componentModel = "spring")
public interface CompraMapper {

    CompraMapper INSTANCE = Mappers.getMapper(CompraMapper.class);

    @Mappings({
            @Mapping(source = "produto.codigo", target = "codigoProduto"),
            @Mapping(source = "produto.tipoVinho", target = "tipoProduto"),
            @Mapping(source = "produto.preco", target = "preco"),
            @Mapping(source = "compra.quantidade", target = "quantidade"),
            @Mapping(target = "valorTotal", expression = "java(new java.math.BigDecimal(produto.getPreco() * compra.getQuantidade()).setScale(2, java.math.RoundingMode.HALF_UP).doubleValue())")
    })
    CompraItemDto toItemDto(ClienteCompraEntity.Compra compra, ProdutoEntity produto);


    @Mapping(source = "produto.codigo", target = "codigoProduto")
    @Mapping(source = "produto.tipoVinho", target = "tipoProduto")
    @Mapping(source = "produto.preco", target = "preco")
    @Mapping(source = "cliente.nome", target = "nome")
    @Mapping(source = "cliente.cpf", target = "cpf")
    @Mapping(source = "compra.quantidade", target = "quantidade")
    @Mapping(
            target = "valorTotal",
            expression = "java(roundToTwoDecimals(produto.getPreco() * compra.getQuantidade()))"
    )
    DetalheCompraDto toDetalheCompraDTO(ClienteCompraEntity cliente, ProdutoEntity produto, ClienteCompraEntity.Compra compra);

    default Double roundToTwoDecimals(double valor) {
        return new BigDecimal(valor).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
