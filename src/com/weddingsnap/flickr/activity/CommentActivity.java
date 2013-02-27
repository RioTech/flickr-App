/**
 * 
 */
package com.weddingsnap.flickr.activity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.weddingsnap.flickr.R;
import com.weddingsnap.flickr.bean.CommentBean;
import com.weddingsnap.flickr.dataprovider.DataProvider;
import com.weddingsnap.flickr.util.Constants;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Ravi Bhojani
 *
 */
public class CommentActivity extends Activity{

	private TextView noCommentsTV;
	private ListView listView;
	private ArrayList<CommentBean> listCommentBeans = new ArrayList<CommentBean>();
	private String photoId;
	private int errorCode = 0x0;
	private final String TAG = CommentActivity.class.getSimpleName();
	private ProgressDialog progressDialog;
	private CommentAdaptor adptor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		
		photoId = getIntent().getStringExtra(Constants.Extra.PHOTO_ID);
		
		noCommentsTV = (TextView) findViewById(R.id.noCommentTV);
		listView = (ListView) findViewById(R.id.commentLV);
		listView.setCacheColorHint(0);
		adptor = new CommentAdaptor(this, R.id.commenterNameTV, listCommentBeans);
		listView.setAdapter(adptor);
		
		new GetCommentData().execute();
		
	}
	
	private class GetCommentData extends AsyncTask<Void, Void, Void>
	{

		 @Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = ProgressDialog.show(CommentActivity.this, null, "Loading...",true);
		}
		 
		@Override
		protected Void doInBackground(Void... params) 
		{
			try 
			{
				DataProvider dataProvider = new DataProvider(CommentActivity.this);
				listCommentBeans = dataProvider.photoComment(photoId);
			}  
			catch (MalformedURLException e)
			{
				errorCode = 0x1;
				Log.i(TAG, "Exception "+e.getMessage(),e);
			} 
			catch (JSONException e) 
			{
				errorCode = 0x2;
				Log.i(TAG, "Exception "+e.getMessage(),e);
			}
			catch (IOException e) 
			{
				errorCode = 0x3;
				Log.i(TAG, "Exception "+e.getMessage(),e);
			} 
			catch (Exception e)
			{
				errorCode = 0x4;
				Log.i(TAG, "Eception "+e.getMessage(),e);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			
			if(errorCode > 0x01)
			{
				AlertDialog.Builder dialog = new AlertDialog.Builder(CommentActivity.this);
				dialog.setMessage(CommentActivity.this.getString(R.string.error_message));
				dialog.setNeutralButton("OK", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						CommentActivity.this.finish();
					}
				}).show();
			}
			else
			{
				Log.i("Log tag", "sizee "+listCommentBeans.size());
				if(listCommentBeans != null && listCommentBeans.size() > 0)
				{
					adptor.notifyDataSetChanged();
				}
				else
				{
					listView.setVisibility(View.GONE);
					noCommentsTV.setVisibility(View.VISIBLE);
				}
			}
		}
	}
	private class CommentAdaptor extends ArrayAdapter<CommentBean>
	{
		private LayoutInflater inflator;
		private ViewHolder holder;
		
		public CommentAdaptor(Context context, int textViewResourceId,List<CommentBean> list)
		{
			super(context, textViewResourceId, list);
			inflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			if(convertView == null)
			{
				convertView = inflator.inflate(R.layout.comment_datarow, parent, false);
				holder = new ViewHolder();
				holder.autherNameTV = (TextView) convertView.findViewById(R.id.commenterNameTV);
				holder.commentTV = (TextView) convertView.findViewById(R.id.commentsTV);
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.autherNameTV.setText(listCommentBeans.get(position).getAutherName());
			holder.commentTV.setText(Html.fromHtml(listCommentBeans.get(position).getComment()));
			return convertView;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listCommentBeans.size();
		}
	}
	private static class ViewHolder
	{
		private TextView autherNameTV, commentTV;
	}
}
