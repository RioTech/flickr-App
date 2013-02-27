/**
 * 
 */
package com.weddingsnap.flickr.request;

import com.weddingsnap.flickr.R;
import com.weddingsnap.flickr.R.string;
import com.weddingsnap.flickr.bean.SearchPhotoBean;

import android.content.Context;
import android.util.Log;

/**
 * @author Ravi Bhojani
 *
 *This class creates request for the webservice.
 */
public class SearchRequest {

	private Context context;
	private final String MARKER = "&";
	private final String KEY = "api_key=";
	private final String METHOD_SEARCH = "method=flickr.photos.search";
	private final String LATTITUDE = "lat=";
	private final String LONGITUDE = "lon=";
	private final String FORMATE_JSON = "format=json&nojsoncallback=1";
	private final String PHOTO_ID = "photo_id=";
	private final String METHOD_COMMENT = "method=flickr.photos.comments.getList";
	
	public SearchRequest(Context context) 
	{
		this.context = context;
	}
	public String getSearchRequest(double lattidue, double longitude)
	{
		String url = context.getResources().getString(R.string.url);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(url);
		stringBuilder.append(METHOD_SEARCH);
		stringBuilder.append(MARKER);
		stringBuilder.append(KEY);
		stringBuilder.append(context.getResources().getString(R.string.fliker_key));
		stringBuilder.append(MARKER);
		stringBuilder.append(LATTITUDE+"24.131068"); //lattidue 24.131068, 120.642199
		stringBuilder.append(MARKER);
		stringBuilder.append(LONGITUDE+"120.642199"); //longitude
		stringBuilder.append(MARKER);
		stringBuilder.append(FORMATE_JSON);
		Log.i("TAG", "Request "+stringBuilder.toString());
		return stringBuilder.toString();
	}
	
	public String getImageRequest(SearchPhotoBean photoBean)
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("http://farm"+photoBean.getFarm()+".staticflickr.com/"
				+photoBean.getServer()+"/"+photoBean.getId()+"_"+photoBean.getSecret()+".jpg");
		return stringBuilder.toString();
	}
	
	public String getComment(String photoId)
	{
		String url = context.getResources().getString(R.string.url);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(url);
		stringBuilder.append(METHOD_COMMENT);
		stringBuilder.append(MARKER);
		stringBuilder.append(KEY);
		stringBuilder.append(context.getResources().getString(R.string.fliker_key));
		stringBuilder.append(MARKER);
		stringBuilder.append(PHOTO_ID+photoId);
		stringBuilder.append(MARKER);
		stringBuilder.append(FORMATE_JSON);
		Log.i("TAG", "Request "+stringBuilder.toString());
		return stringBuilder.toString();
	}
}
