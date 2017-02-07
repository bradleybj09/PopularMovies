package com.example.android.popularmovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
 * Created by Ben on 7/7/2016.
 */
public class MovieDetailFragment extends Fragment {
    String ID;
    TrailerAdapter mTrailers;
    ReviewAdapter mReviews;
    String[] trailers;
    String[] reviews;
    MovieInfo movieInfo;
    String movieTitle;
    String moviePlot;
    String movieYear;
    String movieRating;
    String moviePath;
    ContentResolver cr;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cr = getContext().getContentResolver();
    }

    public void RemoveFavorite() {
        cr.delete(FavoritesContract.TableFavorites.CONTENT_URI, FavoritesContract.TableFavorites.COLUMN_MOVIE_ID+" = "+ID, null);
    }

    public boolean IsFavorite() {
        Cursor c = cr.query(FavoritesContract.TableFavorites.CONTENT_URI, null, FavoritesContract.TableFavorites.COLUMN_MOVIE_ID+" = "+ID, null, null);
        if (c != null) {
            if (c.getCount() > 0) {
                c.close();
                return true;
            } else {
                c.close();
                return false;
            }
        } return false;
    }


    public void AddFavorite() {
        ContentValues movieValues = new ContentValues();
        movieValues.put(FavoritesContract.TableFavorites.COLUMN_MOVIE_ID, ID);
        movieValues.put(FavoritesContract.TableFavorites.COLUMN_PLOT, moviePlot);
        movieValues.put(FavoritesContract.TableFavorites.COLUMN_TITLE, movieTitle);
        movieValues.put(FavoritesContract.TableFavorites.COLUMN_YEAR, movieYear);
        movieValues.put(FavoritesContract.TableFavorites.COLUMN_RATING, movieRating);
        movieValues.put(FavoritesContract.TableFavorites.COLUMN_POSTER, moviePath);
        if (cr != null) {
        cr.insert(FavoritesContract.TableFavorites.CONTENT_URI, movieValues);}
        else {
            Log.v("error", "null");
        }
    }

    public MovieDetailFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        if (ID != null){
        getTrailers getTs = new getTrailers();
        getReviews getRs = new getReviews();
        getTs.execute(ID);
        getRs.execute(ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.movie_fragment_detail, container, false);
        // detail Activity called via method in fragment
        Intent intent = getActivity().getIntent();


        // The detail Activity called main activity fragment.  Inspect the bundle for movie data.
        if (intent.getData() == null && getArguments() != null) {
            Bundle b = getArguments();
            movieInfo = b.getParcelable("MovieInfo");
        }
        // The detail Activity called via intent.  Inspect the intent for movie data.
        else if (intent != null && intent.hasExtra("movieData")) {
            movieInfo = intent.getExtras().getParcelable("movieData");
        }
            if (movieInfo == null){
                rootView = inflater.inflate(R.layout.start_split_detail,container,false);
                return rootView;
            }
            this.ID = movieInfo.ID;
            this.moviePath = "http://image.tmdb.org/t/p/w500" + movieInfo.posterPath;
            this.moviePlot = movieInfo.plot;
            this.movieTitle = movieInfo.title;
            this.movieRating = movieInfo.rating;
            this.movieYear = movieInfo.releaseDate;
            String displayTitle = movieTitle + "\n" + movieYear.substring(0, 4);
            String displayRating = "Rating: " + movieInfo.rating + "/10";
            ((TextView) rootView.findViewById(R.id.detail_title))
                    .setText(displayTitle);
            ((TextView) rootView.findViewById(R.id.detail_plot))
                    .setText(movieInfo.plot);
            ((TextView) rootView.findViewById(R.id.detail_rating))
                    .setText(displayRating);

            ImageView image = (ImageView) rootView.findViewById(R.id.detail_image);
            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w500" + movieInfo.posterPath).into(image);

        Log.v("ID: ", ID);
        //"www.youtube.com/watch?v= JSON HERE;
        //JSON for trailer = "key"
        getActivity().invalidateOptionsMenu();

        return rootView;
    }

    public class getTrailers extends AsyncTask<String, Void, String[]> {
        String LOG_TAG = getTrailers.class.getSimpleName();

        private String[] getMovieDataFromJson(String MovieJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String RESULTS = "results";
            final String ID = "key";

            JSONObject MovieJson = new JSONObject(MovieJsonStr);
            JSONArray trailerArray = MovieJson.getJSONArray(RESULTS);
            if(trailerArray.length()==0){
                cancel(true);
                return null;}
            String trailer = trailerArray.getJSONObject(0).getString(ID);
            Log.v("array: ", trailerArray.getString(0));
            Log.v("Trailer: ", trailer);
            trailers = new String[trailerArray.length()];

            if (trailerArray != null) {
                for (int i = 0; i < trailers.length; i++) {
                    trailers[i] = (trailerArray.getJSONObject(i).getString(ID));
                }
            }
            for (int i = 0; i < trailers.length; i++) {
                Log.v("Trailers: ", trailers[i]);
            }
            return trailers;
        }

        @Override
        protected String[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String trailerJsonStr = null;
            String movieID = params[0];
            String apiKey = "cb25091630d558570e490a256b64c40f";
            String trailerURL = "http://api.themoviedb.org/3/movie/" + movieID + "/videos?api_key=cb25091630d558570e490a256b64c40f";

            try {

                URL url = new URL(trailerURL);
                Log.v("URL", trailerURL);
                // Create the request to TMDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    trailerJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    trailerJsonStr = null;
                }
                trailerJsonStr = buffer.toString();
                Log.v(LOG_TAG, "Trailer JSON: " + trailerJsonStr);
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                trailerJsonStr = null;
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
            if (trailerJsonStr != null) {
                try {
                    String[] trailers = getMovieDataFromJson(trailerJsonStr);
                    for (int i = 0; i < trailers.length; i++) {
                        Log.v("Trailers(DiB): ", trailers[i]);
                    }
                    return trailers;
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }
                //PULLED HERE
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                super.onPostExecute(result);
                ListView trailerList = (ListView) getView().findViewById(R.id.trailer_list);
                mTrailers = new TrailerAdapter(getActivity(), result);
                trailerList.setAdapter(mTrailers);

                trailerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        String trailerURLString = "http://www.youtube.com/watch?v=" + trailers[position];
                        Log.v("Youtube url: ", "http://www.youtube.com/watch?v=" + trailers[position]);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerURLString));
                        startActivity(intent);
                    }
                });
            }
        }
    }

    public class getReviews extends AsyncTask<String, Void, String[]> {
        String LOG_TAG = getReviews.class.getSimpleName();

        private String[] getMovieDataFromJson(String MovieJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String RESULTS = "results";
            final String ID = "url";

            JSONObject MovieJson = new JSONObject(MovieJsonStr);
            JSONArray reviewArray = MovieJson.getJSONArray(RESULTS);
            if(reviewArray.length()==0){
                cancel(true);
                return null;}
            String review = reviewArray.getJSONObject(0).getString(ID);
            Log.v("array: ", reviewArray.getString(0));
            Log.v("Review: ", review);
            reviews = new String[reviewArray.length()];


            if (reviewArray != null) {
                for (int i = 0; i < reviews.length; i++) {
                    reviews[i] = (reviewArray.getJSONObject(i).getString(ID));
                }
            }
            for (int i = 0; i < reviews.length; i++) {
                Log.v("Trailers: ", reviews[i]);
            }
            return reviews;
        }

        @Override
        protected String[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String reviewJsonStr = null;
            String movieID = params[0];
            String apiKey = "cb25091630d558570e490a256b64c40f";
            String reviewURL = "http://api.themoviedb.org/3/movie/" + movieID + "/reviews?api_key=cb25091630d558570e490a256b64c40f";

            try {

                URL url = new URL(reviewURL);
                Log.v("URL", reviewURL);
                // Create the request to TMDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    reviewJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    reviewJsonStr = null;
                }
                reviewJsonStr = buffer.toString();
                Log.v(LOG_TAG, "Review JSON: " + reviewJsonStr);
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                reviewJsonStr = null;
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
            if (reviewJsonStr != null) {
                try {
                    String[] reviews = getMovieDataFromJson(reviewJsonStr);
                    for (int i = 0; i < reviews.length; i++) {
                        Log.v("Reviews(DiB): ", reviews[i]);
                    }
                    return reviews;
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }
                //PULLED HERE
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                super.onPostExecute(result);
                ListView listView = (ListView) getView().findViewById(R.id.review_list);
                mReviews = new ReviewAdapter(getActivity(), result);
                listView.setAdapter(mReviews);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        String reviewURLString = reviews[position];
                        Log.v("Review url: ", reviews[position]);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(reviewURLString));
                        startActivity(intent);
                    }
                });
            }
        }
    }
}

