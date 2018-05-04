package org.happyuc.webuj.protocol.rx;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

import org.happyuc.webuj.protocol.webuj;
import org.happyuc.webuj.protocol.core.DefaultBlockParameter;
import org.happyuc.webuj.protocol.core.DefaultBlockParameterName;
import org.happyuc.webuj.protocol.core.DefaultBlockParameterNumber;
import org.happyuc.webuj.protocol.core.filters.BlockFilter;
import org.happyuc.webuj.protocol.core.filters.LogFilter;
import org.happyuc.webuj.protocol.core.filters.PendingTransactionFilter;
import org.happyuc.webuj.protocol.core.methods.response.HucBlock;
import org.happyuc.webuj.protocol.core.methods.response.Log;
import org.happyuc.webuj.protocol.core.methods.response.Transaction;
import org.happyuc.webuj.utils.Observables;

/**
 * webuj reactive API implementation.
 */
public class JsonRpc2_0Rx {

    private final webuj webuj;
    private final ScheduledExecutorService scheduledExecutorService;
    private final Scheduler scheduler;

    public JsonRpc2_0Rx(webuj webuj, ScheduledExecutorService scheduledExecutorService) {
        this.webuj = webuj;
        this.scheduledExecutorService = scheduledExecutorService;
        this.scheduler = Schedulers.from(scheduledExecutorService);
    }

    public Observable<String> ethBlockHashObservable(long pollingInterval) {
        return Observable.create(subscriber -> {
            BlockFilter blockFilter = new BlockFilter(
                    webuj, subscriber::onNext);
            run(blockFilter, subscriber, pollingInterval);
        });
    }

    public Observable<String> ethPendingTransactionHashObservable(long pollingInterval) {
        return Observable.create(subscriber -> {
            PendingTransactionFilter pendingTransactionFilter = new PendingTransactionFilter(
                    webuj, subscriber::onNext);

            run(pendingTransactionFilter, subscriber, pollingInterval);
        });
    }

    public Observable<Log> ethLogObservable(
            org.happyuc.webuj.protocol.core.methods.request.HucFilter ethFilter, long pollingInterval) {
        return Observable.create((Subscriber<? super Log> subscriber) -> {
            LogFilter logFilter = new LogFilter(
                    webuj, subscriber::onNext, ethFilter);

            run(logFilter, subscriber, pollingInterval);
        });
    }

    private <T> void run(
            org.happyuc.webuj.protocol.core.filters.Filter<T> filter, Subscriber<? super T> subscriber,
            long pollingInterval) {

        filter.run(scheduledExecutorService, pollingInterval);
        subscriber.add(Subscriptions.create(filter::cancel));
    }

    public Observable<Transaction> transactionObservable(long pollingInterval) {
        return blockObservable(true, pollingInterval)
                .flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    public Observable<Transaction> pendingTransactionObservable(long pollingInterval) {
        return ethPendingTransactionHashObservable(pollingInterval)
                .flatMap(transactionHash ->
                        webuj.hucGetTransactionByHash(transactionHash).observable())
                .filter(ethTransaction -> ethTransaction.getTransaction().isPresent())
                .map(ethTransaction -> ethTransaction.getTransaction().get());
    }

    public Observable<HucBlock> blockObservable(
            boolean fullTransactionObjects, long pollingInterval) {
        return ethBlockHashObservable(pollingInterval)
                .flatMap(blockHash ->
                        webuj.hucGetBlockByHash(blockHash, fullTransactionObjects).observable());
    }

    public Observable<HucBlock> replayBlocksObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects) {
        return replayBlocksObservable(startBlock, endBlock, fullTransactionObjects, true);
    }

