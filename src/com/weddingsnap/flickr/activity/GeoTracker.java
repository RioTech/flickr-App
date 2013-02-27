/**
 * 
 */
package com.weddingsnap.flickr.activity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import com.weddingsnap.flickr.util.LocationChangeListener;

/**
 * This class tracks user's geo location 
 * @author Ravi Bhojani
 *
 */
public class GeoTracker extends Service implements LocationListener {

	private Context context;
	private boolean isGPSEnabled = false;
	private boolean isNetworkEnabled = false;
	private boolean isLocationAvaiblable = false;
	private Location location;
	private final long MIN_DISTANCE_UPDATE = 0; //it's 5 meter
	private final long MIN_TIME_UPDATE = 0; //1 min	//1 * 60 * 1000
	private LocationManager locationManager;
	private double longitude;
	private double latitude;
	private LocationChangeListener listener;
	
	public GeoTracker(Context context,LocationChangeListener listener)
	{
		this.context = context;
		this.listener = listener;
		getLocation();
	}
	
	public void getLocation()
	{
		try
		{
			locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
	 
            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
 
            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
 
            if (!isGPSEnabled && !isNetworkEnabled)
            {
                // no network provider is enabled
            }
            else 
            {
                setLocationAvaiblable(true);
                // First get location from Network Provider
                if (isNetworkEnabled)
                {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_DISTANCE_UPDATE,MIN_TIME_UPDATE, this);
                    if (locationManager != null) 
                    {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) 
                        {
                        	setLongitude(location.getLongitude());
                        	setLatitude(location.getLatitude());
                        }
                    }
                }
                
                // if GPS Enabled get lat/long using GPS Services
                else if (isGPSEnabled)
                {
                    if (location == null) 
                    {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_DISTANCE_UPDATE,MIN_TIME_UPDATE, this);
                        if (locationManager != null) 
                        {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null)
                            {
                            	setLongitude(location.getLongitude());
                            	setLatitude(location.getLatitude());
                            }
                        }
                    }
                }
            }
 
        } 
		catch (Exception e)
		{
            e.printStackTrace();
        }
 
	}
	
	/**
	 * Stop gps
	 */
	public void stopUsingGPS()
	{
		if(locationManager != null)
		{
			locationManager.removeUpdates(this);
	    }
	}
	public void startUsingGPS()
	{
		if(locationManager != null)
		{
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_DISTANCE_UPDATE,MIN_TIME_UPDATE, this);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_DISTANCE_UPDATE,MIN_TIME_UPDATE, this);
		}
	}
	 
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		listener.refreshData();
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isLocationAvaiblable() {
		return isLocationAvaiblable;
	}

	public void setLocationAvaiblable(boolean isLocationAvaiblable) {
		this.isLocationAvaiblable = isLocationAvaiblable;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
}
