package vn.com.z11.z11app.RestAPI;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;
import vn.com.z11.z11app.ApiResponseModel.CategoryResponse;
import vn.com.z11.z11app.ApiResponseModel.Code_StatusModel;

/**
 * Created by kienlv58 on 1/12/17.
 */
public interface ApiUserAnswer {
    @POST("useranswer")
    Call<Code_StatusModel> addUserAnswer(@Query("result")String result );
}
