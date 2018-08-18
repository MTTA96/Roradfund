package sample.Model.Symbol;

import sample.Model.Ethplorer.WalletETHplorer;

import java.util.ArrayList;

public class Symbol {

    private String symbol;
    private ArrayList<WalletETHplorer> walletList = new ArrayList<>();

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public ArrayList<WalletETHplorer> getWalletList() {
        return walletList;
    }

    public void setWalletList(ArrayList<WalletETHplorer> walletList) {
        this.walletList = walletList;
    }

}
