package nl.frankkie.movieapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CastMember {

    @PrimaryKey
    public int id;

    public int cast_id;
    public String character;
    public String credit_id;
    public int gender;
    public String name;
    public int order;
    public String profile_path;
}
