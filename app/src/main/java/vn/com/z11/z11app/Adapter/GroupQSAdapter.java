package vn.com.z11.z11app.Adapter;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Handler;

import vn.com.z11.z11app.R;

/**
 * Created by kienlv58 on 12/27/16.
 */
public class GroupQSAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<String> listdata;
    MediaPlayer mediaPlayer = new MediaPlayer();
    int itemplay = 0;


    public GroupQSAdapter(Context context, ArrayList<String> listdata) {
        this.context = context;
        this.listdata = listdata;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final QuestionViewHolder myHolder = (QuestionViewHolder) holder;
        myHolder.questionContent.setText("day la cau hoi thu nhat");
        myHolder.questionTranscript.setText("18 thg 3, 2014 - Please check this steps. Step 1: Get audio duration with MediaPlayer.getDuration() .... setText(\"song.mp3\"); mediaPlayer = MediaPlayer.create(this, R.raw.song); seekbar.setClickable(false); pauseButton.setEnabled(false); ..");
        Picasso.with(context).load("http://www.wn.com.vn/timthumb.php?src=http://www.wn.com.vn/product_images/p/604/anh-dep-girl-xinh(1)__64832.jpg&w=1000&h=606&zc=1").into(myHolder.questionImage);
        myHolder.play_stop_iamge.setImageResource(R.drawable.play_icon);
        myHolder.audioProgess.setMax(99);
        myHolder.play_stop_iamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!mediaPlayer.isPlaying()) {

                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    int mediaFileLengthInMilliseconds = 0;
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            myHolder.play_stop_iamge.setImageResource(R.drawable.play_icon);
                            mediaPlayer.reset();
                            itemplay = 0;
                        }
                    });

                    try {
                        mediaPlayer.setDataSource("https://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3");
                        mediaPlayer.prepare();
                        mediaFileLengthInMilliseconds = mediaPlayer.getDuration();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mediaPlayer.start();
                    myHolder.play_stop_iamge.setImageResource(R.drawable.pause_iocn);

                    myHolder.primarySeekBarProgressUpdater(mediaPlayer, mediaFileLengthInMilliseconds);
                    final int second = mediaFileLengthInMilliseconds;
                    myHolder.audioProgess.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            if (mediaPlayer.isPlaying()) {
                                SeekBar seekBar = (SeekBar) view;
                                int playPositionInMillisecconds = (second / 100) * seekBar.getProgress();
                                mediaPlayer.seekTo(playPositionInMillisecconds);
                            }
                            return false;
                        }
                    });

                } else {
                    mediaPlayer.pause();
                    myHolder.play_stop_iamge.setImageResource(R.drawable.play_icon);
                    mediaPlayer.reset();
                }

            }
        });


    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView questionContent;
        TextView questionTranscript;
        ImageView questionImage;
        SeekBar audioProgess;
        ImageView play_stop_iamge;


        public QuestionViewHolder(View itemView) {
            super(itemView);
            questionContent = (TextView) itemView.findViewById(R.id.question_content);
            questionTranscript = (TextView) itemView.findViewById(R.id.question_transcrip);
            questionImage = (ImageView) itemView.findViewById(R.id.img_question);
            audioProgess = (SeekBar) itemView.findViewById(R.id.seekbar);
            play_stop_iamge = (ImageView) itemView.findViewById(R.id.img_play_stop);

        }

        private void primarySeekBarProgressUpdater(final MediaPlayer mediaPlayer, final int mediaFileLengthInMilliseconds) {
            android.os.Handler handler = new android.os.Handler();
            audioProgess.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100)); // This math construction give a percentage of "was playing"/"song length"
            if (mediaPlayer.isPlaying()) {
                Runnable notification = new Runnable() {
                    public void run() {
                        primarySeekBarProgressUpdater(mediaPlayer, mediaFileLengthInMilliseconds);
                    }
                };
                handler.postDelayed(notification, 1000);
            }
        }

    }
}
