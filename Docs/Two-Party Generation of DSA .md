     Two-Party Generation of DSA                   Signatures
                       (Extended Abstract)

                 Philip MacKenzie and Michael K. Reiter

             Bell Labs, Lucent Technologies, Murray Hill, NJ, USA


     Abstract.  We describe a means of sharing the DSA signature function,
     so that two parties can eﬃciently generate a DSA signature with respect
     to a given public key but neither can alone. We focus on a certain
     instantiation that allows a proof of security for concurrent execution in
     the random oracle model, and that is very practical. We also brieﬂy
     outline a variation that requires more rounds of communication, but
     that allows a proof of security for sequential execution without random
     oracles.

1   Introduction

In this paper we present an eﬃcient and provably secure protocol by which al-
ice and bob, each holding a share of a DSA [25] private key, can (and must)
interact to generate a DSA signature on a given message with respect to the
corresponding public key. As noted in previous work on multiparty DSA signa-
ture generation (e.g., [26,7,16]), shared generation of DSA signatures tends to be
more complicated than shared generation of many other types of ElGamal-based
signatures [10] because (i) a shared secret must be inverted, and (ii) a multi-
plication must be performed on two shared secrets. One can see this diﬀerence
by comparing a Harn signature [20] with a DSA signature, say over parame-
ters <g,p,q>, with public/secret key pair <y(= gx mod p),x> and ephemeral
public/secret key pair <r(= gk mod p),k>. In a Harn signature, one computes

                       s ← x(hash(m)) − kr mod q

and returns a signature <r, s>, while for a DSA signature, one computes

                      s ← k−1(hash(m)+xr)modq,

and returns a signature <r mod q, s>. Obviously, to compute the DSA signature
the ephemeral secret key must be inverted, and the resulting secret value must
be multiplied by the secret key. For security, all of these secret values must be
shared, and thus inversion and multiplication on shared secrets must be per-
formed. Protocols to perform these operations have tended to be much more
complicated than protocols for adding shared secrets.

J. Kilian (Ed.): CRYPTO 2001, LNCS 2139, pp. 137–154, 2001.

c Springer-Verlag Berlin Heidelberg 2001
138   P. MacKenzie and M.K. Reiter

   Of course, protocols for generic secure two-party computation (e.g., [34])
could be used to perform two-party DSA signature generation, but here we ex-
plore a more eﬃcient protocol to solve this particular problem. To our knowledge,
the protocol we present here is the ﬁrst practical and provably secure protocol
for two-party DSA signature generation. As building blocks, it uses a public key
encryption scheme with certain useful properties (for which several examples ex-
ist) and eﬃcient special-purpose zero-knowledge proofs. The assumptions under
which these building blocks are secure are the assumptions required for security
of our protocol. For example, by instantiating our protocol with particular con-
structions, we can achieve a protocol that is provably secure under the decision
composite residuosity assumption (DCRA) [31] and the strong RSA assump-
tion [2] when executed sequentially, or one that is provably secure in the random
oracle model [5] under the DCRA and strong RSA assumption, even when arbi-
trarily many instances of the protocol are run concurrently. The former protocol
requires eight messages, while the latter protocol requires only four messages.
   Our interest in two-party DSA signature generation stems from our broader
research into techniques by which a device that performs private key operations
(signatures or decryptions) in networked applications, and whose local private
key is activated with a password or PIN, can be immunized against oﬄine dic-
tionary attacks in case the device is captured [27]. Brieﬂy, we achieve this by
involving a remote server in the device’s private key computations, essentially
sharing the cryptographic computation between the device and the server. Our
original work [27] showed how to accomplish this for the case of RSA functions
or certain discrete-log-based functions other than DSA, using known techniques
for sharing those functions between two parties. The important case of DSA sig-
natures is enabled by the techniques of this paper. Given our practical goals, in
this paper we focus on the most eﬃcient (four message, random oracle) version
of our protocol, which is quite suitable for use in the context of our system.

2   Related Work

Two-party generation of DSA  signatures falls into the category of threshold
signatures, or more broadly, threshold cryptography. Early work in the ﬁeld is
due to Boyd [4], Desmedt [8], Croft and Harris [6], Frankel [13], and Desmedt and
Frankel [9]. Work in threshold cryptography for discrete-log based cryptosystems
other than DSA is due to Desmedt and Frankel [9], Hwang [22], Pedersen [33],
Harn [20], Park and Kurosawa [32], Herzberg et al. [21], Frankel et al. [14], and
Jarecki and Lysyanskaya [23].
   Several works have developed techniques directly for shared generation of
DSA  signatures. Langford [26] presents threshold DSA schemes ensuring un-
forgeability against one corrupt player out of n ≥ 3; of t corrupt players out of n
for arbitrary t<nunder certain restrictions (see below); and of t corrupt players
out of n ≥ t2 +t+1. Cerecedo et al. [7] and Gennaro et al. [16] present threshold
schemes that prevent t corrupt players out of n ≥ 2t + 1 from forging, and thus
require a majority of correct players. Both of these works further develop robust
                              Two-Party Generation of DSA Signatures 139

solutions, in which the t corrupted players cannot interfere with the other n − t
signing a message, provided that stronger conditions on n and t are met (at least
n ≥ 3t + 1). However, since we consider the two party case only, robustness is
not a goal here.
   The only previous proposal that can implement two-party generation of DSA
signatures is due to Langford [26, Section 5.1], which ensures unforgeability
against t corrupt players out of n for an arbitrary t<n. This is achieved,
however, by using a trusted center to precompute the ephemeral secret key k
for each signature and to share k−1 mod q and k−1x mod q among the n par-
ties. That is, this solution circumvents the primary diﬃculties of sharing DSA
signatures—inverting a shared secret and multiplying shared secrets, as discussed
in Section 1—by using a trusted center. Recognizing the signiﬁcant drawbacks
of a trusted center, Langford extends this solution by replacing the trusted cen-
ter with three centers (that protect k−1 and k−1x from any one) [26, Section
5.2], thereby precluding this solution from being used in the two-party case. In
contrast, our solution suﬃces for the two-party case without requiring the play-
ers to store precomputed, per-signature values. Since our motivating application
naturally admits a trusted party for initializing the system (see [27]), for the
purposes of this extended abstract we assume a trusted party to initialize alice
and bob with shares of the private signing key. In the full version of this paper,
we will describe the additional machinery needed to remove this assumption.

3   Preliminaries

Security parameters. Let κ be the main cryptographic security parameter used
for, e.g., hash functions and discrete log group orders; a reasonable value today
may be κ = 160. We will use κ0 >κas a secondary security parameter for public
key modulus size; reasonable values today may be κ0 = 1024 or κ0 = 2048.

Signature schemes. A digital signature scheme is a triple (Gsig,S,V) of algo-
rithms, the ﬁrst two being probabilistic, and all running in expected polyno-
                             κ0
mial time. Gsig takes as input 1 and outputs a public key pair (pk, sk), i.e.,
               κ0
