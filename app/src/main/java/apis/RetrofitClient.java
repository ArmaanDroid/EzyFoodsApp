package apis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import utils.AppConst;

/**
 * Created by Armaan on 01-11-2017.
 */

public class RetrofitClient {
    private static Retrofit retrofit;
    private static WebApi restClient;

    private RetrofitClient() {

    }

    static {
        setUpRestClient();
    }

    private static void setUpRestClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(3, TimeUnit.MINUTES);
            builder.readTimeout(3, TimeUnit.MINUTES);
            builder.writeTimeout(3, TimeUnit.MINUTES);
            builder.addInterceptor(interceptor);
            OkHttpClient client = builder.build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(AppConst.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();

            restClient=retrofit.create(WebApi.class);
        }
    }

    public static WebApi getRestClient() {
        return restClient;
    }
}
