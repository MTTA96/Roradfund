package sample.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zzzzz on 3/13/2018.
 */

public class RetrofitClient {
    private static Retrofit retrofitEtherScan = null;

    public static Retrofit getClient(String baseUrl) {

        if (retrofitEtherScan==null) {

            retrofitEtherScan = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }

        return retrofitEtherScan;

    }

}
