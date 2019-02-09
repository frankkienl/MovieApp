package nl.frankkie.movieapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * When you receive a list of movies,
 * you only get a subset of the data.
 * This is the full data.
 * For the the subset, see Movie
 */
@Entity
public class MovieExtended {

    @PrimaryKey
    public int id;

    public boolean adult;
    public String backdrop_path;
    public Genre[] genres;
    public String homepage;
    public String imdb_id;

    public String original_language;
    public String original_title;
    public String overview;
    public double popularity;
    public String poster_path;
    public String release_date;
    public int runtime;
    public String status;
    public String tagline;
    public String title;
    public boolean video;
    public double vote_average;
    public int vote_count;

    //Formatting
    public String genres_as_string;

    public class Genre {
        int id;
        String name;

        @NonNull
        @Override
        public String toString() {
            return name;
        }
    }
}
