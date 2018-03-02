# SWARM - IPFS
[英文原文](https://github.com/ethersphere/go-ethereum/wiki/IPFS-&-SWARM)  

关于以太坊群和IPFS之间的相似/差异以及包括各种协作水平在内的未来计划的问题经常出现。我会努力通过给出一个相当详细的答案来公平地对待这个巨大的兴趣（当我更多地使用我的分散的未经编辑的笔记时，随着时间的流逝，这篇文章将被大量编辑并提供更多的技术细节）。

**免责声明**：由于我是swarm的主要作者，本文档是从我的个人角度撰写的，因此不可避免地偏离了我的观点。这反映了我对IPFS的理解，这是不完整或可能不正确的，并且将来可能会发生变化。

Swarm和IPFS

- 这两个项目可以分为“相同，相同，但不同” - 大图景观和高层次视觉将下一个webz各种横幅下的两个项目连接起来。
- 这两个项目是密切的盟友，对技术和营销方面的合作非常开放。
- 为了推动两个项目高度重叠的议程，并最有效地从两个项目资源中受益，提出了实际技术的不同程度的整合。

在这份文件中，总结了这两个项目之间令人惊叹的激励背景和高度相似之处后，我将讨论这两个项目在组织，思想和技术方面的差异。最后，我将提出多种潜在的协作和技术整合的途径，说明每个项目的优缺点。

## 相似

Swarm和IPFS都为下一代互联网未来的高效分散存储层提供了全面的解决方案。高层次的目标和技术都非常相似。swarm和IPFS系统都渴望提供：

- 通用的分散式分布式存储解决方案。
- 内容交付协议。

这是通过创建一个协作节点网络来实现的，每个节点都运行一个符合用于存储和检索任意内容的严格定义的通信协议的客户端。利用个人参与者的剩余存储和带宽，网络节点共同提供无服务器托管平台。

两个项目：

- 渴望为参与节点提供一层（货币）激励，鼓励用户进行健康的运营和/或保险/再保证:)，并为用户提供使用其资源的补偿。
- 使用某种块存储模式，在这种模式下可以将较大的文档切碎，并且可以并行提取这些文件。
- 通过内容寻址提供完整性保护（也适用于加密的部分内容）。
- 这两个项目都提供了URL方案和分散的域名解析。
- 将文件系统目录透明且高效地映射到存储对象集。

因此，两者原则上都非常适合替换当前破碎的互联网的数据层，并且作为web3愿景的存储层（与其他同类尝试一起，特别是zeronet、Maidsafe、i2p、storj等）与通常的必须 - 具有分布式文档存储的特性。

- 低延迟检索。
- 高效的自动缩放（内容缓存）。
- 可靠的容错操作，可抵抗节点的断开，冗余存储的间歇性可用性。
- 零停机时间。
- 审查性。
- 可能永久版本化的内容存档。

## 差异

这两个项目设计中的细微但重要的差异可能会使两个项目保持稳定并在他们自己的相对轨道上分开。由于大局和高层次的解决方案如此神奇地结合在一起，因此可以在其他地方找到差异。我会将他们分组在下面：

（A）发展状况/知名度/用户群。（B）哲学/伦理/政治。（C）较低层次的技术。

### （A）状态

IPFS在代码成熟度、扩展性、采用率、社区参与度以及与专用开发人员社区的互动方面进一步提升。然而，swarm在以太坊生态系统中的地位转化为固有的基础设施优势。

