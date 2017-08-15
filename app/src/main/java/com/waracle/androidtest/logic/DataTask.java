package com.waracle.androidtest.logic;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.waracle.androidtest.models.Cake;
import com.waracle.androidtest.utils.StreamUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Bam on 8/15/2017.
 */
@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
public class DataTask extends AsyncTask<String, Void, List<Cake>> {

    private final        String TAG                     = DataTask.class.getSimpleName();
    private static final String REQUIRED_ENCODING_UTF_8 = "UTF-8";
    private final        int    CONNECTION_TIMEOUT      = 10 * 1000;

    @NonNull
    private CakeCallback callback;

    public DataTask( @NonNull final CakeCallback callback ) {
        this.callback = callback;
    }

    @Override
    protected List<Cake> doInBackground( final String... params ) {
        // At least 1 URL
        if( params.length < 1 ) {
            return Collections.emptyList();
        }

        URL url;
        List<Cake> cakes = new ArrayList<>(20); // Initialize with the best known size.

        try {
            url = new URL(params[0]);
        }
        catch( final MalformedURLException e ) {
            Log.e(TAG, "Failed to create URL", e);
            return cakes; // Returning empty list
        }

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(CONNECTION_TIMEOUT);
            urlConnection.setInstanceFollowRedirects(true);

            int responseCode = urlConnection.getResponseCode();


            // If the URL indicates that the content has been moved
            if( responseCode == HttpURLConnection.HTTP_MOVED_TEMP || responseCode == HttpURLConnection.HTTP_MOVED_PERM ) {
                String redirect = urlConnection.getHeaderField("Location");

                // Attempt to open the URL again, which will be
                urlConnection = (HttpURLConnection) new URL(redirect).openConnection();
                responseCode = urlConnection.getResponseCode(); // Re-set the responseCode after redirect
            }


            if( responseCode == HttpURLConnection.HTTP_OK ) { // Parse response body if status 200
                byte[] body = StreamUtils.readUnknownFully(urlConnection.getInputStream());
                if( body == null || body.length == 0 ) {
                    return Collections.emptyList(); // Immutable empty list
                }

                // The Encoding used for the JSON
                String encoding = parseCharset(urlConnection.getContentType());

                try {
                    JSONArray jsonArray = new JSONArray(new String(body, Charset.forName(encoding)));

                    for( int i = 0; i < jsonArray.length(); ++i ) {
                        try {
                            Cake possibleCake = Cake.jsonTo(jsonArray.getJSONObject(i));

                            if( possibleCake != null ) {
                                cakes.add(possibleCake);
                            }
                        }
                        catch( JSONException e ) {
                            // Internal Exception handling so if a single one fails, the rest are still processed.
                            Log.e(TAG, "JSON Read error: ", e);
                        }
                    }
                }
                catch( final JSONException e ) {
                    Log.e(TAG, "JSON Array Read error:", e);
                }
            }
        }
        catch( final IOException e ) {
            Log.e(TAG, "Connection error:", e);
            return null;
        }
        finally {
            if( urlConnection != null ) {
                urlConnection.disconnect();
            }
        }

        return cakes;
    }

    @Override
    protected void onPostExecute( final List<Cake> cakes ) {
        callback.cakesLoaded(cakes);
    }

    public interface CakeCallback {
        void cakesLoaded( @NonNull List<Cake> cakes );
    }

    /**
     * Returns the charset specified in the Content-Type of this header,
     * or the HTTP most common (UTF-8) if none can be found.
     */
    private static String parseCharset( final String contentType ) {
        if( TextUtils.isEmpty(contentType) ) {
            return REQUIRED_ENCODING_UTF_8;
        }

        String[] params = contentType.split(",");
        for( int i = 1; i < params.length; i++ ) {
            String[] pair = params[i].trim().split("=");
            if( pair.length == 2 ) {
                if( pair[0].equals("charset") ) {
                    return pair[1];
                }
            }
        }

        return REQUIRED_ENCODING_UTF_8;
    }
}