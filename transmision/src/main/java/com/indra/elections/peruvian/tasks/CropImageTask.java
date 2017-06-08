package com.indra.elections.peruvian.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Log;

import com.indra.elections.peruvian.fragments.CameraFragment;
import com.indra.elections.utils.LogManager;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CropImageTask extends AsyncTask<String, String, String>  {

	private Context mContext;
	private byte[] data;

	private String path;
	private CameraFragment callback;
	
	
	public CropImageTask(Context context, byte[] data, CameraFragment callback)
	{
		this.mContext = context; 
		this.data = data;
		this.callback = callback;
	}
	
	@Override
	protected void onPreExecute() {
		Log.i("InicioFormacionTask", "Executing...");
	}

	@Override
	protected void onProgressUpdate(String... progress) {

	}

	@Override
	protected void onPostExecute(String data) {
		try
		{

			callback.addPhoto(path);			

		}
		catch (Exception e) {
			LogManager.addLog(e.getMessage());
		}
	}

	@Override
	protected String doInBackground(String... arg0) {


		try
		{
			System.gc();
			processPhoto();	
		
		return null;
		}
		catch (Exception e) {
			LogManager.addLog(e.toString());
			return null;
		}
	}
	

	private void processPhoto()
	{
		
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inDither = false;                     //Disable Dithering mode
			opts.inPurgeable=true;
			Bitmap myBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
			
		    Matrix matrix = new Matrix();
			matrix.preRotate(-92f);
		    Bitmap rotate = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true);
		    
		    matrix = new Matrix();
			float[] startpoints = {0,0,myBitmap.getWidth(),0,myBitmap.getWidth(),myBitmap.getHeight(),0,myBitmap.getHeight()};
	        float[] endpoints = {0,0,myBitmap.getWidth(),115,myBitmap.getWidth(),myBitmap.getHeight() - 80,10,myBitmap.getHeight()};
	        matrix.setPolyToPoly(startpoints,0,endpoints,0,startpoints.length >> 1);
	        
		    myBitmap.recycle();
		    myBitmap = Bitmap.createBitmap(rotate, 0, 0, rotate.getWidth(), rotate.getHeight(), matrix, true);
		    
		    Bitmap result = Bitmap.createBitmap(myBitmap, 270, 251, myBitmap.getWidth() - 670, myBitmap.getHeight() - 475);
		    myBitmap.recycle();
		    myBitmap = null;
		    
			path = String.format("/sdcard/%d.jpg", System.currentTimeMillis());			
			File f = new File(path);
			f.createNewFile();

			//Convert bitmap to byte array
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			result.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
			byte[] bitmapdata = bos.toByteArray();

			//write the bytes in file
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(bitmapdata);
			fos.flush();
			fos.close();
			
			result.recycle();
			result = null;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
	}
	

}