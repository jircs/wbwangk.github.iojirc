Universally Composable Commitments
  (Extended Abstract)

   Ran Canetti1   and Marc Fischlin2?

   1 IBM  T.J. Watson Research Center
 canetti@watson.ibm.com
 2 Goethe-University of Frankfurt
 marc@mi.informatik.uni-frankfurt.de


Abstract.   We propose a new   security measure for commitment pro-
tocols, called Universally Composable (UC) Commitment.   The measure
guarantees that commitment protocols behave like an “ideal commitment
service,” even when concurrently composed with an arbitrary set of pro-
tocols. This is a strong guarantee: it implies that security is maintained
even when an unbounded number of copies of the scheme are running
concurrently, it implies non-malleability (not only with respect to other
copies of the same protocol but even with respect to other protocols), it
provides resilience to selective decommitment, and more.
Unfortunately, two-party  uc  commitment protocols do not exist in
the plain model. However, we construct two-party uc  commitment
protocols, based on general complexity assumptions, in the   common
reference string model  where all parties have access to a common
string taken  from  a predetermined   distribution. The protocols are
non-interactive, in the sense that both the commitment and the open-
ing phases consist of a single message from the committer to the receiver.

Keywords: Commitmentschemes, concurrentcomposition, non-
malleability, security analysis of protocols.

 1   Introduction

 Commitmentis one of the most basic and useful cryptographic primitives. It is
 an essential building block in Zero-Knowledge protocols (e.g., [gmw91,bcc88,
 d89]), in general function evaluation protocols (e.g., [gmw87,ghy88,g98]), in
 contract-signing and electronic commerce, and more. Indeed, commitment pro-
 tocols have been studied extensively in the past two decades (e.g., [n91,ddn00,
 novy92,b99,dio98,ff00,dkos01]).

The basic idea behind the notion of commitment is attractively simple: A
 committer  provides a receiver with the digital equivalent of a “sealed envelope”
 containing a value x. From this point on, the committer cannot change the value
 inside the envelope, and, as long as the committer does not assist the receiver
 ? part of this work done while visiting IBM T.J. Watson Research Center.

J. Kilian (Ed.): CRYPTO 2001, LNCS 2139, pp. 19−40, 2001.
  Springer-Verlag Berlin Heidelberg 2001
20   R. Canetti and M. Fischlin

 in opening the envelope, the receiver learns nothing about x. When both parties
 cooperate, the value x is retrieved in full.

Formalizing this intuitive idea is, however, non-trivial. Traditionally, two
 quite distinct basic ﬂavors of commitment are formalized: unconditionally bind-
 ing and unconditionally secret commitment protocols (see, e.g., [g95]). These
 basic deﬁnitions are indeed suﬃcient for some applications (see there). But they
 treat commitment as a “stand alone” task and do not in general guarantee se-
 curity when a commitment protocol is used as a building-block within other
 protocols, or when multiple copies of a commitment protocol are carried out
 together. A good ﬁrst example for the limitations of the basic deﬁnitions is the
 selective decommitment problem [dnrs99], that demonstrates our inability to
 prove some very minimal composition properties of the basic deﬁnitions.

Indeed, the basic deﬁnitions turned out to be inadequate in some scenarios,
 and stronger variants that allow to securely “compose” commitment protocols
 —both with the calling protocol and with other invocations of the commitment
 protocol— were proposed and successfully used in some speciﬁc contexts. One
 such family of variants make sure that knowledge of certain trapdoor informa-
 tion allows “opening” commitments in more than a single way. These include
 chameleon commitments  [bcc88], trapdoor commitments [fs90] and equivoca-
 ble commitments [b99]. Another strong variant is non-malleable commitments
 [ddn00], where it is guaranteed that a dishonest party that receives an unopened
 commitment to some value x will be unable to commit to a value that depends
 on x in any way, except for generating another commitment to x. (A more re-
 laxed variant of the [ddn00] notion of non-malleability is non-malleability with
 respect to opening [dio98,ff00,dkos01].)

These stronger measures of security for commitment protocols are indeed very
 useful. However they only solve speciﬁc problems and stop short of guaranteeing
 that commitment protocols maintain the expected behavior in general crypto-
 graphic contexts, or in other words when composed with arbitrary protocols. To
 exemplify this point, notice for instance that, although [ddn00] remark on more
 general notions of non-malleability, the standard notion of non-malleability con-
 siders only other copies of the same protocol. There is no guarantee that a mali-
 cious receiver is unable to “maul” a given commitment by using a totally diﬀerent
 commitment protocol. And it is indeed easy to come up with two commitment
 protocols C and C0 such that both are non-malleable with respect to themselves,
 but an adversary that plays a receiver in C can generate a C0-commitment to a
 related value.

This work proposes a measure of security for commitment protocols that
 guarantees the “envelope-like” intuitive properties of commitment even when
 the commitment protocol is concurrently composed with an arbitrary set of pro-
 tocols. In particular, protocols that satisfy this measure (called universally com-
 posable (uc) commitment protocols) remain secure even when an unbounded
 number of copies of the protocol are executed concurrently in an adversarially
 controlled way; they are resilient to selective decommitment attacks; they are
 non-malleable both with respect to other copies of the same protocol and with re-
spect to arbitrary commitment protocols. In general, a uc commitment protocol
successfully emulates an “ideal commitment service” for any application proto-
col (be it a Zero-Knowledge protocol, a general function evaluation protocol, an
e-commerce application, or any combination of the above).

   This measure of security for commitment protocols is very strong indeed.
It is perhaps not surprising then that uc commitment protocols which involve
only the committer and the receiver do not exist in the standard “plain model”
of computation where no set-up assumptions are provided. (We formally prove
this fact.) However, in the common reference string (crs) model things look
better. (The crs model is a generalization of the common random string model.
Here all parties have access to a common string that was chosen according to
some predeﬁned distribution. Other equivalent terms include the reference string
model [d00] and the public parameter model [ff00].) In this model we construct
uc commitment protocols based on standard complexity assumptions. A ﬁrst
construction, based on any family of trapdoor permutations, requires the length
of the reference string to be linear in the number of invocations of the protocol
throughout the lifetime of the system. A second protocol, based on any claw-free
pair of trapdoor permutations, uses a short reference string for an unbounded
number of invocations. The protocols are non-interactive, in the sense that both
the commitment and the decommitment phases consist of a single message from
the committer to the receiver. We also note that uc commitment protocols can
be constructed in the plain model, if the committer and receiver are assisted by
third parties (or, “servers”) that participate in the protocol without having local
inputs and outputs, under the assumption that a majority of the servers remain
uncorrupted.

1.1  On the NewMeasure

Providing meaningful security guarantees under composition with arbitrary pro-
tocols requires using an appropriate framework for representing and arguing
about such protocols. Our treatment is based in a recently proposed such gen-
eral framework [c00a]. This framework builds on known deﬁnitions for function
evaluation and general tasks [gl90,mr91,b91,pw94,c00,dm00,pw01], and al-
lows deﬁning the security properties of practically any cryptographic task. Most
importantly, in this framework security of protocols is maintained under general
concurrent composition with an unbounded number of copies of arbitrary proto-
cols. We brieﬂy summarize the relevant properties of this framework. See more
details in Section 2.1 and in [c00a].

   As in prior general deﬁnitions, the security requirements of a given task (i.e.,
the functionality expected from a protocol that carries out the task) are captured
via a set of instructions for a “trusted party” that obtains the inputs of the
participants and provides them with the desired outputs. However, as opposed
to the standard case of secure function evaluation, here the trusted party (which
is also called the ideal functionality) runs an arbitrary algorithm and in particular
may interact with the parties in several iterations, while maintaining state in
22   R. Canetti and M. Fischlin

 between. Informally, a protocol securely carries out a given task if running the
 protocol amounts to “emulating” an ideal process where the parties hand their
 inputs to the appropriate ideal functionality and obtain their outputs from it,
 without any other interaction.

