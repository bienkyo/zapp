package vn.com.z11.z11app.Adapter;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by kienlv58 on 12/2/16.
 */
public class RestAdapter {
//    http://www.programcreek.com/java-api-examples/index.php?api=retrofit.http.POST
    public static final String API_BASE_URL = "http://kien.godfath.com/api/v1/";
    private static Retrofit retrofit = null;
    public static String MyauthHeaderContent = "Bearer {your_token}";

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    private  static OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(logging).addNetworkInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            builder.addHeader("Authorization", MyauthHeaderContent);

            return chain.proceed(builder.build());
        }
    });





    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }

}
