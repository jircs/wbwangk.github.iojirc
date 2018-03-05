# 连接到Swarm（高级）

这些说明将首先阐述如何启动一个本地、私有、个人（单个）swarm。使用它来熟悉客户端的功能; 上传、下载以及http代理。

## 准备

要启动一个基本的swarm节点，我们必须在私有网络上开始一个空的数据目录，然后将swarm守护程序连接到这个geth实例。

首先放置一个空的临时目录作为数据存储

注意

如果您按照本指南中的安装说明进行操作，则可以在$GOPATH/bin目录中找到您的可执行文件。确保将文件移动到可执行文件$PATH中，或者在其上包含$GOPATH/bin目录。

```
DATADIR=/tmp/BZZ/`date +%s`
mkdir $DATADIR
```

然后使用此目录创建一个新帐户

```
geth --datadir $DATADIR account new
```

系统会提示您输入密码：

```
Your new account is locked with a password. Please give a password. Do not forget this password.
Passphrase:
Repeat passphrase:
```

一旦你指定了密码（例如MYPASSWORD），输出将是一个地址 - swarm节点的基地址。

```
Address: {2f1cd699b0bf461dcfbf0098ad8f5587b038f0f1}
```

我们将其保存在名称 `BZZKEY`下

```
BZZKEY=2f1cd699b0bf461dcfbf0098ad8f5587b038f0f1
```

完成这些准备工作后，我们现在可以启动我们的swarm客户端。在下面我们将详细介绍几种您可能想要使用swarm的方法。

- 连接到没有区块链的swarm测试网
- 连接到swarm测试网和连接到Ropsten区块链
- 以单点模式使用swarm，为了本地测试
- 启动一个私有swarm
- 使用私有Swarm测试SWAP会计

## 连接到swarm测试网

注意

重要提示：自动连接到测试网络目前无法为所有用户正常工作。这个问题现在正在解决。同时，请手动添加一些enodes来引导您的节点。请参阅下面的“手动添加enodes”。

Swarm需要一个以太坊区块链

- 使用以太坊名称服务（ENS）合约进行域名解析。
- 激励（例如：SWAP）

如果您不关心域名解析并在没有SWAP的情况下运行您的swarm（默认），则不需要连接到区块链。`swarm`默认情况下会尝试通过`geth`IPC的端点连接到区块链（通常为$DATADIR/geth.ipc）。因此，要启动`swarm`没有域名解析，`--ens-api`选项应设置为`""`（空字符串）。只有在SWAP被使能的情况下才应设置`--swap-api`标志。

### 仅连接到swarm（无区块链）

如上所示建立你的环境，即确保你有一个数据目录。

注意

即使你不需要以太坊区块链，你也需要geth来生成一个swarm账户（$BZZKEY），因为这个账户决定了你的swarm节点将要使用的基地址。

在以下示例中，swarm的输出将写入日志文件。这些说明是为了熟悉swarm并使用语法来加快这个文档（例如自动输入密码）。请注意，密码将在您的shell的历史记录中保持纯文本。

```
swarm --bzzaccount $BZZKEY \
       --datadir $DATADIR \
       --ens-api '' \
       2>> $DATADIR/swarm.log < <(echo -n "MYPASSWORD") &
```

该`swarm`守护进程会寻找并连接到其他群节点。它独立管理自己的对等连接，而不依赖`geth`。

### 将群集与Ropsten testnet区块链一起使用

如果您还没有帐户，请运行

```
geth --datadir $DATADIR --testnet account new
```

运行一个geth节点连接到Ropsten测试网络

```
nohup geth --datadir $DATADIR \
       --unlock 0 \
       --password <(echo -n "MYPASSWORD") \
       --testnet \
        2>> $DATADIR/geth.log &
```

然后启动swarm; 将它连接到geth节点（-ens-api）。

```
swarm --bzzaccount $BZZKEY \
       --datadir $DATADIR \
       --keystore $DATADIR/testnet/keystore \
       --ens-api $DATADIR/testnet/geth.ipc \
       2>> $DATADIR/swarm.log < <(echo -n "MYPASSWORD") &
```

### 手动添加enodes

最终自动节点发现将适用于swarm节点。在此之前，您可以通过使用`admin.addPeer`控制台命令手动添加一些对等方(peer)来启动连接过程。

```
geth --exec='admin.addPeer("ENODE")' attach ipc:/path/to/bzzd.ipc
```

ENODE是以下之一：