In order to allow proving the concurrent composition theorem, the notion of
 emulation in [c00a] is considerably stronger than previous ones. Traditionally,
 the model of computation includes the parties running the protocol and an ad-
 versary, A, and “emulating an ideal process” means that for any adversary A
 there should exist an “ideal process adversary” (or, simulator) S that results in
 similar distribution on the outputs for the parties. Here an additional adversar-
 ial entity, called the environment Z, is introduced. The environment generates
 the inputs to all parties, reads all outputs, and in addition interacts with the
 adversary in an arbitrary way throughout the computation. A protocol is said to
 securely realize a given ideal functionality F if for any adversary A there exists an
 “ideal-process adversary” S, such that no environment Z can tell whether it is
 interacting with A and parties running the protocol, or with S and parties that
 interact with F in the ideal process. (In a sense, here Z serves as an “interactive
 distinguisher” between a run of the protocol and the ideal process with access
 to F. See [c00a] for more motivating discussion on the role of the environment.)
 Note that the deﬁnition requires the “ideal-process adversary” (or, simulator)
 S to interact with Z throughout the computation. Furthermore, Z cannot be
 “rewound”.

The following universal composition theorem is proven in [c00a]. Consider
 a protocol π that operates in a hybrid model of computation where parties can
 communicate as usual, and in addition have ideal access to (an unbounded num-
 ber of copies of) some ideal functionality F. Let ρ be a protocol that securely
 realizes F as sketched above, and let πρ be the “composed protocol”. That is,
 πρ is identical to π with the exception that each interaction with some copy of
 F is replaced with a call to (or an invocation of) an appropriate instance of ρ.
 Similarly, ρ-outputs are treated as values provided by the appropriate copy of F.
 Then, π and πρ have essentially the same input/output behavior. In particular,
 if π securely realizes some ideal functionality G given ideal access to F then πρ
 securely realizes G from scratch.

To apply this general framework to the case of commitment protocols, we
 formulate an ideal functionality Fcom that captures the expected behavior of
 commitment. Universally Composable (uc) commitment protocols are deﬁned to
 be those that securely realize Fcom. Our formulation of Fcom is a straightforward
 transcription of the “envelope paradigm”: Fcom ﬁrst waits to receive a request
 from some party C to commit to value x for party R.(C and R are identities
 of two parties in a multiparty network). When receiving such a request, Fcom
 records the value x and notiﬁes R that C has committed to some value for him.
 When  C later sends a request to open the commitment, Fcom sends the recorded
 value x to R, and halts. (Some other variants of Fcom are discussed within.)
 The general composition theorem now implies that running (multiple copies of)
 a uc commitment protocol  π is essentially equivalent to interacting with the
same number of copies of Fcom, regardless of what the calling protocol does. In
particular, the calling protocol may run other commitment protocols and may
use the committed values in any way. As mentioned above, this implies a strong
version of non-malleability, security under concurrent composition, resilience to
selective decommitment, and more.

   The deﬁnition of security and composition theorem carry naturally to the crs
model as well. However, this model hides a caveat: The composition operation
requires that each copy of the uc commitment protocol will have its own copy
of the crs. Thus, a protocol that securely realizes Fcom as described above
is highly wasteful of the reference string. In order to capture protocols where
multiple commitments may use the same reference string we formulate a natural
extension of Fcom that handles multiple commitment requests. Let Fmcom denote
this extension.

   We remark that uc commitment protocols need not, by deﬁnition, be neither
unconditionally secret nor unconditionally binding. Indeed, one of the construc-
tions presented here has neither property.

1.2  On the Constructions

At a closer look, the requirements from a uc commitment protocol boil down
to the following two requirements from the ideal-process adversary (simulator)
S. (a). When the committer is corrupted (i.e., controlled by the adversary), S
must be able to “extract” the committed value from the commitment. (That
is, S has to come up with a value x such that the committer will almost never
be able to successfully decommit to any x0 6= x.) This is so since in the ideal
process S has to explicitly provide Fcom with a committed value. (b). When
the committer is uncorrupted, S has to be able to generate a kosher-looking
“simulated commitment” c that can be “opened” to any value (which will become
known only later). This is so since S has to provide adversary A and environment
Z with the simulated commitment c before the value committed to is known.
All this needs to be done without rewinding the environment Z. (Note that non-
malleability is not explicitly required in this description. It is, however, implied
by the above requirements.)

   From the above description it may look plausible that no simulator S exists
that meets the above requirements in the plain model. Indeed, we formalize and
prove this statement for the case of protocols that involve only a committer and
a receiver. (In the case where the committer and the receiver are assisted by
third parties, a majority of which is guaranteed to remain uncorrupted, stan-
dard techniques for multiparty computation are suﬃcient for constructing uc
commitment protocols. See [c00a] for more details.)

   In the crs model the simulator is “saved” by the ability to choose the ref-
erence string and plant trapdoors in it. Here we present two uc commitment
protocols. The ﬁrst one (that securely realizes functionality Fcom) is based on
the equivocable commitment protocols of [dio98], while allowing the simulator
to have trapdoor information that enables it to extract the values committed
24   R. Canetti and M. Fischlin

 to by corrupted parties. However, the equivocability property holds only with
 respect to a single usage of the crs. Thus this protocol fails to securely realize
 the multiple commitment functionality Fmcom.

In the second protocol (that securely realizes Fmcom), the reference string
 contains a description of a claw-free pair of trapdoor permutations and a public
 encryption key of an encryption scheme that is secure against adaptive chosen ci-
 phertext attacks (CCA) (as in, say, [ddn00,rs91,bdpr98,cs98]). Commitments
 are generated via standard use of a claw-free pair, combined with encrypting po-
 tential decommitments. The idea to use CCA-secure encryption in this context
 is taken from [l00,dkos01].

Both protocols implement commitment to a single bit. Commitment to ar-
 bitrary strings is achieved by composing together several instances of the basic
 protocol. Finding more eﬃcient uc commitment protocols for string commitment
 is an interesting open problem.

 Applicability of the notion.   In addition to being an interesting goal in
 their own right, uc commitment protocols can potentially be very useful in
 constructing more complex protocols with strong security and composability
 properties. To demonstrate the applicability of the new notion, we show how uc
 commitment protocols can be used in a simple way to construct strong Zero-
 Knowledge protocols without any additional cryptographic assumptions.
 Related work.   Pﬁtzmann et. al. [pw94,pw01] present another deﬁnitional
 framework that allows capturing the security requirements of general reactive
 tasks, and prove a concurrent composition theorem with respect to their frame-
 work. Potentially, our work could be cast in their framework as well; however,
 the composition theorem provided there is considerably weaker than the one in
 [c00a].

 Organization.   Section 2 shortly reviews the general framework of [c00a]
 and presents the ideal commitment functionalities Fcom and Fmcom. Section
 3 presents and proves security of the protocols that securely realize Fcom and
 Fmcom. Section 4 demonstrates that functionalities Fcom and Fmcom cannot be
 realized in the plain model by a two-party protocol. Section 5 presents the appli-
 cation to constructing Zero-Knowledge protocols. For lack of space most proofs
 are omitted. They appear in [cf01].

 2   Deﬁnitions

 Section 2.1 shortly summarizes the relevant parts of the general framework of
 [c00a], including the deﬁnition of security and the composition theorem. Section
 2.2 deﬁnes the ideal commitment functionalities, Fcom and Fmcom.

 2.1  The General Framework

 Protocol syntax.  Following [gmra89,g95], protocols are represented as a set
 of interactive Turing machines (ITMs). Speciﬁcally, the input and output tapes
