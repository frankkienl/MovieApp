package nl.frankkie.movieapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_movie_detail.*
import nl.frankkie.movieapp.R

class MovieDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        setSupportActionBar(detail_toolbar)

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            val fragment = MovieDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(
                        MovieDetailFragment.ARG_ITEM_ID,
                        intent.getIntExtra(MovieDetailFragment.ARG_ITEM_ID,0)
                    )
                }
            }

            supportFragmentManager.beginTransaction()
                .add(R.id.movie_detail_container, fragment)
                .commit()
        }
    }

    //Up button
    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                navigateUpTo(Intent(this, MovieListActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}
