 Perfect Hiding and Perfect Binding Universally
           Composable Commitment Schemes
             with Constant Expansion Factor

                    Ivan Damg˚ard and Jesper Buus Nielsen

                    BRICS     Department of Computer Science
                               University of Aarhus
                                  Ny Munkegade
                           DK-8000 Arhus C, Denmark
                              {ivan,buus}@brics.dk


Abstract.   Canetti and Fischlin have recently proposed the security no-
tion universal composability for commitment schemes and provided two
examples. This new notion is very strong. It guarantees that security is
maintained even when an unbounded number of copies of the scheme
are running concurrently, also it guarantees non-malleability and secu-
rity against adaptive adversaries. Both proposed schemes use Θ(k) bits
to commit to one bit and can be based on the existence of trapdoor
commitments and non-malleable encryption.
We present new   universally composable commitment (UCC) schemes
based on extractable  q one-way homomorphisms. These in turn exist
based on the Paillier cryptosystem, the Okamoto-Uchiyama cryptosys-
tem, or the DDH assumption. The schemes are eﬃcient: to commit to   k
bits, they use a constant number of modular exponentiations and commu-
nicates O(k) bits. Furthermore the scheme can be instantiated in either
perfectly hiding or perfectly binding versions. These are the ﬁrst schemes
to show that constant expansion factor, perfect hiding, and perfect bind-
ing can be obtained for universally composable commitments.
We also show   how  the schemes can be applied to do eﬃcient zero-
knowledge proofs of knowledge that are universally composable.

1   Introduction

The notion of commitment is one of the most fundamental primitives in both
theory and practice of modern cryptography. In a commitment scheme, a        com-
mitter chooses an element   m  from  some ﬁnite set  M, and releases some infor-
mation about  m  through a commit protocol to a    receiver. Later, the committer
may release more information to the receiver to open his commitment, so that
the receiver learns m. Loosely speaking, the basic properties we want are ﬁrst
that the commitment scheme is    hiding: a cheating receiver cannot learn m  from

  Basic Research in Computer Science,
  Centre of the Danish National Research Foundation.

M. Yung (Ed.): CRYPTO 2002, LNCS 2442, pp. 581–596, 2002.
 c Springer-Verlag Berlin Heidelberg 2002
582   Ivan Damg˚ard and Jesper Buus Nielsen

the commitment protocol, and second that it is binding: a cheating committer
cannot change his mind about m, the veriﬁer can check in the opening that the
value opened was what the committer had in mind originally. Each of the two
properties can be satisﬁed unconditionally or relative to a complexity assump-
tion. A very large number of commitment schemes are known based on various
notions of security and various complexity assumptions.
   In [CF01] Canetti and Fischlin proposed a new security measure for commit-
ment schemes called universally composable commitments. This is a very strong
notion: it guarantees that security is maintained even when an unbounded num-
ber of copies of the scheme are running concurrently and asynchronous. It also
guarantees non-malleability and maintains security even if an adversary can de-
cide adaptively to corrupt some of the players and make them cheat. The new
security notion is based on the framework for universally composable security
in [Can01]. In this framework one speciﬁes desired functionalities by specifying
an idealized version of them. An idealized commitment scheme is modeled by
assuming a trusted party to which both the committer and the receiver have a
secure channel. To commit to m, the committer simply sends m to the trusted
party who notiﬁes the receiver that a commitment has been made. To open,
the committer asks the trusted party to reveal m to the receiver. Security of
a commitment scheme now means that the view of an adversary attacking the
scheme can be simulated given access to just the idealized functionality.
   It is clearly important for practical applications to have solutions where only
the two main players need to be active. However, in [CF01] it is shown that
universal composability is so strong a notion that no universally composable
commitment scheme for only two players exist. However, if one assumes that a
common reference string with a prescribed distribution is available to the players,
then two-player solutions do exist and two examples are given in [CF01]. Note
that common reference strings are often available in practice, for instance if a
public key infrastructure is given.
   The commitment scheme(s) from [CF01] uses Ω(k) bits to commit to one bit,
where k is a security parameter, and it guarantees only computational hiding
and binding. In fact, as detailed later, one might even get the impression from
the construction that perfect hiding, respectively binding cannot be achieved.
Here, by perfect, we mean that an unbounded receiver gets zero information
about m, respectively an unbounded committer can change his mind about m
with probability zero.
   Our contribution is a new construction of universally composable commit-
ment schemes, which uses O(k) bits of communication to commit to  k bits.
The scheme can be set up such that it is perfectly binding, or perfectly hiding,
without loosing eﬃciency1. The construction is based on a new primitive which
we call a mixed commitment scheme. We give a general construction of mixed

1 [CF01] also contains a scheme which is statistically binding and computationally
  hiding, the scheme however requires a new setup of the common reference string per
  commitment and is thus mostly interesting because it demonstrates that statistically
  binding can be obtained at all.
   Universally Composable Commitment Schemes with Constant Expansion 583

commitments, based on any family of so called extractable q one-way homomor-
phisms, and show two eﬃcient implementations of this primitive, one based on
the Paillier cryptosystem and one based on the Okamoto-Uchiyama cryptosys-
tem. A third example based on the DDH assumption is less eﬃcient, but still
supports perfect hiding or binding. Our commitment protocol has three moves,
but the two ﬁrst messages can be computed independently of the message com-
mitted to and thus the latency of a commitment is still one round as in [CF01].
We use a “personalized” version of the common reference string model where
each player has a separate piece of the reference string assigned to him. It is an
open question if our results can also be obtained with a reference string of size
independent of the number of players.
   As a ﬁnal contribution we show that if a mixed commitment scheme comes
with protocols in a standard 3-move form for proving in zero-knowledge rela-
tions among committed values, the resulting UCC commitment scheme inherits
these protocols, such that usage of these is also universally composable. For our
concrete schemes, this results in eﬃcient protocols for proving binary Boolean
relations among committed values and also (for the version based on Paillier en-
cryption) additive and multiplicative relations modulo N. We discuss how this
can be used to construct eﬃcient universally composable zero-knowledge proofs
of knowledge for NP, improving the complexity of a corresponding protocol from
[CF01].

