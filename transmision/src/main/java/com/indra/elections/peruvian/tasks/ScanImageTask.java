package com.indra.elections.peruvian.tasks;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Executors;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.indra.elections.R;
import com.indra.elections.peruvian.fragments.CameraFragment;
import com.indra.elections.utils.LogManager;


public class ScanImageTask extends AsyncTask<String, String, String>  {

	private Context mContext;
	private byte[] data;

	private String path;
	private String barcode;
	private CameraFragment callback;
	
	private String mesa = "24  10001X 1 1";
	
	public ScanImageTask(Context context, byte[] data, CameraFragment callback)
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
	protected void onPostExecute(String datas) {
		try
		{
			if(TextUtils.isEmpty(barcode))
			{
				callback.focus();
			}
			else
			{
				callback.beep();
				
				if(mesa.compareTo(barcode)==0)
				{
					callback.showProgress();
					
					CropImageTask async = new CropImageTask(mContext, this.data, callback);
					async.executeOnExecutor(Executors.newSingleThreadExecutor());

				}
				else
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
					builder.setIcon(R.drawable.urna_128);
					builder.setTitle(mContext.getString(R.string.app_name));
					builder.setMessage(mContext.getString(R.string.argentina_barcode_error));

					builder.setNeutralButton(mContext.getString(R.string.continuar), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							callback.focus();
						}
					});
					builder.create().show();
				}
			}
			barcode = null;
			path = null;
		}
		catch (Exception e) {
			LogManager.addLog(e.getMessage());
		}
	}

	@Override
	protected String doInBackground(String... arg0) {


		try
		{

			processBarcode();		
		
		return null;
		}
		catch (Exception e) {
			LogManager.addLog(e.toString());
			return null;
		}
	}
	
	private static String scanQRImage(Bitmap bMap) {
	    String contents = null;

	    int[] intArray = new int[bMap.getWidth()*bMap.getHeight()];
	    //copy pixel data from the Bitmap into the 'intArray' array
	    bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());

	    LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
	    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

	    Reader reader = new MultiFormatReader();
	    try {
	        Result result = reader.decode(bitmap);
	        contents = result.getText();
	        bitmap = null;
	    }
	    catch (Exception e) {
	        Log.e("QrTest", "Error decoding barcode", e);
	    }
	    return contents;
	}
	

	private void processPhoto()
	{
		
		try {
			
			Bitmap myBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		    Bitmap result = Bitmap.createBitmap(myBitmap, 0, 130, myBitmap.getWidth(), myBitmap.getHeight()-430);
			
//			path = String.format("/sdcard/%d.jpg", System.currentTimeMillis());
//			outStream = new FileOutputStream(path);
//			outStream.write(data);
//			outStream.close();
		    
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
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
	}
	
	private void processBarcode()
	{
		
		try {			
			if(data != null ){

			    Bitmap myBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			    Log.i("QrTest", "Bitmap ancho="+myBitmap.getWidth()+"Xalto="+myBitmap.getHeight());
			    
			    Matrix matrix = new Matrix();
			    matrix.postRotate(90);
			    Bitmap result = Bitmap.createBitmap(myBitmap, (int)(0.815625 * myBitmap.getWidth()), (int)(0.35625 * myBitmap.getHeight()), (int)(0.125 * myBitmap.getWidth()), (int)(0.4698 * myBitmap.getHeight()), matrix, true);
			    
			    myBitmap.recycle();
			    myBitmap = null;
		    
			    barcode=scanQRImage(result);
			    if(barcode!=null)
			    	LogManager.addLog("processPhoto->Decoded string="+barcode);
			    
			    result.recycle();
			    result = null;

			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}