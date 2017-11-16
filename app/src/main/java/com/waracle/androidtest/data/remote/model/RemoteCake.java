package com.waracle.androidtest.data.remote.model;

import org.json.JSONException;
import org.json.JSONObject;

public class RemoteCake {

  private static final String JSON_TITLE = "title";
  private static final String JSON_DESCRIPTION = "desc";
  private static final String JSON_IMAGE = "image";

  private final JSONObject jsonObject;

  public RemoteCake(JSONObject jsonObject) {
    this.jsonObject = jsonObject;
  }

  public String title() throws JSONException {
    return jsonObject.getString(JSON_TITLE);
  }

  public String description() throws JSONException {
    return jsonObject.getString(JSON_DESCRIPTION);
  }

  public String imageURL() throws JSONException {
    return jsonObject.getString(JSON_IMAGE);
  }
}
