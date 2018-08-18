package sample.Model.Symbol;

import sample.Model.Ethplorer.Token;

import java.util.ArrayList;

public class SymbolList {

    private String symbolName;
    private Double total = 0d;
    private ArrayList<Token> tokenList = new ArrayList();

    public SymbolList() {}

    public SymbolList(String symbolName, Double total, ArrayList<Token> tokenList) {
        this.symbolName = symbolName;
        this.total = total;
        this.tokenList = tokenList;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total += total;
    }

    public ArrayList<Token> getTokenList() {
        return tokenList;
    }

    public void setTokenList(ArrayList<Token> tokenList) {
        this.tokenList = tokenList;
    }

    /** ----- SUPPORTED FUNC ----- */

}
