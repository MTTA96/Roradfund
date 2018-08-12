package sample.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zzzzz on 3/13/2018.
 */

public class RetrofitClient {

    private static Retrofit retrofitEtherScan = null;
    private static Retrofit retrofitEtherplorer = null;

    public static Retrofit getEtherScanClient(String baseUrl) {

        if (retrofitEtherScan==null) {

            retrofitEtherScan = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }

        return retrofitEtherScan;

    }

    public static Retrofit getEtherplorerClient(String baseUrl) {

        if (retrofitEtherScan == null) {

            retrofitEtherplorer = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }

        return retrofitEtherplorer;

    }

}
