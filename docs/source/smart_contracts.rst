Smart Contracts
===============

Developers have the choice of three languages for writing smart contracts:

`Solidity <https://Solidity.readthedocs.io/>`_
  The flagship language of HappyUC, and most popular language for smart contracts.

`Serpent <https://github.com/happyuc-project/wiki/wiki/Serpent>`_
  A Python like language for writing smart contracts.

LISP Like Language (LLL)
  A low level language, Serpent provides a superset of LLL. There's not a great deal of information
  for working with LLL, the following blog `/var/log/syrinx <http://blog.syrinx.net/>`_ and
  associated `lll-resurrected GitHub <https://github.com/zigguratt/lll-resurrected>`_ repository
  is a good place to start.


In order to deploy a smart contract onto the HappyUC blockchain, it must first be compiled into
a bytecode format, then it can be sent as part of a reqTransaction. Webuj can do all of this for you
with its :ref:`smart-contract-wrappers`. To understand what is happening behind the scenes, you
can refer to the details in :ref:`creation-of-smart-contract`.

Given that Solidity is the language of choice for writing smart contracts, it is the language
supported by Webuj, and is used for all subsequent examples.


Getting started with Solidity
-----------------------------

An overview of Solidity is beyond the scope of these docs, however, the following resources are a
good place to start:

- `Contract Tutorial <https://github.com/happyuc-project/go-happyuc/wiki/Contract-Tutorial>`_ on the Go
  HappyUC Wiki
- `Introduction to Smart Contracts <http://Solidity.readthedocs.io/en/develop/introduction-to-smart-contracts.html>`_
  in the Solidity project documentation
- `Writing a contract <https://happyuc-homestead.readthedocs.io/en/latest/contracts-and-transactions/contracts.html#writing-a-contract>`_
  in the HappyUC Homestead Guide

.. _compiling-Solidity:

Compiling Solidity source code
------------------------------

Compilation to bytecode is performed by the Solidity compiler, *solc*. You can install the compiler,
locally following the instructions as per
`the project documentation <http://solidity.readthedocs.io/en/develop/installing-solidity.html>`_.

To compile the Solidity code run:

.. code-block:: bash

   $ solc <contract>.sol --bin --abi --optimize -o <output-dir>/

The *--bin* and *--abi* compiler arguments are both required to take full advantage of working
with smart contracts from Webuj.

*--bin*
  Outputs a Solidity binary file containing the hex-encoded binary to provide with the reqTransaction
  request.

*--abi*
  Outputs a Solidity :doc:`abi` (ABI) file which details all of the publicly
  accessible contract methods and their associated parameters. These details along with the
  contract address are crucial for interacting with smart contracts. The ABI file is also used for
  the generation of :ref:`smart-contract-wrappers`.

There is also a *--gas* argument for providing estimates of the :ref:`gas` required to create a
contract and transact with its methods.


Alternatively, you can write and compile Solidity code in your browser via the
`browser-solidity <https://happyuc-project.github.io/browser-solidity/>`_ project. browser-solidity is
great for smaller smart contracts, but you may run into issues working with larger contracts.

You can also compile Solidity code via HappyUC clients such as Ghuc and Parity, using the JSON-RPC
method `huc_compileSolidity <https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_compileSolidity>`_
which is also supported in Webuj. However, the Solidity compiler must be installed on the client
for this to work.

There are further options available, please refer to the
`relevant section <https://happyuc-homestead.readthedocs.io/en/latest/contracts-and-transactions/contracts.html#compiling-a-contract>`_
in the Homestead documentation.


Deploying and interacting with smart contracts
----------------------------------------------

If you want to avoid the underlying implementation detail for working with smart contracts, Webuj
provides :ref:`smart-contract-wrappers` which enable you to interact directly with all of a smart
contract's methods via a generated wrapper object.

Alternatively, if you wish to send regular transactions or have more control over your
interactions with your smart contracts, please refer to the sections
:ref:`creation-of-smart-contract`, :ref:`transacting-with-contract` and :ref:`querying-state`
for details.


Smart contract examples
-----------------------

Webuj provides a number of smart contract examples in the project directory
`codegen/src/test/resources/solidity <https://github.com/happyuc-project/webu.java/tree/master/codegen/src/test/resources/solidity>`_

It also provides integration tests for demonstrating the deploying and working with those smart
contracts in the
`integration-tests/src/test/java/org/Webuj/protocol/scenarios <https://github.com/happyuc-project/webu.java/tree/master/integration-tests/src/test/java/org/Webuj/protocol/scenarios>`_
module.

.. image:: /images/smart_contract.png

