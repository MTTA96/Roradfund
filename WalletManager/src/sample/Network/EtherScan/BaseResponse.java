package sample.Network.EtherScan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import sample.Model.ETHScanner.WalletScan;

import java.util.ArrayList;

public class BaseResponse {


    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private ArrayList<WalletScan> result = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<WalletScan> getResult() {
        return result;
    }

    public void setResult(ArrayList<WalletScan> result) {
        this.result = result;
    }

}
