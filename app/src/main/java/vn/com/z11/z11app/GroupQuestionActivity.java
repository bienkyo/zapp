package vn.com.z11.z11app;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.com.z11.z11app.Adapter.RestAdapter;
import vn.com.z11.z11app.ApiResponseModel.ChapterResponse;
import vn.com.z11.z11app.ApiResponseModel.Code_StatusModel;
import vn.com.z11.z11app.ApiResponseModel.ErroResponse;
import vn.com.z11.z11app.RestAPI.ApiChapter;
import vn.com.z11.z11app.RestAPI.ApiUserAnswer;
import vn.com.z11.z11app.RestAPI.ErrorUtils;
import vn.com.z11.z11app.RestAPI.onEventListenter;

public class GroupQuestionActivity extends AppCompatActivity implements onEventListenter {
    ProgressBar progressBar;
    TextView txtv_progess;
    FrameLayout fmContent;
    RelativeLayout loading;
    int chapter_id;
    int qs_pos = 0;
    int total_group_qs = 0;
    int total_wrong = 0;
    int a = 0;
    String from;
    String result = "";
    ArrayList<ChapterResponse.GroupQS> listGroupQS;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_question);

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.WhiteColor), PorterDuff.Mode.SRC_ATOP);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Question");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        initView();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void initView() {
        loading = (RelativeLayout) findViewById(R.id.rl_loading);
        loading.setVisibility(View.VISIBLE);
        Intent groupdata = getIntent();
        from = groupdata.getStringExtra("from");
        chapter_id = groupdata.getIntExtra("chapter_id", 0);
        progressBar = (ProgressBar) findViewById(R.id.progessbar);
        txtv_progess = (TextView) findViewById(R.id.txtv_progess);
        txtv_progess.setText("0%");
        progressBar.setProgress(0);
        progressBar.setMax(100);
        fmContent = (FrameLayout) findViewById(R.id.fmcontent);
        if (chapter_id == 0) {
            //no data
            Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show();
        } else {
            //get api
            getQS();

        }

    }

    public void callFragment(Fragment fragment, ChapterResponse.GroupQS groupQS) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable("question", groupQS);
        bundle.putString("from", from);
        fragment.setArguments(bundle);
        transaction.replace(R.id.fmcontent, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (from.equals("train")) {
            if(listGroupQS == null || listGroupQS.size() == 0){
                super .onBackPressed();
                return;
            }

            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.dialog_layout, null);
            ProgressBar complete = (ProgressBar) v.findViewById(R.id.progessbar);
            TextView txtv_complete = (TextView) v.findViewById(R.id.txtv_progess);
            TextView txtv_total_qs = (TextView) v.findViewById(R.id.txtv_total_qs);
            TextView txtv_total_wrong = (TextView) v.findViewById(R.id.txtv_total_wrong);
            TextView txtv_review = (TextView) v.findViewById(R.id.txtv_review);
            complete.setMax(100);
            complete.setProgress(a);
            txtv_complete.setText(a + "%");
            txtv_total_qs.setText("Tong so cau hoi :" + total_group_qs);
            txtv_total_wrong.setText("So lan tra loi sai: " + total_wrong);

            txtv_review.setText("Danh gia :" + "Chua hoan thanh cac cau hoi");


            AlertDialog.Builder customDialog = new AlertDialog.Builder(this);
            customDialog.setView(getLayoutInflater().inflate(R.layout.dialog_layout, null));
            customDialog.setPositiveButton("Tiep tuc", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            customDialog.setNegativeButton("Thoat", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    finish();
                    overridePendingTransition(R.anim.fade_in_left, R.anim.fade_out_left);
                }
            });
            customDialog.setCancelable(false);
            customDialog.setView(v);
            AlertDialog alertDialog = customDialog.create();
            alertDialog.show();
            overridePendingTransition(R.anim.fade_in_left, R.anim.fade_out_left);
        } else {
            if(listGroupQS == null || listGroupQS.size() == 0){
                super .onBackPressed();
                return;
            }

            AlertDialog.Builder exit = new AlertDialog.Builder(GroupQuestionActivity.this);
            exit.setMessage("Bạn chưa hoàn thành bài test. Bài sẽ không được lưu khi bạn thoát ?");
            exit.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            exit.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            AlertDialog dialog = exit.create();
            dialog.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getQS() {
        ApiChapter apiChapter = RestAdapter.getClient().create(ApiChapter.class);
        Call<ChapterResponse> chapterResponseCall = apiChapter.getChapter(chapter_id);
        chapterResponseCall.enqueue(new Callback<ChapterResponse>() {
            @Override
            public void onResponse(Call<ChapterResponse> call, Response<ChapterResponse> response) {
                int code = response.code();
                loading.setVisibility(View.INVISIBLE);
                if (code == 200) {
                    ChapterResponse chapter = response.body();
                    listGroupQS = (ArrayList<ChapterResponse.GroupQS>) chapter.chapter.groupQS;
                    if (listGroupQS != null) {
                        if (listGroupQS.size() > 0) {
                            total_group_qs = listGroupQS.size();
                            callFragment(new QuestionFragment(), listGroupQS.get(qs_pos));
                        }
                    } else {
                        Toast.makeText(GroupQuestionActivity.this, "khong co du lieu", Toast.LENGTH_SHORT).show();
                    }


                } else if (code == 400 || code == 404) {
                    ErroResponse erroResponse = ErrorUtils.parseError(response);
                    if (erroResponse.getNewToken() != null) {
                        String token = "Bearer {" + erroResponse.getNewToken().toString() + "}";
                        MainActivity.localDb.updateToken(token);
                        RestAdapter.MyauthHeaderContent = token;
                        getQS();
                    }

                    Toast.makeText(GroupQuestionActivity.this, erroResponse.getStatus(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChapterResponse> call, Throwable t) {
                Toast.makeText(GroupQuestionActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void eventCheck(int next) {
        total_wrong += next;
        qs_pos++;
        a = (qs_pos * 100) / total_group_qs;
        if (qs_pos < listGroupQS.size()) {
            progressBar.setProgress(a);
            txtv_progess.setText(a + "%");
            callFragment(new QuestionFragment(), listGroupQS.get(qs_pos));
        } else {

            if (from == "train") {
                Toast.makeText(GroupQuestionActivity.this, "ban da hoan thanh tat ca cac cau hoi", Toast.LENGTH_SHORT).show();

                showdialogFinish();


            } else {
                //from == test

                AlertDialog.Builder exit = new AlertDialog.Builder(GroupQuestionActivity.this);
                exit.setMessage("Bạn đã hoàn thành bài test. Submit?");
                exit.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        result = result.substring(0, result.length() - 2);
                        result = result + "}";
                        result = "{" + result + "}";
                        ApiUserAnswer apiUserAnswer = RestAdapter.getClient().create(ApiUserAnswer.class);
                        Call<Code_StatusModel> response = apiUserAnswer.addUserAnswer(result);
                        response.enqueue(new Callback<Code_StatusModel>() {
                            @Override
                            public void onResponse(Call<Code_StatusModel> call, Response<Code_StatusModel> response) {

                                int code = response.code();
                                if (code == 200) {
                                    Toast.makeText(GroupQuestionActivity.this, "submit question success", Toast.LENGTH_SHORT).show();
                                    showdialogFinish();

                                } else if (code == 400) {
                                    ErroResponse erroResponse = ErrorUtils.parseError(response);
                                    if (erroResponse.getNewToken() != null) {
                                        String token = "Bearer {" + erroResponse.getNewToken().toString() + "}";
                                        MainActivity.localDb.updateToken(token);
                                        RestAdapter.MyauthHeaderContent = token;


                                    } else {
                                        Toast.makeText(GroupQuestionActivity.this, erroResponse.getStatus() + "", Toast.LENGTH_SHORT).show();
                                    }


                                } else if (code == 401) {
                                    MainActivity.localDb.deleteUser();
                                    Intent intent = new Intent(GroupQuestionActivity.this, LoginActivity.class);
                                    intent.putExtra("from", "other");
                                    startActivity(intent);
                                    Toast.makeText(GroupQuestionActivity.this, "het phien lam viec", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<Code_StatusModel> call, Throwable t) {
                                Toast.makeText(GroupQuestionActivity.this, t + "", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                exit.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = exit.create();
                dialog.show();

            }
        }

    }

    @Override
    public void eventNext(ArrayList<HashMap<Integer, HashMap<Integer, Boolean>>> user_answer) {
        for (int i = 0; i < user_answer.size(); i++) {
            HashMap<Integer, HashMap<Integer, Boolean>> item = user_answer.get(i);
            Map.Entry<Integer, HashMap<Integer, Boolean>> entry1 = item.entrySet().iterator().next();
            int id_qs = entry1.getKey();
            HashMap<Integer, Boolean> answer = entry1.getValue();
            Map.Entry<Integer, Boolean> entry2 = answer.entrySet().iterator().next();
            int item_answer = entry2.getKey();
            Boolean answer_iscrrect = entry2.getValue();

            String value = "\"" + id_qs + "\"" + ":{\"answer_result\":" + item_answer + ",\"status\":" + answer_iscrrect + "},";
            result += value;
            //Toast.makeText(this, "qs:" + id_qs + "-----" + "item_answer" + item_answer + "-----correct" + answer_iscrrect, Toast.LENGTH_SHORT).show();
        }


    }

    public void showdialogFinish() {
        progressBar.setProgress(a);
        txtv_progess.setText(a + "%");
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_layout, null);
        ProgressBar complete = (ProgressBar) v.findViewById(R.id.progessbar);
        TextView txtv_complete = (TextView) v.findViewById(R.id.txtv_progess);
        TextView txtv_total_qs = (TextView) v.findViewById(R.id.txtv_total_qs);
        TextView txtv_total_wrong = (TextView) v.findViewById(R.id.txtv_total_wrong);
        TextView txtv_review = (TextView) v.findViewById(R.id.txtv_review);
        complete.setMax(100);
        complete.setProgress(a);
        txtv_complete.setText(a + "%");
        txtv_total_qs.setText("Tong so cau hoi :" + total_group_qs);
        txtv_total_wrong.setText("So lan tra loi sai: " + total_wrong);
        if (total_wrong == 0)
            txtv_review.setText("Danh gia :" + "Rat Tot");
        else if (total_wrong <= total_group_qs)
            txtv_review.setText("Danh gia :" + "Tot");
        else if (total_wrong > total_group_qs) {
            txtv_review.setText("Danh gia :" + "Kem");
        }

        AlertDialog.Builder customDialog = new AlertDialog.Builder(this);
        customDialog.setView(getLayoutInflater().inflate(R.layout.dialog_layout, null));
        customDialog.setPositiveButton("Chapter tiep", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        customDialog.setNegativeButton("Tra loi lai", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
            }
        });
        customDialog.setCancelable(false);
        customDialog.setView(v);
        AlertDialog alertDialog = customDialog.create();
        alertDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "GroupQuestion Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://vn.com.z11.z11app/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "GroupQuestion Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://vn.com.z11.z11app/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
