Public-Key Cryptosystems Based on Composite

Degree Residuosity Classes

Pascal Paillier1,2

原文：https://link.springer.com/content/pdf/10.1007%2F3-540-48910-X_16.pdf

1 GEMPLUS

Cryptography Department

34 Rue Guynemer, 92447 Issy-Les-Moulineaux

paillier@gemplus.com 2 ENST

Computer Science Department

46, rue Barrault, 75634 Paris Cedex 13

paillier@inf.enst.fr

Abstract. This paper investigates a novel computational problem, namely the Composite Residuosity Class Problem, and its applications to public-key cryptography. We propose a new trapdoor mechanism and derive from this technique three encryption schemes : a trapdoor permutation and two homomorphic probabilistic encryption schemes computationally comparable to RSA. Our cryptosystems, based on usual modular arithmetics, are provably secure under appropriate assumptions in the standard model.

1 Background

Since the discovery of public-key cryptography by Diffie and Hellman [5], very few convincingly secure asymetric schemes have been discovered despite considerable research efforts.

We refer the reader to [26] for a thorough survey of existing public-key cryptosystems. Basically, two major species of trapdoor techniques are in use today. The first points to RSA [25] and related variants such as Rabin-Williams [24,30],LUC, Dickson’s scheme or elliptic curve versions of RSA like KMOV [10]. The technique conjugates the polynomial-time extraction of roots of polynomials over a finite field with the intractability of factoring large numbers. It is worthwhile pointing out that among cryptosystems belonging to this family, only RabinWilliams has been proven equivalent to the factoring problem so far.

Another famous technique, related to Diffie-Hellman-type schemes (El Gamal[7], DSA, McCurley [14], etc.) combines the homomorphic properties of the modular exponentiation and the intractability of extracting discrete logarithms over finite groups. Again, equivalence with the primitive computational problem remains open in general, unless particular circumstances are reached as described in [12].

Other proposed mechanisms generally suffer from inefficiency, inherent security weaknesses or insufficient public scrutiny : McEliece’s cryptosystem [15]based on error correcting codes, Ajtai-Dwork’s scheme based on lattice problems (cryptanalyzed by Nguyen and Stern in [18]), additive and multiplicative knapsack-type systems including Merkle-Hellman [13], Chor-Rivest (broken by Vaudenay in [29]) and Naccache-Stern [17] ; finally, Matsumoto-Imai and Goubin-Patarin cryptosystems, based on multivariate polynomials, were successively cryptanalyzed in [11] and [21].

We believe, however, that the cryptographic research had unnoticeably witnessed the progressive emergence of a third class of trapdoor techniques : firstly identified as trapdoors in the discrete log, they actually arise from the common algebraic setting of high degree residuosity classes. After Goldwasser-Micali’s scheme [9] based on quadratic residuosity, Benaloh’s homomorphic encryption function, originally designed for electronic voting and relying on prime residuosity, prefigured the first attempt to exploit the plain resources of this theory. Later, Naccache and Stern [16], and independently Okamoto and Uchiyama [19] significantly extended the encryption rate by investigating two different approaches :residuosity of smooth degree in Z∗pq and residuosity of prime degree p in Z∗p2q respectively. In the meantime, other schemes like Vanstone-Zuccherato [28] on elliptic curves or Park-Won [20] explored the use of high degree residues in other settings.

In this paper, we propose a new trapdoor mechanism belonging to this family. By contrast to prime residuosity, our technique is based on composite residuosity classes i.e. of degree set to a hard-to-factor number n = pq where p and q are two large prime numbers. Easy to understand, we believe that our trapdoor provides a new cryptographic building-block for conceiving public-key cryptosystems.

In sections 2 and 3, we introduce our number-theoretic framework and investigate in this context a new computational problem (the Composite Residuosity Class Problem), which intractability will be our main assumption. Further, we derive three homomorphic encryption schemes based on this problem, including a new trapdoor permutation. Probabilistic schemes will be proven semantically secure under appropriate intractability assumptions. All our polynomial reductions are simple and stand in the standard model.

Notations. We set n = pq where p and q are large primes : as usual, we will denote by φ(n) Euler’s totient function and by λ(n) Carmichael’s function1 taken on n, i.e. φ(n)=(p − 1)(q − 1) and λ(n) = lcm(p − 1, q − 1) in the present case. Recall that |Z∗n2| = φ(n2) = nφ(n) and that for any w ∈ Z∗n2,wλ = 1 mod n wnλ = 1 mod n2 ,

