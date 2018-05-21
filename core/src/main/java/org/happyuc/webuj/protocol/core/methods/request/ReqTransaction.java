package org.happyuc.webuj.protocol.core.methods.request;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.happyuc.webuj.utils.Numeric;

/**
 * ReqTransaction request object used the below methods.
 * <ol>
 * <li>huc_call</li>
 * <li>huc_sendTransaction</li>
 * <li>huc_estimateGas</li>
 * </ol>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReqTransaction {
    // default as per https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_sendtransaction
    public static final BigInteger DEFAULT_GAS = BigInteger.valueOf(9000);

    private String from;
    private String to;
    private BigInteger gas;
    private BigInteger gasPrice;
    private BigInteger value;
    private String data;
    private BigInteger nonce;  // nonce field is not present on huc_call/huc_estimateGas

    public ReqTransaction(String from, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger value, String data) {
        this.from = from;
        this.to = to;
        this.gas = gasLimit;
        this.gasPrice = gasPrice;
        this.value = value;

        if (data != null) {
            this.data = Numeric.prependHexPrefix(data);
        }

        this.nonce = nonce;
    }

    public static ReqTransaction createContractTransaction(String from, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, BigInteger value, String init) {

        return new ReqTransaction(from, nonce, gasPrice, gasLimit, null, value, init);
    }

    public static ReqTransaction createContractTransaction(String from, BigInteger nonce, BigInteger gasPrice, String init) {

        return createContractTransaction(from, nonce, gasPrice, null, null, init);
    }

    public static ReqTransaction createHucTransaction(String from, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger value) {

        return new ReqTransaction(from, nonce, gasPrice, gasLimit, to, value, null);
    }

    public static ReqTransaction createFunctionCallTransaction(String from, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger value, String data) {

        return new ReqTransaction(from, nonce, gasPrice, gasLimit, to, value, data);
    }

    public static ReqTransaction createFunctionCallTransaction(String from, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, String data) {

        return new ReqTransaction(from, nonce, gasPrice, gasLimit, to, null, data);
    }

    public static ReqTransaction createHucCallTransaction(String from, String to, String data) {

        return new ReqTransaction(from, null, null, null, to, null, data);
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getGas() {
        return convert(gas);
    }

    public String getGasPrice() {
        return convert(gasPrice);
    }

    public String getValue() {
        return convert(value);
    }

    public String getData() {
        return data;
    }

    public String getNonce() {
        return convert(nonce);
    }

    private static String convert(BigInteger value) {
        if (value != null) {
            return Numeric.encodeQuantity(value);
        } else {
            return null;  // we don't want the field to be encoded if not present
        }
    }
}
