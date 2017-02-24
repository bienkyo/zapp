package vn.com.z11.z11app.RestAPI;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import vn.com.z11.z11app.ApiResponseModel.ChapterResponse;
import vn.com.z11.z11app.ApiResponseModel.FolderResponse;

/**
 * Created by kienlv58 on 12/29/16.
 */
public interface ApiChapter {
    @GET("getChapterToAnswer/{chapter_id}")
    Call<ChapterResponse> getChapter(@Path("chapter_id") int chapter_id);
}
