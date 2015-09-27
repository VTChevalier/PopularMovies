package com.example.android.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by VTChevalier on 9/26/2015.
 */
public class ImageAdapter extends BaseAdapter {
    private final String LOG_TAG = ImageAdapter.class.getSimpleName();

    private Context mContext;
    private List<MovieHolder> mMovieHolder;

    public ImageAdapter(Context context, List<MovieHolder> movieList)
    {
        super();

        mContext = context;
        mMovieHolder = movieList;
    }

    @Override
    public Object getItem(int i) {
        return mMovieHolder.get(i);
    }

    @Override
    public int getCount() {
        return mMovieHolder.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void updateMovieList(List<MovieHolder> movieList)
    {
        mMovieHolder = movieList;
        this.notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        MovieHolder movieHolder;
        View moviePosterView = convertView;

        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            moviePosterView = li.inflate(R.layout.grid_item_movie, parent, false);
            movieHolder = new MovieHolder(moviePosterView);
            moviePosterView.setTag(movieHolder);
        }
        else {
            movieHolder = (MovieHolder) moviePosterView.getTag();
        }
        Picasso.with(mContext).load(mMovieHolder.get(position).mImgPath).into((ImageView) moviePosterView);

        return moviePosterView;
    }

}
