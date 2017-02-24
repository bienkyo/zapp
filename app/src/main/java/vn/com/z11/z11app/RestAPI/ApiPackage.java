package vn.com.z11.z11app.RestAPI;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import vn.com.z11.z11app.ApiResponseModel.ListPackageResponse;
import vn.com.z11.z11app.ApiResponseModel.PackageResponse;

/**
 * Created by kienlv58 on 12/23/16.
 */
public interface ApiPackage {
    @GET("package/{package_id}")
    Call<PackageResponse> getOnepackage(@Path("package_id")int package_id);
    @GET("lession")
    Call<ListPackageResponse> getLession();
}