which are due to Carmichael’s theorem. We denote by RSA [n, e] the (conventionally thought intractable) problem of extracting e-th roots modulo n where n = pq is of unknown factorisation. The relation P1 ⇐ P2 (resp. P1 ≡ P2) will denote that the problem P1 is polynomially reducible (resp. equivalent) to the problem P2.

1 we will adopt λ instead of λ(n) for visual comfort.

2 Deciding Composite Residuosity

We begin by briefly introducing composite degree residues as a natural instance of higher degree residues, and give some basic related facts. The originality of our setting resides in using of a square number as modulus. As said before, n = pq is the product of two large primes.

Definition 1. A number z is said to be a n-th residue modulo n2 if there exists a number y ∈ Z∗n2 such that

z = yn mod n2 .

The set of n-th residues is a multiplicative subgroup of Z∗n2 of order φ(n).Each n-th residue z has exactly n roots of degree n, among which exactly oneis strictly smaller than n (namely √n z mod n). The n-th roots of unity are the numbers of the form (1 + n)x =1+ xn mod n2.

The problem of deciding n-th residuosity, i.e. distinguishing n-th residues from non n-th residues will be denoted by CR [n]. Observe that like the problems of deciding quadratic or higher degree residuosity, CR [n] is a random-selfreducible problem that is, all of its instances are polynomially equivalent. Each case is thus an average case and the problem is either uniformly intractable or uniformly polynomial. We refer to [1,8] for detailed references on random-selfreducibility and the cryptographic significance of this feature.

As for prime residuosity (cf. [3,16]), deciding n-th residuosity is believed to be computationally hard. Accordingly, we will assume that :

Conjecture 2. There exists no polynomial time distinguisher for n-th residues modulo n2, i.e. CR [n] is intractable.

This intractability hypothesis will be refered to as the Decisional Composite Residuosity Assumption (DCRA) throughout this paper. Recall that due to the random-self-reducibility, the validity of the DCRA only depends on the choice of n.

3 Computing Composite Residuosity Classes

We now proceed to describe the number-theoretic framework underlying the cryptosystems introduced in sections 4, 5 and 6. Let g be some element of Z∗n2 and denote by Eg the integer-valued function defined by

Zn × Z∗n 7−→ Z∗n2

(x, y) 7−→ gx · yn mod n2

Depending on g, Eg may feature some interesting properties. More specifically,

Lemma 3. If the order of g is a nonzero multiple of n then Eg is bijective.

We denote by Bα ⊂ Z∗n2 the set of elements of order nα and by B their disjoint union for α = 1, ··· , λ.

Proof. Since the two groups Zn × Z∗n and Z∗n2 have the same number of elements nφ(n), we just have to prove that Eg is injective. Suppose that gx1 yn1 =gx2 yn2 mod n2. It comes gx2−x1 ·(y2/y1)n = 1 mod n2, which implies gλ(x2−x1) =1 mod n2. Thus λ(x2 − x1) is a multiple of g’s order, and then a multiple of n. Since gcd(λ, n) = 1, x2 − x1 is necessarily a multiple of n. Consequently,x2 −x1 = 0 mod n and (y2/y1)n = 1 mod n2, which leads to the unique solution y2/y1 = 1 over Z∗n. This means that x2 = x1 and y2 = y1. Hence, Eg is bijective.ut

Definition 4. Assume that g ∈ B. For w ∈ Z∗n2, we call n-th residuosity class of w with respect to g the unique integer x ∈ Zn for which there exists y ∈ Z∗n such that

Eg(x, y) = w .

Adopting Benaloh’s notations [3], the class of w is denoted [[w]]g. It is worthwhile noticing the following property :

Lemma 5. [[w]]g = 0 if and only if w is a n-th residue modulo n2. Furthermore,

∀w1, w2 ∈ Z∗n2 [[w1w2]]g = [[w1]]g + [[w2]]g mod n

that is, the class function w 7→ [[w]]g is a homomorphism from (Z∗n2, ×) to (Zn, +)for any g ∈ B.

The n-th Residuosity Class Problem of base g, denoted Class [n, g], is defined as the problem of computing the class function in base g : for a given w ∈ Z∗n2,compute [[w]]g from w. Before investigating further Class [n, g]’s complexity, we begin by stating the following useful observations :

Lemma 6. Class [n, g] is random-self-reducible over w ∈ Z∗n2.

