package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Ben on 7/9/2016.
 */
public class FavoritesAdapter extends CursorAdapter {
    public FavoritesAdapter(Context context, Cursor cursor, int flags){
        super(context,cursor,0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.gridview_image, parent, false);
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        ImageView poster = (ImageView) view.findViewById(R.id.movie_image);
        // Extract properties from cursor
        String path = cursor.getString(cursor.getColumnIndexOrThrow("poster"));
        // Populate fields with extracted properties
        Picasso.with(context).load(path).into(poster);
        MovieInfo parcel = new MovieInfo(
                cursor.getString(cursor.getColumnIndexOrThrow("title")),
                cursor.getString(cursor.getColumnIndexOrThrow("plot")),
                cursor.getString(cursor.getColumnIndexOrThrow("rating")),
                cursor.getString(cursor.getColumnIndexOrThrow("year")),
                cursor.getString(cursor.getColumnIndexOrThrow("poster")),
                cursor.getString(cursor.getColumnIndexOrThrow("movie_id")));
        poster.setTag(parcel);
    }
}