```
enode://01f7728a1ba53fc263bcfbc2acacc07f08358657070e17536b2845d98d1741ec2af00718c79827dfdbecf5cfcd77965824421508cc9095f378eb2b2156eb79fa@40.68.194.101:30400
enode://6d9102dd1bebb823944480282c4ba4f066f8dcf15da513268f148890ddea42d7d8afa58c76b08c16b867a58223f2b567178ac87dcfefbd68f0c3cc1990f1e3cf@40.68.194.101:30427
enode://fca15e2e40788e422b6b5fc718d7041ce395ff65959f859f63b6e4a6fe5886459609e4c5084b1a036ceca43e3eec6a914e56d767b0491cd09f503e7ef5bb87a1@40.68.194.101:30428
enode://b795d0c872061336fea95a530333ee49ca22ce519f6b9bf1573c31ac0b62c99fe5c8a222dbc83d4ef5dc9e2dfb816fdc89401a36ecfeaeaa7dba1e5285a6e63b@40.68.194.101:30429
enode://756f582f597843e630b35371fc080d63b027757493f00df91dd799069cfc6cb52ac4d8b1a56b973baf015dd0e9182ea3a172dcbf87eb33189f23522335850e99@40.68.194.101:30430
enode://d9ccde9c5a90c15a91469b865ffd81f2882dd8731e8cbcd9a493d5cf42d875cc2709ccbc568cf90128896a165ac7a0b00395c4ae1e039f17056510f56a573ef9@40.68.194.101:30431
enode://65382e9cd2e6ffdf5a8fb2de02d24ac305f1cd014324b290d28a9fba859fcd2ed95b8152a99695a6f2780c342b9815d3c8c2385b6340e96981b10728d987c259@40.68.194.101:30433
enode://7e09d045cc1522e86f70443861dceb21723fad5e2eda3370a5e14747e7a8a61809fa6c11b37b2ecf1d5aab44976375b6d695fe39d3376ff3a15057296e570d86@40.68.194.101:30434
enode://bd8c3421167f418ecbb796f843fe340550d2c5e8a3646210c9c9d747bbd34d29398b3e3716ee76aa3f2fc46d325eb685ece0375a858f20b759b40429fbf0d050@40.68.194.101:30435
enode://8bb7fb70b80f60962c8979b20905898f8f6172ae4f6a715b89712cb7e965bfaab9aa0abd74c7966ad688928604815078c5e9c978d6e57507f45173a03f95b5e0@40.68.194.101:30436
```

## 单例模式Swarm

要以单例模式启动，请使用`--maxpeers 0`参数启动geth 

```
nohup geth --datadir $DATADIR \
       --unlock 0 \
       --password <(echo -n "MYPASSWORD") \
       --verbosity 4 \
       --networkid 322 \
       --nodiscover \
       --maxpeers 0 \
        2>> $DATADIR/geth.log &
```

和启动swarm; 将它连接到geth节点。为了保持一致性，我们使用与geth相同的网络ID 322。

```
swarm --bzzaccount $BZZKEY \
       --datadir $DATADIR \
       --ens-api $DATADIR/geth.ipc \
       --verbosity 4 \
       --maxpeers 0 \
       --bzznetworkid 322 \
       2>> $DATADIR/swarm.log < <(echo -n "MYPASSWORD") &
```

注意

在这个例子中，运行geth是可选的，并非严格需要。要运行无geth的swarm，只需将ens-api标志更改为`--ens-api ''`（空字符串）。

在这种冗长的级别上，你应该看到很多（！）的输出在日志文件中累积。你可以通过使用命令`tail -f $DATADIR/swarm.log`和`tail -f $DATADIR/geth.log`来查看输出。注意：如果从另一个终端执行此操作，您将不得不手动指定路径，因为不会设置$DATADIR。

您可以在不通过控制台重新启动geth和swarm的情况下更改详细级别：

```
geth --exec "web3.debug.verbosity(3)" attach ipc:$DATADIR/geth.ipc
geth --exec "web3.debug.verbosity(3)" attach ipc:$DATADIR/bzzd.ipc
```

注意

按照这些说明，您现在正在运行一个本地swarm节点，没有连接到任何其他节点。

如果你想在一个脚本中运行所有这些指令，你可以用类似的方法来包装它们

