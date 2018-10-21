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

    public static ETHTrader instance = null;
    public Web3j web3j;
    private BigInteger gasLimit;

    public static ETHTrader getInstance() {

        if(instance == null) {
            instance = new ETHTrader();
        }

        return instance;
    }

    public ETHTrader() {

        // Create client
        // Dev: https://ropsten.infura.io/v3/1a50fe4abf44469ea44c05bd38161534
        //              rinkeby
        // Live: https://mainnet.infura.io/v3/1a50fe4abf44469ea44c05bd38161534
        web3j = Web3j.build(new HttpService("https://rinkeby.infura.io/v3/1a50fe4abf44469ea44c05bd38161534"));  // defaults to http://localhost:8545/

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

    public void sendETH(String fromAddress, String password, String filePath, String toAddress, BigInteger gasPrice, BigInteger gasLimit, BigInteger value, SendETHCallBack sendETHCallBack) {

        try {

//            Credentials credentials = WalletUtils.loadCredentials(password, filePath);
//
//            /// 1: Get the next available nonce
//
//            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
//            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
//
//            /// 2: Create our transaction
//            System.out.println("Gas price: "
//                    + gasPrice // Gas * price + value
//                    + " - value: "
//                    + value); // Main balance
//            RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce,
//                    gasPrice,
//                    gasLimit,
//                    toAddress,
//                    value);
//
//            /// 3: Sign & send our transaction
//
//            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
//            String hexValue = "0x" + Hex.toHexString(signedMessage);
//            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
//
//            /// 4: Handle result
//
//            if(ethSendTransaction.getError() != null) {
//                System.out.println(ethSendTransaction.getError().getMessage());
//                sendETHCallBack.sendETHResult(SupportKeys.FAILED_CODE, ethSendTransaction.getError().getMessage());
//            } else {
//                sendETHCallBack.sendETHResult(SupportKeys.SUCCESS_CODE, "Success");
//            }

            sendETHCallBack.sendETHResult(SupportKeys.SUCCESS_CODE, "Success");
        } catch (Exception e) {
            e.printStackTrace();
            sendETHCallBack.sendETHResult(SupportKeys.FAILED_CODE, e.getLocalizedMessage());
        }

    }

    /// Using private key

    public void sendETH(String fromAddres, String privateKey, String toAddress, BigInteger gasPrice, BigInteger gasLimit, BigInteger value, SendETHCallBack sendETHCallBack) {

        try {
            Credentials credentials = Credentials.create(privateKey);

            // get the next available nonce
            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(fromAddres, DefaultBlockParameterName.LATEST).sendAsync().get();

            BigInteger nonce = ethGetTransactionCount.getTransactionCount();

            // create our transaction
            RawTransaction rawTransaction  = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, toAddress, value);

            // sign & send our transaction
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = "0x" + Hex.toHexString(signedMessage);
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();

            if(ethSendTransaction.getError().getCode() != 0) {
                System.out.println(ethSendTransaction.getError().getMessage());
                throw new Exception(ethSendTransaction.getError().getMessage());
//                sendETHCallBack.sendETHResult(SupportKeys.FAILED_CODE, ethSendTransaction.getError().getMessage());
            } else {
                sendETHCallBack.sendETHResult(SupportKeys.SUCCESS_CODE, "Success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendETHCallBack.sendETHResult(SupportKeys.FAILED_CODE, e.getLocalizedMessage());
        }

    }

}
