Batching Techniques for Accumulators
with Applications to IOPs and
Stateless Blockchains

Dan Boneh, Benedikt B¨unz, Ben Fisch
Stanford University

Abstract

We present batching techniques for cryptographic accumulators and vector commitments in groups of unknown order. Our techniques are tailored
for distributed settings where no trusted accumulator manager exists and updates to the accumulator are processed in batches. We develop techniques for
non-interactively aggregating membership proofs that can be verified with a
constant number of group operations. We also provide a constant sized batch
non-membership proof for a large number of elements. These proofs can be
used to build the first positional vector commitment (VC) with constant sized
openings and constant sized public parameters. As a core building block for
our batching techniques we develop several succinct proof systems in groups
of unknown order. These extend a recent construction of a succinct proof of
correct exponentiation, and include a succinct proof of knowledge of an integer
discrete logarithm between two group elements. We circumvent an impossibility
result for Sigma-protocols in these groups by using a short trapdoor-free CRS.
We use these new accumulator and vector commitment constructions to design
a stateless blockchain, where nodes only need a constant amount of storage in
order to participate in consensus. Further, we show how to use these techniques
to reduce the size of IOP instantiations, such as STARKs.

1 Introduction

A cryptographic accumulator [Bd94] is a primitive that produces a short binding
commitment to a set of elements together with short membership and/or nonmembership proofs for any element in the set. These proofs can be publicly verified
against the commitment. The simplest accumulator is the Merkle tree [Mer88], but
several other accumulators are known, as discussed below. An accumulator is said to
be dynamic if the commitment and membership proofs can be updated efficiently as
elements are added or removed from the set, at unit cost independent of the number
of accumulated elements. Otherwise we say that the accumulator is static. A universal accumulator is dynamic and supports both membership and non-membership
proofs.

A vector commitment (VC) is a closely related primitive [CF13]. It provides the
same functionality as an accumulator, but for an ordered list of elements. A VC is a
position binding commitment and can be opened at any position to a unique value
with a short proof (sublinear in the length of the vector). The Merkle tree is a VC
with logarithmic size openings. Subvector commitments [LM18] are VCs where a
subset of the vector positions can be opened in a single short proof (sublinear in the
size of the subset).

The typical way in which an accumulator or VC is used is as a communicationefficient authenticated data structure (ADS) for a remotely stored database where
users can retrieve individual items along with their membership proofs in the data
structure. Accumulators have been used for many applications within this realm,
including accountable certificate management [BLL00, NN98], timestamping [Bd94],
group signatures and anonymous credentials [CL02], computations on authenticated
data [ABC+12], anonymous e-cash [STS99b, MGGR13a], privacy-preserving data
outsourcing [Sla12], updatable signatures [PS14, CJ10], and decentralized bulletin
boards [FVY14, GGM14].

Our present work is motivated by two particular applications of accumulators
and vector commitments: stateless transaction validation in blockchains, or “stateless blockchains” and short interactive oracle proofs (IOPs) [BCS16].

“Stateless” blockchains. A blockchain has become the popular term for a
ledger-based payment system, in which peer-to-peer payment transactions are asynchronously broadcasted and recorded in an ordered ledger that is replicated across
nodes in the network. Bitcoin and Ethereum are two famous examples. Verifying the validity of a transaction requires querying the ledger state. The state can
be computed uniquely from the ordered log of transactions, but provides a more
compact index to the information required for transaction validation.

For example, in Ethereum the state is a key/value store of account balances
where account keys are the public key addresses of users. In Bitcoin, the state
is the set of unspent transaction outputs (UTXOs). In Bitcoin, every transaction
completely transfers all the funds associated with a set of source addresses to a set
of target addresses. It is only valid if every source address is the output of a previous
transaction that has not yet been consumed (i.e. “spent”). It is important that all
nodes agree on the ledger state.

Currently, in Bitcoin, every node in the system stores the entire UTXO set in
order to verify incoming transactions. This has become cumbersome as the size of
UTXO set has grown to gigabytes. An accumulator commitment to the UTXO set
would alleviate this need. Transactions would include membership proofs for all its
inputs. A node would only need to store the current state of the accumulator and
verify transactions by checking membership proofs against the UTXO accumulator
state. In fact, with dynamic accumulators, no single node in the network would
be required to maintain the entire UTXO set. Only the individual nodes who are
interested in a set of UTXOs (e.g. the users who can spend these outputs) would
need to store them along with their membership proofs. Every node can efficiently
update the UTXO set commitment and membership proofs for individual UTXOs
with every new batch of transactions. The same idea can be applied to the Ethereum
key-value store using a VC instead of an accumulator.

This design concept is referred to as a “stateless blockchain” [Tod16] because
nodes may participate in transaction validation without storing the entire state of
the ledger, but rather only a short commitment to the state. The idea of committing
to a ledgers state was introduced long before Bitcoin by Sanders and Ta-Shma for ECash[STS99a]. While the stateless blockchain design reduces the storage burden of
node performing transaction validation, it increases the network communication due
to the addition of membership proofs to each transaction payload. A design goal is to
minimize the communication impact. Therefore, stateless blockchains would benefit
from an accumulator with smaller membership proofs, or the ability to aggregate
many membership proofs for a batch of transactions into a single constant-size proof.

Interactive oracle proofs (IOPs). Micali [Mic94] showed how probabilistically
checkable proofs (PCPs) can be used to construct succinct non-interactive arguments. In this construction the prover commits to a long PCP using a Merkle tree
and then uses a random oracle to generate a few random query positions. The
prover then verifiably opens the proof at the queried positions by providing Merkle
inclusion paths.

This technique has been generalized to the broader class of interactive oracle
proofs (IOPs)[BCS16]. In an IOP the prover sends multiple proof oracles to a verifier. The verifier uses these oracles to query a small subsets of the proof, and
afterwards accepts or rejects the proof. If the proof oracle is instantiated with a
Merkle tree commitment and the verifier is public coin, then an IOP can be compiled
into a non-interactive proof secure in the random oracle model [BCS16]. In particular, this compiler is used to build short non-interactive (zero-knowledge) proof of
knowledge with a quasilinear prover and polylogarithmic verifier. Recent practical instantiations of proof systems from IOPs include Ligero [AHIV17], STARKs
[BBHR18], and Aurora [BSCR+18].

IOPs use Merkle trees as a vector commitment. Merkle trees have two significant
drawbacks for this application: first, position openings are not constant size, and
second, the openings of several positions cannot be compressed into a single constant
size proof (i.e. it is not a subvector commitment). A vector commitment with these
properties would have dramatic benefits for reducing the communication of an IOP
(or size of the non-interactive proof compiled from an IOP).

1.1 Summary of contributions

Our technical contributions consist of a set of batching and aggregation techniques
for accumulators. The results of these techniques have a wide range of implications, from concrete practical improvements in the proof-size of IOP-based succinct
arguments (e.g. STARKS) and minimizing the network communication blowup of
stateless blockchains to theoretical achievements in VCs and IOPs.

To summarize the theoretical achievements first, we show that it is possible
to construct a VC with constant size subvector openings and constant size public
parameters. Previously, it was only known how to construct a VC with constant
size subvector openings and public parameters linear in the length of the vector.
This has immediate implications for IOP compilers. The Merkle-tree IOP compiler
outputs a non-interactive proof that is O(λq log n) larger (additive blowup) than
the original IOP communication, where q is the number of oracle queries, n is
the maximum length1 of the IOP proof oracles, and λ is the Merkle tree security
parameter. When replacing the Merkle-tree in the IOP compiler with our new VC,
we achieve only O(rλ) blowup in proof size, independent of q and n, but dependent
on the number of IOP rounds r. In the special case of a PCP there is a single round
(i.e. r = 1). A similar result was recently demonstrated [LM18] using the vector
commitments of Catalano and Fiore (CF) [CF13], but the construction requires the
verifier to access public parameters linear in n. It was not previously known how to
achieve this with constant size public parameters.

Lai and Malavolta apply the CF vector commitments to “CS-proofs”, a special
case of a compiled IOP where the IOP is a single round PCP. Instantiated with
theoretical PCPs [Kil92, Mic94], this results in the shortest known setup-free noninteractive arguments (for NP) with random oracles consisting of just 2 elements in
a hidden order group and 240 additional bits of the PCP proof for 80-bit statistical
security. Instantiating the group with class groups and targeting 100-bit security
yields a proof of ≈ 540 bytes. However, the verifier must either use linear storage or
perform linear work for each proof verification to generate the public proof parameters. In similar vein, we can use our new VCs to build the same non-interactive
argument system, but with sublinear size parameters (in fact constant size). Under
the same parameters our proofs are slightly larger, consisting of 5 group elements,
a 128-bit integer, and the 240 bits of the PCP proof (≈ 1.3KB).

Our VCs also make concrete improvements to practical IOPs. Targeting 100-bit
security with class groups, replacing Merkle trees with our VCs would incur only 1
KB per round of the IOP. In Aurora [BSCR+18], it was reported that Merkle proofs
take up 154 KB of the 222 KB proof for a circuit of size 220. Our VCs would reduce
the size of the proof to less than 100 KB, a 54% reduction. For STARKs, a recent
benchmark indicates that the Merkle paths make up over 400 KB of the 600 KB
proof for a circuit of 252 gates [BBHR18]. With our VCs, under the same parameters
the membership proofs would take up roughly 22 KB, reducing the overall proof size
to approximately 222 KB, nearly a 63% reduction.

Furthermore, replacing Merkle trees with our new VCs maintains good performance for proof verification. Roughly, each Merkle path verification of a k-bit block
is substituted with k modular multiplications of λ-bit integers. The performance
comparison is thus log n hashes vs k multiplications, which is even an improvement for k < log n. In the benchmarked STARK example, Merkle path verification
comprises roughly 80% of the verification time.

1

In each round of an IOP, the prover prepares a message and sends the verifier a “proof oracle”,
which gives the verifier random read access to the prover’s message. The “length” of the proof
oracle is the length of this message.

1.2 Overview of techniques

Batching and aggregation. We use the term batching to describe a single action
applied to n items instead of one action per item. For example a verifier can batch
verify n proofs faster than n times verifying a single membership proof. Aggregation
is a batching technique that is used when non-interactively combining n items to a
single item. For example, a prover can aggregate n membership proofs to a single
constant size proof.

Succinct proofs for hidden order groups. Wesolowski [Wes18] recently introduced a constant sized and efficient to verify proof that a triple (u, w, t) satisfies
w = u
2
t
, where u and w are elements in a group G of unknown order. The proof extends to exponents that are not a power of two and still provides significant efficiency
gains over direct verification by computation.

We expand on this technique to provide a new proof of knowledge of an exponent,
which we call a PoKE proof. It is a proof that a computationally bounded prover
knows the discrete logarithm between two elements in a group of unknown order.
The proof is succinct in that the proof size and verification time is independent of the
size of the discrete-log and has good soundness. We also generalize the technique to
pre-images of homomorphisms from Z
q
to G of unknown order. We prove security in
the generic group model, where an adversarial prover operates over a generic group.
Nevertheless, our extractor is classical and does not get to see the adversary’s queries
to the generic group oracles. We also rely on a short unstructured common reference
string (CRS). Using the generic group model for extraction and relying on a CRS is
necessary to bypass certain impossibility results for proofs of knowledge in groups
of unknown order [BCK10, TW12].

We also extend the protocol to obtain a (honest verifier zero-knowledge) ΣProtocol of DLOG in G. This protocol is the first succinct Σ-protocol of this kind.

Distributed accumulator with batching. Next, we extend current RSAbased accumulators [CL02, LLX07] to create a universal accumulator for a distributed/decentralized setting where no single trusted accumulator manager exists
and where updates are processed in batches. Despite this we show how membership
and non-membership proofs can be efficiently aggregated. Moreover, items can efficiently be removed from the accumulator without a trapdoor or even knowledge of
the accumulated set. Since the trapdoor is not required for our construction we can
extend Lipmaa’s [Lip12] work on accumulators in groups of unknown order without
a trusted setup by adding dynamic additions and deletions to the accumulator’s
functionality. Class groups of imaginary quadratic order are a candidate group of
unknown order without a trusted setup[BH01].

Batching non-membership proofs. We next show how our techniques can
be amplified to create a succinct and efficiently verifiable batch membership and
batch non-membership proofs. We then use these batch proofs to create the
first vector commitment construction with constant sized batch openings (recently
called subvector commitments [LM18]) and O(1) setup. This improves on previous
work [CF13, LRY16] which required superlinear setup time and linear public parameter size. It also improves on Merkle tree constructions which have logarithmic
sized non-batchable openings. The efficient setup also allows us to create sparse
vector commitments which can be used as a key-value map commitment.

Soundness lower bounds in hidden order groups. Certain families of sigma
protocols for a relation in a generic group of unknown order can achieve at most
soundness 1/2 per challenge [BCK10, TW12]. Yet, our work gives sigma protocols
in a generic group of unknown order that have negligible soundness error. This
does not contradict the known impossibility result because our protocols involve a
CRS, whereas the family of sigma protocols to which the 1/2 soundness lower bound
applies do not have a CRS. Our results are significant as we show that it suffices
to have a CRS containing two fresh random generic group generators to circumvent
the soundness lower bound.

Note that we only prove how to extract a witness from a successful prover that is
restricted to the generic group model. Proving extraction from an arbitrary prover
under a falsifiable assumption is preferable and remains an open problem.

1.3 Additional related work

Dynamic accumulators can be constructed from the strong RSA assumption in
groups of unknown order (such as an RSA group or the class group) [BP97,
CL02, LLX07, Lip12], from bilinear maps [DT08, CKS09, Ngu05], and from
Merkle hash trees [Mer88, CHKO08]. These accumulators (with the exception of Merkle trees) naturally support batching of membership proofs, but not
batching of non-membership proofs. Vector commitments based on similar techniques [LY10, CF13, LRY16] have constant size openings, but large setup parameters.

Accumulators traditionally utilize a trusted accumulator manager which possesses a trapdoor to efficiently delete elements from the accumulator. This trapdoor
also allows the manager to create membership witnesses for arbitrary elements. Lipmaa [Lip12] was the first to construct a static accumulator without a trusted setup
from hidden order groups.

In concurrent work, Chepurnoy et. al. [CPZ18] also note that accumulators and
vector commitments can be used to build stateless blockchains. The work proposes
a new homomorphic vector commitment based on bilinear maps and multivariate
polynomials. This is applied to a blockchain design where each account stores a
balance, and balances can be updated homomorphically knowing only the vector
commitment to the current blockchain balances. However, the construction requires
linear public parameters, does not have a trustless setup, and does not support
batching of inclusion proofs. The linear public parameter imply that a bound on
the total number of accounts needs to be known at setup time.

2 Preliminaries

Notation.

• a k b is the concatenation of two lists a, b

• a is a vector of elements and ai
is the ith component

• [`] denotes the set of integers {0, 1, . . . , ` − 1}.

• negl(λ) is a negligible function of the security parameter λ

• Primes(λ) is the set of integer primes less than 2λ

• x
$
← S denotes sampling a uniformly random element x ∈ S.
x
$← A(·) denotes the random variable that is the output of a randomized
algorithm A.

• GGen(λ) is a randomized algorithm that generates a group of unknown order
in a range [a, b] such that a, b, and a − b are all integers exponential in λ.

2.1 Assumptions

The adaptive root assumption, introduced in [Wes18], is as follows.

Definition 1. We say that the adaptive root assumption holds for GGen if
there is no efficient adversary (A0, A1) that succeeds in the following task. First, A0
outputs an element w ∈ G and some state. Then, a random prime ` in Primes(λ) is
chosen and A1(`,state) outputs w
1/` ∈ G. More precisely, for all efficient (A0, A1):
AdvAR
(A0,A1)
(λ) := Pr






u
` = w 6= 1 :
G
$
← GGen(λ)
(w,state)
$← A0(G)
`
$
← Primes(λ)
u
$← A1(`,state)






≤ negl(λ).

The adaptive root assumption implies that the adversary can’t compute the
order of any non trivial element. For any element with known order the adversary
can compute arbitrary roots that are co-prime to the order. This immediately allows
the adversary to win the adaptive root game. For the group ZN this means that we
need to exclude {−1, 1}

We will also need the strong RSA assumption for general groups of unknown
order. The adaptive root and strong RSA assumptions are incomparable. The
former states that it is hard to take a random root of a chosen group element, while
the latter says that it is hard to take a chosen root of a random group element.
In groups of unknown order that do not require a trusted setup the adversary A
additionally gets access to GGen’s random coins.

