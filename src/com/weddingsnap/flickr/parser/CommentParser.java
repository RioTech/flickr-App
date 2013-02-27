/**
 * 
 */
package com.weddingsnap.flickr.parser;

import java.util.ArrayList;

import org.json.JSONException;

import com.weddingsnap.flickr.bean.CommentBean;

/**
 * @author Ravi Bhojani
 *
 */
public interface CommentParser {

	public final String AUTHER_NAME = "authorname";
	public final String COMMENT = "_content";
	public final String COMMENTS_TAG = "comments";
	public final String COMMET_ARRAY = "comment";
	public final String STATUS = "stat";
	public final String OK_STATUS = "ok";
	
	public ArrayList<CommentBean> parse(String response) throws JSONException, Exception;
}
