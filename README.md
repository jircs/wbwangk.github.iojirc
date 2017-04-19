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
```
docker run -p 5984:5984 -d -v /var/couchdb:/usr/local/var/lib/couchdb \
 -v /var/couchdb/conf:/usr/local/etc/couchdb/local.d  \
 -v /var/couchdb/log:/usr/local/var/log/couchdb \
registry.aliyuncs.com/imaidev/couchdb
```
couchdb附件功能测试：
```
curl -vX PUT http://couch.imaicloud.com/webb/df7a5aa5bc2a6d6852f61a078a00059f/aa.jpg \
     --data-binary @aa.jpg -H "Content-Type:image/jpg"
```

## 进入docker容器命令行
docker exec -it 36cf373275dc /bin/bash

## imaicloud 虚拟机清单 
 ocs_dev_web01  内网：10.0.9.105 电信：58.56.17.102 联通：60.216.42.102 移动：223.99.170.102  dev.imaicloud.com  (平台开发)
 ocs_dev_app01  内网：10.0.7.105  
 ocs_web01  内网：10.0.9.106  电信：58.56.17.103 联通：60.216.42.103 移动：223.99.170.103  ocs.imaicloud.com  (在线客服生产) 
 ocs_app01  内网：10.0.7.106 
## 查看ubuntu端口占用
   netstat -ap | grep 8080
   curl -v -X OPTIONS https://registry.imaicloud.com/v2/ (测试CORS)
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
## 各种UI
1. docker run --name docker-compose-ui -p 5000:5000 -v /var/run/docker.sock:/var/run/docker.sock  registry.imaicloud.com/docker-compose-ui
2. docker run -d -p 10086:10086 -v /var/run/docker.sock:/var/run/docker.sock registry.imaicloud.com/tobegit3hub/seagull(界面赞，多主题)
3. docker run -d -p 3000:3000 -v /var/run/docker.sock:/var/run/docker.sock --name dockerswarm-ui   registry.imaicloud.com/mlabouardy/dockerswarm-ui (功能太单一，无法从镜像启动容器）
4. docker pull registry.imaicloud.com/atcol/docker-registry-ui
5. docker pull registry.imaicloud.com/hyper/docker-registry-web
6. docker pull registry.imaicloud.com/jgsqware/registry-ui
 
## go lang
```
https://storage.googleapis.com/golang/go1.6.3.linux-amd64.tar.gz
tar -C /usr/local -xzf go1.6.3.linux-amd64.tar.gz
export PATH=$PATH:/usr/local/go/bin
export GOROOT=/usr/local/go
export GOPATH=$HOME/work
```
## nginx第三方模块echo安装
**下载nginx**
下载地址：http://nginx.org/en/download.html
```
wget http://nginx.org/download/nginx-1.11.3.tar.gz
tar -xzvf nginx-1.11.3.tar.gz   （解压创建目录/opt/nginx-1.11.3）
```
**下载echo模块**
该模块地址：https://github.com/openresty/echo-nginx-module
```
wget https://github.com/openresty/echo-nginx-module/archive/v0.59.tar.gz
tar -xzvf v0.59.tar.gz    (解压创建目录/opt/echo-nginx-module-0.59)
```
**安装**
```
cd /opt/nginx-1.11.3
./configure --prefix=/opt/nginx \
    --add-module=/opt/echo-nginx-module-0.59
make -j2
make install
```
ubuntu server版下可能需要安装gcc编译器、PCRE Library、zlib library、make:
```
apt-get install gcc
apt-get install libpcre3 libpcre3-dev
apt-get install zlib1g-dev
apg-get install make
```
如果运行```./sbin/nginx -s reload```提示``` invalid PID number "" in "/opt/nginx/logs/nginx.pid"```则执行```./sbin/nginx -c conf/nginx.conf```

## logstash
```
docker run -it --rm -v /opt/logstash/conf:/config-dir -v /opt/nginx/logs:/opt/nginx/logs logstash logstash -f /config-dir/logstash.conf
```





