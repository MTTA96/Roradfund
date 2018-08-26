package sample.Model.ETHScanner;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sample.Interface.RequestBalanceByAddressCallBack;
import sample.Network.EtherScan.BaseResponse;
import sample.Network.RetrofitClient;
import sample.Network.EtherScan.EthScanServicesImp;
import sample.Util.ServerUrl;
import sample.Util.SupportKeys;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Formatter;

public class WalletScan {

    private int serial;
    @SerializedName("tokenAmount")
    @Expose
    private String tokenAmount;
    @SerializedName("account")
    @Expose
    private String account;
    @SerializedName("balance")
    @Expose
    private String balance;

    public WalletScan() {}

    public WalletScan(int serial, String account, String tokenAmount, String balance) {
        this.serial = serial;
        this.account = account;
        this.tokenAmount = tokenAmount;
        this.balance = balance;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public String getTokenAmount() {

        if (Double.valueOf(tokenAmount) < 1000000000) {

            return tokenAmount;
        }

        return String.valueOf(Double.valueOf(tokenAmount != null ? tokenAmount : String.valueOf(0.0))/1000000000000000000d);

    }

    public void setTokenAmount(String tokenAmount) {
        this.tokenAmount = tokenAmount;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBalance() {
        BigDecimal bg = new BigDecimal(balance);
        Formatter fmt = new Formatter();
        fmt.format("%." + bg.scale() + "f", bg);
//        return String.valueOf(Double.valueOf(balance)/1000000000000000000d);
        return fmt.toString();
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    /** ----- METHOD ----- */

    public static Double sum(ArrayList<WalletScan> walletScanList) {

        Double sum = 0d;

        for (WalletScan walletScan : walletScanList) {
            sum += Double.valueOf(walletScan.getBalance());
        }

        return sum;

    }

    /** ----- API SERVICES ----- */

    public static void checkBalance(String address, RequestBalanceByAddressCallBack requestBalanceByAddressCallBack) {

        EthScanServicesImp ethScanServicesImp = RetrofitClient.getEtherScanClient(ServerUrl.ServerUrl).create(EthScanServicesImp.class);

        ethScanServicesImp.getBalanceByAddress("account", "balancemulti", address, "latest", SupportKeys.etherscanAPIKey).enqueue(new Callback<BaseResponse>() {
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
