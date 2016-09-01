# 将源码拷贝至编译目录
cp -r /tmp/code /tmp/build

# 下载依赖
cd /tmp/build && mvn dependency:resolve

# 构建应用
cd /tmp/build && mvn clean package -Dmaven.test.skip=true

# 拷贝编译结果到指定目录
rm -rf $CATALINA_HOME/webapps/*
mv target/*.war $CATALINA_HOME/webapps/ROOT.war

# 清理编译痕迹
cd / && rm -rf /tmp/build

# 启动tomcat
bash $CATALINA_HOME/bin/catalina.sh run