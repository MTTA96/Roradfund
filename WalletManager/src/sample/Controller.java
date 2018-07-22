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
    private Button btnCheck;
    @FXML
    private TableView<Wallet> tbvResults;
    @FXML
    private TableColumn<Wallet, String> addressCol, balanceCol;
    @FXML
    private TableColumn<Wallet, Integer> serialCol;

    private final ObservableList<Wallet> data = FXCollections.observableArrayList();
    private String[] addressList;
    private int countWallet = 0;
    private int showedWallets = 0;
    private Timer timer = new Timer();
    private boolean isChecking = false;
    private boolean isStopped = false;

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

        if (!isChecking)
            start();
        else
            stop();

    }

    @FXML
    void delete(ActionEvent event) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                timer.cancel();
            }
        });
        txtaAddresses.clear();
        btnCheck.setText("Bắt đầu");
        lblCountedWallet.setText("Kết quả");
        isChecking = false;
        isStopped = true;
        addressList = null;
        countWallet = 0;
        showedWallets = 0;
        data.clear();
        tbvResults.setItems(data);
    }

    private void start() {

        // Prepare data

        if (!txtaAddresses.getText().isEmpty()) {


            System.out.print("Start\n");

            isChecking = true;
            isStopped = false;
            countWallet = 0;
            data.clear();
            tbvResults.setItems(data);
            btnCheck.setText("Dừng");
            
            configColumns();

            addressList = txtaAddresses.getText().split("\n");

            updateCountingLabel(countWallet);

            callAPICheckBalance();

        }

    }

    private void stop() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                btnCheck.setText(isStopped ? "Dừng" : "Tiếp tục");

                if (!isStopped) {
                    System.out.print("Stop\n");
                    timer.cancel();
                    timer = null;
                }
                else {
                    System.out.print("Continue\n");
                    callAPICheckBalance();
                }

                isStopped = !isStopped;
            }
        });

    }

    private void callAPICheckBalance() {

        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                // If reach the last wallet then cancel request

                if (countWallet < addressList.length) {

                    // Format address list

                    String addressesString = "";

                    int calculatedCounting = (countWallet + 20 < addressList.length ? countWallet + 20 : countWallet + (addressList.length - countWallet));

                    for (int i = countWallet; i < calculatedCounting; i++) {
                        if (i == calculatedCounting - 1)
                            addressesString += addressList[i];
                        else
                            addressesString += addressList[i] + ",";
                    }

                    showedWallets = countWallet;
                    countWallet += countWallet + 20 < addressList.length ? 20 : addressList.length - countWallet;

                    // Call API
                    Wallet.checkBalance(addressesString, Controller.this);

                } else {
                    this.cancel();
                }

            }
        },0, 5000);

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

        if (isStopped) {
            System.out.print("Stop plzzz");
            countWallet = showedWallets;
            return;
        }

        if (wallet != null) {
            updateCountingLabel(countWallet);
        }

        updateBalanceCol(wallet);

    }

    private void updateBalanceCol(ArrayList<Wallet> walletList) {

        if (walletList == null) {
            walletList = new ArrayList<>();
            Wallet errorWallet = new Wallet();
            errorWallet.setAccount("Error!");
            walletList.add(errorWallet);
        }

        for (int i = 0; i < walletList.size(); i++) {
            walletList.get(i).setSerial( ( countWallet - (walletList.size()-i) ) + 1 );
        }

        // Handle when finish checking

        if (countWallet == addressList.length) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    System.out.print("Finish\n");
                    btnCheck.setText("Bắt đầu");
                }
            });
            isChecking = false;
            isStopped = true;
        }

        data.addAll(walletList);
        tbvResults.setItems(data);

    }

}
