.. To build this file locally ensure docutils Python package is installed and run:
   $ rst2html.py README.rst README.html

Webuj: Web3 Java HappyUC Ðapp API
==================================

.. image:: https://readthedocs.org/projects/Webuj/badge/?version=latest
   :target: http://docs.Webuj.io
   :alt: Documentation Status

.. image:: https://travis-ci.org/Webuj/Webuj.svg?branch=master
   :target: https://travis-ci.org/Webuj/Webuj
   :alt: Build Status

.. image:: https://codecov.io/gh/Webuj/Webuj/branch/master/graph/badge.svg
   :target: https://codecov.io/gh/Webuj/Webuj
   :alt: codecov

.. image:: https://badges.gitter.im/Webuj/Webuj.svg
   :target: https://gitter.im/Webuj/Webuj?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge
   :alt: Join the chat at https://gitter.im/Webuj/Webuj

Webuj is a lightweight, highly modular, reactive, type safe Java and Android library for working with
Smart Contracts and integrating with clients (nodes) on the HappyUC network:

.. image:: https://raw.githubusercontent.com/Webuj/Webuj/master/docs/source/images/webuj_network.png

This allows you to work with the `HappyUC <https://www.happyuc.org/>`_ blockchain, without the
additional overhead of having to write your own integration code for the platform.

The `Java and the Blockchain <https://www.youtube.com/watch?v=ea3miXs_P6Y>`_ talk provides an
overview of blockchain, HappyUC and Webuj.


Features
--------

- Complete implementation of HappyUC's `JSON-RPC <https://github.com/happyuc-project/wiki/wiki/JSON-RPC>`_
  client API over HTTP and IPC
- HappyUC wallet support
- Auto-generation of Java smart contract wrappers to create, deploy, transact with and call smart
  contracts from native Java code
  (`Solidity <http://solidity.readthedocs.io/en/latest/using-the-compiler.html#using-the-commandline-compiler>`_
  and
  `Truffle <https://github.com/trufflesuite/truffle-contract-schema>`_ definition formats supported)
- Reactive-functional API for working with filters
- `HappyUC Name Service (ENS) <https://ens.domains/>`_ support
- Support for Parity's
  `Personal <https://github.com/paritytech/parity/wiki/JSONRPC-personal-module>`__, and Ghuc's
  `Personal <https://github.com/happyuc-project/go-happyuc/wiki/Management-APIs#personal>`__ client APIs
- Support for `Infura <https://infura.io/>`_, so you don't have to run an HappyUC client yourself
- Comprehensive integration tests demonstrating a number of the above scenarios
- Command line tools
- Android compatible
- Support for JP Morgan's Quorum via `Webuj-quorum <https://github.com/Webuj/quorum>`_


It has five runtime dependencies:

- `RxJava <https://github.com/ReactiveX/RxJava>`_ for its reactive-functional API
- `OKHttp <https://hc.apache.org/httpcomponents-client-ga/index.html>`_ for HTTP connections
- `Jackson Core <https://github.com/FasterXML/jackson-core>`_ for fast JSON
  serialisation/deserialisation
- `Bouncy Castle <https://www.bouncycastle.org/>`_
  (`Spongy Castle <https://rtyley.github.io/spongycastle/>`_ on Android) for crypto
- `Jnr-unixsocket <https://github.com/jnr/jnr-unixsocket>`_ for \*nix IPC (not available on
  Android)

It also uses `JavaPoet <https://github.com/square/javapoet>`_ for generating smart contract
wrappers.

Full project documentation is available at
`docs.Webuj.io <http://docs.Webuj.io>`_.


Donate
------

You can help fund the development of Webuj by donating to the following wallet addresses:

+----------+--------------------------------------------+
| HappyUC | 0x2dfBf35bb7c3c0A466A6C48BEBf3eF7576d3C420 |
+----------+--------------------------------------------+
| Bitcoin  | 1DfUeRWUy4VjekPmmZUNqCjcJBMwsyp61G         |
+----------+--------------------------------------------+


