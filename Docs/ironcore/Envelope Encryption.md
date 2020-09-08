# Envelope Encryption

Envelope encryption is a technique that combines [symmetric](https://ironcorelabs.com/docs/data-control-platform/concepts/envelope-encryption/#symmetric-encryption) and [asymmetric](https://ironcorelabs.com/docs/data-control-platform/concepts/envelope-encryption/#asymmetric-encryption) encryption to improve performance.

​                                     ![Random AES Key (DEK) encrypts document](https://ironcorelabs.com/static/ebc8fabebf95e140c5bd3062895e8585/38b44/1-randomAES-to-doc-key.jpg)            

A key value suitable for use with a symmetric encryption algorithm such as AES is chosen randomly, then it is used to encrypt plaintext. This plaintext is referred to as the *document*, and the key is called the *document encryption key* (DEK).

​                                     ![Public key encrypts DEK](https://ironcorelabs.com/static/cf60096df4595d9e679ecafc1cedcccc/38b44/2-public-key-encrypts.jpg)            

The DEK itself is then asymmetrically encrypted with a user’s public key. The resulting encrypted DEK can be stored with the data or elsewhere.

​                                     ![Private key decrypts encrypted DEK](https://ironcorelabs.com/static/3d6d881ef73e6eb15a0507f61a012eaf/38b44/3-private-key-decrypts.jpg)            

To recover the document the encrypted DEK is first decrypted using the user’s private key.

​                                     ![DEK decrypts encrypted document](https://ironcorelabs.com/static/052e47880d8f106e072b6b5f494e1bc5/38b44/4-DEK-decrypts-doc.jpg)            

Once the DEK is recovered, it can be used to decrypt the underlying document data.

#### Glossary terms:

##### Symmetric Encryption

Symmetric encryption uses one key for both encryption and decryption. It is fast and very secure. The main drawback is that the encrypter of the data and each user that should be allowed to decrypt must find a way to share the symmetric key securely.

##### Asymmetric Encryption

Asymmetric encryption uses two keys that are mathematically related (generally called a key pair). Plaintext or document data is encrypted using the public key, and the resulting ciphertext is decrypted using the corresponding private key.

##### Public Key Encryption

Public key encryption is another name for asymmetric encryption. It is called public key cryptography because the encryption key can be shared publicly, while the decryption key must be kept private.