An Intuitive Explanation of Some Main Ideas.      In the simplest type of
commitment scheme, both committing and opening are non-interactive, so that
committing just consists of running an algorithm commitK , keyed by a public
key K, taking as input the message m to be committed to and a uniformly
random string r. The committer computes c ← commitK (m, r), and sends c to
the receiver. To open, the committer sends m and r to the receiver, who checks
that c = commitK (m, r). For this type of scheme, hiding means that given just
c the receiver does not learn m and binding means that the committer cannot
change his mind by computing m ,r , where c = commit(m ,r ) and m  = m.
   In a trapdoor scheme however, to each public key K a piece of trapdoor
information tK is associated which, if known, allows the committer to change
his mind. We will call such schemes equivocable. One may also construct schemes
where a diﬀerent type of trapdoor information dK exists, such that given dK , one
can eﬃciently compute m from commitK (m, r). We call such schemes extractable.
Note that equivocable schemes cannot be perfect binding and that extractable
schemes cannot be perfect hiding.
   As mentioned, the scheme in [CF01] guarantees only computational binding
and computational hiding. Actually this is important to the construction: to
prove security, we must simulate an adversary’s view of the real scheme with
access to the idealized functionality only. Now, if the committer is corrupted by
the adversary and sends a commitment c, the simulator must ﬁnd out which
message was committed to, and send it to the idealized functionality. The uni-
versally composable framework makes very strict demands to the simulation
implying that rewinding techniques cannot be used for extracting the message.
584   Ivan Damg˚ard and Jesper Buus Nielsen

A solution is to use an extractable scheme, have the public key K in the ref-
erence string, and set things up such that the simulator knows the trapdoor
dk. A similar consideration leads to the conclusion that if instead the receiver is
corrupt, the scheme must be equivocable with trapdoor tK known to the simula-
tor, because the simulator must generate a commitment on behalf of the honest
committer before ﬁnding out from the idealized functionality which value was
actually committed to. So to build universally composable commitments it seems
we must have a scheme that is simultaneously extractable and equivocable. This
is precisely what Canetti’s and Fischlin’s ingenious construction provides.
   In this paper, we propose a diﬀerent technique for universally composable
commitments based on what we call a mixed commitment scheme. A    mixed
commitment scheme is basically a commitment scheme which on some of the
keys is perfectly hiding and equivocable, we call these keys the E-keys, and on
some of the keys is perfectly binding and extractable, we call these keys the
X-keys. Clearly, no key can be both an X- and an E-key, so if we were to put the
entire key in the common reference string, either extractability or equivocability
would fail and the simulation could not work. We remedy this by putting only
a part of the key, the so-called system key, in the reference string. The rest of
the key is set up once per commitment using a two-move protocol. This allows
the simulator to force the key used for each commitment to be an E-key or an
X-key depending on whether equivocability or extractability is needed.
   Our basic construction is neither perfectly binding nor perfectly hiding be-
cause the set-up of keys is randomized and is not guaranteed to lead to any
particular type of key. However, one may add to the reference string an extra
key that is guaranteed to be either an E- or an X-key. Using this in combination
with the basic scheme, one can obtain either perfect hiding or perfect binding.

2   Mixed Commitments

We now give a more formal description of mixed commitment schemes. The most
important diﬀerence to the intuitive discussion above is that the system key N
comes with a trapdoor tN that allows eﬃcient extraction for all X-keys. The
E-keys, however, each come with their own trapdoor for equivocability.
Deﬁnition 1.  By a mixed commitment scheme we mean a commitment scheme
commitK  with some global system key N, which determines the message space
MN  and the key space KN of the commitments. The key space contains two sets,
the E-keys and the X-keys, for which the following holds:
Key generation   One can eﬃciently generate a system key N along with the so-
   called X-trapdoor tN . One can, given the system key N, eﬃciently generate
   random keys from KN  and given tN one can sample random X-keys. Given
   the system key, one can eﬃciently generate an E-key K along with the so-
   called E-trapdoor tK .
Key indistinguishability  Random  E-keys and random X-keys are both com-
   putationally indistinguishable from random keys from KN as long as the
   X-trapdoor tN is not known.
   Universally Composable Commitment Schemes with Constant Expansion 585

Equivocability  Given E-key K and E-trapdoor tK one can generate fake com-
   mitments  c, distributed exactly as real commitments, which can later be
   opened arbitrarily, i.e. given a message m one can compute uniformly ran-
   dom  r for which c = commitK (m, r).
Extraction  Given a commitment  c = commitK  (m, r), where K is an X-key,
   one can given the X-trapdoor tN eﬃciently compute m.
   Note that the indistinguishability of random E-keys, random X-keys, and
random keys from KN  implies that as long as the X-trapdoor is not known the
scheme is computationally hiding for all keys and as long as the E-trapdoor is
not known either the scheme is computationally binding for all keys.
   For the construction in the next section we will need a few special require-
ments on the mixed commitment scheme. First of all we will assume that the
message space MN   and the key space KN are ﬁnite groups in which we can
compute eﬃciently. We denote the group operation by +. Second we need that
the number of E-keys over the total number of keys is negligible and that the
number of X-keys over the total number of keys is negligibly close to 1. Note that
this leaves only a negligible fraction which is neither X-keys nor E-keys. We call
a mixed commitment scheme with these properties a special mixed commitment
scheme.
   The last requirement is that the scheme has two ’independent’ E-trapdoors
per E-key. We ensure this by a transformation. The keys will be of the form
(K1,K2). We let the E-keys be the pairs of E-keys and let the X-keys be the pairs
of X-keys. The message space will be the same. Given a message m we commit as

(commitK1 (m1), commitK2 (m2)), where m1 and m2 are uniformly random values
for which m = m1 + m2. If both keys are E-keys and the E-trapdoor of one of
them, say Kb, is known a fake commitment is made by committing honestly to
random m1−b under K1−b  and making a fake commitment cb under Kb. Then to
open to m,opencb to mb = m1−b −m. Note that the distribution of the result is
independent of b – this will become essential later. All requirements for a special
mixed commitment scheme are maintained under the transformation.

