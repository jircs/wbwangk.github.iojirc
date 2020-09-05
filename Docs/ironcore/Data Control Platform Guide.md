# Data Control Platform Guide

## Preface

### Intended Audience

The Data Control Guide can be used on multiple levels, but is primarily useful as a starting point to understand the core capabilities of the Data Control Platform via use-cases and examples. We have included a number of patterns here, along with sample source code to make things more concrete for developers and architects, but readers looking for a higher-level understanding should be able to read the text and skip over the code snippets fairly easily.

### Data Control Platform in One Paragraph

The Data Control Platform is a suite of SDKs for managing sensitive data. These SDKs implement state-of-the-art privacy and security protocols, and they can help you meet internal and external compliance requirements. The protected data can be database fields, files, or any other storage format, and the security of and control over data can be maintained no matter where or how the data is stored. The Data Control Platform handles the hard problems of cryptographic algorithm selection and implementation as well as key management. Developers can use the platform to build zero-trust architectures, end-to-end encryption solutions, ways to “pull back” shared data, or enforcement of data policies. Depending on the needs of the application, Data Control can be integrated into endpoint applications in the browser, on mobile devices, or in server side applications.

## Introduction: Trade Hoping for Knowing

### What is the Data Control Platform?

Many of us have had that moment of hesitation when collecting data from a user. We think, “Are we just going to store that in the clear? What if ---?” Most developers have learned to ignore that moment of pause, and the result is untracked duplication, misuse, and leaks of stored data. When data is stored without built-in access controls enabled, there are no boundaries to where the data can be used, how it can be duplicated, or who might be able to access it.

You can maintain control over your data. The Data Control Platform can help. **Data Control is a data-centric way of designing privacy and security features into your application by storing the access controls with the data.**

**Data-centric privacy and security by design** changes the mindset of application development. By applying access controls at the data level, uncertainty is removed. Can my application access this data? It can if you can decrypt it! Can I copy the data somewhere else? Go ahead, it’s safe wherever you put it. A customer wants us to delete all his data!?! Remove access to that data and it has effectively been deleted — even from cold backups. Our whole S3 bucket of customer data was just dumped by an unknown party! It’s okay… they can’t decrypt it.

Now you are starting to see the possibilities. This new way of thinking allows your application to send, store, retrieve, process, and delete data with much greater confidence. You don’t have to wonder where the data has migrated and who has access. Gone are the days of blindly hoping the data isn’t misused, or trying to cobble together layers of protection to prevent unauthorized access. **Data Control allows applications to know with mathematical certainty who can access a piece of data by storing Cryptography Based Access Controls (CBAC) with the data itself.**

### Cryptography Based Access Control (CBAC)

Access control schemes, such as RBAC, aim to restrict access to a system to only authorized users. The problem with even the most sophisticated access control schemes is they can be bypassed. Think about it - if your application is checking to see who the current user is and what rights they have and is augmenting SQL queries to enforce those rights, then there’s a lot of room for error. Besides the complexity, your app itself likely has full access to all of the data via its database credentials. If the app is hacked or those credentials are leaked, the data is gone. Additionally, you still have the problem of other devs building new functionality or apps that omit the access control layers or don’t implement them correctly or curious database administrators peeking at data, or government subpoenas going to service providers, or hackers getting access to the infrastructure and therefore to the crown jewels, or… this list could go on and on. The problem with today’s access control schemes is that you can’t know if they’re actually being enforced, even with rigorous security teams, checklists, and policies.

Instead of protecting the soft, gooey nuggets of data at the heart of your application by hoping there are no weak links in your access control, your network, your applications, and your staff, what if the data itself was hardened? What if when an attacker got to the data and instead of finding a diamond, they found only a lump of coal — capable of becoming a diamond, but only under the right conditions? CBAC allows you to know with mathematical certainty who or what can access a piece of data by baking the access control into the data itself.

## Use Cases for the Data Control Platform

The following are some high-level use cases for the Data Control Platform. These are meant to spark your creativity and to help you understand what’s possible. If you have a new use case, please reach out to us. We’d love to hear from you!

### Sharing Sensitive Files

