package eh.com.musicsdk.providers;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import eh.com.musicsdk.data.Music;
import eh.com.musicsdk.utils.Utils;

public class MusicProviders {

    private int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 110;
    public static ArrayList<Music> provideMusics(Context context,String folderPath)
    {
        ArrayList<Music> musicList = new ArrayList<> ();
        musicList = Utils.getMusicFilesfromSDKFolder (context,folderPath);
        return musicList;
    }

}