Proof. Indeed, we can easily transform any w ∈ Z∗n2 into a random instance w0 ∈ Z∗n2 with uniform distribution, by posing w0 = w gαβn mod n2 where αand β are taken uniformly at random over Zn (the event β 6∈ Z∗n occurs with negligibly small probability). After [[w0]]g has been computed, one has simply to return [[w]]g = [[w0]]g − α mod n. ut

Lemma 7. Class [n, g] is random-self-reducible over g ∈ B, i.e.

∀g1, g2 ∈ B Class [n, g1] ≡ Class [n, g2] .

Proof. It can easily be shown that, for any w ∈ Z∗n2 and g1, g2 ∈ B, we have

[[w]]g1 = [[w]]g2 [[g2]]g1 mod n , (1)

which yields [[g1]]g2 = [[g2]]−1g1 mod n and thus [[g2]]g1 is invertible modulo n.Suppose that we are given an oracle for Class [n, g1]. Feeding g2 and w into the oracle respectively gives [[g2]]g1 and [[w]]g1 , and by straightforward deduction :

[[w]]g2 = [[w]]g1 [[g2]]−1g1 mod n .

Lemma 7 essentially means that the complexity of Class [n, g] is independent from g. This enables us to look upon it as a computational problem which purely relies on n. Formally,

Definition 8. We call Composite Residuosity Class Problem the computational problem Class [n] defined as follows : given w ∈ Z∗n2 and g ∈ B, compute [[w]]g.

We now proceed to find out which connections exist between the Composite Residuosity Class Problem and standard number-theoretic problems. We state first :

Theorem 9. Class [n] ⇐ Fact [n].

Before proving the theorem, observe that the set

Sn = u<n2 | u = 1 mod n  

is a multiplicative subgroup of integers modulo n2 over which the function L such that

∀u ∈ Sn L(u) = u − 1n

is clearly well-defined.

Lemma 10. For any w ∈ Z∗n2, L(wλ mod n2) = λ [[w]]1+n mod n.

Proof (of Lemma 10). Since 1 + n ∈ B, there exists a unique pair (a, b) in the set Zn × Z∗n such that w = (1 + n)abn mod n2. By definition, a = [[w]]1+n. Then

wλ = (1 + n)aλbnλ = (1 + n)aλ =1+ aλn mod n2,

which yields the announced result.

Proof (of Theorem 9). Since [[g]]1+n = [[1 + n]]−1g mod n is invertible, a consequence of Lemma 10 is that L(gλ mod n2) is invertible modulo n. Now, factoring n obviously leads to the knowledge of λ. Therefore, for any g ∈ B and w ∈ Z∗n2,we can compute

L(wλ mod n2)L(gλ mod n2) = λ [[w]]1+nλ [[g]]1+n= [[w]]1+n

[[g]]1+n= [[w]]g mod n , (2)

by virtue of Equation 1. 

Theorem 11. Class [n] ⇐ RSA [n, n].

Proof. Since all the instances of Class [n, g] are computationally equivalent for g ∈ B, and since 1 + n ∈ B, it suffices to show that

Class [n, 1 + n] ⇐ RSA [n, n] .

Let us be given an oracle for RSA [n, n]. We know that w = (1 +n)x · yn mod n2 for some x ∈ Zn and y ∈ Z∗n. Therefore, we have w = yn mod n and we get y by giving w mod n to the oracle. From now,

wyn = (1 + n)x =1+ xn mod n2 ,

which discloses x = [[w]]1+n as announced. 

Theorem 12. Let D-Class [n] be the decisional problem associated to Class [n]i.e. given w ∈ Z∗n2, g ∈ B and x ∈ Zn, decide whether x = [[w]]g or not. Then

CR [n] ≡ D-Class [n] ⇐ Class [n] .

Proof. The hierarchy D-Class [n] ⇐ Class [n] comes from the general fact that it is easier to verify a solution than to compute it. Let us prove the left-side equivalence. (⇒) Submit wg−x mod n2 to the oracle solving CR [n]. In case of n-th residuosity detection, the equality [[wg−x]]g = 0 implies [[w]]g = x by Lemma 5 and then answer ”Yes”. Otherwise answer ”No” or ”Failure” according to the oracle’s response. (⇐) Choose an arbitrary g ∈ B (1 + n will do) and submit the triple (g, w, x = 0) to the oracle solving D-Class [n]. Return the oracle’s answer without change. 

To conclude, the computational hierarchy we have been looking for was

