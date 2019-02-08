package nl.frankkie.movieapp.model.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import nl.frankkie.movieapp.model.Movie;
import nl.frankkie.movieapp.model.MovieExtended;

import java.util.List;

public class MovieExtendedViewModel extends ViewModel {
    private LiveData<MovieExtended> movieExtended;

    public MovieExtendedViewModel() {
        //Empty constructor needed for ViewModelProviders
    }

    public MovieExtendedViewModel(LiveData<MovieExtended> movieExtended) {
        setMovie(movieExtended);
    }

    public LiveData<MovieExtended> getMovie() {
        return movieExtended;
    }

    public void setMovie(LiveData<MovieExtended> movie) {
        this.movieExtended = movie;
    }
}