.. _eip:

EIP-20 HappyUC token standard smart contract
---------------------------------------------

There an HappyUC standard, `EIP-20 <https://github.com/happyuc-project/EIPs/issues/20>`_
which started off as an
`HappyUC Improvement Proposal (EIP) <https://github.com/happyuc-project/EIPs>`_, that defines the
standard functions that a smart contract providing tokens should implement.

The EIP-20 standard provides function definitions, but does not provide an implementation example.
However, there is an implementation provided in
`codegen/src/test/resources/solidity/contracts <https://github.com/happyuc-project/webu.java/tree/master/codegen/src/test/resources/solidity/contracts>`_,
which has been taken from ConsenSys'
`Tokens project <https://github.com/ConsenSys/Tokens>`_.

Open Zepplin also provide an example implementation on
`GitHub <https://github.com/OpenZeppelin/zeppelin-solidity/tree/master/contracts/token>`_.

There are two integration tests that have been written to fully demonstrate the functionality of
this token smart contract.

`HumanStandardTokenGeneratedIT <https://github.com/happyuc-project/webu.java/tree/master/integration-tests/src/test/java/org/Webuj/protocol/scenarios/HumanStandardTokenGeneratedIT.java>`_
uses the generated
`HumanStandardTokenGenerated <https://github.com/happyuc-project/webu.java/tree/master/integration-tests/src/test/java/org/Webuj/generated/HumanStandardTokenGenerated.java>`_
:ref:`smart contract wrapper <smart-contract-wrappers>` to demonstrate this.

Alternatively, if you do not wish to use a smart contract wrapper and would like to work directly
with the underlying JSON-RPC calls, please refer to
`HumanStandardTokenIT <https://github.com/happyuc-project/webu.java/tree/master/integration-tests/src/test/java/org/Webuj/protocol/scenarios/HumanStandardTokenIT.java>`_.


.. _smart-contract-wrappers:

Solidity smart contract wrappers
--------------------------------

Webuj supports the auto-generation of smart contract function wrappers in Java from Solidity ABI
files.

The Webuj :doc:`command_line` tools ship with a command line utility for generating the smart contract function wrappers:

.. code-block:: bash

   $ Webuj solidity generate [--javaTypes|--solidityTypes] /path/to/<smart-contract>.bin /path/to/<smart-contract>.abi -o /path/to/src/main/java -p com.your.organisation.name

In versions prior to 3.x of Webuj, the generated smart contract wrappers used native Solidity
types. From Webuj 3.x onwards, Java types are created by default. You can create Solidity types
using the *--solidityTypes* command line argument.

You can also generate the wrappers by calling the Java class directly:

.. code-block:: bash

   org.happyuc.Webuj.codegen.SolidityFunctionWrapperGenerator /path/to/<smart-contract>.bin /path/to/<smart-contract>.abi -o /path/to/src/main/java -p com.your.organisation.name

Where the *bin* and *abi* are obtained as per :ref:`compiling-Solidity`.

The native Java to Solidity type conversions used are detailed in the :doc:`abi` section.

The smart contract wrappers support all common operations for working with smart contracts:

- :ref:`construction-and-deployment`
- :ref:`invoking-transactions`
- :ref:`constant-methods`
- :ref:`contract-validity`

Any method calls that requires an underlying JSON-RPC call to take place will return a Future to
avoid blocking.

Webuj also supports the generation of Java smart contract function wrappers directly from
`Truffle's <http://truffleframework.com/>`_
`Contract Schema <https://github.com/trufflesuite/truffle-contract-schema>`_
via the :doc:`command_line` utility.

.. code-block:: bash

   $ Webuj truffle generate [--javaTypes|--solidityTypes] /path/to/<truffle-smart-contract-output>.json -o /path/to/src/main/java -p com.your.organisation.name

And this also can be invoked by calling the Java class:

.. code-block:: bash

   org.happyuc.Webuj.codegen.TruffleJsonFunctionWrapperGenerator /path/to/<truffle-smart-contract-output>.json -o /path/to/src/main/java -p com.your.organisation.name

A wrapper generated this way ia "enhanced" to expose the per-network deployed address of the
contract.  These addresses are from the truffle deployment at the time the wrapper is generared.

.. _construction-and-deployment:

Construction and deployment
---------------------------

Construction and deployment of smart contracts happens with the *deploy* method::

   YourSmartContract contract = YourSmartContract.deploy(
           <Webuj>, <credentials>, GAS_PRICE, GAS_LIMIT,
           [<initialValue>,]
           <param1>, ..., <paramN>).send();

