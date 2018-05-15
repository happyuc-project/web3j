package org.happyuc.webuj.contracts.token;

import org.happyuc.webuj.protocol.core.DefaultBlockParameter;
import org.happyuc.webuj.protocol.core.RemoteCall;
import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;
import rx.Observable;

import java.math.BigInteger;
import java.util.List;

/**
 * The HappyUC ERC-20 token standard.
 * <p>
 * Implementations should provide the concrete <code>ApprovalEr</code> and
 * <code>TransferEr</code> from their token as the generic types "T" amd "T".
 * </p>
 *
 * @see <a href="https://github.com/happyuc-project/EIPs/blob/master/EIPS/eip-20-token-standard.md">EIPs/EIPS/eip-20-token-standard.md</a>
 * @see <a href="https://github.com/happyuc-project/EIPs/issues/20">ERC: Token standard #20</a>
 */
@SuppressWarnings("unused")
public interface ERC20Interface extends ERC20BasicInterface {

    RemoteCall<BigInteger> allowance(String owner, String spender);

    RemoteCall<RepTransactionReceipt> approve(String spender, BigInteger value);

    RemoteCall<RepTransactionReceipt> transferFrom(String from, String to, BigInteger value);

    <T> List<T> getApprovalEvents(RepTransactionReceipt repTransactionReceipt, EventResponse.Rec<T> rec);

    <T> Observable<T> approvalEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock, EventResponse.Rec<T> rec);
}
