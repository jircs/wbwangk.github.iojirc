# 笔记
[这里](https://github.com/wbwangk/wbwangk.github.io/wiki/Ethereum%E8%BF%9B%E9%98%B6#%E9%83%A8%E7%BD%B2%E5%A4%9A%E8%8A%82%E7%82%B9parity-poa%E5%8C%BA%E5%9D%97%E9%93%BE)是一个部署多节点PoA的教程。  

**Parity**是一个快速、轻量、健壮的以太坊实现（自称）。[https://parity.io](https://parity.io/)

github库地址：https://github.com/paritytech/parity
参考了文章《[从零构建基于以太坊（Ethereum）钱包Parity联盟链](http://www.8btc.com/ethereum-parity)》(简称《从零》)

#### 安装和配置
安装：
```
sudo snap install parity
```
按《从零》的说明在`~/parity`[[1](E:\vagrant9\ambari-vagrant\fabric\devenv)]目录下创建了三个文件：demo-spec.json、node0.toml、node1.toml。

由于使用NAT方式映射到了宿主机，所以需要显式定义IP[2](https://github.com/paritytech/parity/wiki/Configuring-Parity)，以node0.toml为例：
```
[ui]
port = 8081
interface = "0.0.0.0"
```
使用8081端口是因为这个端口已经为NAT映射到了宿主机。

#### 启动
```
cd ~/parity
$ parity --config node0.toml
```
然后在宿主机windows下访问`localhost:8081`就打开了Parity界面。





# 演示PoA教程

本教程将介绍在本地设置两个Parity节点并在它们之间发送交易。如果您想了解更多关于本教程中指定的不同参数的信息，请参阅[链规范](https://wiki.parity.io/Chain-specification.html)页面。

每个节点在必要时将充当网络发布区块的权威，还会有一个额外的用户账户具有较高的初始余额。本教程中生成的所有文件都可以在[这里](https://github.com/keorn/parity-poa-tutorial)找到。

## 1.选择你的链

我们将使用权威Round共识引擎运行一个区块链。首先，我们需要创建一个包含所有必填字段的基本链配置。

```
{
    "name": "DemoPoA",
    "engine": {
        "authorityRound": {
            "params": {
                "stepDuration": "5",
                "validators" : {
                    "list": []
                }
            }
        }
    },
    "params": {
        "gasLimitBoundDivisor": "0x400",
        "maximumExtraDataSize": "0x20",
        "minGasLimit": "0x1388",
        "networkID" : "0x2323"
    },
    "genesis": {
        "seal": {
            "authorityRound": {
                "step": "0x0",
                "signature": "0x0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
            }
        },
        "difficulty": "0x20000",
        "gasLimit": "0x5B8D80"
    },
    "accounts": {
        "0x0000000000000000000000000000000000000001": { "balance": "1", "builtin": { "name": "ecrecover", "pricing": { "linear": { "base": 3000, "word": 0 } } } },
        "0x0000000000000000000000000000000000000002": { "balance": "1", "builtin": { "name": "sha256", "pricing": { "linear": { "base": 60, "word": 12 } } } },
        "0x0000000000000000000000000000000000000003": { "balance": "1", "builtin": { "name": "ripemd160", "pricing": { "linear": { "base": 600, "word": 120 } } } },
        "0x0000000000000000000000000000000000000004": { "balance": "1", "builtin": { "name": "identity", "pricing": { "linear": { "base": 15, "word": 3 } } } }
    }
}

```

`"gasLimitBoundDivisor"`定义了Gas限制调整为通常的以太坊值

`"stepDuration"`设置为至少5秒的出块时间

`"validators"`现在是空的，因为我们尚未创建我们的权威账户

`"params"` 大多数链的标准

`"genesis"` 对于权威Round共识具有一些标准值

`"accounts"` 包含标准的以太坊内部合约，这些应该包括在内以使用Solidity合约书写语言

将上面的内容保存到`demo-spec.json`。

## 2.建立两个节点

现在完成了一个空的骨架链配置已经完成，那么可以建立两个节点。对于每个链，Parity在各自的文件夹中存储账户的创世哈希，因此为了创建正确的账户，我们需要使用`--chain`选项运行。通常这两个节点将在不同的机器上启动，但由于我们使用的是同一个节点，我们需要解决一些可能的冲突：

- `-d` 确定一个Parity实例保存数据和密钥的目录

- `--port` 确定Parity与其他节点通信的端口

- `--jsonrpc-port` 是RPC端口

- `--ui-port` 是图形钱包用户界面使用的端口

- `--ws-port`是钱包用于与节点交互的端口。

- `--jsonrpc-apis web3, eth, net, personal, parity, parity_set, traces, rpc, parity_accounts`我们希望公开所有RPC API以使与节点交互更容易

把它们放在一起后，给了我们以下命令来启动Parity：

```
parity  --chain demo-spec.json -d /tmp/parity0 --port 30300 --jsonrpc-port 8540 --ui-port 8180 --ws-port 8540 --jsonrpc-apis web3,eth,net,personal,parity,parity_set,traces,rpc,parity_accounts
```

由于命令变得相当笨拙，我们可以使用[配置文件](https://wiki.parity.io/Configuring-Parity#config-file.md)来代替使用`--config`选项传递配置文。节点0将这个配置文件保存在`node0.toml`：

```
[parity]
chain = "demo-spec.json"
base_path = "/tmp/parity0"
[network]
port = 30300
[rpc]
port = 8540
apis = ["web3", "eth", "net", "personal", "parity", "parity_set", "traces", "rpc", "parity_accounts"]
[ui]
port = 8180
[websockets]
port = 8450
```

而节点1将配置保存在`node1.toml`：

```
[parity]
chain = "demo-spec.json"
base_path = "/tmp/parity1"
[network]
port = 30301
[rpc]
port = 8541
apis = ["web3", "eth", "net", "personal", "parity", "parity_set", "traces", "rpc", "parity_accounts"]
[ui]
port = 8181
[websockets]
port = 8451
[ipc]
disable = true
```

备选配置文件可以在[这里](https://paritytech.github.io/parity-config-generator/)生成。

我们将创建三个账户：两个权威账户和一个用户账户。创建账户有三种（RPC、UI、命令行）主要方式，选择一种最适合您的方式：

### RPC

使用 `parity --config node0.toml`启动节点0。

RPC可以通过访问`web3`、`parity.js`或简单地使用`curl`。这将创建第一个权威地址：

```
curl --data '{"jsonrpc":"2.0","method":"parity_newAccountFromPhrase","params":["node0", "node0"],"id":0}' -H "Content-Type: application/json" -X POST localhost:8540
```

返回的地址应该是`0x00bd138abd70e2f00903268f3db08f2d25677c9e`。

用户地址：

```
curl --data '{"jsonrpc":"2.0","method":"parity_newAccountFromPhrase","params":["user", "user"],"id":0}' -H "Content-Type: application/json" -X POST localhost:8540
```

返回的地址应该是`0x004ec07d2329997267ec62b4166639513386f32e`。

现在用`parity --config node1.toml`启动另一个节点并创建第二个权威账户：

```
curl --data '{"jsonrpc":"2.0","method":"parity_newAccountFromPhrase","params":["node1", "node1"],"id":0}' -H "Content-Type: application/json" -X POST localhost:8541
```

返回的地址应该是`0x00aa39d30f0d20ff03a22ccfc30b7efbfca597c2`。

### UI

1. 使用`parity --config node0.toml`启动节点0 
2. 使用您的浏览器转到`localhost:8180`，并经过初始设置
3. 点击“RESTORE ACCOUNT”
4. 使用短语“node0”和密码“node0”
5. 新创建的账户应具有地址 `0x00Bd138aBD70e2F00903268F3Db08f2D25677C9e`
6. 现在从另一个短语“user”和密码“user”恢复，这将导致账户 `0x004ec07d2329997267Ec62b4166639513386F32E`
7. 使用 `parity --config node1.toml`启动节点1
8. 前往 `localhost:8181`
9. 再次从短语“node1”和密码“node1”中恢复，这将创建账户 `0x00Aa39d30F0D20FF03a22cCfc30B7EfbFca597C2`

### `parity account new`

您也可以创建一个账户，而无需启动Parity：

```
parity account new --config node0.toml
```

这不会让你控制什么地址将被创建，所以对于本教程的其余部分，我们将坚持使用先前方法创建的账户。

## 3.完成链配置

如果账户是从短语创建的，我们现在应该有以下设置：

- 节点0具有`0x00Bd138aBD70e2F00903268F3Db08f2D25677C9e`权威账户和`0x004ec07d2329997267Ec62b4166639513386F32E`用户账户
- 节点1具有`0x00Aa39d30F0D20FF03a22cCfc30B7EfbFca597C2`权威账户

这些账户现在可以添加到spec文件中。打开`demo-spec.json`备份并将我们刚刚创建的权威添加到`"validators"`数组中：

```
"validators" : {
    "list": [
        "0x00Bd138aBD70e2F00903268F3Db08f2D25677C9e",
        "0x00Aa39d30F0D20FF03a22cCfc30B7EfbFca597C2"
    ]
}
```

然后将用户账户添加到创世块中，以便我们可以发放一些余额：

```
"0x004ec07d2329997267Ec62b4166639513386F32E": { "balance": "10000000000000000000000" }
```

完整的`demo-spec.json`应该是这样的：

```
{
    "name": "DemoPoA",
    "engine": {
        "authorityRound": {
            "params": {
                "gasLimitBoundDivisor": "0x400",
                "stepDuration": "5",
                "validators" : {
                    "list": [
                        "0x00Bd138aBD70e2F00903268F3Db08f2D25677C9e",
                        "0x00Aa39d30F0D20FF03a22cCfc30B7EfbFca597C2"
                    ]
                }
            }
        }
    },
    "params": {
        "maximumExtraDataSize": "0x20",
        "minGasLimit": "0x1388",
        "networkID" : "0x2323"
    },
    "genesis": {
        "seal": {
            "authorityRound": {
                "step": "0x0",
                "signature": "0x0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
            }
        },
        "difficulty": "0x20000",
        "gasLimit": "0x5B8D80"
    },
    "accounts": {
        "0x0000000000000000000000000000000000000001": { "balance": "1", "builtin": { "name": "ecrecover", "pricing": { "linear": { "base": 3000, "word": 0 } } } },
        "0x0000000000000000000000000000000000000002": { "balance": "1", "builtin": { "name": "sha256", "pricing": { "linear": { "base": 60, "word": 12 } } } },
        "0x0000000000000000000000000000000000000003": { "balance": "1", "builtin": { "name": "ripemd160", "pricing": { "linear": { "base": 600, "word": 120 } } } },
        "0x0000000000000000000000000000000000000004": { "balance": "1", "builtin": { "name": "identity", "pricing": { "linear": { "base": 15, "word": 3 } } } },
        "0x004ec07d2329997267Ec62b4166639513386F32E": { "balance": "10000000000000000000000" }
    }
}
```

## 4.运行权威节点

现在配置已经完成，我们可以启动两个节点，这将使链运行起来。要将节点作为权威运行，我们需要使其能够签署共识信息。

首先让我们用节点密码创建一个文件`node.pwds`。每一行将包含我们在创建权威账户时使用的密码，存储在`node.pwds`文件中的内容是：

```
node0
node1
```

现在我们可以添加`engine-signer`到我们的配置文件中。`node0.toml`：

```
[parity]
chain = "demo-spec.json"
base_path = "/tmp/parity0"
[network]
port = 30300
[rpc]
port = 8540
apis = ["web3", "eth", "net", "personal", "parity", "parity_set", "traces", "rpc", "parity_accounts"]
[ui]
port = 8180
[websockets]
port = 8450
[account]
password = ["node.pwds"]
[mining]
engine_signer = "0x00Bd138aBD70e2F00903268F3Db08f2D25677C9e"
reseal_on_txs = "none"
```

和`node1.toml`：

```
[parity]
chain = "demo-spec.json"
base_path = "/tmp/parity1"
[network]
port = 30301
[rpc]
port = 8541
apis = ["web3", "eth", "net", "personal", "parity", "parity_set", "traces", "rpc", "parity_accounts"]
[ui]
port = 8181
[websockets]
port = 8451
[ipc]
disable = true
[account]
password = ["node.pwds"]
[mining]
engine_signer = "0x00Aa39d30F0D20FF03a22cCfc30B7EfbFca597C2"
reseal_on_txs = "none"
```

现在可以启动两个节点。节点0：

```
parity --config node0.toml
```

和节点1：

```
parity --config node1.toml
```

## 5.连接节点

为了确保节点彼此连接，我们需要知道它们的[enode地址](https://github.com/ethereum/wiki/wiki/enode-url-format)，并将地址通知其他节点。有三种方法可以获得enode：

- RPC
- UI（在下面中）
- 启动控制台输出

一旦获得，它们可以作为引导节点添加到`demo-spec.json`或作为保留节点。

这里我们将简单地使用`curl`。获取节点0的enode：

```
curl --data '{"jsonrpc":"2.0","method":"parity_enode","params":[],"id":0}' -H "Content-Type: application/json" -X POST localhost:8540
```

将“结果”添加到节点1（在命令中替换`enode://RESULT`）：

```
curl --data '{"jsonrpc":"2.0","method":"parity_addReservedPeer","params":["enode://RESULT"],"id":0}' -H "Content-Type: application/json" -X POST localhost:8541
```

现在节点应该在控制台中指出`0/1/25 peers`，这意味着它们已经相互连接了。

## 6.发送交易

发送交易的两种主要方式是RPC和UI。

### RPC

从用户账户向权威节点0发送一些代币：

```
curl --data '{"jsonrpc":"2.0","method":"personal_sendTransaction","params":[{"from":"0x004ec07d2329997267Ec62b4166639513386F32E","to":"0x00Bd138aBD70e2F00903268F3Db08f2D25677C9e","value":"0xde0b6b3a7640000"}, "user"],"id":0}' -H "Content-Type: application/json" -X POST localhost:8540
```

一旦提交请求，区块应在几秒钟后发出。您可以检查其他账户是否收到资金：

```
curl --data '{"jsonrpc":"2.0","method":"eth_getBalance","params":["0x00Bd138aBD70e2F00903268F3Db08f2D25677C9e", "latest"],"id":1}' -H "Content-Type: application/json" -X POST localhost:8540
```

我们也可以将一些发送给另一个节点上的账户：

```
curl --data '{"jsonrpc":"2.0","method":"personal_sendTransaction","params":[{"from":"0x004ec07d2329997267Ec62b4166639513386F32E","to":"0x00Aa39d30F0D20FF03a22cCfc30B7EfbFca597C2","value":"0xde0b6b3a7640000"}, "user"],"id":0}' -H "Content-Type: application/json" -X POST localhost:8540
```

并询问其他节点，检查是否收到：

```
curl --data '{"jsonrpc":"2.0","method":"eth_getBalance","params":["0x00Aa39d30F0D20FF03a22cCfc30B7EfbFca597C2", "latest"],"id":1}' -H "Content-Type: application/json" -X POST localhost:8541
```

### UI

您还可以使用节点UI，节点0在`localhost:8180`和节点1在`localhost:8181`。

## 7.进一步开发

您现在可以创建更多账户，发送资金，编写合约并进行部署。所有用于开发和使用以太坊网络的工具都可以在此网络中使用。

要在多台机器上部署Parity，您可能会发现[Docker构建](https://wiki.parity.io/Docker)有用。

要添加一个非权威节点，可以使用这个更简单的配置：

```
[parity]
chain = "demo-spec.json"
base_path = "/tmp/parity2"
[network]
port = 30302
[rpc]
port = 8542
apis = ["web3", "eth", "net", "personal", "parity", "parity_set", "traces", "rpc", "parity_accounts"]
[ui]
port = 8182
[websockets]
port = 8452
[ipc]
disable = true
```

然后，账户和连接节点可以完成相同的权威节点。为了确保交易被接受，权威也可以按`[mining]`下面的`usd_per_tx = "0"`字段运行。任何节点提交一个交易都可以被免费处理。

在独立机器上运行多个节点时，大多数字段都是多余的，因此基本配置将仅包含链和可能的RPC API：

```
[parity]
chain = "demo-spec.json"
[rpc]
apis = ["web3", "eth", "net", "personal", "parity", "parity_set", "traces", "rpc", "parity_accounts"]
```

或简单运行`parity --chain demo-spec.json`！