- IPFS和swarm都是完全开源的，参考实现是用Go语言编写的（swarm有一个过时的java版本，IPFS有javascript）
- 在生产发布前，IPFS和swarm都是alpha软件
- IPFS已被证明可以相当合理地扩展，swarm刚刚开始在更大规模上进行测试（尽管swarm建立在devp2p之上，以太坊 p2p网络层本身几乎不需要测试）
- IPFS已经开放了更长时间的产品，并且招募了一个体面的用户群，Swarm还没有真正推出，POC发布系列今年刚刚开始
- IPFS有很多材料、视频、优秀的文档和论文。Swarm有两个设计小组、分散的文档和两篇论文（ethersphere orange paper系列的前两篇），介绍4月中旬发布的激励措施！一个群体指南正在制作中
- IPFS有一个工作网络（没有激励），Swarm最近刚刚推出了开发者测试网的第一阶段
- IPFS已经成为真实世界企业的工作解决方案，并得到了热情的用户群的支持
- swarm受益于与以太坊强的有力协同，其有前途的生态系统，用户的现场网络以及来自非盈利基金会的可靠持续资助形式的组织背景。IPFS也拥有可靠的资金来源，也得到了以太坊成员的使用和支持。

尽管来自社区的强烈声音不赞同重新发明车轮，但作为全面内部解决方案的swarm在最艰难的时期遭受并幸存下来：由于基金会的财政困难，发展速度放缓，2015年秋季的紧缩措施得以实施。2016年的有利环境再次使我们的原始愿景切合实际，并且发展出现了新的增长可能通过扩大开发团队进一步证明。我确信，构建我们自己的定制系统是一个成功的门票，它可以使web3的这一关键组成部分与以太坊（EVM）及其治理和资金与以太坊相结合，迅速灵活地适应和共同发展。

swarm的特权基础架构/组织地位本身不应当成为web3进入公众时可用备选方案中主要采用的决定性因素。我的意图是用户的选择基于特定技术的固有优点，并且选择不会受到以太坊任意选择/限制（例如，使用devp2p网络层，见下文）的不当限制。

相反，通过对公众发布越来越多的关于我们的路线图的讨论，我们希望能够平衡IPFS的优势，因为它们有更长的时间。如果您现在需要技术，成熟度在选择技术方面有一个合适的位置，所以这里的讨论与具有中长期计划的开发人员相关。希望在这两个项目都已投入生产就绪候选版本的时候，本部分的差异变得微不足道，让特性，效率和易用性成为评估的主导。

### （B）哲学

没有严重的不匹配，但有足够的差异来预测和证明这两个项目的平行演进。

**建议**在世界上许多地方，信息版权的卡特尔或信息自由的倡导者都有资源追随你。如果完全透明和不受阻碍地支持信息自由的原因对你来说很重要（无论是出于道德或机会主义的理由），考虑支持swarm。

Swarm非常特别地意味着成为以太坊生态系统的一部分。从一开始，它一直被认为是下一个webz的三大支柱之一，与以太坊和Whisper一起定义了web3组件的神圣三位一体。其发展受以太坊的需求（最重要的是托管dapps，合约源码/元数据以及区块链/状态等）的指导和启发。它是在以太坊的能力（包括潜在的限制）背景下开发的，并且只要基金会提供的资金能够保证迎合以太坊生态系统中出现的特定用途。

同时IPFS是一个统一的解决方案，可以集成许多现有的协议。在这方面

Swarm有非常强烈的反审查立场。它激励内容不可知的集体存储（块传播/分配方案）。通过混淆和双重掩蔽（目前尚未完成）的组合，实现合理的可否认性以及难以置信的责任。IPFS认为，更广泛的采用通过提供黑名单工具来保证对审查制度进行妥协，但使用这些工具完全是自愿的。

### （C）技术性


- swarm的核心存储组件作为不可变的内容寻址chunkstore而不是通用的DHT（分布式哈希表）。
- 你可以上传到swarm，使用它作为云主机，在ipfs你只能注册/发布已经在你的硬盘上的内容。
- 这两个系统使用不同的网络通信层和对等管理协议
- swarm与以太坊区块链深度整合，激励体系受益于智能合约以及半稳定的对等池(peerpool)。Filecoin计划通过IPFS激励网络，旨在使用其altcoin区块链，并将可检索性证明作为挖掘的一部分。这些选择的后果是深远的。

这些属性在低层次差异中扮演着重要角色。