    public Observable<HucBlock> replayBlocksObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects, boolean ascending) {
        // We use a scheduler to ensure this Observable runs asynchronously for users to be
        // consistent with the other Observables
        return replayBlocksObservableSync(startBlock, endBlock, fullTransactionObjects, ascending)
                .subscribeOn(scheduler);
    }

    private Observable<HucBlock> replayBlocksObservableSync(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects) {
        return replayBlocksObservableSync(startBlock, endBlock, fullTransactionObjects, true);
    }

    private Observable<HucBlock> replayBlocksObservableSync(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects, boolean ascending) {

        BigInteger startBlockNumber = null;
        BigInteger endBlockNumber = null;
        try {
            startBlockNumber = getBlockNumber(startBlock);
            endBlockNumber = getBlockNumber(endBlock);
        } catch (IOException e) {
            Observable.error(e);
        }

        if (ascending) {
            return Observables.range(startBlockNumber, endBlockNumber)
                    .flatMap(i -> webuj.hucGetBlockByNumber(
                            new DefaultBlockParameterNumber(i),
                            fullTransactionObjects).observable());
        } else {
            return Observables.range(startBlockNumber, endBlockNumber, false)
                    .flatMap(i -> webuj.hucGetBlockByNumber(
                            new DefaultBlockParameterNumber(i),
                            fullTransactionObjects).observable());
        }
    }

    public Observable<Transaction> replayTransactionsObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        return replayBlocksObservable(startBlock, endBlock, true)
                .flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    public Observable<HucBlock> catchUpToLatestBlockObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects,
            Observable<HucBlock> onCompleteObservable) {
        // We use a scheduler to ensure this Observable runs asynchronously for users to be
        // consistent with the other Observables
        return catchUpToLatestBlockObservableSync(
                startBlock, fullTransactionObjects, onCompleteObservable)
                .subscribeOn(scheduler);
    }

    public Observable<HucBlock> catchUpToLatestBlockObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects) {
        return catchUpToLatestBlockObservable(
                startBlock, fullTransactionObjects, Observable.empty());
    }

    private Observable<HucBlock> catchUpToLatestBlockObservableSync(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects,
            Observable<HucBlock> onCompleteObservable) {

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
            return Observable.concat(
                    replayBlocksObservableSync(
                            new DefaultBlockParameterNumber(startBlockNumber),
                            new DefaultBlockParameterNumber(latestBlockNumber),
                            fullTransactionObjects),
                    Observable.defer(() -> catchUpToLatestBlockObservableSync(
                            new DefaultBlockParameterNumber(latestBlockNumber.add(BigInteger.ONE)),
                            fullTransactionObjects,
                            onCompleteObservable)));
        }
    }

    public Observable<Transaction> catchUpToLatestTransactionObservable(
            DefaultBlockParameter startBlock) {
        return catchUpToLatestBlockObservable(
                startBlock, true, Observable.empty())
                .flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    public Observable<HucBlock> catchUpToLatestAndSubscribeToNewBlocksObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects,
            long pollingInterval) {

        return catchUpToLatestBlockObservable(
                startBlock, fullTransactionObjects,
                blockObservable(fullTransactionObjects, pollingInterval));
    }

    public Observable<Transaction> catchUpToLatestAndSubscribeToNewTransactionsObservable(
            DefaultBlockParameter startBlock, long pollingInterval) {
        return catchUpToLatestAndSubscribeToNewBlocksObservable(
                startBlock, true, pollingInterval)
                .flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    private BigInteger getLatestBlockNumber() throws IOException {
        return getBlockNumber(DefaultBlockParameterName.LATEST);
    }

    private BigInteger getBlockNumber(
            DefaultBlockParameter defaultBlockParameter) throws IOException {
        if (defaultBlockParameter instanceof DefaultBlockParameterNumber) {
            return ((DefaultBlockParameterNumber) defaultBlockParameter).getBlockNumber();
        } else {
            HucBlock latestHucBlock = webuj.hucGetBlockByNumber(
                    defaultBlockParameter, false).send();
            return latestHucBlock.getBlock().getNumber();
        }
    }

    private static List<Transaction> toTransactions(HucBlock ethBlock) {
        // If you ever see an exception thrown here, it's probably due to an incomplete chain in
        // Ghuc/Parity. You should resync to solve.
        return ethBlock.getBlock().getTransactions().stream()
                .map(transactionResult -> (Transaction) transactionResult.get())
                .collect(Collectors.toList());
    }
}