Special Mixed Commitment Scheme Based on        q One-Way Homomor-
phisms.  Our examples of special mixed commitment schemes are all based on
q one-way homomorphism generators, as deﬁned in [CD98]. Here we extend the
notion to extractable q one-way homomorphisms. In a nutshell, we want to look
at an easily computable homomorphism  f : G →  H between Abelian groups
G, H such that H/f(G) is cyclic and has only large prime factors in its order.
And such that random elements in f(G) are computationally indistinguishable
from random elements chosen from all of H (which in particular implies that f
is hard to invert). However, given also a trapdoor associated with f, it becomes
easy to extract information about the status of an element in H.
   More formally, a family of extractable q one-way homomorphisms is given by
a probabilistic polynomial time (PPT) generator G which on input 1k outputs
a (description of a) tuple (G, H, f, g, q, b, b ,t), where G and H are groups, f :
G → H  is an eﬃciently computable homomorphism, g ∈ H \ f(G), q, b, b  ∈ N,
586   Ivan Damg˚ard and Jesper Buus Nielsen

and t is a string called the trapdoor. Let F = f(G). We require that gF gen-
erates the factor group H/F and let ord(g)=|H/F|. We require that ord(g)
is superpolynomial in k (e.g. 2k), that q is a multiple of ord(g), and that b is a
public lower bound on ord(g), i.e., we require that 2 ≤ b ≤ ord(g) ≤ q.Wesay
that a generator has public order if b = ord(g)=q. Also b  is superpolynomial
in k (e.g. 2k/2) and it is a public lower bound on the primefactors in ord(g),
i.e., all primefactors in ord(g) are at least b . We write operations in G and H
multiplicatively and we require that in both groups one can multiply, exponenti-
ate, take inverses, and sample random elements in PPT given (G, H, f, g, q, b, b ).
The ﬁnal central requirements are as follows:
Indistinguishability. Random elements from F are computationally indistin-
   guishable from random elements from H given (G, H, f, g, q, b, b ).
Extractability. This comes in two ﬂavors. We call the generator fully ex-
   tractable if given (G, H, f, g, q, b, b ,t) and y = gif(r) one can compute
   i mod ord(g) in PPT. Note that, given (G, H, f, g, q, b, b ,t), one can compute
   ord(g) easily. We call a generator 0/1-extractable if given (G, H, f, g, q, b, b ,t)
   and y = gif(r) one can determine whether i = 0 in PPT.
q-invertibility Given (G, H, f, g, q, b, b ) and y ∈ H, it is easy to compute x
   such that yq = f(x). Note that this does not contradict indistinguishability:
   since q is a multiple of ord(g), it is always the case that yq ∈ F .
   We give three examples of extractable q one-way homomorphism generators:

Based on Paillier encryption:  Let n = PQ   be an RSA modulus, where  P
                                       Z∗           Z∗
   and Q  are k/2-bit primes. Let G =   n, let H =    n2 , and let f(r)=
   rn mod n2. Let g =(n + 1), let b = q = n, b  =2k/2−1,and let t =(P, Q).
   Then it follows directly from [Pai99] that relative to the DCRA assumption
   we have a fully extractable generator with public order.
Based on Okamoto-Uchiyama encryption:       Now let N  = Pn  = P 2Q. Let
        Z∗          Z∗                 N
   G =   n, let H =  N , and let f(r)=r  mod  N. Let g =(N  + 1), q = N,
   b = b  =2k/2−1 and  t =(P, Q). Then it follows directly from [OU98] that
   relative to the p-subgroup assumption we have a fully extractable generator.
Based on Diﬃe-Hellman encryption:      Let α be a group of prime order Q.
            x                       ∈ Z∗           Z            ×  
   Let β = α  for uniformly random x    Q. Let G =  Q, let H = α     α ,
   and let f(r)=(αr,βr). Let  g =(1,β), b =  b  = q = Q and t = x. Then
   the scheme is 0/1-extractable: let (A, B)=gmf(r)=(αr,βr+m), then Ax =
   B  iﬀ m =  0. Relative to the DDH assumption we have a 0/1-extractable
   generator with public order.

   We now show how to transform an extractable generator into a special mixed
commitment scheme. We treat fully extractable and 0/1-extractable generators
in parallel, as the diﬀerences are minimal.
   The key space will be H, the message space will be Zb for fully extractable
schemes and Z2 for 0/1-extractable schemes. We commit as commitK (m, r)=
Kmf(r), where  r is uniformly random in H. The E-keys will be the set F =
f(G) and the E-trapdoor will be f −1(K). By the requirement that ord(g)is
   Universally Composable Commitment Schemes with Constant Expansion 587

superpolynomial in k, the set of E-keys is a negligible fraction of the keyspace
as required. For equivocability, we generate a fake commitment as c = f(rc) for
uniformly random rc ∈ H. Assume that K = f(rK ) and that we are given m ∈
Z                −m
 b. Compute r = rK  rc. Then r is uniformly random and c = commitK (m, r).
   For a fully extractable generator the X-keys will be the elements of form
      i
K =  g f(rK ), where i is invertible in Zord(g). By the requirement that ord(g)
only has large primefactors, the X-keys are the entire key-space except for a
negligible fraction as required. They can be sampled eﬃciently given the trap-
door since then ord(g) is known. Assume that we are given c = Kmf(r) for
m ∈ Zb. Using fully extractability we can from c compute im mod ord(g) and
from K we can compute  i mod ord(g). Since i is invertible we can then com-
pute m mod ord(g)=m.Fora0/1-extractable generator the X-keys will be
                              i
the elements of the form K = g f(rK ), where i ∈ Zord(g) \{0}. By the 0/1-
extractability these keys can be eﬃciently sampled given t. For extraction, note
that commitK (0,r) ∈ F and commitK (1,r) ∈ F and use the 0/1-extractability
of the generator. For the fully extractable construction and the 0/1-extractable
construction, the indistinguishability of the key-spaces follows directly from the
indistinguishability requirement on the generator. The transformed scheme is
                                         m1        m2
