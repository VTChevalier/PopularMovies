package com.example.android.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by VTChevalier on 9/26/2015.
 */
public class ImageAdapter extends BaseAdapter {
    private final String LOG_TAG = ImageAdapter.class.getSimpleName();

    private Context mContext;
    private String[] mMovieImages;

    public ImageAdapter(Context context, String[] movieImages)
    {
        super();

        mContext = context;
        mMovieImages = movieImages;
    }

    @Override
    public Object getItem(int i) {
        String item = "";
        if (mMovieImages != null) {
            item = mMovieImages[i];
        }
        return item;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (mMovieImages != null) {
            count = mMovieImages.length;
        }

        return count;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void updateMovieList(String[] movieImages)
    {
        mMovieImages = movieImages;
        this.notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View viewHolder = convertView;
        //ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewHolder = li.inflate(R.layout.grid_item_movie, parent, false);
            //viewHolder = new ViewHolder(v);
            //v.setTag(viewHolder);
        }// else {
            //viewHolder = (ViewHolder) v.getTag();
       // }
        if (mMovieImages != null) {
            Glide.with(mContext).load(mMovieImages[position]).into((ImageView) viewHolder);
        }
        return viewHolder;
    }

/*
    public class ViewHolder {
        public ImageView mPoster;

        public ViewHolder(View rootView) {
            mPoster = (ImageView) rootView;
        }
    }*/
}
