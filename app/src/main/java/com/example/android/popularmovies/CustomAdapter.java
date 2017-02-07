package com.example.android.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Ben on 3/29/2016.
 */
public class CustomAdapter extends BaseAdapter {
    private Context mContext;
    private String[] posterPaths;

    public CustomAdapter(Context c, String[] paths) {
        mContext = c;
        posterPaths = paths;
    }

    public int getCount() {
        return posterPaths.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.gridview_image, parent, false);
        }
        ImageView image = (ImageView) convertView.findViewById(R.id.movie_image);
        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185" + posterPaths[position]).into(image);
        return convertView;
    }
}
