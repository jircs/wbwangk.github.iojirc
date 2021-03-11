EFFICIENT IDENTIFICATION AND SIGNATURES
FOR SMART CARDS ’
C.P. Schnorr
Universitdt Frankfurt
1.	Introduction

We present an efficient interactive identification scheme and a related signature scheme that are based on discrete logarithms and which are particularly suited for smart cards. Previous cryptoschemes, based on the discrete logarithm, have been proposed by El Gamal (1985), Chaum, Evertse, Graaf (1988), Beth (1988) and Gunter (1989). The new scheme comprises the following novel features.

(1) We propose an efficient algorithm to preprocess the exponentiation of random numbers. This preprocessing makes signature generation very fast. It also improves the efficiency of the other discrete log-cryptosystems. The preprocessing algorithm is based on two fundamental principles local randomization and internal randomization.

(2) We use a prime modulus p such that p-l has a prime factor q of appropriate size (e.g. 140 bits long) and we use a base a for the discrete logarithm such that a’ = 1 (mod p). All logarithms are calculated modulo q. The length of signatures is about 212 bits, i.e. it is less than half the length of RSA and Fiat-Shamir signatures. The number of communication bits of the identification scheme is less than half that of other schemes.

The new scheme minimizes the work to be done by the smart card for generating a signature or for proving its identity. This is important since the power of current processors for smart cards is rather limited. Previous signature schemes require many modular multiplications for signature generation. In the new scheme signature generation costs about 12 modular multiplications, and these multiplications do not depend on the message/identification, i.e. they can be done in preprocessing mode during the idle time of the processor.

The security of the scheme relies on the one-way property of the exponentiation y c-c CZ’ (mod p), i.e. we assume that discrete logarithms with base Q are difficult to compute. The security of the preprocessing is established by information theoretic arguments.

This abstract is organised as follows. We present in section 2 a version of the signature scheme that uses exponentiation of a random integer. In section 3 we propose an efficient algorithm that simulates this exponentiation. We study its security in section 4. The performance of the scheme is exemplified in section 5.

2. The identification and signature scheme

Notation. For n E H let Z, be the ring of integers modulo n. We identify Z,with the set of integers (1, ..., n)* 

Ioitiation of the key authentication center (KAC). The KAC chooses

140 61 2 . primes p and q such that q I p-1, q 2 2 , p L 2 ,
.
ct E Z, with order q, i.e. aq - 1 (mod p), a 9 1 ,

a one-way hash function h : Z, x Z -+ (0 ,..., 2 -1) ,
its own private and public key.
The KAC publishes p,q,a,h and its public key.

COMMENTS. The KAC's own keys are used for signing the public keys issued by the KAC. The KAC can use for its own signatures any public key signature scheme, e.g. RSA, Fiat-Shamir, Rabin or the new scheme presented here. The hash function h is only used for signatures and is not needed for identification.

The function h outputs random numbers in (0,...,2t-l]; for the choice of the function h see the end of section 2. The security number t can depend on the application intended, we consider t P 72. The scheme is designed such that forging a signature or an identification requires, with t = 72, about 2 7a steps.

Registration of users. When a user comes to the KAC for registration the KAC verifies its identity, generates an identification string I (containing name, address, ID-number etc.) and signs the pair (1,v) consisting of I and the user's public key v. The user can generate himself his private key s and the corresponding public key v.

The user's private and public key. Every user has a private key s which is a random number in {1,2, ...,q). The corresponding public key v is the number v = Q (mod p). -I Once the private key s has been chosen one can easily compute the corresponding public key v. The inverse process, to compute s from v, requires to compute the discrete logarithm with base Q of v-*, i.e. s -log, v 

The following protocol is related to protocol 1 in Chaum, Evertse, Graaf (1988);it condenses this protocol to a single round.

The Identification protocol

(Prover A proves its identity to verifier B)

1. Initialion. A sends to B its identification string I and its public key v. B checks v by verifying KAC's signature transmitted by A.

2. Preprocessing. A picks a random number r E (1, ...*q- l), computes x := a' (mod p), and sends x to B (see section 3 for an efficient simulation of this exponentiation).

3. B sends a random number e E (0 ,..., 2 -1) to A.

