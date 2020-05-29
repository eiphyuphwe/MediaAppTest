package eh.com.musicsdk.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

import eh.com.musicsdk.data.Music;

public class Utils {



    public static ArrayList< Music> getMusicFilesfromSDKFolder(final Context context,String folderName)
    {
        ArrayList<Music> musicList = new ArrayList<> ( );
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.TITLE, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST,};
        Cursor c = context.getContentResolver().query(uri,
                projection,
                MediaStore.Audio.Media.DATA + " like ? ",
                new String[]{"%musicsdktest%"}, null);
        if(c !=null)
        {
            while ((c.moveToNext ()))
            {
                Music music = new Music ();
                String filePath = c.getString ( 0 );
                String name = c.getString ( 1 );
                String album = c.getString ( 2 );
                String artist = c.getString ( 3 );
                music.setName ( name );
                music.setAlbum ( album );
                music.setArtist ( artist );
                music.setFilePath ( filePath );
                musicList.add ( music );
            }
            c.close ();
        }
        return musicList;
    }
}
