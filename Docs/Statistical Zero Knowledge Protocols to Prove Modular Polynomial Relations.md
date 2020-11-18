Statistical Zero Knowledge Protocols to Prove Modular Polynomial Relations

Eiichiro FUJISAKI and Tatsuaki OKAMOTO

Abstract. This paper proposes a bit commitment scheme, BC(-), and
e1~icient statistical zero knowledge (in short, SZK) protocols in which, for
any given multi-variable polynomial f(X1, ..,Xt) and any given modulus n, prover :P gives (I1,..,h) to verifier V and can convince V that P knows (x1,.., xt) satisfying f(x1,.., xt) -- 0 (mod n) and Ii = BC(xi),
(i = 1, .., t). The proposed protocols are O(Inl) times more efficient than
the corresponding previous ones [Dam93, Dam95, Oka95]. The (knowledge) soundness of our protocols holds under a computational assumption, the intractability of a modified RSA problem (see Def.3), while the
(statistical) zero-knowledgeness of the protocols needs no computational
assumption. The protocols can be employed to construct various practical cryptographic protocols, such as fair exchange, untraceable electronic
cash and verifiable secret sharing protocols.

1 Introduction

I.I Problem

In many cryptographic protocols, a party often wants to prove something related
to his secret while concealing his secret from the others. Such relations are often
specified by modular polynomials and bit commitments are very useful in such
protocols. This paper focuses on the following problem: for given multi-variable
polynomial f(X1,..,Xt) and modulus n, a party (prover) P gives (I1, ..,It) to
another party (verifier) V and convinces 1; that P knows (Xl, ..,xt) satisfying
](xl, .., xt) - 0 (mod u) and I, -- BC(xl), (i = 1, .., t), without revealing the
values, X 1 , .., Xt.

This problem is indeed raised on many cryptographic protocols. In fair exchange and contract signing protocols based on RSA signatures [Dam93, Dam95],
(n, e) is the public-key of the RSA scheme, f(x) = x e - m and I = BC(x).
After proving that 7 ~ knows x satisfying the relations, P releases x bit by bit
using I. In untraceable off-line electronic cash protocols, restricted blind signatures [Bra95, Oka95] play an important role, where, for instance, f(xl, x2, x3) =
(xlx2)x~ and 11 = BC(xl) and I2 = BC(x2). After proving that 7 ) knows
(Xl,X2,X3) satisfying the relations, ]; stores /1 and /2. If 7 ~ double-spends a
coin, ]; can get (Xl, x2) from xlx2 as evidence of double-spending (See [Oka95]
for more details). If f is a polynomial and n is a prime for Shamir's secret
sharing scheme, some protocols related to secret sharing such as (publicly) verifiable secret sharing [CGMA85, Ped91, Sta96] can be interpreted as this type of
problem. 

1.2 Previous Works

Theoretically, the general construction of protocols to solve the above-mentioned
problem has been already given assuming that a secure bit commitment scheme
exists. This is derived from the results of zero knowledge proof for NP-language
[GMRa89, GMW86, BCC86] and converting them to proof of knowledge [FFS88,
TW87, BG92]. Depending on the types of the underlying bit commitment schemes,
there exist two different results: namely, computational ZK (CZK) for interactive
proof (IP) and perfect ZK (PZK) for argument (eomputationally-sound proof).
However, those protocols are very inefficient in general.

In 1993, Damg&rd proposed the first efficient protocol to solve the problem
with a specific form for constructing a fair exchange and contract signing protocol [Dam93, Dam95]. He proposed the protocols in which prover :P can convince
verifier V that he knows s of bit commitment BC(s) and that it is a Rabin signature (s = m 1/2 mod n) or a RSA signature (s = m 1/~ rood n), for a message
m. The protocols are PZK computationally-sound proof of knowledge systems
(PZK arguments of knowledge). Those protocols essentially consist of some primitives: a bit commitment scheme and three protocols, which correspond to the
basic, comparing, and rood-multi protocols in this paper. His basic protocol is the
protocol in which P proves to l) that secret s is in a given range [a, b) and the
comparing and mod-multi protocols are compositions of the basic protocol. It is
easy to construct a PZK argument of knowledge for any multi-variable modular
polynomial (f, n) based on these primitives.

In 1995, Okamoto showed another application of the problem above. He constructed an RSA-type restricted blind signature for his untraceable off-line electronic cash [Oka95] by using similar primitives: a bit commitment scheme and
three protocols, which are essentially equivalent to those of Damgs except
for the bit commitment scheme.
Unfortunately, both of their protocols are not so efficient, because 13, in their
basic protocols, needs to request :P to open one of the commitments, BC(t) or
BC(t+s), many times (the so called cut-and-choose method).

1.3 Results

This paper gives a more efficient solution to the problem above than previous ones. We first propose primitives, a bit commitment scheme and four (statistical) witness indistinguishable (WI) protocols (See [FS90] for WI). Then
we construct, by using these primitives, statistical zero-knowledge protocols
(SZK argument of knowledge) in which, for any given multi-variable polynomial
f(X1, .., Xt) and any given modulus n, prover :P gives (I1, --,/t) to verifier ]3 and
can convince l) that :P knows (Xl,.., xt) satisfying f(xl, .., xt) =- 0 (mod n) and
I~ = BC(xi), (i = 1, .., t) without revealing any additional information. The proposed protocols are O(Inl) times more efficient than the corresponding protocols
in [Dam93, Dam95, Oka95], because our protocols do not need to confirm that
a secret is in any range nor to execute any (single-bit based) cut-and-choose
method. At the same time, the communication complexity of our protocols is
O(Inl) times less than those of [Dam93, Dam95] and [Oka95]. Although a setup procedure for the parameter of the underlying bit-commitment is necessary
and plays an essential role to satisfy the zero-knowledgeness of our protocols,
the procedure can be done separately before the main parts of the protocols
in pre-processing and can be shared by repeated execution of the main parts.
(Similarly, a set-up procedure is also necessary in [Dam93, Dam95].) 