Commercial support and training
-------------------------------

Commercial support and training is available from `blk.io <https://blk.io>`_.


Quickstart
----------

A `Webuj sample project <https://github.com/Webuj/sample-project-gradle>`_ is available that
demonstrates a number of core features of HappyUC with Webuj, including:

- Connecting to a node on the HappyUC network
- Loading an HappyUC wallet file
- Sending Huc from one address to another
- Deploying a smart contract to the network
- Reading a value from the deployed smart contract
- Updating a value in the deployed smart contract
- Viewing an event logged by the smart contract


Getting started
---------------

Add the relevant dependency to your project:

Maven
-----

Java 8:

.. code-block:: xml

   <dependency>
     <groupId>org.Webuj</groupId>
     <artifactId>core</artifactId>
     <version>3.3.1</version>
   </dependency>

Android:

.. code-block:: xml

   <dependency>
     <groupId>org.Webuj</groupId>
     <artifactId>core</artifactId>
     <version>3.3.1-android</version>
   </dependency>

Gradle
------

Java 8:

.. code-block:: groovy

   compile ('org.Webuj:core:3.3.1')

Android:

.. code-block:: groovy

   compile ('org.Webuj:core:3.3.1-android')


Start a client
--------------

Start up an HappyUC client if you don't already have one running, such as
`Ghuc <https://github.com/happyuc-project/go-happyuc/wiki/ghuc>`_:

.. code-block:: bash

   $ ghuc --rpcapi personal,db,huc,net,web3 --rpc --testnet

Or `Parity <https://github.com/paritytech/parity>`_:

.. code-block:: bash

   $ parity --chain testnet

Or use `Infura <https://infura.io/>`_, which provides **free clients** running in the cloud:

.. code-block:: java

   Webuj web3 = Webuj.build(new HttpService("https://ropsten.infura.io/your-token"));

For further information refer to
`Using Infura with Webuj <https://Webuj.github.io/Webuj/infura.html>`_

Instructions on obtaining Huc to transact on the network can be found in the
`testnet section of the docs <http://docs.Webuj.io/transactions.html#happyuc-testnets>`_.


Start sending requests
----------------------

To send synchronous requests:

.. code-block:: java

   Webuj web3 = Webuj.build(new HttpService());  // defaults to http://localhost:8545/
   Web3ClientVersion webuClientVersion = web3.webuClientVersion().send();
   String clientVersion = webuClientVersion.getWeb3ClientVersion();


To send asynchronous requests using a CompletableFuture (Future on Android):

.. code-block:: java

   Webuj web3 = Webuj.build(new HttpService());  // defaults to http://localhost:8545/
   Web3ClientVersion webuClientVersion = web3.webuClientVersion().sendAsync().get();
   String clientVersion = webuClientVersion.getWeb3ClientVersion();

To use an RxJava Observable:

.. code-block:: java

   Webuj web3 = Webuj.build(new HttpService());  // defaults to http://localhost:8545/
   web3.webuClientVersion().observable().subscribe(x -> {
       String clientVersion = x.getWeb3ClientVersion();
       ...
   });

**Note:** for Android use:

.. code-block:: java

   Webuj web3 = webujFactory.build(new HttpService());  // defaults to http://localhost:8545/
   ...


IPC
---

Webuj also supports fast inter-process communication (IPC) via file sockets to clients running on
the same host as Webuj. To connect simply use the relevant *IpcService* implementation instead of
*HttpService* when you create your service:

.. code-block:: java

   // OS X/Linux/Unix:
   Webuj web3 = Webuj.build(new UnixIpcService("/path/to/socketfile"));
   ...

   // Windows
   Webuj web3 = Webuj.build(new WindowsIpcService("/path/to/namedpipefile"));
   ...

**Note:** IPC is not currently available on Webuj-android.


Working with smart contracts with Java smart contract wrappers
--------------------------------------------------------------

Webuj can auto-generate smart contract wrapper code to deploy and interact with smart contracts
without leaving the JVM.

