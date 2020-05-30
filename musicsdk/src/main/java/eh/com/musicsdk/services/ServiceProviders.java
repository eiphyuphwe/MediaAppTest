package eh.com.musicsdk.services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import eh.com.musicsdk.receivers.MediaNotificationReceiver;
import eh.com.musicsdk.utils.MyConstants;

public class ServiceProviders {




    public static void playMusic(Context context, String title, String artist, String path)
    {
        Intent intent = new Intent ( context,MusicPlayerService.class );
        Bundle bundle = new Bundle ( );
        bundle.putString ( MyConstants.TITLE,title );
        bundle.putString ( MyConstants.ARTIST,artist );
        bundle.putString ( MyConstants.PATH,path );
        intent.putExtras ( bundle );
        intent.setAction (MyConstants.ACTION_FORGROUND);
        context.startService ( intent );
        //context.startForegroundService ( intent );
    }


    public static void pauseMusic(Context context)
    {

        context.sendBroadcast (new Intent ( MediaNotificationReceiver.MY_MUSIC_FILTER )
                .putExtra ( MediaNotificationReceiver.ACTIONNAME,MyConstants.ACTION_PAUSE ));
    }

    public static void resumeMusic(Context context)
    {

        context.sendBroadcast (new Intent ( MediaNotificationReceiver.MY_MUSIC_FILTER )
                .putExtra ( MediaNotificationReceiver.ACTIONNAME,MyConstants.ACTION_RESUME ));
    }


    public static void onStopMusic(Context context)
    {

        context.sendBroadcast (new Intent ( MediaNotificationReceiver.MY_MUSIC_FILTER )
                .putExtra ( MediaNotificationReceiver.ACTIONNAME,MyConstants.ACTION_STOP ));
    }


    public static void stopMusic(Context context)
    {
        Intent intent = new Intent ( context,MusicPlayerService.class );
        intent.setAction (MyConstants.ACTION_STOP);
        context.stopService (intent);
    }

    public static boolean isMusicPlaying(Context context)
    {
        MusicPlayerService service = new MusicPlayerService ();
        return  service.isMusicPlaying ();
    }
}
