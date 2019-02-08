package nl.frankkie.movieapp.room;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import nl.frankkie.movieapp.model.Movie;
import nl.frankkie.movieapp.model.MovieExtended;

@Database(entities = {Movie.class, MovieExtended.class}, version = 1)
public abstract class MovieRoomDatabase extends RoomDatabase {
    public abstract MovieDao movieDao();
    public abstract MovieExtendedDao movieExtendedDao();

    private static MovieRoomDatabase INSTANCE;

    public static MovieRoomDatabase getDatabase(final Context context) {
        //Create singleton, use synchronized to prevent race-condition.
        if (INSTANCE == null) {
            synchronized (MovieRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, MovieRoomDatabase.class, "movie_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