4. A sends to B

5. Identification test. B checks that x = aY V' (mod p) and accepts A's proof of y := r + se (mod q) . identity iff equality holds.

Obviously if A and B follow the protocol then B always accepts A's proof of identity. We next consider the possibilities of cheating for A and B. We call (x,y) the proof and e the exam of the identification. The proof (x,y) (the exame, resp.) is called straight if A (B, resp.) has followed the protocol, otherwise the proof (exam, resp.) is called crooked.

A fraudulent A can cheat by guessing the correct e and sending the crooked proof 

The probability of success for this attack is 2-t. By the following proposition this success rate cannot be increased unless computing log,v is easy.

x := ar V' (mod p), y := r .

Proposition 2.1 Suppose there is a probabilistic algorithm AL with time bound VLI which takes for input a public key v and withstands, with probability E > 2-'+' * the identijication test for a straight exam. Then the discrete logarithm of v can be computed in time O(IALI/&) and constant, positive probability.

Proof. This is similar to Theorem 5 in Feige, Fiat, Shamir (1987). The following algorithm AL' computes log,v.

1. Repeat the following steps at most 1/& times: generate x the same way as does algorithm AL, pick a random e' in (0, ..., 2 -1) and check whether AL passes the identification test for (x,e'); if AL succeeds then fix x and go to 2.

2. Probe 1/~ random numbers en in (0,...,2t-1) . If algorithm AL passes the identification test for some en that is distinct from e' then go to 3 and otherwise stop.

3. Choose the numbers y', y" which AL submits to the identification test in response to e', e". (y'-y" is the discrete logarithm of v"-~' (mod P).)

4. Output (y'-y")/(e"-e') (mod q) .

