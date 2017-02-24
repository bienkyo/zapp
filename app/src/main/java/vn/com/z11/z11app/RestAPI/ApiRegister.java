package vn.com.z11.z11app.RestAPI;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import vn.com.z11.z11app.ApiResponseModel.RegisterResponse;

/**
 * Created by kienlv58 on 12/19/16.
 */
public interface ApiRegister {
    @FormUrlEncoded
    @POST("users/register")
    Call<RegisterResponse> userRegister(@Field("name") String name, @Field("email") String email, @Field("gender") String gender, @Field("password") String password, @Field("password_confirmation") String password_confirmation);
}
