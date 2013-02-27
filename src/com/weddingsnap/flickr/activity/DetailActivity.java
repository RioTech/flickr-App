/**
 * 
 */
package com.weddingsnap.flickr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.weddingsnap.flickr.R;
import com.weddingsnap.flickr.bean.SearchPhotoBean;
import com.weddingsnap.flickr.request.SearchRequest;
import com.weddingsnap.flickr.util.Constants;
import com.weddingsnap.flickr.util.ImageLoader;

/**
 * This activity show all the details of the image
 * @author Ravi Bhojani
 *
 */
public class DetailActivity extends Activity{

	private ImageView detailIV;
	private SearchPhotoBean photoBean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
				
		setContentView(R.layout.activity_details);
		detailIV = (ImageView) findViewById(R.id.detailIV);
		photoBean = (SearchPhotoBean) getIntent().getSerializableExtra(Constants.Extra.PHOTO_BEAN);
		
		ImageLoader imageLoader = new ImageLoader(this);
		SearchRequest request =  new SearchRequest(this);
		imageLoader.DisplayImage(request.getImageRequest(photoBean), this, detailIV);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.activity_detail, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if(item.getItemId() == R.id.comment_menu)
		{
			Intent intent = new Intent(this,CommentActivity.class);
			intent.putExtra(Constants.Extra.PHOTO_ID, ""+photoBean.getId());
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}
