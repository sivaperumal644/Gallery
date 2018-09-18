package com.example.siva.galleryflickr;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by siva on 16/9/18.
 */

public class AdapterActivity extends ArrayAdapter<ImageListActivity> {



    public AdapterActivity(Activity context, ArrayList<ImageListActivity> image) {
        super(context, 0, image);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.default_layout, parent, false);
        }


        ImageListActivity currentWord = getItem(position);

        ImageView imageView = (ImageView) listItemView.findViewById(R.id.flickr_image);

        try {
            Picasso.get()
                    .load(currentWord.getmUrl_s())
                    .into(imageView);
        } catch(NullPointerException e) {
            /* sets image to default image

             */
            imageView.setImageResource(R.drawable.default_img);

        }

        TextView image_id = (TextView) listItemView.findViewById(R.id.flickr_id);
        image_id.setText(currentWord.getmId());

        return listItemView;


    }


}
