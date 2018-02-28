# DApp教程

**这是Dapp教程的第三部分。**我们假设您熟悉过去章节的内容。

| [⟵第二部分](https://wiki.parity.io/Tutorial-Part-2.html) | [第四部分⟶](https://wiki.parity.io/Tutorial-Part-4.html) |

------

## Parity Bonds

现在我们对在响应式用户界面中使用`Bond` 的想法感到非常满意，现在是时候深入区块链了。为此，我们将介绍这个`oo7-parity`包，它提供了一个高级别的响应式`Bond`API（它使用我们底层的低级库*parity.js*，但您无需关心）。

要进行设置，我们所需要做的就是从`oo7-parity`模块导入`bonds`，所以确保你在文件顶部有这个：

```
import {bonds} from 'oo7-parity';
```

### 1.监视区块

对于我们的第一招，我们将介绍所有bond中最简单的：`bonds.height`。这将计算出[最新块的编号](https://github.com/paritytech/parity/wiki/JSONRPC-eth-module.md#eth_blocknumber)，用一个简单数字表示。

在中`app.jsx`，删除整个`App`类和两个`const`类，并将其替换为：

```
export class App extends React.Component {
	render() {
		return (
			<Rspan>{bonds.height}</Rspan>
		);
	}
}
```

你应该已经熟悉了基本结构。如果您尝试此操作，您会看到一个跟踪区块链最新高度的数字：

![图片](https://cloud.githubusercontent.com/assets/138296/22700240/ee7d922c-ed27-11e6-8976-25acd1ffdecb.png)

这不是特别漂亮，但你明白了。我们可以很容易地将渲染表达式更改为：

```
<div>
	Current block is:
	&nbsp;
	<Rspan style={{fontWeight: 'bold'}}>
		{bonds.height.map(formatBlockNumber)}
	</Rspan>
</div>
```

我们还需要提供以下`formatBlockNumber`函数：

```
const formatBlockNumber = (n) => '#' + ('' + n).replace(/(\d)(?=(\d{3})+$)/g, "$1,");
```

令人高兴的是，这是由[`oo7-parity`](https://github.com/paritytech/oo7-parity)包提供的，所以你可以选择只导入它：

```
import {bonds, formatBlockNumber} from 'oo7-parity';
```

变成了这样：

![图片](https://cloud.githubusercontent.com/assets/138296/22700625/44b820f2-ed29-11e6-8fae-125303b677ce.png)

### 2.区块

区块编号非常大，但也许我们对最新的区块本身更感兴趣。令人高兴的是，Parity可以通过`bonds.blocks`对象帮助我们。这是一个可以订阅的bond的被动求值“数组”。为了让自己了解其函数，让我们在控制台中尝试一下。首先我们将`bonds`对象暴露给环境，方法是在对象的构造函数的末尾添加这个对象：

```
window.bonds = bonds;
```

重新加载后，快速打开Chrome JS控制台，将环境改为127.0.0.1，并使用`bonds.blocks[69].log()`计算69号区块：

![图片](https://cloud.githubusercontent.com/assets/138296/22701287/41c6e4f8-ed2b-11e6-94d9-d6b5e58cb911.png)

注意，因为它全是异步的，所以我们必须使用这个`.log()`技巧来将结果提供给控制台（它完全等同于`.map(console.log)`）。当然，结果是代表该链上第69个被开采的区块对象。

自然地，`bonds.blocks`能够接受任何数字，即使是一个bond，作为其下标。让dapp总是给我们最新区块的时间戳。

```
<div>
	Latest block's timestamp is:&nbsp;
	<Rspan style={{fontWeight: 'bold'}}>
		{bonds.blocks[bonds.height].map(b => b.timestamp).map(_ => _.toString())}
	</Rspan>
</div>
```

![图片](https://cloud.githubusercontent.com/assets/138296/22701622/26a80d68-ed2c-11e6-8720-946e311a9b34.png)

这个`.map`有点麻烦。方便的是，`Bond`API知道其下标，这意味着它`.timestamp`可以被移出`map`。（`toString`是一个保留的表达式，所以它必须保持`map`ped，但是一般情况下，你会发现你很少要使用这样的钝化转换。）

此外，这`bonds.blocks[bonds.height]`是一个相当普遍的表达。以至于它有一个更短的别名：`bonds.head`，所以实际上最简单的表达方式是：

```
{bonds.head.timestamp.map(_=>_.toString())}
```

### 3.撰写表达式

这些表达式有时非常有用，但通常你想对这些信息做一些基于区块链的计算。例如，获取最近区块的作者（“矿工”）很容易（`block.author`），但使用可能受限; 实际上你不仅知道他们的身份，还想知道他们的账户余额。

Parity提供各种手段来帮助你：

- `bonds.balance(address)`计算当前的账户余额`address`。

- `bonds.transactionCount(address)`计算账户的当前交易计数（“nonce”）`address`。

- `bonds.code(address)`计算`address`账户的当前“合约”代码。

- `bonds.storageAt(address, location)`计算`address`账户的`location`存储的值。

我们在dapp中的首先显示最近的区块作者（“矿工”）的账户余额 - 我们需要的表达式是`bonds.balance(bonds.head.author)`。单独留下它有点难看，所以我们会用以下`oo7-parity`暴露的`formatBalance`函数对它进行一些美化：

```
<div>
	Current block author's balance is:&nbsp;
	<Rspan style={{fontWeight: 'bold'}}>
		{bonds.balance(bonds.head.author).map(formatBalance)}
	</Rspan>
</div>
```

通过用下面内容替换行`import {formatBlockNumber} from 'oo7-parity';`来确保你有`formatBalance`可用：

```
import {bonds, formatBlockNumber, formatBalance} from 'oo7-parity';
```

![图片](https://cloud.githubusercontent.com/assets/138296/22704760/d7468bcc-ed36-11e6-8411-320791d107e8.png)

你会看到它随着区块的增长而更新。

### 我们的余额

Parity可以帮助我们以响应的方式获取有关我们自己账户的信息。提供了几个API：

- `bonds.coinbase` 计算节点的当前区块的创作者地址。

- `bonds.accounts` 计算到可供dapp使用的账户列表。

- `bonds.me`计算该dapp使用的首选账户。在Parity中，这是在页面右下角的Parity签名器中可见的账户。

- `bonds.accountsInfo` 计算到dapp可见地址和账户元数据之间的映射。

要查看可用账户的列表，我们可以将账户用`', '`连接起来：

```
<div>
	Accounts available:&nbsp;
	<Rspan>
		{bonds.accounts.map(_=>_.join(', '))}
	</Rspan>
</div>
```

确定首选账户的地址非常简单：只是`bonds.me`。切换`render`函数以使用它：

```
<div>
	Default account:&nbsp;
	<Rspan>{bonds.me}</Rspan>
</div>
```

试试这个并使用Parity签名器的账户选择器来更改账户：您会看到地址更改！

![图片](https://cloud.githubusercontent.com/assets/138296/22710016/cad4cda0-ed49-11e6-9257-4dbd1885dc13.png)

...然后选择另一个账户给...

![图片](https://cloud.githubusercontent.com/assets/138296/22710003/c4168634-ed49-11e6-94af-177683da719d.png)

也许我们想知道我们在选定的账户中有多少。这可以很容易地完成：

```
<div>
	Default account:&nbsp;
	<Rspan>{bonds.me}</Rspan>
	<br />With a balance of&nbsp;
	<Rspan>
		{bonds.balance(bonds.me).map(formatBalance)}
	</Rspan>
</div>
```

![图片](https://cloud.githubusercontent.com/assets/138296/22710059/f1572b8a-ed49-11e6-874a-7de1ff2ca519.png)

也许我们想知道给该账户的名称（如果有的话）。我们可以通过`bonds.accountsInfo`bond来确定这一点，从而为我们提供账户和元数据之间的映射。因此，计算当前账户名称的表达式就如此简单如`bonds.accountsInfo[bonds.me].name`。

我们的渲染函数现在是：

```
<div>
	Default account:&nbsp;
	<Rspan>{bonds.me}</Rspan>&nbsp;
	<br/>Given the name of&nbsp;<Rspan>
		{bonds.accountsInfo[bonds.me].name}
	</Rspan>
	<br/>With a balance of&nbsp;
	<Rspan>
		{bonds.balance(bonds.me).map(formatBalance)}
	</Rspan>
</div>
```

并应该提供如下内容：

![图片](https://cloud.githubusercontent.com/assets/138296/22709662/8e12672a-ed48-11e6-96a3-be0065ff8cbe.png)

如果您继续操作并更改默认账户，或者只是在Parity钱包中修改其名称，您将看到该页面会自动跟踪所做的更改，就像您期望的那样。

您现在应该熟悉Parity中的核心bond，并乐意将它们组合成更复杂的表达式。接下来，我们将看看我们如何使用`Bond`与位于区块链中的合约进行交互。

------

[第四部分⟶](https://wiki.parity.io/Tutorial-Part-4.html)
