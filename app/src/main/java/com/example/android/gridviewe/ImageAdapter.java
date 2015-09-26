package com.example.android.gridviewe;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Arrays;

/**
 * Created by VTChevalier on 9/26/2015.
 */
public class ImageAdapter extends BaseAdapter {
    private final String LOG_TAG = ImageAdapter.class.getSimpleName();

    private Context mContext;
    private LayoutInflater inflater;


    public ImageAdapter(Context c){
        mContext = c;
    }
    @Override
    public Object getItem(int i) {
        return eatFoodyImages[i];
    }

    @Override
    public int getCount() {
        return eatFoodyImages.length;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView == null){
            Log.d(LOG_TAG, "do I crash?" );

            convertView = inflater.inflate(R.layout.grid_item_movie, parent, false);
            Log.d(LOG_TAG, "no crash" );

            imageView = (ImageView) convertView.findViewById(R.id. grid_imageview);

            //imageView = new ImageView(mContext);

        }
        else{
            imageView = (ImageView) convertView;
        }
        Picasso.with(mContext).setLoggingEnabled(true);
        Log.d(LOG_TAG, "getView()" + eatFoodyImages[i]);

        Picasso.with(mContext)
                .load(eatFoodyImages[i])
                .fit()
                .into(imageView);
        return imageView;
    }

    public static String[] eatFoodyImages = {
            "http://i.imgur.com/rFLNqWI.jpg",
            "http://i.imgur.com/C9pBVt7.jpg",
            "http://i.imgur.com/rT5vXE1.jpg",
            "http://i.imgur.com/aIy5R2k.jpg",
            "http://i.imgur.com/MoJs9pT.jpg",
            "http://i.imgur.com/S963yEM.jpg",
            "http://i.imgur.com/rLR2cyc.jpg",
            "http://i.imgur.com/SEPdUIx.jpg",
            "http://i.imgur.com/aC9OjaM.jpg",
            "http://i.imgur.com/76Jfv9b.jpg",
            "http://i.imgur.com/fUX7EIB.jpg",
            "http://i.imgur.com/syELajx.jpg",
            "http://i.imgur.com/COzBnru.jpg",
            "http://i.imgur.com/Z3QjilA.jpg",
    };
}
