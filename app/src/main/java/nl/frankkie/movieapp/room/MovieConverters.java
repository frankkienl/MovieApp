package nl.frankkie.movieapp.room;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nl.frankkie.movieapp.model.MovieExtended;

import java.lang.reflect.Type;

public class MovieConverters {
    //https://stackoverflow.com/questions/44986626/android-room-database-how-to-handle-arraylist-in-an-entity
    @TypeConverter
    public static MovieExtended.Genre[] fromString(String json) {
        Type listType = new TypeToken<MovieExtended.Genre[]>() {}.getType();
        return new Gson().fromJson(json, listType);
    }

    @TypeConverter
    public static String fromArray(MovieExtended.Genre[] genresArray) {
        Gson gson = new Gson();
        String json = gson.toJson(genresArray);
        return json;
    }
}
