package sample.Model.Ethplorer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Token {

    private String walletAddress;
    @SerializedName("tokenInfo")
    @Expose
    private TokenInfo tokenInfo;
    @SerializedName("balance")
    @Expose
    private Double balance;
//    @SerializedName("totalIn")
//    @Expose
//    private Integer totalIn;
//    @SerializedName("totalOut")
//    @Expose
//    private Integer totalOut;

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public TokenInfo getTokenInfo() {
        return tokenInfo;
    }

    public void setTokenInfo(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    public Double getBalance() {
        if (balance >=
                1000000000000000000000d) {
            return balance/1000000000000000000d;
        }
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

//    public Integer getTotalIn() {
//        return totalIn;
//    }
//
//    public void setTotalIn(Integer totalIn) {
//        this.totalIn = totalIn;
//    }
//
//    public Integer getTotalOut() {
//        return totalOut;
//    }
//
//    public void setTotalOut(Integer totalOut) {
//        this.totalOut = totalOut;
//    }

}
