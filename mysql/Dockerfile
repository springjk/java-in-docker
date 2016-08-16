FROM mysql:5.7

#写入权限配置
RUN usermod -u 1000 mysql && chown mysql.mysql /var/run/mysqld/

#设置时区
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

EXPOSE 3306