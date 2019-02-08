package nl.frankkie.movieapp.room

import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.frankkie.movieapp.Config
import nl.frankkie.movieapp.rest.MovieRestService
import nl.frankkie.movieapp.rest.response.MoviesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.RuntimeException
import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor

object MovieRepository {

    private var restService: MovieRestService? = null
    private var apiKey: String? = null

    fun getNowPlaying(context: Context): LiveData<List<Movie>> {
        //Here's the plan,
        //return from database now, and queue API request.
        val dao = MovieRoomDatabase.getDatabase(context).movieDao()

        if (restService == null) restService = Config.getRestService()

        //ApiKey may not be present in the app.
        if (apiKey == null) {
            //If null, try to get from assets
            apiKey = Config.getApiKey(context)
            //Still null? That's a problem
            if (apiKey == null) {
                Toast.makeText(
                    context,
                    "Api key is not available, check /assets/apikey.txt !!",
                    Toast.LENGTH_LONG
                )
                    .show()
                throw RuntimeException("Api key not available")
            }
        }

        restService!!.nowPlaying(apiKey, "2019-02-01", "2019-02-07")
            .enqueue(object : Callback<MoviesResponse> {
                override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
                    if (response.isSuccessful) {
                        val mr = response.body()
                        if (mr != null) {
                            val movies = mr.results
                            //Separate thread
                            PersistMovies(dao, movies).execute()
                        }
                    } else {
                        Toast.makeText(context, "Network request failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                    Toast.makeText(context, "Network request failed", Toast.LENGTH_SHORT).show()
                }
            })

        var data = dao.movies
        if (data == null) {
            data = MutableLiveData<List<Movie>>()
        }
        return data
    }

    class PersistMovies(private val movieDoa: MovieDao, private val movies: Array<Movie>) : AsyncTask<Void, Void, Int>() {
        override fun doInBackground(vararg params: Void?): Int {
            movieDoa.deleteAll() //clear
            for (movie in movies) {
                movieDoa.insert(movie)
            }
            return movies.size
        }
    }
}
