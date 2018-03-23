# DApp教程

**这是Dapp教程的第一部分**。

[第二部分⟶](https://github.com/paritytech/parity/wiki/Tutorial-Part-2)

本教程将引导您制作一个简单的基于以太坊的分布式应用程序。到最后，你将能够进入Parity，选择你的Dapp并在实践中看到它。

------

## 入门

### 1.产成一个新的Dapp

我们的dapp将使用现代JS技术; [NPM](https://www.npmjs.com/)，[WebPack 2](https://webpack.js.org/)，[React](https://facebook.github.io/react/)，[Babel](https://babeljs.io/)，ES6，JSX和[oo7](https://github.com/paritytech/oo7)。

所有这些都需要花费时间和耐心才能在Javascript中获得工作成果。我们会走捷径，仅克隆一个存储库就准备好了一切。首先，如果你的系统上没有安装Git，node.js，NPM或Webpack，那就装上它们（可以在Ubuntu上运行`sudo apt-get install git npm`）。

接下来，克隆我们准备的`skeleton`库：

```
git clone https://github.com/paritytech/skeleton mydapp
```

这将给您一个全新的库`mydapp`，准备好就可以运行。我们用`cd`进入它，删除原始库，以免混淆Git：

```
cd mydapp
git remote rm origin
```

它是自由许可的（Apache 2.0），所以你不必担心自己的代码开放源代码（尽管显然你会开明，并希望无论如何这样做:-)）。如果你决定在Github上创建一个，你现在可以自由地推送到你自己的Git仓库。

下一个阶段是安装所有的依赖关系。NPM使这很容易，但捆绑的脚本使它更容易！只要运行：

```
./init.sh
```
> (执行init.sh出错,不得不手工执行`npm i --save babel-core webpack webpack-dev-server`后再执行init.sh成功)

这会抓取并安装所需的所有东西。接下来要做的是构建基础dapp的web可用版本。我们为此使用[webpack](https://webpack.js.org/) ; 它会把所有东西都粉碎在一起，并为你提供一个单独`bundle.js`（放在`dist`路径下），`index.html`（已经在那里）会加载它。

```
webpack
```

你现在已经建立了一个基本的dapp。做得好！

> (经测试,这个项目`paritytech/skeleton`需要旧版本的依赖才可以。打开init.sh，修改到指定版本：webpack@2.1.0-beta.22、babel-loader@6.2.5。全局安装时也是这样：`npm i webpack@2.1.0-beta.22 -g`)

### 2.配置它的外观

虽然你的dapp可能很好建造，但它不容易被发现。你将不得不将它托管在某个地方。这可以通过传统的Web服务器完成，但为了开发，我们将使用Parity的内置主机。

Parity使用一个特殊的“manifest”文件来指出如何在Parity钱包中显示您的dapp条目。这个文件叫`manifest.json`，并且必须放在你的dapp目录的根目录下，在我们的例子中，这是我们的“build”目录，即`dist`。

打开编辑器进行编辑`dist/manifest.json`; 你会看到如下所示的内容：

```
{
	"id": "skeleton",
	"name": "Skeleton",
	"description": "A skeleton dapp",
	"version": "0.1",
	"author": "Parity Technologies Ltd",
	"iconUrl": "title.png"
}
```

- 这`id`是dapp的唯一标识; 将`skeleton`改成`mydapp`（或者其他）。

- `name`是dapp的用户可见名称：将其更改为`My Dapp`（或其他）。

- 这`description`是用户可见的副标题，描述了dapp的优点。将其更改为`My demonstration dapp`。

- `version`是dapp的版本 - 你可以暂时设置为`0.1`。

- 你可以改变`author`你的名字。

- `iconUrl`是一个代表dapp的正方形（最好是128x128）图标的路径（`dist`内部）。随意将替代品`title.png`移动到`dist`目录中。

### 3.在Parity中显示它

为了让Parity发现你的dapp，它需要被放置在Parity能看到的地方 - 它的本地“dapps”目录。我们将在Parity的dapp目录中为dapp的`dist` 目录（包含所有准备构建的dapp）创建一个符号链接。

Parity的目录结构因系统而异。对于Mac，Parity的本地dapp目录位于`$HOME/Library/Application Support/io.parity.ethereum/dapps`，因此您需要输入：

```
# For Mac systems
ln -s $PWD/dist $HOME/Library/Application\ Support/io.parity.ethereum/dapps/mydapp
```

对于Linux而言是`$HOME/.local/share/io.parity.ethereum/dapps`- 在这种情况下，您需要输入：

```
# For Linux systems
ln -s $PWD/dist $HOME/.local/share/io.parity.ethereum/dapps/mydapp
```
> (测试时使用了定制的parity路径，所以：`ln -s $PWD/dist $HOME/parity/dapps/mydapp`)

对于Windows，它在`%APPDATA%/Parity/Ethereum/dapps`- 你要输入：

```
%=For Windows systems=%
mklink /D "%APPDATA%/Parity/Ethereum/dapps/mydapp" "%cd%/dist"
```

创建符号链接后，您应该启动（或重新启动，如果已经运行）Parity并转到Parity钱包的Applications页面。你会看到你的新的dapp：

*注意：如果你没有在parity / src / js中运行`npm start`来启动一个开发实例，你的URL可能类似于**localhost：8180**而不是3000端口。*

*注意：它可能发生在旧版Parity版本上，由于X-Frame-Options，Firefox会阻止你的dApp。您将在开发控制台中看到警告。我们建议继续使用**127.0.0.1:8180**来解决问题。*

![图片](https://cloud.githubusercontent.com/assets/138296/22697933/f9d6449a-ed20-11e6-92d2-1afafaba86ea.png)

如果一切正常，你的dapp就会像这样可见。点击它！

![图片](https://cloud.githubusercontent.com/assets/138296/22697890/e1677726-ed20-11e6-9a64-c1832d2c36bf.png)

那就是 - 你的dapp。目前，它只是显示一条简单的消息，但它在这个系列的下一部分中将会改变:-)

------

[第二部分⟶](https://wiki.parity.io/Tutorial-Part-2.html)
