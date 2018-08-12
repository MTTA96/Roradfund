package sample.Network.EtherScan;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import sample.Model.Ethplorer.WalletETHplorer;
import sample.Util.ServerUrl;

public interface ETHplorerImp {

    @GET(ServerUrl.GET_ADDRESS_INFO + "{address}")
    Call<WalletETHplorer> getAddressInfo(@Path("address") String address,
                                         @Query("apiKey") String apiKey);

}
