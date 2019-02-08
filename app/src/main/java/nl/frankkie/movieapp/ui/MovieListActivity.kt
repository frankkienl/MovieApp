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
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import nl.frankkie.movieapp.Config
import nl.frankkie.movieapp.GlideApp
import nl.frankkie.movieapp.R
import nl.frankkie.movieapp.model.viewmodel.MoviesViewModel
import nl.frankkie.movieapp.model.Movie
import nl.frankkie.movieapp.room.MovieRepository


class MovieListActivity : AppCompatActivity(), LifecycleOwner {

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
        //Create ViewModel
        val moviesViewModel =
            ViewModelProviders.of(this).get(MoviesViewModel::class.java)
        //Fill ViewModel with Movies from Repository
        moviesViewModel.movies = MovieRepository.getNowPlaying(applicationContext)

        //Create adapter for RecyclerView
        val adapter = MyListAdapter()
        recyclerView.adapter = adapter
        //Observe changes, to keep list up to date with Database
        moviesViewModel.movies.observe(
            this,
            Observer { list -> adapter.submitList(list) })

        //Set LayoutManager to Grid, use correct amount of columns
        val layoutManager = GridLayoutManager(this, calculateNoOfColumns(this))
        recyclerView.layoutManager = layoutManager
    }



    private fun calculateNoOfColumns(context: Context): Int {
        //https://stackoverflow.com/questions/33575731/gridlayoutmanager-how-to-auto-fit-columns
        val displayMetrics = context.resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density
        return (dpWidth / 180).toInt()
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    class MyListAdapter : ListAdapter<Movie, MovieViewHolder>(MovieDiffCallback()) {

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

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.movie_list_item, parent, false)
            return MovieViewHolder(view)
        }

        override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
            val item = getItem(position)
            holder.title.text = item.title
            holder.year.text = item.release_date.substring(0,4) //only year

            val imageUrl = Config.BASE_URL_IMAGES + "/w300/" + item.poster_path;
            //holder.poster
            GlideApp.with(holder.poster)
                .load(imageUrl)
                .placeholder(R.drawable.no_poster)
                .error(R.drawable.no_poster)
                .into(holder.poster)

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }
    }

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val poster: ImageView = view.findViewById(R.id.movie_list_item_poster)
        val title: TextView = view.findViewById(R.id.movie_list_item_title)
        val year: TextView = view.findViewById(R.id.movie_list_item_year)
    }
}
