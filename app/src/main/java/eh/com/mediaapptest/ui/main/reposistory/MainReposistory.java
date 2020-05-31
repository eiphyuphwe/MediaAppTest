package eh.com.mediaapptest.ui.main.reposistory;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;

import eh.com.musicsdk.data.Music;
import eh.com.musicsdk.providers.MusicProviders;

public class MainReposistory {

    Context context;
    public MainReposistory (Application application)
    {
        this.context = application.getApplicationContext ();
    }

    public ArrayList<Music> loadSongs()
    {
      return MusicProviders.provideMusics (context,"musicsdktest" );


    }
}
