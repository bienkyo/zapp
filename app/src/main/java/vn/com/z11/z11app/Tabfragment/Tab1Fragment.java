package vn.com.z11.z11app.Tabfragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.com.z11.z11app.Adapter.GridviewAdapter;
import vn.com.z11.z11app.Adapter.RestAdapter;
import vn.com.z11.z11app.ApiResponseModel.CategoryResponse;
import vn.com.z11.z11app.FolderActivity;
import vn.com.z11.z11app.MainActivity;
import vn.com.z11.z11app.PackageActivity;
import vn.com.z11.z11app.R;
import vn.com.z11.z11app.RestAPI.ApiCategory;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab1Fragment extends Fragment {
    GridView gridView;
    GridviewAdapter gridviewAdapter;
    CategoryResponse categoryResponse;
    PtrFrameLayout ptrFrameLayout;


    public Tab1Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_tab1, container, false);
        ptrFrameLayout = (PtrFrameLayout)root.findViewById(R.id.store_house_ptr_frame);
        gridView = (GridView) root.findViewById(R.id.gridView1);
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
        },2000);





        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrameLayout.refreshComplete();
                    }
                },3000);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame,content,header);
               // return super.checkCanDoRefresh(frame, content, header);
            }
        });


        //test
        ApiCategory apiCategory = RestAdapter.getClient().create(ApiCategory.class);
        Call<CategoryResponse> call = apiCategory.getAllCategory(5, 0);
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                categoryResponse = response.body();
                ArrayList<CategoryResponse.Category> listCate = (ArrayList<CategoryResponse.Category>) categoryResponse.list_category;
                if(listCate !=  null &&listCate.size() > 0){
                    Bundle bundle = new Bundle();
                    MainActivity.myBundle.putSerializable("listcate",listCate);



                    gridviewAdapter = new GridviewAdapter(listCate, getActivity().getApplicationContext());

                    gridView.setAdapter(gridviewAdapter);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(getContext(), FolderActivity.class);
                            CategoryResponse.Category itemClick = (CategoryResponse.Category) adapterView.getItemAtPosition(i);
                            ArrayList<CategoryResponse.Folder> listFolder = (ArrayList<CategoryResponse.Folder>) itemClick.folder;
                            intent.putExtra("listFolder",listFolder);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.fade_in_right, R.anim.fade_out_right);




                        }
                    });
                    // ArrayList<CategoryResponse.CategoryData> listdata = new ArrayList<CategoryResponse.CategoryData>(response.body().getListdata());
                }

            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {

            }
        });

        return root;
    }

}
//https://viblo.asia/HoaLQ/posts/amoG81kdvz8P
//http://www.androidhive.info/2016/05/android-working-with-retrofit-http-library/
//http://allenlearntocode.com/index.php/tag/retrofit/
//https://viblo.asia/hungtdo/posts/AQrMJVojM40E
