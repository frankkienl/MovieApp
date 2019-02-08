package nl.frankkie.movieapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.movie_detail.view.*
import nl.frankkie.movieapp.R
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

    /**
     * The dummy content this fragment is presenting.
     */
    private var liveDataItem: LiveData<MovieExtended>? = null
    private var item: MovieExtended? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Every time you have to use !! the Kotlin compiler cries.
        if (arguments!!.containsKey(ARG_ITEM_ID)) {

            val viewModel = ViewModelProviders.of(this).get(MovieExtendedViewModel::class.java)

            //get liveDataItem from repository
            liveDataItem = MovieRepository.getMovie(
                context!!.applicationContext,
                arguments!!.getInt(ARG_ITEM_ID)
            )
            //put in viewModel
            viewModel.movie = liveDataItem
            viewModel.movie.observe(
                this,
                Observer { movieExtended -> movieExtendedUpdated(movieExtended) })

            activity?.toolbar_layout?.title = liveDataItem?.value?.title
        }
    }

    private fun movieExtendedUpdated(movieExtended: MovieExtended) {
        item = movieExtended

        //refresh ui
        fillUI(view!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.movie_detail, container, false)

        //Fill ui
        fillUI(rootView)

        return rootView
    }

    private fun fillUI(rootView: View) {
        if (item != null) {
            rootView.movie_detail_title.text = item?.title
            rootView.movie_detail_tagline.text = item?.tagline
            rootView.movie_detail_overview.text = item?.overview
            rootView.movie_detail_rating.text = "Rating: {item?.vote_average}"
            rootView.movie_detail_votes.text = "Votes: {item?.votes}"
        }
    }

    companion object {
        /**
         * The fragment argument representing the liveDataItem ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}
