package nl.frankkie.movieapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.Rule
import androidx.test.rule.ActivityTestRule
import nl.frankkie.movieapp.rules.OkHttpIdlingResourceRule
import nl.frankkie.movieapp.ui.MovieListActivity
import org.junit.Before
import org.junit.Test

//@RunWith(AndroidJUnit4::class)
class DetailScreenTest {

    @get:Rule
    public val activityRule: ActivityTestRule<MovieListActivity> = ActivityTestRule(MovieListActivity::class.java)
    @get:Rule
    var rule = OkHttpIdlingResourceRule()

    //Pick a movie, enter title here
    //Failure to do so will result in failing test
    private val testMovieTitle = "How to Train Your Dragon: The Hidden World"

    @Before
    fun init() {

    }

    @Test
    fun detailScreen_checkTitle() {

        //Check if movie exits
        onView(withText(testMovieTitle))
            .check(matches(withText(testMovieTitle)))

        //Click on it
        onView(withText(testMovieTitle))
            .perform(click())

        //Check correct movie is show on detailpage

        onView(withText(testMovieTitle))
            .check(matches(withText(testMovieTitle)))
    }
}
