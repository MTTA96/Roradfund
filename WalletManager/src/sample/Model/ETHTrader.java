package sample.Model;

import org.bouncycastle.util.encoders.Hex;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import sample.Interface.SendETHCallBack;
import sample.Util.SupportKeys;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class ETHTrader {

    private Web3j web3j;
    private BigInteger gasLimit;

    public ETHTrader() {

        // Create client
        web3j = Web3j.build(new HttpService("https://ropsten.infura.io/v3/1a50fe4abf44469ea44c05bd38161534"));  // defaults to http://localhost:8545/

        // Display client version
        web3j.web3ClientVersion().observable().subscribe(x -> {
            String clientVersion = x.getWeb3ClientVersion();
        });

    }

    // Sending arbitrary token

    public void sendToken(String address, BigInteger gasPrice, String toAddress, BigInteger value) {


        try {
            Credentials credentials = WalletUtils.loadCredentials("password", "/path/to/walletfile");
            TransactionReceipt transactionReceipt = Transfer.sendFunds(
                    web3j,
                    credentials,
                    "0x<address>|<ensName>",
                    BigDecimal.valueOf(1.0),
                    Convert.Unit.ETHER).send();
        } catch (Exception e) {
            e.printStackTrace();
        }


//        Credentials credentials = null;
//        try {
//
//            credentials = WalletUtils.loadCredentials("password", "/path/to/walletfile");
//
//            // get the next available nonce
//            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).sendAsync().get();
//            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
//
//            // create our transaction
//            RawTransaction rawTransaction  = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, toAddress, value);
//
//            // sign & send our transaction
//            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
//            String hexValue = Hex.toHexString(signedMessage);
//            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (CipherException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

    }

    public void sendETH(String password, String filePath, String toAddress, Double value, SendETHCallBack sendETHCallBack) {

        try {
            Credentials credentials = WalletUtils.loadCredentials(password, filePath);
//            TransactionReceipt transactionReceipt = Transfer.sendFunds(
//                    web3j,
//                    credentials,
//                    toAddress,
//                    BigDecimal.valueOf(value),
//                    Convert.Unit.ETHER).send();
            sendETHCallBack.sendETHResult(SupportKeys.SUCCESS_CODE, "Success");
        } catch (Exception e) {
            e.printStackTrace();
            sendETHCallBack.sendETHResult(SupportKeys.FAILED_CODE, e.getLocalizedMessage());
        }

    }

}
