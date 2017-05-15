package com.zeeice.themovie.Data.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Oriaje on 10/05/2017.
 */

public class Review implements Parcelable {


    private String id;
    private String author;
    private String content;
    private String url;

    public Review(Parcel parcel) {

        id = parcel.readString();
        author = parcel.readString();
        content = parcel.readString();
        url = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {

        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static class SearchResult {

        private List<Review> results;

        public List<Review> getItems() {
            return results;
        }
    }
}
