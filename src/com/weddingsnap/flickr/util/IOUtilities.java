package com.weddingsnap.flickr.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;

/**
 * A helper class that assists in IO tasks such as getting File Objects,
 * reading and writing to streams.
 * @author Ravi Bhojani
 *
 */
public final class IOUtilities
{
	
	private static final String LOG_TAG = IOUtilities.class.getSimpleName();

	public static final String THUMBNAIL_CACHE_DIRECTORY = "thumbnails";
	public static final String PHOTOS_CACHE_DIRECTORY = "photos";
	
	public static final int IO_BUFFER_SIZE = 4 * 1024;
	
	/* Enforce static usage using private constructor */
	private IOUtilities(){
		
	}

	public static File getExternalFile(String file)
	{
		return new File(Environment.getExternalStorageDirectory(), file);
	}

	/**
	 * Copy the content of the input stream into the output stream, using a
	 * temporary byte array buffer whose size is defined by
	 * IO_BUFFER_SIZE
	 * 
	 * @param in
	 *            The input stream to copy from.
	 * @param out
	 *            The output stream to copy to.
	 * 
	 * @throws java.io.IOException
	 *             If any error occurs during the copy.
	 */
	public static void copy(InputStream in, OutputStream out) throws IOException
	{
		byte[] b = new byte[IO_BUFFER_SIZE];
		int read;
		while ((read = in.read(b)) != -1)
		{
			out.write(b, 0, read);
		}
	}

	/**
	 * Closes the specified stream.
	 * 
	 * @param stream
	 *            The stream to close.
	 */
	public static void closeStream(Closeable stream)
	{
		if (stream != null)
		{
			try
			{
				stream.close();
			} catch (IOException e)
			{
				android.util.Log.e(LOG_TAG, "Could not close stream", e);
			}
		}
	}

	
	/** Returns the File Descriptor of specified fileName
	 * @throws Exception 
	 * */
	public static FileDescriptor getFile(String filename) throws Exception
	{
		FileInputStream fis = new FileInputStream(filename);
		return fis.getFD();
	}
}