This will create a new instance of the smart contract on the HappyUC blockchain using the
supplied credentials, and constructor parameter values.

The *<initialValue>* parameter is only required if your smart contract accepts Huc on
construction. This requires the Solidity
`payable <http://solidity.readthedocs.io/en/develop/frequently-asked-questions.html?highlight=payable#how-do-i-initialize-a-contract-with-only-a-specific-amount-of-wei>`_
modifier to be present in the contract.

It returns a new smart contract wrapper instance which contains the underlying address of the
smart contract. If you wish to construct an instance of a smart contract wrapper with an existing
smart contract, simply pass in it's address::

   YourSmartContract contract = YourSmartContract.load(
           "0x<address>|<ensName>", Webuj, credentials, GAS_PRICE, GAS_LIMIT);


.. _contract-validity:

Contract validity
-----------------

Using this method, you may want to ascertain that the contract address that you have loaded is the
smart contract that you expect. For this you can use the *isValid* smart contract method, which will
only return true if the deployed bytecode at the contract address matches the bytecode in the
smart contract wrapper.::

   contract.isValid();  // returns false if the contract bytecode does not match what's deployed
                        // at the provided address


.. _transaction-managers:

Transaction Managers
--------------------

Webuj provides a
`TransactionManager <https://github.com/happyuc-project/webu.java/blob/master/core/src/main/java/org/Webuj/tx/TransactionManager.java>`_
abstraction to control the manner you connect to HappyUC clients with. The default mechanism uses
Webuj's
`RawTransactionManager <https://github.com/happyuc-project/webu.java/blob/master/core/src/main/java/org/Webuj/tx/RawTransactionManager.java>`_
which works with HappyUC wallet files to sign transactions offline before submitting them to the
network.

However, you may wish to modify the reqTransaction manager, which you can pass to the smart
contract deployment and creation methods instead of a credentials object, i.e.::

   YourSmartContract contract = YourSmartContract.deploy(
           <Webuj>, <transactionManager>, GAS_PRICE, GAS_LIMIT,
           <param1>, ..., <paramN>).send();

In addition to the RawTransactionManager, Webuj provides a
`ClientTransactionManager <https://github.com/happyuc-project/webu.java/blob/master/src/main/java/org/Webuj/tx/ClientTransactionManager.java>`_
which passes the responsibility of signing your reqTransaction on to the HappyUC client you are
connecting to.

There is also a
`ReadonlyTransactionManager <https://github.com/happyuc-project/webu.java/blob/master/core/src/main/java/org/Webuj/tx/ReadonlyTransactionManager.java>`_
for when you only want to retrieve data from a smart contract, but not transact with it.


Specifying the Chain Id on Transactions (EIP-155)
-------------------------------------------------

The RawTransactionManager takes an optional *chainId* parameter to specify the chain id to be used
on transactions as per
`EIP-155 <https://github.com/happyuc-project/EIPs/issues/155>`_. This prevents transactions from one chain
being re-broadcast onto another chain, such as from Ropsten to Mainnet::

   TransactionManager transactionManager = new RawTransactionManager(
           Webuj, credentials, ChainId.MAIN_NET);

In order to avoid having to change config or code to specify which chain you are working with,
Webuj's default behaviour is to not specify chain ids on transactions to simplify working with the
library. However, the recommendation of the HappyUC community is to use them.

You can obtain the chain id of the network that your HappyUC client is connected to with the
following request::

   Webuj.netVersion().send().getNetVersion();


.. reqTransaction-processors:

Transaction Receipt Processors
------------------------------

By default, when a new reqTransaction is submitted by Webuj to an HappyUC client, Webuj will
continually poll the client until it receives a
`TransactionReceipt <https://github.com/happyuc-project/webu.java/blob/master/core/src/main/java/org/Webuj/protocol/core/methods/response/TransactionReceipt.java>`_,
indicating that the reqTransaction has been added to the blockchain. If you are sending a number of
transactions asynchronously with Webuj, this can result in a number of threads polling the client
concurrently.

To reduce this polling overhead, Webuj provides configurable
`TransactionReceiptProcessors <https://github.com/happyuc-project/webu.java/blob/master/core/src/main/java/org/Webuj/tx/response/TransactionReceiptProcessor.java>`_.

There are a number of processors provided in Webuj:

- `PollingTransactionReceiptProcessor <https://github.com/happyuc-project/webu.java/blob/master/core/src/main/java/org/Webuj/tx/response/PollingTransactionReceiptProcessor.java>`_
  is the default processor used in Webuj, which polls periodically for a reqTransaction receipt for
  each individual pending reqTransaction.
