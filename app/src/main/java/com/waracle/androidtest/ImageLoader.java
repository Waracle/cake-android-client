package com.waracle.androidtest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidParameterException;

/**
 * Created by Riad on 20/05/2015.
 */
public class ImageLoader {

    private static final String TAG = ImageLoader.class.getSimpleName();
    
    private boolean[] bitmapStatus;
    private Bitmap[] bitmap;
    private int length;


    public ImageLoader(int length 
                       ){

    /**
     * Simple function for loading a bitmap image from the web
     *
     * @param url       image url
     * @param imageView view to set image too.
     */
    bitmapStatus=new boolean[length];
        bitmap=new Bitmap[length];
        Arrays.fill(bitmapStatus, Boolean.FALSE);
  }

    public void setlength(int length) {
        this.length=length;
    }


    public void load(String url, ImageView imageView, int position) {
        if (TextUtils.isEmpty(url)) {
            throw new InvalidParameterException("URL is empty!");
        }

        // Can you think of a way to improve loading of bitmaps
        // that have already been loaded previously??

        try {
                        if(!bitmapStatus[position])
                new LoadImage(imageView,position).execute(url);
            else
                setImageView(imageView,bitmap[position]);

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
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
    private class LoadImage extends AsyncTask<String, Void, Bitmap> {

        ImageView image;
        int position;

        public LoadImage(ImageView image,int position)
        {
            this.image=image;
            this.position=position;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            try {
                return convertToBitmap(loadImageData(params[0]));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            bitmapStatus[position]=true;
            bitmap[position]=result;
            setImageView(image,result);
        }

        @Override
        protected void onPreExecute() {
            image.setImageResource(R.mipmap.android);
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }



}
