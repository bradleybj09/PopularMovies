package com.example.android.popularmovies;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Ben on 6/25/2016.
 */
public class FavoritesProvider extends ContentProvider {
    DatabaseHelper mOpenHelper;
    // Uri baseURI = Uri.parse("content://" + FavoritesContract.CONTENT_AUTHORITY + "/favorites");
    static final int FAVORITES = 1;
    static final int FAVORITE_ID = 2;
    private static final UriMatcher uriMatcher = getUriMatcher();
    static UriMatcher getUriMatcher() {
        final String PROVIDER_NAME = FavoritesContract.CONTENT_AUTHORITY;
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, FavoritesContract.PATH_FAVORITES, FAVORITES);
        uriMatcher.addURI(PROVIDER_NAME, "favorites/#", FAVORITE_ID);
        return uriMatcher;
    }

    @Override
    public String getType(Uri uri) {
        /*
        switch (uriMatcher.match(uri)) {
            case FAVORITES:
                return "vnd.android.cursor.dir/vnd.example.favorites";
            //case FAVORITE_ID:
                //return "vnd.android.cursor.item/vnd.example.favorites";
        }
        */
        return FavoritesContract.TableFavorites.CONTENT_TYPE;

    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor c;
        switch (uriMatcher.match(uri)) {
            case FAVORITES:{
                c = mOpenHelper.getReadableDatabase().query(FavoritesContract.TableFavorites.TABLE_NAME,
                        projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
                );
                break;
        }
            case FAVORITE_ID:{
                c = mOpenHelper.getReadableDatabase().query(FavoritesContract.TableFavorites.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case FAVORITES: {
                long _id = db.insert(FavoritesContract.TableFavorites.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FavoritesContract.TableFavorites.buildFavoritesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsDeleted;

        if (null == selection) selection = "1";
        switch (match) {
            case FAVORITES:
                rowsDeleted = db.delete(FavoritesContract.TableFavorites.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case FAVORITES:
                rowsUpdated = db.update(FavoritesContract.TableFavorites.TABLE_NAME,values,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