model inputs and outputs that are received from and given to other programs
running on the same machine, and the communication tapes model messages
sent to and received from the network. Adversarial entities are also modeled as
ITMs; we concentrate on a non-uniform complexity model where the adversaries
have an arbitrary additional input, or an “advice”.

The adversarial model.   [c00a] discusses several models of computation. We
concentrate on one main model, aimed at representing current realistic communi-
cation networks (such as the Internet). Speciﬁcally, the network is asynchronous
without guaranteed delivery of messages. The communication is public (i.e., all
messages can be seen by the adversary) but ideally authenticated (i.e., messages
cannot be modiﬁed by the adversary). In addition, parties have unique identi-
ties.1 The adversary is adaptive in corrupting parties, and is active (or, Byzan-
tine) in its control over corrupted parties. Finally, the adversary and environment
are restricted to probabilistic polynomial time (or, “feasible”) computation.
Protocol execution in the real-life model.  We sketch the “mechanics” of
executing a given protocol π (run by parties P1, ..., Pn) with some adversary
A and an environment machine Z  with input z. All parties have a security pa-
rameter k ∈ N and are polynomial in k. The execution consists of a sequence
of activations, where in each activation a single participant (either Z, A,or
some Pi) is activated. The activated participant reads information from its in-
put and incoming communication tapes, executes its code, and possibly writes
information on its outgoing communication tapes and output tapes. In addi-
tion, the environment can write information on the input tapes of the parties,
and read their output tapes. The adversary can read messages oﬀ the outgoing
message tapes of the parties and deliver them by copying them to the incoming
message tapes of the recipient parties. The adversary can also corrupt parties,
with the usual consequences that it learns the internal information known to the
corrupted party and that from now on it controls that party.

   The environment is activated ﬁrst; once activated, it may choose to acti-
vate either one of the parties (with some input value) or to activate the adver-
sary. Whenever the adversary delivers a message to some party P , this party
is activated next. Once P ’s activation is complete, the environment is acti-
vated. Throughout, the environment and the adversary may exchange infor-
mation freely using their input and output tapes. The output of the protocol
execution is the output of Z. (Without loss of generality Z outputs a single bit.)

   Let realπ,A,Z (k, z, r) denote the output of environment Z when interacting
with adversary A and parties running protocol π on security parameter k, input
z and random input r = rZ ,rA,r1 ...rn as described above (z and rZ for Z,
rA for A; ri for party Pi). Let realπ,A,Z (k, z) denote the random variable

1 Indeed, the communication in realistic networks is typically unauthenticated, in the
  sense that messages may be adversarially modiﬁed en-route. In addition, there is no
  guarantee that identities will be unique. Nonetheless, since authentication and the
  guarantee of unique identities can be added independently of the rest of the protocol,
  we allow ourselves to assume ideally authenticated channels and unique identities.
  See [c00a] for further discussion.

 describing realπ,A,Z (k, z, r) when r is uniformly chosen. Let realπ,A,Z denote
 the ensemble {realπ,A,Z (k, z)}k∈N,z∈{0,1}∗ .
 The ideal process. Security of protocols is deﬁned via comparing the protocol
 execution in the real-life model to an ideal process for carrying out the task
 at hand. A key ingredient in the ideal process is the ideal functionality that
 captures the desired functionality, or the speciﬁcation, of that task. The ideal
 functionality is modeled as another ITM that interacts with the environment and
 the adversary via a process described below. More speciﬁcally, the ideal process
 involves an ideal functionality F,anideal process adversary S, an environment Z
  ˜ ˜
 on input z and a set of dummy parties P1, ..., Pn. The dummy parties are ﬁxed and
 simple ITMS: Whenever a dummy party is activated with input x, it forwards
 x to F, say by copying x to its outgoing communication tape; whenever it is
 activated with incoming message from F it copies this message to its output. F
 receives information from the (dummy) parties by reading it oﬀ their outgoing
 communication tapes. It hands information back to the parties by sending this
 information to them. The ideal-process adversary S proceeds as in the real-life
 model, except that it has no access to the contents of the messages sent between
 F and the parties. In particular, S is responsible for delivering messages, and it
 can corrupt dummy parties, learn the information they know, and control their
 future activities.

The order of events in the ideal process is as follows. As in the real-life model,
 the environment is activated ﬁrst. As there, parties are activated when they re-
 ceive new information (here this information comes either from the environment
 or from F). In addition, whenever a dummy party P sends information to F,
 then F is activated. Once F completes its activation, P is activated again. Also,
 F may exchange messages directly with the adversary. It is stressed that in the
 ideal process there is no communication among the parties. The only “commu-
 nication” is in fact idealized transfer of information between the parties and the
 ideal functionality. The output of the ideal process is the (one bit) output of Z.

Let idealF,S,Z (k, z, r) denote the output of environment Z after interact-
 ing in the ideal process with adversary S and ideal functionality F, on security
 parameter k, input z, and random input r = rZ ,rS ,rF as described above (z
 and rZ for Z, rS for S; rF for F). Let idealF,S,Z (k, z) denote the random vari-
 able describing idealF,S,Z (k, z, r) when r is uniformly chosen. Let idealF,S,Z
 denote the ensemble {idealF,S,Z (k, z)}k∈N,z∈{0,1}∗ .

 Securely realizing an ideal functionality. We say that a protocol ρ securely
 realizes an ideal functionality F if for any real-life adversary A there exists an
 ideal-process adversary S such that no environment Z, on any input, can tell with
 non-negligible probability whether it is interacting with A and parties running ρ
 in the real-life process, or it is interaction with A and F in the ideal process. This
 means that, from the point of view of the environment, running protocol ρ is
 ‘just as good’ as interacting with an ideal process for F.(Inaway,Z serves as an
 “interactive distinguisher” between the two processes. Here it is important that
 Z can provide the process in question with adaptively chosen inputs throughout
 the computation.)

Deﬁnition 1.  Let X = {X(k, a)}k∈N,a∈{0,1}∗ and Y = {Y (k, a)}k∈N,a∈{0,1}∗ be
two distribution ensembles over {0, 1}. We say that X and Y are indistinguishable
c
(written X ≈Y) if for any c ∈ N there exists k0 ∈ N such that | Pr(X(k, a)=
  −c
1) − Pr(Y (k, a)=1)| <k  for all k>k0 and all a.

Deﬁnition 2 ([c00a]). Let n ∈ N.LetFbe an ideal functionality and let π
be an n-party protocol. We say that π securely realizes F if for any adversary A
there exists an ideal-process adversary S such that for any environment Z we
  c
have idealF,S,Z ≈ realπ,A,Z .

The common reference string (crs) model.  In this model it is assumed
that all the participants have access to a common string that is drawn from
some speciﬁed distribution. (This string is chosen ahead of time and is made
available before any interaction starts.) It is stressed that the security of the
protocol depends on the fact that the reference string is generated using a pre-
speciﬁed randomized procedure, and no “trapdoor information” related to the
string exists in the system. This in turn implies full trust in the entity that
generates the reference string. More precisely, the crs model is formalized as
follows.

 – The real-life model of computation is modiﬁed so that all participants have
   access to a common string that is chosen in advance according to some
   distribution (speciﬁed by the protocol run by the parties) and is written in
   a special location on the input tape of each party.
 – The ideal process is modiﬁed as follows. In a preliminary step, the ideal-
   model adversary chooses a string in some arbitrary way and writes this
   string on the input tape of the environment machine. After this initial step
   the computation proceeds as before. It is stressed that the ideal functionality
   has no access to the reference string.

