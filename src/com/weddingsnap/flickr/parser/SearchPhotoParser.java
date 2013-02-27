/**
 * 
 */
package com.weddingsnap.flickr.parser;

import java.util.ArrayList;

import org.json.JSONException;

import com.weddingsnap.flickr.bean.SearchPhotoBean;

/**
 * This is interface of search photo parser
 * 
 * @author Ravi Bhojani
 *
 */
public interface SearchPhotoParser {

	public final String STATUS = "stat";
	public final String OK_STATUS = "ok";
	public final String PHOTOS_INFO = "photos";
	public final String PAGE = "page";
	public final String NO_OF_PAGES = "pages";
	public final String PER_PAGE = "perpage";
	public final String TOTAL_PHOTO = "total";
	public final String PHOTO = "photo";
	public final String ID = "id";
	public final String OWNER = "owner";
	public final String SECRET = "secret";
	public final String SERVER = "server";
	public final String FARM = "farm";
	public final String TITLE = "title";
	public final String IS_PUBLIC = "ispublic";
	public final String IS_FRIEND = "isfriend";
	public final String IS_FAMILY = "isfamily";
	
	public ArrayList<SearchPhotoBean> parse(String response) throws JSONException, Exception;
}