(pk, sk) ← Gsig(1 ). S takes a message m and a secret key sk as input and out-
puts a signature σ for m, i.e., σ ← Ssk(m). V takes a message m, a public key
pk, and a candidate signature σ0 for m and returns the bit b =1ifσ0 is a valid
                                                                      0
signature for m, and otherwise returns the bit b = 0. That is, b ← Vpk(m, σ ).
Naturally, if σ ← Ssk(m), then Vpk(m, σ)=1.

DSA.  The Digital Signature Algorithm [25] was proposed by NIST in April
1991, and in May 1994 was adopted as a standard digital signature scheme
in the U.S. [12]. It is a variant of the ElGamal signature scheme [10], and is
deﬁned as follows, with κ = 160, κ0 set to a multiple of 64 between 512 and
1024, inclusive, and hash function hash deﬁned as SHA-1 [11]. Let “z ←R S”
denote the assignment to z of an element of S selected uniformly at random.
Let ≡q denote equivalence modulo q.
140   P. MacKenzie and M.K. Reiter


                 κ0                               0
         GDSA(1   ): Generate a κ-bit prime q and κ -bit prime p such that
                     q divides p − 1. Then generate an element g of order q
                         ∗
                     in Zp. The triple <g,p,q> is public. Finally generate
                                        x
                     x ←R  Zq and  y ← g  mod p, and let <g,p,q,x> and
                     <g,p,q,y>  be the secret and public keys, respectively.
       S<g,p,q,x>(m): Generate an ephemeral secret key  k  ←R   Zq and
                     ephemeral public key r ←  gk mod  p. Compute s  ←
                     k−1(hash(m)+xr)modq. Return      <r  mod  q, s> as
                     the signature of m.
V<g,p,q,y>(m, <r, s>): Return 1 if 0 <r<q,0<s<q, and              r ≡q
                     (ghash(m)s−1 yrs−1 mod p) where s−1 is computed mod-
                     ulo q. Otherwise, return 0.

Encryption schemes. An  encryption scheme is a triple (Genc,E,D) of algo-
rithms, the ﬁrst two being probabilistic, and all running in expected polyno-
                              κ0
mial time. Genc takes as input 1 and outputs a public key pair (pk, sk), i.e.,
                κ0
(pk, sk) ← Genc(1 ). E takes a public key pk and a message m as input and
outputs an encryption c for m; we denote this c ← Epk(m). D takes a ciphertext
c and a secret key sk and returns either a message m such that c is a valid
encryption of m,ifsuchanm  exists, and otherwise returns ⊥.
   Our protocol employs a semantically secure encryption scheme with a certain
additive homomorphic property. For any public key pk output from the Genc
function, let Mpk be the space of possible inputs to Epk, and Cpk to be the
space of possible outputs of Epk. Then we require that there exist an eﬃcient
implementation of an additional function +pk : Cpk × Cpk → Cpk such that
(written as an inﬁx operator):

    m1,m2,m1  + m2  ∈ Mpk  ⇒   Dsk(Epk(m1)+pk   Epk(m2)) =  m1 + m2  (1)

Examples of cryptosystems for which +pk exist (with Mpk =[−v, v] for a certain
value v) are due to Naccache and Stern [28], Okamoto and Uchiyama [30], and
Paillier [31].1 Note that (1) further implies the existence of an eﬃcient function
×pk : Cpk × Mpk → Cpk such that

         m1,m2,m1m2   ∈ Mpk   ⇒   Dsk(Epk(m1)  ×pk m2)=m1m2          (2)
   In addition, in our protocol, a party may be required to generate a nonin-
teractive zero knowledge proof of a certain predicate P involving decryptions
of elements of Cpk, among other things. We denote such a proof as zkp [P ].
In Section 6.1, we show how these proofs can be accomplished if the Paillier
cryptosystem is in use. We emphasize, however, that our use of the Paillier cryp-
tosystem is only exemplary; the other cryptosystems cited above could equally
well be used with our protocol.
1 The cryptosystem of Benaloh [1] also has this additive homomorphic property, and
  thus could also be used in our protocol. However, it would be less eﬃcient for our
  purposes.
                              Two-Party Generation of DSA Signatures 141

System model. Our system includes two parties, alice and bob. Communication
between alice and bob occurs in sessions (or protocol runs), one per message that
they sign together. alice plays the role of session initiator in our protocol. We
presume that each message is implicitly labeled with an identiﬁer for the session
to which it belongs. Multiple sessions can be executed concurrently.
   The adversary in our protocol controls the network, inserting and manip-
ulating communication as it chooses. In addition, it takes one of two forms:
an alice-compromising adversary learns all private initialization information for
alice.Abob-compromising adversary is deﬁned similarly.
   We note that a proof of security in this two-party system extends to a proof of
security in an n-party system in a natural way, assuming the adversary decides
which parties to compromise before any session begins. The basic idea is to
guess for which pair of parties the adversary forges a signature, and focus the
simulation proof on those two parties, running all other parties as in the real
protocol. The only consequence is a factor of roughly n2 lost in the reduction
argument from the security of the signature scheme.

4   Signature Protocol

In this section we present a new protocol called S-DSA by which alice and bob
sign a message m.

4.1  Initialization
For our signature protocol, we assume that the private key x is multiplicatively
shared between alice and bob, i.e., that alice holds a random private value x1 ∈ Zq
and bob holds a random private value x2 ∈ Zq such that x ≡q x1x2. We also
                             x                 x
assume that along with y, y1 = g 1 mod p and y2 = g 2 mod p are public. In this
extended abstract, we do not concern ourselves with this initialization step, but
simply assume it is performed correctly, e.g., by a trusted third party. We note,
however, that achieving this without a trusted third party is not straightforward
(e.g., see [17]), and so we will describe such an initialization protocol in the full
version of this paper.
   We use a multiplicative sharing of x to achieve greater eﬃciency than using
either polynomial sharing or additive sharing. With multiplicative sharing of
keys, inversion and multiplication of shared keys becomes trivial, but addition
of shared keys becomes more complicated. For DSA, however, this approach
seems to allow a much more eﬃcient two-party protocol.
   In addition to sharing x, our protocol assumes that alice holds the private key
sk corresponding to a public encryption key pk, and that there is another public
encryption key pk0 for which alice does not know the corresponding sk0. (As
above, we assume that these keys are generated correctly, e.g., by a trusted third
party.) Also, it is necessary for our particular zero-knowledge proof constructions
                                   8  8
that the range of Mpk be at least [−q ,q ] and the range of Mpk0 be at least
[−q6,q6], although we believe a slightly tighter analysis would allow both to have
a range of [−q6,q6].
142   P. MacKenzie and M.K. Reiter

4.2  Signing Protocol

