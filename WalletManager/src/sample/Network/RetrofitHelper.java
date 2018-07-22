package sample.Network;

import retrofit2.Retrofit;
import sample.Util.ServerUrl;

public class RetrofitHelper {

    private ServicesImp servicesImp;

    public static final String BASE_URL = ServerUrl.ServerUrl;

    public RetrofitHelper(Retrofit retrofit) {
        this.servicesImp = RetrofitClient.getClient(BASE_URL).create(ServicesImp.class);
    }

    public static void request() {

    }

}
