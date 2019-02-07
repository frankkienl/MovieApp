package nl.frankkie.movieapp.model.viewmodel;

import androidx.lifecycle.ViewModel;
import nl.frankkie.movieapp.room.Movie;

public class MovieViewModel extends ViewModel {
    private Movie movie;

    public Movie getMovie() {
        return movie;
    }
}
