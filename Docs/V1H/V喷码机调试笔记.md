供应商提供的调试工具[SDK Demo V1.0.1.10](https://pan.baidu.com/s/1fLu8QrP67NtwAPNTaqou1w)  
[官方说明书]()  
供应商提供的[V系列喷码机通讯协议.pdf](https://github.com/wbwangk/wbwangk.github.io/blob/master/Docs/V1H/V1H%E8%AF%B4%E6%98%8E%E4%B9%A6.pdf)  



从设备的“关于”中查到设备名称“SOJET”，版本1.0.0.4291，网址：www.sojet-tech.com  公司：Sojet Electronics(Xiamen) Co.,Ltd.  客户编码：1208651001  

设备开机后，首页上面的状态条有个wifi图标，点该图标连接上wifi热点，会显示设备IP(我的是192.168.43.134)。

### 在喷码机上定义资料
首页上点“资料管理”大图标，点新建资料，选条码。  
#### 源
新建源，动态文本，保存。源清单中多了一个源，叫BAR1_DYT1。（猜测：BAR代表条码，DYT代表动态文本）
#### 类型
选普通二维码，选QR类型

点左上角弧形箭头，提示保存资料，资料名称MSG004。

### 客户端调试
在PC上连接同一个wifi热点。下载上面的SDK Demo，解压，执行SDKTest.exe。  
在弹出窗口上输入喷码机IP（192.168.43.134），点建立连接，提示连接成功。  
资料名称输入：MSG004  
数据源名称输入: BAR1_DYT1，点添加本地动态文本，提示“添加本地动态文本成功”。  
动态数据随便输入：520，点发送本地动态文本，提示“”设备未开启喷印！  

