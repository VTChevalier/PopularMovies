package com.example.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    private final String LOG_TAG = MainFragment.class.getSimpleName();

    GridView mGridView;
    ImageAdapter mGridViewAdapter;
    private List<MovieHolder> mMovieHolder = new ArrayList<MovieHolder>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.gridview);
        mGridViewAdapter = new ImageAdapter(getActivity().getApplicationContext(), mMovieHolder);
        mGridView.setAdapter(mGridViewAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieHolder movie = (MovieHolder)mGridViewAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra(Intent.EXTRA_TEXT, movie.mTitle);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void updateMovieList() {
        PullMovieList movieList = new PullMovieList();
        movieList.execute("popularity.desc");
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovieList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.main, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class PullMovieList extends AsyncTask<String, Void, List<MovieHolder>> {

        private final String LOG_TAG = PullMovieList.class.getSimpleName();
        // TMDB JSON Tags
        private final String TMDB_TITLE       = "title";
        private final String TMDB_RELEASE_DATE= "release_date";
        private final String TMDB_OVERVIEW    = "overview";
        private final String TMDB_USER_RATING = "vote_average";
        private final String TMDB_POSTER_PATH = "poster_path";

        private final String TMDB_PIC_PATH    = "https://image.tmdb.org/t/p/";
        private final String TMDB_RESULTS     = "results";
        private final String TMDB_PIC_SIZE    = "w342";


        protected List<MovieHolder> doInBackground(String... params) {
            //if there's no rule of ordering,there is nothing to look up.Verify size of params.
            if (params.length == 0) {
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String popMovieJsonStr = null;
            String sort_by = "popularity.desc";
            String api_key = getString(R.string.api_key);

            try {
                // The format is : https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=fffffffffffffffffffffffffffff
                final String POP_MOVIE_BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
                final String SORT_BY = "sort_by";
                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(POP_MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY, params[0])
                        .appendQueryParameter(API_KEY, api_key)
                        .build();
                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                popMovieJsonStr = buffer.toString();

            }
            catch (IOException e) {
                Log.e(LOG_TAG, "Error", e);
                return null;
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return parsePopularMovie(popMovieJsonStr);
            }
            catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        private List<MovieHolder> parsePopularMovie(String jsonStr)
                throws JSONException {
            JSONObject movieJson  = new JSONObject(jsonStr);
            JSONArray  movieArray = movieJson.getJSONArray(TMDB_RESULTS);

            mMovieHolder.clear();
            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject eachMovie = movieArray.getJSONObject(i);
                String title   = eachMovie.getString(TMDB_TITLE);
                String rDate   = eachMovie.getString(TMDB_RELEASE_DATE);
                String overview= eachMovie.getString(TMDB_OVERVIEW);
                String rating  = eachMovie.getString(TMDB_USER_RATING);
                String imgPath = TMDB_PIC_PATH + TMDB_PIC_SIZE + eachMovie.getString(TMDB_POSTER_PATH);

                mMovieHolder.add(new MovieHolder(title, rDate, overview, rating, imgPath));
            }
            return mMovieHolder;
        }

        protected void onPostExecute(List<MovieHolder> result) {
            if (result != null) {
                mGridViewAdapter.updateMovieList(result);
            }

        }
    }
}