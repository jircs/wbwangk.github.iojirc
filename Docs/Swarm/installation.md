# 安装和更新

## 安装

Swarm是以太坊栈的一部分，参考实现目前在POC（概念验证）版本0.2。

源代码可在github上找到：[https](https://github.com/ethereum/go-ethereum/tree/master/)：[//github.com/ethereum/go-ethereum/tree/master/](https://github.com/ethereum/go-ethereum/tree/master/)

## 支持的平台

Geth运行在所有主流平台上（Linux，MacOSX，Windows，Raspberry Pi，Android OS，iOS）。

注意

此软件包未在除Linux和OSX以外的平台上测试过。

## 先决条件

构建swarm守护进程**swarm**需要以下包：

- go：[https://golang.org](https://golang.org/)
- git：[http://git.org](http://git.org/)

达到相关的先决条件并从源头上构建。

在Linux上（ubuntu / debian变体）使用`apt`安装go和git

```
sudo apt install golang git
```

而在Mac OSX上，您可以使用**brew**

```
brew install go git
```

然后，您必须按照以下步骤准备好您的环境

```
mkdir ~/go
export GOPATH="$HOME/go"
echo 'export GOPATH="$HOME/go"' >> ~/.profile
```

## Ubuntu的

Ubuntu存储库带有旧版本的Go。

Ubuntu用户可以使用'gopher'PPA安装Go的最新版本（首选版本为1.7或更高版本）。有关更多信息，请参阅<https://launchpad.net/~gophers/+archive/ubuntu/archive>。请注意，此PPA需要将/usr/lib/go-1.X/bin添加到可执行文件路径。

其他发行版

下载最新的发行版

```
curl -O https://storage.googleapis.com/golang/go1.9.2.linux-amd64.tar.gz
```

将它解压到/usr/local（可能需要sudo）

```
tar -C / usr / local -xzf go1.9.2.linux-amd64.tar.gz
```

设置GOPATH和PATH

为了正常工作，您需要设置以下两个环境变量：

创建一个go文件夹

```
mkdir -p ~/go; echo "export GOPATH=$HOME/go" >> ~/.bashrc
```

更新你的路径

```
echo "export PATH=$PATH:$HOME/go/bin:/usr/local/go/bin" >> ~/.bashrc
```

将环境变量读入当前会话：

```
source ~/.bashrc
```

## 从源代码安装

一旦满足所有先决条件，请下载以太坊源代码

```
mkdir -p $GOPATH/src/github.com/ethereum
cd $GOPATH/src/github.com/ethereum
git clone https://github.com/ethereum/go-ethereum
cd go-ethereum
git checkout master
go get github.com/ethereum/go-ethereum
```

最后编译swarm守护进程`swarm`和主要的以太坊客户端`geth`。

```
go install -v ./cmd/geth
go install -v ./cmd/swarm
```

你现在可以运行**swarm**来启动你的swarm节点。让我们来检查swarm的安装

```
$GOPATH/bin/swarm version
```

应该给你一些相关的信息

```
Swarm
Version: 0.2
Network Id: 0
Go Version: go1.9.2
OS: linux
GOPATH=/home/user/go
GOROOT=/usr/local/go
```

## 更新您的客户端

要更新您的客户端，只需下载最新的源代码并重新编译即可。

```
cd $GOPATH/src/github.com/ethereum/go-ethereum
git checkout master
git pull
go install -v ./cmd/geth
go install -v ./cmd/swarm
```

[下一个 ](http://swarm-guide.readthedocs.io/en/latest/simpleuser.html)[ 以前](http://swarm-guide.readthedocs.io/en/latest/introduction.html)
