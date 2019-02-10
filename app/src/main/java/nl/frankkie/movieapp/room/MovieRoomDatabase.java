package nl.frankkie.movieapp.room;

import android.content.Context;
import androidx.room.*;
import nl.frankkie.movieapp.model.CastMember;
import nl.frankkie.movieapp.model.Movie;
import nl.frankkie.movieapp.model.MovieExtended;

@Database(entities = {Movie.class, MovieExtended.class, CastMember.class}, version = 1)
@TypeConverters(MovieConverters.class)
public abstract class MovieRoomDatabase extends RoomDatabase {
    public abstract MovieDao movieDao();
    public abstract MovieExtendedDao movieExtendedDao();
    public abstract CastDao castDao();

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
