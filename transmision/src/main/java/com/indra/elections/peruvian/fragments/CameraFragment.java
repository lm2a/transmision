package com.indra.elections.peruvian.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.client.android.BeepManager;
import com.indra.elections.NavigationActivity;
import com.indra.elections.R;

import com.indra.elections.peruvian.MainPeruvianActivity;
import com.indra.elections.peruvian.tasks.ScanImageTask;
import com.indra.elections.views.fragments.FragmentEnvioPhotos;

import java.io.IOException;
import java.util.concurrent.Executors;

/**
 * TODO clase de la aplicacion vieja, se debe ver si se la conserva en parte, en principio la reemplace por PeruvianCameraFragment
 */
public class CameraFragment extends Fragment implements AutoFocusCallback, TextureView.SurfaceTextureListener
{
	// Controles de constituci?n
	private View rootView;
	private MainPeruvianActivity activity;

	//Camera
	private TextureView surfaceView=null;
	private RelativeLayout surfacegb=null;
//	private SurfaceHolder surfaceHolder=null;
	private Camera mCamera=null;
	//	private BarcodeDetector detector= null;
	private BeepManager beepManager;

	private PictureCallback jpegCallback;
	private ProgressDialog progress;

	protected String mesa;
	protected String dc;
	private String codele;
	private SurfaceTexture surface;

	public CameraFragment()
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
		rootView = inflater.inflate(R.layout.fragment_camera, null, false);
		initComponents();
		//configureBackActionBar();
		try{

			//			detector = new BarcodeDetector.Builder(activity.getApplicationContext())
			//				                        .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.CODE_39)
			//				                        .build();
		} catch (Exception e)
		{
			e.printStackTrace();
		}


		return rootView;	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		beepManager.updatePrefs();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		beepManager.close();
	}

	protected void initComponents()
	{
		//activity.showOutKeyboard();
		this.mesa = "001";//getArguments().getString("mesa");
		this.codele = "1";//getArguments().getString("codele");

		this.surfaceView = (TextureView) rootView.findViewById(R.id.photo_preview);
		this.surfacegb = (RelativeLayout) rootView.findViewById(R.id.photo_bg);
//		this.surfaceHolder = surfaceView.getHolder();
		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		surfaceView.setSurfaceTextureListener(this);
		
		beepManager = new BeepManager(activity);

		jpegCallback = new PictureCallback() {
			public void onPictureTaken(byte[] data, Camera camera) {
				ScanImageTask async = new ScanImageTask(activity, data, CameraFragment.this);
				async.executeOnExecutor(Executors.newSingleThreadExecutor());
			}
		};


		rootView.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				surfaceView.setVisibility(View.VISIBLE);
				surfacegb.setVisibility(View.VISIBLE);
			}
		});


	}

	protected void configureBackActionBar()
	{
		RelativeLayout actionBarMenuRelativeLayout = (RelativeLayout) activity.findViewById(R.id.actionBarMenuRelativeLayout);
		actionBarMenuRelativeLayout.setVisibility(View.GONE);

		RelativeLayout actionBarBackRelativeLayout = (RelativeLayout) activity.findViewById(R.id.actionBarBackRelativeLayout);
		actionBarBackRelativeLayout.setVisibility(View.VISIBLE);

		actionBarBackRelativeLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				back();				
			}
		});

		//((MainPeruvianActivity) activity).lockDrawerLayout(true);

		TextView title = (TextView)activity.findViewById(R.id.header_title);

		if(title!=null)
		{
			title.setText(getString(R.string.kenia_footer));
		}
	}


	public void refreshCamera() {
		if (surface == null) {
			// preview surface does not exist
			return;
		}

		// stop preview before making changes
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}

		// set preview size and make any resize, rotate or
		// reformatting changes here
		// start preview with new settings

		try {
			mCamera.setDisplayOrientation(270);
			/*Parameters params = camera.getParameters();
			params.setPreviewSize(800, 480);
			params.setPictureSize(2880, 1728);
			camera.setParameters(params);*/
			mCamera.setPreviewTexture(surface);
			mCamera.startPreview();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void back()
	{
		progress = ProgressDialog.show(rootView.getContext(), null, getString(R.string.procesando), true);

		surfaceView.setVisibility(View.GONE);
		surfacegb.setVisibility(View.GONE);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim).remove(CameraFragment.this).commit();
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
		},1000);
	}

	public void addPhoto(String path)
	{
		
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
	}

	public void showProgress()
	{
		progress = ProgressDialog.show(rootView.getContext(), null, getString(R.string.procesando), true);
	}

	public void focus() 
	{
		if(mCamera!=null)
		{
			refreshCamera();
			mCamera.autoFocus(this);
			rootView.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(mCamera!=null)
					{
						mCamera.cancelAutoFocus();
						mCamera.takePicture(null, null, jpegCallback);
					}
				}
			}, 2000);
		}
	}

	public void beep()
	{
		beepManager.playBeepSoundAndVibrate();
	}

	@Override
	public void onAutoFocus(boolean success, Camera camera) {
		// TODO Auto-generated method stub
		//		camera.takePicture(null, null, jpegCallback);
	}

	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int arg1,
			int arg2)
	{


        try {
			mCamera = Camera.open();
			mCamera.setDisplayOrientation(270);
			mCamera.setPreviewTexture(surface);
			mCamera.startPreview();
        	Matrix transform = new Matrix();
    		Size size = mCamera.getParameters().getPreviewSize();
    		float[] startpoints = {0,0,size.width,0,size.width,size.height,0,size.height};
            float[] endpoints = {-30,8,size.width - 5,16,size.width + 17,size.height+2,-6,size.height + 16};
            transform.setPolyToPoly(startpoints,0,endpoints,0,startpoints.length >> 1);
    		surfaceView.setTransform(transform);
        	focus();
        	this.surface = surface;
        } catch (Exception e) {
            // Something bad happened
			e.printStackTrace();
        }
	}

//	private void releaseCameraAndPreview() {
//		myCameraPreview.setCamera(null);
//		if (mCamera != null) {
//			mCamera.release();
//			mCamera = null;
//		}
//	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface)
	{
		this.surface = null;
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
        return true;
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture arg0, int arg1,
			int arg2)
	{
		refreshCamera();
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture arg0)
	{
		// TODO Auto-generated method stub
		
	}

}
