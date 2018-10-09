package sample.Model.Ethplorer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Formatter;

import static java.lang.Math.pow;

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
        BigDecimal bg = new BigDecimal(String.valueOf(balance));
        Formatter fmt = new Formatter();
        fmt.format("%" + bg.scale() + "f", bg);
        if (balance >= 100000000d) {
//            System.out.print(balance + " / 10^" + (fmt.toString().length()-11) + " - " + pow(10, fmt.toString().length()-11) + " = " + balance / pow(10, fmt.toString().length()-11) + "\n");
            return balance / pow(10, fmt.toString().length()-11);
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