Justiﬁcation of the crs  model.  Allowing the ideal-process adversary (i.e.,
the simulator) to choose the reference string is justiﬁed by the fact that the
behavior of the ideal functionality does not depend on the reference string. This
means that the security guarantees provided by the ideal process hold regardless
of how the reference string is chosen and whether trapdoor information regarding
this string is known.

On the composition theorem: The hybrid model. In order to state the
composition theorem, and in particular in order to formalize the notion of a real-
life protocol with access to an ideal functionality, the hybrid model of computa-
tion with access to an ideal functionality F (or, in short, the F-hybrid model)
is formulated. This model is identical to the real-life model, with the following
exceptions. In addition to sending messages to each other, the parties may send
messages to and receive messages from an unbounded number of copies of F.
Each copy of F is identiﬁed via a unique session identiﬁer (SID); all messages
addressed to this copy and all message sent by this copy carry the corresponding
SID. (The SIDs are chosen by the protocol run by the parties.)
28   R. Canetti and M. Fischlin

The communication between the parties and each one of the copies of F
 mimics the ideal process. That is, once a party sends a message to some copy
 of F, that copy is immediately activated and reads that message oﬀ the party’s
 tape. Furthermore, although the adversary in the hybrid model is responsible
 for delivering the messages from the copies of F to the parties, it does not have
 access to the contents of these messages.

 Replacing a call to F  with a protocol invocation.   Let π be a protocol
 in the F-hybrid model, and let ρ be a protocol that securely realizes F (with
 respect to some class of adversaries). The composed protocol πρ is constructed
 by modifying the code of each ITM in π so that the ﬁrst message sent to each
 copy of F is replaced with an invocation of a new copy of π with fresh random
 input, and with the contents of that message as input. Each subsequent message
 to that copy of F is replaced with an activation of the corresponding copy of ρ,
 with the contents of that message given to ρ as new input. Each output value
 generated by a copy of ρ is treated as a message received from the corresponding
 copy of F.

 Theorem   statement.  In its general form, the composition theorem basically
 says that if ρ securely realizes F then an execution of the composed protocol πρ
 “emulates” an execution of protocol π in the F-hybrid model. That is, for any
 real-life adversary A there exists an adversary H in the F-hybrid model such
 that no environment machine Z can tell with non-negligible probability whether
 it is interacting with A and πρ in the real-life model or it is interacting with H
 and π in the F-hybrid model..

A more speciﬁc corollary of the general theorem states that if π securely
 realizes some functionality G in the F-hybrid model, and ρ securely realizes F in
 the real-life model, then πρ securely realizes G in the real-life model. (Here one
 has to deﬁne what it means to securely realize functionality G in the F-hybrid
 model. This is done in the natural way.)

 Theorem   1 ([c00a]). Let F, G be ideal functionalities. Let π be an n-party
 protocol that realizes G in the F-hybrid model and let ρ be an n-party protocol
 that securely realizes F Then protocol πρ securely realizes G.

 Protocol composition in the crs model. Some words of clariﬁcation are in order
 with respect to the composition theorem in the crs model. Speciﬁcally, it is stressed
 that each copy of protocol ρ within the composed protocol πρ should have its own
 copy of the reference string, or equivalently uses a separate portion of a long string.
 (If this is not the case then the theorem no longer holds in general.) As seen below,
 the behavior of protocols where several copies of the protocol use the same instance of
 the reference string can be captured using ideal functionalities that represent multiple
 copies of the protocol within a single copy of the functionality.

 2.2  The Commitment Functionalities

 We propose ideal functionalities that represent the intuitive “envelope-like” prop-
 erties of commitment, as sketched in the introduction. Two functionalities are
  Universally Composable Commitments 29

presented: functionality Fcom that handles a single commitment-decommitment
process, and functionality Fmcom that handles multiple such processes.. (Indeed,
in the plain model functionality Fmcom would be redundant, since one can use
the composition theorem to obtain protocols that securely realize Fmcom from
any protocol that securely realizes Fcom. However, in the crs model realizing
Fmcom is considerably more challenging than realizing Fcom.) Some further dis-
cussion on the functionalities and possible variants appears in [cf01].

   Both functionalities are presented as bit commitments. Commitments to
strings can be obtained in a natural way using the composition theorem. It is
also possible, in principle, to generalize Fcom and Fmcom to allow commitment
to strings. Such extensions may be realized by string-commitment protocols that
are more eﬃcient than straightforward composition of bit commitment protocols.
Finding such protocols is an interesting open problem.


 Functionality Fcom

   Fcom proceeds as follows, running with parties P1, ..., Pn and an adversary S.

1. Upon receiving a value (Commit, sid,Pi,Pj ,b) from Pi, where b ∈{0, 1},
record the value b and send the message (Receipt, sid,Pi,Pj ) to Pj and
S. Ignore any subsequent Commit messages.
2. Upon receiving a value (Open, sid,Pi,Pj ) from Pi, proceed as fol-
lows: If some value b was previously recoded, then send the message
(Open, sid,Pi,Pj ,b) to Pj and S and halt. Otherwise halt.

  Fig. 1. The Ideal Commitment functionality for a single commitment


   Functionality Fcom, described in Figure 1, proceeds as follows. The commit-
ment phase is modeled by having Fcom receive a value (Commit, sid,Pi,Pj,b),
from some party Pi (the committer). Here sid is a Session ID used to distinguish
among various copies of Fcom, Pj is the identity of another party (the receiver),
and b ∈{0, 1} is the value committed to. In response, Fcom lets the receiver
Pj and the adversary S know that Pi has committed to some value, and that
this value is associated with session ID sid. This is done by sending the message
(Receipt, sid,Pi,Pj) to Pj and S. The opening phase is initiated by the com-
mitter sending a value (Open, sid,Pi,Pj) to Fcom. In response, Fcom hands the
value (Open, sid,Pi,Pj,b) to Pj and S.

   Functionality Fmcom, presented in Figure 2, essentially mimics the operation
of Fcom for an unbounded number of times. In addition to the session ID sid,
functionality Fmcom uses an additional identiﬁer, a Commitment ID cid, that is
used to distinguish among the diﬀerent commitments that take place within a
single run of Fmcom. The record for a committed value now includes the Commit-
ment ID, plus the identities of the committer and receiver. To avoid ambiguities,
no two commitments with the same committer and veriﬁer are allowed to have
 the same Commitment ID. It is stressed that the various Commit and Open re-
 quests may be interleaved in an arbitrary way. Also, note that Fmcom allows a
 committer to open a commitment several times (to the same receiver).


  Functionality Fmcom

Fmcom proceeds as follows, running with parties P1, ..., Pn and an adversary S.

 1. Upon  receiving a value (Commit, sid, cid,Pi,Pj ,b) from Pi, where
 b ∈{0,   1}, record the tuple (cid,Pi,Pj ,b) and send the mes-
 sage (Receipt, sid, cid,Pi,Pj ) to Pj and S. Ignore subsequent
 (Commit, sid, cid,Pi,Pj , ...) values.
 2. Upon receiving a value (Open, sid, cid,Pi,Pj ) from Pi, proceed as fol-
 lows: If the tuple (cid,Pi,Pj ,b) is recorded then send the message
 (Open, sid, cid,Pi,Pj ,b) to Pj and S. Otherwise, do nothing.