given by commitK1,K2 (m, (r1,r2,m1))=(K1   f(r1),K2  f(r2)), where m2 =
m − m1 mod  q.

Proofs of Relations.  For the mixed commitment schemes we exhibit in this
paper, there are eﬃcient protocols for proving in zero-knowledge relations among
committed values. As we shall see, it is possible to have the derived universally
composable commitment schemes inherit these protocols while maintaining uni-
versal composability. In order for this to work, we need the protocols to be
non-erasure Σ-protocols.
   A non-erasure Σ-protocol for relation R is a protocol for two parties, called the
prover P and the veriﬁer V . The prover gets as input (x, w) ∈ R, the veriﬁer gets
as input x, and the goal is for the prover to convince the veriﬁer that he knows
w such that (x, w) ∈ R, without revealing information about w. We require that
it is done using a protocol of the following form. The prover ﬁrst computes a
message a ← A(x, w, ra), where ra is a uniformly random string, and sends a to
V . Then V returns a random challenge e of length l. The prover then computes
a responds to the challenge z ← Z(x, w, ra,e), and sends z to the veriﬁer. The
veriﬁer then runs a program B on (x, a, e, z) which outputs b ∈{0, 1} indicating
where to believe that the prover knows a valid witness w or not. Besides the
protocol being of this form we furthermore require that the following hold:

Completeness   If (x, w) ∈ R, then the veriﬁer always accepts (b = 1).
Special honest veriﬁer zero-knowledge   There exists a PPT algorithm, the
   honest veriﬁer simulator hvs, which given instance x (where there exists w
   such that (x, w) ∈ R) and any challenge e generates (a, z) ← hvs(x, e, r),
   where r is a uniformly random string, such that (x, a, e, z) is distributed
   identically to a successful conversation where e occurs as challenge.
 588   Ivan Damg˚ard and Jesper Buus Nielsen

 State construction  Given (x, w, a, e, z, r), where (a, z)=hvs(x, e, r) and
    (x, w) ∈ R it should be possible to compute uniformly random ra for which
    a = A(x, w, ra) and z = Z(x, w, ra,e).
 Special soundness  There exists a PPT algorithm, which given x,(a, e, z), and
    (a, e ,z ), where e = e , B(x, a, e, z)=1,andB(x, a, e ,z ) = 1, outputs w
    such that (x, w) ∈ R.

    In [Dam00] it is shown how to use Σ-protocols in a concurrent setting. This
 is done by letting the ﬁrst message be a commitment to a and then letting the
 third message be (a, r, z), where (a, r) is an opening of the commitment and z is
 computed as usual. If the commitment scheme used is a trapdoor commitment
 scheme this will allow for simulation using the honest veriﬁer simulator. In an
 adaptive non-erasure setting, where an adversary can corrupt parties during
 the execution, it is also necessary with the State Construction property as the
 adversary is entitled to see the internal state of a corrupted party.

 Proofs of Relations for the Schemes Based on    q One-Way Homomor-
 phisms. The basis for the proofs of relations between commitments will be the
 following proof of knowledge which works for fully extractable generators with
 public order, so we have (G, H, f, g, q, b, b ,t), with b = ord(g)=q. Assume that
 the prover is given K ∈ H, m ∈ Zb and r ∈ G, and the veriﬁer is given K and
 C = Kmf(r). To prove knowledge of m, r, we do as follows:

                           m
 1. The prover sends C = K  f(r) for uniformly random m ∈ Zq and r ∈ G.
                                                                   
 2. The veriﬁer sends a uniformly random challenge e from Zb  , where b is the
    public bound on the smallest primefactor in ord(g).
 3. The prover replies withm ˜ = em + m mod q andr ˜ = f −1(Kq)˜irer, where
    ˜i = em + m div q. The veriﬁer accepts iﬀ Km˜ f(˜r)=CeC.

    We argue that this is a non-erasure Σ-protocol: The completeness is imme-
 diate. For special soundness assume that we have two accepting conversations
 (C,e,m,˜ r˜) and (C,e , m˜  , r˜ ). By the requirement that b  is smaller than the
 smallest primefactor of q we can compute α, β s.t. 1 = αq + β(e − e ). By our
                                  −1   q            −1   q
 assumptions, we can compute rc = f (C  ) and rK = f  (K ). Then compute
        −                                 β α    n div q           m
 n =(˜m   m˜ )β, m = n mod q, and r =(˜r/r˜ ) rc (rK ) . Then C = K f(r).
 For special honest veriﬁer zero-knowledge, given C and e, pickm ˜ ∈ Zq andr ˜ ∈ G
 at random and let C = Km˜ f(˜r)C−e. For the state construction, assume that
 we are then given m, r such that C = Kmf(r). Then let m =˜m − em mod  q,
˜i = em + m div q, r =˜rf−1(Kq)˜ir−e. Then all values have the correct distribu-
 tion.
    We extend this scheme to prove relations between committed values. Assume
                                                   l
 that the prover knows K1,m1,r1,...,Kl,ml,rl where i=1 aimi = a0 mod q for
          ∈ Z                                                  mi
 a0,...,al   q, and assume that the veriﬁer knows Ki and Ci = Ki f(ri) for
 i =1,...,l and knows a0,...,al. The prover proves knowledge as follows: Run
 a proof of knowledge as that described above for each of the commitments using
 the same challenge e in them all. Letm ˜ i be them ˜ -value of the protocol for Ci.
   Universally Composable Commitment Schemes with Constant Expansion 589
                                             
                                               l
We furthermore instruct the veriﬁer to check that i=1 aim˜ i = ea0. For special
soundness assume that we have accepting conversations for the two challenges
                                   −       −    −1
e = e . Then we can compute mi =(˜mi m˜ i)(e e )  mod  q andri as above
                 mi                    l            −    −1   l       −
such that Ci = Ki f(ri). Furthermore  i=1 aimi =(e   e )  (  i=1 aim˜ i
  l            −    −1    −  
  i=1 aim˜ i)=(e e ) (ea0  e a0)=a0. The other properties of a non-erasure
