## 阿里镜像仓库etcd-viewer
(imaidev/123456a?)
[阿里镜像仓库地址](https://cr.console.aliyun.com/?spm=0.0.0.0.5n07DB#/docker/image/list)
拉取镜像
```
$ sudo docker login --username=hi31915183@aliyun.com registry.aliyuncs.com
$ sudo docker pull registry.aliyuncs.com/imaidev/etcd-viewer
```
推送镜像
```
$ sudo docker login --username=hi31915183@aliyun.com registry.aliyuncs.com
$ sudo docker tag [ImageId] registry.aliyuncs.com/imaidev/etcd-viewer
$ sudo docker push registry.aliyuncs.com/imaidev/etcd-viewer
```
