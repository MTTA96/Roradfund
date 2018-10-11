package sample.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sample.Interface.RequestGasCallBack;
import sample.Model.Ethplorer.WalletETHplorer;
import sample.Network.EtherScan.ETHplorerImp;
import sample.Network.GasImp;
import sample.Network.RetrofitClient;
import sample.Util.ServerUrl;
import sample.Util.SupportKeys;

public class Gas {

    @SerializedName("average")
    @Expose
    private Double average;
    @SerializedName("fastestWait")
    @Expose
    private Double fastestWait;
    @SerializedName("fastWait")
    @Expose
    private Double fastWait;
    @SerializedName("fast")
    @Expose
    private Double fast;
    @SerializedName("safeLowWait")
    @Expose
    private Double safeLowWait;
    @SerializedName("blockNum")
    @Expose
    private Integer blockNum;
    @SerializedName("avgWait")
    @Expose
    private Double avgWait;
    @SerializedName("block_time")
    @Expose
    private Double blockTime;
    @SerializedName("speed")
    @Expose
    private Double speed;
    @SerializedName("fastest")
    @Expose
    private Double fastest;
    @SerializedName("safeLow")
    @Expose
    private Double safeLow;

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Double getFastestWait() {
        return fastestWait;
    }

    public void setFastestWait(Double fastestWait) {
        this.fastestWait = fastestWait;
    }

    public Double getFastWait() {
        return fastWait;
    }

    public void setFastWait(Double fastWait) {
        this.fastWait = fastWait;
    }

    public Double getFast() {
        return fast;
    }

    public void setFast(Double fast) {
        this.fast = fast;
    }

    public Double getSafeLowWait() {
        return safeLowWait;
    }

    public void setSafeLowWait(Double safeLowWait) {
        this.safeLowWait = safeLowWait;
    }

    public Integer getBlockNum() {
        return blockNum;
    }

    public void setBlockNum(Integer blockNum) {
        this.blockNum = blockNum;
    }

    public Double getAvgWait() {
        return avgWait;
    }

    public void setAvgWait(Double avgWait) {
        this.avgWait = avgWait;
    }

    public Double getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(Double blockTime) {
        this.blockTime = blockTime;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getFastest() {
        return fastest;
    }

    public void setFastest(Double fastest) {
        this.fastest = fastest;
    }

    public Double getSafeLow() {
        return safeLow;
    }

    public void setSafeLow(Double safeLow) {
        this.safeLow = safeLow;
    }

    /** ----- METHODS ----- */

    public static void getGas(RequestGasCallBack requestGasCallBack) {

        GasImp gasImp = RetrofitClient.getGasClient(ServerUrl.GAS_BASE_URL).create(GasImp.class);

        gasImp.getGas().enqueue(new Callback<Gas>() {
            @Override
            public void onResponse(Call<Gas> call, Response<Gas> response) {

                System.out.print("Request: " + call.request().toString() + "\n");

                // Handle errors
                if(!response.isSuccessful()) {
//                    System.out.print("Get balance: " + response.body().error.getMessage() + "\n");
                    requestGasCallBack.requestGasCallBack(SupportKeys.FAILED_CODE, "error", 0d);
                    return;
                }

                // Success
                System.out.print("Get balance: " + String.valueOf(response.body()) + "\n");
                requestGasCallBack.requestGasCallBack(SupportKeys.SUCCESS_CODE, null, response.body().getSafeLow());

            }

            @Override
            public void onFailure(Call<Gas> call, Throwable throwable) {
                System.out.print("Request: " + call.request().toString() + "\n");
                System.out.print("Get balance: " + throwable.getLocalizedMessage() + "\n");
                requestGasCallBack.requestGasCallBack(SupportKeys.FAILED_CODE, throwable.getLocalizedMessage(), 0d);
            }

        });

    }

}
