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
