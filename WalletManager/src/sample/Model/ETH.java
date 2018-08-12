package sample.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ETH {

    @SerializedName("balance")
    @Expose
    private Double balance;

    public Double getBalance() {
        return Double.valueOf(balance)/100000000000000000d;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

}
