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

import java.util.ArrayList;

public class Wallet {

    private int serial;
    @SerializedName("account")
    @Expose
    private String account;
    @SerializedName("balance")
    @Expose
    private String balance;

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBalance() {
        return String.valueOf(Double.valueOf(balance)/100000000000000000d);
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    /** ----- METHOD ----- */

    public static Double sum(ArrayList<Wallet> walletList) {

        Double sum = 0d;

        for (Wallet wallet: walletList) {
            sum += Double.valueOf(wallet.getBalance());
        }

        return sum;

    }

    /** ----- API SERVICES ----- */

    public static void checkBalance(String address, RequestBalanceByAddressCallBack requestBalanceByAddressCallBack) {

        ServicesImp servicesImp = RetrofitClient.getClient(ServerUrl.ServerUrl).create(ServicesImp.class);

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
