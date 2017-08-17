package com.waracle.androidtest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidParameterException;
/**
 * Created by Riad on 20/05/2015.
 */
public class ImageLoader {

    private static final String TAG = ImageLoader.class.getSimpleName();

    private WeakReference<ImageView> imageViewReference;

    public ImageLoader() { }

    /**
     * Simple function for loading a bitmap image from the web
     *
     * @param url       image url
     * @param imageView view to set image too.
     */
    public void load(String url, ImageView imageView) {
        if (TextUtils.isEmpty(url)) {
            throw new InvalidParameterException("URL is empty!");
        }

        imageViewReference = new WeakReference<ImageView>(imageView);

        // Can you think of a way to improve loading of bitmaps
        // that have already been loaded previously??

        try {
            loadImageData(url, imageView);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void loadImageData(String url, ImageView imageView) throws IOException {
        new GetCakeFeedTask(imageView).execute(new URL(url));
    }

    private static Bitmap convertToBitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    private static void setImageView(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }


    private class GetCakeFeedTask extends AsyncTask<URL, Void, byte[]> {

        private final WeakReference<ImageView> imageViewRef;

        public GetCakeFeedTask(ImageView imageView)
        {
            imageViewRef = imageViewReference;
        }

        @Override
        protected byte[] doInBackground(URL... urls) {
            URL url = urls[0];
            HttpURLConnection connection = null;
            InputStream inputStream = null;
            try {
                 connection = (HttpURLConnection) url.openConnection();

                    // Read data from workstation
                    inputStream = connection.getInputStream();


                // Can you think of a way to make the entire
                // HTTP more efficient using HTTP headers??

                return StreamUtils.readUnknownFully(inputStream);
            } catch (IOException e) {
                // Read the error from the workstation
                inputStream = connection.getErrorStream();
                return null;
            } finally {
                // Close the input stream if it exists.
                StreamUtils.close(inputStream);

                // Disconnect the connection
                connection.disconnect();
            }
        }

        protected void onPostExecute(byte[] array) {
            if (imageViewRef != null) {
                ImageView imageView = imageViewRef.get();
                if (imageView != null) {
                    imageView.setImageBitmap(convertToBitmap(array));
                }
            }

        }
    }
}
