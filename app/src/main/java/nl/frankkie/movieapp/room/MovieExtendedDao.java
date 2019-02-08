package nl.frankkie.movieapp.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import nl.frankkie.movieapp.model.Movie;
import nl.frankkie.movieapp.model.MovieExtended;

import java.util.List;

@Dao
public interface MovieExtendedDao {
    @Query("SELECT * FROM MovieExtended")
    LiveData<List<MovieExtended>> getMovies();

    @Query("SELECT * FROM movie WHERE id = :id")
    LiveData<MovieExtended> getMovie(int id);

    @Insert
    void insert(MovieExtended movie);

    @Query("DELETE FROM MovieExtended")
    void deleteAll();
}