A computational assumption, the intractability of a modified RSA problem (defined in Def.3), is necessary to prove the (knowledge) soundness regarding (Xl, .., xt) in our protocols, while no computational assumption is required
for (statistical) zero-knowledgeness. In addition, any poly-time bounded prover
P'can open the bit commitment in any different ways with negligible probability
under the factoring assumption.
These protocols can be employed to construct various practical cryptographic
protocols such as fair exchange, untraceable electronic cash and some protocols
regarding secret sharing. In Section 5, we demonstrate how to employ the
proposed protocols to construct the fair exchange and contract signing protocol.

2 Definitions and Assumptions

This section mainly defines the factoring assumption and the modified RSA
problem and its assumption; the modified RSA problem is a little different from
the well-known RSA problem at the point that a cracking algorithm, A, can on
input (N, Y) choose a convenient exponent, e (_> 2), to output (X, e) such that
X -- ~ (mod N) (Of course, it is less intractable than the factoring problem
since a cracking algorithm, A, which can factor N, can solve the modified RSA
problem of N). The validity (soundness) of the whole protocols against 7 ~ can
be guaranteed under Assumption 4 while the validity of the commitment against
can be guaranteed under Assumption 2.

Definition 1. f(n) is negligible in n if, for any constant c, there exists a constant, N, such that fin) < (l/n) ~ for any n > N. f(n) is non-negligible in n
if, there exits constants c and N such that fin) > (l/n) c for any n > N. f(n) is
overwhelming in n if, for any constant c, there exists a constant, N, such that
f(n) > 1 - (l/n) r for any n > N.

Assumption 2. (Factoring Assumption) A probabilistic polynomial-time generator A1 exists which on input IlNI outputs composite N where N is a composite of two prime numbers, P and Q, such that for any probabilistic polynomialsize circuit family, A, the probability that A can factor N is negligible. The
probability is taken over the random choices of A1 and A.

Definition 3. Modified RSA problem is, for given (N, Y), finding X and e
( e >_ 2 ), such that Y - X ~ (rood N), where N is the composite of two prime
numbers, P and Q.

Assumption 4. (Modified RSA Assumption) A probabilistic polynomialtime generator A2 exists which on input IlNI outputs (N, Y) such that for any
probabilistic polynomial-size circuit family A, the probability that A can solve
the modified RSA problem is negligible. The probability is taken over the random
choices of A2 and A.

In this paper, we use the following symbols. "a ER S" means uniformly choosing a random element, a, from a set, S. Let ZN be a residue class ring modulo
N, and Z~v the reduced residue class group. Other symbols and definitions will
be set as needed. 

3 Bit Commitment and WI protocols

In this section, we propose a bit commitment scheme, and four WI protocols. The
suitable parallel executions of those protocols, for any multi-variable polynomial
](X1, .., Xt) and any modulus n, can construct an WI protocol over the relation
((I1,..,/t,h+l), (xl,rh..,xt,rt, y, rt+l) ) such that y -- f(xl,..,xt) (mod n)
(For simplicity, we often call the protocol WI protocol to confirm y = f(Xl, .., xt)
(mod n)). We show later, as an example, a WI protocol to confirm y =- ax 5 +
b mod n.

3.1 Bit Commitment Scheme

Our proposed commitment statistically reveals to the verifier no information of
secret s in BC(s) and holds computational validity against the prover. The validity of the commitment is guaranteed if the factoring assumption (Assumption 2)
holds true. The commitments are given by

BCbo(S,r) = boSbr 1 rood N or BCbo(s,rl,r2) = boSb~11b~2 mod N.

Here, (N, bo, bl, b2) is a set of system parameters given by verifier Y or authority (i.e. trusted third party).

To set the system parameters, verifier ]2 (or authority) executes the following
procedure:

[Set-up procedure]

1. ~ generates large primes, P and Q, including odd prime divisors, p and q,
such that p = (P - 1)/2, q = (Q - 1)/2, and p ~ q).

2. 12 finds at random gp e Gp\{1}, and gq E Gq\{1}, where Gp, Gq are subgroups of the order p, q in Zb, Z~ respectively (The complexity of finding gp
and gq is comparable to that of finding generator elements of Z~ and Z~).

3. ~ computes, bo E Z~v, by using the Chinese Remainder Theorem, such that
bo = gp rood P and bo = gq rood Q (b0 is a generator element of Gpq).

4. ]) finds at random a, ~ E Z~q and sets bl = bo a mod N and b2 = bo a rood N.

5. Y sends (N, bo, bx, b2) to prover 7 ~ . Then Y proves that he knows a, a -1 ,
/~, and/~-1 such that bl -- bo a mod N, and b2 -- bo a mod N in the zero
knowledge manner (that is, the orders of b0, bl,and b2 are equivalent).

In the bit-commitment phase, 7 ) sends to Y, BCbo (x, r) = b0Zbl r mod N or
BCbo(X, rl,r2) = b0Zblrlb2 r2 mod N where x 9 [ 0, N) is a secret and r, rl,r2 9
[ 0, 2 raN) are auxiliary random numbers.

