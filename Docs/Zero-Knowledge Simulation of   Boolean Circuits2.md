Zero-Knowledge Simulation of   Boolean Circuits 

  GiNes BRASSARD 7 and Claude CREPEAUS 
Departement  d'informatique et de RO. 
 Universite de Montr6al 
C.P. 6128, Succursale "A" 
  Montrtal (Qutbec) 
Canada H3C 3J7 

  ABSTRACT 

A zero-knowledge interactive proof  is a protocol by which Alice can convince a 
polynomially-bounded Bob  of the  truth of some theorem without giving him any hint as to 
how the proof might proceed. Under cryptographic assumptions, we give a general technique 
for achieving this goal for every problem in NP. This extends to a presumably larger class, 
which combines the powers of  non-determinism and  randomness.  Our protocol is powerful 
enough to allow Mice to convince Bob of theorems  for which she does not  even have  a 
proof:  it is  enough for Alice to convince herself probabilistidly of a theorem, perhaps 
thanks to her knowledge of some trap-door information, in order for her to be able to con- 
vince Bob as well, without compromising the map-door  in any way. 

1. INTRODUCTION 
Assume  that Alice holds the proof of some theorem.  A zero-knowledge interactive  proof 
(ZIP) is  a protocol that allows her to convince a polynomially  bounded Bob  that she owns such a 
proof,  in  a way that he will  gain nothing else than  this conviction:  engaging  in  the protocol with 
Alice gives Bob no hint on Alice's  proof, or at least  nothing he can make use of in polynomial time. 
In phcular, it does not enable him to later  convince anyone  else  that  Alice has a prmf of the 
theorem or even merely that the theorem is true  (much less that he himself has a proof!). This notion 
was introduced by Goldwasser,  Micali  and Rackoff [GMR]; the reader  is refered to this paper for 
formal definitions. An intuitive notion  of ZKIP suffices to understand this extended abstract. 
The early examples of ZIP'S were  all number theoretic  and restricted to problems in 
NP n CO-NP[GMR,   GHY]. It was  conjectured by Silvio iMcali, and  believed by most researchers, 
that such  protocols could not exist for NP-complete problems. Under  cryptographic assumptions, we 
show here that this ktuition was wrong by providing  a ZKIP for satisfiability. The same result was 
obtained independently and slightly earlier by [GMW] as they gave  a ZKIP for graph 3-colouring. 
Obviously (because Karp reductions carry NP certificates),  it  suffices to find a ZKP for W' 
NP-complete problem in order to get one for every  problem in NP. Protocols  very similar to ours for 
satisfiability are also given in  [Be, Ch]. Our  protocol is  more  attractive  in practice than that of 
[GW], but  we depend on a specific  cryptographic assumption (quadratic residuosity) whereas they 
merely need to assume the existence of secure encryption  schemes in the sense of [GMI. 

  t Supported in part by SSERC grant A4107. 
  + Supported in part by an NSERC postgraduate scholarship ; current address : M.I.T. 

A.M. Odlyzko (Ed.): Advances in Cryptology - CRYPT0 '86, LNCS 263, pp. 223-233, 1987. 
0 Springer-Verlag Berlin Heidelberg  1987 
 224 

ZKIP's are conceivable even if Alice does not  have a proof to start with. Let us assume that she 
merely has a convincing argument that  the  theorem is true. h this case, she might  wish to convince 
Bob of the theorem with a  level of confidence comparable to her own. This wader of confidence is 
zero-knowledge if it does not provide a polynomially-bounded Bob with any information on the argu- 
ment itself, except for its existence  and Alice's knowledge of it. Our main result is that such proto- 
cols exists for a class of problems probably more extensive than Np. 
TO illustrate the ideas, let us assume  that Alice  wishes to convince Bob that some integer rn 
(of her choosing) is the  product of exactly k distinct primes. Alice is convinced of the truth  of her 
claim  because she randomly selected k distinct integers pl,pz  , . . . ,pk  that passed some probabilistic 
primality test [R, SS] to her  satisfaction. Although  proofs of primality for  these factors exist  since 
PRIMES E NP  Pr], there  is no known feasible algorithm for Alice to get these proofs1. In other 
words, Alice knows (with an arbitrary  small probability of error) that rn is in the proper form,  she 
knows there exists a short proof of this statement, but she cannot find the proof. Using our protocol, 
she can nonetheless convince Bob without compromising the factorization of m in any way (except 
for the fact that Bob will how the number of factors). 
The above example illustrates the fact that our model does not  assume that Alice has more com- 
puting power than Bob nor access to some oracle. Although she starts with one piece of additional 
knowledge (either a formal proof of some theorem or merely a convincing argument), this may be the 
result of her using trap-door  infonnation. The entire protocol itself can be carried out with polyno- 
 mial time resources. 
