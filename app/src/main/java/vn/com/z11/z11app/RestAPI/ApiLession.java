package vn.com.z11.z11app.RestAPI;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;
import vn.com.z11.z11app.ApiResponseModel.Code_StatusModel;

/**
 * Created by kienlv58 on 1/17/17.
 */
public interface ApiLession {
    @POST("lession")
    Call<Code_StatusModel> addMyLession(@Query("package_id") int package_id );
}
