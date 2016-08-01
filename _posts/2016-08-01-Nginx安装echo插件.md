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
