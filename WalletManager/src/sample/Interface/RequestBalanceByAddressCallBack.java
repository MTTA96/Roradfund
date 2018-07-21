package sample.Interface;

import sample.Model.Wallet;

public interface RequestBalanceByAddressCallBack {

    void balanceByAddressCallBack(int errorCode, Wallet wallet);

}