Definition 2 (Strong RSA assumption). GGen satisfies the strong RSA assumption
if for all efficient A:
Pr "
u
` = g and ` is an odd prime :
G
$
← GGen(λ), g
$
← G,
(u, `) ∈ G × Z
$← A(G, g)
#
≤ negl(λ).

2.2 Generic group model for groups of unknown order

We will use the generic group model for groups of unknown order as defined by
Damgard and Koprowski [DK02]. The group is parameterized by two integer public
parameters A, B. The order of the group is sampled uniformly from [A, B]. The
group G is defined by a random injective function σ : Z|G| → {0, 1}
`
, for some `
where 2`   |G|. The group elements are σ(0), σ(1), . . . , σ(|G| − 1). A generic group
algorithm A is a probabilistic algorithm. Let L be a list that is initialized with the
encodings given to A as input. The algorithm can query two generic group oracles:

• O1 samples a random r ∈ Z|G| and returns σ(r), which is appended to the list
of encodings L.

• When L has size q, the second oracle O2(i, j, ±) takes two indices i, j ∈
{1, . . . , q} and a sign bit, and returns σ(xi ± xj ), which is appended to L.

Note that unlike Shoup’s generic group model [Sho97], the algorithm is not given
|G|, the order of the group G.

2.3 Argument systems

An argument system for a relation R ⊂ X ×W is a triple of randomized polynomial
time algorithms (Pgen, P, V), where Pgen takes an (implicit) security parameter
λ and outputs a common reference string (crs) pp. If the setup algorithm uses
only public randomness we say that the setup is transparent and that the crs is
unstructured. The prover P takes as input a statement x ∈ X , a witness w ∈
W, and the crs pp. The verifier V takes as input pp and x and after interaction
with P outputs 0 or 1. We denote the transcript between the prover and verifier
by hV(pp, x), P(pp, x, w)i and write hV(pp, x), P(pp, x, w)i = 1 to indicate that the
verifier accepted the transcript. If V uses only public randomness we say that the
protocol is public coin.

Definition 3 (Completeness). We say that an argument system (Pgen, P, V) for a
relation R is complete if for all (x, w) ∈ R:
Pr
hV(pp, x), P(pp, x, w)i = 1 : pp
$
← Pgen(λ)

= 1.

We now define soundness and knowledge extraction for our protocols. The adversary is modeled as two algorithms A0 and A1, where A0 outputs the instance
x ∈ X after Pgen is run, and A1 runs the interactive protocol with the verifier
using a state output by A0. In slight deviation from the soundness definition used in
statistically sound proof systems, we do not universally quantify over the instance
x (i.e. we do not require security to hold for all input instances x). This is due to
the fact that in the computationally-sound setting the instance itself may encode
a trapdoor of the crs pp (e.g. the order of a group of unknown order), which can
enable the adversary to fool a verifier. Requiring that an efficient adversary outputs
the instance x prevents this. In our soundness definition the adversary A1 succeeds
if he can make the verifier accept when no witness for x exists. For the stronger
argument of knowledge definition we require that an extractor with access to A1’s
internal state can extract a valid witness whenever A1 is convincing. We model this
by enabling the extractor to rewind A1 and reinitialize the verifier’s randomness.

Definition 4 (Arguments (of Knowledge)). We say that an argument system
(Pgen, P, V) is sound if for all poly-time adversaries A = (A0, A1):
Pr "
hV(pp, x), A1(pp, x,state)i = 1
and @w (x, w) ∈ R :
pp
$
← Pgen(1λ
)
(x,state) ← A0(pp)
#
= negl(λ).

Additionally, the argument system is an argument of knowledge if for all polytime adversaries A1 there exists a poly-time extractor Ext such that for all poly-time
adversaries A0:
Pr



hV(pp, x), A1(pp, x,state)i = 1
and (x, w0
) 6∈ R :
pp
$
← Pgen(1λ
)
(x,state) ← A0(pp)
w
0
$
← Ext(pp, x,state)


 = negl(λ).

Any argument of knowledge is also sound. In some cases we may further restrict
A in the security analysis, in which case we would say the system is an argument
of knowledge for a restricted class of adversaries. For example, in this work we construct argument systems for relations that depend on a group G of unknown order.
In the analysis we replace G with a generic group and restrict A to a generic group
algorithm that interacts with the oracles for this group. For simplicity, although
slightly imprecise, we say the protocol is an argument of knowledge in the generic
group model. Groth [Gro16] recently proposed a SNARK system for arbitrary relations that is an argument of knowledge in the generic group model in a slightly
different sense, where the generic group is used as part of the construction rather
than the relation and the adversary is a generic group algorithm with respect to this
group generated by the setup.

Definition 5 (Non interactive arguments). A non-interactive argument system
is an argument system where the interaction between P and V consists of only a
single round. We then write the prover P as π
$
← Prove(pp, x, w) and the verifier as
{0, 1} ← Vf(pp, x, π).

The Fiat-Shamir heuristic [FS87] and its generalization to multi-round protocols [BCS16] can be used to transform public coin argument systems to noninteractive systems.

3 Succinct proofs for hidden order groups

In this section we present several new succinct proofs in groups of unknown
order. The proofs build on a proof of exponentiation recently proposed by
Wesolowski [Wes18] in the context of verifiable delay functions [BBBF18]. We show
that the Wesolowski proof is a succinct proof of knowledge of a discrete-log in a
group of unknown order. We then derive a succinct zero-knowledge argument of
knowledge for a discrete-log relation, and more generally for knowledge of the inverse of a homomorphism h : Z
n → G, where G is a group of unknown order. Using
the Fiat-Shamir heuristic, the non-interactive version of this protocol is a special
purpose SNARK for the pre-image of a homomorphism.

3.1 A succinct proof of exponentiation

Let G be a group of unknown order. Let [`] := {0, 1, . . . , ` − 1} and let Primes(λ)
denote the set of odd prime numbers in [0, 2
λ
]. We begin by reviewing Wesolowski’s
(non-ZK) proof of exponentiation [Wes18] in the group G. Here both the prover and
verifier are given (u, w, x) and the prover wants to convince the verifier that w = u
x
holds in G. That is, the protocol is an argument system for the relation
RPoE =
 (u, w ∈ G, x ∈ Z); ⊥
 
: w = u
x ∈ G
	
.

The verifier’s work should be much less than computing u
x by itself. Note that
x ∈ Z can be much larger than |G|, which is where the protocol is most useful. The
protocol works as follows:

Protocol PoE (Proof of exponentiation) for RPoE [Wes18]
Params: G
$
← GGen(λ); Inputs: u, w ∈ G, x ∈ Z; Claim: u
x = w
1. Verifier sends `
$
← Primes(λ) to prover.
2. Prover computes the quotient q = bx/`c ∈ Z and residue r ∈ [`] such that
x = q` + r.
Prover sends Q ← u
q ∈ G to the Verifier.
3. Verifier computes r ← (x mod `) ∈ [`] and accepts if Q`u
r = w holds in
G.

The protocol above is a minor generalization of the protocol from [Wes18] in
that we allow an arbitrary exponent x ∈ Z, where as in [Wes18] the exponent
was restricted to be a power of two. This does not change the soundness property
captured in the following theorem, whose proof is given in [Wes18, Prop. 2] (see also
[BBF18, Thm. 2]) and relies on the adaptive root assumption for GGen.

Theorem 1 (Soundness PoE [Wes18]). Protocol PoE is an argument system for Relation RPoE with negligible soundness error, assuming the adaptive root assumption
holds for GGen.

For the protocol to be useful the verifier must be able to compute r = x mod `
faster than computing u
x ∈ G. The original protocol presented by Wesolowski
assumed that x = 2T
is a power of two, so that computing x mod ` requires only
log(T) multiplications in Z` whereas computing u
x
requires T group operations.

For a general exponent x ∈ Z, computing x mod ` takes O((log x)/λ) multiplications in Z`
. In contrast, computing u
x ∈ G takes O(log x) group operations in G.
Hence, for the current groups of unknown order, computing u
x
takes λ
3
times as
long as computing x mod `. Concretely, when ` is a 128 bit integer, a multiplication
in Z`
is approximately 5000 time faster than a group operation in a 2048-bit RSA
group. Hence, the verifier’s work is much less than computing w = u
x
in G on its
own.

Note that the adaptive root assumption is not only a sufficient security requirement but also necessary. In particular it is important that no known order elements are in the group G. Assume for example that −1 ∈ G such that
(−1)2 = 1 ∈ G. If g
x = y then an adversary can succeed in PoE(g, −y, x) by setting
Q0 ← −1 · g
bx/`c
. It is, therefore, important to not directly use the multiplicative
RSA group G := (Z/N)
∗ but rather G+ := G/{−1, 1} as described in [BBF18].

The PoE protocol can be generalized to a relation involving any homomorphism
φ : Z
n → G for which the adaptive root assumption holds in G. The details of this
generalization are discussed in Appendix A.1.

3.2 A succinct proof of knowledge of a discrete-log

We next show how the protocol PoE can be adapted to provide an argument of
knowledge of discrete-log, namely an argument of knowledge for the relation:
RPoKE =
 (u, w ∈ G); x ∈ Z
 
: w = u
x ∈ G
	
.

The goal is to construct a protocol that has communication complexity that is much
lower than simply sending x to the verifier. As a stepping stone we first provide
an argument of knowledge for a modified PoKE relation, where the base u ∈ G is
fixed and encoded in a CRS. Concretely let CRS consist of the unknown-order group
G and the generator g. We construct an argument of knowledge for the following
relation:
RPoKE∗ =
 w ∈ G; x ∈ Z
 
: w = g
x ∈ G
	
.

The argument modifies the PoE Protocol in that x is not given to the verifier, and
the remainder r ∈ [`] is sent from the prover to the verifier:

Protocol PoKE∗
(Proof of knowledge of exponent) for Relation RPoKE∗
Params: G
$
← GGen(λ), g ∈ G; Inputs: w ∈ G; Witness: x ∈ Z; Claim:
g
x = w
1. Verifier sends `
$
← Primes(λ).
2. Prover computes the quotient q ∈ Z and residue r ∈ [`] such that x =
q` + r. Prover sends the pair (Q ← g
q
, r) to the Verifier.
3. Verifier accepts if r ∈ [`] and Q`
g
r = w holds in G.

Here the verifier does not have the witness x, but the prover additionally sends
r := (x mod `) along with Q in its response to the verifier’s challenge. Note that
the verifier no longer computes r on its own, but instead relies on the value from
the prover. We will demonstrate an extractor that extracts the witness x ∈ Z
from a successful prover, and prove that this extractor succeeds with overwhelming
probability against a generic group prover. In fact, in the next section we will present
a generalization of Protocol PoKE∗
to group representations in terms of bases {gi}
n
i=1
included in the CRS, i.e. a proof of knowledge of an integer vector x ∈ Z
n
such that
Q
i
g
xi
i = w. We will prove that this protocol is an argument of knowledge against a
generic group adversary. The security of Protocol PoKE∗ above follows as a special
case. Hence, the following theorem is a special case of Theorem 7 below.

Theorem 2. Protocol PoKE∗
is an argument of knowledge for relation RPoKE∗ in
the generic group model.

An attack. Protocol PoKE∗
requires the discrete logarithm base g to be encoded
in the CRS. When this protocol is applied to a base freely chosen by the adversary
it becomes insecure. In other words, Protocol PoKE∗
is not a secure protocol for the
relation RPoKE.

To describe the attack, let g be a generator of G and let u = g
x and w = g
y
where y 6= 1 and x does not divide y. Suppose that the adversary knows both x and
y but not the discrete log of w base u. Computing an integer discrete logarithm of
w base u is still difficult in a generic group (as follows from Lemma 3), however an
efficient adversary can nonetheless succeed in fooling the verifier as follows. Since
the challenge ` is co-prime with x with overwhelming probability, the adversary can
compute q, r ∈ Z such that q` + rx = y. The adversary sends (Q = g
q
, r) to the
verifier, and the verifier checks that indeed Q`u
r = w. Hence, the verifier accepts
despite the adversary not knowing the discrete log of w base u.

This does not qualify as an “attack” when x = 1, or more generally when x
divides y, since then the adversary does know the discrete logarithm y/x such that
u
y/x = w.

Extending PoKE for general bases. To obtain a protocol for the relation
RPoKE we start by modifying protocol PoKE∗
so that the prover first sends z = g
x
,
for a fixed base g, and then executes two PoKE∗
style protocols, one base g and one
base u, in parallel, showing that the discrete logarithm of w base u equals the one
of z base g. We show that the resulting protocol is a secure argument of knowledge
(in the generic group model) for the relation RPoKE. The transcript of this modified
protocol now consists of two group elements instead of one.

Protocol PoKE (Proof of knowledge of exponent)
Params: G
$
← GGen(λ), g ∈ G; Inputs: u, w ∈ G; Witness: x ∈ Z;
Claim: u
x = w
1. Prover sends z = g
x ∈ G to the verifier.
2. Verifier sends `
$
← Primes(λ).
3. Prover finds the quotient q ∈ Z and residue r ∈ [`] such that x = q` + r.
Prover sends Q = u
q and Q0 = g
q and r to the Verifier.
4. Verifier accepts if r ∈ [`], Q`u
r = w, and Q0`
g
r = z.

The intuition for the security proof is as follows. The extractor first uses the
same extractor for Protocol PoKE∗
(specified in Theorem 7) to extract the discrete
logarithm x of z base g. It then suffices to argue that this extracted discrete logarithm x is a correct discrete logarithm of w base u. We use the adaptive root
assumption to argue that the extracted x is a correct discrete logarithm of w base
u.

We can optimize the protocol to bring down the proof size back to a single group
element. We do so in the protocol PoKE2 below by adding one round of interaction.
The additional round has no effect on proof size after making the protocol noninteractive using Fiat-Shamir.

Protocol PoKE2 (Proof of knowledge of exponent)
Params: G
$
← GGen(λ); Inputs: u, w ∈ G; Witness: x ∈ Z; Claim: u
x = w
1. Verifier sends g
$
← G to the Prover.
2. Prover sends z ← g
x ∈ G to the verifier.
3. Verifier sends `
$
← Primes(λ) and α
$
← [0, 2
λ
).
4. Prover finds the quotient q ∈ Z and residue r ∈ [`] such that x = q` + r.
Prover sends Q = u
q
g
αq and r to the Verifier.
5. Verifier accepts if r ∈ [`] and Q`u
r
g
αr = wzα.

The intuition for the security proof is the same as for Protocol PoKE, but we additionally show that (in the generic group model) a similar extraction argument
holds when the prover instead sends Q ← u
q
g
q and r such that Q`u
r
g
r = wz. The
extraction argument uses the fact that with overwhelming probability the generic
adversary did not obtain g from any of its group oracle queries prior to forming
w and therefore the adversary’s representation of w does not contain g as a base
with a non-zero exponent. The extractor is able to obtain an exponent x such that
(gu)
x = wz. This alone does not yet imply that u
x = w, however if the prover
sends Q, r such that Q`u
r
g
αr = wzα, then the extractor obtains a fixed x such that
(g
αu)
x = wzα with high probability over the random choice of α. This implies that
either u
x = w or w/ux
is an element of low order, which breaks the adaptive root
assumption. We summarize this in the following theorem. See Appendix C for the
proof.

Theorem 3 (PoKE Argument of Knowledge). Protocol PoKE and Protocol PoKE2
are arguments of knowledge for relation RPoKE in the generic group model.

The PoKE argument of knowledge can be extended to an argument of knowledge
for the pre-image of a homomorphism φ : Z
n → G. This is included in Appendix A.2.

We can also construct a (honest-verifier) zero-knowledge version of the PoKE
argument of knowledge protocol using a method similar to the classic Schnorr Σprotocol for hidden order groups. This is covered in Appendix A.4.

3.3 Aggregating Knowledge of Co-prime Roots

Unlike exponents, providing a root of an element in a hidden order group is already
succinct (it is simply a group element). There is a simple aggregation technique
for providing a succinct proof of knowledge for multiple co-prime roots x1, ..., xn
simultaneously. This is useful for aggregating PoKE proofs.

When the roots are all for the same element α then the witness is trivially a root
α
1/x∗
where x
∗ = x1 · · · xn. From this witness one can publicly extract the xith root
of α for each i. We show a method where the elements need not be the same, i.e.
the witness is a list of elements w1, ..., wn for public elements α1, ..., αn and public
integers x1, ..., xn such that w
xi
i = αi for each i and gcd(xi
, xj ) = 1∀i, j ∈ [1, n], i 6= j.
The size of the proof is still a single element.

Concretely the PoKCR protocol is a proof for the relation:
RPoKCR =
 α ∈ G
n
; x ∈ Z
n
 
: w = φ(x) ∈ G
	
.

The proof is the product of witnesses, w ← w1 · · · wn. From this product and
the public xi
’s and αi
’s it is possible to extract an xith root of each αi
. (This is
not necessarily the same as wi as roots are not unique). Moreover, the verification
algorithm does not need to run this extraction procedure in full, it only needs
to check that w
x
∗
=
Q
i α
x
∗/xi
i
. This equation can be verifier with O(n log n)
group exponentiations with exponents of size at most maxi
|xi
| using the optimized
recursive MultiExp algorithm shown below.

Protocol PoKCR for Relation RPoKCR
Input: G, α1, ..., αn ∈ G, x1, ..., xn ∈ Z s.t. gcd(x1, ..., xn) = 1;
Witness: w ∈ Gn
s.t. w
xi
i = αi
1. Prover sends w ←
Qn
i=1 wi to the Verifier.
2. Verifier computes x
∗ ←
Qn
i=1 xi
, and y ←
Qn
i=1 α
x
∗/xi
i
using MultiExp(n, α, x). Verifier accepts if w
x
∗
= y.
MultiExp(n, α, x):
1. if n = 1 return α
2. αL ← (α1, ..., αn/2
); αR ← (αn/2+1, ..., αn)
3. xL ← (x1, ..., xn/2
); xR ← (xn/2+1, ..., xn)
4. x
∗
L ← x1 · · · xn/2
; x
∗
R ← xn/2+1 · · · xn
5. L ← MultiExp(n/2, αL, xL); R ← MultiExp(n/2, αR, xR)
6. return L
x
∗
R · Rx
∗
L
Lemma 1. Protocol PoKCR is an argument of knowledge for Relation RPoKCR.

Proof. We show that given any w such that w
x
∗
= y =
Qn
i=1 α
x
∗/xi
i
it is possible to
compute directly an xith root of αi for all i. For each i and j 6= i let zij = x
∗/(xixj ).
For each i, let Aj =
Q
i6=j α
zij
i
, then we can express y = A
xi
j α
x
∗/xi
i
. This shows that
the element u = w
(x
∗/xi)A
−1
j
is an xith root of α
x
∗/xi
i
. Since gcd(x
∗/xi
, xi) = 1,
there exist Bezout coefficients a, b such that a(x
∗/xi) + bxi = 1. Finally, u
aα
b
i
is an
xith root of αi as (u
aα
b
i
)
xi = α
(ax∗/xi)+bxi
i = αi
.

Non-interactive proofs All of the protocols can be made non-interactive using
the standard Fiat-Shamir transform. In the Fiat-Shamir transform, the prover noninteractively builds a simulated transcript of the protocol by replacing each of the
verifier’s challenges with a hash of the protocol transcript preceding the challenge
using a collision-resistant hash function H as a heuristic substitute for a random
oracle. In our protocols, the verifier’s challenges are sampled from Primes(λ) and
G. Therefore, the non-interactive version must involve a canonical mapping of the
output seed σ of the random oracle to a random prime or element of G. Furthermore,
it is important that hashing to an element g ∈ G does not reveal the discrete log
relation between g and any another element (i.e. g ← u
σ
is not secure). The simplest
way to map σ to a prime in Primes(λ) is find the smallest integer i such that the
first λ bits of H(σ, i) is prime. More efficient methods are described in Section 7.
It is these non-interactive, succinct, and efficiently verifiable proofs that are most
useful for the applications discussed later in this paper. Appendix D summarizes
the non-interactive proofs that will be used later.

Aggregating PoKE proofs Several non-interactive PoE/PoKE/PoKE2 proofs can
be aggregated using the PoKCR protocol. The value Q sent to the verifier in this
proof is the `th root of yg−r
. As long as the primes sampled in each proof instance
are distinct then these proofs (specifically the values Qi) are a witness for an instance
of PoKCR. Since the primes are generated by hashing the inputs to the proof they
need not be included in the proof.

4 Trapdoorless Universal Accumulator

In this section we describe a number of new techniques for manipulating accumulators built from the strong RSA assumption in a group of unknown order. We
show how to efficiently remove elements from the accumulator, how to use the proof
techniques from Section 3 to give short membership proofs for multiple elements,
and how to non-interactively aggregate inclusion and exclusion proofs. All our techniques are geared towards the setting where there is no trusted setup. We begin by
defining what an accumulator is and what it means for an accumulator to be secure.

Our presentation of a trapdoorless universal accumulator mostly follows the definitions and naming conventions of [BCD+17]. Figure 1 summarizes the accumulator
syntax and list of associated operations. One notable difference in our syntax is the
presence of a common reference string pp generated by the Setup algorithm in place
of private/public keys.

The security definition we follow [Lip12] formulates an undeniability property
for accumulators. For background on how this definition relates to others that have
been proposed see [BCD+17], which gives generic transformations between different
accumulators with different properties and at different security levels.

The following definition states that an accumulator is secure if an adversary
cannot construct an accumulator, an element x and a valid membership witness w
t
x
and a non-membership witness u
t
x where w
t
x
shows that x is in the accumulator
and u
t
x
shows that it is not. Lipmaa [Lip12] also defines undeniability without a
trusted setup. In that definition the adversary has access to the random coins used
by Setup.

Definition 6 (Accumulator Security (Undeniability)).
Pr



pp, A0 ∈ G
$
← Setup(λ)
(A, x, wx, ux)
$← A(pp, A0)
VerMem(A, x, wt
x
) ∧ VerNonMem(A, x, ut
x
)


 = negl(λ)

4.1 Accumulator construction

Several sub-procedures that are used heavily in the construction are summarized
below. Bezout(x,y) refers to a sub-procedure that outputs Bezout coefficients
a, b ∈ Z for a pair of co-prime integers x, y (i.e. satisfying the relation ax + by = 1).
ShamirTrick uses Bezout coefficient’s to compute an (xy)-th root of a group element g from an x-th root of g and a yth root of g. RootFactor is a procedure
that given an element y = g
x and the factorization of the exponent x = x1 · · · xn
computes an xi-th root of y for all i = 1, . . . , n in total time O(n log(n)). Naively
this procedure would take time O(n
2
). It is related to the MultiExp algorithm
described earlier and was originally described by [STSY01].

ShamirTrick(w1
, w2
, x, y): [Sha83]
1. if w
x
1
6= w
y
2
return ⊥
2. a, b ← Bezout(x, y)
3. return w
b
1w
a
2
Hprime(x):
1. y ← H(x)
2. while y is not odd prime:
3. y ← H(y)
4. return y
RootFactor(g, x1, . . . , xn):
1. if n = 1 return g
2. n
0 ← bn
2
c
3. gL ← g
Qn
0
j=1 xj
4. gR ← g
Qn
j=n0+1 xj
5. L ←RootFactor(gR, x1, . . . , xn0)
6. R ←RootFactor(gL, xn0+1, . . . , xn)
7. return L k R

Groups of unknown order The accumulator requires a procedure GGen(λ)
which samples a group of unknown order in which the strong root assumption (Definition 2) holds. One can use the quotient group (Z/N)
∗/{−1, 1}, where N is an
RSA modulus, which may require a trusted setup to generate the modulus N. Alternatively, one can use a class group which eliminates the trusted setup. Note that
the adaptive root assumption requires that these groups have no known elements
of low order, and hence the group (Z/N)
∗
is not suitable because (−1) ∈ (Z/N)
∗
has order two [BBF18]. Given an element of order two it is possible to convince a
PoE-verifier that g
x = −y when in fact g
x = y.

The basic RSA accumulator. We review he classic RSA accumulator [CL02,
Lip12] below, omitting all the procedures that require trapdoor information. All
accumulated values are odd primes. If the strong RSA assumption (Definition 2)
holds in G, then the accumulator satisfies the undeniability definition [Lip12].
The core procedures for the basic dynamic accumulator are the following:

• Setup generates a group of unknown order and initializes the group with a
generator of that group.

• Add takes the current accumulator At
, an element from the odd primes domain, and computes At+1 = At
.

• Del does not have such a trapdoor and therefore needs to reconstruct the set
from scratch. The RootFactor algorithm can be used for pre-computation.
Storing 2k
elements and doing n · k work, the online removal will only take
(1 −
1
2
k
) · n steps.

• A membership witness is simply the accumulator without the aggregated item.

• A membership non-witness, proposed by [LLX07], uses the fact that for
any x 6∈ S, gcd(x, Q
s∈S
s) = 1. The Bezout coefficients (a, b) ←
Bezout(x, Q
s∈S
s) are therefore a valid membership witness. The actual witness is the pair (a, gb
) which is short because |a| ≈ |x|.

• Membership and non-membership witnesses can be efficiently updated as in
[LLX07]

Setup(λ):
1. G
$
← GGen(λ)
2. g
$
← G
3. return G, g
Add(At
, S, x):
1. if x ∈ S : return At
2. else :
3. S ← S ∪ {x}
4. upmsg ← x
5. return Ax
t
, upmsg
Del(At
, S, x):
1. if : x 6∈ S : return At
2. else :
3. S ← S \ {x}
4. At+1 ← g
Q
s∈S
s
5. upmsg ← {x, At
, At+1}
6. return At+1, upmsg
MemWitCreate(A, S, x) :
1. w
t
x ← g
Q
s∈S,s6=x
s
2. return w
t
x
NonMemWitCreate(A, S, x) :
1. s
∗ ←
Q
s∈S
s
2. a, b ← Bezout(s
∗
, x)
3. B ← g
b
4. return u
t
x ← {a, B}
VerMem(A, wx
, x) :
1. return 1 if (wx
)
x = A
VerNonMem(A, ux
, x) :
1. {a, B} ← ux
2. return 1 if AaBx = g

Theorem 4 (Security accumulator [Lip12]). Assume that the strong RSA assumption (Definition 2) holds in G. Then the accumulator satisfies undeniability (Definition 6) and is therefore secure.

Proof. We construct an ARSA that given an AAcc for the accumulator breaks the
strong RSA assumption. ARSA receives a group G ← GGen(λ) and a challenge
g
$
← G. We now run AAcc on input G and A0 = g. AAcc returns a tuple (A, x, wx, ux)
such that VerMem(A, x, wx
) = 1 and VerNonMem(A, x, ux
) = 1. ARSA parses
(a, B) = ux and computes B · (wx)
a as the xth root of g. x is an odd prime by
definition and (B ·w
a
x
)
x = Bx
·Ab = g. This contradicts the strong RSA assumption
and thus shows that the accumulator construction satisfies undeniability.

Updating membership witnesses [CL02, LLX07] Updating membership witnesses when an item is added simply consists of adding the item to the witness which
itself is an accumulator. The membership witness is an xth root of the accumulator
At
. After removal of ˆx, At+1 is an ˆxth root of At
. We can use the ShamirTrick
to compute an x · xˆth root of At which corresponds to the updated witness. Updating the non-membership witnesses is done by computing the Bezout coefficients
between x and the newly added/deleted item ˆx and then updating non-membership
witness such that it represents the Bezout coefficient’s between x and the product
of the accumulated elements. For a complete description and correctness proof see
[LLX07].

4.2 Batching and aggregation of accumulator witnesses

Aggregating membership witnesses Aggregating membership witnesses for
many elements into a single membership witness for the set is straightforward using
ShamirTrick. However, verification of this membership witness is linear in the
number of group operations. Note that the individual membership witnesses can
still be extracted from the aggregated witness as wx = w
y
xy. Security, therefore, still
holds for an accumulator construction with aggregated membership witnesses. The
succinct proof of exponentiation (NI-PoE) enables us to produce a single membership
witness that can be verified in constant time. The verification VerAggMemWit
simply checks the proof of exponentiation.

Aggregating existing membership witnesses for elements in several distinct accumulators (that use the same setup parameters) can be done as well. The algorithm
MemWitX simply multiplies together the witnesses wx
for an element x ∈ A1
and wy
for y ∈ A2 to create an inclusion proof wxy for x and y. The verification
checks w
x·y
xy = A
y
1Ax
2
. If x and y are co-prime then we can directly recover wx and
wy
from the proof wxy. In particular wx = ShamirTrick(A
y
1
, A1, w
y
xyA
−1
2
, y, x) and
wy = ShamirTrick(Ax
2
, A2, wx
xyA
−1
1
, x, y).

AggMemWit(A, wx, wy, x, y) :
1. wx·y ← ShamirTrick(A, wx, wy, x, y)
2. return wx·y, NI-PoE(wx·y, x · y, A)
MemWitCreate*(A, {x1, . . . , xn}) :
1. x
∗ =
Qn
i=1 xi
2. wx∗ ← MemWitCreate(A, x∗
)
3. return wx∗ , NI-PoE(x, wx∗ , A)
VerMem*(A, {x1, . . . , xn}, w = {wx, π}):
1. return NI-PoE.verify(
Qn
i=1 xi
, w, A, π)
MemWitX(A1, A2, wx, wy, x, y) :
1. return wxy ← wx · wy
VerMemWitX(A1, A2, wxy, x, y) :
1. if gcd(x, y) 6= 1
2. return ⊥
3. else
4. return w
x·y
xy ← A
y
1Ax
2

Distributed accumulator updates In the decentralized/distributed setting, the
accumulator is managed by a distributed network of participants who only store
the accumulator state and a subset of the accumulator elements along with their
membership witnesses. These participants broadcast their own updates and listen
for updates from other participants, updating their local state and membership
witnesses appropriately when needed.

We observe that the basic accumulator functions do not require a trapdoor or
knowledge of the entire state, summarized in Figure 2. In particular, deleting an
item requires knowledge of the item’s current membership witness (the accumulator
state after deletion is this witness). Moreover, operations can be performed in
batches as follows:

The techniques are summarized as follows:

• BatchAdd An NI-PoE proof can be used to improve the amortized verification
efficiency of a batch of updates that add elements x1, ..., xm at once and update
the accumulator to At+1 ← Ax
∗
t
. A network participant would check that
x
∗ =
Q
i
xi and verify the proof rather than compute the m exponentiations.

• BatchDel Deleting elements in a batch uses the AggMemWit function to a
compute the aggregate membership witness from the individual membership
witnesses of each element. This is the new state of the accumulator. A NI-PoE
proof improves the verification efficiency of this batch update.

• CreateAllMemWit It is possible for users to update membership and nonmembership witnesses [LLX07]. The updates do not require knowledge of
the accumulated set S but do require that every accumulator update is processed. Since this is cumbersome some users may rely on service providers for
maintaining the witness. The service provider may store the entire state or
just the users witnesses. Creating all users witnesses naively requires O(n
2
)
operations. Using the RootFactor algorithm this time can be reduced to
O(n log(n)) operations or amortized O(log(n)) operations per witness.

1The condition that gcd(x, y) = 1 is minor as we can simply use a different set of primes as
the domains for each accumulator. Equivalently we can utilize different collision resistant hash
functions with prime domain for each accumulator. The concrete security assumption would be
that it is difficult to find two values a, b such that both hash functions map to the same prime. We
utilize this aggregation technique in our IOP application (Section 6.2).

• CreateManyNonMemWit Similarly to CreateAllMemWit it is possible
to create m non-membership witness using O(max(n, m) + m log(m)) operations. This stands in contrast to the naive algorithm that would take O(m·n)
operations. The algorithm is in Figure 4.2.

Add(At, x):
1. return Ax
t
BatchAdd(At, {x1, . . . , xm}):
1. x
∗ ←
Qm
i=1 xi
2. At+1 ← Ax
∗
t
3. return At+1, NI-PoE(x
∗
, At, At+1)
DelWMem(At, wt
x
, x):
1. if VerMem(At, wt
x
, x) = 1
2. return w
t
x
BatchDel(At,(x1, wt
x1
). . . ,(xm, wt
xm
)):
1. At+1 ← w
t
x1
2. x
∗ ← x1
3. for i ← 2, i ≤ m
4. At+1 ← ShamirTrick(At+1, wt
xi
, x, xi)
5. x
∗ ← x
∗
· xi
6. return At+1, NI-PoE(x
∗
, At+1, At)
CreateAllMemWit(S) :
1.	return RootFactor(g, S)

Figure 2: Distributed and stateless accumulator functions.

Batching non-membership witnesses A non-membership witness ux for x in
an accumulator with state A for a set S is ux = {a, gb} such that as∗ + bx = 1
for s
∗ ←
Q
s∈S
s. The verification checks Aa
g
bx = g. Since gcd(s
∗
, x) = 1 and
gcd(s
∗
, y) = 1 if and only if gcd(s
∗
, xy) = 1, to batch non-membership witnesses
we could simply construct a non-membership witness for x · y. A prover computes a
0
, b0 ← Bezout(s
∗
, xy) and sets uxy ← a
0
, gb
0
. This is still secure as a
non-membership witness for both x and y because we can easily extract a nonmembership witness for x as well as for y from the combined witness (a
0
, B0
) by
setting ux = (a
0
,(B0
)
y
) and uy = (a
0
,(B0
)
x
).

Unfortunately, |a
0
| ≈ |xy| so the size of this batched non-membership witness
is linear in the number of elements included in the batch. A natural idea is to set
uxy = (V, B) ← (Aa
0
, gb
0
) ∈ G2
instead of (a
0
, B) ∈ Z×G as the former has constant
size. The verification would check that V Bxy = g. This idea doesn’t quite work as an
adversary can simply set V = gB−xy without knowing a discrete logarithm between
A and V . Our solution is to use the NI-PoKE2 protocol to ensure that V was created
honestly. Intuitively, soundness is achieved because the knowledge extractor for the
NI-PoKE2 can extract a
0
such that (a
0
, B) is a standard non-membership witness for
xy.

The new membership witness is V, B, π ← NI-PoKE(A,v;b). The size of this
witness is independent of the size of the statement. We can further improve the
verification by adding a proof of exponentiation that the verification equation holds:
NI-PoE(x · y, B, g · V
−1
). Lastly, recall from Section 3 that the two independent
NI-PoKE2 and NI-PoE proofs can be aggregated into a single group element.
We present the non-membership protocol bellow as NonMemWitCreate*. The
verification algorithm VerNonMem* simply verifies the NI-PoKE2 and NI-PoE.
NonMemWitCreate*(A, s∗
, x∗
) : // A = g
s
∗
, s
∗ =
Q
s∈S
s, x =
Q xi, xi ∈ Primes(λ)
1. a, b ← Bezout(s
∗
, x∗
)
2. V ← Aa
, B ← g
b
3. πV ← NI-PoKE2(A, V ; a) // V = Aa
4. πg ← NI-PoE(x
∗
, B, g · V
−1
) // Bx = g · V −1
5. return {V, B, πV , πg}
VerNonMem*(A, u = {V, B, πV , πg}, {x1, . . . , xn}):
1. return NI-PoKE2.verify(A, V, πV ) ∧ NI-PoE.verify(
Qn
i=1 xi
, B, g · V
−1
, πg)

Soundness of batch non-membership witnesses Using the knowledge extractor for NI-PoKE2 and relying on the soundness of NI-PoE, and given an adversary
who outputs a valid batch non-membership witness (V, B, πV , πg) for a set of odd
prime elements x1, ..., xk with respect to an accumulator state A, we can extract individual non-membership witnesses for each xi
. The knowledge extractor for NI-PoKE2
(Theorem 3) obtains a such that V = Aa and the soundness of NI-PoE (Theorem 1)
guarantees that Bx
∗
· V = g where x
∗ =
Q
i
xi
. Given a and B we can compute
a non-membership witness for xi
|x
∗ as B
x
∗
xi ,a because (B
x
∗
xi )
xiAa = Bx
∗
V = g Recall that we proved the existence of a knowledge extractor only for the interactive
form of PoKE2 and soundness for the interactive form of PoE, relying on the generic
group model. The existence of a knowledge extractor for NI-PoKE2 and soundness
of NI-PoE are derived from the Fiat-Shamir heuristic.

Batch accumulator security We now formally define security for an accumulator with batch membership and non-membership witnesses. The definition naturally
generalizes Definition 6. We omit a correctness definition as it follows directly from
the definition of the batch witnesses. We assume that correctness holds perfectly.

Definition 7 (Batch Accumulator Security (Undeniability)).
Pr



pp, A0 ∈ G
$
← Setup(λ)
(A, I, E, wI , uE)
$← A(pp, A0) :
VerMem*(A, I, wI
) ∧ VerNonMem*(A, S, uS
) ∧ I ∩ S 6= ∅


 = negl(λ)

From the batch witnesses wI and uS we can extract individual accumulator
witnesses for each element in I and S. Since the intersection of the two sets is not
empty we have an element x and extracted witnesses wx and ux for that element.
As in the proof of Theorem 4 this lets us compute and xth root of g which directly
contradicts the strong RSA assumption. Our security proof will be in the generic
group model as it implies the strong RSA assumption, the adaptive root assumption
and can be used to formulate extraction for the PoKE2 protocol. Our security proof
uses the interactive versions of PoKE2 and PoE protocols but extraction/soundness
holds for their non-interactive variants as well.

Theorem 5. The batch accumulator construction presented in Section 4.2 is secure
(Definition 7) in the generic group model.

Proof. We will prove security by showing that given an adversary that can break
the accumulator security we can construct an efficient extractor that will break the
strong RSA assumption (Definition 2). This, however, contradicts the generic group
model in which strong RSA holds [DK02]. Given a strong RSA challenge g ∈ G we
set A0 the accumulator base value to g. Now assume there exists such an adversary
A that on input (G, g) with non-negligible probability outputs (A, I, E, wI , uE) such
that wI and uE are valid witnesses for the accumulator A and the inclusion proof
elements I intersect with the exclusion proof elements E. Let x ∈ I ∩ S be in
the intersection. The batch membership witness wI is such that w
Q
xi∈I
xi
I = A
with overwhelming probability. This follows directly from the soundness of the
accompanying PoE proof (Theorem 1). We can directly compute wx = w
Q xi∈I,xi6=x
I
,
i.e. a membership witness for x.

The batch non-membership witness uE consists of B, V ∈ G as well as a PoKE2
and a PoE We now use the PoKE2 extractor to compute a ∈ Z, B ∈ G. Given
that the extractor succeeds with overwhelming probability (Theorem 3) and the
overwhelming soundness of PoE(Theorem 1), a, B satisfy AaB
Q
xi∈E xi = g. From
this we can compute B0 = B
Q
xi∈E,xi
6=x
xi
such that AaB0x = g. As in the proof
of Theorem 4 we can now compute B0w
a
x as an xth root of g. This is because
B0x
(w
a
x
)
x = B0xAa = g. This, however, contradicts the strong RSA assumption.

Aggregating non-membership witnesses We have shown how to create a constant sized batch non-membership witness for arbitrary many witnesses. However
this process required knowledge of the accumulated set S. Is it possible to aggregate
multiple independent non-membership witnesses into a single constant size witness?
We show that we can aggregate unbatched witnesses and the apply the same batching technique to the aggregated witness. This is useful in the stateless blockchain
setting where a node may want to aggregate non-membership witnesses created by
independent actors. Additionally it will allow us to aggregate vector commitment
openings for our novel construction presented in Section 5.

Given two non-membership witness ux = {ax, Bx} and uy = {ay, By} for two
distinct elements x and y and accumulator A we want to create a witness for x · y.
As shown for batch non-membership witnesses a non-membership witness for x · y
is equivalent to a witness for x and y if x and y are co-prime.

Concretely we will compute axy ∈ Z and Bxy ∈ G such that AaxyB
x·y
xy = g. First
we compute α, β ← Bezout(x, y). Then we set B0 ← B
β
xBα
y and set a
0 ← βaxy +
αayx. Note that B0 ∈ G, a
0 ∈ Z already satisfy Aa
0
B0xy = (AaxBx
x
)
βy(AayB
y
y )
αx =
g
βy+αx = g but that |a
0
| is not necessarily less than xy. To enforce this we simply
reduce a
0 mod xy, setting axy ← a
0 mod xy and Bxy ← B0A
b
a
0
xy
c
. The verification
equation AaxyB
xy
xy = Aa
0
B0xy = g is still satisfied.

The full protocol is presented below:

AggNonMemWit(A, x, y, ux = (ax ∈ [x], Bx ∈ G), uy = (ay, By)) :
1. α, β ← Bezout(x, y)
2. B0 ← B
β
xBα
y
3. a
0 ← βaxy + αayx
4. axy ← a
0 mod xy
5. Bxy ← B0A
b
a
0
xy
c
6. return {axy, Bxy}

As in Theorem 5, non-membership witnesses for x and y individually can be computed from an aggregated non-membership witness for x and y. Note that we can
also use a PoKE proof and apply the non-membership batching technique presented
above to make the proof constant size. The final witness can be verified using the
VerNonMem* algorithm.

Unions and Multiset accumulators Our succinct proofs can be used to prove
that an accumulator is the union of two other accumulators. This uses a succinct
proof of a DDH tuple, another special case of a homomorphism preimage. Further
details are given in Appendix B.1. In the distributed accumulator setting, it is
necessary to assume that no item is added twice to the accumulator. Otherwise, the
distributed delete operation will fail. Alternatively, the construction can be viewed
as a multi-set accumulator, where every element has a counter. Generating a valid
membership witness for an element requires knowing the count of that element
in the accumulator multi-set. Further details on this construction are given in
Appendix B.2.

5 Batchable Vector Commitments with Small Parameters

5.1 VC Definitions

We review briefly the formal definition of a vector commitment. We only consider
static commitments that do not allow updates, but our scheme can naturally be
modified to be dynamic.

Vector commitment syntax A VC is a tuple of four algorithms: VC.Setup,
VC.Com, VC.Open, VC.Verify.

1. VC.Setup(λ, n,M) → pp Given security parameter λ, length n of the vector,
and message space of vector components M, output public parameters pp,
which are implicit inputs to all the following algorithms.

2. VC.Com(m) → τ, com Given an input m = (m1, ..., mn) output a commitment
com and advice τ .

3. VC.Update(com, m, i, τ ) → τ, com Given an input message m and position i
output a commitment com and advice τ .

4. VC.Open(com, m, i, τ ) → π On input m ∈ M and i ∈ [1, n], the commitment
com, and advice τ output an opening π that proves m is the ith committed
element of com.

5. VC.Verify(com, m, i, π) → 0/1 On input commitment com, an index i ∈ [n],
and an opening proof π output 1 (accept) or 0 (reject).

If the vector commitment does not have an VC.Update functionality we call it a
static vector commitment.

Definition 8 (Static Correctness). A static vector commitment scheme VC is correct
if for all m ∈ Mn and i ∈ [1, n]:
Pr

VC.Verify(com, mi
, i, π) = 1 :
pp ← VC.Setup(λ, n,M)
τ, com ← VC.Com(m)
π ← V C.Open(com, mi
, i, τ )

 = 1

The correctness definition for dynamic vector commitments also incorporates updates. Concretely whenever VC.Update is invoked the underlying committed vector
m is updated correctly.

Binding commitments The main security property of vector commitments (of
interest in the present work) is position binding. The security game augments the
standard binding commitment game

Definition 9 (Binding). A vector commitment scheme VC is position binding if
for all O(poly(λ))-time adversaries A the probability over pp ← VC.Setup(λ, n,M)
and (com, i, m, m0
, π, π0
) ← A(pp) the probability that VC.Verify(com, m, i, π) =
VC.Verify(com, m0
, i, π0
) = 1 and m 6= m0
is negligible in λ.

5.2 VC construction

We first present a VC construction for bit vectors, i.e. using the message space
M = {0, 1}. We then explain how this can be easily adapted for a message space of
arbitrary bit length.

Our VC construction associates a unique prime2
integer pi with each ith index
of the bitvector m and uses an accumulator to commit to the set of all primes
corresponding to indices where mi = 1. The opening of the ith index to mi = 1 is
an inclusion proof of pi and the opening to mi = 0 is an exclusion proof of pi
. By
using our accumulator from Section 4, the opening of each index is constant-size.
Moreover, the opening of several indices can be batched into a constant-size proof
by aggregating all the membership witnesses for primes on the indices opened to 1
and batching all the non-membership witnesses for primes on the indices opened to
0.

The VC for vectors on a message space of arbitrary bit length is exactly the
same, interpreting the input vector as a bit vector. Opening a λ-bit component is
then just a special case of batch opening several indices of a VC to a bit vector. The
full details are in Figure 4.

VC.Setup(λ):
• A ← Accumulator.Setup(λ)
• return pp ← (A, n)
VC.Com(m, pp) :
• P ← {pi
|i ∈ [1, n] ∧ mi = 1}
• A.BatchAdd(P)
• return A
VC.Update(b, b0 ∈ {0, 1}, i ∈ [1, n]):
• if b = b
0
return A
• elseif b = 1
• return A.Add(pi)
• else
• return A.Del(pi)
VC.Open(b ∈ {0, 1}, i ∈ [1, n]) :
• if b = 1
• return A.MemWitCreate(pi)
• else
• return A.NonMemWitCreate(pi)
VC.Verify(A, b ∈ {0, 1}, i, π) :
• if b = 1 :
• return A.VerMem(π, pi)
• else :
• return A.VerNonMem(π, pi)
VC.BatchOpen(b ∈ {0, 1}
m, i ∈ [1, n]
m) :
• Ones ← {j ∈ [1, m] : bj = 1}
• Zeros ← {j ∈ [1, m] : bj = 0}
• p
+ ←
Q
j∈Ones pi[j]
; p
− ←
Q
j∈Zeros pi[j]
• πI ← A.MemWitCreate*(p
+)
• πE ← A.NonMemWitCreate*(p
−)
• return {πI , πE}
VC.BatchVerify(A, b, i, πI , πE) :
• Ones ← {j ∈ [1, m] : bj = 1}
• Zeros ← {j ∈ [1, m] : bj = 0}
• p
+ ←
Q
j∈Ones pi[j]
; p
− ←
Q
j∈Zeros pi[j]
• return A.VerMem(p
+, πI ) ∧
A.VerNonMem*(p
−, πE)
VC.BatchOpen∗
(b ∈ {0, 1}
m, i ∈ [1, n]
m) :
• Ones ← {j ∈ [1, m] : bj = 1}
• Zeros ← {j ∈ [1, m] : bj = 0}
• p
+ ←
Q
j∈Ones pi[j]
; p
− ←
Q
j∈Zeros pi[j]
• πI ← A.MemWitCreate*(p
+)
• πE ← A.NonMemWitCreate(p
−)
• return {πI , πE}
VC.BatchVerify∗
(A, b, i, πI , πE) :
• Ones ← {j ∈ [1, m] : bj = 1}
• Zeros ← {j ∈ [1, m] : bj = 0}
• p
+ ←
Q
j∈Ones pi[j]
; p
− ←
Q
j∈Zeros pi[j]
• return A.VerMem(p
+, πI ) ∧
A.VerNonMem(p
−, πE)

Figure 4: Vector commitment scheme from accumulator with batchable membership
and non-membership witnesses.

Both the accumulator’s CRS as well as PrimeGen can be represented in constant
space independent of n. This means that the public parameters for the vector commitment are also constant-size and independent of n, unlike all previous vector commitments with O(1) size openings [CF13, LRY16, LM18]. The batch opening of several (mixed value) indices consists of 2 elements in G for the aggregate membershipwitness and an additional 5 elements in G for the batch non-membership witness,
plus one λ-bit integer.

Aggregating Openings Just as for our accumulator construction we can aggregate vector commitment openings. The aggregation does not require knowledge of
the vector contents and the running time of the aggregation is independent of the
length of the vector. The opening of a bit in the vector commitment consists of an
accumulator inclusion proof and an exclusion proof, both of which we can aggregate
as shown in Section 4.2.

This aggregation protocol works for outputs of VC.Open, but unfortunately it
does not extend to outputs of VC.BatchOpen. The latter contain PoKE proofs created
by VerNonMem*, which would somehow need to be aggregated as well along with
their inputs. When opening only a small number of bit indices, say in a 256-
bit component of the vector, VC.BatchOpen∗
could be used instead so that these
openings can be later aggregated. While the output size of VC.BatchOpen∗
grows
linearly in m, the number of batched indices, it still contains only three group
elements and an integer whose size is proportional to the product of at most m
λ-bit primes. These are the unique primes associated with indices of the vector and
heuristically can be chosen to be much smaller than λ bits, i.e. closer to log n bits.
After the aggregation step is completed, the aggregate non-membership witness can
be further compressed with a PoKE proof.

This aggregation protocol is important for the account-based stateless blockchain
application, described in more detail in Section 6. In this application, there is a
distributed network of participants who who each hold openings for only a partial
number of the vector components (e.g. every user knows the value corresponding
to their own account data). A batch of transactions will typically contain many
openings (of small values) produced by many different participants in the network.
In this case, it makes sense for the participants to each produce an opening of the
form VC.BatchOpen∗
so that after the individual participants post all the openings
they can be aggregated into a single constant size value that is persisted in the
transaction log.

Optimization The number of group elements can be reduced by utilizing a
PoKCR for all of the PoE and PoKE roots involved in the membership/nonmembership witness generation. It is important that all PoE and PoKE protocols
use different challenges. These challenges are then guaranteed to be co-prime. This
reduces the number of opening proof elements to 4 ∈ G and 1 λ-bit integer.

5.3 Comparison

Table 5.3 compares the performance of our new VC scheme, the Catalano-Fiore
(CF)[CF13] RSA-based VC scheme, and Merkle trees. The table assumes the VC
input is a length n vector of k bit elements with security parameter λ. The performance for the CF scheme include batch openings which were introduced by Lai
and Malatova[LM18]. We note that the MultiExp algorithm from Section 3.3 also
applies to the CF scheme. In particular it can improve the Setup and Open time.
The comparison reflects these improvements.

Metric This Work Catalono-Fiore [CF13, LM18] Merkle Tree
Setup
Setup O(1) O(n · log(n) · λ) G O(1)
|pp| O(1) O(n) G O(1)
Com(m) → c O(n · log(n) · k) G O(n · k) G O(n) H
|c| 1 G 1 G 1 |H|
Proofs
Open(mi
, i) → π O(k · n log n) G O(n · (k + λ)) G. O(log n) H
Verify(mi
, i, π) O(λ) G+ k · log n F O(k + λ) G O(log n) H
|π| O(1) |G| 1 |G| O(log n) |H|
Open(m, i) → πi O(k · n log n) G O(n log n · (k + λ)) G O(n log n) H
Verify(m, i, πi) O(λ) G + O(k · n log n) F O(nk) G O(n log n) H
|πi
| 4 |G|+ λ 1 |G| O(n log n) |H|
Aggregatable Yes No No

Table 1: Comparison between Merkle trees, Catalano-Fiore RSA VCs and the new
VCs presented in this work. The input m is a vector of n. G refers to a group
operation in G, F to a multiplication in a field of size roughly 2λ
, and H to a hash
operation. Group operations are generally far more expensive than hashes which
are more expensive than multiplication in F. |G| is the size of a hidden order group
element and |H| is the size of a hash output.

5.4 Key-Value Map Commitment

Our vector-commitment can be used to build a commitment to a key-value map. A
key-value map can be built from a sparse vector. The key-space is represented by
positions in the vector and the associated value is the data at the keys position. The
vector length is exponential in the key length and most positions are zero (null).
Our VC commitment naturally supports sparse vectors because the complexity of
the commitment is proportional to the number of bit indices that are set to 1, and
otherwise independent of the vector length.

Optimization with honest updates In order to commit to arbitrary length
values we can hash the value and then commit to the resulting hash. Unfortunately
this still requires setting λ bits in the vector commitment which corresponds to
adding λ, λ-bit primes to the underlying accumulator. This can make updating the
commitment computationally quite expensive. In some settings we can do better
than this. Note that the VC and the accumulator definitions (Definition 6) assume
that the adversary outputs the commitment. This requirement is too strong for
settings where every update follows the rules of the system, i.e. is performed by the
challenger. In this case we can implement a key-value map commitment by storing
in the VC which keys exist and storing in the accumulator a key, value tuple. If
the key already exists then an update will update the entry in the accumulator.
Otherwise it will add an entry to the accumulator and set the corresponding VC bit
to 1. The construction requires only 1 bit to be stored per key in the VC and 1 entry
in the accumulator. The construction also is not secure if the adversary can output
an accumulator value as it could contain multiple entries for the same key. We omit
a formal security definition and proof but note that security follows directly from
the binding guarantees of the underlying accumulator and vector commitment constructions. The VC ensures that each key appears at most ones in the accumulator
and the accumulator ensures the integrity of the committed data.

6 Applications

6.1 Stateless Blockchains

UTXO commitment We first consider a simplified blockchain design which
closely corresponds to Bitcoin’s UTXO design where users own coins and issue
transaction by spending old coins and creating new coins. We call the set of unspent coins the UTXO set. Updates to the blockchain can be viewed as asynchronous updates to the UTXO set. In most current blockchain designs (with some
exceptions[MGGR13a, BCG+14]) nodes participating in transaction validation store
the whole UTXO set and use it to verify whether a coin was unspent. Instead, we
consider a blockchain design where the network maintains the UTXO set in a dynamic accumulator [STS99a, TMA13, Tod16, Dra]. We instantiate this accumulator
with our new construction from Section 4.1, taking advantage of our distributed
batch updates and aggregate membership proofs.

Each transaction block will contain an accumulator state, which is a commitment to the current UTXO set. To spend a coin, a user provides a membership
witness for the coin (UTXO) that is being spent inside a transaction. Any validator
(aka miner) may verify the transactions against the latest accumulator state and
also uses BatchDel to delete all spent coins from the accumulator, derive its new
state, and output a proof of correctness for the deletions. The proof is propagated
to other validators in the network. For the newly minted coins, the validator uses
BatchAdd to add them to the accumulator and produce a second proof of correctness to propagate. Other validators are able to verify that the accumulator was
updated correctly using only a constant number of group operations and highly
efficient arithmetic over λ-bit integers.

In this design, users store the membership witnesses for their own coins and are
required to update their witnesses with every block of transactions. It is plausible
that users use third-party services to help with this maintenance. These services
are not trusted for integrity, but only for availability. Note that a may produce
many (e.g. n) membership witnesses at once in O(n log(n)) time using the CreateAllMemWit algorithm

Accounts commitment Some currencies such as Ethereum [Woo14] or Stellar
[SYB14] use an account-based system where the state is a key-value map. A transaction updates the balances of the sending and the receiving accounts. To enable
stateless validation in this setting, a user can provide proofs of the balances of the
sending and receiving accounts in the current ledger state. Instead of using an accumulator to commit to this state, we use the new key-value map commitment from
Section 5.4. This commitment supports batch distributed updates, similar to our
new accumulator. Using the aggregation of vector commitment openings a miner
or validator can perform the aggregation and batching operations without storing
the state providing efficient proofs that the openings are correct. Other nodes can
verify these opening proofs efficiently requiring only a constant number of group
operations.

6.2 Short IOPs

Merkle tree paths contribute significant overhead to both the proof size of a compiled
IOP proof and its verification time. Vector commitments with smaller openings than
Merkle trees, or batchable openings (i.e. subvector commitments), can help reduce
this overhead [LM18]. Using our new VCs, the opening proof for each round of the
compiled IOP is just 4 group elements in G and a λ-bit integer (plus one additional
element for the VC commitment itself). Instantiating G with a class group of
quadratic imaginary order and tuning security to 128-bits requires elements of size
approximately 2048-bits [HM00]. Thus, the VC openings contribute 8320 bits to
the proof size per IOP round. When applied to the “CS-proof” SNARK considered
by Lai and Malavolta, which is based on a theoretical PCP that checks 3 bits per
query and has 80 queries, the proof size is 5 · 2048 + 128 + 3 · 80 = 10608 bits, or
1.3 KB. This is the shortest (theoretical) setup-free SNARK with sublinear public
parameters to date.

Our VCs also achieve concrete improvements to practical IOPs. Targeting 100-
bit security in the VC component and otherwise apples-to-apples comparisons with
benchmarks for Aurora [BSCR+18] and STARKS [BBHR18], we can conservatively
use 2048-bit class group elements. With these parameters, our VCs reduce the size
of the Aurora proofs on a 220 size circuit from 222 KB to less than 100 KB, a 54%
reduction, and the size of STARK proofs for a circuit of 252 gates from 600 KB
to approximately 222 KB, a 63% reduction. This rough estimate is based on the
Merkle path length 42 and round number 21 extrapolated from the most recent
STARK benchmarks for this size circuit [BBHR18].

Replacing Merkle trees with our VCs does not significantly impact the verification cost, and in some cases it may even improve verification time. Recall that
verifying a batch VC proof costs approximately one lamdba-bit integer multiplication and a primality check per bit. Furthermore, using the optimization described
in Section 7 eliminates the primality checks for the verifier (at a slight cost to the
prover). Computing a SHA256 hash function (whether SHA256 or AES with DaviesMeyer) is comparable to the cost of a λ-bit integer multiplication. Thus, as a loose
estimate, replacing each Merkle path per query with a single λ-bit multiplication
would achieve a factor log n = 36 reduction. In STARKS, Merkle paths are constructed over 256-bit blocks of the proof rather than bits, thus the comparison is
36 hashes vs 256 modular multiplications. The Merkle path validation accounts for
80% of the verification time.

While using our vector commitment has many benefits for IOPs, there are several
sever downsides. Our vector commitment is not quantum secure as a quantum
computer can find the order of the group and break the Strong-RSA assumption.
Merkle trees are more plausibly quantum secure. Additionally, the prover for an
IOP instantiated with our vector commitment would be significantly slower than
one with a Merkle tree.

7 Hashing To Primes

Our constructions use a hash-function with prime domains in several places: Elements in the accumulator are mapped to primes, using a collision resistant hash
function with prime domain. The vector commitment associates a unique prime with
each index. All of the proofs presented in Section 3 use a random prime as a challenge. When the proofs are made non-interactive, using the Fiat-Shamir heuristic
the challenge is generated by hashing the previous transcript.

In Section 4.1 we present a simple algorithm for a collision-resistant hash function Hprime with prime-domain built from a collision resistant hash function H with
domain Z2
λ . The hash function iteratively hashes a message and a counter, increasing the counter until the output is prime. If we model H as a random function with
then the expected running time of Hprime is O(λ). This is because there are O(
n
log(n)
)
primes below n.

The problem of hashing to primes has been studied in several contexts: Cramer
and Shoup [CS99] provide a way to generate primes with efficiently checkable certificates. Fouque and Tibouchi[FT14] showed how to quickly generate random primes.
Seeding the random generation with a collision resistant hash function can be used
to generate an efficient hash function with prime domain. Despite these improvements, the hash function actually introduces a significant overhead for verification
and in this section we present several techniques how the hashing can be further
sped up.

PoE,PoKE proofs We first investigate the PoE,PoKE family of protocols. In the
non-interactive variant the challenge ` is generated by hashing the previous transcript to a prime. The protocol can be modified by having the prover provide a
short nonce such that ` ← H(transcript||nonce) with ` ∈ Primes(λ). In expectation the nonce is just log(λ) bits and with overwhelming probability it is less than
2 log(λ) bits. This modification allows the adversary to produce different challenges
for the same transcript. However it does not increase an adversary’s advantage.
The prover can always alter the input to generate new challenges. By changing the
nonce the prover can grind a polynomial number of challenges but the soundness
error in all of our protocols is negligible. The change improves the verification as
the verifier only needs to do a single primality check instead of λ. The change is
particularly interesting if proof verification is done in a circuit model of computation, where variable time operations are difficult and costly to handle. Circuit
computations have become increasingly popular for general purpose zero-knowledge
proofs[GGPR13, BBB+18, BSCR+18]. Using the adapted protocol verification becomes a constant time operation which uses only a single primality check.

Accumulator A similar improvement can be applied to accumulators. The users
can provide a nonce such that element||nonce is accumulated instead of just the
element. This of course allows an adversary to accumulate the same element twice.
In some applications this is acceptable. In other applications such as stateless
blockchains it is guaranteed that no element is accumulated twice(see Section 6).
One way to guarantee uniqueness is to commit to the current state of the accumulator for every added element. In an inclusion proof, the prover would provide the
nonce as part of the proof. The verifier now only does a single primality check to
ensure that H(element||nonce) is indeed prime. This stands in contrast to O(λ)
primality checks if Hprime is used. The nonce construction prohibits efficient exclusion proofs but these are not required in some applications, such as the blockchain
application.

Vector Commitments The vector commitment construction uses one prime per
index to indicate whether the vector is 1 at that index or 0. The security definition
for a vector commitment states that a secure vector commitment cannot be opened
to two different openings at the same index. In our construction this would involve
giving both an inclusion as well as an exclusion proof for a prime in an accumulator,
which is impossible if the accumulator itself is secure. Using a prime for each index
again requires using a collision resistant hash function with prime domain which
uses O(λ) primality checks or an injective function which runs in time O(log(n)
2
),
where n is the length of the vector. What if instead of accumulating a prime for
each index we accumulate a random λ bit number at each index? The random
number could simply be the hash of the index. Is this construction still secure?
First consider the case where each index’s number has a unique prime factor. This
adapted construction is trivially still secure. What, however, if xk, associated with
index k, is the product of xi and xj . Then accumulating xi and xj lets an adversary
also give an inclusion proof for xk. Surprisingly, this does still not break security.
While it is possible to give an inclusion proof for xk, i.e. open the vector at index k
to 1 it is suddenly impossible to give an exclusion proof for xk, i.e. open the vector
at index k to 0. The scenario only breaks the correctness property of the scheme,
in that it is impossible to commit to a vector that is 1 at i and j but 0 at k. In a
setting, where the vector commitment is used as a static commitment to a vector,
correctness only needs to hold for the particular vector that is being committed
to. In the IOP application, described in Section 6.2, the prover commits to a long
proof using a vector commitment. If these correctness failures only happen for few
vectors, it may still be possible to use the scheme. This is especially true because
in the IOP application the proof and also the proof elements can be modified by
hashing the proof elements along with a nonce. A prover would modify the nonces
until he finds a proof, i.e. a vector that he can commit to. To analyze the number of
correctness failures we can compute the probability that a k-bit element divides the
product of n k-bit random elements. Fortunately, this question has been analyzed
by Coron and Naccache[CN00] with respect to the Gennaro-Halevi-Rabin Signature
Scheme[GHR99]. They find that for 50 Million integers and 256-bit numbers the
probability that even just a single correctness failure occurs is 1%. Furthermore
we find experimentally that for 220 integers and 80-bit numbers only about 8, 000
integers do not have a unique prime factor. Thus, any vector that is 1 at these 8, 000
positions can be committed to using just 80-bit integers. Our results suggest that
using random integer indices instead of prime indices can be useful, if a) perfect
completeness is not required b) primality checks are a major cost to the verifier.

8 Conclusion

We expect that our techniques and constructions will have more applications beyond
what was discussed. Several interesting open questions remain: What practical
limitations occur when deploying the scheme? Is it possible to efficiently compute
unions of accumulators? This is certainly true for Merkle trees but these do not
have the batching properties and constant size of RSA accumulators. Similarly can
one build an accumulator with constant sized witnesses from a quantum resistant
assumption? Additionally, we hope that this research motivates further study of
class groups as a group of unknown order.

Acknowledgments

This work was partially supported by NSF, ONR, the Simons Foundation and the
ZCash foundation. We thank Dario Fiore and Oliver Tran for pointing out typos
and small errors.

References

[ABC+12] Jae Hyun Ahn, Dan Boneh, Jan Camenisch, Susan Hohenberger, abhi
shelat, and Brent Waters. Computing on authenticated data. In
25
Ronald Cramer, editor, TCC 2012, volume 7194 of LNCS, pages 1–20.
Springer, Heidelberg, March 2012.
[AHIV17] Scott Ames, Carmit Hazay, Yuval Ishai, and Muthuramakrishnan
Venkitasubramaniam. Ligero: Lightweight sublinear arguments without a trusted setup. In Bhavani M. Thuraisingham, David Evans, Tal
Malkin, and Dongyan Xu, editors, ACM CCS 17, pages 2087–2104.
ACM Press, October / November 2017.
[BBB+18] Benedikt B¨unz, Jonathan Bootle, Dan Boneh, Andrew Poelstra, Pieter
Wuille, and Greg Maxwell. Bulletproofs: Short proofs for confidential
transactions and more. In 2018 IEEE Symposium on Security and
Privacy, pages 315–334. IEEE Computer Society Press, May 2018.
[BBBF18] Dan Boneh, Joseph Bonneau, Benedikt B¨unz, and Ben Fisch. Verifiable delay functions. In Hovav Shacham and Alexandra Boldyreva,
editors, CRYPTO 2018, Part I, volume 10991 of LNCS, pages 757–788.
Springer, Heidelberg, August 2018.
[BBF18] Dan Boneh, Benedikt B¨unz, and Ben Fisch. A survey of two verifiable
delay functions. Cryptology ePrint Archive, Report 2018/712, 2018.
https://eprint.iacr.org/2018/712.
[BBHR18] Eli Ben-Sasson, Iddo Bentov, Yinon Horesh, and Michael Riabzev.
Scalable, transparent, and post-quantum secure computational integrity. Cryptology ePrint Archive, Report 2018/046, 2018. https:
//eprint.iacr.org/2018/046.
[BCD+17] Foteini Baldimtsi, Jan Camenisch, Maria Dubovitskaya, Anna Lysyanskaya, Leonid Reyzin, Kai Samelin, and Sophia Yakoubov. Accumulators with applications to anonymity-preserving revocation. Cryptology
ePrint Archive, Report 2017/043, 2017. http://eprint.iacr.org/
2017/043.
[BCG+14] Eli Ben-Sasson, Alessandro Chiesa, Christina Garman, Matthew
Green, Ian Miers, Eran Tromer, and Madars Virza. Zerocash: Decentralized anonymous payments from bitcoin. In 2014 IEEE Symposium on Security and Privacy, pages 459–474. IEEE Computer Society
Press, May 2014.
[BCK10] Endre Bangerter, Jan Camenisch, and Stephan Krenn. Efficiency limitations for S-protocols for group homomorphisms. In Daniele Micciancio, editor, TCC 2010, volume 5978 of LNCS, pages 553–571. Springer,
Heidelberg, February 2010.
[BCM05] Endre Bangerter, Jan Camenisch, and Ueli Maurer. Efficient proofs of
knowledge of discrete logarithms and representations in groups with
hidden order. In Serge Vaudenay, editor, PKC 2005, volume 3386 of
LNCS, pages 154–171. Springer, Heidelberg, January 2005.
[BCS16] Eli Ben-Sasson, Alessandro Chiesa, and Nicholas Spooner. Interactive
oracle proofs. In Martin Hirt and Adam D. Smith, editors, TCC 2016-
B, Part II, volume 9986 of LNCS, pages 31–60. Springer, Heidelberg,
October / November 2016.
[Bd94] Josh Cohen Benaloh and Michael de Mare. One-way accumulators: A
decentralized alternative to digital sinatures (extended abstract). In
Tor Helleseth, editor, EUROCRYPT’93, volume 765 of LNCS, pages
274–285. Springer, Heidelberg, May 1994.
[BH01] Johannes Buchmann and Safuat Hamdy. A survey on iq cryptography.
In Public-Key Cryptography and Computational Number Theory, pages
1–15, 2001.
26
[BLL00] Ahto Buldas, Peeter Laud, and Helger Lipmaa. Accountable certificate
management using undeniable attestations. In S. Jajodia and P. Samarati, editors, ACM CCS 00, pages 9–17. ACM Press, November 2000.
[BP97] Niko Bari and Birgit Pfitzmann. Collision-free accumulators and failstop signature schemes without trees. In Walter Fumy, editor, EUROCRYPT’97, volume 1233 of LNCS, pages 480–494. Springer, Heidelberg, May 1997.
[BSCG+14] Eli Ben-Sasson, Alessandro Chiesa, Christina Garman, Matthew
Green, Ian Miers, Eran Tromer, and Madars Virza. Zerocash: Decentralized anonymous payments from Bitcoin. In IEEE Symposium
on Security and Privacy, 2014.
[BSCR+18] Eli Ben-Sasson, Alessandro Chiesa, Michael Riabzev, Nicholas
Spooner, Madars Virza, and Nicholas P. Ward. Aurora: Transparent succinct arguments for r1cs. Cryptology ePrint Archive, Report
2018/828, 2018. https://eprint.iacr.org/2018/828.
[CF13] Dario Catalano and Dario Fiore. Vector commitments and their applications. In Kaoru Kurosawa and Goichiro Hanaoka, editors, PKC 2013,
volume 7778 of LNCS, pages 55–72. Springer, Heidelberg, February / March 2013.
[CHKO08] Philippe Camacho, Alejandro Hevia, Marcos A. Kiwi, and Roberto
Opazo. Strong accumulators from collision-resistant hashing. In
Tzong-Chen Wu, Chin-Laung Lei, Vincent Rijmen, and Der-Tsai Lee,
editors, ISC 2008, volume 5222 of LNCS, pages 471–486. Springer,
Heidelberg, September 2008.
[CJ10] S´ebastien Canard and Amandine Jambert. On extended sanitizable
signature schemes. In Josef Pieprzyk, editor, CT-RSA 2010, volume
5985 of LNCS, pages 179–194. Springer, Heidelberg, March 2010.
[CKS09] Jan Camenisch, Markulf Kohlweiss, and Claudio Soriente. An accumulator based on bilinear maps and efficient revocation for anonymous
credentials. In Stanislaw Jarecki and Gene Tsudik, editors, PKC 2009,
volume 5443 of LNCS, pages 481–500. Springer, Heidelberg, March
2009.
[CL02] Jan Camenisch and Anna Lysyanskaya. Dynamic accumulators and
application to efficient revocation of anonymous credentials. In Moti
Yung, editor, CRYPTO 2002, volume 2442 of LNCS, pages 61–76.
Springer, Heidelberg, August 2002.
[CN00] Jean-S´ebastien Coron and David Naccache. Security analysis of the
Gennaro-Halevi-Rabin signature scheme. In Bart Preneel, editor, EUROCRYPT 2000, volume 1807 of LNCS, pages 91–101. Springer, Heidelberg, May 2000.
[CPZ18] Alexander Chepurnoy, Charalampos Papamanthou, and Yupeng
Zhang. Edrax: A cryptocurrency with stateless transaction validation. Cryptology ePrint Archive, Report 2018/968, 2018. https:
//eprint.iacr.org/2018/968.
[CS99] Ronald Cramer and Victor Shoup. Signature schemes based on the
strong RSA assumption. Cryptology ePrint Archive, Report 1999/001,
1999. http://eprint.iacr.org/1999/001.
[DK02] Ivan Damg˚ard and Maciej Koprowski. Generic lower bounds for root
extraction and signature schemes in general groups. In Lars R. Knudsen, editor, EUROCRYPT 2002, volume 2332 of LNCS, pages 256–271.
Springer, Heidelberg, April / May 2002.
27
[Dra] Justin Drake. Accumulators, scalability of utxo blockchains,
and data availability. https://ethresear.ch/t/
accumulators-scalability-of-utxo-blockchains-and-data-availability/
176.
[DT08] Ivan Damg˚ard and Nikos Triandopoulos. Supporting non-membership
proofs with bilinear-map accumulators. Cryptology ePrint Archive,
Report 2008/538, 2008. http://eprint.iacr.org/2008/538.
[FS87] Amos Fiat and Adi Shamir. How to prove yourself: Practical solutions to identification and signature problems. In Andrew M. Odlyzko,
editor, CRYPTO’86, volume 263 of LNCS, pages 186–194. Springer,
Heidelberg, August 1987.
[FT14] Pierre-Alain Fouque and Mehdi Tibouchi. Close to uniform prime
number generation with fewer random bits. In Javier Esparza, Pierre
Fraigniaud, Thore Husfeldt, and Elias Koutsoupias, editors, ICALP
2014, Part I, volume 8572 of LNCS, pages 991–1002. Springer, Heidelberg, July 2014.
[FVY14] Conner Fromknecht, Dragos Velicanu, and Sophia Yakoubov. A decentralized public key infrastructure with identity retention. Cryptology
ePrint Archive, Report 2014/803, 2014. http://eprint.iacr.org/
2014/803.
[GGM14] Christina Garman, Matthew Green, and Ian Miers. Decentralized
anonymous credentials. In NDSS 2014. The Internet Society, February
2014.
[GGPR13] Rosario Gennaro, Craig Gentry, Bryan Parno, and Mariana Raykova.
Quadratic span programs and succinct NIZKs without PCPs.
In Thomas Johansson and Phong Q. Nguyen, editors, EUROCRYPT 2013, volume 7881 of LNCS, pages 626–645. Springer, Heidelberg, May 2013.
[GHR99] Rosario Gennaro, Shai Halevi, and Tal Rabin. Secure hash-and-sign
signatures without the random oracle. In Jacques Stern, editor, EUROCRYPT’99, volume 1592 of LNCS, pages 123–139. Springer, Heidelberg, May 1999.
[Gro16] Jens Groth. On the size of pairing-based non-interactive arguments. In
Marc Fischlin and Jean-S´ebastien Coron, editors, EUROCRYPT 2016,
Part II, volume 9666 of LNCS, pages 305–326. Springer, Heidelberg,
May 2016.
[HM00] Safuat Hamdy and Bodo M¨oller. Security of cryptosystems based on
class groups of imaginary quadratic orders. In Tatsuaki Okamoto,
editor, ASIACRYPT 2000, volume 1976 of LNCS, pages 234–247.
Springer, Heidelberg, December 2000.
[Kil92] Joe Kilian. A note on efficient zero-knowledge proofs and arguments
(extended abstract). In 24th ACM STOC, pages 723–732. ACM Press,
May 1992.
[Lip12] Helger Lipmaa. Secure accumulators from euclidean rings without
trusted setup. In Feng Bao, Pierangela Samarati, and Jianying Zhou,
editors, ACNS 12, volume 7341 of LNCS, pages 224–240. Springer,
Heidelberg, June 2012.
[LLX07] Jiangtao Li, Ninghui Li, and Rui Xue. Universal accumulators with
efficient nonmembership proofs. In Jonathan Katz and Moti Yung,
editors, ACNS 07, volume 4521 of LNCS, pages 253–269. Springer,
Heidelberg, June 2007.
28
[LM18] Russell W.F. Lai and Giulio Malavolta. Optimal succinct arguments
via hidden order groups. Cryptology ePrint Archive, Report 2018/705,
2018. https://eprint.iacr.org/2018/705.
[LRY16] Benoˆıt Libert, Somindu C. Ramanna, and Moti Yung. Functional
commitment schemes: From polynomial commitments to pairing-based
accumulators from simple assumptions. In Ioannis Chatzigiannakis,
Michael Mitzenmacher, Yuval Rabani, and Davide Sangiorgi, editors,
ICALP 2016, volume 55 of LIPIcs, pages 30:1–30:14. Schloss Dagstuhl,
July 2016.
[LY10] Benoˆıt Libert and Moti Yung. Concise mercurial vector commitments
and independent zero-knowledge sets with short proofs. In Daniele
Micciancio, editor, TCC 2010, volume 5978 of LNCS, pages 499–517.
Springer, Heidelberg, February 2010.
[Mer88] Ralph C. Merkle. A digital signature based on a conventional encryption function. In Carl Pomerance, editor, CRYPTO’87, volume 293 of
LNCS, pages 369–378. Springer, Heidelberg, August 1988.
[MGGR13a] Ian Miers, Christina Garman, Matthew Green, and Aviel D. Rubin.
Zerocoin: Anonymous distributed E-cash from Bitcoin. In 2013 IEEE
Symposium on Security and Privacy, pages 397–411. IEEE Computer
Society Press, May 2013.
[MGGR13b] Ian Miers, Christina Garman, Matthew Green, and Aviel D Rubin. Zerocoin: Anonymous Distributed E-Cash from Bitcoin. In IEEE Symposium on Security and Privacy, 2013.
[Mic94] Silvio Micali. CS proofs (extended abstracts). In 35th FOCS, pages
436–453. IEEE Computer Society Press, November 1994.
[Ngu05] L. Nguyen. Accumulators from bilinear maps and applications. CTRSA, 3376:275–292, 2005.
[NN98] Kobbi Nissim and Moni Naor. Certificate revocation and certificate
update. In Usenix, 1998.
[PS14] Henrich Christopher P¨ohls and Kai Samelin. On updatable redactable
signatures. In Ioana Boureanu, Philippe Owesarski, and Serge Vaudenay, editors, ACNS 14, volume 8479 of LNCS, pages 457–475. Springer,
Heidelberg, June 2014.
[Sha83] Adi Shamir. On the generation of cryptographically strong pseudorandom sequences. ACM Transactions on Computer Systems (TOCS),
1(1):38–44, 1983.
[Sho97] Victor Shoup. Lower bounds for discrete logarithms and related problems. In Walter Fumy, editor, EUROCRYPT’97, volume 1233 of
LNCS, pages 256–266. Springer, Heidelberg, May 1997.
[Sla12] Daniel Slamanig. Dynamic accumulator based discretionary access
control for outsourced storage with unlinkable access - (short paper).
In Angelos D. Keromytis, editor, FC 2012, volume 7397 of LNCS,
pages 215–222. Springer, Heidelberg, February / March 2012.
[STS99a] Tomas Sander and Amnon Ta-Shma. Auditable, anonymous electronic
cash. In Michael J. Wiener, editor, CRYPTO’99, volume 1666 of
LNCS, pages 555–572. Springer, Heidelberg, August 1999.
[STS99b] Tomas Sander and Amnon Ta-Shma. Flow control: A new approach
for anonymity control in electronic cash systems. In Matthew Franklin,
editor, FC’99, volume 1648 of LNCS, pages 46–61. Springer, Heidelberg, February 1999.
29
[STSY01] Tomas Sander, Amnon Ta-Shma, and Moti Yung. Blind, auditable
membership proofs. In Yair Frankel, editor, FC 2000, volume 1962 of
LNCS, pages 53–71. Springer, Heidelberg, February 2001.
[SYB14] David Schwartz, Noah Youngs, and Arthur Britto. The Ripple Protocol
Consensus Algorithm, September 2014.
[TMA13] Peter Todd, Gregory Maxwell, and Oleg Andreev. Reducing
UTXO: users send parent transactions with their merkle branches.
bitcointalk.org, October 2013.
[Tod16] Peter Todd. Making UTXO Set Growth Irrelevant With LowLatency Delayed TXO Commitments . https://petertodd.org/
2016/delayed-txo-commitments, May 2016.
[TW12] Bj¨orn Terelius and Douglas Wikstr¨om. Efficiency limitations of Sprotocols for group homomorphisms revisited. In Ivan Visconti and
Roberto De Prisco, editors, SCN 12, volume 7485 of LNCS, pages
461–476. Springer, Heidelberg, September 2012.
[Wes18] Benjamin Wesolowski. Efficient verifiable delay functions. Cryptology
ePrint Archive, Report 2018/623, 2018. https://eprint.iacr.org/
2018/623.
[Woo14] Gavin Wood. Ethereum: A secure decentralized transaction ledger.
http://gavwood.com/paper.pdf, 2014.
30

A PoE/PoKE Generalizations and Zero Knowledge

A.1 A succinct proof of homomorphism preimage

We observe that the protocol PoE can be generalized to a relation for any homomorphism φ : Z
n → G for which the adaptive root assumption holds in G. Specifically,
Protocol PoHP below is a protocol for the relation:
Rφ,PoHP =
 (w ∈ G, x ∈ Z
n
); ⊥
 
: w = φ(x) ∈ G
	
.

This generalization will be useful in our applications.

Protocol PoHP (Proof of homomorphism preimage) for Rφ,PoHP
Params: G
$
← GGen(λ), φ : Z
n → G; Inputs: x ∈ Z
n
, w ∈ G; Claim:
φ(x) = w
1. Verifier sends `
$
← Primes(λ).
2. For i = 1, . . . , n: Prover finds integers qi and ri ∈ [`] s.t. xi = qi` + ri
.
Let q ← (q1, ..., qn) ∈ Z
n and r ← (r1, ..., rn) ∈ [`]
n
.
Prover sends Q ← φ(q) ∈ G to Verifier.
3. Verifier computes ri = (xi mod `) ∈ [`] for all i = 1, . . . , n, sets r =
(r1, . . . , rn), and accepts if Q`φ(r) = w holds in G.

Theorem 6 (Soundness PoHP). Protocol PoHP is an argument system for Relation Rφ,PoHP with negligible soundness error, assuming the adaptive root assumption
holds for GGen.

Proof. Suppose that φ(x) 6= w, but the adversary succeeds in making the verifier
accept with non-negligible probability. Let q and r be as defined in step (2) of
the protocol and let Q be the prover’s message to the verifier. Then [Q/φ(q)]` =
[w/φ(r)]/[φ(x)/φ(r)] = w/φ(x) 6= 1. We thus obtain an algorithm to break the
adaptive root assumption for the instance ˆw := w/φ(x) by interacting with the
adversary, giving it the adaptive root challenge `, and outputting ˆu := Q/φ(q) ∈ G,
where Q is the value output by the adversary.

A.2 A succinct proof of knowledge of a homomorphism preimage

The PoKE argument of knowledge can be extended to an argument of knowledge
for the pre-image of a homomorphism φ : Z
n → G.
Rφ =
 w ∈ G; x ∈ Z
n
 
: w = φ(x) ∈ G
	
.

For a general homomorphism φ we run into the same extraction challenge that
we encountered in extending Protocol PoKE∗
to work for general bases. The solution
for Protocol PoKE was to additionally send g
x where g is either a base in the CRS
or chosen randomly by the verifier and execute a parallel PoKE for g 7→ g
x
. We
can apply exactly the same technique here on each component xi of the witness,
i.e. send g
xi to the verifier and execute a parallel PoKE that g 7→ g
xi
. This
allows the extractor to obtain the witness x, and the soundness of the protocol then
follows from the soundness of Protocol PoHP. However, as an optimization to reduce
the communication we can instead use the group representation homomorphism
Rep : Z
n → G defined as
Rep(x) = Yn
i=1
g
xi
i
for base elements gi defined in the CRS. The prover sends Rep(x) in its first message,
which is a single group element independent of n.
Protocol PoKHP (Proof of knowledge of homomorphism preimage)
Params: G
$
← GGen(λ), (g1, ..., gn) ∈ Gn
, φ : Z
n → G; Inputs: w ∈ G;
Witness: x ∈ Z; Claim: φ(x) = w

1. Prover sends z = Rep(x) = Q
i
g
xi
i ∈ G to the verifier.
2. Verifier sends `
$
← Primes(λ).
3. For each xi
, Prover computes qi
, ri s.t. xi = qi`+ri
, sets q ← (q1, ..., qn) ∈
Z
n and r ← (r1, ..., rn) ∈ [`]
n
. Prover sends Qφ ← φ(q) ∈ G, QRep ←
Rep(q) ∈ G, and r to Verifier.
4. Verifier accepts if r ∈ [`]
n
, Q`
φ
φ(r) = w, and Q`
RepRep(r) = z.

In order to analyze the security of this protocol, it is helpful to first consider
a special case of Protocol PoKHP protocol for the homomorphism Rep : Z
n → G,
which is a generalization of Protocol PoKE∗
. In this case the prover of course does
not need to separately send Rep(x) in the first message. The protocol is as follows:
Protocol PoKRep (Proof of knowledge of representation)
Params: G
$
← GGen(λ),(g1, ..., gn) ∈ Gn
; Inputs: w ∈ G; Witness: x ∈ Z;
Claim: Rep(x) = Qn
i=1 g
xi
i = w
1. Verifier sends `
$
← Primes(λ).
2. For each xi
, Prover finds qi
, ri s.t. xi = qi` + ri
, sets q ← (q1, ..., qn) ∈ Z
n
and r ← (r1, ..., rn) ∈ [`]
n
. Prover sends Q ← Rep(q) = Q
i
g
qi
i ∈ G and r
to Verifier.
3. Verifier accepts if r ∈ [`]
n
, Q`Rep(r) = w.
The following theorems prove security of the two protocols above.

Theorem 7 (PoKRep Argument of Knowledge). Protocol PoKRep is an argument
of knowledge for relation RRep in the generic group model.
Proof. See Appendix C.

Theorem 8 (PoKHP Argument of Knowledge). Protocol PoKHP is an argument
of knowledge for the relation Rφ in the generic group model.
Proof. See Appendix C.

A.3 A succinct proof of integer exponent mod n

There are several applications of accumulators that require proving complex
statements about integer values committed in an accumulator (e.g. [BSCG+14,
MGGR13b]). Practical succinct argument systems (SNARGs/SNARKs/STARKs)
operate on statements defined as an arithmetic circuit, and the prover efficiency
scales with the multiplication complexity of the statement. Since RSA accumulators are an algebraic accumulator, in constrast to Merkle trees, one would hope
that the arithmetic complexity of statements involving RSA accumulator elements
would be much lower than those involving Merkle tree elements. Unfortunately, this
is not the case because RSA accumulator operations are in ZN for composite N with
unknown factorization, whereas arithmetic circuits for SNARGs are always defined
over finite fields Zp. Finding ways to combine RSA accumulators with SNARKs
more efficiently is an interesting research direction.

We present a variant of PoKE∗
for a group of unknown order G, which is an
argument of knowledge that the integer discrete log x of an element y ∈ G is
equivalent to ˆx modulo a public odd prime integer n. Concretely, the new protocol
PoKEMon is for the following relation:
RPoKEMon =
 w ∈ G, xˆ ∈ [n]; x ∈ Z
 
: w = g
x ∈ G, x mod n = ˆx
	
.

As with PoKE∗
, the base element g and the unknown order group G are fixed
public parameters. PoKEMon modifies PoKE∗ by setting the challenge to be ` · n
where `
$
← Primes(λ).

Protocol PoKEMon (Proof of equality mod n) for Relation RPoKEMon
Params: G
$
← GGen(λ), g ∈ G; Inputs: Odd prime n, w ∈ G, xˆ ∈ [n]; Witness:
x ∈ Z; Claim: g
x = w and x mod n = ˆx
1. Verifier sends `
$
← Primes(λ).
2. Prover computes the quotient q ∈ Z and residue r ∈ [` · n] such that
x = q(` · n) + r. Prover sends the pair (Q ← g
q
, r) to the Verifier.
3. Verifier accepts if r ∈ [` · n] and Q`·n
g
r = w holds in G and r mod n = ˆx.

The same technique can be applied to PoKE, where the base element can be
freely chosen by the prover.

We can prove security by directly reducing it to the security of the PoKE∗ protocol and additionally the strong RSA assumption, which assumes it is hard to
compute an `th root of a random group element g for odd prime `.

Theorem 9 (PoKEMon Argument of Knowledge). Protocol PoKEMon is an argument of knowledge for the relation RPoKEMon if Protocol PoKE∗
is an argument of
knowledge for the relation RPoKE∗ and the strong RSA assumption holds for GGen.

Proof. We use the extractor Ext∗
of the PoKE* protocol to build an extractor Ext for
PoKEMon, which succeeds with overwhelming probability in extracting x such that
g
x = w and x = ˆx mod n from any PoKEMon adversary that has a non-negligible
success rate.

Ext runs a copy of Ext∗
and simulates both the PoKE* challenges and a PoKE*
adversary’s response. When Ext receives the challenge ` and the PoKEMon adversary’s response (Q, r), it computes q
0 = dr/`e and r
0 = r mod ` so that r = q
0
` + r
0
and sets Q0 ← Qn
g
q
0
. It forwards (`, Q0
, r0
) to Ext∗
. If the PoKEMon adversary’s
response is valid then Q`ng
r = w, implying that Q0`
g
r
0
= w. Thus, Ext simulates
for Ext∗
a transcript of the PoKE* protocol for a PoKE* adversary that succeeds
with the same rate as the PoKEMon adversary. By hypothesis, Ext∗
succeeds with
overwhelming probability to output x such that g
x = w.

Consider any iteration in which Ext had received an accepting PoKEMon transcript (`, Q, r). We claim that x = r mod ` · n with overwhelming probability, by
the strong RSA assumption.

Suppose that x − r 6= 0 mod ` · n then given that ` and n are prime we have
that either gcd(`, x − r) = 1 or gcd(n, x − r) = 1. Without loss of generality we
assume the latter. Let Q0 = Q`
then Q0n = g
x−r and gcd(n, x − r) = 1. Now using
Shamir’s trick we can compute an nth root of g. Let a, b = Bezout(n, x − r) such
that an + b(x − r) = 1. Then u = Q0b
g
a
is an nth root of g as u
n = Q0bng
an =
g
b·(x−r)
g
an = g. This shows that (u, n) breaks the strong RSA assumption for
G
$
← GGen(λ).

This contradicts the hypothesis and we, therefore, have that r = x mod `·n which
implies that r = x mod n for an overwhelming number of accepting transcripts.
And since in any accepting transcript ˆx = r mod n we have that x = ˆx mod n with
overwhelming probability.

A.4 A succinct zero-knowledge proof of discrete-log

The PoKE protocol for succinctly proving knowledge of an exponent can further
be made zero-knowledge using a method similar to the classic Schnorr Σ-protocol
for hidden order groups. The Schnorr protocol for hidden order groups has the
same structure as the standard Schnorr protocol for proving knowledge of a discrete
logarithm x such that u
x = w in a group of known order. Here, the prover first
samples a blinding factor k ∈ [−B, B] and sends A = u
k
, obtains a challenge c, and
returns s = k + cx. The verifier checks that u
z = awc
. In hidden order groups, k
must be sampled from a range of integers [−B, B] such that |G|/B is negligible.

The classical Schnorr protocol for hidden order groups is an honest verifier statistical zero-knowledge (HVSZK) protocol and has soundness error of only 1/2 against
a classical adversary [BCK10]. Only for a small subclass of homomorphisms better
soundness can be proven [BCM05]. Unfortunately, [BCK10] proved that the soundness limitation is fundamental and cannot be improved against a classical adversary,
and therefore requires many rounds of repetition. However, we are able to show that
we can prove much tighter soundness if the adversary is restricted to operating in a
generic group.

Definition 10 (Zero Knowledge). We say an argument system (Pgen, P, V) for R
has statistical zero-knowledge if there exists a poly-time simulator Sim such that
for (x, w) ∈ R the following two distribution are statistically indistinguishable:
D1 =
n
hP(pp, x, w), V(pp, x)i, pp
$
← Pgen(λ)
o
D2 =
n
Sim(pp, x, V(pp, x)), pp
$
← Pgen(λ)
o

The protocol. Our ZK protocol applies Protocol PoKE to the last step of the
Schnorr protocol, which greatly improves the communication efficiency of the classical protocol when the witness is large. In fact, we can interleave the first step
of Protocol PoKE where the verifier sends a random prime ` with the second step
of the Schnorr protocol where the verifier sends a challenge c. This works for the
case when u is a base specified in the CRS, i.e. it is the output of a query to the
generic group oracle O1, however a subtlety arises when u is selected by the prover.
In fact, we cannot even prove that the Schnorr protocol itself is secure (with negligible soundness error) when u is selected by the prover. The method we used for
PoKE on general bases involved sending g
x
for g specified in the CRS. This would
immediately break ZK since the simulator cannot simulate g
x without knowing the
witness x. Instead, in the first step the prover will send a Pedersen commitment
g
xh
ρ where ρ is sampled randomly in some interval and h is another base specified
in the CRS.

We will first present a ZK proof of knowledge of a representation in terms of
bases specified in the CRS and show that there is an extractor that can extract the
witness. We then use this as a building block for constructing a ZK protocol for the
relation RPoKE.
Protocol ZKPoKRep for Relation Rφ where φ := Rep
Params: (g1, . . . gn) ∈ G, G
$
← GGen(λ), B > 2
2λ
|G|; Inputs: w ∈ G;
Witness: x = (x1, . . . , xn) ∈ Z
n
; Claim: Rep(x) = Qn
i=1 g
xi
i = w
1. Prover chooses random k1, . . . , kn
$
← [−B, B], sends A =
Qn
i=1 g
ki
i
to
Verifier.
2. Verifier sends c
$
← [0, 2
λ
], ` $
← Primes(λ).
3. Prover computes si = ki + c · xi∀i ∈ [1, n] and then derives quotients
q ∈ Z
n and residues r ∈ [`]
n
such that qi
· ` + ri = si for all 1 ≤ i ≤ n.
Prover sends Q =
Qn
i=1 g
qi
i
and r to the Verifier.
4. Verifier accepts if ri ∈ [`] for all 1 ≤ i ≤ n and that Q` Qn
i=1 g
ri
i = Awc
.

Theorem 10 (Protocol ZKPoKRep). Protocol ZKPoKRep is an honest-verifier statistically zero-knowledge argument of knowledge for relation RRep in the generic
group model.

Proof. See Appendix C.

Finally, we use the protocol above to obtain a ZK protocol for the relation
RPoKE. The protocol applies (in parallel) the Σ-protocol for PoKRep to a Pedersen
commitment g
xh
ρ
for g and h specified in the CRS. In order to achieve statistical
zero-knowledge we require that g and h generate the same subgroup of G. This
requirement can be lifted when computation zero-knowledge suffices. The extractor
for this protocol will invoke the PoKRep extractor to open the commitment. The
protocol works as follows:

Protocol ZKPoKE for RPoKE
Params: (g, h) ∈ G s.t. hgi = hhi, G
$
← GGen(λ); Inputs: u, w ∈ G, B >
2
2λ
|G|;
Witness: x ∈ Z; Claim: u
x = w
Let Com(x; r) := g
xh
r
.
1. Prover chooses random k, ρx, ρk
$
← [−B, B] and sends (z, Ag, Au) to the
verifier where z = Com(x; ρx), Ag = Com(k; ρk), Au = u
k
.
2. Verifier sends c
$
← [0, 2
λ
], ` $
← Primes(λ).
3. Prover computes sx = k + c · x and sρ = ρk + c · ρx and then derives
quotients q1, q2 ∈ Z and residues rx, rρ ∈ [`] such that qx · ` + rx = sx and
qρ · ` + rρ = sρ.
Prover sends Qg = Com(qx; qρ), Qu = u
qx and rx, rρ to the Verifier.
4. Verifier accepts if rx, rρ ∈ [`] and
Q
`
g
· Com(rx; rρ) = Agz
c
and Q
`
u
· u
rx = Auw
c
.

Theorem 11 (Protocol ZKPoKE). Protocol ZKPoKE is an honest verifier statistically zero-knowledge argument of knowledge for relation RPoKE in the generic group
model.

Proof. See Appendix C.

B More Accumulator techniques

B.1 Accumulator unions

Yet another application of our succinct proofs to accumulators is the ability to
prove that an accumulator is the union of two other accumulators. Given three
accumulators A1 = g
Q
s∈S1
s
1
, A2 = g
Q
s∈S2
s
2
and A3 = A
Q
s∈S1
s
2
a prover can use
the NI-PoDDH protocol to convince a verifier that (A1, A2, A3) forms a valid DDH
tuple. If S1 and S2 are guaranteed to be disjoint, then A3 will be an accumulator of
S1 ∪ S2. If they are not disjoint, then resulting accumulator will be an accumulator
for a multi-set as described in the next paragraph. The NI-PoDDH is independent
of the size of S1 and S2 in both the proof size and the verification time. This
union proof can be used to batch exclusion proofs over multiple accumulators. The
prover verifiably combines the accumulators and then creates a single aggregate
non-membership proof in the union of the accumulators. This is sound but only
works if the domains of the accumulators are separate.

B.2 Multiset accumulator

A dynamic multiset accumulator is an accumulator where items can be added and
deleted more than once, and every element has a count. In other words, it is a
commitment to a mapping from items to non-negative integer counters. It has the
following properties:

• Each element in the domain is implicitly in the mapping with a counter of 0.

• Add increments the counter of the added element by 1

• Del decrements the counter of the added element by 1

• A membership witness for an element x and a counter k proves that the counter
of x is at least k

• A membership witness for x
k and a non-membership witness for Ax−k proves
that the counter for x is exactly k. Note that Ax−k
is exactly the membership
witness for x
k
.

To build the multi-set accumulator we again employ a hash function mapping an
arbitrary domain to an exponentially large set of primes. The Add and Del algorithms are as described in Section 4.2. The membership witness change in that
they now also contain a counter of how many times a certain element has been
added. That is if an element x is k times in the accumulator the membership witness is the x
k
th root of the accumulator as well as k. VerMem,MemWitCreate,
MemWitUpAdd,MemWitUpDel are changed accordingly. The completeness
definition also needs to be updated to reflect the new multi-set functionalities.

C Security Proofs

C.1 Preliminary lemmas

In the following lemmas, which all concern the generic group model, we restrict
ourselves to adversaries that do not receive any group elements as input. This
is sufficient to prove our theorems. For our proof protocols we require that the
adversary itself outputs the instance after receiving a description of the group. We
require this in order to prevent that the instance itself encodes a trapdoor, such as
the order of the group.

Lemma 2 (Element representation [Sho97]). Using the notation of Section 2.2, let
G be a generic group and A a generic algorithm making q1 queries to O1 and q2
queries to O2. Let {g1, . . . , gm} be the outputs of O1. There is an efficient algorithm
Ext that given as input the transcript of A’s interaction with the generic group
oracles, produces for every element u ∈ G that A outputs, a tuple (α1, . . . , αm) ∈ Z
m
such that u =
Qm
i=1 g
αi
i
and αi ≤ 2
q+2
.

Lemma 3 (Computing multiple of orders of random elements). Let G be a generic
group where |G| is a uniformly chosen integer in [A, B]. Let A be a generic algorithm
making q1 queries to O1 and q2 queries to O2. The probability that A succeeds in
computing 0 6= k ∈ N such that for a g which is a response to an O1 query g
k = 1
is at most (q1+q2)
3
M , where 1/M is negligible whenever |B − A| = exp(λ). When A
succeeds we say that event Root happened.

We denote ordG(g) as the order of g ∈ G. By definition g
k = 1 ∧ 0 6= k ∈ Z ↔
k mod ordG(g) = 0.

Proof. This lemma is a direct corollary of Theorem 1 from [DK02]. That theorem
shows that an adversary that interacts with the two generic group oracles cannot
solve the strong RSA problem with probability greater than (q1 +q2)
3/M, where M
is as in the statement of the lemma. Recall that a strong RSA adversary takes as
input a random g ∈ G and outputs (u, x) where u
x = g and x is an odd prime. Let
A be an adversary from the statement of the lemma, that is, A outputs 0 < k ∈ Z
where k ≡ 0 mod |G| with some probability  . This A immediately gives a strong
RSA adversary that also succeeds with probability  : run A to get k and g such
that g
k = 1 ∈ G. Then find an odd prime x that does not divide k, and output
(u, x) where u = g
(x−1 mod k)
. Clearly u
x = g which is a solution to the given strong
RSA challenge. It follows by Theorem 1 from [DK02] that   ≤ (q1 + q2)
3/M, as
required.

Lemma 4 (Discrete Logarithm). Let G be a generic group where |G| is a uniformly chosen integer in [A, B], where 1/A and 1/|B − A| are negligible in λ. Let
A be a generic algorithm and let {g1, . . . , gm} be the outputs of O1. Then if A
runs in polynomial time, it succeeds with at most negligible probability in outputting
α1, . . . , αm, β1, . . . , βm ∈ Z such that Qm
i=1 g
αi
i =
Qm
i=1 g
βi
i
and αi 6= βi for some i.
We call this event DLOG.

Proof sketch. We follow the structure of Shoup’s argument [Sho97]. By Lemma 2
every group element u ∈ G that the adversary obtains in response to an O2 query
can be written as u =
Qm
i=1 g
αi
i
for some known αi ∈ Z. Let g =
Qm
i=1 g
αi
i
and
36
h =
Qm
i=1 g
βi
i
be two such group elements. If there is some i for which αi 6≡ βi
(mod ordG(gi)) then the probability that g = h is at most negligible, as shown
in [DK02]. Hence, if g = h then with overwhelming probability we have that αi ≡ βi
(mod ordG(gi)) for all i. From this it follows by Lemma 3 that αi = βi ∈ Z with
overwhelming probability, since otherwise one obtains a multiple of |G|. Since A
constructs at most polynomially many group elements, there are at most polynomially many pairs of such elements. Therefore, a union bound over all pairs shows
that the probability that event DLOG happens is at most negligible, as required.

Lemma 5 (Dlog extraction). Let G be a generic group where |G| is a uniformly
chosen integer in [A, B] and g an output of a query to O1. Let A be a generic
algorithm that outputs w ∈ G and then runs the interactive protocol Protocol PoKE∗
with g in the CRS. Let (`1, Q1, r1) and (`2, Q2, r2) two accepting transcripts for
Protocol PoKE∗
generated one after the other. If 1/A and 1/|B − A| are negligible
in λ, then with overwhelming probability there exist integers α and β such that
α · l1 + r1 = β · l2 + r2 and g
α·l1+r1 = w. Further if A makes q queries to O2 then
|α|, |β| are bounded by 2
q
.

Proof. W.l.o.g. let g1 = g be encoded in the PoKE∗ CRS. The PoKE∗ verification
equations give us w = Q
`1
1
g
r1 = Q
`2
2
g
r2
. We can write Q1 =
Qm
i=1 g
αi
i
and Q2 = Qm
i=1 g
βi
i
. This implies that Q
`1
1
g
r1 = g
α1·`1+r1
Qm
i=2 g
αi·`1
i = g
β1·`2+r2
Qm
i=2 g
βi·`2
i
. By
Lemma 4, αi`1 = βi`2 ∈ Z for all i 6= 1 with overwhelming probability (i.e. unless
event DLOG occurs), and therefore `2|αi`1. The primes `1 and `2 are co-prime
unless `1 = `2, which happens with probability ln(2)λ
2
λ . Thus, with overwhelming
probability `2|αi
. However, αi ≤ 2
q2 and αi
is chosen before `2 is sampled, hence
the probability that `2|αi for αi 6= 0 is at most q2λ ln(2)
2
λ . We conclude that with
overwhelming probability αi = βi = 0 for all i 6= 1. It follows that except with
probability Pr[DLOG]+ 2q2λ ln(2)
2
λ , we can express w = g
α1`1+r1 = g
β1`2+r2
for integers
α1, r1, β1, r2 such that α1`1 + r1 = β1`2 + r2.

In what follows we will use the following notation already introduced in Section 3:
for generators g1, . . . , gn ∈ G we let Rep : Z
n → G be the homomorphism
Rep(x) = Yn
i=1
g
xi
i
.

Lemma 6 (Representation extraction). Let G be a generic group where |G| is a
uniformly chosen integer in [A, B] and let g1, . . . , gn ∈ G be responses to queries
to oracle O1. Let A be a generic algorithm that outputs w ∈ G and then runs
the interactive protocol Protocol PoKRep on input w with g1, ..., gn in the CRS. Let
(`1, Q1, r1) and (`2, Q2, r2) be two accepting transcripts for Protocol PoKRep. If
1/A and 1/|B −A| are negligible in λ, then with overwhelming probability there exist
integer vectors α, β ∈ Z
n
such that αl1 + r1 = βl2 + r2 and Rep(αl1 + r1) = w.
Further if A makes q queries to O2 then each component αj and βj of α and β are
bounded by 2
q
.

Proof. The proof is a direct generalization of the argument in Lemma 5 above. From
the verification equations of the protocol we have Q
`1
1 Rep(r1) = Q
`2
2 Rep(r2) = w.
With overwhelming probability, the generic group adversary knows α1, ..., αm and
β1, .., βm for m > n such that it can write Q1 =
Qm
i=1 g
αi
i
and Q2 =
Qm
i=1 g
βi
i
. From
the verification equation and Lemma 4, with overwhelming probability αi`1+r1[i] =
βi`2 + r2[i] for each i ≤ n and αi`1 = βi`2 for each i > n. As explained in the
proof of Lemma 5, this implies that with overwhelming probability αi = βi = 0
for each i > n, in which case w =
Qn
i=1 g
αi`1+r1[i]
i =
Qn
i=1 g
βi`2+r2[i]
i
. Setting α :=
(α1, ..., αn) and β := (β1, ..., βn), we conclude that with overwhelming probability
w = Rep(α`1 + r1) = Rep(β`2 + r2) and α`1 + r1 = α`2 + r2. Finally, if A has
made at most q queries to O2 then αi < 2
q and βi < 2
q
for each i.

The next two corollaries show that the adaptive root problem and the known
order element problem are intractable in a generic group.

Corollary 1 (Adaptive root hardness). Let G be a generic group where |G| is a
uniformly chosen integer in [A, B] such that 1/|A| and 1/|B − A| are negligible
in λ. Any generic adversary A that performs a polynomial number of queries to
oracle O2 succeeds in breaking the adaptive root assumption on G with at most
negligible probability in λ.

Proof. Recall that in the adaptive root game the adversary outputs w ∈ G, the
challenger then responds with a prime ` ∈ [2, 2
λ
], and the adversary succeeds if it
outputs u such that u
` = w. According to Lemma 2 we can write u =
Qm
i=1 g
αi
i
and w =
Qm
i=1 g
βi
i
, where g1, . . . , gm are the responses to oracle O1 queries. By
Lemma 4 we know that αi` = βi mod |G| for all i = 1, . . . , m with overwhelming
probability, namely 1 − Pr[DLOG]. Therefore, αi` = βi + k · |G| for some k ∈ Z. By
Lemma 3, an efficient adversary can compute a multiple of the order of the group
with at most negligible probability Pr[Root]. It follows that k = 0 and αi` = βi ∈ Z
with probability greater than 1 − Pr[DLOG] − Pr[Root], since otherwise αi` − βi
is
a multiple of G. Now, because αi` = βi we know that ` must divide βi
. However,
βi
is chosen before ` and if A makes q2 generic group queries then βi ≤ 2
q2
. The
probability that ` divides βi
, for βi 6= 0, is bounded by the probability that a
random prime in Primes(λ) divides a number less than 2q2
. Any such number has
less than q2 distinct prime factors and there are more than 2λ/λ primes in Primes(λ).
Therefore, the probability that ` divides βi 6= 0 is at most q2·λ
2
λ . Overall, we obtain
that a generic adversary can break the adaptive root assumption with probability at
most (q1+q2)
2
A + 2 ·
(q1+q2)
3
M +
q2·λ
2
λ , which is negligible if A and B − A are exponential
in λ and q1, q2 are bounded by some polynomial in λ.

Corollary 2 (Non-trivial order hardness). Let G be a generic group where |G| is a
uniformly chosen integer in [A, B] such that 1/|A| and 1/|B −A| are negligible in λ.
Any generic adversary A that performs a polynomial number of queries to oracle O2
succeeds in finding an element h 6= 1 ∈ G and an integer d such that h
d = 1 with at
most negligible probability in λ.

Proof. We can construct an adaptive root adversary that first uses A to obtain
h and d, and then computes the `th root of h by computing c = `
−1 mod d and
h
c = h
1/`. Since the adaptive root assumption holds true in the generic group model
(Corollary 1), we can conclude that A succeeds with negligible probability.

Fact 1 (Chinese Remainder Theorem (CRT)). Let `1, . . . , `n be coprime integers and
let r1, . . . , rn ∈ Z, then there exists a unique 0 ≤ x < Qn
i=1 `i such that x = ri mod `i
and there is an efficient algorithm for computing x.

C.2 Proofs of the main theorems

Proof of Theorem 7.

Protocol PoKRep is an argument of knowledge for the relation Rφ where
φ := Rep, in the generic group model.

Fix G
$
← GGen(λ) and g = (g1, ..., gn) ∈ G. Let A0, A1 be poly-time generic
adversaries where (w,state)
$
← A0(g) and A1(state) runs Protocol PoKRep with a
verifier V (g, w). We need to show that for all A1 there exists a poly-time Ext
such that for all A0 the following holds: if A1 convinces V (g, w) to accept with
probability   ≥ 1/poly(λ), then Ext outputs a vector x ∈ Z
n
such that Rep(x) = w
with overwhelming probability.

Subclaim In Protocol PoKRep, for any polynomial number of accepting
transcripts {(`i
, Qi
, ri)}
poly(λ)
i=1 obtained by rewinding A1 on the same
input (w,state), with overwhelming probability there exists x ∈ Z
n
such
that x = ri mod `i for each i and Rep(x) = w. Furthermore, xj ≤ 2
q
for each jth component xj of x, where q is the total number of queries
that A makes to the group oracle.

The subclaim follows from Lemma 6. With overwhelming probability there exists
α, β, and x in Z
n
such that x = α`1 + r1 = β`2 + r2 and Rep(x) = w, and each
component of x is bounded by 2q
. Consider any third transcript, w.l.o.g. (`3, Q3, r3).
Invoking the lemma again, there exists α0
, β
0
, and x
0
such that x
0 = α0
`2 + r2 =
β
0
`3 +r3. Thus, with overwhelming probability, x
0 −x = (α0 −β)`2. However, since
`2 is sampled randomly from an exponentially large set of primes independently
from r1, r3, `1, and `3 (which fix the value of x
0 −x) there is a negligible probability
that x
0 − x ≡ 0 (mod `2), unless x
0 = x. By a simple union bound over the poly(λ)
number of transcripts, there exists a single x such that x = ri mod `i for all i.

To complete the proof of Theorem 7 we describe the extractor Ext:

1. run A0 to get output (w,state)

2. let R ← {}

3. run Protocol PoKRep with A1 on input (w,state), sampling fresh randomness
for the verifier

4. if the transcript (`, Q, r) is accepting set R ← R ∪ {(r, `)}, and otherwise
return to Step 3

5. use the CRT algorithm to compute x such that x = ri mod `i for each (ri
, `i) ∈
R

6. if Rep(x) = w output x and stop

7. return to Step 3

It remains to argue that Ext succeeds with overwhelming probability in a poly(λ)
number of rounds. Suppose that after some polynomial number of rounds the extractor has obtained M accepting transcripts {`i
, Qi
, ri} for independent values of
`i ∈ Primes(λ). By the subclaim above, with overwhelming probability there exists
x ∈ Z
n
such that x = ri mod `i and Rep(x) = w and xj < 2
q
for each component
of x. Hence, the CRT algorithm used in Step 5 will recover the required vector x
once |R| > q.

Since a single round of interaction with A1 results in an accepting transcript with
probability   ≥ 1/poly(λ), in expectation the extractor obtains |R| > q accepting
transcripts for independent primes `i after q · poly(λ) rounds. Hence, Ext outputs a
vector x such that Rep(x) = w in expected polynomial time, as required.

Proof of Theorem 3.

Protocol PoKE and Protocol PoKE2 are arguments of knowledge for relation RPoKE in the generic group model.

Fix G
$
← GGen(λ) and g ∈ G. Let A0, A1 be poly-time adversaries where
(u, w,state)
$← A0(g) and A1 runs Protocol PoKE or Protocol PoKE2 with the verifier
V(g, u, w). We need to show that for all A1 there exists a poly-time Ext such that for
all A0 the following holds: if V(g, u, w) outputs 1 with non-negligible probability on
interaction with A1(g, u, w,state) then Ext outputs an integer x such that u
x = w
in G with overwhelming probability.

Proof for Protocol PoKE. Protocol PoKE includes an execution of
Protocol PoKE∗ on g ∈ G and input z (the first message sent by the prover to the
verifier), and the prover succeeds in Protocol PoKE only if it succeeds in this subprotocol for Protocol PoKE∗
. Since Protocol PoKE∗
is a special case of Protocol PoKRep,
by Theorem 7 there exists Ext∗
for A1 that outputs x
∗ ∈ Z such that g
(x
∗) = z. Furthermore, as already shown in the analysis of Theorem 7, once Ext∗ has obtained x
∗
it can continue to replay the protocol, sampling a fresh prime `
$
← Primes(λ), and in
each fresh round that produces an accepting transcript it obtains from the Prover a
triple (Q, Q0
, r) such that r = x
∗ mod ` with overwhelming probability. This is due
to the fact that the adversary outputs Q0
such that Q0`
g
r = z = g
x
∗
, and the generic
group adversary can write Q0 = g
q Q
i>1
g
qi
i
(Lemma 2) such that q` + r = x
∗ with
overwhelming probability (Lemma 4).

The extractor Ext will simply run Ext∗
to obtain x
∗
. Now we will show that
either u
x
∗
= w, i.e. Ext∗
extracted a valid witness, or otherwise the adaptive root
assumption would be broken, which is impossible in the generic group model (Corollary 1). To see this, we construct an adaptive root adversary AAR that first runs
Ext∗ with A0, A1 to obtain x
∗ and provides h = w/ux
∗
∈ G to the challenger. When
provided with `
$
← Primes(λ) from the challenger, AAR rewinds A1, passes ` to
A1, and with overwhelming probability obtains Q, r such that x
∗ = r mod ` and
Q`u
r = w. Finally, AAR outputs v =
Q
u
b
x∗
` c
, which is an `th root of h:
v
` =
 Q
u
b
x∗
`
c
 ` =
 Q
u
b
x∗
`
c
 ` u
r
u
r
=
w
u
x∗ = h

If w 6= u
x
∗
so that h 6= 1, then AAR succeeds in the adaptive root game.
In conclusion, the value x
∗ output by Ext satisfies w = u
x
∗
with overwhelming
probability.

Proof for protocol PoKE2 Showing that Protocol PoKE2 requires a fresh
argument (similar to the analysis in Theorem 7) since the protocol no longer
directly contains Protocol PoKE∗ as a subprotocol. Ext first obtains u, w from A0
and runs the first two steps of Protocol PoKE2 with A1 playing the role of the
verifier, sampling g
$
← G and receiving z ∈ G from A1. Ext is a simple modification
of the extractor for Protocol PoKE:

1. Set R ← {} and sample α
$
← [0, 2
λ
].

2. Sample `
$
← Primes(λ) and send α, ` to A1.

3. Obtain output Q, r from A0. If Q`u
r
g
αr = wzα (i.e. the transcript is accepting) then update R ← R ∪ {(r, `)}. Otherwise return to step 2.

4. Use CRT to compute x = ri mod `i for each (ri
, `i) ∈ R. If u
x = w then
output x, otherwise return to step 2.

Note that the extractor samples a fresh prime challenge ` each time it rewinds the
adversary but keeps the challenge α fixed each time. Since these are independently
sampled in the real protocol, keeping α fixed while sampling a fresh prime does not
change the output distribution of the adversary. This subtle point of the rewinding
strategy is important.

There is a negligible probability that the random g sampled by the extractor was
contained in the group oracle queries from A0 to O1. Thus, by Lemma 2, A0 knows
representations w =
Q
i
g
ωi
i
and u =
Q
i
g
µi
i
such that gi 6= g for all i. A0 also knows
a representation z = g
ζ Q
i
g
ζi
i
and for each Q obtained A0 knows a representation
Q = g
q Q
i
g
qi
i
, which it can pass in state to A1. If Q`u
r
g
αr = wzα, then A1 obtains
an equation g
q`+αr Q
i
g
qi`+µir
i = g
ζα Q
i
g
ζiα+ωi
i
.

By Lemma 4, with overwhelming probability q` + αr = ζα, which implies α|q`.
Since gcd(α, `) = 1 with overwhelming probability, it follows that α|q and setting
a = q/α shows that ζ = a` + r, i.e. ζ = r mod `. Also for the same reasoning
qi` + µir = ζiα + ωi with overwhelming probability. Repeating the argument for a
different `
0
sampled by the extractor yields a similar equation ζ = a
0
`
0 + r
0
, hence
a` + r = a
0
`
0 + r
0
for some a
0 = q
0/α. Also qi` + µir − ζiα = q
0
i
`
0 + µir
0 − ζiα.
Substituting for r and r
0 gives qi` + µi(ζ − a`) = q
0
i
`
0 + µi(ζ − a
0
`
0
) implying:
(qi − µia)` = (q
0
i − µia
0
)`
0

(This is where it was important that α is fixed by the extractor, as otherwise we
could not cancel the ζiα term on each side of the equation). Now since ` 6= `
0 6= 0
with overwhelming probability, it follows that `|q
0
i − µia
0 and `
0
|qi − µia. However,
qi − µia was fixed independently before `
0 was sampled, hence there is a negligible
probability that it has `
0 as a factor unless qi − µia = 0, in which case q
0
i − µia
0 = 0
as well. We conclude that with overwhelming probability qi` + µir = q
0
i
`
0 + µir
0 =
µiζ. In other words, for each ` sampled, as long as Q`u
r
g
αr = wzα then with
overwhelming probability:
wzα = g
q`+αr Y
i
g
qi`+µir
i = g
ζαY
i
g
µiζ
i = g
ζαu
ζ

Finally, if u
ζ 6= w then g
ζ/z 6= 1 and yet (g
ζ/z)
α = u
ζ/w. Since α is sampled
independently from u, w, g, and ζ, this relation can only hold true with non-negligible
probability over the choice of α if both g
ζ/z and u
ζ/w are elements of a small (i.e.
poly(λ) size) subgroup generated by g
ζ/z. In other words, g
ζ/z is an element of
low order, and it is possible to compute its order in polynomial time. This would
be a contradiction in the generic group model since it is hard to find a non-trivial
element and its order (Corollary 2). In conclusion, with overwhelming probability
u
ζ = w.

Repeating this analysis for each accepting transcript (`i
, Qi
, ri) shows that ζ =
ri mod `i with overwhelming probability. The remainder of the analysis is identical
to the last part of the proof of Theorem 7. Namely, since ζ < 2
q where q < poly(λ) is
an upper bound on the number of queries the adversary makes to the group oracle,
we can show there exists a polynomial number of rounds after which Ext would
succeed in extracting ζ with overwhelming probability.

Proof of Theorem 8.

For any homomorphism φ : Z
n → G, Protocol PoKHP for relation Rφ =
{(w; x) : φ(x) = w} is an argument of knowledge in the generic group
model.

The proof is a direct generalization of the proof of Theorem 3 for Protocol PoKE.
As usual, fix G
$
← GGen(λ) and g = (g1, ..., gn) ∈ G. Let A0, A1 be poly-time
generic adversaries where (w,state)
$
← A0(g) and A1(state) runs Protocol PoKHP
with the verifier V (g, w). We need to show that for all A1 there exists a poly-time
Ext such that for all A0 the following holds: if A1 convinces V (g, w) to accept with
probability at least 1/poly(λ) then Ext outputs x ∈ Z
n
such that φ(x) = w with
overwhelming probability.

Protocol PoKHP includes an execution of Protocol PoKRep on g1, ..., gn ∈ G and
input z (the first message sent by the prover to the verifier), and the prover succeeds
in Protocol PoKHP only if it succeeds in this subprotocol for Protocol PoKRep. By
Theorem 7 there exists Ext∗
for each A1 that outputs x
∗
such that Rep(x
∗
) = z.
Furthermore, as shown in the analysis of Theorem 7, once Ext∗ has obtained x
∗
it
can continue to replay the protocol, sampling a fresh prime `
$
← Primes(λ), and in
each fresh round that produces an accepting transcript it obtains from the Prover
values Q, Q0 and r such that r = x
∗ mod ` with overwhelming probability.

The extractor Ext simply runs Ext∗
to obtain x
∗
. Now we will show that either φ(x
∗
) = w, i.e. Ext∗
extracted a valid witness, or otherwise the adaptive root
assumption would be broken, which is impossible in the generic group model (Corollary 1). To see this, we construct an adaptive root adversary AAR that first runs
Ext∗ with A0, A1 to obtain x
∗ and provides h = w/φ(x
∗
) ∈ G to the challenger.
When provided with `
$
← Primes(λ) from the challenger, AAR rewinds A1, passes
` to A1, and with overwhelming probability obtains Q, r such that x
∗ = r mod `
and Q`φ(r) = w. Finally, define bx
∗/`c to be the vector obtained by replacing each
component xi with the quotient bxi/`c. AAR outputs v =
Q
φ(bx∗/`c)
. Using the fact
that φ is a group homomorphism we can show that this is an `th root of h:
v
` =
 Q
φ(b
x∗
`
c)
 ` =
Q`
φ(` · bx∗
`
c)
=
Q`
φ(x∗ − r)
φ(r)
φ(r)
=
w
φ(x∗)
= h

If w 6= φ(x
∗
) so that h 6= 1, then AAR succeeds in the adaptive root game.
In conclusion, the value x
∗ output by Ext satisfies w = φ(x
∗
) with overwhelming
probability.

Proof of Theorem 10.

Protocol ZKPoKRep is an honest-verifier statistical zero-knowledge argument of knowledge for relation RRep in the generic group model.

Part 1: HVZK To show that the protocol is honest-verifier zero-knowledge we
build a simulator Sim. Sim samples (A, ˜ c, ˜
˜`, r˜, Q˜) as follows. Let Gi denote the
subgroup of G generated by the base gi
.

1. c˜
$
← [0, 2
λ
], ˜`
$
← Primes(λ)

2. q˜
$
← [B]
n

3. r˜
$
← [`]
n

4. Q˜ ←
Qn
i=1 g
q˜i
i

5. A˜ ← Q˜`˜
(
Qn
i=1 g
r˜i
i
)
−1w
−c˜
.

We now argue that (A, ˜ c, ˜
˜`, r˜, Q˜) is statistically indistinguishable from a transcript between an honest prover and verifier: (A, c, `, r, Q). Sim chooses ˜` and ˜c
identically to the honest verifier in the real protocol. It also solves for A˜ uniquely
from the other values such that the verification holds. Therefore, it remains only
to show that r˜ and Q˜ have the correct distribution. We must show that in the
real protocol, independent of ` and c, r has statistical distance less than 2−λ
from
the uniform distribution over [`]
n and each g
qi
i
has statistical distance less than 2−λ
from uniform over Gi (recall that Q =
Q
i
g
qi
i
). In addition we must argue that Q
and r are independent.

For this we use the following facts, which are easy to verify:

1. Fact 1: If Z is uniform random variable over N consecutive integers and
m < N then Z mod m has statistical distance at most m/N from the uniform
distribution over [m].

2. Fact 2: For independent random variables X1, X2, Y1, Y2, the distance between
the joint distributions (X1, X2) and (Y1, Y2) is at most the sum of statistical
distances of X1 from Y1 and X2 from Y2. Similarly, if these variables are group
elements in G, the statistical distance between X1 ·X2 and Y1 ·Y2 is no greater
than the sum of statistical distances of X1 from Y1 and X2 from Y2.

3. Fact 3: Consider random variables X1, X2, Y1, Y2 with statistical distances
s1 = ∆(X1, X2) and s2 = ∆(Y1, Y2), where P r(X1 = x|Y1 = y) < P r(X1 =
x) +  1 and P r(X2 = x|Y2 = y) < P r(X1 = x) +  2 for all values x, y. Then
the joint distributions (X1, X2) and (Y1, Y2) have statistical distance at most
s1 + s2 +  2|supp(X1)| +  1|supp(Y1)|, where supp is the support.

Consider fixed values of c, xi and `. In the real protocol, for each i ∈ [n] the prover
computes si = cxi + ki where ki
is uniform in [−B, B] and sets ri = si mod ` and
qi = b
si
`
c. The value of si
is distributed uniformly over a range of 2B+ 1 consecutive
integers, thus ri has statistical distance at most `/(2B + 1) from uniform over [`].
This bounds the distance between ri and the simulated ˜ri
, which is uniform over [`].
Next we show that each g
qi
i
is statistically indistinguishable from uniform in Gi
.
Consider the distribution of b
si
`
c over the consecutive integers in [b
cxi−B
`
c, b
cxi+B
`
c].
Denote this by the random variable Zi
. The distribution of g
qi
i
over Gi
is determined
by the distribution of qi mod |Gi
|. The probability that qi = z is the probability
that si falls in the interval [z`,(z + 1)` − 1]. This probability is `/(2B + 1) for
all points where z` ≥ cxi − B and (z + 1)` ≤ cxi + B, which includes all points
except possibly the two endpoints z = b
cxi−B
`
c and z = b
cxi+B
`
c. Call this set of
points Y . The distance of qi from a uniform random variable UY over Y is largest
when cxi − B = 1 mod ` and cxi + B = 0 mod `. In this case, qi
is one of the
two endpoints outside Y with probability 1/B. For each z ∈ Y , P r[qi = z] =
`/(2B + 1). As |Y | = 2(B − 1)/`, the statistical distance of qi from UY is at most:
1
2
[Y (
1
Y −
`
2B
) + 1
B
] = 1
2
(1 −
B−1
B +
1
B
) = 1
B
. Moreover, the statistical distance of
qi mod |Gi
| from UY mod |Gi
| is no larger.

As noted in the Fact 1 above, UY mod |Gi
| has statistical distance at most
|Gi
|/|Y | ≤ `|G|/2(B − 1) < 1/(n2
λ+1) for B > n2
2λ
|G|. By the triangle inequality,
the statistical distance of qi mod |Gi
| from uniform is at most 1/B + 1/n2
λ+1 <
1/(n2
λ
). This also bounds the distance of g
qi
i
from uniform in |G|i
. The simulated
g
q˜i
i
has distance at most 1/(n2
2λ
) from uniform in Gi since ˜qi mod |G|i has distance
B/|G|i < 1/(n2
2λ
) from uniform (again by the Fact 1 above). By the triangle
inequality, the distance between g
q˜i
i
and g
qi
i
is at most 1/B+1/(n2
λ+1)+1/(n2
2λ
) <
(1/2
λ + 1/2 + 1/2
λ
)1/(n2
λ
) < 1/(n2
λ
).

We have shown that each ri
is statistically indistinguishable from the simulated
r˜i and each g
qi
is statistically indistinguishable from the simulated g
q˜i
. However,
we must consider the distances between the joint distributions. Since qi and ri are
not independently distributed, arguing about the joint distributions requires more
work. The simulated ˜qi and ˜ri are independent on the other hand.

Consider the conditional distribution of qi
|ri (i.e. the distribution of the random
variable for qi conditioned the value of ri). Note that qi = z if (si − ri)/` = z.
We repeat a similar argument as above for bounding the distribution of qi from
uniform. For each possible value of z, there always exists a unique value of si
such that si//` = z and si = 0 mod `, except possibly at the two endpoints of
the range of qi (i.e. e1 = b
cxi−B
`
c and e2 = b
cxi+B
`
c). When ri disqualifies the
two points e1 and e2, then each of the remaining points z 6∈ {e1, e2} still have
equal probability mass, and thus the probability P r(qi = z|ri) increases by at most
[P r(qi = e1) + P r(qi = e2)]/(2b
B
`
c) < 1/B2
. The same applies to the variable
qi
|ri mod |G|i and hence the variable g
qi
|ri
.

We can compare the joint distribution Xi = (g
qi
, ri) to the simulated Yi(g
q˜i
, r˜i)
using Fact 3 above. Setting  1 = 1/B2 and  2 = 0, the distance between these joint
distributions is at most 1/(n2
λ
)+`/(2B+1)+`/B2
. Moreover, as each Xi = (g
qi
, ri)
is independent from Xj = (g
qj
, rj ) for i 6= j, we use Fact 2 to bound the distances
between the joint distributions (g
q1
, ..., gqn , r1, ..., rn) and (g
q˜1
, ..., gq˜n , r˜1, ..., r˜n) by
the sum of individual distances between each Xi and Yi
, which is at most 1/2
λ +
n`/(2B + 1)+n`/B2 < 2
−λ+1. Finally, this also bounds the distance between (Q, r)
and (Q, ˜ r˜) where Q =
Q
i
g
qi and Q˜ =
Q
i
g
q˜i
.

Part 2: PoK For extraction we describe an efficient extractor Ext. Ext randomly
samples two random challenges c and c
0
, and c 6= c
0 with probability 1
2
λ . Ext then
uses the extractor from T heorem 7 to extract s and s
0
such that Qn
i=1 g
si
i = Awc
and Qn
i=1 g
s
0
i
i = Awc
0
. We now compute ∆si = si−s
0
i
for all i ∈ [1, n] and ∆c = c−c
0
.
This gives us Qn
i=1 g
∆si
i = w
∆c
. We now claim that ∆c ∈ Z divides ∆si ∈ Z for each
i ∈ [1, n] with overwhelming probability and that Qn
i=1 g
∆si/∆c
i = w. By Lemma 2,
we can write w =
Qm
i=1 g
αi
i
, for integers αi ∈ Z that can be efficiently computed
from A’s queries to the generic group oracle. Since Qn
i=1 g
∆si
i = w
∆c
it follows by
Lemma 4 that, with overwhelming probability, αj = 0 for all j > n and ∆si = αi∆c
for all i ∈ [1, n].

Furthermore, if µ =
Qn
i=1 g
∆si/∆c
i
6= w, then since µ
∆c =
Qn
i=1 g
∆si
i = w
∆c
it
would follow that µ/w is an element of order ∆c > 1. As ∆c is easy to compute this
would contradict the hardness of computing a non-trivial element and its order in the
generic group model (Corollary 2). We can conclude that µ = w with overwhelming
probability. The extractor outputs α = (α1, ..., αn) where αi = ∆si/∆c.

Proof of Theorem 11.

Protocol ZKPoKE is an honest-verifier statistically zero-knowledge argument of knowledge for relation RPoKE in the generic group model.

To prove that the protocol is honest-verifier zero-knowledge we build a simulator
Sim which generates valid transcripts that are statistically indistinguishable from
honestly generated ones. The simulator generates a transcript as follows:

1. c˜
$
← [0, 2
λ
],
˜`
$
← Primes(λ)

2. z˜ ← h
ρ˜
, ρ
$
← [B]

3. q˜x, q˜r
$
← [B]
2

4. r˜x, r˜ρ ∈ [`]
2

5. Q˜
g ← g
q˜xh
q˜ρ
, Q˜
u ← u
q˜x

6. A˜
g ← Q˜`
g
g
r˜xh
r˜ρ z
−c˜
, A˜
u ← Q˜`
uu
r˜xw
−c˜

We now argue that the transcript (˜z, A˜
g, A˜
u, c, ˜
˜`, Q˜
g, Q˜
u, r˜x, r˜ρ) is statistically
indistinguishable from a transcript between an honest prover and verifier:
(z, Ag, Au, c, `, Qg, Q, u, rx, rρ)
˜`, c˜ are identically chosen as by the random verifier
and A˜
g, A˜
u are uniquely defined by the rest of the transcript and the verification
equations. It thus suffices to argue that ˜z, Q˜
g, Q˜
u, r˜x, r˜ρ as well as z, Qg, Qu, rx, rρ
are statistically indistinguishable from uniform in their respective domain.

Using Fact 1 stated in the proof of Theorem 10 and that B > 2
λ
|G| we can see
that ˜z is indistinguishable from a uniform element in the subgroup of G generated by
h. Since g and h generate the same subgroup the same argument applies to z. For
Q˜
g, Q˜
u, r˜x, r˜ρ and Qg, Qu, rx, rρ the same argument as in the proof of T heorem 10
apply, showing that all values are nearly uniform. The simulation therefore produces
valid, statistically indistinguishable transcripts. Note that the requirement that g, h
generate the same group can be relaxed under computational assumptions. The
assumption states that it is difficult to distinguish between g, h which generate the
same subgroup and g
0
, h0 which don’t. Given this we can use a hybrid argument
which replaces g
0
, h0 with g, h and the applies the same simulation argument as
above.

For extraction, note that the protocol contains Protocol ZKPoKRep as a subprotocol on input Ag and bases g, h in the CRS, and therefore we can use the
ZKPoKReP and PoKRep extractors to extract x, ρ such that z = g
xh
ρ and s1, s2
such that g
s1 h
s2 = Agz
c with overwhelming probability. Moreover, as shown in the
analysis for the PoKRep extractor, we can rewind the adversary on fresh challenges
so that each accepting transcript outputs an r1, ` where s1 = r1 mod ` with overwhelming probability. If u
s1 6= Auw
c = Q`
uu
r1 then γ = (r1 − s1)/` is an integer
and Quu
γ
is an `th root of Auw
c/us1 6= 1. This would break the adaptive root
assumption, hence by Corollary 1 it follows that u
s1 = Auw
c with overwhelming
probability.

Recall from the analysis of Theorem 10 that the extractor obtains a pair of
accepting transcripts with s1, s2, s0
1
, s0
2
, c, c0
so that x = ∆s1/∆c = (s1 − s
0
1
)/(c − c
0
)
and ρ = ∆s2/∆c = (s2 − s
0
2
)/(c − c
0
). Since u
s1 = Auw
c and u
s
0
1 = Auw
c
0
with
overwhelming probability, we obtain u
∆s1 = w
∆c with overwhelming probability.
Finally, this implies (u
x
)
∆c = w
∆c
. If u
x 6= w, then u
x/w is a non-trivial element of
order ∆c, which would contradict the hardness of computing a non-trivial element
and its order in the generic group model (Corollary 2). Hence, we conclude that
u
x = w with overwhelming probability.

D Non-interactive PoE and PoKE variants

NI-PoE
{x, u, w : u
x = w}
Prove(x, u, w) :
` ← Hprime(x, u, w)
q ← bx/`c
Q ← u
q
Verify(x, u, w, Q) :
` ← Hprime(x, u, w)
r ← x mod `
Check: Q
`u
r = w
NI-PoKE2
{(u, w; x) : u
x = w}
Prove(x, u, w) :
g ← HG(u, w), z = g
x
` ← Hprime(u, w, z), α = H(u, w, z, `)
q ← bx/`c, r ← x mod `
π ← {z,(ugα
)
q
, r}
Verify(u, w, z, Q, r) :
g ← HG(u, w)
` ← Hprime(u, w, z), α ← H(u, w, z, `)
Check: Q
`
(ugα
)
r = wzα

NI-PoDDH
{(y1, y2, y3); (x1, x2) : g
x1 = y1 ∧ g
x2 = y2 ∧ y
x2
1 = y3
Prove(x = (x1, x2), y = (y1, y2, y3)) :
` ← Hprime(y)
(q1, q2) ← (bx1/`c, bx2/`c)
(r1, r2) ← (x1 mod `, x2 mod `)
π ← {(g
q1
, gq2
, y
q2
1
), r1, r2}
Verify(y, π) :
` ← Hprime(y)
{Qy1
, Qy2
, Qy3
, r1, r2} ← π
Check:
r ∈ [`]
2 ∧ Q
`
y1
g
r1 = y1 ∧ Q
`
y2
g
r2 = y2 ∧ Q
`
y3
y
r2
1 = y3
NI-ZKPoKE
{(u, w; x) : u
x = w}
Prove(x, u, w) :
k, ρx, ρk
$
← [−B, B]; z = g
xh
ρx
; Ag = g
kh
ρk
; Au = u
k
;
` ← Hprime(u, w, z, Ag, Au); c ← H(`);
qx ← b(k + c · x)/`c; qρ ← b(ρk + c · ρx)/`c;
rx ← (k + c · x) mod `; rρ ← (ρk + c · ρx) mod `;
π ← {`, z, gqx h
qρ
, uqx
, rx, rρ}
Verify() :
{c, z, Qg, Qu, rx, rρ} ← π
c = H(`) Ag ← Q
`
g
g
rx h
rρ z
−c
; Au ← Q
`
uu
rx w
−c
Check: rx, rρ ∈ [`]; ` = Hprime(u, w, z, Ag, Au)
45