Lemma 5. (Indistinguishability)/fro = O([N[), BCbo (x, r) and BCbo (x, rl, r2)
statistically reveal no information of x to 12 .

The following results show that the validity (security) of these commitments
are guaranteed if the factoring assumption (Assumption 2) holds true.

Lennna 6. (Miller) Let N = p~l ...p~m be the prime factorization of the odd
integer g. Let A(N) : lcm{p~l-l(pl - 1), ..,p~n~-l(pm -- 1)} (the Carmichael
A-function) and L be a multiple of A(N) (i.e., A(N)IL ). There exists a probabilistic polynomial-time algorithm M which, on input (N, L), can output the
factorization of N with non-negligible probability in INI. (Note: N is given by
A1 and the probability is taken over the coin tosses of A 1 and M.) 

The proof of Lemma 6 is implied by Theorem 2 in [Mi176].

Definition 7. (Generator ABC) Let ABe be a probabilistic polynomial-time
algorithm which on input 11NI outputs (N, bo, bl, b2) where the distribution of
N is equal to that of A2 and (bo, bl, b2) is generated by the Set-up procedure
of the bit commitment scheme.

Theorem8. (Validity against 7)*) If Assumption 2 holds true, there exists
no probabilistic polynomial-time algorithm 7)* which, on input ( N, bo, bl), given
by ABC, can output (sl, rx) and (s2, r2), with non-negligible probability in INI,
where (sl,rl) # (s2,r2) and bo'lb~ ' - bo82b~ 2 (mod g). (Note: the probability
is taken over the coin tosses of ABc and 7)*.)

Sketch of Proof:

The proof is by contradiction. Assuming that a probahilistic polynomial-time
algorithm 7)* can output (sl,rl) and (s2, r2), with non-negligible probability,
then we can construct a probabilistic poly-time algorithm M which can factor
N with non-negligible probability. Let s -- sl -s2, and r = r2-rl. The algorithm
P* above can be replaced by the algorithm which, on input (N, bo, bl), can output

boSbl r - 1 (mod N), (1)

where (s, r) r (0, 0). In addition, by Lemma 6, the algorithm M can be replaced
by the algorithm which on input N outputs L' such that )~(N)[L'.

The strategy of M is the following:

Algorithm M

1. Input N generated by ABc to M.

2. M picks bo ER ZN and t~ ER (0,2kN) (k = O([ND), then computes bl =
bo a mod N.

3. M inputs (N, bo, bl) to 7)*.

4. If 7 ~* returns (s,r), go the next step, otherwise M halts.

5. M outputs L = 2(r - as) if L r 0, otherwise halts.

The algorithm M can output L with non-negligible probability.

When M picks bo uniformly in ZN in Step 2, the probability that the order
of b0 is pq is non-negligible because ~(Pq) #ZN = (2p+l)(2q+l) (P-1)(q-D ~ ~' 1 where ~o(.) is
the Eulerian function and ~(pq) is the number of generators of Gpq. This means
that the distribution of a non-negligible fraction (about 1/4) of (N, b, bl)'s picked
by M is indistinguishable from those generated by ABe. 7 )* therefore outputs
(s, r) with non-negligible probability in Step 4. In Step 5, the probability of
L ~ 0 is non-negligible. This is because even infinite power 7)* can only know
a0 -- c~ mod pq. Therefore, if a is uniformly picked in [0, 2kN), the probability
of L r 0 is non-negligible. From equation (1), L - 0 (mod pq). This means
that

L = 2kpq : kA(N), (2)

where k ~ 0, A(N) = lcm(P - 1, Q - 1). [] 

Corollary 9. (Validity against 79*) If Assumption 2 holds true, there exists
no probabilistie polynomial-time algorithm 79* which, on input ( N, bo, bl, b2),
given by ABC, can output (t, u, v) ~ (0, 0, 0) such that b~b~b~ = 1 (mod N),
with non-negligible probability.

If base bo is clear, we use the expressions BC(s, r) and BC(s, rl, r2). If auxiliary parameters are not important, we use just BC(s).

3.2 Basic Protocol

Let R (1) (N,bo,bl) := {(/' (x,r))[I = J~C(N,bo,bl)(X,r)). The basic protocol is (statistical) witness indistinguishable (WI) over the relation ~(1) and convinces * ~( N,bo,bl )
that 79 knows (x, r) such that I = BC(N,bo,bl)(x, r).

[Basic Protocol]

1. )2 executes with 79 the set-up procedure for parameter (N, bo, bl, b2).

2. 79 sets I = BC(N,bo,b~)(x,r) and sends it to V.

3. 79 chooses w ~ w~ eR [0, 22raN) and sets w ~ w I by w ~ = w ~ - 22mN and
w21 = w~ -22roW. 79 picks four elements, w2,3 's eR [ 0, 2raN), then computes
0 1 2 t,,3 = BC(wi, wj, wij), where 1 < i,j < 2.

4. 79 sends to 1?, four unordered commitments, ti,j's.

5. )2 picks a challenge c 9 [ 0, 2 m) and sends it to 79 .

6. 79 setsX = cx+w o i and R = cr+wj 1 such that X,R 9 [0, 22raN), and
sends to 1), the pair, ( X, R, w?,,3 )"

7. ]2 checks there exists a tij such that BC(X, R, w2j) = t,,jI ~ (mod N).

The completeness is obvious, since when X = cx+wi ~ and R = cr+w~ (if 7 9 is
honest, there exists X, R E [ 0, 22raN) ), the left-hand side in the verification
equation is equal to the right-hand, because

8 1ll 0 r 1 2 0 1 2 boXblRb2 ~~ =- bo c + 'bl c +W~bzW',~ =_ boW'bl~Jb2~',~I c (rood N).

