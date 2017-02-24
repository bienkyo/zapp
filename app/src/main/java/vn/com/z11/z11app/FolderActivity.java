package vn.com.z11.z11app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.z11.z11app.Adapter.GridViewFolderAdapter;
import vn.com.z11.z11app.ApiResponseModel.CategoryResponse;

public class FolderActivity extends AppCompatActivity {
    GridView gridView_folder;
    GridViewFolderAdapter gridViewFolderAdapter;
    TextView txtv_noti;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.WhiteColor), PorterDuff.Mode.SRC_ATOP);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Folder");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        Intent intent = getIntent();
        ArrayList<CategoryResponse.Folder> listfolder = (ArrayList<CategoryResponse.Folder>) intent.getSerializableExtra("listFolder");
        txtv_noti = (TextView)findViewById(R.id.txtv_noti);
        txtv_noti.setText("khong co du lieu");
        if(listfolder.size() == 0)
        txtv_noti.setVisibility(View.VISIBLE);
        else
        txtv_noti.setVisibility(View.INVISIBLE);
        gridView_folder = (GridView)findViewById(R.id.gridview_folder);
        gridViewFolderAdapter = new GridViewFolderAdapter(listfolder,FolderActivity.this);
        gridView_folder.setAdapter(gridViewFolderAdapter);

        gridView_folder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               CategoryResponse.Folder itemFolder = (CategoryResponse.Folder) adapterView.getItemAtPosition(i);
                ArrayList<CategoryResponse.Packages> lisPackage = (ArrayList<CategoryResponse.Packages>) itemFolder.packages;
                Intent intent1 = new Intent(FolderActivity.this,PackageActivity.class);
                intent1.putExtra("listPackage",lisPackage);
                startActivity(intent1);
                overridePendingTransition(R.anim.fade_in_right, R.anim.fade_out_right);
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