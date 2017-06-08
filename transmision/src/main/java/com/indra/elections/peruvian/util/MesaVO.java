package com.indra.elections.peruvian.util;

import android.renderscript.Sampler;

/**
 * Created by lm2a on 03/06/2017.
 */

public class MesaVO implements ValueObject {

    public int cantidadDeSobres;
    public int votosTotales;
    public int votosImpugnados;


    public MesaVO(int cantidadDeSobres, int votosTotales, int votosImpugnados) {
        this.cantidadDeSobres = cantidadDeSobres;
        this.votosTotales = votosTotales;
        this.votosImpugnados = votosImpugnados;
    }

    @Override
    public int getVOId() {
        return ValueObject.MESA_VO;
    }
}
