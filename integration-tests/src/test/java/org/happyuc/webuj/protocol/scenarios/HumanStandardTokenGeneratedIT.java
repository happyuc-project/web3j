package org.happyuc.webuj.protocol.scenarios;

import org.happyuc.webuj.generated.HumanStandardToken;
import org.happyuc.webuj.protocol.core.DefaultBlockParameterName;
import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;
import org.junit.Test;
import rx.Subscription;
import rx.functions.Action1;

import java.math.BigInteger;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.happyuc.webuj.generated.HumanStandardToken.deploy;
import static org.happyuc.webuj.tx.TransactionManager.DEFAULT_POLLING_FREQUENCY;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Generated HumanStandardToken integration test for all supported scenarios.
 */
public class HumanStandardTokenGeneratedIT extends Scenario {

    @Test
    public void testContract() throws Exception {
        BigInteger aliceQty = BigInteger.valueOf(1000000);
        final String aliceAddress = ALICE.getAddress();
        final String bobAddress = BOB.getAddress();

        HumanStandardToken contract = deploy(webuj, ALICE, GAS_PRICE, GAS_LIMIT, aliceQty, "webuj tokens", BigInteger.valueOf(18), "w3j$").send();

        assertTrue(contract.isValid());

        assertThat(contract.totalSupply().send(), equalTo(aliceQty));

        assertThat(contract.balanceOf(ALICE.getAddress()).send(), equalTo(aliceQty));

        // CHECKSTYLE:OFF
        final CountDownLatch transferEventCountDownLatch = new CountDownLatch(2);
        Subscription transferEventSubscription = contract.transferEventObservable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST).subscribe(new Action1<HumanStandardToken.TransferEventResponse>() {
            @Override
            public void call(HumanStandardToken.TransferEventResponse transferEventResponse) {
                transferEventCountDownLatch.countDown();
            }
        });

        final CountDownLatch approvalEventCountDownLatch = new CountDownLatch(1);
        Subscription approvalEventSubscription = contract.approvalEventObservable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST).subscribe(new Action1<HumanStandardToken.ApprovalEventResponse>() {
            @Override
            public void call(HumanStandardToken.ApprovalEventResponse transferEventResponse) {
                transferEventCountDownLatch.countDown();
            }
        });
        // CHECKSTYLE:ON

        // transfer tokens
        BigInteger transferQuantity = BigInteger.valueOf(100_000);

        RepTransactionReceipt aliceTransferReceipt = contract.transfer(BOB.getAddress(), transferQuantity).send();

        HumanStandardToken.TransferEventResponse aliceTransferEventValues = contract.getTransferEvents(aliceTransferReceipt).get(0);

        assertThat(aliceTransferEventValues._from, equalTo(aliceAddress));
        assertThat(aliceTransferEventValues._to, equalTo(bobAddress));
        assertThat(aliceTransferEventValues._value, equalTo(transferQuantity));

        aliceQty = aliceQty.subtract(transferQuantity);

        BigInteger bobQty = BigInteger.ZERO;
        bobQty = bobQty.add(transferQuantity);

        assertThat(contract.balanceOf(ALICE.getAddress()).send(), equalTo(aliceQty));
        assertThat(contract.balanceOf(BOB.getAddress()).send(), equalTo(bobQty));

        // set an allowance
        assertThat(contract.allowance(aliceAddress, bobAddress).send(), equalTo(BigInteger.ZERO));

        transferQuantity = BigInteger.valueOf(50);
        RepTransactionReceipt approveReceipt = contract.approve(BOB.getAddress(), transferQuantity).send();

        HumanStandardToken.ApprovalEventResponse approvalEventValues = contract.getApprovalEvents(approveReceipt).get(0);

        assertThat(approvalEventValues._owner, equalTo(aliceAddress));
        assertThat(approvalEventValues._spender, equalTo(bobAddress));
        assertThat(approvalEventValues._value, equalTo(transferQuantity));

        assertThat(contract.allowance(aliceAddress, bobAddress).send(), equalTo(transferQuantity));

        // perform a transfer as Bob
        transferQuantity = BigInteger.valueOf(25);

        // Bob requires his own contract instance
        HumanStandardToken bobsContract = HumanStandardToken.load(contract.getContractAddress(), webuj, BOB, GAS_PRICE, GAS_LIMIT);

        RepTransactionReceipt bobTransferReceipt = bobsContract.transferFrom(aliceAddress, bobAddress, transferQuantity).send();

        HumanStandardToken.TransferEventResponse bobTransferEventValues = contract.getTransferEvents(bobTransferReceipt).get(0);
        assertThat(bobTransferEventValues._from, equalTo(aliceAddress));
        assertThat(bobTransferEventValues._to, equalTo(bobAddress));
        assertThat(bobTransferEventValues._value, equalTo(transferQuantity));

        aliceQty = aliceQty.subtract(transferQuantity);
        bobQty = bobQty.add(transferQuantity);

        assertThat(contract.balanceOf(aliceAddress).send(), equalTo(aliceQty));
        assertThat(contract.balanceOf(bobAddress).send(), equalTo(bobQty));

        transferEventCountDownLatch.await(DEFAULT_POLLING_FREQUENCY, TimeUnit.MILLISECONDS);
        approvalEventCountDownLatch.await(DEFAULT_POLLING_FREQUENCY, TimeUnit.MILLISECONDS);

        approvalEventSubscription.unsubscribe();
        transferEventSubscription.unsubscribe();
        Thread.sleep(1000);
        assertTrue(approvalEventSubscription.isUnsubscribed());
        assertTrue(transferEventSubscription.isUnsubscribed());
    }
}