- `QueuingTransactionReceiptProcessor <https://github.com/happyuc-project/webu.java/blob/master/core/src/main/java/org/Webuj/tx/response/QueuingTransactionReceiptProcessor.java>`_
  has an internal queue of all pending transactions. It contains a worker that runs periodically
  to query if a reqTransaction receipt is available yet. If a receipt is found, a callback to the
  client is invoked.
- `NoOpProcessor <https://github.com/happyuc-project/webu.java/blob/master/core/src/main/java/org/Webuj/tx/response/NoOpProcessor.java>`_
  provides an
  `EmptyTransactionReceipt <https://github.com/happyuc-project/webu.java/blob/master/core/src/main/java/org/Webuj/tx/response/EmptyTransactionReceipt.java>`_
  to clients which only contains the reqTransaction hash. This is for clients who do not want Webuj
  to perform any polling for a reqTransaction receipt.

**Note:** the
`EmptyTransactionReceipt <https://github.com/happyuc-project/webu.java/blob/master/core/src/main/java/org/Webuj/tx/response/EmptyTransactionReceipt.java>`_
is also provided in the the initial response from the `QueuingTransactionReceiptProcessor <https://github.com/happyuc-project/webu.java/blob/master/core/src/main/java/org/Webuj/tx/response/QueuingTransactionReceiptProcessor.java>`_.
This allows the caller to have the reqTransaction hash for the reqTransaction that was submitted to the
network.

If you do not wish to use the default processor
(`PollingTransactionReceiptProcessor <https://github.com/happyuc-project/webu.java/blob/master/core/src/main/java/org/Webuj/tx/response/PollingTransactionReceiptProcessor.java>`_), you can
specify the reqTransaction receipt processor to use as follows::

   TransactionReceiptProcessor transactionReceiptProcessor =
           new QueuingTransactionReceiptProcessor(Webuj, new Callback() {
                    @Override
                    public void accept(TransactionReceipt repTransactionReceipt) {
                        // process repTransactionReceipt
                    }

                    @Override
                    public void exception(Exception exception) {
                        // handle exception
                    }
   TransactionManager transactionManager = new RawTransactionManager(
           Webuj, credentials, ChainId.MAIN_NET, transactionReceiptProcessor);


If you require further information, the
`FastRawTransactionManagerIT <https://github.com/happyuc-project/webu.java/blob/master/integration-tests/src/test/java/org/Webuj/protocol/scenarios/FastRawTransactionManagerIT.java>`_
demonstrates the polling and queuing approaches.


.. _invoking-transactions:

Invoking transactions and events
--------------------------------

All transactional smart contract methods are named identically to their Solidity methods, taking
the same parameter values. Transactional calls do not return any values, regardless of the return
type specified on the method. Hence, for all transactional methods the
`Transaction Receipt <https://github.com/happyuc-project/wiki/wiki/JSON-RPC#eth_gettransactionreceipt>`_
associated with the reqTransaction is returned.::

   TransactionReceipt repTransactionReceipt = contract.someMethod(
                <param1>,
                ...).send();


The reqTransaction receipt is useful for two reasons:

#. It provides details of the mined block that the reqTransaction resides in
#. `Solidity events <http://Solidity.readthedocs.io/en/develop/contracts.html?highlight=events#events>`_
   that are called will be logged as part of the reqTransaction, which can then be extracted

Any events defined within a smart contract will be represented in the smart contract wrapper with
a method named *process<Event Name>Event*, which takes the Transaction Receipt and from this
extracts the indexed and non-indexed event parameters, which are returned decoded in an instance of
the
`EventValues <https://github.com/happyuc-project/webu.java/blob/master/abi/src/main/java/org/Webuj/abi/EventValues.java>`_
object.::

   EventValues eventValues = contract.processSomeEvent(repTransactionReceipt);

Alternatively you can use an Observable filter instead which will listen for events associated with
the smart contract::

   contract.someEventObservable(startBlock, endBlock).
           .subscribe(event -> ...);

For more information on working with Observable filters, refer to :doc:`filters`.

**Remember** that for any indexed array, bytes and string Solidity parameter
types, a Keccak-256 hash of their values will be returned, see the
`documentation <http://Solidity.readthedocs.io/en/latest/contracts.html#events>`_
for further information.


.. _constant-methods:

Calling constant methods
------------------------

Constant methods are those that read a value in a smart contract, and do not alter the state of
the smart contract. These methods are available with the same method signature as the smart
contract they were generated from::

   Type result = contract.someMethod(<param1>, ...).send();







Examples
--------

Please refer to :ref:`eip`.