CR [n] ≡ D-Class [n] ⇐ Class [n] ⇐ RSA [n, n] ⇐ Fact [n] , (3)

with serious doubts concerning a potential equivalence, excepted possibly between D-Class [n] and Class [n]. Our second intractability hypothesis will be to assume the hardness of the Composite Residuosity Class Problem by making the following conjecture :

Conjecture 13. There exists no probabilistic polynomial time algorithm that solves the Composite Residuosity Class Problem, i.e. Class [n] is intractable.

By contrast to the Decisional Composite Residuosity Assumption, this conjecture will be refered to as the Computational Composite Residuosity Assumption (CCRA). Here again, random-self-reducibility implies that the validity of the CCRA is only conditioned by the choice of n. Obviously, if the DCRA is true then the CCRA is true as well. The converse, however, still remains a challenging open question.

4 A New Probabilistic Encryption Scheme

We now proceed to describe a public-key encryption scheme based on the Composite Residuosity Class Problem. Our methodology is quite natural : employing Eg for encryption and the polynomial reduction of Theorem 9 for decryption, using the factorisation as a trapdoor.

Set n = pq and randomly select a base g ∈ B : as shown before, this can be done efficiently by checking whether

gcdL(gλ mod n2), n= 1 . (4)

Now, consider (n, g) as public parameters whilst the pair (p, q) (or equivalently λ) remains private. The cryptosystem is depicted below.

Encryption :

plaintext m<n

select a random r<n

ciphertext c = gm · rn mod n2

Decryption :

ciphertext c<n2

plaintext m = L(cλ mod n2)

L(gλ mod n2) mod n

Scheme 1. Probabilistic Encryption Scheme Based on Composite Residuosity.

The correctness of the scheme is easily verified from Equation 2, and it is straightforward that the encryption function is a trapdoor function with λ (that is, the knowledge of the factors of n) as the trapdoor secret. One-wayness is based on the computational problem discussed in the previous section.

Theorem 14. Scheme 1 is one-way if and only if the Computational Composite Residuosity Assumption holds.

Proof. Inverting our scheme is by definition the Composite Residuosity Class Problem. 

Theorem 15. Scheme 1 is semantically secure if and only if the Decisional Composite Residuosity Assumption holds.

Proof. Assume that m0 and m1 are two known messages and c the ciphertext of either m0 or m1. Due to Lemma 5, c is the ciphertext of m0 if and only if cg−m0 mod n2 is a n-th residue. Therefore, a successfull chosen-plaintext attacker could decide composite residuosity, and vice-versa. 

5 A New One-Way Trapdoor Permutation

One-way trapdoor permutations are very rare cryptographic objects : we refer the reader to [22] for an exhaustive documentation on these. In this section, we show how to use the trapdoor technique introduced in the previous section to derive a permutation over Z∗n2.

As before, n stands for the product of two large primes and g is chosen as in Equation 4.

Encryption :

plaintext m<n2

split m into m1, m2 such that m = m1 + nm2

ciphertext c = gm1m2

n mod n2

Decryption :

ciphertext c<n2

Step 1. m1 = L(cλ mod n2)

L(gλ mod n2) mod n

Step 2. c

0 = cg−m1 mod n

Step 3. m2 = c

0n−1 mod λ mod n

plaintext m = m1 + nm2

Scheme 2. A Trapdoor Permutation Based on Composite Residuosity.

We first show the scheme’s correctness. Clearly, Step 1 correctly retrieves m1 = m mod n as in Scheme 1. Step 2 is actually an unblinding phase which is necessary to recover mn 2 mod n. Step 3 is an RSA decryption with a public exponent e = n. The final step recombines2 the original message m. The fact that Scheme 2 is a permutation comes from the bijectivity of Eg. Again, trapdoorness is based on the factorisation of n. Regarding one-wayness, we state :

2 note that every public bijection m ↔ (m1, m2) fits the scheme’s structure, but euclidean division appears to be the most natural one.

Theorem 16. Scheme 2 is one-way if and only if RSA [n, n] is hard.

Proof. a) Since Class [n] ⇐ RSA [n, n] (Theorem 11), extracting n-th roots modulo n is sufficient to compute m1 from Eg(m1, m2). Retrieving m2 then requires one more additionnal extraction. Thus, inverting Scheme 2 cannot be harder than extracting n-th roots modulo n. b) Conversely, an oracle which inverts Scheme 2 allows root extraction : first query the oracle to get the two numbers a and b such that 1 + n = gabn mod n2. Now if w = yn0 mod n, query the oracle again to obtain x and y such that w = gxyn mod n2. Since 1 + n ∈ B,we know there exists an x0 such that w = (1 + n)x0 yn0 mod n2, wherefrom

