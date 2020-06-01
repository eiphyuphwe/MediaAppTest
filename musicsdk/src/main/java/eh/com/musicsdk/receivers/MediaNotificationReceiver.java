package eh.com.musicsdk.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import eh.com.musicsdk.utils.Constants;

public class MediaNotificationReceiver extends BroadcastReceiver {


    @Override
    public void onReceive (Context context, Intent intent) {

        if(intent.getAction ().equals ( Constants.ACTION_RESTART ))
        {
            Bundle bundle = intent.getExtras ();
            Intent intent1 = new Intent ( Constants.MY_MUSIC_FILTER );
            intent1.putExtra ( "music_bundle",intent.getExtras () );
            intent1.setAction ( intent.getAction () );
            context.sendBroadcast (intent1  );
        } // new added
        else {

            context.sendBroadcast ( new Intent ( Constants.MY_MUSIC_FILTER )
                    .putExtra ( Constants.ACTIONNAME, intent.getAction () ) );
        }


    }
}
