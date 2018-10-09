package sample.Interface;

import sample.Model.ETHScanner.WalletScan;
import sample.Model.Ethplorer.WalletETHplorer;

import java.util.ArrayList;

public interface RequestWalletEthplorerInfoCallBack {

    void walletInfoCallBack(int errorCode, String msg, WalletETHplorer wallet);

}