w = (gabn)x0 yn0 = gax0 mod ngax0 div nbx0 y0n mod n2 .

By identification with w = gxyn mod n2, we get x0 = xa−1 mod n and finally y0 = yg−(ax0 div n)b−x0 mod n which is the wanted value. ut

Remark 17. Note that by definition of Eg, the cryptosystem requires that m2 ∈Z∗n, just like in the RSA setting. The case m2 6∈ Z∗n either allows to factor n or leads to the ciphertext zero for all possible values of m1. A consequence of this fact is that our trapdoor permutation cannot be employed ad hoc to encrypt short messages i.e. messages smaller than n.

Digital Signatures. Finally, denoting by h : N 7→ {0, 1}k ⊂ Z∗n2 a hash function see as a random oracle [2], we obtain a digital signature scheme as follows.For a given message m, the signer computes the signature (s1, s2) where

s1 = L(h(m)λ mod n2)

L(gλ mod n2) mod n

s2 =

h(m)g−s1

1/n mod λ mod n

and the verifier checks that

h(m) ?= gs1 sn2 mod n2 .

Corollary 18 (of Theorem 16). In the random oracle model, an existential forgery of our signature scheme under an adaptive chosen message attack has a negligible success probability provided that RSA [n, n] is intractable.

Although we feel that the above trapdoor permutation remains of moderate interest due to its equivalence with RSA, the rarity of such objects is such that we find it useful to mention its existence. Moreover, the homomorphic properties of this scheme, discussed in section 8, could be of a certain utility regarding some (still unresolved) cryptographic problems.

6 Reaching Almost-Quadratic Decryption Complexity

Most popular public-key cryptosystems present a cubic decryption complexity,and this is the case for Scheme 1 as well. The fact that no faster (and still appropriately secure) designs have been proposed so far strongly motivates the search for novel trapdoor functions allowing increased decryption performances.This section introduces a slightly modified version of our main scheme (Scheme 1)which features an O|n|2+decryption complexity.

Here, the idea consists in restricting the ciphertext space Z∗n2 to the subgroup <g> of smaller order by taking advantage of the following extension of Equation 2. Assume that g ∈ Bα for some 1 ≤ α ≤ λ. Then for any w ∈ <g>,

[[w]]g = L(wα mod n2)L(gα mod n2) mod n . (5)

This motivates the cryptosystem depicted below.

Encryption :

plaintext m<n

randomly select r<n

ciphertext c = gm+nr mod n2

Decryption :

ciphertext c<n2

plaintext m = L(cα mod n2)

L(gα mod n2) mod n

Scheme 3. Variant with fast decryption.

Note that this time, the encryption function’s trapdoorness relies on the knowledge of α (instead of λ) as secret key. The most computationally expensive operation involved in decryption is the modular exponentiation c → cα mod n2 which runs in complexity O|n|2|α|(to be compared to O|n|3in Scheme 1). If g is chosen in such a way that |α| = Ω (|n|) for some  > 0, then decryption will only take O|n|2+bit operations. To the best of our knowledge, Scheme 3 is the only public-key cryptosystem based on modular arithmetics whose decryption function features such a property.

Clearly, inverting the encryption function does not rely on the composite residuosity class problem, since this time the ciphertext is known to be an element of <g>, but on a weaker instance. More formally,

Theorem 19. We call Partial Discrete Logarithm Problem the computational problem PDL [n, g] defined as follows : given w ∈ <g>, compute [[w]]g. Then Scheme 3 is one-way if and only if PDL [n, g] is hard.

Theorem 20. We call Decisional Partial Discrete Logarithm Problem the decisional problem D-PDL [n, g] defined as follows : given w ∈ <g> and x ∈ Zn,decide whether [[w]]g = x. Then Scheme 3 is semantically secure if and only if D-PDL[n, g] is hard.

The proofs are similar to those given in section 4. By opposition to the original class problems, these ones are not random-self-reducible over g ∈ B but over cyclic subgroups of B, and present other interesting characteristics that we do not discuss here due to the lack of space. Obviously,

PDL [n, g] ⇐ Class [n] and D-PDL [n, g] ⇐ CR [n]