The protocol by which alice and bob cooperate to generate signatures with re-
spect to the public key <g,p,q,y>is shown in Figure 1. As input to this protocol,
alice receives the message m to be signed. bob receives no input (but receives m
from alice in the ﬁrst message).
   Upon receiving m to sign, alice ﬁrst computes its share k1 of the ephemeral
                                             −1
private key for this signature, computes z1 =(k1) mod q, and encrypts both
z1 and x1z1 mod q under pk. alice’s ﬁrst message to bob consists of m and these
ciphertexts, α and ζ. bob performs some simple consistency checks on α and ζ
(though he cannot decrypt them, since he does not have sk), generates his share
                                                         k
k2 of the ephemeral private key, and returns his share r2 = g 2 mod p of the
ephemeral public key.
   Once alice has received r2 from bob and performed simple consistency checks
                                            ∗
on it (e.g., to determine it has order q modulo Zp), she is able to compute the
                            k
ephemeral public key r =(r2) 1 mod p, which she sends to bob in the third
message of the protocol. alice also sends a noninteractive zero-knowledge proof
Π that there are values η1 (= z1) and η2 (= x1z1 mod q) that are consistent
                                                   3  3
with r, r2, y1, α and ζ, and that are in the range [−q ,q ]. This last fact is
necessary so that bob’s subsequent formation of (a ciphertext of) s does not leak
information about his private values.
   Upon receiving <r, Π>, bob veriﬁes Π and performs additional consistency
checks on r. If these pass, then he proceeds to compute a ciphertext µ of the value
s (modulo q) for the signature, using the ciphertexts α and ζ received in the ﬁrst
                                           −1
message from alice; the values hash(m), z2 =(k2) mod q, r mod q, and x2; and
the special ×pk and +pk operators of the encryption scheme. In addition, bob uses
+pk to “blind” the plaintext value with a random, large multiple of q. So, when
alice later decrypts µ, she statistically gains no information about bob’s private
                                                        0
values. In addition to returning µ, bob computes and returns µ ← Epk0 (z2) and
                                     0
a noninteractive zero-knowledge proof Π that there are values η1 (= z2) and
                                              0
η2 (= x2z2 mod p) consistent with r2, y2, µ and µ , and that are in the range
[−q3,q3]. After receiving and checking these values, alice recovers s from µ to
complete the signature.
   The noninteractive zero-knowledge proofs Π and Π0 are assumed to satisfy
the usual completeness, soundness, and zero-knowledge properties as deﬁned
in [3,29], except using a public random hash function (i.e., a random oracle)
instead of a public random string. In particular, we assume in Section 5 that (1)
these proofs have negligible simulation error probability, and in fact a simulator
exists that generates a proof that is statistically indistinguishable from a proof
generated by the real prover, and (2) these proofs have negligible soundness error
probability, i.e., the probability that a prover could generate a proof for a false
statement is negligible. The implementations of Π and Π0 in Section 6 enforce
these properties under reasonable assumptions. To instantiate this protocol with-
out random oracles, Π and Π0 would need to become interactive zero-knowledge
protocols. It is not too diﬃcult to construct four-move protocols for Π and Π0,
and by overlapping some messages, one can reduce the total number of moves in
                              Two-Party Generation of DSA Signatures 143

this instantiation of the S-DSA protocol to eight. For brevity, we omit the full
description of this instantiation.
   When the zero-knowledge proofs are implemented using random oracles, we
can show that our protocol is secure even when multiple instances are executed
concurrently. Perhaps the key technical aspect is that we only require proofs of
language membership, which can be implemented using random oracles without
requiring rewinding in the simulation proof. In particular, we avoid the need for
any proofs of knowledge that would require rewinding in knowledge extractors
for the simulation proof, even if random oracles are used. The need for rewinding
(and particularly, nested rewinding) causes many proofs of security to fail in the
concurrent setting (e.g., [24]).

5   Security for   S-DSA

In this section we sketch a formal proof of security for our protocol. We begin
by deﬁning security for signatures and encryption in Section 5.1 and for S-DSA
in Section 5.2. We then state our theorems and proofs in Section 5.3.

5.1  Security for DSA   and Encryption
First we state requirements for security of DSA and encryption. For DSA, we
specify existential unforgeability versus chosen message attacks [19]. That is,
                                                                     κ0
a forger is given <g,p,q,y>, where (<g,p,q,y>,<g,p,q,x>)  ←  GDSA(1   ),
and tries to forge signatures with respect to <g,p,q,y>. It is allowed to query
a signature oracle (with respect to <g,p,q,x>) on messages of its choice. It
succeeds if after this it can output some (m, σ) where V<g,p,q,y>(m, σ)=1
but m was not one of the messages signed by the signature oracle. We say a
forger (q, )-breaks DSA if the forger makes q queries to the signature oracle and
succeeds with probability at least .
   For encryption, we specify semantic security [18]. That is, an attacker A is
                                κ0
given pk, where (pk, sk) ← Genc(1 ). A generates X0,X1 ∈  Mpk and sends
these to a test oracle, which chooses b ←R {0, 1}, and returns Y = Epk(Xb).
Finally A outputs b0, and succeeds if b0 = b. We say an attacker A-breaks
encryption if 2 · Pr(A succeeds) − 1 ≥ . Note that this implies Pr(A guesses 0 |
b =0)−  Pr(A guesses 0 | b =1)≥ .

5.2  Security for S-DSA

                                                                     κ0
A forger F is given <g,p,q,y>, where (<g,p,q,y>,<g,p,q,x>) ← GDSA(1   ),
and the public data generated by the initialization procedure for S-DSA, along
with the secret data of either alice or bob (depending on the type of forger). As
in the security deﬁnition for signature schemes, the goal of the forger is to forge
signatures with respect to <g,p,q,y>. Instead of a signature oracle, there is an
alice oracle and a bob oracle.
144    P. MacKenzie and M.K. Reiter

                   alice                                   bob
 k1 ←R  Zq
            −1
 z1 ←R  (k1)   mod q
 α ←  Epk(z1)
 ζ ←  Epk(x1z1 mod  q)
                                <m,α,ζ>
                                         -

                                    abort if (α 6∈ Cpk ∨ ζ 6∈ Cpk)
                                    k2 ←R  Zq
                                          k2
                                    r2 ← g   mod  p

                                    r2
                               

                ∗       q
 abort if (r2 6∈ Zp ∨ (r2) 6≡p 1)
         k1
 r ← (r2)   mod  p
                              3  3  
            ∃η1,η2 : η1,η2 ∈ [−q ,q ]
                           η1       
           ∧              r   ≡p r2 
                        η2/η1       
 Π  ←  zkp  ∧          g     ≡p  y1 
                                    
            ∧          Dsk(α)  ≡q η1
            ∧          Dsk(ζ)  ≡q η2

                                  <r,Π>
                                         -

                                                  ∗     q
                                    abort if (r 6∈ Zp ∨ r 6≡p 1)
                                    abort if (verify(Π) = false)
                                    m0 ←  hash(m)
                                    r0 ← r mod  q
                                             −1
                                    z2 ← (k2)   mod  q
                                    c ←R  Zq5
                                                  0
                                    µ ←  (α ×pk m  z2)+pk
                                                 0
                                         (ζ ×pk r x2z2)+pk  Epk(cq)
                                     0
                                    µ  ← Epk0 (z2)
                                                                        3  3  
                                               ∃η1,η2 :       η1,η2 ∈ [−q ,q ]
                                                                      η1      
                                               ∧                  (r2)  ≡p  g 
                                                                  η2/η1       
                                      0        ∧                 g     ≡p  y2 
                                    Π  ←  zkp                        0        
                                               ∧               Dsk0 (µ ) ≡q η1 
                                                                         0    
                                               ∧ Dsk(µ)  ≡q Dsk((α  ×pk m  η1)
                                                                         0
                                                             +pk (ζ ×pk r η2))

                                <µ,µ0,Π0>
                               

                       0
 abort if (µ 6∈ Cpk ∨ µ 6∈ Cpk0 )
 abort if (verify(Π0) = false)

 s ← Dsk(µ)modq
 publish <r mod  q, s>

                     Fig. 1. S-DSA  shared signature protocol
                              Two-Party Generation of DSA Signatures 145

   F may query the  alice oracle by invoking aliceInv1(m), aliceInv2(r2), or
