package com.gok.demo.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.math.RoundingMode;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class CompraMapperTest {

    CompraMapper INSTANCE = Mappers.getMapper(CompraMapper.class);

    @Test
    void deveArredondarParaDuasCasasDecimais() {
        double valor = 123.4567;
        double esperado = new java.math.BigDecimal(valor).setScale(2, RoundingMode.HALF_UP).doubleValue();
        double resultado = INSTANCE.roundToTwoDecimals(valor);
        assertEquals(esperado, resultado);
    }
}


