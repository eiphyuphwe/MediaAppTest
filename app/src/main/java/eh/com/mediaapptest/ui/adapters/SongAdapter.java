package eh.com.mediaapptest.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import eh.com.mediaapptest.R;
import eh.com.musicsdk.data.Music;


public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private Context context;
    private ArrayList< Music > songList;
    private RecyclerItemClickListener listener;
    private int selectedPosition;

    public SongAdapter (Context context, ArrayList<Music> songList, RecyclerItemClickListener listener){

        this.context = context;
        this.songList = songList;
        this.listener = listener;

    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_row, parent, false);

        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {

        Music song = songList.get(position);
        if(song != null){

            if(selectedPosition == position){
                holder.itemView.setBackgroundColor( ContextCompat.getColor(context, R.color.colorPrimary));
                holder.iv_play_active.setVisibility( View.VISIBLE);
            }else{
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                holder.iv_play_active.setVisibility( View.INVISIBLE);
            }

            holder.tv_title.setText(song.getName ());
            holder.tv_artist.setText(song.getArtist());


            holder.bind(song, listener);

        }

    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_title, tv_artist, tv_duration;
        private ImageView  iv_play_active,iv_stop;

        public SongViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_artist = (TextView) itemView.findViewById(R.id.tv_artist);
            iv_play_active = (ImageView) itemView.findViewById(R.id.iv_play_active);
            iv_stop = (ImageView) itemView.findViewById ( R.id.iv_stop );

        }

        public void bind(final Music song, final RecyclerItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickListener(song, getLayoutPosition());
                }
            });
        }

    }

    public interface RecyclerItemClickListener{
        void onClickListener (Music song, int position);
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
}
