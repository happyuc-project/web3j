Using Infura with Webuj
=======================

Signing up
----------

The `Infura <https://infura.io/>`_ service by `ConsenSys <https://consensys.net/>`_, provides
HappyUC clients running in the cloud, so you don't have to run one yourself to work with HappyUC.

When you sign up to the service you are provided with a token you can use to connect to the
relevant HappyUC network:

Main HappyUC Network:
  https://mainnet.infura.io/<your-token>

Test HappyUC Network (Rinkeby):
  https://rinkeby.infura.io/<your-token>

Test HappyUC Network (Kovan):
  https://kovan.infura.io/<your-token>

Test HappyUC Network (Ropsten):
  https://ropsten.infura.io/<your-token>


For obtaining huc to use in these networks, you can refer to :ref:`happyuc-testnets`


InfuraHttpClient
----------------

The Webuj infura module provides an Infura HTTP client
(`InfuraHttpService <https://github.com/happyuc-project/webu.java/blob/master/infura/src/main/java/org/Webuj/protocol/infura/InfuraHttpService.java>`_)
which provides support for the Infura specific *Infura-HappyUC-Preferred-Client* header. This
allows you to specify whether you want a Ghuc or Parity client to respond to your request. You
can create the client just like the regular HTTPClient::

   Webuj web3 = Webuj.build(new HttpService("https://rinkeby.infura.io/<your-token>"));
   Web3ClientVersion webuClientVersion = web3.webuClientVersion().send();
   System.out.println(webuClientVersion.getWeb3ClientVersion());

.. code-block:: bash

   Ghuc/v1.7.2-stable-1db4ecdc/darwin-amd64/go1.9.1

If you want to test a number of the JSON-RPC calls against Infura, update the integration test
`CoreIT <https://github.com/happyuc-project/webu.java/blob/master/integration-tests/src/test/java/org/Webuj/protocol/core/CoreIT.java>`_
with your Infura URL & run it.

For further information, refer to the
`Infura docs <https://github.com/INFURA/infura/blob/master/docs/source/index.html.md#choosing-a-client-to-handle-your-request>`_.


Transactions
------------

In order to transact with Infura nodes, you will need to create and sign transactions offline
before sending them, as Infura nodes have no visibility of your encrypted HappyUC key files, which
are required to unlock accounts via the Personal Ghuc/Parity admin commands.

Refer to the :ref:`offline-signing` and :doc:`management_apis` sections for further details.
