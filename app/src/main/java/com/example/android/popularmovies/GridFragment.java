package com.example.android.popularmovies;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

/**
 * Created by Ben on 3/27/2016.
 */
public class GridFragment extends Fragment {
    FavoritesAdapter mFavorites;
    CustomAdapter mMovies;
    String[] pPaths;
    String[] titles;
    String[] plots;
    String[] ratings;
    String[] dates;
    String[] IDs;
    ContentResolver cr;
    public GridFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();

        PopulateMovies populateMovies = new PopulateMovies();
        populateMovies.execute("popular");
        Log.v("popmovies?","yep");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.gridview_host, container, false);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        cr = getContext().getContentResolver();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MovieDetailFragment frag = (MovieDetailFragment)getFragmentManager().findFragmentByTag(MainActivity.DETAILFRAGMENT_TAG);
        int id = item.getItemId();
        if (id == R.id.Popularity) {
            PopulateMovies popMovies = new PopulateMovies();
            popMovies.execute("popular");
            return true;
        } else if (id == R.id.Rating) {
            PopulateMovies popMovies = new PopulateMovies();
            popMovies.execute("top_rated");
            return true;
        } else if (id == R.id.ShowNormal) {
            PopulateMovies popMovies = new PopulateMovies();
            popMovies.execute("popular");
            ((MainActivity)getActivity()).SetFavorites("hide");
            getActivity().invalidateOptionsMenu();
            return true;
        } else if (id == R.id.ShowFavorites) {
            PopulateFavorites();
            ((MainActivity) getActivity()).SetFavorites("show");
            getActivity().invalidateOptionsMenu();
            return true;
        } if (id == R.id.AddFavorite) {
            frag.AddFavorite();
            getActivity().invalidateOptionsMenu();
            return true;
        } else if (id == R.id.RemoveFavorite) {
            frag.RemoveFavorite();
            getActivity().invalidateOptionsMenu();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public class PopulateMovies extends AsyncTask<String, Void, String[]> {
        String LOG_TAG = PopulateMovies.class.getSimpleName();

        private String[] getMovieDataFromJson(String MovieJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String RESULTS = "results";
            final String POSTER = "poster_path";
            final String PLOT = "overview";
            final String TITLE = "original_title";
            final String RATING = "vote_average";
            final String DATE = "release_date";
            final String ID = "id";

            JSONObject MovieJson = new JSONObject(MovieJsonStr);
            JSONArray movieArray = MovieJson.getJSONArray(RESULTS);
            String pPath = movieArray.getJSONObject(0).getString(POSTER);
            Log.v("array: ", movieArray.getString(0));
            Log.v("pPath: ", pPath);
            pPaths = new String[movieArray.length()];
            titles = new String[movieArray.length()];
            plots = new String[movieArray.length()];
            ratings = new String[movieArray.length()];
            dates = new String[movieArray.length()];
            IDs = new String[movieArray.length()];

            if (movieArray != null) {
                for (int i = 0; i < pPaths.length; i++) {
                    pPaths[i] = (movieArray.getJSONObject(i).getString(POSTER));
                    titles[i] = (movieArray.getJSONObject(i).getString(TITLE));
                    plots[i] = (movieArray.getJSONObject(i).getString(PLOT));
                    ratings[i] = (movieArray.getJSONObject(i).getString(RATING));
                    dates[i] = (movieArray.getJSONObject(i).getString(DATE));
                    IDs[i] = (movieArray.getJSONObject(i).getString(ID));
                }
            }
            for (int i = 0; i < pPaths.length; i++) {
                Log.v("Paths: ", pPaths[i]);
            }
            for (int i = 0; i < titles.length; i++) {
                Log.v("Titles: ", titles[i]);
            }
            for (int i = 0; i < plots.length; i++) {
                Log.v("Plots: ", plots[i]);
            }
            for (int i = 0; i < ratings.length; i++) {
                Log.v("Ratings: ", ratings[i]);
            }
            for (int i = 0; i < dates.length; i++) {
                Log.v("Dates: ", dates[i]);
            }
            for (int i = 0; i < IDs.length; i++) {
                Log.v("IDs: ", IDs[i]);
            }
            return pPaths;
        }

        @Override
        protected String[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;
            String sorting = params[0];
            String apiKey = "cb25091630d558570e490a256b64c40f";
            String currentURL = "http://api.themoviedb.org/3/movie/" + sorting + "?api_key=cb25091630d558570e490a256b64c40f";

            try {

                URL url = new URL(currentURL);
                Log.v("URL", currentURL);
                // Create the request to TMDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    movieJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    movieJsonStr = null;
                }
                movieJsonStr = buffer.toString();
                Log.v(LOG_TAG, "JSON: " + movieJsonStr);
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                movieJsonStr = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            //PULLED HERE
            if(movieJsonStr != null) {
                try {
                    String[] pPaths = getMovieDataFromJson(movieJsonStr);
                    for (int i = 0; i < pPaths.length; i++) {
                        Log.v("Paths(DiB): ", pPaths[i]);
                    }
                    return pPaths;
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }
                //PULLED HERE
                return null;
            }return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                super.onPostExecute(result);
                GridView gridView = (GridView) getView().findViewById(R.id.gridview_movies);
                mMovies = new CustomAdapter(getActivity(), result);
                gridView.setAdapter(mMovies);
                ((MainActivity)getActivity()).CheckAndLoadFavorites();

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        MovieInfo parcel = new MovieInfo(titles[position], plots[position], ratings[position], dates[position], pPaths[position],IDs[position]);
                        ((MainActivity)getActivity()).ShowDetail(parcel);
                    }
                });
            }
        }
    }
    public void PopulateFavorites() {
        Cursor cursor = cr.query(FavoritesContract.TableFavorites.CONTENT_URI, null, null, null, null);
        GridView gridView = (GridView) getView().findViewById(R.id.gridview_movies);
        mFavorites = new FavoritesAdapter(getActivity(),cursor,0);
        gridView.setAdapter(mFavorites);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MovieInfo parcel = (MovieInfo)view.getTag();

                ((MainActivity) getActivity()).ShowDetail(parcel);
                getActivity().invalidateOptionsMenu();
            }
        });
    }
}