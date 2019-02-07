package nl.frankkie.movieapp.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Movie {

    @PrimaryKey
    public int id;

    public boolean adult;
    public String backdrop_path;
    //public int[] genre_ids; //TODO: https://stackoverflow.com/questions/44986626/android-room-database-how-to-handle-arraylist-in-an-entity
    public String original_language;
    public String original_title;
    public String overview;
    public String poster_path;
    public String release_date;
    public String title;
    public boolean video;
    public double vote_average;
    public int vote_count;
    public double populairity;
}