```
#!/bin/bash

# Working directory
cd /tmp

# Preparation
DATADIR=/tmp/BZZ/`date +%s`
mkdir -p $DATADIR
read -s -p "Enter Password. It will be stored in $DATADIR/my-password: " MYPASSWORD && echo $MYPASSWORD > $DATADIR/my-password
echo
BZZKEY=$($GOPATH/bin/geth --datadir $DATADIR --password $DATADIR/my-password account new | awk -F"{|}" '{print $2}')

echo "Your account is ready: "$BZZKEY

# Run geth in the background
nohup $GOPATH/bin/geth --datadir $DATADIR \
    --unlock 0 \
    --password <(cat $DATADIR/my-password) \
    --verbosity 6 \
    --networkid 322 \
    --nodiscover \
    --maxpeers 0 \
    2>> $DATADIR/geth.log &

echo "geth is running in the background, you can check its logs at "$DATADIR"/geth.log"

# Now run swarm in the background
$GOPATH/bin/swarm \
    --bzzaccount $BZZKEY \
    --datadir $DATADIR \
    --ens-api $DATADIR/geth.ipc \
    --verbosity 6 \
    --maxpeers 0 \
    --bzznetworkid 322 \
    &> $DATADIR/swarm.log < <(cat $DATADIR/my-password) &


echo "swarm is running in the background, you can check its logs at "$DATADIR"/swarm.log"

# Cleaning up
# You need to perform this feature manually
# USE THESE COMMANDS AT YOUR OWN RISK!
##
# kill -9 $(ps aux | grep swarm | grep bzzaccount | awk '{print $2}')
# kill -9 $(ps aux | grep geth | grep datadir | awk '{print $2}')
# rm -rf /tmp/BZZ
```

## 运行一个私有swarm

您可以将您的单例节点扩展到私有swarm。首先按照上述说明启动多个`swarm`实例。您可以保留相同的数据目录，因为所有节点特定的数据都将驻留在下级目录`$DATADIR/bzz-$BZZKEY/`中 。确保您为要运行的每个swarm实例创建一个帐户。为简单起见，假设您运行一个geth实例，并且每个swarm守护进程都通过ipc连接到它，如果它们位于同一台计算机（或本地网络）上，则可以使用http或websockets作为eth网络通信的传输。

一旦你的`n`节点是启动和运行，您可以在swarm控制台中使用`admin.nodeInfo.enode`（或cleaner：`console.log(admin.nodeInfo.enode)`）列出所有的enodes。运行：

```
geth --exec "console.log(admin.nodeInfo.enode)" attach /path/to/bzzd.ipc
```

然后你可以通过注入`admin.addPeer(enode)`swarm控制台来连接每个节点和一个特定节点（称为bootnode）（这与你`static-nodes.json`为devp2p 创建一个文件具有相同的效果：

```
geth --exec “admin.addPeer（$ BOOTNODE ）” attach /path/to/bzzd.ipc

```

幸运的是，还有一个更简单的捷径，即在启动Swarm时添加标志`--bootnodes $BOOTNODE`。

这些管理连接的繁琐步骤只需要执行一次。如果您第二次调出相同的节点，之前的peer会被会被记住并连接。

注意

请注意，如果您在同一个实例上本地运行多个swarm守护进程，则可以使用相同的数据目录（$DATADIR），每个swarm将自动使用与bzzaccount相对应的自己的子目录。这意味着您可以将所有密钥存储在一个密钥存储目录中：$DATADIR/keystore。

如果您想在本地运行多个节点，并且您位于防火墙后面，则使用外部IP的节点之间的连接可能无法工作。在这种情况下，您需要用`[::]`（表明localhost）将enode中的IP地址替换。

要列出本地swarm的所有enode：

```
for i in `ls $DATADIR | grep -v keystore`; do geth --exec "console.log(admin.nodeInfo.enode)" attach $DATADIR/$i/bzzd.ipc; done > enodes.lst
```

要将IP更改为localhost：

```
cat enodes.lst | perl -pe 's/@[\d\.]+/@[::]/' > local-enodes.lst
```

注意

如果您只想连接到swarm testnet，则本节中的步骤不是必需的。由于默认情况下testnet的bootnode已设置，您的节点将有一种方法来引导其连接。

## 测试SWAP

注意

重要！请仅在私有网络上测试SWAP。

### 在您的私有区块链上测试SWAP。

SWarm记帐协议（SWAP）默认是禁用的。设置`--swap`标志来启用它。如果它被设置为true，那么SWAP将被启用。但是，激活SWAP不仅仅需要添加-swap标志。这是因为它需要部署支票本合约，为此我们需要在主账户中拥有以太币。我们可以通过挖矿或者通过在一个定制创世区块中简单地发给自己一些以太币来获得一些以太币。

#### 定制创世区块

打开一个文本编辑器并写下以下内容（确保包含正确的BZZKEY）

```
{
"nonce": "0x0000000000000042",
  "mixhash": "0x0000000000000000000000000000000000000000000000000000000000000000",
  "difficulty": "0x4000",
  "alloc": {
    "THE BZZKEY address starting with 0x eg. 0x2f1cd699b0bf461dcfbf0098ad8f5587b038f0f1": {
    "balance": "10000000000000000000"
    }
  },
  "coinbase": "0x0000000000000000000000000000000000000000",
  "timestamp": "0x00",
  "parentHash": "0x0000000000000000000000000000000000000000000000000000000000000000",
  "extraData": "Custom Ethereum Genesis Block to test Swarm with SWAP",
  "gasLimit": "0xffffffff"
}
```

