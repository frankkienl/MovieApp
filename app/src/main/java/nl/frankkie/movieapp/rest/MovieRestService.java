package nl.frankkie.movieapp.rest;

import nl.frankkie.movieapp.model.MovieExtended;
import nl.frankkie.movieapp.rest.response.EmptyBodyResponse;
import nl.frankkie.movieapp.rest.response.MoviesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieRestService {

    @GET("/3/trending/{media_type}/{time_window}")
    Call<MoviesResponse> trending(@Path("media_type") String mediaType, @Path("time_window") String time_window, @Query("api_key") String apiKey);

    @GET("/3/discover/movie")
    //?primary_release_date.gte={date_ago}&primary_release_date.lte={date_now}&region={region}
    Call<MoviesResponse> nowPlaying(@Query("api_key") String apiKey, @Query("region") String region, @Query("primary_release_date.gte") String dateAgo, @Query("primary_release_date.lte") String dateNow);

    @GET("/3/discover/movie")
        //?primary_release_date.gte={date_now}&region={region}
    Call<MoviesResponse> upcoming(@Query("api_key") String apiKey, @Query("region") String region, @Query("primary_release_date.gte") String dateNow);

    @GET("/3/movie/{id}")
    Call<MovieExtended> movie(@Path("id") int id, @Query("api_key") String apiKey);
}