aliceInv3(<µ, µ0,Π0>) for input parameters of F ’s choosing. (These invocations
are also accompanied by a session identiﬁer, which is left implicit.) These in-
vocations correspond to a request to initiate the protocol for message m and
the ﬁrst and second messages received ostensibly from bob, respectively. These
return outputs of the form <m, α, ζ>, <r, Π>, or a signature for the message
m from the previous aliceInv1 query in the same session, respectively, or abort.
Analagously, F may query the bob oracle by invoking bobInv1(<m, α, ζ>)or
bobInv2(<r, Π>) for arguments of the F ’s choosing. These return messages of
                   0  0
the form r2 or <µ, µ ,Π >, respectively, or abort. F may invoke these queries
in any order, arbitrarily many times.
   An alice-compromising forger F succeeds if after gaining access to the private
initialization state of alice, and invoking the alice and bob oracles as it chooses, it
can output (m, σ) where V<g,p,q,y>(m, σ)=1andm  is not one of the messages
sent to bob in a bobInv1 query. Similarly, a bob-compromising forger F succeeds
if after gaining access to the private initialization state of bob, and invoking the
alice and bob oracles as it chooses, it can output (m, σ) where V<g,p,q,y>(m, σ)=
1 and m is not one of the messages sent to alice in a aliceInv1 query.
   Let qalice be the number of aliceInv1 queries to alice. Let qbob be the num-
ber of bobInv1 queries. Let qo be the number of other oracle queries. Let
q = <qalice,qbob,qo>. In a slight abuse of notation, let |q| = qalice + qbob + qo,
i.e., the total number of oracle queries. We say a forger (q, )-breaks S-DSA if
it makes |q| oracle queries (of the respective type and to the respective oracles)
and succeeds with probability at least .

5.3  Theorems
Here we state theorems and provide proof sketches showing that if a forger
breaks the S-DSA system with non-negligible probability, then either DSA or the
underlying encryption scheme used in S-DSA can be broken with non-negligible
probability. This implies that if DSA and the underlying encryption scheme are
secure, our system will be secure.
   We prove security separately for alice-compromising and bob-compromising
forgers. The idea behind each proof is a simulation argument. Assuming that
a forger F can break the S-DSA system, we then construct a forger F ∗ that
breaks DSA. Basically F ∗ will run F over a simulation of the S-DSA system,
and when F  succeeds in forging a signature in the simulation of S-DSA, then
F ∗ will succeed in forging a DSA signature.
   In the security proof against an alice-compromising forger F , there is a slight
complication. If F were able to break the encryption scheme (Genc,E,D), an
attacker F ∗ as described above may not be able to simulate properly. Thus we
show that either F forges signatures in a simulation where the encryptions are
of strings of zeros, and thus we can construct a forger F ∗ for DSA, or F does not
forge signatures in that simulation, and thus it must be able to distinguish the
true encryptions from the zeroed encryptions. Then we can construct an attacker
A that breaks the underlying encryption scheme. A similar complication arises
146   P. MacKenzie and M.K. Reiter

in the security proof against a bob-compromising forger F , and the simulation
argument is modiﬁed in a similar way.
   Theorem 1 below states that an alice-compromising forger that breaks S-DSA
with a non-negligible probability can break either DSA or (Genc,E,D) with non-
negligible probability. Theorem 2 makes a similar claim for a bob-compromising
forger. In these theorems, we use “≈” to indicate equality to within negligible
factors. Moreover, in our simulations, the forger F is run at most once, and so
the times of our simulations are straightforward and omitted from our theorem
statements.

Theorem   1. Suppose an alice-compromising forger (q, )-breaks S-DSA. Then
                                0                       0    
either there exists an attacker that  -breaks (Genc,E,D) with  ≈ , or there
                                                            2qbob
                       00                 00   
exists a forger that (qbob, )-breaks DSA with  ≈ 2 .
Proof. Assume an alice-compromising forger F (q, )-breaks the S-DSA scheme.
Then consider a simulation Sim of the S-DSA scheme that takes as input a DSA
public key <g,p,q,y>, a corresponding signature oracle, and a public key pk0
for the underlying encryption scheme. Sim generates the initialization data for
                                  κ0
alice: x1 ←R Zq and (pk, sk) ← Genc(1 ), and gives these to F . The public data
        (x−1 mod q)            0
y, y2 = g 1       mod p, and pk are also revealed to F . Then Sim responds
to alice queries as a real alice oracle would, and to bob queries using the help of
the DSA signature oracle, since Sim does not know the x2 value used by a real
bob oracle. Speciﬁcally Sim answers as follows:

1. bobInv1(<m, α, ζ>): Set z1 ← Dsk(α). Query the DSA signature oracle with
   m to get a signature <r,ˆ s>ˆ , and compute r ← ghash(m)ˆs−1 yrˆsˆ−1 mod p where
    −1                                     z
   sˆ  is computed modulo q. Compute r2 ← r 1 mod p, and return r2.
                                               ∗     q
2. bobInv2(<r, Π>): Reject if Π is invalid, r 6∈ Zp or r 6≡p 1. Else, choose
                                        0                         0
   c ←R  Zq5 and set µ ← Epk(ˆs + cq). Set µ ← Epk0 (0), and generate Π using
   the simulator for the zkp []. Return <µ, µ0,Π0>.

   Notice that Sim sets µ0 to an encryption of zero, and simulates the proof of
consistency Π0. In fact, disregarding the negligible statistical diﬀerence between
the simulated Π0 proofs and the real Π0 proofs, the only way Sim and the real
S-DSA  scheme diﬀer (from F ’s viewpoint) is with respect to the µ0 values, i.e.,
                                            0
