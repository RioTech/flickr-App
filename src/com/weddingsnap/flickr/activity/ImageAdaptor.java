/**
 * 
 */
package com.weddingsnap.flickr.activity;

import java.util.ArrayList;

import com.weddingsnap.flickr.bean.SearchPhotoBean;
import com.weddingsnap.flickr.request.SearchRequest;
import com.weddingsnap.flickr.util.Constants;
import com.weddingsnap.flickr.util.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Adaptor class for gridview. It creates image tiles
 * @author Ravi Bhojani
 *
 */
public class ImageAdaptor extends BaseAdapter{

	private Context context;
	private ArrayList<SearchPhotoBean> listofPhotoBeans;
	private ImageLoader imageLoader;
	private SearchRequest searchRequest;
	
	public ImageAdaptor(Context context,ArrayList<SearchPhotoBean> listofPhotoBeans) {
		this.context = context;
		this.listofPhotoBeans = listofPhotoBeans;
		imageLoader = new ImageLoader(context.getApplicationContext());
		searchRequest = new SearchRequest(context);
	}
	
	public void setList(ArrayList<SearchPhotoBean> listofPhotoBeans)
	{
		this.listofPhotoBeans = listofPhotoBeans;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listofPhotoBeans.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView = new ImageView(context);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(Constants.Misc.THUMBNAIL_WIDTH, Constants.Misc.THUMBNAIL_WIDTH));
        imageLoader.DisplayImage(searchRequest.getImageRequest(listofPhotoBeans.get(position)), (Activity) context, imageView);
		return imageView;
	}

}
