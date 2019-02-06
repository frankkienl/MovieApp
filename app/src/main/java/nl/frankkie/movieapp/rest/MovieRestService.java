package nl.frankkie.movieapp.rest;

import nl.frankkie.movieapp.model.response.EmptyBodyResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MovieRestService {

    @GET("/trending/{media_type/{time_window}")
    Call<EmptyBodyResponse> trending(@Path("media_type") String mediaType, @Path("time_window") String time_window);

}
