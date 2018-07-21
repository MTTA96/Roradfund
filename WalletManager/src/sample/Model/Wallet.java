package sample.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sample.Interface.RequestBalanceByAddressCallBack;
import sample.Network.EtherScan.BaseResponse;
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

//        servicesImp.getBalanceByAddress(address, "freekey").enqueue(new Callback<Wallet>() {
//
//            @Override
//            public void onResponse(Call<Wallet> call, Response<Wallet> response) {
//
//                System.out.print("Request: " + call.request().toString() + "\n");
//
//                // Handle errors
//                if(!response.isSuccessful()) {
//                    System.out.print("Get balance: Error from server - " + response.message() + "\n");
//                    requestBalanceByAddressCallBack.balanceByAddressCallBack(SupportKeys.FAILED_CODE, null);
//                    return;
//                }
//
//                if (response.body().error  != null ) {
//                    System.out.print("Get balance: " + response.body().error.getMessage() + "\n");
//                    requestBalanceByAddressCallBack.balanceByAddressCallBack(SupportKeys.FAILED_CODE, response.body());
//                    return;
//                }
//
//                // Success
//                System.out.print("Get balance: " + String.valueOf(response.body().eTH.getBalance()) + "\n");
//                requestBalanceByAddressCallBack.balanceByAddressCallBack(SupportKeys.SUCCESS_CODE, response.body());

//            }
//
//            @Override
//            public void onFailure(Call<Wallet> call, Throwable throwable) {
//                System.out.print("Get balance: " + throwable.getLocalizedMessage() + "\n");
//                requestBalanceByAddressCallBack.balanceByAddressCallBack(SupportKeys.FAILED_CODE, null);
//            }
//
//        });

//        module=account&action=balancemulti&address=0x9542d13876b23e347D3fe455C4a6CF4172EFf5F4&tag=latest&apikey=A2YM3RHX37AVY5G5312XD2QS9N76FDV4Z3




        servicesImp.getBalanceByAddress("account", "balancemulti", address, "latest", "A2YM3RHX37AVY5G5312XD2QS9N76FDV4Z3").enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                System.out.print("Request: " + call.request().toString() + "\n");

                // Handle errors
                if(!response.isSuccessful()) {
//                    System.out.print("Get balance: " + response.body().error.getMessage() + "\n");
                    requestBalanceByAddressCallBack.balanceByAddressCallBack(SupportKeys.FAILED_CODE, null);
                    return;
                }

//                if (response.body().error  != null ) {
//                System.out.print("Get balance: Error from server - " + response.message() + "\n");
//                    requestBalanceByAddressCallBack.balanceByAddressCallBack(SupportKeys.FAILED_CODE, response.body().getResult());
//                    return;
//                }

                // Success
                System.out.print("Get balance: " + String.valueOf(response.body().getResult().get(0).getBalance()) + "\n");
                requestBalanceByAddressCallBack.balanceByAddressCallBack(SupportKeys.SUCCESS_CODE, response.body().getResult());

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable throwable) {
                System.out.print("Request: " + call.request().toString() + "\n");
                System.out.print("Get balance: " + throwable.getLocalizedMessage() + "\n");
                requestBalanceByAddressCallBack.balanceByAddressCallBack(SupportKeys.FAILED_CODE, null);
            }

        });
    }

}
