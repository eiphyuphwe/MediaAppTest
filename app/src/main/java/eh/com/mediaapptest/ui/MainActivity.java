package eh.com.mediaapptest.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import eh.com.mediaapptest.R;
import eh.com.mediaapptest.ui.adapters.SongAdapter;
import eh.com.musicsdk.data.Music;
import eh.com.musicsdk.providers.MusicProviders;

public class MainActivity extends AppCompatActivity implements Playable{

    private int currentIndex;
    boolean isPlaying = false;
    private RecyclerView rcySongs;
    private SongAdapter songAdapter;
    private ImageView iv_play,iv_stop;
    private TextView tvTitle,tvArtist,tvAlbum;
    private ArrayList<Music> songList;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        declare ();


        //getMusicDatafromSDK ( getApplicationContext (),"musicsdktest" );
        if(checkPermissionREAD_EXTERNAL_STORAGE ( MainActivity.this))
        {
            getMusicDatafromSDK ( getApplicationContext (),"musicsdktest" );
        }

        songAdapter = new SongAdapter ( getApplicationContext (), songList, new SongAdapter.RecyclerItemClickListener () {
            @Override
            public void onClickListener (Music song, int position) {

                changeSelectedSong ( position );
                showPlaySong ( song );

            }
        } );

        rcySongs.setAdapter (songAdapter);


    }

    private void changeSelectedSong(int index){
        songAdapter.notifyItemChanged(songAdapter.getSelectedPosition());
        currentIndex = index;
        songAdapter.setSelectedPosition(currentIndex);
        songAdapter.notifyItemChanged(currentIndex);

    }

    private void showPlaySong(Music music)
    {
        tvTitle.setText ( music.getName ()+"" );
        tvAlbum.setText ( music.getAlbum ()+"" );
        tvArtist.setText ( music.getArtist () );

    }


    private void catchEvents()
    {
        iv_play.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick (View v) {

            }
        } );
    }






    private void declare()
    {
        songList = new ArrayList<> (  );
        rcySongs = (RecyclerView)findViewById ( R.id.rcy_songs );
        iv_play = (ImageView)findViewById ( R.id.iv_play );
        iv_stop = (ImageView)findViewById ( R.id.iv_stop );
        tvTitle = (TextView)findViewById ( R.id.tb_title );
        tvAlbum = (TextView) findViewById ( R.id.tb_album );
        tvArtist = (TextView) findViewById ( R.id.tb_artist );
        rcySongs.setLayoutManager(new LinearLayoutManager (getApplicationContext()));

    }




    private boolean checkPermissionREAD_EXTERNAL_STORAGE(Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                    getMusicDatafromSDK ( getApplicationContext (),"musicsdktest" );
                } else {

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

    public void getMusicDatafromSDK(Context context,String filePath)
    {
       songList = MusicProviders.provideMusics ( context,filePath);

    }



    @Override
    public void onMediaPlay ( ) {

    }

    @Override
    public void onMediaPause ( ) {

    }

    @Override
    public void onMediaStop ( ) {

    }
}
