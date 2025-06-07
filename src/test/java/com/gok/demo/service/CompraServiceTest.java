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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompraServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ClienteCompraRepository clienteCompraRepository;

    @Mock
    private CompraMapper compraMapper;

    @InjectMocks
    private CompraService compraService;

    @Test
    void deveObterClientesMaisFieis() {
        ClienteComprasDto cliente1 = new ClienteComprasDto();
        cliente1.setCpf("111");
        cliente1.setNome("Cliente A");
        cliente1.setValorTotalCompras(500.0);

        ClienteComprasDto cliente2 = new ClienteComprasDto();
        cliente2.setCpf("222");
        cliente2.setNome("Cliente B");
        cliente2.setValorTotalCompras(800.0);

        ClienteComprasDto cliente3 = new ClienteComprasDto();
        cliente3.setCpf("333");
        cliente3.setNome("Cliente C");
        cliente3.setValorTotalCompras(300.0);

        ClienteComprasDto cliente4 = new ClienteComprasDto();
        cliente4.setCpf("444");
        cliente4.setNome("Cliente D");
        cliente4.setValorTotalCompras(1000.0);

        List<ClienteComprasDto> lista = List.of(cliente1, cliente2, cliente3, cliente4);

        CompraService spyService = spy(compraService);
        doReturn(lista).when(spyService).listarComprasAgrupadasPorCliente();

        List<ClienteFielDto> resultado = spyService.obterClientesMaisFieis();

        assertEquals(3, resultado.size());
        assertEquals("444", resultado.get(0).getCpf());
        assertEquals("222", resultado.get(1).getCpf());
        assertEquals("111", resultado.get(2).getCpf());
    }

    @Test
    void deveObterRecomendacao() {
        ClienteCompraEntity clienteEntity = new ClienteCompraEntity();
        clienteEntity.setNome("Nome Cliente");
        clienteEntity.setCpf("12345678900");

        ClienteCompraEntity.Compra compra = new ClienteCompraEntity.Compra();
        compra.setCodigo("1");
        clienteEntity.setCompras(List.of(compra));

        ProdutoEntity produtoEntity = new ProdutoEntity();
        produtoEntity.setCodigo(1);
        produtoEntity.setTipoVinho("Tipo");
        produtoEntity.setAnoCompra(2023L);

        CompraItemDto itemDto = CompraItemDto.builder()
                .codigoProduto(1)
                .tipoProduto("Tipo")
                .quantidade(5)
                .build();

        when(clienteCompraRepository.findAll()).thenReturn(List.of(clienteEntity));
        when(produtoRepository.findByCodigo(1)).thenReturn(produtoEntity);
        when(compraMapper.toItemDto(compra, produtoEntity)).thenReturn(itemDto);

        List<Map<String, Object>> resultado = compraService.obterRecomendacao();

        assertEquals(1, resultado.size());
        Map<String, Object> map = resultado.get(0);
        assertEquals("Nome Cliente", map.get("nome"));
        assertEquals("12345678900", map.get("cpf"));
        assertEquals(1, map.get("codigo_produto"));
        assertEquals("Tipo", map.get("tipo_produto"));
        assertEquals(5, map.get("quantidade"));
    }

    @Test
    void deveObterMaiorCompraPorAno() {
        ClienteCompraEntity cliente = new ClienteCompraEntity();
        cliente.setNome("Nome");
        cliente.setCpf("cpf");

        ClienteCompraEntity.Compra compra1 = new ClienteCompraEntity.Compra();
        compra1.setCodigo("1");

        ClienteCompraEntity.Compra compra2 = new ClienteCompraEntity.Compra();
        compra2.setCodigo("2");

        cliente.setCompras(List.of(compra1, compra2));

        ProdutoEntity produto1 = new ProdutoEntity();
        produto1.setAnoCompra(2023L);
        produto1.setCodigo(1);

        ProdutoEntity produto2 = new ProdutoEntity();
        produto2.setAnoCompra(2023L);
        produto2.setCodigo(2);

        when(clienteCompraRepository.findAll()).thenReturn(List.of(cliente));
        when(produtoRepository.findByCodigo(1)).thenReturn(produto1);
        when(produtoRepository.findByCodigo(2)).thenReturn(produto2);

        DetalheCompraDto dto1 = DetalheCompraDto.builder().valorTotal(100.0).build();
        DetalheCompraDto dto2 = DetalheCompraDto.builder().valorTotal(200.0).build();

        when(compraMapper.toDetalheCompraDTO(cliente, produto1, compra1)).thenReturn(dto1);
        when(compraMapper.toDetalheCompraDTO(cliente, produto2, compra2)).thenReturn(dto2);

        Optional<DetalheCompraDto> maiorCompra = compraService.obterMaiorCompraPorAno(2023L);

        assertTrue(maiorCompra.isPresent());
        assertEquals(200.0, maiorCompra.get().getValorTotal());
    }

    @Test
    void deveListarComprasAgrupadasPorCliente() {
        ClienteCompraEntity cliente = new ClienteCompraEntity();
        cliente.setNome("Nome Cliente");
        cliente.setCpf("cpf");

        ClienteCompraEntity.Compra compra = new ClienteCompraEntity.Compra();
        compra.setCodigo("1");

        cliente.setCompras(List.of(compra));

        ProdutoEntity produto = new ProdutoEntity();
        produto.setCodigo(1);
        produto.setAnoCompra(2023L);

        CompraItemDto itemDto = CompraItemDto.builder()
                .valorTotal(150.0)
                .build();

        when(clienteCompraRepository.findAll()).thenReturn(List.of(cliente));
        when(produtoRepository.findByCodigo(1)).thenReturn(produto);
        when(compraMapper.toItemDto(compra, produto)).thenReturn(itemDto);

        List<ClienteComprasDto> resultado = compraService.listarComprasAgrupadasPorCliente();

        assertEquals(1, resultado.size());
        ClienteComprasDto dto = resultado.get(0);
        assertEquals("Nome Cliente", dto.getNome());
        assertEquals("cpf", dto.getCpf());
        assertEquals(150.0, dto.getValorTotalCompras());
        assertEquals(1, dto.getCompras().size());
    }
}

