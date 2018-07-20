package sample.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
