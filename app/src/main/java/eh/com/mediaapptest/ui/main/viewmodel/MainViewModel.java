package eh.com.mediaapptest.ui.main.viewmodel;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import eh.com.musicsdk.data.Music;
import eh.com.musicsdk.providers.MusicProviders;
import eh.com.musicsdk.services.ServiceProviders;

public class MainViewModel extends AndroidViewModel {

    private Context context;
    private MutableLiveData< ArrayList< Music >> musicLists = new MediatorLiveData<> ();
    public MainViewModel(Application application)
    {
        super(application);
        this.context = application.getApplicationContext ();
    }

    public void loadMusicLists(String folderName)
    {

        ArrayList<Music> musicArrayList = MusicProviders.provideMusics (context,folderName );
        musicLists.setValue ( musicArrayList );

    }

    public MutableLiveData< ArrayList< Music > > getSongLists()
    {
        return musicLists;
    }

    public void onStopService(Context context)
    {
        ServiceProviders.stopService ( context );
    }

    public void onPlayMusic(Context context, String title, String artist, String path)
    {
        ServiceProviders.onPlayMusic (context, title, artist, path );
    }

    public void onPauseMusic(Context context)
    {
        ServiceProviders.onPauseMusic ( context );
    }

    public void onResumeMusic(Context context)
    {
        ServiceProviders.onResumeMusic ( context );
    }

    public void onStopMusic(Context context)
    {
        ServiceProviders.onStopMusic ( context);
    }

    public void onRestart(Context context, String title, String artist, String path)
    {
        ServiceProviders.onRestart ( context,title,artist,path );
    }

}
