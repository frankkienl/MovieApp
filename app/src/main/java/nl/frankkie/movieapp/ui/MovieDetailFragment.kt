package nl.frankkie.movieapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_movie_detail.*
import nl.frankkie.movieapp.Config
import nl.frankkie.movieapp.GlideApp
import nl.frankkie.movieapp.R
import nl.frankkie.movieapp.databinding.MovieDetailBinding
import nl.frankkie.movieapp.model.MovieExtended
import nl.frankkie.movieapp.model.viewmodel.MovieExtendedViewModel
import nl.frankkie.movieapp.room.MovieRepository

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is contained in a [MovieDetailActivity]
 */
class MovieDetailFragment : Fragment(), LifecycleOwner {

    //Data binding
    private lateinit var binding: MovieDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Every time you have to use !! the Kotlin compiler cries.
        if (arguments!!.containsKey(ARG_ITEM_ID)) {

            //Get the ViewModel, to observe the LiveData
            val viewModel = ViewModelProviders.of(this).get(MovieExtendedViewModel::class.java)

            //get liveDataItem from repository
            val liveDataItem = MovieRepository.getMovie(
                context!!.applicationContext,
                arguments!!.getInt(ARG_ITEM_ID)
            )
            //put in viewModel
            viewModel.movie = liveDataItem
            viewModel.movie.observe(
                this,
                Observer { movieExtended -> movieExtendedUpdated(movieExtended) })

            activity?.toolbar_layout?.title = liveDataItem.value?.title
        }
    }

    private fun movieExtendedUpdated(item: MovieExtended?) {
        if (item == null) {
            return
        }
        //Refresh databinding
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

        return rootView
    }

    fun shareMovie(){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getText(R.string.checkout_movie).toString().format(binding.item?.title, binding.item?.homepage))
            type = "text/plain"
        }
        startActivity(sendIntent)
    }

    companion object {
        /**
         * The fragment argument representing the liveDataItem ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}
