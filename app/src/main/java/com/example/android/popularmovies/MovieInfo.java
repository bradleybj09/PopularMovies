package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by Ben on 4/3/2016.
 */
public class MovieInfo implements Parcelable {
    String title;
    String plot;
    String rating;
    String releaseDate;
    String posterPath;
    String ID;

    @Override
    public int describeContents() {
        return 0;
    }

    public ClassLoader getClassLoader(){
        return getClass().getClassLoader();
    }

    public MovieInfo(String vTitle, String vPlot, String vRating, String vReleaseDate, String vPosterPath, String vID) {
        this.title = vTitle;
        this.plot = vPlot;
        this.rating = vRating;
        this.releaseDate = vReleaseDate;
        this.posterPath = vPosterPath;
        this.ID = vID;
    }

    private MovieInfo(Parcel in) {
        title = in.readString();
        plot = in.readString();
        rating = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        ID = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(plot);
        parcel.writeString(rating);
        parcel.writeString(releaseDate);
        parcel.writeString(posterPath);
        parcel.writeString(ID);
    }

    public static final Parcelable.Creator<MovieInfo> CREATOR = new Parcelable.Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel parcel) {
            return new MovieInfo(parcel);
        }

        @Override
        public MovieInfo[] newArray(int i) {
            return new MovieInfo[i];
        }

    };
}
