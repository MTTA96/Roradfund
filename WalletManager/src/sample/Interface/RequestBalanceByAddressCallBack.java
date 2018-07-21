package sample.Interface;

import sample.Model.EtherScan.WalletEtherScan;
import sample.Model.Wallet;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface RequestBalanceByAddressCallBack {

    void balanceByAddressCallBack(int errorCode, ArrayList<WalletEtherScan> walletList);

}
