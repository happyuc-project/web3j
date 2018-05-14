Getting Started
===============

Add the latest webuj version to your project build configuration.

Maven
-----

Java 8:

.. code-block:: xml

   <dependency>
     <groupId>org.webuj</groupId>
     <artifactId>core</artifactId>
     <version>3.2.0</version>
   </dependency>

Android:

.. code-block:: xml

   <dependency>
     <groupId>org.webuj</groupId>
     <artifactId>core</artifactId>
     <version>3.1.1-android</version>
   </dependency>

Gradle
------

Java 8:

.. code-block:: groovy

   compile ('org.webuj:core:3.2.0')

Android:

.. code-block:: groovy

   compile ('org.webuj:core:3.1.1-android')


Start a client
--------------

Start up an Ethereum client if you don't already have one running, such as
`Geth <https://github.com/ethereum/go-ethereum/wiki/geth>`_:

.. code-block:: bash

   $ geth --rpcapi personal,db,eth,net,web3 --rpc --rinkeby

Or `Parity <https://github.com/paritytech/parity>`_:

.. code-block:: bash

   $ parity --chain testnet

Or use `Infura <https://infura.io/>`_, which provides **free clients** running in the cloud:

.. code-block:: java

   Web3j web3 = Web3j.build(new HttpService("https://morden.infura.io/your-token"));

For further information refer to :doc:`infura`.

Instructions on obtaining Ether to transact on the network can be found in the
:ref:`testnet section of the docs <ethereum-testnets>`.


Start sending requests
----------------------

To send synchronous requests::

   Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
   Web3ClientVersion webuClientVersion = web3.webuClientVersion().send();
   String clientVersion = webuClientVersion.getWeb3ClientVersion();

To send asynchronous requests using a CompletableFuture (Future on Android)::

   Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
   Web3ClientVersion webuClientVersion = web3.webuClientVersion().sendAsync().get();
   String clientVersion = webuClientVersion.getWeb3ClientVersion();

To use an RxJava Observable::

   Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
   web3.webuClientVersion().observable().subscribe(x -> {
       String clientVersion = x.getWeb3ClientVersion();
       ...
   });

**Note:** for Android use::

   Web3j web3 = Web3jFactory.build(new HttpService());  // defaults to http://localhost:8545/
   ...


IPC
---

webuj also supports fast inter-process communication (IPC) via file sockets to clients running on
the same host as webuj. To connect simply use the relevant *IpcService* implementation instead of
*HttpService* when you create your service:

.. code-block:: java

   // OS X/Linux/Unix:
   Web3j web3 = Web3j.build(new UnixIpcService("/path/to/socketfile"));
   ...

   // Windows
   Web3j web3 = Web3j.build(new WindowsIpcService("/path/to/namedpipefile"));
   ...

**Note:** IPC is not available on *webuj-android*.


.. _smart-contract-wrappers-summary:

Working with smart contracts with Java smart contract wrappers
--------------------------------------------------------------

webuj can auto-generate smart contract wrapper code to deploy and interact with smart contracts
without leaving the JVM.

To generate the wrapper code, compile your smart contract:

.. code-block:: bash

   $ solc <contract>.sol --bin --abi --optimize -o <output-dir>/

Then generate the wrapper code using webuj's :doc:`command_line`:

.. code-block:: bash

   webuj solidity generate /path/to/<smart-contract>.bin /path/to/<smart-contract>.abi -o /path/to/src/main/java -p com.your.organisation.name

Now you can create and deploy your smart contract::

   Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
   Credentials credentials = WalletUtils.loadCredentials("password", "/path/to/walletfile");

   YourSmartContract contract = YourSmartContract.deploy(
           <webuj>, <credentials>,
           GAS_PRICE, GAS_LIMIT,
           <param1>, ..., <paramN>).send();  // constructor params

Or use an existing contract::

   YourSmartContract contract = YourSmartContract.load(
           "0x<address>|<ensName>", <webuj>, <credentials>, GAS_PRICE, GAS_LIMIT);

To transact with a smart contract::

   TransactionReceipt repTransactionReceipt = contract.someMethod(
                <param1>,
                ...).send();

To call a smart contract::

   Type result = contract.someMethod(<param1>, ...).send();

For more information refer to :ref:`smart-contract-wrappers`.


Filters
-------

webuj functional-reactive nature makes it really simple to setup observers that notify subscribers
of events taking place on the blockchain.

To receive all new blocks as they are added to the blockchain::

   Subscription subscription = webuj.blockObservable(false).subscribe(block -> {
       ...
   });

To receive all new repTransactions as they are added to the blockchain::

   Subscription subscription = webuj.transactionObservable().subscribe(tx -> {
       ...
   });

To receive all pending repTransactions as they are submitted to the network (i.e. before they have
been grouped into a block together)::

   Subscription subscription = webuj.pendingTransactionObservable().subscribe(tx -> {
       ...
   });

Or, if you'd rather replay all blocks to the most current, and be notified of new subsequent
blocks being created::

   Subscription subscription = catchUpToLatestAndSubscribeToNewBlocksObservable(
           <startBlockNumber>, <fullTxObjects>)
           .subscribe(block -> {
               ...
   });

