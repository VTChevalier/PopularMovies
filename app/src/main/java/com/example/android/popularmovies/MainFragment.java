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

public class MainFragment extends Fragment {
    private final String LOG_TAG = MainFragment.class.getSimpleName();

    GridView mGridView;
    ImageAdapter mGridViewAdapter;
    private String[] mMovieImages;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.v(LOG_TAG, "onCreate()");
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.gridview);
        updateMovie();
        //Log.v(LOG_TAG, mMovieImages.toString());
        mGridViewAdapter = new ImageAdapter(getActivity().getApplicationContext(), mMovieImages);
        mGridView.setAdapter(mGridViewAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String movie = mGridViewAdapter.getItem(position).toString();
                Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra(Intent.EXTRA_TEXT, movie);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void updateMovie() {
        PullMovieList movieList = new PullMovieList();
        movieList.execute("popularity.desc");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(LOG_TAG, "onStart()");

      //  updateMovie();
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

    public class PullMovieList extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = PullMovieList.class.getSimpleName();

        private String[] getPopMovieFromJson(String popMovieJsonStr)
                throws JSONException {

            final String TMDB_PIC_SIZE = "w342";
            final String TMDB_RESULTS = "results";
            final String TMDB_POSTER_PATH = "poster_path";
            JSONObject movieJson = new JSONObject(popMovieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(TMDB_RESULTS);

            //String[] items = new String[movieArray.length()];
            mMovieImages = new String[movieArray.length()];

            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject eachMovie = movieArray.getJSONObject(i);
                String picFileName = eachMovie.getString(TMDB_POSTER_PATH);
                mMovieImages[i] = "http://image.tmdb.org/t/p/" + TMDB_PIC_SIZE + picFileName;
            }
            return mMovieImages;
        }

        protected String[] doInBackground(String... params) {
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
                // The format is : http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=fffffffffffffffffffffffffffff
                final String POP_MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
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
                return getPopMovieFromJson(popMovieJsonStr);
            }
            catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String[] result) {
            if (result != null) {
                mGridViewAdapter.updateMovieList(result);
            }

        }
    }
}