Graphite 是一个Python写的web应用，采用django框架，Graphite用来进行收集服务器所有的及时状态，用户请求信息，Memcached命中率，RabbitMQ消息服务器的状态，Unix操作系统的负载状态，Graphite服务器大约每分钟需要有4800次更新操作，Graphite采用简单的文本协议和绘图功能可以方便地使用在任何操作系统上。

使用pip安装到share1：
```
apt-get install python-dev libcairo2-dev libffi-dev
pip install cairocffi
pip install django
export PYTHONPATH="/opt/graphite/lib/:/opt/graphite/webapp/"
pip install https://github.com/graphite-project/whisper/tarball/master
pip install https://github.com/graphite-project/carbon/tarball/master
pip install https://github.com/graphite-project/graphite-web/tarball/master
```
share1的内存改成了1.5G，否则安装djongo报内存溢出。
Webapp Database Setup：
```
export GRAPHITE_ROOT=/opt/graphite
pip install scandir
PYTHONPATH=$GRAPHITE_ROOT/webapp django-admin.py migrate --settings=graphite.settings --run-syncdb
```

### docker方式部署
在share1上：
```
docker pull hopsoft/graphite-statsd

```
