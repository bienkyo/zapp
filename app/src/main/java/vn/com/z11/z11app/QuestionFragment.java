package vn.com.z11.z11app;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vn.com.z11.z11app.ApiResponseModel.ChapterResponse;
import vn.com.z11.z11app.RestAPI.onEventListenter;


public class QuestionFragment extends Fragment implements View.OnClickListener {
    LinearLayout layout_test,layout_train;
    onEventListenter eventNext;
    RelativeLayout loading;
    TextView txtv_questionContent, txtv_questionTranscript;
    ImageView img_question, img_audio, img_translate, img_check;
    ImageView img_previos,img_next;
    SeekBar seekBar;
    RadioGroup radioGroup;
    LinearLayout layout_radio;
    ChapterResponse.GroupQS groupquestion;
    String from;
    float dX;
    float dY;
    int lastAction;
    MediaPlayer mediaPlayer = new MediaPlayer();
    int mediaFileLengthInMilliseconds = 0;
    boolean played = false;
    HashMap<Integer, Integer> itemCorrect = new HashMap<>();
    HashMap<Integer, Integer> colorChange = new HashMap<>();
    HashMap<Integer,HashMap<Integer,Boolean>> saveRadio_Answer = new HashMap<>();
    ArrayList<HashMap<Integer,HashMap<Integer,Boolean>>> useraswer = new ArrayList<>();
    ArrayList<RadioGroup> listRDG = new ArrayList<>();
    ArrayList<TextView> listTextTitle = new ArrayList<>();
    int totol_wrong = 0;
    android.os.Handler handler = new android.os.Handler();
    Runnable notification;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            eventNext = (onEventListenter) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        groupquestion = (ChapterResponse.GroupQS) getArguments().getSerializable("question");
        from = getArguments().getString("from");

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_question, container, false);
        loading = (RelativeLayout) root.findViewById(R.id.rl_loading);
        txtv_questionContent = (TextView) root.findViewById(R.id.question_content);
        txtv_questionTranscript = (TextView) root.findViewById(R.id.question_transcrip);
        img_question = (ImageView) root.findViewById(R.id.img_question);
        seekBar = (SeekBar) root.findViewById(R.id.seekbar);
        img_audio = (ImageView) root.findViewById(R.id.img_play_stop);
        img_translate = (ImageView) root.findViewById(R.id.img_translate);
        img_check = (ImageView) root.findViewById(R.id.img_check);
        img_previos = (ImageView)root.findViewById(R.id.img_previos);
        img_next = (ImageView)root.findViewById(R.id.img_next);
        radioGroup = (RadioGroup) root.findViewById(R.id.group_answer);
        layout_radio = (LinearLayout) root.findViewById(R.id.layout_radio);
        layout_train = (LinearLayout)root.findViewById(R.id.layout_train);
        layout_test = (LinearLayout)root.findViewById(R.id.layout_test);
        if(from.equals("train")){
            layout_test.setVisibility(View.GONE);
            layout_train.setVisibility(View.VISIBLE);
            img_translate.setOnClickListener(this);
            img_check.setOnClickListener(this);
        }else if(from.equals("test")){
            layout_test.setVisibility(View.VISIBLE);
            layout_train.setVisibility(View.GONE);
            img_next.setOnClickListener(this);
            img_previos.setOnClickListener(this);
        }

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAnswer();

        txtv_questionContent.setText(groupquestion.group_question_content);
        if (groupquestion.group_question_transcript != null )
        txtv_questionTranscript.setText(groupquestion.group_question_transcript);
        else
        txtv_questionTranscript.setVisibility(View.GONE);
        if (groupquestion.group_question_image != null)
            Picasso.with(getContext()).load(groupquestion.group_question_image).placeholder(R.drawable.loading)
                    .error(R.drawable.erro).into(img_question);
        else {
            img_question.setVisibility(View.GONE);
        }
        if (groupquestion.group_question_audio != null )
            img_audio.setImageResource(R.drawable.play_icon);
        else{
            img_audio.setVisibility(View.GONE);
            seekBar.setVisibility(View.GONE);
        }
        img_audio.setOnClickListener(this);


        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                img_audio.setImageResource(R.drawable.play_icon);
                mediaPlayer.seekTo(0);
            }
        });
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_check:
                checkAnswer();
                break;
            case R.id.img_translate:
                Toast.makeText(getContext(), "click translate", Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_next:
                getAnswer();
                break;
            case R.id.img_previos:
                break;
            case R.id.img_play_stop:
                if (played == false) {

                    try {
                        mediaPlayer.setDataSource(groupquestion.group_question_audio);
                        mediaPlayer.prepare();
                        played = true;
                        mediaFileLengthInMilliseconds = mediaPlayer.getDuration();
                        mediaPlayer.start();
                        img_audio.setImageResource(R.drawable.pause_iocn);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (!mediaPlayer.isPlaying()) {
                        int curren = mediaPlayer.getCurrentPosition();
                        mediaPlayer.seekTo(curren);
                        mediaPlayer.start();
                        img_audio.setImageResource(R.drawable.pause_iocn);
                    } else {
                        mediaPlayer.pause();
                        img_audio.setImageResource(R.drawable.play_icon);
                    }
                }
                if (mediaPlayer != null)
                    primarySeekBarProgressUpdater(mediaPlayer, mediaFileLengthInMilliseconds);
                final int second = mediaFileLengthInMilliseconds;
                seekBar.setOnTouchListener(new View.OnTouchListener() {
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

                break;
            default:
                return;
        }
    }

    private void primarySeekBarProgressUpdater(final MediaPlayer mediaPlayer, final int mediaFileLengthInMilliseconds) {

        seekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100)); // This math construction give a percentage of "was playing"/"song length"
        if (mediaPlayer.isPlaying()) {
            notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater(mediaPlayer, mediaFileLengthInMilliseconds);
                }
            };
            handler.postDelayed(notification, 1000);
        }
    }

    public void getAnswer(){
        for (int i = 0; i < listRDG.size(); i++) {
            int id = listRDG.get(i).getId();
            //on check change color change

            Integer selectItem;
            try {
                selectItem = listRDG.get(i).getCheckedRadioButtonId();
            } catch (Exception e) {
                Toast.makeText(getContext(), "ban chua chon het cau tra loi", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectItem == -1) {
                Toast.makeText(getContext(), "ban chua chon het cau tra loi", Toast.LENGTH_SHORT).show();
                return;
            } else {
                HashMap<Integer,Boolean> a = saveRadio_Answer.get(selectItem);
                Map.Entry<Integer, Boolean> entry = a.entrySet().iterator().next();
                int id_answer = entry.getKey();

                for(int j = 0; j < groupquestion.questions.size();j++){
                    for(int k = 0; k < groupquestion.questions.get(j).answers.size();k++){
                        if( id_answer == groupquestion.questions.get(j).answers.get(k).answer_item_id){
                            HashMap<Integer,HashMap<Integer,Boolean>> data = new HashMap<>();
                            data.put(groupquestion.questions.get(j).question_id,a);
                            useraswer.add(data);
                        }
                    }

                }


            }
            if (!selectItem.equals(itemCorrect.get(listRDG.get(i).getId()))) {
                totol_wrong++;
            }
        }

        eventNext.eventNext(useraswer);
        eventNext.eventCheck(totol_wrong);
    }


    public void setAnswer() {
        int count_qs = groupquestion.questions.size();

        int numID = 100;
        int id_group = 1000;
        RadioGroup.LayoutParams rprms = new RadioGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < count_qs; i++) {
            listRDG.add(new RadioGroup(getContext()));
            listRDG.get(i).setId(id_group + i);
            layout_radio.addView(listRDG.get(i));
        }
        for (int i = 0; i < listRDG.size(); i++) {
            listTextTitle.add(new TextView(getContext()));
            listTextTitle.get(i).setPadding(10, 10, 10, 10);
            listTextTitle.get(i).setTextColor(Color.BLACK);
            listTextTitle.get(i).setText(groupquestion.questions.get(i).sub_question_content);
            listRDG.get(i).addView(listTextTitle.get(i), rprms);
            int count_answer = groupquestion.questions.get(i).answers.size();
            //save with question id how many answer item

            for (int j = 0; j < count_answer; j++) {
                RadioButton radioButton = new RadioButton(getContext());
                radioButton.setText(groupquestion.questions.get(i).answers.get(j).answer_item_value);
                radioButton.setId(numID + j);
                //key = radio_id ,value = answer_item
                HashMap<Integer,Boolean> answer = new HashMap<>();


                Boolean iscorrect = false;
                if (groupquestion.questions.get(i).answers.get(j).answer_is_correct == 1){
                    itemCorrect.put(listRDG.get(i).getId(), numID + j);
                    iscorrect = true;
                }

                answer.put(groupquestion.questions.get(i).answers.get(j).answer_item_id,iscorrect);
                saveRadio_Answer.put(numID+j,answer);

                listRDG.get(i).addView(radioButton, rprms);
            }

            numID += 100;
        }
        loading.setVisibility(View.GONE);
    }

    public void checkAnswer() {
        int k = itemCorrect.size();
        boolean correct_all = true;
        for (int i = 0; i < listRDG.size(); i++) {
            int id = listRDG.get(i).getId();
            //on check change color change
            listRDG.get(i).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (colorChange.get(radioGroup.getId()) != null) {
                        RadioButton radioButton = (RadioButton) getActivity().findViewById(colorChange.get(radioGroup.getId()));
                        radioButton.setTextColor(Color.BLACK);
                        colorChange.remove(colorChange.get(radioGroup.getId()));
                    }
                }
            });
            Integer selectItem;
            try {
                selectItem = listRDG.get(i).getCheckedRadioButtonId();
            } catch (Exception e) {
                Toast.makeText(getContext(), "ban chua chon het cau tra loi", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectItem == -1) {
                Toast.makeText(getContext(), "ban chua chon het cau tra loi", Toast.LENGTH_SHORT).show();
                return;
            } else if (!selectItem.equals(itemCorrect.get(listRDG.get(i).getId()))) {
                correct_all = false;
                totol_wrong++;
                RadioButton check = (RadioButton) getActivity().findViewById(selectItem);
                check.setTextColor(Color.RED);
            } else {
                RadioButton check = (RadioButton) getActivity().findViewById(selectItem);
                check.setTextColor(Color.GREEN);
            }
            colorChange.put(listRDG.get(i).getId(), selectItem);
        }
        if (correct_all) {
            eventNext.eventCheck(totol_wrong);
        } else {
            Toast.makeText(getContext(), "ban tra loi sai", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (notification != null)
            handler.removeCallbacks(notification);
        if (mediaPlayer != null && mediaPlayer.isPlaying())
            mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}