Σ-protocol follows using similar arguments.
   This handles proofs of knowledge for the basic scheme. Recall, however, that
in our UCC construction we need a transformed scheme where pairs of basic
commitments are used, as described above. So assume, for instance, that we
are given transformed commitments (C1,C2), (C3,C4), (C5,C6) and we want to
prove that the value committed to by (C1,C2) is the sum modulo  q of the
values committed by (C3,C4) and (C5,C6). This can be done by using the above
protocol to prove knowledge of m1,...,m6 contained in C1,...,C6 such that
(m1 +m2)−(m3  +m4)−(m5   +m6) = 0. All linear relations between transformed
commitments can be dealt with in a similar manner.
   By extending the proof of multiplicative relations from [CD98] in a manner
equivalent to what we did for the additive proof we obtain a non-erasure Σ-
protocol for proving multiplicative relations between transformed commitments.
   Now for schemes without public order, the Σ-protocols given above do not
directly apply because we were assuming that b = ord(g)=q. However, we can
modify the basic protocol by setting m = m =˜m = 0. This results in a non-
erasure Σ-protocol which allows the prover to prove knowledge of r, where C =
f(r) is known by the veriﬁer. I.e. the prover can prove that C ∈ F , in other words
that C commits to 0. Given C = Kf(r) the prover can using the same protocol
prove that CK−1 ∈ F , i.e. prove that C ∈ KF, in other words that C commits
to 1. Using the technique from [CDS94] for monotone logical combination of
Σ-protocols we can then combine such proofs. Let C1,...,Cl be commitments
           {  i     i }a  ⊂{    }l
and let R = (b1,...,bl) i=1  0, 1 be a Boolean relation. We can then prove
                                    ∈                a   l     ∈   mi
that C1,...,Cl commits to (m1,...,ml) R  by proving  i=1 i=1 Ci  K   F .
Let in particular 0 = {(0, 0), (1, 1)} and let 1 = {(0, 1), (1, 0)}. Then proving
knowledge of (m1,m2) ∈ 0 for transformed commitment  C =(C1,C2) proves
that C commits to 0, similar for 1. Then using the relation And = 0 × 0 × 0 ∪
0×0×1∪0×1×0∪1×1×1, we can prove that three transformed commitments
C1,C2,C3 commits to bits m1,m2,m3  s.t. m1 = m2 ∧ m3. All Boolean relations
of arity O(log(k)) can handled in a similar manner. This will work for both fully
and 0/1-extractable schemes.
   The following theorem summarizes what we have argued:

Theorem   1. If there exists a fully (0/1) extractable q one-way homomorphism
generator, then there exists a special mixed commitment scheme with message
space Zb (Z2) as described above and with proofs of relations of the form m =
f(m1,m2,...,ml)  where f is a Boolean predicate and l = O(log(k)). If the
scheme is with public order b = ord(g)=q and is fully extractable, we also have
proofs of additive and multiplicative relations modulo q.
590   Ivan Damg˚ard and Jesper Buus Nielsen

3   Universally Composable Commitments

In the framework from [Can01] the security of a protocol is deﬁned by comparing
its real-life execution to an ideal evaluation of its desired behavior.
   The protocol π is modeled by n interactive Turing Machines P1,...,Pn called
the parties of the protocol. In the real-life execution of π an adversary A and an
environment Z modeling the environment in which A is attacking the protocol
participates. The environment gives inputs to honest parties, receives outputs
from honest parties, and can communication with A at arbitrary points in the
execution. The adversary can see all messages and schedules all message deliver-
ies. The adversary can corrupted parties adaptively. When a party is corrupted,
the adversary learns the entire execution history of the corrupted party, includ-
ing the random bits used, and will from the point of corruption send messages
on behalf of the corrupted party. Both A and Z are PPT interactive Turing
Machines.
   Second an ideal evaluation is deﬁned. In the ideal evaluation an ideal func-
tionality F is present to which all the parties have a secure communication line.
The ideal functionality is an interactive Turing Machine deﬁning the desired
input-output behavior of the protocol. Also present is an ideal model adversary
S, the environment Z, and n so-called dummy parties P˜1,...,P˜n – all PPT in-
teractive Turing Machines. The only job of the dummy parties is to take inputs
from the environment and send them to the ideal functionality and take mes-
sages from the ideal functionality and output them to the environment. Again
the adversary schedules all message deliveries, but can now not see the contents
of the messages. This basically makes the ideal process a trivially secure protocol
with the same input-output behavior as the ideal functionality. The framework
also deﬁnes the hybrid models, where the execution proceeds as in the real-life
execution, but where the parties in addition have access to an ideal functionality.
An important property of the framework is that these ideal functionalities can
securely be replaced with sub-protocols securely realizing the ideal functional-
ity. The real-life model including access to an ideal functionality F is called the
F-hybrid model.
   At the beginning of the protocol all parties, the adversary, and the envi-
ronment is given as input the security parameter k and random bits. Further-
more the environment is given an auxiliary input z. At some point the envi-
ronment stops activating parties and outputs some bit. This bit is taken to
be the output of the execution. We use REALπ,A,Z (k, z) to denote the output
of Z in the real-life execution and use IDEALF,S,Z (k, z) to denote the output
of Z in the ideal evaluation. Let REALπ,A,Z denote the distribution ensemble
{REALπ,A,Z (k, z)}k∈N,z∈{0,1}∗ and let IDEALF,S,Z (k, z) denote the distribu-
tion ensemble {IDEALF,S,Z (k, z)}k∈N,z∈{0,1}∗ .

Deﬁnition 2 ([Can01]).  We say that  π securely realizes F if for all real-life
adversaries A there exists an ideal model adversary S such that for all environ-
ments Z we have that IDEALF,S,Z  and REALπ,A,Z  are computationally indis-
tinguishable.
   Universally Composable Commitment Schemes with Constant Expansion 591

   An important fact about the above security notion is that it is maintained
