package vn.com.z11.z11app;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.com.z11.z11app.Adapter.RestAdapter;
import vn.com.z11.z11app.Adapter.pagerAdapter;
import vn.com.z11.z11app.ApiResponseModel.LoginResponse;
import vn.com.z11.z11app.Database.Query.LocalDb;
import vn.com.z11.z11app.Utilities.AppConstants;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    //http://www.devexchanges.info/2016/05/android-using-searchview-in.html
    LoginResponse.Metadata.User user;
    CircleImageView imageView;
    TextView txtv_name;
    TextView txtv_email;
    ImageView img_login_out;
    MenuItem item1;
    SearchView searchView;

    TabLayout tabLayout;
    ViewPager viewPager;
    pagerAdapter viewpagerAdapter;

    //search
    MenuItem searchItem;
    boolean isSearchOpend = false;
    EditText edt_search;
    public static LocalDb localDb;
    public static Bundle myBundle = new Bundle();
    private AccountManager mAccountManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mAccountManager = AccountManager.get(this);
        getUser();
        initView();
        setupTablayout();
        Log.d("token", RestAdapter.MyauthHeaderContent);

    }

    public void initView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);
        imageView = (CircleImageView) headerview.findViewById(R.id.imageView);
        txtv_email = (TextView) headerview.findViewById(R.id.txtv_email);
        txtv_name = (TextView) headerview.findViewById(R.id.txtv_name);
        img_login_out = (ImageView) headerview.findViewById(R.id.img_login_out);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        imageView.setOnClickListener(this);
        txtv_email.setOnClickListener(this);
        txtv_name.setOnClickListener(this);
        img_login_out.setOnClickListener(this);

        item1 = navigationView.getMenu().getItem(0);
        MenuItem item2 = navigationView.getMenu().getItem(1);
        MenuItem item3 = navigationView.getMenu().getItem(2);
        MenuItem item4 = navigationView.getMenu().getItem(3);
        getUser();
        checkLogin();


    }

    public void getUser() {
        localDb = new LocalDb(this);
        user = localDb.
                getUser();
        if (user != null)
            RestAdapter.MyauthHeaderContent = localDb.getToken();


    }


    public void checkLogin() {
        if (user == null) {
            Picasso.with(this).load(R.drawable.erro).into(imageView);
            txtv_name.setText("Login");
            txtv_email.setText("");
            img_login_out.setImageResource(R.drawable.login_draw);
            item1.setIcon(R.drawable.login_draw);
            item1.setTitle("Login");
        } else {
            if(user.getProfile().getImage() != null)
            Picasso.with(MainActivity.this)
                    .load(user.getProfile().getImage().toString())
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.erro)
                    .into(imageView);
            txtv_name.setText(user.getProfile().getName().toString());
            txtv_email.setText(user.getEmail().toString());
            img_login_out.setImageResource(R.drawable.logout_variant);
            item1.setIcon(R.drawable.profile);
            item1.setTitle("Profile");
        }
    }

    public void setupTablayout() {
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewpagerAdapter = new pagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(viewpagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
//        MenuItem itemSearch = menu.findItem(R.id.search_view);
//        searchView = (SearchView) itemSearch.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Toast.makeText(MainActivity.this,query,Toast.LENGTH_SHORT).show();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SettingActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_search) {
           Intent search = new Intent(MainActivity.this,PackageActivity.class);
            search.putExtra("search",1);
            startActivity(search);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            if (user != null) {
                Intent intent1 = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent1);
            }else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, 100);
            }
            // Handle the camera action
        } else if (id == R.id.nav_lession) {

        } else if (id == R.id.nav_folder) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView:
            case R.id.txtv_name:
            case R.id.txtv_email:
                if (user != null) {
                    Intent intent1 = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent1);
                } else {
                    Intent intent1 = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivityForResult(intent1,100);
                }
                break;
            case R.id.img_login_out:
                if (user == null) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivityForResult(intent, 100);
                } else {
                    user = null;
                    localDb.clear();
                    checkLogin();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == AppConstants.LOGIN_KEY) {
            getUser();
            checkLogin();
            LoginResponse dataUser = (LoginResponse) data.getSerializableExtra("loginResponse");
            if (dataUser.getMetadata().getUser().getProfile().getImage() != null)
                Picasso.with(MainActivity.this)
                        .load(dataUser.getMetadata().getUser().getProfile().getImage().toString())
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.erro)
                        .into(imageView);
            txtv_name.setText(dataUser.getMetadata().getUser().getProfile().getName().toString());
            txtv_email.setText(dataUser.getMetadata().getUser().getEmail().toString());
        }
    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        searchItem = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RestAdapter.MyauthHeaderContent = null;
    }
}

