/**
 * 
 */
package com.weddingsnap.flickr.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Stack;
import java.util.WeakHashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;


/**
 * This class load image in background and cache it
 * @author Ravi Bhojani
 *
 */
public class ImageLoader {
    
	private static final String TAG =ImageLoader.class.getSimpleName();
    private MemoryCache memoryCache=new MemoryCache();
    private Context context;
    private HttpManager httpManager;
    private FileCache fileCache;
    private Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    
    
    public ImageLoader(Context context){
        //Make the background thead low priority. This way it will not affect the UI performance
        photoLoaderThread.setPriority(Thread.NORM_PRIORITY-1);
        this.context=context;
        fileCache=new FileCache(context);
    }
    
   
    public void DisplayImage(String url, Activity activity, ImageView imageView)
    {
        imageViews.put(imageView, url);
        Bitmap bitmap=memoryCache.get(url);
        if(bitmap!=null)
            imageView.setImageBitmap(bitmap);
        else
        {
            queuePhoto(url, activity, imageView);
        
        }    
    }
        
    private void queuePhoto(String url, Activity activity, ImageView imageView)
    {
        //This ImageView may be used for other images before. So there may be some old tasks in the queue. We need to discard them. 
        photosQueue.Clean(imageView);
        PhotoToLoad p=new PhotoToLoad(url, imageView);
        synchronized(photosQueue.photosToLoad){
            photosQueue.photosToLoad.push(p);
            photosQueue.photosToLoad.notifyAll();
        }
        
        //start thread if it's not started yet
        if(photoLoaderThread.getState()==Thread.State.NEW)
            photoLoaderThread.start();
    }
    
    public Bitmap getBitmap(String url) 
    {
        File f=fileCache.getFile(url);
        
        Bitmap b = null ;
        if(null!=f)
        //from SD cache
        b = decodeFile(f);
        if(b!=null)
            return b;
        
        //from web
        try {
            Bitmap bitmap=null;
          
            Log.d(TAG,"In Image Loader::"+url);
             httpManager =new HttpManager();
             InputStream is=httpManager.getResponse(url);
          
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Exception ex){
           ex.printStackTrace();
           return null;
        }
    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f){
        try {
            //decode image size
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,opts);
            
            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=Constants.Misc.THUMBNAIL_WIDTH;

            int bmpHeight = opts.outHeight;
            int bmpWidth = opts.outWidth;

            float wFactor = (float)REQUIRED_SIZE / bmpWidth;
            float hFactor = (float)REQUIRED_SIZE / bmpHeight;
            float factor = (hFactor > wFactor) ? hFactor : wFactor;
            int decodeSample = (int)(1/factor);
            
            Log.d(TAG, "onSurfaceChanged - initial bitmap: " + bmpWidth + "," + bmpHeight + "; scale factor: " + factor);

            BitmapFactory.Options decodeOpts = new BitmapFactory.Options();
            decodeOpts.inSampleSize = decodeSample;
            decodeOpts.inScaled = false;
            
            Log.d(TAG, "onSurfaceChanged - bitmap sample size: " + decodeSample);
            
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f),null, decodeOpts);
            
            bmpHeight = bitmap.getHeight();
            bmpWidth = bitmap.getWidth();

            Log.d(TAG, "onSurfaceChanged - decoded bitmap: " + bmpWidth + "x" + bmpHeight);

            wFactor = (float)REQUIRED_SIZE / bmpWidth;
            hFactor = (float)REQUIRED_SIZE / bmpHeight;
            factor = (hFactor > wFactor) ? hFactor : wFactor;
            
            return BitmapFactory.decodeStream(new FileInputStream(f), null, decodeOpts);
        }
        catch (FileNotFoundException e) 
        {
        	e.printStackTrace();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        return null;
    }
    
    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageView imageView;
        public PhotoToLoad(String u, ImageView i){
            url=u; 
            imageView=i;
        }
    }
    
    PhotosQueue photosQueue=new PhotosQueue();
    
    public void stopThread()
    {
        photoLoaderThread.interrupt();
    }
    
    //stores list of photos to download
    class PhotosQueue
    {
        private Stack<PhotoToLoad> photosToLoad=new Stack<PhotoToLoad>();
        
        //removes all instances of this ImageView
        public void Clean(ImageView image)
        {
        	try{
            for(int j=0 ;j<photosToLoad.size();){
                if(photosToLoad.get(j).imageView==image)
                    photosToLoad.remove(j);
                else
                    ++j;
            }
        	}
            catch(Exception e) {
            	 Log.e(TAG, "Exception "+e.getMessage(),e);
            }
        }
    }
    
    class PhotosLoader extends Thread {
        public void run() {
            try {
                while(true)
                {
                    //thread waits until there are any images to load in the queue
                    if(photosQueue.photosToLoad.size()==0)
                        synchronized(photosQueue.photosToLoad){
                            photosQueue.photosToLoad.wait();
                        }
                    if(photosQueue.photosToLoad.size()!=0)
                    {
                        PhotoToLoad photoToLoad;
                        synchronized(photosQueue.photosToLoad){
                            photoToLoad=photosQueue.photosToLoad.pop();
                        }
                        Bitmap bmp=getBitmap(photoToLoad.url);
                        memoryCache.put(photoToLoad.url, bmp);
                        String tag=imageViews.get(photoToLoad.imageView);
                        if(tag!=null && tag.equals(photoToLoad.url)){
                            BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad.imageView);
                            Activity a=(Activity)photoToLoad.imageView.getContext();
                            a.runOnUiThread(bd);
                        }
                    }
                    if(Thread.interrupted())
                        break;
                }
            } catch (InterruptedException e) {
                //allow thread to exit
            }
        }
    }
    
    PhotosLoader photoLoaderThread=new PhotosLoader();
    
    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        ImageView imageView;
        public BitmapDisplayer(Bitmap b, ImageView i)
        {
        	bitmap=b;
        	imageView=i;
        }
        public void run()
        {
            if(bitmap!=null)
                imageView.setImageBitmap(bitmap);
                       
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }
}