Fig. 2. The Ideal Commitment functionality for multiple commitments


 Deﬁnition 3.  Aprotocolisauniversally composable (uc) commitment protocol
 if it securely realizes functionality Fcom. If the protocol securely realizes Fmcom
 then it is called a reusable-crs uc commitment protocol.

 Remark: On duplicating commitments.Notice that functionalities Fcom
 and Fmcom disallow “copying commitments”. That is, assume that party A com-
 mits to some value x for party B, and that the commitment protocol in use allows
 B to commit to the same value x for some party C, before A decommitted to x.
 Once A decommits to x for B, B will decommit to x for C. Then this protocol
 does not securely realize Fcom or Fmcom. This requirement may seem hard to
 enforce at ﬁrst, since B can always play “man in the middle” (i.e., forward A’s
 messages to C and C’s messages to A.) We enforce it using the unique identities
 of the parties. (Recall that unique identities are assumed to be provided via an
 underlying lower-level protocol that also guarantees authenticated communica-
 tion.)

 3   Universally Composable Commitment Schemes

 We present two constructions of uc commitment protocols in the common refer-
 ence string model. The protocol presented in Section 3.1 securely realizes func-
 tionality Fcom, i.e. each part of the public string can only be used for a single
 commitment. It is based on any trapdoor permutation. The protocol presented
 in Section 3.2 securely realizes Fmcom, i.e. it reuses the public string for multiple
 commitments. This protocol requires potentially stronger assumptions (either
 existence of claw-free pairs of trapdoor permutations or alternatively the hard-
 ness of discrete log).

3.1  One-Time Common Reference String

The construction in this section works in the common random string model where
each part of the commitment can be used only once for a commitment. It is based
on the equivocable bit commitment scheme of Di Crescenzo et al. [dio98], which
in turn is a clever modiﬁcation of Naor’s commitment scheme [n91].

   Let G be a pseudorandom generator stretching n-bit inputs to 4n-bit outputs.
For security parameter n the receiver in [n91] sends a random 4n-bit string σ to
the sender, who picks a random r ∈{0, 1}n, computes G(r) and returns G(r)or
G(r) ⊕ σ to commit to 0 and 1, respectively. To decommit, the sender transmits
b and r. By the pseudorandomness of G the receiver cannot distinguish both
cases, and with probability 2−2n over the choice of σ it is impossible to ﬁnd
openings r0 and r1 such that G(r0)=G(r1) ⊕ σ.

   In [dio98] an equivocable version of Naor’s scheme has been proposed. Sup-
pose that σ is not chosen by the receiver, but rather is part of the common
random string. Then, if instead we set σ = G(r0) ⊕ G(r1) for random r0,r1, and
let the sender give G(r0) to the receiver, it is later easy to open this commitment
as 0 with r0 as well as 1 with r1 (because G(r0) ⊕ σ = G(r1)). On the other
hand, choosing σ in that way in indistinguishable from a truly random choice.

   We describe a uc bit commitment protocol UCCOneTime (for universally com-
posable commitment scheme in the one-time-usable common reference string
model). The idea is to use the [dio98] scheme with a special pseudorandom
generator, namely, the Blum-Micali-Yao generator based on any trapdoor per-
mutation [y82,bm84]. Let KGen denote an eﬃcient algorithm that on input 1n
generates a random public key pk and the trapdoor td. The key pk describes
n
a trapdoor permutation fpk over {0, 1} . Let B(·) be a hard core predicate for
fpk . Deﬁne a pseudorandom generator expanding n bits to 4n bits with public
description pk by

   Gpk (r)=  fpk (r),B  fpk(r) ,...,B fpk (r) ,B(r)

where fpk (r)isthei-th fold application of fpk to r. An important feature of
this generator is that given the trapdoor td to pk it is easy to recognize images
 4n
y ∈{0, 1}  under Gpk .
   The public random string in our scheme consists of a random 4n-bit string
σ, together with two public keys pk 0, pk 1 describing trapdoor pseudorandom
generators G  and G; both generators stretch n-bit inputs to 4n-bit output.
pk 0 pk 1
The public keys pk 0, pk 1 are generated by two independent executions of the key
generation algorithm KGen on input 1n. Denote the corresponding trapdoors by
td 0 and td 1, respectively.

   In order to commit to a bit b ∈{0, 1}, the sender picks a random string
r ∈{0, 1}n, computes G  (r), and sets y = G (r)ifb =0,ory  = G(r) ⊕ σ
pk b  pk bpk b
for b = 1. The sender passes y to the receiver. In the decommitment step the
sender gives (b, r) to the receiver, who veriﬁes that y=G (r) for b = 0 or that
  pk b
y = G   (r) ⊕ σ for b = 1. See also Figure 3.
 pk b

Commitment schemeUCCOneTime

 public string:
  σ — random string in {0, 1}4n
  pk , pk — keys for generators G ,G : {0, 1}n →{0, 1}4n
0   1pk0 pk1

 commitment for b ∈{0, 1} with SID sid:
  compute G  (r) for random r ∈{0, 1}n
pkb
  set y = G (r) for b =0,ory = G  (r) ⊕ σ for b =1
   pkbpkb
  send (Com, sid,y) to receiver

 decommitment for y:
  send b, r to receiver
  receiver checks y =? G (r) for b =0,ory =? G (r) ⊕ σ for b =1
  pkbpkb

 Fig. 3. Commitment Scheme in the One-Time-Usable Common Reference String Model


Clearly, the scheme is computationally hiding and statistically binding. An
 important observation is that our scheme inherits the equivocability property of
 [dio98]. In a simulation we replace σ by G (r ) ⊕ G  (r ) and therefore, if
 pk 0 0 pk 1 1
 we impersonate the sender and transmit y = Gpk (r0) to a receiver, then we can
 later open this value with 0 by sending r0 and with 1 via r1.
Moreover, if we are given a string y∗, e.g., produced by the adversary, and
 we know the trapdoor td 0 to pk 0, then it is easy to check if y is an image under
 Gand therefore represents a 0-commitment. Unless y∗ belongs to G and,
  pk 0   pk 0
 simultaneously, y∗ ⊕σ belongs to G , the encapsulated bit is unique and we can
pk 1
 extract the correct value with td 0. (We stress, however, that this property will
 not be directly used in the proof. This is so since there the crs has a diﬀerent
 distribution, so a more sophisticated argument is needed.)

To summarize, our commitment scheme supports equivocability and extrac-
 tion. The proof of the following theorem appears in [cf01].

 Theorem   2. Protocol UCCOneTime securely realizes functionality Fcom in the
 crs model.

 3.2  Reusable Common Reference String

 The drawback of the construction in the previous section is that a fresh part of
 the random string must be reserved for each committed bit. In this section, we
 overcome this disadvantage under a potentially stronger assumption, namely the
 existence of claw-free trapdoor permutation pairs. We concentrate on a solution
 that only works for erasing parties in general, i.e., security is based on the parties’
 ability to irrevocably erase certain data as soon as they are supposed to. At the
end of this section we sketch a solution that does not require data erasures. This
solution is based on the Decisional Diﬃe-Hellman assumption.
   Basically, a claw-free trapdoor permutation pair is a pair of trapdoor permu-
tations with a common range such that it is hard to ﬁnd two elements that are
preimages of the same element under the two permutations. More formally, a key

generation KGenclaw outputs a random public key pk claw and a trapdoor td claw.
The public key deﬁnes permutations f   ,f  : {0, 1}n →{0, 1}n, whereas
 0,pk claw 1,pk claw
the secret key describes the inverse functions f −1 ,f−1 . It should be in-
 0,pk claw 1,pk claw
feasible to ﬁnd a claw x ,x with f(x )=f(x ) given only pk  .
 0  1  0,pk claw 0   1,pk claw 1claw
 −1   −1

For ease of notation we usually omit the keys and write f0,f1,f0 ,f1 instead.
Claw-free trapdoor permutation pairs exist for example under the assumption
that factoring is hard [gmri88]. For a more formal deﬁnition see [g95].

   We also utilize an encryption scheme E =(KGen, Enc, Dec) secure against
