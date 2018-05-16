package org.happyuc.webuj.contracts.token;

import org.happyuc.webuj.protocol.core.DefaultBlockParameter;
import org.happyuc.webuj.protocol.core.RemoteCall;
import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;
import org.happyuc.webuj.utils.Convert;
import rx.Observable;

import java.math.BigInteger;
import java.util.List;

/**
 * Describes the HappyUC "Basic" subset of the ERC-20 token standard.
 * <p>
 * Implementations should provide the concrete <code>TransferEr</code>
 * from their token as the generic type "T".
 * </p>
 *
 * @see <a href="https://github.com/happyuc-project/EIPs/issues/179">ERC: Simpler Token Standard #179</a>
 * @see <a href="https://github.com/OpenZeppelin/zeppelin-solidity/blob/master/contracts/token/ERC20Basic.sol">OpenZeppelin's zeppelin-solidity reference implementation</a>
 */
@SuppressWarnings("unused")
public interface ERC20BasicInterface {

    RemoteCall<BigInteger> totalSupply();

    RemoteCall<BigInteger> balanceOf(String who);

    RemoteCall<RepTransactionReceipt> transfer(String to, BigInteger value, Convert.Unit unit, String remark);

    <T> List<T> getTransferEvents(RepTransactionReceipt repTransactionReceipt, EventResponse.Rec<T> rec);

    <T> Observable<T> transferEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock, EventResponse.Rec<T> rec);
}
