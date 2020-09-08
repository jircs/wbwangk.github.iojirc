# Transform Encryption

Encryption is the process of encoding data, making it unintelligible to anyone but the intended recipient(s). In this article, we’re going to explore three types of encryption: symmetric encryption, asymmetric encryption, and transform encryption.

In the descriptions below, we use the terms ***plaintext*** and ***ciphertext***. Plaintext is original data that someone wants to secure so it cannot be accessed by anyone but the intended recipients, and ciphertext is the scrambled result produced when the encryption process is applied to the plaintext. A good encryption algorithm produces ciphertexts that look like random data and that require a lot of work to recover the plaintext by anyone that is not authorized. This process of recovering the original plaintext from a ciphertext is called ***decryption***.

Most encryption algorithms use a ***key*** as part of the encryption/decryption process. Possession of the key that is correct for a particular ciphertext serves as a person's authorization to access the plaintext that produced that ciphertext.

## Symmetric encryption

Symmetric encryption uses the same key for both encryption and decryption. It’s fast and relatively simple, but users must find a way to securely share the key, since it is used for both encryption and decryption.

​                  ![A symmetric key is used for both encrypt and decrypt](https://d33wubrfki0l68.cloudfront.net/9c5ab3b797e6508db638f251c9e92fb4df2a9b6b/90aa8/static/9e270ae2ea572f258f50d633bb961872/38b44/symmetric-encryption.jpg)            

## Asymmetric encryption

Asymmetric encryption uses two keys that are mathematically related, generally called a key pair. The plaintext is encrypted with the ***public key***, and the ciphertext is decrypted using the corresponding ***private key***. Asymmetric encryption is also known as public key encryption because the encryption key can be shared publicly, while the decryption key must be kept private.

​                  ![A public key is used for encrypt, a private key for decrypt](https://d33wubrfki0l68.cloudfront.net/d5fb3104d42a7b379a372efdf312aececf285a39/54554/static/0858dc77539ce87850c942cd57810a8f/38b44/asymmetric-encryption.jpg)            

## Transform encryption

> Academic circles have long discussed transform encryption as proxy re-encryption (PRE). IronCore is the first commercialization of PRE. The word transformation is more descriptive of the process, so you will see the term transform encryption instead of proxy re-encryption.

Transform encryption uses three keys that are mathematically related: one to encrypt plaintext to a recipient, a second to decrypt the ciphertext, and a third to transform ciphertext encrypted to one recipient so it can be decrypted by a different recipient. The first and second keys are the same pieces of a key pair that are used in asymmetric encryption.

IronCore uses transform encryption to create access control groups. A plaintext is encrypted using a ***group public key***, a ***transform key*** converts the ***group ciphertext*** to ***member ciphertext***, and the ***member private key*** decrypts the member ciphertext on the member’s client device, recovering the plaintext.

A key aspect of the transformation process is that it does not require (or allow) the party doing the transformation to decrypt the ciphertext while transforming it. This allows transforms to be done by a semi-trusted service. The server never gains access to the plaintext and cannot get any information about the private key of either the group or the member.

​                  ![A group public key is used for encrypt, and after transform a user private key is used for decrypt](https://d33wubrfki0l68.cloudfront.net/bcc8fa7abfd493b6b275771e9cd737dab66a6f0f/98ef2/static/0a479a01c9a6d4de5f391cd65da50137/38b44/transform-encryption.jpg)            

## Why should you care?

The three encryption approaches - symmetric, asymmetric, and transform - are used together, and each has its benefits.

Symmetric encryption is fast and straightforward, but you must carefully safeguard the key. Revocation of access to data from someone who has the key means decrypting and re-encrypting the data using a new key, and making sure the person whose access is being revoked doesn't get access to the new key. If you don't have the encrypted data in your possession, this process is not possible.

Asymmetric encryption is more secure, and the web uses it to set up secure data tunnels using https. It’s also used for secure email and text message applications. However, it is not often used to secure data that must be accessed by several people, since data must be encrypted separately to the public key of each intended recipient. Network effects make asymmetric encryption impractical when multiple users share data in the cloud. Revocation of data means touching every encrypted file and removing a given recipient, which is increasingly impractical as the number of files or the number of users grows.

Transform encryption is a secure and scalable approach to encryption, designed explicitly for cryptographic access control in the cloud. It solves the problems of securely sharing with large numbers of users, allowing revocation of data without touching (or even possessing) the data, and of ancillary issues such as key management.

## Example

James T. Kirk is the captain of the Starship Enterprise. When the Enterprise explores a new planet, Captain Kirk selects crewmembers to visit the planet surface. These crewmembers are called the away-team. Kirk wants to be able to give the away-team orders without having commands compromised by adversaries.

Using transform encryption, the dev team for the Starship Enterprise takes these steps:

**Step 1**
 Kirk encrypts his commands to the group public key for the away-team. The encrypted data can be safely stored anywhere - cloud, device, or external service.

**Step 2**
 Kirk adds Mr. Spock as a member of the away-team group. Kirk generates a transform key between the group away-team and the member Mr. Spock and sends the transform key to IronCore to hold.

**Step 3**
 Mr. Spock requests encrypted data.

**Step 4**
 The IronCore service uses the transform key to transform the away team ciphertext into ciphertext that only Mr. Spock can decrypt using his private key. IronCore is never able to decrypt the secret data.

**Step 5**
 When Mr. Spock receives encrypted data, he decrypts it using his private key. Data is decrypted on Mr. Spock’s device and the plain text is not exposed anywhere else, providing true end-to-end encryption.

## What’s next

An essential property of transform encryption is that Captain Kirk can encrypt data to the away-team without knowing who is currently on the team or who will be added or removed. This concept is called ***orthogonal access control***, and it’s the topic for our next concept.

