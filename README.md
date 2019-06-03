# VODSys
VODSys

这是我本科毕业设计 [《基于移动互联网的流媒系统的设计与实现》](https://raw.githubusercontent.com/wolfbrother/VODSys/master/%E6%9C%AC%E7%A7%91%E6%AF%95%E4%B8%9A%E8%AE%BE%E8%AE%A1.pdf)相关的代码，主要包括：

1. 移动客户端程序（Android）。这一部分占主要代码，包括用户界面注册、登录、点播、流视频播放等功能。
2. 以及一个用户信息管理服务器（基于socket协议）。只是一个c文件(见目录下的[server.c](https://raw.githubusercontent.com/wolfbrother/VODSys/master/server.c))，运行在一个Ubuntu系统上，用于用户管理和验证。
3.流媒体服务器用的是Apple公司开源的流媒体服务器Darwin Streaming Server （见我的博客[ubuntu安装流媒体服务器Darwin Streaming Server](http://blog.csdn.net/u012176591/article/details/21625325)），这一项牵涉到的代码不多，故没有包含进来。