Lemma 10. (Soundness) Under Assumption 4, there exists a probabilistic polytime algorithm M such that, for any probabilistic poly-time algorithm 7 9., if
probabilistic interactive algorithm (79",1) ) accepts with non-negligible probability in INI, then M with ABe and 79*as oracles can extract (x,r) satisfying
I = BC(N,bo,bl)(x,r) with overwhelming probability in IN I where I is given by
79* as output. The success probability of (79",V) is taken over the coin tosses of
79* and 1; (including ABe), while the success probability of M over those of
ABC, 79* and M.

The proof of the soundness is given in Appendix A.

Lemma 11. (Witness Indistinguishable) Ifm = O(IND andx, r 9 [0, 2"nN),
the basic protocol is statistically witness indistinguishable over Rll),bo,bl ). 

3.3 Checking Protocol

The following protocol is considered as a kind of the basic protocol. However,
since it is also utilized in the mod-multi protocol and in Subsection 3.6, we
state it as a different one. Let "~(N,bo) ~(2) :---- {(1,7)1 1 = b~ mod N}. The checking
protocol is WI over the relatl "o n R(N,ao) (~) and convinces l) that 50 can know ~/such
that I = bo ~ mod N.

[Checking Protocol]

1. ]) executes with 50 a set-up procedure for (N, b0, bl).

2. 50 sets I -- bo 7 mod N and sends it to l).

3. 50 chooses w ~ 9 [ 0, 22"~l) and sets w ~ by w ~ = w ~ - 22ml. 50 picks two
elements, w i 1, s 9 [ 0, 2raN), then computes tl = BCbo(W ~ w i 1 ), where 1 <
i < 2 and I := max[b - a, N].

4. 50 sends to ];, two unordered commitments, ti's.

5. l; picks a challenge c 9 [ 0, 2") and sends it to 50.

6. 50 sets X := c(7 - a) +w ~ 9 [ 0, 22ml), and sends to Y, the pair, (X, wl).

7. Y checks there exists a tij such that BC(X,w 1) - ti(Ibo-a) ~ (mod N).

The following results are easily obtained by the properties of the basic protocol.

Len~aa 12. (Soundness) Under Assumption 4, there exists a probabilistic algorithm M such that, for any probabilistic poly-time algorithm 50", if probabilistic
interactive algorithm (50",V) accepts with non-negligible probability in INI, then
M with ABC and 50* as oracles can extract 7 satisfying I = bo ~ rood N with
overwhelming probability in IN[ where I is given by 50*as output. The success
probability of (50",1) ) is taken over the coin tosses of 50* and V (including
ABC), while the success probability of M over those of ABe, 50* and M.

Lemma 13. (Witness Indistinguishable) If m = O(INI) and 7 9 [ a, b ), the
checking protocol is statistically witness indistinguishable over R( N,bo).(2)

3.4 Comparing Protocol

Let Rl3),bo,bl,, ) := (((I1,I2), (x, rl,r2))]It = BCbo(X, rl),I~ = BC~(x, r2)}. The
comparing protocol is WI over the relation ~(3) "~(N,bo,bl,~)' in which 50 can convince
l) that he knows (x, rl,r2) such that/1 = BCbo(X, rl) and I2 = BCa(x, rz).

[Comparing Protocol]

1. ]) executes with 50 the set-up procedure for parameters (N, bo, bt, b2).

2. 50 sets 11 = BCbo(x, rl) and 12 = BCa(x, r2), and sends them to ]) .
0 1 2 

3. P computes, for 1 _< i, j < 2, tii = BCbo (w ~ wJ, w, 2) and u,j = BCa(w i , Tlj , 71~j).

4. 50 sends to V, four unordered pairs, (ti,3, uij)'s.

5. V picks a c ER [ 0, 2 "~) and sends it to 50 .

6. 50 sets X := cx+w o i, R1 := crl +wJ, and R2 := cr2+w~ such that
X, R1 , R2 9 [ 0, 22'~N). 50 then sends to V , the pair, (X, R1, R~, wi,~, 2 7h,k). 2 

7. V checks that there exists a pair ( tl,j, u~,k ) such that

BCbo(X, Rl,W, 2) - ti,jI~ (mod N) and BC,(X, R2,~j) = u,,j~ (rood N).

If l) sets a new base a, he has to convince P that there exists an a such that
a = b0 ~ mod N before executing this protocol, but in many cases, a is set by 7) as
a := 11. Note that in the case of a :=/1, 7) can show )2/2 = BCbo(x2,rlxl +r2).
This means P can convince ]) that commitments, BCbo (x, rl) and BCbo (y, r2),
satisfy y = x 2.

The following results are easily obtained by the properties of the basic protocol.

Lem_ma 14. (Soundness) Under Assumption 4, there exists a probabilistic algorithm M such that, for any probabilistic poly-time algorithm 7)*, i] probabilistic
interactive algorithm (P*,V ) accepts with non-negligible probability in INI, then
M, with A B C and 7)*as oracles, can extract ( x, r l , 7"2) with overwhelming probability in INI, where (/1,I2) is given by 7)*as output, and h = bo~b~ ~ mod N,
I2 = a~b~ 2 mod N. The success probability of (7)*,)2) is taken over the coin
tosses of P* and ]2 (including ABe), while the success probability of M over
those of ABe, 7 )* and M.

