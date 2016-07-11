## 阿里镜像仓库etcd-viewer
(imaidev/123456a?)
[阿里镜像仓库地址](https://cr.console.aliyun.com/?spm=0.0.0.0.5n07DB#/docker/image/list)   

## NGINX重写URL
```
    location /ethercalc {
        return 301 $scheme://$server_name$request_uri/;
    }
    location /ethercalc/ {
        proxy_pass      http://127.0.0.1:8000;
        rewrite /ethercalc(/.*) $1 break;
    }
```
## 启动couchdb(couch/fo)
docker run -p 5984:5984 -d -v /var/couchdb:/usr/local/var/lib/couchdb \
 -v /var/couchdb/conf:/usr/local/etc/couchdb/local.d  \
 -v /var/couchdb/log:/usr/local/var/log/couchdb \
registry.aliyuncs.com/imaidev/couchdb

curl -vX PUT http://couch.imaicloud.com/webb/df7a5aa5bc2a6d6852f61a078a00059f/aa.jpg \
     --data-binary @aa.jpg -H "Content-Type:image/jpg"


## 进入docker容器命令行
docker exec -it 36cf373275dc /bin/bash

## imaicloud 虚拟机清单 
 ocs_dev_web01  内网：10.0.9.105 电信：58.56.17.102 联通：60.216.42.102 移动：223.99.170.102  dev.imaicloud.com  (平台开发)
 ocs_dev_app01  内网：10.0.7.105  
 ocs_web01  内网：10.0.9.106  电信：58.56.17.103 联通：60.216.42.103 移动：223.99.170.103  ocs.imaicloud.com  (在线客服生产) 
 ocs_app01  内网：10.0.7.106 
## 查看ubuntu端口占用
   netstat -ap | grep 8080
## ui-for-docker
docker run -d -p 9001:9000 --privileged -v /var/run/docker.sock:/var/run/docker.sock registry.aliyuncs.com/imaidev/ui-for-docker


        location /.well-known {
            root html-imaicloud;
        }
## docker registry
docker run -d -p 5000:5000 --name registry registry.aliyuncs.com/imaidev/registry
生成密码文件:docker run --entrypoint htpasswd registry.aliyuncs.com/imaidev/registry -Bbn wbwang 1 > auth/htpasswd
登录：docker login --username=wbwang registry.imaicloud.com

docker run -d -p 5000:5000 --restart=always -v /var/lib/registry:/var/lib/registry \
--name registry  registry.aliyuncs.com/imaidev/registry

