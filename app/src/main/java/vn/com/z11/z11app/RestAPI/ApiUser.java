package vn.com.z11.z11app.RestAPI;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import vn.com.z11.z11app.ApiResponseModel.LanguageResponse;
import vn.com.z11.z11app.ApiResponseModel.LoginResponse;

/**
 * Created by kienlv58 on 12/6/16.
 */
public interface ApiUser {
    @FormUrlEncoded
    @POST("users/login")
    Call<LoginResponse> userLogin(@Field("grant_type") String grant_type,@Field("email") String email,@Field("password") String password,@Field("token") String token);

    @GET("language")
    Call<LanguageResponse> getLanguages();

    @GET("users/profile")
    Call<LoginResponse> getUserProfile();
}
