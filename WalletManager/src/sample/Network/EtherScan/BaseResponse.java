package sample.Network.EtherScan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import sample.Model.EtherScan.WalletEtherScan;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class BaseResponse {


    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private ArrayList<WalletEtherScan> result = null;

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

    public ArrayList<WalletEtherScan> getResult() {
        return result;
    }

    public void setResult(ArrayList<WalletEtherScan> result) {
        this.result = result;
    }

}
