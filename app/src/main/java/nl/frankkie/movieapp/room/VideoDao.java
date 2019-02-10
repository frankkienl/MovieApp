package nl.frankkie.movieapp.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import nl.frankkie.movieapp.model.CastMember;
import nl.frankkie.movieapp.model.Video;

import java.util.List;

@Dao
public interface VideoDao {
    @Query("SELECT * FROM Video")
    LiveData<List<Video>> getVideos();

    @Query("SELECT * FROM Video WHERE id = :id")
    LiveData<Video> getVideo(String id);

    @Insert
    void insert(Video castMember);

    @Query("DELETE FROM Video")
    void deleteAll();
}
