package sample.Network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by zzzzz on 3/13/2018.
 */

public class RetrofitClient {

    private static Retrofit retrofitEtherScan = null;
    private static Retrofit retrofitEtherplorer = null;
    private static Retrofit retrofitGas = null;

    private static final int timeOut = 90;

    public static Retrofit getEtherScanClient(String baseUrl) {

        if (retrofitEtherScan==null) {
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(timeOut, TimeUnit.SECONDS)
                    .connectTimeout(timeOut, TimeUnit.SECONDS)
                    .build();
            retrofitEtherScan = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }

        return retrofitEtherScan;

    }

    public static Retrofit getEtherplorerClient(String baseUrl) {

        if (retrofitEtherScan == null) {
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(timeOut, TimeUnit.SECONDS)
                    .connectTimeout(timeOut, TimeUnit.SECONDS)
                    .build();

            retrofitEtherplorer = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }

        return retrofitEtherplorer;

    }

    public static Retrofit getGasClient(String baseUrl) {

        if (retrofitGas == null) {
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(timeOut, TimeUnit.SECONDS)
                    .connectTimeout(timeOut, TimeUnit.SECONDS)
                    .build();

            retrofitGas = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }

        return retrofitGas;

    }

}