#### devp2p vs libp2p：

Swarm使用以太坊的devp2p（协议多路复用、通过帧、加密、认证、握手和协议消息API标准，对等连接管理支持，节点发现进行消息交织），并充分利用其强壮性，并最显着地继承了其审计和广泛赞誉）安全属性。

IPFS使用libp2p网络层，这是一种类似的先进通用p2p解决方案。这是一个基于主流bittorrent dht实施的内部开发，它经受住了时间的考验，但是通过最先进的选择改进。为了历史的准确性，devp2p似乎深受libp2p（2014年11月柏林Devcon0 IPFS谈话以及Juan Benet（IPFS）与Gav Wood＆Alex Leverington（ETH）之间的早期交流）的启发。

以太坊devp2p通过TCP提供半永久连接池。由于以太坊生态系统的结果，许多节点长期承诺。这些属性在激励和存储/检索中都支持相对较新颖的解决方案。

Swarm就像IPFS一样，基于xor对数距离实现基于密钥的路由（适用于node-id和内容散列的共享地址空间），但swarm使用转发kademlia的混合风格：而不是迭代查找和过滤请求的发起者依赖于较大的对等池，swarm递归地将连续的查找步骤外包并仅使用较小的活动连接池。该算法的其他方面对于本说明的范围过于技术性。

Swarm是内容寻址块存档，而IPFS更类似于bittorrent，其内容是DHT（分布式散列表）。DHT是分布式存储解决方案用于查找内容寻址数据的分布式索引。虽然这些数据通常是关于下载内容的（IPFS）元数据，在swarm中是其内容本身。请注意，DHT只是IPFS中的一种可用协议（IPFS的分层设计是高度模块化的）。这种对不变内容的严格解释解决了块存储是swarm的一个主要设计特性，它与devp2p一起允许swarm执行：

- 高效的配对链外计费（用于公平激励带宽以及即时结算受保存储）
- 更流畅的自动缩放流行内容
- 准匿名浏览
- 对完整性（在很少访问的内容上）进行有效的集体审核

Juan在这里评论说，kademlia DHT只是IPFS中的一个可选路由组件，它实际上可以完成所有这些工作。（我的家庭作业，以弄清楚是否和如何）。

我相信IPFS和swarm将提供具有完整性保护（甚至是部分内容）的加密内容的流式传输。

#### 激励

Filecoin是IPFS的姊妹项目，它为IPFS增加了激励层，并依靠自己的altchain。在文件币区块链上检索“挖矿”的证据是一种向存储器提供持续补偿以保留内容的方案。作为工作证明任务的一部分的随机审核会以可检索性的证据作出回应，并且获胜的矿工将得到相应的补偿。这种制度具有固有的局限性：IPFS只能实施积极的激励措施，并依靠集体责任。

Swarm利用智能合约的全部功能来处理注册节点并存入利息。这允许采取惩罚措施作为威慑。Swarm提供了一个追踪责任的计划，使得存储者为特定内容单独负责。

IPFS无法保证存储，而swarm强制内容不可知的行为，并提供由用户灵活调整的内容特定级别的安全性。Juan在这里评论说，他们一直在向Filecoin增加一些东西，这也将有一个智能合约区块链，但这些还没有发表的想法和计划。

作为内容保险的一部分（一个关键特征），Swarm将对区块链上的极少访问内容实施高效的自动化集体审计，并提供最后诉讼。使用配对会计协议和延迟微支付链外swarm可在保证安全性的同时大幅节省交易成本。IPFS + filecoin对托管挖矿的竞争性证据的依赖意味着过度使用区块链以及对正常运营进行固有冗余的资源利用。

通过配对核算、延期付款和集体链外审计，swarm都依赖于区块链的重要性，这限制了其仅用于注册和最后诉讼。

#### Manifests

最后，swarm的“Manifests”概念（具有完整性保护的通用路由表/键值索引）允许

