package nl.frankkie.movieapp.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import nl.frankkie.movieapp.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> getMovies();

    @Query("SELECT * FROM movie WHERE id = :id")
    LiveData<Movie> getMovie(int id);

    @Query("SELECT * FROM movie WHERE title LIKE :search || '%'")
    LiveData<List<Movie>> searchMovies(String search);

    @Insert
    void insert(Movie movie);

    @Query("DELETE FROM movie")
    void deleteAll();
}
