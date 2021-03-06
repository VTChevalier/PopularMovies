package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_container, new DetailActivityFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       // if (id == R.id.action_settings) {
       //     return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailActivityFragment extends Fragment {
        private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
        private String mMovieStr;

        public DetailActivityFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // The detail Activity called via intent.  Inspect the intent for forecast data.
            MovieHolder movie = (MovieHolder)getActivity().getIntent().getSerializableExtra("MovieHolder");
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            ((TextView) rootView.findViewById(R.id.movie_title)).setText(movie.mTitle);
            ImageView moviePoster = (ImageView) rootView.findViewById(R.id.movie_image);
            ((TextView) rootView.findViewById(R.id.movie_release_date)).setText("Released: " + movie.mReleaseDate);
            ((TextView) rootView.findViewById(R.id.movie_user_rating)).setText("Viewer Rating: " + movie.mUserRating);
            ((TextView) rootView.findViewById(R.id.movie_overview)).setText(movie.mOverview);
            Picasso.with(getContext()).load(movie.mImgPath).into(moviePoster);
            return rootView;
        }
    }
}
