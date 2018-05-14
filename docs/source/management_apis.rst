Management APIs
===============

In addition to implementing the standard
`JSON-RPC <https://github.com/ethereum/wiki/wiki/JSON-RPC>`_ API, Ethereum clients, such as
`Geth <https://github.com/ethereum/go-ethereum/wiki/geth>`__ and
`Parity <https://github.com/paritytech/parity>`__ provide additional management via JSON-RPC.

One of the key common pieces of functionality that they provide is the ability to create and
unlock Ethereum accounts for transacting on the network. In Geth and Parity, this is implemented
in their Personal modules, details of which are available below:

- `Parity <https://github.com/paritytech/parity/wiki/JSONRPC-personal-module>`__
- `Geth <https://github.com/ethereum/go-ethereum/wiki/Management-APIs#personal>`__

Support for the personal modules is available in webuj. Those methods that are common to both Geth
and Parity reside in the `Admin <https://github.com/webuj/webuj/blob/master/core/src/main/java/org/webuj/protocol/admin/Admin.java>`_ module of webuj.

You can initialise a new webuj connector that supports this module using the factory method::

   Admin webuj = Admin.build(new HttpService());  // defaults to http://localhost:8545/
   PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount("0x000...", "a password").send();
   if (personalUnlockAccount.accountUnlocked()) {
       // send a reqTransaction
   }

For Geth specific methods, you can use the
`Geth <https://github.com/webuj/webuj/blob/master/geth/src/main/java/org/webuj/protocol/geth/Geth.java>`_
connector, and for Parity you can use the associated
`Parity <https://github.com/webuj/webuj/blob/master/parity/src/main/java/org/webuj/protocol/parity/Parity.java>`_
connector. The *Parity* connector also provides support for Parity's
`Trace <https://github.com/paritytech/parity/wiki/JSONRPC-trace-module>`_ module. These connectors
are available in the webuj *geth* and *parity* modules respectively.

You can refer to the integration test
`ParityIT <https://github.com/webuj/webuj/blob/master/integration-tests/src/test/java/org/webuj/protocol/parity/ParityIT.java>`_
for further examples of working with these APIs.
