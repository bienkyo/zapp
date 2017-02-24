package vn.com.z11.z11app.RestAPI;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import vn.com.z11.z11app.Adapter.RestAdapter;
import vn.com.z11.z11app.ApiResponseModel.ErroResponse;

/**
 * Created by kienlv58 on 12/6/16.
 */
public class ErrorUtils {
    public static ErroResponse parseError(Response<?> response) {
        Converter<ResponseBody, ErroResponse> converter =
                RestAdapter.getClient()
                        .responseBodyConverter(ErroResponse.class, new Annotation[0]);

        ErroResponse error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ErroResponse();
        }

        return error;
    }
}
