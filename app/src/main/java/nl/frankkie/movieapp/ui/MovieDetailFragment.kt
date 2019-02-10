package nl.frankkie.movieapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
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
import nl.frankkie.movieapp.Config
import nl.frankkie.movieapp.GlideApp
import nl.frankkie.movieapp.R
import nl.frankkie.movieapp.databinding.MovieDetailBinding
import nl.frankkie.movieapp.model.CastMember
import nl.frankkie.movieapp.model.Movie
import nl.frankkie.movieapp.model.MovieExtended
import nl.frankkie.movieapp.model.Video
import nl.frankkie.movieapp.model.viewmodel.CastViewModel
import nl.frankkie.movieapp.model.viewmodel.MovieExtendedViewModel
import nl.frankkie.movieapp.model.viewmodel.VideosViewModel
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
    private lateinit var castRecyclerView: RecyclerView
    private lateinit var videosRecyclerView: RecyclerView
    val castAdapter: MyCastListAdapter = MyCastListAdapter()
    val videosAdapter: MyVideoListAdapter = MyVideoListAdapter()

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
            val videosViewModel = ViewModelProviders.of(this).get(VideosViewModel::class.java)

            //get liveDataItem from repository
            val movieLiveDataItem = MovieRepository.getMovie(context!!.applicationContext, movieId)
            val castLiveData = MovieRepository.getCast(context!!.applicationContext, movieId)
            val videosLiveDate = MovieRepository.getVideos(context!!.applicationContext, movieId)

            //put in viewModel (movie)
            movieViewModel.movie = movieLiveDataItem
            movieViewModel.movie.observe(
                this,
                Observer { movieExtended -> movieExtendedUpdated(movieExtended) })
            //Cast
            castViewModel.cast = castLiveData
            castViewModel.cast.observe(this,
                Observer { cast -> updateCastList(cast) })
            //Videos
            videosViewModel.videos = videosLiveDate
            videosViewModel.videos.observe(
                this,
                Observer { videos -> updateVideosList(videos) }
            )

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

        castRecyclerView = binding.movieDetailCast
        videosRecyclerView = binding.movieDetailVideos
        setupRecyclerViews()

        return rootView
    }

    private fun updateCastList(list: List<CastMember>) {
        castAdapter.submitList(list)

        //Scroll to start
        castRecyclerView.scrollToPosition(0)

        if (list.isEmpty()) {
            binding.movieDetailCastLabel.visibility = View.GONE
        } else {
            binding.movieDetailCastLabel.visibility = View.VISIBLE
        }
    }

    private fun updateVideosList(list: List<Video>) {
        videosAdapter.submitList(list)

        //Scroll to start
        videosRecyclerView.scrollToPosition(0)

        if (list.isEmpty()) {
            binding.movieDetailVideosLabel.visibility = View.GONE
        } else {
            binding.movieDetailVideosLabel.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerViews() {
        //Set LayoutManager to Horizontal LinearLayout (Cast)
        val horizontalLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        castRecyclerView.layoutManager = horizontalLayoutManager

        //Videos
        val verticalLayoutManager = LinearLayoutManager(context)
        videosRecyclerView.layoutManager = verticalLayoutManager

        //Set castAdapter in RecyclerView
        castRecyclerView.adapter = castAdapter
        videosRecyclerView.adapter = videosAdapter
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


    class MyVideoListAdapter : ListAdapter<Video, VideoViewHolder>(VideoDiffCallback()) {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as Video

                //YouTube intent
                val intent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=" + item.key))

                v.context.startActivity(intent)

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.video, parent, false)
            return VideoViewHolder(view)
        }

        override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
            val item = getItem(position)
            holder.title.text = item.name

            val imageUrl = Config.BASE_URL_IMAGE_YT.format(item.key)
            //holder.poster
            GlideApp.with(holder.thumbnail)
                .load(imageUrl)
                .placeholder(R.drawable.no_poster)
                .error(R.drawable.no_poster)
                .into(holder.thumbnail)

            if (item.site == "YouTube") {
                with(holder.itemView) {
                    tag = item
                    setOnClickListener(onClickListener)
                }
            }
        }
    }

    class VideoDiffCallback : DiffUtil.ItemCallback<Video>() {
        override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem == newItem
        }
    }

    class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thumbnail: ImageView = view.findViewById(R.id.video_thumbnail)
        val title: TextView = view.findViewById(R.id.video_title)
    }

    companion object {
        /**
         * The fragment argument representing the liveDataItem ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}
