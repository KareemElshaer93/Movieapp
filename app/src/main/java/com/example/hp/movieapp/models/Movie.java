package com.example.hp.movieapp.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

/**
 * Created by Hp on 21/10/2016.
 */

public class Movie implements Parcelable{
    String title,overView,rating,releaseDate;
    String poster;
    String id;

    public Movie(String title, String overView, String rating, String releaseDate, String poster,String id) {
        this.title = title;
        this.overView = overView;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.poster = poster;
        this.id=id;
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
        title = in.readString();
        overView = in.readString();
        rating = in.readString();
        releaseDate = in.readString();
        poster = in.readString();
        id = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(overView);
        dest.writeString(rating);
        dest.writeString(releaseDate);
        dest.writeString(poster);
        dest.writeString(id);
    }
}
