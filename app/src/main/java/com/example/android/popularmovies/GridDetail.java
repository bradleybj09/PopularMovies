package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by bbradley on 4/2/2016.
 */
public class GridDetail extends AppCompatActivity {

    MovieDetailFragment frag;
    private int fragIsFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            frag = new MovieDetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, frag)
                    .commit();
            }
        }

    @Override
     public void onSaveInstanceState(Bundle savedInstanceState){
        // Saving variables
        if (fragIsFavorite != 1 && fragIsFavorite !=2) {
            if (frag.IsFavorite()) {
                fragIsFavorite = 1;
            } else {
                fragIsFavorite = 2;
            }
        }
        savedInstanceState.putInt("fragIsFavorite", fragIsFavorite);

        // Call at the end
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        fragIsFavorite = savedInstanceState.getInt("fragIsFavorite");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        if (fragIsFavorite == 1) {
            menu.removeItem(R.id.AddFavorite);
        } else if (fragIsFavorite == 2) {
            menu.removeItem(R.id.RemoveFavorite);
        }
        else if (frag.IsFavorite()) {
            menu.removeItem(R.id.AddFavorite);
        } else {
            menu.removeItem(R.id.RemoveFavorite);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.AddFavorite) {
            frag.AddFavorite();
            invalidateOptionsMenu();
            return true;
        } else if (id == R.id.RemoveFavorite) {
            frag.RemoveFavorite();
            invalidateOptionsMenu();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}