另存为文件`$DATADIR/genesis.json`。

如果你已经运行了swarm和geth，杀死进程

```
killall -s SIGKILL geth
killall -s SIGKILL swarm
```

并从$ DATADIR中删除旧数据，然后使用定制创世区块重新初始化

```
rm -rf $DATADIR/geth $DATADIR/swarm
geth --datadir $DATADIR init $DATADIR/genesis.json
```

现在我们利用定制创世块重新启动geth和swarm

```
nohup geth --datadir $DATADIR \
       --mine \
       --unlock 0 \
       --password <(echo -n "MYPASSWORD") \
       --verbosity 6 \
       --networkid 322 \
       --nodiscover \
       --maxpeers 0 \
        2>> $DATADIR/geth.log &
```

并启动swarm（带SWAP）; 将它连接到geth节点。为了一致性，我们对swarm私有网络使用相同的网络ID 322。

```
swarm --bzzaccount $BZZKEY \
       --swap \
       --swap-api $DATADIR/geth.ipc \
       --datadir $DATADIR \
       --verbosity 6 \
       --ens-api $DATADIR/geth.ipc \
       --maxpeers 0 \
       --bzznetworkid 322 \
       2>> $DATADIR/swarm.log < <(echo -n "MYPASSWORD") &
```

如果全部成功，您将在swarm.log上看到消息“正在部署新支票本(chequebook)”。交易一旦确认(mined)，SWAP就准备好了。

注意

精明的读者会注意到，在将maxpeers设置为0时启用SWAP似乎是徒劳的。这些说明将很快更新，以允许您使用多个peer运行私有 SWAP testnet。

#### 在您的私有链上挖矿

除了创建定制创世区块之外，另一种获取以太币的方法是在您的私有链挖矿。您可以使用`--mine`标志以挖矿模式启动geth节点，或者（在我们的例子中），我们可以通过发出以下`miner.start()`命令开始在已经运行的geth节点上进行挖矿：

```
geth --exec 'miner.start()' attach ipc:$DATADIR/geth.ipc
```

在生成必要的DAG时会有一个初始延迟。您可以在geth.log文件中看到进度。挖矿开始后，您可以通过`eth.getBalance()`看到您的余额正在增加：

```
geth --exec 'eth.getBalance(eth.coinbase)' attach ipc:$DATADIR/geth.ipc
# or
geth --exec 'eth.getBalance(eth.accounts[0])' attach ipc:$DATADIR/geth.ipc
```

一旦余额大于0，我们可以重启`swarm`来启用swap。

```
killall swarm
swarm --bzzaccount $BZZKEY \
     --swap \
     --swap-api $DATADIR/geth.ipc \
     --datadir $DATADIR \
     --verbosity 6 \
     --ens-api $DATADIR/geth.ipc \
     --maxpeers 0 \
     2>> $DATADIR/swarm.log < <(echo -n "MYPASSWORD") &
```

注意：如果没有定制创世区块，采矿难度可能太高而不实用（取决于您的系统）。你用`admin.nodeInfo`查看目前的困难度

```
geth --exec 'admin.nodeInfo' attach ipc:$DATADIR/geth.ipc | grep difficulty
```

## 配置

## swarm的命令行选项

Swarm可执行文件支持以下配置选项：

- 配置文件
- 环境变量
- 命令行

通过命令行提供的选项将覆盖环境变量中的选项，而环境变量会将覆盖配置文件中的选项。如果没有明确提供选项，则会选择默认值。

为了保持标志和变量集合的可管理性，通过命令行和环境变量只有一部分的配置选项可用。有些仅可以通过TOML配置文件提供。

注意

Swarm重用了以太坊的代码，特别是一些p2p网络协议和其他常见部分。为此，它接受许多实际来自`geth`环境的环境变量。请参阅geth文档以获取有关这些标志的参考信息。

这是继承自`geth`以下标志的列表：

```
--identity
--bootnodes
--datadir
--keystore
--nodiscover
--v5disc
--netrestrict
--nodekey
--nodekeyhex
--maxpeers
--nat
--ipcdisable
--ipcpath
--password
```

下表列出了所有配置选项以及如何提供它们。

## 配置选项

注意

swarm可以使用*dumpconfig*命令执行，该命令将默认配置打印到STDOUT，从而可以将其重定向到一个文件，该文件可以作为配置文件模板。

TOML配置文件按章节(section)组织。下面按章节列出了可用配置选项。这些章节对应于Go模块，因此需要遵守以使文件配置正常工作。有关Golang的TOML解析器和编码器库参阅<https://github.com/naoina/toml>，更多关于TOML的信息参阅<https://github.com/toml-lang/toml>。

