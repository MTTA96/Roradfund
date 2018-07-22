package sample.Interface;

import sample.Model.Wallet;

import java.util.ArrayList;

public interface RequestBalanceByAddressCallBack {

    void balanceByAddressCallBack(int errorCode, ArrayList<Wallet> walletList);

}
