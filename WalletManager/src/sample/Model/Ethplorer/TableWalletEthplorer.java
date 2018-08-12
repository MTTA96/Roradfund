package sample.Model.Ethplorer;

public class TableWalletEthplorer {

    private int serial;
    private String symbol;
    private String tokenName;
    private int numberOfToken;
    private int numberOfWallet;
    private Double sum;

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public int getNumberOfToken() {
        return numberOfToken;
    }

    public void setNumberOfToken(int numberOfToken) {
        this.numberOfToken = numberOfToken;
    }

    public int getNumberOfWallet() {
        return numberOfWallet;
    }

    public void setNumberOfWallet(int numberOfWallet) {
        this.numberOfWallet = numberOfWallet;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }
}
