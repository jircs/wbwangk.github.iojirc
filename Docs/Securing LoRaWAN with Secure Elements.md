立即加入
登录
Securing LoRaWAN with Secure Elements
Secure element and device claiming process
Securing LoRaWAN with Secure Elements
发布日期: N 26, 2019

Johan Stokking
Johan StokkingFollow
CTO & Co-founder of The Things Industries and Tech Lead of The Things Network
Like112
Comment6
0
LoRaWAN® is an open protocol for secure messaging between devices and networks, typically leveraging LoRa® modulation in unlicensed sub-GHz spectrum for low power wide area networking (LPWAN).

At the heart of the protocol, LoRaWAN addresses the three pillars of security: authenticity, integrity and confidentiality. Since LoRaWAN uses symmetric cryptography, it is crucial that the root keys are provisioned securely and kept safe. On the network side, you need a LoRaWAN Join Server that you trust as it has access to the root keys. On the device side, the most secure solution is using dedicated secure elements that are pre-provisioned and have physical protection against tampering.

When data authenticity, integrity and confidentiality really matters, you need secure elements.

What Is In It For Me?
Before diving in the technical solution, here are the main benefits for device makers, device owners, integrators and network operators.

As a device maker, you can build devices where authenticity, integrity and confidentiality is enforced by dedicated hardware. In addition, the secure elements are pre-provisioned, so the production and distribution becomes simpler and more secure. Also, you free up space for your application code by leveraging secure elements for cryptographic operations. Finally, you can deploy firmware updates with confidence at scale as the secure element verifies the update. The secure elements are only 2 by 3 mm in size and are very low power.

Secure elements and a 1 eurocent coin
As a device owner, you can be sure that nobody has seen the root keys of your devices; neither the device maker, distributors, network operators nor previous owners. Also, even when the device gets physically compromised, an attacker will not be able to spoof the device or install malicious firmware.

As an integrator or service provider, secure elements provide confidence throughout the lifetime of devices. With devices deployed in the field, you can switch networks and even Join Servers. You can install devices in public spaces without risking tampering. And you can ensure that devices will only run firmware with the right signature.

Finally, as a network operator, not having to store root keys is one thing less to worry about. Integrating with Join Servers will be a necessity to activate next generation LoRaWAN devices on your network. Customers bring their certified devices and they expect activation.

Why Do I Need a Join Server?
The LoRaWAN security model is symmetric. This means that the device root keys must also be stored somewhere in the network, for example in the cloud or on-premises. Asymmetric cryptography would simply not fit: most LoRaWAN devices have very limited resources to reduce cost and increase battery life, and LoRaWAN payload sizes are too small for using block ciphers, certificates and signatures effectively.

Traditionally, root keys are configured in the LoRaWAN Network Server (LNS) of the network where the devices are activated. This means that keys get transferred from the device makers, via distributors and device owners to network operators. Transferring keys via many parties is a vulnerable process and is hard to do right. Today, if you buy a bunch of LoRaWAN devices, don't be surprised if you receive the root keys in Excel files attached to an email or printed on the invoice.

Keys printed on paper
In addition, once you have the root keys of an end device, you can activate the device for the rest of its lifetime. Therefore, moving from one network to another is painful; you have to trust the old network to delete your root keys.

So, you want your keys to be in a safe place and you don't want a lock-in with your network operator. This is where a LoRaWAN Join Server comes in: a dedicated service for handling the sensitive part of the device activation procedure on any LoRaWAN network.

Network Servers contact the Join Server for activating a device. The Join Server authenticates the Network Server and checks whether the device owner wants to activate the device on that Network Server. The Join Server derives the session keys from the root keys and sends these session keys securely to the Network Server and the Application Server of choice. All LoRaWAN network stacks implementing the standardized LoRaWAN Backend Interfaces support this mechanism.

So, provisioning devices on a Join Server is no-brainer really. However, some LoRaWAN networks are built as silos and are not as interoperable as they should be. Please consult your network operator or service provider if you can choose, control or bring your own Join Server before deploying devices at scale.

Why Do I Need Secure Elements?
Secure elements are a dedicated micro-controller unit (mcu) for carrying out cryptographic operations and come with physical protection against tampering.

While Join Servers are highly recommended in any LoRaWAN deployment, the use of secure elements is a trade-off between security and cost.

First, secure elements are already provisioned with a JoinEUI (formerly AppEUI), a unique DevEUI and root keys. There is no longer a need to generate root keys yourself and provision the devices by trusting contract manufacturers and distributors. Pre-provisioning happens in highly secure facilities before secure elements become part of the device. This is not only secure, it is also cost effective as all other key provisioning steps can be removed from the production and distribution process. You can safely deliver pre-provisioned secure elements to contract manufacturers: they will not be able to see any root keys.