Lennna 15. (Witness Indistinguishable) /Ira = O(IN D and xl, rl, x2, r2 E
[ 0, 2raN), the comparing protocol is statistically witness indistinguishable over
R(3)
( N,bo,bl ) "

3.5 Mod-Multi Protocol

R(4) .-s Let .~(N,bo,bl).-- t ((Ii,I2, I3),(xl,r,,..,x3,r3)) I Ii = BC(x~,r~), x3 -- XlX2
(mod n) }. The mod-multi protocol is WI over the relation n(4) (We call it "~( N,bo,bl )
a rood-multi protocol to confirm x3 - XlX2 (mod n)). In the mod-multi protocol, 7) can convince V that he knows (xl,x2,x3,rl,r2,r3) such that x3 - xlx2
(mod n), where/1 = BCbo(Xl, rl), I2 = BCbo (x2, r2) and/3 = BCbo(X3, r3).

[Mod-Multi Protocol]

1. ]2 executes with 7) the set-up procedure and sends to P, parameters (N, b0, bl, b2).

2. P sets Ii ----- BCbo(Xl,rl), 12 = BCbo(X2,r2), and Is = BCbo(Xs, rs), and
sends them to l).

3. P sets I 5 = BCl2(Xl,r4) = BCbo(X, x2,rsxl + r4), and Ia = BC~(d, rd)
where d = (xs - XlX2)/n.

4. P executes in parallel with ]2 the comparing protocol for (I1,I~) and the
three basic protocols for/2,/3, and Id.

5. P computes 7 = (r2xl + r4 + rdn) -- r3, and executes with ]2 the checking
protocol for b~ = I3(I~Id~) -1 rood N and range [ -2"~N, 22"~+1N) over the
relation ~(2) "~(N,b~)"

In this protocol, P executes one comparing protocol, three basic protocols,
and one checking protocol for b~ in parallel. (in the case of xl = x2 the number
of the basic protocols is reduced to two). This protocol is also WI. 

Lemma 16. (Soundness) Under Assumption 4, there exists a probabilistic algorithm M such that, for any probabilistic poly-time algorithm 7 9., if probabilistic
interactive algorithm (79",1)) accepts with non-negligible probability in [NI, then
M, with AB e and 79*as oracles, can extract (Xl, rl, .., x3, r3) with overwhelming
probability in IN[, where (I1,I2,I3) is given by 79*as output, Ii = BC(xi, r,)(i =
1,...,3) and x3 - XlX2 (rood n). The success probability of (79",1;) is taken
over the coin tosses of 7 9* and 12 (including ABe), while the success probability
of M over those of ABe, 79* and M.

Sketch of Proof:

From lemma 10, if (P*,I;) has non-negligible success probability, we can construct a probabilistic poly-time knowledge extractor M, which extracts (Xl, rl, ..,
x3, r3) and d such that I, = BC(xi, ri) and x3 = XlX2 +dn (mod pq). Then the
probability of x3 # XlX2 +dn is negligible. If it is non-negligible, we can construct
an algorithm M', with poly-time bounded 79*as an oracle, which can factor N
given by ABe with non-negligible probability in IN[. This is a contradiction.
M' indeed extract L such that L = 2(x3 - XlX2 -dn) ( = 2kpq = kA(N) )
where A(N) = lcm(P - 1, Q - 1). By Lemma 6, this contradicts Assumption 2
and thereby contradicts Assumption 4. Consequently, M extracts (Xl, x2, x3, d)
such that x3 -- XlX2 (mod n) with overwhelming probability in ]N[. []

Lemma 17. (Witness Indistinguishable) If m = O([N[) and Xx, rl, .., x3, r3
E [ 0, 2raN), the rood-multi protocol is statistically witness indistinguishable over
R(4)
(N,b0,bl)"

3.6 WI protocol to Confirm y -- ax 5 + b (mod n)

We show, as an example, a WI protocol to confirm y -- ax 5 + b mod n.

Let [Xl,X2;X3] be the mod-multi protocol to confirm xa - xlx2 (mod n)
and let [Xl;X2] be the mod-multi protocol to confirm x2 - Xl 2 (mod n).

Prover 79 sets (Iy, Ix, Id, I1,12,13) as (BCbo (y, r~), BCbo (x, r), BCbo (d, rd),
BCbo(tl), BCbo(t2), BCbo(t3,r3)) where d = y-(at3+b) tl = x 2 modn, t2
x 4 mod n and t 3 = X 5 mod n. 7 ) executes with V the two basic protocols for I v
and Id and the three mod-multi protocols, [x; tl], [tl; t2], and [x, t2; t3], in parallel.
79 then executes with V a checking protocol for b~ and range [ -2raN, 22re+iN),
where 7 = ar3 + rdn -- ry and b~ =_ I~bob(IyId) -1 (mod N).

4 Statistical Zero Knowledge Protocol

In this section, we state the main results of this paper. As mentioned above in
Section 3, for any multi-variable polynomial f(Xt, .., Xt) and any modulus n, we
can construct a statistical WI protocol to prove that P knows (xl, rt, .., xt, rt,
y, rt+l) such that iT, -- BC(xi,rl) (i = 1,..,t), /t+l = BC(y, rt+l), and y -
](Xl,..,xt) (mod n). This WI protocol can be transformed to the following
statistical zero knowledge (SZK) protocol.

Here, we define some terminology. Let ] be a multi-variable polynomial.
Let 3 :={(],n)l S(Xl,..,xt)e Z t s.t. f(xl,..,xt) - 0 mod n}. We can assume,
without loss of generality, coefficients of f, number of variables in f, i.e. t, and 
parameters (N, bo, bl, b2) are related to modulus n regarding their size (that is,
the size of them is O(InD).

The SZK protocol is constructed as follows:

[SZK Protocol]

common input: (f,n).

output: I1,--, It, N, b0, bl, and the remaining conversation of (7) , V ).

knowledge of 7) : (xi, rl, .., xt, rt) such that I, = BC(N,bo,bl) (X,, r,) (i = 1, .., t)
and f(xl,..,xt) -- 0 (mod n).

1. %; executes with 7) a set-up procedure for (N, bo, bl,/)2)-