the (at most) qbob ciphertexts generated using pk .
   Now  consider a  forger F ∗ that takes as input a   DSA   public  key
<g,p,q,y>and corresponding signature oracle, generates a public key pk0 using
   0   0           κ0
<pk ,sk > ← Genc(1  ), runs Sim using these parameters as inputs, and outputs
                                                                
whatever F outputs. If F produces a forgery with probability at least 2 in Sim,
F ∗ produces a forgery in the underlying DSA signature scheme with probability
       
at least 2 .
                                                          
   Otherwise F produces a forgery with probability less than 2 in Sim. Then
using a standard hybrid argument, we can construct an attacker A that 0-
breaks the semantic security of the underlying encryption scheme for pk0, where
0 ≈   . Speciﬁcally, A takes a public key pk0 and corresponding test oracle
    2qbob
                              Two-Party Generation of DSA Signatures 147

as input, generates a DSA public/private key pair (<g,p,q,y>,<g,p,q,x>) ←
       κ0
GDSA(1   ), and runs a slightly modiﬁed Sim using <g,p,q,y>as the DSA public
key parameter, simulating the DSA signature oracle with <g,p,q,x>, and using
pk0 as the public encryption key parameter. Sim is modiﬁed only in the bobInv2
query, as follows:

                                  −1
1. A  computes the value z2 ← (kz1)  mod  q, where k was computed in the
   simulation of the DSA signature oracle in the corresponding bobInv1 query,
2. A chooses to produce the ﬁrst j ciphertexts under pk0 as in the real protocol
         0
   (i.e., µ ← Epk0 (z2)), for a random j ∈{0,...,qbob}, and
3. A  produces the next ciphertext under pk0 by using the response from the
   test oracle with input X0 = z2 and X1 =0.

Finally A outputs 0 if F produces a forgery, and 1 otherwise. Since the case of j =
0 corresponds to Sim, and the case of j = qbob corresponds to the real protocol, an
averaging argument can be used to show that A0-breaks the semantic security
of the underlying encryption scheme for pk0 with probability 0 ≈  .
                                                            2qbob

Theorem   2. Suppose a bob-compromising forger (q, )-breaks S-DSA. Then ei-
                               0                        0    
ther there exists an attacker that  -breaks (Genc,E,D) with  ≈ , or there
                                                           4qalice
                        00                  00  
exists a forger that (qalice, )-breaks DSA, with  ≈ 2 .
Proof. Assume a bob-compromising forger F (q, )-breaks the S-DSA scheme.
Then consider a simulation Sim of the S-DSA scheme that takes as input a DSA
public key <g,p,q,y>, a corresponding signature oracle, and a public key pk for
the underlying encryption scheme. Sim generates the initialization data for bob:
                0   0          κ0
x2 ←R Zq and (pk ,sk ) ← Genc(1  ), and gives these to F . The public data y,
      (x−1 mod q)
y1 = g 2       mod  p, and pk are also revealed to F . Then Sim responds to
bob queries as a real bob oracle would, and to alice queries using the help of the
DSA signature oracle, since Sim does not know the x1 value used by a real alice
oracle. Speciﬁcally Sim answers as follows:

1. aliceInv1(m): Set α ← Epk(0) and ζ ← Epk(0), and return <m, α, ζ>.
                              ∗       q
2. aliceInv2(r2): Reject if r2 6∈ Zp or (r2) 6≡p 1. Call the DSA signature oracle
   with m, let (ˆr, sˆ) be the resulting signature, and compute
   r ← ghash(m)ˆs−1 yrˆsˆ−1 mod p wheres ˆ−1 is computed modulo q. Construct Π
   using the simulator for the zkp []. Store <r,ˆ s>ˆ and return <r, Π>.
                 0  0                       0
3. aliceInv3(<µ, µ ,Π >): Reject if µ 6∈ Cpk, µ 6∈ Cpk0 , or the veriﬁcation of
   Π0 fails. Otherwise, return <r,ˆ s>ˆ .

   Notice that Sim sets α and ζ to encryptions of zero, and simulates the proof of
consistency Π. In fact, disregarding the negligible statistical diﬀerence between
the simulated Π proofs and the real Π proofs, the only way Sim and the real
S-DSA  scheme diﬀer (from F ’s viewpoint) is with respect to the α and ζ values,
i.e., the (at most) 2qalice ciphertexts generated using pk.
148   P. MacKenzie and M.K. Reiter

   Now consider a forger F ∗ that takes as input a DSA public key <g,p,q,y>
and  a corresponding signature oracle, generates a public key  pk  using
                  κ0
<pk, sk> ←  Genc(1  ), runs Sim using these parameters as inputs, and out-
                                                                       
puts whatever F outputs. If F produces a forgery with probability at least 2
in Sim, F ∗ produces a forgery in the underlying DSA signature scheme with
                  
probability at least 2 .
                                                          
   Otherwise F produces a forgery with probability less than 2 in Sim. Then
using a standard hybrid argument, we can construct an attacker A that 0-
breaks the semantic security of the underlying encryption scheme for pk, where
0 ≈    . Speciﬁcally, A takes a public key pk and corresponding test oracle
     4qalice
as input, generates a DSA public/private key pair (<g,p,q,y>,<g,p,q,x>) ←
       κ0
GDSA(1   ), and runs a slightly modiﬁed Sim using <g,p,q,y>as the DSA public
key parameter, and using pk as the public encryption key parameter. Sim is
modiﬁed only in the alice oracle queries, as follows:

1. In aliceInv1,
    a) A chooses to produce the ﬁrst j ciphertexts under pk as in the real
       protocol (i.e., either α ← Epk(z1)orζ ← Epk(x1z1 mod q)), for a random
       j ∈{0,...,2qalice},
    b) A produces the next ciphertext under pk by using the response from the
       test oracle with input X0 being the plaintext from the real protocol (i.e.,
       either X0 = z1 or X0 = x1z1 mod q, depending on whether j is even or
       odd) and X1 =0.
2. In aliceInv2, A computes r as in the real protocol, without calling the DSA
   signature oracle.
3. In aliceInv3, instead of returning the result of calling the DSA signature ora-
                             0             −1
   cle, A computes z2 ← Dsk0 (µ ) and k2 ← (z2) mod q, sets k ← k1k2 mod q,
   and returns the DSA signature for m using DSA secret key <g,p,q,x>with
   k as the ephemeral secret key.

Finally A outputs 0 if F produces a forgery, and 1 otherwise. Since the case
of j = 0 corresponds to Sim (in particular, notice that the distribution of r
is identical), and the case of j =2qalice corresponds to the real protocol, an
averaging argument can be used to show that A0-breaks the semantic security
of the underlying encryption scheme for pk with probability 0 ≈  .
                                                           4qalice

6   Proofs   Π  and   Π0