adaptive-chosen ciphertext attacks, i.e., in the notation of [bdpr98] the encryp-
tion system should be IND-CCA2. On input 1n  the key generation algorithm

KGen returns a public key pk E and a secret key sk E . An encryption of a message
m is given by c←Enc   (m), and the decryption of a ciphertext c is Dec (c).
 pk E sk E
It should always hold that Dec (c)=mfor c←Enc(m), i.e., the system
 sk E   pk E
supports errorless decryption. Again, we abbreviate Enc (·)byEnc(·) and
   pk E
Dec   (·)byDec(·). IND-CCA2 encryption schemes exist for example under the
   sk E
assumption that trapdoor permutations exist [ddn00]. A more eﬃcient solution
is based on the decisional Diﬃe-Hellman assumption [cs98]. Both schemes have
errorless decryption.

   The commitment scheme  UCCReUse (for universally composable commitment
with reusable reference string) is displayed in Figure 4. The (reusable) public

string contains random public keys pk claw and pk E . For a commitment to a bit b
  n
the sender Pi applies the trapdoor permutation fb to a random x ∈{0, 1} , com-
putes c ←Enc   (x, P ) and c ←Enc(0n,P ), and sends the tuple (y, c ,c )
  b pk E   i  1−b pk E i  0  1
with y = fb(x) to the receiver. The sender is also instructed to erase the ran-
  n
domness for the encryption of (0 ,Pi) before the commitment message is sent.
This ciphertext is called a dummy ciphertext.

   To open the commitment, the committer Pi sends b, x and the randomness
used for encrypting (x, Pi). The receiver Pj veriﬁes that y = fb(x), that the
encryption randomness is consistent with cb, and that cid was never used before
in a commitment of Pi to Pj.

   We remark that including the sender’s identity in the encrypted strings plays
an important role in the analysis. Essentially, this precaution prevents a cor-
rupted committer from “copying” a commitment generated by an uncorrupted
party.

   The fact that the dummy ciphertext is never opened buys us equivocability.
Say that the ideal-model simulator knows the trapdoor of the claw-free permu-
tation pair. Then it can compute the pre-images x0,x1 of some y under both
functions f0,f1 and send y as well as encryptions of (x0,Pi) and (x1,Pi). To
open it as 0 hand 0,x0 and the randomness for ciphertext (x0,Pi) to the re-
ceiver and claim to have erased the randomness for the other encryption. For a
1-decommitment send 1,x1, the randomness for the encryption of (x1,Pi) and

Commitment schemeUCCReUse

  public string:

   pk claw — public key for claw-free trapdoor permutation pair f0,f1
   pk E — public key for encryption algorithm Enc

  commitment by party  Pi to party Pj to b ∈{0, 1} with identiﬁer sid, cid:
n
   compute  y = fb(x) for random x ∈{0, 1} ;
   compute  cb←Enc(x, Pi) with randomness  rb;
  n
   compute  c1−b←Enc(0   ,Pi) with randomness r1−b;
   erase r1−b;
   send (Com, sid, cid, (y, c0,c1)), and record (sid, cid,b,x,rb).
   Upon receiving (Com, sid, cid, (y, c0,c1)) from Pi,
 Pj outputs (Receipt, sid, cid,Pi,Pj ))

  decommitment for  (Pi,Pj , sid, cid,b,x,rb):

   Send (Dec, sid, cid,b,x,rb)toPj .
 ?
   Upon receiving (Dec, sid, cid,b,x,rb), Pj veriﬁes that y = fb(x),
that cb is encryption of (x, Pi) under randomness rb
where  Pi is the committer’s identity
and that cid has not been used with this committer before.

 Fig. 4. Commitment Scheme with Reusable Reference String


 deny to know the randomness for the other ciphertext. If the encryption scheme
 is secure then it is intractable to distinguish dummy and such fake encryptions.
 Hence, this procedure is indistinguishable from   the actual steps of the honest
 parties.

Analogously to the extraction procedure for the commitment scheme in the
 previous section, here an ideal-process adversary can also deduce the bit from an
 adversarial commitment (y,c0,c1) if it knows the secret key of the encryption
 scheme. Speciﬁcally, decrypt  c0  to obtain (x0,Pi  ); if x0 maps to y   under  f0
 then let the guess be 0, else predict 1. This decision is only wrong if the adversary
 has found a claw, which happens only with negligible probability. The proof of
 the following theorem appears in [cf01].

 Theorem3. Protocol  UCCReUse  securely realizes functionality Fmcom in the crs
 model.

 A  solution for non-erasing parties.The security of the above scheme depends
 on the ability and good-will of parties to securely erase sensitive data (speciﬁcally, to
 erase the randomness used to generate the dummy ciphertext). A careful look shows
 that it is possible to avoid the need to erase: It is suﬃcient to be able to generate a
 ciphertext without knowing the plaintext. Indeed, it would be enough to enable the
 parties to obliviously generate a string that is indistinguishable from a ciphertext.
 Then the honest parties can use this mechanism  to produce the dummy ciphertext,
while the simulator is still able to place the fake encryption into the commitment. For 
example, the Cramer-Shoup system   in subgroup G of Zp has this property under the
decisional Diﬃe-Hellman assumption: To generate a dummy ciphertext simply generate
four random elements in G.

Relaxing the need for claw-free pairs.The above scheme was presented and
proven using any claw-free pair of trapdoor permutations. However, it is easy to see
that the claw-free pair can be substituted by chameleon commitments a la [bcc88],
thus basing the security of the scheme on the hardness of the discrete logarithm or
factoring. Further relaxing the underlying hardness assumptions is an interesting task.

4   Impossibility of UC Commitments in the Plain Model

This section demonstrates that in the plain model there cannot exist univer-
sally composable commitment protocols that do not involve third parties in the
interaction and allow for successful completion when both the sender and the
receiver are honest. This impossibility result holds even under the more liberal
requirement that for any real-life adversary and any environment there should be
an ideal-model adversary (i.e., under a relaxed deﬁnition where the ideal-model
simulator may depend on the environment).

   We remark that universally composable commitment protocols exist in the
plain model if the protocol makes use of third parties, as long as a majority of the
parties remain uncorrupted. This follows from a general result in [c00a], where
it is shown that practically any functionality can be realized in this setting.

   Say that a protocol π between  n parties P1,...,Pn  is bilateral if all except two
parties stay idle and do not transmit messages. A bilateral commitment protocol
π is called terminating if, with non-negligible probability, the receiver Pj accepts
a commitment of the honest sender Pi and outputs (Receipt,sid,Pi,Pj), and
moreover if the receiver, upon getting a valid decommitment for a messagem
and sid from the honest sender, outputs (Open,  sid,Pi,Pj,m) with non-negligible
probability.

Theorem4. There exists no bilateral, terminating protocol  π that securely re-
alizes functionality Fcom in the plain model. This holds even if the ideal-model
adversary S  is allowed to depend on the environment   Z.

Proof. The idea of the proof is as follows. Consider a protocol execution between an
adversarially controlled committer Pi and an honest receiver Pj , and assume that
the adversary merely sends messages that are generated by the environment. The
environment secretly picks a random bit b at the beginning and generates the messages

for Pi by running the protocol of the honest committer for b and Pj ’s answers. In order
to simulate this behavior, the ideal-model adversary S must be able to provide the
ideal functionality with a value for the committed bit. For this purpose, the simulator
has to “extract” the committed bit from the messages generated by the environment,
without the ability to rewind the environment. However, as will be seen below, if the
commitment scheme allows the simulator to successfully extract the committed bit,
then the commitment is not secure in the ﬁrst place (in the sense that a corrupted
 receiver can obtain the value of the committed bit from interacting with an honest
 committer).

