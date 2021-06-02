An Accumulator Based on Bilinear Maps and
Efficient Revocation for Anonymous Credentials
Jan Camenisch1
, Markulf Kohlweiss2
, and Claudio Soriente3
1
IBM Research Zurich jca@zurich.ibm.com
2 Katholieke Universiteit Leuven / IBBT markulf.kohlweiss@esat.kuleuven.be
3 University of California, Irvine csorient@ics.uci.edu
Abstract. The success of electronic authentication systems, be it eID card systems or Internet authentication systems such as CardSpace,
highly depends on the provided level of user-privacy. Thereby, an important requirement is an efficient means for revocation of the authentication credentials. In this paper we consider the problem of revocation
for certificate-based privacy-protecting authentication systems. To date,
the most efficient solutions for revocation for such systems are based on
cryptographic accumulators. Here, an accumulate of all currently valid
certificates is published regularly and each user holds a witness enabling
her to prove the validity of her (anonymous) credential while retaining
anonymity. Unfortunately, the users’ witnesses must be updated at least
each time a credential is revoked. For the know solutions, these updates
are computationally very expensive for users and/or certificate issuers
which is very problematic as revocation is a frequent event as practice
shows.
In this paper, we propose a new dynamic accumulator scheme based on
bilinear maps and show how to apply it to the problem of revocation of
anonymous credentials. In the resulting scheme, proving a credential’s
validity and updating witnesses both come at (virtually) no cost for
credential owners and verifiers. In particular, updating a witness requires
the issuer to do only one multiplication per addition or revocation of a
credential and can also be delegated to untrusted entities from which
a user could just retrieve the updated witness. We believe that thereby
we provide the first authentication system offering privacy protection
suitable for implementation with electronic tokens such as eID cards or
drivers’ licenses.
Keywords: dynamic accumulators, anonymous credentials, revocation.
1 Introduction
The desire for strong electronic authentication is growing not only for the Internet but also in the physical world where authentication tokens such as electronic
identity cards, driving licenses, and e-tickets are being widely deployed and are
set to become a pervasive means of authentication. It has been realized that
thereby the protection of the citizens’ privacy is of paramount importance and
hence that the principle of data minimization needs to be applied: any individual
should only disclose the minimal amount of personal information necessary for
the transaction at hand. While privacy is of course not a major concern for the
primary use of these tokens, e.g., for e-Government, it becomes vital for their
so-called secondary use. For instance, when accessing a teenage chat room with
an e-ID card, users should only have to reveal that they are indeed between, say,
10 and 16 years old but should not reveal any other information stored on the
card such as birth date, name or address.
In the literature, there exist a fair number of privacy-preserving technologies
that allow one to meet these requirements. These technologies include anonymous credential systems [1–3], pseudonym systems [4–7], anonymous e-cash [8–
10], or direct anonymous attestation [11]. Almost all of these schemes exhibit
a common architecture with certificate issuers, users (certificate recipients) and
certificate verifiers: Users obtain a signature from an issuing authority on a
number of attributes and, at later time, can convince verifiers that they indeed
possess a signature on those attributes [12]. Individual transactions are anonymous and unlikable by default and users can select which portions of a certificate
to reveal, which portions to keep hidden, and what relations between certified
items to expose.
A crucial requirement for all authorization and authentication systems is
that certificates issued can be later revoked, in case of unexpected events or malicious use of the certificate. For traditional certificates, this is typically achieved
either by publishing a certificate revocation list or by enforcing a short certificate
lifetime via expiration date. For anonymous certificates, the former approach violates privacy while the latter is typically rather inefficient as it would require
the users to frequently engage in the usually quite involved issuing protocol.
In principle, the approach of certificate revocation list can be made to work
also for anonymous credentials by having the user to prove in zero-knowledge
that her certificate is not contained on the (black) list. Such a proof, however,
would not be efficient as the computational and communication cost of the user
and the verifier become preventive as they grow at least logarithmic with number
of entries in the list. The literature provides two kinds solutions that overcome
this.
The first kind is called verifier local revocation [13, 14, 11, 15]. In the best
solution here, the cost for the user is independent of the number of entries in
the revocation list, but the computational cost of the verifier is linear in this
number (at least a modular exponentiation or, worse, a pairing operation per
entry). Thus, these solutions are not at all suited for large scale deployments.
The second kind [16, 17] employs cryptographic accumulators [18]. Such accumulators allow one to hash a large set of inputs in a single short value, the
accumulator, and then provide evidence by an accumulator witness that a given
value is indeed contained in the accumulator. Thus, the serial numbers of all
currently valid credentials are accumulated and the resulting value is published.
Users can then show to verifiers that their credential is still valid, by using their
witness to prove (in zero-knowledge) that their credential’s serial number is contained in the published accumulator. Such proofs can be realized with practical
efficiency [16, 17] and incur only cost to the user and the verifier that are independent of the number of revoked or currently valid credentials. The drawback
of these solutions, however, is that the users need to update their accumulator
witnesses and an update requires at least one modular exponentiation for each
newly revoked credential. Assuming a driving license application and based on
the, e.g., 0.07% rate of driver’s license revocation in West Virginia USA [19],
the number of credentials revoked will quickly become a couple of thousands per
day. Thus, these solutions incur a computational (and communication) cost far
greater that what an electronic token such as a smart card can possibly handle.
Our contribution. In this paper we are therefore considering revocation solutions
that incur (virtually) no cost to the verifier and the users, and only limited costs
to the issuer (or the revocation authority). More precisely, for each revocation
epoch (e.g., every day), verifiers and users need to retrieve the issuer’s current
public key (i.e., the new accumulator value) while users further need to retrieve
their witnesses (a single group element). Upon revocation of a credential, the
revocation authority only needs to perform one multiplication per remaining
user to update (and provide) the users’ witnesses, a cost which can easily be
handled by today’s standards. We note that this update operation requires no
secret keys and does not need to be performed by the issuer, i.e., it could be
performed by other untrusted entities.
As building block for this solution, we introduce a novel dynamic accumulator
based on bilinear maps and show how to employ it for revocation at the example
of the Bangerter, Camenisch and Lysyanskaya private certificate framework [12],
which is essentially a generalization of e-cash, anonymous credentials, and group
signatures. Thus we provide for the first time a practical solution for anonymous
authentication with e-ID cards.
Related Work. Camenisch and Lysyanskaya [17] introduce a dynamic accumulator and show its applicability to revocation in Anonymous Credential Systems
as well as Identity Escrow and Group Signatures. Update of the proposed accumulator, as well as user witnesses, require a number of exponentiations that is
linear in the number of users added to or revoked from the system. In [20], the
authors extend the above accumulator, introducing witnesses and proofs that a
value was not accumulated.
Nguyen [21] constructs a dynamic accumulator from bilinear pairings. Its application to an anonymous credential system require users to store large system
parameters, in order to prove validity of their credential. Moreover, updating a
witness takes one exponentiation per event and therefore is not efficient enough
for what we are after (in the paper the authors write multiplication and use addition as base operation for the algebraic group as is done sometimes in connection
with bi-linear maps and elliptic curve groups).
In [22], the authors propose a dynamic accumulator for batch update. Users
who missed many witness updates, can request update information to the issuer
and update their witness with one multiplication. In our scheme, we can provide
the same feature, relaxing the requirement that the issuer takes part to the
witness update. We note, however, that the authors do not show how to achieve
an efficient proof of knowledge of an element contained in the accumulator as is
needed for the use of the accumulator for revocation of credentials.
Outline. The rest of the paper is organized as follow. In Section 2 we discuss
assumptions and recall existing building blocks. In Section 3 we introduce our
novel dynamic accumulator. In Section 4 we show how to extend the Bangerter
et al. private certificate framework with an efficient revocation mechanism. Conclusion and further discussion are given in Section 5
2 Preliminaries
In this section we list assumptions and cryptographic tools used as building
blocks of the introduced accumulator as well as our anonymous credential revocation system.
A function ν is negligible if, for every integer c, there exists an integer K such
that for all k > K, |ν(k)| < 1/kc
. A problem is said to be hard (or intractable)
if there exists no probabilistic polynomial time (p.p.t.) algorithm on the size of
the input to solve it.
Bilinear Pairings Let G and GT be groups of prime order q. A map e : G×G →
GT must satisfy the following properties:
(a) Bilinearity: a map e : G × G → GT is bilinear if e(a
x
, by
)t = e(a, b)
xy;
(b) Non-degeneracy: for all generators g, h ∈ G, e(g, h) generates GT ;
(c) Efficiency: There exists an efficient algorithm BMGen(1k
) that outputs (q, G,
GT , e, g) to generate the bilinear map and an efficient algorithm to compute
e(a, b) for any a, b ∈ G.
The security of our scheme is based on the following number-theoretic assumptions. Our accumulator construction is based on the Diffie-Hellman Exponent assumption. The unforgeability of credentials is based on the Strong
Diffie-Hellman assumption. For credential revocation we need to prove possession of an accumulator witness for a credential. This proof is based on our new
Hidden Strong Diffie-Hellman Exponent (SDHE) assumption.
Definition 1 (n-DHE). Diffie-Hellman Exponent (DHE) assumption: The nDHE problem in a group G of prime order q is defined as follows: Let gi =
g
γ
i
, γ ←R Zq. On input {g, g1, g2, . . . , gn, gn+2, . . . , g2n} ∈ G2n, output gn+1.
The n-DHE assumption states that this problem is hard to solve.
Boneh, Boyen, and Goh [23] introduced the Bilinear Diffie-Hellman Exponent
(BDHE) assumption that is defined over a bilinear map. Here the adversary has
to compute e(g, h)
γ
n+1
∈ GT .
Lemma 1. The n-DHE assumption for a group G with a bilinear pairing e :
G × G → GT is implied by the n-BDHE assumption for the same groups.
Boneh and Boyen introduced the Strong Diffie-Hellman assumption in [24].
Definition 2 (n-SDH [24]). On input g, gx
, gx
2
, . . . , gx
n ← G, it is computationally infeasible to output (g
1/(x+c)
, c).
Boyen and Waters [25] introduced the Hidden Strong Diffie-Hellman assumption under which BB signatures [24] are secure for any message space. We require
a variant of the Hidden Strong Diffie-Hellman assumption that we call the Hidden
Strong Diffie-Hellman Exponent (n-HSDHE) assumption. The two assumptions
are hitherto incomparable.
Definition 3 (n-HSDHE). Given g, gx
, u ∈ G, {g
1/(x+γ
i
)
, gγ
i
, uγ
i
}i=1...n, and
{g
γ
i
}i=n+2...2n, it is infeasible to compute a new tuple (g
1/(x+c)
, gc
, uc
).
2.1 Known Discrete-Logarithm-Based, Zero-Knowledge Proofs
In the common parameters model, we use several previously known results for
proving statements about discrete logarithms, such as (1) proof of knowledge
of a discrete logarithm modulo a prime [26], (2) proof of knowledge of equality
of some elements in different representation [27], (3) proof that a commitment
opens to the product of two other committed values [28–30], and also (4) proof
of the disjunction or conjunction of any two of the previous [31].
When referring to the above proofs, we will follow the notation introduced by
Camenisch and Stadler [32] for various proofs of knowledge of discrete logarithms
and proofs of the validity of statements about discrete logarithms. For instance,
PK{(α, β, δ) : y = g
αh
β ∧ y˜ = ˜g
αh˜δ
}
denotes a “zero-knowledge Proof of Knowledge of integers α, β, and δ such that
y = g
αh
β and y˜ = ˜g
αh˜δ holds,” where y, g, h, y, ˜ g, ˜ and h˜ are elements of some
groups G = hgi = hhi and G˜ = hg˜i = hh˜i that have the same order. (Note that
the some elements in the representation of y and ˜y are equal.) The convention is
that values (α, β, δ) denote quantities of which knowledge is being proven (and
are kept secret), while all other values are known to the verifier. For prime-order
groups which include all groups we consider in this paper, it is well known that
there exists a knowledge extractor which can extract these quantities from a
successful prover.
2.2 Signature Scheme with Efficient Protocols
For our credential system we use a signature scheme that is loosely based on weak
Boneh and Boyen signatures [24, 16]. It is described in [33] and has been proven
secure under the n-SDH assumption [34, 35]. It assumes a non-degenerate bilinear
map e : G × G → GT of prime order q with generators h, h0, h1, . . . , h`, h`+1.
The signer’s secret key is x ∈ Zq while the public key is y = h
x
.
A signature on a message m ∈ Z
∗
q
is computed by picking c, s ← Z
∗
q and
computing σ = (h0h
m
1 h
s
2
)
1
x+c . The signature is (σ, c, s). It is verified by checking whether e(σ, yh
c
) = e(h0h
m
1 h
s
2
, h). Multiple messages m1, . . . , m` ∈ Z
∗
q
can
be signed as σ = (h0h
m1
1
· · · h
m`
`
h
s
`+1)
1
x+c and verification is done by checking
whether e(σ, yh
c
) = e(h0h
m
1
· · · h
m`
`
h
s
`+1, h).
Proving Knowledge of a Signature. Now assume that we are given a signature
(σ, c, s) on messages m1 . . . , m` ∈ Zq and want to prove that we indeed possess
such a signature. To this end, we need to augment the public key with a value
h˜ ∈ G such that logh h˜ are not known.
Knowledge of a signature is proven as follows:
1. Choose random values r ← Zq and open ← Zq and compute a commitment
B = h
rh˜open and a blinded signature A = σh˜r
.
2. Compute the following proof
PK{(c, s, r, open, mult, tmp, m1, . . . , m`) :
B = h
rh˜open ∧ 1 = B
ch
−multh˜−tmp ∧
e(h0, h)
e(A, y)
= e(A, h)
c
·e(h, ˜ y)
−r
·e(h, h ˜ )
−mult
·
Y
`
i=1
e(hi
, h)
−mi
·e(h`+1, h)
−s
} .
Why this proof works is explained in Appendix B.
3 A Pairing Based Dynamic Accumulator with Efficient
Updates
We define and build a dynamic accumulator with efficient updates and assess
its security. With efficient updates we mean that witnesses can be updated by
any party without knowledge of any secret key and require only multiplications
(no exponentiations) linear in the number of changes to the accumulator. Our
construction is based on the broadcast encryption scheme by Boneh, Gentry and
Waters [36].
3.1 Definition of Dynamic Accumulators
A secure accumulator consists of the five algorithms AccGen, AccAdd, AccUpdate,
AccWitUpdate, and AccVerify.
These algorithms are used by the accumulator authority (short authority),
an untrusted update entity, a user and a verifier. The authority creates an accumulator key pair (skA, pkA), the accumulator acc∅ and a public state state∅
using the AccGen algorithm; it can add a new value i to the accumulator accV
using the AccAdd algorithm to obtain a new accumulator accV ∪{i} and state
stateU∪{i}, together with a witness witi
. The accumulator for a given set of
values V , can be computed using the AccUpdate algorithm.
Throughout these operations, accV and witi are of constant size (independent of the number of accumulated values). The authority does some bookkeeping about the values contained in the accumulator and the status of the
accumulator when a witness witi was created. These sets are denoted as V and
Vw respectively. The bookkeeping information is made public and is only needed
for updating witnesses, it is not needed for verifying that a value is contained in
an accumulator.
Each time an accumulator changes, the old witnesses become invalid. It is
however possible to update all witnesses for values i ∈ V contained in the accumulator from the bookkeeping information Vw. This updating is the most performance intensive operation in existing accumulator systems. We show how it can
be efficiently offloaded to an untrusted update entity that runs AccWitUpdate
and is only given the accumulator state stateU and the bookkeeping information
V and Vw. The accumulator state stateU also contains book keeping information U, the set of elements ever added to the accumulator (but not necessarily
contained in the current accumulator). This is a superset of V and Vw.
4
After users obtained an updated witness wit0
i
for a value i for the current
accumulator, they can prove to any verifier that i is in the accumulator, using
the AccVerify algorithm.
AccGen(1k
, n) creates an accumulator key pair (skA, pkA), an empty accumulator
acc∅ (for accumulating up to n values) and an initial state state∅.
AccAdd(skA, i, accV , stateU ) allows the authority to add i to the accumulator.
It outputs a new accumulator accV ∪{i} and state stateU∪{i}, together with
a witness witi for i.
AccUpdate(pkA, V , stateU ) outputs an accumulator accV for values V ⊂ U.
AccWitUpdate(pkA, witi
, Vw, accV , V , stateU ) outputs a witness wit0
i
for accV
if witi was a witness for accVw and i ∈ V .
AccVerify(pkA, i, witi
, accV ) verifies that v ∈ V using an up-to-date witness witi
and the accumulator accV . In that case the algorithm accepts, otherwise it
rejects.
Note that the purpose of an accumulator is to have accumulator and witnesses
of size independent of the number of accumulated elements.
Correctness. Correctly accumulated values have verifying witnesses.
Security. For all probabilistic polynomial time adversaries A,
P r[(skA, pkA, accO, stateO) ← AccGen(1k
);
(i, witi) ← A(pkA, accO, stateO)
OAccAdd(.),OAccUpdate(.)
:
AccVerify(pkA, i, witi
, accO) = accept ∧ i ∈/ VO] = neg(k) ,
where the oracles OAccAdd(.) and OAccUpdate(.) keep track of shared variables accO,
stateO and a set VO that is initialized to ∅. The oracle OAccAdd(i) computes and
outputs (accO, stateO, witi) ← AccAdd(skA, i, accO, stateO) and adds i to VO
while OAccUpdate(V ) computes and outputs accO ← AccUpdate(pkA, V , stateO)
and sets VO to V .
4 Allowing accumulators to change their state over time can allow for better performance tradeoffs. While our accumulator construction does not use this possibility in
order to keep things simple, we outline such an optimization in Appendix D.
3.2 Construction
We now construct the algorithms AccGen, AccAdd, AccUpdate, AccWitUpdate,
and AccVerify.
AccGen(1k
, n). Run BMGen(1k
) to obtain the setup paramsBM = (q, G, GT , e, g)
of a bilinear map e : G × G → GT .
Pick a random value γ ∈ Zq. Generate a key pair sk and pk for a secure
signature scheme, for instance the BB signature scheme that is secure under
the SDH assumption. Let pkA = (paramsBM , pk, z = e(g, g)
γ
n+1 ), skA =
(paramsBM , γ, sk), acc∅ = 1 and state∅ = (∅, g1 = g
γ
1
, . . . , gn = g
γ
n
, gn+2 =
g
γ
n+2
, . . . , g2n = g
γ
2n
).5
AccAdd(skA, i, accV , stateU ). Compute w =
Qj6=i
j∈V gn+1−j+i and a signature σi
on giki under signing key sk. The algorithm outputs witi = (w, σi
, gi), an
updated accumulator value accV ∪{i} = accV · gn+1−i
, and stateU∪{i} =
(U ∪ {i}, g1, . . . , gn, gn+2, . . . , g2n).
AccUpdate(pkA, V , stateU ). Check whether V ⊂ U and outputs ⊥ otherwise.
The algorithm outputs accV =
Q
v∈V
gn+1−v for values i ∈ V .
AccWitUpdate(pkA, witi
, Vw, accV , V , stateU ). Parse witi as (w, σi
, gi). If i ∈ V
and V ∪ Vw ⊂ U, compute
w
0 = w ·
Q
j∈V \Vw
gn+1−j+i
Q
j∈Vw\V
gn+1−j+i
.
Output the updated witness wit0
i = (w
0
, σi
, gi). Otherwise output ⊥.
AccVerify(pkA, i, witi
, accV ). Parse witi = (w, σi
, gi). Output accept, if σi
is a
valid signature on giki under verification key pk and e(gi ,accV )
e(g,w) = z. Otherwise output reject.
In the construction above, we accumulate the group elements g1, . . . , gn instead of, e.g., the integers 1, . . . , n. Depending on the application, one would
want to accumulate the latter, or more generally an arbitrary set of size n. In
this case, the issuer of the accumulator would need to publish a mapping from
this set to the gi values that get actually accumulated. In order to avoid large
public parameters during verification the issuer of the accumulator uses a signature scheme to sign the gi together with the value to which they map. Thus,
the verifier can check whether a given gi
is a (potentially) valid input to the
accumulator (cf. discussion in Section 3.3).
We also note that the algorithm to update the witness does not require any
secret information. Thus, the witnesses could be kept up-to-date for the users
either by the users themselves, the issuer, or by some third party. In the latter
5 We define stateU = (U, g1, . . . , gn, gn+2, . . . , g2n) where U is book keeping information that keeps track of all elements that were ever added to the accumulator
(but might have been subsequently removed). The rest of the state is static. See
Appendix D for a modification that reduces the size of stateU .
two cases, the users can just retrieve the current valid witness whenever needed.
In applications, one would typically define epochs such that the accumulator
value and witnesses are only updated at the beginning of each epoch and remain
valid throughout the epoch. Finally note that maintaining the witnesses for all
users is well within reach of current technologies — indeed, all witnesses can be
kept in main memory and the update performed rather quickly.
Correctness. Let accV be an accumulator for skA = (paramsBM , γ, sk), pkA =
(paramsBM , pk, z = e(g, g)
γ
n+1 ), and stateU = (U, g1 = g
γ
1
, . . . , gn = g
γ
n
, gn+2 =
g
γ
n+2
, . . . , g2n = g
γ
2n
Q
). Then a correct accumulator always has a value accV =
j∈V gn+1−j . Moreover, for each i ∈ V with up-to-date witness witi = (w =
Qj6=i
j∈V gn+1−j+i
, σi
, gi) the following equation holds:
e(gi
, accV )
e(g, w)
=
e(g, g)
P
j∈V
γ
n+1−j+i
e(g, g)
Pj6=i
j∈V
γn+1−j+i = e(g, g)
γ
n+1
= z .
Security. Suppose there exists an adversary A that breaks the security of our
accumulator. We show how to construct an algorithm B that either forges the
signature scheme used to sign accumulated elements or breaks the n-DHE assumption.
Algorithm B has access to a signing oracle Oσ and obtains as input the
corresponding signature verification key pk, the parameters of a bilinear map
paramsBM = (q, G, GT , e, g), and an instance of the n-DHE assumption (g1, . . . ,
gn, gn+2, . . . , g2n) ∈ G2n−1
. B provides A with pkA = (paramsBM , pk, z =
e(g1, gn)), acc∅ = 1 and state∅ = (∅, g1, . . . , gn, gn+2, . . . , g2n). The oracle queries
of the adversary are answered as defined in the game except that Oσ is called
for creating the signatures.
Given an adversary that can compute (i, witi) such that the verification succeeds even though i ∈/ VO. We parse witi as (w, σˆi
, gˆi). If ˆgi does not correspond
to gi the adversary attacked the signature and ˆσi
is a signature forgery. Otherwise we learn from the verification equation that
e(gi
, accO) = e(g, w)z
and
e(g, Y
j∈V
gn+1−j+i) = e(g, wgn+1) .
This means that
gn+1 =
Q
j∈V gn+1−j+i
w
.
For i ∈ {1, . . . , n} \ V , all gn+1−j+i are contained in state∅ and it is possible to
compute this value. This breaks the n-DHE assumption.
3.3 Efficient Proof That a Hidden Value was Accumulated
It is often only required for a user to prove that she possesses a value that is
indeed contained in the current accumulator, or in other words, to prove membership of the current accumulator without revealing which value she possesses
(or which index i is assigned to her). In this section, we give an efficient protocol
that achieves this for our accumulator construction.
For the accumulator to be secure, the verifier needs to check that the value the
user claims to own, is one of g1, . . . , gn. In the previous construction, g1, . . . , gn
are authenticated either by making them public as a whole or by having each
one signed (in which case the user would provide the gi and the signature to the
verifier). However, using a public list would require the prover to either reveal
gi (which would violate privacy) or to prove that the gi which she claims possession of, is a valid one. The latter, however, would require an involving proof
that would make the use of the accumulator inefficient. We therefore resort to
sign gi values and then require the prover to prove that she knows a signature
by the accumulator issuer on “her” gi without revealing neither the signature
nor the gi value. As such a proof needs to be efficient, this requires a special signature scheme. Since user never reveal the accumulated valued they are proving
possession of, it is possible to avoid signing gi
||i as it is done in Section 3.2. This
allows for a more efficient signature scheme and proof system.
Prerequisites. We instantiate the signature scheme used for signing the gi with
a variant of the weakly secure Boneh-Boyen scheme [24]. Instead of a gi value we
sign γ
i
. The authentic gi
is a by-product of the signing process. For simplicity
we reduce the security of the accumulator proof directly to the n-HSDHE assumption.6 The n-HSDHE assumption is the weakest assumption under which
we can prove our scheme. The n-HSDHE assumption is implied by the iHSDH
assumption of [37].
The signer (the accumulator issuer) picks a fresh u ← G, secret key sk ← Zq
and public key pk = g
sk . A signature consists of the two elements σi = g
1/(sk+γ
i
)
and ui = u
γ
i
and is verified by checking that e(pk · gi
, σi) = e(g, g).
Let pkA = (paramsBM , pk, z = e(g, g)
γ
n+1 ), skA = (paramsBM , γ, sk) and
stateU = (∅, g1 = g
γ
1
, . . . , gn = g
γ
n
, gn+2 = g
γ
n+2
, . . . , g2n = g
γ
2n
) be as generated by the accumulator operations in the previous section. We also pick an
additional h˜ ← G for commitments. The discrete logarithm of h and u with
respect to g must be unknown to the prover.
Q
Proof of Knowledge. For arbitrary V ⊂ {1, . . . , n} and i ∈ V , on input accV =
i∈V
gn+1−i and the corresponding witness witi = (w, σi
, ui
, gi), where w = Qj6=i
j∈V gn+1−j+i
, for value i, the prover performs the following randomization:
6 We do not prove the signature scheme itself secure, but we refer to [37] for a similar
scheme.
Pick at random r, r0
, r00, r000
, open ∈ Zq and computing G = gih˜r
, W = wh˜r
0
,
D = g
rh˜open
, S = σih˜r
00 , and U = uih˜r
000 respectively. Then the prover, proves
PK{(r, r0
, r00, r000
, open, mult, tmp) : D = g
rh˜open ∧ 1 = Dr
00
g
−multh˜−tmp∧
e(pk · G, S)
e(g, g)
= e(pk · G, h˜)
r
00
e(h, ˜ h˜)
−mult e(h, ˜ S)
r∧
e(G, accV )
e(g, W)z
= e(h, ˜ accV )
r
e(1/g, h˜)
r
0
∧
e(G, u)
e(g, U)
= e(h, u ˜ )
r
e(1/g, h˜)
r
000} .
Theorem 1. Under the n-DHE and the n-HSDHE assumptions the protocol
above is a proof of knowledge of a randomization value r that allows to derandomize G to a value gi, where i is accumulated in accV , i.e., i ∈ V . The
proof of this theorem can be found in Section C.1.
4 Efficient Revocation of Private Certificates
In this section we will show how to employ our accumulator to achieve efficient revocation for schemes where users get some form of certificate and then
later can use these certificates in an anonymity protecting way. Such schemes
include group signatures, anonymous credential systems, pseudonym systems,
anonymous e-cash, and many others. Most of these schemes work as follows. In
a first phase an issuer provides the user with a signature on a number of messages. Then, in a second phase the user convinces the verifier that 1) she owns
a signatures by the issuer on a number of messages and 2) that these messages
satisfy some further properties that are typically dependent on the particular
purpose of the scheme. Based on this observation, Bangerter et al. [12] give a
cryptographic framework for the controlled release of certified information. They
also show how different applications (such as the ones mentioned above) can be
realized. Thus, they basically generalize the concepts of anonymous credentials,
anonymous e-cash, and group signatures into a single framework. We therefore
just show how their framework can be extended with revocation to provide this
features for all these applications. From this it will become clear how to extend
particular schemes (e.g., the anonymous credentials and group signatures [16,
33]) with our revocation mechanisms.
More precisely, Bangerter et al. employ special signature protocols, called CL
signatures [?], for issuing private certificates to users. A private certificate (1)
consists of attributes and a signature over the attributes much alike a traditional
certificate, only that a more powerful signature scheme is used.
cert = (σ, m1, . . . , ml) with σ = Sign(m1, . . . , ml
; skI ) (1)
Let (skI , pkI ) ← IssuerKeygen(1k
) be the certificate issuer’s keypair. The
framework supports two types of protocols: 1) an interactive certificate issuing
protocol ObtainCert that allows to obtain a signature on committed values without revealing these values and 2) efficient zero-knowledge proofs of knowledge of
signature possession.
Let (m1, . . . , m`) denote a list of data items and H ⊂ L = {1, . . . , `} a
subset of data items. Using the first protocol, a user can obtain a certificate on
(m1, . . . , m`) such that the issuer does not learn any information on the data
items in H, while it learns the other data items, i.e., L \ H.
The private certificates of a user remain private to the user, that is, they are
never released (as a whole) to any other party: when using (showing) certificates
for asserting attribute information, the user proves that she knows (has) certificates with certain properties. The user may release certain attributes, while only
proving the knowledge of the rest of the certificate:
PK{(σ, m1, . . . , m`
0 ) : 1 = VerifySign(σ, m1, . . . m`
0 , m`
0+1, . . . , m`; pkI ) ∧ . . . }
In the above proof only the attribute values of m`
0+1 to m` are revealed.
Certificate revocation. We now extend the above framework with certificate revocation as follows. Let V be the set of valid certificates for an epoch with epoch
information epochV
. A certificate is assigned a unique identifier i (which will be
embedded into it as one of the attributes) and a witness wit i
. We require that
the user can prove to a verifier that she possesses a non-revoked certificate only
if i ∈ V . This is achieved by having the user prove that the identifier embedded
into her credential is a valid one for the current epoch. Thus, before engaging
in a proof, the user needs to update her witness and both parties (the user and
the verifier) need to obtain the most up-to-date epoch information epochV
for
V . The user can either update the witness herself, or just retrieve the currently
valid witness from a witness update entity. Indeed, a witness update computation does not require knowledge of any secret and can be performed by untrusted
entities (e.g., by a third party or a high availability server cluster at the issuer).
In particular, those entities are only responsible for computing user witnesses
according to the current epoch information. Misbehavior by such entities would
lead in a denial of service (the verification algorithm would reject, but would not
break the security of the system). Also note that a witness update requires a
number of multiplications that is linear in the number of elements added to or
removed from the accumulator, hence providing such an update service to users
is feasible (one could even hold all users’ witnesses in main memory).
More formally, a certificate revocation system for the certification framework consists of updated IssuerKeygen and ObtainCert protocols, new algorithms
UpdateEpoch and UpdateWitness for managing revocation, and a zero-knowledge
proof system for a new predicate VerifyEpoch that allows to prove possession of
a witness wit i
:
IssuerKeygen(1k
, n) creates the issuer key pair (skI , pkI ), the epoch information
epoch∅
, and state∅ for issuing up to n certificates.
ObtainCert(U(pkI , H, {mj}j∈H), I(skI , H, {mj}j∈L\H, epochV
, stateU )) allows a
user to obtain a private certificate cert i from the issuer. The issuer computes and publishes the user’s witness wit i
, and updated epoch information
epochV ∪{i} and stateU∪{i}.
UpdateEpoch(V, stateU ) outputs epoch information epochV
, if V ⊂ U. Otherwise
it outputs ⊥.
UpdateWitness(wit i
, epochV
, stateU ) outputs an updated witness wit0
i
if V ⊂ U.
Otherwise it outputs ⊥.
A user who knows a certificate cert i and a corresponding up-to-date witness
wit i can prove, to a verifier, possession of the certificate and its validity for the
current epoch using the new predicate VerifyEpoch as follows. The user’s secret
input is cert i
. The common input of the protocol is the issuer’s public key pkI ,
the epoch information epochV
, and a specification of the proof statement (this
includes the information revealed about the certificate). In the example below
the user chooses to keep the first `
0 messages secret while he reveals the rest of
the messages.
PK{(σ, m1, . . . , m`
0 , i, wit i) : 1 =VerifySign(σ, m1, . . . m`
0 , m`
0+1, . . . , m`, i; pkI )∧
1 =VerifyEpoch(i, wit i
; epochV
, pkI )} .
Using the Bangerter et al. framework [12], it is not hard to extend this proof or
combine it with other proof protocols given therein.
4.1 Adapted Signature Scheme for Accumulated Values
As described above, a user would have to prove that the value i encoded into
her credential is also contained in the current accumulator. However, the accumulator construction as given in the previous section does not allow one to
accumulate i directly but only gi = ˜g
γ
i
. Now, instead of introducing a mapping
of i to gi (and including this in our proofs which would make them inefficient),
we are going to make the mapping implicit by including gi
into the credential.
Thus, the gi values will be used both in the private certificate and the accumulator to represent the certificate id i. This requires that we extend the signature
scheme in Section 2.2 to allow verification without knowing the secret exponent
γ
i
:
1. The signer creates g, h, h0, h1, . . . , h`, h`+1 ← G and creates keys x ∈ Zq and
y = h
x
.
2. Next, the signer publishes a list (g1 = g
γ
, . . . , gn = g
γ
n
) that he allows in
signatures.
3. The signer picks random c, s ← Z
∗
q and then computes the signature as
(σ = (h0h
m1
1
· · · h
m`
`
gih
s
`+1)
1
x+c , c).
4. A signature (σ, c, s) on messages m1, . . . , m`, gˆi
is verified by checking that
gˆi
is in the list of gi values and that e(σ, yh
c
) = e(h0(
Q`
j=1 h
mj
j
)ˆgih
s
`+1, h)
holds.
We note that the check that ˆgi
is in the list of gi values as prescribed in the last
step will later on be replaced by a signature/authenticator on gi as done for the
accumulator in Section 3.3.
It is straightforward to reduce the security of this modified signature scheme
to the original one with ` + 1 messages as the signer knows the “messages” γ
i
encoded by the gi
. We omit the details here.
4.2 Construction
IssuerKeygen(1k
, n). Run BMGen(1k
) to generate the parameters paramsBM =
(q, G, GT , e, g) of a (symmetric) bilinear map e : G×G → GT . Pick additional
bases h, h0, . . . , h`+1, h, u ˜ ← G and x , sk, γ ← Zq and compute y = h
x
and pk = g
sk
.
7 Compute g1, ..., gn, gn+2, ..., g2n, where gi = g
γ
i
, and z =
e(g, g)
γ
n+1
.
Output (skI , pkI ) = ((paramsBM , x , sk, γ),(paramsBM , y, h, h0, . . . , h`+1, h, u, ˜ pk, z)),
epoch∅ = (acc∅ = 1, ∅), and state∅ = (∅, g1, ..., gn, gn+2, ..., g2n).
ObtainCert(U(pkI , H, {mj}j∈H), I(skI , H, {mj}j∈L\H, epochV
, stateU ). The user
runs the following protocol to obtain a certificate cert i from the issuer:
1. The user chooses a random s
0 ∈ Z
∗
q
, computes X =
Q
j∈H h
mj
j h
s
0
`+1, and
sends X to the issuer.
2. The user (as prover) engages the issuer (as verifier) in the following proof
PK{{mj}j∈H, s0
) : X =
Y
j∈H
h
mj
j h
s
0
`+1} ,
which will convince the issuer that X is correctly formed.
3. The issuer parses epochV as (accV , V ) and stateU as (U, g1, . . . , gn, gn+2,
. . . , g2n). He then computes epochV ∪{i} = (accV · gn+1−i
, V ∪ {i}) and
stateU∪{i} = (U ∪ {i}, g1, . . . , gn, gn+2, . . . , g2n)
8
.
4. The issuer chooses random c, s00 ∈ Z
∗
q and then computes the signature
σ = ((Q
j∈L\H h
mj
j
)Xgih
s
00
`+1)
1/(x+c)
.
5. The issuer computes w =
Qj6=i
j∈V
gn+1−j+i
, σi = g
1/(sk+γ
i
)
, and ui = u
γ
i
and sets wit i = (σi
, ui
, gi
, w, V∪{i}).
6. The issuer sends (σ, c, s00
, {mj}j∈L\H, gi
, i) to the user and outputs wit i
,
epochV ∪{i}
, and stateU∪{i}.
7. The user verifies the certificate gotten and outputs cert i = (σ, c, m1, . . . ,
m`, gi
, s = s
0 + s
00, i).
UpdateEpoch(V, stateU ) checks whether V ⊂ U and outputs ⊥ otherwise. The
algorithm creates epochV
for proving possessions of cert i
Q , i ∈ V . Let accV =
i∈V
gn+1−i
, output epochV = (accV , V ).
UpdateWitness(wit i
, epochV
, stateU ) aborts with ⊥, if V 6⊂ U. Otherwise it parses
wit i as (σi
, ui
, gi
, w, Vw). Let w
0 = w
Q
j∈V \Vw
gn+1−j+i
Q
j∈Vw\V
gn+1−j+i
. The algorithm outputs
wit0
i = (σi
, ui
, gi
, w0
, V ).
Proof protocol. We now show a protocol that allows a user to prove possession
of an unrevoked (and updated) credential credi = (σ, c, m1, . . . , m`, gi
, s, i) using
wit i = (σi
, gi
, ui
, w, Vw). The common input of the protocol is the issuer’s public
7 Note that the discrete logarithms of g, h, h˜ and u with respect to each other are
mutually unknown.
8 Both epochV ∪{i} and stateU∪{i} could be signed by the issuer to prevent proliferation
of fake accumulators.
key pkI , the epoch information epochV
, and a specification of the proof statement
(this includes the information revealed about the certificate). In the example
below the user chooses to keep the first `
0 messages secret while he reveals the
rest of the messages.
The user (as prover) picks ρ, ρ0
, r, r0
, r00, r000 ← Zq, and picks opening open,
open0 ← Zq to commit to ρ and r respectively. He computes commitments
C = h
ρh˜open
, D = g
rh˜open0
and blinded values A = σh˜ρ
, G = gih˜r
, W = wh˜r
0
,
S = σih˜r
00 , and U = uih˜r
000 . The user sends C , D, A, G, W, S and U to the verifier
and engages the verifier in the following proof:
PK{(c, ρ, open, mult, tmp, m1, . . . , m`
0 , s, r, open0
, mult0
, tmp0
, r0
, r00, r000) :
C = h
ρh˜open ∧ 1 = C
ch
−multh˜−tmp ∧ (1)
e(h0 ·
Q`
j=`
0+1 h
mj
j
· G, h)
e(A, y)
= e(A, h)
c
· e(h, h ˜ )
r
(2)
· e(h, ˜ y)
−ρ
· e(h, h ˜ )
−mult
·
`
Y0
j=1
e(hj , h)
−mj
· e(h`+1, h)
−s∧
e(G, accV )
e(g, W)z
= e(h, ˜ accV )
r
e(1/g, h˜)
r
0
∧ (3)
D = g
rh˜open0
∧ 1 = Dc
g
−mult 0
h˜−tmp0
∧ (4)
e(pk · G, S)
e(g, g)
= e(pk · G, h˜)
r
00
e(h, ˜ h˜)
−mult 0
e(h, ˜ S)
r∧ (5)
e(G, u)
e(g, U)
= e(h, u ˜ )
r
e(1/g, h˜)
r
000} . (6)
This proof merges the proof of knowledge of Section 3.3 with a proof of knowledge
of an adapted signature as the ones described in Section 4.1. The latter is similar
to the proof of knowledge of a signature in Section 2.2. Special care needs to be
taken to bind the gi
in the accumulator to the gi value in the adapted signature.
Theorem 2. Under the n-HSDHE and the n-DHE assumptions, the protocol
above is a proof of knowledge of an adapted signature on (m1, . . . , m`, gi) such
that i ∈ V . The proof can be found in Section C.1.
5 Conclusion and Discussion
In this paper we have introduced a novel dynamic accumulator based on bilinear
maps and have shown how it can be used to achieve efficient revocation in
privacy-preserving systems such as group signatures or anonymous credential
systems.
Previous proposals require expensive computations for updating witnesses
and are not suitable for electronic token based systems with a large number of
users, as the ones that will soon appear with the introduction of e-ID’s, e-tickets
and alike. Our accumulator overcomes the aforementioned drawback introducing efficient witness updates. In the envisioned system, at the beginning of each
epoch, the users retrieve their currently valid witness from an updating authority (as the number of revocation per epoch is likely to be very large, the users
will typically not be able to handle them). As updating a witness in our scheme
requires only a number of multiplication linear in the number of changes to
the accumulator (in particular, linear in |(V \Vw) ∪ (Vw\V )|) a single authority
(which not necessarily needs to be the issuer) can keep the witness values for all
users easily up-to-date (and in main memory). This is a key feature that enables
the adoption of dynamic accumulators for revocation in privacy-preserving systems with large number of users as, e.g., in the case of electronic driving license
systems. Although not necessary, there could even be several witness update
entities, responsible for upgrading witnesses for groups of users. For example, in
a national e-ID’s systems, witness updates could be performed by per-county or
per-city witness update entity. The latter requires only public parameters and
are only responsible for correct computation of the witness updates for the users
in their group. Malicious behavior by one of the witness update entities, does
not break system security (recall that they only require public parameters) but
can only lead to denial of service. That is, if a witness is not correctly computed
(not reflecting the latest changes in the accumulator) it would prevent a user
to prove validity of her credential. In this case, users can report to the issuing
authority to obtain a valid witness update and signal the misbehaving of the
witness update entity.
6 Acknowledgements
During this work, we enjoyed many discussion with Thomas Gross and Tom
Heydt-Benjamin on various kinds of revocation of anonymous credentials. Thank
you! The research leading to these results has received funding from the European Community’s Seventh Framework Programme (FP7/2007-2013) under
grant agreement no 216483.
References
1. Camenisch, J., Van Herreweghen, E.: Design and implementation of the idemix
anonymous credential system. Technical Report Research Report RZ 3419, IBM
Research Division (May 2002)
2. Camenisch, J., Lysyanskaya, A.: Efficient non-transferable anonymous multi-show
credential system with optional anonymity revocation. Technical Report Research
Report RZ 3295, IBM Research Division (November 2000)
3. Persiano, G., Visconti, I.: An efficient and usable multi-show non-transferable
anonymous credential system. In Juels, A., ed.: Financial Cryptography. Volume
3110 of Lecture Notes in Computer Science., Springer (2004) 196–211
4. Chaum, D.: Security without identification: Transaction systems to make big
brother obsolete. Commun. ACM 28(10) (1985) 1030–1044
5. Chaum, D., Evertse, J.H.: A secure and privacy-protecting protocol for transmitting personal information between organizations. In Odlyzko, A.M., ed.: CRYPTO.
Volume 263 of Lecture Notes in Computer Science., Springer (1986) 118–167
6. Chen, L.: Access with pseudonyms. In Dawson, E., Golic, J.D., eds.: Cryptography: Policy and Algorithms. Volume 1029 of Lecture Notes in Computer Science.,
Springer (1995) 232–243
7. Lysyanskaya, A., Rivest, R.L., Sahai, A., Wolf, S.: Pseudonym systems. In Heys,
H.M., Adams, C.M., eds.: Selected Areas in Cryptography. Volume 1758 of Lecture
Notes in Computer Science., Springer (1999) 184–199
8. Okamoto, T.: An efficient divisible electronic cash scheme. In Coppersmith, D.,
ed.: CRYPTO. Volume 963 of Lecture Notes in Computer Science., Springer (1995)
438–451
9. Chan, A.H., Frankel, Y., Tsiounis, Y.: Easy come - easy go divisible cash. In:
EUROCRYPT. (1998) 561–575
10. Camenisch, J., Hohenberger, S., Lysyanskaya, A.: Compact e-cash. [38] 302–321
11. Brickell, E.F., Camenisch, J., Chen, L.: Direct anonymous attestation. [39] 132–145
12. Bangerter, E., Camenisch, J., Lysyanskaya, A.: A cryptographic framework for
the controlled release of certified data. In Christianson, B., Crispo, B., Malcolm,
J.A., Roe, M., eds.: Security Protocols Workshop. Volume 3957 of Lecture Notes
in Computer Science., Springer (2004) 20–42
13. Ateniese, G., Song, D.X., Tsudik, G.: Quasi-efficient revocation in group signatures. In Blaze, M., ed.: Financial Cryptography. Volume 2357 of Lecture Notes
in Computer Science., Springer (2002) 183–197
14. Boneh, D., Shacham, H.: Group signatures with verifier-local revocation. [39]
168–177
15. Nakanishi, T., Funabiki, N.: Verifier-local revocation group signature schemes with
backward unlinkability from bilinear maps. IEICE Transactions 90-A(1) (2007)
65–74
16. Boneh, D., Boyen, X., Shacham, H.: Short group signatures. [40] 41–55
17. Camenisch, J., Lysyanskaya, A.: Dynamic accumulators and application to efficient
revocation of anonymous credentials. In Yung, M., ed.: CRYPTO. Volume 2442 of
Lecture Notes in Computer Science., Springer (2002) 61–76
18. Benaloh, J.C., de Mare, M.: One-way accumulators: A decentralized alternative to
digital sinatures (extended abstract). In: EUROCRYPT. (1993) 274–285
19. of Motor Vehicles, W.V.D.: Wvdmv fy 2005 annual report. http://www.wvdot.
com/6_motorists/dmv/downloads/DMV-AnnualReport2005.pdf (2005)
20. Li, J., Li, N., Xue, R.: Universal accumulators with efficient nonmembership proofs.
In Katz, J., Yung, M., eds.: ACNS. Volume 4521 of Lecture Notes in Computer
Science., Springer (2007) 253–269
21. Nguyen, L.: Accumulators from bilinear pairings and applications. In Menezes,
A., ed.: CT-RSA. Volume 3376 of Lecture Notes in Computer Science., Springer
(2005) 275–292
22. Wang, P., Wang, H., Pieprzyk, J.: A new dynamic accumulator for batch updates.
In Qing, S., Imai, H., Wang, G., eds.: ICICS. Volume 4861 of Lecture Notes in
Computer Science., Springer (2007) 98–112
23. Boneh, D., Boyen, X., Goh, E.J.: Hierarchical identity based encryption with
constant size ciphertext. [38] 440–456
24. Boneh, D., Boyen, X.: Short signatures without random oracles. In Cachin, C.,
Camenisch, J., eds.: EUROCRYPT. Volume 3027 of Lecture Notes in Computer
Science., Springer (2004) 56–73
25. Boyen, X., Waters, B.: Full-domain subgroup hiding and constant-size group signatures. In Okamoto, T., Wang, X., eds.: Public Key Cryptography. Volume 4450
of Lecture Notes in Computer Science., Springer (2007) 1–15
26. Schnorr, C.P.: Efficient signature generation by smart cards. J. Cryptology 4(3)
(1991) 161–174
27. Chaum, D., Pedersen, T.P.: Wallet databases with observers. In Brickell, E.F.,
ed.: CRYPTO. Volume 740 of Lecture Notes in Computer Science., Springer (1992)
89–105
28. Camenisch, J., Michels, M.: Proving in zero-knowledge that a number is the product of two safe primes. In: EUROCRYPT. (1999) 107–122
29. Camenisch, J.L.: Group Signature Schemes and Payment Systems Based on the
Discrete Logarithm Problem. PhD thesis, ETH Z¨urich (1998) Diss. ETH No. 12520,
Hartung Gorre Verlag, Konstanz.
30. Brands, S.: Rapid demonstration of linear relations connected by boolean operators. In: EUROCRYPT. (1997) 318–333
31. Cramer, R., Damg˚ard, I., Schoenmakers, B.: Proofs of partial knowledge and
simplified design of witness hiding protocols. In Desmedt, Y., ed.: CRYPTO.
Volume 839 of Lecture Notes in Computer Science., Springer (1994) 174–187
32. Camenisch, J., Stadler, M.: Proof systems for general statements about discrete
logarithms. Technical Report TR 260, Institute for Theoretical Computer Science,
ETH Z¨urich (March 1997)
33. Camenisch, J., Lysyanskaya, A.: Signature schemes and anonymous credentials
from bilinear maps. [40] 56–72
34. Okamoto, T.: Efficient blind and partially blind signatures without random oracles.
In Halevi, S., Rabin, T., eds.: TCC. Volume 3876 of Lecture Notes in Computer
Science., Springer (2006) 80–99
35. Au, M.H., Susilo, W., Mu, Y.: Constant-size dynamic -taa. In Prisco, R.D., Yung,
M., eds.: SCN. Volume 4116 of Lecture Notes in Computer Science., Springer (2006)
111–125
36. Boneh, D., Gentry, C., Waters, B.: Collusion resistant broadcast encryption with
short ciphertexts and private keys. In Shoup, V., ed.: CRYPTO. Volume 3621 of
Lecture Notes in Computer Science., Springer (2005) 258–275
37. Belenkiy, M., Chase, M., Kohlweiss, M., Lysyanskaya, A.: P-signatures and noninteractive anonymous credentials. In Canetti, R., ed.: TCC. Volume 4948 of Lecture
Notes in Computer Science., Springer (2008) 356–374
38. Cramer, R., ed.: Advances in Cryptology - EUROCRYPT 2005, 24th Annual International Conference on the Theory and Applications of Cryptographic Techniques,
Aarhus, Denmark, May 22-26, 2005, Proceedings. In Cramer, R., ed.: EUROCRYPT. Volume 3494 of Lecture Notes in Computer Science., Springer (2005)
39. Atluri, V., Pfitzmann, B., McDaniel, P.D., eds.: Proceedings of the 11th ACM
Conference on Computer and Communications Security, CCS 2004, Washingtion,
DC, USA, October 25-29, 2004. In Atluri, V., Pfitzmann, B., McDaniel, P.D., eds.:
ACM Conference on Computer and Communications Security, ACM (2004)
40. Franklin, M.K., ed.: Advances in Cryptology - CRYPTO 2004, 24th Annual International CryptologyConference, Santa Barbara, California, USA, August 15-19,
2004, Proceedings. In Franklin, M.K., ed.: CRYPTO. Volume 3152 of Lecture
Notes in Computer Science., Springer (2004)
A Generic Group Security of n-HSDHE
We provide more confidence in the n-HSDHE assumption by proving lower
bounds on the complexity of a harder problem in the generic group model.
Definition 4 (n-HSDHE). Given g, gx
, u ∈ G, {g
1/(x+γ
i
)
, gγ
i
, uγ
i
}i=1...n, and
{g
γ
i
}i=n+2...2n, it is infeasible to compute a new tuple (g
1/(x+c)
, gc
, uc
).
To simplify our analysis we prove the generic group security of the weaker
assumption in which the adversary is given the value γ instead of the hiding
values {g
γ
i
, uγ
i
}i=1...n and {g
γ
i
}i=n+2...2n.
Definition 5 (weakened n-HSDHE). Given g, gx
, u ∈ G, {g
1/(x+γ
i
)}i=1...n
and γ it is infeasible to compute a new tuple (g
1/(x+c)
, gc
, uc
).
Any attack algorithm that can break the n-HSDHE assumption can be used
to break the weakened n-HSDHE. All the reduction does is compute the values {g
γ
i
, uγ
i
}i=1...n and {g
γ
i
}i=n+2...2n from γ and hand the now completed
n-HSDHE problem instance to the n-HSDHE attack algorithm. Consequently
it is sufficient to prove that the weakened n-HSDHE assumption holds in the
generic group model.
Let e : G × G → GT be a bilinear map over groups G and GT of prime
order q. In the generic group model, we encode group elements of G and GT
as unique random strings. Group operations are performed by an oracle, that
operates on these strings and keeps an internal representation of the group. We
set α : Zq → {0, 1}
∗
to be the opaque encoding of elements of G. Let g be a
generator of G; α(a) maps a ∈ Zq to the string representation of g
a ∈ G. The
function τ : Zq → {0, 1}
∗ maps a ∈ Zq to e(g, g)
a ∈ GT .
We can represent all operations in terms of the random maps α, τ . Note
that the maps do not need to be explicitly given. It is sufficient if the oracles
create them using lazy evaluation. Let a, b ∈ Zq. We define oracle queries for the
following operations:
Group Operation. α(a)·α(b) = α(a+b). This is because α(a)·α(b) = g
a
·g
b =
g
a+b = α(a + b). The same holds for the group operation in τ .
Exponentiation by a Constant. α(b)
a = α(ab). This is because α(b)
a =
(g
b
)
a = g
ab = α(ab). The same holds for multiplication by a constant in
τ .
Pairing. e(α(a), α(b)) = τ (ab). This is because e(α(a), α(b)) = e(g
a
, gb
) =
e(g, g)
ab = τ (ab).
When an adversary tries to break the n-HSDHE assumption to the generic
group model, the adversary does not get g, gx
, u ∈ G, {g
1/(x+γ
i
)
, gγ
i
, uγ
i
}i=1...n,
and {g
γ
i
}i=n+2...2n Instead, we encode these values using the random maps α
and τ . The generators g and e(g, g) become α(1) and τ (1) respectively.
We encode g
x as α(x), g
1/x+γ
i
as α(1/(x + γ
i
)) and g
x as α(x). Since g is
a generator of G1, there exists a y ∈ Zq such that g
y = u. So we choose y at
random and set u = α(y).
To break the n-HSDHE assumption, the adversary needs to output a triple
(A, B, C) of the form (g
1/x+c
, gc
, uc
) for some c ∈ Zq. Normally, we can test that
the triple is well-formed using the bilinear map: e(A, gxB) = e(g, g) ∧ e(C, g) =
e(u, B).
In the generic group model we require that he outputs random representations (αA, αB, αC ). The adversary can either compute these values using the
group oracles, or pick them at random, which he could also do by doing a generic
exponentiation with a random constant. Thus it is meaningful to say that the
adversary succeeds if αA = α(1/(x + c)) ∧ αB = α(c) ∧ αC = α(yc) for some c.
Theorem 3 (Weakened n-HSDHE is Hard in the Generic Group Model).
Let G and GT be groups of prime order q, (where q is a k-bit prime) with bilinear map e : G × G → GT . We choose maps α and τ at random. There exists a
negligible function ν : N → [0, 1] such that for every PPTM A:
Pr[x, y, γ ← Zq; (αA, αB, αC ) ← A(α(1), α(x), α(y), {α(1/(x+γ
i
))}i=1...n, γ) :
∃c : αA = α(1/x + c) ∧ αB = α(c) ∧ αC = α(yc)] ≤ ν(k)
Proof. Let A be a PPTM that can break the n-HSDHE assumption. We create
an environment R that interacts with A as follows:
R maintains two lists: Lα = {(Fα,s, αs) : s = 0, . . . , Sα − 1} and Lτ =
{(Fτ,s, τs) : s = 0, . . . , Sτ − 1}. The Fα,s and Fτ,s contain rational functions;
their numerators and denominators are polynomials in Zq[x, y]. R uses Fα,s
and Fτ,s to store the group action queries that A makes and αs, τs to store the
results. Thus αs = α(Fα,s) and τs = τ (Fτ,s).
R chooses random strings α0, α1, α2, {α2+i}i=1...4n−1, τ0 ∈ {0, 1}
∗
, and sets
the corresponding polynomials as:
Fα,0 = 1 Fα,1 = x Fα,2 = y
Fα,2+i = 1/(x + γ
i
) (1 ≤ i ≤ n) Fτ,0 = 1
R sets Sα = n + 3 and Sτ = 1. Then R sends the strings to A . Whenever A
calls the group action oracle, R updates its lists.
Multiplication. A inputs αs and αt. R checks that αs and αt are in its list
Lα, and returns ⊥ if they are not. Then R computes F = Fα,s + Fα,t. If F
is already in the list Lα, then R returns the appropriate αv. Otherwise, R
chooses a random αSα
, sets Fα,Sα = F and adds this new tuple to the list.
R increments the counter Sα by 1. R performs a similar operation if the
inputs are in GT .
Division. A inputs αs and αt. R checks that αs and αt are in its list Lα,
and returns ⊥ if they are not. Then R computes F = Fα,s − Fα,t. If F is
already in the list Lα, then R returns the appropriate αv. Otherwise, R
chooses a random αSα
, sets Fα,Sα = F and adds this new tuple to the list.
R increments the counter Sα by 1. R performs a similar operation if the
inputs are in GT .
Exponentiation by a Constant A inputs αs and a constant a ∈ Zq. R checks
that αs is in its list Lα, and returns ⊥ if it is not. Then R computes F =
Fα,s · a. If F is already in the list Lα, then R returns the appropriate αv.
Otherwise, R chooses a random αSα
, sets Fα,Sα = F and adds this new
tuple to the list. R increments the counter Sα by 1. R performs a similar
operation if the inputs are in G2 or GT .
Pairing. A inputs αs and αt. R checks that αs and αt are in its list Lα and
returns ⊥ if they are not. Then R computes F = Fα,s · Fα,t. If F is already
in the list Lτ , then R returns the appropriate τv. Otherwise, R chooses a
random τSτ
, sets Fτ,Sτ = F and adds this new tuple to the list. R increments
the counter Sτ by 1.
At the end of the game, A outputs (αA, αB, αC ). These values must correspond to bivariate polynomials Fα,A, Fα,B and Fα,C in our lists. (If one of these
values is not in our lists, then A must have guessed a random group element;
he might as well have asked the oracle to perform exponentiation on a random
constant and added a random value to the list. Thus we ignore this case.)
Since A must have computed these polynomials as a result of oracle queries,
they must be of the form a0 + a1x + a2y +
Pn
i=1 a3,i/(x + γ
i
). If A is to be
successful,
Fα,A(x + Fα,B) = 1 and (7)
Fα,C = yFα,B. (8)
For Equation (8) to hold identically in Zq[x, y], Fα,C and Fα,B either need
to be 0 or Fβ,B needs to be constant, because the only possible term for y in
the polynomials is y(a2 +
Pn
i=1 a5,iγ
i
). In both cases, the term (x + Fβ,B) in
Equation (8) has maximum degree 1, and Equation (7) can only be satisfied
identically in Zq[x, y] if Fα,A has at least degree q − 1 in variable x. We know
that the degree of Fα,A is at most n and conclude that there exists an assignment
in Zq to the variables x and y for which the Equations (7) and (8) do not hold.
Since Equation (7) is a non-trivial polynomial equation of degree ≤ 2n, it admits
at most 2n roots in Zq.
Analysis of R’s Simulation. At this point R chooses a random x
∗
, y∗ ∈ Z
∗
q
, and
now sets x = x
∗ and y = y
∗
. Using Equations (9), (10), and (11) R now tests if
its simulation was perfect; that is, if the instantiation of x by x
∗ or y by y
∗ does
not create any equality relation among the polynomials that was not revealed
by the random strings provided to A. Thus, A’s overall success is bounded by
the probability that any of the following holds:
Fα,i(x
∗
, y∗
) − Fα,j (x
∗
, y∗
) = 0 in Zq, for some i, j and Fα,i 6= Fα,j , (9)
Fτ,i(x
∗
, y∗
) − Fτ,j (x
∗
, y∗
) = 0 in Zq, for some i, j and Fτ,i 6= Fτ,j ,
(10)
Fα,A(x
∗
, y∗
)(x
∗ + Fβ,B(x
∗
, y∗
)) = 1 ∧
Fα,C (x
∗
, y∗
) = y
∗Fβ,B(x
∗
, y∗
) in Zq. (11)
Each polynomial Fα,i and Fτ,i has degree at most n or 2n, respectively.
For fixed i and j, we satisfy Equations (9) with probability ≤ n/(q − 1) and
Equations (10) with probability ≤ 2n/(q − 1). We can bound the probability
that Equation (11) holds by ≤ 2n/(q − 1).
Now summing over all (i, j) pairs in each case, we bound A’s overall success
probability
 ≤ 2

Sα
2

n
q − 1
+

Sτ
2

2n
q − 1
+
2n
q − 1
≤
2n
q − 1
(

Sα
2

+

Sτ
2

+ 1).
Let nG be the total number of group oracle queries made, then we know that
Sα +Sτ = nG +n+ 6. We obtain that  ≤ (nG +n+ 6)2 2n
q−1 = O(n
2
Gn/q +n
3/q).

The following corollary restates the above result:
Theorem 4. Any adversary that breaks the weakened n-HSDHE assumption
with constant probability  > 0 in generic bilinear groups of order q such that
n < O(
√3 q) requires Ω(
p3
q/n) generic group operations.
B Protocol To Prove Knowledge of a Signature
Here we explain the protocol
PK{(c, s, r, open, mult, tmp, m1, . . . , m`) :
B = h
rh˜open ∧ 1 = B
ch
−multh˜−tmp ∧
e(h0, h)
e(A, y)
= e(A, h)
c
· e(h, ˜ y)
−r
· e(h, h ˜ )
−mult
·(
Y
`
i=1
e(hi
, h)
−mi
)· e(h`+1, h)
−s
} .
as given in Section 2.2.
The first statement proves the prover’s knowledge of values r, open for opening the Pedersen commitment B = h
rh˜open. Assuming that computing logh h˜ is
hard (this is implied by n-SDH), the next statements assert the prover’s knowledge of values c, mult, and tmp such that mult = rc and tmp = open · c.
Let us consider the last line of the proof. It asserts the prover’s knowledge of
values c, s, m1, . . . , m` such that
e(A, y) · e(A, h)
c
· e(u, y)
−r
· e(h, h ˜ )
−cr = e(h0, h)
Y
`
i=1
e(hi
, h)
mi
· e(h`+1, h)
s
holds. Here we have made use of the relation mult = rc. By simplifying this
equation we obtain
e(Ah˜−r
, yh
c
) = e(h0
Y
`
i=1
h
mi
i
· h
s
`+1, h).
Clearly (Ah˜−r
, c, s) fulfills the verification equation of the signature scheme for
messages m1, . . . , m`.
Also note that the values A and B are random group elements.
C Proofs
C.1 Proof of Theorem 1
It is standard to show that from a convincing prover of the protocol
PK{(r, r0
, r00, r000
, open, mult, tmp) :
D = g
rh˜open ∧ 1 = Dr
00
g
−multh˜−tmp ∧ (12)
e(pk · G, S)
e(g, g)
= e(pk · G, h˜)
r
00
e(h, ˜ h˜)
−mult e(h, ˜ S)
r ∧ (13)
e(G, accV )
e(g, W)z
= e(h, ˜ accV )
r
e(1/g, h˜)
r
0
∧ (14)
e(G, u)
e(g, U)
= e(h, u ˜ )
r
e(1/g, h˜)
r
000} . (15)
one can with overwhelming probability extract values r, r
0
, r
00
, r
000, and mult
such that the Equations (14), (13), (15) hold. From Equation (14) we learn
through simple transformation that e(Gh˜−r
,accV )
e(g,Wh˜−r0
)
= z. We distinguish three cases:
In the first case Gh˜−r
corresponds to a gi
in stateU and i ∈ V . In this case the
extraction was successful.
In the second case Gh˜−r
corresponds to a gi
in stateU but i ∈/ V . In this case
we can use a successful prover to break the n-DHE assumption. The reduction obtains as input the parameters of a bilinear map paramsBM = (q, G, GT , e, g), and
an instance of the n-DHE assumption (g1, g2, . . . , gn, gn+2, . . . , g2n) ∈ G2n−1
.
It provides the prover with pkA = (paramsBM , pk, z = e(g1, gn)), accV and
stateU = (U, g1, . . . , gn, gn+2, . . . , g2n). The reduction computes the additional
setup for the proof using a fresh sk. Given a successful prover it can extract r,
r
0
, r
00
, r
000, and mult such that
e(Gh˜−r
, accV ) = e(g, Wh˜−r
0
)z
and
e(g, Y
j∈V
gn+1−j+i) = e(g, Wh˜−r
0
gn+1) .
This means that
gn+1 =
Q
j∈V gn+1−j+i
Wh˜−r
0
.
For i ∈ {1, . . . , n} \ V , all gn+1−j+i are contained in stateU and it is possible to
compute this value. This breaks the n-DHE assumption (Consult also the proof
in Section 3.2).
In the third case Gh˜−r does not correspond to a gi
in stateU . We will
show that we can use such a prover to break the dedicated signature scheme
(more concretely the n-HSDHE assumption) using the remaining Equations
(13) and (6). The reduction works as follows. On input a HSDHE instance
(g, gx
, u, {g
1/(x+γ
i
)
, gγ
i
, uγ
i
}i=1...n, {g
γ
i
}i=n+2...2n), the reduction uses the g
γ
i
to build stateU and the remaining values to construct the additional setup for
the proof by setting pk = g
x
(and implicitly sk = x).
After extracting r, r
0
, r
00
, r
000
, open, mult and tmp note that (based on Equation (12)) mult = rr00 and tmp = openr
00 (or one can compute logg h which
would in turn allow us to break n-HSDHE). After obtaining a Gh˜−r
that does
not correspond to a value in {g
γ
i
}i=1...n it is easy to see from Equation (13)
that e(pkGh˜−r
, Sh˜−r
00 ) = 1. Let c = logg Gh˜−r
. then Sh˜−r
00
= g
1/(x+c)
. Similarly from Equation (15) we learn that e(Gh˜−r
,u)
e(g,Uh˜−r000 )
= 1. If Gh˜−r = g
c
, then
Uh˜−r
000
= u
c
. This contradicts the n-HSDHE assumption.
As the malicious prover has no way to distinguish between the first or the
second reduction (as well as the real setup), we can randomly pick one of the
two reductions to break either the n-DHE or the n-HSDHE assumption (we only
loose a factor of 1/2 in the tightness of the reduction).
C.2 Proof of Theorem 2
We extract the value from the above proof. From Equation (1) we know that if
mult 6= ρc or mult0
6= rr00, we can compute the discrete logarithm logg h. This
contradicts the DL assumption.
From Equations (3,4,5,6) and the security of the accumulator proof protocol
in Section 3.3 we know that Gh˜−r
equals a gi
, i ∈ V , such that Wh˜−r
0
is a
verifying accumulator witness for this value. Otherwise we break the n-DHE or
the n-HSDHE assumption. (The reductions would be set up in the same way as
in Appendix C.1.)
Now we consider Equation (2) of the proof. It asserts the prover’s knowledge
of values m1, . . . , m0
`
such that
e(h0·
`
Y0
j=q
h
mj
j
·
Y
`
j=`
0+1
h
mj
j
·G, g)e(h, ˜ y)
ρ
·e(h, g ˜ )
ρc
·e(h`+1, g)
s = e(A, y)e(A, g)
c
·e(h, g ˜ )
r
.
Here we have made use of the relation mult = ρc. By simplifying this equation
further we obtain
e(h0 ·
`
Y0
j=q
h
mj
j
·
Y
`
j=`
0+1
h
mj
j
· h`+1
s
· G/h˜r
, g) = e(A/h˜ρ
, yg
c
) .
This shows that (A/h˜ρ
, c, s) is a valid adapted signature for (m1, . . . , m`, g˜
γ
i
).

D Computing Accumulator Parameters on the Fly
The accumulator presented in Section 3 allows for efficient witness update but it
has public parameters of considerable size. The maximum number of values that
might be accumulated must be estimated when the accumulator is instantiated
and envisioned usage scenarios would require a number of elements in the order
of 230. We believe that modern computers can easily handle such a large number
of elements
However, in this section we show how in the special case where at a given
point in time we never accumulate values greater than ˜n ≤ n we can bring the
cost of storing public parameters down to O(˜n). We achieve this through lazy
evaluation of the accumulator parameters.
Let ˜n be the greatest accumulated value. For a value i ∈ V we need (1) the
value gn+1−i to add i to the accumulator, (2) the value gi to verify membership in
the accumulator, and (3) values gn+1−j+i for j ∈ (V \Vw)∪(Vw \V ) to compute
and update witnesses. Note that for all j ∈ V ∪ Vw, j is smaller than ˜n. We
claim that the values {gi
, gn+1−i
, gn+i}
n˜
i=1} \ {gn+1} are sufficient for all of these
operations. Obviously the first two requirements are met. Moreover, if j > i,
then gn+1−j+i ∈ {gn+1−i}
n˜
i=1. Otherwise, 1 ≤ j < i and gn+1−j+i ∈ {gn+i}
n˜
i=1.
This establishes the third requirement.
In the ObtainCert protocol our revocation scheme ˜n is increased by one
and the missing accumulator parameters are added to stateU . Note that U =
{1, . . . , n˜}. The updated state is then used to update the epoch information
epochV and the witnesses wit i
.