In this section we provide an example of how alice and bob can eﬃciently con-
struct and verify the noninteractive zero-knowledge proofs Π and Π0. The form
of these proofs naturally depends on the encryption scheme (Genc,E,D), and
the particular encryption scheme for which we detail Π and Π0 here is that due
to Paillier [31]. We reiterate, however, that our use of Paillier is merely exem-
plary, and similar proofs Π and Π0 can be constructed with other cryptosystems
satisfying the required properties (see Section 3).
                              Two-Party Generation of DSA Signatures 149

   We caution the reader that from this point forward, our use of variables is
not necessarily consistent with their prior use in the paper; rather, it is necessary
to replace certain variables or reuse them for diﬀerent purposes.

6.1  The Paillier Cryptosystem

A speciﬁc example of a cryptosystem that has the homomorphic properties
required for our protocol is the ﬁrst cryptosystem presented in [31]. It uses the
           λ(N)             Nλ(N)                      ∗
facts that w    ≡N  1 and w       ≡N 2 1 for any w ∈ ZN 2 , where λ(N)is
the Carmichael function of N. Let L be a function that takes input elements
                   2                                 u−1
from the set {u<N   |u ≡ 1modN}   and returns L(u)=   N  . We then deﬁne
the Paillier encryption scheme (GPai,E,D) as follows. This deﬁnition diﬀers
from that in [31] only in that we deﬁne the message space Mpk for public key
pk = <N, g> as M<N,g>  =[−(N  − 1)/2, (N − 1)/2] (versus ZN in [31]).

           κ0            0
     GPai(1  ): Choose  κ /2-bit primes p, q, set N = pq, and choose
                                        ∗                   λ(N)
                a random  element g ∈ ZN 2 such that gcd(L(g    mod
                N 2),N) = 1. Return the public key <N, g> and the pri-
                vate key <N,g,λ(N)>.
                                    ∗                 m N        2
   E<N,g>(m): Select a random  x ∈ ZN  and return c = g x  mod N  .
                                 λ(N)     2
D          (c): Compute  m  = L(c    mod N ) mod N. Return m  if m ≤
  <N,g,λ(N)>                  L(gλ(N) mod N 2)
                (N − 1)/2, and otherwise return m − N.
                                  2
  c1 +<N,g> c2: Return c1c2 mod N  .
                        m       2
   c ×<N,g> m: Return  c  mod  N .
Paillier [31] shows that both cλ(N) mod N 2  and  gλ(N) mod N 2 are  ele-
                         d
ments of the form (1 + N)  ≡N  2 1+dN, and thus the   L  function can be
easily computed for decryption. The security of this cryptosystem relies on the
Decision Composite Residuosity Assumption, DCRA.

6.2  Proof  Π

In this section we show how to eﬃciently implement the proof Π in our protocol
when the Paillier cryptosystem is used. Π0 is detailed in Section 6.3. Both proofs
rely on the following assumption:

   Strong RSA    Assumption.   Given an RSA modulus generator  GRSA
   that takes as input 1κ0 and produces a value N that is the product of
   two random  primes of length κ0/2, the Strong RSA assumption states
   that for any probabilistic polynomial-time attacker A:

                   κ0         ∗                                  e
    Pr[N ←  GRSA(1   ); y ←R ZN ;(x, e) ← A(N,y):(e ≥ 3) ∧ (y ≡N x )]
   is negligible.
                                                     ˜
   In our proofs, it is assumed that there are public values N, h1 and h2. Sound-
ness requires that N˜ be an RSA modulus that is the product of two strong
150   P. MacKenzie and M.K. Reiter

primes and for which the factorization is unknown to the prover, and that the
                                                    ˜
discrete logs of h1 and h2 relative to each other modulo N are unknown to the
prover. Zero knowledge requires that discrete logs of h1 and h2 relative to each
             ˜
other modulo N exist (i.e., that h1 and h2 generate the same group). As in Sec-
tion 4.1, here we assume that these parameters are distributed to alice and bob
by a trusted third party. In the full paper, we will describe how this assumption
can be eliminated.
   Now consider the proof Π. Let p and q be as in a DSA public key, pk =
<N, g> be a Paillier public key, and sk = <N,g,λ(N)> be the corresponding
                      6
private key, where N>q. For public values c, d, w1, w2, m1, m2, we construct
a zero-knowledge proof Π of:

                                           3  3 
                           ∃x1,x2 : x1,x2 ∈ [−q ,q ]
                                        x1      
                          ∧            c  ≡p w1 
                                     x2/x1      
                     P =  ∧         d     ≡p w2 
                                                
                           ∧        Dsk(m1)=x1
                           ∧        Dsk(m2)=x2

The proof is constructed in Figure 2, and its veriﬁcation procedure is given
                                          ∗
in Figure 3. We assume that c, d, w1,w2 ∈ Zp and are of order q, and that
          ∗
m1,m2  ∈ ZN 2 . (The prover should verify this if necessary, and abort if not true.)
                                                  ∗            x1
We assume the prover knows x1,x2 ∈ Zq and r1,r2 ∈ ZN such that c ≡p  w1,
 x /x                  x    N               x     N
d 2 1 ≡p w2, m1  ≡N 2 g 1 (r1) and m2 ≡N 2 g 2 (r2) . The prover need not
know sk, though a malicious prover might. If necessary, the veriﬁer should verify
                  ∗                                     ∗
that c, d, w1,w2 ∈ Zp and are of order q, and that m1,m2 ∈ ZN 2 .
   Intuitively, the proof works as follows. Commitments z1 and z2 are made to
                               ˜
x1 and x2 over the RSA modulus N, and these are proven to fall in the desired
range using proofs as in [15]. Simultaneously, it is shown that the commitment
z1 corresponds to the decryption of m1 and the discrete log of w1. Also simul-
taneously, it is shown that the commitment z2 corresponds to the decryption
of m2, and that the discrete log of w2 is the quotient of the two commitments.
The proof is shown in two columns, the left column used to prove the desired
properties of x1, w1 and m1, and the right column used to prove the desired
properties of x2, w2 and m2. The proof of the following lemma will appear in
the full version of this paper.

Lemma 1.   Π  is a noninteractive zero-knowledge proof of P .

6.3  Proof  Π0

Now we look at the proof Π0. Let p and q be as in a DSA public key, pk = <N, g>
and sk = <N,g,λ(N)>   be a Paillier key pair with N>q8, and pk0 = <N 0,g0>
and sk0 = <N 0,g0,λ(N 0)> be a Paillier key pair with N 0 >q6. For values c, d,
                                                     4 4
w1, w2, m1, m2, m3, m4 such that for some n1,n2 ∈ [−q ,q ], Dsk(m3)=n1
                                                     0
and Dsk(m4)=n2, we construct a zero-knowledge proof Π  of:
                                  Two-Party Generation of DSA Signatures       151

                                                     3  3  
                           ∃x1,x2,x3  :    x1,x2 ∈ [−q ,q ]
                                                     7  7  
                           ∧                 x3 ∈ [−q ,q ] 
                           ∧                    cx1 ≡  w   
                    P 0 =                            p   1 
                                              x2/x1        
                           ∧                 d      ≡p w2  
                                                           
                           ∧                Dsk0 (m1)=x1
                           ∧  Dsk(m2)=n1x1    + n2x2 + qx3

   We note that   P 0 is stronger than what is needed as shown in Figure 1. The