To generate the wrapper code, compile your smart contract:

.. code-block:: bash

   $ solc <contract>.sol --bin --abi --optimize -o <output-dir>/

Then generate the wrapper code using Webuj's `Command line tools`_:

.. code-block:: bash

   Webuj solidity generate /path/to/<smart-contract>.bin /path/to/<smart-contract>.abi -o /path/to/src/main/java -p com.your.organisation.name

Now you can create and deploy your smart contract:

.. code-block:: java

   Webuj web3 = Webuj.build(new HttpService());  // defaults to http://localhost:8545/
   Credentials credentials = WalletUtils.loadCredentials("password", "/path/to/walletfile");

   YourSmartContract contract = YourSmartContract.deploy(
           <Webuj>, <credentials>,
           GAS_PRICE, GAS_LIMIT,
           <param1>, ..., <paramN>).send();  // constructor params

Alternatively, if you use `Truffle <http://truffleframework.com/>`_, you can make use of its `.json` output files:

.. code-block:: bash

   # Inside your Truffle project
   $ truffle compile
   $ truffle deploy

Then generate the wrapper code using Webuj's `Command line tools`_:

.. code-block:: bash

   $ cd /path/to/your/Webuj/java/project
   $ Webuj truffle generate /path/to/<truffle-smart-contract-output>.json -o /path/to/src/main/java -p com.your.organisation.name

Whether using `Truffle` or `solc` directly, either way you get a ready-to-use Java wrapper for your contract.

So, to use an existing contract:

.. code-block:: java

   YourSmartContract contract = YourSmartContract.load(
           "0x<address>|<ensName>", <Webuj>, <credentials>, GAS_PRICE, GAS_LIMIT);

To transact with a smart contract:

.. code-block:: java

   TransactionReceipt repTransactionReceipt = contract.someMethod(
                <param1>,
                ...).send();

To call a smart contract:

.. code-block:: java

   Type result = contract.someMethod(<param1>, ...).send();

For more information refer to `Smart Contracts <http://docs.Webuj.io/smart_contracts.html#solidity-smart-contract-wrappers>`_.


Filters
-------

Webuj functional-reactive nature makes it really simple to setup observers that notify subscribers
of events taking place on the blockchain.

To receive all new blocks as they are added to the blockchain:

.. code-block:: java

   Subscription subscription = Webuj.blockObservable(false).subscribe(block -> {
       ...
   });

To receive all new transactions as they are added to the blockchain:

.. code-block:: java

   Subscription subscription = Webuj.transactionObservable().subscribe(tx -> {
       ...
   });

To receive all pending transactions as they are submitted to the network (i.e. before they have
been grouped into a block together):

.. code-block:: java

   Subscription subscription = Webuj.pendingTransactionObservable().subscribe(tx -> {
       ...
   });

Or, if you'd rather replay all blocks to the most current, and be notified of new subsequent
blocks being created:

.. code-block:: java
   Subscription subscription = catchUpToLatestAndSubscribeToNewBlocksObservable(
           <startBlockNumber>, <fullTxObjects>)
           .subscribe(block -> {
               ...
   });

There are a number of other reqTransaction and block replay Observables described in the
`docs <http://docs.Webuj.io/filters.html>`_.

Topic filters are also supported:

.. code-block:: java

   HucFilter filter = new HucFilter(DefaultBlockParameterName.EARLIEST,
           DefaultBlockParameterName.LATEST, <contract-address>)
                .addSingleTopic(...)|.addOptionalTopics(..., ...)|...;
   Webuj.hucLogObservable(filter).subscribe(log -> {
       ...
   });

Subscriptions should always be cancelled when no longer required:

.. code-block:: java

   subscription.unsubscribe();

**Note:** filters are not supported on Infura.

For further information refer to `Filters and Events <http://docs.Webuj.io/filters.html>`_ and the
`webujRx <https://github.com/happyuc-project/webu.java/blob/master/src/core/main/java/org/Webuj/protocol/rx/webujRx.java>`_
interface.