even if an unbounded number of copies of the protocol (and other protocols)
are carried out concurrently – see [Can01] for a formal statement and proof. In
proving the composition theorem it is used essentially that the environment and
the adversary can communicate at any point in an execution. The price for this
strong security notion, which is called universal composability in [Can01], is that
rewinding cannot be used in the simulation.

The Commitment Functionality.      We now specify the task that we want
to implement as an ideal functionality. We look at a slightly diﬀerent version
of the commitment functionality than the one in [CF01]. The functionality in
[CF01] is only for committing to one bit. Here we generalize. The domain of
our commitments will be the domain of the special mixed commitment used in
the implementation. Therefore the ideal functionality must specify the domain
by initially giving a system key N. For technical reasons, in addition, the X-
trapdoor of N is revealed to the the ideal model adversary, i.e., the simulator.
This is no problem in the ideal model since here the X-trapdoor cannot be
used to ﬁnd committed values – the ideal functionality stores committed values
internally and reveals nothing before opening time. The simulator, however,
needs the X-trapdoor in order to do the simulation of our implementation. The
implementation, on the other hand, will of course keep the X-trapdoor of N
hidden from the real-life adversary. The ideal functionality for homomorphic
commitments is named FHCOM   and is as follows.

0. Generate a uniformly random system key N along with the X-trapdoor tN .
   Send N  to all parties and send (N,tN ) to the adversary.
1. Upon receiving (commit, sid, cid, Pi,Pj,m) from P˜i, where m is in the do-
   main  of system  key N, record  (cid, Pi,Pj,m) and  send the message
   (receipt, sid, cid, Pi,Pj)toP˜j and the adversary. Ignore subsequent
   (commit, sid, cid,...) messages. The values sid and cid are a session id and
   a commitment id.
2. Upon receiving the message (prove, sid, cid, Pi,Pj,R,cid1,...,cida) from
   P˜i, where (cid1,Pi,Pj,m1), ...,(cida,Pi,Pj,ma) have been recorded, R is
   an a-ary relation with a non-erasure Σ-protocol, and (m1,m2,...,ma) ∈ R,
   send the message (prove, sid, cid, Pi,Pj,R,cid1,...,cida)toP˜j and the ad-
   versary.
3. Upon receiving a message (open, sid, cid, Pi,Pj) from P˜i, where (cid, Pi,
   Pj,m) has been recorded, send the message (open, sid, cid, Pi,Pj,m)toP˜j
   and the adversary.

   It should be noted that a version of the functionality where N and tN are not
speciﬁed by the ideal functionality could be used. We could then let the domain
of the commitments be a domain contained in (or easy to encode in) the domain
of all the system keys.

The Common Reference String Model.       As mentioned in the introduction
we cannot hope to construct two-party UCC in the plain real-life model. We
592   Ivan Damg˚ard and Jesper Buus Nielsen

need a that a common reference string (CRS) with a prescribed distribution
is available to the players. It is straightforward to model a CRS as an ideal
functionality FCRS, see e.g. [CF01].

4   UCC with Constant Expansion Factor

Given a special mixed commitment scheme com we construct the following pro-
tocol UCCcom.

The CRS   The CRS is (N,K1,...,Kn), where   N is a random system key and
   K1,...,Kn  are n random E-keys for the system key N, Ki for Pi.
Committing

  C.1 On input (commit, sid, cid, Pi,Pj,m) party Pi generates a random com-
       mitment key  K1  for system  key N  and  commits to it as  c1  =
                                  com                   2
       commitKi (K1,r1), and sends (  1, sid, cid, c1)toPj .
  R.1  Pj replies with (com2, sid, cid, K2) for random key K2.
  C.2  Pi computes K   = K1  + K2  and c2  = commitK  (m, r2) for random
       r2, and   records (sid, cid, Pj,K,m,r2) and  sends  the  message
       (com3, sid, cid, K1,r1,c2)toPj.

  R.2  Pj checks that c1 = commitKi (K1,r1), and if so computes K = K1 +K2,
       records (sid, cid, Pj,K,c2), and outputs (receipt, sid, cid, Pi,Pj).
Opening

  C.3 On input (open, sid, cid, Pi,Pj), Pi sends (open, sid, cid, m, r2)toPj.
  R.3  Pj checks that c2 = commitK (m, r2), and if so outputs (open, sid, cid, Pi,
       Pj,m).
Proving Relation

  C.4 On input (prove, sid, cid, Pi,Pj,R,cid1,...,cida), where (sid, cid1,Pj,
       K1,m1,r1), ...,(sid, cida,Pj,Ka,ma,ra) are recorded commitments,
       compute the ﬁrst message, a,oftheΣ-protocol from the recorded wit-
                                                                   prv
       nesses and compute c3 = commitKi (a, r3) for random r3 and send ( 1,
       sid, cid, R, cid1,...,cida,c3)toPj.
                                                 prv
  R.4  Pj generates a random challenge e and sends ( 2, sid, cid, Pj,e)toPi.
                                         prv
  C.5  Pi computes the answer z and sends ( 3, sid, cid, a, r3,z)toPj.

  R.5  Pj checks that c3 = commitKi (a, r3) and that (a, e, z) is an accepting
       conversation. If so Pj outputs (prove, sid, cid, Pi,Pj,R,cid1,..., cida).

Theorem   2. If com is a special mixed commitment scheme, then the protocol
UCCcom securely realizes FHCOM in the CRS-hybrid model.

2 We assume that the key space is a subset of the message space. If this is not the
                                                      l
  case the message space can be extended to a large enough MN by committing to l
  values in the original scheme.
   Universally Composable Commitment Schemes with Constant Expansion 593

Proof. We construct a simulator S running a real-life adversary A and simulates
to it a real-life execution consistent with the values input to and output from the
ideal functionality in ideal-world in which S is running. The main requirements
is that S given |m| can simulate a commitment to m in such a way that it can
later open the commitment to any value of m; That S can extract from the
commitments given by A the value committed to; And that S does not rewind
A as this is not possible in the model from [Can01].
   The simulator S sets up the CRS s.t. the keys Ki are E-keys for which S
