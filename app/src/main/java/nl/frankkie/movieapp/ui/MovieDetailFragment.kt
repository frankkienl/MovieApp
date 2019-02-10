package nl.frankkie.movieapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.*
import kotlinx.android.synthetic.main.activity_movie_detail.*
import nl.frankkie.movieapp.Config
import nl.frankkie.movieapp.GlideApp
import nl.frankkie.movieapp.R
import nl.frankkie.movieapp.databinding.MovieDetailBinding
import nl.frankkie.movieapp.model.CastMember
import nl.frankkie.movieapp.model.MovieExtended
import nl.frankkie.movieapp.model.viewmodel.CastViewModel
import nl.frankkie.movieapp.model.viewmodel.MovieExtendedViewModel
import nl.frankkie.movieapp.room.MovieRepository
import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is contained in a [MovieDetailActivity]
 */
class MovieDetailFragment : Fragment(), LifecycleOwner {

    //Data binding
    private lateinit var binding: MovieDetailBinding
    private lateinit var recyclerView: RecyclerView
    val adapter: MyCastListAdapter = MyCastListAdapter()

    private val dateFormatterLocale: DateFormat = SimpleDateFormat.getDateInstance()
    @SuppressLint("SimpleDateFormat")
    private val dateFormatterApi: DateFormat = SimpleDateFormat("y-M-d")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Every time you have to use !! the Kotlin compiler cries.
        if (arguments!!.containsKey(ARG_ITEM_ID)) {

            val movieId = arguments!!.getInt(ARG_ITEM_ID)

            //Get the ViewModels, to observe the LiveData
            val movieViewModel = ViewModelProviders.of(this).get(MovieExtendedViewModel::class.java)
            val castViewModel = ViewModelProviders.of(this).get(CastViewModel::class.java)

            //get liveDataItem from repository
            val movieLiveDataItem = MovieRepository.getMovie(context!!.applicationContext, movieId)
            val castLiveData = MovieRepository.getCast(context!!.applicationContext, movieId)

            //put in viewModel (movie)
            movieViewModel.movie = movieLiveDataItem
            movieViewModel.movie.observe(
                this,
                Observer { movieExtended -> movieExtendedUpdated(movieExtended) })
            //Cast
            castViewModel.cast = castLiveData
            castViewModel.cast.observe(this,
                Observer { cast -> updateCastList(cast) })

            activity?.toolbar_layout?.title = movieLiveDataItem.value?.title
        }
    }

    private fun movieExtendedUpdated(item: MovieExtended?) {
        if (item == null) {
            return
        }

        //String formatting
        item.genres_as_string = item.genres.joinToString() //Thank you Kotlin :-)
        item.release_date = dateFormatterLocale.format(dateFormatterApi.parse(item.release_date))

        //Refresh data-binding
        binding.item = item

        //Refresh poster
        val imageUrl = Config.BASE_URL_IMAGES + "/w300/" + item.poster_path;
        //holder.poster
        GlideApp.with(binding.movieDetailPoster)
            .load(imageUrl)
            .placeholder(R.drawable.no_poster)
            .error(R.drawable.no_poster)
            .into(binding.movieDetailPoster)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Databinding creates the view
        binding = MovieDetailBinding.inflate(inflater)

        val rootView = binding.root

        recyclerView = binding.movieDetailCast
        setupRecyclerView()

        return rootView
    }

    private fun updateCastList(list: List<CastMember>) {
        adapter.submitList(list)

        //Scroll to start
        recyclerView.scrollToPosition(0)

        if (list.isEmpty()){
            binding.movieDetailCastLabel.visibility = View.GONE
        } else {
            binding.movieDetailCastLabel.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView() {
        //Set LayoutManager to Grid, use correct amount of columns
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        //Set adapter in RecyclerView
        recyclerView.adapter = adapter
    }

    fun shareMovie() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                getText(R.string.checkout_movie).toString().format(binding.item?.title, binding.item?.homepage)
            )
            type = "text/plain"
        }
        startActivity(sendIntent)
    }

    class MyCastListAdapter : ListAdapter<CastMember, CastViewHolder>(CastDiffCallback()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.cast_member, parent, false)
            return CastViewHolder(view)
        }

        override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
            val item = getItem(position)
            holder.name.text = item.name
            holder.role.text = item.character

            val imageUrl = Config.BASE_URL_IMAGES + "/w185/" + item.profile_path
            //holder.poster
            GlideApp.with(holder.photo)
                .load(imageUrl)
                .placeholder(R.drawable.no_poster)
                .error(R.drawable.no_poster)
                .into(holder.photo)

        }
    }

    class CastDiffCallback : DiffUtil.ItemCallback<CastMember>() {
        override fun areItemsTheSame(oldItem: CastMember, newItem: CastMember): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CastMember, newItem: CastMember): Boolean {
            return oldItem == newItem
        }
    }

    class CastViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val photo: ImageView = view.findViewById(R.id.cast_photo)
        val name: TextView = view.findViewById(R.id.cast_name)
        val role: TextView = view.findViewById(R.id.cast_role)
    }

    companion object {
        /**
         * The fragment argument representing the liveDataItem ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}