### 一般配置参数

| Config file | Command-line flag | Environment variable  | Default value                              | Description                                                  |
| ----------- | ----------------- | --------------------- | ------------------------------------------ | ------------------------------------------------------------ |
| n/a         | –config           | n/a                   | n/a                                        | Path to config file in TOML format                           |
| Contract    | –chequebook       | SWARM_CHEQUEBOOK_ADDR | 0x0000000000000000000000000000000000000000 | Swap chequebook contract address                             |
| EnsRoot     | –ens-addr         | SWARM_ENS_ADDR        | ens.TestNetAddress                         | Ethereum Name Service contract address                       |
| EnsApi      | –ens-api          | SWARM_ENS_API         | <$GETH_DATADIR>/geth.ipc                   | Ethereum Name Service API address                            |
| Path        | –datadir          | GETH_DATADIR          | <$GETH_DATADIR>/swarm                      | Path to the geth configuration directory                     |
| ListenAddr  | –httpaddr         | SWARM_LISTEN_ADDR     | 127.0.0.1                                  | Swarm listen address                                         |
| Port        | –bzzport          | SWARM_PORT            | 8500                                       | Port to run the http proxy server                            |
| PublicKey   | n/a               | n/a                   | n/a                                        | Public key of swarm base account                             |
| BzzKey      | n/a               | n/a                   | n/a                                        | Swarm node base address (hash(PublicKey)hash(PublicKey))hash(PublicKey)hash(PublicKey)). This is used to decide storage based on radius and routing by kademlia. |
| NetworkId   | –bzznetworkid     | SWARM_NETWORK_ID      | 3                                          | Network ID                                                   |
| SwapEnabled | –swap             | SWARM_SWAP_ENABLE     | false                                      | Enable SWAP                                                  |
| SyncEnabled | –sync             | SWARM_SYNC_ENABLE     | true                                       | Disable swarm node synchronization. This option will be deprecated. It is only for testing. |
| SwapApi     | –swap-api         | SWARM_SWAP_API        |                                            | URL of the Ethereum API provider to use to settle SWAP payments |
| Cors        | –corsdomain       | SWARM_CORS            |                                            | Domain on which to send Access-Control-Allow-Origin header (multiple domains can be supplied separated by a ‘,’) |
| BzzAccount  | –bzzaccount       | SWARM_ACCOUNT         |                                            | Swarm account key                                            |
| BootNodes   | –boot-nodes       | SWARM_BOOTNODES       |                                            | Boot nodes                                                   |

### 存储参数

| Config file   | Command-line flag | Environment variable | Default value                               | Description                                                  |
| ------------- | ----------------- | -------------------- | ------------------------------------------- | ------------------------------------------------------------ |
| ChunkDbPath   | n/a               | n/a                  | <$GETH_ENV_DIR>/swarm/bzz-<$BZZ_KEY>/chunks | Path to leveldb chunk DB                                     |
| DbCapacity    | n/a               | n/a                  | 5000000                                     | DB capacity, number of chunks (5M is roughly 20-25GB)        |
| CacheCapacity | n/a               | n/a                  | 5000                                        | Cache capacity, number of recent chunks cached in memory     |
| Radius        | n/a               | n/a                  | 0                                           | Storage Radius: minimum proximity order (number of identical prefix bits of address key) for chunks to warrant storage. Given a storage radius rr and total number of chunks in the network nn, the node stores n∗2−rn∗2−r chunks minimum. If you allow bb bytes for guaranteed storage and the chunk storage size is cc, your radius should be set to int(log2(nc/b))int(log2(nc/b)) |

### Chunker参数

| Config file | Command-line flag | Environment variable | Default value | Description                                                  |
| ----------- | ----------------- | -------------------- | ------------- | ------------------------------------------------------------ |
| Branches    | n/a               | n/a                  | 128           | Number of branches in bzzhash merkle tree. Branches∗ByteSize(Hash)Branches∗ByteSize(Hash) gives the datasize of chunks |
| Hash        | n/a               | n/a                  | SHA3          | Hash: The hash function used by the chunker (base hash algo of bzzhash): SHA3 or SHA256.This option will be removed in a later release. |

### Hive参数

| Config file  | Command-line flag | Environment variable | Default value                         | Description                                                  |
| ------------ | ----------------- | -------------------- | ------------------------------------- | ------------------------------------------------------------ |
| CallInterval | n/a               | n/a                  | 3000000000                            | Time elapsed before attempting to connect to the most needed peer |
| KadDbPath    | n/a               | n/a                  | <$GETH_ENV_DIR>/swarm/bzz-<$BZZ_KEY>/ | Kademblia DB path, json file path storing the known bzz peers used to bootstrap kademlia table. |

