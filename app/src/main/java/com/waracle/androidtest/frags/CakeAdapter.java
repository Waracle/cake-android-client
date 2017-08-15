package com.waracle.androidtest.frags;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.waracle.androidtest.R;
import com.waracle.androidtest.logic.ImageLoader;
import com.waracle.androidtest.models.Cake;

import java.util.List;
/**
 * Created by Bam on 8/15/2017.
 */

public class CakeAdapter extends ArrayAdapter<Cake> {

    @NonNull
    private LayoutInflater inflater;

    public CakeAdapter( @NonNull Context context, @NonNull List<Cake> objects ) {
        super(context, R.layout.list_item_layout, objects);

        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView( int position, @Nullable View convertView, @NonNull ViewGroup parent ) {
        CakeViewHolder holder;

        if( convertView == null ) {
            convertView = inflater.inflate(R.layout.list_item_layout, null);

            holder = new CakeViewHolder();
            
            holder.mTitle = (TextView) convertView.findViewById(R.id.title);
            holder.mDescription = (TextView) convertView.findViewById(R.id.desc);
            holder.mImage = (ImageView) convertView.findViewById(R.id.image);

            convertView.setTag(holder);
        } else {
            holder = (CakeViewHolder) convertView.getTag();
        }


        Cake object = getItem(position);
        if( object != null ) { // In theory, this can be null
            holder.mTitle.setText(object.getTitle());
            holder.mDescription.setText(object.getDesc());
            holder.mImage.setImageBitmap(null); // This resets the image (while loading)
            ImageLoader.getInstance().load(object.getImageUrl(), holder.mImage);
        }

        return convertView;
    }


    private class CakeViewHolder {
        private TextView  mTitle;
        private TextView  mDescription;
        private ImageView mImage;
    }
}
