package com.indra.elections.peruvian.util;

import android.content.SharedPreferences;

import com.indra.elections.peruvian.MainPeruvianActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by lm2a on 03/06/2017. Singleton to keep state during Transmission workflow
 */

public class Estados {

    private static Estados instance = null;
    private static final String PREFERENCES = "state_store";

    public static final int INITIAL_STATE = 0;
    public static final int LLENAR_RESUMEN_MESAS = 2;
    public static final int LLENAR_VOTOS_PARTIDOS = 4;
    public static final int FOTO_ENVIO_MENU = 5;
    public static final int ESCANEAR_CODIGO = 6;
    public static final int FOTOGRAFIAR_ACTA = 8;
    public static final int ENVIAR_DATOS = 10;


    public static final int ENVIO_ESCANEAR_CODIGO = 1;
    public static final int ENVIO_FOTOGRAFIAR_ACTA = 2;
    public static final int ENVIO_ENVIAR_DATOS = 3;


    public static final int DATA_READY_TO_SEND_STATE = 7;
    public static final int DATA_SENT_STATE = 8;



    int mCurrentState;



    MainPeruvianActivity mMainPeruvianActivity;
    MesaVO mesaVO;

    //estas dos variables manejas estado en pantallas que requieren estado (Tabs y workflow de envio)
    int tabEleccionCorriente;//valores posibles 0, 1 y 2
    int envioWorkflowEstado;//0 (debe Escanear), 1 (debe Fotografiar) y 2 (Debe enviar)

    public static Estados getInstance() {
        if(instance == null) {
            instance = new Estados();
        }
        return instance;
    }

    protected Estados() {

    }

    public void initiate(){
        //setInitialState();
    }


    public int getCurrentState() {
        return mCurrentState;
    }

    /**
     * metodo poco otodoxo ;-), pero necesario para permitir al singleton guardar el estado
     * @param mMainPeruvianActivity
     */
    public void setMainPeruvianActivity(MainPeruvianActivity mMainPeruvianActivity) {
        this.mMainPeruvianActivity = mMainPeruvianActivity;
    }

    /**
     * cada vez que cambio el estado lo salvo por si se interrumpe el proceso
     * @param mCurrentState
     */
    public void setCurrentState(int mCurrentState) {
        this.mCurrentState = mCurrentState;
        saveState();
        mMainPeruvianActivity.navegation();
    }

    public MesaVO getMesaVO() {
        return mesaVO;
    }

    public void setMesaVO(MesaVO mesaVO) {
        this.mesaVO = mesaVO;
    }

    private void saveState(){
        SharedPreferences.Editor editor = mMainPeruvianActivity.getSharedPreferences(PREFERENCES, MODE_PRIVATE).edit();
        editor.putInt("idState", mCurrentState);
        editor.commit();
    }

//    private void setInitialState(){
//        SharedPreferences prefs = mMainPeruvianActivity.getSharedPreferences(PREFERENCES, MODE_PRIVATE);
//        mCurrentState = prefs.getInt("idState", 0);
//    }


    //----------fragments state method---------------------------


    public int getTabEleccionCorriente() {
        return tabEleccionCorriente;
    }

    public void setTabEleccionCorriente(int tabEleccionCorriente) {
        this.tabEleccionCorriente = tabEleccionCorriente;
    }

    public int getEnvioWorkflowEstado() {
        return envioWorkflowEstado;
    }

    public void setEnvioWorkflowEstado(int envioWorkflowEstado) {
        this.envioWorkflowEstado = envioWorkflowEstado;
    }
}
