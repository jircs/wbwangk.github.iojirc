本文是[DApp教程的第三部分](https://github.com/wbwangk/wbwangk.github.io/blob/master/Parity/Tutorial-Part-3.md)的调试笔记。

## Parity Bonds

在这一部分的教程中网页会通过`oo7-parity`模块访问区块链。这个模块的源码位于https://github.com/paritytech/oo7-parity。

### 1.监视区块
在`oo7-parity`模块中，在`src/index.js`文件有一个语句：
```
return new ParityApi.Provider.Http('http://localhost:8545');
```
而在我的环境中，parity的rpc地址是`http://192.168.16.107:8540`，所以这个语句要修改，否则就访问不到parity的rpc端口。

编辑`node_modules/oo7-parity/src/index.js`为：
```
return new ParityApi.Provider.Http('http://192.168.16.107:8540');
```
然后编辑`src/client/scripts/app.jsx`为：
```
import React from 'react';
import {bonds} from 'oo7-parity';

export class App extends React.Component {
        render() {
                return (
                        <Rspan>{bonds.height}</Rspan>
                );
        }
}
```
运行`npm run build`构建mydapp（或执行`npx webpack`）。

这里碰到一个parity的bug，花了很多时间。这个bug是parity配置文件中配置cors不管用：
```
[rpc]
cors = ["all"]
```
必须在命令行中显式的指定，即：
```
$ parity ui --config node1.toml --jsonrpc-cors all
```
否则通过浏览器访问`http://192.168.16.107:8180`
