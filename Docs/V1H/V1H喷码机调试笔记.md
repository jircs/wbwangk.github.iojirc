供应商提供的调试工具[SDK Demo V1.0.1.10](https://github.com/wbwangk/wbwangk.github.io/blob/master/Docs/V1H/SDK%20Demo%20V1.0.1.10.zip)  
[官方说明书](https://github.com/wbwangk/wbwangk.github.io/blob/master/Docs/V1H/V1H%E8%AF%B4%E6%98%8E%E4%B9%A6.pdf)  
供应商提供的[V系列喷码机通讯协议.pdf](https://github.com/wbwangk/wbwangk.github.io/blob/master/Docs/V1H/V1H%E8%AF%B4%E6%98%8E%E4%B9%A6.pdf)  

从设备的“关于”中查到设备名称“SOJET”，版本1.0.0.4291，网址：www.sojet-tech.com  公司：Sojet Electronics(Xiamen) Co.,Ltd.  客户编码：1208651001  

设备开机后，首页上面的状态条有个wifi图标，点该图标连接上wifi热点，会显示设备IP(我的是192.168.43.134)。

### 在喷码机上定义资料
首页上点“资料管理”大图标，点新建资料，选条码。  
#### 源
新建源，动态文本，保存。源清单中多了一个源，叫BAR1_DYT1。（BAR代表条码，DYT代表动态文本）
#### 类型
选普通二维码，选QR类型

点左上角弧形箭头，提示保存资料，资料名称MSG004。

### 说明书学习
#### 备份
u盘插到喷码机，点工具->备份，选资料，点备份，输入资料名（实际上是目录名）MSG003，点备份。把U盘插到电脑上，发现在目录`F:\Inkjet\Backup\MSG003\MSG\`下是有几个目录，目录名是在喷码机上创建资料名称，如MSG004。  
目录下的文件中，Setup.ini是定义的资料信息，即喷印的模板定义。

### 客户端调试
在PC上连接同一个wifi热点。下载上面的SDK Demo，解压，执行SDKTest.exe。  
在弹出窗口上输入喷码机IP（192.168.43.134），点建立连接，提示连接成功。  
资料名称输入：MSG004  
数据源名称输入: BAR1_DYT1，点添加本地动态文本，提示“添加本地动态文本成功”。  
动态数据随便输入：520，点发送本地动态文本，提示“”设备未开启喷印！  

点获取设备SN号，显示序列号：730288  
### 继续调试
在V1H首页上点“喷印管理”，选择MSG004，点“开启喷印”，屏幕切换等待接收客户端的动态文本。  
在SDKTest.exe客户端窗口，重新点“发送本地动态文本”（或者先点“添加本地动态文本”，成功后再点“发送本地动态文本”）。提示“发送本地文本成功”。  
在V1H上显示接收到的文本：  
```
BAR1_DYT1: 520
```
手指勾一下扳机按键，滑动滚轮，喷印出了二维码。如果用二维码扫码器识别其内容，发现是`520`。

## 笔记
资料位置超出画布范围，对象在预览界面变黄显示，点击保存仍可正常保存，但只能喷印正常范围内癿内容。  

#### 询问济南安固捷技术人员的反馈
```
>BON>|0|1234567|1^CMD_BASEINFO`DEVSN`IPADR|=EOC=
```
把`1234567`替换为实际的设备id后用telnet尝试与V1H通讯，成功获得响应：
```
telnet 192.168.43.134 18885
Trying 192.168.43.134...
Connected to 192.168.43.134.
Escape character is '^]'.
>BON>|0|730288|1^CMD_BASEINFO`DEVSN`IPADR|=EOC=
<BON<|0|730288|1^CMD_OK`CMD_BASEINFO`DEVSN`730288`IPADR`192.168.43.134|=EOC=
```
#### 使用SDK DOEMO工具链接V1H，创建网络连接，用Mircrosoft Network Monitor 3.4 网络监控工具观察到
```
>BON>|6|0|1^CMD_BASEINFO`DEVSN|=EOC=
<BON<|1|0|1^CMD_OK`CMD_PRINTSTATUS`ISPRINTING`FALSE`PRINTINGMSG`NULL|=EOC=
```
（本机地址是192.168.43.198）上面的前者是本机发送到请求到V1H(192.168.43.134)，后者是V1H返回的响应。

用telnet工具手工发出上述信息（前者），得到的反馈有所不同：
```
>BON>|6|0|1^CMD_BASEINFO`DEVSN|=EOC=
<BON<|6|0|1^CMD_OK`CMD_BASEINFO`DEVSN`730288|=EOC=
```
### Mircrosoft Network Monitor 3.4 网络监控工具
下载地址：[Mircrosoft Network Monitor 3.4(]https://www.microsoft.com/en-us/download/details.aspx?id=4865)

安装后，需要重新启动windows才能完成对网络的监控！

进入工具后，在“Start Page”标签页可以选择网络。我仅选中了“WLAN”网卡。因为我用电脑wifi连接到V1H，我想监控电脑与V1H喷码机的通讯。

点击工具栏的“New Capture”，打开一个新的监控窗口“Capture1”。

打开“SDK DOEMO工具”（可执行文件叫SDKTest.exe）。在窗口的`IP`输入域中输入V1H的IP地址：192.168.43.134，然后点击“CreateConnet”按钮。

这时Mircrosoft Network Monitor网络监控工具的左侧窗口“Network Conversations”中应该显示了`SDKTest.exe`，点击`SDKTest.exe`，以便在右侧监控窗口中显示`SDKTest.exe`发出的网络信息。

在右侧Frame Summary中会显示`SDKTest.exe`发出或收到的网络信息。

多条信息是二进制的，但有两条含文本信息：
```
>BON>|6|0|1^CMD_BASEINFO`DEVSN|=EOC=
<BON<|1|0|1^CMD_OK`CMD_PRINTSTATUS`ISPRINTING`FALSE`PRINTINGMSG`NULL|=EOC=
```

### 用 Mircrosoft Network Monitor 3.4 工具监控SDKTest与V1H喷码机的通讯
忽略看不懂的二进制通讯，如下面的Frame Number 46。
CreateConnect:
```
Frame Number  Source        Details
45  192.168.43.198  >BON>|6|0|1^CMD_BASEINFO`DEVSN|=EOC=
47  192.168.43.134  <BON<|1|0|1^CMD_OK`CMD_PRINTSTATUS`ISPRINTING`FALSE`PRINTINGMSG`NULL|=EOC=
```
另一次CreateConnect：
```
>BON>|1|0|1^CMD_PRINTSTATUS`ISPRINTING`PRINTINGMSG|=EOC=
<BON<|1|0|1^CMD_OK`CMD_PRINTSTATUS`ISPRINTING`FALSE`PRINTINGMSG`NULL|=EOC=
```
断开连接，重新CreateConnect，仍是上面的信息。  

GetDeviceSN(SN: 730288)：
```
>BON>|2|0|1^CMD_BASEINFO`DEVSN|=EOC=
<BON<|2|0|1^CMD_OK`CMD_BASEINFO`DEVSN`730288|=EOC=
```
断开连接，重新GetDevicesSN，仍是上面的信息。  

GetDeviceIP：(IP:192.168.43.234)
点击这个按钮没有发出任何网络请求。  

GetPrintCounter(Counter: 22):
```
2522  <BON<|2|0|1^CMD_OK`CMD_BASEINFO`DEVSN`730288|=EOC=
2524  <BON<|3|0|1^CMD_OK`CMD_PRINTSTATUS`PRODUCTCOUNTER`22|=EOC=
```
还陆续收到了下列响应，不知有啥用：
```
2533  <BON<|3|0|1^CMD_OK`CMD_PRINTSTATUS`PRODUCTCOUNTER`22|=EOC=
2534  <BON<|4|0|1^CMD_OK`CMD_PRINTSTATUS`PRODUCTCOUNTER`22|=EOC=
2537  <BON<|5|0|1^CMD_OK`CMD_PRINTSTATUS`PRODUCTCOUNTER`22|=EOC=
```
GetPrintStatus：(Status: Device No Printing!)
```
>BON>|4|0|1^CMD_PRINTSTATUS`ISPRINTING`PRINTINGMSG|=EOC=
<BON<|4|0|1^CMD_OK`CMD_PRINTSTATUS`ISPRINTING`TRUE`PRINTINGMSG`MSG003|=EOC=
```
实测中发现GetPrintStatus可以用下列通讯命令代替：
```
>BON>|0|730288|1^CMD_PRINTSTATUS`ISPRINTING`PRINTINGMSG|=EOC=
<BON<|0|730288|1^CMD_OK`CMD_PRINTSTATUS`ISPRINTING`TRUE`PRINTINGMSG`MSG003|=EOC=
```
上述方式貌似可以更精准地把命令发送给wifi网络上的多个V1H喷码机（730288是设备号）。

StartPrint:(Start Print Succeed!) 启动喷印
```
>BON>|4|0|1^CMD_PRINTON`MSG003|=EOC=
<BON<|4|0|1^CMD_OK`CMD_PRINTON|=EOC=
```
StopPrint:(Stop Print Succeed!)  停止喷印
```
>BON>|3|0|1^CMD_PRINTOFF|=EOC=
<BON<|3|0|1^CMD_OK`CMD_PRINTOFF|=EOC=
```
发送两个纯动态文本数据：
```
>BON>|6|0|1^CMD_DYNTEXT`2`DYT1`BAR1_DYT1`332`332|=EOC=
<BON<|6|0|1^CMD_OK`CMD_DYNTEXT|=EOC=
```
#### 动态数据测试
Message Name: MSG003  
Source Name: BAR1_DYT1  
点击AppendlocalDynTest按钮后，提示Append Local Dynamic Test Succeed!  （没有发出任何网络请求）  

在Dynamic Test输入：`bing`，点击SendLocalDynText按钮。显示Device No Printing!  
```
>BON>|2|0|1^CMD_DYNTEXT`1`BAR1_DYT1`|=EOC=
<BON<|2|0|1^CMD_ERROR`CMD_DYNTEXT`NOPRINTING|=EOC=
```

关闭V1H喷码机，安装墨盒，检查V1H的wifi连接。
在V1H的首页进入“喷印管理”，选择MSG003，点“开启喷印”。屏幕切换为空白，等待SDKTest.exe向它发送动态文本。  
（由于发生了V1H的关机重启，实测时需要重新CreateConnet）  

在Dynamic Text中输入`bing`，然后点击“SendLocalDynTest”，显示Send Local Dynamic Text Succeed!

在V1H的屏幕上显示了`bing`的字样。扣动V1H的橙色扳机键，然后喷印二维码到纸上。V1H屏幕上显示的`bing`消失，等待SDKTest.exe再次向它发送动态文本。

用二维码扫码器检查喷印出的二维码内容，发现是`bing`。

通过Mircrosoft Network Monitor查看网络通讯：
```
>BON>|3|0|1^CMD_DYNTEXT`1`BAR1_DYT1`bing|=EOC=
<BON<|3|0|1^CMD_OK`CMD_DYNTEXT|=EOC=
```


#### 用telnet模仿上述网络通讯
```
telnet 192.168.43.134 18885
Trying 192.168.43.134...
Connected to 192.168.43.134.
Escape character is '^]'.
>BON>|3|0|1^CMD_DYNTEXT`1`BAR1_DYT1`https://bing.com|=EOC=
<BON<|3|0|1^CMD_OK`CMD_DYNTEXT|=EOC=
```
V1H喷码机屏幕上显示：
```
BAR_DYT1:https://bing.com
```
可以再次扣动V1H的扳机键喷印出上述内容的二维码，喷印后屏幕上的上述内容消失。

#### 用telnet完整模仿整个V1H开机和喷印过程
```
$ telnet 192.168.43.134 18885
Trying 192.168.43.134...
Connected to 192.168.43.134.
Escape character is '^]'.
>BON>|1|0|1^CMD_PRINTSTATUS`ISPRINTING`PRINTINGMSG|=EOC=
<BON<|1|0|1^CMD_OK`CMD_PRINTSTATUS`ISPRINTING`FALSE`PRINTINGMSG`NULL|=EOC=
>BON>|1|0|1^CMD_DYNTEXT`1`BAR1_DYT1`http://baidi.com|=EOC=
<BON<|1|0|1^CMD_ERROR`CMD_DYNTEXT`NOPRINTING|=EOC=
>BON>|1|0|1^CMD_DYNTEXT`1`BAR1_DYT1`http://baidi.com|=EOC=
<BON<|1|0|1^CMD_OK`CMD_DYNTEXT|=EOC=
```
上面显示`CMD_ERROR`是因为V1H没有“启动喷印”。

#### 发送两个动态文本
在V1H上定义资料MSG001，添加一个二维码，源是BAR1_DYT1，再添加一个动态文本，名称是DYT1。则同时发送两个动态文本到V1H的命令：
```
>BON>|4|0|1^CMD_PRINTON`MSG001|=EOC=
>BON>|4|0|1^CMD_DYNTEXT`2`BAR1_DYT1`DYT1`333`444|=EOC=
<BON<|4|0|1^CMD_OK`CMD_DYNTEXT|=EOC=
>BON>|4|0|1^CMD_DYNTEXT`2`BAR1_DYT1`DYT1`555`666|=EOC=
<BON<|4|0|1^CMD_OK`CMD_DYNTEXT|=EOC=
>BON>|4|0|1^CMD_DYNTEXT`2`BAR1_DYT1`DYT1`777`888|=EOC=
<BON<|4|0|1^CMD_OK`CMD_DYNTEXT|=EOC=
```
可以向V1H连续发送多个喷码命令，它会缓存起来，自动喷下一个标签。

#### 主动回馈指令
主动回馈指令监听端口是19885。  
另外启动一个linux终端窗口，执行：
```
telnet 192.168.43.134 19885
Trying 192.168.43.134...
Connected to 192.168.43.134.
Escape character is '^]'.
>BON>|100|730288|1^CMD_DEVICEPRINTONCE`PRODUCTCOUNTER`33`DATASOURCE`BAR1_DYT1`333`DYT1`444|=EOC=
```
喷印后就19885端口就接收到了喷码机的成功喷印消息，其中含有喷印计数（PRODUCTCOUNTER）。