There are a number of other reqTransaction and block replay Observables described in :doc:`filters`.

Topic filters are also supported::

   EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST,
           DefaultBlockParameterName.LATEST, <contract-address>)
                .addSingleTopic(...)|.addOptionalTopics(..., ...)|...;
   webuj.ethLogObservable(filter).subscribe(log -> {
       ...
   });

Subscriptions should always be cancelled when no longer required::

   subscription.unsubscribe();

**Note:** filters are not supported on Infura.

For further information refer to :doc:`filters` and the
`Web3jRx <https://github.com/webuj/webuj/blob/master/core/src/main/java/org/webuj/protocol/rx/Web3jRx.java>`_
interface.


Transactions
------------

webuj provides support for both working with Ethereum wallet files (*recommended*) and Ethereum
client admin commands for sending repTransactions.

To send Ether to another party using your Ethereum wallet file::

   Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
   Credentials credentials = WalletUtils.loadCredentials("password", "/path/to/walletfile");
   TransactionReceipt repTransactionReceipt = Transfer.sendFunds(
           web3, credentials, "0x<address>|<ensName>",
           BigDecimal.valueOf(1.0), Convert.Unit.ETHER)
           .send();

Or if you wish to create your own custom reqTransaction::

   Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
   Credentials credentials = WalletUtils.loadCredentials("password", "/path/to/walletfile");

   // get the next available nonce
   EthGetTransactionCount hucGetRepTransactionCount = webuj.hucGetRepTransactionCount(
                address, DefaultBlockParameterName.LATEST).send();
   BigInteger nonce = hucGetRepTransactionCount.getTransactionCount();

   // create our reqTransaction
   RawTransaction rawTransaction  = RawTransaction.createEtherTransaction(
                nonce, <gas price>, <gas limit>, <toAddress>, <value>);

   // sign & send our reqTransaction
   byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
   String hexValue = Numeric.toHexString(signedMessage);
   EthSendTransaction hucSendRepTransaction = webuj.hucSendRawRepTransaction(hexValue).send();
   // ...

Although it's far simpler using webuj's `Transfer <https://github.com/webuj/webuj/blob/master/core/src/main/java/org/webuj/tx/Transfer.java>`_
for transacting with Ether.

Using an Ethereum client's admin commands (make sure you have your wallet in the client's
keystore)::

   Admin webuj = Admin.build(new HttpService());  // defaults to http://localhost:8545/
   PersonalUnlockAccount personalUnlockAccount = webuj.personalUnlockAccount("0x000...", "a password").sendAsync().get();
   if (personalUnlockAccount.accountUnlocked()) {
       // send a reqTransaction
   }

If you want to make use of Parity's
`Personal <https://github.com/paritytech/parity/wiki/JSONRPC-personal-module>`__ or
`Trace <https://github.com/paritytech/parity/wiki/JSONRPC-trace-module>`_, or Geth's
`Personal <https://github.com/ethereum/go-ethereum/wiki/Management-APIs#personal>`__ client APIs,
you can use the *org.webuj:parity* and *org.webuj:geth* modules respectively.


Command line tools
------------------

A webuj fat jar is distributed with each release providing command line tools. The command line
tools allow you to use some of the functionality of webuj from the command line:

- Wallet creation
- Wallet password management
- Transfer of funds from one wallet to another
- Generate Solidity smart contract function wrappers

Please refer to the :doc:`documentation <command_line>` for further
information.


Further details
---------------
In the Java 8 build:

- webuj provides type safe access to all responses. Optional or null responses
  are wrapped in Java 8's
  `Optional <https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html>`_ type.
- Asynchronous requests are wrapped in a Java 8
  `CompletableFutures <https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html>`_.
  webuj provides a wrapper around all async requests to ensure that any exceptions during
  execution will be captured rather then silently discarded. This is due to the lack of support
  in *CompletableFutures* for checked exceptions, which are often rethrown as unchecked exception
  causing problems with detection. See the
  `Async.run() <https://github.com/webuj/webuj/blob/master/core/src/main/java/org/webuj/utils/Async.java>`_ and its associated
  `test <https://github.com/webuj/webuj/blob/master/core/src/test/java/org/webuj/utils/AsyncTest.java>`_ for details.

In both the Java 8 and Android builds:

- Quantity payload types are returned as `BigIntegers <https://docs.oracle.com/javase/8/docs/api/java/math/BigInteger.html>`_.
  For simple results, you can obtain the quantity as a String via
  `Response <https://github.com/webuj/webuj/blob/master/core/src/main/java/org/webuj/protocol/core/Response.java>`_.getResult().
- It's also possible to include the raw JSON payload in responses via the *includeRawResponse*
  parameter, present in the
  `HttpService <https://github.com/webuj/webuj/blob/master/core/src/main/java/org/webuj/protocol/http/HttpService.java>`_
  and
  `IpcService <https://github.com/webuj/webuj/blob/master/core/src/main/java/org/webuj/protocol/ipc/IpcService.java>`_
  classes.
