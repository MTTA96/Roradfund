package sample.Model.Symbol;

import sample.Model.Ethplorer.Token;

import java.util.ArrayList;

public class SymbolList {

    private String symbolName;
    private String tokenName;
    private Double total = 0d;
    private ArrayList<Token> tokenList = new ArrayList();

    public SymbolList() {}

    public SymbolList(String symbolName, String tokenName, Double total, ArrayList<Token> tokenList) {
        this.symbolName = symbolName;
        this.tokenName = tokenName;
        this.total = total;
        this.tokenList = tokenList;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public Double getTotal() {

        this.total = 0d;
        for (Token token : tokenList) {
            total += token.getBalance();
        }

        if (total < 10000000000d) {
            return total;
        }

        return total/1000000000000000000d;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public ArrayList<Token> getTokenList() {
        return tokenList;
    }

    public void setTokenList(ArrayList<Token> tokenList) {
        this.tokenList = tokenList;
    }

    /** ----- SUPPORTED FUNC ----- */

}
