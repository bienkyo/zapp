package vn.com.z11.z11app.ApiResponseModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kienlv58 on 12/6/16.
 */
public class ErroResponse {
    @SerializedName("code")
    private int code;
    @SerializedName("status")
    private String status;
    @SerializedName("newToken")
    private String newToken = null;

    public ErroResponse(int code, String status) {
        this.code = code;
        this.status = status;
    }

    public ErroResponse() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNewToken() {
        return newToken;
    }

    public void setNewToken(String newToken) {
        this.newToken = newToken;
    }
}