2. 7) sets/, := BCbo(xi,ri) (i = 1, ..,t), It+l := BCbo(O, rt+l), and sends them
to %).

3. 7) executes, with l;, the WI protocol mentioned above to prove that 7) knows
xl,rl,..,xt,rt, and y, rt+l such that Ii = BC(xi,ri) (i = 1,..,t), /t+l =
BC(y, rt+l), and y =_ f(xl,..,xt) (mod n) where y = 0.

4. 7) sends rt+l to 1).

5. %; checks that It+l = BC(O, rt+O (mod N).

Prover 7 ~ Verifier %)
( ~l~rl~ ..,xt,rt,O, rt+l )
set-up procedure
for (N, bo, bl, b2) (
11, ..., It+l
)
a WI protocol
to confirm
f(xl, .., xt)
-- 0 mod n.
?t+l
11, .., It, It+l
rt+l ) /t+l ------ BC(0, rt+l)

Fig. 1. The SZK protocol that convinces V that /)knows (Xl,..,Xt) satisfying
](Xl,..,xt) -- 0 (rood n) and Ii ---- SC(x 0 (i = 1,.., t).

Theorem l8. (Soundness) Under Assumption 4, there exists a probabilistic
poly-time algorithm M such that, for any probabilistie poly-time algorithm P*
and for any input (f,n) E S, if probabilistic interactive algorithm (TP*,]; ) accepts
on input (f,n) with non-negligible probability in [nl, then M, with ABO and
7)*as oracles, can extract (xl, rl, ..., xt, rt) with overwhelming probability in
Inl, where I, = BC(x,,r 0 and It(x1, ..,xt) - 0 mod n. The success probability
of (P*,Y ) is taken over the coin tosses of 7 )* and Y (including ABe), and the
success probability of M over those of ABe, 7)* and M. 

Sketch of Proof:

Assume that (79",])) has non-negligible success probability. The sketch of the
proof is as follows: M executes, with 79", the set-up procedure for parameters
(N, bo,bl,b2) given by ABe, in which M should convince 7 9* that he knows
c~, c~ -1, ~, and ~-1 such that bl -- bo a mod N, and b2 = bo ~ mod N. Instead
of using the values, c~, a -1, f~, and/~-1, M can execute the set-up procedure
using the resetable simulation technique for 79* because the set-up procedure is a
zero-knowledge system of (M, 79*). After M completes the set-up procedure, 79*
sends to M, 11, .., It, and It+l to start a WI protocol. Note that this protocol has
(knowledge) soundness over the relation ((I1,.., It, It+ 1 ), (Xl, rl,--, xt, rt, O, rt+l ))
such that /i = BC(xi,ri) (i = 1,..,t), It+l = BC(O, rt+l), and f(xl,..,xt) =-
0 mod n. Therfore M can extract from 79* desirable witnesses, (Xl, rl, ..., xt,
rt, O, rt+l). []

Theorem 19. (Zero Knowledge) Let m = O(InJ). There exists a probabilistic
algorithm M which runs in expected polynomial time such that, for any 1;*, and
for any common input (f, n) E S, the view of 1)* is statistically indistinguishable
from the output of M with 1)* as an oracle.

Sketch of Proof:

Let M be an expected poly-time algorithm allowed to use V* as an oracle. M can extract a and c~ -1 from 1)* in the set-up procedure. Let L :=
aa -1 - 1. Note that the order of b0, bl, and b2 divides L. Next, M chooses
x~,rl, 9 .,xt,rt,rt+l ' ' ER [ 0, 2raN) and sets If, ..., I~ ' and/t+l := BC(O, rt+l).
M computes y := f(x~,..,x~t)mod n and r~+ 1 :-- rt+l -a-ly mod L. Note
that It+l = BC(O, rt+l) = BC(y,r~+l). M executes with 1)* a (statistical)
WI protocol over the relation ((/~, .., If,/t+l), (x~, r~, .., x~, r~, y, r~+l) ) such that
If = BC(x:,r~) (i = 1,..,t),/t+l = BC(y,r~+I), and y - f(Xl,..,xt) (mod n).
Finally, M sends rt+l to 1)*.

Here the distribution of (I1, ---, /t, /t+l) such that /, = BC(xi,r,) (i =
1,..,t),/t+l = BC(O, rt+l) and f(Xl, ..,xt) = 0 (mod n) and that of (/~, ...,
g, /t+l) such that Ii = BC(x:,r~) (i = 1,..,t), It+l = BC(y,r't+l) and y =
f(Xl, .., xt) (mod n) are statistically indistinguishable. In addition, for common
input (I~, ..., If, /t+l) the protocols with witness (Xl, rl, ..., xt, rt, O, rt+l)
and with witness (x~, r~, ..., x~, r~, y, r~+l) are statistically indistinguishable.
Therefore the view of 1;* is also statistically indistinguishable from the output
ofM v*. []

Example 1. Suppose that f(X) -- X ~ - m (mod n). :P can prove, in the statistical zero knowledge manner, that he knows s such that f(s) -- 0 (rood n) and
BC(s).

Remark. Although the set-up procedure is described in the first step of the
proposed SZK protocol, the procedure can be executed in an off-line manner
before the remaining protocol begins. In addition, the set-up procedure can be
shared by repeated execution of the main protocol. The zero-knowledgeness is
still guranteed even if the set-up procedure is shared by repeated execution of
the main protocol between 79 and 1). 

5 Application to Fair Exchange and Contract Signing