Transactions
------------

Webuj provides support for both working with HappyUC wallet files (recommended) and HappyUC
client admin commands for sending transactions.

To send Huc to another party using your HappyUC wallet file:

.. code-block:: java
		
   Webuj web3 = Webuj.build(new HttpService());  // defaults to http://localhost:8545/
   Credentials credentials = WalletUtils.loadCredentials("password", "/path/to/walletfile");
   TransactionReceipt repTransactionReceipt = Transfer.sendFunds(
           web3, credentials, "0x<address>|<ensName>",
           BigDecimal.valueOf(1.0), Convert.Unit.HUC)
           .send();

Or if you wish to create your own custom reqTransaction:

.. code-block:: java

   Webuj web3 = Webuj.build(new HttpService());  // defaults to http://localhost:8545/
   Credentials credentials = WalletUtils.loadCredentials("password", "/path/to/walletfile");

   // get the next available nonce
   HucGetTransactionCount hucGetRepTransactionCount = Webuj.hucGetRepTransactionCount(
                address, DefaultBlockParameterName.LATEST).sendAsync().get();
   BigInteger nonce = hucGetRepTransactionCount.getTransactionCount();

   // create our reqTransaction
   RawTransaction rawTransaction  = RawTransaction.createHucTransaction(
                nonce, <gas price>, <gas limit>, <toAddress>, <value>);

   // sign & send our reqTransaction
   byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
   String hexValue = Hex.toHexString(signedMessage);
   HucSendTransaction hucSendRepTransaction = Webuj.hucSendRawRepTransaction(hexValue).send();
   // ...

Although it's far simpler using Webuj's `Transfer <https://github.com/happyuc-project/webu.java/blob/master/core/src/main/java/org/Webuj/tx/Transfer.java>`_
for transacting with Huc.

Using an HappyUC client's admin commands (make sure you have your wallet in the client's
keystore):

.. code-block:: java
  		
   Admin Webuj = Admin.build(new HttpService());  // defaults to http://localhost:8545/
   PersonalUnlockAccount personalUnlockAccount = Webuj.personalUnlockAccount("0x000...", "a password").sendAsync().get();
   if (personalUnlockAccount.accountUnlocked()) {
       // send a reqTransaction
   }

If you want to make use of Parity's
`Personal <https://github.com/paritytech/parity/wiki/JSONRPC-personal-module>`__ or
`Trace <https://github.com/paritytech/parity/wiki/JSONRPC-trace-module>`_, or Ghuc's
`Personal <https://github.com/happyuc-project/go-happyuc/wiki/Management-APIs#personal>`__ client APIs,
you can use the *org.Webuj:parity* and *org.Webuj:ghuc* modules respectively.


Command line tools
------------------

A Webuj fat jar is distributed with each release providing command line tools. The command line
tools allow you to use some of the functionality of Webuj from the command line:

- Wallet creation
- Wallet password management
- Transfer of funds from one wallet to another
- Generate Solidity smart contract function wrappers

Please refer to the `documentation <http://docs.Webuj.io/command_line.html>`_ for further
information.


Further details
---------------

In the Java 8 build:

- Webuj provides type safe access to all responses. Optional or null responses
  are wrapped in Java 8's
  `Optional <https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html>`_ type.
- Asynchronous requests are wrapped in a Java 8
  `CompletableFutures <https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html>`_.
  Webuj provides a wrapper around all async requests to ensure that any exceptions during
  execution will be captured rather then silently discarded. This is due to the lack of support
  in *CompletableFutures* for checked exceptions, which are often rethrown as unchecked exception
  causing problems with detection. See the
  `Async.run() <https://github.com/happyuc-project/webu.java/blob/master/core/src/main/java/org/Webuj/utils/Async.java>`_ and its associated
  `test <https://github.com/happyuc-project/webu.java/blob/master/core/src/test/java/org/Webuj/utils/AsyncTest.java>`_ for details.

In both the Java 8 and Android builds:

- Quantity payload types are returned as `BigIntegers <https://docs.oracle.com/javase/8/docs/api/java/math/BigInteger.html>`_.
  For simple results, you can obtain the quantity as a String via
  `Response <https://github.com/happyuc-project/webu.java/blob/master/src/main/java/org/Webuj/protocol/core/Response.java>`_.getResult().
- It's also possible to include the raw JSON payload in responses via the *includeRawResponse*
  parameter, present in the
  `HttpService <https://github.com/happyuc-project/webu.java/blob/master/core/src/main/java/org/Webuj/protocol/http/HttpService.java>`_
  and
  `IpcService <https://github.com/happyuc-project/webu.java/blob/master/core/src/main/java/org/Webuj/protocol/ipc/IpcService.java>`_
  classes.


Tested clients
--------------

- Ghuc
- Parity

You can run the integration test class
`CoreIT <https://github.com/happyuc-project/webu.java/blob/master/integration-tests/src/test/java/org/Webuj/protocol/core/CoreIT.java>`_
to verify clients.


Related projects
----------------

For a .NET implementation, check out `Nhappyuc <https://github.com/Nhappyuc/Nhappyuc>`_.

For a pure Java implementation of the HappyUC client, check out
`HappyUCJ <https://github.com/happyuc-project/happyucj>`_ and
`HappyUC Harmony <https://github.com/huc-camp/happyuc-harmony>`_.


Projects using Webuj
--------------------

Please submit a pull request if you wish to include your project on the list:

- `ERC-20 RESTful Service <https://github.com/blk-io/erc20-rest-service>`_
- `Huc Wallet <https://play.google.com/store/apps/details?id=org.vikulin.hucwallet>`_ by
  `@vikulin <https://github.com/vikulin>`_
- `eth-contract-api <https://github.com/adridadou/huc-contract-api>`_ by
  `@adridadou <https://github.com/adridadou>`_
- `HappyUC Paper Wallet <https://github.com/matthiaszimmermann/happyuc-paper-wallet>`_ by
  `@matthiaszimmermann <https://github.com/matthiaszimmermann>`_
- `Trust HappyUC Wallet <https://github.com/TrustWallet/trust-wallet-android>`_
- `Presto HappyUC <https://github.com/xiaoyao1991/presto-happyuc>`_
- `Kundera-HappyUC data importer and sync utility <https://github.com/impetus-opensource/Kundera/tree/trunk/src/kundera-happyuc>`_ by `@impetus-opensource <https://github.com/impetus-opensource>`_


Companies using Webuj
---------------------

Please submit a pull request if you wish to include your company on the list:

- `Amberdata <https://www.amberdata.io/>`_
- `blk.io <https://blk.io>`_
- `comitFS <http://www.comitfs.com/>`_
- `ConsenSys <https://consensys.net/>`_
- `ING <https://www.ing.com>`_
- `Othera <https://www.othera.io/>`_
- `Pactum <https://pactum.io/>`_
- `TrustWallet <http://trustwalletapp.com>`_
- `Impetus <http://www.impetus.com/>`_


Build instructions
------------------

Webuj includes integration tests for running against a live HappyUC client. If you do not have a
client running, you can exclude their execution as per the below instructions.

To run a full build (excluding integration tests):

.. code-block:: bash

   $ ./gradlew check


To run the integration tests:

.. code-block:: bash

   $ ./gradlew  -Pintegration-tests=true :integration-tests:test

Thanks and credits
------------------

- The `Nhappyuc <https://github.com/Nhappyuc/Nhappyuc>`_ project for the inspiration
- `Othera <https://www.othera.com.au/>`_ for the great things they are building on the platform
- `Finhaus <http://finhaus.com.au/>`_ guys for putting me onto Nhappyuc
- `bitcoinj <https://bitcoinj.github.io/>`_ for the reference Elliptic Curve crypto implementation
- Everyone involved in the HappyUC project and its surrounding ecosystem
- And of course the users of the library, who've provided valuable input & feedback