We bound from below the success probability of this algorithm. The algorithm finds in step 1 a passing pair (x,e') with probability at least i. With probability at least a, the x chosen in step 1, has the property that AL withstands the identification test for at least a '$ s-fraction of all e E (0, ..., 2 -1). For such an x step 2 finds a passing number en that is distinct from e'with probability at least
1 - (l-s/2)'/" > 1 - 2.7-'/* > 0.3 . This shows that the success probability of the algorithm is at least 0.3/4.
1
t
0

The verifier B is free to choose the bit string e in step 3 of the identification protocol, thus he can try to choose e in order to obtain useful information from A. The informal (but non rigorous) reason that A reveals no information is that the numbers x and y are random. The random number x reveals no information. Furthermore it is unlikely that the number y reveals any useful information because y is superposed by the discrete logarithm of x, y 5 log,x + e . s (mod q) , and the cryptanalyst cannot infer r = logax from x. The scheme is not zero-knowledge because the tripe1 (x,y,e) may be a particular solution of the equation x = aYve (mod p) due to the fact that the choice of e may depend on x.

Minimizing the number of communication bits. We can reduce the number of communication bits for identification. For this A sends in step 2 h(x) (instead of x) and B computes in step 5 x := aYve (mod p) and checks that h(x) = h(x).

It is not necessary that h is a one-way function because x = a' (mod p) is already the result of a one-way function. We can take for h(x) the t least significant bits of x. The total number of communication bits for h(x),e,y is 2t+ 140 which is less than half that of other schemes. The transmission of e is not necessary, e can be fixed to h(x). Then the pair (y,h(x)) is a signature of the empty message with respect to the following signature scheme.

Protocol for signature generation.

To sign message m using the private key s perform the following steps:

1. Preprocessing (see section 3). Pick a random number r E (1. ...,q) and compute x := ar(mod p).

2. Compute e := h(x,m) E (0 ,..., 2 -1).

3. Compute y :a r + se (mod q) and output the signature (e,y).

Protocol for signature verification.

To verify the signature (e,y) for message m and public key v compute x = a' V' (mod p) and check that e = h(x,m) (signature test). 

A signature (e,y) is considered to be valid if it withstands the signature test.

A signature generated according to the protocol is always valid since With t = 72 and q - 2"' the signature (e,y) is 212 bits long.

v' = a' ve (mod p) . r r+ae
x=a =a

Efficiency. The work for signature generation consists mainly of the preprocessing (see section 3) and the computation of se(mod q) where the numbers s and e are about 140 and t = 72 bits long. The latter multiplication is negligible compared with a modular multiplication in the RSA-scheme.

Signature verification consists mainly of the computation of x = a' ve (mod P) which can be done on the average using 1.5 I + 0.25 t multiplications modulo P where 1 = rlog2ql is the bit length of q. For this let y and e have the binary representations
I-1 1-1
i=O i =O
i
y = yi2' , e - ei2 with yi,ei E (0,l) , ei = 0 for i I t .

We compute av in advance and we obtain x as follows

1. i:=l, z:=l,
2. while i 2 0 do [i := i-1, z := za ayi v*~ (mod p)] ,
3. x:- z.
I

This computation requires at most I + t - 1 + C yi modular multiplications. If half of the bits yi with i 2 t are zero, and ei = yi = 0 holds for one fourth of the i < t , then there are at most 1.5 I + 0.25 t modular multiplications.
i=t

Comparison with ElGamal signatures. An ElGamal signature (y,x) for the message m and keys v,s with v = a-' (mod p) satisfies the equation am = v x (mod p) and can be generated from a random number r by setting x := a' (mod p) and by computing y from the equation

We replace in equation (1) x by the hash value e = h(x,m) . Then we can dispense with the right side m in equation (1) which we make zero. We further simplify (1) in that we replace the product ry by y-r and p-1 by 9. This transforms (1) into the new equation y = r + es (mod q) . The new signatures are much shorter.
XY
ry - sx = m (mod p-1) (1)

The choice of the prime q. The prime q must be at least 140 bits long in order to sustain a security level of 2" steps. This is because can be found in O(&) steps by the baby step giant step method. In order to compute u,v 5 r-&l such that log,(x) = u + r&lv we enumerate the sets Si = log,(x) E (1, ...,q) (a"(mod p) 10 I u 5 r61) and S2 = {x a -rfilV (mod p) I o 5 v 5 rJsii and we search for a common element a" = XQ - rG1v (mod p) . 

The choice of the hash function h. We distinguish two types of attacks:

a) Given a message m find a signature for m,

b) chosen message attack. Sign an unsigned message by using signatures of

In order to thwart the attack a) the function h(x,m) must be almost uniform with respect to x in the following sense. For every message m, every e E (0,...,2t-1) and random x E Zi the probability probJh(x,m) = el must be near to 2-t. Otherwise, in case that for fixed m,e the event has nonnegligible probability with respect to random x, the cryptanalyst can compute x := a' re (mod p) for arbitrary y-values until the equality e = h(x,m) holds. The equality yields a signature (y,e) for message m. If h(x,m) is uniformly distributed with respect to random x then this attack requires about 2' steps. messages of your choice. h(x,m) = e
-
In order to thwart the chosen message attack the function h(x,m) must be one-way in the argument m. Otherwise the cryptanalyst can choose y,e arbitrarily, he computes x := a' ve (mod p) and solves e = h(x,m) for m.

Then he has found a signature for an arbitrary message m.

It is not necessary that the function h(x,m) is collision-free with respect to m. Suppose the cryptanalyst finds messages m and m' such that h(x,m) = h(x,m')
for some x - a' (mod p) . If he asks for a signature for m' then this signature is
based on a new random number x' and cannot simply be used to sign m. The
equality h(x,m) = h(x,m') only helps to sign m if a signature (y,e) for m' is
given such that x = a' v* (mod p) , But if h(x,m) is one-way in m then it is
difficult to solve h(x,m) = h(x,m') for given x,m'.

3. Preprocessing the random number exponentiation

We describe an efficient method for preprocessing the random numbers r
and x := a' (mod p), that are used for signature generation. This preprocessing
mode also applies to other discrete log-cryptosystems such as the schemes by
ElGamal (1985), Beth (1988) and Ghter (1989).

The smart card stores a collection of k independent random pairs (ri,xi) for
i=l, ..., k such that xi = ari (mod p) where the numbers ri are independent
random numbers in (I, ...,q). Initially these pairs can be generated by the KAC.
For every signature/identification the card uses a random combination (r,x) of
these pairs and subsequently rejuvenates the collection of pairs by combining
randomly selected pairs. We use a random combination (r,x) in order to release
minimum information on the pairs (ri,xi) i = I, ..., k . For each signature
generation we randomize the pairs (ri,xi) so that no useful information can be 
collected on the long run. We give an example of a preprocessing algorithm that
demonstrates the method. It uses a security parameter d, for all practical
purposes d and k can be fairly small integers, for this paper we assume that 6 I
d,k .

Preprocessing algorithm

Initiation Load q,xi for i-1, ..., k , v := 1 (v is the round number).

1. Pick random numbers a(0), ..., a(d-3) E {l, ..., k), a(d-2) := a(d) := v-1 (mod k),
a(d-1) :- v.
d d
i=O i=O

2. rv := C ra(i) 2' (mod 9) , X, := n Xa(i) (mod P) 9
(Below we give a detailed algorithm for this computation.)

3. Keep for the next signature/identification the pair r, x with
r := r;" + 2.rW-l (mod q), x := x,

4. v := v+l (mod k) , go to 1 for the next round.
old . 2 xv-l (mod p).

REMARKS. 1. By the choice of a(d-1) the preprocessing preserves the uniform
distribution on (r1, ... ,rk).

2. The setting a(d) :- v-1 (mod k) has the effect that step 2 shifts the binary
representation of rv-l for d positions to the left and subsequently adds it to r,.

Theorem 4.2 relies on the choice of a(d-1). Lemma 4.3 relies on the choice a(d),
and Theorem 4.4 relies on the choice of a(d-2), a(d-1) and a(d).

3. The preprocessing algorithm must not be public. Each smart card can have its
own secret algorithm for preprocessing. There are many variations of the above
technique. It is possible to take for (ra(i),xa(i)) with 0 I i < d-2 the key pair
(-S,V).

We describe step 2 of the preprocessing algorithm in detail. Step 2 can be done
using only 2d multiplications modulo p, d additions modulo q and d shifts.

Step 2 of the preprocessing algorithm.
1. u := rap) , 2 := Xa(d) , i := d-1 . 

2. while i L 0 do [u := 2u + ra(i) (mod q) , z := z xa0) (mod p) ,

3. r, := u ,
a
if i = d-1 then (r := u, x := z) , i := i-l] . x, := z .

4. Cryptanalysls of preprocessing

The preprocessing algorithm combines two fundamental principles local
randomization and internal randomization. The pairs (r,x) that are used for
signatures are locally random in the sense that every k consecutive pairs are
independent, see Theorem 4.2. The random indices a(0), ..., a(d-3) perform an
internal randomization. The principles of local and of internal randomization
are complementary and can also be used for the construction of pseudo-random 
number generators and hash functions.

Notations. We denote the number a(i) of round Y as a(i,u). Let T, be the kxk
integer matrix that describes the transformation of the numbers r1, ..., rk in
round u of the preprocessing algorithm, i.e. step 2 of round u performs rT := T,
r (mod q) where r = (rl, ..., rk) . For j L 0 let r; be the number r after j
rounds. The sequence of r-values that is used for signatures is ri,ri, ... *ri .
T

Lemma 4.1
then this distribution is preserved throughout the preprocessing provided that 2
4.

If the initial vector (rl ,..., rk) is uniformly distributed over (1, ..., q)
d <

Proof. T, is the identity matrix except for row u. Row u is determined by the
transformation of r, in step 2:
r, := r, (det T,) + C ra(i,,) 2' (mod q)
a(i,u) #J

where det T, - C 2' . It follows from a(d-1,u) = u and a(d,u) u that

det T, is a nonzero integer and thus 1 I det T, < 2 < q . We see that T, is
invertible modulo q. Therefore T, preserves the uniform distribution on
a(ip)=Y
d
{I*..., qIk - 0

A similar argument proves the next theorem.

Theorem 4.2 If the initial vector (r1, ..., rk) is uniformly distributed over {l, ..., q}
then for all j 10 and for all numbers a(i,u) , 0 I i I d-3 , u I k+j the vector
k (ri+j ,... *rz+j) is, for sufficiently large q, uniformly distributed over (1, ..., q) .

It is an open problem whether the vector (r;lr...,r:k)
15 u 5 ik .
is uniformly
distributed for all indices 1s it < iz ... < ik . We believe that this holds for all
but a negligible fraction of the instances for a(i,u)

Because of Theorem 4.2 the cryptanalyst can only attack a sequence of more
than k consecutive signatures/identifications. The set of the first k+l signatures
can be attacked by guessing the numbers a(0), ..., a(d-3) of the first k rounds.
Given these numbers and the first k+l signatures the cryptanalyst can determine
the secret key s and the initial numbers rl, ..., rk by solving a system of k+l
linear equations modulo q. This attack requires an exhaustive search over k
cases.
(d-2)k

Let rnyer be the number r, after u rounds of preprocessing. If q and the
numbers a(0), ..., a(d-3) for u rounds are fixed then the number myw is a
function of the initial numbers r1, ..., rk which is linear over Z,. 

Lemma 4.3 Pairwise distinct instances for the numbers a(0), ..., a(d-3) of v
rounds generate, for sufficiently large q, pairwise distinct linear functions r?" =
r, (r1, ..., rk) depending on the initial numbers TI,.. .,rk and q. n *w

Proof. Let S, := T, T,-l -.. TI be the product matrix that describes the
transformation on r for the first v rounds of preprocessing. This is an integer
matrix that does not depend on q. The dominant row (i.e. the row with the
maximal entry) of S, is the row v(mod k), call this row vector s,. We show how
to decipher all numbers a(i) of the first v rounds from s,. To simplify the
argument let a(i,l) for i=O, ..., d be pairwise distinct. Then the j-largest entry of
s, is in column a(d-j+l,l) for j-0, ..., d. (In general we can determine from the
relative size of the largest entries of s, which of the numbers a(i,l) coincide.)
This clearly holds for v = 1 and the induction step from v - 1 to v follows
from a(d,v) - v-1 (mod k). This shows how to obtain from s,, the matrix TI.
Given the matrix TI we form the vector s, Ti' which is the dominant row of
the matrix T, T,-1 .-. Tt that corresponds to v-1 rounds starting with round
number 2. Thus we can decipher in the same way the numbers a(i.2) for i=l, ..., d
and the matrix T2 from s, Ti'. Recursively we obtain from s, all numbers a(i)
of the first Y rounds. Now the claim follows from the equation
rU s,, rT (mod q) n.r =

where r = (r1, ..., rk) is the initial r-vector. 0

For random input (rl ,..., rk) E (a,) two distinct linear functions over Z,
give the same output with probability l/q. Therefore if the number of choices
for a(0), ..., a(d-3) over v rounds is about q then the number r;" is completely
randomised by the numbers of v rounds, and thus r?" is
quasi-independent of r1, ..., rk .
a(0), ..., a(d-3)

Let a be the vector a = (a(i,v) I i=O ,..., d-3, v=l, ..., k) . The number rL+1 is
determined by rl ,..., rk , q and a. We know from Theorem 4.2 that the linear
transformation (rl, ..., rk) - (rl, ..., rk) is invertible modulo q. Therefore we have
a function rk+1 = rk+l(rl ,..., rk,q,a) that is linear in rl ,..., rk over 2,. By the
next theorem distinct instances of a yield, for sufficiently large q, distinct
functions r;+1 in ri, ..., ri .
+.
2.. 1.

Theorem 4.4 Pairwise distinct instances for the numbers a(0), ..., a(d-3) of the
first k rounds generate, for sufficiently large q, pairwise distinct linear functions
r;+l depending on ri, ..., rl .

Proof. We show that distinct vectors a generate, for sufficiently large q, distinct
linear functions ri+l(rI, ..., rk,q,a) where the inputs are the initial numbers 
rl, ... ,rk. Let s;+1 be the coefficient vector of the linear function
r;+l(rI, ..., rk,q,a), i.e. r;+l (r1, ..., rk,q,a) = s;+1 r (mod q) with r = (rl, ..., rk) . BY
the method in the proof of Lemma 4.3 we can decipher from s;+1 all numbers
a(i) of the first k rounds.
T

Now the claim follows from the choice a(d-2,v) = v-1 (mod k) . It follows
by an argument that is similar but more involved than the one for the proof of
Lemma 4.3. 0

The fastest attack to the preprocessing algorithm that we are aware of
enumerates the linear functions rk+l(rl,...,rk,q,a) that have high probability;
the probability space is the set of all vectors a. For the security level 272 it is
necessary that the maximal probability for these linear functions is not much
larger than 2-72. In order to break the preprocessing it is sufficient to guess two
functions rk+l(rl, ..., rk,q,a) and rk+2(r2,...,rk+l,q,a) . Given these two functions
we can uncover the secret key s from the first k+2 signatures by solving a
system of linear equations.
.**
.*. ...

We finally consider attacks on arbitrarily many signatures from a different
point of view. The problem to recover the secret key s and the initial numbers
r1, ..., rk when the first n signatures are given, can be put into the following
form.

Given integers y1, ..., yn E (1 ,... ,q} and el ,..., en E Z

Find integers s,r1. ..., rk E (l,...,~) such that there exist integers tij, 0 I tij <
(4.1)
k
j=1
2i (d +1) , satisfying yi = eis + 1 tij rj (mod q) i=l, ..., n .

The searched integers tij are from the linear transformation (r1 ,..., rk) -
(rl, ..., rn) , hence 0 I tij -< 2 i(d+l) If k(d-2)k > q the equation (4.1) is, for
almost all yl,el, ..., y,,en.s,rl ,..., rk , solvable for ti2 such that 0 I tij < 2
This makes this attack useless. However if k and d are small the solvability of
equation (4.1) with
rl, ..., rk . It is interesting to determine the complexity of finding r1, ..., rk such
that (4.1) is solvable with "small" integers tij. It seems that this problem is more
difficult than the knapsack problem since in our case all knapsack items s and
r1, ..., rk are unknown.
*.
i(d+l)
i(d+l) 0 5 ti8 .c 2 may characterize the searched numbers

Conclusion. There is a trade-off between the parameters k and d. It is
sufficient to have q L 214' , k = 8 and d = 6 , then k ('-2)' = 296 . It is
possible to further reduce k and d but we must have k 22 . (d-2)k 72 

5. The performance of the signature scheme

We wish to achieve a security level of 272 operations, i.e. the best known
method for forging a signatures/identification should require at least Z7' steps.
In order to obtain the security level 272 we choose q 2 2"' and t = 72 . We
choose for the preprocessing algorithm, the parameters k = 8, and d = 6. For
the new scheme the number of multiplication steps and the length of signatures
are independent of the bit length of p. Only the length of the public key
depends on p. For this we assume that p is 512 bits long. We compare the
performance of the new scheme to the Fiat-Shamir scheme (k=8, t-9) the
RSA-scheme and the GQ-scheme of Guillou and Quisquater.

# of multiplications new scheme Fiat-Shamir RSA GQ
t-72 k-8 , t-9
signature generation 0
(without preprocessing)
451 750; 216*
preprocessing 12* 0 0 0
signature verification 228* 45* 12 108*
*) can ba reduced by optimiiation

Fast algorithms for signature verification exist for the RSA-scheme with small
exponent and for the Micali-Shamir variant of the Fiat-Shamir scheme. The
new scheme is most efficient for signature generation.

# bytes for the new scheme
System parameters p,q
a 64
public key v 64
private key s 18.5
signature (e,y) 26.5
preprocessing (ri,Xi) i=1, ..., 8 (6, resp.)
82.5 (26, resp. see below) ll
660 (495, resp. see below)

We can choose particular primes q and p such that

The particular form simplifies the arithmetic modulo q and modulo p, and
requires only 26 bytes to store p and q. We are not aware of any disadvantage of
this particular form for p and q. In total about 800 (635, resp.) bytes EEPROM
are sufficient to store p,q,v,e,y and (ri,xi) for i-1, ..., 8 (6, resp.), a is not needed
for signature generation. About 192 bytes RAM are necessary to perform
modular multiplications with a 512 bit modulus p. The program for signature
generation requires less than 500 bytes ROM.
lq-2*'01 I 240 , 1~-2~~~l . 

Optimization. We give a variant of the preprocessing algorithm that uses only
k=6 pairs (ri,Xi) and which require on the average 12.76 modular multiplications
per round. First let k-6 and let (r7,xV) be the pair (-s,v).

Optimized preprocessing

1. r := r,,-l + r,, (mod q) , x :=
keep the pair r, x for the next signature/identification,
u := r + rU-1 (mod q) , z := x -
[pick with probability 7-3/29, 7/29, 1/29 resp.

2 , 1 , 0 resp. distinct random numbers a E (1 ,..., 7) .
u := 2u + 1 ra (mod q) , z := z IT x, (mod p)].

3. r,, := u, x,, := z, u := v+l (mod 7), go to 1 for the next round.
+ x, (mod p) ,
(mod p)
2. for j = 1,...,4 do
2
a a

The number of possible transformations per round is about [7.3 + 7 + l]' =
29'. The number of possible transformations over 6 rounds is about 29'" CCI 211*
which is sufficiently large to perform an internal randomization. The average
number of modular multiplications is 6 + 4(2.7.3 + 7) / 29 w 12.76 .

We can further reduce either the number of pairs (ri.xi) or the number of
modular multiplications by inserting write operations into step 2 of the
preprocessing. We can at the end of the inner loop of step 2 decide, based on a
coin flip, whether to replace some pair (ra,x.) by (u,z). This will increase the
number of possible transformations per round. However this variant will only be
practical if write operations are sufficiently fast.


Acknowledgement I wish to thank J. Hastad for his criticism of the previous
version of the preprocessing algrithm.

References

BETH, T.: A Fiat-Shamir-like authentication protocol for the ElGamal scheme.
Proceedings of Eurocrypt' 88, Lecture Notes in Computer Science 330, (1988) pp.
77-86.
CHAUM, D., EVERTSE, J.H. and van de GRAAF, I.: An Improved protocol for
Demonstration Possession of Discrete Logarithms and some Generalizations.
Proceedings of Eurocrypt' 87, Lecture Notes in Computer Science 304, (1988).
pp. 127-141.
COPPERSMITH, D., ODLYZKO, A. and SCHROEPPEL, R.: Discrete
Logarithms. Algorithmica 1, (1986), pp. 1-15. 
251
ELGAMAL, T.: A Public Key Cryptosystem and a Signature Scheme Based on
Discrete Logarithms. IEEE Transactions on Information Theory 31 (198% pp.
469-472.
FEIGE, U., FIAT, A. and SHAMIR, A.: Zero knowledge proofs of identity
Proceedings of STOC 1987, pp. 210-217.
FIAT, A. and SHAMIR, A.: How to Prove Yourself: Practical Solutions of
Identification and Signature Problems. Proceedings of Crypto 1986, in Lecture
Notes in Computer Science (Ed. A. Odlyzko), Springer Verlag, 263, (1987) pp.
186-194.
GOLDWASSER, S., MICALI, S. and RACKOFF, C.: Knowledge Complexity of
Interactive Proof Systems. Proceedings of STOC 1985, pp. 291-304.
GONTER, C.G.: Diffie-Hellman and ElGamal protocols with one single
authentication key. Abstracts of Eurocrypt' 89, Houthalen (Belgium) April 1989.
MICALI, S. and SHAMIR, A.: An Improvement of the Fiat-Shamir
Identification and Signature Scheme. Crypto 1988.
QUISQUATER, J.J. and GUILLOU, L.S.: A practical zero-knowledge protocol
fitted to security microprocessor minimizing both transmission and memory.
Proceedings Eurocrypt' 88. Springer Verlag, Lecture Notes in Computer Sciences,
VOI. 330, (1988), pp. 123-128.
RABIN, M.O.: Digital signatures and public-key functions as intractable as
factorization. Technical Report MIT/LCS/TR-212 (1978). 
I,S,V
pick random r
x := Q~ (mod p)
y := r + se (mod q)
prover
x := gr (mod p)
e := h(x,m)
y := r + se (mod q)
signature neneration
J
identification
FIG. 1
clA9P.h
message m
J
I,v,
e,y>
check I, v
pick random e
-
x := aYve(mod p)
check that
h(x) = h(y)
verifier
check I, v
-
x := Q’ ve (mod p)
check that
e = h(x,m)
signature verification
FIG. 2
