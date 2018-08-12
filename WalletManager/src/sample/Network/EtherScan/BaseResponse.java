package sample.Network.EtherScan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import sample.Model.ETHScanner.Wallet;

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
    private ArrayList<Wallet> result = null;

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

    public ArrayList<Wallet> getResult() {
        return result;
    }

    public void setResult(ArrayList<Wallet> result) {
        this.result = result;
    }

}