- 在云上建模分层文件系统
- 无服务器的服务器，具有路由表和元数据原理系统（内容类型、加密和保险信息等）
- 在swarm内部实现任意的DHT，所以它可以支持“侧链”或传统webapps的db组件（如mysql在LAMP栈等）

Juan在这里评论IPFS可以做所有这些事情。无可否认，这两个项目都没有工作代码或文档，这里有点乱码......

## 整合与协作

虽然大局良好比对，有两个项目整合的挑战。概述了几个建议，分别提供每个建议的优缺点。

IPFS的胡安长期以来一直是以太坊活动的常客，这两个社区似乎相互赞赏。在技术细节和通用目标方面这两种努力的相似性导致许多人质疑这两种努力是否可以并且应该以某种方式统一或协调。这导致一些人认为swarm“仅仅是任意分散存储之上的激励层”。这种误解也可能是因为在去年秋天的艰难时期，基金会必须限制项目的范围，而Vitalik（很明智地）认为开发工作应该转向包括激励措施，但不包括研发部分由IPFS负责照料。这些限制的动机不再存在。

### 将IPFS整合到swarm中

经过大量的思考，并通过IPFS文档和代码，我觉得自己承诺达到某种程度的集成。接下来是各种各样的初步清单 - 公然相当推测的方法。

- 作为其网络协议的云存储抽象的实现，将IPFS插件实现到swarm中

  - 优点：
    - 实现这一点需要最少的开发工作
    - 正如我现在看到的，这种方法将允许群体激励系统驱动IPFS节点。
    - 这种方法提供了测试两种系统效率的正确性和基准的方法
  - 缺点：
    - 不清楚在IPFS类型路由和检索上执行的基于群块的存储是否会有任何性能增益
    - 目前还不清楚IPFS之上的以太坊激励措施是否会影响IPFS用户群等。
    - 不清楚是否使用IPFS libp2p（与devp2p一起）会增加安全风险并且或者使网络流量监控/负载平衡更难
    - 不清楚这个（ab）使用IPFS组件是否适合协调IPFS和Swarm并行开发的现实方式（这里我正在考虑规划软件更新等）。胡安在这里评论说，他认为这会很好;）

  由于其最佳的收益/努力比率，该解决方案很可能被追求

- 制定并实施更适合IPFS的更简单的以太坊激励层。

  - 优点：
    - IPFS的整个机制的完整性得以保留
    - IPFS和Swarm由不同时间表分开的团队进行的并行开发使得保持长期比第一个提案更现实一些
  - 缺点：
    - 这样一个激励层（IPFS）尚不存在，坦率地说，我个人很难很快看到它的正确完成，
    - 我对IPFS的一个可行的激励计划的粗略想法的夫妇将需要在永久网络激励系统的期望规范上妥协

- lulz一些疯狂的想法：

  - 将文件币用作swarm侧链
  - 考虑将以太坊迁移到libp2p并使用IPFS的所有荣耀

- 将swarm集成到IPFS中作为子协议

  - 优点：
    - 它有点酷
  - 缺点：
    - 真正的好处不明

- 将IPFS挂载到devp2p的传输层（RLPx）上 - Juan最近向我提出了这个建议，但我们并没有完全捕获并调查这个先决条件或后果

  - 优点：
    - IPFS可能会被用于其所有的荣耀
    - devp2p的完整性将被保留
  - 缺点：
    - ??

- 思想的交叉化和抢夺代码：由于所有上述更强有力的合作形式都失败了，我仍然看到可能的协同效应，这两个项目的共同利益

  - 执行允许现在和未来快速采用相关想法，而不需要附加任何字符串
  - IPFS和以太坊都是非常性感的项目，它们加强了彼此的公关并增加了成功的机会
  - 鉴于crypto 2.0 / web3领域令人难以置信的创新动力和创新速度，上述一些提议很可能会改变，新的机会也会出现。

  总而言之，继续成为朋友并以任何（道德上合理的）方式利用彼此的资源来促进免费的，私人的，资源节约型的无服务器网站的共同目标具有坚实的基础。

