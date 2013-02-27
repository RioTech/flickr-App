/**
 * 
 */
package com.weddingsnap.flickr.dataprovider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONException;

import android.content.Context;
import android.util.Log;

import com.weddingsnap.flickr.bean.CommentBean;
import com.weddingsnap.flickr.bean.SearchPhotoBean;
import com.weddingsnap.flickr.parser.CommentParser;
import com.weddingsnap.flickr.parser.SearchPhotoParser;
import com.weddingsnap.flickr.parserImpl.CommentParserImpl;
import com.weddingsnap.flickr.parserImpl.SearchPhotoParserImpl;
import com.weddingsnap.flickr.request.SearchRequest;
import com.weddingsnap.flickr.util.HttpManager;

/**
 * @author Ravi Bhojani
 *
 */
public class DataProvider {

	private Context contex;
	
	public DataProvider(Context context) {
		this.contex = context;
	}
	
	/**
	 * This funcation create request, get data from server and parse it
	 * @param latitude
	 * @param longitude
	 * @return
	 * @throws MalformedURLException
	 * @throws JSONException
	 * @throws IOException
	 * @throws Exception
	 */
	public ArrayList<SearchPhotoBean> searchPhoto(double latitude, double longitude) throws MalformedURLException, JSONException, IOException, Exception
	{
		SearchRequest request = new SearchRequest(contex);
		HttpManager connection = new HttpManager();
		String response = convertStreamToString(connection.getResponse(request.getSearchRequest(latitude,longitude))); //"37.81778516606761", "-122.34374999999999"
		
		Log.i("Response", response);
		SearchPhotoParser photoParser = new SearchPhotoParserImpl();
		ArrayList<SearchPhotoBean> listOfPhoto = photoParser.parse(response);
		return listOfPhoto;		
	}
	
	/**
	 * This funcation handles all the data related to user's comments
	 * @param photoId
	 * @return
	 * @throws MalformedURLException
	 * @throws JSONException
	 * @throws IOException
	 * @throws Exception
	 */
	public ArrayList<CommentBean> photoComment(String photoId) throws MalformedURLException, JSONException, IOException, Exception
	{
		SearchRequest request = new SearchRequest(contex);
		HttpManager connection = new HttpManager();
		String response = convertStreamToString(connection.getResponse(request.getComment(photoId)));
		CommentParser commentParser = new CommentParserImpl();
		ArrayList<CommentBean> listOfComment = commentParser.parse(response);
		return listOfComment;
	}
	/**
	 * This function convert input stream to string
	 * @param inputStream
	 * @return response string
	 * @throws JSONException
	 * @throws IOException
	 */
	public String convertStreamToString(InputStream inputStream) throws JSONException, IOException
	{
		if (inputStream != null) 
		{
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			try 
			{
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"));
				while ((line = reader.readLine()) != null)
				{
					stringBuilder.append(line).append("\n");
				}
			} 
			finally
			{
				inputStream.close();
			}

			return stringBuilder.toString();
		} 
		else 
		{
			return "";
		}
	}
}
