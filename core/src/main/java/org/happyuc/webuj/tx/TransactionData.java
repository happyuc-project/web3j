package org.happyuc.webuj.tx;

import java.math.BigInteger;

public class TransactionData {

    private String to;
    private String data;
    private BigInteger value;
    private BigInteger gasPrice;
    private BigInteger gasLimit;
    private BigInteger nonce;

    public TransactionData(String to, String data, BigInteger value, BigInteger gasPrice, BigInteger gasLimit, BigInteger nonce) {
        this.to = to;
        this.data = data;
        this.value = value;
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
        this.nonce = nonce;
    }

    public TransactionData(String to, String data, BigInteger value, BigInteger gasPrice, BigInteger gasLimit) {
        this.to = to;
        this.data = data;
        this.value = value;
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
    }


    public BigInteger getNonce() {
        return nonce;
    }

    public BigInteger getGasPrice() {
        return gasPrice;
    }

    public BigInteger getGasLimit() {
        return gasLimit;
    }

    public String getTo() {
        return to;
    }

    public BigInteger getValue() {
        return value;
    }

    public String getData() {
        return data;
    }

}
