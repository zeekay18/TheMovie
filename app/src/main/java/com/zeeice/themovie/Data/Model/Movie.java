package com.zeeice.themovie.Data.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oriaje on 12/04/2017.
 */

public class Movie implements Parcelable{

    private long id;
    @SerializedName("poster_path")
    private String imageUrl;
    @SerializedName("original_title")
    private String title;
    @SerializedName("overview")
    private String synopsis;
    @SerializedName("vote_average")
    private String rating;
    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("backdrop_path")
    private String backdropPath;

    private boolean favored = false;

    public Movie(){}

    public Movie(Long id,String imageUrl, String backdropPath, String title, String synopsis,
                 String rating, String releaseDate, int favored)
    {
        this.id = id;
        this.imageUrl = imageUrl;
        this.backdropPath = backdropPath;
        this.title = title;
        this.synopsis = synopsis;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.favored = favored != 0;
    }

    protected Movie(Parcel in) {
        id = in.readLong();
        imageUrl = in.readString();
        title = in.readString();
        synopsis = in.readString();
        rating = in.readString();
        releaseDate = in.readString();
        backdropPath = in.readString();
        favored = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(imageUrl);
        dest.writeString(title);
        dest.writeString(synopsis);
        dest.writeString(rating);
        dest.writeString(releaseDate);
        dest.writeString(backdropPath);
        dest.writeInt(favored ? 1 : 0);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public long getId()
    {return id;}

    public void setId(long id){this.id = id;}
    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
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

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public boolean isFavored() {
        return favored;
    }

    public void setFavored(boolean favored) {
        this.favored = favored;
    }

    public static class SearchResult {

        private List<Movie> results;

        public List<Movie> getItems(){return results;}
    }
}
