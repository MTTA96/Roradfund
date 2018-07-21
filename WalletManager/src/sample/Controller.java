package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Interface.RequestBalanceByAddressCallBack;
import sample.Model.Balance;
import sample.Model.Wallet;
import sample.Util.SupportKeys;

import java.util.Timer;
import java.util.TimerTask;

public class Controller implements RequestBalanceByAddressCallBack {

    @FXML
    private Label lblCountedWallet;
    @FXML
    private TextArea txtaAddresses;
    @FXML
    private Button check, stop;
    @FXML
    private TableView<Balance> tbvResults;
    @FXML
    private TableColumn<Balance, String> addressCol, balanceCol;

    private final ObservableList<Balance> data = FXCollections.observableArrayList();
    private String[] addressList;
    private int countWallet = 0;

    /** ACTIONS */

    @FXML
    void checkBalance(ActionEvent event) {
        countWallet = 0;
        data.clear();
        tbvResults.setItems(data);

        //0x3750fC1505ba9a4cA3907b94Cda8e5758d31F3aD
        updateCountingLabel(countWallet);

        addressCol.setCellValueFactory(
                new PropertyValueFactory<Balance,String>("address")
        );
        balanceCol.setCellValueFactory(
                new PropertyValueFactory<Balance, String>("balance")
        );

        Wallet wallet = new Wallet();

        if (!txtaAddresses.getText().isEmpty()) {

            addressList = txtaAddresses.getText().split("\n");

            Timer timer = new Timer();

            timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (countWallet < addressList.length) {
                            wallet.checkBalance(addressList[countWallet], Controller.this);
                            countWallet += 1;
                        } else {
                            this.cancel();
                        }
                    }
                },0, 6000);

        }

    }

    @FXML
    void delete(ActionEvent event) {

    }

    @FXML
    void stop(ActionEvent event) {

    }

    private void updateCountingLabel(int countedWallet) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lblCountedWallet.setText("Kết quả: " + countedWallet + "/" + addressList.length);
            }
        });
    }
    /** HANDLE RESULTS */

    @Override
    public void balanceByAddressCallBack(int errorCode, Wallet wallet) {

        updateCountingLabel(countWallet);

        updateBalanceCol(wallet);

    }

    private void updateBalanceCol(Wallet wallet) {
        String address;
        String balance;

        if (wallet == null) {
            address = addressList[countWallet];
            balance = "Error!";
        } else {
            address = wallet.getAddress() != null ? wallet.getAddress():"Error!";
            balance = wallet.getETH().getBalance()!= null ? String.valueOf(wallet.getETH().getBalance()):"Error!";
        }

        data.add(new Balance(address, balance));
        tbvResults.setItems(data);
    }

}
