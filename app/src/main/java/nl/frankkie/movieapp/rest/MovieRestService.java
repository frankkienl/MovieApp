package nl.frankkie.movieapp.rest;

import nl.frankkie.movieapp.rest.response.EmptyBodyResponse;
import nl.frankkie.movieapp.rest.response.MoviesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieRestService {

    @GET("/3/trending/{media_type/{time_window}")
    Call<EmptyBodyResponse> trending(@Path("media_type") String mediaType, @Path("time_window") String time_window);

    @GET("/3/discover/movie")
    //?primary_release_date.gte={date_week_ago}&primary_release_date.lte={date_now}
    Call<MoviesResponse> nowPlaying(@Query("api_key") String apiKey, @Query("primary_release_date.gte") String dateWeekAgo, @Query("primary_release_date.lte") String dateNow);

}
