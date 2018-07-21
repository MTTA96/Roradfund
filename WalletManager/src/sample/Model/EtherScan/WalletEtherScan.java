package sample.Model.EtherScan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletEtherScan {

    @SerializedName("account")
    @Expose
    private String account;
    @SerializedName("balance")
    @Expose
    private String balance;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

}
