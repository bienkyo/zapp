package vn.com.z11.z11app.Tabfragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.com.z11.z11app.Adapter.LessionAdapter;
import vn.com.z11.z11app.Adapter.RestAdapter;
import vn.com.z11.z11app.ApiResponseModel.ErroResponse;
import vn.com.z11.z11app.ApiResponseModel.ListPackageResponse;
import vn.com.z11.z11app.MainActivity;
import vn.com.z11.z11app.R;
import vn.com.z11.z11app.RestAPI.ApiPackage;
import vn.com.z11.z11app.RestAPI.ErrorUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab2fragment extends Fragment {
    RecyclerView recyclerView;
    TextView txtv_noti;
    PtrFrameLayout ptrFrameLayout;
    LessionAdapter lessionAdapter;
    ArrayList<ListPackageResponse.Packages> listLession;

    public Tab2fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_tab2, container, false);
        ptrFrameLayout = (PtrFrameLayout)root.findViewById(R.id.store_house_ptr_frame);
        recyclerView = (RecyclerView) root.findViewById(R.id.recycle_mylession);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setHasFixedSize(true);
        txtv_noti = (TextView)root.findViewById(R.id.txtv_noti);
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

        return  root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLession();
    }

    public void getLession(){
        ApiPackage apiPackage = RestAdapter.getClient().create(ApiPackage.class);
        Call<ListPackageResponse> responseCall = apiPackage.getLession();
        responseCall.enqueue(new Callback<ListPackageResponse>() {
            @Override
            public void onResponse(Call<ListPackageResponse> call, Response<ListPackageResponse> response) {
                int code = response.code();
                if(code == 200){
                    ListPackageResponse res = response.body();
                    listLession = (ArrayList<ListPackageResponse.Packages>) res.mPackage;
                    lessionAdapter = new LessionAdapter(listLession,getActivity());
                    recyclerView.setAdapter(lessionAdapter);

                }else if(code == 400 || code == 404){
                    ErroResponse erroResponse = ErrorUtils.parseError(response);
                    if (erroResponse.getNewToken() != null) {
                        String token = "Bearer {" + erroResponse.getNewToken().toString() + "}";
                        MainActivity.localDb.updateToken(token);
                        RestAdapter.MyauthHeaderContent = token;
                        getLession();
                    }
                    if (erroResponse.getStatus() != null)
                        if (erroResponse.getStatus() != null) {
                            txtv_noti.setVisibility(View.VISIBLE);
                        }
                    Toast.makeText(getActivity().getApplication(), erroResponse.getStatus(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ListPackageResponse> call, Throwable t) {

            }
        });
    }

}
