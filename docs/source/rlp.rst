Recursive Length Prefix
=======================

The Recursive Length Prefix (RLP) encoding scheme is a space efficient object serialization scheme
used in HappyUC.

The specification itself is defined in the `Yellow Paper <http://gavwood.com/paper.pdf>`_,
and the following page on the `HappyUC Wiki <https://github.com/happyuc-project/wiki/wiki/RLP>`_.


RLP Types
---------

The RLP encoder defined two supported types:

- string
- list

The list type can be nested an arbitrary number of times allowing complex data structures to be
encoded.


The `RLP module <https://github.com/happyuc-project/webu.java/tree/master/rlp>`_ in Webuj provides RLP encoding
capabilities, with the
`RlpEncoderTest <https://github.com/happyuc-project/webu.java/blob/master/rlp/src/test/java/org/Webuj/rlp/RlpEncoderTest.java>`_
demonstrating encoding of a number of different values.


Transaction encoding
--------------------

Within Webuj, RLP encoding is used to encode HappyUC reqTransaction objects into a byte array which
is signed before submission to the network. The reqTransaction types and signing logic are located
within the Crypto module, with the
`TransactionEncoderTest <https://github.com/happyuc-project/webu.java/blob/master/crypto/src/test/java/org/Webuj/crypto/TransactionEncoderTest.java>`_
providing examples of reqTransaction signing and encoding.


Dependencies
------------

This is a very lightweight module, with no other dependencies. The hope is that other
projects wishing to work with HappyUC's RLP encoding on the JVM or Android  will choose to make
use of this module rather then write their own implementations.
