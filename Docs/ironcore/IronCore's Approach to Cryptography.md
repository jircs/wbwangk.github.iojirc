# IronCore's Approach to Cryptography

IronCore provides a turnkey way to secure data and handles all of the hard bits at every level from key management to elliptic curve math. We are making available technology that has until now only been talked about in academia. To do that, we built all new encryption libraries, implemented new protocols, and wrapped them all in easier to use interfaces. Below we explain the different components, our choices, and the measures we've taken to ensure that we're delivering to you rigorous quality and security.

This is a detailed look at the low-level cryptographic details. To understand how this is used and the practical applications in our system, please look at the [IronCore Concepts](https://ironcorelabs.com/docs/data-control-platform/concepts/transform-encryption/) documentation.

## The myth of older is better

The conventional wisdom holds that the old crypto libraries are the most trustworthy since they've been around the block so many times. Because of this, much of the Internet relies on these [outdated libraries](https://queue.acm.org/detail.cfm?id=2602816) with their [poor unit test coverage](http://www.opencoverage.net/openssl/index_html/index.html) and hard to reason about tangles of code that lead to vulnerabilities with their own brand names, like HeartBleed, Goto Fail, Logjam, and DROWN.

But let's not kid ourselves. Building crypto libraries is fraught with peril. There are many, many ways to slip up and combine trusted primitives in ways that open up sneaky attacks. Despite the state of some of the most widely deployed options, there's plenty of good reasons not to build something new.

That is, unless what you want to build can't be done using existing libraries. A lot of the cool concepts in academia have been trapped there for years. But as we bring these ideas into a place where they can be used and widely adopted, there's a set of criteria that we believe should be used as a baseline for safely and thoughtfully building new encryption libraries the right way:

1. The algorithms, implementation, and implementation choices should all be fully transparent, open source, and explained to avoid any possibility of “cooked” constants or other bad smells.
2. The underlying algorithms should have formal mathematical security proofs showing their assumptions.
3. Unit test coverage should be as near 100% as possible and should include property-based tests with random inputs as well as specific edge cases.
4. Code should be written using [functional programming](https://en.wikipedia.org/wiki/Functional_programming) paradigms that use principles of math to make software more testable, understandable, and less prone to unforeseen and unhandled errors, to the greatest extent possible.
5. The crypto code should be audited by a reputable crypto review company. Our most recent audit from the NCC Group is discussed in [NCC Group Audit of Open Source Proxy Re-Encryption Library](https://blog.ironcorelabs.com/ironcore-labs-proxy-re-encryption-library-audit-by-ncc-group-f67abe666838).

The discussion below and linked papers show how we live up to these ideals.

## Peeling back the onion

![img](https://ironcorelabs.com/images/encryption/image1.png)

It helps to start with the most primitive building blocks and work our way up to the high level services that sit on top of those foundational blocks. We'll take that approach here, but you should feel free to skim to the layer of the onion that interests you the most. 

 

## Cryptographic primitives

IronCore uses common and widely adopted primitives for asymmetric crypto, symmetric crypto, digital signatures, secure hashes, and secure random number generation.

### Asymmetric crypto

#### Elliptic curve cryptography

IronCore uses standard Elliptic Curve Cryptography and the related Discrete Log Problem (ECDLP) security assumptions as the foundation for its public key system. All public/private key pairs are standard elliptic curve key pairs consisting of the private key, a number between 1 and the curve's prime modulus, chosen randomly, and the public key, which is a point on the curve produced by scalar multiplication of the private key and the curve's generator point. The prime and generator point are pre-selected public parameters of the scheme.

We support both 256-bit keys and 480-bit keys.

#### Pairing-based cryptography

[Pairings](https://en.wikipedia.org/wiki/Pairing-based_cryptography) were first used in cryptography in 1991. By 2004, there were hundreds of pairing-based crypto schemes, and there are now tens of thousands. Pairing-based cryptography is the basis for Identity-based Encryption (IBE), Attribute-based Encryption (ABE), Intel's Enhanced Privacy ID (EPID), zkSnarks, and a number of schemes in the areas of searchable encryption and Proxy Re-encryption (PRE).

Bilinear pairings are a cryptographic primitive that operate on top of elliptic curves. Standard ECC operations are point addition (point plus point equals point) and scalar multiplication (number times point equals point). The pairing operation takes two points and produces a scalar number (point paired with point from a different group equals number) and the operation is only considered secure on elliptic curves that meet certain criteria, so it is used cryptographically only on curves that are vetted for the purpose.

Schemes have been proposed by Matthew Green, Susan Hohenberger, Matt Blaze, Dan Boneh, and others. Notable commercialization of pairing-based crypto schemes include IBE schemes sold by Trend Micro and Voltage Security (purchased by HP).

#### Public parameters for elliptic curve

IronCore's public parameters are pulled straight from public, widely cited, published papers. We use a Barreto—Naehrig (BN) curve first described in the 2005 paper, [“Pairing-Friendly Elliptic Curves of Prime Order”](https://www.cryptojedi.org/papers/pfcpo.pdf) and later improved in the paper, [“New software speed records for cryptographic pairings”](https://www.cryptojedi.org/papers/dclxvi-20100714.pdf) by Naehrig et al. Per that paper, we use the Optimal Ate Pairing algorithm to calculate pairings on our curve.

### Symmetric cryptography

The system uses a standard envelope encryption protocol to secure data, where the message to be secured is encrypted using a symmetric encryption algorithm with a randomly chosen key (the Document Encryption Key or DEK), then that key is encrypted to the intended recipient(s) using asymmetric encryption. This is how PGP, SSL, SMIME, and other public key schemes work under the hood.

Our system uses AES256-GCM for all symmetric encryption operations. This primitive provides authenticated encryption, which can detect whether the ciphertext was modified after encryption. The initialization vector (IV) is randomly chosen each time the message is encrypted, and the IV is prepended to the output of the encryption algorithm to form the final encrypted message.

### Signatures

To prevent tampering and provide strong guarantees about identity, and also for reasons of performance, IronCore uses one of two signing schemes.

#### Ed25519

We use Ed25519 with dedicated signing keys in the following cases:

- Users sign the encrypted messages so decrypting users can know who encrypted it and that it wasn't modified.
- The IronCore service signs transformed encrypted DEKs so users know an untrusted party hasn't interfered with the transformation process.
- Most calls to IronCore's APIs are signed with a user's Ed25519 signing certificate for authentication purposes.

#### Schnorr

There is one case where we use the Schnorr signature algorithm to sign an API request: when we create new keys for a new device and submit the public key to the IronCore server. This proves that the user submitting the public key also possesses the related private key. Schnorr is an elliptic curve-based signature algorithm [introduced in 1990](https://link.springer.com/chapter/10.1007/3-540-46885-4_68).

## Primitive primitives

### Hashing

We use SHA-256 for cryptographic hashing.

### Random number generation

On the server, we use the standard Linux /dev/urandom for randomization. In the browser, we use the [Web Crypto API](https://developer.mozilla.org/en-US/docs/Web/API/Crypto/getRandomValues) to generate random numbers.

## Cryptographic protocols

### Proxy re-encryption (PRE)

IronCore's protocol is primarily built on top of a proxy re-encryption scheme. In general, a PRE scheme works like this: A ciphertext for Alice can be transformed into a ciphertext for Bob by one or more semi-trusted proxies without the proxies getting any information about the encrypted message or the private keys of either party.

PRE schemes were [first proposed by Matt Blaze, Gerrit Bleumer, and Martin Strauss](https://link.springer.com/chapter/10.1007/BFb0054122) in 1998. It has been a very active area of research since then.

#### E-Multi-Use-PRE by Cai et al.

IronCore selected the E-Multi-Use-PRE Scheme specified in the paper, “A multi-use CCA-secure proxy re-encryption scheme” by Cai and Liu.

This scheme builds on and improves an algorithm first published by Wang et al. The scheme makes use of pairing-based crypto and we chose it because it has the following desirable properties:

| Property        | Description                                                                                                                                                                         | | ~~~~~~~~        | ~~~~~~~~~~~                                                                                                                                                                         | | Unidirectional  | Delegation from Alice → Bob does not allow re-encryption from Bob → Alice.                                                                                                          | | Multi-hop       | Transformations can be stacked so if a proxy has transform keys for Alice → Bob and Bob → Charlie, two transforms can delegate access from Alice → Charlie.                         | | Collusion-safe  | Delegation from Alice → Bob does not allow recovery of Alice's key even if the proxy colludes with Bob.                                                                             | | Non-interactive | Re-encryption keys can be generated by Alice using Bob's public key; no trusted third party or interaction is required.                                                             | | Non-transitive  | The proxy cannot re-delegate decryption rights. For example, given transform keys from Alice → Bob and Bob → Charlie, the proxy cannot create a transform key from Alice → Charlie. |

Additionally, the scheme is adaptive chosen ciphertext (IND-Pr-CCA2) secure under the Decisional Bilinear Diffie-Hellman (DBDH) problem, which is a much stronger standard than most PRE schemes. Most schemes use the chosen plain-text attack model (CPA security) instead.

The scheme consists of these five algorithms:

|           | Environment | Description                                          |
| --------- | ----------- | ---------------------------------------------------- |
| KeyGen    | Client      | Generate standard elliptic curve keys.               |
| ReKeyGen  | Client      | Generate transform key from user i to user j.        |
| Encrypt   | Client      | Encrypt message m to user i.                         |
| ReEncrypt | Proxy       | Transform cipher text from user i to user j or fail. |
| Decrypt   | Client      | Decrypt transformed (or not) cipher text or fail.    |

#### Transform terminology

The term “re-encryption” is commonly used to capture the event where a piece of data has to be decrypted and then encrypted with a new key, as when someone who may have had access to a key should no longer have access to the data it unlocks. This can be a very painful and negative thing, particularly in systems with many files, users or keys. It also makes talking about proxy re-encryption tricky since in the case of PRE, the data is not decrypted, but is instead transformed such that a delegate can decrypt it. Consequently, we prefer to use the term “Transform Encryption” instead of “Proxy Re-encryption” and we propagate that change to other parts of the system. For example, for the algorithms above, we make the following name changes:

```text
ReKeyGen -> TransformKeyGen
ReEncrypt -> Transform
```

#### IronCore's implementation of PRE

Our implementation leaves the above five algorithms unchanged except to use a stronger signature scheme. We also augment the above algorithms with two more that make administration of groups a responsibility that can be shared between users and revoked without the need to rotate keys or re-encrypt data (in this sense, we mean decrypt and then encrypt with a new key).

|                          | Environment | Description                                                  |
| ------------------------ | ----------- | ------------------------------------------------------------ |
| AugmentGroupPublicKey    | Proxy       | Compute a new public key for a group using the client-generated public key and a server-side private key. |
| AugmentGroupTransformKey | Proxy       | Compute a transform key from from the new (augmented) public key for the group to the delagatee using the client-generated transform key and the server-side private key. |

The full details of our protocol can be found in this ACM paper:

| ![ACM DL Author-ize service](https://ironcorelabs.com/images/acm.png) | [Cryptographically Enforced Orthogonal Access Control at Scale](https://dl.acm.org/authorize?N654085)                   [Bob Wall](https://dl.acm.org/author_page.cfm?id=81371592859),         [Patrick Walsh](https://dl.acm.org/author_page.cfm?id=99659274307)                  SCC '18 Proceedings of the 6th International Workshop on Security in Cloud Computing, 2018 |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
|                                                              |                                                              |

<iframe width="100%" height="30" src="https://dl.acm.org/authorizestats?N654085" frameborder="0" scrolling="no">frames are not supported</iframe>

#### The open source recrypt library

Our implementation of the PRE scheme and the underlying primitives mentioned to this point, including the pairing primitives, have been implemented in our [recrypt library](https://github.com/ironcorelabs/recrypt), **which is open source and available on Github.**

![img](https://ironcorelabs.com/images/encryption/image3.png)

This library is written in Scala and **uses functional programming techniques** wherever possible. The library enjoys 94.98% code coverage as of its release including numerous property-based tests.

In addition, this library has been **reviewed by NCC Group**. The review and public report is discussed in [NCC Group Audit of Open Source Proxy Re-Encryption Library](https://blog.ironcorelabs.com/ironcore-labs-proxy-re-encryption-library-audit-by-ncc-group-f67abe666838).

## IronCore's SDK and protocols

IronCore's end-to-end encryption scheme is a zero visibility protocol where the server is completely blind as to the contents of any secret information. This protocol allows identification, key exchange, and other basic cryptographic operations without leaking any information about secrets or the keys used to unlock them.

IronCore's service consists of two main parts: an SDK that runs in the client and that performs all key generation, encryption, and decryption operations; and a cloud service that stores public keys, signed assertions of the identities that relate to those keys, transform keys, detailed and tamper-evident audit logs, and, in some cases, encrypted escrowed private keys.

For more information on how we protect our services and how we live up to our principles of security, privacy, transparency, and reliability, please take a look at our [Trust Center](https://ironcorelabs.com/trust-center/).

