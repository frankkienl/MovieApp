package nl.frankkie.movieapp;

import android.content.Context;
import android.content.res.AssetManager;
import androidx.test.espresso.IdlingResource;
import com.jakewharton.espresso.OkHttp3IdlingResource;
import nl.frankkie.movieapp.rest.MovieRestService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Config {
    public static final String BASE_URL = "https://api.themoviedb.org/";
    public static final String BASE_URL_IMAGES = "https://image.tmdb.org/t/p/";
    public static final String BASE_URL_IMAGE_YT = "https://img.youtube.com/vi/%s/hqdefault.jpg";

    private static MovieRestService restService;
    private static OkHttpClient httpClient;

    /**
     * Get the API key
     * This is in a file that's not under source-control
     *
     * @param context application context to access the assets
     * @return apiKey if found, null otherwise
     */
    public static String getApiKey(Context context) {
        AssetManager assetManager = context.getAssets();
        try {
            String[] assets = assetManager.list("");
            for (String asset : assets) {
                if (asset.equals("apikey.txt")) { //if exists
                    InputStream assetInputStream = assetManager.open(asset);
                    Scanner scanner = new Scanner(assetInputStream);
                    return scanner.next();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static MovieRestService getRestService() {
        if (restService == null) {
            synchronized (Config.class) {
                //prevent race-condition for singleton-creation
                if (restService == null) {
                    OkHttpClient client = getHttpClient();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(Config.BASE_URL)
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    restService = retrofit.create(MovieRestService.class);
                }
            }
        }
        return restService;
    }

    public static OkHttpClient getHttpClient() {
        if (httpClient == null) {
            synchronized (Config.class) {
                //prevent race-condition for singleton-creation
                if (httpClient == null) {
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    if (BuildConfig.DEBUG) {
                        //only in debug, because will log http-body.
                        builder.addInterceptor(interceptor);
                    }
                    OkHttpClient client = builder.build();
                    IdlingResource resource = OkHttp3IdlingResource.create("OkHttp", client);
                    //Espresso.registerIdlingResources(resource);
                    httpClient = client;
                }
            }
        }
        return httpClient;
    }
}
