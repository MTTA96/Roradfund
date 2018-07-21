package sample.Model;

import javafx.beans.property.SimpleStringProperty;

public class Balance {
    private final SimpleStringProperty address;
    private final SimpleStringProperty balance;

    public Balance(String address, String balance) {
        this.address = new SimpleStringProperty(address);
        this.balance = new SimpleStringProperty(balance);
    }

    public String getAddress() {
        return address.get();
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getBalance() {
        return balance.get();
    }

    public SimpleStringProperty balanceProperty() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance.set(balance);
    }
}
