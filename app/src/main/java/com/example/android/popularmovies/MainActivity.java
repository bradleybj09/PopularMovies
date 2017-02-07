package com.example.android.popularmovies;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {
    private boolean mTwoPane;
    private boolean showingFavorites;
    public static final String DETAILFRAGMENT_TAG = "DFTAG";
    ContentResolver cr;
    FavoritesAdapter mFavorites;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        // Saving variables
        savedInstanceState.putBoolean("showingFavorites", showingFavorites);

        // Call at the end
        super.onSaveInstanceState(savedInstanceState);
    }
    public void MainPopulateFavorites() {
        Cursor cursor = cr.query(FavoritesContract.TableFavorites.CONTENT_URI, null, null, null, null);
        GridView gridView = (GridView) findViewById(R.id.gridview_movies);
        mFavorites = new FavoritesAdapter(this,cursor,0);
        gridView.setAdapter(mFavorites);
        invalidateOptionsMenu();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MovieInfo parcel = (MovieInfo) view.getTag();

                ShowDetail(parcel);
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        // Call at the start
        super.onRestoreInstanceState(savedInstanceState);

        // Retrieve variables
        showingFavorites = savedInstanceState.getBoolean("showingFavorites");
        if(showingFavorites){
            MainPopulateFavorites();
            Log.v("Faves run?","yes");
        }
        invalidateOptionsMenu();
    }
    public boolean ShowingFavorites(){
        return showingFavorites;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cr = getContentResolver();
        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mTwoPane) {
            getMenuInflater().inflate(R.menu.main, menu);
            FragmentManager fm = getSupportFragmentManager();
            MovieDetailFragment movieDetailFragment = (MovieDetailFragment) fm.findFragmentByTag(DETAILFRAGMENT_TAG);
            if (movieDetailFragment.IsFavorite()) {
                menu.removeItem(R.id.AddFavorite);
            } else {
                menu.removeItem(R.id.RemoveFavorite);
            }
            if (!showingFavorites) {
                menu.removeItem(R.id.ShowNormal);
            } else {
                menu.removeItem(R.id.ShowFavorites);
            }
        } else {
            getMenuInflater().inflate(R.menu.main, menu);
            if (!showingFavorites) {
                menu.removeItem(R.id.ShowNormal);
            } else {
                menu.removeItem(R.id.ShowFavorites);
            }
            menu.removeItem(R.id.RemoveFavorite);
            menu.removeItem(R.id.AddFavorite);
        }return true;
    }


    public void SetFavorites(String param) {
        if (param == "show") {
            showingFavorites = true;
        }
        else if (param == "hide"){
            showingFavorites = false;
        }
    }
    public void CheckAndLoadFavorites(){
        if (showingFavorites){
            MainPopulateFavorites();
            invalidateOptionsMenu();
        }
    }
    public void ShowDetail(MovieInfo movieInfo){
        if (mTwoPane){
            MovieDetailFragment fragment = new MovieDetailFragment();
            Bundle b = new Bundle(movieInfo.getClassLoader());
            b.putParcelable("MovieInfo", movieInfo);
            fragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG).commit();
            invalidateOptionsMenu();

        }
        else {
            Intent intent = new Intent(this, GridDetail.class)
                    .putExtra("movieData", movieInfo);
            startActivity(intent);
            invalidateOptionsMenu();
        }

    }
}
