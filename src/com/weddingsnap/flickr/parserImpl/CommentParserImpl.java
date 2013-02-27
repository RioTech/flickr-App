/**
 * 
 */
package com.weddingsnap.flickr.parserImpl;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.weddingsnap.flickr.bean.CommentBean;
import com.weddingsnap.flickr.parser.CommentParser;


/**
 * @author Ravi Bhojani
 *
 */
public class CommentParserImpl implements CommentParser{

	private ArrayList<CommentBean> listOfCommentBean = new ArrayList<CommentBean>();
	private CommentBean commentBean;
	
	@Override
	public ArrayList<CommentBean> parse(String response) throws JSONException, Exception {
		// TODO Auto-generated method stub
		
		JSONObject parser = new JSONObject(response);
		if(parser.getString(CommentParser.STATUS).equalsIgnoreCase(CommentParser.OK_STATUS))
		{
			JSONObject info = parser.getJSONObject(CommentParser.COMMENTS_TAG);
			if(info.has(CommentParser.COMMET_ARRAY))
			{
				JSONArray comment = info.getJSONArray(CommentParser.COMMET_ARRAY);
				for(int index = 0; index < comment.length(); index++)
				{
					commentBean = new CommentBean();
					commentBean.setAutherName(comment.getJSONObject(index).getString(CommentParser.AUTHER_NAME));
					commentBean.setComment(comment.getJSONObject(index).getString(CommentParser.COMMENT));
					listOfCommentBean.add(commentBean);
				}
			}
		}
		return listOfCommentBean;
	}

}
