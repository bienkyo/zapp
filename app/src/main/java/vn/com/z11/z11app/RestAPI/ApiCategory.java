package vn.com.z11.z11app.RestAPI;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.com.z11.z11app.ApiResponseModel.CategoryResponse;

/**
 * Created by kienlv58 on 12/2/16.
 */
public interface ApiCategory {

//    @Path – variable substitution for the API endpoint. For example movie id will be swapped for{id} in the URL endpoint.
//
//    @Query – specifies the query key name with the value of the annotated parameter.
//
//    @Body – payload for the POST call
//
//    @Header – specifies the header with the value of the annotated parameter


    @GET("category/{id}")
    Call<CategoryResponse> getCategory(@Path("id")int id);
    @GET("categories/{take}/{skip}")
    Call<CategoryResponse> getAllCategory(@Path("take")int take,@Path("skip") int skip);
    @POST("admin/add_category")
    Call<CategoryResponse> addCategory(@Query("uid")int uid,@Query("category_code") String category_code,@Query("translate") String translate);

}
