# Java多线程下载器
学习一下多线程的使用, 并且实现一个简单的多线程下载器.

支持多线程下载, 可以自定义线程数量, 断点续传



# 使用方法
```
 java -jar ThreadDownload.jar download_url(only support http/https/ftp) threadNum(1-64)`
```
例:
1. 下载文件
```cmd
java -jar ThreadDownload.jar https://download-cdn.jetbrains.com/idea/ideaIU-2022.1.3.exe 16
```

![gif](/images/download.gif)

2. 断点续传

![gif](/images/c.gif)

下载完成后会在当前Jar包所造目录下生成一个download文件夹, 存放下载的文件.