Second, all sensitive information is kept safe, not just the root keys. Secure elements perform all LoRaWAN security operations: session key derivation, message integrity code (MIC) calculation and payload encryption. In fact, only the result of the cryptographic operations. Did you know that it is feasible to read sensitive information from off-the-shelf devices without secure elements? Read Erich Styger's article about reverse engineering a LoRaWAN device and learn how.

Third, you get more space for your application code. Since secure elements carry out many cryptographic operations, you don't longer need an AES implementation, for example. Less code dependencies, so more space for your code to make your device even smarter.

Fourth, you can securely boot and update your devices. Secure elements have slots available for storing certificates and public keys. This allows you to verify firmware updates with a public key so you can update devices deployed in the field with confidence and at scale. Read more about firmware updates over LoRaWAN.

Fifth, there's already legislation as well as governmental and industrial recommendations that requires keys in connected devices to be stored in tamper resistant hardware and allows change of ownership. For instance, the California law SB-327 requires reasonable hardware security features depending on the function of the device. This law is exemplary for other state and federal governments to legislate the requirement of hardware security features. Adopting secure elements now is not only a competitive advantage, it also ensures longevity of the design.

So why would you not use secure elements? What are the disadvantages? When we're talking to device makers, their main concerns are cost and network lock-in. Cost depends on many factors, but when doing the math, please take into account the cost of key provisioning, the cost of leaking sensitive information and the cost of devices running malicious firmware leading to manual interventions. As for the network lock-in: that is unfortunately often a valid point, but our solution accounts for this.

The Solution
Early 2019, The Things Industries and Microchip launched the first secure element solution designed specifically for LoRaWAN. It's fully compliant with LoRaWAN 1.0.x and 1.1.x. We're happy to announce that we have a second generation available that is even easier to onboard on networks.

The new secure element is the Microchip ATECC608A-TNGLORA. See the development kits to get started quickly. The minimum order quantity is as low as 10 (€ 1.27 per unit), with volume discounts with packages up to 2000 units (€ 0.82 per unit).

Device claiming process
When purchasing secure elements, device makers receive a manifest file from Microchip. The manifest does not contain any keys, it just acts as a proof of ownership. Device makers upload the manifest to The Things Industries Join Server to claim their secure elements. Once devices are built and shipped, device makers can print QR codes with a standard format for easy onboarding by device owners. They can also use APIs for onboarding devices at scale.

The ATECC608A-TNGLORA is pre-provisioned on The Things Industries Join Server, a LoRaWAN Backend Interfaces compliant Join Server.

Trust
The Things Industries and Microchip have securely exchanged master keys between their hardware secure modules (HSMs). Microchip derives root keys offline in NISK certified facilities. The Things Industries uses a FIPS 140-2 Level 3 certified HSM to store the master keys that are used by the Join Server. The Things Industries Join Server derives root keys only temporarily during the activation flow. Root keys are never stored, logged or exposed to anyone.

By nature of LoRaWAN, a Join Server has access to the device's session address (DevAddr) and session keys. In theory, that information allows for reading traffic sent by devices. However, The Things Industries Join Server is a dedicated Join Server and never sees any LoRaWAN traffic.

No Vendor Lock-in
The solution presented allows for secure and zero effort key provisioning for device makers that is fully standardized to work on any LoRaWAN network. To give more control and confidence, we present three extensions for the presented solution: exporting root keys, and online and offline rekeying.

Export Root Keys
The Things Industries Join Server allows for exporting the root keys of devices that you own. This allows for migrating the devices to a new Join Server. This typically involves changing the JoinEUI of the devices as well, so networks contact the new Join Server via the LoRaWAN Backend Interfaces.

Caution should be taken when exporting keys: once they leave The Things Industries Join Server, you are on your own. When exporting the keys, you provide a public key with which the root keys are encrypted. They can be decrypted with the corresponding private key and loaded in any other Join Server or traditional Network Server.

Online and Offline Rekeying
The secure element is future compatible with rekeying to a new Join Server. This means that the root keys change and that a new Join Server gets contacted for the device activation flow.

There are two types of rekeying: online and offline. In either case, The Things Industries Join Server securely transfers the new root keys to the new Join Server.

Online Rekeying

Device owners configure online rekeying per device by configuring a new JoinEUI on The Things Industries Join Server. Whenever the device joins with a rekeying configuration set, the Join Server sends the rekeying instruction (containing the new JoinEUI and a nonce called security cookie) as part of the join-accept message. The device stores the new JoinEUI, generates the new root keys from the rekeying key and the nonce, and joins again on the new Join Server. Generation of new root keys happens in the secure element; old and new root keys are never exposed.

Online rekeying is specified for LoRaWAN 1.1.1 and is expected to be released mid-2020. However, The Things Industries backports this feature for 1.0.x devices for demonstration purposes.

Offline Rekeying

