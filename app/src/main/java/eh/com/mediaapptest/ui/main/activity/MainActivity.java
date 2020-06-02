package eh.com.mediaapptest.ui.main.activity;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import eh.com.mediaapptest.R;
import eh.com.mediaapptest.ui.main.adapters.SongAdapter;
import eh.com.mediaapptest.ui.main.interfaces.Playable;
import eh.com.mediaapptest.ui.main.viewmodel.MainViewModel;
import eh.com.mediaapptest.utils.MediaAppConstants;
import eh.com.mediaapptest.utils.Utils;
import eh.com.musicsdk.data.Music;

public class MainActivity extends AppCompatActivity implements Playable {

    private int currentIndex;
    boolean isPlaying = false;
    private RecyclerView rcySongs;
    private SongAdapter songAdapter;
    private ImageView iv_play, iv_stop;
    private TextView tvTitle, tvArtist, tvAlbum;
    private ArrayList< Music > songList;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    String path = "";
    androidx.appcompat.widget.Toolbar toolbar;
    public static Music selectedSong;
    MainViewModel viewModel;
    private boolean notiIsPlay = false;
    private boolean isResume = false;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        declare ();


        if (checkPermissionREAD_EXTERNAL_STORAGE ( MainActivity.this )) {
            viewModel.loadMusicLists ( "musicsdktest" );

        }

        getMusicDatafromSDK ();

        songAdapter = new SongAdapter ( getApplicationContext (), songList, new SongAdapter.RecyclerItemClickListener () {
            @Override
            public void onClickListener (Music song, int position) {

                toolbar.setVisibility ( View.VISIBLE );
                if (!path.equalsIgnoreCase ( "" )) {
                    if (!path.equalsIgnoreCase ( song.getFilePath () )) {
                        // not same song

                        if (isPlaying) {
                            viewModel.onRestart ( MainActivity.this, song.getName (), song.getArtist (), song.getFilePath () );
                            Utils.createNotification ( MainActivity.this, song, true );
                        } else {

                            NotificationManager manager = getSystemService ( NotificationManager.class );
                            manager.cancelAll (); // clear notification also
                            isResume = false;
                            resetPlayer ();
                        }

                    }
                }

                changeSelectedSong ( position ); // highlight selected song
                showPlaySong ( song ); // show play song , visible the toobar


            }
        } );

        rcySongs.setAdapter ( songAdapter );


