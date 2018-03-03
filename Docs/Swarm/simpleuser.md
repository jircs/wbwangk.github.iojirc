# 连接到Swarm（简单）

这些说明陈述了使用swarm的最简单方法。

## 我如何连接？

要启动一个基本的swarm节点，你的机器上必须安装geth和swarm。您可以在Swarm手册的安装部分找到相关说明。

注意

您可以在Swarm手册的安装和更新部分找到相关说明。

如果您尚未创建以太坊帐户，请先运行以下命令：

```
geth account new
```

系统会提示您输入密码：

```
Your new account is locked with a password. Please give a password. Do not forget this password.
Passphrase:
Repeat passphrase:
```

一旦你指定了密码（例如MYPASSWORD），输出将是你的以太坊地址。这也是您的Swarm节点的基地址。

```
Address: {2f1cd699b0bf461dcfbf0098ad8f5587b038f0f1}
```

因为我们稍后需要使用它，请将其保存到名称 `BZZKEY`下的ENV变量中

```
BZZKEY=2f1cd699b0bf461dcfbf0098ad8f5587b038f0f1
```

接下来，启动您的geth节点并使用以下命令建立与以太坊主网络的连接

```
geth
```

建立连接后，打开另一个终端窗口并使用下列命令连接到Swarm

```
swarm --bzzaccount $BZZKEY
```

## 我如何上传和下载？

Swarm运行一个HTTP API。因此，从Swarm上传/下载文件的简单方法是通过此API。我们可以使用`curl`工具来举例说明如何与此API进行交互。

注意

文件可以上传到单个HTTP请求中，其中请求体(body)是要存储的单个文件、一个tar流(application/x-tar)或一个多部分表单(multipart/form-data)。

要上传单个文件，请运行以下命令：

```
curl -H "Content-Type: text/plain" --data-binary "some-data" http://localhost:8500/bzz:/
```

文件上传完成后，您将收到一个十六进制字符串，该字符串看起来很相似。

```
027e57bcbae76c4b6a1c5ce589be41232498f1af86e1b1a2fc2bdffd740e9b39
```

这是您的Swarm内容的地址字符串。

要从swarm下载文件，只需要该文件的地址字符串。一旦你拥有了它，这个过程很简单。运行：

```
curl http://localhost:8500/bzz:/027e57bcbae76c4b6a1c5ce589be41232498f1af86e1b1a2fc2bdffd740e9b39/
```

结果集应该是你的文件：

```
some-data
```

就是这样。请注意，如果您从网址中省略了斜线，则请求将导致重定向。

### Tar流上传

```
( mkdir dir1 dir2; echo "some-data" | tee dir1/file.txt | tee dir2/file.txt; )

tar c dir1/file.txt dir2/file.txt | curl -H "Content-Type: application/x-tar" --data-binary @- http://localhost:8500/bzz:/
> 1e0e21894d731271e50ea2cecf60801fdc8d0b23ae33b9e808e5789346e3355e

curl http://localhost:8500/bzz:/1e0e21894d731271e50ea2cecf60801fdc8d0b23ae33b9e808e5789346e3355e/dir1/file.txt
> some-data

curl http://localhost:8500/bzz:/1e0e21894d731271e50ea2cecf60801fdc8d0b23ae33b9e808e5789346e3355e/dir2/file.txt
> some-data
```

GET请求的工作方式与上面的相同，通过设置*Accept: application/x-tar*来添加下载多个文件的功能：

```
curl -s -H "Accept: application/x-tar" http://localhost:8500/bzz:/ccef599d1a13bed9989e424011aed2c023fce25917864cd7de38a761567410b8/ | tar t
> dir1/file.txt
  dir2/file.txt
  dir3/file.txt
```

### 多部分表单上传

```
curl -F 'dir1/file.txt=some-data;type=text/plain' -F 'dir2/file.txt=some-data;type=text/plain' http://localhost:8500/bzz:/
> 9557bc9bb38d60368f5f07aae289337fcc23b4a03b12bb40a0e3e0689f76c177

curl http://localhost:8500/bzz:/9557bc9bb38d60368f5f07aae289337fcc23b4a03b12bb40a0e3e0689f76c177/dir1/file.txt
> some-data

curl http://localhost:8500/bzz:/9557bc9bb38d60368f5f07aae289337fcc23b4a03b12bb40a0e3e0689f76c177/dir2/file.txt
> some-data
```

### 文件也可以添加到现有的清单中：

```
curl -F 'dir3/file.txt=some-other-data;type=text/plain' http://localhost:8500/bzz:/9557bc9bb38d60368f5f07aae289337fcc23b4a03b12bb40a0e3e0689f76c177
> ccef599d1a13bed9989e424011aed2c023fce25917864cd7de38a761567410b8

curl http://localhost:8500/bzz:/ccef599d1a13bed9989e424011aed2c023fce25917864cd7de38a761567410b8/dir1/file.txt
> some-data

curl http://localhost:8500/bzz:/ccef599d1a13bed9989e424011aed2c023fce25917864cd7de38a761567410b8/dir3/file.txt
> some-other-data
```

### 文件也可以使用简单的HTML表单上传：

```
<form method="POST" action="/bzz:/" enctype="multipart/form-data">
  <input type="file" name="dir1/file.txt">
  <input type="file" name="dir2/file.txt">
  <input type="submit" value="upload">
</form>
```

### 列出文件

一个使用`bzz-list`URL schema的GET请求会返回这个路径下的文件清单，分组为表示目录的常用前缀的文件列表：

```
curl -s http://localhost:8500/bzz-list:/ccef599d1a13bed9989e424011aed2c023fce25917864cd7de38a761567410b8/ | jq .
> {
   "common_prefixes": [
     "dir1/",
     "dir2/",
     "dir3/"
   ]
 }
```

```
curl -s http://localhost:8500/bzz-list:/ccef599d1a13bed9989e424011aed2c023fce25917864cd7de38a761567410b8/dir1/ | jq .
> {
  "entries": [
    {
      "path": "dir1/file.txt",
      "contentType": "text/plain",
      "size": 9,
      "mod_time": "2017-03-12T15:19:55.112597383Z",
      "hash": "94f78a45c7897957809544aa6d68aa7ad35df695713895953b885aca274bd955"
    }
  ]
}
```

设置 Accept: text/html将返回列表为一个可浏览的HTML文档

祝你好运，我们希望你会喜欢使用Swarm！