proof is constructed in Figure 4, and the veriﬁcation procedure for it is given in
                                            ∗
Figure 5. We assume that    c, d, w1,w2 ∈ Zp  and are of order  q, and that m1  ∈
 ∗                  ∗
Z(N 0)2 and m2  ∈ ZN 2 . (The prover should verify this if necessary.) We assume
                                                         ∗              x1
the prover knows  x1,x2  ∈ Zq,  x3 ∈ Zq5 , and r1,r2 ∈ ZN  , such that c   ≡p  w1,
 x /x                       0 x    N 0                   x      x   qx     N
d 2  1 ≡p w2, m1  ≡(N 0)2 (g ) 1 (r1)  and m2  ≡N 2 (m3)  1 (m4) 2 g  3 (r2) . The


            α ←R  Zq3                      δ ←R  Zq3
                   ∗                               ∗
            β ←R  ZN                       µ ←R  ZN
            γ ←R  Zq3N˜                    ν ←R  Zq3N˜
            ρ1 ←R  ZqN˜                    ρ2 ←R  ZqN˜
                                           ρ3 ←R  Zq
                                            ←R  Zq

                     x1    ρ1      ˜                 x2    ρ2      ˜
            z1 ← (h1)  (h2)   mod Nz2         ←  (h1)  (h2)  mod  N
                  α                              x2+ρ3
            u1 ← c  mod  py←                    d      mod  p
                  α  N        2                   δ+
            u2 ← g  β  mod  N              v1 ←  d    mod p
                     α     γ      ˜                  α 
            u3 ← (h1)  (h2) mod  Nv2          ←  (w2) d  mod  p
                                                  δ N        2
                                           v3 ←  g µ  mod  N
                                                     δ    ν      ˜
                                           v4 ←  (h1) (h2) mod  N

            e ← hash(c, w1,d,w2,m1,m2,z1,u1,u2,u3,z2,y,v1,v2,v3,v4)

            s1 ← ex1 + αt1                    ← ex2 + δ
                     e
            s2 ← (r1) β mod  Nt2              ← eρ3 +  mod  q
                                                    e         2
            s3 ← eρ1 + γt3                    ← (r2) µ mod  N
                                           t4 ← eρ2 + ν

            Π ←  <z1,u1,u2,u3,z2,y,v1,v2,v3,v4,s1,s2,s3,t1,t2,t3,t4>

                            Fig. 2. Construction of Π

            <z1,u1,u2,u3,z2,y,v1,v2,v3,v4,s1,s2,s3,t1,t2,t3,t4>  ←  Π
                                                    t1+t2     e
       Verify s1,t1 ∈ Zq3 .                 Verify d     ≡p  y v1.
               s1        e                             s1 t2     e
       Verify c  ≡p  (w1) u1.               Verify (w2)  d   ≡p y v2.
               s1    N          e                   t1   N           e
       Verify g  (s2)  ≡N2  (m1) u2.        Verify g  (t3) ≡N2  (m2)  v3.
                  s1    s3        e                    t1    t4        e
       Verify (h1)  (h2)  ≡N˜ (z1) u3.      Verify (h1)  (h2)  ≡N˜ (z2) v4.

                             Fig. 3. Veriﬁcation of Π
152    P. MacKenzie and M.K. Reiter


      α ←R  Zq3                      δ ←R  Zq3
             ∗                               ∗
      β ←R  ZN0                      µ ←R   ZN
      γ ←R  Zq3N˜                    ν ←R   Zq3N˜
      ρ1 ←R  ZqN˜                    ρ2 ←R  ZqN˜
                                     ρ3 ←R  Zq

                                     ρ4 ←R  Zq5N˜
                                      ←R  Zq
                                     σ ←R   Zq7
                                     τ ←R   Zq7N˜

               x1    ρ1      ˜                 x2    ρ2      ˜
      z1 ← (h1)  (h2)   mod  Nz2        ←  (h1)  (h2)  mod  N
            α                              x2+ρ3
      u1 ← c  mod  py←                    d      mod  p
             0 α  N0        0 2             δ+
      u2 ← (g ) β   mod (N   )       v1 ←  d    mod  p
               α     γ      ˜                  α  
      u3 ← (h1)  (h2) mod  Nv2          ←  (w2) d  mod  p
                                                α    δ  qσ N        2
                                     v3 ←  (m3)  (m4) g   µ  mod  N
                                               δ    ν      ˜
                                     v4 ←  (h1) (h2)  mod N
                                               x3    ρ4      ˜
                                     z3 ←  (h1)  (h2)  mod  N
                                               σ    τ      ˜
                                     v5 ←  (h1) (h2)  mod  N

         e ← hash(c, w1,d,w2,m1,m2,z1,u1,u2,u3,z2,z3,y,v1,v2,v3,v4,v5)

      s1 ← ex1 + αt1                    ←  ex2 + δ
               e         0
      s2 ← (r1) β mod  N             t2 ←  eρ3 +  mod q
                                              e
      s3 ← eρ1 + γt3                    ←  (r2) µ mod N
                                     t4 ←  eρ2 + ν
                                     t5 ←  ex3 + σ
                                     t6 ←  eρ4 + τ

        0
      Π  ←  <z1,u1,u2,u3,z2,z3,y,v1,v2,v3,v4,v5,s1,s2,s3,t1,t2,t3,t4,t5,t6>

                            Fig. 4. Construction of Π0

                                                                        0
         <z1,u1,u2,u3,z2,z3,y,v1,v2,v3,v4,v5,s1,s2,s3,t1,t2,t3,t4,t5,t6> ← Π
                                               t1+t2    e
   Verify s1,t1 ∈ Zq3 .                 Verify d    ≡p y v1.
                                                  s1 t2    e
   Verify t5 ∈ Zq7 .                    Verify (w2) d  ≡p y v2.
          s1       e                              s1    t1 qt5  N          e
   Verify c ≡p (w1) u1.                 Verify (m3) (m4)  g  (t3)  ≡N2 (m2) v3.
                  0
          0 s1   N            e                   t1   t4       e
   Verify (g ) (s2) ≡(N0)2 (m1) u2.     Verify (h1) (h2) ≡N˜ (z2) v4.
            s1    s3       e                      t5   t6       e
   Verify (h1) (h2) ≡N˜ (z1) u3.        Verify (h1) (h2) ≡N˜ (z3) v5.

                             Fig. 5. Veriﬁcation of Π0

prover need not know   sk or sk0, though a malicious prover might know    sk0.We
assume the veriﬁer knows   n1 and  n2. If necessary, the veriﬁer should verify that
               ∗                                       ∗                  ∗