More precisely, let the bilateral protocol π take place between the sender Pi and
 the receiver Pj . Consider the following environment Z and real-life adversary A.At
 the outset of the execution the adversary A corrupts the committer Pi. Then, in the
 sequel, A has the corrupted committer send every message it receives from  Z, and
 reports any reply received by Pj to Z. The environment  Z  secretly picks a random
 bit b and follows the program of the honest sender to commit to b, as speciﬁed by π.
 Once the the honest receiver has acknowledged the receipt of a commitment, Z lets A
0
 decommit to b by following protocol π. Once the receiver outputs (Open, sid,Pi,Pj ,b),
 Z outputs 1 if b = b0 and outputs 0 otherwise.

Formally, suppose that there is an ideal-model adversary S such that realπ,A,Z
  0
 ≈idealFcom,S,Z . Then we construct a new environment Z and a new real-life adversary
 A0 for which there is no appropriate ideal-model adversary for π. This time, A0 corrupts
0
 the receiver Pj at the beginning. During the execution A obtains messages form the
 honest committer Pi and feeds these messages into a virtual copy of S. The answers of S,
 made on behalf of an honest receiver, are forwarded to Pi in the name of the corrupted
 0
 party Pj . At some point, S creates a submission (Commit, sid,Pi,Pj ,b)toFcom; the
 adversary A0 outputs b0 and halts. If S halts without creating such a submission then
 A0 outputs a random bit and halts.
  0

The environment  Z  instructs the honest party Pi to commit to a randomly chosen
 secret bit b. (No decommitment is ever carried out.) Conclusively, Z0 outputs 1 iﬀ the
 adversary’s output b0 satisﬁes b = b0.

By the termination property, we obtain from the virtual simulator S a bit b0 with
 non-negligible probability. This bit is a good approximation of the actual bit b, since
 S simulates the real protocol π except with negligible error. Hence, the guess of A0 for
 b is correct with 1/2 plus a non-negligible probability. But for a putative ideal-model
 adversary S0 predicting this bit b with more than non-negligible probability over 1/2
 is impossible, since the view of S0 in the ideal process is statistically independent from
 the bit b. (Recall that the commitment to b is never opened).

 5   Application to Zero-Knowledge

 In order to exemplify the power of UC commitments we show how they can be
 used to construct simple Zero-Knowledge (ZK) protocols with strong security
 properties. Speciﬁcally, we formulate an ideal functionality,  Fzk, that captures
 the notion of Zero-Knowledge in a very strong sense. (In fact,   Fzk implies con-
 current and non-malleable Zero-Knowledge proofs of knowledge.) We then show
 that in the Fcom-hybrid model (i.e., in a model with ideal access to  Fcom) there
 is a 3-round protocol that securely realizes Fzk with respect to any NP relation.
 Using the composition theorem of [c00a], we can replace   Fcom   with any  uc
 commitment protocol. (This of course requires using the crs model, unless we
 involve third parties in the interaction. Also, using functionality Fmcom  instead
 of Fcom is possible and results in a more eﬃcient use of the common string.)

Functionality Fzk, described in Figure 5, is parameterized by a binary relation
 R(x, w). It ﬁrst waits to receive a message  (verifier, id, Pi,Pj,x)   from  some
 party Pi, interpreted as saying that  Pi wants  Pj  to prove to Pi  that it knows

a value w such that R(x, w) holds. Next, Fzk waits for Pj to explicitly provide
a value w, and notiﬁes Pi whether R(x, w) holds. (Notice that the adversary
is notiﬁed whenever either the prover or the veriﬁer starts an interaction. It is
also notiﬁed whether the veriﬁer accepts. This represents the fact that ZK is not
traditionally meant to hide this information.)


Functionality Fzk

   Fzk proceeds as follows, running with parties P1, ..., Pn and an adversary S.
   The functionality is parameterized by a binary relation R.

1. Wait to receive a value (verifier, id, Pi,Pj ,x) from some party Pi. Once
such a value is received, send (verifier, id, Pi,Pj ,x) to S, and ignore all
subsequent (verifier...) values.
   0
2. Upon receipt of a value (prover, id, Pj ,Pi,x,w) from Pj , let v =1if
0
x = x and R(x, w) holds, and v = 0 otherwise. Send (id, v) to Pi and S,
and halt.

  Fig. 5. The Zero-Knowledge functionality, Fzk


R

   We demonstrate a protocol for securely realizing Fzk for any NP relation R.
The protocol is a known one: It consists of n parallel repetitions of the 3-round
protocol of Blum for graph Hamiltonicity, where the provers commitments are
replaced by invocations of Fcom. The protocol (in the Fcom-hybrid model) is
presented in Figure 6.

   We remark that in the Fcom-hybrid model the protocol securely realizes Fzk
without any computational assumptions, and even if the adversary and the envi-
ronment are computationally unbounded. (Of course, in order to securely realize
Fcom the adversary and environment must be computationally bounded.) Also,
in the Fcom-hybrid model there is no need in a common reference string. That
is, the crs model is needed only for realizing Fcom.
 H

   Let Fzk denote functionality Fzk parameterized by the Hamiltonicity relation
H. (I.e., H(G, h)=1iﬀh   is a Hamiltonian cycle in graph G.) The following
theorem is proven in [cf01].

Theorem   5. Protocol hc securely realizes Fzk in the Fcom-hybrid model.

Acknowledgements. We thank Yehuda Lindell for suggesting to use non-
malleable encryptions for achieving non-malleability of commitments in the com-
mon reference string model. This idea underlies our scheme that allows to reuse
the common string for multiple commitments. (The same idea was independently
suggested in [dkos01].)

Protocol Hamilton-Cycle (hc)

  1. Given input (Prover, id, P, V, G, h), where G is a graph over nodes 1, ..., n,
  the prover P proceeds as follows. If h is not a Hamiltonian cycle in G,
  then P sends a message  reject to V . Otherwise, P proceeds as follows
  for k =1, ..., n:

  a) Choose a random permutationπk over [n].
  b) Using  Fcom, commit to the edges of the permuted graph. That is, for
  2
  each (i, j) ∈ [n] send (Commit,(i, j, k),P,V,e) to Fcom, where e =1
  if there is an edge between πk(i) and πk(j)inG, and e = 0 otherwise.
   c) Using Fcom, commit to the permutation  πk. That is, for l =1, ..., L
  send (Commit,(l, k),P,V,pl) to Fcom where p1, ..., pL is a representa-
  tion of πk in some agreed format.
  2. Given input (Verifier, id, V, P, G), the veriﬁer V waits to receive either
  reject from  P ,or(Receipt,(i, j, k),P,V)   and (Receipt,(l, k),P,V)

  from Fcom, for i, j, k =1, ..., n and l =1, ..., L.Ifreject is received, then
  V output 0 and halts. Otherwise, once all the (Receipt,...)  messages
  are received V randomly chooses n bits c1, ..., cn and sends to P .
  3. Upon receiving c1, ..., cn from V , P proceeds as follows for k =1, ..., n:
  a) If ck =  0 then send (Open,(i, j, k),P,V) and (Open,(l, k),P,V) to
  Fcom for all i, j =1, ..., n and l =1, ..., L.
  b) If ck = 1 then send (Open,(i, j, k),P,V) to Fcom for all i, j =1, ..., n
  such that the edge πk(i),πk(j) is in the cycle h.
  
4. Upon receiving the appropriate (Open,...) messages from Fcom, the ver-
  iﬁer V veriﬁes that for all k such that ck = 0 the opened edges agree with
  the input graph G  and the opened permutation   πk, and for all k such
  that ck = 1 the opened edges are all 1 and form a cycle. If veriﬁcation
  succeeds then output 1, otherwise output 0.

