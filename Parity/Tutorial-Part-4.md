# DApp教程

**这是Dapp教程的第四部分。**我们假设您熟悉过去章节的内容。

| [⟵第三部分](https://wiki.parity.io/Tutorial-Part-3.html) | [第五部分⟶](https://wiki.parity.io/Tutorial-Part-5.html) |


## 调用合约

您现在应该熟悉了`Bond`s和一些核心Parity bond API。接下来，我们将把这些知识用于更复杂的表达式，这些表达式将以合约为特色。

合约API基本上分为三部分。首先，有一些改变状态的交易，如将代币转让给对方。其次，当发生这种状态变化时，通常会发生事件接收和报告。最后，通过调用`constant`函数检查合约状态。现在我们将限制自己到后者。

### 我们的第一份合约

我们将要处理的第一份合约是全局（名称）注册库。如果你还不熟悉，这是一个存在于所有明智区块链上的注册表，它记录任何所需名称的信息字段。注册库记录所有权信息，以便日后可以注册名称和更改其信息。

注册库合约有一个相当简单的API用于查看。我们只需要关注两个函数：

- `getOwner(bytes32) -> address` 给定一个名称的Keccak哈希，如果它已被保留，则返回其拥有者的地址。

- `getData(bytes32, bytes32) -> bytes32` 给定一个名称的Keccak哈希和第二个字段key，这将返回关联的数据。

有几个后者的从属函数，如`getAddress`和`getUint`，将数据强制转换为一些其它类型的。需要注意的是，标准化的字段key是：

- `A`，该`address`主要与该名称相关联; 如果你想把资金发送到名称上，这是发送给他们的地方（假设它不为空）。

- `CONTENT`（`bytes32`），这等于与这个名称关联的任何内容的Keccak哈希值。例如，如果名称代表一个dapp，那么这将是dapp内容的哈希。

- `IMG`（`bytes32`），它等于关联图像的Keccak散列; 这可能是一个人的头像或dapp图标，具体取决于所命名的内容。

我们首先显示与名称`'gavofyork'`关联的地址。为此，我们需要创建一个特殊的`Bond`-API合约对象。做这个的函数是`bonds.makeContract`; 它采用合约的地址和ABI，并为每个合约的函数返回`Bond`一个具有返回函数的对象。(待续===============)

地址很容易找到; 这可以通过`parity.api.parity.registry`呼叫来确定。ABI规范相当长，可以从ethcore / contracts存储库中提供的合约代码中派生。由于只有一个规范注册表，所以Parity可以方便地为您构建，并将其提供给`bonds.registry`对象。

要找出`gavofyork`名称的主要关联地址，我们可以使用该`getAddress`呼叫，并将`parity.api.util.sha3`呼叫与我们名称的Keccak哈希一起使用。完整的表达方式是：

```
bonds.registry.getAddress(parity.api.util.sha3('gavofyork'))

```

`parity.api.util.sha3(...)`每次你想在注册表中查找一个名字时都要打字，这很快就会变得乏味。令人高兴的是，奇偶校验提供了许多衍生物辅助功能作为一部分`bonds.registry`对象：`lookupData`，`lookupAddress`，`lookupUint`和`lookupOwner`; 他们都像`get`前缀的弟兄，但是为你做哈希。我们的表达因此可以变成：

```
bonds.registry.lookupAddress('gavofyork', 'A')

```

让我们来看看我们的dapp。将`render()`ed HTML 更改为：

```
<div>
	gavofyork's address is&nbsp;
	<Rspan>{bonds.registry.lookupAddress('gavofyork', 'A')}</Rspan>
</div>

```

刷新你的dapp页面，假设你在Kovan上运行，你会看到类似于：

![图片](https://cloud.githubusercontent.com/assets/138296/22712813/2e36a65c-ed54-11e6-896d-c123bd95d3d5.png)

### 2.动态查找

现在这一切都很好，但也许你不仅仅对我的账户感兴趣，而是希望允许用户输入他们想要的任何账户。bond可以让你轻松：

```
export class App extends React.Component {
	constructor() {
		super();
		this.bond = new Bond;
	}
	render() {
		return (
			<div>
				Address of <InputBond bond={this.bond} placeholder='Lookup a name' /> is:<br/>
				<Rspan>{bonds.registry.lookupAddress(this.bond, 'A')}</Rspan>
				, it's balance is <Rspan>
					{bonds.balance(bonds.registry.lookupAddress(this.bond, 'A')).map(formatBalance)}
				</Rspan>
			</div>
		);
	}
}

```

在这里，我们重写了组件，以包含一个新的`Bond`，通过`InputBond`我们用来表示文本输入字段中的当前文本。我们将这个`lookupAddress`函数传递给该函数，将其转换`Bond`为与注册表中该名称的地址等效的值，并将其用作`value`反应`Hash`显示组件的参数。我们还将它与其结合使用`bonds.getBalance`以显示账户的格式化余额。

这是它的样子：

![图片](https://cloud.githubusercontent.com/assets/138296/22713122/2c8146e0-ed55-11e6-8809-c5329cf89bae.png)

如果您当前查找的名称碰巧发生了地址更改，或者它们的余额发生了变化，那么您当然会看到实时反映的这些详细信息。

### 3.衍生合约

目前为止这么好，但是虽然注册合约很有趣，但通常不是最终目的地。通常，注册表用于查找您实际想要使用的第二个合约的地址。

假设第二份合约是GithubHint合约; 如果您还不熟悉，GithubHint合约允许您建议哪些URL可以为特定哈希提供内容。这是一个半集中式的，可以替代内容可寻址传送系统的替代方案，如BitTorrent / Kademlia，Swarm和IPFS。我们在Parity中广泛使用它作为内容传播的一种手段。

由于它是Parity中的“标准”合约，因此它的ABI可在`oo7-parity`as中使用`GitHubHintABI`。每个链的地址更改，但可以通过名称下的注册表来发现`'githubhint'`; 该表达式因此将是`bonds.registry.lookupAddress('githubhint', 'A')`。

关于这个`makeContract`功能的一个重要的事情是，它不需要一个“简单”地址的合约，但实际上可以使用一个`Bond`地址; 如果`Bond`评估结果发生变化，所有东西都会神奇地反应。

因此我们的GithubHint合约对象可以用下面的表达式创建：

```
bonds.makeContract(
	bonds.registry.lookupAddress('githubhint', 'A'),
	GitHubHintABI);

```

虽然我们不能忘记进口`GitHubHint`：

```
import {GitHubHintABI} from 'oo7-parity';

```

GithubHint合约只有一种检查方法：`entries`。这需要`bytes32`（内容的散列被发现）并返回三个项目（通过数组）。有三种入口; Github存储库条目，第一个和第二个条目构成特定存储库特定提交的地址; 一般URL，其中第一项是一个URL，第二项是空散列; 和两个项目都为空的空项目。第三项始终是条目的所有者（如果有）以及唯一能够更改提示信息的账户。

在这个小型演示中，我们假设我们只查找网址，因此只对第一项感兴趣。因此，我们需要一个表达式：

```
GithubHint.entries(hash)[0]

```

哪里`hash`是一些内容散列并且`GithubHint`是合约对象。

综合起来，我们可以将我们的dapp改为：

```
export class App extends React.Component {
	constructor() {
		super();
		this.bond = new Bond;
		this.GithubHint = bonds.makeContract(bonds.registry.lookupAddress('githubhint', 'A'), GitHubHintABI);
	}
	render() {
		return (
			<div>
				URL for content <HashBond bond={this.bond} floatingLabelText='Content-hash' /> is:<br/>
				<Rspan>{this.GithubHint.entries(this.bond)[0]}</Rspan>
			</div>
		);
	}
}

```

请注意，我们使用的`HashBond`是`oo7-react`而不是`InputBond`。这只是确保我们只输入有效的32字节散列。确保导入行更改为：

```
import {InputBond, HashBond} from 'parity-reactive-ui';

```

当你刷新页面时，输入你知道有一些URL提示的内容的散列，例如：

`0xd40679a3a234d8421c678d64f4df3308859e8ad07ac95ce4a228aceb96955287`

然后观看URL出现！

![图片](https://cloud.githubusercontent.com/assets/138296/22715524/3f1545cc-ed5f-11e6-9536-f7fd9fed423d.png)

### 进一步改进

让我们显示与注册名称相关联的图像 - 我们想打字`gavofyork`并让我的杯子出现。首先，我们需要从包中导入`img`元素（`Rimg`组件）的反应版本`oo7-react`，因此我们应该更改`oo7-react`导入行：

```
import {Rspan, Rimg} from 'oo7-react';

```

接下来，让我们改变dapp的渲染`div`：

```
<div>
	<InputBond bond={this.bond} placeholder='Name' />
	<Rimg src={this.GithubHint.entries(bonds.registry.lookupData(this.bond, 'IMG'))[0]} />
</div>

```

这里唯一有趣的事情是我们正在查找`IMG`输入名称的输入，将其传入GithubHint并从结果中获取URL项目。

打开并输入具有`IMG`字段（例如`gavofyork`）的条目的名称：

![图片](https://cloud.githubusercontent.com/assets/138296/22715677/1e3e2be2-ed60-11e6-9609-16475996e7f3.png)

经过足够的时间下载，你应该看到头像！

现在您已熟悉如何检查合约状态，在下一篇教程中，我们将着眼于状态变化的API，首先进行简单的事务处理。

------

[第五部分⟶](https://wiki.parity.io/Tutorial-Part-5.html)
