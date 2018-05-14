package org.happyuc.webuj.protocol.rx;

import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.DefaultBlockParameter;
import org.happyuc.webuj.protocol.core.DefaultBlockParameterName;
import org.happyuc.webuj.protocol.core.DefaultBlockParameterNumber;
import org.happyuc.webuj.protocol.core.filters.BlockFilter;
import org.happyuc.webuj.protocol.core.filters.Callback;
import org.happyuc.webuj.protocol.core.filters.Filter;
import org.happyuc.webuj.protocol.core.filters.LogFilter;
import org.happyuc.webuj.protocol.core.filters.PendingTransactionFilter;
import org.happyuc.webuj.protocol.core.methods.request.HucReqFilter;
import org.happyuc.webuj.protocol.core.methods.response.HucBlock;
import org.happyuc.webuj.protocol.core.methods.response.HucRepTransaction;
import org.happyuc.webuj.protocol.core.methods.response.Log;
import org.happyuc.webuj.protocol.core.methods.response.RepTransaction;
import org.happyuc.webuj.utils.Observables;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

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

    public Observable<String> hucBlockHashObservable(final long pollingInterval) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                BlockFilter blockFilter = new BlockFilter(webuj, new Callback<String>() {
                    @Override
                    public void onEvent(final String value) {
                        subscriber.onNext(value);
                    }
                });
                JsonRpc2_0Rx.this.run(blockFilter, subscriber, pollingInterval);
            }
        });
    }

    public Observable<String> hucPendingTransactionHashObservable(final long pollingInterval) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                PendingTransactionFilter pendingTransactionFilter = new PendingTransactionFilter(webuj, new Callback<String>() {
                    @Override
                    public void onEvent(final String value) {
                        subscriber.onNext(value);
                    }
                });
                JsonRpc2_0Rx.this.run(pendingTransactionFilter, subscriber, pollingInterval);
            }
        });
    }

    public Observable<Log> hucLogObservable(final HucReqFilter hucReqFilter, final long pollingInterval) {
        return Observable.create(new Observable.OnSubscribe<Log>() {
            @Override
            public void call(final Subscriber<? super Log> subscriber) {
                LogFilter logFilter = new LogFilter(webuj, new Callback<Log>() {
                    @Override
                    public void onEvent(final Log t) {
                        subscriber.onNext(t);
                    }
                }, hucReqFilter);

                JsonRpc2_0Rx.this.run(logFilter, subscriber, pollingInterval);
            }
        });
    }

    private <T> void run(final Filter<T> filter, Subscriber<? super T> subscriber, final long pollingInterval) {

        filter.run(scheduledExecutorService, pollingInterval);
        subscriber.add(Subscriptions.create(new Action0() {
            @Override
            public void call() {
                filter.cancel();
            }
        }));
    }

    public Observable<RepTransaction> transactionObservable(final long pollingInterval) {
        return blockObservable(true, pollingInterval).flatMapIterable(new Func1<HucBlock, Iterable<? extends RepTransaction>>() {
            @Override
            public Iterable<? extends RepTransaction> call(final HucBlock hucBlock) {
                return JsonRpc2_0Rx.this.toTransactions(hucBlock);
            }
        });
    }

    public Observable<RepTransaction> pendingTransactionObservable(final long pollingInterval) {
        return hucPendingTransactionHashObservable(pollingInterval).flatMap(new Func1<String, Observable<HucRepTransaction>>() {
            @Override
            public Observable<HucRepTransaction> call(final String transactionHash) {
                return webuj.hucGetTransactionByHash(transactionHash).observable();
            }
        }).map(new Func1<HucRepTransaction, RepTransaction>() {
            @Override
            public RepTransaction call(final HucRepTransaction hucRepTransaction) {
                return hucRepTransaction.getTransaction();
            }
        });
    }

    public Observable<HucBlock> blockObservable(final boolean fullTransactionObjects, final long pollingInterval) {
        return this.hucBlockHashObservable(pollingInterval).flatMap(new Func1<String, Observable<? extends HucBlock>>() {
            @Override
            public Observable<? extends HucBlock> call(final String blockHash) {
                return webuj.hucGetBlockByHash(blockHash, fullTransactionObjects).observable();
            }
        });
    }

    public Observable<HucBlock> replayBlocksObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock, boolean fullTransactionObjects) {
        return replayBlocksObservable(startBlock, endBlock, fullTransactionObjects, true);
    }

    public Observable<HucBlock> replayBlocksObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock, boolean fullTransactionObjects, boolean ascending) {
        // We use a scheduler to ensure this Observable runs asynchronously for users to be
        // consistent with the other Observables
        return replayBlocksObservableSync(startBlock, endBlock, fullTransactionObjects, ascending).subscribeOn(scheduler);
    }

    private Observable<HucBlock> replayBlocksObservableSync(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock, final boolean fullTransactionObjects) {
        return replayBlocksObservableSync(startBlock, endBlock, fullTransactionObjects, true);
    }

    private Observable<HucBlock> replayBlocksObservableSync(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock, final boolean fullTransactionObjects, boolean ascending) {

        BigInteger startBlockNumber = null;
        BigInteger endBlockNumber = null;
        try {
            startBlockNumber = getBlockNumber(startBlock);
            endBlockNumber = getBlockNumber(endBlock);
        } catch (IOException e) {
            Observable.error(e);
        }

        if (ascending) {
            return Observables.range(startBlockNumber, endBlockNumber).flatMap(new Func1<BigInteger, Observable<? extends HucBlock>>() {
                @Override
                public Observable<? extends HucBlock> call(BigInteger i) {
                    return webuj.hucGetBlockByNumber(new DefaultBlockParameterNumber(i), fullTransactionObjects).observable();
                }
            });
        } else {
            return Observables.range(startBlockNumber, endBlockNumber, false).flatMap(new Func1<BigInteger, Observable<? extends HucBlock>>() {
                @Override
                public Observable<? extends HucBlock> call(BigInteger i) {
                    return webuj.hucGetBlockByNumber(new DefaultBlockParameterNumber(i), fullTransactionObjects).observable();
                }
            });
        }


    }

    public Observable<RepTransaction> replayTransactionsObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        return replayBlocksObservable(startBlock, endBlock, true).flatMapIterable(new Func1<HucBlock, Iterable<? extends RepTransaction>>() {
            @Override
            public Iterable<? extends RepTransaction> call(HucBlock hucBlock) {
                return toTransactions(hucBlock);
            }
        });
    }

    public Observable<HucBlock> catchUpToLatestBlockObservable(DefaultBlockParameter startBlock, boolean fullTransactionObjects, Observable<HucBlock> onCompleteObservable) {
        // We use a scheduler to ensure this Observable runs asynchronously for users to be
        // consistent with the other Observables
        return catchUpToLatestBlockObservableSync(startBlock, fullTransactionObjects, onCompleteObservable).subscribeOn(scheduler);
    }

    public Observable<HucBlock> catchUpToLatestBlockObservable(DefaultBlockParameter startBlock, boolean fullTransactionObjects) {
        return catchUpToLatestBlockObservable(startBlock, fullTransactionObjects, Observable.<HucBlock>empty());
    }

    private Observable<HucBlock> catchUpToLatestBlockObservableSync(DefaultBlockParameter startBlock, final boolean fullTransactionObjects, final Observable<HucBlock> onCompleteObservable) {

        BigInteger startBlockNumber;
        final BigInteger latestBlockNumber;
        try {
            startBlockNumber = getBlockNumber(startBlock);
            latestBlockNumber = getLatestBlockNumber();
        } catch (IOException e) {
            return Observable.error(e);
        }

        if (startBlockNumber.compareTo(latestBlockNumber) > -1) {
            return onCompleteObservable;
        } else {
            return Observable.concat(replayBlocksObservableSync(new DefaultBlockParameterNumber(startBlockNumber), new DefaultBlockParameterNumber(latestBlockNumber), fullTransactionObjects), Observable.defer(new Func0<Observable<HucBlock>>() {
                @Override
                public Observable<HucBlock> call() {
                    return JsonRpc2_0Rx.this.catchUpToLatestBlockObservableSync(new DefaultBlockParameterNumber(latestBlockNumber.add(BigInteger.ONE)), fullTransactionObjects, onCompleteObservable);
                }
            }));
        }
    }

    public Observable<RepTransaction> catchUpToLatestTransactionObservable(DefaultBlockParameter startBlock) {
        return catchUpToLatestBlockObservable(startBlock, true, Observable.<HucBlock>empty()).flatMapIterable(new Func1<HucBlock, Iterable<? extends RepTransaction>>() {
            @Override
            public Iterable<? extends RepTransaction> call(HucBlock hucBlock) {
                return toTransactions(hucBlock);
            }
        });
    }

    public Observable<HucBlock> catchUpToLatestAndSubscribeToNewBlocksObservable(DefaultBlockParameter startBlock, boolean fullTransactionObjects, long pollingInterval) {

        return catchUpToLatestBlockObservable(startBlock, fullTransactionObjects, blockObservable(fullTransactionObjects, pollingInterval));
    }

    public Observable<RepTransaction> catchUpToLatestAndSubscribeToNewTransactionsObservable(DefaultBlockParameter startBlock, long pollingInterval) {
        return catchUpToLatestAndSubscribeToNewBlocksObservable(startBlock, true, pollingInterval).flatMapIterable(new Func1<HucBlock, Iterable<? extends RepTransaction>>() {
            @Override
            public Iterable<? extends RepTransaction> call(HucBlock hucBlock) {
                return toTransactions(hucBlock);
            }
        });
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
        List<HucBlock.TransactionResult> transactionResults = hucBlock.getBlock().getTransactions();
        List<RepTransaction> repTransactions = new ArrayList<RepTransaction>(transactionResults.size());

        for (HucBlock.TransactionResult transactionResult : transactionResults) {
            repTransactions.add((RepTransaction) transactionResult.get());
        }
        return repTransactions;
    }
}
