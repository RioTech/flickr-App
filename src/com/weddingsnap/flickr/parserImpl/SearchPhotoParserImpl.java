/**
 * 
 */
package com.weddingsnap.flickr.parserImpl;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.weddingsnap.flickr.bean.SearchPhotoBean;
import com.weddingsnap.flickr.parser.SearchPhotoParser;

/**
 * @author Ravi Bhojani
 *
 */
public class SearchPhotoParserImpl implements SearchPhotoParser{

	private ArrayList<SearchPhotoBean> listOfSearchPhoto = new ArrayList<SearchPhotoBean>();
	private SearchPhotoBean searchPhotoBean;
	 
	@Override
	public ArrayList<SearchPhotoBean> parse(String response) throws JSONException,Exception {
		// TODO Auto-generated method stub
		
		JSONObject parser = new JSONObject(response);
		
		if(parser.getString(SearchPhotoParser.STATUS).equalsIgnoreCase(SearchPhotoParser.OK_STATUS))
		{
			JSONObject photoInfo = parser.getJSONObject(SearchPhotoParser.PHOTOS_INFO);
			/*int currentPage = photoInfo.getInt(SearchPhotoParser.PAGE);
			int totalPages = photoInfo.getInt(SearchPhotoParser.NO_OF_PAGES);*/
			
			JSONArray arrayOfPhoto = photoInfo.getJSONArray(SearchPhotoParser.PHOTO);
			for(int index = 0; index < arrayOfPhoto.length(); index++)
			{
				searchPhotoBean = new SearchPhotoBean();
				searchPhotoBean.setId(arrayOfPhoto.getJSONObject(index).getLong(SearchPhotoParser.ID));
				searchPhotoBean.setOwner(arrayOfPhoto.getJSONObject(index).getString(SearchPhotoParser.OWNER));
				searchPhotoBean.setSecret(arrayOfPhoto.getJSONObject(index).getString(SearchPhotoParser.SECRET));
				searchPhotoBean.setServer(arrayOfPhoto.getJSONObject(index).getLong(SearchPhotoParser.SERVER));
				searchPhotoBean.setFarm(arrayOfPhoto.getJSONObject(index).getInt(SearchPhotoParser.FARM));
				searchPhotoBean.setTitle(arrayOfPhoto.getJSONObject(index).getString(SearchPhotoParser.TITLE));
				searchPhotoBean.setPublic(arrayOfPhoto.getJSONObject(index).getInt(SearchPhotoParser.IS_PUBLIC) == 1 ? true : false);
				searchPhotoBean.setFriend(arrayOfPhoto.getJSONObject(index).getInt(SearchPhotoParser.IS_FRIEND) == 1 ? true : false);
				searchPhotoBean.setFamily(arrayOfPhoto.getJSONObject(index).getInt(SearchPhotoParser.IS_FAMILY) == 1 ? true : false);
				listOfSearchPhoto.add(searchPhotoBean);
			}
		}
		return listOfSearchPhoto;
	}

}
