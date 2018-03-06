# 用法

## 从命令行使用swarm

### 上传文件或目录到swarm

确保你已经编译了swarm命令

```
cd $GOPATH/src/github.com/ethereum/go-ethereum
go install ./cmd/swarm
```

swarm up子命令可以方便地上传文件和目录。用法：

```
swarm up /path/to/file/or/directory
```

默认情况下，假定您正在默认端口（8500）上运行带有本地http代理的自己的swarm节点。请参阅[连接到swarm（高级）](http://swarm-guide.readthedocs.io/en/latest/runninganode.html#run-swarm-client)以了解如何运行本地节点。可以使用`--bzzapi`选项指定替代代理端点。

您可以使用其中一个公共网关作为代理，在这种情况下，您可以上传到swarm，而无需运行节点。

注意

这种做法可能会在未来消失或受到严重限制。目前它也接受有限的文件大小。

```
swarm --bzzapi http://swarm-gateways.net up /path/to/file/or/directory
```

#### 示例：上传文件

发出以下命令将go-ethereum自述文件上传到您的swarm

```
swarm up $GOPATH/src/github.com/ethereum/go-ethereum/README.md
```

它产生以下输出

```
> d1f25a870a7bb7e5d526a7623338e4e9b8399e76df8b634020d11d969594f24a
```

返回的哈希是一个清单(manifest)的哈希，该清单包含README.md文件作为唯一条目。所以默认情况下，主要内容和清单都会上传。你从swarm访问这个文件，方法是利用浏览器访问下面的链接

```
http://localhost:8500/bzz:/d1f25a870a7bb7e5d526a7623338e4e9b8399e76df8b634020d11d969594f24a
```

清单确保您可以使用正确的MIME类型检索文件。

您可能希望不为您的内容创建清单，而仅上传原始内容。也许你想将它包含在一个自定义索引中，或者将它作为一个已知的datablob进行处理，并仅由知道其mimetype的应用程序使用。为此，你可以设置-manifest = false：

```
swarm --manifest=false --bzzapi http://swarm-gateways.net/ up yellowpaper.pdf 2> up.log
> 7149075b7f485411e5cc7bb2d9b7c86b3f9f80fb16a3ba84f5dc6654ac3f8ceb
```

该选项可阻止自动清单上传。它按原样上传内容。但是，如果您希望检索该文件，则无法明确告知浏览器该文件所代表的内容。因此，swarm将返回一个404 Not Found。为了访问此文件，您可以使用`bzz-raw`方案(scheme)，请参阅[bzz-raw](http://swarm-guide.readthedocs.io/en/latest/usage.html#bzz-raw)。

#### 示例：上传目录

上传目录是通过`swarm --recursive up`。

让我们创建一些测试文件

```
mkdir upload-test
echo "one" > upload-test/one.txt
echo "two" > upload-test/two
mkdir upload-test/three
echo "four" > upload-test/three/four
```

我们可以用下列命令上传这个目录

```
swarm --recursive up upload-test/
```

输出再次是您上传的目录的根哈希值，它可用于检索完整的目录

```
ab90f84c912915c2a300a94ec5bef6fc0747d1fbaf86d769b3eed1c836733a30
```

然后，您可以像这样检索与根清单相关的文件：

```
curl http://localhost:8500/bzz:/ab90f84c912915c2a300a94ec5bef6fc0747d1fbaf86d769b3eed1c836733a30/three/four
```

结果应该是

```
four
```

如果您希望能够通过人类可读的名称（如'mysite.eth'）而不是上面的长十六进制字符串来访问您的内容，请参阅下面的[以太坊名称服务](http://swarm-guide.readthedocs.io/en/latest/usage.html#ethereum-name-service)部分。

## 内容检索：哈希和清单

### 使用http代理检索内容

如上所述，您的本地swarm实例具有在端口8500上运行的HTTP API（默认情况下）。检索内容很简单，只需将浏览器指向下列路径

```
GET http://localhost:8500/bzz:/HASH
```

HASH是swarm清单的ID。这是swarm可以为web提供服务的最常见用例。

它看起来像从服务器传输HTTP内容，但实际上它使用的是swarm的无服务器体系结构。

一般模式是：

```
<HTTP proxy>/<URL SCHEME>:/<DOMAIN OR HASH>/<PATH>?<QUERY_STRING>
```

如果您在浏览器中注册了适当的方案处理程序，或者您使用了Mist，则可以取消HTTP代理部分。

Swarm提供3种不同的URL方案：

### bzz url方案

#### bzz

例如：

```
GET http://localhost:8500/bzz:/theswarm.test
```

bzz方案假定url的域部分指向一个清单。当检索由url寻址的资产时，清单条目将与url路径匹配。具有最长匹配路径的条目将被检索，并与相应清单条目中指定的内容类型一起提供。

例如：

```
GET http://localhost:8500/bzz:/2477cc8584cc61091b5cc084cdcdb45bf3c6210c263b0143f030cf7d750e894d/read
```

如果给定哈希地址处的清单包含这样的条目，则返回readme.md文件。

如果清单中包含可以解析URL的多个条目，就像上面的例子，清单中包含条目readme.md和reading-list.txt，那么API将返回HTTP响应“300 Multiple Choices”，指示该请求无法明确解决。可用条目的列表通过HTTP或JSON返回。

此通用方案支持在以太坊名称服务（ENS，请参阅以太坊名称服务）上注册的域的名称解析。这是一个只读方案，意味着它只支持GET请求并用于从swarm中检索内容。

#### bzz-immutable

```
GET http://localhost:8500/bzz-immutable:/2477cc8584cc61091b5cc084cdcdb45bf3c6210c263b0143f030cf7d750e894d
```

与通用方案相同，但没有ENS域解析，路径的域部分需要是有效的哈希。这也是一种只读方案，但其完整性保护很明确。一个特定的bzz-immutable url将始终处理完全相同的固定不变内容。

#### bzz-raw

```
GET http://localhost:8500/bzz-raw:/2477cc8584cc61091b5cc084cdcdb45bf3c6210c263b0143f030cf7d750e894d
```

当使用bzz-raw方案响应GET请求时，swarm不会假定这是清单，而是直接返回url指定的资源。

`content_type`查询参数可以提供给指定您所请求的MIME类型，否则内容供应按默认的字节流。例如，如果你有一个PDF文件（不是包裹它的清单）位于哈希`6a182226...`然后下面的URL将正确地提供它。

```
GET http://localhost:8500/bzz-raw:/6a18222637cafb4ce692fa11df886a03e6d5e63432c53cbf7846970aa3e6fdf5?content_type=application/pdf
```

raw方案支持POST和PUT请求，对于通用方案来说非常重要且有点不同寻常。正如我们所知，这是swarm与互联网不同的一个至关重要的方式。

POST的可能性使swarm成为真正的云服务，为您的浏览带来上传功能。

事实上，命令行工具`swarm up`在底层使用了带有bzz raw方案的http代理。

#### bzz-list

```
GET http://localhost:8500/bzz-list:/2477cc8584cc61091b5cc084cdcdb45bf3c6210c263b0143f030cf7d750e894d/path
```

返回<path>路径下包含在<manifest>中的所有文件的列表，按分隔符`/`为前缀进行分组。如果路径是`/`，则返回清单中的所有文件。响应是带有`common_prefixes`字符串字段和`entries`列表字段的JSON编码对象。

#### bzz-hash

```
GET http://localhost:8500/bzz-hash:/theswarm.test
```

Swarm接受bzz-hash url方案的GET请求，并用原始内容的哈希值作出响应，这与使用bzz-raw方案的请求返回的内容相同。清单的哈希也是存储在ENS中的哈希，所以bzz-hash可以用于ENS域的解析。

响应内容类型是*text/plain*。

#### bzzr和bzzi

简称方案bzzr和bzzi被弃用，代之以bzz-raw和bzz-immutable。他们保持向后兼容性，并将在下一个版本中删除。

### 清单(manifests)

一般而言，清单声明了一个与swarm哈希关联的字符串列表。清单与一个哈希恰好相匹配，它由一系列声明内容的条目组成，内容可通过该哈希检索。让我们从一个介绍性的例子开始。

以下示例说明了这一点。让我们创建一个目录，其中包含两个黄皮书和一个列出两个pdf文档的html index文件。

```
$ ls -1 orange-papers/
index.html
smash.pdf
sw^3.pdf

$ cat orange-papers/index.html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
  </head>
  <body>
    <ul>
      <li>
        <a href="./sw^3.pdf">Viktor Trón, Aron Fischer, Dániel Nagy A and Zsolt Felföldi, Nick Johnson: swap, swear and swindle: incentive system for swarm.</a>  May 2016
      </li>
      <li>
        <a href="./smash.pdf">Viktor Trón, Aron Fischer, Nick Johnson: smash-proof: auditable storage for swarm secured by masked audit secret hash.</a> May 2016
      </li>
    </ul>
  </body>
</html>
```

我们现在使用`swarm up`命令将目录上传到swarm以创建一个小虚拟站点。

```
swarm --recursive --defaultpath orange-papers/index.html --bzzapi http://swarm-gateways.net/ up orange-papers/ 2> up.log
> 2477cc8584cc61091b5cc084cdcdb45bf3c6210c263b0143f030cf7d750e894d
```

返回的哈希值是上传内容（orange-papers目录）的清单的哈希值：

我们现在可以通过使用bzz-raw协议`bzz-raw`直接获取清单本身（而不是它们引用的文件）：

```
wget -O - "http://localhost:8500/bzz-raw:/2477cc8584cc61091b5cc084cdcdb45bf3c6210c263b0143f030cf7d750e894d"

> {
  "entries": [
    {
      "hash": "4b3a73e43ae5481960a5296a08aaae9cf466c9d5427e1eaa3b15f600373a048d",
      "contentType": "text/html; charset=utf-8"
    },
    {
      "hash": "4b3a73e43ae5481960a5296a08aaae9cf466c9d5427e1eaa3b15f600373a048d",
      "contentType": "text/html; charset=utf-8",
      "path": "index.html"
    },
    {
      "hash": "69b0a42a93825ac0407a8b0f47ccdd7655c569e80e92f3e9c63c28645df3e039",
      "contentType": "application/pdf",
      "path": "smash.pdf"
    },
    {
      "hash": "6a18222637cafb4ce692fa11df886a03e6d5e63432c53cbf7846970aa3e6fdf5",
      "contentType": "application/pdf",
      "path": "sw^3.pdf"
    }
  ]
}
```

清单包含它们引用的哈希的content_type信息。在其他情况下，如果未提供content_type，或者怀疑信息错误，则可以在搜索查询中手动指定content_type。例如，清单本身应该是*text/plain*：

```
http://localhost:8500/bzz-raw:/2477cc8584cc61091b5cc084cdcdb45bf3c6210c263b0143f030cf7d750e894d?content_type="text/plain"
```

现在，您还可以检查清单哈希是否与内容匹配（实际上swarm会为您执行此操作）：

```
$ wget -O- http://localhost:8500/bzz-raw:/2477cc8584cc61091b5cc084cdcdb45bf3c6210c263b0143f030cf7d750e894d?content_type="text/plain" > manifest.json

$ swarm hash manifest.json
> 2477cc8584cc61091b5cc084cdcdb45bf3c6210c263b0143f030cf7d750e894d
```

### 在清单上的路径匹配

清单的一个有用功能是我们可以将路径与URL匹配。在某种意义上，这使得清单成为一个路由表，因此清单swarm条目就好像它是一个主机。

更具体地说，继续我们的例子，当我们请求：

```
GET http://localhost:8500/bzz:/2477cc8584cc61091b5cc084cdcdb45bf3c6210c263b0143f030cf7d750e894d/sw^3.pdf
```

swarm首先检索与上面的清单匹配的文档。然后将url路径`sw^3`与条目进行匹配。在这种情况下，找到了完美匹配，并将6a182226 ...处的文档作为pdf返回。

正如你所看到的清单包含4个条目，虽然我们的目录只包含3个。额外的条目在那里，因为`swarm up`时的选项`--defaultpath orange-papers/index.html`，它关联到您用参数给出的空路径文件。这使得在URL路径为空时可以提供默认页面。此功能实质上实现了最常用的网络服务器重写规则，用于设置网址仅包含域时提供的网站的登录页面。所以当你请求下面的路径时

```
GET http://localhost:8500/bzz:/2477cc8584cc61091b5cc084cdcdb45bf3c6210c263b0143f030cf7d750e894d
```

你得到的索引页面（内容类型`text/html`）在`4b3a73e43ae5481960a5296a08aaae9cf466c9d5427e1eaa3b15f600373a048d`。

## 以太坊名称服务

ENS是Swarm用来允许通过人类可读名称引用内容的系统，例如“orangepapers.eth”。它的操作类似于DNS系统，将人类可读的名字翻译成机器标识符 - 在这种情况下，就是您所指的内容的swarm哈希。通过注册名称并将其设置为解析您网站的根清单的内容哈希值，用户可以通过诸如*bzz://orange-papers.eth/*之类的URL访问您的网站。

如果我们采用前面的示例并将哈希2477cc85 ...设置为域“orangepapers.eth”的内容哈希，我们可以请求：

```
GET http://localhost:8500/bzz:/orange-papers.eth/sw^3.pdf
```

并获得与以下相同的内容：

```
GET http://localhost:8500/bzz:/2477cc8584cc61091b5cc084cdcdb45bf3c6210c263b0143f030cf7d750e894d/sw^3.pdf
```

有关ENS的完整文档[可在此处获得](https://github.com/ethereum/ens/wiki)。

如果您只是想设置ENS，以便您可以将您的Swarm内容托管在一个域中，下面是一系列快速入门的步骤。

### 使用ENS检索内容

swarm的默认配置是使用在Ropsten测试网上注册的名称。为了让您能够将名称解析为swarm哈希，需要发生的一切就是您的swarm客户端连接到一个与Ropsten测试网络同步的geth节点。请参阅[这里的](http://swarm-guide.readthedocs.io/en/latest/runninganode.html#using-swarm-together-with-the-ropsten-testnet-blockchain) “运行swarm客户端”一节。

### 为swarm内容注册名称

有几个步骤涉及注册新名称并为其分配swarm哈希。首先，您需要注册一个域，然后您需要将一个解析器分配给该域，然后将swarm哈希添加到解析器。

注意

ENS系统可以让你注册甚至是无效的名字 - 例如带大写字母的名字，或禁止的Unicode字符 - 但你的浏览器永远不会解析它们。因此，在注册之前请确保您尝试注册的域名格式正确

#### 准备

第一步是下载[ensutils.js](https://github.com/ethereum/ens/blob/master/ensutils.js)（[直接链接](https://raw.githubusercontent.com/ethereum/ens/master/ensutils.js)）。

你当然应该运行并连接到ropsten（*geth -testnet*）。连接到geth控制台：

```
./geth attach ipc:/path/to/geth.ipc
```

一旦进入控制台，运行：

```
loadScript('/path/to/ensutils.js')
```

注意：您可以随时按ctrl + D离开控制台

#### 注册一个.test域

最简单的选择是注册一个[.test域](https://github.com/ethereum/ens/wiki/Registering-a-name-with-the-FIFS-registrar)。任何人都可以随时注册这些域名，但他们会在28天后自动过期。

我们将在Ropsten上发送一笔交易，所以如果您还没有这样做，先让自己获得一些ropsten测试网以太币。你可以[在这里免费得到一些](http://faucet.ropsten.be:3001/)。

在发送交易之前，您需要使用`personal.unlockAccount(account)` 解锁您的账户，也就是

```
personal.unlockAccount(eth.accounts[0])
```

然后，仍然在geth控制台（加载ensutils.js后）中键入以下内容（用您希望注册的名称替换MYNAME）：

```
testRegistrar.register(web3.sha3('MYNAME'), eth.accounts[0], {from: eth.accounts[0]});
```

注意

警告：请勿使用大写字母注册名称。ENS会让你注册它们，但你的浏览器永远不会解析它们。

输出将是一个交易哈希。一旦这个交易在测试网络上被确认（通过挖矿），您可以验证名称MYNAME.test属于您：

```
eth.accounts[0] == ens.owner(namehash('MYNAME.test'))
```

#### 注册.eth域

注册.eth域涉及更多工作。如果您只想快速测试，请从.test域开始。.eth域名需要一段时间才能注册，因为他们使用的是拍卖系统（而.test域名可以立即注册，但只能持续28天）。此外，.eth域也被限制为至少7个字符长。有关完整文档[在这里看到](https://github.com/ethereum/ens/wiki/Registering-a-name-with-the-auction-registrar)。

就像注册.test域一样，您需要测试网以太币，并且您必须解锁您的帐户。然后，您可以[开始在一个域名上出价](https://github.com/ethereum/ens/wiki/Registering-a-name-with-the-auction-registrar)。

快速参考：

1. 准备：

```
personal.unlockAccount(eth.accounts[0])
loadScript('/path/to/ensutils.js')
```

2. 出价：

```
bid = ethRegistrar.shaBid(web3.sha3('myname'), eth.accounts[0], web3.toWei(1, 'ether'), web3.sha3('secret'));
```
3. 显示您的出价：

```
ethRegistrar.unsealBid(web3.sha3('myname'), eth.accounts[0], web3.toWei(1, 'ether'), web3.sha3('secret'), {from: eth.accounts[0], gas: 500000});
```

4. 最终确定：

```
ethRegistrar.finalizeAuction(web3.sha3('myname'), {from: eth.accounts[0], gas: 500000});
```

有关如何提高出价的信息，请查看当前最高出价，查看拍卖结束时间，检查名称是否可用，请参阅[官方文档](https://github.com/ethereum/ens/wiki/Registering-a-name-with-the-auction-registrar)。

#### 设置一个解析器

下一步是为您的新域名设置一个解析器。尽管可以编写和部署您自己的自定义解析器，但对于Swarm的日常使用，可以提供通用的解析器，并且已经部署在testnet上。

在geth（testnet）控制台上：

```
loadScript('/path/to/ensutils.js')
personal.unlockAccount(eth.accounts[0], "")
ens.setResolver(namehash('MYNAME.test'), publicResolver.address, {from: eth.accounts[0], gas: 100000});
```

#### 在publicResolver上注册一个swarm哈希

最后，如上所述，将您的内容上传到Swarm之后，您可以使用以下命令更新您的网站：

```
publicResolver.setContent(namehash('MYNAME.test'), 'HASH', {from: eth.accounts[0], gas: 100000})
```

再次，用您注册的名称替换“MYNAME.test”，并用上传内容时获得的哈希（以0x开头）替换“HASH”。

在成功执行后，运行正确配置和同步的Swarm客户端的任何人都可以通过 *bzz://MYNAME.test/* 访问当前版本的网站。

```
http://localhost:8500/bzz:/MYNAME.test
```

#### 手动在ENS中查找名称

在注册你的名字和swarm哈希之后，你可以通过手动查找名字来检查是否所有内容都能正确更新。

连接到geth控制台并像以前一样加载ensutils.js。然后键入

```
getContent('MYNAME.test')
```

您也可以在swarm控制台中使用以下选项来检查它：

```
bzz.resolve('MYNAME.test')
```

如果一切正常，它会返回你之前调用setContent时指定的哈希。

#### 更新您的内容

之后每次更新网站内容时，您只需重复最后一步即可更新您拥有的名称与您希望它指向的内容之间的映射。任何通过名称访问站点的人都会看到最近使用setHash更新过的版本。

```
publicResolver.setContent(namehash('MYNAME.test'), 'NEWHASH', {from: eth.accounts[0], gas: 100000})
```

## HTTP API

- GET <http://localhost:8500/bzz:/domain/some/path>

  检索 domain/some/path 中的文档，允许域通过[以太坊名称服务](http://swarm-guide.readthedocs.io/en/latest/usage.html#ethereum-name-service)进行解析

- GET <http://localhost:8500/bzz-immutable:/HASH/some/path>

  在HASH/some/path处检索文件，其中HASH是有效的swarm哈希

- GET <http://localhost:8500/bzz-raw:/domain/some/path>

  检索domain/some/path中的原始内容，允许域通过[以太坊名称服务](http://swarm-guide.readthedocs.io/en/latest/usage.html#ethereum-name-service)进行解析

- POST <http://localhost:8500/bzz-raw>:

  post请求是最简单的上传方法。直接上传文件 - 不创建清单。它返回上传文件的哈希

- PUT <http://localhost:8500/bzz>:/HASH|domain/some/path

  PUT请求将上传的资产发布到清单。它按域或哈希查找清单，制作它的副本并使用新资产更新其集合。它返回新创建的清单的哈希。

## Swarm IPC API

Swarm在`bzz`命名空间下暴露一个RPC API 。

注意

请注意，这不是用户或dapps与swarm交互的推荐方式，仅用于调试广告测试目的。鉴于此模块提供本地文件系统访问权限，允许dapps使用此模块或通过远程连接暴露它会造成重大安全风险。出于这个原因，`swarm`只通过本地ipc暴露这个API（不像geth不允许websockets或http）。

该API提供了以下方法：

- `bzz.upload(localfspath, defaultfile)`

  上传文件或目录`localfspath`。第二个可选参数指定当空路径匹配时将被提供的文件的路径。匹配空路径通常很常见`index.html`它返回清单的内容哈希，然后可以用它来下载它。

- `bzz.download(bzzpath, localdirpath)`

  它递归地下载从manifest清单开始的所有路径，`bzzpath`并在`localdirpath`使用路径中的斜杠指示子目录下将它们下载到相应的目录结构中。假设`dirpath.orig`任何目录树的根目录不包含软链接或特殊文件，上传和下载将导致文件系统中的数据相同：bzz.download（bzz.upload（dirpath.orig），dirpath.replica）diff -r dirpath.orig dirpath.replica || 回声“相同”

- `bzz.put(content, contentType)`

  可用于将原始数据blob推送到swarm中。使用条目创建清单。此条目具有空路径并指定作为第二个参数给出的内容类型。它返回此清单的内容哈希。

- `bzz.get(bzzpath)`

  它下载清单`bzzpath`并返回一个包含内容，MIME类型，状态码和内容大小的响应json对象。这应该只用于小块数据，因为内容在内存中被实例化。

- `bzz.resolve(domain)`

  使用ENS将域名解析为内容哈希并返回。如果swarm没有连接到区块链，它会返回一个错误。请注意，您的eth后端需要同步以获得最新的域名解析。

- `bzz.info()`

  返回关于swarm节点的信息

- `bzz.hive()`

  以人性化的表格格式输出kademlia表格

### 安装群

另一种使用Swarm的方法是使用Fuse（aka swarmfs）将其作为本地文件系统。有三个IPC api可以帮助你做到这一点。

注意

需要在操作系统上安装保险丝才能使这些命令正常工作。Windows不支持Fuse，所以这些命令只能在Linux，Mac OS和FreeBSD上运行。有关操作系统的安装说明，请参阅下面的“安装FUSE”部分。

- `swarmfs.mount(HASH|domain, mountpoint))`

  将swarm或ens域名表示的swarm内容挂载到指定的本地目录。本地目录必须是可写的，并且应该是空的。一旦这个命令成功，你应该看到本地目录中的内容。HASH以rw模式安装，这意味着目录中的任何更改都会自动反映在swarm中。例如：如果您将某个文件从其他位置复制到挂载点，则等同于使用“swarm up <file>”命令。

- `swarmfs.unmount(mountpoint)`

  该命令卸载挂载在指定挂载点中的HASH |域。如果设备忙，卸载失败。在这种情况下，请确保退出正在使用该目录的进程并尝试再次卸载。

- `swarmfs.listmounts()`

  对于每个活动挂载，此命令显示三件事情。挂载点，提供启动HASH和最新的HASH。由于HASH以rw模式安装，因此当文件系统发生更改（添加文件，删除文件等）时，会计算新的HASH。这个哈希称为最新的HASH。

#### 安装FUSE

1. Linux（Ubuntu）

```
sudo apt-get安装保险丝
sudo modprobe保险丝
sudo chown <用户名>：<组名> /etc/fuse.conf
sudo chown <用户名>：<组名> / dev / fuse

```

1. 苹果系统

   从<https://osxfuse.github.io/>安装最新的软件包，或使用brew如下

```
酿造更新
brew安装caskroom / cask / brew-cask
酿造木桶安装osxfuse

```

### 支票簿RPC API

Swarm还为支票簿提供了一个RPC API，提供了followng方法：

- `chequebook.balance()`

  返回wei中交换支票簿合约的余额。如果没有设置支票簿，则会出错。

- `chequebook.issue(beneficiary, value)`

  向受益人发送支票（以太坊地址），金额为价值（以wei为单位）。返回的json结构可以被复制并发送给受益人，受益人可以使用它兑现`chequebook.cash(cheque)`。如果没有设置支票簿，则会出错。

- `chequebook.cash(cheque)`

  兑现发行的支票。请注意，任何人都可以兑现支票。其成功与否仅取决于支票的有效性和发行人的偿付能力，支票合约最多可达支票中指定的金额。这个tranasction是从你的bzz基础账户中支付的。返回交易哈希。如果没有设置支票簿，或者您的帐户没有足够的资金发送交易，则会发生错误。

- `chequebook.deposit(amount)`

  将金额从您的bzz基本账户转入您的掉期支票簿合约。如果没有设置支票簿或者您的账户资金不足，则会发生错误。

### 例如：使用控制台

#### 上传内容

可以从swarm控制台上传文件（不需要swarm命令或http代理）。控制台命令是

```
bzz.upload（“/ path / to / file /或/ directory”，“filename”）

```

该命令返回清单的根哈希。第二个参数是可选的; 它指定了空路径应该解决什么（通常这是`index.html`）。按照上述示例进行操作（[例如：上传目录](http://swarm-guide.readthedocs.io/en/latest/usage.html#example-uploading-a-directory)）。准备一些文件：

```
mkdir upload-test
echo“one”> upload-test / one.txt
echo“two”> upload-test / two
mkdir upload-test / three
回声“四”>上传测试/三/四

```

然后`bzz.upload`在swarm控制台上执行该命令:(注意`bzzd.ipc`不是`geth.ipc`）

```
./geth --exec'bzz.upload（“upload-test /”，“one.txt”）'attach ipc：$ DATADIR / bzzd.ipc

```

我们得到的结果是：

```
dec805295032e7b712ce4d90ff3b31092a861ded5244e3debce7894c537bd440

```

如果我们在浏览器中打开这个HASH

```
HTTP：//本地主机：8500 / BZZ：/ dec805295032e7b712ce4d90ff3b31092a861ded5244e3debce7894c537bd440 /

```

我们看到“one”，因为空路径解析为“one.txt”。其他有效的网址是

```
HTTP：//本地主机：8500 / BZZ：/dec805295032e7b712ce4d90ff3b31092a861ded5244e3debce7894c537bd440/one.txt
HTTP：//本地主机：8500 / BZZ：/ dec805295032e7b712ce4d90ff3b31092a861ded5244e3debce7894c537bd440 /两
HTTP：//本地主机：8500 / BZZ：/ dec805295032e7b712ce4d90ff3b31092a861ded5244e3debce7894c537bd440 /三/四

```

我们只建议将此API用于测试目的或命令行脚本。由于它们节省了http文件上传的时间，因此它们的性能比使用http API要好一些。

#### 下载内容

作为替代HTTP来获取内容，您可以使用`bzz.get(HASH)`或在群控制台上（注意不是）`bzz.download(HASH, /path/to/download/to)``bzzd.ipc``geth.ipc`

```
./geth --exec'bzz.get（HASH）'attach ipc：$ DATADIR / bzzd.ipc
./geth --exec'bzz.download（HASH，“/ path / to / download / to”）'attach ipc：$ DATADIR / bzzd.ipc

```

[下一个 ](http://swarm-guide.readthedocs.io/en/latest/architecture.html)[ 以前](http://swarm-guide.readthedocs.io/en/latest/runninganode.html)
