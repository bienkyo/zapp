package vn.com.z11.z11app.RestAPI;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.com.z11.z11app.ApiResponseModel.Code_StatusModel;

/**
 * Created by kienlv58 on 1/12/17.
 */
public interface ApiPurchases {

//item_code = package or item_code  = explain
    @POST("purchases")
    Call<Code_StatusModel> addPurchase(@Query("item_id")int item_id, @Query("item_code") String item_code);
    @GET("purchases/status/{item_code}/{item_id}")
    Call<Code_StatusModel> getChargeItem(@Path("item_code") String item_code,@Path("item_id") int item_id);
}