but equivalence can be reached when g is of maximal order nλ and n the product of two safe primes. When g ∈ Bα for some α<λ such that |α| = Ω (|n|) for > 0, we conjecture that both PDL [n, g] and D-PDL [n, g] are intractable.

In order to thwart Baby-Step Giant-Step attacks, we recommend the use of 160-bit prime numbers for αs in practical use. This can be managed by an appropriate key generation. In this setting, the computational load of Scheme 3 is smaller than a RSA decryption with Chinese Remaindering for |n| ≥ 1280.Next section provides tight evaluations and performance comparisons for all the encryption schemes presented in this paper.

7 Efficiency and Implementation Aspects

In this section, we briefly analyse the main practical aspects of computations required by our cryptosystems and provide various implementation strategies for increased performance.

Key Generation. The prime factors p and q must be generated according to the usual recommandations in order to make n as hard to factor as possible. The fast variant (Scheme 3) requires additionally λ = lcm(p−1, q −1) to be a multiple of a 160-bit prime integer, which can be managed by usual DSA-prime generation or other similar techniques. The base g can be chosen randomly among elements of order divisible by n, but note that the fast variant will require a specific treatment (typically raise an element of maximal order to the power λ/α). The whole generation may be made easier by carrying out computations separately mod p2 and mod q2 and Chinese-remaindering g mod p2 and g mod q2 at the very end.

Encryption. Encryption requires a modular exponentiation of base g. The computation may be significantly accelerated by a judicious choice of g. As an illustrative example, taking g = 2 or small numbers allows an immediate speed-up factor of 1/3, provided the chosen value fulfills the requirement g ∈ B imposed by the setting. Optionally, g could even be fixed to a constant value if the key generation process includes a specific adjustment. At the same time, pre-processing techniques for exponentiating a constant base can dramatically reduce the computational cost. The second computation rn or gnr mod n2 can also be computed in advance.

Decryption. Computing L(u) for u ∈ Sn may be achieved at a very low cost(only one multiplication modulo 2 |n|) by precomputing n−1 mod 2 |n|. The constant parameter

L(gλ mod n2)−1 mod n or L(gα mod n2)−1 mod n

can also be precomputed once for all.

Decryption using Chinese-remaindering. The Chinese Remainder Theorem [6] can be used to efficiently reduce the decryption workload of the three cryptosystems. To see this, one has to employ the functions Lp and Lq defined over

Sp = x<p2 | x = 1 mod p   and Sq = x<q2 | x = 1 mod q

  by

Lp(x) = x – 1 p and Lq(x) = x − 1q .

Decryption can therefore be made faster by separately computing the message mod p and mod q and recombining modular residues afterwards :

mp = Lp(cp−1 mod p2) hp mod p

mq = Lq(cq−1 mod q2) hq mod q

m = CRT(mp, mq) mod pq

with precomputations

hp = Lp(gp−1 mod p2)−1 mod p and

hq = Lq(gq−1 mod q2)−1 mod q .

where p − 1 and q − 1 have to be replaced by α in the fast variant.

Performance evaluations. For each |n| = 512, ··· , 2048, the modular multiplication of bitsize |n| is taken as the unitary operation, we assume that the execution time of a modular multiplication is quadratic in the operand size and that modular squares are computed by the same routine. Chinese remaindering, as well as random number generation for probabilistic schemes, is considered to be negligible. The RSA public exponent is taken equal to F4 = 216 + 1. The parameter g is set to 2 in our main scheme, as well as in the trapdoor permutation.Other parameters, secret exponents or messages are assumed to contain about the same number of ones and zeroes in their binary representation.

Schemes Main Scheme Permutation Fast Variant RSA ElGamal

One-wayness Class [n] RSA [n, n] PDL [n, g] RSA [n, F4] DH [p]

Semantic Sec. CR [n] none D-PDL [n, g] none D-DH [p]

Plaintext size |n| 2 |n| |n| |n| |p|

Ciphertext size 2 |n| 2 |n| 2 |n| |n| 2 |p|

Encryption

|n|, |p| = 512 5120 5120 4032 17 1536

|n|, |p| = 768 7680 7680 5568 17 2304

|n|, |p| = 1024 10240 10240 7104 17 3072

|n|, |p| = 1536 15360 1536 10176 17 4608

|n|, |p| = 2048 20480 20480 13248 17 6144

Decryption

|n|, |p| = 512 768 1088 480 192 768