knows the E-trapdoor, and such that the X-trapdoor is known too. When S is
simulating a honest party acting as the committing party it use the E-trapdoor
to open c1 to K1 = K  − K2, where K  is generated as a random E-key with
known E-trapdoor. Then S generates c2 as an equivocable commitment, which
it can later open to the actual committed value once it becomes known. When
S is simulating a honest party acting as the receiver in a commitment it simply
follows the protocol. Since no trapdoors are known to the adversary, the resulting
key K will be random and in particular it will be an X-key except with negligible
probability since all but a negligible fraction of the keys are X-keys. So, since
S knows the X-trapdoor, it can compute from the c2 sent by the adversary the
value m committed to except with negligible probability.
   For the proofs of relations, when A is giving a proof, S simply follows the
protocol. The proofs given by honest Pi are simulated by S. Here the non-
rewinding simulation technique from [Dam00] applies. If the party Pi is later
corrupted, the messages which should have been committed to are learned. Us-
ing the E-trapdoor the simulator then opens the commitments in the relation
appropriately. Then given the messages and the random bits (the witnesses of
the proof), the state construction property of the proof allows to construct a
consistent internal state to show to the adversary.
   The main technical problem in proving this simulation indistinguishable from
the real-life execution is that the X-trapdoor is used by the simulator, so we
cannot do reductions to the computational binding of the mixed commitment
scheme. We deal with this by deﬁning a hybrid distribution that is generated by
the following experiment: Run the simulation except do not use the X-trapdoor.
Each time the adversary makes a commitment instead simply use the message
0 as input to the ideal functionality. When the adversary then opens the com-
mitment to m, simply change the ideal-world communication to make it look as
if m was the committed value. Up to the time of opening, the entire execution
seen from the the environment and A is independent of whether 0 or m was
given to the ideal functionality, and hence this hybrid is distributed exactly as
the simulation. It is therefore enough to prove this hybrid indistinguishable from
the real-life execution, which is possible using standard techniques. 

Perfect Hiding and Perfect Binding.   The scheme described above has nei-
ther perfect binding nor perfect hiding. Here we construct a version of the com-
mitment scheme with both perfect hiding and perfect binding. The individual
commitments are obviously not simultaneously perfect hiding and perfect bind-
ing, but it can be chosen at the time of commitment whether a commitment
594   Ivan Damg˚ard and Jesper Buus Nielsen

should be perfect binding or perfect hiding and proofs of relations can include
both types of commitments. We sketch the scheme and the proof of its security.
The details are left to the reader.
   In the extended scheme we add to the CRS a random E-key KE and a random
X-key KX  (both for system key N). Then to do a perfect binding commitment
to m the committer will in Step C.2 compute c2 = commitK (m, r2) as before,

but will in addition compute c3 = commitKX (m, r3). To open the commitment
the committer will then have to send both a correct opening (m, r2)ofc2 and a
correct opening (m, r3)ofc3. This is perfect binding as the X-key commitment
is perfect binding.
   To do a perfect hiding commitment the committer computes a uniformly
random  message m  and commits with  c2 = commitK  (m + m, r2) and c3 =

commitKE (m, r3). To open to m the committer must then send a correct opening
(m2,r2)ofc2 and a correct opening (m3,r3)ofc3 for which m2 = m3 + m. This
is perfect hiding because c3 hides m perfectly and m + m thus hides m perfectly.
   To do the simulation simply let the simulator make the excusable mistake of
letting KE be a random X-key and letting KX be a random E-key. This mistake
will allow to simulate and cannot be detected by the fact that E-keys and X-
keys are indistinguishable. For perfect binding commitments both K and KX
will then be E-keys when the simulator does a commitment, which allows to
fake. When the adversary does a commitment K will (except with negligible) be
an X-key and the simulator can extract m from commitK (m). For perfect hiding
commitments both  K  and KE  will (except with negligible probability) be X-
keys when the adversary does a commitment, which allows to extract. When
the simulator commits, K will be an E-key, which allows to fake an opening by
faking commitK (m).
   For perfect binding commitments the proofs of relations can be used directly
for the modiﬁed commitments by doing the proof on the commitK (m) values.
For perfect hiding commitments there is no general transformation that will
carry proofs of relations over to the modiﬁed system. If however there is a proof
of additive relation, then one can publish commitK (m) and prove that the sum

of the values committed to by commitK (m) and commitKE (m) is committed to
by commitK (m + m), and then use the commitment commitK  (m) when doing
the proofs of relations.

5   Eﬃcient Universally Composable
    Zero-Knowledge Proofs

In [CF01] Canetti and Fischlin showed how universally composable commitments
can be used to construct simple zero-knowledge (ZK) protocols which are uni-
versally composable. This is a strong security property, which implies concurrent
and non-malleable ZK proof of knowledge.
                   F R
   The functionality ZK for universally composable zero-knowledge (for binary
relation R) is as follows.
   Universally Composable Commitment Schemes with Constant Expansion 595

1. Wait to receive a value (verifier, id, Pi,Pj,x) from some party Pi. Once
   such a value is received, send (verifier, id, Pi,Pj,x)toS, and ignore all
   subsequent (verifier,...) values.
                                                                        
2. Upon receipt of a value (prover, id, Pj,Pi,x,w) from Pj, let v =1ifx = x
   and R(x, w) holds, and v = 0 otherwise. Send (id, v)toPi and S, and halt.

Exploiting the Multi-bit Commitment Property.       In [CF01] a protocol
                                                              F HC
for Hamiltonian-Cycle (HC) is given and proven to securely realize ZK . The
protocol is of a common cut-and-choose form. It proceeds in t rounds. In each
round the prover commits to l bits m ∈{0, 1}l. Then the veriﬁer sends a bit
b as challenge. If b = 0 then the prover opens all commitments and if b =1
the prover opens some subset of the challenges. Say that the subset is given
             l
