package nl.frankkie.movieapp.room

import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import androidx.lifecycle.LiveData
import nl.frankkie.movieapp.Config
import nl.frankkie.movieapp.model.Movie
import nl.frankkie.movieapp.model.MovieExtended
import nl.frankkie.movieapp.rest.MovieRestService
import nl.frankkie.movieapp.rest.response.MoviesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException

object MovieRepository {

    private var restService: MovieRestService? = null
    private var apiKey: String? = null

    private fun prepareRestClient(context: Context) {
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
    }

    fun getMovie(context: Context, id: Int): LiveData<MovieExtended> {
        val dao = MovieRoomDatabase.getDatabase(context).movieExtendedDao()
        prepareRestClient(context)

        //Request from API, put in Database, let LiveData pick it up
        restService!!.movie(apiKey, id)
            .enqueue(object : Callback<MovieExtended> {
                override fun onResponse(call: Call<MovieExtended>, response: Response<MovieExtended>) {
                    if (response.isSuccessful) {
                        val movie = response.body()
                        if (movie != null) {
                            //Separate thread
                            MovieRepository.PersistMovieExtended(dao, movie).execute()
                        }
                    } else {
                        Toast.makeText(context, "Network request failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<MovieExtended>, t: Throwable) {
                    Toast.makeText(context, "Network request failed", Toast.LENGTH_SHORT).show()
                }
            })

        return dao.getMovie(id)
    }

    fun getNowPlaying(context: Context): LiveData<List<Movie>> {
        //Here's the plan,
        //return from database now, and queue API request.
        //LiveData will refresh data when received
        val dao = MovieRoomDatabase.getDatabase(context).movieDao()

        prepareRestClient(context)

        //Request from API, put in Database, let LiveData pick it up
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

        return dao.movies
    }

    class PersistMovies(private val movieDoa: MovieDao, private val movies: Array<Movie>) :
        AsyncTask<Void, Void, Int>() {
        override fun doInBackground(vararg params: Void?): Int {
            movieDoa.deleteAll() //clear
            for (movie in movies) {
                movieDoa.insert(movie)
            }
            return movies.size
        }
    }

    class PersistMovieExtended(private val movieDoa: MovieExtendedDao, private val movieExtended: MovieExtended) :
        AsyncTask<Void, Void, Int>() {
        override fun doInBackground(vararg params: Void?): Int {
            movieDoa.deleteAll() //clear
            movieDoa.insert(movieExtended)
            return movieExtended.id
        }
    }
}
