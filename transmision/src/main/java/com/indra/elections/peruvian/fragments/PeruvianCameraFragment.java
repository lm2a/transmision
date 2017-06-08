package com.indra.elections.peruvian.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.client.android.BeepManager;
import com.indra.elections.R;
import com.indra.elections.peruvian.MainPeruvianActivity;
import com.indra.elections.peruvian.camera.CameraView;
import com.indra.elections.peruvian.tasks.ScanImageTask;
import com.indra.elections.peruvian.util.Estados;
import com.indra.elections.views.fragments.FragmentEnvioPhotos;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.Executors;

public class PeruvianCameraFragment extends Fragment
{

	private View rootView;
	private MainPeruvianActivity activity;
	private static Camera mCamera = null;
	private CameraView mCameraView = null;
    ProgressDialog mDialog;

	public PeruvianCameraFragment()
	{

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		this.activity = (MainPeruvianActivity) activity;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.peruvian_camera_fragment, null, false);
        return rootView;
	}

	@Override
	public void onResume() {
        super.onResume();
        initComponents();
	}

	@Override
	public void onPause() {
        super.onPause();
	}

    private void init(){
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
    }
	protected void initComponents()
	{
		try{
			mCamera = Camera.open();//you can use open(int) to use different cameras
		} catch (Exception e){
			Log.d("ERROR", "Failed to get camera: " + e.getMessage());
		}

		if(mCamera != null) {
			mCameraView = new CameraView(getActivity(), mCamera);//create a SurfaceView to show camera data
			FrameLayout camera_view = (FrameLayout)getActivity().findViewById(R.id.camera_view);
			camera_view.addView(mCameraView);//add the SurfaceView to the layout
		}

		//btn to close the application
		ImageButton imgClose = (ImageButton)getActivity().findViewById(R.id.imgClose);
		imgClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				System.exit(0);
			}
		});
        ImageButton imgShot = (ImageButton)getActivity().findViewById(R.id.imgPhoto);
        imgShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shot();
            }
        });
	}

    @Override
    public void onDestroyView() {
        if (mCamera != null) {
            mCamera.release();
        }
        super.onDestroy();
    }


	public void back()
	{
/*		progress = ProgressDialog.show(rootView.getContext(), null, getString(R.string.procesando), true);

		surfaceView.setVisibility(View.GONE);
		surfacegb.setVisibility(View.GONE);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim).remove(PeruvianCameraFragment.this).commit();
		if(fragmentManager.getBackStackEntryCount() > 0)
		{
			fragmentManager.popBackStack();
		}
		
		rootView.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				progress.dismiss();
			}
		},1000);*/
	}

    public void shot(){
        MyShutterCallBack shutter = new MyShutterCallBack();
        MyRawPictureCallback pictureRaw = new MyRawPictureCallback();
        MyJpgPictureCallback pictureJPG = new MyJpgPictureCallback();
        mCamera.takePicture(shutter, null, pictureJPG);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PeruvianCameraFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PeruvianCameraFragment newInstance() {
        PeruvianCameraFragment fragment = new PeruvianCameraFragment();
        return fragment;
    }

    private class MyShutterCallBack implements Camera.ShutterCallback {

        @Override
        public void onShutter() {
                //aca puedo poner un sonido de camara...
            AudioManager mgr = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
            mgr.playSoundEffect(AudioManager.FLAG_PLAY_SOUND);
        }
    }
    private class MyJpgPictureCallback implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap m_bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            m_bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] byteArray = stream.toByteArray();
            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
            //SEND IT
            photoOk();
            }
        }


    private void photoOk(){
            Estados.getInstance().setCurrentState(Estados.FOTO_ENVIO_MENU);
            Estados.getInstance().setEnvioWorkflowEstado(Estados.ENVIO_ENVIAR_DATOS);
            ((MainPeruvianActivity)getActivity()).navegation();

    }

    private class MyRawPictureCallback implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap m_bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            m_bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] byteArray = stream.toByteArray();
            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
            //SEND IT

 /*           try {
                //SEND IT

                RequestParams params = new RequestParams();

                params.put("city", city2);
                params.put("country", country);
                params.put("langi", langi);
                params.put("lati", lati);
                params.put("details", details);
                params.put("type", type);
                params.put("ID", myID);
                params.put("image", encodedImage);
                AsyncHttpClient client = new AsyncHttpClient();
                client.post(getResources().getString(R.string.app_server_Name)
                        + "addnewplace.php", params, new TextHttpResponseHandler() {


                    @Override
                    public void onStart() {
                        Toast.makeText(getApplicationContext(),"saving...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                        Toast.makeText(getApplicationContext(), responseString, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {

                        Toast.makeText(getApplicationContext(), responseString, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFinish() {

                    }
                });

            } catch (Exception e) {
                // TODO: handle exception
            } finally {
                frameLayout.destroyDrawingCache();
            }
        }        }*/
        }


        public void addPhoto(String path) {
		
/*
		surfaceView.setVisibility(View.GONE);
		surfacegb.setVisibility(View.GONE);
		
		FragmentEnvioPhotos fragment = new FragmentEnvioPhotos();
		Bundle bundle = new Bundle();
		bundle.putStringArray("paths", new String[]{path});
		bundle.putString("mesa", mesa);
		bundle.putString("codele", codele);
		fragment.setArguments(bundle);

		FragmentManager fragmentManager = getFragmentManager();
		final FragmentTransaction fragmentManager1 = fragmentManager.beginTransaction()
				.setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim)
				.replace(R.id.content_frame, fragment, FragmentEnvioPhotos.class.getName());

		fragmentManager1.commit();
		
		rootView.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				progress.dismiss();
			}
		},1000);
*/
        }

        public void showProgress() {
            mDialog = ProgressDialog.show(rootView.getContext(), null, getString(R.string.procesando), true);
        }
    }

}