We propose a gradual release protocol to realize fair exchange and contract
signing. We modify our commitments into bit releascable commitments like
those of [Dam93, Dam95] for our gradual release protocol. The protocol is as
follows:

V executes with 7 ) a set-up procedure and they hold parameters ( N, b0, bl, b2)
in common. Pand Vset I such that Is t < I and compute b~ :- b 2~modN
( V should prove that he knows (21) -1 mod pq in the ZK manner to show that bl
and b~ have the same order) (set-up phase). P sends to V, (m, BCbo,b,~(s, rl),
BCbo,b,~ (0, r2)). For parameters (N, b0, b~, b2), 7 ~ executes, with V, the protocol
to confirm that BCb,e~ (s, rl) and BCb,b,~ (0, r2) satisfy the relation s ~ - m -= 0
(mod n), where (e, n) are RSA (or Rabin) public-key. P then open the commitment BCb,b,~ ( 0, r) (confirming phase). 7 ) releases the secret s bit by bit from
the least-significant bit (LSB). Let sk be the remaining secret of s after k bit release. :P opens the LSB of sk by revealing X~+x -- bo~=~b~ 2~-k-~ mod N. V can
know the LSB of sk by checking Xk -= X~+lbo i (mod N) (bit by bit release
phase).

6 Efficiency

We compare our protocols with those in [Dam95] from the view points of computational and communication complexity. In [Dam95], the commitment is defined
by the form BC(s,r) = gSr2~ mod N. As our comparing and rood-multi protocols are constructed in a similar manner to those in [Dam95], it is enough to
compare our basic protocol with that in [Dam95]. Our comparing protocol is
composed of at most two basic protocols and our mod-multi protocol consists of
three basic, a comparing, and a checking protocols. Therefore those in [Dam95]
have nearly the same construction. We assume below that m = INI = Inl = Ic[.

We estimate the computational complexity of the both basic protocols from
the number of modular multiplications. In our basic protocol, P needs to compute four auxiliary parameters, tij's (tij = boW~ and ]) needs to check
the verification tijI c = boXb~b2 '~, where Iw~ = Iw~l = IXI = ]R I = 122mY[ =
3m and Iwi~l = [2mNI = 2m./~ and V both need O(m) modular multiplications
of N (about 32m, 9m respectively). In Damgs basic protocol, 7 ~ needs to
compute 2m auxiliary parameters, ti's (ti = gW~ and V needs to check
m verifications, tiI= gXR2', where Iw~ = Iwll = IXt = 3m and l = 2m.
P and V both need O(m 2) modular multiplications of N (about 6m 2, 3m 2 respectively). Accordingly, our protocol is about O(m) times more efficient than
Darags

The amount of communication in our basic protocol is O(m) bits since 4]tijl+
Icl + IXI + IRI + Iw2jI = 8m while that of [Dam95] is O(m 2) bits since m. (21ti I +
IX] + IRI) = 4m 2. Hence, the communication complexity of ours is also Oim )
times less than that of Damg~rd's.
Comparing our protocols with those in [Oka95], the modulus size of Okamoto's
bit commitment, BC(s, r) = g~G r mod p, should be at least twice ours. Hence, 
our protocol is about O(m 3) times more efficient than [Oka95]. The communication complexity of ours is also O(m) times less than that of [Oka95].

7 Conclusions

We have proposed a bit commitment scheme, BC(-), and related statistical
zero knowledge (SZK) protocols in which, for any given multi-variable polynomial f(X1, .., Xt) and any given modulus n, prover P gives (h, .-, It) to verifier l)and can convince l)that :P knows (Xl,..,xt) satisfying f(xl, ..,xt) = 0
(mod n) and Ii = BC(xi), (i = 1, .., t). The proposed protocols are O(Inl) times
more efficient than the corresponding previous ones [Dam93, Dam95, Oka95].
The (knowledge) soundness of our protocols holds under a computational assumption, the intractability of the modified RSA problem, while the (statistical)
zero-knowledgeness of the protocols needs no computational assumption. We
have also shown the applications of fair exchange and contract signing by using
the proposed protocol.

