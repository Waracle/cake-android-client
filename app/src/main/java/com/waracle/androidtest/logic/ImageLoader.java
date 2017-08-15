package com.waracle.androidtest.logic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import com.waracle.androidtest.utils.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Riad on 20/05/2015.
 */
@SuppressWarnings("WeakerAccess")
public class ImageLoader {

    private static final String      TAG         = ImageLoader.class.getSimpleName();
    private static final ImageLoader ourInstance = new ImageLoader();
    private LruCache<String, Bitmap> mMemoryCache;

    private ArrayList<ImageTask> imageTasks = new ArrayList<>();


    public static ImageLoader getInstance() {
        return ourInstance;
    }

    private ImageLoader() {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) ( Runtime.getRuntime().maxMemory() / 1024 );

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf( String key, Bitmap bitmap ) {
                // The cache size will be measured in kilobytes rather than number of items.
                return ( bitmap.getRowBytes() * bitmap.getHeight() ) / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache( @NonNull String key, @NonNull Bitmap bitmap ) {
        if( getBitmapFromMemCache(key) == null ) {
            mMemoryCache.put(key, bitmap);
        }
    }

    @Nullable
    public Bitmap getBitmapFromMemCache( @NonNull String key ) {
        return mMemoryCache.get(key);
    }

    /**
     * Simple function for loading a bitmap image from the web
     *
     * @param url       image url
     * @param imageView view to set image too.
     */
    public void load( @NonNull final URL url, @NonNull ImageView imageView ) {
        final Bitmap bitmap = getBitmapFromMemCache(url.toString());
        if( bitmap != null ) {
            setImageView(imageView, bitmap);
        } else {
            ImageTask task = new ImageTask(imageView);
            imageTasks.add(task);
            task.execute(url);
        }
    }

    private class ImageTask extends AsyncTask<URL, Void, Bitmap> {

        private WeakReference<ImageView> weakRefImageView;

        public ImageTask( @NonNull ImageView imageView ) {
            this.weakRefImageView = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground( URL... params ) {
            Bitmap bitmap = null;
            URL url = params[0];

            try {
                bitmap = convertToBitmap(loadImageData(url));

                if( bitmap != null ) { addBitmapToMemoryCache(url.toString(), bitmap); }
            }
            catch( IOException e ) {
                Log.e(TAG, e.getMessage());
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute( Bitmap bitmap ) {
            super.onPostExecute(bitmap);
            ImageView imageView = weakRefImageView.get();
            if( imageView != null ) { setImageView(imageView, bitmap); }

            imageTasks.remove(this); // Removes the task from the list of task.
        }
    }

    private static byte[] loadImageData( @NonNull URL url ) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // Possibly better caching
        connection.setUseCaches(true);

        InputStream inputStream = null;
        try {
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setInstanceFollowRedirects(true);

            int responseCode = connection.getResponseCode();

            // If the URL indicates that the content has been moved
            if( responseCode == HttpURLConnection.HTTP_MOVED_TEMP || responseCode == HttpURLConnection.HTTP_MOVED_PERM ) {
                String redirect = connection.getHeaderField("Location");

                // Attempt to open the URL again, which will be
                connection = (HttpURLConnection) new URL(redirect).openConnection();
            }

            try {
                // Read data from workstation
                inputStream = connection.getInputStream();
            }
            catch( IOException e ) {
                e.printStackTrace();
            }

            // Can you think of a way to make the entire
            // HTTP more efficient using HTTP headers??

            return StreamUtils.readUnknownFully(inputStream);
        }
        finally {
            // Close the input stream if it exists.
            StreamUtils.close(inputStream);

            // Disconnect the connection
            connection.disconnect();
        }
    }

    private static Bitmap convertToBitmap( @NonNull byte[] data ) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    private void setImageView( @NonNull ImageView imageView, @Nullable Bitmap bitmap ) {
        if( bitmap != null ) { imageView.setImageBitmap(bitmap); }
    }

    public void stopAndClear() {
        for( ImageTask imageTask : imageTasks ) {
            if( imageTask != null ) {
                imageTask.cancel(true);
            }
        }

        imageTasks.clear();
    }
}