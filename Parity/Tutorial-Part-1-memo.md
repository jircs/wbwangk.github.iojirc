## DApp教程第一部分

本文参照了[Parity DApp Tutorial Part1](https://github.com/wbwangk/wbwangk.github.io/blob/master/Parity/Tutorial-Part-1.md) 

本文的调试环境是一个[多节点Parity PoA网络](https://github.com/wbwangk/wbwangk.github.io/wiki/Ethereum%E8%BF%9B%E9%98%B6#%E9%83%A8%E7%BD%B2%E5%A4%9A%E8%8A%82%E7%82%B9parity-poa%E5%8C%BA%E5%9D%97%E9%93%BE)。u1601充当第一个节点，u1607充当第二个节点。u1607是个ubuntu16.4桌面系统，u1607代替了文章中u1602节点。

### 1.生成一个新的Dapp
在u1607上克隆`skeleton`库：
```
git clone https://github.com/wbwangk/skeleton mydapp
cd mydapp 
git remote rm origin
./init.sh
```
执行下列命令来确保webpack安装成功了：
```
$ npx webpack
```
这回执行webpack构建，把src目录下的源码编译后输出到dist目录。
> parity官方英文教程中使用的是旧版本的webpack（如webpack@2.1.0-beta.22），与新版本webpack的差异很大。

### 2. 配置它的外观
编辑`dist/manifest.json`为:
```json
{
        "id": "mydapp",
        "name": "My Dapp",
        "description": "My demonstration dapp",
        "version": "0.1",
        "author": "webb",
        "iconUrl": "title.png"
}
```
### 3.在Parity中显示它
由于在配置文件`node1.toml`中指定了数据目录`base_path = "/home/vagrant/parity"`，Parity客户端会到这个目录的下级`dapps`中去找部署的dapp。所以需要在`dapps`目录下创建一个软连接指向`mydapp/dist`目录(当前用户是vagrant)：
```
cd ~/mydapp
ln -s $PWD/dist $HOME/parity/dapps/mydapp
```
确保parity是这样启动的：
```
parity ui --config node0.toml
```
然后用浏览器访问地址`https://192.168.16.107:8180`。点击`Browse Dapps`图标可以进入dapp浏览页面，浏览器右下角会提示授权，点击`Approve`按钮进行授权。

找到刚开发的应用`My Dapp`，点`Open`按钮就进入了mydapp的index.html，会显示`Hello world`。

#### 用webpack自带http-server
有两种方式，我也不知道哪种是更好了：
```
$ npx webpack-dev-server --content-base dist
$ npx http-server dist
```
两种方式都会在8080端口上将dapp服务出来。差异从各自的输出上可以看出来，前者的服务地址只有`127.0.0.1:8080`，后者还增加了`192.168.16.107:8080`的服务，这使得可以在宿主机的浏览器上直接访问dapp。

由于u1607是ubuntu桌面，可以用桌面带的火狐浏览器访问`127.0.0.1:8080`来访问`My Dapp`。

通过webpack自动的http服务好处调试简单，但只用于开发。

你可能注意到`webpack-dev-server --content-base dist`已经定义到了package.json的scripts脚本中，所以可以直接用`npm start`来启动webpack自动的http服务。