### Kademlia参数

| Config file          | Command-line flag | Environment variable | Default value   | Description                                                  |
| -------------------- | ----------------- | -------------------- | --------------- | ------------------------------------------------------------ |
| MaxProx              | n/a               | n/a                  | 8               | highest Proximity order (i.e., Maximum number of identical prefix bits of address key) considered distinct. Given the total number of nodes in the network NN, MaxProx should be larger than log2(N/ProxBinSize)log2(N/ProxBinSize)), safely log2(N)log2(N). |
| ProxBinSize          | n/a               | n/a                  | 2               | Number of most proximate nodes lumped together in the most proximate kademlia bin |
| BuckerSize           | n/a               | n/a                  | 4               | maximum number of active peers in a kademlia proximity bin. If new peer is added, the worst peer in the bin is dropped. |
| PurgeInterval        | n/a               | n/a                  | 151200000000000 |                                                              |
| InitialRetryInterval | n/a               | n/a                  | 42000000        |                                                              |
| MaxIdleInterval      | n/a               | n/a                  | 42000000000     |                                                              |
| ConnRetryExp         | n/a               | n/a                  | 2               |                                                              |

### SWAP配置文件参数

这些参数可能会在POC 0.3中发生变化

| Config file   | Command-line flag | Environment variable | Default value | Description                                                  |
| ------------- | ----------------- | -------------------- | ------------- | ------------------------------------------------------------ |
| BuyAt         | n/a               | n/a                  | 20000000000   | (2∗10102∗1010 wei), highest accepted price per chunk in wei  |
| SellAt        | n/a               | n/a                  | 20000000000   | (2∗10102∗1010 wei) offered price per chunk in wei            |
| PayAt         | n/a               | n/a                  | 100           | Maximum number of chunks served without receiving a cheque. Debt tolerance. |
| DropAtMaximum | n/a               | n/a                  | 10000         | Number of chunks served without receiving a cheque. Debt tolerance. |

### SWAP策略参数
这些参数可能会在POC 0.3中发生变化

| Config file          | Command-line flag | Environment variable | Default value   | Description                                                  |
| -------------------- | ----------------- | -------------------- | --------------- | ------------------------------------------------------------ |
| AutoCashInterval     | n/a               | n/a                  | 300000000000    | (3∗10<sup>11</sup>, 5 minutes) Maximum Time before any outstanding cheques are cashed |
| AutoCashThreshold    | n/a               | n/a                  | 50000000000000  | (5∗10135∗1013) Maximum total amount of uncashed cheques in Wei |
| AutoDepositInterval  | n/a               | n/a                  | 300000000000    | (3∗10113∗1011, 5 minutes) Maximum time before cheque book is replenished if necessary by sending funds from the baseaccount |
| AutoDepositThreshold | n/a               | n/a                  | 50000000000000  | (5∗10135∗1013) Minimum balance in Wei required before replenishing the cheque book |
| AutoDepositBuffer    | n/a               | n/a                  | 100000000000000 | (10141014) Maximum amount of Wei expected as a safety credit buffer on the cheque book |

### SWAP支付配置文件参数
这些参数可能会在POC 0.3中发生变化

| Config file | Command-line flag | Environment variable | Default value                              | Description                                                  |
| ----------- | ----------------- | -------------------- | ------------------------------------------ | ------------------------------------------------------------ |
| PublicKey   | n/a               | n/a                  |                                            | Public key of your swarm base account use                    |
| Contract    | n/a               | n/a                  | 0x0000000000000000000000000000000000000000 | Address of the cheque book contract deployed on the Ethereum blockchain. If blank, a new chequebook contract will be deployed. |
| Beneficiary | n/a               | n/a                  | 0x0000000000000000000000000000000000000000 | Ethereum account address serving as beneficiary of incoming cheques |

### 同步参数

