package com.weddingsnap.flickr.activity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.weddingsnap.flickr.R;
import com.weddingsnap.flickr.bean.SearchPhotoBean;
import com.weddingsnap.flickr.dataprovider.DataProvider;
import com.weddingsnap.flickr.util.Constants;
import com.weddingsnap.flickr.util.LocationChangeListener;

public class GalleryActivity extends Activity implements LocationChangeListener{

	private GridView gridView;
	private TextView noDataTV;
	private ArrayList<SearchPhotoBean> listOfPhotoBeans = new ArrayList<SearchPhotoBean>();
	private int errorCode = 0x0;
	private final String TAG = GalleryActivity.class.getSimpleName();
	private double latitude;
	private double longitude;
	private GeoTracker geoTracker;
	private SearchPublicPhoto searchPublicPhoto;
	private ImageAdaptor imageAdaptor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		gridView = (GridView) findViewById(R.id.photoGV);
		noDataTV = (TextView) findViewById(R.id.nodataTV);
		
		//Didnt get time to make thumbnail width dynamic based on screen size,
		//to make it better.
		gridView.setColumnWidth(Constants.Misc.THUMBNAIL_WIDTH);
		imageAdaptor = new ImageAdaptor(GalleryActivity.this, listOfPhotoBeans);
		gridView.setAdapter(imageAdaptor);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
				Intent intent = new Intent(GalleryActivity.this,DetailActivity.class);
				intent.putExtra(Constants.Extra.PHOTO_BEAN, listOfPhotoBeans.get(position));
				startActivity(intent);
			}
		});
		
		//As we need to call do registration with gps and call webservice only once
		//that's why i have written code at onCreate
		
		geoTracker = new GeoTracker(this,this);
		if(geoTracker.isLocationAvaiblable())
		{
			latitude = geoTracker.getLatitude();
			longitude = geoTracker.getLongitude();
			Log.i(TAG, "latitude "+latitude+" longitude "+longitude);
			searchPublicPhoto = new SearchPublicPhoto();
			searchPublicPhoto.execute();
		}
		else
		{
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		    alertDialog.setTitle(GalleryActivity.this.getString(R.string.gpssetting_title));
		    alertDialog.setMessage(GalleryActivity.this.getString(R.string.gpssetting_message));
		    alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener()
		    {
		    	public void onClick(DialogInterface dialog,int which)
		        {
		    		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		            GalleryActivity.this.startActivity(intent);
		        }
	        });
		 
		    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
		    {
		    	public void onClick(DialogInterface dialog, int which) 
		    	{
		    		dialog.dismiss();
		    	}
	        });
		    alertDialog.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_gallery, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		if(item.getItemId() == R.id.refresh)
		{
			if(geoTracker != null)
			{
				Log.i(TAG, "Refresh");
				geoTracker.startUsingGPS();
			}
		}
		else if(item.getItemId() == R.id.stopRefresh)
		{
			if(geoTracker != null)
			{
				Log.i(TAG, "Stop Refresh");
				geoTracker.stopUsingGPS();
			}
					
		}
		return super.onOptionsItemSelected(item);
	}
	private class SearchPublicPhoto extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... params) {
			DataProvider provider = new DataProvider(GalleryActivity.this);
			try 
			{
				listOfPhotoBeans.clear();
				listOfPhotoBeans = provider.searchPhoto(latitude,longitude);
			} 
			catch (MalformedURLException e)
			{
				errorCode = 0x1;
				Log.i(TAG, "Exception "+e.getMessage(),e);
			} 
			catch (JSONException e) 
			{
				errorCode = 0x2;
				Log.i(TAG, "Exception "+e.getMessage(),e);
			}
			catch (IOException e) 
			{
				errorCode = 0x3;
				Log.i(TAG, "Exception "+e.getMessage(),e);
			} 
			catch (Exception e)
			{
				errorCode = 0x4;
				Log.i(TAG, "Eception "+e.getMessage(),e);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(errorCode > 0x01)
			{
				AlertDialog.Builder dialog = new AlertDialog.Builder(GalleryActivity.this);
				dialog.setMessage(GalleryActivity.this.getString(R.string.error_message));
				dialog.setNeutralButton("OK", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						GalleryActivity.this.finish();
					}
				}).show();
			}
			else
			{
				if(listOfPhotoBeans != null && listOfPhotoBeans.size() > 0)
				{
					imageAdaptor.setList(listOfPhotoBeans);
					imageAdaptor.notifyDataSetChanged();
				}
				else
				{
					gridView.setVisibility(View.GONE);
					noDataTV.setVisibility(View.VISIBLE);
				}
			}
		}
	}
	

	@Override
	public void refreshData() {
		// TODO Auto-generated method stub
		latitude = geoTracker.getLatitude();
		longitude = geoTracker.getLongitude();
		Log.i(TAG, "latitude "+latitude+" longitude "+longitude);
		if(searchPublicPhoto != null && searchPublicPhoto.getStatus() == Status.FINISHED)
		{
			searchPublicPhoto = new SearchPublicPhoto();
			searchPublicPhoto.execute();
		}
	}
}
