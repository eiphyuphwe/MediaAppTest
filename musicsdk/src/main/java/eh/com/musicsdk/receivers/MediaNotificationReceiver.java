package eh.com.musicsdk.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MediaNotificationReceiver extends BroadcastReceiver {

    public static String ACTIONNAME = "actioname";
    public static String MY_MUSIC_FILTER = "TRACKS_FILTER";
    @Override
    public void onReceive (Context context, Intent intent) {

        context.sendBroadcast (new Intent ( MY_MUSIC_FILTER )
        .putExtra ( ACTIONNAME,intent.getAction () ));


    }
}
