package sample.Model;

import org.bouncycastle.util.encoders.Hex;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class ETHTrader {

    private Web3j web3j;
    private BigInteger gasLimit;

    public void ETHTrader() {

        // Create client
        web3j = Web3j.build(new HttpService());  // defaults to http://localhost:8545/

        // Display client version
        web3j.web3ClientVersion().observable().subscribe(x -> {
            String clientVersion = x.getWeb3ClientVersion();
        });

    }

    // Sending arbitrary token

    public void sendToken(String address, BigInteger gasPrice, String toAddress, BigInteger value) {
        Credentials credentials = null;
        try {

            credentials = WalletUtils.loadCredentials("password", "/path/to/walletfile");

            // get the next available nonce
            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).sendAsync().get();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();

            // create our transaction
            RawTransaction rawTransaction  = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, toAddress, value);

            // sign & send our transaction
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Hex.toHexString(signedMessage);
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


}