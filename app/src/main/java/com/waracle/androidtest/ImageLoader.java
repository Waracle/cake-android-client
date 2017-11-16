package com.waracle.androidtest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Riad on 20/05/2015.
 */
public class ImageLoader {

    private static final int DEFAULT_THREAD_COUNT = 3;
    private static final String TAG = ImageLoader.class.getSimpleName();

    ExecutorService executorService;
    ImageLoader() {
        executorService = Executors.newFixedThreadPool(DEFAULT_THREAD_COUNT);
    }

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

        // Can you think of a way to improve loading of bitmaps
        // that have already been loaded previously??

        executorService.submit(new RemoteImageLoading(imageView, url));
    }

    static class RemoteImageLoading implements Runnable {

        private final ImageView imageView;
        private final String url;

        public RemoteImageLoading(ImageView imageView, String url) {
            this.imageView = imageView;
            this.url = url;
        }

        @Override
        public void run() {
            try {
                setImageView(imageView, convertToBitmap(loadImageData(url)));
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private static byte[] loadImageData(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        InputStream inputStream = null;
        try {
            try {
                // Read data from workstation
                inputStream = connection.getInputStream();
            } catch (IOException e) {
                // Read the error from the workstation
                inputStream = connection.getErrorStream();
            }

            // Can you think of a way to make the entire
            // HTTP more efficient using HTTP headers??

            return StreamUtils.readUnknownFully(inputStream);
        } finally {
            // Close the input stream if it exists.
            StreamUtils.close(inputStream);

            // Disconnect the connection
            connection.disconnect();
        }
    }

    private static Bitmap convertToBitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    private static void setImageView(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}
