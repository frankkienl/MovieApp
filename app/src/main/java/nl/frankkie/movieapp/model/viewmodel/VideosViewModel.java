package nl.frankkie.movieapp.model.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import nl.frankkie.movieapp.model.CastMember;
import nl.frankkie.movieapp.model.Video;

import java.util.List;

public class VideosViewModel extends ViewModel {
    private LiveData<List<Video>> videos;

    public VideosViewModel() {
        //Empty constructor needed for ViewModelProviders
    }

    public VideosViewModel(LiveData<List<Video>> video) {
        setVideos(video);
    }

    public LiveData<List<Video>> getVideos() {
        return videos;
    }

    public void setVideos(LiveData<List<Video>> videos) {
        this.videos = videos;
    }
}
