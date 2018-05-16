package org.happyuc.webuj.protocol.rx;

import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.DefaultBlockParameter;
import org.happyuc.webuj.protocol.core.DefaultBlockParameterName;
import org.happyuc.webuj.protocol.core.DefaultBlockParameterNumber;
import org.happyuc.webuj.protocol.core.filters.BlockFilter;
import org.happyuc.webuj.protocol.core.filters.LogFilter;
import org.happyuc.webuj.protocol.core.filters.PendingTransactionFilter;
import org.happyuc.webuj.protocol.core.methods.request.HucReqFilter;
import org.happyuc.webuj.protocol.core.methods.response.HucBlock;
import org.happyuc.webuj.protocol.core.methods.response.Log;
import org.happyuc.webuj.protocol.core.methods.response.RepTransaction;
import org.happyuc.webuj.utils.Observables;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

/**
 * webuj reactive API implementation.
 */
public class JsonRpc2_0Rx {

    private final Webuj webuj;
    private final ScheduledExecutorService scheduledExecutorService;
    private final Scheduler scheduler;

    public JsonRpc2_0Rx(Webuj webuj, ScheduledExecutorService scheduledExecutorService) {
        this.webuj = webuj;
        this.scheduledExecutorService = scheduledExecutorService;
        this.scheduler = Schedulers.from(scheduledExecutorService);
    }

    public Observable<String> hucBlockHashObservable(long pollingInterval) {
        return Observable.create(subscriber -> {
            BlockFilter blockFilter = new BlockFilter(webuj, subscriber::onNext);
            run(blockFilter, subscriber, pollingInterval);
        });
    }

    public Observable<String> hucPendingTransactionHashObservable(long pollingInterval) {
        return Observable.create(subscriber -> {
            PendingTransactionFilter pendingTransactionFilter = new PendingTransactionFilter(webuj, subscriber::onNext);

            run(pendingTransactionFilter, subscriber, pollingInterval);
        });
    }

    public Observable<Log> hucLogObservable(HucReqFilter hucReqFilter, long pollingInterval) {
        return Observable.create((Subscriber<? super Log> subscriber) -> {
            LogFilter logFilter = new LogFilter(webuj, subscriber::onNext, hucReqFilter);

            run(logFilter, subscriber, pollingInterval);
        });
    }

    private <T> void run(org.happyuc.webuj.protocol.core.filters.Filter<T> filter, Subscriber<? super T> subscriber, long pollingInterval) {

        filter.run(scheduledExecutorService, pollingInterval);
        subscriber.add(Subscriptions.create(filter::cancel));
    }

    public Observable<RepTransaction> transactionObservable(long pollingInterval) {
        return blockObservable(true, pollingInterval).flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    public Observable<RepTransaction> pendingTransactionObservable(long pollingInterval) {
        return hucPendingTransactionHashObservable(pollingInterval).flatMap(transactionHash -> webuj.hucGetTransactionByHash(transactionHash).observable()).filter(hucTransaction -> hucTransaction.getTransaction().isPresent()).map(hucTransaction -> hucTransaction.getTransaction().get());
    }

    public Observable<HucBlock> blockObservable(boolean fullTransactionObjects, long pollingInterval) {
        return hucBlockHashObservable(pollingInterval).flatMap(blockHash -> webuj.hucGetBlockByHash(blockHash, fullTransactionObjects).observable());
    }

    public Observable<HucBlock> replayBlocksObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock, boolean fullTransactionObjects) {
        return replayBlocksObservable(startBlock, endBlock, fullTransactionObjects, true);
    }

