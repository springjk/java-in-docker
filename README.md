# Docker: Ubuntu, Tomcat, Maven and JAVA Stack
## 背景
Hi，JAVA！

JAVA 的项目一般比较复杂，为了标准化流程与简易化部署，本项目根据官方示例构建一套简易的 JAVA 的开发／运行环境，build，ship，run！

## 快速体验
``` bash
# 根据当前目录的配置后台启动项目容器
$ docker-composer up
```

## 访问
* **Windows**

[http://docker:9527](http://docker:9527)

* **Linux / MAC OS**

[http://127.0.0.1:9527](http://127.0.0.1:9527)

* **Windows / Mac OS [Toolbox](https://docs.docker.com/docker-for-mac/docker-toolbox/)**

访问**虚拟机**所在 IP 的 `9527` 端口。

``` bash
# 查看 docker 所在虚拟机 IP
$ docker-machine ip
```

## 加载项目
实际使用中我们需要加载自己项目的代码，这里有两种方式：挂载项目目录(荐)和导入到项目中，选择一种你喜欢的即可。

### 挂载项目目录
1. 修改 `docker-compose.yml` 中的代码挂载目录为你源代码目录（pom.xml 的父级目录）
2. 打开相关注释（line 12 & 18）
3. `$ docker-compose down`
4. `$ docker-compose up`

### 导入到项目中
1. 将本项目中 `code` 目录除外的文件拷贝至你的项目文件中
2. `$ docker-compose down`
3. `$ docker-compose up --force-recreate`

## 数据库信息
* **DB_HOST** : `mysql`
* **DB_PORT** : `3306`
* **DB_NAME** : `docker`
* **DB_USER_NAME** : `root`
* **DB_USER_PWD** : `docker`

此处的数据库连接地址 `DB_HOST` 值为 `mysql` 是因为名为 `java-maven-tomcat` 的容器 `link` 了名为 `mysql` 的容器，故 `java-maven-tomcat` 容器中会在自己的 `/etc/hosts` 中添加一条 `1xx.xx.xx.xx   mysql` 的 host，这会将 `http://mysql` 这个地址指向 `mysql` 容器的实际内网地址，等效于常见的 `http://localhost` 。

## 数据持久化
容器内的数据会随着容器的销毁而丢失，所以建议配置 `docker-compose.yml` 文件将以下目录同步到你的物理机目录进行持久化：

* `/root/.m2` 	Maven 的 jar 包缓存目录，挂载后将无需每次构建都去下载。
* `/var/lib/mysql` MySQL 的文件存放目录，必须挂载，否则销毁容器后数据丢失。

Windows 下挂载目录写法示例：`d:/project/`，MAC OS 与 Linux 下如挂载目录后出现无法写入则是权限问题，对挂载文件夹给与 777 权限即可。

## 项目部署
数据库信息，暴露端口等信息都在 `docker-compose.yml` 中配置，一切调试完成后可在启动命令后添加 `-d` 参数，让其后台启动，此时如果想实时查看启动日志可以使用 `docker-compose logs -f` 进行查看。

``` bash
# 后台启动运行
$ docker-composer up -d
	
# 跟踪查看启动日志
$ docker-compose logs -f
```

## 版本信息
* **JAVA** ：`1.8`
* **MySQL** ：`5.7`
* **Maven** ：`3.3.9`
* **Tomcat** ：`8.5.3`

版本信息可在 `Dockerfile` 中进行修改满足项目需求，修改 `Dockerfile` 后需要将 `docker-compose` 中的构建方式改为构建本地镜像：

``` bash
# 免构建镜像
# image: registry.aliyuncs.com/springjk/java-maven-tomcat
# 构建本地镜像
  build: .
```

修改完成后启动时需要重新构建镜像：

``` bash
$ docker-compose up --build
```

## 目录结构

```
├── Dockerfile	  # JAVA-Tomcat-Maven 构建文件
├── LICENSE
├── README.md
├── build.sh  # Maven 构建部署脚本
├── code  # 示例代码目录
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── bingo
│       │   │           └── helloworld
│       │   │               └── HelloWorld.java
│       │   ├── resources
│       │   └── webapp
│       │       ├── META-INF
│       │       │   └── MANIFEST.MF
│       │       └── WEB-INF
│       │           ├── lib
│       │           └── web.xml
│       └── test
│           ├── java
│           └── resources
├── docker-compose.yml  # docker-compose 配置文件
└── mysql
    └── Dockerfile  # MySQL 构建文件
``` 

## 工作流程
`Dockerfile -->(build)–> Image -->(run)–> Container`

1. 构建 MySQL 镜像
2. 构建 JAVA-Tomcat-Maven 镜像
3. 拷贝 `docker-compose.yml` 同级的示例代码 `code` 放入镜像 `/tmp/code`
4. 如已挂载代码目录则使用挂载目录覆盖镜像中的 `/tmp/code`
5. 启动 MySQL 镜像
6. 启动 JAVA-Tomcat-Maven 镜像
7. 执行 Maven 代码构建脚本 `build.sh` 生成 WAR 包并放入 Tomcat 工作目录
8. 启动 Tomcat

更多请参考 [Docker Docs](https://docs.docker.com/)。

## License
MIT