c, d, w1,w2 ∈ Zp and are of order  q, and that  m1 ∈  Z(N 0)2 and m2  ∈ ZN 2 . The
proof of the following lemma will appear in the full version of this paper.

Lemma 2.     Π0 is a noninteractive zero-knowledge proof of  P 0.
                                  Two-Party Generation of DSA Signatures       153

References

 1. J. Benaloh. Dense probabilistic encryption. In Workshop on Selected Areas of Cryp-
    tography, pages 120–128, 1994.
 2. N. Bari´c and B. Pﬁtzmann. Collision-free accumulators and fail-stop signature
    schemes without trees. In EUROCRYPT ’96    (LNCS 1233), pages 480–494, 1997.
 3. M. Blum, A. DeSantis, S. Micali, and G. Persiano. Noninteractive zero-knowledge.
    SIAM  Journal of Computing  20(6):1084–1118, 1991.
 4. C. Boyd. Digital multisignatures. In H. J. Beker and F. C. Piper, editors, Cryp-
    tography and Coding, pages 241–246. Clarendon Press, 1986.
 5. M. Bellare and P. Rogaway. Random oracles are practical: A paradigm for design-
    ing eﬃcient protocols. In 1st ACM Conference on Computer and Communications
    Security, pages 62–73, November 1993.
 6. R. A. Croft and S. P. Harris. Public-key cryptography and reusable shared secrets.
    In H. Baker and F. Piper, editors, Cryptography and Coding, pages 189–201, 1989.
 7. M. Cerecedo, T. Matsumoto, H. Imai. Eﬃcient and secure multiparty generation
    of digital signatures based on discrete logarithms. IEICE Trans. Fundamentals of
    Electronics Communications and Computer Sciences  E76A(4):532–545, April 1993.
 8. Y. Desmedt. Society and group oriented cryptography: a new concept. In CRYPTO
    ’87 (LNCS 293), pages 120–127, 1987.
 9. Y. Desmedt and Y. Frankel. Threshold cryptosystems. In   CRYPTO    ’89 (LNCS
    435), pages 307–315, 1989.
10. T. ElGamal. A public key cryptosystem and a signature scheme based on discrete
    logarithms. IEEE Transactions on Information Theory, 31:469–472, 1985.
11. FIPS 180-1. Secure hash standard. Federal Information Processing Standards Pub-
    lication 180-1, U.S. Dept. of Commerce/NIST, National Technical Information Ser-
    vice, Springﬁeld, Virginia, 1995.
12. FIPS 186. Digital signature standard. Federal Information Processing Standards
    Publication 186, U.S. Dept. of Commerce/NIST, National Technical Information
    Service, Springﬁeld, Virginia, 1994.
13. Y. Frankel. A practical protocol for large group oriented networks. In EURO-
    CRYPT ’89   (LNCS 434), pages 56–61, 1989.
14. Y. Frankel, P. MacKenzie, and M. Yung. Adaptively-secure distributed threshold
    public key systems. In European Symposium   on Algorithms (LNCS 1643), pages
    4–27, 1999.
15. E. Fujisaki and T. Okamoto. Statistical zero-knowledge protocols to prove modular
    polynomial relations. In CRYPTO ’97  (LNCS 1294), pages 16–30, 1997.
16. R. Gennaro, S. Jarecki, H. Krawczyk, and T. Rabin. Robust threshold DSS signa-
    tures. In EUROCRYPT ’96    (LNCS 1070), pages 354–371, 1996.
17. R. Gennaro, S. Jarecki, H. Krawczyk, and T. Rabin. Secure distributed key gen-
    eration for discrete-log based cryptosystems. In EUROCRYPT ’99   (LNCS 1592),
    pages 295–310, 1999.
18. S. Goldwasser and S. Micali. Probabilistic encryption. Journal of Computer and
    System Sciences 28:270–299, 1984.
19. S. Goldwasser, S. Micali, and R. L. Rivest. A  digital signature scheme secure
    against adaptive chosen-message attacks. SIAM Journal of Computing  17(2):281–
    308, April 1988.
20. L. Harn. Group oriented (t, n) threshold digital signature scheme and digital mul-
    tisignature. IEE Proc.-Comput. Digit. Tech. 141(5):307–313, 1994.
                     154    P. MacKenzie and M.K. Reiter

                     21. A. Herzberg, M. Jakobsson, S. Jarecki, H. Krawczyk, and M. Yung. Proactive
                         public-key and signature schemes. In 4th ACM Conference on Computer and Com-
                         munications Security, pages 100–110, 1997.
                     22. T. Hwang. Cryptosystem   for group oriented cryptography. In EUROCRYPT ’90
                         (LNCS 473), pages 352–360, 1990.
                     23. S. Jarecki and A. Lysyanskaya. Adaptively secure threshold cryptography: intro-
                         ducing concurrency, removing erasures. In EUROCRYPT 2000   (LNCS 1807), pages
                         221–242, 2000.
                     24. J. Kilian, E. Petrank, and C. Rackoﬀ. Lower bounds for zero knowledge on the
                         internet. In 39th IEEE Symposium   on Foundations of Computer Science, pages
                         484–492, 1998.
                     25. D. W. Kravitz. Digital signature algorithm. U.S. Patent 5,231,668, 27 July 1993.
                     26. S. Langford. Threshold DSS signatures without a trusted party. In CRYPTO ’95
                         (LNCS 963), pages 397–409, 1995.
                     27. P. MacKenzie and M. K. Reiter. Networked cryptographic devices resilient to cap-
                         ture. DIMACS Technical Report 2001-19, May 2001. Extended abstract in    2001
                         IEEE Symposium   on Security and Privacy, May 2001.
                     28. D. Naccache and J. Stern. A new public-key cryptosystem. In EUROCRYPT ’97
                         (LNCS 1233), pages 27–36, 1997.
                     29. M. Naor and M. Yung. Public-key cryptosystems provably secure against chosen
                         ciphertext attacks. In 22nd ACM Symposium on Theory of Computing, pages 427–
                         437, 1990.
                     30. T. Okamoto and S. Uchiyama. A new public-key cryptosystem, as secure as fac-
                         toring. In EUROCRYPT ’98    (LNCS 1403), pages 308–318, 1998.
                     31. P. Paillier. Public-key cryptosystems based on composite degree residuosity classes.
                         In EUROCRYPT ’99     (LNCS 1592), pages 223–238, 1999.
                     32. C. Park and K. Kurosawa. New ElGamal type threshold digital signature scheme.
                         IEICE Trans. Fundamentals of Electronics Communications and Computer Sci-
                         ences E79A(1):86–93, January, 1996.
                     33. T. Pedersen. A threshold cryptosystem without a trusted party. In EUROCRYPT
                         ’91 (LNCS 547), pages 522–526, 1991.
                     34. A. Yao. Protocols for secure computation. In 23rd IEEE Symposium   on Founda-
                         tions of Computer Science, pages 160–164, 1982.


View publication stats