    public Observable<HucBlock> replayBlocksObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock, boolean fullTransactionObjects, boolean ascending) {
        // We use a scheduler to ensure this Observable runs asynchronously for users to be
        // consistent with the other Observables
        return replayBlocksObservableSync(startBlock, endBlock, fullTransactionObjects, ascending).subscribeOn(scheduler);
    }

    private Observable<HucBlock> replayBlocksObservableSync(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock, boolean fullTransactionObjects) {
        return replayBlocksObservableSync(startBlock, endBlock, fullTransactionObjects, true);
    }

    private Observable<HucBlock> replayBlocksObservableSync(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock, boolean fullTransactionObjects, boolean ascending) {

        BigInteger startBlockNumber = null;
        BigInteger endBlockNumber = null;
        try {
            startBlockNumber = getBlockNumber(startBlock);
            endBlockNumber = getBlockNumber(endBlock);
        } catch (IOException e) {
            Observable.error(e);
        }

        if (ascending) {
            return Observables.range(startBlockNumber, endBlockNumber).flatMap(i -> webuj.hucGetBlockByNumber(new DefaultBlockParameterNumber(i), fullTransactionObjects).observable());
        } else {
            return Observables.range(startBlockNumber, endBlockNumber, false).flatMap(i -> webuj.hucGetBlockByNumber(new DefaultBlockParameterNumber(i), fullTransactionObjects).observable());
        }
    }

    public Observable<RepTransaction> replayTransactionsObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        return replayBlocksObservable(startBlock, endBlock, true).flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    public Observable<HucBlock> catchUpToLatestBlockObservable(DefaultBlockParameter startBlock, boolean fullTransactionObjects, Observable<HucBlock> onCompleteObservable) {
        // We use a scheduler to ensure this Observable runs asynchronously for users to be
        // consistent with the other Observables
        return catchUpToLatestBlockObservableSync(startBlock, fullTransactionObjects, onCompleteObservable).subscribeOn(scheduler);
    }

    public Observable<HucBlock> catchUpToLatestBlockObservable(DefaultBlockParameter startBlock, boolean fullTransactionObjects) {
        return catchUpToLatestBlockObservable(startBlock, fullTransactionObjects, Observable.empty());
    }

    private Observable<HucBlock> catchUpToLatestBlockObservableSync(DefaultBlockParameter startBlock, boolean fullTransactionObjects, Observable<HucBlock> onCompleteObservable) {

        BigInteger startBlockNumber;
        BigInteger latestBlockNumber;
        try {
            startBlockNumber = getBlockNumber(startBlock);
            latestBlockNumber = getLatestBlockNumber();
        } catch (IOException e) {
            return Observable.error(e);
        }

        if (startBlockNumber.compareTo(latestBlockNumber) > -1) {
            return onCompleteObservable;
        } else {
            return Observable.concat(replayBlocksObservableSync(new DefaultBlockParameterNumber(startBlockNumber), new DefaultBlockParameterNumber(latestBlockNumber), fullTransactionObjects), Observable.defer(() -> catchUpToLatestBlockObservableSync(new DefaultBlockParameterNumber(latestBlockNumber.add(BigInteger.ONE)), fullTransactionObjects, onCompleteObservable)));
        }
    }

    public Observable<RepTransaction> catchUpToLatestTransactionObservable(DefaultBlockParameter startBlock) {
        return catchUpToLatestBlockObservable(startBlock, true, Observable.empty()).flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    public Observable<HucBlock> catchUpToLatestAndSubscribeToNewBlocksObservable(DefaultBlockParameter startBlock, boolean fullTransactionObjects, long pollingInterval) {

        return catchUpToLatestBlockObservable(startBlock, fullTransactionObjects, blockObservable(fullTransactionObjects, pollingInterval));
    }

    public Observable<RepTransaction> catchUpToLatestAndSubscribeToNewTransactionsObservable(DefaultBlockParameter startBlock, long pollingInterval) {
        return catchUpToLatestAndSubscribeToNewBlocksObservable(startBlock, true, pollingInterval).flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    private BigInteger getLatestBlockNumber() throws IOException {
        return getBlockNumber(DefaultBlockParameterName.LATEST);
    }

    private BigInteger getBlockNumber(DefaultBlockParameter defaultBlockParameter) throws IOException {
        if (defaultBlockParameter instanceof DefaultBlockParameterNumber) {
            return ((DefaultBlockParameterNumber) defaultBlockParameter).getBlockNumber();
        } else {
            HucBlock latestHucBlock = webuj.hucGetBlockByNumber(defaultBlockParameter, false).send();
            return latestHucBlock.getBlock().getNumber();
        }
    }

    private static List<RepTransaction> toTransactions(HucBlock hucBlock) {
        // If you ever see an exception thrown here, it's probably due to an incomplete chain in
        // Ghuc/Parity. You should resync to solve.
        return hucBlock.getBlock().getTransactions().stream().map(transactionResult -> (RepTransaction) transactionResult.get()).collect(Collectors.toList());
    }
}