|n|, |p| = 768 1152 1632 480 288 1152

|n|, |p| = 1024 1536 2176 480 384 1536

|n|, |p| = 1536 2304 3264 480 576 2304

|n|, |p| = 2048 3072 4352 480 768 3072

These estimates are purely indicative, and do not result from an actual implementation. We did not include the potential pre-processing stages. Chinese remaindering is taken into account in cryptosystems that allow it i.e. all of them excepted ElGamal.

8 Properties

Before concluding, we would like to stress again the algebraic characteristics of our cryptosystems, especially those of Schemes 1 and 3.

Random-Self-Reducibility. This property actually concerns the underlying number-theoretic problems CR [n] and Class [n] and, to some extent, their weaker versions D-PDL [n, g] and PDL [n, g]. Essentially, random-self-reducible problems are as hard on average as they are in the worst case : both RSA and the Discrete Log problems have this feature. Problems of that type are believed to yield good candidates for one-way functions [1].

Additive Homomorphic Properties. As already seen, the two encryption functions m 7→ gmrn mod n2 and m 7→ gm+nr mod n2 are additively homomorphic on Zn. Practically, this leads to the following identities :

∀m1, m2 ∈ Zn and k ∈ N

d(e(m1) e(m2) mod n2) = m1 + m2 mod n

d(e(m)k mod n2) = km mod n

d(e(m1) gm2 mod n2) = m1 + m2 mod n

d(e(m1)m2 mod n2)

d(e(m2)m1 mod n2)

)= m1m2 mod n .

These properties are known to be particularly appreciated in the design of voting protocols, threshold cryptosystems, watermarking and secret sharing schemes, to quote a few. Server-aided polynomial evaluation (see [27]) is another potential field of application.

Self-Blinding. Any ciphertext can be publicly changed into another one without affecting the plaintext :

∀m ∈ Zn and r ∈ N

d(e(m) rn mod n2) = m or d(e(m) gnr mod n2) = m ,

depending on which cryptosystem is considered. Such a property has potential applications in a wide range of cryptographic settings.

9 Further Research

In this paper, we introduced a new number-theoretic problem and a related trapdoor mechanism based on the use of composite degree residues. We derived three new cryptosystems based on our technique, all of which are provably secure under adequate intractability assumptions.

Although we do not provide any proof of security against chosen ciphertext attacks, we believe that one could bring slight modifications to Schemes 1 and 3 to render them resistant against such attacks, at least in the random oracle model.

Another research topic resides in exploiting the homomorphic properties of our systems to design distributed cryptographic protocols (multi-signature, secret sharing, threshold cryptography, and so forth) or other cryptographically useful objects.

10 Acknowledgments

The author is especially grateful to David Pointcheval for his precious comments and contributions to this work. We also thank Jacques Stern and an anonymous referee for having (independently) proved that Class [n] ⇐ RSA [n, n]. Finally,

Dan Boneh, Jean-S´ebastien Coron, Helena Handschuh and David Naccache are

acknowledged for their helpful discussions and comments during the completion of this work.

References

\1. D. Angluin and D. Lichtenstein, Provable Security of Cryptosystems: A Survey,

Computer Science Department, Yale University, TR-288, 1983.

\2. M. Bellare and P. Rogaway, Random Oracles are Practical : a Paradigm for Designing Efficient Protocols, In Proceedings of the First CCS, ACM Press, pp. 62–73,

1993.

\3. J. C. Benaloh, Verifiable Secret-Ballot Elections, PhD Thesis, Yale University, 1988.

\4. R. Cramer, R. Gennaro and B. Schoenmakers, A Secure And Optimally Efficient Multi-Authority Election Scheme, LNCS 1233, Proceedings of Eurocrypt’97,

Springer-Verlag, pp. 103-118, 1997.

\5. W. Diffie and M. Hellman, New Directions in Cryptography, IEEE Transaction on

Information Theory, IT-22,6, pp. 644–654, 1995.

\6. C. Ding, D. Pei and A. Salomaa, Chinese Remainder Theorem - Applications in

Computing, Coding, Cryptography, World Scientific Publishing, 1996.

\7. T. ElGamal, A Public-Key Cryptosystem an a Signature Scheme Based on Discrete

Logarithms, IEEE Trans. on Information Theory, IT-31, pp. 469–472, 1985.

\8. J. Feigenbaum, Locally Random Reductions in Interactive Complexity Theory,

in Advances in Computational Complexity Theory, DIMACS Series on Discrete

