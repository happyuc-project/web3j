Filters and Events
==================

Filters provide notifications of certain events taking place in the HappyUC network. There are
three classes of filter supported in HappyUC:

#. Block filters
#. Pending transaction filters
#. Topic filters

Block filters and pending transaction filters provide notification of the creation of new
transactions or blocks on the network.

Topic filters are more flexible. These allow you to create a filter based on specific criteria
that you provide.

Unfortunately, unless you are using a WebSocket connection to Ghuc, working with filters via the
JSON-RPC API is a tedious process, where you need to poll the HappyUC client in order to find out
if there are any updates to your filters due to the synchronous nature of HTTP and IPC requests.
Additionally the block and transaction filters only provide the transaction or block hash, so a
further request is required to obtain the actual transaction or block referred to by the hash.

Webuj's managed `Filter <https://github.com/happyuc-project/webu.java/blob/master/core/src/main/java/org/Webuj/protocol/core/filters/Filter.java>`_
implementation address these issues, so you have a fully asynchronous event based API for working
with filters. It uses `RxJava <https://github.com/ReactiveX/RxJava>`_'s
`Observables <http://reactivex.io/documentation/observable.html>`_ which provides a consistent API
for working with events, which facilitates the chaining together of JSON-RPC calls via
functional composition.

**Note:** filters are not supported on Infura.


Block and transaction filters
-----------------------------

To receive all new blocks as they are added to the blockchain (the false parameter specifies that
we only want the blocks, not the embedded transactions too)::

   Subscription subscription = Webuj.blockObservable(false).subscribe(block -> {
       ...
   });

To receive all new transactions as they are added to the blockchain::

   Subscription subscription = Webuj.transactionObservable().subscribe(tx -> {
       ...
   });

To receive all pending transactions as they are submitted to the network (i.e. before they have
been grouped into a block together)::

   Subscription subscription = Webuj.pendingTransactionObservable().subscribe(tx -> {
       ...
   });

Subscriptions should always be cancelled when no longer required via *unsubscribe*::

   subscription.unsubscribe();

Other callbacks are also provided which provide simply the block or transaction hashes,
for details of these refer to the
`webujRx <https://github.com/happyuc-project/webu.java/blob/master/core/src/main/java/org/Webuj/protocol/rx/webujRx.java>`_
interface.


Replay filters
--------------

Webuj also provides filters for replaying block and transaction history.

To replay a range of blocks from the blockchain::

   Subscription subscription = Webuj.replayBlocksObservable(
           <startBlockNumber>, <endBlockNumber>, <fullTxObjects>)
           .subscribe(block -> {
               ...
   });

To replay the individual transactions contained within a range of blocks::

   Subscription subscription = Webuj.replayTransactionsObservable(
           <startBlockNumber>, <endBlockNumber>)
           .subscribe(tx -> {
               ...
   });

You can also get Webuj to replay all blocks up to the most current, and provide notification
(via the submitted Observable) once you've caught up::

   Subscription subscription = Webuj.catchUpToLatestBlockObservable(
           <startBlockNumber>, <fullTxObjects>, <onCompleteObservable>)
           .subscribe(block -> {
               ...
   });

Or, if you'd rather replay all blocks to the most current, then be notified of new subsequent
blocks being created::

   Subscription subscription = Webuj.catchUpToLatestAndSubscribeToNewBlocksObservable(
           <startBlockNumber>, <fullTxObjects>)
           .subscribe(block -> {
               ...
   });

As above, but with transactions contained within blocks::

   Subscription subscription = Webuj.catchUpToLatestAndSubscribeToNewTransactionsObservable(
           <startBlockNumber>)
           .subscribe(tx -> {
               ...
   });

All of the above filters are exported via the
`webujRx <https://github.com/happyuc-project/webu.java/blob/master/core/src/main/java/org/Webuj/protocol/rx/webujRx.java>`_
interface.


.. _filters-and-events:

Topic filters and EVM events
----------------------------

Topic filters capture details of HappyUC Virtual Machine (EVM) events taking place in the network.
These events are created by smart contracts and stored in the transaction log associated with a
smart contract.

The `Solidity documentation <http://solidity.readthedocs.io/en/develop/contracts.html#events>`_
provides a good overview of EVM events.

You use the
`HucFilter <https://github.com/happyuc-project/webu.java/blob/master/core/src/main/java/org/Webuj/protocol/core/methods/request/HucFilter.java>`_
type to specify the topics that you wish to apply to the filter. This can include the address of
the smart contract you wish to apply the filter to. You can also provide specific topics to filter
on. Where the individual topics represent indexed parameters on the smart contract::

   HucFilter filter = new HucFilter(DefaultBlockParameterName.EARLIEST,
           DefaultBlockParameterName.LATEST, <contract-address>)
                [.addSingleTopic(...) | .addOptionalTopics(..., ...) | ...];

This filter can then be created using a similar syntax to the block and transaction filters above::

   Webuj.hucLogObservable(filter).subscribe(log -> {
       ...
   });

The filter topics can only refer to the indexed Solidity event parameters. It is not possible to
filter on the non-indexed event parameters. Additionally, for any indexed event parameters that are
variable length array types such as string and bytes, the Keccak-256 hash of their value is stored
on the EVM log. It is not possible to store or filter using their full value.

If you create a filter instance with no topics associated with it, all EVM events taking place in
the network will be captured by the filter.


A note on functional composition
--------------------------------

In addition to *send()* and *sendAsync*, all JSON-RPC method implementations in Webuj support the
*observable()* method to create an Observable to execute the request asynchronously. This makes it
very straight forwards to compose JSON-RPC calls together into new functions.

For instance, the
`blockObservable <https://github.com/happyuc-project/webu.java/blob/master/core/src/main/java/org/Webuj/protocol/rx/JsonRpc2_0Rx.java>`_ is
itself composed of a number of separate JSON-RPC calls::

   public Observable<HucBlock> blockObservable(
           boolean fullTransactionObjects, long pollingInterval) {
       return this.hucBlockHashObservable(pollingInterval)
               .flatMap(blockHash ->
                       Webuj.hucGetBlockByHash(blockHash, fullTransactionObjects).observable());
   }

Here we first create an observable that provides notifications of the block hash of each newly
created block. We then use *flatMap* to invoke a call to *hucGetBlockByHash* to obtain the full
block details which is what is passed to the subscriber of the observable.


Further examples
----------------

Please refer to the integration test
`ObservableIT <https://github.com/happyuc-project/webu.java/blob/master/integration-tests/src/test/java/org/Webuj/protocol/core/ObservableIT.java>`_
for further examples.

For a demonstration of using the manual filter API, you can take a look at the test
`EventFilterIT <https://github.com/happyuc-project/webu.java/blob/master/integration-tests/src/test/java/org/Webuj/protocol/scenarios/EventFilterIT.java>`_..