by S ∈{0,  1} , where Si = 1 if commitment number   i should be revealed.
Then if b = 0 the prover should see m and if b = 1 the prover should see
(S, m ∧ S). The veriﬁer has two predicates V0 and V1 for verifying the reply
from the prover. If b = 0 it veriﬁes that V0(m) = 1 and if b = 1 it veriﬁes
that V1(S, m ∧ S) = 1. The protocol is such that seeing m or (S, m ∧ S) reveals
no knowledge about the witness (Hamiltonian cycle), but if V0(m)=1and
V1(S, m ∧ S) = 1, then one can compute a witness from m and S. The veriﬁer
accepts if it can verify the reply in each of the t rounds. Obviously S should
be kept secret when b = 0 – otherwise m and S would reveal the witness. This
makes it hard to use the multi-bit commitments to commit to the l bits in such
a way that just the subset S can be opened later. However, in [KMO89] Kilian,
Micali, and Ostrovski presented a general technique for transforming a multi-
bit commitment scheme into a multi-bit commitment scheme with the property
that individual bits can be open independently. Unfortunately their technique
adds one round of interaction. However, we do not need the full generality of
their result. This allows us to modify the technique to avoid the extra round of
interaction.
                                                           l
   We commit by generating a uniformly random pad m1 ∈{0, 1} and commit-
ting to the four values S, m1, m2 = m⊕m1, and m3 = m1 ∧S individually using
multi-bit commitments. The veriﬁer then challenges uniformly with b ∈{0, 1, 2}.
If b = 0, then reveal m1 and m2 and verify that V0(m1 ⊕ m2)=1.Ifb = 1 then
reveal S, m2, m3 and verify that V1(S, m2 ∧ S ⊕ m3) = 1. Finally, if b = 2, then
reveal S, m1, and m3 and verify that m3 = m1 ∧ S. This is still secure as at
no time are S and m revealed at the same time. For the soundness, note that if
V0(m1⊕m2)=1,V1(S, m2∧S⊕m3)=1,andm3         =  m1∧S, then for m = m1⊕m2
we have that V0(m)=1andV1(S, m   ∧ S) = 1 and can thus compute a witness.
If we increase the number of rounds by a factor log3/2(2) < 1.71 we will get
cheating probability no larger than for t rounds with cheating probability 1/2in
each round. The number of bits committed to in each round is 4l for a total of
less than 6.84tl bits. However, now the bits can be committed to k bits at a time
using the multi-bit commitment scheme. Therefore, if we implement the modi-
ﬁed protocol using our commitment scheme, we get communication complexity
O((l +k)t). This follows because we can commit to O(l) bits by sending O(l +k)
                                        lk
bits. This is an improvement by a factor θ( l+k )=θ(min(l, k)) over [CF01].
596    Ivan Damg˚ard and Jesper Buus Nielsen

Exploiting Eﬃcient Proofs of Relations.           We show how we can use the
eﬃcient proofs of relations on committed values to reduce the communication
complexity and the round complexity in a diﬀerent way. This can simply be
done by the parties agreeing in a Boolean circuit for the relation. Then the
prover commits to the witness and the evaluation of the circuit on the witness
and instance bit by bit and proves for each gate in the circuit that the committed
values are consistent with the gate. The commitment to the output gate is opened
and the prover then takes the revealed value as its output.
   This protocol will have no messages of its own. All interaction is done through
the ideal commitment functionality   FHCOM. Let    l be the size of the gate used.
This protocol requires  O(l) commitments to single bits, each of which require
O(k) bits of communication. Then we need to do      O(l) proofs of relations, each
of which require  O(k) bits of communication. This amounts to       O(lk) bits of
communication, and is an improvement over the        O(lkt) bits when using the
scheme of [CF01] by a factor  O(t).

References

Can01.    Ran Canetti. Universally composable security: A new paradigm for crypto-
          graphic protocols. In 42th Annual Symposium on Foundations of Computer
          Science. IEEE, 2001.
CD98.     Ronald Cramer and Ivan Damgaard. Zero-knowledge proofs for ﬁnite ﬁeld
          arithmetic, or: Can zero-knowledge be for free. In Hugo Krawczyk, editor,
          Advances in Cryptology - Crypto ’98, pages 424–441, Berlin, 1998. Springer-
          Verlag. Lecture Notes in Computer Science Volume 1462.
CDS94.    R. Cramer, I. B. Damg˚ard, and B. Schoenmakers. Proofs of partial knowl-
          edge and simpliﬁed design of witness hiding protocols. In Yvo Desmedt,
          editor, Advances in Cryptology - Crypto ’94, pages 174–187, Berlin, 1994.
          Springer-Verlag. Lecture Notes in Computer Science Volume 839.
CF01.     Ran Canetti and Marc Fischlin. Universally composable commitments. In
          J. Kilian, editor, Advances in Cryptology - Crypto 2001, pages 19–40, Berlin,
          2001. Springer-Verlag. Lecture Notes in Computer Science Volume 2139.
Dam00.    Ivan Damg˚ard. Eﬃcient concurrent zero-knowledge in the auxiliary string
          model. In Bart Preneel, editor, Advances in Cryptology - EuroCrypt 2000,
          pages 418–430, Berlin, 2000. Springer-Verlag. Lecture Notes in Computer
          Science Volume 1807.
KMO89.    Joe Kilian, Silvio Micali, and Rafail Ostrovsky. Minimum  resource zero-
          knowledge proofs (extended abstract). In 30th Annual Symposium on Foun-
          dations of Computer Science, pages 474–479, Research Triangle Park, North
          Carolina, 30 October–1 November 1989. IEEE.
OU98.     Tatsuaki Okamoto and Shigenori Uchiyama. A new public-key cryptosys-
          tem  as secure as factoring. In K. Nyberg, editor, Advances in Cryptology -
          EuroCrypt ’98, pages 308–318, Berlin, 1998. Springer-Verlag. Lecture Notes
          in Computer Science Volume 1403.
Pai99.    P. Paillier. Public-key cryptosystems based on composite degree residue
          classes. In Jacques Stern, editor, Advances in Cryptology - EuroCrypt ’99,
          pages 223–238, Berlin, 1999. Springer-Verlag. Lecture Notes in Computer
          Science Volume 1592.
