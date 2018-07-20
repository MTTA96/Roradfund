package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import sample.Interface.RequestBalanceByAddressCallBack;
import sample.Model.Wallet;
import sample.Util.SupportKeys;

import java.util.ArrayList;

public class Controller implements RequestBalanceByAddressCallBack {

    @FXML
    private TextArea txtaAddresses;
    @FXML
    private Button check;
    @FXML
    private TableView<?> tbvResults;

    private String[] addressList;

    @FXML
    void checkBalance(ActionEvent event) {
        //0x3750fC1505ba9a4cA3907b94Cda8e5758d31F3aD

        Wallet wallet = new Wallet();
        if (!txtaAddresses.getText().isEmpty()) {
            addressList = txtaAddresses.getText().split("\n");
            for (String address:addressList) {

                wallet.checkBalance(address, this);
            }
//            wallet.checkBalance(txtaAddresses.getText(), this);
        }

    }

    @Override
    public void balanceByAddressCallBack(int errorCode, String balance) {
        txtaAddresses.setText(errorCode == SupportKeys.SUCCESS_CODE ? txtaAddresses.getText() + "\n" + balance:"Failed");

//        tbvResults.getColumns().addAll();
    }

}
