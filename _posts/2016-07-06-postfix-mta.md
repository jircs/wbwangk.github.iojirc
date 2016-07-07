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


1. 向DNS中添加MX和SPF记录

DNS中增加MX记录，主机记录是：```@```，值是：```imaicloud.com.```
检查MX记录：在windows系统的命令行控制体输入```nslookup -qt=mx imaicloud.com```，能显示```imaicloud.com```表示MX记录配置正确。

在DNS中增加TXT记录，值是：```v=spf1 ip4:imaicloud.com ~all```

2. 安装opendkim

```
yum(或apt-get) install opendkim
```
3. 生成公钥/私钥

创建目录：```/etc/opendkim/keys/imaicloud.com```
在上述目录下执行```opendkim-genkey -d imaicloud.com -s default ```  就生成了两个文件。defaut.txt中就是DKIM签名的公钥，要以txt记录的形式放到DNS中。而另一个文件是私钥，执行postfix时要告诉它私钥的所在目录。

4. 运行postfix

```
docker run -p 25:25 \
         -e maildomain=mail.imaicloud.com -e smtp_user=admin:123jnyg \
         -v /etc/opendkim/keys/imaicloud.com:/etc/opendkim/domainkeys \
         --name postfix -d registry.aliyuncs.com/imaidev/postfix
```
5.PTR反向域名解析
 这个不是ISP（如万网）负责，而是专线提供商，如联通，提供的服务。
