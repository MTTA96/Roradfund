package sample.Network;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by zzzzz on 3/13/2018.
 */

public class RetrofitClient {
    private static Retrofit retrofitELearning = null;

    public static Retrofit getClient(String baseUrl) {

        if (retrofitELearning==null) {

            retrofitELearning = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }

        return retrofitELearning;

    }

}
