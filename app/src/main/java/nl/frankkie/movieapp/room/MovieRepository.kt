package nl.frankkie.movieapp.room

import android.content.Context
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

object MovieRepository {

    private var restService: MovieRestService? = null

    fun getNowPlaying(context: Context): LiveData<List<Movie>> {
        //Here's the plan,
        //return from database now, and queue API request.
        val dao = MovieRoomDatabase.getDatabase(context).movieDao()

        if (restService == null) {
            synchronized(MovieRepository::class.java) {
                if (restService == null) {
                    val b = Retrofit.Builder()
                    b.baseUrl(Config.BASE_URL)
                        b.addConverterFactory(GsonConverterFactory.create())
                    val retrofit = b.build()
                    restService = retrofit.create(MovieRestService::class.java)
                }
            }
        }

        restService!!.nowPlaying("2019-02-01", "2019-02-07")
            .enqueue(object : Callback<MoviesResponse> {
                override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
                    val mr = response.body()
                    if (mr!!.results == null){

                    }
                    val movies = mr!!.results
                    dao.deleteAll() //clear
                    for (movie in movies) {
                        dao.insert(movie)
                    }
                }

                override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                    Toast.makeText(context, "Network request failed", Toast.LENGTH_SHORT).show()
                }
            })

        var data = dao.movies
        if (data == null){
            data = MutableLiveData<List<Movie>>()
        }
        return data
    }

}
