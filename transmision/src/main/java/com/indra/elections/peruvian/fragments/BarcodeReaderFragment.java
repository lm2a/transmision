package com.indra.elections.peruvian.fragments;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;
import com.indra.elections.peruvian.MainPeruvianActivity;
import com.indra.elections.peruvian.util.Estados;
import com.indra.elections.peruvian.util.Util;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BarcodeReaderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BarcodeReaderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BarcodeReaderFragment extends Fragment {

    public static final String TAG = BarcodeReaderFragment.class.getName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_COD_ELECCION = "cod_eleccion";


    // TODO: Rename and change types of parameters
    private String mParam;


    private OnFragmentInteractionListener mListener;

    public BarcodeReaderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param Parameter 1.
     * @return A new instance of fragment BarcodeReaderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BarcodeReaderFragment newInstance(String param) {
        BarcodeReaderFragment fragment = new BarcodeReaderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COD_ELECCION, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_COD_ELECCION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // No layout
        scanBar();
        return null;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

/*    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BarcodeReaderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BarcodeReaderFragment newInstance() {
        BarcodeReaderFragment fragment = new BarcodeReaderFragment();
        return fragment;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

/*    IntentIntegrator integrator = new IntentIntegrator(yourActivity);
    integrator.initiateScan();
    Second, add this to your Activity to handle the result:

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            // handle scan result
        }
        // else continue with any other code you need in the method
        ...
    }*/

boolean justOneTime = true;

    // fucntion to scan barcode
    public void scanBar() {
        if(justOneTime) {
            justOneTime=false;
            Intent intent = new Intent(getActivity().getApplicationContext(), CaptureActivity.class);
            intent.setAction("com.google.zxing.client.android.SCAN");
            intent.putExtra("SAVE_HISTORY", false);//TODO ?
            startActivityForResult(intent, 0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String barcode = data.getStringExtra("SCAN_RESULT");
                validarActa(barcode);
                Log.d(TAG, "contents: " + barcode);
            } else if (resultCode == RESULT_CANCELED) {
                validarActa("x"); //falla y muestra el mensaje
               Log.d(TAG, "RESULT_CANCELED");
            }
        }
    }


    public void validarActa(String barcode){
        boolean check = Util.isRightPollingAct(barcode, mParam);
        if(check){
            Toast.makeText(getActivity(), "Acta OK, puede fotografiar el acta", Toast.LENGTH_SHORT).show();
            Log.i(this.getClass().getName(), "Acta OK, puede fotografiar el acta");

            navegar(Estados.ENVIO_FOTOGRAFIAR_ACTA);
        }else{
            Toast.makeText(getActivity(), "Acta no corresponde a la eleccion", Toast.LENGTH_SHORT).show();
            Log.i(this.getClass().getName(), "Acta no corresponde a la eleccion");
            navegar(Estados.ENVIO_ESCANEAR_CODIGO);
        }
    }




    public void navegar(int state){
        Estados.getInstance().setCurrentState(Estados.FOTO_ENVIO_MENU);
        Estados.getInstance().setEnvioWorkflowEstado(state);
        ((MainPeruvianActivity)getActivity()).navegation();

    }
}