        catchEvents ();
        IntentFilter notiIntentFilter = new IntentFilter ( MediaAppConstants.NOTIFILTER );
        registerReceiver ( broadcastReceiver, notiIntentFilter );

    }


    private void resetPlayer ( ) {
        isPlaying = false;
        iv_play.setImageResource ( R.drawable.ic_play_circle_outline_black_24dp );
        viewModel.onStopMusic ( getApplicationContext () );
        //viewModel.onStopService ( getApplicationContext () );
    }

    private void changeSelectedSong (int index) {
        songAdapter.notifyItemChanged ( songAdapter.getSelectedPosition () );
        currentIndex = index;
        songAdapter.setInitSelect ( true );
        songAdapter.setSelectedPosition ( currentIndex );
        songAdapter.notifyItemChanged ( currentIndex );

    }

    private void showPlaySong (Music music) {
        selectedSong = music; // put the selected song
        //show in play mode
        toolbar.setVisibility ( View.VISIBLE );
        tvTitle.setText ( music.getName () + "" );
        tvAlbum.setText ( music.getAlbum () + "" );
        tvArtist.setText ( music.getArtist () );
        path = music.getFilePath ();


    }


    @Override
    protected void onNewIntent (Intent intent) {
        super.onNewIntent ( intent );
        Bundle bundle = new Bundle ();
        if (intent != null)
            if (intent.getExtras () != null) {
                bundle = intent.getExtras ();
                String path = bundle.getString ( "path" );
                notiIsPlay = bundle.getBoolean ( MediaAppConstants.ISNOTIPLAY );
                if (path != null) {
                    isResume = true;
                    findMusicbyPath ( path );

                }

            }
    }

    private void findMusicbyPath (String path) {
        for (int i = 0; i < songList.size (); i++) {
            if (path.equalsIgnoreCase ( songList.get ( i ).getFilePath () )) {
                changeSelectedSong ( i );
                showPlaySong ( songList.get ( i ) );
                if (notiIsPlay) {
                    isPlaying = true;
                    iv_play.setImageResource ( R.drawable.ic_pause_circle_outline_black_24dp );
                } else {
                    isPlaying = false;
                    iv_play.setImageResource ( R.drawable.ic_play_circle_outline_white );
                }


            }
        }
    }


    private void catchEvents ( ) {
        iv_play.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (!isPlaying) {
                    if (!path.equalsIgnoreCase ( "" )) {
                        iv_play.setImageResource ( R.drawable.ic_pause_circle_outline_black_24dp );
                        isPlaying = true;
                        Utils.createNotification ( MainActivity.this, selectedSong, true );
                        if (!isResume) // if it is not resume
                        {
                            viewModel.onPlayMusic ( getApplicationContext (), tvTitle.getText ().toString (), tvArtist.getText ().toString (), path );
                            isResume = true;

                        } else {

                            viewModel.onResumeMusic ( getApplicationContext () );
                        }

                    }
                } else {


                    viewModel.onPauseMusic ( MainActivity.this );
                    resetPlayButton ();
                    Utils.createNotification ( MainActivity.this, selectedSong, false );


                }

            }
        } );

        iv_stop.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick (View v) {


                viewModel.onStopMusic ( MainActivity.this );
                Utils.createNotification ( MainActivity.this, selectedSong, false ); // show pause button
                resetPlayButton ();
                isResume = false;


            }
        } );
    }


    private void resetPlayButton ( ) {
        iv_play.setImageResource ( R.drawable.ic_play_circle_outline_white );
        isPlaying = false;
    }


    private void declare ( ) {
        viewModel = ViewModelProviders.of ( this ).get ( MainViewModel.class );
        songList = new ArrayList<> ();
        rcySongs = (RecyclerView) findViewById ( R.id.rcy_songs );
        iv_play = (ImageView) findViewById ( R.id.iv_play );
        iv_stop = (ImageView) findViewById ( R.id.iv_stop );
        tvTitle = (TextView) findViewById ( R.id.tb_title );
        tvAlbum = (TextView) findViewById ( R.id.tb_album );
        tvArtist = (TextView) findViewById ( R.id.tb_artist );
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById ( R.id.toolbar );
        toolbar.setVisibility ( View.INVISIBLE );
        rcySongs.setLayoutManager ( new LinearLayoutManager ( getApplicationContext () ) );
        selectedSong = new Music ();

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver () {
        @Override
        public void onReceive (Context context, Intent intent) {

            String action = intent.getExtras ().getString ( MediaAppConstants.NOTIACTIONNAME );
            switch (action) {
                case MediaAppConstants.ACTION_PLAY: {
                    onResumed ();
                }
                break;
                case MediaAppConstants.ACTION_PAUSE: {
                    onPaused ();
                }
                break;
                case MediaAppConstants.ACTION_STOP: {
                    onStopped ();
                }
                break;
            }

        }
    };


    private boolean checkPermissionREAD_EXTERNAL_STORAGE (Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission ( context,
                    Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat
                        .requestPermissions (
                                (Activity) context,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE );

                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult (int requestCode,
                                            String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                    viewModel.loadMusicLists ( "musicsdktest" );
                } else {

                }
                break;
            default:
                super.onRequestPermissionsResult ( requestCode, permissions,
                        grantResults );
        }
    }

    public void getMusicDatafromSDK ( ) {

        viewModel.getSongLists ().observe ( this, new Observer< ArrayList< Music > > () {
            @Override
            public void onChanged (ArrayList< Music > music) {

                songList = music;
                songAdapter.setSongLists ( songList );
                onNewIntent ( getIntent () );
            }
        } );
    }


    @Override
    protected void onDestroy ( ) {
        super.onDestroy ();

        unregisterReceiver ( broadcastReceiver );

    }

    @Override
    public void onResumed ( ) {


        isResume = true;
        viewModel.onResumeMusic ( MainActivity.this );
        Utils.createNotification ( MainActivity.this, selectedSong, true );
        resetPauseButton ();
    }

    @Override
    public void onPaused ( ) {


        viewModel.onPauseMusic ( MainActivity.this );
        Utils.createNotification ( MainActivity.this, selectedSong, false );
        resetPlayButton ();


    }

    @Override
    public void onStopped ( ) {


        viewModel.onStopMusic ( MainActivity.this );
        Utils.createNotification ( MainActivity.this, selectedSong, false );
        isResume = false;
        resetPlayButton ();
    }


    private void resetPauseButton ( ) {
        isPlaying = false;
        iv_play.setImageResource ( R.drawable.ic_pause_circle_outline_black_24dp );
    }


    @Override
    public void onBackPressed ( ) {
        // super.onBackPressed ();
    }

}

