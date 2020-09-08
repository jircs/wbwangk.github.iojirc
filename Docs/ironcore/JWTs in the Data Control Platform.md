JWTs in the Data Control Platform

The IronCore Data Control Platform does not perform authentication and authorization; instead, it relies on the consuming application to perform those functions. The Data Control Platform does associate a cryptographic identity with an authenticated user; it requires the consuming application to supply a mechanism to generate a user identity assertion for the authenticated user in order to make that association. This mechanism is provided to the Data Control Platform SDKs to use when necessary.

The information that is included in the user assertion includes the following:

- The project ID associated with the application
- The segment ID associated with the user
- The user ID
- A timestamp that indicates when the assertion was generated
- A digital signature that is used to ensure that an application associated with the specified project actually generated the assertion

Administrators that register with the IronCore administration application (the admin console) create projects. The admin application assigns a unique non-negative integer ID to each project when it is created; this project ID can be viewed in the admin console.

Application developers can use segments to subdivide the users within a particular project. Each project must have at least one segment. A suggested segmentation is by tenant if the application is a multi-tenant SaaS app. The application provides a text string in the user identity assertion that uniquely identifies the segment within the project to which the user belongs. Each user must be associated with a segment.

Each user must have an identifier that is unique within the user's segment. The user identity assertion contains this user ID as a text string.

A user assertion is only considered valid if the timestamp it contains is within +/- two minutes of the current server time (to account for possible clock skew), and if the signature can be validated using a public key associated with the project. The admin console allows administrators to create identity assertion key pairs that are generated randomly for the project. A project can have any number of identity assertion keys; each one has a unique non-negative integer key ID. The administrator is expected to retain and protect the private part of the key pair, along with the key ID, while IronCore stores the public key as part of the data for the project. An application must have access to the key ID and private key in order to sign user assertions.

Format of the User Assertion

We use the JWT (JSON Web Token) standard for the user assertion. JWTs are described at jwt.io, and they are specified in RFC 7519.

NOTE: we provide some information here about the format and contents of the JWT, but it is primarily informational. In your application, you can choose one of the libraries mentioned on jwt.io and use it to produce a properly formatted JWT, as a single string that is easy to handle in your application. Because JWTs are standardized and their use is widespread, there are a number of libraries available for many different programming languages that will do all the work for you. You just need to find one that supports ES256 signing, you can just use that to generate JWTs. Typically, you just need to provide a PEM file that contains the private key and an object that contains the ID values you need for the assertion (the value described in the Payload section below), and the library will do all the work.

A JWT consists of three pieces:

1. A header that indicates that the token is in fact a JWT and specifies the algorithm that is used to secure the token
2. A payload that contains data about the user
3. A signature that is used to validate the token

Each of these sections is base64-encoded, and the encoded strings are concatenated with '.' separators to form the complete JWT.

Header

The header is a JSON object with the following fields and values:

    {"alg":"ES256","typ":"JWT"}

The typ field indicates that the token is a JWT, and the alg field indicates that ECDSA, an elliptic curve digital signature algorithm that uses the NIST P-256 curve, with SHA256 as the hash algorithm, is used to sign the assertion.

NOTE: There are a number of other algorithms that can be used, but the Data Control Platform requires the use of a public key algorithm, and ECDSA keys are significantly shorter. The Data Control Platform also supports the RS256 signature algorithm, but IronCore recommends the use of ES256 signatures.

This object is serialized to a UTF-8 string and base64 encoded to generate the JWT header field.

Payload

The payload is a JSON object that contains a JWT Claim Set. The claim set we support contains the following fields:

1. sub - the subject of the assertion (which is the user ID of the authenticated user)
2. iat - an issued at timestamp that indicates when the claim was generated (a NumericDate - an integer value that is the number of seconds since the epoch)
3. exp - the expiration timestamp for the JWT. This is a NumericDate that indicates the date/time after which the JWT is no longer valid
4. pid - the project ID
5. sid - the segment ID
6. kid - the identity assertion key ID

Note that we strongly recommend that JWTs include an expiration time. JWTs should not be valid for any significant length of time after their generation, since the user's authorization might have changed. However, the JWT standard does not require the inclusion of any claims, so the Data Control Platform assumes that the expiration of a JWT is 120 seconds after the issued time. The exp claim is currently not used.

The first three claim names are registered claims, meaning the names are reserved across all JWTs in the JWT standard. The last three are private claims, meaning the name is not reserved and could be subject to collision. Aside from the exp claim, the other five claims are required by the Data Control Platform service.

For example, the payload might consist of the following claims:

    { "sub":"bob@icl.com", "iat":1486877352, "exp":1486877472, "pid":23, "sid":"customer1.com","kid":123 }

We will accept JWT claim sets that have claims with the names pid, sid, and kid, but the JWT standard also provides for the addition of a namespace prefix to claim names in order to minimize the risk of collision, and some JWT generators and libraries expect to add a namespace to private claims. We also recognize these three private claims if they have the http://ironcore/ namespace prefix. Thus, the following claim set is equivalent to the previous one:

    { "sub":"bob@icl.com", "iat":1486877352, "exp":1486877472, "http://ironcore/pid":23,
     "http://ironcore/sid":"customer1.com","http://ironcore/kid":123 }

