 Demonstrating that a Public Predicate can be Satisfied Without Revealing Any Information About How 

    David Chaum 

 Centre for Mathematics and Computer Science 
    Kruislaan 413 1098 SJ Amsterdam  the Netherlands 

It’s not unlike a technique of probabilistic mathematical proof 
in which you allow a receiver to select one of two cuses. 
-Norman  Shapiro 
[responding] Yes, you’re right .... 
but the residue of doubt is provubb, negligibb small. 
-Michael Rabin 1977 


Introduction 

The problem solved here may be defined in the following way: Both parties y and z agree 
on a Boolean expression called a predicate; y claims to know a secret value satisfymg the predi- 
cate; z wants very high certainty that y does have such a value; while y is willing to demonstrate 
possession of the secret satisfying value,y is unwilling to reveal the secret value to z. The solu- 
tion requires z to assume that y cannot quickly solve certain problem instances provided by I. 
But y is sure not to reveal anything about the secret, even if z has unlimited computing power. 


Relation to Other Work 

The result presented is a dual of those by [Goldreich, et al 861 and [Brassard & Crepeau 
861: their model is an x with infinite computational  ability and a z with limited ability; here z 
may have infinite computational ability and y has only limited ability. Besides being of theoreti- 
cal interest for this reason, the approach presented here offers several advantages: 

The Only possibility for cheating is to solve specific instances of the hard problem (factoring 
in the example construction) within  the time allotted to compute legal responses. 

A variation is Secure even if some known fraction of instances of the assumed hard problem 

 A.M. Odlyzko (Ed.): Advances in Cryptology - CRYPT0 ’86, LNCS 263, pp. 195-199, 1987. 
 0 Springer-Verlag Berlin Heidelberg  1987 
    196 

    can be solved within the allotted time. 

    If there are multiple solutions, no information about which one(s) the prover knows is 
    released by the protocol, even to someone who actually has infinite computing power. 

    The model is consistent with previous proposals of the author [Chaum 85b], where an in&- 
    vidual may have to demonstrate something to an organization that has potentially Unkno~n 
    resources or abilities. In fact, the result is a special case of a protocol previously presented 
    by the author [Chaum SSa], whose properties are described in [Chaum 85b page 10391. But 
    the underlying problem assumed hard in that work differs from those relied on here. 
    Giving the verifier a chance to cheat of less than zs requires only an amount of computa- 
    tion linear in s and the number of gates needed to represent the predicate. For s = 100 and 
    say 200 digit composites, this requires for each gate only about as much computation as a 
    single RSA decryption. 

    The protocol is easily adapted to the dual model. 


1. PROTOCOL 

    In overview, the protocol presented  involvesy making known to z transformed and 
encrypted copies of a truth table for each gate of a circuit representation of the predicate, after 
which z is allowed to “select one of two cases”. The basic idea of getting exponential security by 
one party first committing by revealing encrypted forms and then allowing the other party to 
choose between several cases, which is relied on here, was first proposed in the context of crypto- 
graphic protocols by Rabin in [77] (which is the subject of the discussion quoted at the beginning 
of this article). 


1.1 Protocol Set-Up 

Initiallyy and z agree on a predicate and its realization by a circuit comprising rn gates 
gl, . . . , g,, defined by their respective truth tables TI,.  . . , T,,,. The gates are interconnected 
by n wires w 1, . . . , w,, with each column of every truth table corresponding to a wire. Thus the 
predicate may be thought of as a Boolean function on say r secret input bits involving m elemen- 
tary Boolean operations each (except one) of whose output bits becomes an input for one or 
more other elementary operations without feedback. This means that the memoryless circuit has 
r input wires, each of which is an input to one or more gates (elementary operations defined by a 
corresponding truth table); n -r - 1 internal wires, each serving as the output of a single gate 
and “fanning-out’’ to serve as input to one or more other gates; and a single output wire of a sin- 
gle gate, which is the output of the whole circuit. 

Consider a gate gk with I inputs and an output defined by a truth table Tk (subsequently 
denoted without subscript) represented  in matrix form as T=(t,,,), with i E{1,  ..., 2‘) and j E Wk, 
    197 

where wk is the set of wires corresponding to the inputs and outputs of gate gk and (the cardi- 
nality) # wk =I + 1, which is the total number of inputs and outputs of gate gk, and 
Wk C(w1, * . . , w,,}. The entries of Tare 0’s and I’s, i.e. ti,, E{O, I), in the usual way: the rows 
(apart from the last column) contain all defined input coniigurations, and the last entry in each 
row is the corresponding output. 

It is sufficient to consider all the wires as having secret values, except the single output wire 
for the whole predicate. Since the value of this wire should be 1, the truth table of its gate is 
modified as follows: all rows with 0 in the output column are removed, and then the output 
column itself is removed. 

First, y choses an inversion Z, at random for each wire w,, i.e. ZJ E (0, 1 } for 
j E {wl,.  . . , w,,},where  random choices (as used throughout) are uniform choices that are sta- 
tistically independent of everythmg else. 

Next, y successively transforms  each T, first to a permuted  form T ’, second to an obscured 
form T ”, and third to an encrypted form E as follows: (a) Each T is transformed into a matrix 
T’=(t‘i,,), by a random row permutation. (b) Each T’ is transformed into a table T”=(f”i,,) for 
which all entries in all columns corresponding to inverted wires are inverted: t”j,j=t  ‘;,,@I,. (c) 
Each entry of the obscured form T” is encrypted in a special way to yield E =(ei,,): for each 
entry in T“ a random residue  modulo N that is coprime with N, shown as rI,,, is chosen with 
Jacobi symbol (ri,,/  N) equal 1 when t f’i,j = 1 and equal - 1 otherwise, and e;,,&, (mod N), 
where N is supplied toy by z. 

Theny displays all the matrices E to z and allows z to choose between two cases: 

(1)  Display byy of I, and, for each gate, all the rI,,’s used in forming the corresponding 6s. 
This allows z to recover every T” from the Jacobi symbols of the ri,,’s, to check that the 
entries of each E are the squares of the corresponding r,,j, and to venfy that each T” 

satisfies t I>,; =t ‘,,J@IJ, for  some row permutation Tf of T. 

(2)  Display byy of one row of rl,j’sfor  each E, which should correspond to the actual row of 
the truth table that is satisfied by the secret wire values. This allows z to check that the 
entries of a row of each E are the squares of the corresponding r‘s, to recover the 
corresponding rows of the T”’s from the Jacobi symbols of the rl,,’s, and to venfy that all 
entries t ”i,, of the displayed rows with the same j are equal. 

2. SECURITY 

Theorem: No Shannon-information about the secret wire values is revealed by y following theproto- 
col, assuming N has on& two oddptime factors and they are each congruent to 3 modulo 4. 

Prooj First note that no information in the Shannon sense is revealed before z chooses a we, 
since each quadratic residue displayed has exactly the same probability of corresponding to a 1 as 
to a 0, because it has exactly two distinct roots with each Jacobi symbol. The secret wire values 
have no influence on what is revealed in case 1. In case 2, the indices of the displayed rows 
reveal nothing since the permutation of rows is chosen at random; a bit with indexj in a 
revealed row corresponds with the jth wire, is equal to all other such bits with index j, and is just 
the exclusive-or of the Secret wire value with Ij, which is just the encryption of the secret value 
under a true one-time pad.O 

Theorem Theprobability that y satisfies z’s verification cannot exceed M when y is unable to learn 
secret wire values satishing the circuit, assuming y cannot find two square roots of the same residue 
modulo N that  have distinct Jacobi symbols. 

Prooj It is sufficient to show that if y can satisfy z in both cases, then y can learn wire values 

satisfying the circuit. All T ” are uniquely determined (from the assumption), are known toy, 
and contain only valid truth table rows when exclusive-ored with the corresponding bits of the 
I,’s known toy, as a consequence of y being able to satisfy case 1. From case 2, y knows a way 
to choose one row from each table T“ such that each wire is assigned the same value in all the 
chosen rows. Thus, y can form the exclusive-or of the Ij’s known from case 1 with the rows 
known from case 2, which yields a valid row for each gate (from case 1) with an assignment of 
bits to wires that  satisfies each such row (from case 2).0 

Lemma: If the above protocol is successfulb repeated s times, using moduli each of which can be fac- 
tored in the allotted time with independent probabiliy p, then the probabilig of one-half in the previ- 
ous theorem may be replaced by (y2 + p / 2y. 

Pro,) Follows immediately from elementary probability  theory. 

3. DISCUSSION 

The protocol description used certain well known number theoretic functions (first intro- 
duced by Blum [82]) for clarity and concreteness, but the present results should not be inter- 
preted as limited to these specific functions. A natural generalization is to any pair of so called 
“claw free” (as defined in [Goldwasser et al 851) one-way bijections with the same image. Other 
choices of encryption functions switch the protocol to the dual model mentioned in the introduc- 
tion: any suitable encryption of a single bit (or actually row of bits) with a unique inverse mes- 
sage could be used to encrypt a T” to form an E. 

In the protocol presented above, y must be convinced that N is a “Blum integer,” or better, 
that it is of the form used in [Goldwasser et al 851. There are at least two ways to address such a 
requirement.  One is just to complete the protocol and then lety reveal the factorization of N to 
convince z that no cheating has occurred. When such an after-the-fact check is not acceptable, 
and where the particular encryption functions used require some such checking based on trap- 
door information, z could use a protocol of the dual type to convincey that a predicate indicat- 
ing suitability of the functions is satisfied. 

Other claw free functions based on the dscrete log problem do not require such checking 
 [Damggrd 861. 
199 

A cknowledgements 

I am pleased to thank Oded Goldreich and Silvio Micali for their excitement about the 
difference between this work and their own and for encouraging me to publish it. Leoned Levin 
also expressed enthusiasm and reminded me about the claw free property.  Additionally, thanks 
to A& Shamir and Johan Hastad for listening to various versions of the protocol and inspiring its 
simplification; to Jeroen van de Graaf for several discussions; and to Jan-Hendrik Evertse for his 
comments. 

References 

Blm, M., “Coin flipping by telephone,” Proceedings of IEEE Compcon, 1982, pp. 133-137. 

Brassard, G. and Crepeau, C., “Zero-knowledge simulation of boolean circuits,” preprint of 
extended abstract, April 1986. 

Chaum, D., “Showing credentials without identifcation: signatures transferred between 
unconditionally unlinkable pseudonyms,” Presented at Eurocrypt’85, Linz Austria, Apd 
1985a. 

Chaum, D., “Security without identification: transaction systems to make big brother 
obsolete,” Comm ACM 28, 10 (October 1985b), pp. 1030-1044. 

Damghrd, I., private communication 1986. 

Goldreich, O., Midi, S., and Wigderson, A., “Proofs that yield nothing but the validity of 
the assertion and the methodology of cryptographic protocol design,” preprint, April 1986. 

Goldwasser, S., Mid, S. and Rivest, R.L., “A ‘paradoxical‘ solution to the signature prob- 
lem,” FOCS 84. 

Rabin, M.O., “DigitaLized signatures,” in Foundations of Secure Computation, Academic 
Press, NY,1978.  
