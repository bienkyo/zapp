package vn.com.z11.z11app.RestAPI;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.com.z11.z11app.ApiResponseModel.Code_StatusModel;
import vn.com.z11.z11app.ApiResponseModel.MyRateResponseModel;

/**
 * Created by kienlv58 on 1/13/17.
 */
public interface ApiRate {
    @GET("packages/ownerPackage/{folder_id}/{package_id}")
    Call<MyRateResponseModel> getMyRate(@Path("folder_id") int folder_id,@Path("package_id") int package_id);
    @PUT("package_rate")
    Call<Code_StatusModel> addMyRate(@Query("package_id") int package_id ,@Query("rate_star") float rate_star);
    @PUT("package_edit_rate")
    Call<Code_StatusModel> editMyRate(@Query("package_id") int package_id ,@Query("rate_star") float rate_star);
}
