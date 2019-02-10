package nl.frankkie.movieapp.rules

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import com.jakewharton.espresso.OkHttp3IdlingResource
import nl.frankkie.movieapp.Config
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

//https://github.com/chiragkunder/espresso-okhttp-idling-resource/blob/master/app/src/androidTest/java/com/ckunder/espressodemo/rules/OkHttpIdlingResourceRule.kt
class OkHttpIdlingResourceRule : TestRule {

    private val resource: IdlingResource = OkHttp3IdlingResource.create("okhttp", Config.getHttpClient())

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                IdlingRegistry.getInstance().register(resource)
                base.evaluate()
                IdlingRegistry.getInstance().unregister(resource)
            }
        }
    }
}