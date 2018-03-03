# 介绍

[英文原文](http://swarm-guide.readthedocs.io/en/latest/introduction.html)

Swarm是一个分布式存储平台和内容分发服务，它是以太坊*web 3*堆栈的本地基础层服务。Swarm的主要目标是提供一个足够分散和冗余的以太坊公共记录库，特别是存储和分发dapp代码和数据（就像区块链数据一样）。从经济角度来看，它允许参与者高效地共享他们的存储和带宽资源，以向所有参与者提供上述服务。

从最终用户的角度来看，Swarm与WWW没有什么不同，除了上传不是指向特定的服务器。我们的目标是提供一种点对点存储和服务解决方案，该解决方案由于内置激励系统（该系统使用点对点通信技术）可以抵御DDOS、零停机时间、容错和审查制度以及自我维持，使用对等会计并允许交易资源获利。Swarm旨在与以太坊的devp2p多协议网络层以及用于域名解析、服务支付和内容可用性保险的以太坊区块链（后者将于2018年第二季度前在POC 0.4中实施）深度集成。

本文为您提供以下信息：

- 如何运行和配置本地swarm节点
- 如何连接到测试网络
- 如何在swarm中存储和访问内容
- swarm体系结构和概念，如块，哈希和清单
- 与swarm相关的命令行工具
- http swarm代理的API文档
- bzz RPC模块的API文档
- 如何使用以太坊名称服务注册swarm域
- 如何测试，管理日志记录，调试和报告问题

## 背景

Swarm的主要目标是提供一个足够分散和多余的以太坊公共记录存储库，特别是存储和分发Đapp代码和数据以及区块链数据。[请注意，后者不是当前版本的一部分]。

从经济角度来看，它允许参与者高效地共享他们的存储和带宽资源，以向所有参与者提供上述服务。

这些目标需要满足以下设计要求：

- 分布式存储，包容性，电力法律的长尾
- 灵活扩展空间，无需硬件投资决策，无限增长
- 零停机时间
- 不可变的、不可伪造的、可验证的，但可信的存储
- 没有单点故障，故障和攻击恢复能力
- 审查抵制，普遍可用的永久性公共记录
- 激励制度的可持续性
- 有效的市场驱动定价。可交易的内存、持久存储、带宽
- swarm计费协议有效使用区块链
- 基于保证金挑战的担保存储[计划于POC 0.4到2018年第二季度]

## 基础

Swarm客户端是以太坊堆栈的一部分，参考实现是用golang编写的，并可以在go-ethereum存储库下找到。目前在POC（概念验证）版本0.2在所有节点上运行。

Swarm定义的*BZZ子协议*运行在以太坊devp2p网络上。bzz子协议不断变化，只有在2018年第二季度预计POC 0.4时，才会认为有线协议的规范是稳定的。

Swarm群是devp2p网络节点的集合，每个网络在相同的网络ID上运行bzz协议。

Swarm节点也连接到以太坊区块链。运行相同网络ID的节点应该连接到相同的区块链。这种swarm网络由其网络ID来标识，该网络ID是一个任意整数。

Swarm允许*上传和消失*，这意味着任何节点都可以将内容上传到swarm，然后被允许离线。只要节点不会丢失或不可用，由于“同步”过程中节点不断地将可用数据传递给对方，因此内容仍然可以访问。

注意

上传的内容不能保证持久化，直到存储保险被实现（预计POC 0.4到2018年第二季度）。所有参与节点都应该考虑志愿服务，没有任何正式义务，应该按照自己的意愿删除内容。因此，用户在任何情况下都不应将swarm视为安全存储，直到激励体系有效。

注意

Swarm POC 0.2不使用加密。上传敏感数据和私人数据非常令人沮丧，因为无法撤消上传。换句话说，用户应该避免上传未加密的敏感数据

- 没有宝贵的个人内容
- 没有非法、有争议或不道德的内容

Swarm定义了3个重要的概念

- *块*

  数据片段（最大4K），这是swarm中存储和检索的基本单位

- *哈希*

  数据的加密哈希，用作其唯一标识符和地址

- *manifest*

  描述集合的数据结构，允许基于URL访问内容

在本指南中，内容在技术意义上被理解得非常广泛，表示任何数据。Swarm为一段内容定义了一个特定的标识符。该标识符用作内容的检索地址。标识符必须是

- 无碰撞（两个不同的数据块永远不会映射到相同的标识符）
- 确定性的（相同的内容将始终接收相同的标识符）
- 均匀分布

Swarm中标识符的选择是[Swarm哈希中](http://swarm-guide.readthedocs.io/en/latest/architecture.html#swarm-hash)描述的层级Swarm[哈希](http://swarm-guide.readthedocs.io/en/latest/architecture.html#swarm-hash)。以上属性让我们将标识符视为预计可以找到内容的地址。由于哈希可以被认为是无冲突的，所以它们被绑定到一个特定版本的内容，即哈希地址因此在强烈的意义上是不可变的，你甚至不能表达可变内容：“改变内容会改变哈希”。

然而，用户通常使用对数据的发现和/或语义访问，这是由以太坊名称服务（ENS）实现的。ENS支持基于助记符（或品牌）名称的内容检索，非常类似于万维网的DNS，但没有服务器。

参与网络的Swarm节点也有其自己的*基地址（也称为bzzkey）*，该*地址*是作为节点的所谓*swarm base帐户*的以太坊地址（keccak 256bit sha3）哈希派生的。这些节点地址在与数据相同的地址空间中定义一个位置。

当内容上传到swarm时，它被切成块，称为块。每个块在由其swarm哈希定义的地址处被访问。数据块本身的哈希被打包成一个块，而块又拥有自己的哈希。通过这种方式，内容被映射到块树。这种分层的Swarm哈希结构允许在一段内容中对块进行merkle证明，从而为swarm提供完整性保护的随机访问（大型）文件（允许例如在流视频中安全地跳过）。

当前版本的swarm实现了*严格内容寻址的分布式哈希表*（DHT）。这里的“严格内容寻址”意味着最靠近块的地址的节点不仅提供关于内容的信息，而且实际上托管数据。（请注意，虽然它是协议的一部分，但我们不能保证它会被保留下来，这是一个值得再次说明的警告：不能保证持久性和持久性）。换句话说，为了检索一段内容（作为较大集合/文档的一部分），块在存储/上传时必须从上传者到达存储者，并且还必须在检索/下载时返回给请求者。这种可行性的假设前提是，任何节点（上传者/请求者）都可以“到达”任何其他节点（存储者）。这种假设是通过特殊的*网络拓扑*来保证的（称为*kademlia*），它提供（非常低）恒定时间进行对数网络大小的查找。

注意

swarm中没有删除/移除这回事。一旦上传数据，您就无法撤销数据。

节点在检索时缓存它们传递的内容，从而导致自动缩放弹性云：流行（经常访问）的内容在整个网络中被复制，从而减少其检索延迟。缓存还会导致*资源利用率达到最高*，同时节点将通过数据通过它们填充专用存储空间。如果达到容量，垃圾收集过程将清除最少访问的块。因此，不受欢迎的内容最终会被删除。储存保险（将在2018年第二季度预计采用POC 0.4）将用于保护重要内容免受这种命运的影响。

Swarm内容访问以清单(manifest)的概念为中心。清单文件描述文档集合，例如，

- 一个文件系统目录
- 数据库的索引
- 一个虚拟服务器

清单指定允许基于URL的内容检索的路径和相应的内容哈希。因此，清单可以为（静态）资产（包括使用静态JavaScript的动态内容）定义路由表。这提供了*虚拟主机*，存储整个目录或web（3）站点的功能，类似于www，但没有服务器。

您可以在[Architecture中](http://swarm-guide.readthedocs.io/en/latest/architecture.html#architecture)阅读更多关于这些组件的内容。

## 关于

### 这个文件

本文档的源代码可在<https://github.com/ethersphere/swarm-guide>找到 。各种格式的最新丛书可在旧网站 <http://ethersphere.org/swarm/docs>上找到以及在swarm bzz://swarm/guide

### 状态

Swarm的状态证明了玩具网络上测试的vanilla原型的概念。这个版本是POC 0.2.5

注意

Swarm是实验性代码，并且在野外未经测试。谨慎使用。

### 协议

### 积分

Swarm由Ethersphere编写（ΞTHΞRSPHΞЯΞ）https://github.com/ethersphere

Swarm背后的团队：

- ViktorTrón@zelig
- DánielA. Nagy @nagydani
- Aron Fischer @homotopycolimit
- 尼克约翰逊@Arachnid
- ZsoltFelföldi@zsfelfoldi

Swarm由以太坊基金会资助。

特别感谢

- Felix Lange，Alex Leverington发明并实现了devp2p / rlpx;
- Jeffrey Wilcke和继续支持，测试和指导的团队;
- Gavin Wood和Vitalik Buterin提供了这个愿景;
- Alex Van der Sande，Fabian Vogelsteller，Bas van Kervel和Mist团队
- Nick Savers，Alex Beregszaszi，Daniel Varga，Juan Benet为鼓舞人心的讨论和想法
- 橙色休息室研究小组的参与者
- 用于java实现的Roman Mandeleil和Anton Nashatyrev
- 例如Igor Sharudin dapps
- 社区贡献者提供反馈和测试

### 社区

日常开发和讨论正在各种gitter渠道中进行：

- <https://gitter.im/ethereum/swarm>：关于swarm dev的一般公共聊天室
- <https://gitter.im/ethersphere/orange-lounge>：我们的阅读/写作/工作组和研发环节
- <https://gitter.im/ethereum/pss>：关于Swarm上的邮政服务 - 具有确定性路由的消息传递
- <https://gitter.im/ethereum/swatch>：可变比特率媒体流和多播/广播解决方案

有关以太坊subreddit的Swarm讨论：[http](http://www.reddit.com/r/ethereum)：[//www.reddit.com/r/ethereum](http://www.reddit.com/r/ethereum)

### 报告错误并做出贡献

问题仅在github和github上进行跟踪。与swarm相关的问题和PR用swarm标记：[https](https://github.com/ethereum/go-ethereum/labels/swarm)：[//github.com/ethereum/go-ethereum/labels/swarm](https://github.com/ethereum/go-ethereum/labels/swarm)

报告问题时请包括提交和分支。

Pull请求应该默认在主分支（边缘）上提交。

### 路线图和资源

Swarm路线图以及功能和POC系列的暂定计划可在wiki上找到：[https](https://github.com/ethereum/go-ethereum/wiki/swarm-roadmap) ：[//github.com/ethereum/go-ethereum/wiki/swarm-roadmap ](https://github.com/ethereum/go-ethereum/wiki/swarm-roadmap)[https://github.com/ethereum/go-ethereum/wiki /群-POC系列](https://github.com/ethereum/go-ethereum/wiki/swarm---POC-series)

该*群的主页*是通过群访问在BZZ：//群或网关http://swarm-gateways.net/bzz:/swarm/

群组页面还包含与Swarm相关的会谈列表（视频录像和幻灯片）。

您还可以在那里找到（前两个）醚球橙色纸。

公共门户是：

- <http://swarm-gateways.net/>
- <http://web3.download/>
- <http://ethereum-swarm.net/>

Swarm testnet监视器：[http](http://stats.ens.domains/)：[//stats.ens.domains/](http://stats.ens.domains/)

源代码位于<https://github.com/ethereum/go-ethereum/>

示例dapps位于<https://github.com/ethereum/swarm-dapps>

本文档来源<https://github.com/ethersphere/swarm-guide>

[下一个 ](http://swarm-guide.readthedocs.io/en/latest/installation.html)[ 以前](http://swarm-guide.readthedocs.io/en/latest/index.html)