The general technique allows Alice to guide Bob  through  the  simulation of an arbitrary Boolean 
 circuit without ever having to disclose its inputs or any intermediary results. At the end of the proto- 
 col, she  can nonetheless convince  Bob of the final outcome of the  circuit. If this turns out to  be 1, 
 Bob will be convinced that the Boolean function computed by the circuit is satisfiable and that Alice 
 holds a satisfying assignment,  but  he  will known nothing else. The bottom line is that, whenever 
 Alice  can convince herself probabilistically of a fact or theorem, perhaps thanks to her knowledge of 
 some trap-door information, she can convince Bob as well without compromising the trapdoor. 

 2. NUMBER THEORETIC BACKGROUND 
 Let n be an integer. 2$ denotes  the set of integers relatively primes to n between 1 and n-1. 
 An integer z E Zz is  a quadratic residue modulo n (z E QRJ if there is an x E Z: such  that 
 z E x2 (mod n). An integer z E 6 is  a quadratic  non-residue modulo n (z E QNRJ if z 4 QR,. 
 If p is a prime  and if z E Z$, it is easy to determine whether z E QRp because this is so if and only 
 if z@-l)'* 1 (mod p). Let n = pq be the product of two distinct odd primes. Given z E Zz , let zp 
 and z, denote (z mod p) and (z mod q), respectively. Given the factorization of n, it is easy to deter- 
 mine whether z € QR, because this is so if and only if zF E QRF and z, E QR,. Given the factoriza- 
 tion of n and given z E Q%, it is also easy (by a Las Vegas  algorithm in general [Pel) to find every 
 x E Zz such that z I x2 (mod n). This is however believed to be hard without the factorization of n. 

  ' Goldwassa and Kilian's new provably correct and probably fast primaliry test [GK]allows  Alice to 
 "efficiently" (the running time is currently a 12th power polynomial) get short proofs for those primes on 
 which the  algorithm turns out to be fast. This might reduce the interest of chk particular example, but not 
 the interest of our general protwol. 
 225 

Given z E Zz, the Jacobi symbol (zh) is defined  as +1 if both zo and zq are quadratic residues 
modulo p and q, respectively, or if both are quadratic non-residues; it is defined as -1 otherwise. It is 
easy to compute (z/n) even if the factorization of n is unknown BSA]. Let Z:[+l] denote the set of 
z  Zz  such  that (z/nj = +1 and define Zz[-1] similarly.  Let Qi\iR,[+l] denote QNR, n ZZ[+ll. 
  E* 
It is clear that Z,[-l] C QNR,; moreover, exactly half the memkrs of Z:[+1] are quadratic resi- 
dues modulo n and the other half are quadratic non-residues. Both C[+l]and  QR, are closed under 
multiplication modulo n, the product modulo n of two  members of QNR,[+I] is  a member of QR,, 
and the product modulo n of  a member of QR, by a member of Qhi,[+l] is a member of 
QNR,[+l]. A*  uniformly  distributed random element of QR, can be obtained by randomly  choosing 
some x E Z, and squaring it modulo n; given any  fixed y E QhX,,[+l], a uniformly* dismbuted ran- 
dom  element  of Qh;R,[+l] can be obtained by randomly  choosing some x E Z, and computing 
x2y mod n. Furthermore, everything we have said so far, except for the definition of the Jacobi sym- 
bol, remains true  if n is of the form piq’, where p and q are distinct odd primes and i and j are posi- 
tive powers of which at least one is odd. 
It is  believed that no efficient  algorithm  can distinguish a quadratic residue from a quadratic 
non-residue, even probabilistically speaking, as long as  the  latter has Jacobi symbol +l and the fac- 
torization of n is  unknown. For a more formal statement of this quadratic  residuosity assumption 
 (QRA) and for more background on number theory, please refer to [GM]. 

 3. THE ENCRYPTIONOF SECRETS 
At  the beginning of our protocols, Alice randomly chooses two distinct large primes p and q, 
 and she discloses their product n = pq to Bob. Following  the QRA, we  assume throughout that Bob 
cannot distinguish a quadratic  residue modulo n from a quadratic  non-residue, as long as the latter 
 belongs to Zz[+1]. Alice  also randomly chooses and  discloses  to  Bob some y E QNR,[+I]. (It is 
 proven  in [GM] that  this cannot help  Bob distinguish  residues  from  non-residues.) Using the  zero- 
 knowledge interactive protocol of  [GHY], Alice convinces Bob that R is of the form p‘qj for distinct 
 odd primes p and 4. and positive integers i and j of which at  least one is odd2. Using the  zero- 
 knowledge protocol of [GMR], Alice  convinces Bob that y E QNR,[+l]. 
At  this point, Bob could*  produce uniformly  distributed  random members of  QR, and QNR,[+11 
 by choosing a random x E Z, and computing either x2 mod n or x2y mod n. The fact that only Alice 
 can distinguish between  these two occurrences was  the basis of Goldwasser and Micali’s original pro- 
 babilistic encryption [GM].  Here,  we use this idea in rhe reverse direcrion: it  will atways be Alice 
 that produces random  members of QR, and QNR,[+l].  By convention, members of  QR, are used  as 
 encryptions  of the bit 0 and members of QNR,[+l] are used  as encryptions of the bit 1. Whenever 
 Alice  shows Bob  the encryption z of some bit b, he has no clue as to  which  bit it encodes (under 
 QRA).  It is *however  possible for Alice to prove  to Bob whether b = 0 or b = 1 by showing him 
 some x E Zn such that z = x2yb mod n. This operation will be refered to as opening the 
 secref z. Notice tha: this  is  a  zero-knowledge proof  even though a square root  of either z or q-’is  
 given to Bob, because x was randomly chosen by Alice.  For this reason,  whenever she  wishes  to 


 ~~  ~~ 
It would be nicer if .-Uice could convince Bob directly that n is of the form pq, but we offer in the 
 sequel the first ZKIP capable of achieving this (and therefore we cannot use  it  yet). This is however of no 
 consequence because .Ucz could only make herself more vulnerahle by choosing n = p’qj without i=j= 1. 
226 

open a secret z, there is no need for Alice to use the ZKlp of [GMR] in order to convince Bob of 
which among v-'or  z belongs  to QNR,[+l]. We give in  the last section of this paper a simplified 
ZKIP for quadratic residuosity when the target is chosen by Bob. 

4. CAN BOB  COMPUTE ON ENCRYPTED BITS? 
   Let bl and bz be two secret bits of Alice, and let z1 and z2 be their encryptions as given to Bob. 
Even  though Bob has no knowledge of b, or b,, he can still compute an encryption of some func- 
tions of bl and b2 . For instance, Bob can compute z1 y mod n, which is an encryption for the nega- 
tion of bl . Similarly, Bob can compute z1z2 mod n, which is  an encryption of the exclusive-or of bl 
and b, because if z1 = $y"l mod n and z2 = x$ybz mod n, then 

   zlz2 mod n = (x1x2)2ybl+b1mod  n = x2yfb1* mod mod n, 

where x = xlx2y(b'ib3 d'v mod n. 
   Could Bob compute an encryption of the and or the or of bl and b, given only z1 and z,? This 
remains an open question. We will show, however, that it is possible for Bob to do so with the 
(zero-knowledge) help of Alice. As a corollary, Bob  can  compute an encryption of  arbitrary Boolean 
functions of bits for which he only has encryptions. After this computation,  Alice can open the result 
for Bob without ever having  had  to open the input Boolean  variables or any intermediary informa- 
tion. This idea leads to a simple ZKIP for SAT in  Section 6. 

5. HOW ALICE CAN HELP BOB COMPUTE ON ENCRYPTED  BITS 
Let u = blb2 . . . bk be a  k-bit string of Alice. For each i, 1 I i I k, let zi and ii be two encryp- 
tions  of bi randomly chosen by  Alice. It is easy for Alice  to convince  Bob that the k-bit striiigs 
encrypted by z1z2 . . zk and f1f2 . . . fk are identical  without  providing Bob with any  additional 
information. 

String equality protocol: For each i, 1 Ii  Ik,  Alice  gives  Bob some xi E ZE such that 
ziii = $ (mod n). Once again, this is a ZKP because the encryptions were randomly 
chosen by Alice and not influenced by Bob. 0 
As above, let u = b,bz. . . bk and  let zi encrypt bi for each i, 1 i i I k. Now, let 
1- 
ri = blbz ' . 6, be some k-bit string different from u and let fi be an encryption of & for each i, 
1 Ii  Ik.  It is no longer so obvious  that Alice can convince Bob that the strings encrypted by 
z1z2 . . . zk and fl& . . . fk are different without yielding  some  additional information (such as a 
specific i for which bi # 6'). The fact that this is possible, and the technique  that achieves  this proto- 
col, illushate the core of our main result. 
String inequality protocol: For each i, 1 5 i Ik,  let vi = ziii mod n. The problem reduces 
to convincing Bob  (by a ZKIP) that  the string  encrypted by vIvz . . vk is not identically 
zero. For this, Alice  randomly  chooses some permutation (T of { 1,2, . . ,k}  and xi E 
for 1 5 i I k. She then computes and discloses to Bob wi = 2 v~(,~mod n for each 1 1. i 1.k. 
At this point, Bob sends either challenge A or challenge B to Alice. 
 If Bob  sent challenge A, Alice must disclose  some i such  that wiencrypts  a 1, and 
 open this wifor  Bob by giving him a square root of wiy-' modulo n. 
 227 

 If Bob sent  challenge B,  Alice must disclose the  permutation D and use the string 
 equality  protocol  to  convince Bob  that wlw2 . . . wk encrypts  the same  string as 

 va(l)vo(2). ' . 'o(k). 
This  process is repeated s times, for some safety parameter s agreed upon between Alice 
and Bob. In order to convince Bob, Alice must meet ever)' single challenge. 0 
Theorem 
 (i)  The only  knowledge  obtainable by Bob from this protocol is  that zlz2 . . . zk and 
 2122 . . . 2k encrypt distinct bit strings, and 
 (ii)  Alice  only has  a  probability 2-' of convincing Bob of this when in fact the strings 
 are identical. 
Proof (sketch). 
 (i) Observe that whenever  Bob chooses  challenge A, he learns that the original  bit 
 strings are distinct in at least one place (if Alice was honest), but this gives him no 
 clue as to any single i such that bi # Li because the permutation (T is then kept secret. 
 On the other hand,  whenever Bob chooses challenge B, he gains no information 
 whatsoever on the original strings. 
 (ii) Tf in fact v1v2 . . . vk encrypts the identically zero string,  the only thing Alice can do 
 to hope convincing  Bob of the contrary is to guess exactly which challenge Bob will 

 choose for each round and to encrypt non-identically zero strings with w1w2 . . ' Wk 
 whenever she expects  Bob  to use challenge A and identically zero strings otherwise. 
 The results follows from the fact that there  are 25 equally likely sequences of choices 
 for Bob.   0 
We  are  now ready for the main tool used in this paper. Consider any Boolean  function 
B :{O,  1)' -+ {0,1} agreed upon between Alice and Bob, and any bits b, , bz, . . . , b, known to Alice 
only. For 1 Ii  S t, let zi be  an encryption of b, known to Bob. Let b = B(b1, b,, * . * ,b,).  
 Alice  can produce an encryption z for b and convince Bob tha z encrypts the correct bit without giv- 
 ing him any information on the input bits b, , b2, . . . , b, nor on the result b. 
Definition. A pennured truth table for the  Boolean  function B is a binary string of length 
 (r+1)2' formed of 2' blocks of t+l bits. The last bit of  each block is the value of B on the 
other r bits of the  block,  and  each assignment of truth values occurs exactly once in the 
 first t bits of some block. For example, here is a permuted truth table for  the binary Or: 
 011000111101, which should be read as 0 or 1 = 1,0 or 0 = 0, 1 or 1 = 1 and 1 or o= 1. 
 Boolean computation protocol: Let the situation be as in the paragraph just before the 
 above definition.  Alice  randomly chooses a permuted truth table for B and she  &closes 
 encryptions for each of its bits. At  this point, Bob sends either challenge A or challenge B 
 to Alice. 
  If Bob sent challenge A, Alice must open the entire encryption of the vmutd truth 
  table, so that Bob can check that it is a valid truth table for B. 
  If Bob sent challenge B, Alice must point out to the appropriate block in the enWP 
  tion of the permuted  truth table and use the string equality protocol to convince Bob 
  that 2122 . . . ztz encrypts the same bit string as this block. 
 228 

This process is repeated s times,  for  some safety parameter s agreed upon between Alice 
and Bob. In order to convince Bob  that z is an encryption for B(b, , b,, . . . , bt), Alice 
must succeed in meeting every single challenge.   CI 
A theorem very similar to the  one for the string inequality protocol can be stated and the proof 
is essentially identical. Notice  that  this protocol is interesting only for small t because it is exponen- 
 tial in t. In the sequel, we will use it exclusively with r S 2. A very similar Boolean computation pro- 
tocol was discovered independently by Josh Benaloh [Be] as an application of the general tool of 
 “cryptographic capsules” [CFJ 

 6. ZKIPFORSAT 
The  zero-knowledge  interactive  proof for  satisfiability should  now be obvious.  Let 
f:(0,  l}k + (0,l) be the  function  computed by some satisfiable Boolean formula  for which Alice 
 knows an assignment b, , b,, . . . , bk E {0,1} such that f(bl, 9,. .  . , bJ = 1. Assume  the 
 Boolean formula is given  using  arbitrary unary and binary  Boolean operators. In order to  convince 
 Bob that the formula is satisfiable,  Alice produces encryptions zl, z2, . . . , zk of b, , b2, * - . , bk, 
 respectively. She  then  guides Bob through the  encrypted evaluation of the  formula, one  Boolean 
 operator  at a time3,  using  the BOOIW computation protocol  (with t I2).  This results is an encryp- 
 tion z for the value of f(b,, b,, . . . , bJ. It  then only remains for Alice to open z and show Bob 
 that it encrypts a 1. 

 7. ZKIP FOR THE NUMBER OF PRIME FACTORS 
Let us now come  back to the problem mentioned  in the introduction. Alice has selected k dis- 
 tinct primes PI,p2,  . . . ,pt  and she has formed their product m = pp, . . . pk. She wishes to con- 
 vince Bob that m is indeed the product of  exactly k distinct primes. Let 1 be the number of  bits in m. 
 Each factor will be considered  as  a  length 1 binary string, with leading zeroes if needed. As a  first 
 step, Alice encrypts each of the factors  and  she discloses these encryptions to Bob. The string ine- 
 quality protocol is used to convince Bob that the factors are all distinct and that  none of them is 
 equal to 1. She then guides  Bob  through  the simulation of a Boolean circuit for iterated multiplica- 
 tion. This produces  the  encryption of a  length kl bit string, which Alice opens to show  that it 
 encrypts (k-l)f zeroes followed by the binary representation of m. 
 At this point, Alice still has to convince Bob that  each of these factors is a prime. If she had a 
 proof of this, she could encode it  as  the input to a proof  verification  Boolean circuit and guide Bob 
 through its evaluation. Recall, however,  that her conviction that each of the pi is prime comes  from 
 her own running of a probabilistic primality test. None of these runs can  be considered as convincing 
 by Bob because he cannot trust that Alice was honest in her coin  tosses. 
 This is where. our technique  is most powerful. Consider a Boolean circuit with two I-bit inputs p 
 and c that outputs 1 if and only if c is a certificate that p is composite (where primes  have  no 
 certificates and composites  have  lots m,SS]).  Recall that Bob was given by Alice an encryption of 
 each bit of each pi. With the help of  Alice, he can run as many randomly chosen c’s as he wishes 
 into the circuit for each pi and ask her to open the circuit outcomes. If he ever gets a 1, he will know 
 for sure that  the corresponding pi is composite and that Alice had been cheating (or perhaps that 

To save on the number of communications rounds, the various operators can be processed in pdlel. 
 229 

 Alice  was honest after all, and that she just discovered with him that this pi is composite!). Other- 
 wise, since he has complete control  over the c’s, he  can convince  himself, with any  level of 
 confidence, that m is the product  of exactly k distinct primes. This protocol can be adapted if Alice 
 wished instead to convince  Bob that there are exactly k distinct primes  in the factorization of m, 
 regardless  of  their multiplicities. A more  practical variation  allows Alice to  convince  Bob that 
 the prime  factors of n have  interesting properties, such as being of the  form 2~1,where  4 is 
 also a prime. 

 8.  THE GENERAL PROTOCOL 
Recall that BPP stands for the  class of decision problems  that  can be solved in probabilistic 
polynomial time with  bounded  error probability [GI. It is reasonable to  consider BPP as the real 
 class  of  tractable problems  (rather than P) because the  error  probability can  always be decreased 
 below  any E > 0 by repeating the  algorithm clog&-’ times  and  taking the majority answer, where c 
 depends only on the original error probability. It is generally  believed that there is no inclusion rela- 
 tion either way between NP and BPP: non-determinism and  randomness seem to be incomparable 
 powers. These powers  can be combined in several ways. We  believe  the most natural to be Babai’s 
 class MA [Ba], which we would  rather  call RNP as random NP. This class is such that 
 NP u BPP C RNP, hence NP is almost certainly a strict subset of RNP. For a discussion as to why 
 we favour MA over the seemingly more powerful AM or  interactive proof systems [GMR],please  
 consult [BC]. 

 Definition. Let Z stand  for (0,l). **A decision   problem X Z* belongs to RNP if and 
 only if there exists a predicate A I: x Z and a polynomial p(n) such that 
  (i) A E BPP, and 
 (ii) (Vx E Z*)[x E X e (3a E Z*)[jal = p(bl) and or,& E A]] 
  (such an a is refered to as an argument for x).  0 

 Notice that this would correspond to the polynomial hierarchy characterization of NP had we insisted 
 that A E P. The restriction [a1 = p(bl) instead of the  usual la1 2 p(b\) is there for a technical reason. 
 Notice also that X E NP whenever A E NP. 
 Intuitively, X E RNP means  that whenever x E X, there is a (possibly hard  to find) short ugu- 
 ment for this,  and that  the validity of this argument can be checked probabilistically in polynomial 
 time. We are about to prove that ifX E RNP, if the  proof that X E RNP is in the public domain, and 
 if  Alice knows  an argument a for some x E X, she can convince Bob with a ZKIP that x E X. As a 
 warm UP,let  us first restrict ourselves to one-sided probabilistic algorithms. 
 Recall that RP (sometimes  refered to as R) is  the class of decision problems that can be solved 
 in polynomial  time by a one-sided bounded error probabilistic algorithm [A]. Here, each time  the 
 probabilistic algorithm is run on a yes-instance, it accepts with probability at  least %, whereas it 
 always rejects neinstances. It is  well known that RP E NP n BPP and  that co-RP C BPP, but 
 co-RP and NP are probably  incomparable. Whenever x is  a yes-instance of a co-RP problem,  one 
 can convince himherself that  this is SO (by repeating the algorithm), but there does not have to exist 
 a succinct proof of this. 
230 

   Theorem  (under QRA). Consider a problem X E RhT such that  the corresponding A 
   (refer to the definition of RNP) belongs to co-RP. Assume that the characterization A for X 
   and a co-RP algorithm for A are in  the public  domain. Let Alice  have an  argument a for 
   some x E X. Although she may not have a  definite proof  that x E X, she convinced herself 
   probabilistically that cxe E A, hence x E X. It is then possible for Alice to convince Bob 
   in polynomial time that x E X without  disclosing any additional information. 
   Proof (sketch). Alice and Bob agree on a  probabilistic  one-sided  Boolean circuit for the 
   complement of A. (That is : on any yes-instance of A, using any random choices, the circuit 
   outputs a 0 ; on any neinstance of A, the circuit outputs a 1 for at least 50% of  the random 
   choices.) Alice gives Bob an encryption for each  bit of x, and she opens  them to show that 
   they encrypt x. Alice also gives Bob an encryption for each  bit of a, but she keeps a itself 
   secret. She then guides Bob through the  evaluation of the  Boolean circuit on input -,a>, 
   using Bob's coin tosses,  until  the  encrypted outcome  is  obtained. She then opens this out- 
   come to Bob, who can ascertain that it is indeed  a 0. This process is repeated  until Bob is 
   convinced that e,a>E  A, hence  that x E X. Clearly,  this  gives Bob no information on a 
(except for its mere existence  and Alice's knowledge  of  it)  because  the only possible  out- 
   come for  the Boolean circuit is 0, provided Alice  was not trying  to  cheat.  Bob does not 
   even leam the length of a because it had to be exactly p(bl)by  definition of RNP. 

   The above  protocol does not work directly  for X E RNP in  general,  because it  would not 
be zero-knowledge. Indeed, Bob would gain information on Alice's argument a from know- 
ledge of which random  choices  made the  circuit accept ocp and which made it reject, or even 
merely from  knowledge of  the  number of each of these occurrences. (Recall  that  if A E BPP 

but A @ RP LJ co-RP, the  probabilistic Boolean test circuit  for A is  expected to output sometimes 0 
and sometimes 1 on the same input; the most frequent  answer  being  correct  with  high probability.) 
Two ideas are needed to solve this difficulty: 
Alice and Bob agree in advance on the number of runs they wish  to carry through the  test 
circuit (depending on the error probability they are willing to tolerate).  At the end of each 
run, Alice no longer opens the  outcome. After  all  the  runs are completed, Alice guides 
Bob through the  evaluation of a majority Boolean  circuit,  using  the  previously obtained 
encrypted outcomes as input. It is  only  the resulting majority bit  that Alice finally opens 
for Bob. 
Even if Alice is honest, the above idea leaves  the door open for Bob  to cheat: it could be 
that the circuit outcome is not what she expected  because Bob had deliberately chosen  the 
"random"  coin tosses  to  make this  occurrence 50% likely.  Assuming Alice's good faith, 
this could yield up to one bit of  information to Bob about  the argument a, which  is 
intolerable.  Alice would be almost certain that Bob cheated,  but it would  be too late by 
then.  In order to prevent this possibility,  it is essential  that all coins be  tossed SO that nei- 
ther Alice nor Bob can influence  the outcome,  and  such that Bob does not get to see the 
outcome  (i.e. : coin tossing in a well).  Fortunately, such a protocol is very simple : to toss 
a coin,  Alice gives Bob a randomly chosen  element of 2:[+1] and Bob tells her whether 
to multiply it or not by the standard y E QNR,[+I]. 
231 

Main Theorem (under QRA). Consider any X E RNP and  some x E X for  which  Alice 
knows  an argument u. Assume the proof  that X E RNP is in the public domain4. Even 
though  Alice may not have a  definite proof  that x E X, she convinced herself probabilisti- 
cally  that -,a> E A, hence x E X. It is possible for Alice to convince Bob in polynomial 
time  that x EX and that  she knows some argument for this without  disclosing any addi- 
tional information. 
Proof (sketch). By the above discussion. 

Let us  stress again  that this protocol is interesting even  when A E NP, hence X E NT (as in sec- 
tion 7 because PRIMES E NP), despite the reduction to SAT in these cases. This is so because Alice 
could know  the argument a for x as a result of her choosing u in the first place (as trap-door  informa- 
tion) and producing x from it. She might not, however,  have an accepting  computation for -,a>, 
even though A E NP. She can  nonetheless make use of OUI protocol. In other  words, it does not 
require Alice to have more computing power than Bob  or to have access to some NP-complete om- 
cle. As long as she can convince herself with the help of her own trap-door, she can convince 
Bob as well without compromising the trapdoor. 

9. OTHER  EXAMPLESOF ZKTp’s 
Our basic technique can be used in various  situations. Let us briefly  mention a few of them. 
It allows Alice to convince Bob of the  quadratic residuosity of a member of e[+l]chosen  by Bob 
without yielding additional information, in  a way much  simpler than those of [GMR, GHY]. It also 
allows  Alice to  convince Bob that an encrypted function  is a permutation (see below). More gen- 
erally, all  these building blocks can be used directly to obtain ej‘icient ZKIP’s for a variety of 
NP-complete problems such as Hamiltonian circuit, clique, knapsack, graph 3-colouing, etc. 
Quadratic Residumity  Protocol: Bob shows some z E e[+l]to  Alice  and she is willing 
to convince him of whether it is  a quadratic residue or not Assume initially that z E QR,. 
Alice  uses her  knowledge  of  the factors  of n to compute some x E such  that 
z = x2 mod n. Because z was chosen by Bob, it would be far  from a ZKIP if Alice 
revealed x to Bob as proof (it could give Bob a 50% chance  of factoring  Alice’s master 
secret n). Instead,  Alice  randomly generates some u E Zz. She then computes and dis- 
closes w = uz mod n. At this point, Bob sends either challenge A or challenge B to Alice. 
 If Bob sent challenge A, Alice must disclose u so that Bob can check that 
 w = u2 mod n, hence that w is a quadratic residue. 
 If Bob  sent challenge B, Alice must  disclose ux mod n so that Bob can check that 
 (m)’ a wz (mod n), hence that w has the same quadratic character as z. 
This process is repeated s times for some  safety parmeter s agreed upon between  Alice 
and Bob. The protocol is very similar if z 4 QR, but it requires  that some  standard 
y E QNR,[+l] has already been  proven once and for all. Thus, the protocol  of [GMR] must 
be used the very frrsr time in order to make ours effective.0 


   i.e. : the predicate A and the BPP algorithm for A are already known to Bob 
 232 

A similar protocol is independently given in [Be] ; its essence was already in [CF]. Notice also 
 that our protocol  would not work for quadratic non-residuosity if n had more than two distinct prime 
 factors, whereas the protocol of [GMR] could still be used. 
Finally, here is the permutation problem. Let m be some integer agreed upon between Alice and 
 Bob. Let G be a permutation of {1,2, . . . , m} randomly  and secfftly chosen by Alice. This pennu- 
 tation can be naturally represented by a table of mk bits,  where k = pogzml. Alice discloses to Bob 
 an encryption for each of these  bits, so that it will  not be possible for her to change her originally 
 chosen permutation. At this point, Bob would like to be convinced  that he was given the encryption 
of a  permutation, not just of any  function from (1, 2, . . . , m} to {I,& - . . ,2’L). No doubt the 
reader  has seen our technique  used enough times by now to design hisker own ZKIP. This problem 
has applications if one wishes to keep an electronic poker face [Cr], and its solution is central to the 
above mentioned efficient ZKIP’s for Hamiltonian circuit, clique, knapsack, etc. 

 10. OPEN PROBLEM 
Can  Bob compute  encryptions of arbitrary Boolean  functions of encrypted Boolean inputs 
 withour the help of Alice ? For instance, given encryptions for the bits b, and b2, can he compute an 
 encryption for (b, and b2)?  If so, this might allow a dramatic  improvement  in our protocols, includ- 
 ing the possibility of publishing ZKIP’s (an idea originally investigated by Manuel Blum). 

 ACKNOWLEDGEMENT 
We  wish  to thank Josh  Benaloh, Joan Feigenbaurn, Oded Goldreich, Shafi Goldwasser, Silvio 
 Micali, Jean-Marc Robert, Steven Rudich and Moti Yung for fruitful discussions. 

 REFERENCES 

 Adleman, L., “Reducibility, randomness and intractability”, Proceedings of the 9th Annual 
 ACM  Symposium on the Theory of Computing, 1977, pp. 151-163. 
 Babai, L., “Trading group theory for randomness”, Proceedings of the 17th Annual ACM 
 Symposium on the Theory of Computing, 1985, pp.421-429. 
 Benaloh (Cohen), J. D., “Cryptographic capsules : a disjunctive  primitive for interactive 
 protocols”, these CRYPTO 86 Proceedings, Springer-Verlag, 1987. 
 Brassard, G. and C. Crkpeau, “Non-transitive transfert of confidence:  a perfect zero- 
 knowledge interactive  protocol  for SAT and beyond”, Proceedings of the 27rh Annual 
 IEEE Symposium on the Foundations of Computer Science, 1986, pp. 188-195. 
 Chaum, D., “Demonstrating that a public  predicate can be satisfied without revealing my 
 information about how”, these CRYPTO 86 Proceedings, Springer-Verlag, 1987. 
 Cohen (Benaloh), J. D. and M. J. Fisher, “A robust  and  verifiable cryptographically secure 
 election scheme“, Proceedings of the 26th Annual IEEE Symposium on the Foundations of 
 Computer Science, 1985, pp. 372-382. 
 Crkpeau, C., “A zero-knowledge Poker protocol that achieves confidentiality of the players’ 
 strategy, or How  to  achieve an electronic Poker face”, rhese CRYPTO 86 Proceedings, 
 Springer-Verlag, 1987. 
 Galil, Z., S. Haber and M. Yung, “A private  interactive  test of a Boolean predicate and 
 minimum-knowledge  public-key  cryptosystems”, Proceedings of the 26th Annual IEEE 
 Symposium on the Foundations of Computer Science, 1985, pp. 360-371. 
233 

[GI Gill, J. “Computational complexity of probabilistic Turing machines”, SIAM Journaf on 
 Computing, vol. 6, no.4, 1977, pp.675-695. 
[GMW]   Goldreich, O., S. MiCali and A. Wigderson, “Proofs that yield  nothing but their validity 
 and a  methodology of cryptographic protocol design”, Proceedings of the 27th Annual 
 IEEE Symposium on the Foundations of Computer Science, 1986, pp. 174-187. 
[GK]Goldwasser, S. and J. Kilian, “Almost all primes can be quickly certified”, Proceedings of 
 the 18th Annual ACM Symposium on the Theory of Computing, 1986, pp.316-329. 
[GM]Goldwasser, S. and S. Micali, “Probabilistic encryption”, Journal of Computer and System 
 Sciences, vol. 28, no. 2, 1984, pp. 270-299. 
[GMR]   Goldwasser, S., S. MiCali and C. Rackoff, “The knowledge  complexity of interactive 
 proof-systems”, Proceedings of the 17th Annual ACM Symposium on the Theory of Com- 
 puting, 1985, pp. 291-304. 
[PelPeralta, R., “A simple and fast probabilistic  algorithm for computing  square roots modulo a 
 prime number”, IEEE Transactions on Informution Theory, to appear. 
[Pr]Pratt, V., “Every  prime  has  a succinct certificate”, SIM Journal on Computing, ~01.4, 
 1975, pp. 214-220. 
[R] Rabin, M. O., “Probabilistic  algorithms”, in Algorirh and Their Complexity: Recent 
 Results  and New Directions, J.F. Traub (editor), Academic Press, New York, New York, 
 1976, pp. 21-39. 
[RSA]   Rivest, R.L., A. Shamir and L. Adleman, “A method for obtaining digital signatures and 
 public-key cryptosystems”, Communications ofthe ACM, vol. 21, no. 2,  1978, pp. 120-126. 
[SS]Solovay, R. and V. Shassen, “A  fast Monte Carlo test for primality”, SIAM Jourmf on 
 Computing, vol. 6, 1977, pp. 84-85. 
