package vn.com.z11.z11app.Tabfragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.com.z11.z11app.Adapter.GridViewFolderAdapter;
import vn.com.z11.z11app.Adapter.GridviewAdapter;
import vn.com.z11.z11app.Adapter.RestAdapter;
import vn.com.z11.z11app.ApiResponseModel.CategoryResponse;
import vn.com.z11.z11app.ApiResponseModel.ErroResponse;
import vn.com.z11.z11app.ApiResponseModel.FolderResponse;
import vn.com.z11.z11app.Database.Query.LocalDb;
import vn.com.z11.z11app.MainActivity;
import vn.com.z11.z11app.PackageActivity;
import vn.com.z11.z11app.R;
import vn.com.z11.z11app.RestAPI.APIFolder;
import vn.com.z11.z11app.RestAPI.ErrorUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab3Fragment extends Fragment {
    ProgressBar loading;
    GridView gridView;
    TextView txtv_noti;
    GridviewAdapter gridviewAdapter;
    CategoryResponse categoryResponse;
    FloatingActionButton btn_add;
    PtrFrameLayout ptrFrameLayout;
    ArrayList<CategoryResponse.Folder> list_MyFolder;
    GridViewFolderAdapter gridViewFolderAdapter;
    LocalDb localDb = new LocalDb(getContext());
    ArrayList<CategoryResponse.Category> listcate;
    ArrayList<String> cate_code = new ArrayList<>();
    float dX, dY, lastAction;

    public Tab3Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_tab3, container, false);



            listcate = (ArrayList<CategoryResponse.Category>) MainActivity.myBundle.getSerializable("listcate");
        if (listcate != null && listcate.size() > 0) {
            for(int i = 0; i <listcate.size();i++){
                cate_code.add(listcate.get(i).category_code);
            }
        }

        gridView = (GridView) root.findViewById(R.id.gridview_folder);
        txtv_noti = (TextView) root.findViewById(R.id.txtv_noti);
        btn_add = (FloatingActionButton) root.findViewById(R.id.btn_floating);
        txtv_noti.setVisibility(View.GONE);
        loading = (ProgressBar) root.findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
        if(listcate != null)
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View v = inflater.inflate(R.layout.add_layout, null);
                Spinner spin_cate = (Spinner)v.findViewById(R.id.list_cate);
                Spinner spin_language1 = (Spinner)v.findViewById(R.id.list_language);
                Spinner spin_language2 = (Spinner)v.findViewById(R.id.list_language2);
                final EditText edt_name = (EditText) v.findViewById(R.id.edt_name);
                final EditText edt_description = (EditText) v.findViewById(R.id.edt_description);
                final EditText edt_name2 = (EditText) v.findViewById(R.id.edt_name2);
                final EditText edt_description2 = (EditText) v.findViewById(R.id.edt_description2);

                ArrayAdapter<String> spiner1ADT = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,cate_code);
                spiner1ADT.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                spin_cate.setAdapter(spiner1ADT);


                Button btn_add = (Button)v.findViewById(R.id.btn_add);
                btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name1 = edt_name.getText().toString().trim();
                        String desc1 = edt_description.getText().toString().trim();
                        String name2 = edt_name2.getText().toString().trim();
                        String desc2 = edt_description2.getText().toString().trim();
                        if(name1 == null || desc1 == null){
                            Toast.makeText(getContext(),"input must not null",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(v);
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
//        btn_add.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                switch (event.getActionMasked()) {
//                    case MotionEvent.ACTION_DOWN:
//                        dX = view.getX() - event.getRawX();
//                        dY = view.getY() - event.getRawY();
//                        lastAction = MotionEvent.ACTION_DOWN;
//                        break;
//
//                    case MotionEvent.ACTION_MOVE:
////
//                        view.setY(event.getRawY() + dY);
//                        view.setX(event.getRawX() + dX);
//                        lastAction = MotionEvent.ACTION_MOVE;
//                        break;
//
//                    case MotionEvent.ACTION_UP:
//                        if (lastAction == MotionEvent.ACTION_DOWN)
//                            Toast.makeText(getContext(), "Clicked!", Toast.LENGTH_SHORT).show();
//                        break;
//
//                    default:
//                        return false;
//                }
//                return true;
//
//            }
//        });

        ptrFrameLayout = (PtrFrameLayout) root.findViewById(R.id.store_house_ptr_frame);
        ptrFrameLayout.setResistance(1.7f);
        ptrFrameLayout.setRatioOfHeaderHeightToRefresh(1.2f);
        ptrFrameLayout.setDurationToClose(200);
        ptrFrameLayout.setDurationToCloseHeader(1000);
//// default is false
//        ptrFrameLayout.setPullToRefresh(true);
//// default is true
//        ptrFrameLayout.setKeepHeaderWhenRefresh(true);
        /**
         * CUSTOM VIEW REFRESH
         */
        final MaterialHeader header = new MaterialHeader(getActivity().getApplicationContext());
        int[] colors = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);

        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        // header.setPadding(0,  LocalDisplay.dp2px(15), 0, LocalDisplay.dp2px(10));


        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
//        ptrFrameLayout.setFooterView(header);
        ptrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrFrameLayout.refreshComplete();
            }
        }, 2000);


        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrameLayout.refreshComplete();
                    }
                }, 3000);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                // return super.checkCanDoRefresh(frame, content, header);
            }
        });


        getFolder();

        return root;

    }

    public void getFolder() {
        APIFolder apiFolder = RestAdapter.getClient().create(APIFolder.class);
        Call<FolderResponse> call_folder = apiFolder.getMyFolder();
        call_folder.enqueue(new Callback<FolderResponse>() {
            @Override
            public void onResponse(Call<FolderResponse> call, Response<FolderResponse> response) {
                loading.setVisibility(View.INVISIBLE);
                int code = response.code();
                response.body();
                if (code == 200) {
                    FolderResponse result = response.body();
                    list_MyFolder = result.folders;
                    gridViewFolderAdapter = new GridViewFolderAdapter(list_MyFolder, getContext());
                    gridView.setAdapter(gridViewFolderAdapter);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            CategoryResponse.Folder itemFolder = (CategoryResponse.Folder) adapterView.getItemAtPosition(i);
                            ArrayList<CategoryResponse.Packages> lisPackage = (ArrayList<CategoryResponse.Packages>) itemFolder.packages;
                            Intent intent1 = new Intent(getActivity(), PackageActivity.class);
                            intent1.putExtra("listPackage", lisPackage);
                            startActivity(intent1);
                            getActivity().overridePendingTransition(R.anim.fade_in_right, R.anim.fade_out_right);
                        }
                    });

                } else if (code == 400 || code == 404 || code == 500) {
                    ErroResponse erroResponse = ErrorUtils.parseError(response);
                    if (erroResponse.getNewToken() != null) {
                        String token = "Bearer {" + erroResponse.getNewToken().toString() + "}";
                        localDb.updateToken(token);
                        RestAdapter.MyauthHeaderContent = token;
                        getFolder();
                    }
                    if (erroResponse.getStatus() != null)
                        if (erroResponse.getStatus().equals("you have not folder")) {
                            txtv_noti.setVisibility(View.VISIBLE);
                            txtv_noti.setText("you have not folder");
                        }
                    Toast.makeText(getActivity().getApplication(), erroResponse.getStatus(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FolderResponse> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.INVISIBLE);
            }
        });
    }

}
