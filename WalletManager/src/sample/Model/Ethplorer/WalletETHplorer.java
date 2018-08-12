package sample.Model.Ethplorer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sample.Interface.RequestBalanceByAddressCallBack;
import sample.Interface.RequestWalletEthplorerInfoCallBack;
import sample.Model.ETH;
import sample.Network.EtherScan.BaseResponse;
import sample.Network.EtherScan.ETHplorerImp;
import sample.Network.RetrofitClient;
import sample.Network.EtherScan.EthScanServicesImp;
import sample.Util.ServerUrl;
import sample.Util.SupportKeys;

import java.util.List;

public class WalletETHplorer {

    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("ETH")
    @Expose
    private ETH eTH;
    @SerializedName("countTxs")
    @Expose
    private Integer countTxs;
    @SerializedName("tokens")
    @Expose
    private List<Token> tokens = null;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ETH getETH() {
        return eTH;
    }

    public void setETH(ETH eTH) {
        this.eTH = eTH;
    }

    public Integer getCountTxs() {
        return countTxs;
    }

    public void setCountTxs(Integer countTxs) {
        this.countTxs = countTxs;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    /** ----- API SERVICES ----- */

    public static void getWalletInfo(String address, RequestWalletEthplorerInfoCallBack requestWalletEthplorerInfoCallBack) {

        ETHplorerImp etHplorerImp = RetrofitClient.getEtherplorerClient(ServerUrl.EthplorerUrl).create(ETHplorerImp.class);

        etHplorerImp.getAddressInfo(address, SupportKeys.etherplorerAPIKey).enqueue(new Callback<WalletETHplorer>() {
            @Override
            public void onResponse(Call<WalletETHplorer> call, Response<WalletETHplorer> response) {

                System.out.print("Request: " + call.request().toString() + "\n");

                // Handle errors
                if(!response.isSuccessful()) {
//                    System.out.print("Get balance: " + response.body().error.getMessage() + "\n");
                    requestWalletEthplorerInfoCallBack.walletInfoCallBack(SupportKeys.FAILED_CODE, null);
                    return;
                }

                // Success
                System.out.print("Get balance: " + String.valueOf(response.body()) + "\n");
                requestWalletEthplorerInfoCallBack.walletInfoCallBack(SupportKeys.SUCCESS_CODE, response.body());

            }

            @Override
            public void onFailure(Call<WalletETHplorer> call, Throwable throwable) {
                System.out.print("Request: " + call.request().toString() + "\n");
                System.out.print("Get balance: " + throwable.getLocalizedMessage() + "\n");
                requestWalletEthplorerInfoCallBack.walletInfoCallBack(SupportKeys.FAILED_CODE, null);
            }

        });
    }

}
