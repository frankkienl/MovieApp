package nl.frankkie.movieapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Video {

    @PrimaryKey
    @NonNull
    public String id;

    public String iso_639_1;
    public String iso_3166_1;
    public String key;
    public String name;
    public String site;
    public int size;
    public String type;
}
