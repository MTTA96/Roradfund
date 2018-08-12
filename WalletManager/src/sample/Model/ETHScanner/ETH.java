package sample.Model.ETHScanner;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ETH {

    @SerializedName("balance")
    @Expose
    private Double balance;

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

}
