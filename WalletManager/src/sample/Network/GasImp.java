package sample.Network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import sample.Model.Ethplorer.WalletETHplorer;
import sample.Model.Gas;
import sample.Util.ServerUrl;

public interface GasImp {


    @GET(ServerUrl.GET_GAS_URL)
    Call<Gas> getGas();

}