Fig. 6. The protocol for proving Hamiltonicity in the Fcom-hybrid model

 References

 [b91]  D. Beaver, “Secure Multi-party Protocols and Zero-Knowledge Proof Sys-
 tems Tolerating a Faulty Minority”, J. Cryptology, Springer-Verlag, (1991)
 4: 75-122.
 [b99]  D. Beaver, “Adaptive Zero-Knowledge and Computational Equivocation”,
 28th Symposium   on Theory of Computing (STOC),   ACM, 1996.
 [bbm00]M. Bellare, A. Boldyreva and S. Micali, “Public-Key Encryption in a Multi-
 user Setting: Security Proofs and Improvements,” Eurocrypt 2000, pp. 259–
 274, Springer LNCS 1807,   2000.
 [bdjr97]   M  Bellare, A. Desai, E. Jokipii and P. Rogaway, “A concrete security treat-
 ment of symmetric encryption: Analysis of the DES modes of operations,”
 38th Annual Symp. on Foundations of Computer Science (FOCS), IEEE,
 1997.
 [bdpr98]   M. Bellare, A. Desai, D. Pointcheval and P. Rogaway,  “Relations among
 notions of security for public-key encryption schemes”, CRYPTO ’98, 1998,
 pp. 26-40.
Universally Composable Commitments39

[bm84] M.Blum, S.Micali: How to Generate Cryptographically Strong Sequences
of Pseudorandom Bits,  SIAM  Journal on Computation, Vol. 13, pp. 850–
864, 1984.
[bcc88]G. Brassard, D. Chaum   and C. Cr´epeau. MinimumDisclosure Proofs of
Knowledge.  JCSS, Vol. 37, No. 2, pages 156–189, 1988.
[c00]  R. Canetti, “Security and composition of multi-party cryptographic pro-
tocols”, Journal of Cryptology, Vol. 13, No. 1, winter 2000.
[c00a] R. Canetti, “A  uniﬁed framework for analyzing security of Protocols”,
manuscript, 2000. Available at http://eprint.iacr.org/2000/067.
[cf01] R. Canetti and M. Fischlin, “Universally Composable Commitments”.
Available at http://eprint.iacr.org/2001.
[cs98] R. Cramer and V. Shoup, “A paractical public-key cryptosystem provably
secure against adaptive chosen ciphertext attack”, CRYPTO ’98,  1998.
[d89]  I. Damgard, On the existence of bit commitment schemes and zero-
knowledge proofs, Advances in Cryptology - Crypto ’89, pp. 17–29, 1989.
[d00]  I. Damgard. Eﬃcient Concurrent Zero-Knowledge in the Auxiliary String
Model.  Eurocrypt 00, LNCS, 2000.
[dio98]G. Di Crescenzo, Y. Ishai and R. Ostrovsky, Non-interactive and non-
malleable commitment,  30th STOC,   1998, pp. 141-150.
[dkos01]   G. Di Crecenzo, J. Katz, R. Ostrovsky and A. Smith. Eﬃcient and
Perfectly-Hiding Non-Interactive, Non-Malleable Commitment.   Eurocrypt
’01, 2001.
[dm00] Y. Dodis and S. Micali, “Secure Computation”,  CRYPTO ’00,   2000.
[ddn00]D. Dolev, C. Dwork and M. Naor, Non-malleable cryptography,   SIAM.. J.
Computing,  Vol. 30, No. 2, 2000, pp. 391-437. Preliminary version in 23rd
Symposium on Theory of Computing (STOC),  ACM, 1991.
[dnrs99]   C. Dwork, M. Naor, O. Reingold, and L. Stockmeyer. Magic functions.
In 40th Annual Symposiumon Foundations of Computer Science, pages
523–534. IEEE, 1999.
[fs90] U. Feige and A. Shamir. Witness Indistinguishability and Witness Hiding
Protocols. In 22nd STOC, pages 416–426, 1990.
[ff00] M. Fischlin   and  R. Fischlin, “Eﬃcientnon-malleable  commitment
schemes”, CRYPTO ’00, LNCS 1880, 2000, pp. 413-428.
[ghy88]Z. Galil, S. Haber and M. Yung, Cryptographic computation: Secure faut-
tolerant protocols and the public-key model, CRYPTO ’87, LNCS 293,
Springer-Verlag, 1988, pp. 135-155.
[g95]  O. Goldreich,  “Foundations of Cryptography (Fragments of a book)”,
Weizmann Inst. of Science, 1995. (Avaliable at http://philby.ucsd.edu)
[g98]  O. Goldreich.  “Secure Multi-Party Computation”,1998. (Avaliable at
http://philby.ucsd.edu)
[gmw91]O. Goldreich, S. Micali and A. Wigderson, “Proofs that yield nothing
but their validity or All Languages in NP Have Zero-Knowledge Proof
Systems”,  Journal of the ACM,  Vol 38, No. 1, ACM, 1991, pp. 691–729.
Preliminary version in 27th Symp. on Foundations of Computer Science
(FOCS),  IEEE, 1986, pp. 174-187.
[gmw87]O. Goldreich, S. Micali and A. Wigderson, “How to Play any Mental
Game”,  19th Symposium   on Theory of Computing (STOC),ACM, 1987,
pp. 218-229.
[gl90] S. Goldwasser, and L. Levin, “Fair Computation of General Functions
in Presence of Immoral Majority”,  CRYPTO ’90, LNCS 537,  Springer-
Verlag, 1990.
40R. Canetti and M. Fischlin

 [gmra89]   S. Goldwasser, S. Micali and C. Rackoﬀ, “The Knowledge Complexity of
 Interactive Proof Systems”, SIAM   Journal on Comput.,  Vol. 18, No. 1,
 1989, pp. 186-208.
 [gmri88]   S.Goldwasser, S.Micali, R.Rivest: A   Digital Signature Scheme Secure
 Against Adaptive Chosen-Message Attacks,  SIAM   Journal on Computing,
 Vol. 17, No. 2, pp. 281–308, 1988.
 [l00]  Y. Lindell, private communication, 2000.
 [mr91] S.  Micali  and   P.  Rogaway,   “Secure   Computation”,   unpublished
 manuscript, 1992. Preliminary   version in  CRYPTO ’91, LNCS   576,
 Springer-Verlag, 1991.
 [n91]  M.Naor: Bit Commitment Using Pseudo-Randomness,  Journal of Cryptol-
 ogy, vol. 4, pp. 151–158, 1991.
 [novy92]   M. Naor, R. Ostrovsky, R. Venkatesan, and  M. Yung, Perfect zero-
 knowledge arguments for NP can be based on general complexity assump-
 tions, Advances in Cryptology - Crypto ’92, pp. 196–214, 1992.
 [pw94] B. Pﬁtzmann and M. Waidner, “A general framework for formal notions
 of secure systems”, Hildesheimer Informatik-Berichte 11/94, Universit¨at
 Hildesheim, 1994. Available at http://www.semper.org/sirene/lit.
 [pw01] B. Pﬁtzmann and M. Waidner, “A model for asynchronous reactive systems
 and its application to secure message transmission”, IEEE Symposium
 on Security and Privacy, 2001. See also IBM  Research Report RZ 3304
 (#93350), IBM   Research, Zurich, December 2000.
 [rs91] C. Rackoﬀ and D. Simon, “Non-interactive zero-knowledge proof of knowl-
 edge and chosen ciphertext attack”, CRYPTO ’91,   1991.
 [y82]  A. Yao, Theory and applications of trapdoor functions,In Proc. 23rd
 Annual Symp. on Foundations of Computer Science (FOCS),  pages 80–
 91. IEEE, 1982.
