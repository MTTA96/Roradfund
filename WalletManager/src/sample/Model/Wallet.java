package sample.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sample.Interface.RequestBalanceByAddressCallBack;
import sample.Network.RetrofitClient;
import sample.Network.ServicesImp;
import sample.Util.ServerUrl;
import sample.Util.SupportKeys;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Wallet {

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("ETH")
    @Expose
    private ETH eTH;

    @SerializedName("countTxs")
    @Expose
    private Integer countTxs;

    @SerializedName("error")
    @Expose
    private Error error;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

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


    /** ----- METHOD ----- */

    public void checkBalance(String address, RequestBalanceByAddressCallBack requestBalanceByAddressCallBack) {

        ServicesImp servicesImp = RetrofitClient.getClient(ServerUrl.ServerUrl).create(ServicesImp.class);

        servicesImp.getBalanceByAddress(address, "freekey").enqueue(new Callback<Wallet>() {
            @Override
            public void onResponse(Call<Wallet> call, Response<Wallet> response) {
                Log.getLog("Request: ", call.request().toString(), 0);
                if(!response.isSuccessful()) {
                    Log.getLog("Get balance: ", response.message(), 0);
                    requestBalanceByAddressCallBack.balanceByAddressCallBack(SupportKeys.FAILED_CODE, null);
                    return;
                }

                if (response.body().error  != null ) {
                    Log.getLog("Get balance: ", response.body().error.getMessage(), 0);
                    requestBalanceByAddressCallBack.balanceByAddressCallBack(SupportKeys.SUCCESS_CODE, null);
                    return;
                }

                Log.getLog("Get balance: ", String.valueOf(response.body().eTH.getBalance()), 0);
                requestBalanceByAddressCallBack.balanceByAddressCallBack(SupportKeys.SUCCESS_CODE, String.valueOf(response.body().eTH.getBalance()));

            }

            @Override
            public void onFailure(Call<Wallet> call, Throwable throwable) {
                Log.getLog("Get balance: ", throwable.getLocalizedMessage(), 0);
                requestBalanceByAddressCallBack.balanceByAddressCallBack(SupportKeys.FAILED_CODE, null);
            }
        });

    }

}
