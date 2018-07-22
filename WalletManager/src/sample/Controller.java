package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Interface.RequestBalanceByAddressCallBack;
import sample.Model.Wallet;

import java.util.ArrayList;
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
    private TableView<Wallet> tbvResults;
    @FXML
    private TableColumn<Wallet, String> addressCol, balanceCol;
    @FXML
    private TableColumn<Wallet, Integer> serialCol;

    private final ObservableList<Wallet> data = FXCollections.observableArrayList();
    private String[] addressList;
    private int countWallet = 0;

    //0x3750fC1505ba9a4cA3907b94Cda8e5758d31F3aD

    /** CONFIG */

    private void configColumns() {

        serialCol.setCellValueFactory(new PropertyValueFactory<Wallet, Integer>("serial"));

        addressCol.setCellValueFactory(new PropertyValueFactory<Wallet,String>("account"));

        balanceCol.setCellValueFactory(new PropertyValueFactory<Wallet, String>("balance"));

    }

    /** ACTIONS */

    @FXML
    void checkBalance(ActionEvent event) {
        countWallet = 0;
        data.clear();
        tbvResults.setItems(data);

        updateCountingLabel(countWallet);

        configColumns();

        // Prepare data

        Wallet wallet = new Wallet();

        if (!txtaAddresses.getText().isEmpty()) {

            addressList = txtaAddresses.getText().split("\n");

            Timer timer = new Timer();

            timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (countWallet < addressList.length) {
                            String addressesString = "";

                            int calculatedCounting = (countWallet + 20 < addressList.length ? countWallet + 20 : countWallet + (addressList.length - countWallet));

                            for (int i = countWallet; i < calculatedCounting; i++) {
                                if (i == calculatedCounting - 1)
                                    addressesString += addressList[i];
                                else
                                    addressesString += addressList[i] + ",";
                            }

                            countWallet += countWallet + 20 < addressList.length ? 20 : addressList.length - countWallet;
                            wallet.checkBalance(addressesString, Controller.this);
                        } else {
                            this.cancel();
                        }
                    }
                },0, 5000);

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
    public void balanceByAddressCallBack(int errorCode, ArrayList<Wallet> wallet) {

        if (wallet != null) {
            updateCountingLabel(countWallet);
        }

        updateBalanceCol(wallet);

    }

    private void updateBalanceCol(ArrayList<Wallet> walletList) {
//        String address;
//        String balance;
//
//        if (wallet == null) {
//            address = addressList[countWallet];
//            balance = "Error!";
//        } else {
//            address = wallet.getAddress() != null ? wallet.getAddress():"Error!";
//            balance = wallet.getETH().getBalance()!= null ? String.valueOf(wallet.getETH().getBalance()):"Error!";
//        }

//        data.add(walletList);
        if (walletList == null) {
            walletList = new ArrayList<>();
            Wallet errorWallet = new Wallet();
            errorWallet.setAccount("Error!");
            walletList.add(errorWallet);
        }

        for (int i = 0; i < walletList.size(); i++) {
            walletList.get(i).setSerial( ( countWallet - (walletList.size()-i) ) + 1 );
        }

        data.addAll(walletList);
        tbvResults.setItems(data);
    }

}
