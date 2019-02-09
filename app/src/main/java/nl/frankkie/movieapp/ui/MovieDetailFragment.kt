package nl.frankkie.movieapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.movie_detail.view.*
import nl.frankkie.movieapp.R
import nl.frankkie.movieapp.databinding.MovieDetailBinding
import nl.frankkie.movieapp.model.MovieExtended
import nl.frankkie.movieapp.model.viewmodel.MovieExtendedViewModel
import nl.frankkie.movieapp.room.MovieRepository

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a [MovieListActivity]
 * in two-pane mode (on tablets) or a [MovieDetailActivity]
 * on handsets.
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

    private fun movieExtendedUpdated(movieExtended: MovieExtended?) {
        //Refresh databinding
        binding.item = movieExtended
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

    companion object {
        /**
         * The fragment argument representing the liveDataItem ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}
