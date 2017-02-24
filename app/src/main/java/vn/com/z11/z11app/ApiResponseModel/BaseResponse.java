package vn.com.z11.z11app.ApiResponseModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Bien-kun on 21/02/2017.
 */

public class BaseResponse {
    @SerializedName("code")
    public int code;
    @SerializedName("status")
    public String status;
}
