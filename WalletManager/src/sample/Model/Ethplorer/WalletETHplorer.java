package sample.Model.Ethplorer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import sample.Model.ETH;

import java.util.List;

public class WalletETHplorer {

    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("ETH")
    @Expose
    private ETH eTH;
    @SerializedName("countTxs")
    @Expose
    private Integer countTxs;
    @SerializedName("tokens")
    @Expose
    private List<Token> tokens = null;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ETH getETH() {
        return eTH;
    }

    public void setETH(ETH eTH) {
        this.eTH = eTH;
    }

    public Integer getCountTxs() {
        return countTxs;
    }

    public void setCountTxs(Integer countTxs) {
        this.countTxs = countTxs;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }
}