This object is serialized to a UTF-8 string and base64 encoded to generate the JWT payload field.

Signature

The signature is created by applying the algorithm specified in the JWT header to the hash of the header and payload fields (as base64-encoded strings). If the type is ES256, the ECDSA algorithm is used with the P-256 curve. If the type is RS256, the RSA signature algorithm is used. Both of these selections use SHA256 as the hash algorithm.

This signature is our mechanism for ensuring that a JWT that is asserting the identity of a user associated with a project and segment was actually created by an application connected with that project.  When the Data Control Platform receives a JWT, it finds the public key associated with the included identity assertion key ID and uses that public key to validate the signature.

The bytes generated by the signature algorithm are base64 encoded to generate the JWT signature field.

Complete JWT

The final JWT is formed by concatenating the header, payload, and signature fields, separated by periods.

For the previous example, the JWT might be

    eyJhbGciOiJQUzI1NiIsInR5cCI6IkpXVCJ9Cg.
    eyAic3ViIjoiYm9iQGljbC5jb20iLCAiaWF0IjoxNDg2ODc3MzUyLCAiZXhwIjoxNDg2O
    Dc3NDcyLCAicGlkIjoyMywgInNpZCI6ImN1c3RvbWVyMS5jb20iLCJraWQiOjEyMyB9Cg.
    EkN-DOsnsuRjRO6BxXemmJDm3HbxrbRzXglbN2S4sOkopdU4IsDxTI8jO19W_A4K8ZPJi
    jNLis4EZsHeY559a4DFOd50_OqgHGuERTqYZyuhtF39yxJPAjUESwxk2J5k_4zM3O-vtd
    1Ghyo4IbqKKSy6J9mTniYJPenn5-HIirE

Reminder: your life will be much easier if you choose a JWT library that supports ES256 signing and use it to generate your JWTs. All you need is the private identity assertion key (often in a PEM file format) and an object containing the values for the claims, and a call to the library will produce the JWT you need.

Keys

The identity assertion key pair that the IronCore administration application generates for a project has a 256-bit private key and the corresponding 512-bit public key, generated using the NIST P-256 curve. Note that a project can have multiple identity assertion keys; each will have a unique ID that should be included as the kid claim in JWTs signed using that private key.

The administration application generates the keys in the browser, to eliminate the possibility that the server somehow leaks or retains the private key. The public key is transmitted to the server when the identity assertion key is generated. It is formatted as a string by base64-encoding the two binary components of the key and separating the strings with a '.'.

The private key is displayed so that the admin can record it, and the browser offers to download a file to the local device that contains the private key, along with the identity assertion key ID and the project ID. The private key is converted to a URL-safe base64 string for both display and storage.

Validating JWTs

The Data Control Platform will only accept JWTs if the "typ" field is "JWT" and the "alg" field in the header is either "ES256" or "RS256". In the payload, the claims sub, iat, pid, sid, and kid are required. A JWT will be rejected if the iat timestamp is more than +/- 120 seconds from the current server time.  It will also be rejected if the signature cannot be validated using the public key associated with the key ID specified in the kid claim, the kid is not associated with the project specified by the pid, or the sid is not recognized for the specified pid.

Security Considerations

A number of security experts have questioned the suitability of JWTs as a security mechanism. While there are certainly opportunities for JWTs to be used inappropriately, using a JWT as a one-time assertion of user identity is exactly the intended use case. Concerns about the JWT typically center around the fact that once a JWT has been generated and signed with a valid key, it might be accepted even after the authorization for the user identified by the JWT is no longer valid. A common misuse that highlights this problem is using a JWT as a session token.

Seems like a good idea - you can validate the token using public key cryptography, it is extensible so you can encapsulate extra information about the user in the token, possibly eliminating the need for an application to query that information from a back-end service, and you can add an expiration date to it. Pretty useful, right? Unfortunately, once a user authenticates, that JWT is "live" and acceptable until it expires, even if the identified user's access was revoked or information about the user that was embedded in the JWT was subsequently modified. For conveying session state, a session ID and some session and user information stored in your backend service is a better choice.

But if you are making a one-time assertion about the identity of a user, a JWT is a much more appropriate choice.

Another concern is that the JWT standard is very flexible, and it is possible to create a valid JWT that is not signed (by specifying "none" for the "alg" field in the header). While this is an unfortunate choice made during development of the JWT spec, it does not affect the Data Control Platform, because the services will not accept any JWT whose algorithm is neither ES256 nor RS256. This restriction likewise circumvents the JWT attack that changes the algorithm from RS256 to HS256 - the Data Control Platform services will reject that JWT.

We believe that, while JWTs are inappropriate for some use cases, particularly if anything that conforms to the loose JWT standard is accepted, they are actually a good choice for conveying an assertion of user identity from your application servers to our Data Control Platform SDKs and services.

Integrations with Identity Providers

Because the JWT specifications are standardized and JWTs are fairly common, JWTs might be an option for integrating an existing identity provider into your application and using it to provide user identity assertions for the Data Control Platform. For example, we have instructions on configuring Auth0 to generate JWTs that can be used with the Data Control Platform.

ON THIS PAGE

Format of the User AssertionHeaderPayloadSignatureComplete JWTKeysValidating JWTsSecurity ConsiderationsIntegrations with Identity Providers
