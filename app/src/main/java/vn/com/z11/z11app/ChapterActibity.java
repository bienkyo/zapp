package vn.com.z11.z11app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.com.z11.z11app.Adapter.ChapterAdapter;
import vn.com.z11.z11app.Adapter.RestAdapter;
import vn.com.z11.z11app.ApiResponseModel.ErroResponse;
import vn.com.z11.z11app.ApiResponseModel.PackageResponse;
import vn.com.z11.z11app.RestAPI.ApiPackage;
import vn.com.z11.z11app.RestAPI.ErrorUtils;

public class ChapterActibity extends AppCompatActivity {
    RecyclerView recyclerView_chapter;
    TextView txtv_noti;
    ChapterAdapter adapter;
    int package_id;
    ArrayList<PackageResponse.Chapters> list_chapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_actibity);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.WhiteColor), PorterDuff.Mode.SRC_ATOP);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Chapter");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);


        txtv_noti = (TextView)findViewById(R.id.txtv_noti);
        recyclerView_chapter = (RecyclerView)findViewById(R.id.recycle_chapter);
        txtv_noti.setVisibility(View.INVISIBLE);
        txtv_noti = (TextView)findViewById(R.id.txtv_noti);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_chapter.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView_chapter.setItemAnimator(itemAnimator);
        recyclerView_chapter.setHasFixedSize(true);
        txtv_noti.setText("khong co du lieu");

        Intent intent = getIntent();
        package_id = intent.getIntExtra("package_id",0);

        if (package_id == 0){
            txtv_noti.setVisibility(View.VISIBLE);

        }else {
            getOnePackage(package_id);
        }


    }

    public void getOnePackage(int package_id){
        ApiPackage apiPackage = RestAdapter.getClient().create(ApiPackage.class);
        Call<PackageResponse> packageResponseCall = apiPackage.getOnepackage(package_id);
        packageResponseCall.enqueue(new Callback<PackageResponse>() {
            @Override
            public void onResponse(Call<PackageResponse> call, Response<PackageResponse> response) {
                int code = response.code();
                if(code == 200){
                    PackageResponse packageResponse = response.body();
                    list_chapter = (ArrayList<PackageResponse.Chapters>) packageResponse.mPackage.chapters;
                    if(list_chapter.size() == 0){
                        txtv_noti.setVisibility(View.VISIBLE);
                    }
                    adapter = new ChapterAdapter(list_chapter,ChapterActibity.this);
                    recyclerView_chapter.setAdapter(adapter);
                }else if(code == 400 || code == 404 || code == 300){
                    ErroResponse erroResponse = ErrorUtils.parseError(response);
                    if(erroResponse.getNewToken() != null){
                        String token = "Bearer {"+erroResponse.getNewToken().toString()+"}";
                        MainActivity.localDb.updateToken(token);
                        getOnePackage(ChapterActibity.this.package_id);
                    }
                    Toast.makeText(ChapterActibity.this,erroResponse.getStatus(),Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<PackageResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in_left, R.anim.fade_out_left);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
