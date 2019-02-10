package nl.frankkie.movieapp.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import nl.frankkie.movieapp.model.CastMember;

import java.util.List;

@Dao
public interface CastDao {
    @Query("SELECT * FROM CastMember")
    LiveData<List<CastMember>> getCast();

    @Query("SELECT * FROM CastMember WHERE id = :id")
    LiveData<CastMember> getCastMember(int id);

    @Insert
    void insert(CastMember castMember);

    @Query("DELETE FROM CastMember")
    void deleteAll();
}