References
[BCC86]
[BG92]
[Bra95]
[CDS941
[CGMA85]
[Dam93]
[Dam95]
[FFS88]
[FS90]
[GMRa89]
[GMW86]
[ii176]
[Oka95]
G.Brassard, D.Chaum, and C.Crdpeau, "Minimum Disclosure Proofs
of Knowledge," Journal of Computer and System Sciences, Vol.37,
pp.156-189 (1988)
Bellare, M. and Goldreich, O., "On Defining Proofs of Knowledge",
Proceedings of Crypto 92, pp.390-420 (1992).
Brands, S., "Restrictive Blinding of Secret-Key Certificates", Proceedings of Eurocrypt 95, pp.231-247 (1995).
Cramer, R., Damgs I. and Schoenmakers, B., "Proofs of Partial
Knowledge and Simplified Design of Witness Hiding Protocols", Proc.
of Crypto'94, LNCS, Springer, pp.174-187 (1994)
Chor, B., Goldwasser, S., Micali, S. and Awerbuch, B., "Verifiable
Secret Sharing and Achieving Simultaneity in the Presence of Faults",
Proc. of FOCS, pp.383-395 (1985).
Damgs I., "Practical and Provably Secure Release of a Secret and
Exchange of Signatures," Proceedings of Eurocrypt 93 (1993).
DamgKrd, I., "Practical and Provably Secure Release of a Secret and
Exchange of Signatures," vol. 8 pp.201-222, Journal of CRYPTOLOGY(1995).
U.Feige, A.Fiat and A.Shamir, "Zero Knowledge Proofs of Identity,"
Journal of Cryptology, Vol. 1, pp.77-94 (1988).
U.Feige, and A.Shamir, "Witness Indistinguishable and Witness Hiding Protocols," Proc. of STOC90.
Goldwasser, S., Micali, S., and Rackoff, C., "The knowledge complexity of interactive proof systems", SIAM J. Comput., vol. 18, pp. 186-208
(1989).
O.Goldreich, S.Micali, and A.Wigderson, "Proofs that Yield Nothing
But their Validity and a Methodology of Cryptographic Protocol Design," Proc. FOCS, pp.174-187 (1986)
Miller, G.L., "Riemann's Hypothesis and Tests for Primality", Journal
of Computer and System Sciences 13, 300-317 (1976).
Okamoto, T., "An Efficient Divisible Electronic Cash Scheme", Proceedings of Crypto 95, pp.438-451 (1995). 
29
[Ped91]
[Sta96]
[TW87]
Pedersen, T. P., "Non-Interactive and Information-Theoretic Secure
Verifiable Secret Sharing", Proceedings of Crypto 91, pp. 129-140
(1992).
Stadler, M., "Publicly Verifiable Secret Sharing", Proe. of Eurocrypt'96, LNCS 1070, Springer, pp.190-199 (1996)
Tompa, M., and Woll, H., "Random Self-Reducibility and ZeroKnowledge Interactive-Proofs of Possession of Information", Proe.
FOCS, pp 472-482 (1987).
A Proof of Lemma 10
Sketch of Proof:
The top level strategy of knowledge extractor M is as follows:
Protocol:
Step 1 M inputs llnl to ABe and gets parameter (N, b0, bl,b2).
Step 2 M executes, with P*, the set-up procedure for parameters (N, b0, bl, b2),
in which M should convince P* that he knows a, a -1, /3, and/3-1
such that bl = b0 ~ mod N, and b2 -- b0 ~ mod N. Instead of using
the values, a, a -I,/3, and/3 -1, M can execute the set-up procedure
using the resetable simulation technique for 7 ~* because the set-up
procedure is a zero-knowledge system of (M, P*).
Step 3 M can extract (ti,3, c, X, R, w~,j) and (rid, c', X', R~, w2i,p ~ for the same
ti,j, by using P* as an oracle.
AX AX ) Step 4 M outputs (~, zxc as a witness of I, where Ac := c - c', AX :=
X-X'andAR:=R-R I.
We explain Step 3 and Step 4.
Consider Step 3. Let ei,z be the success probability of (P*,V) with the conversation, (ti,j, c, X,R, w~5 ). Note that at least one of e,,j's is non-negligible if
(7~*,V) accepts with non-negligible probability. Then M can find two different
pairs for a ti,j in expected polynomial time in INI. Indeed, the following strategy
succeeds with overwhelming probability (See also [FFS88]):
1. For any (i,j), do the following steps.
2. Probe O(1/e) random entries in Hi,j (Here Hi,j's are boolean matrices and
each Hi,j's rows corresponds to all possible states a of RP and its columns
correspond to all possible choices c of RV, where the RP is 7~*'s random
tape, and the RV is V's random tape. ).
3. If find the first (ti,j, c, X, R, w,,~) 2 which (P*,V) accepts, then probe O(1/e)
random entries along the same row in order to find (tis, ca, X' .... R' w 2 jj which
(:P*,V) accepts.
In Step 4, (t~5,c,X,R, wi2,j) and (t,,j,c', X' , R' , w ~,j, 2 ~ satisfy that X - cx +
w~~ X' - c'x + w i~ R - cr + w~ modpq, and R ~ :- e~R +
w~ mod pq. Therefore,
AX_=Ac.x (modpq) and AR=Ac.r (modpq). (3)
M can obtain x and r only AX and AR dividing by Ac respectively, with
overwhelming probability in IN[ under Assumption 4. 
30
Let a0 E Zpq such that bl = b~ ~ mod N. Let d := gcd(Ac, AX + ARao).
From (3), the following relation holds
/iX +/iRt~o -/ic(x + rao) (mod pq). (4)
Here we replace, without loss of generality, P* with the poly-time bounded machine which, on input (N, b0, bl, b~) given by ABe, outputs (I, Ac,/iX,/iR) with
overwhelming probability in INI. We consider a poly-time bounded algorithm M'
using :P* as an oracle in the following:
Algorithm M t
1. inputs (N, C) generated by/i2 to M t.
2. M * picks b2 ER ZN, a ER ( O, 2kN) ( k is a constant. ), then computes
bl = C a mod N.
3. M t inputs (N,C, bl,b2) to P*.
4. If :P* returns (I,/ic,/iX,/iR), go to the next step, otherwise M halts.
5. M ~ outputs (IYC z mod N, _~_e) and halts, where Y and Z are integers such
that
/iX +/iRay /ic
d +---~Z=I.
Note that C - C ,~x+.,R. d Y +-4~--Z = I-Z- ,~cy C-~ ,c z = (iYcZ) ~- (mod N).
If d ~ /ic, M ~ is a machine, with :P* as an oracle, which can solve the
modified RSA problem with non-negligible probability. It contradicts Assumption 4. Therefore d = /ic, namely/icl(/iX + Alia). Moreover, (/ic,/iX,/iR)
must satisfy that/icl/iX and Acl/iR to hold d --/ic. Let a = a0 + ~pq. From
d =/ic,
/iX +/iP~ /iX +/iRao +/iP~,
/ic /ic
As even an infinite power 7 ~* can never know ~, The condition of/icl/iX and
/icl/iR has to be held to satisfy that of d =/ic.
Thus, M can extract (x, r) with overwhelming probability in INI. [] 
