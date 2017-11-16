package com.waracle.androidtest.core.model;

public final class Cake {

  private final String title;
  private final String description;
  private final String imageUrl;

  public Cake(String title, String description, String imageUrl) {
    this.title = title;
    this.description = description;
    this.imageUrl = imageUrl;
  }

  public String title() {
    return title;
  }

  public String description() {
    return description;
  }

  public String imageURL() {
    return imageUrl;
  }
}
