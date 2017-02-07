package com.example.android.popularmovies;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Ben on 6/25/2016.
 */
public class FavoritesContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";

    private FavoritesContract() {}

    public static final class TableFavorites implements BaseColumns {

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String TABLE_NAME = "favorites";
        //column storing TMDB movie id
        public static final String COLUMN_MOVIE_ID = "movie_id";
        //column storing title
        public static final String COLUMN_TITLE = "title";
        //column storing rating
        public static final String COLUMN_RATING = "rating";
        //column storing rating
        public static final String COLUMN_YEAR = "year";
        //column storing plot
        public static final String COLUMN_PLOT = "plot";
        //column storing image
        public static final String COLUMN_POSTER = "poster";
        //sql to create table
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + _ID + " INTEGER PRIMARY KEY,"
                + COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
                + COLUMN_TITLE + " TEXT NOT NULL, "
                + COLUMN_RATING + " TEXT NOT NULL, "
                + COLUMN_YEAR + " TEXT NOT NULL, "
                + COLUMN_PLOT + " TEXT NOT NULL, "
                + COLUMN_POSTER + " TEXT NOT NULL"
                + ");";
        //sql to delete table
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        public static Uri buildFavoritesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
