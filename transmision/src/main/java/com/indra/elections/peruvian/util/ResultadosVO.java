package com.indra.elections.peruvian.util;

import java.util.Map;

/**
 * Created by lm2a on 03/06/2017.
 */

public class ResultadosVO implements ValueObject {

    public String codigoEleccion;
    public Map<String, Integer> partidosResultados;

    public ResultadosVO(Map<String, Integer> partidosResultados, String codigoEleccion) {
        this.partidosResultados = partidosResultados;
        this.codigoEleccion = codigoEleccion;
    }

    @Override
    public int getVOId() {
        return ValueObject.RESULTADO_VO;
    }
}
