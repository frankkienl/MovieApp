package nl.frankkie.movieapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.GridLayoutManager
import nl.frankkie.movieapp.R
import nl.frankkie.movieapp.room.Movie

import nl.frankkie.movieapp.room.MovieRepository


class MovieListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
    }

    private fun initUI() {
        setContentView(R.layout.activity_movie_list)

        val movieList = findViewById<RecyclerView>(R.id.movie_list)
        setupRecyclerView(movieList)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        val movies = MovieRepository.getNowPlaying(applicationContext);
        recyclerView.adapter = MovieRecyclerViewAdapter(movies)
        val layoutManager = GridLayoutManager(this, calculateNoOfColumns(this))
        recyclerView.layoutManager = layoutManager

        //Show a loading spinner when the list is empty
        if (recyclerView.adapter?.itemCount == 0) {
            findViewById<View>(R.id.movie_list_empty).visibility = View.VISIBLE
        } else {
            findViewById<View>(R.id.movie_list_empty).visibility = View.GONE
        }
    }

    private fun calculateNoOfColumns(context: Context): Int {
        //https://stackoverflow.com/questions/33575731/gridlayoutmanager-how-to-auto-fit-columns
        val displayMetrics = context.resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density
        return (dpWidth / 180).toInt()
    }

    class MovieRecyclerViewAdapter(
        private val liveData: LiveData<List<Movie>>
    ) :
        RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as Movie

                val intent = Intent(v.context, MovieDetailActivity::class.java).apply {
                    putExtra(MovieDetailFragment.ARG_ITEM_ID, item.id)
                }
                v.context.startActivity(intent)

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.movie_list_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (liveData.value == null || liveData.value!!.isEmpty()){
                return
            }
            val item = liveData.value!![position]
            holder.title.text = item.title
            holder.year.text = item.release_date

            val imageUrl = item.poster_path;
            //holder.poster

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() : Int {
            if (liveData.value == null){
                return 0
            }
            return liveData.value!!.size
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val poster: ImageView = view.findViewById(R.id.movie_list_item_poster)
            val title: TextView = view.findViewById(R.id.movie_list_item_title)
            val year: TextView = view.findViewById(R.id.movie_list_item_year)
        }
    }
}