Devices can also be rekeyed to a new Join Server before they leave the factory or distribution center. This allows device makers and distributors to leverage the physical security aspects of the secure element. Offline rekeying also avoids provisioning keys in factories and distribution facilities.

This solution works by instructing the secure element to rekey with a new JoinEUI and security cookie, just like the online rekeying mechanism described above.

Commercial Model
With this groundbreaking technical solution comes a disruptive commercial model: there is only one billing relationship. Only the device maker pays for secure elements, purchased directly from Microchip or via distributors.

Neither device makers, device owners nor network operators need to setup a billing relationship with The Things Industries: one year device activation service is included in the price of the secure element. Microchip and The Things Industries put a revenue sharing agreement in place to facilitate the complementary one year service. The Things Industries does not charge for handling device activations and does not charge a setup fee during this one year period. We welcome all parties to adopt this solution.

At any time, device root keys can be exported or rekeyed to a new Join Server free of charge. If the device owner is happy with The Things Industries Join Server, the service can be extended for a few cents per year depending on volume.

Conclusion
Although security is at the heart of the LoRaWAN protocol, symmetric root keys are often transferred in insecure ways between device makers, distributors and device owners. Also, root keys are oftentimes stored in the network, leading to security lock-ins with the network operators.

The Things Industries and Microchip offer the most secure LoRaWAN solution in the market, using a combination of a trusted LoRaWAN Join Server and secure elements. This alleviates the need for any key provisioning by device makers, the keys are kept safe with physical protection from tampering, there's more space for application code and the secure element can verify firmware updates.

There is no vendor lock-in: customers can export keys to their own Join Server at any time and the solution is forward-compatible with LoRaWAN standardized online rekeying. Finally, a one year device activation service is included in the price of the secure element, so there are no additional costs.

Come meet The Things Industries and Microchip at The Things Conference, the yearly LoRaWAN ecosystem flagship event, in Amsterdam on January 30th and 31st 2020. Buy tickets now.

Want to know more about the technical solution? Watch the webinar recording:




发布者:
Johan Stokking
Johan Stokking
CTO & Co-founder of The Things Industries and Tech Lead of The Things Network
关注
In my series of technical #LoRaWAN articles, I wrote an article how to secure LoRaWAN devices with secure elements. This article also describes the technicalities of The Things Industries and Microchip Technology Inc. security solution Happy to answer questions!

6 条评论
article-comment__guest-image
登录 发布评论
Sijmen Ruwhof
Sijmen Ruwhof
Freelance IT Security Consultant / Ethical Hacker
Ping @Gerhald Freriks

赞
回复
1 年前

Dimitar Tomov
Dimitar Tomov
Founder at TPM.dev
There is NO #Trust in the #onboarding, only the messaging mechanism is protected, but that is not enough guarantee. Because we do not know if the message payload comes from a #trustworthy system. How did we check the #state of the #device(software) generating the onboarding request? We did not, so we do not know. We only know a device provided the expected key/signed message. We know nothing about the software running in that moment and signing that message. Because after Secure Boot there is no evidence for the state of the software. We do not know if the device is trustworthy to be onboarded. We only know there is a software capable of interfacing to the Secure Element to sign a message, a modified software can do that too. The use of Secure elements create false sense of security during on-boarding without system integrity. Because the TLS Secure communication can not create Trusted System without actual evidence of #SystemIntegrity.

赞
回复
1 年前

Massimo Vecchio
Massimo Vecchio
Senior Researcher of the OpenIoT Unit in FBK
and the price for a minimum order (eg 10 units)?

赞
回复
赞 (2)
回复 (2)
Massimo Vecchio
Massimo Vecchio
Senior Researcher of the OpenIoT Unit in FBK
Johan Stokking: would you be interested in contributing a short article for the next issue of the IEEE IoT newsletter for which I serve as the managing editor? in case, feel free to drop me a line about your intentions. thanks!

赞
回复
1 年前

Johan Stokking
Johan Stokking
CTO & Co-founder of The Things Industries and Tech Lead of The Things Network
Good question, I'll add that. It's € 1,27 per 10 per unit, down to € 0,82 per 2000 per unit

赞
回复
赞 (2)
1 年前

1 年前

Jeroen Hobbelman
Jeroen Hobbelman
Senior Client Engagement Manager at Microchip Technology
Great writing Johan Stokking! You have the gift to make technical hard topics easy to understand!

赞
回复
赞 (4)
1 年前

Johan Stokking的其他文章
3 篇文章
Launching Packet Broker
Launching Packet Broker
F 18, 2020

5 x Why LoRaWAN Roaming Is Not A Solution
5 x Why LoRaWAN Roaming Is Not A Solution
J 10, 2019

领英
© 2020
关于
无障碍模式
用户协议
隐私政策
Cookie 政策
版权政策
品牌政策
访客设置
社区准则
