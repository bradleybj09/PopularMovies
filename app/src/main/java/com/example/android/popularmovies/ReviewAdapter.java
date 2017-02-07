package com.example.android.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Ben on 5/22/2016.
 */
public class ReviewAdapter extends BaseAdapter {
    private Context mContext;
    private String[] reviews;

    public ReviewAdapter(Context c, String[] urls){
        mContext = c;
        reviews = urls;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int count = position + 1;
        String counts = " " + reviews.length;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.review_list, parent, false);
        }
        TextView title = (TextView) convertView.findViewById(R.id.review_title);
        title.setText("Review " + count);
        Log.v("review count:", counts);
        return convertView;
    }

    public int getCount() {
        return reviews.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

}
