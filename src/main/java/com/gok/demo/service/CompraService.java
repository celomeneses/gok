package com.gok.demo.service;

import com.gok.demo.database.entity.ClienteCompraEntity;
import com.gok.demo.database.entity.ProdutoEntity;
import com.gok.demo.database.repository.ClienteCompraRepository;
import com.gok.demo.database.repository.ProdutoRepository;
import com.gok.demo.dto.ClienteComprasDto;
import com.gok.demo.dto.ClienteFielDto;
import com.gok.demo.dto.CompraItemDto;
import com.gok.demo.dto.DetalheCompraDto;
import com.gok.demo.mapper.CompraMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompraService {

    private final ProdutoRepository produtoRepository;
    private final ClienteCompraRepository clienteCompraRepository;
    private final CompraMapper compraMapper;

    public List<Map<String, Object>>  obterRecomendacao() {

        List<Map<String, Object>> recomendacoes = new ArrayList<>();
        List<ClienteComprasDto> clienteCompras = this.listarComprasAgrupadasPorCliente();
        log.info("Iniciando a busca por recomendações.");
        for(ClienteComprasDto clienteCompra : clienteCompras){
            Optional<CompraItemDto> maisComprado = clienteCompra.getCompras().stream()
                    .max(Comparator.comparing(CompraItemDto::getQuantidade));

            maisComprado.ifPresent(produto -> {
                Map<String, Object> info = new HashMap<>();
                info.put("nome", clienteCompra.getNome());
                info.put("cpf", clienteCompra.getCpf());
                info.put("codigo_produto", produto.getCodigoProduto());
                info.put("tipo_produto", produto.getTipoProduto());
                info.put("quantidade", produto.getQuantidade());
                recomendacoes.add(info);
            });
        }
        log.info("Quantidade de recomendações encontradas: {}.", recomendacoes.size());
        return recomendacoes;
    }

    public List<ClienteFielDto> obterClientesMaisFieis() {
        List<ClienteComprasDto> top3 = this.listarComprasAgrupadasPorCliente().stream()
                .sorted(Comparator.comparing(ClienteComprasDto::getValorTotalCompras).reversed())
                .limit(3)
                .toList();
        log.info("Retornando top3 clientes.");
        return top3.stream()
                .map(cliente -> ClienteFielDto.builder()
                        .cpf(cliente.getCpf())
                        .nome(cliente.getNome())
                        .valorTotalCompras(cliente.getValorTotalCompras())
                        .build())
                .toList();
    }

    public Optional<DetalheCompraDto> obterMaiorCompraPorAno(Long anoDesejado) {
        log.info("Obtendo maior compra do ano por cliente.");
        return clienteCompraRepository.findAll().stream()
                .flatMap(cliente -> cliente.getCompras().stream()
                        .map(compra -> {
                            ProdutoEntity produto = produtoRepository.findByCodigo(Integer.valueOf(compra.getCodigo()));
                            if (produto != null && produto.getAnoCompra().equals(anoDesejado)) {
                                return compraMapper.toDetalheCompraDTO(cliente, produto, compra);
                            }
                            return null;
                        }))
                .filter(Objects::nonNull)
                .max(Comparator.comparingDouble(DetalheCompraDto::getValorTotal));
    }

    public List<ClienteComprasDto> listarComprasAgrupadasPorCliente() {
        log.info("Buscando todas as compras efetuadas.");
        List<ClienteCompraEntity> clientes = clienteCompraRepository.findAll();
        List<ClienteComprasDto> resultado = new ArrayList<>();

        for (ClienteCompraEntity cliente : clientes) {
            ClienteComprasDto clienteDTO = new ClienteComprasDto();
            clienteDTO.setNome(cliente.getNome());
            clienteDTO.setCpf(cliente.getCpf());

            Double valorTotalCompras = 0d;
            List<CompraItemDto> itens = new ArrayList<>();
            for (ClienteCompraEntity.Compra compra : cliente.getCompras()) {
                Integer codigo = Integer.parseInt(compra.getCodigo());
                log.info("Buscando o produto codigo: {}.", codigo);
                ProdutoEntity produto = produtoRepository.findByCodigo(codigo);
                log.info("Produto encontrado: {}.", produto);
                if (produto != null) {
                    CompraItemDto item = compraMapper.toItemDto(compra, produto);
                    itens.add(item);
                    valorTotalCompras += item.getValorTotal() != null ? item.getValorTotal() : 0;
                }
            }

            itens.sort(Comparator.comparingDouble(CompraItemDto::getValorTotal));
            clienteDTO.setValorTotalCompras(BigDecimal.valueOf(valorTotalCompras)
                    .setScale(2, java.math.RoundingMode.HALF_UP).doubleValue());
            clienteDTO.setCompras(itens);
            resultado.add(clienteDTO);
            resultado.sort(Comparator.comparingDouble(ClienteComprasDto::getValorTotalCompras));
        }

        return resultado;
    }


}