Securely sharing files is very straightforward with the Data Control Platform. In fact, we have a free standalone tool available to do this on Linux, MacOS, and Windows called [IronHide](https://github.com/IronCoreLabs/ironhide). The tool is an open-source, easy-to-use command line utility that lets you choose where to store the encrypted file. You can think of it as a souped-up gpg. What’s unique about IronHide vs a tool like gpg is that encrypting the data and deciding (or changing) who can decrypt the data are completely separable! We call this separation of encryption from cryptographic permissioning [orthogonal access control](https://ironcorelabs.com/docs/data-control-platform/concepts/orthogonal-access/).

Secure file sharing can also be built directly into your app using the Data Control SDKs. If your application stores file attachments, for example, maybe the files can remain opaque to backend services and be converted back to a usable format at the point of use, where an end user actually needs access. Or maybe a backend service only needs limited or one-time access to a file; for instance, maybe a file attachment is scanned for viruses once when it is uploaded to the server. With Data Control, you can implement a zero-trust end-state where your users and their collaborators can see those files, but the people who operate your systems and the attackers that penetrate your security cannot.

### Shared Developer/CI Secrets

Most secret sharing systems involve some centralized store of secrets. But this can be inconvenient, particularly in a world of distributed version control systems. Using the Data Control Platform, secrets can be stored in their most natural place and shared with only those developers or systems who need access — even if a repository is public.

### Storing Encrypted Data In a Database

The Data Control Platform is independent of the backend storage. Controlled data can be stored in relational databases, key/value stores, document databases, or any storage mechanism that fits your application. Your application uses a Data Control SDK to encrypt the data and then stores the protected data.

By default, the Data Control SDKs return encrypted data as bytes. This means that the application’s data store must accept those raw bytes, or the application can use Base64 or a similar encoding scheme to turn the bytes into a string that the database will accept.

While the approach of storing opaque encrypted data suits many use cases, a data store’s search, query, sort, and indexing capabilities can not operate directly on the encrypted data. The Data Control Platform does provide [encrypted search capabilities](https://ironcorelabs.com/docs/data-control-platform/concepts/encrypted-search/), and future versions will expand this capability.

### Encrypting Data To Large Groups of Users

The Data Control Platform supports the use of [Scalable Encryption Groups](https://ironcorelabs.com/docs/data-control-platform/guide/#scalable-encryption-groups) to protect your data, and these groups handle a large number of users in an extremely efficient manner. When you encrypt data, you decide which users and/or Scalable Encryption Groups to encrypt to, but the decision about which users can decrypt the data via the group is an independent decision. This is [orthogonal access control](https://ironcorelabs.com/docs/data-control-platform/concepts/orthogonal-access/) at work.

You decide how to organize your users into groups, so that all the users that need access to a particular type of data are grouped together. There is no limit to the size of a group - maybe it’s a handful of people in a small department working on a sensitive project, or maybe it’s a group of all the company’s employees. Users can belong to multiple groups, so your decisions about group structure can be governed by your privacy and security needs, not by group size or other arbitrary limitation.

Either way, when you decide a piece of data needs to be accessed by a group, you encrypt that data to that group. This encryption operation takes the same amount of time regardless of the size of the group - a group of 100,000 users takes no longer than a group of 10 users. If you need to add people to or remove people from the group, that can be done without touching any data that was encrypted to the group. See [Encrypting Data To Dynamic Groups of Users](https://ironcorelabs.com/docs/data-control-platform/guide/#encrypting-data-to-dynamic-groups-of-users) and [Scalable Encryption Groups](https://ironcorelabs.com/docs/data-control-platform/guide/#scalable-encryption-groups) for more information.

### Encrypting Data To Dynamic Groups of Users

[Scalable Encryption Groups](https://ironcorelabs.com/docs/data-control-platform/guide/#scalable-encryption-groups) support not only [large groups of users](https://ironcorelabs.com/docs/data-control-platform/guide/#encrypting-data-to-large-groups-of-users), but also groups with very dynamic membership. Your decisions about how to protect your data should be based on deciding what individuals, roles, or projects need access to that data. We allow you to create Scalable Encryption Groups to represent collections of users that have the same access requirements, and to then easily encrypt data to those groups. But the real world is a messy place, and few groups of users stay static. New employees come on board, old ones leave, roles change, and so do decisions about how much access a role requires. Once a person no longer needs access to a piece of data, their ability to decrypt the data should be revoked. If you use a traditional encryption scheme, that means that each time a change in group membership occurs, you need to re-encrypt all of that group’s data to the updated set of users, redistribute the new copy of the data to everyone that still needs access, and trust that any users who were removed delete their copies of the data. Or you could just give up and not revoke access from users that no longer need it.

With IronCore’s Data Control SDKs, you no longer need to make that concession. Because the management of group membership is independent from the encryption of data to a group (thanks to [orthogonal access control](https://ironcorelabs.com/docs/data-control-platform/concepts/orthogonal-access/)!), you can change group membership whenever necessary, without touching any encrypted data. If your applications are designed to leave data encrypted when it is in storage and only decrypt it at the point of use, you can be confident that the only people that can get to the data are the users who should have access.

This use case is also applicable to give access to newly provisioned users in a self-sign-up or self-install type of environment. When a new user installs an application, a semi-automated flow can assist with getting the user into the correct groups to be able to decrypt the necessary data.

### Auditing Data Access

Control of data isn’t just about determining who can access the data. You only really have control when you know who has accessed the data. Your confidence increases if that audit trail also provides the ability to detect anomalous behavior like a user who is decrypting unusual amounts of data.

Some audit trails only tell you when data has changed, but the Data Control Platform provides an unbypassable audit trail also shows when data is accessed. This is possible because a mathematical step, called a transform, must be performed by the platform for data to be decrypted on a device.

This level of audit trail helps companies meet compliance obligations. If your data is subject to regulations, you may be required to track how that data is accessed, when, and by whom, and be ready to share that information with regulators or auditors.

### "Deleting" User Data By Removing Access (GDPR Right To Be Forgotten / Right To Erasure)

In a standard application architecture, if a user requests that their data be deleted, it can be difficult to locate and delete all copies of the data. By utilizing CBAC and the Data Control Platform, access to data can be revoked without locating or modifying the data using a technique called “crypto-shredding.”

Crypto-shredding is a way of deleting data by instead deleting the key that’s needed to decrypt it. If you can do this, then you can effectively delete all copies of the encrypted data even if they reside in offline backups. This can help with the right to erasure and right to be forgotten requirements of GDPR and other similar data privacy laws.

The Data Control Platform provides several ways to approach crypto-shredding that orient around revoking access to the data. This accomplishes the same thing by making sure that a company can no longer decrypt the data.

### Protecting Personal Data that Is Used for Record Location

Suppose you have a front end application that communicates with a back end service you provide, and that your system deals with customer data. Some part of that data is almost certainly Personally Identifiable Information (PII), and you should definitely protect access to that data. You probably store each customer’s name, email address, mailing address, and maybe even some more sensitive information like birth date or social security number. You can use the IronCore SDKs to encrypt that data in the front end application and to only decrypt it at the point of use, in another instance of the application. If your back end service does not need access to this PII data, you can protect the data end to end and eliminate the concern that an attacker or a curious administrator might extract the information from the back end.

However, some parts of the customer data are probably needed by your application to look up a customer’s record, such as the person’s name, email address, and mailing address. This doesn’t eliminate your opportunity to encrypt the data; you can use IronCore’s encrypted search feature to index this data so that you can encrypt the data at the point of origin and store it safely, but you can still use the encrypted search features that the Data Control Platform provides to find the right records. Our concepts section has an overview of our [encrypted search functionality](https://ironcorelabs.com/docs/data-control-platform/concepts/encrypted-search/).

Suppose your application needs to look up customers by name, email, and mailing address. You will probably get the most usability and performance by indexing each of those elements separately. Your application can create three separate indices, and each time a user enters or updates data about a customer, the application uses the IronCore SDK to also generate the index tokens to represent that customer record in each of these indices. Your application and back end service are responsible for storing the index tokens in a persistent store that will allow the back end to search for matches.

Suppose your application needs to search for a customer record given a name. It uses the IronCore SDK, specifying the customer name index and the search string entered by a user, to generate a list of index tokens for the search and retrieve all the matching records from the back end. The IronCore SDK can help your application to decrypt the sensitive data in those records and filter out records that don’t match the query (*false positives*).

The details on how to put all these pieces together are shown in the [Encrypted Search Patterns](https://ironcorelabs.com/docs/data-control-platform/guide/#encrypted-search-patterns).

## Core Concepts

### Basics: Users, Devices, and Scalable Encryption Groups

For the Basic Patterns to make sense it will be useful to introduce the main actors in the Data Control Platform. **Users, Devices and Scalable Encryption Groups** are each cryptographic entities with their own public/private key-pairs. It is essential to understand that with CBAC, the links between these entities are mathematical and not based on traditional permissioning concepts.

NOTE: The underlying technology that makes this possible is [Transform Encryption](https://ironcorelabs.com/docs/data-control-platform/concepts/transform-encryption/). It is not necessary to understand the mathematics of Transform Encryption to utilize Users, Devices, and Scalable Encryption Groups.

##### CBAC Users

**Users** are the basic building block for CBAC. A CBAC “User” is a flexible concept that could be a user in an application, a kubernetes pod, a cloud function, or another actor that needs to perform a cryptographic operation. Each user is a unique identity that has its own public/private key-pair. Users can have many devices and can belong to many groups.

##### Devices

**Devices** are the only entity in the Data Control Platform that can decrypt data. A device is authorized using a user’s private key, and thus a device is tightly bound to a user. Since data is never encrypted directly to a device, devices can be considered ephemeral as there is no penalty for deleting a device and creating another one. Device authorizations can also be revoked, removing the ability to perform cryptographic operation, including decryption.

All SDK operations happen in the context of a particular device.

##### Scalable Encryption Groups

**Scalable Encryption Groups**, or simply **Groups**, are collections of users. A user in the context of a group is called a member. Groups can have any number of members. Group membership can be modified by users designated as group administrators. Adding a user to a group is a series of cryptographic operations involving the administrating user’s keys, the group’s keys, and the new member’s public key.

USERDEVICEGROUP1 User : N Devices  N Groups : M Users 

Now you know what Users, Devices, and Scalable Encryption Groups are, but what can they do? This is best demonstrated in the context of encryption and decryption operations.

Data can be encrypted to zero or more Users and zero or more Scalable Encryption Groups. Data can only be decrypted via an authorized Device

This “unbalanced” relationship between encryption and decryption might seem odd at first, but the power of this design is revocation. In CBAC, **revocation** occurs when the cryptographic link, called a Transform Key, between a User and a Device or Group and a User is severed. If you are interested in the underlying cryptographic operations, check out [Transform Encryption](https://ironcorelabs.com/docs/data-control-platform/concepts/transform-encryption/). Each time data is transformed during the decryption process, there is the possibility for the Data Control Platform to be unable to perform the transformation if access has been revoked. This failure might occur if a User has been removed from a Group, or a Device has been revoked by User.

This is incredibly powerful, as revocation can occur without modifying the encrypted data! That is, **changing the users who can decrypt data via a Scalable Encryption Group, or deleting a device attached to a user doesn’t require doing anything to encrypted data.** This concept - separation between encrypting data and deciding who can decrypt it, is known as [Orthogonal Access Control](https://ironcorelabs.com/docs/data-control-platform/concepts/orthogonal-access/).

## Data Control Patterns

The following are common patterns we’ve helped architects and developers implement in their systems and applications. This library will continue to grow as we continue to explore the applications of Data Control and CBAC. If you have implemented a pattern not represented here, please let us know!

### Adding Data Control To an App With Distinct Users

Use this pattern when:

- You are integrating a Data Control Platform SDK into a new or existing app with distinct users
- You are building a server-side app, but don’t want to maintain a `DeviceContext` secret

But don’t forget:

- You will need to be able to generate a Data Control Platform JWT for applications with user logins
- You can use [ironoxide-cli](https://github.com/IronCoreLabs/ironoxide-cli) to generate device keys for server-side apps or to just test things out

To use the Data Control Platform your app must have a Data Control DeviceContext. For most apps, devices should be generated dynamically via the CBAC User linked to a user login within your app. The process of linking your cryptographic identity (CBAC User) to your login identity is facilitated via a JWT (JSON Web Token) issued by your identity provider. This JWT is an assertion that your app has successfully authenticated the current user, and it securely provides that user’s identity in the form of a user ID. If you are using a third party identity provider (Auth0, Google, Microsoft, etc), they will have a way of issuing a JWT (sometimes called an ID token) for the currently logged in user. You will need to add some “custom claims” to the provider’s JWT. There is more information on the JWT format and the claims in the [How It Works](https://ironcorelabs.com/docs/data-control-platform/how-it-works/jwts/) section.

If you are doing your own authentication, you will need to add an endpoint on your backend that can generate a JWT that asserts the identity of the current user. See the [Generating a User Identity Assertion](https://ironcorelabs.com/docs/data-control-platform/guide/#generating-a-user-identity-assertion) pattern for more information and an example implementation.

#### Option 1: Data Control Platform SDK Managing the DeviceContext

Some Data Control Platform SDKs offer the option of letting the SDK manage the `DeviceContext` internally, freeing the app from dealing with any device secrets. In these cases, the SDK is taking advantage of platform-specific secure storage mechanisms, and the `DeviceContext` will be stored and retrieved as needed.

Here’s a basic flow of creating a CBAC User where the app wants to delegate `DeviceContext` storage to the Data Control Platform SDK.

Your AppSDKIdentity ProviderUser LoginjwtCallbackGET JWTGenerate JWT with DCP Custom ClaimsJWT for Logged In UserSDK.initialize(jwtCallback, passwdCallback)Create UserCreate DeviceStore Device in Platform's Secure StorageSDK handleYour AppSDKIdentity Provider

```jsx
import {initialize, ErrorCodes, document} from "@ironcorelabs/ironweb";

/**
 * Request the endpoint on your server that we setup above. Since we setup the endpoint
 * above to return the token in plaintext, we need to parse the response as such.
 */
function getJWTForUser() {
    //The fetch() API returns a Promise which we can then chain to parse out
    //the plaintext content of the response, which will be a valid JWT.
    return fetch("/generateJWT").then((response) => response.text());
}

/**
 * Request the user's password. This function will only be called if the user
 * has not authorized this browser to access their data.
 */
function getUsersPassword(didUserExist) {
    //The `didUserExist` argument is a boolean parameter which gives you the opportunity
    //to alter the user experience for newly created/synced users.
    return new Promise((resolve) => {
        const password = window.prompt("Please provide your secure password.");
        resolve(password);
    });
}

/**
 * Initialize the IronWeb SDK with the two provided callbacks. The Promise will resolve
 * once successful at which point the document/group/user methods are available to be
 * invoked
 */
initialize(getJWTForUser, getUsersPassword)
    .then((initResult) => {
        //initResult.user.id is the initialized user
        return document.list();
    })
    .then((userDocs) => {
        //Show user a list of their documents
    })
    .catch((sdkError) => {
        if (sdkError.code === ErrorCode.USER_PASSCODE_INCORRECT) {
            alert("Wrong password provided! Please try again");
        }
    });
```

#### Option 2: Direct Management of the DeviceContext

Some platforms do not have a standard secure storage mechanism. If this is the case, or if you would rather handle the storage of the `DeviceContext` secret within your app, the Data Control Platform SDKs provide APIs to directly create users and devices.

A cryptographic identity (CBAC User) only needs to be linked to the logged in user once per account. Once the Data Control Platform knows about a user, the next step is to create a device that is securely associated with the user. If your app has a secure place to store the `DeviceContext`, device generation can be skipped on subsequent logins, but it is also possible for devices to be created as needed. Reusing a `DeviceContext` is preferred to reduce start-up/login time for your app.

Here’s the basic flow of creating a CBAC User with the Data Control Platform SDK where the caller wants to handle the storage of the DeviceContext. Future sessions would skip user and device creation and reuse the DeviceContext. If the user keys have already been created but a new device is needed, just the generateNewDevice portion would be needed.

Your AppSDKIdentity ProviderUser LoginGET JWTGenerate JWT with DCP Custom ClaimsJWT for Logged-in UserUser.create(jwt, password)User CreatedUser.generateNewDevice(jwt, password)DeviceContextSecurely store DeviceContextinitialize(deviceContext)SDK handleYour AppSDKIdentity Provider

```rust
use ironoxide::prelude::*;

const USER_PASSWORD: &str = "get password from user";

pub async fn init_sdk_with_config(config: &IronOxideConfig) -> Result<IronOxide, IronOxideErr> {
    IronOxide::user_create(&get_jwt(), USER_PASSWORD, &UserCreateOpts::default(), None).await?;
    let device = IronOxide::generate_new_device(
        &get_jwt(),
        USER_PASSWORD,
        &DeviceCreateOpts::default(),
        None,
    )
    .await?;
    ironoxide::initialize(&device.into(), config).await
}

#[tokio::main]
async fn main() -> Result<(), IronOxideErr> {
    let sdk = init_sdk_with_config(&IronOxideConfig::default()).await?;
    // use sdk
    Ok(())
}

fn get_jwt() -> String {
    // call out to identity provider or your back-end to get a DataControl JWT
    unimplemented!()
}
```

#### A Note About Passwords

In both flows a password is required. This password is used to protect the CBAC User’s private key. Unlike most passwords, this is not something that can be easily reset or recovered. If a lost password is a possibility, particularly anytime end-user input is required, we highly recommend using one of the [Password Recovery Patterns](https://ironcorelabs.com/docs/data-control-platform/guide/#password-recovery-tokens).

### Password Recovery Tokens

Password recovery tokens allow a user to securely recover from a forgotten password protecting their CBAC User’s private key.

Use this pattern when:

- A user provided private key escrow password gates access to cryptographic data.

But don’t forget:

- If the user forgets their password and loses their recovery token, they are locked out. Make sure this is made clear and make it easy for them to download or otherwise store the recovery token.
- You will need to have a way of storing an `encrypted_password` associated with the user.
- If the user changes their password, a new recovery token must be generated and given to the user. The user must understand that the old token has expired and the new one should be saved.

If end users are going to create and input a private key password it would be wise to have some way to handle the user losing or forgetting that password. If you don’t have an alternative and the user forgets their password, they would be unable to decrypt their data.

One way to handle this (lifted from two factor authentication flows) is a recovery token. The user is given a generated token displayed to them one time, usually when they first create their password. That token can be used as a fallback, letting them securely access their private key, which can be used to reset their password.

This token is actually a randomly generated key that has been used to encrypt a stored version of their password. The recovery process involves using this token to decrypt their password, which can then be used to log them in.

The basic flow to create a recovery token is:

1. Start with a user’s `password`
2. Generate a new crypto key (`key`) and random initialization vector, or IV (`iv`)
3. Use `key` and `iv` to encrypt the password, producing `encrypted_password`
4. Export the `key` as `recovery_token`
5. Store the `iv` on the front of the `encrypted_password` bytes and persist it (e.g. save it to a database).
6. Provide the user with the `recovery_token`

> This token creation process must be repeated any time the user’s password changes.

The flow to use a recovery token is:

1. Request the `recovery_token` from the user
2. Retrieve the `encrypted_password` from the database
3. Import/reconstitute the `key` from the `recovery_token`
4. Split the `iv` from the front of the `encrypted_password`
5. Use the `iv` and the `key` to decrypt the `encrypted_password`
6. Log the user in with their decrypted password and make them change their password (creating a new token in the process)

#### Diagram

Your UIYour App LogicYour ServiceCapture user pwdCreate encrypted pwd & recovery tokenPersist encrypted pwdDisplay recovery token to userUser forgets pwdCapture user recovery tokenRequest encrypted pwdReturn encrypted pwdDecrypt pwd using recovery keyLog user in with decrypted pwdForce pwd change & recreate recovery tokenYour UIYour App LogicYour Service

#### Code Sample

```jsx
const AES_ALGO = "AES-GCM";
const AES_KEYLEN = 256;
const KEY_FORMAT = "raw";
const KEY_PERM = ["encrypt", "decrypt"];
const IV_LEN = 12;

const toByteArray = (s) => new TextEncoder().encode(s);
const fromByteArray = (b) => new TextDecoder().decode(b);

const exportRecoveryToken = (key) =>
    window.crypto.subtle
    .exportKey(KEY_FORMAT, key)
    .then((rawKey) => new Uint8Array(rawKey));

const importRecoveryToken = (token) =>
    window.crypto.subtle.importKey(KEY_FORMAT, token, AES_ALGO, true, KEY_PERM);
/**
 * Encrypt a password to a random key and iv combination. The encrypted password should be
 * persisted and the combination of the key and iv will be provided to the user as a one
 * time use recovery token to unlock it in the future.
 */
const encryptPasswordWithRecoveryToken = (password) =>
    window.crypto.subtle.generateKey({name: AES_ALGO, length: AES_KEYLEN}, true, KEY_PERM)
    .then((key) => {
        const iv = window.crypto.getRandomValues(new Uint8Array(IV_LEN));
        const encryptPass = window.crypto.subtle.encrypt(
            {name: AES_ALGO, iv},
            key,
            toByteArray(password)
        );
        const exportToken = exportRecoveryToken(key);
        return Promise.all([encryptPass, exportToken, Promise.resolve(iv)])
            .then(([encryptedPassword, recoveryToken]) => ({
                recoveryToken: new Uint8Array(recoveryToken),
                encryptedPassword: new Uint8Array([
                    ...iv,
                    ...new Uint8Array(encryptedPassword)]
                ),
            }));
    });

/**
 * Decrypt a user's password using their recovery token. Once this is done you should force a
 * password change to generate and save a new recovery token.
 */
const decryptPasswordWithRecoveryToken = (recoveryToken, encryptedPassword) =>
    importRecoveryToken(recoveryToken)
        .then((key) => {
            const iv = encryptedPassword.slice(0, IV_LEN);
            const encryptedPass = encryptedPassword.slice(IV_LEN);
            return window.crypto.subtle.decrypt({name: AES_ALGO, iv}, key, encryptedPass);
        })
        .then((password) => fromByteArray(new Uint8Array(password)));

async function testRecovery() {
    const {encryptedPassword, recoveryToken} =
        await encryptPasswordWithRecoveryToken("supersecretpassword");
    console.assert(encryptedPassword !== undefined);
    console.assert(recoveryToken !== undefined);
    // We recommend serializing/deserializing with base64 for user interaction.
    console.log(`Recovery token: ${recoveryToken}\n Encrypted password: ${encryptedPassword}`);

    const password = await decryptPasswordWithRecoveryToken(recoveryToken, encryptedPassword);
    console.assert(password === "supersecretpassword", "decrypted password didn't match input");

    console.log(`Successfully encrypted then decrypted "${password}" using a recovery token.`);
}
```

### Generating a User Identity Assertion

As we have mentioned, the Data Control Platform maintains a cryptographic identity for each registered user, but it does not handle authentication for users. Your application already needs to do that, so we want to rely on your existing authentication mechanism as much as possible. In order to use a Data Control Platform SDK in a client-side app, where the user identity is not hard-coded in a *DeviceContext*, you need to provide a way for your application to assert the identity of the currently authenticated user.

If your application has a client-side front end that talks to one or more back end services, then you have hopefully already built a mechanism for the front end to prompt users for some sort of credentials, then to validate those credentials with the back end, establishing the user's identity. Chances are good that as part of that process, you create a *session*, which represents that user's authentication with the application. The session typically has an ID that is transmitted to the back end by the client application with each request to communicate who is making the request and to facilitate processes like permission checking.

Since you already have this session in place, and hopefully your application has implemented appropriate protections against cross-domain attacks, we ask you to extend your back end service slightly to allow it to generate an assertion of the identity for the user associated with a session. If you add an endpoint to your back end that will generate the assertion and add a method to your front end that will hit that endpoint to fetch the assertion, your session tracking mechanism will allow you to correctly identify the user.

We use the *JSON Web Token (JWT)* standard for the assertions - more details on the JWT and the format of the JWT we expect are available [here](https://ironcorelabs.com/docs/data-control-platform/how-it-works/jwts/).

Use this pattern when:

- You have a front end application that talks to a back end service
- You have a protocol to authenticate users and establish a session
- You can add an endpoint to your back end service and make a call to that endpoint from the front end

But don't forget:

- You need to be able to supply a function to the Data Control Platform SDK that will make the call to the endpoint and return the JWT
- You need to add a dependency to your back end service that will generate the JWT
- You need to make a cryptographic signing key available to that package that can sign the JWT
- You need some other information from the IronCore admin console to populate the JWT - you must provide a project ID, a segment ID, and an identity assertion key ID

#### Adding a JWT Endpoint

First, let's look at how you might add an endpoint to your back end in order to generate a JWT containing the authenticated user's identity. This simple example shows an HTTP server implemented in Node.js, using the `express` framework and the `express-session` package to manage sessions. This package generates a GUID for each new session and manages an in-memory session data store. We just store and retrieve the user name associated with each session.

This simple example includes a `/login` endpoint on the server to establish a session, a `/generateJwt` endpoint to retrieve an assertion for the authenticated user, and the root endpoint `/` which downloads a simple app that demonstrates how this works in the client.

```jsx
/******************************************************************************
 * Example to illustrate generating the JWTs that assert user identity for use
 * in initializing the IronCore Data Control Platform SDKs.
 *
 * This is a very simple server written in node, using the Express framework,
 * to demonstrate the ideas. The server includes a login endpoint that does
 * not actually validate the supplied password, but does capture the user ID /
 * login and creates a session. For simplicity, the example utilizes the
 * express-session package to create a session ID for each new session, manage
 * the session IDs (using an in-memory store), and store and retrieve session
 * data. A real application would have actual authentication and authorization
 * checks, and it would use a more durable session persistence mechanism, but
 * this will demonstrate the key ideas with a very straightforward example.
 *
 * This simple server also serves up a very simple single-page app to run in
 * a browser. It just prompts for user login and password, hits the server's
 * login endpoint to "authenticate" and establish a session, then uses the
 * server's endpoint to generate a JWT and feed that into the ironweb SDK to
 * initialize it.
 *****************************************************************************/

const express = require('express')
const session = require('express-session')
const parseurl = require('parseurl')
const fs = require("fs");
const path = require("path");
const jwt = require("jsonwebtoken");

/* Retrieve the configuration for the IronCore application from the file
 * ./ironcore-config.json. This file contains the project, segment, and
 * identity assertion key IDs needed to generate JWTs for the IronWeb SDK
 * embedded in the client webapp.
 *
 * Also retrieve the private signing key for the specified identity
 * assertion key ID from the file ./private.key. This is the actual key
 * that will be used to sign JWTs.
 */
const ironCoreConfig = require("./ironcore-config.json");
const privateKey = fs.readFileSync(path.join(__dirname, "private.key"), "utf8");


// Create an Express server and configure it to generate and track sessions using
// the default MemoryStore persistence mechanism. The session ID will be set in
// a cookie named sessionId
var app = express()

app.use(session({
    secret: 'crazed wombat attacks koala',
    name: 'sessionId',
    resave: false,
    saveUninitialized: true
}))

// Configure the server to parse POST bodies in URL-encoded and JSON formats.
app.use(express.json())
app.use(express.urlencoded({ extended: true }))

// Add a login route to the server that accepts a post body containing login
// and password fields. The password is currently ignored - this is where you
// would actually authenticate the user. Once the user is authenticated, store
// the login for the authenticated user in session storage and return a message
// indicating that the user is logged in.
app.post('/login', function (req, res) {
    console.log('Login for ' + req.body['login'])
    req.session.userId = req.body['login']
    res.end("User " + req.session.userId + " logged in.")
})

// Add a generateJwt route to the server that checks to make sure the request
// included a recognized sessionId and that the userId was successfully retrieved
// from the session store. If so, generate a Jwt asserting the user's login and
// the IronCore parameters from the configuration.
app.get('/generateJwt', function (req, res) {
    console.log('generateJwt')
    if (!req.session.userId || 0 === req.session.userId.length) {
        // If we didn't get a valid userId from the session, return a 401.
        res.status(401).send("Missing or invalid session")
    } else {
        const token = jwt.sign(
            {
                pid: ironCoreConfig.projectId,
                sid: ironCoreConfig.segmentId,
                kid: ironCoreConfig.serviceKeyId,
            },
            privateKey,
            {
                algorithm: "ES256",
                expiresIn: "2m",
                subject: req.session.userId,
            }
        )
        res.end(token)
    }
})

// The root route will download the example Javascript app
app.get('/', function(req,res) {
    res.sendFile(path.join(__dirname, 'index.html'))
})

app.listen(3003)
console.log('Server listening on port 3003.')
```

#### Using the JWT Endpoint

This is a portion of the example app that is loaded from our sample server. The page just has a login button, which when pressed displays a simple login form. After the user name and password are entered and the "Go" button is pressed, the credentials are submitted to the server's `/login` endpoint. If authentication is successful, the server creates a session and sets the session ID in a cookie. In the client, once it gets this far, it initializes the IronWeb SDK using a function that fetches a user identity assertion from the server's `/generateJwt` endpoint. If all these steps complete successfully, the SDK is initialized and your app is ready to do any operations that require the SDK to protect sensitive data.

```html
    <!-- Add a button to open a modal login form -->
    <button id="loginButton" onclick="document.getElementById('loginform').style.display='block'">Login</button>

    <!-- Modal login form -->
    <div id="loginform" class="modal">
      <span onclick="document.getElementById('loginform').style.display='none'"
    class="close" title="Close Login">&times;</span>

      <!-- Modal Content -->
       <form class="modal-content" action="" method="get" onsubmit="event.preventDefault();callLogin()">
        <label for="login"><b>Username</b></label>
        <input id="loginField" type="text" placeholder="Username" name="login" required>

        <label for="password"><b>Password</b></label>
        <input id="passwordField" type="password" placeholder="Password" name="password" required>

        <input type="submit" value="Go">
      </form>
    </div>

    <div id="continueText" style='display:none'>
       The IronCore SDK is initialized - you can now use it to encrypt and decrypt data.
    </div>

    <script>
      /* This function just hits the server's generateJwt endpoint and
         lets the session handling mechanism do the rest. Server should
         return a JWT asserting the authenticated user's identity, or
         an error. */
      function fetchJwt() {
        return fetch('/generateJwt')
          .then(response => response.text())
          .catch(e => {
            console.log('Error fetching JWT - ' + e)
            return ''
          })
      }

      /* This is where you would add a way to return a promise that resolves
         to the password to use to encrypt the user's master private key if
         the IronCore SDK needs to create one for the user.  */
      function getPassword() {
        return Promise.resolve('TEST_PASSWORD')
      }

      /* This is hooked to the Login button - get the user name and password,
         POST to the /login endpoint on the server, then when the response
         comes back, attempt to initialize the IronCore SDK.  */
      function callLogin() {
        var loginVal = document.getElementById('loginField').value
        var passwordVal = document.getElementById('passwordField').value
        var loginData = JSON.stringify({login : loginVal, password : passwordVal})
        fetch('/login', {
          method: 'POST',
          mode: 'same-origin',
          headers: { 'Content-Type': 'application/json' },
          body: loginData
        }).then(response => {
          if (response.status !== 200) {
            alert('Error logging in - status code ' + response.status)
          } else {
            ironweb.initialize(fetchJwt, getPassword).then(response => {
              //  Hide the login dialog and button
              document.getElementById('loginform').style.display='none'
              document.getElementById('loginButton').style.display='none'
              document.getElementById('continueText').style.display='block'
            })
          }
        }).catch(function(err) {
          alert('Fetch error ' + err)
        })
      }

    </script>
```

#### Alternative Solution

An alternative to creating an endpoint on your service that can generate a JWT asserting the current user's identity is to instead generate the JWT while processing the login request and return it in the response. Note that the JWT has a two minute lifespan, so this approach might not work for every client application. But if you are sure that the client will initialize the SDK within two minutes of completing the login sequence, this can be a simpler approach than adding the JWT endpoint.

### Encrypt Directly To Users

Encrypting a piece of data directly to a user allows any Device generated by that user to decrypt the data. The IronCore SDKs allow data to be encrypted to a list of one or more users, using the provided method to *encrypt a document*.

Use this pattern when:

- Sharing with a fixed, small number of users.
- Decryption performance is much more important than encryption performance.
- Revoking access needs to be more granular. Revoking access to a user does not affect anyone else.

But don’t forget:

- Additional design is required to ensure data is recoverable in the event the user’s private key is lost.
- Scaling to a large number of users is computationally expensive. The time it takes to share data scales linearly with the number of users.
- Adding new people requires an encrypt call for each shared document. This scales poorly if the number of documents shared is large.
- Granting access user by user requires an encrypt call for each user AND for each document. This can become very expensive with large numbers of documents

appSDKironcoreWebServiceEncrypt data to bobGet bob's public keyEncrypt dataStore encrypted AES keysReturn encrypted dataappSDKironcoreWebService

Example Code:

```rust
    let message = "This is my secret for a single user.";
    let encrypted_result = sdk
        .document_encrypt(
            message.as_bytes(),
            &DocumentEncryptOpts::with_explicit_grants(None, None, true, vec![user_id.into()]),
        )
        .await?;
```

### Encrypting to Group

Encrypting to a Scalable Encryption Group allows any CBAC User that is a member of that Group to decrypt the data.

Use this pattern when:

- The users that need to decrypt the data changes over time. Group membership can be changed at any time without affecting the encrypted data. Keep in mind that encrypting data to a group is completely decoupled from granting or revoking access for an individual CBAC User to decrypt data.
- There are more than one or two users that need to decrypt the same data. Scalable Encryption Groups allow you to encrypt data once and let (up to) thousands of users decrypt the data.
- There is a logical grouping of users that often need to have access to the same data.
- You want to be able to revoke access but may not be able to edit all copies of the encrypted file.
- The user you want to encrypt to doesn’t yet have keys.

But don't forget:

- Decrypting data encrypted to a Scalable Encryption Group is slightly more expensive than decrypting data encrypted directly to a User since there’s one extra transform operation needed. Both are very fast, but this might be an issue when decrypting on power constrained devices.
- Groups must be administered. It is strongly advised that at least three CBAC Users are designated as Group administrators to minimize the risk of all admins losing access to their private keys. Without a functioning Group Admin, a Scalable Encryption Group’s membership cannot be changed. Data can still be decrypted by current members, however.

appSDKironcoreWebServiceEncrypt data to groupGet group's public keyEncrypt dataStore encrypted AES keysReturn encrypted dataappSDKironcoreWebService

Example Code:

```rust
    let group_id = create_group(sdk).await?;
    let message = "This is my secret which a whole group should see.";
    let encrypted_result = sdk
        .document_encrypt(
            message.as_bytes(),
            &DocumentEncryptOpts::with_explicit_grants(None, None, true, vec![(&group_id).into()]),
        )
        .await?;
```

### Encrypting to Users and/or Groups via a Policy

Encrypting directly to a set of users and/or groups works when it's known ahead of time who should have access to decrypt the data. In many use cases, however, the data should be encrypted to a different set of users/groups depending on what type of data is being encrypted. For example, you might want to encrypt PII information that a user enters to a different group than the attachments that the user provides. This allows for finer grained access control of the data you want to protect.

But implementing this logic in your app can cause it to become brittle, confusing, and error prone. It's also logic that is likely to change repeatedly over time as more types of data are introduced into the system. Because of this, the Data Control Platform exposes a feature we call Policies. A Policy is a collection of data labels and rules that allow callers of the SDK to encrypt data to specific users and groups purely based on the type of data being encrypted. The Policy feature also supports the ability to swap out placeholder values in the names of groups based on who is encrypting the data. This allows for the creation of user-owned groups which can be used to support many GDPR/CCPA use cases.

For a deeper dive on how Policies work, read the [How It Works - Policies](https://ironcorelabs.com/docs/data-control-platform/how-it-works/policy/) page for more information.

Use this pattern when:

- The data you're encrypting in your app has varying classifications or sensitivities that control who should be able to decrypt the data.
- You have Data Governance requirements or want centralized control of the Policy rules for which data labels go to which users/groups to take the decision making out of developers hands.
- The ID of the groups that should have access to decrypt the data depend on the user who is encrypting the data.

But don't forget:

- It's still up to the app developer to call the SDK correctly to use a Policy when encrypting data.
- It's your responsibility to keep the data labels in your application synced with the data labels in the Policy. If these labels get out of sync you can have data that gets encrypted to the wrong entity or cannot be matched when trying to evaluate the Policy.

appSDKironcoreWebServiceEncrypt data via PolicyMatch Policy to rulesReturn matching user/groups public keysEncrypt dataStore encrypted AES keysReturn encrypted dataappSDKironcoreWebService

Example Code:

```rust
    let message = "this is my secret which has some labels.";
    let data_labels = PolicyGrant::new(
        Some(Category::try_from("PII")?),
        Some(Sensitivity::try_from("PRIVATE")?),
        None,
        None,
    );
    let encrypted_result = sdk
        .document_encrypt(
            message.as_bytes(),
            &DocumentEncryptOpts::with_policy_grants(None, None, data_labels),
        )
        .await?;
```

### Decrypting as Needed

There are multiple ways your application could handle decryption timing, but the most straightforward one is decrypting as needed. In this case, decryption of a document occurs as late as possible, only happening when specifically requested. For example, if a user were presented with a list of encrypted documents, the documents would remain encrypted until the user selects one. The decryption would then take place on the user’s device, and the decrypted contents would then be displayed. By not preprocessing the decryption, the user has complete control over which documents get decrypted and know exactly when the decryption takes place.

Use this pattern when:

- You have a large number of documents but don’t need them decrypted all at once.
- You want strict control of what documents are decrypted and when.
- You are supporting devices with lower performance where decryption may take additional time.
- You want to limit the number of calls you make to the IronCore Web Service.

But don’t forget:

- There may be some perceived latency for the user while the application makes the decryption call.

Your AppData Control SDKICL Web ServiceDecrypt documentGet EDEKTransform EDEK to deviceReturn EDEKDecrypt documentReturn decrypted documentYour AppData Control SDKICL Web Service

Example Code:

```rust
    let encrypted_doc = documents.get(doc_index).expect("Index out of range.");
    let id = sdk.document_get_id_from_bytes(encrypted_doc)?;
    println!("Decrypting document with ID {}\n", id.id());
    let decrypted_doc = sdk.document_decrypt(encrypted_doc).await?;
```

### Lost, Stolen, or Exposed Device

As [mentioned above](https://ironcorelabs.com/docs/data-control-platform/guide/#devices/), each device has its own set of cryptographic keys, and a device is the only entity that is able to decrypt data. This gives devices a lot of power in a CBAC application but is also the source of their flexibility because data is never encrypted directly to a device. This gives devices the ability to be short lived and easily rotated. If a device key is accidentally deleted, generating a new one is a simple, low-cost change that doesn't require updating any encrypted data.

Independent devices can and should be created for all of the various locations where data needs to be decrypted. For an actual user, this means creating a separate set of device keys for their phone, tablet, and laptop. For the scenario where the "user" is a [service account](https://ironcorelabs.com/docs/data-control-platform/guide/#service-accounts), this can mean creating a separate set of device keys for every Docker container running your service. Multiple devices also greatly improve auditability. Instead of all audit logs showing operations performed by a user, it is possible to track down which of the user's specific devices was used for an SDK operation.

But what if a set of device keys is lost, stolen, or otherwise compromised? Phones can be stolen, plaintext keys end up in Git, users forget to logout on public computers, etc. In traditional applications, once you've lost the keys, you've lost the kingdom and have to generate a new key and re-encrypt all of the data. Not so with the Data Control Platform. When the keys for one of a user's devices becomes compromised, the user can easily remotely revoke those keys using any other valid device. Revocation of a device makes it mathematically impossible to perform any SDK operations using the keys that were on that device, thus making the device inert. Attackers might have your device, but they won't be able to do anything with it.

Practically speaking, a revoked device will be unable to initialize a Data Control SDK and thus unable to perform any cryptographic operations such as decrypting data and modifying groups, but even if a sophisticated user were able to coerce the Data Control SDK to accept the device on initialization, there would be no way, mathematically, to decrypt data. Device revocation should also be used to periodically rotate device keys as a form of good cryptographic hygiene. Given the low cost and scalability of devices, pre-emptive rotation can help ensure the security of your data.

Use this pattern when:

- A device key may have been compromised
- Policy or user action warrants deauthorizing a specific device or browser session
- A User logs out of a Data Control app

But don’t forget:

- Creating a new device requires access to a newly-issued Data Control JWT and CBAC User password or an Identity Assertion Key from the IronCore Admin Console
- A device is necessary to perform cryptographic operations with the Data Control SDK, including decrypting data

Your AppData Control SDKICL Web ServiceUser.deleteDevice(deviceId)DELETE devices/idDelete transform key from User to DeviceDelete ConfirmationDelete ConfirmationYour AppData Control SDKICL Web Service

### Service Accounts

While the Data Control Platform makes it possible to implement full end-to-end encryption in your application, there can still be many use cases where you need to be able to perform cryptographic operations on the server outside of the context of a specific user. This could be because you're incrementally adding encryption to your application but you're not quite ready to implement full end-to-end encryption support (if you have this use case, you might consider our [Customer Managed Keys solution](https://ironcorelabs.com/docs/saas-shield/)). Alternatively, you might need an automated backend process to be able to decrypt and process encrypted data or manage your users and groups. For these use cases the best approach is to create a service account.

Users in the Data Control Platform can be very flexible. There's no requirement that they be tied to an actual user in your system. All that is required to create a user is a unique ID and a private key escrow password. For service accounts, they should be created manually in advance. This can be done via the [IronOxide CLI](https://github.com/IronCoreLabs/ironoxide-cli) tool, which can be used by administrators to create users and manage your groups.

Once a new service user account is created, the only configuration that you need to make available for your server-side process is a set of device keys. A valid set of device keys is necessary to initialize the Data Control Platform SDKs and make server-side use cases easier as you don't have to deal with generating JWTs for your service account. As mentioned above, generating device keys should be considered a low-cost operation, so it's best practice to generate more than one device key for your service account. For example, if you have multiple Docker containers that run your service, a separate set of device keys should be used for each Docker instance. This makes audit trails more granular and revocation less disruptive. Similarly, if you have multiple backend processes that need to perform Data Control Platform operations, it's best practice to create separate service accounts.

Use this pattern when:

- You have a server-side process that isn't tied to an actual user that needs the ability to use the Data Control Platform SDKs

But don’t forget:

- This service account should only have the permissions necessary to perform its required tasks. For example, don't give this account group member permissions if it only needs to be able to manage a group's membership.
- You'll need to think about how best to split up separate service accounts vs. separate devices. You'll need to balance the ability to get rich audit trails for each operation with the ability for coarse or granular device revocation if device keys are compromised.

## Encrypted Search Patterns

The following implementation patterns illustrate how you can use the IronCore SDKs in your application to do short substring searches of data that is end-to-end encrypted.

The current examples all use IronCore's `IronOxide` SDK and are implemented using the Rust programming language. However, the `IronWeb` SDK, which is intended for use in web applications written in Javascript, also supports all the encrypted search functions. The functionality is similar - you can find the details in the documentation on the search functions in `IronWeb` [here](https://ironcorelabs.com/docs/data-control-platform/javascript/search/).

### Creating an Index

The first step in updating your application to support encrypted search is to create the index that will be used to protect the privacy of the index data. This index encapsulates the information necessary to generate *index tokens* from strings. The tokenization process extracts *index terms* from a string, and for each of those terms, it prepends an optional partition name that is provided by your application, then uses a secret salt value (which functions as the secret key) to generate a 32 bit integer *index token*. As long as that salt remains secret, an attacker cannot create a rainbow table of entries for a given partition name. Our SDKs simplify the process of generating and protecting this salt value, providing a `create_blind_index` method to handle the details.

There is some setup work that must be done before you can create the index. In order to use an index to process new or updated data or to search, you must be able to access the salt. We protect that salt value using our transform cryptography solution, allowing you to manage which people should have access easily. You just create a [scalable encryption group](https://ironcorelabs.com/docs/data-control-platform/guide/#scalable-encryption-groups) that includes all the users that can enter protected data or need to search for protected records. The ID of this group must be provided when you create a new index. Because IronCore’s transform cryptography supports [orthogonal access control](https://ironcorelabs.com/docs/data-control-platform/concepts/orthogonal-access/), you don’t need to have all the users assigned to the group before you can use it to generate your search index.

The IronCore SDK's `create_blind_index` method takes the group ID as input, and it generates a random value for the salt, encrypts that value to the group whose ID you provided, and returns the encrypted salt. Your application is responsible for storing that encrypted salt and for providing it when initializing the blind index for future use.

ClientSDKICLBackendCreate Blind Index(groupId)Get public key(groupId)public key for groupgenerate random saltencrypt salt to groupencrypted saltsave encrypted saltOKClientSDKICLBackend

#### Using IronOxide

This example uses the `IronOxide` SDK's [`group_create`](https://docs.rs/ironoxide/latest/ironoxide/struct.IronOxide.html#method.group_create) and [`create_blind_index`](https://docs.rs/ironoxide/latest/ironoxide/struct.IronOxide.html#method.create_blind_index) methods to set up the group that will be used to protect your blind index's salt and to create the index.

Note: the [`EncryptedBlindIndexSalt`](https://docs.rs/ironoxide/latest/ironoxide/search/struct.EncryptedBlindIndexSalt.html) that is returned implements serialization using the `serde` package, which allows you to decide which of the serde-supported formats to use for serialized value. This example uses JSON for the at-rest representation of the encrypted salt.

This code assumes that there is an initialized instance of the IronOxide SDK available as the object `sdk`.

```rust
    create_group(&sdk, &salt_group_id, "PII Search").await?;
    let encrypted_salt = sdk.create_blind_index(&salt_group_id).await?;
    let encrypted_salt_str = serde_json::to_string(&encrypted_salt)?;
    save_encrypted_salt_to_app_server(encrypted_salt_str);
```

The group ID is created from a string by doing a call like this: `rust let salt_group_id = GroupId::try_from("indexedSearchGroup")?;`

and the function to create a group is

```rust
async fn create_group(
    sdk: &IronOxide,
    group_id: &GroupId,
    name: &str,
) -> Result<GroupCreateResult> {
    let opts = GroupCreateOpts::new(
        Some(group_id.to_owned()),                   // ID
        Some(GroupName::try_from(name.to_owned())?), // name
        true,                                        // add as admin
        true,                                        // add as user
        None,                                        // owner - defaults to caller
        vec![],                                      // additional admins
        vec![],                                      // additional users
        false,                                       // needs rotation
    );
    let group = sdk.group_create(&opts).await?;
    Ok(group)
}
```

### Preparing an Index for Use

Once you have created a blind index and stored the encrypted salt, your application can start using the index to process new records, update existing records, or search for records. You first need to retrieve the `EncryptedBlindIndexSalt` that you initially created and serialized. Once you have retrieved the value and deserialized it, you can initialize the index for use, using the `initialize_search` method, which takes the encrypted salt and uses the SDK to decrypt it.

ClientBackendSDKICLfetch encrypted saltencrypted saltInitialize SDKOKInit Blind Index(encryptedSalt)transform(encrypted salt keys)transformed keysdecrypt keysdecrypt saltOKClientBackendSDKICL

#### Using IronOxide

This example uses the SDK's [`EncryptedBlindIndexSalt`](https://docs.rs/ironoxide/latest/ironoxide/search/struct.EncryptedBlindIndexSalt.html) object and its [`initialize_search`](https://docs.rs/ironoxide/latest/ironoxide/search/struct.EncryptedBlindIndexSalt.html#method.initialize_search) method to set up a `blind_index` object for further use in your application. It assumes that there is an initialized instance of the IronOxide SDK available as the object `sdk`.

```rust
    let encrypted_salt_str = get_encrypted_salt_from_app_server();
    let encrypted_salt: EncryptedBlindIndexSalt = serde_json::from_str(&encrypted_salt_str)?;
    let blind_index = encrypted_salt.initialize_search(&sdk).await?;
```

### Indexing New Data

Assuming your application has executed the steps shown above in the pattern [Preparing an Index for Use](https://ironcorelabs.com/docs/data-control-platform/guide/#preparing-an-index-for-use), and that the `sdk` and `blind_index` are available in your application at the point where you have a new record that has a sensitive data field, you can use code like the following to index the data using the `tokenize_data` method, then use the SDK to encrypt the field. Let’s assume that you have a `customer` struct that contains a field `name`, a string that contains PII, and that you want to make the customer data available to the *customerService* group. We’ll assume that group has already been created.

In this example, after the customer name has been encrypted, the resulting bytes are base64 encoded, and this string replaces the customer name in the struct. In order to simplify the back end service, a new field `name_keys` is assumed to be added to the `customer` record to hold the EDEKs that are needed to decrypt the encrypted name field. Once the index tokens are generated and the PII in the customer record is encrypted, the index tokens and the customer record can be sent together to the back end service for storage.

ClientSDKBackendtokenize data(blind index, PII)token setencrypt data(PII, users and groups)encrypted PII, EDEKsupdate record with encrypted PII, EDEKssave data(record, token set)save recordsave token set for recordOKClientSDKBackend

#### Using IronOxide

This example uses the `IronOxide` SDK's [`tokenize_data`](https://docs.rs/ironoxide/latest/ironoxide/search/struct.BlindIndexSearch.html#method.tokenize_data) and [`document_encrypt_unmanaged`](https://docs.rs/ironoxide/latest/ironoxide/struct.IronOxide.html#method.document_encrypt_unmanaged) methods to prepare a customer record to be saved. It assumes that there is an initialized instance of the IronOxide SDK available as the object `sdk`.

```rust
    let name_tokens = blind_index
        .tokenize_data(&customer.name, None)?
        .into_iter()
        .collect::<Vec<u32>>();
    let encrypt_opts = DocumentEncryptOpts::with_explicit_grants(
        None,                  // document ID - create unique
        None,                  // document name
        false,                 // don't encrypt to self
        vec![group_id.into()], // users and groups to which to grant access
    );
    let enc_name = sdk
        .document_encrypt_unmanaged(customer.name.as_bytes(), &encrypt_opts)
        .await?;
    // Replace name with encoded encrypted version. Also need to store EDEKs to decrypt name.
    customer.name = base64::encode(enc_name.encrypted_data());
    customer.name_keys = base64::encode(enc_name.encrypted_deks());
    save_customer(customer.clone(), &name_tokens, &[]);
```

### Updating an Indexed Field

If you have implemented indexing of the sensitive fields in new records before you encrypt them, as described in [Indexing New Data](https://ironcorelabs.com/docs/data-control-platform/guide/#indexing-new-data), you will probably need to handle the case where a record is being updated, and one of the sensitive fields that has been indexed is changed. On the front end, handling this follows much the same process as you used for a new record: generate the index tokens for the new value of the field, encrypt the new value, then send the updated record and the new tokens to the back end for storage. However, on the back end, you do need to perform an additional step with the index tokens; before you save the new index tokens associated with the record, you should delete the old set of index tokens for that record. Once the old tokens are deleted, save the new set of tokens the same way you would for a new record, then update the actual record data with the new values.

ClientSDKBackendtokenize data(blind index, new PII)token setencrypt data(new PII, users and groups)encrypted PII, EDEKsupdate record with encrypted PII, EDEKsupdate data(record, token set)delete old tokens for recordupdate recordsave token set for recordOKClientSDKBackend

### Searching Using an Index

Once your application has started indexing the values in sensitive fields then encrypting them, you can add the capability to search on the contents of those fields. For our example, if a user of your application wants to search for a customer by name, or some part of the name, your application just extracts a set of index tokens from the search string and using the `tokenize_query` method, then it uses those tokens to find matching records in your back end service. Given the set of index tokens generated by a search query, any record whose set of index tokens is a superset of the search tokens (i.e. it contains every one of the tokens) is a possible match.

Once your back end has found potential matches and returned the records to the front end, your application needs to make sure that some of the matches are not *false positives*. To do that, it must use the IronCore SDK to decrypt the field in each record, then confirm that the field does actually match the search query. To do this, the application should apply the `transliterate_string` method to the decrypted field and to the search query. It should take the transliterated search query, split it into words on white space, and check the transliterated string to ensure that it contains each of the words from the query within it somewhere. For example, suppose your search query was "bei foo". The following strings would match: "北亰 football" and "bei jing egg foo yung". Other returned records whose transliterated field does not contain "bei" and "foo" as substrings are false positives.

ClientUserSDKBackendget search queryquery stringtokenize query(blind index, query)token setget matching records(token set)matching recordstransliterate string(query)transliterated stringdecrypt data(record PII)decrypted PIItransliterate string(PII)transliterated PIIfilter transliterated PII(transliterated query)record(if match)loop[for each matching record]search completeClientUserSDKBackend

#### Using IronOxide

This example uses the `IronOxide` SDK's [`tokenize_query`](https://docs.rs/ironoxide/latest/ironoxide/search/struct.BlindIndexSearch.html#method.tokenize_query), [`transliterate_string`](https://docs.rs/ironoxide/latest/ironoxide/search/fn.transliterate_string.html), and [`document_decrypt_unmanaged`](https://docs.rs/ironoxide/latest/ironoxide/struct.IronOxide.html#method.document_decrypt_unmanaged) methods to process a user query, fetch matching records, and filter them to display the matching records for the user. It assumes that there is an initialized instance of the IronOxide SDK available as the object `sdk` and an instance of the [`BlindSearchIndex`](https://docs.rs/ironoxide/latest/ironoxide/search/struct.BlindIndexSearch.html) called `blind_index` that was initialized using the encrypted salt from the previous examples.

This function uses the `sdk` to take a search query, extract index tokens, retrieve customer records from the server that match those index tokens, and display the returned records that actually match the query string.

```rust
async fn display_matching_customers(
    sdk: &IronOxide,
    name_index: &BlindIndexSearch,
    query_str: &str,
) -> Result<()> {
    let query_tokens = name_index
        .tokenize_query(query_str, None)?
        .into_iter()
        .collect();
    let customer_recs = search_customers(&query_tokens);
    let trans_query = ironoxide::search::transliterate_string(&query_str);
    let name_parts: Vec<&str> = trans_query.split_whitespace().collect();
    for cust in customer_recs.iter() {
        let result = filter_customer(&sdk, &cust, &name_parts).await?;
        match result {
            Some(decrypted_name) => println!("{} {} matched query", cust.id, decrypted_name),
            None => println!("{} did not match query", cust.id),
        }
    }
    Ok(())
}
```

This is the function that actually checks an individual customer record, using the `sdk` to decrypt the name field and compare a transliterated verison of it against the transliterated query string to make sure they match.

```rust
async fn filter_customer(
    sdk: &IronOxide,
    cust: &Customer,
    name_parts: &Vec<&str>,
) -> Result<Option<String>> {
    let cust_enc_name = base64::decode(&cust.name)?;
    let cust_name_keys = base64::decode(&cust.name_keys)?;
    let dec_result = sdk
        .document_decrypt_unmanaged(&cust_enc_name, &cust_name_keys)
        .await?;
    let dec_name = std::str::from_utf8(&dec_result.decrypted_data()).unwrap();
    let dec_name_trans = ironoxide::search::transliterate_string(&dec_name);
    if name_parts
        .iter()
        .all(|name_part| dec_name_trans.contains(name_part))
    {
        Ok(Some(dec_name.to_string()))
    } else {
        Ok(None)
    }
}
```

Displaying a list of customers that match a query just uses those functions, like this.

```rust
    let query_str = get_search_query();
    display_matching_customers(&sdk, &blind_index, &query_str).await?;
```

### Using Multiple Indices

So far, our encrypted search patterns have considered a customer record that had a single field of PII, the customer name. Suppose you decide you need to protect a second field - the customer's email address, for example. You could use the same blind index to index and search both strings, but search performance will be better if you create a second blind index for the email address. You can just invoke the `create_blind_index` function twice, once to create an index for the names, and again to create an index for the emails. Since the indices use different salt values, knowledge about any of the contents of the set of index tokens created by one blind index won't provide any insight into the contents of the data protected by the second blind index.

Once you have created the two blind indices, you can use them to index data in your customer record before you encrypt it. Use one blind index to create the tokens for the name field and the other to create the tokens for the email field. We recommend that you maintain these two sets of index tokens separately - you could store them in the back end persistent store in two separate tables or key-value stores, or in the same store with a type discriminator.

You have some options for the actual encryption. You can encrypt each of the fields separately, producing separate encrypted data and EDEKs, or you could put the values into a single structure that you can serialize to a byte stream and encrypt as a single element. This could be a JSON object or a structure that is serialized using protobuf. In either case, you will need to store the encrypted data and the EDEKs, plus the two sets of index tokens.

One consideration of maintaining two separate indices is that you will need to understand the context when a user enters a search query - if the user enters a string that is a potential name, you should generate the index tokens using the name index and search for matches to those tokens in the store of name tokens. Likewise, if the user enters a string that is a potential email address, you would use the email index and search the store of email tokens. If the context of the search query is difficult to determine in your app, you do have the option of generating the index tokens for the query string using each of the blind indices, then searching both of the token stores for matches. This will likely generate additional false positives, but your client-side filtering should be able to handle that.

We do recommend that you use a single scalable encryption group to protect the salt for each of the blind indices. This will simplify the administration of the groups necessary to allow access to everyone who can generate or search data.

#### Using IronOxide

This example is similar to the one in the pattern [Creating an Index](https://ironcorelabs.com/docs/data-control-platform/guide/#creating-an-index), using the `IronOxide` SDK's [`create_blind_index`](https://docs.rs/ironoxide/latest/ironoxide/struct.IronOxide.html#method.create_blind_index) method to create two different indices. It assumes that there is an initialized instance of the IronOxide SDK available as the object `sdk`.

Creating the second index is straightforward. We assume we still have several objects that were created in the previous patterns.

```rust
    let encrypted_salt2 = sdk.create_blind_index(&salt_group_id).await?;
    let blind_index2 = encrypted_salt2.initialize_search(&sdk).await?;
```

Now suppose you have an instance of the IronOxide SDK available as the object `sdk`, and the two indices in `name_index` and `email_index`. It is easy to use the two blind indices to generate index tokens for the fields before encrypting. Like the pattern for [Indexing New Data](https://ironcorelabs.com/docs/data-control-platform/guide/#indexing-new-data), this code uses the SDK's [`tokenize_data`](https://docs.rs/ironoxide/latest/ironoxide/search/struct.BlindIndexSearch.html#method.tokenize_data) and [`document_encrypt_unmanaged`](https://docs.rs/ironoxide/latest/ironoxide/struct.IronOxide.html#method.document_encrypt_unmanaged) methods to prepare a customer record to be saved.

```rust
    // Generate the index tokens for the customer name and email address, then encrypt them
    let name_tokens = blind_index
        .tokenize_data(&customer2.name, None)?
        .into_iter()
        .collect::<Vec<u32>>();
    let email_tokens = blind_index2
        .tokenize_data(&customer2.email, None)?
        .into_iter()
        .collect::<Vec<u32>>();
    let enc_name = sdk
        .document_encrypt_unmanaged(&customer2.name.as_bytes(), &encrypt_opts)
        .await?;
    let enc_email = sdk
        .document_encrypt_unmanaged(&customer2.email.as_bytes(), &encrypt_opts)
        .await?;
    // Replace name and email with encoded encrypted versions. Also need to store EDEKs to decrypt both.
    customer2.name = base64::encode(enc_name.encrypted_data());
    customer2.name_keys = base64::encode(enc_name.encrypted_deks());
    customer2.email = base64::encode(enc_email.encrypted_data());
    customer2.email_keys = base64::encode(enc_email.encrypted_deks());
    save_customer(customer, &name_tokens, &email_tokens);
```

### Support in other languages

Remember, although the code samples in these patterns were all written in Rust using IronCore's `IronOxide`, the search functionality is available in our other SDKs as well. In particular, Javascript-based web applications that use the `IronWeb` SDK can access all the encrypted search functions. The details in the documentation on the search functions in `IronWeb` are available [here](https://ironcorelabs.com/docs/data-control-platform/javascript/search/).

