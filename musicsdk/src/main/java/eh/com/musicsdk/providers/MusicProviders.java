package eh.com.musicsdk.providers;

import android.content.Context;

import java.util.ArrayList;

import eh.com.musicsdk.data.Music;
import eh.com.musicsdk.utils.Utils;

public class MusicProviders {


    public static ArrayList<Music> provideMusics(Context context,String folderName)
    {
        ArrayList<Music> musicList = new ArrayList<> ();
        musicList = Utils.getMusicFilesfromSDCardFolder (context,folderName);
        return musicList;
    }

}