| Config file        | Command-line flag | Environment variable | Default value                                 | Description                                                  |
| ------------------ | ----------------- | -------------------- | --------------------------------------------- | ------------------------------------------------------------ |
| RequestDbPath      | n/a               | n/a                  | <$GETH_ENV_DIR>/swarm/bzz-<$BZZ_KEY>/requests | Path to request DB                                           |
| RequestDbBatchSize | n/a               | n/a                  | 512                                           | Request DB Batch size                                        |
| KeyBufferSize      | n/a               | n/a                  | 1024                                          | In-memory cache for unsynced keys                            |
| SyncBatchSize      | n/a               | n/a                  | 128                                           | In-memory cache for unsynced keys                            |
| SyncBufferSize     | n/a               | n/a                  | 128                                           | In-memory cache for outgoing deliveries                      |
| SyncCacheSize      | n/a               | n/a                  | 1024                                          | Maximum number of unsynced keys sent in one batch            |
| Sync priorities    | n/a               | n/a                  | [2, 1, 1, 0, 0]                               | Array of 5 priorities corresponding to 5 delivery types:<delivery, propagation, deletion, history, backlog>.Specifying a monotonically decreasing list of priorities is highly recommended. |
| SyncModes          | n/a               | n/a                  | [true, true, true, true, false]               | A boolean array specifying confirmation mode ON corresponding to 5 delivery types:<delivery, propagation, deletion, history, backlog>. Specifying true for a type means all deliveries will be preceeded by a confirmation roundtrip: the hash key is sent first in an unsyncedKeysMsg and delivered only if confirmed in a deliveryRequestMsg. |

注意

这个项目的状态保证这些选项可能会有很多变化。

如果`config.Contract`为空（零地址），则部署新的支票簿合约。在区块链上确认合约之前，不允许传出检索请求。

### 设置SWAP

