package org.happyuc.webuj.contracts.token;

import java.math.BigInteger;
import java.util.List;

import org.happyuc.webuj.protocol.core.DefaultBlockParameter;
import org.happyuc.webuj.protocol.core.RemoteCall;
import org.happyuc.webuj.protocol.core.methods.response.TransactionReceipt;

import rx.Observable;

/**
 * Describes the HappyUC "Basic" subset of the ERC-20 token standard.
 * <p>
 * Implementations should provide the concrete <code>TransferEventResponse</code>
 * from their token as the generic type "T".
 * </p>
 *
 * @see <a href="https://github.com/happyuc-project/EIPs/issues/179">ERC: Simpler Token Standard #179</a>
 * @see <a href="https://github.com/OpenZeppelin/zeppelin-solidity/blob/master/contracts/token/ERC20Basic.sol">OpenZeppelin's zeppelin-solidity reference implementation</a>
 */
@SuppressWarnings("unused")
public interface ERC20BasicInterface<T> {

    RemoteCall<BigInteger> totalSupply();

    RemoteCall<BigInteger> balanceOf(String who);

    RemoteCall<TransactionReceipt> transfer(String to, BigInteger value);

    List<T> getTransferEvents(TransactionReceipt transactionReceipt);

    Observable<T> transferEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock);

}
