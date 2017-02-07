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
public class TrailerAdapter extends BaseAdapter {
    private Context mContext;
    private String[] trailers;

    public TrailerAdapter(Context c, String[] urls){
        mContext = c;
        trailers = urls;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int count = position + 1;
        String counts = " " + trailers.length;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.trailer_list, parent, false);
        }
        TextView title = (TextView) convertView.findViewById(R.id.trailer_title);
        title.setText("Trailer " + count);
        Log.v("trailer count:", counts);
        return convertView;
    }

    public int getCount() {
        return trailers.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

}
