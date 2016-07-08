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

被退信的要素：

the server is an open mail relay
the sender's or server's IP address is blacklisted
the server does not have a [Fully Qualified Domain Name](https://github.com/DigitalOcean-User-Projects/Articles-and-Tutorials/blob/master/set_hostname_fqdn_on_ubuntu_centos.md) (FQDN) and a PTR record
the [Sender Policy Framework](https://www.digitalocean.com/community/tutorials/how-to-use-an-spf-record-to-prevent-spoofing-improve-e-mail-reliability) (SPF) DNS record is missing or it is misconfigured
the DomainKeys Identified Mail (DKIM) implementation is missing or it's not properly set up

1. [Fully Qualified Domain Name](https://github.com/DigitalOcean-User-Projects/Articles-and-Tutorials/blob/master/set_hostname_fqdn_on_ubuntu_centos.md) (FQDN)

编辑/etc/hosts，增加了一条记录：
```
10.0.9.105 ocs_dev_web01
60.216.42.102 mail.imaicloud.com
```
如果不加mail.imaicloud.com，邮件貌似发不出去。

1. 向DNS中添加MX和SPF记录

DNS中增加MX记录，主机记录是：```@```，值是：```imaicloud.com.```
检查MX记录：在windows系统的命令行控制体输入```nslookup -qt=mx imaicloud.com```，能显示```imaicloud.com```表示MX记录配置正确。

在DNS中增加TXT记录，值是：```v=spf1 ip4:60.216.42.102 ip4:223.99.170.103 ~all```。```ip4```还可以替换成```a```，表示A记录。可[参考](http://www.openspf.org/Introduction)

#### 安装opendkim、生成公钥/私钥、在DNS中增加DKIM记录
安装opendkim，执行```yum(或apt-get) install opendkim```。
创建目录：```/etc/opendkim/keys/imaicloud.com```
在上述目录下执行```opendkim-genkey -d imaicloud.com -s default ```  就生成了两个文件：defaut.txt中就是DKIM签名的公钥，要以txt记录的形式放到DNS中。而另一个文件是私钥，执行postfix时要告诉它私钥的所在目录。

编辑opendkim配置文件，执行```vi /etc/opendkim.conf```。可以看到opendkim创建了一个socket供MTA来通信：```Socket inet:8891@localhost```
加入两行配置，以便opendkim找到key：

```
KeyTable       /etc/opendkim/KeyTable
SigningTable   refile:/etc/opendkim/SigningTable
```
需要执行下列命令改一下私钥文件的访问权限。opendkim:opendkim是用户和用户组。
```
chown opendkim:opendkim default.private
```
这貌似是一个bug，如果不改权限，可以从日志文件/var/log/maillog中看到错误，说opendkim没有权限访问default.private

执行```vi /etc/opendkim/KeyTable```，加入一行

```
default._domainkey.imaicloud.com imaicloud.com:default:/etc/opendkim/keys/imaicloud.com/default.private
```
执行```vi /etc/opendkim/SigningTable```，加入一行

```
*@imaicloud.com default._domainkey.imaicloud.com
```

在DNS中增加DKMIM记录，记录值来自```/etc/opendkim/keys/imaicloud.com/default.txt```
记录类型TXT，主机记录是```default._domainkey```，记录值：

```
v=DKIM1; k=rsa;p=MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDBoFohDShGCZF+Wkk4BqOz+IlcjCm9nSwDWFWjGIr1T+gDhyyUMJVJv5kP7/dVnjR/aWYx3A1Tk7gb9wJlvZrSZXF+io0EgxtZpKZnxrGjD07kREzxrWEKsQnjRVMnOW+Y1m1MWvs+4CIYBtEug3cOhuwDOXgEMhLgDERHDxFn/QIDAQAB
```
#### 安装和运行postfix（MTA发送邮件)

CentOS默认已经安装了postfix。编辑/etc/postfix/main.cf。增加了如下配置，其他使用的默认配置。
```
myhostname = mail.imaicloud.com
mydomain = imaicloud.com
myorigin = $mydomain
mydestination = $myhostname, localhost.$mydomain, localhost, $mydomain
```
#### dovecot与PTR反向域名解析
安装dovecot(IMAP接收邮件)
```yum(或apt-get) install dovecot```
编辑配置文件```/etc/dovecot/dovecot.conf```
配置文件删除和增加的内容见参考1.

PTR反向域名解析
 这个不是ISP（如万网）负责，而是专线提供商，如联通，提供的服务。
 
 
 这个文档的[参考1](https://www.digitalocean.com/community/tutorials/how-to-set-up-a-postfix-e-mail-server-with-dovecot)和[参考2](https://www.digitalocean.com/community/tutorials/how-to-install-and-configure-dkim-with-postfix-on-debian-wheezy)
