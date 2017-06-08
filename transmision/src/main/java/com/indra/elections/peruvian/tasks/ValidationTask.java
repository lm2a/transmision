package com.indra.elections.peruvian.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.indra.elections.model.Candidatura;
import com.indra.elections.model.FTIPELE;
import com.indra.elections.peruvian.util.Estados;
import com.indra.elections.peruvian.util.MesaVO;
import com.indra.elections.peruvian.util.ResultadosVO;
import com.indra.elections.peruvian.util.Util;
import com.indra.elections.peruvian.util.ValueObject;
import com.indra.elections.utils.ElectionsDBHelper;

/**
 * Created by lm2a on 03/06/2017.
 * AsyncTask polivalente para validar tanto la mesa como los resultados electorales
 */

public class ValidationTask extends AsyncTask<Void, Integer, Boolean> {

    Context mContext;

    //mensaje para mostrar al usuario TODO: mandarlo a strings.xml
    String mMensaje;

    //interface para mandar diferentes objetos a la misma asynctask
    ValueObject mValueObject;



    @Override
    protected Boolean doInBackground(Void... params) {
        Boolean result;
        switch(mValueObject.getVOId()){
            case ValueObject.MESA_VO:
                //procesar una mesa
                MesaVO mesa = (MesaVO) mValueObject;
                result = checkMesas(mesa);
                break;
            case ValueObject.RESULTADO_VO:
                //procesar resultados electorales
                ResultadosVO resultados = (ResultadosVO) mValueObject;
                result = checkResultados(resultados);
                break;
            default:
                Log.d("ValidationTask","Valor de objeto desconocido en Validation task");
                result = false;
                break;
        }
        return result;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    public ValidationTask(Context context, ValueObject valueObject) {
        super();
        mContext = context;
        mValueObject = valueObject;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(aBoolean) {
            Toast.makeText(mContext, "Tarea finalizada! OK",
                    Toast.LENGTH_SHORT).show();
            Estados.getInstance().setCurrentState(Estados.LLENAR_VOTOS_PARTIDOS);
        }else{
            Toast.makeText(mContext, mMensaje,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }


    public boolean checkMesas(MesaVO mesa){
        //traigo los resultados de la BBDD del epb del tablet
        int v = getVotantesFromDB();
        int c = getCensoFromDB();
        boolean result = true;
        if(mesa.votosTotales != v){
            //cantidad de votantes introducida difiere de la real
            mMensaje = "La cantidad de votantes introducida difiere de la real";
            result = false;
        }
        else if(mesa.cantidadDeSobres > c){
            //votaron mas de los que tocaba
            mMensaje = "Se ingresaron mas votos que votantes hubo";
            result = false;
        }
        else if(mesa.votosImpugnados > c){
            //se impugnaron mas votos de los que votaron
            mMensaje = "Se impugnaron mas votos que votantes hubo";
            result = false;
        }else{
            //TODO CREAR EL JSON
        }
        //guardo el objeto en el singleton para usarlo luegp
        Estados.getInstance().setMesaVO(mesa);

        return result;
    }

    public boolean checkResultados(ResultadosVO resultados){
        int sumaVotosCandidaturas = 0;
        Candidatura[] listaCandidaturas;
        boolean result = true;

        //recupero datos
        listaCandidaturas = ElectionsDBHelper.getCandidaturas(resultados.codigoEleccion);
        Integer [] votosCandidaturas = new Integer [listaCandidaturas.length];
        FTIPELE eleccion = FTIPELE.getFTIPELEbyCODELE(mContext, resultados.codigoEleccion);
        MesaVO mesaVO = Estados.getInstance().getMesaVO();
        int censo = getCensoFromDB();//podria usar el de mesaVO
        sumaVotosCandidaturas = getTotalDeVotosEnTodasLasCandidaturas(listaCandidaturas);


        if(sumaVotosCandidaturas + mesaVO.votosImpugnados > censo){
            mMensaje = "Los votos totales mas los impugnados superan a los votantes";
            result = false;
        }else{
            //TODO CREAR EL JSON
        }

        return result;
    }


    public int getTotalDeVotosEnTodasLasCandidaturas(Candidatura[] listaCandidaturas){
        for(int i=0; i < listaCandidaturas.length; i++){

//TODO adaptar lo que sigue
/*
            int votosCandidato = getIntValue(fragmentEnvioEscrutinio.getItemValue(listaCandidaturas[i].get_orden()));
            votosCandidaturas[i] = votosCandidato;
            sumaVotosCandidaturas += votosCandidaturas[i];
            //Al recorrer, armar el JSON
            */
        }
        return -1;
    }

    //---------------- DDBB -----------------------------------

    //TODO probar en el tablet
    public int getVotantesFromDB() {
        String votantesSQL = "sqlite3 /data/data/com.indra.identifier/databases/epb.s3db \"Select count(*) from TCENSO where has_voted = 1\";";
        String votantes = Util.execCommand(mContext, votantesSQL);
        //TODO en real usar esto: return Util.getIntFromString(votantes);
        return 5;
    }

    public int getCensoFromDB() {
        String censoSQL = "sqlite3 /data/data/com.indra.identifier/databases/epb.s3db \"Select census from TSTATUS\";";
        String censo = Util.execCommand(mContext, censoSQL);
        //TODO en real usar esto: return Util.getIntFromString(censo);
        return 5;
    }
}
