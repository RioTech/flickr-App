/**
 * 
 */
package com.weddingsnap.flickr.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Ravi Bhojani
 *
 *This class handle all the operation related to HTTP
 */
public class HttpManager {

	public InputStream getResponse(String request) throws MalformedURLException,IOException,Exception
	{
		URL url = new URL(request);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		InputStream is =  connection.getInputStream();
		if(connection.getResponseCode() == HttpURLConnection.HTTP_OK)
		{
			return is;
		}
		else
		{
			return null;
		}
	}
}