下一步4月27 - 28日柏林IPFS-Swarm协同纠缠和行星间异花授粉

所有的意见和错误是我的，我欢迎反馈。

# 资源

原始的stackoverflow问题：[http](http://ethereum.stackexchange.com/questions/2138/what-is-the-difference-between-swarm-and-ipfs) : [//ethereum.stackexchange.com/questions/2138/what-is-the-difference-between-swarm-and-ipfs](http://ethereum.stackexchange.com/questions/2138/what-is-the-difference-between-swarm-and-ipfs)

## reddit上的IPFS / SWARM：

- <http://ethereum.stackexchange.com/questions/2138/what-is-the-difference-between-swarm-and-ipfs>
- <https://www.reddit.com/r/ethereum/comments/4bms6v/what_is_the_difference_between_swarm_and_ipfs/>
- <https://www.reddit.com/r/ethereum/comments/3npsoz/ethereum_ipfs_and_filecoin/>
- <https://www.reddit.com/r/ethereum/comments/3hbqbv/ipfs_vs_swarm/>
- <https://www.reddit.com/r/ethereum/comments/2z7iyr/a_polite_discussion_about_symbiosis_between_ipfs/>

## IPFS

- [https://ipfs.io](https://ipfs.io/)
- DEVCON1通过Juan Benet谈话 - <https://www.youtube.com/watch?v=ewpIi1y_KDc>
- <https://github.com/ipfs/specs/>
- <https://github.com/ipfs/faq/issues/>
- <https://github.com/ipfs/specs/blob/master/libp2p/>
- <https://github.com/ipfs/specs/blob/master/libp2p/4-architecture.md>

## Swarm

### ÐΞVcon在群组会谈

- [ViktorTrón，Daniel A. Nagy：Swarm - 以太坊ÐΞVcon-1在youtube上发言](https://www.youtube.com/watch?v=VOC45AgZG5Q)
- [丹尼尔A.纳吉：保持公共记录安全无障碍 - 以太坊ÐΞVcon-0在youtube上发言](https://www.youtube.com/watch?v=QzYZQ03ON2o&list=PLJqWcTqh_zKEjpSej3ddtDOKPRGl_7MhS)

### ETHERSPHERE橙色纸

- ViktorTrón，Aron Fischer，Daniel A Nagy和ZsoltFelföldi：交换，发誓和诈骗：群体奖励制度。[pdf](http://52.70.20.40:32200/bzz:/ethersphere/orange-papers/1/swap-swear-and-swindle.pdf) | [html](http://52.70.20.40:32200/bzz:/ethersphere/orange-papers/1/swap-swear-and-swindle.html) | [bibtex条目](http://52.70.20.40:32200/bzz:/ethersphere/orange-papers/1/swap-swear-and-swindle.bib)
- ViktorTrón，Aron Fischer，Daniel Varga：防粉碎：通过屏蔽审计秘密散列来保护群体的可审计存储。[pdf](http://52.70.20.40:32200/bzz:/ethersphere/orange-papers/2/smash.pdf) | [html](http://52.70.20.40:32200/bzz:/ethersphere/orange-papers/2/smash.html) | [bibtex条目](http://52.70.20.40:32200/bzz:/ethersphere/orange-papers/2/smash.bib)

### 代码和状态

- [资源](https://github.com/ethereum/go-ethereum/tree/swarm)
- [github上的问题](https://github.com/ethereum/go-ethereum/labels/swarm)
- [发展路线图](https://github.com/ethereum/go-ethereum/wiki/swarm-roadmap)

## 跟随群

- [@在这里推特](https://twitter.com/ethersphere)
- [gitter swarm房间](https://gitter.im/ethereum/swarm)
- [swarm on swarm：bzz：// swarm](http://swarm-gateways.net/bzz:/swarm/) public gateway（感谢@TerekJudi | @uwaterloo）一旦testnet公开
