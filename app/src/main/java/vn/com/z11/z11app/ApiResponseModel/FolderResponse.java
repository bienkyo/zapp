package vn.com.z11.z11app.ApiResponseModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kienlv58 on 12/22/16.
 */
public class FolderResponse {


    @SerializedName("code")
    public int code;
    @SerializedName("status")
    public String status;
    @SerializedName("metadata")
    public ArrayList<CategoryResponse.Folder> folders;
}
