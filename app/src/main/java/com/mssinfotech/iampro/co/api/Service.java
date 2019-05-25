package com.mssinfotech.iampro.co.api;

/**
 * Created by mssinfotech on 15/01/19.
 */



import com.mssinfotech.iampro.co.model.HomesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by delaroy on 5/18/17.
 */
public interface Service {

    @GET("movie/popular")
    Call<HomesResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<HomesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

}
