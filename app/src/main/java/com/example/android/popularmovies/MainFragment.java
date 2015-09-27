package com.example.android.popularmovies;

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
    GridView mGridView;

    private static String[] mMovieImages = {
            "http://image.tmdb.org/t/p/w342/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg",
            "http://image.tmdb.org/t/p/w342/7SGGUiTE6oc2fh9MjIk5M00dsQd.jpg",
            "http://image.tmdb.org/t/p/w342/uXZYawqUsChGSj54wcuBtEdUJbh.jpg",
            "http://image.tmdb.org/t/p/w342/s5uMY8ooGRZOL0oe4sIvnlTsYQO.jpg",
            "http://image.tmdb.org/t/p/w342/aBBQSC8ZECGn6Wh92gKDOakSC8p.jpg"
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mGridView = (GridView) rootView.findViewById(R.id.gridview);

        mGridView.setAdapter(new ImageAdapter(getActivity().getApplicationContext(), mMovieImages));

        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();
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

    public class pullMovieList extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = pullMovieList.class.getSimpleName();

        private String[] getPopMovieFromJson(String popMovieJsonStr)
                throws JSONException {

            final String TMDB_PIC_SIZE = "w342";
            final String TMDB_RESULTS = "results";
            final String TMDB_POSTER_PATH = "poster_path";
            JSONObject movieJson = new JSONObject(popMovieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(TMDB_RESULTS);

            String[] items = new String[movieArray.length()];

            for (int i = 0; i < movieArray.length(); i++) {
//                Log.d(LOG_TAG, "ITEMS_VALUE" + items[i]);

                JSONObject eachMovie = movieArray.getJSONObject(i);
                String picFileName = eachMovie.getString(TMDB_POSTER_PATH);
                items[i] = "http://image.tmdb.org/t/p/" + TMDB_PIC_SIZE + picFileName;
                Log.d(LOG_TAG, "ITEMS_VALUE" + items[i]);
            }
            return items;
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
                //The format is : http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=22fae03d55ebd9a2ad6df0f36e489087
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

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error", e);
                return null;
            } finally {
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
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String[] result) {
            if (result != null) {
                //  System.arraycopy(result, 0, items, 0, result.length);
                //gridView.setAdapter(new GridViewAdapter(getActivity(), items));
        ///        gridViewAdapter.notifyDataSetChanged();
            }
            /*
                        if (result != null) {
                mForecastAdapter.clear();
                for(String dayForecastStr : result) {
                    mForecastAdapter.add(dayForecastStr);
                }
                // New data is back from the server.  Hooray!
            }
             */
        }
    }
}





/*


 */