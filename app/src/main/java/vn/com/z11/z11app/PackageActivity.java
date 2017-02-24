package vn.com.z11.z11app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import vn.com.z11.z11app.Adapter.PackageAdapter;
import vn.com.z11.z11app.ApiResponseModel.CategoryResponse;
import vn.com.z11.z11app.ApiResponseModel.PackageResponse;

public class PackageActivity extends AppCompatActivity {
    RecyclerView recyclerView_package;
    TextView txtv_noti;
    PackageAdapter packageAdapter;
    ArrayList<CategoryResponse.Packages> listPackage = new ArrayList<>();
    int search;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.WhiteColor), PorterDuff.Mode.SRC_ATOP);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        Intent intent = getIntent();
        search = intent.getIntExtra("search", 0);
        if (search == 1)
            toolbar.setTitle("");
//
        else
            toolbar.setTitle("Package");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        listPackage = (ArrayList<CategoryResponse.Packages>) intent.getSerializableExtra("listPackage");


        recyclerView_package = (RecyclerView) findViewById(R.id.recycle_package);
        txtv_noti = (TextView) findViewById(R.id.txtv_noti);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_package.setLayoutManager(linearLayoutManager);
        txtv_noti.setText("khong co du lieu");
        if (listPackage != null) {
            for (int i = 0; i < listPackage.size(); i++) {
                if (listPackage.get(i).approval != 1) {
                    listPackage.remove(i);
                    i--;
                }
            }


            if (listPackage.size() == 0)
                txtv_noti.setVisibility(View.VISIBLE);
            else
                txtv_noti.setVisibility(View.INVISIBLE);

            packageAdapter = new PackageAdapter(listPackage, null, PackageActivity.this);
            recyclerView_package.setAdapter(packageAdapter);
            RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
            itemAnimator.setAddDuration(1000);
            itemAnimator.setRemoveDuration(1000);
            recyclerView_package.setItemAnimator(itemAnimator);
            recyclerView_package.setHasFixedSize(true);
        } else {
            txtv_noti.setVisibility(View.VISIBLE);
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (search == 1) {
            getMenuInflater().inflate(R.menu.search_view, menu);
            MenuItem itemSearch = menu.findItem(R.id.search_view);
            searchView = (SearchView) itemSearch.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Toast.makeText(PackageActivity.this, query, Toast.LENGTH_SHORT).show();
                    searchView.clearFocus();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
            searchView.setFocusable(true);
            itemSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem menuItem) {
                    return false;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                    menuItem.getActionView().requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
            });
            return true;
        } else
            return super.onCreateOptionsMenu(menu);


    }
}
