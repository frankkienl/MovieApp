package nl.frankkie.movieapp.rest.response;

import nl.frankkie.movieapp.model.Movie;

public class MoviesResponse {

    public int page;
    public int total_pages;
    public int total_results;

    public Movie[] results;
}
