package com.indra.elections.peruvian.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by lm2a on 03/06/2017.
 */

public class Util {

    public static final String ELECCION_PRESIDENTE_VICEPRESIDENTES = "05400109990011"; // Eleccion 1
    public static final String ELECCION_PRESIDENTE_REGIONAL_VICEPRESIDENTE = "05500109990031"; // Eleccion 2
    public static final String ELECCION_CONSEJERO_REGIONAL = "05600109990021"; // Eleccion 3



    public static int getIntFromString(String integer){
       if(integer != null) {
           try {
               return Integer.parseInt(integer);
           } catch (NumberFormatException e) {
               return -1;
           }
       }else{
           return -1;
       }
    }

    public static String execCommand(Context context, String command)
    {

        Runtime runtime = Runtime.getRuntime();
        Process proc = null;
        OutputStreamWriter osw = null;
        BufferedReader reader = null;
        StringBuffer output = null;

        if(command.compareTo("")!=0)
        {
            try { // Run Script

                proc = runtime.exec("su");
                osw = new OutputStreamWriter(proc.getOutputStream());
                osw.write(command);

                osw.flush();
                osw.close();

                Log.i(context.getPackageName(), command);

                reader = new BufferedReader(
                        new InputStreamReader(proc.getInputStream()));
                int read;
                char[] buffer = new char[4096];
                output = new StringBuffer();
                while ((read = reader.read(buffer)) > 0) {
                    output.append(buffer, 0, read);
                }
                reader.close();

                try {
                    if (proc != null)
                        proc.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (osw != null) {
                    try {
                        osw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                if (proc != null)
                    proc.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(output!=null)
            return output.toString();
        else
            return "";
    }


    public static boolean isRightPollingAct(String barcodeValue, String codElection){
        if(barcodeValue.equals(ELECCION_PRESIDENTE_VICEPRESIDENTES)){
            if(codElection.equals("1")){
                return true;
            }else{
                return false;
            }
        } else if(barcodeValue.equals(ELECCION_PRESIDENTE_REGIONAL_VICEPRESIDENTE)){
            if(codElection.equals("2")){
                return true;
            }else{
                return false;
            }
        }else if(barcodeValue.equals(ELECCION_CONSEJERO_REGIONAL)){
            if(codElection.equals("3")){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

}
