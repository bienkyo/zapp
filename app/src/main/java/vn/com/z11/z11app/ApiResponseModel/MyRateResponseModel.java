package vn.com.z11.z11app.ApiResponseModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kienlv58 on 1/13/17.
 */
public class MyRateResponseModel {

    @SerializedName("code")
    public int code;
    @SerializedName("status")
    public String status;
    @SerializedName("name")
    public String name;
    @SerializedName("email")
    public String email;
    @SerializedName("avatar")
    public String avatar;
    @SerializedName("my_rate")
    public String my_rate;
}
