package sample.Network;

import retrofit2.Retrofit;
import sample.Network.EtherScan.EthScanServicesImp;
import sample.Util.ServerUrl;

public class RetrofitHelper {

    private EthScanServicesImp ethScanServicesImp;

    public static final String BASE_URL = ServerUrl.ServerUrl;

    public RetrofitHelper(Retrofit retrofit) {
        this.ethScanServicesImp = RetrofitClient.getEtherScanClient(BASE_URL).create(EthScanServicesImp.class);
    }

    public static void request() {

    }

}
