package com.mssinfotech.iampro.co.model;

/**
 * Created by mssinfotech on 15/01/19.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by delaroy on 5/18/17.
 */
public class HomesResponse implements Parcelable {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<Home> results;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Home> getResults() {
        return results;
    }

    public List<Home> getMovies() {
        return results;
    }

    public void setResults(List<Home> results) {
        this.results = results;
    }

    public void setMovies(List<Home> results) {
        this.results = results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.page);
        dest.writeTypedList(this.results);
        dest.writeInt(this.totalResults);
        dest.writeInt(this.totalPages);
    }

    public HomesResponse() {
    }

    protected HomesResponse(Parcel in) {
        this.page = in.readInt();
        this.results = in.createTypedArrayList(Home.CREATOR);
        this.totalResults = in.readInt();
        this.totalPages = in.readInt();
    }

    public static final Creator<HomesResponse> CREATOR = new Creator<HomesResponse>() {
        @Override
        public HomesResponse createFromParcel(Parcel source) {
            return new HomesResponse(source);
        }

        @Override
        public HomesResponse[] newArray(int size) {
            return new HomesResponse[size];
        }
    };
}