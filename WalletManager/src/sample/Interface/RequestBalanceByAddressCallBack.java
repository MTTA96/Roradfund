package sample.Interface;

import sample.Model.ETHScanner.WalletScan;

import java.util.ArrayList;

public interface RequestBalanceByAddressCallBack {

    void balanceByAddressCallBack(int errorCode, ArrayList<WalletScan> walletScanList);

}
