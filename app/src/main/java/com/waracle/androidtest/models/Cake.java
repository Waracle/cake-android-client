package com.waracle.androidtest.models;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Bam on 8/15/2017. TODO- Desc
 */
@SuppressWarnings("WeakerAccess")
public class Cake implements Parcelable {
    @NonNull
    private String mTitle;
    @NonNull
    private String mDesc;
    @NonNull
    private URL    mImageUrl;

    public Cake( @NonNull String mTitle, @NonNull String mDesc, @NonNull URL mImageUrl ) {
        this.mTitle = mTitle;
        this.mDesc = mDesc;
        this.mImageUrl = mImageUrl;
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    @NonNull
    public String getDesc() {
        return mDesc;
    }

    @NonNull
    public URL getImageUrl() {
        return mImageUrl;
    }


    @Nullable
    public static Cake jsonTo( @NonNull final JSONObject jsonObject ) {
        try {
            String title = jsonObject.getString("title");
            String desc = jsonObject.getString("desc");
            String image = jsonObject.getString("image");

            return new Cake(title, desc, new URL(image));
        }
        catch( JSONException e ) {
            Log.e(Cake.class.getSimpleName(), "Failed to read JSON: ", e);
        }
        catch( MalformedURLException e ) {
            Log.e(Cake.class.getSimpleName(), "Failed to read URL: ", e);
        }

        return null;
    }


    protected Cake( Parcel in ) {
        mTitle = in.readString();
        mDesc = in.readString();
        mImageUrl = (URL) in.readValue(URL.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel( Parcel dest, int flags ) {
        dest.writeString(mTitle);
        dest.writeString(mDesc);
        dest.writeValue(mImageUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Cake> CREATOR = new Parcelable.Creator<Cake>() {
        @Override
        public Cake createFromParcel( Parcel in ) {
            return new Cake(in);
        }

        @Override
        public Cake[] newArray( int size ) {
            return new Cake[size];
        }
    };
}