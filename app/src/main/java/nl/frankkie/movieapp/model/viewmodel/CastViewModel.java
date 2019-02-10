package nl.frankkie.movieapp.model.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import nl.frankkie.movieapp.model.CastMember;
import nl.frankkie.movieapp.model.Movie;

import java.util.List;

public class CastViewModel extends ViewModel {
    private LiveData<List<CastMember>> cast;

    public CastViewModel() {
        //Empty constructor needed for ViewModelProviders
    }

    public CastViewModel(LiveData<List<CastMember>> cast) {
        setCast(cast);
    }

    public LiveData<List<CastMember>> getCast() {
        return cast;
    }

    public void setCast(LiveData<List<CastMember>> cast) {
        this.cast = cast;
    }
}