SWAP（蜂窝计费协议）是允许公平使用带宽的系统（请参见[Incentivisation](http://swarm-guide.readthedocs.io/en/latest/architecture.html#incentivisation)，特别是[SWAP - Swarm Accounting Protocol](http://swarm-guide.readthedocs.io/en/latest/architecture.html#swap)）。为了使用SWAP，必须已经部署支票簿合约。如果启动客户端时支票簿合约不存在或者如果在配置文件中指定的合约无效，则客户端将尝试自动部署支票簿：

> [BZZ] SWAP部署新支票簿（所有者：0xe10536 .. 5e491）

如果您已经在区块链上拥有有效的支票簿，只需将其输入到配置文件`Contract`字段即可。

您可以设置一个单独的帐户作为您的服务兑现支票付款的受益人。将其设置`Beneficiary`在配置文件的字段中。

如果基本帐户没有资金并且无法支付交易费用，自动部署支票簿可能会失败。请注意，如果您的区块链不同步，也会发生这种情况。在这种情况下，您将看到日志消息：

```
[ BZZ ] SWAP无法部署新支票簿：无法发送支票簿创建交易：帐户
 不存在或帐户余额太低..在10秒内重新开始

[ BZZ ] SWAP安排与<enode：// 23ae0e62 .. .. .. 8a4c6bc93b7d2aa4fb@195.228.155.76：30301>：从同伴购买禁用; 卖给同伴禁用）

```

由于这里没有业务可能，所以在至少一方签署合约之前，连接处于闲置状态。实际上，这仅适用于测试阶段。如果我们不被允许购买块，那么不允许传出请求。如果我们仍然尝试下载我们本地没有的内容，则请求将失败（除非我们与其他同行相信）。

```
[ BZZ ] netStore.startSearch：无法发送retrieveRequest到对方[ <addr> ]：[ SWAP ] <enode：// 23ae0e62 .. .. .. 8a4c6bc93b7d2aa4fb@195.228.155.76：30301>我们不能有债务（无法购买）

```

一旦有一个节点有资金（比如挖掘一下之后），并且网络上的某个人正在挖掘，那么自动部署将最终成功：

```
[支票簿]支票簿部署在0x77de9813e52e3a .. .c8835ea7 （所有者：0xe10536ae628f7d6e319435ef9b429dcdc085e491 ）
[支票簿]从0x77de9813e52e3a .. .c8835ea7初始化新支票簿（所有者：0xe10536ae628f7d6e319435ef9b429dcdc085e491 ）
[ BZZ ] SWAP自动存款ON 为 0xe10536 - > 0x77de98：间隔 = 5m0s，阈值 =  50000000000000，缓冲区 =  100000000000000 ）
[ BZZ ]Swarm：新支票簿集：保存配置文件，重置蜂巢中的所有连接
 [ KΛÐ ]：从表中删除节点enode：// 23ae0e6 .. .aa4fb @ 195.228.155.76：30301

```

一旦节点部署了新的支票簿，其地址就会在配置文件中设置，并且所有连接都将重置为新条件。应该启用一个方向的购买。从没有有效支票簿的同行的角度来看日志：

```
[ CHECKBOOK ]初始化的收件箱（ 0x9585 .. .3bceee6c  - > 0xa5df94be .. .bbef1e5 ）期望的签名者：041e18592 .. .. 702cf5e73cf8d618
 [ SWAP ] <enode：// 23ae0e62 .. .. .. 8a4c6bc93b7d2aa4fb@195.228.155.76 ：30301>     set autocash to every 5m0s，maximum uncashed limit：50000000000000 
[ SWAP ] <enode：// 23ae0e62 .. .. .. 8a4c6bc93b7d2aa4fb@195.228.155.76：30301> autodeposit off （ not buying ）
[ SWAP ] <enode：/ / 23ae0e62 .. .. 8a4c6bc93b7d2aa4fb@195.228.155.76：30301>远程配置文件集：支付：100，降至：10000，买入：20000000000，卖出：20000000000 
[ BZZ ] SWAP安排与<enode：// 23ae0e62 .. .. .. 8a4c6bc93b7d2aa4fb@195.228.155.76：30301>：从同伴购买禁用;    以20000000000 wei / chunk 销售给同行）

```

根据自动存款设置，支票簿将定期补充：

```
[ BZZ ] SWAP自动存入ON 为 0x6d2c5b  - > 0xefbb0c：
  interval  = 5m0s，threshold  =  50000000000000，
  buffer  =  100000000000000 ） 
 将100000000000000 wei 存入支票簿（ 0xefbb0c0 .. .16dea，余额：100000000000000，目标：100000000000000 ）

```

没有支票簿（尚未）的同行不应该被允许下载，因此检索请求不会出去。然而，另一个同伴能够支付，因此这个其他同伴可以从第一个同伴中检索块并为它们付钱。这反过来又使第一个同行积极，他们可以同时使用它们（自动）部署自己的支票簿并支付检索数据。如果他们不出于任何原因部署支票簿，他们可以使用他们的余额来支付检索数据，但只能达到0余额; 在此之后，不再有任何请求被允许出去。你会再次看到：

```
[ BZZ ] netStore.startSearch：无法向[ peer89da0c6 ... 623e5671c01 ]发送retrieveRequest ：[ SWAP ]   <enode：//23ae0e62...8a4c6bc93b7d2aa4fb@195.228.155.76：30301>我们不能有债务（无法购买）

```

如果没有支票簿的对等体尝试发送请求而没有支付，那么远程对等体（谁可以看到他们没有支票簿合约）将其解释为导致对等体被丢弃的恶意行为。

在本例中，我们开始挖掘，然后重新启动节点。第二本支票簿autodeploys，同行同步他们的链和重新连接，然后如果一切顺利日志将显示如下所示：

```
初始化的收件箱（ 0x95850c6 .. .bceee6c - > 0xa5df94b .. .bef1e5 ）预期签名者：041e185925bb .. .. .. 702cf5e73cf8d618
 [ SWAP ] <e节点：// 23ae0e62 .. .. .. 8a4c6bc93b7d2aa4fb@195.228.155.76：30301> 设置自动收到每5m0s，最大未兑现限制：500000亿
[ SWAP ] <e节点：// 23ae0e62 .. .. .. 8a4c6bc93b7d2aa4fb@195.228.155.76：30301> 设置 autodeposit每5m0s，付于：500000亿，缓冲液：百万亿
[ SWAP ] <enode：// 23ae0e62 .. .. .. 8a4c6bc93b7d2aa4fb@195.228.155.76：30301>远程配置文件集：支付在：100，降在：10000，买入：20000000000，卖出：20000000000 
[ SWAP ] <enode：// 23ae0e62 .. .. .. 8a4c6bc93b7d2aa4fb@195.228.155.76：30301>远程配置文件集：支付在：100，降到：10000，买入：20000000000，卖出：20000000000 
[ BZZ ] SWAP安排与<node：//23ae0e62...8a4c6bc93b7d2aa4fb@195.228.155.76：30301>：以20000000000 wei / chunk 启用同级购买; 以20000000000 wei / chunk 销售给同行）

```

作为正常操作的一部分，在对等体达到`PayAt`（块数）余额后，通过协议发送支票付款。登录接收端：

```
[支票簿]校验检查：合约：0x95850 .. .eee6c，受益：0xe10536ae628 .. .cdc085e491，量：8680200亿，签名：a7d52dc744b8 .. .. .. f1fe2001 -总和：8660200亿
[支票簿]接收的校验二万亿伟在收件箱中（ 0x95850 .. .eee6c，未兑现：420000亿）

```

支票被验证。如果未兑现的支票余额超过`AutoCashThreshold`，则最后一张支票（累计金额）兑现。这是通过发送一个包含支票的交易给远程同行的cheuebook合约来完成的。因此，为了兑现付款，您的发件人帐户（baseaddress）需要有资金，网络应该是挖掘。

```
[支票簿]兑现支票（总：1040000亿）上支票簿（ 0x95850c6 .. .eee6c ）发送到0xa5df94be .. .e5aaz

```

要进一步细调SWAP，请参阅[SWAP配置文件参数](http://swarm-guide.readthedocs.io/en/latest/runninganode.html#swap-params)。
