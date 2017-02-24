package vn.com.z11.z11app.RestAPI;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import vn.com.z11.z11app.ApiResponseModel.CategoryResponse;
import vn.com.z11.z11app.ApiResponseModel.FolderResponse;

/**
 * Created by kienlv58 on 12/22/16.
 */
public interface APIFolder {
    @GET("folder_myfolder")
    Call<FolderResponse> getMyFolder();
}
