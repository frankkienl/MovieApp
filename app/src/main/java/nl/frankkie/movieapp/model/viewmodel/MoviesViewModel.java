package nl.frankkie.movieapp.model.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import nl.frankkie.movieapp.model.Movie;

import java.util.List;

public class MoviesViewModel extends ViewModel {
    private LiveData<List<Movie>> movies;

    public MoviesViewModel() {
        //Empty constructor needed for ViewModelProviders
    }

    public MoviesViewModel(LiveData<List<Movie>> movies) {
        setMovies(movies);
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public void setMovies(LiveData<List<Movie>> movies) {
        this.movies = movies;
    }
}
