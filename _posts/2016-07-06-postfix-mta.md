---
layout: blog
title: postfix邮件服务器
summary: postfix,MTA,mail,邮件
---

## {{ page.title }}

发送邮件的要素：
1. SPF 记录 （重要）
2. DKIM 签名
3. IP 地址反向解析
4. 邮件内容不要含有特殊字符以及明显的广告内容

1. 安装opendkim

```
yum(或apt-get) install opendkim
```
2. 生成公钥/私钥

创建目录：```/etc/opendkim/keys/imai365.cc```
在上述目录下执行```opendkim-genkey -d YourDomain.com -s default ```  就生成了两个文件。defaut.txt中就是DKIM签名的公钥，要以txt记录的形式放到DNS中。而另一个文件是私钥，执行postfix时要告诉它私钥的所在目录。

3. 运行postfix

