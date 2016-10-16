# Docker: Ubuntu, Tomcat, Maven and JAVA Stack
## 背景
Hi，JAVA！

JAVA 的项目通常都比较繁琐，为了标准化流程与简易化部署，本项目根据官方示例构建一套简易的 JAVA 的开发／运行环境（Ubuntu, Tomcat, Maven and JAVA Stack），最终达到 build，ship，run！

## 依赖

* [Git](https://git-scm.com/downloads)
* [Docker](https://www.docker.com/products/docker/) `>= 1.12`
* [Docker-Compose](https://www.docker.com/products/docker/) `Windows/macOS 版 Docker 已包含`

> 由于众所周知的原因，梯子不够坚固的童鞋请使用 [国内镜像](http://get.daocloud.io/) 以及 [加速器](https://www.daocloud.io/mirror.html#accelerator-doc)。

## 快速体验
1. `$ git clone https://github.com/springjk/java-in-docker`
2. `$ cd java-in-docker`
3. `$ docker-compose up`
4. 访问 [http://127.0.0.1](http://127.0.0.1)

<!-- more -->

![java-in-docker](http://oac57xnsh.bkt.clouddn.com/java-in-docker.jpg)

运维命令：

```bash
$ docker-compose help   # docker-compose 命令帮助
$ docker-compose up     # 创建并启动 docker 编排服务
$ docker-compose down   # 停止并移除 docker 编排服务 (更改配置文件时建议使用)
$ docker-compose exec java-maven-tomcat bash # ssh 登入 java 容器
```


## 加载项目
上述方式为体验项目使用，运行的为 `code` 目录下的示例代码，实际使用中我们需要加载自己项目的代码，这里有两种方式：挂载项目目录和导入到项目中，选择一种你喜欢的即可。

### 挂载项目目录
1. 修改 `docker-compose.yml` 中的代码挂载目录为你源代码目录（pom.xml 的父级目录）
2. `$ docker-compose up`

### 导入到项目中
1. `$ cd {your-project-path}`
2. `$ git submodule add https://github.com/springjk/java-in-docker`
2. `$ cd java-in-docker`
3. `$ docker-compose up`

## 数据库信息
* **DB_HOST** : `mysql`
* **DB_PORT** : `3306`
* **DB_NAME** : `docker`
* **DB_USER_NAME** : `root`
* **DB_USER_PWD** : `docker`

此处的数据库连接地址 `DB_HOST` 值为 `mysql` 是因为名为 `java-maven-tomcat` 的容器 `link` 了名为 `mysql` 的容器，故 `java-maven-tomcat` 容器中会在自己的 `/etc/hosts` 中添加一条 `1xx.xx.xx.xx   mysql` 的 host，这会将 `http://mysql` 这个地址指向 `mysql` 容器的实际内网地址，等效于常见的 `http://localhost` 。

## 数据持久化
容器内的数据会随着容器的销毁而丢失，所以需要配置 `docker-compose.yml` 文件将以下目录同步到你的物理机目录进行持久化：

* `/root/.m2` Maven 的 jar 包缓存目录，挂载后将无需每次构建时重新下载。
* `/var/lib/mysql` MySQL 的文件存放目录，必须挂载，否则销毁容器后数据丢失。
* `/usr/local/tomcat/logs` Tomcat 的日志目录，挂载后查看日志无须进入容器内部。

Windows 下挂载目录写法示例：`d:/project/`，macOS 与 Linux 下如挂载目录后出现无法写入则是权限问题，ssh 登陆容器后对文件夹给与写入权限即可。

## 项目部署
数据库信息，暴露端口等信息都在 `docker-compose.yml` 中配置，一切调试完成后可在启动命令后添加 `-d` 参数，让其后台启动，此时如果想实时查看启动日志可以使用 `docker-compose logs -f` 进行查看。

``` bash
# 后台启动运行
$ docker-compose up -d

# 跟踪查看启动日志
$ docker-compose logs -f
```

## 版本信息
* **JAVA** ：`1.8`
* **MySQL** ：`5.7`
* **Maven** ：`3.3.9`
* **Tomcat** ：`8.5.3`

版本信息可在 `Dockerfile` 中进行修改，修改 `Dockerfile` 后需要将 `docker-compose` 中的构建方式改为构建本地镜像：

``` bash
# 免构建镜像
  #image: registry.cn-hangzhou.aliyuncs.com/springjk/java-maven-tomcat
# 构建本地镜像
  build: ./java-maven-tomcat
```

修改完成后启动时需要重新构建镜像：

``` bash
$ docker-compose up --build
```

## 目录结构

```
├── LICENSE
├── README.md
├── code # JAVA 演示代码
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
├── data # 容器数据挂载目录
│   ├── maven
│   └── mysql
├── docker-compose.yml # docker-compose 配置文件
├── java-maven-tomcat
│   ├── Dockerfile # JAVA-Tomcat-Maven 构建文件
│   └── build.sh
├── log # 容器日志挂载目录
│   └── tomcat
└── mysql
    └── Dockerfile # MySQL 构建文件
```

## 工作流程
`Dockerfile -->(build)–> Image -->(run)–> Container`

1. 构建 MySQL 镜像
2. 构建 JAVA-Tomcat-Maven 镜像
3. 挂载 MySQL 数据目录并启动 MySQL 容器
4. 挂载 JAVA 代码目录并启动 JAVA-Tomcat-Maven 容器
5. 执行 Maven 的代码构建脚本 `build.sh` 生成 WAR 包并放入 Tomcat 工作目录
6. 启动 Tomcat

更多请参考 [Docker Docs](https://docs.docker.com/)。

## License
MIT