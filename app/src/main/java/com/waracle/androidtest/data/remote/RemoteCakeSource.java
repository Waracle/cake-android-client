package com.waracle.androidtest.data.remote;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import com.waracle.androidtest.StreamUtils;
import com.waracle.androidtest.core.base.UseCase;
import com.waracle.androidtest.core.model.Cake;
import com.waracle.androidtest.data.remote.model.RemoteCake;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;

public class RemoteCakeSource implements RemoteCakeDataSource {

  private static String JSON_URL =
      "https://gist.githubusercontent.com/hart88/198f29ec5114a3ec3460/" +
          "raw/8dd19a88f9b8d24c23d9960f3300d0c917a4f07c/cake.json";

  private final GetCakesTask getCakesTask = new GetCakesTask();

  @Override
  public void getCakes(UseCase.Callback<List<Cake>> cakesCallback) {
    getCakesTask.withCallback(cakesCallback);
    getCakesTask.execute();
  }

  @Override
  public void cancelDataRequest() {
    getCakesTask.cancel(true);
  }

  private static final class GetCakesTask extends AsyncTask<Void, String, List<Cake>> {

    @Nullable private UseCase.Callback<List<Cake>> callback;
    @Nullable private Exception exception;

    void withCallback(@Nullable UseCase.Callback<List<Cake>> callback) {
      this.callback = callback;
    }

    @Override
    protected List<Cake> doInBackground(Void... voids) {
      try {
        List<Cake> cakes = new ArrayList<>();
        JSONArray jsonArray = loadData();
        for (int i = 0, size = jsonArray.length(); i < size; i++) {
          RemoteCake remoteCake = new RemoteCake(jsonArray.getJSONObject(i));
          cakes.add(new Cake(remoteCake.title(), remoteCake.description(), remoteCake.imageURL()));
        }
        return cakes;
      } catch (IOException | JSONException e) {
         exception = e;
      }
      return null;
    }

    @Override
    protected void onPostExecute(List<Cake> cakes) {
      super.onPostExecute(cakes);
      if (cakes != null && exception != null && callback != null) {
        callback.onError(exception);
      } else if (callback != null){
        callback.onSuccess(cakes);
      }
    }

    private static JSONArray loadData() throws IOException, JSONException {
      URL url = new URL(JSON_URL);
      HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
      try {
        InputStream in = new BufferedInputStream(urlConnection.getInputStream());

        // Can you think of a way to improve the performance of loading data
        // using HTTP headers???

        // Also, Do you trust any utils thrown your way????

        byte[] bytes = StreamUtils.readUnknownFully(in);

        // Read in charset of HTTP content.
        String charset = parseCharset(urlConnection.getRequestProperty("Content-Type"));

        // Convert byte array to appropriate encoded string.
        String jsonText = new String(bytes, charset);

        // Read string as JSON.
        return new JSONArray(jsonText);
      } finally {
        urlConnection.disconnect();
      }
    }
  }

  /**
   * Returns the charset specified in the Content-Type of this header,
   * or the HTTP default (ISO-8859-1) if none can be found.
   */
  public static String parseCharset(String contentType) {
    if (contentType != null) {
      String[] params = contentType.split(",");
      for (int i = 1; i < params.length; i++) {
        String[] pair = params[i].trim().split("=");
        if (pair.length == 2) {
          if (pair[0].equals("charset")) {
            return pair[1];
          }
        }
      }
    }
    return "UTF-8";
  }
}
