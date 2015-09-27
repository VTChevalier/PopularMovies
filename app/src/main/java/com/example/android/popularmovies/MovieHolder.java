package com.example.android.popularmovies;

import android.view.View;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by VTChevalier on 9/27/2015.
 */

public class MovieHolder implements Serializable {
    public String mTitle;
    public String mReleaseDate;
    public String mOverview;
    public String mUserRating;
    public String mImgPath;
    public ImageView mPoster;

    public MovieHolder(String title, String rDate, String overview, String userRating, String imgPath) {
        mTitle = title;
        mReleaseDate = rDate;
        mOverview = overview;
        mUserRating = userRating;
        mImgPath = imgPath;
    }

    public void SetImageView(View rootView) {
        mPoster = (ImageView) rootView;
    }

    public MovieHolder(View rootView) {
        mPoster = (ImageView) rootView;
    }
}