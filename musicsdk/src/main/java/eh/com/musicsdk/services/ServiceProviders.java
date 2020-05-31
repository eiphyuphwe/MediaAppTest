package eh.com.musicsdk.services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import eh.com.musicsdk.utils.Constants;

public class ServiceProviders {




    public static void onPlayMusic (Context context, String title, String artist, String path)
    {
        Intent intent = new Intent ( context,MusicPlayerService.class );
        Bundle bundle = new Bundle ( );
        bundle.putString ( Constants.TITLE,title );
        bundle.putString ( Constants.ARTIST,artist );
        bundle.putString ( Constants.PATH,path );
        intent.putExtras ( bundle );
        intent.setAction ( Constants.ACTION_FORGROUND);
        //context.startService ( intent );
        context.startForegroundService ( intent );
    }


    public static void onPauseMusic(Context context)
    {

        context.sendBroadcast (new Intent ( Constants.MY_MUSIC_FILTER )
                .putExtra ( Constants.ACTIONNAME, Constants.ACTION_PAUSE ));
    }

    public static void onResumeMusic(Context context)
    {

        context.sendBroadcast (new Intent ( Constants.MY_MUSIC_FILTER )
                .putExtra ( Constants.ACTIONNAME, Constants.ACTION_RESUME ));
    }


    public static void onStopMusic(Context context)
    {

        context.sendBroadcast (new Intent ( Constants.MY_MUSIC_FILTER )
                .putExtra ( Constants.ACTIONNAME, Constants.ACTION_STOP ));
    }


    public static void stopService(Context context)
    {
        Intent intent = new Intent ( context,MusicPlayerService.class );
        intent.setAction ( Constants.ACTION_STOP);
        context.stopService (intent);
    }


}
