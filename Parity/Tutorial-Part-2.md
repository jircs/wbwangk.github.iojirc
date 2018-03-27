# DApp教程

**这是Dapp教程的第二部分。**我们假设您熟悉过去章节的内容。

| [⟵第一部分](https://wiki.parity.io/Tutorial-Part-1.html) | [第三部分⟶](https://wiki.parity.io/Tutorial-Part-3.html) 

## oo7债券

现在我们有了基本的dapp，我们可以开始引入更多有趣的功能。不用太多，让我们开始吧。前往`src/client/scripts/app.jsx`。你会看到我们的基本文件：

```
import React from 'react';

export class App extends React.Component {
	render() {
		return (
			<div>Hello world!</div>
		);
	}
}
```

这是一个[JSX](https://facebook.github.io/jsx/)文件。基本上，这意味着它可以在描述如何呈现React组件时处理嵌入式HTML（以及更精确的XHTML方言）。如果这对你来说都是新的，你可能需要[熟悉这项技术](http://www.hackingwithreact.com/read/1/3/introduction-to-jsx)。现在，我会假设你知道或不关心。

### 1.你的第一个 `Bond`

我们要做的第一件事是介绍[oo7库](https://github.com/paritytech/oo7)。这将JavaScript引入了被称为“bond”的响应值的概念。[响应值](https://en.wikipedia.org/wiki/Reactive_programming)与正常的“变量”类似，区别在于它们使用的任何地方都会随着值的变化自动更新。可以将它们`map`组合成任意复杂的表达式，然后放置在UI组件中以创建毫不费力的动态UI。

oo7包仍然在开发中，因此在本教程中可能会有一两个粗糙的边缘。Mea culpa。确保您使用最新的软件包运行。如果你仍然有麻烦，请来找我[gitter](https://gitter.im/paritytech/parity) :-)

**我们的第一个例子将通过将可编辑文本框的内容动态复制到一个<span>中，来演示Bond如何引入反应性。**

通过在以下位置放置三行来导入所需的组件`app.jsx`：

```
import {Bond} from 'oo7';
import {Rspan} from 'oo7-react';
import {InputBond} from 'parity-reactive-ui';
```

接下来，我们需要引入一个新的实例`Bond`。它将表示文本字段的当前内容。我们将在类的构造函数中初始化它。将这些行直接插入到`App`类声明中。

```
constructor() {
	super();
	this.bond = new Bond();
}
```

你的代码现在应该是这样的：

```
import React from 'react';
import {Bond} from 'oo7';
import {Rspan} from 'oo7-react';
import {InputBond} from 'parity-reactive-ui';

export class App extends React.Component {
	constructor() {
		super();
		this.bond = new Bond();
	}
	render() {
		return (
			<div>Hello world!</div>
		);
	}
}
```

接下来，我们需要创建文本输入框和 `<span>`元素（文本字段的内容将反映在其中）。我们将使用一个语义UI版本的`Input`元素，以将该值传播到一个命名的`Bond`。这叫做`InputBond`。同样，对于`<span>`，我们将使用一个特殊的“响应”版本的`span`元素，它能够接受`Bond`作为子元素和某些属性的值; 这被称为`Rspan`。我们以前都导入过。

将该`<div>Hello world</div>`行更改为：

```
<div>
	<InputBond bond={this.bond} placeholder="Go ahead and type some text"/>
	<Rspan>{this.bond}</Rspan>
</div>
```

正如你所见，这里并没有那么多。我们只是告诉文本字段输入`InputBond`将其值放入`this.bond`并反过来告诉`Rspan`显示该值`this.bond`。

运行Webpack并让它监视你的文件，以确保你的dapp不断重建。我们为您定义了一个别名来执行此操作：

```
npm run build    (即npx webpack)
npm start    (即npx http-server dist)
```

在Parity钱包中重新加载我们的dapp页面将提供一个简单的表单; 选择文本框并键入内容。无论您键入什么，您都会看到它反映在旁边的`<Rspan>`元素上：

![图片](https://cloud.githubusercontent.com/assets/138296/22694357/e9eae790-ed14-11e6-898b-932b56847a18.png)

### 2.转换`Bond`s

**Bond不只是传递数据，它们也可以表示对数据的转换。** 文本转换的一个例子是简单的大小写转换。转为大写文本的函数是`text => text.toUpperCase()`。我们可以通过这个函数`map` Bond，使`<Rspan>`显示在该字段任何输入的大写字母：

```
<Rspan>{this.bond.map(t => t.toUpperCase())}</Rspan>
```

重新加载并运行：

![图片](https://cloud.githubusercontent.com/assets/138296/22694526/9f1bf442-ed15-11e6-9e46-f3752f479b76.png)

### 3.重新使用`Bond`s

现在我们只有一个“用户” `this.bond`，但实际上`Bond`s可以根据需要使用和重用。让我们`this.bond`用来为我们创建一个样式`<Rspan>`，以便根据我们输入的文字介绍颜色。`<Rspan>`如果我们输入了一个简单的数字，我们的意志就会变成红色，在其他情况下，我们的意志变成黑色

将您的`<Rspan>`路线替换为：

```
<Rspan style={this.bond.map(t => t.match(/^[0-9]+$/) ? {color: 'red'} : {color: 'black'})}>
	{this.bond.map(t => t.toUpperCase())}
</Rspan>
```

该代码是相当简单：要得到`style`的`<Rspan>`，我们`map`我们的`this.bond`到`{color: 'red'}`或`{color: 'black'}`其中之一，取决于它是否与正则表达式`/^[0-9]+$/`匹配。

我们的Bond在两个不同的映射中被使用了两次，但所有的工作都如你所期望的那样：

![图片](https://cloud.githubusercontent.com/assets/138296/22694918/fa77761c-ed16-11e6-9d18-7431c79eceb3.png)

虽然上面的内容可能已经*足够*清晰，但是敏锐的读者可能会想知道是否有一种办法[复用](https://en.wikipedia.org/wiki/Don%27t_repeat_yourself)`{color: ...}`，改变这个：

```
this.bond.map(t => t.match(/^[0-9]+$/) ? {color: 'red'} : {color: 'black'})
```

...为这个：

```
{color: this.bond.map(t => t.match(/^[0-9]+$/) ? 'red' : 'black')}
```

当然，这两者非常不同。前者无疑是一个`Bond`，后者是一个简单的对象，它恰好具有`Bond`其中一个值。事实上，后者确实有效。**为了方便起见，不仅可以直接识别响应值，而且可以在数组或对象字段的值内识别这些值。** 为了提高效率，这只能达到一个深度。任何进一步进入对象结构的内容都将被忽略（然而，有些API会改变你希望`Bond`识别的深度）。

### 4.结合`Bond`s

到目前为止，我们只用一个`Bond`来标记我们的`<Rspan>`。如果我们想使用几个呢？这也适用。

我们可以在构造函数中初始化另一个`Bond`，创建另一个`InputBond`输入字段并在`span`内容中使用它们，但这会有点相似。相反，我们将使用内置`Bond`中的一个：`TimeBond`。

首先，从`oo7`导入`TimeBond`，在`Bond`旁边：

```
import {Bond, TimeBond} from 'oo7';
```

然后在构造函数中引入它：

```
constructor() {
	super();
	this.bond = new Bond();
	this.time = new TimeBond();
}
```

最后，让我们在`<Rspan>`中使用它。将多个`Bond` 的值组合成单个表达式有多种方法。现在我们将使用最简单的：`Bond.all`。这个函数允许你提供一些表达式，并且将对所有这些表达式求值后存入一个数组。这意味着`Bond.all([this.bond, this.time])`将是一个*组合*`Bond`，它的值是一个[JS数组](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/array)，第一项是我们文本字段的内容，第二项是时间（作为数字）。

尝试一下。用下面这个新的替换`<Rspan>`：
```
<Rspan style = {{color：this.bond.map（t => t.match（/ ^ [0-9] + $ /）？'red'：'black'）}}> {Bond.all（[ this.bond，this.time]）} </ Rspan>
```
输入一个适当的消息，你就会得到：

![image](https://cloud.githubusercontent.com/assets/138296/22697591/e779b292-ed1f-11e6-8beb-2ff654e6ac02.png)

当时间该表它会自动更新。 因为时间变化很快，及时处理代价昂贵，我们选择了一个现实的解决方案，仅仅每秒更新`TimeBond`。

首先格式化时间可以让它更好一点：

```jsx
<Rspan style={{ color: this.bond.map(t => t.match(/^[0-9]+$/) ? 'red' : 'black') }}>
	{Bond.all([this.bond, this.time]).map(([msg, t]) => `${new Date(t)}: ${msg}`)}
</Rspan>
```

![图片](https://cloud.githubusercontent.com/assets/138296/22697729/62243e2c-ed20-11e6-931a-1693dd865837.png)

为了使代码更具可读性，我们将把颜色处理和时间格式化抽象为不同的函数。我们的代码现在看起来像这样：

```
import React from 'react';
import {Bond, TimeBond} from 'oo7';
import {Rspan} from 'oo7-react';
import {InputBond} from 'parity-reactive-ui';

const computeColor = t => t.match(/^[0-9]+$/) ? {color: 'red'} : {color: 'black'}
const format = ([msg, t]) => `${new Date(t)}: ${msg}`

export class App extends React.Component {
	constructor() {
		super();
		this.bond = new Bond();
		this.time = new TimeBond();
	}
	render() {
		return (
			<div>
				<InputBond
					bond={this.bond}
					placeholder="Go ahead and type some text"
				/>
				<Rspan
					style={this.bond.map(computeColor)}
				>
					{Bond.all([this.bond, this.time]).map(format)}
				</Rspan>
			</div>
		);
	}
}
```

这应该给了你一个很好的`Bond`介绍，以及如何在简单的React对象中使用它。接下来，我们将看看Parity通用Bond API。

------

[第三部分⟶](https://wiki.parity.io/Tutorial-Part-3.html)
