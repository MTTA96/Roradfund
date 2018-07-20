package sample.Network;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import sample.Model.Wallet;
import sample.Util.ServerUrl;

import java.util.ArrayList;

public class RetrofitHelper {

    private ServicesImp servicesImp;

    public static final String BASE_URL = ServerUrl.ServerUrl;

    public RetrofitHelper(Retrofit retrofit) {
        this.servicesImp = RetrofitClient.getClient(BASE_URL).create(ServicesImp.class);
    }

    public static void request() {

    }

}
