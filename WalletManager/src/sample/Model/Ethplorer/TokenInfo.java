package sample.Model.Ethplorer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenInfo {
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("decimals")
    @Expose
    private String decimals;
    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("totalSupply")
    @Expose
    private String totalSupply;
    @SerializedName("owner")
    @Expose
    private String owner;
    @SerializedName("lastUpdated")
    @Expose
    private Integer lastUpdated;
    @SerializedName("issuancesCount")
    @Expose
    private Integer issuancesCount;
    @SerializedName("holdersCount")
    @Expose
    private Integer holdersCount;
    @SerializedName("price")
    @Expose
    private Boolean price;
    @SerializedName("ethTransfersCount")
    @Expose
    private Integer ethTransfersCount;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDecimals() {
        return decimals;
    }

    public void setDecimals(String decimals) {
        this.decimals = decimals;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(String totalSupply) {
        this.totalSupply = totalSupply;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Integer lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getIssuancesCount() {
        return issuancesCount;
    }

    public void setIssuancesCount(Integer issuancesCount) {
        this.issuancesCount = issuancesCount;
    }

    public Integer getHoldersCount() {
        return holdersCount;
    }

    public void setHoldersCount(Integer holdersCount) {
        this.holdersCount = holdersCount;
    }

    public Boolean getPrice() {
        return price;
    }

    public void setPrice(Boolean price) {
        this.price = price;
    }

    public Integer getEthTransfersCount() {
        return ethTransfersCount;
    }

    public void setEthTransfersCount(Integer ethTransfersCount) {
        this.ethTransfersCount = ethTransfersCount;
    }
}