Mathematics and Theoretical Computer Science, vol. 13, American Mathematical

Society, Providence, pp. 73–98, 1993.

\9. S. Goldwasser and S. Micali, Probabilistic Encryption, JCSS Vol. 28 No 2, pp.

270–299, 1984.

\10. K. Koyama, U. Maurer, T. Okamoto and S. Vanstone, New Public-Key Schemes

based on Elliptic Curves over the ring Zn, LNCS 576, Proceedings of Crypto’91,

Springer-Verlag, pp. 252–266, 1992.

\11. T. Matsumoto and H. Imai, Public Quadratic Polynomial-Tuples for Efficient

Signature-Verification and Message-Encryption, LNCS 330, Proceedings of Eurocrypt’88, Springer-Verlag, pp. 419–453, 1988.

\12. U. Maurer and S. Wolf, On the Complexity of Breaking the Diffie-Hellman Protocol.

\13. R. Merkle and M. Hellman, Hiding Information and Signatures in Trapdoor Knapsacks, IEEE Trans. on Information Theory, Vol. 24, pp. 525–530, 1978.

\14. K. McCurley, A Key Distribution System Equivalent to Factoring, Journal of Cryptology, Vol. 1, pp. 95–105, 1988.

\15. R. McEliece, A Public-Key Cryptosystem Based on Algebraic Coding Theory, DSN

Progress Report 42-44, Jet Propulsion Laboratories, Pasadena, 1978.

\16. D. Naccache and J. Stern, A New Public-Key Cryptosystem Based on Higher

Residues, LNCS 1403, Advances in Cryptology, Proceedings of Eurocrypt’98,

Springer-Verlag, pp. 308–318, 1998.

238 Pascal Paillier

\17. D. Naccache and J. Stern, A New Public-Key Cryptosystem, LNCS 1233, Advances

in Cryptology, Proceedings of Eurocrypt’97, Springer-Verlag, pp. 27–36, 1997.

\18. P. Nguyen and J. Stern, Cryptanalysis of the Ajtai-Dwork Cryptosystem, LNCS

1462, Proceedings of Crypto’98, Springer-Verlag, pp. 223–242, 1998.

\19. T. Okamoto and S. Uchiyama, A New Public-Key Cryptosystem as secure as

Factoring, LNCS 1403, Advances in Cryptology, Proceedings of Eurocrypt’98,

Springer-Verlag, pp. 308–318, 1998.

\20. S. Park and D. Won, A Generalization of Public-Key Residue Cryptosystem, In

Proceedings of 1993 Korean-Japan Joint Workshop on Information Security and

Cryptology, pp. 202–206, 1993.

\21. J. Patarin, The Oil and Vinegar Algorithm for Signatures, presented at the

Dagstuhl Workshop on Cryptography, 1997.

\22. J. Patarin and L. Goubin, Trapdoor One-Way Permutations and Multivariate Polynomials, LNCS 1334, Proceedings of ICICS’97, Springer-Verlag, pp. 356–368, 1997.

\23. R. Peralta and E. Okamoto, Faster Factoring of Integers of a Special Form, IEICE,

Trans. Fundamentals, E79-A, Vol. 4, pp. 489–493, 1996.

\24. M. Rabin, Digital Signatures and Public-Key Encryptions as Intractable as Factorization, MIT Technical Report No 212, 1979.

\25. R. Rivest, A. Shamir and L. Adleman, A Method for Obtaining Digital Signatures

and Public-Key Cryptosystems, Communications of the ACM, Vol. 21, No 2, pp.

120–126, 1978.

\26. A. Salomaa, Public-Key Cryptography, Springer-Verlag, 1990.

\27. T. Sander and F. Tschudin, On Software Protection Via Function Hiding, Proceedings of Information Hiding Workshop’98, 1998.

\28. S. Vanstone and R. Zuccherato, Elliptic Curve Cryptosystem Using Curves of

Smooth Order Over the Ring Zn, IEEE Trans. Inf. Theory, Vol. 43, No 4, July

1997.

\29. S. Vaudenay, Cryptanalysis of the Chor-Rivest Cryptosystem, LNCS 1462, Proceedings of Crypto’98, Springer-Verlag, pp. 243–256, 1998.

\30. H. Williams, Some Public-Key Crypto-Functions as Intractable as Factorization,

LNCS 196, Proceedings of Crypto’84, Springer-Verlag, pp. 66–70, 1985.
